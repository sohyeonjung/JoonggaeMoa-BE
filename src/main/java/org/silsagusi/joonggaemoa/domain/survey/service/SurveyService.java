package org.silsagusi.joonggaemoa.domain.survey.service;

import java.util.ArrayList;
import java.util.List;

import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.domain.agent.service.AgentService;
import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.repository.ConsultationRepository;
import org.silsagusi.joonggaemoa.domain.customer.entity.Customer;
import org.silsagusi.joonggaemoa.domain.customer.repository.CustomerRepository;
import org.silsagusi.joonggaemoa.domain.customer.service.CustomerService;
import org.silsagusi.joonggaemoa.domain.survey.entity.Answer;
import org.silsagusi.joonggaemoa.domain.survey.entity.Question;
import org.silsagusi.joonggaemoa.domain.survey.entity.Survey;
import org.silsagusi.joonggaemoa.domain.survey.repository.AnswerRepository;
import org.silsagusi.joonggaemoa.domain.survey.repository.QuestionRepository;
import org.silsagusi.joonggaemoa.domain.survey.repository.SurveyRepository;
import org.silsagusi.joonggaemoa.domain.survey.service.command.AnswerCommand;
import org.silsagusi.joonggaemoa.domain.survey.service.command.QuestionCommand;
import org.silsagusi.joonggaemoa.domain.survey.service.command.SurveyCommand;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {

	private final SurveyRepository surveyRepository;
	private final AgentRepository agentRepository;
	private final QuestionRepository questionRepository;
	private final CustomerService customerService;
	private final AgentService agentService;
	private final CustomerRepository customerRepository;
	private final AnswerRepository answerRepository;
	private final ConsultationRepository consultationRepository;

	public void createSurvey(
		Long agentId,
		String title,
		String description,
		List<QuestionCommand> questionCommandList
	) {
		Agent agent = agentRepository.findById(agentId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

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

	@Transactional
	public void deleteSurvey(Long surveyId) {
		questionRepository.deleteBySurvey_Id(surveyId);
		surveyRepository.deleteById(surveyId);
	}

	public void updateSurvey(
		Long surveyId,
		String title,
		String description,
		List<QuestionCommand> questionCommandList
	) {
		Survey survey = surveyRepository.findById(surveyId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

		survey.updateSurveyTitleDescription(
			(title == null || title.isBlank()) ? survey.getTitle() : title,
			(description == null || description.isBlank()) ? survey.getDescription() : description
		);

		List<Question> questionList = survey.getQuestionList();

		//삭제 된 질문
		for (Question question : questionList) {
			boolean found = false;
			for (QuestionCommand command : questionCommandList) {
				if (question.getId().equals(command.getId())) {
					found = true;
					break;
				}
			}
			if (!found)
				question.deleteQuestion();
		}

		//바뀐 질문, 새로운 질문
		for (QuestionCommand command : questionCommandList) {
			//새로운 질문
			if (command.getId() < 0 || command.getId() == null) {
				Question question = new Question(
					survey,
					command.getContent(),
					command.getType(),
					command.getIsRequired(),
					command.getOptions()
				);
				survey.getQuestionList().add(question);
			} else {
				//바뀐 질문
				for (Question question : questionList) {
					if (question.getId().equals(command.getId())) {
						//새롭게 만들고
						Question question2 = new Question(
							survey,
							command.getContent(),
							command.getType(),
							command.getIsRequired(),
							command.getOptions()
						);
						survey.getQuestionList().add(question2);
						// 기존 질문의 isDeleted = true
						question.deleteQuestion();
						break;
					}
				}
			}

		}
		questionRepository.saveAll(questionList);
		surveyRepository.save(survey);
	}

	public List<SurveyCommand> getAllSurveys() {
		List<Survey> surveyList = surveyRepository.findAll();
		return surveyList.stream().map(it -> SurveyCommand.of(it)).toList();
	}

	public SurveyCommand findById(Long surveyId) {
		Survey survey = surveyRepository.findById(surveyId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
		;
		return SurveyCommand.of(survey);
	}

	public void submitSurveyAnswer(
		Long agentId,
		Long surveyId,
		String name,
		String email,
		String phone,
		Boolean consent,
		String answer
	) {
		Survey survey = surveyRepository.findById(surveyId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
		Agent agent = agentRepository.findById(agentId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

		// 고객인지 판별(휴대폰 번호) 후 고객 데이터 추가
		Customer customer = customerService.getCustomerByPhone(phone);
		if (customer == null) {
			Customer newCustomer = new Customer(
				name,
				phone,
				email,
				consent,
				agent
			);
			customerRepository.save(newCustomer);
			customer = newCustomer;
		}

		// 상담 추가
		Consultation consultation = new Consultation(
			customer,
			Consultation.ConsultationStatus.WAITING
		);
		consultationRepository.save(consultation);

		// 응답 추가
		Answer newanswer = new Answer(
			customer,
			survey,
			answer
		);
		answerRepository.save(newanswer);

	}

	public List<AnswerCommand> getAllAnswers() {
		List<Answer> answerList = answerRepository.findAll();
		return answerList.stream().map(it -> AnswerCommand.of(it)).toList();

	}
}
