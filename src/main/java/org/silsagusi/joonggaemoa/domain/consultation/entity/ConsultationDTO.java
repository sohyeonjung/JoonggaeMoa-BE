package org.silsagusi.joonggaemoa.domain.consultation.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationDTO {

    private Long consultationId;

    private Long customerId;

    @JsonFormat(pattern = "yyyyMMdd HH:mm")  // JSON 날짜 포맷 지정
    private LocalDateTime date;

    private String purpose;

    private Integer interestProperty;

    private String interestLocation;

    private String contractType;

    private String assetStatus;

    private String memo;

    private String consultationStatus;
}
