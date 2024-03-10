package com.mds.weather.local;

import com.mds.weather.config.ConfigurationWeatherSDK;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.model.Direct;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class DirectCache {

    /**
     * Cache size
     */
    private Integer size = 10;

    private final Queue<String> directKeyQueue = new ArrayDeque<>();

    private final Map<String, Direct> cache = new HashMap<>();

    private final HashMap<String, String> namesDirect = new HashMap<>();

    private final ConcurrentHashMap<String, Future<Direct>> orderRequest = new ConcurrentHashMap<>();

    private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

    private final AtomicBoolean isStopped = new AtomicBoolean(false);

    public DirectCache(ConfigurationWeatherSDK configurationWeatherSDK) {
        size = configurationWeatherSDK.getCacheSize();
    }

    /**
     * Getting location information from cache by location name
     *
     * @param name location name
     * @return object location information
     */
    public Direct getDirectByName(String name) throws WeatherSDKThreadException {
        if (size == 0) {
            return null;
        }
        try {
            if (readWriteLock.readLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    String directName = namesDirect.get(name);
                    if (directName == null) {
                        return null;
                    }
                    return cache.get(directName);
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
     * Update location information in cache
     *
     * @param name location name
     * @param direct location information
     */
    public void putDirectAndNamesByName(String name, Direct direct, Collection<String> names, String findName) throws WeatherSDKThreadException {
        if (size == 0) {
            return;
        }
        if (direct.getName() == null) {
            return;
        }
        if (isStopped.get()) {
            return;
        }
        try {
            if (readWriteLock.writeLock().tryLock(30, TimeUnit.SECONDS)) {
                try {
                    if (cache.containsKey(name)) {
                        namesDirect.put(findName, name);
                    } else {
                        if (directKeyQueue.size() >= size) {
                            String keyForRemove = directKeyQueue.poll();
                            cache.remove(keyForRemove);
                            afterRemoveDirectFromCache(keyForRemove);
                        }
                        directKeyQueue.add(name);
                        cache.put(name, direct);
                        for (String s : names) {
                            namesDirect.put(s, name);
                        }
                        namesDirect.put(findName, name);
                        return;
                    }
                } finally {
                    readWriteLock.writeLock().unlock();
                }
            }
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource weather cache", e.getCause());
        }
        throw new WeatherSDKThreadException("Error accessing the resource weather cache");
    }

    /**
     * Getting queue requests to geo api
     *
     * @return Queue requests
     */
    public ConcurrentHashMap<String, Future<Direct>> getOrderRequest() {
        return orderRequest;
    }

    /**
     * Procedure clean similar location names
     *
     * @param key location name
     */
    private void afterRemoveDirectFromCache(String key) {
        Set<String> keysForRemove = new HashSet<>();
        for (Map.Entry<String, String> entry : namesDirect.entrySet()) {
            if (key.equals(entry.getValue())) {
                keysForRemove.add(entry.getKey());
            }
        }
        for (String k : keysForRemove) {
            namesDirect.remove(k);
        }
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
                    directKeyQueue.clear();
                    namesDirect.clear();
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
