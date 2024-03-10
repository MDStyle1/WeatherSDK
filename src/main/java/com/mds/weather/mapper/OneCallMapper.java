package com.mds.weather.mapper;

import com.mds.weather.dto.one.WeatherApiDto;
import com.mds.weather.dto.one.WeatherStatusApiDto;
import com.mds.weather.model.*;

public class OneCallMapper implements MainMapper<Result, WeatherApiDto> {

    @Override
    public Class<WeatherApiDto> getClassDto() {
        return WeatherApiDto.class;
    }

    @Override
    public Result createFromApiDto(WeatherApiDto weatherApiDto) {
        Result weather = new Result();
        weather.setName(weatherApiDto.getTimezone());
        weather.setDatetime(weatherApiDto.getCurrent().getDt());
        weather.setTimezone(weatherApiDto.getTimezoneOffset());
        if (!weatherApiDto.getCurrent().getWeather().isEmpty()) {
            WeatherStatusApiDto weatherStatusApiDto = weatherApiDto.getCurrent().getWeather().get(0);
            Weather w = new Weather();
            w.setMain(weatherStatusApiDto.getMain());
            w.setDescription(weatherStatusApiDto.getDescription());
            weather.setWeather(w);
        }
        Temperature temperature = new Temperature();
        temperature.setTemp(weatherApiDto.getCurrent().getTemp());
        temperature.setFeelsLike(weatherApiDto.getCurrent().getFeelsLike());
        weather.setTemperature(temperature);
        weather.setVisibility(weatherApiDto.getCurrent().getVisibility());
        Wind wind = new Wind();
        wind.setSpeed(weatherApiDto.getCurrent().getWindSpeed());
        weather.setWind(wind);
        Sys sys = new Sys();
        sys.setSunrise(weatherApiDto.getCurrent().getSunrise());
        sys.setSunset(weatherApiDto.getCurrent().getSunset());
        weather.setSys(sys);
        return weather;
    }
}
