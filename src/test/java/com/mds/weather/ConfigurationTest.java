package com.mds.weather;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.ConfigurationWeatherSDKBuilder;
import com.mds.weather.config.RequestMode;
import com.mds.weather.config.WeatherApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigurationTest {

    @Test
    public void testBuildConfigurationCacheSize() {
        Integer cacheSize = 2;
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(cacheSize)
                .build();

        assertEquals(cacheSize, configurationWeatherSDK.getCacheSize());

        cacheSize = 0;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(cacheSize)
                .build();

        assertEquals(cacheSize, configurationWeatherSDK.getCacheSize());

        cacheSize = -2;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(cacheSize)
                .build();

        assertNotEquals(cacheSize, configurationWeatherSDK.getCacheSize());
    }

    @Test
    public void testBuildConfigurationExpiredTime() {
        Integer expiredTime = 2;
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(expiredTime)
                .build();

        assertEquals(expiredTime, configurationWeatherSDK.getTimeMinutesExpiredWeather());

        expiredTime = 0;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(expiredTime)
                .build();

        assertEquals(expiredTime, configurationWeatherSDK.getTimeMinutesExpiredWeather());

        expiredTime = -2;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(expiredTime)
                .build();

        assertNotEquals(expiredTime, configurationWeatherSDK.getTimeMinutesExpiredWeather());

        expiredTime = 11000;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(expiredTime)
                .build();

        assertNotEquals(expiredTime, configurationWeatherSDK.getTimeMinutesExpiredWeather());
    }

    @Test
    public void testBuildConfigurationWeatherApi() {
        WeatherApi weatherApi = WeatherApi.CURRENT_WEATHER_API;
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setWeatherApi(weatherApi)
                .build();

        assertEquals(weatherApi, configurationWeatherSDK.getWeatherApi());

        weatherApi = WeatherApi.ONE_CALL_API;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setWeatherApi(weatherApi)
                .build();

        assertEquals(weatherApi, configurationWeatherSDK.getWeatherApi());
    }

    @Test
    public void testBuildConfigurationRequestMode() {
        RequestMode requestMode = RequestMode.ON_DEMAND_MODE;
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setRequestMode(requestMode)
                .build();

        assertEquals(requestMode, configurationWeatherSDK.getRequestMode());

        requestMode = RequestMode.POLLING_MODE;
        configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setRequestMode(requestMode)
                .build();

        assertEquals(requestMode, configurationWeatherSDK.getRequestMode());
    }
}
