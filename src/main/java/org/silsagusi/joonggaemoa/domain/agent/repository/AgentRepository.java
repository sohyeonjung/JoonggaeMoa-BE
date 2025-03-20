package org.silsagusi.joonggaemoa.domain.agent.repository;

import java.util.Optional;

import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentRepository extends JpaRepository<Agent, Long> {
	Optional<Agent> getAgentById(Long id);

	Agent findByUsername(String username);
}
