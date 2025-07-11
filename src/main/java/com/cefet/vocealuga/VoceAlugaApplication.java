package com.cefet.vocealuga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VoceAlugaApplication {

	public static void main(String[] args) {
		SpringApplication.run(VoceAlugaApplication.class, args);
	}
}
