package com.mds.weather.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.mapper.MainMapper;
import com.mds.weather.remote.WeatherServiceRemote;

/**
 * Object configuration client sdk
 * Produce in ConfigurationWeatherSDKBuilder
 */
public final class ConfigurationWeatherSDK {

    private final ObjectMapper objectMapper;

    /**
     * Type work SDK client
     */
    private final RequestMode requestMode;

    /**
     * Type weather api
     */
    private final WeatherApi weatherApi;

    /**
     * Time expired data weather in minutes
     *
     * 0 for off
     */
    private final Integer timeMinutesExpiredWeather;

    /**
     * Cache size
     *
     * 0 for off
     */
    private final Integer cacheSize;

    /**
     * Mapper for dto to model
     */
    private final MainMapper mainMapper;

    /**
     * Service for request to Api
     */
    private final WeatherServiceRemote weatherServiceRemote;

    /**
     * Url for api
     */
    private final String weatherApiUrl;

    protected ConfigurationWeatherSDK(ObjectMapper objectMapper,
                                      RequestMode requestMode,
                                      WeatherApi weatherApi,
                                      Integer timeMinutesExpiredWeather,
                                      Integer cacheSize,
                                      MainMapper mainMapper,
                                      WeatherServiceRemote weatherServiceRemote,
                                      String weatherApiUrl) {
        this.objectMapper = objectMapper;
        this.requestMode = requestMode;
        this.weatherApi = weatherApi;
        this.timeMinutesExpiredWeather = timeMinutesExpiredWeather;
        this.cacheSize = cacheSize;
        this.mainMapper = mainMapper;
        this.weatherServiceRemote = weatherServiceRemote;
        this.weatherApiUrl = weatherApiUrl;
    }

    public RequestMode getRequestMode() {
        return requestMode;
    }

    public WeatherApi getWeatherApi() {
        return weatherApi;
    }

    public Integer getTimeMinutesExpiredWeather() {
        return timeMinutesExpiredWeather;
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public MainMapper getMainMapper() {
        return mainMapper;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public WeatherServiceRemote getWeatherServiceRemote() {
        return weatherServiceRemote;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }
}
