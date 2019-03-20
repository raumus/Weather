package com.develogical;

import java.util.Date;

public class WeatherCache {
    public String forecast;
    public Date timestamp;

    WeatherCache(String forecast, Date timestamp){
        this.forecast = forecast;
        this.timestamp = timestamp;
    }
}

