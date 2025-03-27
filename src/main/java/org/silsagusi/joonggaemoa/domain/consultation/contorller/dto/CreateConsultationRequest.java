package org.silsagusi.joonggaemoa.domain.consultation.contorller.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateConsultationRequest {

	private Long customerId;

	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")  // JSON 날짜 포맷 지정
	private LocalDateTime date;

	private String purpose;

	private String interestProperty;

	private String interestLocation;

	private String contractType;

	private String assetStatus;

	private String memo;

	private String consultationStatus;
}
