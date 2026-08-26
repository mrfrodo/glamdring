package com.frodo.glamdring;

import com.frodo.glamdring.application.applicationservices.TipOfTheDayApplicationService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(excludeName = {
		"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
public class Glamdring {

	public static void main(String[] args) {
		SpringApplication.run(Glamdring.class, args);
	}

	/**
	 * Quick smoke test for the local Ollama model — refreshes the tip of the
	 * day on startup through the application service, so it's obvious the
	 * model is actually running. Remove once real usage lands.
	 */
	@Bean
	ApplicationRunner ollamaTipOfTheDay(TipOfTheDayApplicationService tipOfTheDayApplicationService) {
		return args -> {
			try {
				tipOfTheDayApplicationService.refreshTip();
				System.out.println("=== Ollama tip of the day ===");
				System.out.println(tipOfTheDayApplicationService.getTip().orElse("(no tip)"));
				System.out.println("==============================");
			} catch (Exception e) {
				System.out.println("Ollama not reachable — skipping tip of the day (" + e.getMessage() + ")");
			}
		};
	}

}
