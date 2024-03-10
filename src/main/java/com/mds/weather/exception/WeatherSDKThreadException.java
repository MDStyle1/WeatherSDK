package com.mds.weather.exception;

/**
 * Exception thread or multi thread
 */
public class WeatherSDKThreadException extends WeatherSDKException{

    public WeatherSDKThreadException(String message) {
        super(message);
    }

    public WeatherSDKThreadException(String message, Throwable cause) {
        super(message, cause);
    }
}
