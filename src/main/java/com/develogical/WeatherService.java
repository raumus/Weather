package com.develogical;

import com.weather.Day;
import com.weather.Region;

public interface WeatherService {
    String getWeather(Region region, Day day);
}
