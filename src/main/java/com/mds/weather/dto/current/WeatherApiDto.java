package com.mds.weather.dto.current;

import com.mds.weather.dto.MainApiDto;

import java.util.ArrayList;
import java.util.List;

public class WeatherApiDto extends MainApiDto {

    private WeatherCoordApiDto coord;

    private List<WeatherStatusApiDto> weather = new ArrayList<>();

    private String base;

    private String name;

    private Integer timezone;

    private Long dt;

    private WeatherMainApiDto main;

    private Integer visibility;

    private WeatherSysApiDto sys;

    private WeatherWindApiDto wind;

    private WeatherRainApiDto rain;

    private WeatherCloudsApiDto clouds;

    private Integer cod;

    public WeatherCoordApiDto getWeatherCoord() {
        return coord;
    }

    public void setWeatherCoord(WeatherCoordApiDto weatherCoordApiDto) {
        this.coord = weatherCoordApiDto;
    }

    public List<WeatherStatusApiDto> getWeather() {
        return weather;
    }

    public void setWeather(List<WeatherStatusApiDto> weather) {
        this.weather = weather;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getTimezone() {
        return timezone;
    }

    public void setTimezone(Integer timezone) {
        this.timezone = timezone;
    }

    public Long getDt() {
        return dt;
    }

    public void setDt(Long dt) {
        this.dt = dt;
    }

    public WeatherMainApiDto getMain() {
        return main;
    }

    public void setMain(WeatherMainApiDto main) {
        this.main = main;
    }

    public Integer getVisibility() {
        return visibility;
    }

    public void setVisibility(Integer visibility) {
        this.visibility = visibility;
    }

    public WeatherSysApiDto getSys() {
        return sys;
    }

    public void setSys(WeatherSysApiDto sys) {
        this.sys = sys;
    }

    public WeatherWindApiDto getWind() {
        return wind;
    }

    public void setWind(WeatherWindApiDto wind) {
        this.wind = wind;
    }

    public Integer getCod() {
        return cod;
    }

    public void setCod(Integer cod) {
        this.cod = cod;
    }

    public WeatherCoordApiDto getCoord() {
        return coord;
    }

    public void setCoord(WeatherCoordApiDto coord) {
        this.coord = coord;
    }

    public WeatherRainApiDto getRain() {
        return rain;
    }

    public void setRain(WeatherRainApiDto rain) {
        this.rain = rain;
    }

    public WeatherCloudsApiDto getClouds() {
        return clouds;
    }

    public void setClouds(WeatherCloudsApiDto clouds) {
        this.clouds = clouds;
    }
}
