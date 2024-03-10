package com.mds.weather.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.dto.MainApiDto;
import com.mds.weather.exception.WeatherSDKHttpException;
import com.mds.weather.exception.WeatherSDKMapperException;
import com.mds.weather.mapper.MainMapper;
import com.mds.weather.model.MainResult;
import com.mds.weather.model.Direct;
import com.mds.weather.util.BuilderException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class WeatherServiceRemote<W extends MainResult, D extends MainApiDto> {

    private final String weatherApiUrl;

    protected final ObjectMapper objectMapper;

    protected final MainMapper<W, D> weatherMapper;

    public WeatherServiceRemote(String weatherApiUrl,
                                ObjectMapper objectMapper,
                                MainMapper<W, D> weatherMapper) {
        this.weatherApiUrl = weatherApiUrl;
        this.objectMapper = objectMapper;
        this.weatherMapper = weatherMapper;
    }

    public W fetchCurrentWeatherByDirectAndApiKey(Direct direct, String apiKey) throws WeatherSDKHttpException, WeatherSDKMapperException {
        HttpUriRequest httpGet = createHttpUriForCurrentWeather(direct, apiKey);

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw BuilderException.createSDKHttpExceptionByStatusCodeAndServiceName(statusCode, "weather api");
                }
                HttpEntity entity = response.getEntity();
                return createFromApiDto(objectMapper.readValue(entity.getContent(), weatherMapper.getClassDto()));
            }
        } catch (IOException | ParseException e){
            throw new WeatherSDKMapperException("Error read resource from request weather api", e);
        }
    }

    protected W createFromApiDto(D dto) {
        return weatherMapper.createFromApiDto(dto);
    }

    protected HttpUriRequest createHttpUriForCurrentWeather(Direct direct, String apiKey) {
        RequestBuilder builder = RequestBuilder.get()
                .setUri(weatherApiUrl);

        for (Map.Entry<String, String> entry : getQueryParametersByDirectAndApiKey(direct, apiKey).entrySet()) {
            builder.addParameter(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }

    /**
     * Create query parameters for request to api
     *
     * @param direct location information
     * @param apiKey
     * @return query parameters
     */
    protected Map<String, String> getQueryParametersByDirectAndApiKey(Direct direct, String apiKey) {
        Map<String, String> map = new HashMap<>();
        map.put("appid", apiKey);
        map.put("lat", direct.getLat().toString());
        map.put("lon", direct.getLon().toString());
        return map;
    }
}
