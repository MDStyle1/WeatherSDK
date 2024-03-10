package com.mds.weather.exception;

/**
 * Exception from http request for api response 401
 */
public class WeatherSDKHttpUnauthorizedException extends WeatherSDKHttpException {

    public WeatherSDKHttpUnauthorizedException(String message) {
        super(message);
    }

    public WeatherSDKHttpUnauthorizedException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    public WeatherSDKHttpUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherSDKHttpUnauthorizedException(String message, Throwable cause, Integer statusCode) {
        super(message, cause, statusCode);
    }

}
