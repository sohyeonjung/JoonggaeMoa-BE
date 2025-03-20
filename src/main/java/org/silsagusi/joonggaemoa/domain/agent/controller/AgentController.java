package org.silsagusi.joonggaemoa.domain.agent.controller;

import org.silsagusi.joonggaemoa.domain.agent.controller.dto.SignupRequestDto;
import org.silsagusi.joonggaemoa.domain.agent.service.AgentService;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AgentController {

	private final AgentService agentService;

	@PostMapping("/api/signup")
	public ResponseEntity<ApiResponse<Void>> signup(@RequestBody SignupRequestDto signupRequestDto) {
		agentService.signup(
			signupRequestDto.getUsername(),
			signupRequestDto.getPassword(),
			signupRequestDto.getName(),
			signupRequestDto.getPhone(),
			signupRequestDto.getEmail(),
			signupRequestDto.getOffice(),
			signupRequestDto.getRegion(),
			signupRequestDto.getBusinessNo());

		return ResponseEntity.ok(ApiResponse.ok());
	}
}
