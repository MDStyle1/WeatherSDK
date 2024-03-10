package com.mds.weather.exception;

/**
 * Exception from sdk by bad input data from user
 */
public class WeatherSDKInputException extends WeatherSDKException{

    public WeatherSDKInputException(String message) {
        super(message);
    }
}
