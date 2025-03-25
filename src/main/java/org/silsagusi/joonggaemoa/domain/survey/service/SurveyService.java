package org.silsagusi.joonggaemoa.domain.survey.service;

import java.util.ArrayList;
import java.util.List;

import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.domain.survey.entity.Question;
import org.silsagusi.joonggaemoa.domain.survey.entity.Survey;
import org.silsagusi.joonggaemoa.domain.survey.repository.QuestionRepository;
import org.silsagusi.joonggaemoa.domain.survey.repository.SurveyRepository;
import org.silsagusi.joonggaemoa.domain.survey.service.command.QuestionCommand;
import org.silsagusi.joonggaemoa.domain.survey.service.command.SurveyCommand;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {

	private final SurveyRepository surveyRepository;
	private final AgentRepository agentRepository;
	private final QuestionRepository questionRepository;

	public void createSurvey(
		Long agentId,
		String title,
		String description,
		List<QuestionCommand> questionCommandList
	) {
		Agent agent = agentRepository.getOne(agentId);

		Survey survey = new Survey(agent, title, description, new ArrayList<>());

		List<Question> questionList = questionCommandList.stream()
			.map(
				it -> {
					Question question = new Question(
						survey,
						it.getContent(),
						it.getType(),
						it.getIsRequired(),
						it.getOptions()
					);
					survey.getQuestionList().add(question);
					return question;
				}
			).toList();

		surveyRepository.save(survey);
		questionRepository.saveAll(questionList);

	}

	public List<SurveyCommand> getAllSurveys() {
		List<Survey> surveyList = surveyRepository.findAll();
		return surveyList.stream().map(it -> SurveyCommand.of(it)).toList();
	}

	public SurveyCommand findById(Long surveyId) {
		Survey survey = surveyRepository.getOne(surveyId);
		return SurveyCommand.of(survey);
	}
}
