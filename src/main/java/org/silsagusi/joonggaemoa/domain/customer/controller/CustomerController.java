package org.silsagusi.joonggaemoa.domain.customer.controller;

import org.silsagusi.joonggaemoa.domain.customer.controller.dto.CreateCustomerRequest;
import org.silsagusi.joonggaemoa.domain.customer.service.CustomerService;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@RestController("/api/agents")
@RequiredArgsConstructor
public class CustomerController {

	private final CustomerService customerService;

	@PostMapping("/{agentId}/customers")
	public ResponseEntity<ApiResponse> createCustomer(
		@PathVariable("agentId") Long agentId,
		@RequestBody CreateCustomerRequest createCustomerRequestDto
	) {
		customerService.createCustomer(
			agentId,
			createCustomerRequestDto.getName(),
			createCustomerRequestDto.getBirthday(),
			createCustomerRequestDto.getPhone(),
			createCustomerRequestDto.getEmail(),
			createCustomerRequestDto.getJob(),
			createCustomerRequestDto.getIsVip(),
			createCustomerRequestDto.getMemo(),
			createCustomerRequestDto.getConsent()
		);

		return ResponseEntity.ok(ApiResponse.ok());
	}

	@PostMapping(value = "/{agentId}/customers/bulk", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ApiResponse> bulkCreateCustomer(
		@PathVariable("agentId") Long agentId,
		@RequestParam("file") MultipartFile file
	) {
		customerService.bulkCreateCustomer(agentId, file);
		return ResponseEntity.ok(ApiResponse.ok());
	}

}
