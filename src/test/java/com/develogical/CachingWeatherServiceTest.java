package com.develogical;

import com.weather.Day;
import com.weather.Region;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.*;

public class CachingWeatherServiceTest {
    @Test
    public void delegatesWhenCacheEmpty(){
        WeatherService delegate = mock(WeatherService.class);
        when(delegate.getWeather(Region.BIRMINGHAM, Day.FRIDAY)).thenReturn("cold");

        CachingWeatherService weatherService = new CachingWeatherService(delegate, 10);

        String temperature = weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        assertThat(temperature, equalTo("cold"));
    }

    @Test
    public void doesNotAskForTheSameForecastTwice(){
        WeatherService delegate = mock(WeatherService.class);
        when(delegate.getWeather(Region.BIRMINGHAM, Day.FRIDAY)).thenReturn("cold");

        CachingWeatherService weatherService = new CachingWeatherService(delegate, 10);

        weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        String temperature = weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        assertThat(temperature, equalTo("cold"));

        verify(delegate, times(1)).getWeather(Region.BIRMINGHAM, Day.FRIDAY);
    }

    @Test
    public void doesNotMixUpForecastsForDifferentThings(){
        WeatherService delegate = mock(WeatherService.class);
        when(delegate.getWeather(Region.BIRMINGHAM, Day.FRIDAY)).thenReturn("cold");
        when(delegate.getWeather(Region.EDINBURGH, Day.FRIDAY)).thenReturn("hot");

        CachingWeatherService weatherService = new CachingWeatherService(delegate, 10);

        weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        String temperature = weatherService.getWeather(Region.EDINBURGH, Day.FRIDAY);
        assertThat(temperature, equalTo("hot"));
    }

    @Test
    public void doesNotMixUpForecastsForDifferentDays(){
        WeatherService delegate = mock(WeatherService.class);
        when(delegate.getWeather(Region.BIRMINGHAM, Day.FRIDAY)).thenReturn("cold");
        when(delegate.getWeather(Region.BIRMINGHAM, Day.SATURDAY)).thenReturn("hot");

        CachingWeatherService weatherService = new CachingWeatherService(delegate, 10);

        weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        weatherService.getWeather(Region.BIRMINGHAM, Day.SATURDAY);
        String temperature = weatherService.getWeather(Region.BIRMINGHAM, Day.FRIDAY);
        assertThat(temperature, equalTo("cold"));
        temperature = weatherService.getWeather(Region.BIRMINGHAM, Day.SATURDAY);
        assertThat(temperature, equalTo("hot"));

    }

    @Test
    public void doesNotGoOverLimit(){
        WeatherService delegate = mock(WeatherService.class);
        when(delegate.getWeather(Region.BIRMINGHAM, Day.MONDAY)).thenReturn("1");
        when(delegate.getWeather(Region.BIRMINGHAM, Day.TUESDAY)).thenReturn("2");

        CachingWeatherService weatherService = new CachingWeatherService(delegate, 1);

        weatherService.getWeather(Region.BIRMINGHAM, Day.MONDAY);
        weatherService.getWeather(Region.BIRMINGHAM, Day.TUESDAY);
        weatherService.getWeather(Region.BIRMINGHAM, Day.MONDAY);

        verify(delegate, times(3)).getWeather(any(Region.class), any(Day.class));

    }

}
