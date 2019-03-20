package com.develogical;

import com.weather.Day;
import com.weather.Region;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CachingWeatherService implements WeatherService {
    private final WeatherService delegate;
    private final int maxNumberToKeep;
    private final Clock clock;
    private Map<String, WeatherCache> weatherCondition = new HashMap<>();

    public CachingWeatherService(WeatherService delegate, int maxNumberToKeep, Clock clock) {
        this.delegate = delegate;
        this.maxNumberToKeep = maxNumberToKeep;
        this.clock = clock;
    }

    @Override
    public String getWeather(Region region, Day day) {
//        return delegate.getWeather(region, day);
        String key = region.name() + day.name();
        if (!weatherCondition.containsKey(key)) {
            if(weatherCondition.size() >= maxNumberToKeep){
                weatherCondition.clear();
            }
            WeatherCache weatherCache = new WeatherCache(delegate.getWeather(region, day), clock.now());
            weatherCondition.put(key, weatherCache);
        }

        return weatherCondition.get(key).forecast;
    }

}