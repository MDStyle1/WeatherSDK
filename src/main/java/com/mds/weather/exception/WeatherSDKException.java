package com.mds.weather.exception;

/**
 * Exception from sdk
 */
public class WeatherSDKException extends Exception{

    public WeatherSDKException(String message) {
        super(message);
    }

    public WeatherSDKException(String message, Throwable cause) {
        super(message, cause);
    }
}
