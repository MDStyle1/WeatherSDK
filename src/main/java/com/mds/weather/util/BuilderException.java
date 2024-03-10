package com.mds.weather.util;

import com.mds.weather.exception.WeatherSDKHttpException;
import com.mds.weather.exception.WeatherSDKHttpForbiddenException;
import com.mds.weather.exception.WeatherSDKHttpNotFoundException;
import com.mds.weather.exception.WeatherSDKHttpUnauthorizedException;
import org.apache.http.HttpStatus;

public class BuilderException {

    public static WeatherSDKHttpException createSDKHttpExceptionByStatusCodeAndServiceName(int statusCode, String serviceName) {
        StringBuilder stringBuilder = new StringBuilder("Error request ");
        stringBuilder.append(serviceName);
        stringBuilder.append(" ");
        stringBuilder.append(statusCode);
        if (statusCode == HttpStatus.SC_UNAUTHORIZED) {
            return new WeatherSDKHttpUnauthorizedException(stringBuilder.toString(), statusCode);
        }
        if (statusCode == HttpStatus.SC_FORBIDDEN) {
            return new WeatherSDKHttpForbiddenException(stringBuilder.toString(), statusCode);
        }
        if (statusCode == HttpStatus.SC_NOT_FOUND) {
            return new WeatherSDKHttpNotFoundException(stringBuilder.toString(), statusCode);
        }
        return new WeatherSDKHttpException(stringBuilder.toString(), statusCode);
    }
}
