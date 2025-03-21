package org.silsagusi.joonggaemoa.domain.consultation.contorller;

import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.ConsultationRequestDto;
import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.ConsultationResponseDto;
import org.silsagusi.joonggaemoa.domain.consultation.service.ConsultationService;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConsultationApiController {

	private final ConsultationService consultationService;

	//상담 예약
	@PostMapping("/api/agents/{agentId}/consultations")
	public ResponseEntity<ApiResponse<ConsultationResponseDto>> createConsultation(
		@PathVariable Long agentId,
		@RequestBody ConsultationRequestDto consultationRequestDto
	) {
		ConsultationResponseDto responseDto = consultationService.createConsultation(agentId,
			consultationRequestDto);
		return ResponseEntity.ok(ApiResponse.created(responseDto));
	}
}
