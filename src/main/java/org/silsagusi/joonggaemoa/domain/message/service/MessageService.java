package org.silsagusi.joonggaemoa.domain.message.service;

import org.silsagusi.joonggaemoa.domain.message.repository.MessageRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageService {

	private final MessageRepository messageRepository;
}
