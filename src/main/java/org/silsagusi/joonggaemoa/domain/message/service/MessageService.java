package org.silsagusi.joonggaemoa.domain.message.service;

import java.time.LocalDateTime;
import java.util.List;

import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.domain.customer.repository.CustomerRepository;
import org.silsagusi.joonggaemoa.domain.message.entity.ReservedMessage;
import org.silsagusi.joonggaemoa.domain.message.repository.MessageRepository;
import org.silsagusi.joonggaemoa.domain.message.repository.ReservedMessageRepository;
import org.silsagusi.joonggaemoa.domain.message.service.command.MessageCommand;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final AgentRepository agentRepository;
	private final CustomerRepository customerRepository;
	private final MessageRepository messageRepository;
	private final ReservedMessageRepository reservedMessageRepository;

	public Page<MessageCommand> getMessage(Long agentId, Long lastMessageId) {
		Pageable pageable = PageRequest.of(0, 10, Sort.by("agentId").descending());

		return null;
	}

	public void reserveMessage(String content, String sendAt, List<Long> customerIdList) {
		customerIdList.stream()
			.map(id -> customerRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT)))
			.map(customer -> new ReservedMessage(customer, LocalDateTime.parse(sendAt), content))
			.forEach(reservedMessageRepository::save);
	}
}
