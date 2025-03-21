package org.silsagusi.joonggaemoa.domain.consultation.service;

import org.silsagusi.joonggaemoa.domain.agent.entity.AgentCustomer;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentCustomerRepository;
import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.ConsultationRequestDto;
import org.silsagusi.joonggaemoa.domain.consultation.contorller.dto.ConsultationResponseDto;
import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.entity.ConsultationStatus;
import org.silsagusi.joonggaemoa.domain.consultation.repository.ConsultationRepository;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationService {

	private final ConsultationRepository consultationRepository;
	private final AgentCustomerRepository agentCustomerRepository;

	@Transactional
	public ConsultationResponseDto createConsultation(Long agentId, ConsultationRequestDto dto) {
		// 중개인 고객 정보 조회
		AgentCustomer agentCustomer = agentCustomerRepository.findByAgentIdAndCustomerId(agentId, dto.getCustomerId())
			.orElseThrow(() -> new IllegalArgumentException("해당 중개인-고객 정보가 없습니다."));

		// 엔티티 변환 및 저장
		Consultation consultation = Consultation.builder()
			.agentCustomer(agentCustomer)
			.date(dto.getDate())
			.consultationStatus(ConsultationStatus.WAITING)
			.build();

		Consultation savedConsultation = consultationRepository.save(consultation);

		// DTO 변환 후 반환
		return ConsultationResponseDto.builder()
			.consultationId(savedConsultation.getId())
			.customerId(savedConsultation.getAgentCustomer().getCustomer().getId())
			.date(savedConsultation.getDate())
			.consultationStatus(savedConsultation.getConsultationStatus().name())
			.build();
	}
}
