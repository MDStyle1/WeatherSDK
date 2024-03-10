package com.mds.weather;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.exception.WeatherSDKException;
import com.mds.weather.exception.WeatherSDKInputException;
import com.mds.weather.local.WeatherServiceLocal;

/**
 * Object client SDK for work with weather api.
 * There is a possibility of separate creation.
 * Better use with WeatherSDKController
 */
public final class WeatherSDKClient {

    private final WeatherServiceLocal weatherServiceLocal;

    public WeatherSDKClient(WeatherServiceLocal weatherServiceLocal) {
        this.weatherServiceLocal = weatherServiceLocal;
    }

    /**
     * Request current weather by name city
     *
     * @param nameCity
     * @return Json object current weather
     */
    public String fetchCurrentWeatherByNameCity(String nameCity) throws WeatherSDKException {
        if (nameCity == null || nameCity.isEmpty()) {
            throw new WeatherSDKInputException("Name city can not be empty or is null");
        }
        return weatherServiceLocal.fetchCurrentWeatherByNameCity(nameCity);
    }

    void stopClient() {
        weatherServiceLocal.stopService();
    }

    /**
     * Request api key from this client
     *
     * @return api key
     */
    public String getApiKey() {
        return weatherServiceLocal.getApiKey();
    }

    /**
     * Request current configuration
     *
     * @return current configuration
     */
    public ConfigurationWeatherSDK getConfigurationWeatherSDK() {
        return weatherServiceLocal.getConfigurationWeatherSDK();
    }
}
