# Spring Batch 커넥션 점유 최적화

> 기존 문제 기록: [Connection 부족 문제 해결기](https://github.com/Kernel360/KDEV4-JoonggaeMoa-BE/wiki/%F0%9F%A7%AF-Connection-%EB%B6%80%EC%A1%B1-%EB%AC%B8%EC%A0%9C-%ED%95%B4%EA%B2%B0%EA%B8%B0)
>
> 이 문서는 **2026년 신규 리팩터링**의 변경사항을 기술한다. 과거 Wiki의 '커넥션 오류 90% 이상 감소'는 **과거 개선 결과**이며 이번 변경의 성능 측정치가 아니다.

## 1. 배경과 문제 정의

공인중개사 CRM은 네이버부동산·직방으로부터 지역별 매물 및 단지 정보를 수집한다. 이전 개선에서 API·Batch를 별도 서버로 분리하고 OSIV를 비활성화했지만, Batch 코드에는 다음과 같은 추가 최적화 여지가 있었다.

- `chunk(1)`의 Item은 **매물 1건이 아니라 ScrapeStatus(지역·플랫폼·유형) 1건**이었다.
- Processor가 한 지역의 모든 외부 페이지를 반복 호출했고, 각 페이지 사이 3~7초 대기 및 예외 발생 시 10분 대기가 존재했다.
- 기존 Chunk는 DB 트랜잭션 경계 내에서 Processor를 실행했다.
- `ArticleValidator`, `ComplexValidator`의 단건 존재 여부 조회로 매물/단지마다 SELECT가 발생할 수 있었다.
- 기존 `REQUIRES_NEW`는 페이지별 저장 트랜잭션을 분리하려는 의도였지만 외부 Chunk가 DB 커넥션을 보유한 상태라면 추가 커넥션을 요구할 수 있다.

## 2. 핵심 설계

```text
Spring Batch Step (PROPAGATION_NOT_SUPPORTED)
  ├─ Reader: 다음 PENDING ScrapeStatus와 Region 조회
  ├─ Processor: 외부 API 페이지 호출  [DB 저장 트랜잭션 없음]
  │    ├─ 응답 변환 / 페이지 내 중복 제거
  │    ├─ ScrapePagePersistenceService [짧은 dataTransactionManager 트랜잭션]
  │    │    ├─ 기존 코드 일괄 조회 (IN 쿼리)
  │    │    ├─ 신규 매물 또는 단지 저장
  │    │    └─ 동일 트랜잭션 내 lastScrapedPage 체크포인트 기록
  │    └─ API 호출 간 대기 (DB 저장 트랜잭션 없음)
  └─ Writer: 완료/실패 상태 저장
```

**트랜잭션 분리 기준:** 외부 HTTP 호출 및 sleep은 업무 DB 트랜잭션 밖에서 실행하고, 페이지 저장과 체크포인트 갱신만 동일 DB 트랜잭션으로 처리한다.

**페이지 커서 의미:**
- 네이버: `lastScrapedPage` = 마지막으로 **성공적으로 저장한 페이지 번호**. 다음 실행은 `+1`부터.
- 직방 매물: `lastScrapedPage` = **다음에 요청할 offset**. 다음 실행은 그 offset부터.
- 직방 단지: `lastScrapedPage` = **다음에 처리할 geohash index**.

실패하면 마지막 **커밋된** 커서를 유지해 실패 페이지를 다시 요청할 수 있다. 완료/실패 상태는 기존 ItemWriter가 담당한다.

## 3. 변경사항 및 목적

| 변경 | 적용 위치 | 목적 |
| --- | --- | --- |
| Chunk `PROPAGATION_NOT_SUPPORTED` | 네이버/직방 매물·단지 Step | 장시간 외부 호출 중 외부 Chunk DB 트랜잭션 방지 |
| 페이지별 짧은 저장 트랜잭션 | `ScrapePagePersistenceService` | 데이터 저장+진행 위치를 원자적으로 확정 |
| 중복 코드 `IN` 일괄 조회 | Article/Complex Repository | 개별 존재 여부 쿼리 감소 |
| `@EntityGraph(region)` | `ScrapeStatusRepository` | 트랜잭션 밖 Processor에서 지역 Lazy Loading 문제 방지 |
| 플랫폼별 커서 보존 | NaverLandScraper, ZigbangScraper | 실패 후 다시 시작할 페이지를 건너뛰지 않기 |
| 직방 종료조건 변경 | ZigbangScraper | 마지막 offset 처리 후 추가 요청 방지 |

## 4. 검증 방법

단위 테스트: `./gradlew :batch:test --tests '*ScrapePagePersistenceServiceTest'`

**주의:** 이번 변경은 GitHub에 커밋했지만 이 작업 환경에서 Gradle 및 MySQL 연동 테스트를 실제 실행하지 못했다. 테스트 성공이나 성능 향상을 주장하지 않는다.

추가로 다음 항목을 실제 DB + Mock HTTP 서버 기반 통합 테스트에서 검증해야 한다.

1. 저장 실패 시 매물 INSERT와 체크포인트 모두 롤백되는지.
2. API 재시도 및 프로세스 재시작 시 마지막 성공 페이지부터 시작하는지.
3. 네이버/직방 각각 페이지 종료조건이 실제 응답의 `more`, `count` 의미와 일치하는지.
4. 두 Job 동시 실행 시 HikariCP `active`, `pending`, 획득 대기시간, DB `Threads_connected` 변화.
5. 외부 API 호출 및 `sleep()` 중 업무 DB 커넥션이 점유되지 않는지.
6. 데이터에 이미 중복 `articleCode`/`complexCode`가 존재하거나 다른 프로세스가 같은 코드를 동시에 삽입할 때의 동작.
7. 주소 보강 Step(`JpaCursorItemReader` + 외부 카카오 API 호출)은 **이번 변경 범위 밖**이므로 별도 트랜잭션/커넥션 점유 분석.

### 실행 시 주의사항

- 이 변경은 현재 DB의 유니크 제약을 추가하지 않았다. 일괄 중복 조회만으로 동시 수집 과정의 완전한 멱등성이 보장되지는 않는다. 운영 전 플랫폼·매물 코드 키를 포함한 유니크 제약 및 충돌 처리를 검토해야 한다.
- 직방의 `count` 필드는 총 결과 수로 가정하여 종료조건을 변경했다. 실제 외부 API의 응답 계약 검증이 필요하다.
- 기본 OSIV 비활성화, 기존 Job 순서 및 3~7초 호출 간 대기 정책은 유지했다.
- 새 브랜치를 통한 변경이며 운영 DB나 기존 공유 팀 저장소에는 배포하지 않았다.

## 5. 기대효과와 남은 한계

**기대효과(미측정):** 외부 API 응답 대기 중 업무 DB 커넥션 점유 감소, 지역별 중복 확인 쿼리 수 감소, 페이지 저장 실패 시 체크포인트 정합성 향상.

**남은 과제:** 실제 JDBC batch insert/upsert는 별도로 튜닝해야 한다. API·Batch는 동일 MySQL의 CPU/IO/락/최대 커넥션 한도를 여전히 공유한다. 배치 커넥션 풀 크기·동시 실행 수와 주소 보강 Step의 외부 호출 구조도 추가 개선 후보이다.

## 6. 변경 전후 확인 쿼리/지표

- HikariCP: `hikaricp.connections.active`, `hikaricp.connections.pending`, `hikaricp.connections.acquire`
- API: p95/p99 응답시간, JDBC 커넥션 획득 실패 건수
- DB: `Threads_connected`, CPU/IO, lock wait, slow query
- 수집: 페이지당 저장 소요시간, 재시도 횟수, 마지막 체크포인트, 전체 작업 완료시간

성능 수치를 PR에 기입할 때는 동일 조건의 리팩터링 전/후 부하 테스트 결과를 사용해야 한다.
