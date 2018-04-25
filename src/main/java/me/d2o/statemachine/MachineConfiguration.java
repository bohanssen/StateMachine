package me.d2o.statemachine;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableJpaRepositories
@EntityScan
@EnableAsync
public class MachineConfiguration {

	public static void main(String[] args) {
	}

}
