package com.mds.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperature {

    private Float temp;

    @JsonProperty(value = "feels_like")
    private Float feelsLike;

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
}
