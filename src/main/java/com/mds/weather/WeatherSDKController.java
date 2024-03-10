package com.mds.weather;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.ConfigurationWeatherSDKBuilder;
import com.mds.weather.exception.WeatherSDKInputException;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.local.DirectCache;
import com.mds.weather.local.DirectServiceLocal;
import com.mds.weather.local.WeatherCache;
import com.mds.weather.local.WeatherServiceLocal;
import com.mds.weather.remote.GeocodingServiceRemote;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Main class for initialization SDK
 * Stores and manages clients for work with weather api
 */
public final class WeatherSDKController {

    private final static ReentrantLock lock = new ReentrantLock();

    private final static Map<String, WeatherSDKClient> clients = new HashMap<>();

    private WeatherSDKController() {
    }

    /**
     * Getting what has already been created WeatherSDKClient by apiKey
     *
     * @param apiKey     The string api key from weather api
     * @return WeatherSDKClient from created clients
     */
    public static WeatherSDKClient getWeatherSDKClient(String apiKey) throws WeatherSDKThreadException, WeatherSDKInputException {
        if (apiKey == null || apiKey.replaceAll(" ", "").isEmpty()) {
            throw new WeatherSDKInputException("Api key can not be is null or empty");
        }
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try {
                    return clients.get(apiKey);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource SDK clients", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource SDK clients");
    }

    /**
     * Create new WeatherSDKClient by apiKey and default configuration for client
     *
     * @param apiKey     The string api key from weather api
     * @return new WeatherSDKClient
     */
    public static WeatherSDKClient createWeatherSDKClient(String apiKey) throws WeatherSDKInputException, WeatherSDKThreadException {
        return createWeatherSDKClient(apiKey, new ConfigurationWeatherSDKBuilder().build());
    }

    /**
     * Create new WeatherSDKClient by apiKey and configuration for client
     *
     * @param configurationWeatherSDK     The configuration for client
     * @param apiKey     The string api key from weather api
     * @return new WeatherSDKClient
     */
    public static WeatherSDKClient createWeatherSDKClient(String apiKey, ConfigurationWeatherSDK configurationWeatherSDK) throws WeatherSDKInputException, WeatherSDKThreadException {
        if (configurationWeatherSDK == null) {
            throw new WeatherSDKInputException("Configuration can not be is null");
        }
        if (apiKey == null || apiKey.replaceAll(" ", "").isEmpty()) {
            throw new WeatherSDKInputException("Api key can not be is null or empty");
        }
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try {
                    if (clients.containsKey(apiKey)) {
                        throw new WeatherSDKInputException("Weather SDK client with this api key already exist");
                    }
                    WeatherSDKClient weatherSDKClient = new WeatherSDKClient(getWeatherServiceLocal(apiKey, configurationWeatherSDK));
                    clients.put(apiKey, weatherSDKClient);
                    return weatherSDKClient;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource SDK clients", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource SDK clients");
    }

    /**
     * Remove and stop WeatherSDKClient by apiKey
     *
     * @param apiKey     The string api key from weather api
     */
    public static void removeClientByApiKey(String apiKey) throws WeatherSDKInputException, WeatherSDKThreadException {
        if (apiKey == null || apiKey.replaceAll(" ", "").isEmpty()) {
            throw new WeatherSDKInputException("Api key can not be is null or empty");
        }
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try {
                    WeatherSDKClient weatherSDKClient = clients.get(apiKey);
                    if (weatherSDKClient != null) {
                        clients.remove(apiKey);
                        weatherSDKClient.stopClient();
                    }
                    return;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource SDK clients", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource SDK clients");
    }

    /**
     * Remove and stop all WeatherSDKClient
     */
    public static void removeAllClients() throws WeatherSDKInputException, WeatherSDKThreadException {
        try {
            if (lock.tryLock(30, TimeUnit.SECONDS)) {
                try {
                    List<WeatherSDKClient> clientList = clients.values().stream().toList();
                    for (WeatherSDKClient weatherSDKClient : clientList) {
                        clients.remove(weatherSDKClient.getApiKey());
                        weatherSDKClient.stopClient();
                    }
                    return;
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource SDK clients", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource SDK clients");
    }

    private static GeocodingServiceRemote getGeocodingServiceRemote(ConfigurationWeatherSDK configurationWeatherSDK) {
        return new GeocodingServiceRemote(configurationWeatherSDK.getObjectMapper());
    }

    private static WeatherServiceLocal getWeatherServiceLocal(String apiKey, ConfigurationWeatherSDK configurationWeatherSDK) {
        return new WeatherServiceLocal(getDirectServiceLocal(apiKey, configurationWeatherSDK), configurationWeatherSDK.getWeatherServiceRemote(), getWeatherCache(configurationWeatherSDK), configurationWeatherSDK.getObjectMapper(), configurationWeatherSDK, apiKey);
    }

    private static DirectServiceLocal getDirectServiceLocal(String apiKey, ConfigurationWeatherSDK configurationWeatherSDK) {
        return new DirectServiceLocal(getGeocodingServiceRemote(configurationWeatherSDK), getDirectCache(configurationWeatherSDK), apiKey);
    }

    private static WeatherCache getWeatherCache(ConfigurationWeatherSDK configurationWeatherSDK) {
        return new WeatherCache(configurationWeatherSDK);
    }

    private static DirectCache getDirectCache(ConfigurationWeatherSDK configurationWeatherSDK) {
        return new DirectCache(configurationWeatherSDK);
    }
}
