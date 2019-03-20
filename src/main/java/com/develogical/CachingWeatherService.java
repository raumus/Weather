package com.develogical;

import com.weather.Day;
import com.weather.Region;

import java.util.HashMap;
import java.util.Map;

public class CachingWeatherService implements WeatherService {
    private final WeatherService delegate;
    private final int maxNumberToKeep;
    private Map<String, String> weatherCondition = new HashMap<>();

    public CachingWeatherService(WeatherService delegate, int maxNumberToKeep) {
        this.delegate = delegate;
        this.maxNumberToKeep = maxNumberToKeep;
    }

    @Override
    public String getWeather(Region region, Day day) {
//        return delegate.getWeather(region, day);
        String key = region.name() + day.name();
        if (!weatherCondition.containsKey(key)) {
            if(weatherCondition.size() >= maxNumberToKeep){
                weatherCondition.clear();
            }
            weatherCondition.put(key, delegate.getWeather(region, day));
        }

        return weatherCondition.get(key);
    }

}

