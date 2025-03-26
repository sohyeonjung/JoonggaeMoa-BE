package org.silsagusi.joonggaemoa.domain.consultation.contorller;

import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.CreateConsultationRequest;
import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.UpdateConsultationRequest;
import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.service.ConsultationService;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ConsultationApiController {

	private final ConsultationService consultationService;

	@PostMapping("/api/agents/{agentId}/consultations")
	public ResponseEntity<ApiResponse<Void>> createConsultation(
		@PathVariable("agentId") String agentId,
		@RequestBody CreateConsultationRequest createConsultationRequest
	) {
		consultationService.createConsultation(
			createConsultationRequest.getCustomerId(),
			createConsultationRequest.getDate(),
			createConsultationRequest.getPurpose(),
			createConsultationRequest.getInterestProperty(),
			createConsultationRequest.getInterestLocation(),
			createConsultationRequest.getContractType(),
			createConsultationRequest.getAssetStatus(),
			createConsultationRequest.getMemo(),
			createConsultationRequest.getConsultationStatus()
		);
		return ResponseEntity.ok(ApiResponse.ok());

	}

	//상담 리스트 조회
	//	@GetMapping("")

	//상담 상태별 건수 조회
	//	@GetMapping("")

	//상담 상세 조회
	//	@GetMapping("")

	@PatchMapping("/api/agents/{agentId}/consultations/{consultationId}")
	public ResponseEntity<ApiResponse<Void>> updateConsultation(
		@PathVariable("consultationId") Long consultationId,
		@RequestBody UpdateConsultationRequest updateConsultationRequest
	) {
		consultationService.updateConsultation(
			consultationId,
			updateConsultationRequest.getDate(),
			updateConsultationRequest.getPurpose(),
			updateConsultationRequest.getInterestProperty(),
			updateConsultationRequest.getInterestLocation(),
			updateConsultationRequest.getContractType(),
			updateConsultationRequest.getAssetStatus(),
			updateConsultationRequest.getMemo(),
			updateConsultationRequest.getConsultationStatus()
		);
		return ResponseEntity.ok(ApiResponse.ok());
	}

	@PatchMapping("/api/agents/{agentId}/consultations/{consultationId}/status")
	public ResponseEntity<ApiResponse<Void>> updateConsultationStatus(
		@PathVariable("consultationId") Long consultationId,
		@RequestParam Consultation.ConsultationStatus consultationStatus
	) {
		consultationService.updateConsultationStatus(consultationId, consultationStatus);
		return ResponseEntity.ok(ApiResponse.ok());
	}

}
