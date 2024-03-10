package com.mds.weather.exception;

/**
 * Exception from http request for api response 404
 */
public class WeatherSDKHttpNotFoundException extends WeatherSDKHttpException {

    public WeatherSDKHttpNotFoundException(String message) {
        super(message);
    }

    public WeatherSDKHttpNotFoundException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    public WeatherSDKHttpNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherSDKHttpNotFoundException(String message, Throwable cause, Integer statusCode) {
        super(message, cause, statusCode);
    }

}
