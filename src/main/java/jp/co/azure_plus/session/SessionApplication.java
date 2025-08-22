package jp.co.azure_plus.session;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class SessionApplication {

	public static void main(String[] args) {
		SpringApplication.run(SessionApplication.class, args);

		System.out.println(new BCryptPasswordEncoder().encode("password"));

	}

}
