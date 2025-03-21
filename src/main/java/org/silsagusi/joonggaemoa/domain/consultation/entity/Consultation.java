package org.silsagusi.joonggaemoa.domain.consultation.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.silsagusi.joonggaemoa.domain.agent.entity.AgentCustomer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "consultations")
@Getter
public class Consultation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "consultation_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private AgentCustomer agentCustomer;

	private LocalDateTime date;

	private String purpose;

	private Integer interestProperty;

	private String interestLocation;

	private String contractType;

	private String assetStatus;

	private String memo;

	@Column(name = "consultation_status")
	@Enumerated(EnumType.STRING)
	private ConsultationStatus consultationStatus;
}
