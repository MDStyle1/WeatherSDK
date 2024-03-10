package com.mds.weather.mapper;

import com.mds.weather.dto.current.WeatherStatusApiDto;
import com.mds.weather.dto.current.WeatherApiDto;
import com.mds.weather.model.*;

public class CurrentMapper implements MainMapper<Result, WeatherApiDto> {

    @Override
    public Class<WeatherApiDto> getClassDto() {
        return WeatherApiDto.class;
    }

    @Override
    public Result createFromApiDto(WeatherApiDto weatherApiDto) {
        Result weather = new Result();
        weather.setName(weatherApiDto.getName());
        weather.setDatetime(weatherApiDto.getDt());
        weather.setTimezone(weatherApiDto.getTimezone());
        if (!weatherApiDto.getWeather().isEmpty()) {
            WeatherStatusApiDto weatherStatusApiDto = weatherApiDto.getWeather().get(0);
            Weather w = new Weather();
            w.setMain(weatherStatusApiDto.getMain());
            w.setDescription(weatherStatusApiDto.getDescription());
            weather.setWeather(w);
        }
        Temperature temperature = new Temperature();
        temperature.setTemp(weatherApiDto.getMain().getTemp());
        temperature.setFeelsLike(weatherApiDto.getMain().getFeelsLike());
        weather.setTemperature(temperature);
        weather.setVisibility(weatherApiDto.getVisibility());
        Wind wind = new Wind();
        wind.setSpeed(weatherApiDto.getWind().getSpeed());
        weather.setWind(wind);
        Sys sys = new Sys();
        sys.setSunrise(weatherApiDto.getSys().getSunrise());
        sys.setSunset(weatherApiDto.getSys().getSunset());
        weather.setSys(sys);
        return weather;
    }
}
