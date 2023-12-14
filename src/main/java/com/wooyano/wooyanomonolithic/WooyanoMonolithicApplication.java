package com.wooyano.wooyanomonolithic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing // JPA Auditing 활성화
public class WooyanoMonolithicApplication {

    public static void main(String[] args) {
        SpringApplication.run(WooyanoMonolithicApplication.class, args);
    }

}
