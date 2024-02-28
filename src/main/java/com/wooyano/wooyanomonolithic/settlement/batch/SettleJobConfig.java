package com.wooyano.wooyanomonolithic.settlement.batch;

import com.wooyano.wooyanomonolithic.settlement.batch.processor.PaymentItemProcessor;
import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import com.wooyano.wooyanomonolithic.settlement.dto.SettleResult;
import jakarta.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import javax.sql.DataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.transaction.PlatformTransactionManager;



@Configuration
@Slf4j
@RequiredArgsConstructor
public class SettleJobConfig {

    private final EntityManagerFactory entityManagerFactory;
    private final DataSource dataSource;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;

    private final static int CHUNK_SIZE = 10;
    private final PaymentItemProcessor paymentItemProcessor;




    @Bean
    public Job createJob() {
        return new JobBuilder("settleJob", jobRepository)
              //  .validator(new CustomJobParameterValidator())
                .start(settleStep())
                .build();
    }

    @Bean
    @JobScope
    public Step settleStep() {
        return new StepBuilder("settleStep", jobRepository)
                .<SettleResult, DailySettle>chunk(CHUNK_SIZE,transactionManager) // Chunk 크기를 지정
                .reader(reader())
                .processor(paymentItemProcessor)
                .writer(jdbcBatchItemWriter())
                .build();

    }

   // @Value("#{jobParameters['requestDate']}") String requestDateStr
    @Bean
    @StepScope
    public JpaPagingItemReader<SettleResult> reader() {
/*        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate requestDate = LocalDate.parse(requestDateStr, formatter);*/
        LocalDate requestDate = LocalDate.now().minusDays(1);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("startDateTime", LocalDateTime.parse(requestDate + "T00:00:00"));
        parameters.put("endDateTime", LocalDateTime.parse(requestDate + "T23:59:59"));


        String queryString = String.format("select new %s(p.clientEmail, sum(p.totalAmount),sum(p.fee),sum(p.payOutAmount))"
                + " From Payment p where p.approvedAt between :startDateTime and :endDateTime group by p.clientEmail",
                SettleResult.class.getName());
        JpaPagingItemReaderBuilder<SettleResult> jpaPagingItemReaderBuilder  = new JpaPagingItemReaderBuilder<>();
        JpaPagingItemReader<SettleResult> paymentItemReader = jpaPagingItemReaderBuilder
                .name("paymentItemReader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .queryString(queryString)
                .pageSize(10)
                .build();
        return paymentItemReader;
    }



    @Bean
    public JdbcBatchItemWriter<DailySettle> jdbcBatchItemWriter() {
        log.info("jdbcBatchItemWriter");
        JdbcBatchItemWriter<DailySettle> build = new JdbcBatchItemWriterBuilder<DailySettle>()
                .dataSource(dataSource)
                .sql("INSERT INTO daily_settle(settlement_date, total_Amount, client_Email, settle_Status, fee, pay_Out_Amount) "
                        + "VALUES (:settlementDate, :totalAmount, :clientEmail, :settleType, :fee, :payOutAmount)")
                .beanMapped()
                .build();
        return build;
    }







}
