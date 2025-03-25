package org.silsagusi.joonggaemoa.global.batch;

import java.time.LocalDateTime;
import java.util.Map;

import org.silsagusi.joonggaemoa.domain.message.entity.Message;
import org.silsagusi.joonggaemoa.domain.message.entity.ReservedMessage;
import org.silsagusi.joonggaemoa.domain.message.repository.MessageRepository;
import org.silsagusi.joonggaemoa.domain.message.repository.ReservedMessageRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.PlatformTransactionManager;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class SmsBatchJogConfig {

	private static final String JOB_NAME = "smsJob1";
	private static final int CHUNK_SIZE = 10;

	private final JobRepository jobRepository;
	private final PlatformTransactionManager platformTransactionManager;
	private final ReservedMessageRepository reservedMessageRepository;
	private final MessageRepository messageRepository;

	@Bean
	public Job createJob(Step step) {
		return new JobBuilder(JOB_NAME, jobRepository)
			.start(step)
			.build();
	}

	@Bean
	public Step createStep() {
		return new StepBuilder(JOB_NAME + "Step1", jobRepository)
			.<ReservedMessage, Message>chunk(CHUNK_SIZE, platformTransactionManager)
			.reader(reader())
			.processor(processor())
			.writer(writer())
			.build();
	}

	@Bean
	public RepositoryItemReader<ReservedMessage> reader() {
		return new RepositoryItemReaderBuilder<ReservedMessage>()
			.name("reservedMessageReader")
			.pageSize(CHUNK_SIZE)
			.methodName("findPendingMessages")
			.arguments(LocalDateTime.now(), LocalDateTime.now().plusMinutes(1))
			.repository(reservedMessageRepository)
			.sorts(Map.of("id", Sort.Direction.DESC))
			.build();
	}

	@Bean
	public ItemProcessor<ReservedMessage, Message> processor() {
		return reservedMessage -> {
			log.info("Processing reserved message: {}", reservedMessage);
			if (reservedMessage == null) {
				log.warn("Reserved message is null");
				return null;
			}

			try {
				if (reservedMessage.getCustomer() == null || reservedMessage.getContent() == null) {
					log.error("Reserved message content is null");
					return null;
				}

				return new Message(reservedMessage.getCustomer(), reservedMessage.getContent());
			} catch (Exception e) {
				log.error("Error processing reserved message", e);
				return null;
			}
		};
	}

	@Bean
	public ItemWriter<Message> writer() {
		return items -> {
			try {
				if (items == null || items.isEmpty()) {
					log.warn("Items is null");
					return;
				}

				log.info("Writing {} messages", items.size());
				messageRepository.saveAll(items);
				log.info("Successfully writing messages");
			} catch (Exception e) {
				log.error("Error writing messages", e);
				throw e;
			}
		};
	}
}
