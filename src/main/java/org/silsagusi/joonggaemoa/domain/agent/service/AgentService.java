package org.silsagusi.joonggaemoa.domain.agent.service;

import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.domain.agent.service.command.AgentCommand;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AgentService {

	private final AgentRepository agentRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public void signup(
		String username,
		String password,
		String name,
		String phone,
		String email,
		String office,
		String region,
		String businessNo
	) {
		Agent agent = Agent.builder()
			.username(username)
			.password(bCryptPasswordEncoder.encode(password))
			.name(name)
			.phone(phone)
			.email(email)
			.office(office)
			.region(region)
			.businessNo(businessNo)
			.build();

		agentRepository.save(agent);
	}

	public AgentCommand getAgent(Long id) {
		Agent agent = agentRepository.getAgentById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

		return AgentCommand.toCommand(agent);
	}
}
