package org.silsagusi.joonggaemoa.domain.message.service;

import org.silsagusi.joonggaemoa.domain.message.repository.MessageLogRepository;
import org.silsagusi.joonggaemoa.domain.message.repository.MessageRepository;
import org.silsagusi.joonggaemoa.domain.message.service.command.MessageCommand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
	private final MessageLogRepository messageLogRepository;

	public Page<MessageCommand> getMessage(Long agentId, Long lastMessageId) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("agentId").descending());

		return messageRepository.findByCustomerAgentIdAndIdLessThan(agentId, lastMessageId, pageable)
			.map(MessageCommand::of);
	}
}
