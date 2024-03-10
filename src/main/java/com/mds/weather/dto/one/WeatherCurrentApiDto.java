package com.mds.weather.dto.one;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class WeatherCurrentApiDto {

    private Long dt;

    private Long sunrise;

    private Long sunset;

    private Float temp;

    @JsonProperty(value = "feels_like")
    private Float feelsLike;

    private Integer visibility;

    @JsonProperty(value = "wind_speed")
    private Float windSpeed;

    private List<WeatherStatusApiDto> weather = new ArrayList<>();

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public Long getSunrise() {
        return sunrise;
    }

    public void setSunrise(Long sunrise) {
        this.sunrise = sunrise;
    }

    public Long getSunset() {
        return sunset;
    }

    public void setSunset(Long sunset) {
        this.sunset = sunset;
    }

    public Float getTemp() {
        return temp;
    }

    public void setTemp(Float temp) {
        this.temp = temp;
    }

    public Float getFeelsLike() {
        return feelsLike;
    }

    public void setFeelsLike(Float feelsLike) {
        this.feelsLike = feelsLike;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public Float getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(Float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public List<WeatherStatusApiDto> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherStatusApiDto> weather) {
        this.weather = weather;
    }
}
