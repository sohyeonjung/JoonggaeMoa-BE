package org.silsagusi.joonggaemoa.domain.consultation.service;

import java.time.LocalDateTime;

import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.repository.ConsultationRepository;
import org.silsagusi.joonggaemoa.domain.customer.entity.Customer;
import org.silsagusi.joonggaemoa.domain.customer.repository.CustomerRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ConsultationService {

	private final ConsultationRepository consultationRepository;
	private final CustomerRepository customerRepository;

	public void createConsultation(
		Long customerId,
		LocalDateTime date,
		String purpose,
		String interestProperty,
		String interestLocation,
		String contractType,
		String assetStatus,
		String memo,
		Consultation.ConsultationStatus consultationStatus
	) {
		Customer customer = customerRepository.getById(customerId);
		Consultation consultation = new Consultation(
			customer,
			date,
			purpose,
			interestProperty,
			interestLocation,
			contractType,
			assetStatus,
			memo,
			consultationStatus
		);
		consultationRepository.save(consultation);

	}

	public void updateConsultationStatus(Long consultationId, Consultation.ConsultationStatus consultationStatus) {
		Consultation consultation = consultationRepository.getById(consultationId);
		consultation.updateStatus(consultationStatus);
		consultationRepository.save(consultation);
	}

	public void updateConsultation(
		Long consultationId,
		LocalDateTime date,
		String purpose,
		String interestProperty,
		String interestLocation,
		String contractType,
		String assetStatus,
		String memo,
		Consultation.ConsultationStatus consultationStatus
	) {
		Consultation consultation = consultationRepository.getById(consultationId);
		consultation.updateConsultation(
			(date == null) ? consultation.getDate() : date,
			(purpose == null || purpose.isBlank()) ? consultation.getPurpose() : purpose,
			(interestProperty == null || interestProperty.isBlank()) ? consultation.getInterestProperty() :
				interestProperty,
			(interestLocation == null || interestLocation.isBlank()) ? consultation.getInterestLocation() :
				interestLocation,
			(contractType == null || contractType.isBlank()) ? consultation.getContractType() : contractType,
			(assetStatus == null || assetStatus.isBlank()) ? consultation.getAssetStatus() : assetStatus,
			(memo == null || memo.isBlank()) ? consultation.getMemo() : memo,
			(consultationStatus == null) ? consultation.getConsultationStatus() : consultationStatus

		);
		consultationRepository.save(consultation);
	}
}
