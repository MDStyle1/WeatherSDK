package com.mds.weather.dto.one;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mds.weather.dto.MainApiDto;

public class WeatherApiDto extends MainApiDto {

    private String timezone;

    @JsonProperty(value = "timezone_offset")
    private Integer timezoneOffset;

    private WeatherCurrentApiDto current;

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Integer getTimezoneOffset() {
        return timezoneOffset;
    }

    public void setTimezoneOffset(Integer timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public WeatherCurrentApiDto getCurrent() {
        return current;
    }

    public void setCurrent(WeatherCurrentApiDto current) {
        this.current = current;
    }
}
