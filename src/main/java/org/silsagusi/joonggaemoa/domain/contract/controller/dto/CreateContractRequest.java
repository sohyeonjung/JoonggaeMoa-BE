package org.silsagusi.joonggaemoa.domain.contract.controller.dto;

import java.time.LocalDate;

import jakarta.websocket.server.ServerEndpoint;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateContractRequest {
	private Long landlordId;
	private Long tenantId;
	private LocalDate createdAt;
	private LocalDate expiredAt;
	private String url;
}
