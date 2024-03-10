package com.mds.weather.local;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.model.MainResult;
import com.mds.weather.model.Direct;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class WeatherCache {

    /**
     * Cache size
     */
    private Integer size = 10;

    /**
     * Time expired result weather in minutes
     */
    private Integer expiredTime = 10;

    private final Queue<Direct> weatherKeyQueue = new ArrayDeque<>();

    private final Map<Direct, MainResult> cache = new HashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final ConcurrentHashMap<Direct, Future<MainResult>> orderRequest = new ConcurrentHashMap<>();

    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    public WeatherCache(ConfigurationWeatherSDK configurationWeatherSDK) {
        size = configurationWeatherSDK.getCacheSize();
        expiredTime = configurationWeatherSDK.getTimeMinutesExpiredWeather();
    }

    /**
     * Getting current weather from cache by location information
     *
     * @param direct location information
     * @return object current weather
     */
    public MainResult getCurrentWeatherByDirect(Direct direct) throws WeatherSDKThreadException {
        if (size == 0) {
            return null;
        }
        try {
            if (readWriteLock.readLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    MainResult weather = cache.get(direct);
                    if (weather == null) {
                        return null;
                    }
                    //TODO weather.getDatetime() different from the request time maybe need use this time
                    if (expiredTime != 0 && weather.getDatetimeCreate() <= LocalDateTime.now().minusMinutes(expiredTime).toEpochSecond(ZoneOffset.UTC)) {
                        return null;
                    }
                    return weather;
                } finally {
                    readWriteLock.readLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource weather cache", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource weather cache");
    }

    /**
     * Update current weather in cache
     *
     * @param direct location information
     * @param currentWeather
     */
    public void putCurrentWeatherByDirect(Direct direct, MainResult currentWeather) throws WeatherSDKThreadException {
        if (size == 0) {
            return;
        }
        if (isStopped.get()) {
            return;
        }
        try {
            if (readWriteLock.writeLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    if (cache.containsKey(direct)) {
                        cache.replace(direct, currentWeather);
                    } else {
                        if (weatherKeyQueue.size() >= size) {
                            cache.remove(weatherKeyQueue.poll());
                        }
                        weatherKeyQueue.add(direct);
                        cache.put(direct, currentWeather);
                    }
                    return;
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource weather cache", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource weather cache");
    }

    public ConcurrentHashMap<Direct, Future<MainResult>> getOrderRequest() {
        return orderRequest;
    }

    /**
     * Getting all locations from cache weather for update polling
     *
     * @return list current locations for update
     */
    public List<Direct> getAllDirect() throws WeatherSDKThreadException {
        if (size == 0) {
            return new ArrayList<>();
        }
        try {
            if (readWriteLock.readLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    return cache.keySet().stream().toList();
                } finally {
                    readWriteLock.readLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource weather cache", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource weather cache");
    }

    /**
     * Procedure stop client and clean cache
     */
    public void stopCache() throws WeatherSDKThreadException {
        isStopped.set(true);
        try {
            if (readWriteLock.writeLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    cache.clear();
                    weatherKeyQueue.clear();
                    return;
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource weather cache", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource weather cache");
    }
}
