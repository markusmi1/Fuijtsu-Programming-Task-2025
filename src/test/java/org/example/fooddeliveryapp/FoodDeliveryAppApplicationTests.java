package org.example.fooddeliveryapp;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class FoodDeliveryAppApplicationTests {
	@Autowired
	private MockMvc mvc;
	@Autowired
	WeatherDataRepository weatherDataRepository;
	LocalDateTime time = LocalDateTime.now();

	/**
	 * inserts fake data to database
	 */
	@BeforeAll
	void testData() {
		WeatherData weatherDataGlaze = new WeatherData();
		weatherDataGlaze.setStationName("Tartu-Tõravere");
		weatherDataGlaze.setAirTemp(5);
		weatherDataGlaze.setWindSpeed(2);
		weatherDataGlaze.setTimestamp(LocalDateTime.now());

		weatherDataGlaze.setWeatherPhenomenon("glaze");

		WeatherData weatherDataWind = new WeatherData();
		weatherDataWind.setStationName("Tallinn-Harku");
		weatherDataWind.setAirTemp(5);
		weatherDataWind.setWindSpeed(24);
		weatherDataWind.setTimestamp(LocalDateTime.now());
		weatherDataWind.setWeatherPhenomenon("");

		WeatherData weatherDataTemp = new WeatherData();
		weatherDataTemp.setStationName("Pärnu");
		weatherDataTemp.setAirTemp(-25);
		weatherDataTemp.setWindSpeed(2);
		weatherDataTemp.setTimestamp(LocalDateTime.now());
		weatherDataTemp.setWeatherPhenomenon("");

		weatherDataRepository.save(weatherDataTemp);
		weatherDataRepository.save(weatherDataGlaze);
		weatherDataRepository.save(weatherDataWind);
	}

	/**
	 * deletes fake data from database
	 */
	@AfterAll
	void after(){
		weatherDataRepository.deleteWeatherDataByTimestampAfter(time);
	}
	@Test
	void testGetDeliveryFeeWithoutParams() throws Exception {
		mvc.perform(get("/api/fee"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\":\"Both 'city' and 'vehicleType' must be provided in the request\"}"));
	}
	@Test
	void testGetDeliveryFeeInvalidCity() throws Exception {
		mvc.perform(get("/api/fee").param("city", "").param("vehicleType", "Bike"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Invalid city or vehicle type. Valid cities - (Tallinn, Tartu, Pärnu), Valid vehicle types - (Bike, Car, Scooter).\"}"));
	}
	@Test
	void testGetDeliveryFeeInvalidVehicle() throws Exception {
		mvc.perform(get("/api/fee").param("city", "Tartu").param("vehicleType", "bus"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Invalid city or vehicle type. Valid cities - (Tallinn, Tartu, Pärnu), Valid vehicle types - (Bike, Car, Scooter).\"}"));
	}
	@Test
	void testWeatherPhenomenon() throws Exception{
		mvc.perform(get("/api/fee").param("city", "Tartu").param("vehicleType", "bike"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Usage of selected vehicle type is forbidden\"}"));

	}
	@Test
	void testWeatherWind() throws Exception{
		mvc.perform(get("/api/fee").param("city", "Tallinn").param("vehicleType", "bike"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Usage of selected vehicle type is forbidden\"}"));

	}
	@Test
	void testWeatherTemp() throws Exception{
		mvc.perform(get("/api/fee").param("city", "Pärnu").param("vehicleType", "scooter"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"delivery-fee\": \"3.5 €\"}"));

	}
}
