package org.silsagusi.joonggaemoa.domain.message.controller.dto;

import java.time.LocalDate;

import org.silsagusi.joonggaemoa.domain.message.service.command.MessageCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageResponse {

	private Long id;
	private Long customerId;
	private String customerName;
	private String content;
	private LocalDate sendAt;
	private String sendStatus;

	public static MessageResponse of(MessageCommand messageCommand) {
		return MessageResponse.builder()
			.id(messageCommand.getId())
			.customerId(messageCommand.getCustomerId())
			.customerName(messageCommand.getCustomerName())
			.content(messageCommand.getContent())
			.sendAt(messageCommand.getSendAt())
			.sendStatus(messageCommand.getSendStatus())
			.build();
	}
}
