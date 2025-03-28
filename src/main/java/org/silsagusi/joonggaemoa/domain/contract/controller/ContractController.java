package org.silsagusi.joonggaemoa.domain.contract.controller;

import java.util.List;

import org.silsagusi.joonggaemoa.domain.contract.controller.dto.ContractResponse;
import org.silsagusi.joonggaemoa.domain.contract.controller.dto.CreateContractRequest;
import org.silsagusi.joonggaemoa.domain.contract.controller.dto.UpdateContractRequest;
import org.silsagusi.joonggaemoa.domain.contract.service.ContractService;
import org.silsagusi.joonggaemoa.domain.contract.service.command.ContractCommand;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import retrofit2.http.Path;

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

	@GetMapping("/api/agents/{agentId}/contracts")
	public ResponseEntity<ApiResponse<List<ContractResponse>>> getAllContracts()
	{
		List<ContractCommand> contractCommandList = contractService.getAllContracts();
		List<ContractResponse> contractResponseList = contractCommandList.stream()
			.map(it->ContractResponse.of(it)).toList();
		return ResponseEntity.ok(ApiResponse.ok(contractResponseList));
	}

	@GetMapping("/api/agents/{agentId}/contracts/{contractId}")
	public ResponseEntity<ApiResponse<ContractResponse>> getContract(
		@PathVariable("contractId") Long contractId
	){
		ContractCommand contractCommand = contractService.getContractById(contractId);
		return ResponseEntity.ok(ApiResponse.ok(ContractResponse.of(contractCommand)));
	}

	@PatchMapping("/api/agents/{agentId}/contracts/{contractId}")
	public ResponseEntity<ApiResponse<Void>> updateContract(
		@PathVariable("contractId") Long contractId,
		@RequestBody UpdateContractRequest updateContractRequest
	){
		contractService.updateContract(
			contractId,
			updateContractRequest.getCreatedAt(),
			updateContractRequest.getExpiredAt(),
			updateContractRequest.getUrl()
		);
		return ResponseEntity.ok(ApiResponse.ok());
	}

	@DeleteMapping("/api/agents/{agentId}/contracts/{contractId}")
	public ResponseEntity<ApiResponse<Void>> deleteContract(
		@PathVariable("contractId") Long contractId
	){
		contractService.deleteContract(contractId);
		return ResponseEntity.ok(ApiResponse.ok());
	}


}
