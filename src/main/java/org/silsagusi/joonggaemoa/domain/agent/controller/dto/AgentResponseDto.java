package org.silsagusi.joonggaemoa.domain.agent.controller.dto;

import org.silsagusi.joonggaemoa.domain.agent.service.command.AgentCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AgentResponseDto {

	private Long id;
	private String name;
	private String phone;
	private String email;
	private String username;
	private String password;
	private String office;
	private String region;
	private String businessNo;
	private String role;

	public static AgentResponseDto toDto(AgentCommand agentCommand) {
		return AgentResponseDto.builder()
			.id(agentCommand.getId())
			.name(agentCommand.getName())
			.phone(agentCommand.getPhone())
			.email(agentCommand.getEmail())
			.username(agentCommand.getUsername())
			.password(agentCommand.getPassword())
			.office(agentCommand.getOffice())
			.region(agentCommand.getRegion())
			.businessNo(agentCommand.getBusinessNo())
			.role(agentCommand.getRole() + "")
			.build();
	}
}
