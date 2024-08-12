package com.igloo_club.nungil_v3;

import com.igloo_club.nungil_v3.config.SecurityConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@ConfigurationPropertiesScan
@EnableConfigurationProperties(SecurityConfigProperties.class)
public class NungilV3Application {

	public static void main(String[] args) {
		SpringApplication.run(NungilV3Application.class, args);
	}
}
