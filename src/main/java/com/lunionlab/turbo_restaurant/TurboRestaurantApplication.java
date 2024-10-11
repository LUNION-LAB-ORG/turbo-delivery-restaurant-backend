package com.lunionlab.turbo_restaurant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.lunionlab.turbo_restaurant.services.SeederService;

@SpringBootApplication
public class TurboRestaurantApplication {
	@Autowired
	SeederService seederService;

	public static void main(String[] args) {
		SpringApplication.run(TurboRestaurantApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**").allowedOrigins("*");
			}
		};
	}

	@Bean
	public CommandLineRunner commandLineRunner(ApplicationContext applicationContext) {
		return args -> {
			if (args.length == 0) {
				return;
			}
			String seedRun = "seed";
			if (args[0].equals(seedRun)) {
				seederService.run();
				System.out.println("seed run successfully");
				System.exit(0);
			}
		};
	}

}
