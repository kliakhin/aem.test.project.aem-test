package aem.test.project.core.service.impl;

import aem.test.project.core.bean.WeatherItems;
import aem.test.project.core.service.WeatherService;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.HttpRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;
import org.json.JSONObject;

@Component(immediate = true)
@Service(WeatherService.class)
@Slf4j
public class WeatherServiceImpl implements WeatherService {

    private static final String PATH_TO_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    private static final String APP_ID = "appid";
    private static final String APP_ID_VALUE = "cc2038a06d80851744267bea177a80e8";

    @Override
    public WeatherItems getWeather(String city) {
        log.info("WeatherServiceImpl: get weather by city {}", city);
        return getWeatherFromApi(city);
    }

    private WeatherItems getWeatherFromApi(String city) {
        HttpRequest httpRequest = buildRequest(city);

        JSONObject obj = execute(httpRequest);

        return convertToWeatherItems(obj);
    }

    private HttpRequest buildRequest(String city) {
        return Unirest.get(PATH_TO_WEATHER_API)
                .queryString("q", city)
                .queryString(APP_ID, APP_ID_VALUE);
    }

    private JSONObject execute(HttpRequest httpRequest) {
        try {
            return httpRequest.asJson().getBody().getObject();
        } catch (UnirestException e) {
            log.error("Exception message: " + e.getMessage());
            return null;
        }
    }

    private WeatherItems convertToWeatherItems(JSONObject obj) {
        if (obj == null || checkIfJsonValid(obj)) {
            return new WeatherItems();
        }

        WeatherItems items = new WeatherItems();
        items.setName(obj.getString("name"));
        items.setDescription(obj.getJSONArray("weather").getJSONObject(0).getString("description"));
        items.setTemperature(obj.getJSONObject("main").getDouble("temp"));
        items.setMinTemperature(obj.getJSONObject("main").getDouble("temp_min"));
        items.setMaxTemperature(obj.getJSONObject("main").getDouble("temp_max"));
        items.setWindSpeed(obj.getJSONObject("wind").getDouble("speed"));
        items.setVisibility(obj.getInt("visibility"));
        items.setPressure(obj.getJSONObject("main").getInt("pressure"));

        return items;
    }

    private boolean checkIfJsonValid(JSONObject obj) {
        return obj.getInt("cod") == 404;
    }
}