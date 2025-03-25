package org.silsagusi.joonggaemoa.domain.customer.service;

import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.silsagusi.joonggaemoa.domain.agent.entity.Agent;
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
		Agent agent = agentRepository.getOne(agentId);

		Customer customer = new Customer(
			name,
			birthday,
			phone,
			email,
			job,
			isVip,
			memo,
			consent,
			agent
		);

		customerRepository.save(customer);

	}

	public void bulkCreateCustomer(Long agentId, MultipartFile file) {
		//TODO: 엑셀 파일 타입 확인
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());

			int sheetsLength = workbook.getNumberOfSheets();
			Agent agent = agentRepository.getOne(agentId);

			for (int sheetIndex = 0; sheetIndex < sheetsLength; sheetIndex++) {
				XSSFSheet workSheet = workbook.getSheetAt(sheetIndex);

				for (int i = 1; i < workSheet.getLastRowNum(); i++) {
					try {

						Row row = workSheet.getRow(i);
						if (row == null)
							continue;
						Customer customer = new Customer(
							row.getCell(0).getStringCellValue(),
							row.getCell(1).getLocalDateTimeCellValue().toLocalDate(),
							row.getCell(2).getStringCellValue(),
							row.getCell(3).getStringCellValue(),
							row.getCell(4).getStringCellValue(),
							row.getCell(5).getBooleanCellValue(),
							row.getCell(6).getStringCellValue(),
							row.getCell(7).getBooleanCellValue(),
							agent
						);

						customerRepository.save(customer);

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
