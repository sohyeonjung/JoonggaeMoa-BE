package org.silsagusi.joonggaemoa.domain.consultation.contorller.dto;

import org.silsagusi.joonggaemoa.domain.consultation.service.command.ConsultationStatusCommand;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ConsultationStatusResponse {
	Long consultationAll;
	Long consultationWaiting;
	Long consultationConfirmed;
	Long consultationCancelled;
	Long consultationCompleted;

	public static ConsultationStatusResponse of(ConsultationStatusCommand consultationStatusCommand) {
		return ConsultationStatusResponse.builder()
			.consultationAll(consultationStatusCommand.getConsultationAll())
			.consultationWaiting(consultationStatusCommand.getConsultationWaiting())
			.consultationConfirmed(consultationStatusCommand.getConsultationConfirmed())
			.consultationCancelled(consultationStatusCommand.getConsultationCancelled())
			.consultationCompleted(consultationStatusCommand.getConsultationCompleted())
			.build();
	}
}
