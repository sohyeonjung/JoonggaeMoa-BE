package org.silsagusi.joonggaemoa.domain.message.controller;

import org.silsagusi.joonggaemoa.domain.message.controller.dto.MessageRequest;
import org.silsagusi.joonggaemoa.domain.message.controller.dto.MessageResponse;
import org.silsagusi.joonggaemoa.domain.message.service.MessageService;
import org.silsagusi.joonggaemoa.domain.message.service.command.MessageCommand;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;

	@GetMapping("/api/agents/{agentId}/messages")
	public ResponseEntity<ApiResponse<Page<MessageResponse>>> getMessage(
		@PathVariable Long agentId,
		@RequestParam Long lastMessageId
	) {
		Page<MessageCommand> messageCommandPage = messageService.getMessage(agentId, lastMessageId);

		Page<MessageResponse> messageResponsePage = messageCommandPage.map(MessageResponse::of);

		return ResponseEntity.ok(ApiResponse.ok(messageResponsePage));
	}

	@PostMapping("/api/agents/{agentId}/messages")
	public ResponseEntity<ApiResponse<Void>> reserveMessage(
		@PathVariable Long agentId,
		@RequestBody MessageRequest request
	) {
		messageService.reserveMessage(request.getContent(), request.getSendAt(), request.getCustomerIdList());

		return ResponseEntity.ok(ApiResponse.ok());
	}
}
