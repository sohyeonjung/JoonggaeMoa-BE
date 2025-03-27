package org.silsagusi.joonggaemoa.domain.survey.controller.dto;

import lombok.Getter;

@Getter
public class AnswerRequest {
	private String name;
	private String email;
	private String phone;
	private Boolean consent;
	private String answer;
}
