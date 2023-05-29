package com.mlv.cameldsl.dao;

import com.mlv.cameldsl.entity.Weather;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class WeatherDao {

    private static Map<String, Weather> weatherMap = new HashMap<>();

    public WeatherDao() {

        Weather weather = Weather.builder()
                .id(1)
                .city("CHENNAI")
                .temp("100")
                .unit("C")
                .time(new Date().toString()).build();

        weatherMap.put("CHENNAI", weather);

    }

    public Weather getCurrentWeather(String city) {

        return weatherMap.get(city.toUpperCase());

    }

    public void setWeather(Weather weather) {
        weather.setTime(new Date().toString());
        weatherMap.put(weather.getCity().toUpperCase(), weather);

    }
}
