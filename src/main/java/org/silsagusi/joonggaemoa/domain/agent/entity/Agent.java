package org.silsagusi.joonggaemoa.domain.agent.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "agents")
@Getter
public class Agent {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "agent_id")
	private Long id;

	private String name;

	private String phone;

	private String email;

	private String username;

	private String password;

	private String office;

	private String region;

	private String businessNo;

	private Role role;

	enum Role {
		ROLE_AGENT, ROLE_CUSTOMER
	}
}
