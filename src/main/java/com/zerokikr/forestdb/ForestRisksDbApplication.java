package com.zerokikr.forestdb;

import com.zerokikr.forestdb.configuration.ForestDbSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ForestRisksDbApplication {

	public static void main(String[] args) {
		SpringApplication.run(new Class[] {ForestRisksDbApplication.class, ForestDbSecurityConfig.class}, args);
	}

}
