package com.mds.weather.exception;

/**
 * Exception from mapping objects
 */
public class WeatherSDKMapperException extends WeatherSDKException{

    public WeatherSDKMapperException(String message) {
        super(message);
    }

    public WeatherSDKMapperException(String message, Throwable cause) {
        super(message, cause);
    }
}
