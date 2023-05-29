package com.mlv.cameldsl.dsl;

import com.mlv.cameldsl.dao.WeatherDao;
import com.mlv.cameldsl.entity.Weather;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.support.DefaultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class JavaDSL extends RouteBuilder {
    @Autowired
    private WeatherDao weatherDao;

    @Override
    public void configure() throws Exception {

        //http://localhost:8080/services/javadsl/weather/CHENNAI

        from("rest:get:javadsl/weather/{city}?produces=application/json")
                .outputType(Weather.class)
                .process(this::getWeatherData);
    }

    public void getWeatherData(Exchange exchange) {

        String city = exchange.getMessage().getHeader("city", String.class);
        Weather weather = weatherDao.getCurrentWeather(city);

        if (Objects.nonNull(weather)) {
            Message message = new DefaultMessage(exchange.getContext());
            message.setBody(weather);
            exchange.setMessage(message);

        } else {
            exchange.getMessage().setHeader(HTTP_RESPONSE_CODE, NOT_FOUND.value());
        }
    }
}
