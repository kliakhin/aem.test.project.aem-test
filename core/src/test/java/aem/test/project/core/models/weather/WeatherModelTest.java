package aem.test.project.core.models.weather;

import aem.test.project.core.bean.WeatherItems;
import aem.test.project.core.service.WeatherService;
import junitx.util.PrivateAccessor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class WeatherModelTest {

    private static final String CITY_LONDON = "London";
    private static final String INCORRECT_CITY = "sdgfdsfgdfgdfh";
    private static final String CITY_FIELD = "city";
    private static final String WEATHER_FIELD = "weather";

    @Mock
    private WeatherService weatherService;

    @Mock
    private WeatherItems weatherItems;

    @InjectMocks
    private WeatherModel sut;

    @Before
    public void setUp() throws NoSuchFieldException {
        PrivateAccessor.setField(sut, CITY_FIELD, CITY_LONDON);
        PrivateAccessor.setField(sut, WEATHER_FIELD, weatherItems);
    }

    @Test
    public void shouldValidModelWhenCityLondon() {
        sut.init();

        assertThat(sut.isValid(), is(Boolean.TRUE));
    }
}