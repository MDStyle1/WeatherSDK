package com.mds.weather.local;

import com.mds.weather.dto.DirectApiDto;
import com.mds.weather.exception.WeatherSDKException;
import com.mds.weather.exception.WeatherSDKHttpException;
import com.mds.weather.exception.WeatherSDKMapperException;
import com.mds.weather.exception.WeatherSDKThreadException;
import com.mds.weather.model.Direct;
import com.mds.weather.remote.GeocodingServiceRemote;

import java.util.concurrent.*;

public class DirectServiceLocal {

    private final DirectCache directCache;

    private final GeocodingServiceRemote geocodingServiceRemote;

    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final String apiKey;

    public DirectServiceLocal(GeocodingServiceRemote geocodingServiceRemote,
                              DirectCache directCache,
                              String apiKey) {
        this.geocodingServiceRemote = geocodingServiceRemote;
        this.directCache = directCache;
        this.apiKey = apiKey;
    }

    /**
     * Getting location information by direct name
     *
     * @param name direct name
     * @return location information
     */
    public Direct getDirectFromCache(String name) throws WeatherSDKException {
        Direct direct = directCache.getDirectByName(name);
        if (direct == null) {
            direct = getDirectByNameFromQueue(name);
        }
        return direct;
    }

    /**
     * pre request location information by direct name from queue
     *
     * @param name direct name
     * @return location information
     */
    private Direct getDirectByNameFromQueue(String name) throws WeatherSDKException {
        Future<Direct> future = directCache.getOrderRequest().computeIfAbsent(name,k -> executorService.submit(() -> requestDirect(name)));
        try {
            Direct direct = future.get();
            directCache.getOrderRequest().remove(name);
            return direct;
        } catch (InterruptedException e) {
            throw new WeatherSDKThreadException("Error accessing the resource order request for geo", e.getCause());
        } catch (ExecutionException e) {
            Throwable cause = e.getCause();
            if (cause instanceof WeatherSDKException exception) {
                throw exception;
            }
            throw new WeatherSDKThreadException("Error accessing the resource order request for geo", e.getCause());
        }
    }

    /**
     * Request information location geo api
     *
     * @param name direct name
     * @return Location information
     */
    private Direct requestDirect(String name) throws WeatherSDKMapperException, WeatherSDKHttpException, WeatherSDKThreadException {
        DirectApiDto dto = geocodingServiceRemote.fetchDirectByNameAndApiKey(name, apiKey);
        if (dto == null) {
            return null;
        }
        Direct direct = createForApiDto(dto);
        directCache.putDirectAndNamesByName(direct.getName(), direct, dto.getLocalNames().values(), name);
        return direct;
    }

    /**
     * Create Direct object from dto geo api
     *
     * @param dto dto geo api
     * @return Object location information
     */
    private Direct createForApiDto(DirectApiDto dto) {
        if (dto == null) {
            return null;
        }
        Direct direct = new Direct();
        direct.setName(dto.getName());
        direct.setLat(dto.getLat());
        direct.setLon(dto.getLon());
        return direct;
    }

    /**
     * Procedure stop client
     */
    public void stop() throws WeatherSDKThreadException {
        directCache.stopCache();
    }
}
