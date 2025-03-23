package org.example.fooddeliveryapp;


import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

	/**
	 * /api/fee endpoint tests
	 */
	@Test
	void testGetDeliveryFeeWithoutParams() throws Exception {
		mvc.perform(get("/api/fee"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\":\"Both 'city' and 'vehicleType' must be provided in the request\"}"));
	}
	@Test
	void testGetDeliveryFeeMissingVehicleType() throws Exception {
		mvc.perform(get("/api/fee").param("city", "Tallinn"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Both 'city' and 'vehicleType' must be provided in the request\"}"));
	}
	@Test
	void testGetDeliveryFeeMissingCity() throws Exception {
		mvc.perform(get("/api/fee").param("vehicleType", "Bike"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Both 'city' and 'vehicleType' must be provided in the request\"}"));
	}
	@Test
	void testInvalidDateTimeFormat() throws Exception {
		mvc.perform(get("/api/fee")
						.param("city", "Tartu")
						.param("vehicleType", "Scooter")
						.param("dateTime", "invalid-date-format"))
				.andExpect(content().json("{\"error\": \"Datetime is not in correct format (yyyy-MM-dd'T'HH:mm:ss)\"}"));
	}
	@Test
	void testValidDateTime() throws Exception {

		mvc.perform(get("/api/fee")
						.param("city", "Tartu")
						.param("vehicleType", "Car")
						.param("dateTime", time.toString().substring(0,19)))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.delivery-fee").exists());
	}
	@Test
	void testNormalConditionsFeeCalculation() throws Exception {
		mvc.perform(get("/api/fee")
						.param("city", "Tallinn")
						.param("vehicleType", "Car"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.delivery-fee").exists());
	}
	@Test
	void testWeatherDataNotAvailable() throws Exception {
		mvc.perform(get("/api/fee")
						.param("city", "Tallinn")
						.param("vehicleType", "Car")
						.param("dateTime", "1999-01-01T12:00:00"))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"No weather data available for Tallinn\"}"));
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






	/**
	 * /api/basefee endpoint tests
	 */
	@Test
	void testGetCurrentBaseFees() throws Exception {
		mvc.perform(get("/api/basefee"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.['Base fees']").isArray());
	}

	@Test
	void testUpdateBaseFeeValidData() throws Exception {
		String requestBody = """
        {
            "city": "Tallinn",
            "vehicleType": "Scooter",
            "newFee": "3.5"
        }
    """;

		mvc.perform(put("/api/basefee")
						.contentType("application/json")
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"success\":\"New fee for city Tallinn vehicle Scooter is 3.5\"}"));
	}
	@Test
	void testUpdateBaseFeeMissingFields() throws Exception {
		String requestBody = """
        {
            "city": "Tallinn"
        }
    """;

		mvc.perform(put("/api/basefee")
						.contentType("application/json")
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"'city', 'vehicleType' and 'newFee' must be provided in the request body\"}"));
	}
	@Test
	void testUpdateBaseFeeInvalidCity() throws Exception {
		String requestBody = """
        {
            "city": "InvalidCity",
            "vehicleType": "Scooter",
            "newFee": "5"
        }
    """;

		mvc.perform(put("/api/basefee")
						.contentType("application/json")
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"Invalid city or vehicle type. Valid cities - (Tallinn, Tartu, Pärnu), Valid vehicle types - (Bike, Car, Scooter).\"}"));
	}
	@Test
	void testUpdateBaseFeeInvalidNewFee() throws Exception {
		String requestBody = """
        {
            "city": "Pärnu",
            "vehicleType": "Bike",
            "newFee": "invalid_fee"
        }
    """;

		mvc.perform(put("/api/basefee")
						.contentType("application/json")
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"newFee must be double >=0\"}"));
	}
	@Test
	void testUpdateBaseFeeNegativeNewFee() throws Exception {
		String requestBody = """
        {
            "city": "Tallinn",
            "vehicleType": "Car",
            "newFee": "-5"
        }
    """;

		mvc.perform(put("/api/basefee")
						.contentType("application/json")
						.content(requestBody))
				.andExpect(status().isOk())
				.andExpect(content().json("{\"error\": \"newFee must be double >=0\"}"));
	}

}
