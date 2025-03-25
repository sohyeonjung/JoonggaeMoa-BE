package org.silsagusi.joonggaemoa.domain.survey.service.command;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QuestionCommand {
	private Long id;
	private Long surveyId;
	private String content;
	private String type;
	private Boolean isRequired;
	private List<String> options;
}
