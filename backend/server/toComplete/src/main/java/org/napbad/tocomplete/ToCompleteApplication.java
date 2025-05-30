package org.napbad.tocomplete;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ToCompleteApplication {

	public static void main(String[] args) {
		SpringApplication.run(ToCompleteApplication.class, args);
	}

}
