package org.silsagusi.joonggaemoa.domain.consultation.contorller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ConsultationResponseDto {

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
