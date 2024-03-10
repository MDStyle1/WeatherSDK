package com.mds.weather;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.ConfigurationWeatherSDKBuilder;
import com.mds.weather.config.RequestMode;
import com.mds.weather.dto.DirectApiDto;
import com.mds.weather.exception.*;
import com.mds.weather.local.DirectCache;
import com.mds.weather.local.DirectServiceLocal;
import com.mds.weather.local.WeatherCache;
import com.mds.weather.local.WeatherServiceLocal;
import com.mds.weather.mapper.MainMapper;
import com.mds.weather.model.Result;
import com.mds.weather.model.MainResult;
import com.mds.weather.model.Direct;
import com.mds.weather.model.Temperature;
import com.mds.weather.remote.GeocodingServiceRemote;
import com.mds.weather.remote.WeatherServiceRemote;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SDKTest {

    @Test
    public void testCreateClient() throws WeatherSDKInputException, WeatherSDKThreadException {
        String apiKey1 = "aassdd";
        String apiKey2 = null;
        String apiKey3 = "   ";
        WeatherSDKClient weatherSDKClient1 = WeatherSDKController.createWeatherSDKClient(apiKey1);
        assertNotNull(weatherSDKClient1);
        WeatherSDKClient foundedClient = WeatherSDKController.getWeatherSDKClient(apiKey1);
        assertNotNull(foundedClient);

        assertThrows(WeatherSDKInputException.class, () -> WeatherSDKController.createWeatherSDKClient(apiKey2));
        assertThrows(WeatherSDKInputException.class, () -> WeatherSDKController.createWeatherSDKClient(apiKey3));
        assertThrows(WeatherSDKInputException.class, () -> WeatherSDKController.createWeatherSDKClient(apiKey1));
    }

    @Test
    public void testRemoveClient() throws WeatherSDKInputException, WeatherSDKThreadException {
        String apiKey1 = "aassdd1";
        String apiKey2 = "aassdd2";
        String apiKey3 = "aassdd3";
        WeatherSDKClient weatherSDKClient1 = WeatherSDKController.createWeatherSDKClient(apiKey1);
        assertNotNull(weatherSDKClient1);
        WeatherSDKClient foundedClient = WeatherSDKController.getWeatherSDKClient(apiKey1);
        assertNotNull(foundedClient);

        WeatherSDKClient weatherSDKClient2 = WeatherSDKController.createWeatherSDKClient(apiKey2);
        assertNotNull(weatherSDKClient2);
        assertNotEquals(weatherSDKClient1, weatherSDKClient2);
        foundedClient = WeatherSDKController.getWeatherSDKClient(apiKey2);
        assertNotNull(foundedClient);
        assertEquals(foundedClient, weatherSDKClient2);

        WeatherSDKController.createWeatherSDKClient(apiKey3);

        assertNotNull(WeatherSDKController.getWeatherSDKClient(apiKey3));

        WeatherSDKController.removeClientByApiKey(apiKey2);
        assertNotNull(WeatherSDKController.getWeatherSDKClient(apiKey3));
        assertNotNull(WeatherSDKController.getWeatherSDKClient(apiKey1));
        assertNull(WeatherSDKController.getWeatherSDKClient(apiKey2));

        WeatherSDKController.removeAllClients();
        assertNull(WeatherSDKController.getWeatherSDKClient(apiKey3));
        assertNull(WeatherSDKController.getWeatherSDKClient(apiKey1));
        assertNull(WeatherSDKController.getWeatherSDKClient(apiKey2));
    }

    @Test
    public void testStopClient() throws WeatherSDKInputException, WeatherSDKThreadException {
        String apiKey1 = "firstKey";
        WeatherSDKClient weatherSDKClient1 = WeatherSDKController.createWeatherSDKClient(apiKey1);

        WeatherSDKController.removeClientByApiKey(apiKey1);

        assertThrows(WeatherSDKStatusException.class, () -> weatherSDKClient1.fetchCurrentWeatherByNameCity("test"));
    }

    @Test
    public void testFetchCurrentWeather() throws WeatherSDKException, IOException {
        String apiKey = "testKeyFetchCurrentWeather";

        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(10)
                .build();

        GeocodingServiceRemote geocodingServiceRemoteSpy = spy(new GeocodingServiceRemote(configurationWeatherSDK.getObjectMapper()));
        WeatherServiceRemote weatherServiceRemoteSpy = spy(configurationWeatherSDK.getWeatherServiceRemote());
        WeatherCache weatherCache = new WeatherCache(configurationWeatherSDK);
        DirectCache directCache = new DirectCache(configurationWeatherSDK);
        DirectServiceLocal directServiceLocal = new DirectServiceLocal(geocodingServiceRemoteSpy, directCache, apiKey);
        WeatherServiceLocal weatherServiceLocal = new WeatherServiceLocal(directServiceLocal, weatherServiceRemoteSpy, weatherCache, configurationWeatherSDK.getObjectMapper(), configurationWeatherSDK, apiKey);

        WeatherSDKClient weatherSDKClient = new WeatherSDKClient(weatherServiceLocal);

        DirectApiDto directApiDto1 = new DirectApiDto();
        directApiDto1.setName("London");
        directApiDto1.setLon(1.1f);
        directApiDto1.setLon(1.1f);
        doReturn(directApiDto1).when(geocodingServiceRemoteSpy).fetchDirectByNameAndApiKey(directApiDto1.getName(), apiKey);

        Result currentWeather1 = new Result();
        currentWeather1.setName(directApiDto1.getName());
        currentWeather1.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setTimezone(0);
        doReturn(currentWeather1).when(weatherServiceRemoteSpy).fetchCurrentWeatherByDirectAndApiKey(directServiceLocal.getDirectFromCache(directApiDto1.getName()), apiKey);

        String result1 = weatherSDKClient.fetchCurrentWeatherByNameCity(directApiDto1.getName());

        Result resultWeather1 = configurationWeatherSDK.getObjectMapper().readValue(result1, Result.class);

        assertEquals(currentWeather1.getName(), resultWeather1.getName());
        assertEquals(currentWeather1.getDatetime(), resultWeather1.getDatetime());

        DirectApiDto directApiDto2 = new DirectApiDto();
        directApiDto2.setName("Madrid");
        directApiDto2.setLon(2.1f);
        directApiDto2.setLon(2.1f);
        doReturn(directApiDto2).when(geocodingServiceRemoteSpy).fetchDirectByNameAndApiKey(directApiDto2.getName(), apiKey);

        Result currentWeather2 = new Result();
        currentWeather2.setName(directApiDto2.getName());
        currentWeather2.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setTimezone(0);
        doReturn(currentWeather2).when(weatherServiceRemoteSpy).fetchCurrentWeatherByDirectAndApiKey(directServiceLocal.getDirectFromCache(directApiDto2.getName()), apiKey);

        String result2 = weatherSDKClient.fetchCurrentWeatherByNameCity(directApiDto2.getName());

        Result resultWeather2 = configurationWeatherSDK.getObjectMapper().readValue(result2, Result.class);

        assertEquals(currentWeather2.getName(), resultWeather2.getName());
        assertEquals(currentWeather2.getDatetime(), resultWeather2.getDatetime());
    }

    @Test
    public void testPollingMode() throws WeatherSDKException, IOException, InterruptedException {
        String apiKey = "testKeyPollingMode";

        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setTimeMinutesExpiredWeather(1)
                .setRequestMode(RequestMode.POLLING_MODE)
                .build();

        GeocodingServiceRemote geocodingServiceRemoteSpy = spy(new GeocodingServiceRemote(configurationWeatherSDK.getObjectMapper()));
        WeatherServiceRemote weatherServiceRemoteSpy = spy(configurationWeatherSDK.getWeatherServiceRemote());
        WeatherCache weatherCache = new WeatherCache(configurationWeatherSDK);
        DirectCache directCache = new DirectCache(configurationWeatherSDK);
        DirectServiceLocal directServiceLocal = new DirectServiceLocal(geocodingServiceRemoteSpy, directCache, apiKey);
        WeatherServiceLocal weatherServiceLocal = new WeatherServiceLocal(directServiceLocal, weatherServiceRemoteSpy, weatherCache, configurationWeatherSDK.getObjectMapper(), configurationWeatherSDK, apiKey);

        WeatherSDKClient weatherSDKClient = new WeatherSDKClient(weatherServiceLocal);

        DirectApiDto directApiDto1 = new DirectApiDto();
        directApiDto1.setName("London");
        directApiDto1.setLon(1.1f);
        directApiDto1.setLon(1.1f);
        doReturn(directApiDto1).when(geocodingServiceRemoteSpy).fetchDirectByNameAndApiKey(directApiDto1.getName(), apiKey);

        Result currentWeather1 = new Result();
        currentWeather1.setName(directApiDto1.getName());
        currentWeather1.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setTimezone(0);
        doReturn(currentWeather1).when(weatherServiceRemoteSpy).fetchCurrentWeatherByDirectAndApiKey(directServiceLocal.getDirectFromCache(directApiDto1.getName()), apiKey);

        String result1 = weatherSDKClient.fetchCurrentWeatherByNameCity(directApiDto1.getName());

        Result resultWeather1 = configurationWeatherSDK.getObjectMapper().readValue(result1, Result.class);

        assertEquals(currentWeather1.getName(), resultWeather1.getName());
        assertEquals(currentWeather1.getDatetime(), resultWeather1.getDatetime());

        currentWeather1.setDatetime(LocalDateTime.now().plusHours(1).toEpochSecond(ZoneOffset.UTC));
        currentWeather1.setDatetimeCreate(LocalDateTime.now().plusHours(1).toEpochSecond(ZoneOffset.UTC));

        Result currentWeather2 = new Result();
        currentWeather2.setName(directApiDto1.getName());
        currentWeather2.setDatetime(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        currentWeather2.setTimezone(0);
        doReturn(currentWeather2).when(weatherServiceRemoteSpy).fetchCurrentWeatherByDirectAndApiKey(directServiceLocal.getDirectFromCache(directApiDto1.getName()), apiKey);

        result1 = weatherSDKClient.fetchCurrentWeatherByNameCity(directApiDto1.getName());

        resultWeather1 = configurationWeatherSDK.getObjectMapper().readValue(result1, Result.class);

        assertEquals(currentWeather1.getName(), resultWeather1.getName());
        assertEquals(currentWeather1.getDatetime(), resultWeather1.getDatetime());

        Thread.sleep(35 * 1000);

        result1 = weatherSDKClient.fetchCurrentWeatherByNameCity(directApiDto1.getName());

        resultWeather1 = configurationWeatherSDK.getObjectMapper().readValue(result1, Result.class);

        assertEquals(currentWeather1.getName(), resultWeather1.getName());
        assertEquals(currentWeather2.getDatetime(), resultWeather1.getDatetime());
    }
}
