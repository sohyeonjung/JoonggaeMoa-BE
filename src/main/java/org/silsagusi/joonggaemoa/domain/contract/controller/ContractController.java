package org.silsagusi.joonggaemoa.domain.contract.controller;

import org.silsagusi.joonggaemoa.domain.contract.controller.dto.CreateContractRequest;
import org.silsagusi.joonggaemoa.domain.contract.service.ContractService;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class ContractController {

	private final ContractService contractService;

	@PostMapping("/api/agents/{agentId}/contracts")
	public ResponseEntity<ApiResponse<Void>> createContract(
		@RequestBody CreateContractRequest createContractRequest
	){
			contractService.createContract(
				createContractRequest.getLandlordId(),
				createContractRequest.getTenantId(),
				createContractRequest.getCreatedAt(),
				createContractRequest.getExpiredAt(),
				createContractRequest.getUrl()
			);
			return ResponseEntity.ok(ApiResponse.ok());
	}





}
