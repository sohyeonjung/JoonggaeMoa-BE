package org.silsagusi.joonggaemoa.domain.consultation.service.command;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConsultationStatusCommand {
	Long consultationAll;
	Long consultationWaiting;
	Long consultationConfirmed;
	Long consultationCancelled;
	Long consultationCompleted;
}
