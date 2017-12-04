package edu.sjsu.cloudbread.service;

import java.util.List;

public interface WeatherForecastService {

	String getTodaysWeather();

	List<String> getForecast();

}
