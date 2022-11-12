package com.zerokikr.forestdb;

import com.zerokikr.forestdb.configuration.FDbWebMvcConfig;
import com.zerokikr.forestdb.configuration.ForestDbSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication

public class ForestRisksDbApplication {

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	public static void main(String[] args) {
		SpringApplication.run(new Class[] {ForestRisksDbApplication.class, ForestDbSecurityConfig.class, FDbWebMvcConfig.class}, args);
	}

}
