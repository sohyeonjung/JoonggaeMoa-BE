package org.silsagusi.joonggaemoa.domain.survey.service.command;

import java.time.LocalDate;
import java.util.List;

import lombok.Builder;

@Builder
public class SurveyCommand {
	private String name;
	private LocalDate birthday;
	private String phone;
	private String email;
	private String job;
	private Boolean isVip;
	private String memo;
	private Boolean consent;
	private List<QuestionCommand> questionList;

}
