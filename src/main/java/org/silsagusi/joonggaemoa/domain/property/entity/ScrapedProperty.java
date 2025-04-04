package org.silsagusi.joonggaemoa.domain.property.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.ToString;

@ToString
@Entity(name = "scraped_properties")
@Getter
public class ScrapedProperty {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "scraped_property_id")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Complex complex;

	private String sido;

	private String gugun;

	private String dongeupmeon;

	private String roadName;

	private String agentName;

	private String agentOffice;

	private String agentNo;
}
