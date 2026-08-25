package com.frodo.glamdring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(excludeName = {
		"org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration",
		"org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration"
})
public class Glamdring {

	public static void main(String[] args) {
		SpringApplication.run(Glamdring.class, args);
	}

}
