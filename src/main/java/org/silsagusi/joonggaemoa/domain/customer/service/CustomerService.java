package org.silsagusi.joonggaemoa.domain.customer.service;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
import org.silsagusi.joonggaemoa.domain.agent.entity.AgentCustomer;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentCustomerRepository;
import org.silsagusi.joonggaemoa.domain.agent.repository.AgentRepository;
import org.silsagusi.joonggaemoa.domain.customer.entity.Customer;
import org.silsagusi.joonggaemoa.domain.customer.repository.CustomerRepository;
import org.silsagusi.joonggaemoa.global.api.exception.CustomException;
import org.silsagusi.joonggaemoa.global.api.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomerService {

	private final CustomerRepository customerRepository;
	private final AgentCustomerRepository agentCustomerRepository;
	private final AgentRepository agentRepository;

	public void createCustomer(
		Long agentId,
		String name,
		LocalDate birthday,
		String phone,
		String email,
		String job,
		Boolean isVip,
		String memo,
		Boolean consent
	) {
		Customer customer = Customer.builder()
			.name(name)
			.birthday(birthday)
			.phone(phone)
			.email(email)
			.job(job)
			.isVip(isVip)
			.memo(memo)
			.consent(consent)
			.build();

		Customer savedCustomer = customerRepository.save(customer);
		Agent agent = agentRepository.getOne(agentId);

		AgentCustomer agentCustomer = AgentCustomer.builder()
			.customer(savedCustomer)
			.agent(agent)
			.build();

		agentCustomerRepository.save(agentCustomer);

	}

	public void bulkCreateCustomer(Long agentId, MultipartFile file) {
		//TODO: 엑셀 파일 타입 확인
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

			int sheetsLength = workbook.getNumberOfSheets();

			for (int sheetIndex = 0; sheetIndex < sheetsLength; sheetIndex++) {
				XSSFSheet workSheet = workbook.getSheetAt(sheetIndex);

				for (int i = 3; i < workSheet.getLastRowNum(); i++) {
					try {

						Row row = workSheet.getRow(i);
						if (row == null)
							continue;
						Customer customer = Customer.builder()
							.name(row.getCell(0).getStringCellValue())
							.birthday(row.getCell(1).getLocalDateTimeCellValue().toLocalDate())
							.phone(row.getCell(2).getStringCellValue())
							.email(row.getCell(3).getStringCellValue())
							.job(row.getCell(4).getStringCellValue())
							.isVip(row.getCell(5).getBooleanCellValue())
							.memo(row.getCell(6).getStringCellValue())
							.consent(row.getCell(5).getBooleanCellValue())
							.build();

						Customer savedCustomer = customerRepository.save(customer);
						Agent agent = agentRepository.getOne(agentId);

						AgentCustomer agentCustomer = AgentCustomer.builder()
							.customer(savedCustomer)
							.agent(agent)
							.build();

						agentCustomerRepository.save(agentCustomer);

					} catch (Exception innerException) {
						System.err.println("Error processiong row " + (i) + ": " + innerException.getMessage());
						throw innerException;
					}
				}
			}

		} catch (Exception e) {
			throw new CustomException(ErrorCode.FILE_NOT_FOUND);
		}

	}
}
