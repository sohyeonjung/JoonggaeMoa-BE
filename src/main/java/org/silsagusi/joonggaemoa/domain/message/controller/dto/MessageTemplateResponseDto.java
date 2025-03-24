package org.silsagusi.joonggaemoa.domain.message.controller.dto;

import org.silsagusi.joonggaemoa.domain.message.service.command.MessageTemplateCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MessageTemplateResponseDto {

	private String category;
	private String content;

	public static MessageTemplateResponseDto of(MessageTemplateCommand messageTemplateCommand) {
		return MessageTemplateResponseDto.builder()
			.category(messageTemplateCommand.getCategory())
			.content(messageTemplateCommand.getContent())
			.build();
	}
}
