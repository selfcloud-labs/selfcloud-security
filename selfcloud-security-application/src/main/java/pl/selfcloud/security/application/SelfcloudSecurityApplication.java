package pl.selfcloud.security.application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {
    "pl.selfcloud.security.web",
    "pl.selfcloud.security.domain",
    "pl.selfcloud.security.infrastructure",
    "pl.selfcloud.security.saga",
    "pl.selfcloud.security.api"
})
@EnableJpaRepositories(basePackages = "pl.selfcloud.security.domain.repository")
@EntityScan(basePackages = "pl.selfcloud.security.domain.model")
@EnableDiscoveryClient
public class SelfcloudSecurityApplication {
    public static void main(String[] args) {
        SpringApplication.run(SelfcloudSecurityApplication.class, args);
    }
}
