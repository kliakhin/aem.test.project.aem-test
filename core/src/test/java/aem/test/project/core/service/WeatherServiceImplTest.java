package aem.test.project.core.service;

import aem.test.project.core.bean.WeatherItems;
import aem.test.project.core.service.impl.WeatherServiceImpl;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.powermock.api.mockito.PowerMockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Unirest.class, WeatherServiceImpl.class})
public class WeatherServiceImplTest {

    private static final String CITY_LONDON = "London";
    private static final String INCORRECT_CITY = "sdgfdsfgdfgdfh";
    private static final String PATH_TO_WEATHER_API = "https://api.openweathermap.org/data/2.5/weather";
    private static final String Q_PARAMETER = "q";
    private static final String APP_ID = "appid";
    private static final String NAME_FIELD = "name";
    private static final String DESCRIPTION_FIELD = "description";
    private static final String TEMPERATURE_FIELD = "temp";
    private static final String MAX_TEMPERATURE_FIELD = "temp_max";
    private static final String MIN_TEMPERATURE_FIELD = "temp_min";
    private static final String WIND_SPEED_FIELD = "speed";
    private static final String VISIBILITY_FIELD = "visibility";
    private static final String PRESSURE_FIELD = "pressure";
    private static final String CODE_FIELD = "cod";
    private static final String WIND_FIELD = "wind";
    private static final String MAIN_FIELD = "main";
    private static final String WEATHER_FIELD = "weather";
    private static final String APP_ID_VALUE = "cc2038a06d80851744267bea177a80e8";
    private static final String NAME_VALUE = "London";
    private static final String DESCRIPTION_VALUE = "Sightly";
    private static final double TEMPERATURE_VALUE = 300.0;
    private static final double MAX_TEMPERATURE_VALUE = 310.0;
    private static final double MIN_TEMPERATURE_VALUE = 299.0;
    private static final double WIND_SPEED_VALUE = 15.0;
    private static final int VISIBILITY_VALUE = 10000;
    private static final int PRESSURE_VALUE = 1024;
    private static final int CODE_404 = 404;

    @Mock
    private GetRequest getRequest;

    @Mock
    private HttpResponse<JsonNode> httpResponse;

    @Mock
    private JsonNode body;

    @Mock
    private JSONObject object;


    @Mock
    private JSONObject jsonObject;

    @Mock
    private JSONArray jsonArray;

    @InjectMocks
    private WeatherServiceImpl sut;

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(Unirest.class);
        when(Unirest.get(PATH_TO_WEATHER_API)).thenReturn(getRequest);
        when(getRequest.queryString("q", CITY_LONDON)).thenReturn(getRequest);
        when(getRequest.queryString(APP_ID, APP_ID_VALUE)).thenReturn(getRequest);
        when(getRequest.asJson()).thenReturn(httpResponse);
        when(httpResponse.getBody()).thenReturn(body);
        when(body.getObject()).thenReturn(object);

        when(object.getString(NAME_FIELD)).thenReturn(NAME_VALUE);
        when(object.getJSONArray(WEATHER_FIELD)).thenReturn(jsonArray);
        when(jsonArray.getJSONObject(0)).thenReturn(jsonObject);
        when(jsonObject.getString(DESCRIPTION_FIELD)).thenReturn(DESCRIPTION_VALUE);
        when(object.getJSONObject(MAIN_FIELD)).thenReturn(jsonObject);
        when(jsonObject.getDouble(TEMPERATURE_FIELD)).thenReturn(TEMPERATURE_VALUE);
        when(jsonObject.getDouble(MIN_TEMPERATURE_FIELD)).thenReturn(MIN_TEMPERATURE_VALUE);
        when(jsonObject.getDouble(MAX_TEMPERATURE_FIELD)).thenReturn(MAX_TEMPERATURE_VALUE);
        when(object.getJSONObject(WIND_FIELD)).thenReturn(jsonObject);
        when(jsonObject.getDouble(WIND_SPEED_FIELD)).thenReturn(WIND_SPEED_VALUE);
        when(object.getInt(VISIBILITY_FIELD)).thenReturn(VISIBILITY_VALUE);
        when(jsonObject.getInt(PRESSURE_FIELD)).thenReturn(PRESSURE_VALUE);
    }

    @Test
    public void shouldGetCorrectWeatherFromApi() throws UnirestException {
        WeatherItems weather = sut.getWeather(CITY_LONDON);

        verify(getRequest).queryString(Q_PARAMETER, CITY_LONDON);
        verify(getRequest).queryString(APP_ID, APP_ID_VALUE);
        verify(getRequest).asJson();
        verify(httpResponse).getBody();
        verify(body).getObject();
        assertThat(weather.getName(), is(NAME_VALUE));
        assertThat(weather.getDescription(), is(DESCRIPTION_VALUE));
        assertThat(weather.getTemperature(), is(TEMPERATURE_VALUE));
        assertThat(weather.getMaxTemperature(), is(MAX_TEMPERATURE_VALUE));
        assertThat(weather.getMinTemperature(), is(MIN_TEMPERATURE_VALUE));
        assertThat(weather.getWindSpeed(), is(WIND_SPEED_VALUE));
        assertThat(weather.getVisibility(), is(VISIBILITY_VALUE));
        assertThat(weather.getPressure(), is(PRESSURE_VALUE));
    }

    @Test
    public void shouldGetReturnEmptyWeatherItemsWhenInputCityIsIncorrect() {
        when(getRequest.queryString("q", INCORRECT_CITY)).thenReturn(getRequest);
        when(object.getInt(CODE_FIELD)).thenReturn(CODE_404);

        WeatherItems weather = sut.getWeather(INCORRECT_CITY);

        assertThat(weather.getName(), nullValue());
    }

    @Test
    public void shouldReturnNullWhenThrowUnirestException() throws UnirestException {
        when(getRequest.asJson()).thenThrow(new UnirestException(""));

        WeatherItems weather = sut.getWeather(CITY_LONDON);

        assertThat(weather.getName(), nullValue());
    }
}