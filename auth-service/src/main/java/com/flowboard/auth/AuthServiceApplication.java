package com.flowboard.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.flowboard.auth.entity.User;
import com.flowboard.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			if (!userRepository.existsByEmail("admin@flowboard.com")) {
				User admin = new User();
				admin.setFullName("Platform Admin");
				admin.setUsername("admin");
				admin.setEmail("admin@flowboard.com");
				admin.setPasswordHash(passwordEncoder.encode("admin123"));
				admin.setRole("PLATFORM_ADMIN");
				admin.setIsActive(true);
				admin.setProvider("LOCAL");
				userRepository.save(admin);
				log.info(">>> Default Admin Account Created!");
				log.info(">>> Email: admin@flowboard.com");
				log.info(">>> Password: [PROTECTED]");
			}
		};
	}
}
