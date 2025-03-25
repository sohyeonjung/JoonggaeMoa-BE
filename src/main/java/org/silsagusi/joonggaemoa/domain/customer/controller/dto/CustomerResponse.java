package org.silsagusi.joonggaemoa.domain.customer.controller.dto;

import java.time.LocalDate;

import org.silsagusi.joonggaemoa.domain.customer.service.command.CustomerCommand;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {
	private String name;
	private LocalDate birthday;
	private String phone;
	private String email;
	private String job;
	private Boolean isVip;
	private String memo;
	private Boolean consent;

	public static CustomerResponse of(CustomerCommand customerCommand) {
		return CustomerResponse.builder()
			.name(customerCommand.getName())
			.birthday(customerCommand.getBirthday())
			.phone(customerCommand.getPhone())
			.email(customerCommand.getEmail())
			.job(customerCommand.getJob())
			.isVip(customerCommand.getIsVip())
			.memo(customerCommand.getMemo())
			.consent(customerCommand.getConsent())
			.build();
	}

}
