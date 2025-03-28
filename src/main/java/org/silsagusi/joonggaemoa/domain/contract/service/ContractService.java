package org.silsagusi.joonggaemoa.domain.contract.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.silsagusi.joonggaemoa.domain.contract.entity.Contract;
import org.silsagusi.joonggaemoa.domain.contract.repository.ContractRepository;
import org.silsagusi.joonggaemoa.domain.contract.service.command.ContractCommand;
import org.silsagusi.joonggaemoa.domain.customer.entity.Customer;
import org.silsagusi.joonggaemoa.domain.customer.repository.CustomerRepository;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ContractService {

	private final ContractRepository contractRepository;
	private final CustomerRepository customerRepository;

	public void createContract(
		Long landlordId,
		Long tenantId,
		LocalDate createdAt,
		LocalDate expiredAt,
		String url
	) {

		Customer customerLandlord = customerRepository.findById(landlordId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
		Customer customerTenant = customerRepository.findById(tenantId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_ELEMENT));

		Contract contract = new Contract(
			customerLandlord,
			customerTenant,
			createdAt,
			expiredAt,
			url
		);
		contractRepository.save(contract);
	}

	public List<ContractCommand> getAllContracts() {
		List<Contract> contractList = contractRepository.findAll();
		return contractList.stream().map(it->ContractCommand.of(it)).toList();
	}

	public ContractCommand getContractById(Long contractId) {
		Contract contract = contractRepository.findById(contractId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_ELEMENT));
		return ContractCommand.of(contract);
	}
}
