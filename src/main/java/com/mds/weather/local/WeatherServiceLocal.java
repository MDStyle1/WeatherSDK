package com.mds.weather.local;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.config.RequestMode;
import com.mds.weather.exception.*;
import com.mds.weather.model.*;
import com.mds.weather.remote.WeatherServiceRemote;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class WeatherServiceLocal {

    private final DirectServiceLocal directServiceLocal;

    private final WeatherServiceRemote weatherServiceRemote;

    private final WeatherCache weatherCache;

    private final ObjectMapper objectMapper;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ConfigurationWeatherSDK configurationWeatherSDK;

    private final String apiKey;

    private ScheduledExecutorService scheduler;

    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    private Integer delaySeconds;

    public WeatherServiceLocal(DirectServiceLocal directServiceLocal,
                               WeatherServiceRemote weatherServiceRemote,
                               WeatherCache weatherCache,
                               ObjectMapper objectMapper,
                               ConfigurationWeatherSDK configurationWeatherSDK,
                               String apiKey) {
        this.directServiceLocal = directServiceLocal;
        this.weatherServiceRemote = weatherServiceRemote;
        this.weatherCache = weatherCache;
        this.objectMapper = objectMapper;
        this.configurationWeatherSDK = configurationWeatherSDK;
        this.apiKey = apiKey;
        if (configurationWeatherSDK.getRequestMode().equals(RequestMode.POLLING_MODE)) {
            if (configurationWeatherSDK.getCacheSize() != 0) {
                scheduler = Executors.newSingleThreadScheduledExecutor();
                delaySeconds = configurationWeatherSDK.getTimeMinutesExpiredWeather() * 30;
                scheduler.schedule(this::updateWeatherByScheduler, delaySeconds, TimeUnit.SECONDS);
            }
        } else {
            scheduler = null;
        }
    }

    /**
     * Request current weather by name city
     *
     * @param name name city
     * @return Json object current weather
     */
    public String fetchCurrentWeatherByNameCity(String name) throws WeatherSDKException {
        if (isStopped.get()) {
            throw new WeatherSDKStatusException("Client is stopped");
        }
        Direct direct = directServiceLocal.getDirectFromCache(name);
        if (direct == null) {
            return null;
        }
        MainResult weather = weatherCache.getCurrentWeatherByDirect(direct);
        if (weather == null) {
            weather = getWeatherByDirectFromQueue(direct);
        }
        try {
           return objectMapper.writeValueAsString(weather);
        } catch (JsonProcessingException e) {
            throw new WeatherSDKMapperException("Error read result for response", e.getCause());
        }
    }

    /**
     * Pre request current weather by direct from the queue
     *
     * @param direct location information
     * @return Current weather
     */
    private <W extends MainResult> W getWeatherByDirectFromQueue(Direct direct) throws WeatherSDKException {
        Future<MainResult> future = weatherCache.getOrderRequest().computeIfAbsent(direct, k -> executorService.submit(() -> requestWeatherByDirectAndApiKey(direct)));
        try {
            MainResult weather = future.get();
            weatherCache.getOrderRequest().remove(direct);
            return (W) weather;
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource order request for weather", e.getCause());
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof WeatherSDKException exception) {
                throw exception;
            }
            throw new WeatherSDKThreadException("Error accessing the resource order request for weather", e.getCause());
        }
    }

    /**
     * Request current weather by direct from weather api
     *
     * @param direct location information
     * @return Current weather
     */
    private MainResult requestWeatherByDirectAndApiKey(Direct direct) throws WeatherSDKMapperException, WeatherSDKHttpException, WeatherSDKThreadException {
        MainResult weather = weatherServiceRemote.fetchCurrentWeatherByDirectAndApiKey(direct, apiKey);
        if (weather == null) {
            return null;
        }
        weather.setDatetimeCreate(LocalDateTime.now().toEpochSecond(ZoneOffset.UTC));
        weatherCache.putCurrentWeatherByDirect(direct, weather);
        return weather;
    }

    /**
     * Request current configuration
     *
     * @return current configuration
     */
    public ConfigurationWeatherSDK getConfigurationWeatherSDK() {
        return configurationWeatherSDK;
    }

    /**
     * Getting api key client
     *
     * @return Api key
     */
    public String getApiKey() {
        return apiKey;
    }

    /**
     * Method for polling mode request
     */
    private void updateWeatherByScheduler() {
        if (isStopped.get()) {
            return;
        }
        try {
            for (Direct direct : weatherCache.getAllDirect()) {
                try {
                    getWeatherByDirectFromQueue(direct);
                } catch (WeatherSDKException e) {
                    e.printStackTrace();
                }
            }
        } catch (WeatherSDKThreadException e) {
            e.printStackTrace();
        } finally {
            scheduler.schedule(this::updateWeatherByScheduler, delaySeconds, TimeUnit.SECONDS);
        }
    }

    /**
     * Procedure stop client
     */
    public void stopService() {
        isStopped.set(true);
        if (configurationWeatherSDK.getRequestMode().equals(RequestMode.POLLING_MODE)) {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        }
        try {
            weatherCache.stopCache();
        } catch (WeatherSDKThreadException e) {
        }
        try {
            directServiceLocal.stop();
        } catch (WeatherSDKThreadException e) {
        }
    }
}
