package com.wooyano.wooyanomonolithic.settlement.batch.processor;

import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import com.wooyano.wooyanomonolithic.settlement.dto.SettleResult;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentItemProcessor implements ItemProcessor<SettleResult, DailySettle> {

    @Override
    public DailySettle process(SettleResult item)  {
        String clientEmail = item.getClientEmail();
        long totalAmount = item.getTotalAmount();
        long payOutAmount = item.getPayOutAmount();
        long fee = item.getFee();
        return DailySettle.createSettle(clientEmail, totalAmount, LocalDate.now(), "0", fee, payOutAmount);

    }
}