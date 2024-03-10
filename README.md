# WeatherSDK


# Introduction

This SDK works with OpenWeatherAPI

OpenWeatherAPI provides access to weather and geo data via a JSON/XML restful API. It allows developers to create desktop, web and mobile applications using this data very easy.

This sdk provide following data through our API:

  * Real-time weather

# Getting Started

You need to [signup](https://home.openweathermap.org/users/sign_in) and then you can find your API key under [your account](https://home.openweathermap.org/api_keys), and start using API right away!

## How to Build

The generated code uses a few Maven dependencies e.g., Jackson,
and Apache HttpClient. The reference to these dependencies is already
added in the pom.xml file will be installed automatically. Therefore,
you will need internet access for a successful build.

To get starting using with maven add in pom.xml

    <dependencies>
        <dependency>
            <groupId>com.mds</groupId>
            <artifactId>weather-sdk</artifactId>
            <version>1.0.0</version>
            <systemPath>path-to-jar\weather-sdk-1.0.0.jar</systemPath>
            <scope>system</scope>
        </dependency>
        ...
    </dependencies>

To get starting using with gradle add in Gradle.build

    dependencies {
        compile files('system-path/weather-sdk-1.0.0.jar')
    }

or all from path

    dependencies {
        compile fileTree(dir: 'system-path', include: '*.jar')
    }

## How to Use

## Initialization

### Authentication
In order to setup authentication and initialization of the API client, you need the following information.

API client can be initialized as following.

```java
String apiKey = "apiKey";

WeatherSDKClient client = WeatherSDKController.createWeatherSDKClient(apiKey);
```

Already ready for use

```java
String cityName = "London";
String result = client.fetchCurrentWeatherByNameCity();
```

### Cofiguration

```java
String apiKey = "apiKey";

ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder().build(); 

WeatherSDKClient client = WeatherSDKController.createWeatherSDKClient(apiKey, configurationWeatherSDK);
```

# Configurations Reference

### Cache
```java
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
        .setTimeMinutesExpiredWeather(2)
        .setCacheSize(10)
        .build();
```
SDK has a function for caching results


#### setTimeMinutesExpiredWeather default 10 minutes
time after which the result is considered expired in minutes. Set 0 to disable the function

#### setCacheSize default 10
size for number of places where they are stored result weather. Set 0 to disable the function

### Type WeatherApi
```java
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
        .setWeatherApi(WeatherApi.CURRENT_WEATHER_API)
        .build();
```
Setting service remote for weather api

[ONE_CALL_API](https://openweathermap.org/api/one-call-3)

[CURRENT_WEATHER_API](https://openweathermap.org/current) default

```java
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
        .setWeatherApiUrl("http://...")
        .build();
```

Custom url for weather api

### Request mode
```java
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
        .setRequestMode(RequestMode.ON_DEMAND_MODE)
        .build();
```
ON_DEMAND_MODE - SDK updates the weather information only on customer requests.

POLLING_MODE - SDK requests new weather information for all stored locations.
every half-time expired

### Mapping

Default result
```json
{"datetime":1710014410,"name":"London","weather":{"main":"Clouds","description":"few clouds"},"temperature":{"temp":282.36,"feels_like":280.66},"visibility":10000,"wind":{"speed":3.09},"sys":{"sunrise":1709965653,"sunset":1710006877},"timezone":0}
```

If be inherited from MainResult
```java
    private class CustomWeather extends MainResult {

        private Temperature temperature;

        public Temperature getTemperature() {
            return temperature;
        }

        public void setTemperature(Temperature temperature) {
            this.temperature = temperature;
        }
    }
```
And create mapper that implements WeatherCurrentMainMapper
```java
private class CustomMapper implements MainMapper<CustomWeather, WeatherApiDto> {

    @Override
    public Class<WeatherApiDto> getClassDto() {
        return WeatherApiDto.class;
    }

    @Override
    public CustomWeather createFromApiDto(WeatherApiDto weatherApiDto) {
        CustomWeather weather = new CustomWeather();
        Temperature temperature = new Temperature();
        temperature.setTemp(weatherApiDto.getMain().getTemp());
        temperature.setFeelsLike(weatherApiDto.getMain().getFeelsLike());
        weather.setTemperature(temperature);
        return weather;
    }
}
```

Add custom mapper in configuration

```java
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder()
        .setWeatherMapper(new CustomMapper())
        .build();
```

new result
```json
{"datetime":1710014916,"name":"London","temperature":{"temp":282.45,"feels_like":280.24}}
```

And can also replace the model of the weather api dto
```java

public class CustomWeatherDto extends MainApiDto {

    private String name;

    private WeatherMainApiDto main;
    
    ... getters and setters
}
```

```java
private class CustomMapper implements MainMapper<CustomWeather, MainApiDto> {

    @Override
    public Class<MainApiDto> getClassDto() {
        return MainApiDto.class;
    }

    @Override
    public CustomWeather createFromApiDto(MainApiDto weatherApiDto) {
        CustomWeather weather = new CustomWeather();
        ...
        return weather;
    }
}
```
### Weather service remote

```java
private class CustomWeatherServiceRemote extends WeatherServiceRemote {
    
    public CustomWeatherServiceRemote(String weatherApiUrl, ObjectMapper objectMapper, MainMapper mainMapper) {
        super(weatherApiUrl, objectMapper, mainMapper);
    }
}
```

Edit query parameters for request

```java
@Override
protected Map<String, String> getQueryParametersByDirectAndApiKey(Direct direct, String apiKey) {
    return super.getQueryParametersByDirectAndApiKey(direct, apiKey);
}
```

```java
ConfigurationWeatherSDKBuilder builder = new ConfigurationWeatherSDKBuilder();
WeatherServiceRemote weatherServiceRemote = new CustomWeatherServiceRemote("custom Url", builder.getObjectMapper(), new CustomMapper()); 
 
ConfigurationWeatherSDK configurationWeatherSDK = builder         
        .setWeatherServiceRemote(weatherServiceRemote)
        .build();
```

# Example for use with other weather api 
#Air Pollution API

### Custom dto
```java
public class AirPollutionDto extends MainApiDto {

    ...
    
    private Double co;
    
    ... getters and setters
}
```

### Custom model

```java
import com.mds.weather.model.MainResult;

public class CurrentAirPollution extends MainResult {
    ...

    private Double co;
    
    ...
    getters and
    setters
}
```
### Custom mapper
```java
private class CustomMapper implements MainMapper<CurrentAirPollution, AirPollutionDto> {

    @Override
    public Class<AirPollutionDto> getClassDto() {
        return AirPollutionDto.class;
    }

    @Override
    public CurrentAirPollution createFromApiDto(AirPollutionDto airPollutionDto) {
        CurrentAirPollution currentAirPollution = new CurrentAirPollution();
        ...
        return currentAirPollution;
    }
}
```
### Configuration

```java 
ConfigurationWeatherSDK configurationWeatherSDK = new ConfigurationWeatherSDKBuilder() 
        .setWeatherMapper(new CustomMapper())
        .setWeatherApiUrl("http://api.openweathermap.org/data/2.5/air_pollution")
        .build();
```

### start

```java
String apiKey = "apiKey";

WeatherSDKClient client = WeatherSDKController.createWeatherSDKClient(apiKey, configurationWeatherSDK);

String cityName = "London";

String result = client.fetchCurrentWeatherByNameCity();
```



# Errors

| Error type | Error Description                                |
|------------|--------------------------------------------------|
| WeatherSDKException        | Main exception from sdk                          |
| WeatherSDKInputException        | Exception from sdk by bad input data from user   |
| WeatherSDKStatusException        | Exception status work client or sdk              |
| WeatherSDKThreadException        | Exception thread or multi thread                 |
| WeatherSDKMapperException        | Exception from mapping objects                   |
| WeatherSDKResultException        | Exception from results maybe replace of null     |
| WeatherSDKHttpException        | Exception from http requests                     |
| WeatherSDKHttpNotFoundException        | Exception from http request for api response 404 |
| WeatherSDKHttpForbiddenException        | Exception from http request for api response 403 |
| WeatherSDKHttpUnauthorizedException        | Exception from http request for api response 401 |
