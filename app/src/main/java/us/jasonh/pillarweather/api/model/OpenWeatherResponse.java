package us.jasonh.pillarweather.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OpenWeatherResponse {
  @SerializedName("clouds")
  public Clouds clouds;
  @SerializedName("cod")
  public float cod;
  @SerializedName("coord")
  public Coord coord;
  @SerializedName("dt")
  public float dt;
  @SerializedName("id")
  public int id;
  @SerializedName("main")
  public Main main;
  @SerializedName("name")
  public String name;
  @SerializedName("rain")
  public Rain rain;
  @SerializedName("sys")
  public Sys sys;
  @SerializedName("weather")
  public List<Weather> weather = new ArrayList<>();
  @SerializedName("wind")
  public Wind wind;

  public class Clouds {
    @SerializedName("all")
    public float all;
  }

  public class Coord {
    @SerializedName("lon")
    public float longitude;
    @SerializedName("lat")
    public float latitude;
  }

  public class Main {
    @SerializedName("temp")
    public float temp;
    @SerializedName("humidity")
    public float humidity;
    @SerializedName("pressure")
    public float pressure;
    @SerializedName("temp_min")
    public float temp_min;
    @SerializedName("temp_max")
    public float temp_max;
  }

  public class Rain {
    @SerializedName("3h")
    public float h3;
  }

  public class Sys {
    @SerializedName("country")
    public String country;
    @SerializedName("sunrise")
    public long sunrise;
    @SerializedName("sunset")
    public long sunset;
  }

  public class Weather {
    @SerializedName("id")
    public int id;
    @SerializedName("main")
    public String main;
    @SerializedName("description")
    public String description;
    @SerializedName("icon")
    public String icon;
  }

  public class Forecast {
    @SerializedName("dt")
    public long timestamp;
    @SerializedName("temp")
    public Temperature temperature;
    @SerializedName("pressure")
    public float pressure;
    @SerializedName("humidity")
    public int humdity;
    @SerializedName("weather")
    public List<Weather> mWeather;
    @SerializedName("speed")
    public float mWindSpeed;
    @SerializedName("deg")
    public int mWindDirection;
    @SerializedName("clouds")
    public int mClouds;
  }

  public class City {
    @SerializedName("id")
    public int id;
    @SerializedName("name")
    public String name;
    @SerializedName("coord")
    public Coord coord;
    @SerializedName("country")
    public String country;
    @SerializedName("population")
    public int population;
  }

  public class Temperature {
    @SerializedName("day")
    public float dayTemperature;
    @SerializedName("min")
    public float lowTemperature;
    @SerializedName("max")
    public float highTemperature;
    @SerializedName("night")
    public float nightTemperature;
    @SerializedName("eve")
    public float eveningTemperature;
    @SerializedName("morn")
    public float morningTemperature;
  }

  public class Wind {
    @SerializedName("speed")
    public float speed;
    @SerializedName("deg")
    public float deg;
  }

}