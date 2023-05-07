package com.coderly.inmobipay;

import com.coderly.inmobipay.core.repositories.CreditRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class InmobipayApplication {

	private final CreditRepository creditRepository;

	public InmobipayApplication(CreditRepository creditRepository) {
		this.creditRepository = creditRepository;
	}


	public static void main(String[] args) {
		SpringApplication.run(InmobipayApplication.class, args);
	}

}
