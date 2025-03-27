package org.silsagusi.joonggaemoa.domain.survey.service.command;

import org.silsagusi.joonggaemoa.domain.customer.service.command.CustomerCommand;
import org.silsagusi.joonggaemoa.domain.survey.entity.Answer;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AnswerCommand {
	private CustomerCommand customer;
	private SurveyCommand survey;
	private String answer;

	public static AnswerCommand of(Answer answer) {
		return AnswerCommand.builder()
			.customer(CustomerCommand.of(answer.getCustomer()))
			.survey(SurveyCommand.of(answer.getSurvey()))
			.answer(answer.getAnswer())
			.build();
	}
}
