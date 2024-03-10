package com.mds.weather.remote;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mds.weather.dto.DirectApiDto;
import com.mds.weather.dto.ListDirectDto;
import com.mds.weather.exception.WeatherSDKHttpException;
import com.mds.weather.exception.WeatherSDKMapperException;
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

public class GeocodingServiceRemote {

    private final String geocodingApiUrl = "http://api.openweathermap.org/geo/1.0/direct";

    private final ObjectMapper objectMapper;

    public GeocodingServiceRemote(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public DirectApiDto fetchDirectByNameAndApiKey(String name, String apiKey) throws WeatherSDKHttpException, WeatherSDKMapperException {

        HttpUriRequest httpGet = RequestBuilder.get()
                .setUri(geocodingApiUrl)
                .addParameter("q", name)
                .addParameter("appid", apiKey)
                .addParameter("limit", "1")
                .build();

        try (CloseableHttpClient httpclient = HttpClients.createDefault()) {
            try (CloseableHttpResponse response = httpclient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != HttpStatus.SC_OK) {
                    throw BuilderException.createSDKHttpExceptionByStatusCodeAndServiceName(statusCode, "geo api");
                }
                HttpEntity entity = response.getEntity();
                ListDirectDto directResultDtos = objectMapper.readValue(entity.getContent(), ListDirectDto.class);
                if (directResultDtos.isEmpty()) {
                    return null;
                }
                return directResultDtos.get(0);
            }
        } catch (IOException | ParseException e){
            throw new WeatherSDKMapperException("Error read resource from request geo api", e.getCause());
        }
    }

}
