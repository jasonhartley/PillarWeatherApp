package us.jasonh.pillarweather.api.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {

  @SerializedName("dt")
  private long mTimestamp;
  @SerializedName("temp")
  private Temperature mTemperature;
  @SerializedName("pressure")
  private float mPressure;
  @SerializedName("humidity")
  private int mHumdity;
  @SerializedName("weather")
  private List<Weather> mWeather;
  @SerializedName("speed")
  private float mWindSpeed;
  @SerializedName("deg")
  private int mWindDirection;
  @SerializedName("clouds")
  private int mClouds;

  public long getTimestamp() {
    return mTimestamp;
  }

  public void setTimeStamp(int timestamp) {
    mTimestamp = timestamp;
  }

  public Temperature getTemperature() {
    return mTemperature;
  }

  public void setTemperature(Temperature temperature) {
    mTemperature = temperature;
  }

  public float getPressure() {
    return mPressure;
  }

  public void setPressure(float pressure) {
    mPressure = pressure;
  }

  public int getHumidity() {
    return mHumdity;
  }

  public void setHumidity(int humidity) {
    mHumdity = humidity;
  }

  public List<Weather> getWeather() {
    return mWeather;
  }

  public void setWeather(List<Weather> weather) {
    mWeather = weather;
  }

  public float getWindSpeed() {
    return mWindSpeed;
  }

  public void setWindSpeed(float windSpeed) {
    mWindSpeed = windSpeed;
  }

  public int getWindDirection() {
    return mWindDirection;
  }

  public void setWindDirection(int windDirection) {
    mWindDirection = windDirection;
  }

  public int getClouds() {
    return mClouds;
  }

  public void setClouds(int clouds) {
    mClouds = clouds;
  }

}