package org.silsagusi.joonggaemoa.domain.agent.repository;

import org.silsagusi.joonggaemoa.domain.agent.entity.AgentCustomer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AgentCustomerRepository extends JpaRepository<AgentCustomer, Long> {
    Optional<AgentCustomer> findByAgentIdAndCustomerId(Long agentId, Long customerId);
}
