package com.mds.weather;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.ConfigurationWeatherSDKBuilder;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.local.DirectCache;
import com.mds.weather.model.Direct;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DirectCacheTest {

    @Test
    public void testDirectCacheSize() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(2)
                .build();
        DirectCache directCache = new DirectCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        addToDirectCache(directCache, direct1);

        assertNotNull(directCache.getDirectByName(direct1.getName()));

        Direct direct2 = new Direct();
        direct2.setName("direct2");
        assertNotEquals(direct1, direct2);
        addToDirectCache(directCache, direct2);

        assertNotNull(directCache.getDirectByName(direct1.getName()));
        assertEquals(directCache.getDirectByName(direct1.getName()), direct1);
        assertNotNull(directCache.getDirectByName(direct2.getName()));
        assertEquals(directCache.getDirectByName(direct2.getName()), direct2);

        Direct direct3 = new Direct();
        direct3.setName("direct3");
        assertNotEquals(direct1, direct3);
        assertNotEquals(direct2, direct3);
        addToDirectCache(directCache, direct3);

        assertNotNull(directCache.getDirectByName(direct3.getName()));
        assertEquals(directCache.getDirectByName(direct3.getName()), direct3);
        assertNotNull(directCache.getDirectByName(direct2.getName()));
        assertEquals(directCache.getDirectByName(direct2.getName()), direct2);
        assertNull(directCache.getDirectByName(direct1.getName()));
    }

    @Test
    public void testDirectCacheSize0() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(0)
                .build();
        DirectCache directCache = new DirectCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        addToDirectCache(directCache, direct1);

        assertNull(directCache.getDirectByName(direct1.getName()));
    }

    @Test
    public void testDirectCacheFindName() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(2)
                .build();
        DirectCache directCache = new DirectCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        Direct direct2 = new Direct();
        direct2.setName("direct2");
        assertNotEquals(direct1, direct2);
        String findName = "Dir1";
        addToDirectCache(directCache, direct1, findName);
        addToDirectCache(directCache, direct2);

        assertNotNull(directCache.getDirectByName(findName));
        assertNotEquals(directCache.getDirectByName(findName), direct2);
    }

    @Test
    public void testDirectCacheNames() throws WeatherSDKThreadException {
        ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
                .setCacheSize(2)
                .build();
        DirectCache directCache = new DirectCache(configurationWeatherSDK);

        Direct direct1 = new Direct();
        direct1.setName("direct1");
        Direct direct2 = new Direct();
        direct2.setName("direct2");
        assertNotEquals(direct1, direct2);
        List<String> names = List.of("d1", "dir1", "dd1");
        addToDirectCache(directCache, direct1, names);
        addToDirectCache(directCache, direct2);

        for (String name : names) {
            assertNotNull(directCache.getDirectByName(name));
            assertNotEquals(directCache.getDirectByName(name), direct2);
        }
    }

    private void addToDirectCache(DirectCache directCache, Direct direct) throws WeatherSDKThreadException {
        addToDirectCache(directCache, direct, new ArrayList<>(), direct.getName());
    }

    private void addToDirectCache(DirectCache directCache, Direct direct, String findName) throws WeatherSDKThreadException {
        addToDirectCache(directCache, direct, new ArrayList<>(), findName);
    }

    private void addToDirectCache(DirectCache directCache, Direct direct, List<String> names) throws WeatherSDKThreadException {
        addToDirectCache(directCache, direct, names, direct.getName());
    }

    private void addToDirectCache(DirectCache directCache, Direct direct, List<String> names, String findName) throws WeatherSDKThreadException {
        directCache.putDirectAndNamesByName(direct.getName(), direct, names, findName);
    }
}
