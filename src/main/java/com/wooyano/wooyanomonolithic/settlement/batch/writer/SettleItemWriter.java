package com.wooyano.wooyanomonolithic.settlement.batch.writer;

import com.wooyano.wooyanomonolithic.settlement.domain.DailySettle;
import com.wooyano.wooyanomonolithic.settlement.infrastructure.DailySettleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
@Transactional
@RequiredArgsConstructor
@Slf4j
public class SettleItemWriter implements ItemWriter<DailySettle> {
    private final DailySettleRepository dailySettleRepository;


    @Override
    public void write(Chunk<? extends DailySettle> chunk) throws Exception {

    for(DailySettle dailySettle : chunk){
        dailySettleRepository.save(dailySettle);
    }
    }
}





