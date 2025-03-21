package org.silsagusi.joonggaemoa.domain.message.repository;

import org.silsagusi.joonggaemoa.domain.message.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {
}
