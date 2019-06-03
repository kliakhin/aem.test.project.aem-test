package aem.test.project.core.models.weather;

import aem.test.project.core.bean.WeatherItems;
import aem.test.project.core.service.WeatherService;
import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class WeatherModel {

    private static final String DEFAULT_CITY = "London";
    private static final String ERROR_MESSAGE = "City not found, please try another city";
    public static final WeatherItems EMPTY_WEATHER_ITEMS = new WeatherItems();

    @Inject
    @Default(values = DEFAULT_CITY)
    private String city;

    @ValueMapValue
    @Default(values = ERROR_MESSAGE)
    private String errorMessage;

    @Inject
    private WeatherService weatherService;

    private WeatherItems weather;

    private boolean valid;

    @PostConstruct
    public void init() throws RuntimeException {
        weather = weatherService.getWeather(city);
        if (!EMPTY_WEATHER_ITEMS.equals(weather) || weather != null) {
            valid = true;
        }
    }
}