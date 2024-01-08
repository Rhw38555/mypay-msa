package com.mypay.settlement.job;

import com.mypay.settlement.tasklet.SettlementTasklet;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Configuration
@RequiredArgsConstructor
public class SettlementJob {
    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final SettlementTasklet settlementTasklet;

    @Bean
    public Job settlement() {
        return jobBuilderFactory.get("settlement")
                .start(settlementStep())
                .build();
    }

    @Bean
    public Step settlementStep() {
        return stepBuilderFactory.get("settlementStep")
                .tasklet(settlementTasklet)
                .build();
    }
}
