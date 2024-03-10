package com.mds.weather;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.ConfigurationWeatherSDKBuilder;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.local.WeatherCache;
import com.mds.weather.model.Result;
import com.mds.weather.model.Direct;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static org.junit.jupiter.api.Assertions.*;

public class WeatherCacheTest {

    @Test
    public void testWeatherCacheSize() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(2)
                .setTimeMinutesExpiredWeather(10)
                .build();
        WeatherCache weatherCache = new WeatherCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        Result currentWeather1 = new Result();
        currentWeather1.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setName(direct1.getName());
        addToWeatherCache(weatherCache, direct1, currentWeather1);

        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct1));

        Direct direct2 = new Direct();
        direct2.setName("direct2");
        Result currentWeather2 = new Result();
        currentWeather2.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setName(direct2.getName());

        assertNotEquals(direct1, direct2);
        assertNotEquals(currentWeather1, currentWeather2);

        addToWeatherCache(weatherCache, direct2, currentWeather2);

        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct2));
        assertEquals(weatherCache.getCurrentWeatherByDirect(direct2), currentWeather2);

        Direct direct3 = new Direct();
        direct3.setName("direct3");
        Result currentWeather3 = new Result();
        currentWeather3.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather3.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather3.setName(direct3.getName());

        assertNotEquals(direct1, direct3);
        assertNotEquals(direct2, direct3);
        assertNotEquals(currentWeather1, currentWeather3);
        assertNotEquals(currentWeather2, currentWeather3);

        addToWeatherCache(weatherCache, direct3, currentWeather3);

        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct2));
        assertEquals(weatherCache.getCurrentWeatherByDirect(direct2), currentWeather2);
        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct3));
        assertEquals(weatherCache.getCurrentWeatherByDirect(direct3), currentWeather3);
        assertNull(weatherCache.getCurrentWeatherByDirect(direct1));
    }

    @Test
    public void testWeatherCacheSize0() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(0)
                .setTimeMinutesExpiredWeather(10)
                .build();
        WeatherCache weatherCache = new WeatherCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        Result currentWeather1 = new Result();
        currentWeather1.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setName(direct1.getName());
        addToWeatherCache(weatherCache, direct1, currentWeather1);

        assertNull(weatherCache.getCurrentWeatherByDirect(direct1));
    }

    @Test
    public void testWeatherCacheTimeExpired() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(2)
                .setTimeMinutesExpiredWeather(4)
                .build();
        WeatherCache weatherCache = new WeatherCache(configurationWeatherSDK);

        Long timeNotExpired = LocalDateTime.now().minusMinutes(1).toEpochSecond(ZoneOffset.UTC);
        Long timeExpired = LocalDateTime.now().minusMinutes(5).toEpochSecond(ZoneOffset.UTC);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        Result currentWeather1 = new Result();
        currentWeather1.setDatetime(timeNotExpired);
        currentWeather1.setDatetimeCreate(timeNotExpired);
        currentWeather1.setName(direct1.getName());
        addToWeatherCache(weatherCache, direct1, currentWeather1);

        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct1));

        Direct direct2 = new Direct();
        direct2.setName("direct2");
        Result currentWeather2 = new Result();
        currentWeather2.setDatetime(timeExpired);
        currentWeather2.setDatetimeCreate(timeExpired);
        currentWeather2.setName(direct2.getName());

        assertNotEquals(direct1, direct2);
        assertNotEquals(currentWeather1, currentWeather2);

        addToWeatherCache(weatherCache, direct2, currentWeather2);

        assertNotNull(weatherCache.getCurrentWeatherByDirect(direct1));
        assertNull(weatherCache.getCurrentWeatherByDirect(direct2));
        assertEquals(weatherCache.getCurrentWeatherByDirect(direct1), currentWeather1);
    }

    private void addToWeatherCache(WeatherCache weatherCache, Direct direct, Result currentWeather) throws WeatherSDKThreadException {
        weatherCache.putCurrentWeatherByDirect(direct, currentWeather);
    }
}
