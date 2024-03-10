package com.mds.weather.dto.current;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WeatherRainApiDto {

    @JsonProperty(value = "1h")
    private Float oneHour;

    public Float getOneHour() {
        return oneHour;
    }

    public void setOneHour(Float oneHour) {
        this.oneHour = oneHour;
    }
}
