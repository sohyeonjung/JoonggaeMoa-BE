package org.silsagusi.joonggaemoa.domain.message.service.command;

import java.time.LocalDate;

import org.silsagusi.joonggaemoa.domain.message.entity.Message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageCommand {

	private Long id;
	private Long customerId;
	private String customerName;
	private String content;
	private LocalDate sendAt;
	private String sendStatus;

	public static MessageCommand of(Message message) {
		return MessageCommand.builder()
			.id(message.getId())
			.customerId(message.getCustomer().getId())
			.customerName(message.getCustomer().getName())
			.content(message.getContent())
			.sendAt(message.getSendAt())
			.sendStatus(String.valueOf(message.getSendStatus()))
			.build();
	}
}
