package org.silsagusi.joonggaemoa.domain.consultation.contorller;

import org.silsagusi.joonggaemoa.domain.consultation.service.ConsultationService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/agents/{agentId}/consultations")
@RequiredArgsConstructor
public class ConsultationApiController {

	private final ConsultationService consultationService;

	/*
	//상담 예약
	@PostMapping("")
	public ResponseEntity<ApiResponse<ConsultationResponseDto>> createConsultation(
		@PathVariable Long agentId,
		@RequestBody ConsultationRequestDto consultationRequestDto
	) {
		ConsultationResponseDto responseDto = consultationService.createConsultation(agentId,
			consultationRequestDto);
		return ResponseEntity.ok(ApiResponse.created(responseDto));
	}*/

	//상담 리스트 조회
	//	@GetMapping("")

	//상담 상태별 건수 조회
	//	@GetMapping("")

	//상담 상세 조회
	//	@GetMapping("")

	//상담 상세 작성
	//	@PatchMapping("{consultationId}")

	//상담 상세 수정
	//	@PatchMapping("{consultationId}")

	//상담 삭제
	//	@DeleteMapping("{consultationId}")

	//상담 상태 변경
	//	@PatchMapping("{consultationId}")
}
