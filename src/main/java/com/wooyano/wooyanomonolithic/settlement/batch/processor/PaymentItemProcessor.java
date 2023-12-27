package com.wooyano.wooyanomonolithic.settlement.batch.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import com.wooyano.wooyanomonolithic.settlement.dto.PaymentResult;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentItemProcessor implements ItemProcessor<PaymentResult, DailySettle> {
    private static final double vat = 0.02;
    private final ObjectMapper objectMapper;

/*    @Override
    public DailySettle process(String item) throws Exception {

return null;
        PaymentResult paymentResult = objectMapper.readValue(item, PaymentResult.class);
        log.info("paymentResult : {}", paymentResult);
        String clientEmail = paymentResult.getClientEmail();
        long totalAmount = paymentResult.getTotalAmount();

        long fee = (long) (totalAmount * vat);
        long paymentAmount = totalAmount - fee;
        String settleStatus = "0";


        DailySettle settle = DailySettle.createSettle(clientEmail, totalAmount, LocalDate.now(), settleStatus, fee,
                paymentAmount);
        log.info("settle : {}", settle);
        return settle;
    }*/

    @Override
    public DailySettle process(PaymentResult item) throws Exception {
        String clientEmail = item.getClientEmail();
        long totalAmount = item.getTotalAmount();
        return DailySettle.createSettle(clientEmail, totalAmount, LocalDate.now(), "0", 0L, 0L);

    }
}