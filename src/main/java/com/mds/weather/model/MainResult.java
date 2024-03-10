package com.mds.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class MainResult {

    private Long datetime;

    @JsonIgnore
    private Long datetimeCreate;

    private String name;

    public Long getDatetime() {
        return datetime;
    }

    public void setDatetime(Long datetime) {
        this.datetime = datetime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getDatetimeCreate() {
        return datetimeCreate;
    }

    public void setDatetimeCreate(Long datetimeCreate) {
        this.datetimeCreate = datetimeCreate;
    }
}
