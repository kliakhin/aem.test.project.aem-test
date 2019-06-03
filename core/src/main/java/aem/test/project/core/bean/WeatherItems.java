package aem.test.project.core.bean;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class WeatherItems {

    private String name;
    private String description;
    private double temperature;
    private double maxTemperature;
    private double minTemperature;
    private double windSpeed;
    private int visibility;
    private int pressure;
}