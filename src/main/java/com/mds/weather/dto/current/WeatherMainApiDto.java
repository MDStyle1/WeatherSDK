package com.mds.weather.dto.current;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherMainApiDto {

    private Float temp;

    @JsonProperty(value = "feels_like")
    private Float feelsLike;

    @JsonProperty(value = "temp_min")
    private Float tempMin;

    @JsonProperty(value = "temp_max")
    private Float tempMax;

    private Integer pressure;

    private Integer humidity;

    @JsonProperty(value = "grnd_level")
    private Integer grndLevel;

    @JsonProperty(value = "sea_level")
    private Integer seaLevel;

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

    public Float getTempMin() {
        return tempMin;
    }

    public void setTempMin(Float tempMin) {
        this.tempMin = tempMin;
    }

    public Float getTempMax() {
        return tempMax;
    }

    public void setTempMax(Float tempMax) {
        this.tempMax = tempMax;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getGrndLevel() {
        return grndLevel;
    }

    public void setGrndLevel(Integer grndLevel) {
        this.grndLevel = grndLevel;
    }

    public Integer getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(Integer seaLevel) {
        this.seaLevel = seaLevel;
    }
}
