package org.silsagusi.joonggaemoa.domain.message.controller;

import org.silsagusi.joonggaemoa.domain.message.service.MessageService;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MessageController {

	private final MessageService messageService;
}
