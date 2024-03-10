package com.mds.weather.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class DirectApiDto {

    private String name;

    @JsonProperty(value = "local_names")
    private Map<String, String> localNames = new HashMap<>();

    private Float lat;

    private Float lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getLocalNames() {
        return localNames;
    }

    public void setLocalNames(Map<String, String> localNames) {
        this.localNames = localNames;
    }

    public Float getLat() {
        return lat;
    }

    public void setLat(Float lat) {
        this.lat = lat;
    }

    public Float getLon() {
        return lon;
    }

    public void setLon(Float lon) {
        this.lon = lon;
    }
}
