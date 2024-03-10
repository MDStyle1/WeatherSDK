package com.mds.weather.exception;

/**
 * Exception from http requests
 */
public class WeatherSDKHttpException extends WeatherSDKException{

    private Integer statusCode;

    public WeatherSDKHttpException(String message) {
        super(message);
    }

    public WeatherSDKHttpException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public WeatherSDKHttpException(String message, Throwable cause) {
        super(message, cause);
    }

    public WeatherSDKHttpException(String message, Throwable cause, Integer statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}
