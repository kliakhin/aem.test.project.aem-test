package aem.test.project.core.service;

import aem.test.project.core.bean.WeatherItems;

public interface WeatherService {
    WeatherItems getWeather(String city);
}