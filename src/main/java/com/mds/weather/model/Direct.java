package com.mds.weather.model;


import java.util.Objects;

public class Direct {

    private String name;

    private Float lat;

    private Float lon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Direct direct = (Direct) o;
        return Objects.equals(name, direct.name) && Objects.equals(lat, direct.lat) && Objects.equals(lon, direct.lon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, lat, lon);
    }
}
