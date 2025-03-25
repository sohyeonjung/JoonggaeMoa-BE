package org.silsagusi.joonggaemoa.domain.survey.controller;

import java.util.List;

import org.silsagusi.joonggaemoa.domain.survey.controller.dto.SurveyRequest;
import org.silsagusi.joonggaemoa.domain.survey.controller.dto.SurveyResponse;
import org.silsagusi.joonggaemoa.domain.survey.repository.SurveyRepository;
import org.silsagusi.joonggaemoa.domain.survey.service.SurveyService;
import org.silsagusi.joonggaemoa.domain.survey.service.command.QuestionCommand;
import org.silsagusi.joonggaemoa.domain.survey.service.command.SurveyCommand;
import org.silsagusi.joonggaemoa.global.api.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SurveyController {

	private final SurveyService surveyService;
	private final SurveyRepository surveyRepository;

	@PostMapping("/api/agents/{agentId}/surveys")
	public ResponseEntity<ApiResponse> createSurvey(
		@PathVariable("agentId") Long agentId,
		@RequestBody SurveyRequest surveyRequest
	) {
		List<QuestionCommand> questionCommandList = surveyRequest.getQuestionList()
			.stream().map(it -> QuestionCommand.builder()
				.surveyId(it.getSurveyId())
				.content(it.getContent())
				.type(it.getType())
				.isRequired(it.getIsRequired())
				.options(it.getOptions())
				.build()
			).toList();

		surveyService.createSurvey(
			agentId,
			surveyRequest.getTitle(),
			surveyRequest.getDescription(),
			questionCommandList
		);
		return ResponseEntity.ok(ApiResponse.ok());
	}

	@GetMapping("/api/agents/{agentId}/surveys")
	public ResponseEntity<ApiResponse<List<SurveyResponse>>> getAllSurveys() {
		List<SurveyCommand> surveyCommandList = surveyService.getAllSurveys();
		List<SurveyResponse> surveyResponseList = surveyCommandList.stream()
			.map(it -> SurveyResponse.of(it)).toList();
		return ResponseEntity.ok(ApiResponse.ok(surveyResponseList));
	}

	@GetMapping("/api/agents/{agentId}/surveys/{surveyId}")
	public ResponseEntity<ApiResponse<SurveyResponse>> getSurvey(
		@PathVariable("surveyId") Long surveyId
	) {
		SurveyCommand surveyCommand = surveyService.findById(surveyId);
		return ResponseEntity.ok(ApiResponse.ok(SurveyResponse.of(surveyCommand)));
	}

}
