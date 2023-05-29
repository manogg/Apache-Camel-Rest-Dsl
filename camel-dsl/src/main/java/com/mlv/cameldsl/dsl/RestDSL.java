package com.mlv.cameldsl.dsl;

import com.mlv.cameldsl.dao.WeatherDao;
import com.mlv.cameldsl.entity.Weather;
import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.support.DefaultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.Objects;

import static org.apache.camel.Exchange.HTTP_RESPONSE_CODE;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Component
public class RestDSL extends RouteBuilder {
    @Autowired
    private WeatherDao weatherDao;

    @Override
    public void configure() throws Exception {

        restConfiguration().component("servlet").bindingMode(RestBindingMode.json);

        rest().consumes(MediaType.APPLICATION_JSON_VALUE)
                .produces(MediaType.APPLICATION_JSON_VALUE)
                .get("/weather/{city}")
                .outType(Weather.class)
                .to("direct:get-weather-data");

        from("direct:get-weather-data").process(this::getWeatherData);

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
