package org.silsagusi.joonggaemoa.domain.consultation.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.silsagusi.joonggaemoa.domain.agent.entity.AgentCustomer;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentCustomerRepository;
import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.entity.ConsultationDTO;
import org.silsagusi.joonggaemoa.domain.consultation.entity.ConsultationStatus;
import org.silsagusi.joonggaemoa.domain.consultation.repository.ConsultationRepository;
import org.silsagusi.joonggaemoa.domain.customer.entity.Customer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ConsultationService {

    private final ConsultationRepository consultationRepository;
    private final AgentCustomerRepository agentCustomerRepository;
    @Transactional
    public ConsultationDTO createConsultation(Long agentId, ConsultationDTO dto){
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
        return ConsultationDTO.builder()
                .consultationId(savedConsultation.getId())
                .customerId(dto.getCustomerId())
                .date(savedConsultation.getDate())
                .consultationStatus(savedConsultation.getConsultationStatus().name())
                .build();
    }
}
