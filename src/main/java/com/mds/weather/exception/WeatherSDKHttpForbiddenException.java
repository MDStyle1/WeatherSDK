package com.mds.weather.exception;

/**
 * Exception from http request for api response 403
 */
public class WeatherSDKHttpForbiddenException extends WeatherSDKHttpException {

    public WeatherSDKHttpForbiddenException(String message) {
        super(message);
    }

    public WeatherSDKHttpForbiddenException(String message, Integer statusCode) {
        super(message, statusCode);
    }

    public WeatherSDKHttpForbiddenException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherSDKHttpForbiddenException(String message, Throwable cause, Integer statusCode) {
        super(message, cause, statusCode);
    }

}
