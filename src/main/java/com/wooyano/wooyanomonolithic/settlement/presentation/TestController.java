package com.wooyano.wooyanomonolithic.settlement.presentation;

import com.wooyano.wooyanomonolithic.settlement.batch.BatchScheduler;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/settle")
@RequiredArgsConstructor
public class TestController {

    private final BatchScheduler batchScheduler;

    @GetMapping("/test")
    public void test(){
        batchScheduler.runJob();
    }
}
