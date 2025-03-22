

package org.example.fooddeliveryapp;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableScheduling
public class FoodDeliveryAppApplication implements CommandLineRunner {
	@Autowired
	private WeatherDataScheduler weatherDataScheduler;

	public static void main(String[] args) {
		SpringApplication.run(FoodDeliveryAppApplication.class, args);
	}

	/**
	 * When app is booted then database gets the latest weather data
	 */
	@Override
	public void run(String... args) throws Exception {
		weatherDataScheduler.fetchWeatherData();
	}
}










