package org.silsagusi.joonggaemoa.domain.message.repository;

import org.silsagusi.joonggaemoa.domain.message.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
	Page<Message> findByCustomerAgentIdAndIdLessThan(Long agentId, Long idIsLessThan, Pageable pageable);
}
