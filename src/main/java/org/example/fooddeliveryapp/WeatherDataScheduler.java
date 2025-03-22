package org.example.fooddeliveryapp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.net.URL;
import java.time.LocalDateTime;

/**
 * Class for fetching weather data
 */
@Service
public class WeatherDataScheduler {
    @Autowired
    private WeatherDataService weatherDataService;

    private static final String WEATHER_API_URL = "https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php";

    /**
     * Fetches weather data once every hour, 15 minutes past a full hour and saves it into H2 database
     */
    @Scheduled(cron = "0 15 * * * *")
    public void fetchWeatherData() {
        try {
            System.out.println("Fetching weather data...");
            URL url = new URL(WEATHER_API_URL);
            InputStream stream = url.openStream();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(stream);
            document.getDocumentElement().normalize();

            NodeList nodes = document.getElementsByTagName("station");
            String[] stations = {"Tallinn-Harku", "Tartu-Tõravere", "Pärnu"};

            for (int i = 0; i < nodes.getLength(); i++) {
                Element stationElement = (Element) nodes.item(i);
                String stationName = stationElement.getElementsByTagName("name").item(0).getTextContent();

                for (String station : stations) {
                    if (station.equals(stationName)) {
                        WeatherData weatherData = new WeatherData();
                        weatherData.setStationName(station);
                        weatherData.setWmoCodeOfStation(getElementValue(stationElement, "wmocode"));
                        weatherData.setAirTemp(getDoubleValue(stationElement, "airtemperature"));
                        weatherData.setWindSpeed(getDoubleValue(stationElement, "windspeed"));
                        weatherData.setWeatherPhenomenon(getElementValue(stationElement, "phenomenon"));
                        weatherData.setTimestamp(LocalDateTime.now());
                        weatherDataService.saveWeatherData(weatherData);
                    }
                }
            }
            System.out.println("Weather data saved successfully.");
        } catch (Exception e) {
            System.err.println("Error fetching weather data: " + e.getMessage());
        }
    }
    private String getElementValue(Element element, String tag) {
        NodeList nodeList = element.getElementsByTagName(tag);
        return nodeList.getLength() > 0 ? nodeList.item(0).getTextContent() : "";
    }

    private double getDoubleValue(Element element, String tag) {
        String value = getElementValue(element, tag);
        return value.isEmpty() ? 0.0 : Double.parseDouble(value);
    }
}