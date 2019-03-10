package us.jasonh.pillarweather.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import us.jasonh.pillarweather.api.model.OpenWeatherResponse;

public interface OpenWeatherService {

  //api.openweathermap.org/data/2.5/weather?q={city name}
  //api.openweathermap.org/data/2.5/weather?q=London
  @GET("data/2.5/weather?")
  Call<OpenWeatherResponse> getWeatherCity(@Query("q") String city,
                                           @Query("units") String units,
                                           @Query("APPID") String app_id);

  //api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}
  //api.openweathermap.org/data/2.5/weather?lat=35&lon=139
  @GET("data/2.5/weather?")
  Call<OpenWeatherResponse> getWeatherLatLon(@Query("lat") String lat,
                                             @Query("lon") String lon,
                                             @Query("units") String units,
                                             @Query("APPID") String app_id);

  //api.openweathermap.org/data/2.5/weather?zip={zip code},{country code}
  //api.openweathermap.org/data/2.5/weather?zip=94040,us
  @GET("data/2.5/weather?")
  Call<OpenWeatherResponse> getWeatherZip(@Query("zip") String zip,
                                          @Query("units") String units,
                                          @Query("APPID") String app_id);

}