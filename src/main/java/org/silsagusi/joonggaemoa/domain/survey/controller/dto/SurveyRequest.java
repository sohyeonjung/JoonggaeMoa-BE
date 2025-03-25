package org.silsagusi.joonggaemoa.domain.survey.controller.dto;

import java.util.List;

import lombok.Data;

@Data
public class SurveyRequest {
	private Long id;
	private String title;
	private String description;
	private List<QuestionRequest> questionList;
}
