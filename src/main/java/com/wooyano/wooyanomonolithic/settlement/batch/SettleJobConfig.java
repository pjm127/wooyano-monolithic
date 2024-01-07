package com.wooyano.wooyanomonolithic.settlement.batch;

import com.wooyano.wooyanomonolithic.settlement.batch.processor.PaymentItemProcessor;
import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import com.wooyano.wooyanomonolithic.settlement.dto.PaymentResult;
import jakarta.persistence.EntityManagerFactory;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
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
import org.springframework.batch.item.kafka.KafkaItemReader;
import org.springframework.batch.item.kafka.builder.KafkaItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
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
    //private final SettleItemWriter settleItemWriter;

    //private final ConsumerConfiguration consumerConfiguration;

    //private final RedisTemplate<String, String> redisTemplate;

    /*@Value("${spring.kafka.topic}")
    private String topic;*/

    @Bean
    public Job createJob() {
        return new JobBuilder("settleJob", jobRepository)
           //     .validator(new CustomJobParameterValidator())
                .start(settleStep(null))
                .build();
    }

    @Bean
    @JobScope
    public Step settleStep(@Value("#{jobParameters['requestDate']}") String requestDate ) {
        return new StepBuilder("settleStep", jobRepository)
                .<PaymentResult, DailySettle>chunk(CHUNK_SIZE,transactionManager) // Chunk 크기를 지정
                .reader(reader(null))
                .processor(paymentItemProcessor)
                .writer(jdbcBatchItemWriter())
                .build();

    }


    //@Value("#{jobParameters['requestDate']}") String requestDate
    @Bean
    @StepScope
    public JpaPagingItemReader<PaymentResult> reader(@Value("#{jobParameters['requestDate']}") String requestDate) {

        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("startDateTime", LocalDateTime.parse(requestDate + "T00:00:00"));
        parameters.put("endDateTime", LocalDateTime.parse(requestDate + "T23:59:59"));


        String queryString = String.format("select new %s(p.clientEmail, sum(p.totalAmount)) From Payment p "
                + "where p.approvedAt between :startDateTime and :endDateTime group by p.clientEmail", PaymentResult.class.getName());
        //String queryString = "SELECT p FROM Payment p";
        JpaPagingItemReaderBuilder<PaymentResult> jpaPagingItemReaderBuilder  = new JpaPagingItemReaderBuilder<>();
        JpaPagingItemReader<PaymentResult> paymentItemReader = jpaPagingItemReaderBuilder
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
                .sql("INSERT INTO daily_settle(start_Date, total_Amount, client_Email, settle_Status, fee, pay_Out_Amount) "
                        + "VALUES (:settlementDate, :totalAmount, :clientEmail, :settleType, :fee, :payOutAmount)")
                .beanMapped()
                .build();
        return build;
    }

/*


    //@Value("#{jobParameters['requestDate']}") String requestDate
    @Bean
    @StepScope
    public JpaPagingItemReader<PaymentResult> reader() {

        String requestDate = "2023-11-09";
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("startDateTime", LocalDateTime.parse(requestDate + "T00:00:00"));
        parameters.put("endDateTime", LocalDateTime.parse(requestDate + "T23:59:59"));


       String queryString = String.format("select new %s(p.clientEmail, sum(p.totalAmount)) From Payment p "
               + "where p.approvedAt between :startDateTime and :endDateTime group by p.clientEmail", PaymentResult.class.getName());
        //String queryString = "SELECT p FROM Payment p";
        JpaPagingItemReaderBuilder<PaymentResult> jpaPagingItemReaderBuilder  = new JpaPagingItemReaderBuilder<>();
        JpaPagingItemReader<PaymentResult> paymentItemReader = jpaPagingItemReaderBuilder
                .name("paymentItemReader")
                .entityManagerFactory(entityManagerFactory) //readerEntityManagerFactory.getObject()
                .parameterValues(parameters)
                .queryString(queryString)
                .pageSize(10)
                .build();
        log.info("reader={}",paymentItemReader.toString());
        return paymentItemReader;
    }

    //@Value("#{jobParameters['requestDate']}") String requestDate
    @Bean
    @StepScope
    public JpaCursorItemReader<PaymentResult> reader3() {
        String requestDate = "2023-11-09";
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("startDateTime", LocalDateTime.parse(requestDate + "T00:00:00"));
        parameters.put("endDateTime", LocalDateTime.parse(requestDate + "T23:59:59"));

        String queryString = String.format("select new %s(p.clientEmail, sum(p.totalAmount)) From Payment p "
                + "where p.approvedAt between :startDateTime and :endDateTime group by p.clientEmail", PaymentResult.class.getName());

        JpaCursorItemReaderBuilder<PaymentResult> jpaCursorItemReaderBuilder = new JpaCursorItemReaderBuilder<>();
        JpaCursorItemReader<PaymentResult> paymentItemReader = jpaCursorItemReaderBuilder
                .name("paymentItemReader")
                .entityManagerFactory(entityManagerFactory) // readerEntityManagerFactory.getObject()
                .parameterValues(parameters)
                .queryString(queryString)
                .build();

        log.info("reader={}", paymentItemReader);
        return paymentItemReader;
    }




@Bean
public QuerydslPagingItemReader<PaymentResult> reader2(){
    String requestDate = "2023-11-09";
    Map<String, Object> parameters = new LinkedHashMap<>();
    LocalDateTime startDateTime = LocalDateTime.parse(requestDate + "T00:00:00");
    LocalDateTime endDateTime = LocalDateTime.parse(requestDate + "T23:59:59");
    //     parameters.put("startDateTime", LocalDateTime.parse(requestDate + "T00:00:00"));
    //   parameters.put("endDateTime", LocalDateTime.parse(requestDate + "T23:59:59"));
    QuerydslPagingItemReader<PaymentResult> totalAmount = new QuerydslPagingItemReader<>(entityManagerFactory,
            CHUNK_SIZE,
            queryFactory ->
                    queryFactory.select(Projections.fields(PaymentResult.class, payment.clientEmail,
                                    payment.totalAmount.sum().as("totalAmount")))
                            .from(payment)
                            .where(payment.approvedAt.between(startDateTime, endDateTime))
                            .groupBy(payment.clientEmail));
    return totalAmount;
    //Projections.fields(PaymentResult.class,payment.clientEmail,payment.totalAmount.sum().as("totalAmount"))
}







    @Bean
    public JpaItemWriter<DailySettle> writer() {
        JpaItemWriter<DailySettle> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }
*/



}
