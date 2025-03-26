package org.silsagusi.joonggaemoa.domain.consultation.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.silsagusi.joonggaemoa.domain.consultation.entity.Consultation;
import org.silsagusi.joonggaemoa.domain.consultation.repository.ConsultationRepository;
import org.silsagusi.joonggaemoa.domain.consultation.service.command.ConsultationCommand;
import org.silsagusi.joonggaemoa.domain.consultation.service.command.ConsultationStatusCommand;
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
		Customer customer = customerRepository.findById(customerId).orElseThrow();
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
		Consultation consultation = consultationRepository.findById(consultationId).orElseThrow();
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
		Consultation consultation = consultationRepository.findById(consultationId).orElseThrow();
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

	public List<ConsultationCommand> getAllConsultations() {
		List<Consultation> consultationList = consultationRepository.findAll();
		return consultationList.stream().map(it -> ConsultationCommand.of(it)).toList();
	}

	public List<ConsultationCommand> getConsultationsByStatus(String consultationStatus) {
		List<Consultation> consultationList = consultationRepository.findAllByConsultationStatus(
			Consultation.ConsultationStatus.valueOf(consultationStatus));
		return consultationList.stream().map(it -> ConsultationCommand.of(it)).toList();
	}

	public ConsultationCommand getConsultation(Long consultationId) {
		Consultation consultation = consultationRepository.findById(consultationId).orElseThrow();
		return ConsultationCommand.of(consultation);
	}

	public ConsultationStatusCommand getStatusInformation() {
		List<Consultation> consultations = consultationRepository.findAll();

		Map<Consultation.ConsultationStatus, Long> statusCountMap = consultations.stream()
			.collect(Collectors.groupingBy(Consultation::getConsultationStatus, Collectors.counting()));

		return ConsultationStatusCommand.builder()
			.consultationAll((long)consultations.size())
			.consultationWaiting(statusCountMap.getOrDefault(Consultation.ConsultationStatus.WAITING, 0L))
			.consultationConfirmed(statusCountMap.getOrDefault(Consultation.ConsultationStatus.CONFIRMED, 0L))
			.consultationCancelled(statusCountMap.getOrDefault(Consultation.ConsultationStatus.CANCELED, 0L))
			.consultationCompleted(statusCountMap.getOrDefault(Consultation.ConsultationStatus.COMPLETED, 0L))
			.build();

	}
}
