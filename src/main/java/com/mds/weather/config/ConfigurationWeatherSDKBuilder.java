package com.mds.weather.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.mapper.MainMapper;
import com.mds.weather.mapper.CurrentMapper;
import com.mds.weather.mapper.OneCallMapper;
import com.mds.weather.remote.WeatherServiceRemote;

/**
 * Object for create configuration
 */
public final class ConfigurationWeatherSDKBuilder {

    private ObjectMapper objectMapper = new ObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    /**
     * Type work SDK client
     */
    private RequestMode requestMode = RequestMode.ON_DEMAND_MODE;

    /**
     * Type weather api
     */
    private WeatherApi weatherApi = WeatherApi.CURRENT_WEATHER_API;

    /**
     * Time expired data weather in minutes
     *
     * 0 for off
     */
    private Integer timeMinutesExpiredWeather = 10;

    /**
     * Cache size
     *
     * 0 for off
     */
    private Integer cacheSize = 10;

    /**
     * Mapper for dto to model
     */
    private MainMapper mainMapper = null;

    /**
     * Service for request to Api
     */
    private WeatherServiceRemote weatherServiceRemote = null;

    /**
     * Url for api
     */
    private String weatherApiUrl = null;

    /**
     * Method for create configuration with stored parameters
     * @return Object Configuration
     */
    public ConfigurationWeatherSDK build() {
        updateWeatherMapper();
        updateWeatherApiUrl();
        updateWeatherServiceRemote();
        return new ConfigurationWeatherSDK(objectMapper, requestMode, weatherApi, timeMinutesExpiredWeather, cacheSize, mainMapper, weatherServiceRemote, weatherApiUrl);
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public ConfigurationWeatherSDKBuilder setObjectMapper(ObjectMapper objectMapper) {
        if (objectMapper != null)
        this.objectMapper = objectMapper;
        return this;
    }

    public RequestMode getRequestMode() {
        return requestMode;
    }

    public ConfigurationWeatherSDKBuilder setRequestMode(RequestMode requestMode) {
        if (requestMode != null) this.requestMode = requestMode;
        return this;
    }

    public WeatherApi getWeatherApi() {
        return weatherApi;
    }

    public ConfigurationWeatherSDKBuilder setWeatherApi(WeatherApi weatherApi) {
        if (weatherApi != null) this.weatherApi = weatherApi;
        return this;
    }

    public Integer getTimeMinutesExpiredWeather() {
        return timeMinutesExpiredWeather;
    }

    public ConfigurationWeatherSDKBuilder setTimeMinutesExpiredWeather(Integer timeMinutesExpiredWeather) {
        if (timeMinutesExpiredWeather != null && timeMinutesExpiredWeather >= 0 && timeMinutesExpiredWeather <= 10080)
            this.timeMinutesExpiredWeather = timeMinutesExpiredWeather;
        return this;
    }

    public Integer getCacheSize() {
        return cacheSize;
    }

    public ConfigurationWeatherSDKBuilder setCacheSize(Integer cacheSize) {
        if (cacheSize != null && cacheSize >= 0)
            this.cacheSize = cacheSize;
        return this;
    }

    public MainMapper getMainMapper() {
        return mainMapper;
    }

    public ConfigurationWeatherSDKBuilder setMainMapper(MainMapper mainMapper) {
        if (mainMapper != null)
        this.mainMapper = mainMapper;
        return this;
    }

    public WeatherServiceRemote getWeatherServiceRemote() {
        return weatherServiceRemote;
    }

    public ConfigurationWeatherSDKBuilder setWeatherServiceRemote(WeatherServiceRemote weatherServiceRemote) {
        if (weatherServiceRemote != null)
            this.weatherServiceRemote = weatherServiceRemote;
        return this;
    }

    public String getWeatherApiUrl() {
        return weatherApiUrl;
    }

    public ConfigurationWeatherSDKBuilder setWeatherApiUrl(String weatherApiUrl) {
        if (weatherApiUrl != null)
            this.weatherApiUrl = weatherApiUrl;
        return this;
    }

    private void updateWeatherMapper() {
        if (mainMapper == null) {
            mainMapper = switch (weatherApi) {
                case ONE_CALL_API -> new OneCallMapper();
                default -> new CurrentMapper();
            };
        }
    }

    private void updateWeatherApiUrl() {
        if (weatherApiUrl == null) {
            weatherApiUrl = switch (weatherApi) {
                case ONE_CALL_API -> "https://api.openweathermap.org/data/3.0/onecall";
                default -> "https://api.openweathermap.org/data/2.5/weather";
            };
        }
    }

    private void updateWeatherServiceRemote() {
        if (weatherServiceRemote == null) {
            weatherServiceRemote = new WeatherServiceRemote(weatherApiUrl, objectMapper, mainMapper);
        }
    }
}
