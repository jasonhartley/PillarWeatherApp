package us.jasonh.pillarweather.api.model;

import com.google.gson.annotations.SerializedName;

public class Temperature {

  @SerializedName("day")
  private float mDayTemperature;
  @SerializedName("min")
  private float mLowTemperature;
  @SerializedName("max")
  private float mHighTemperature;
  @SerializedName("night")
  private float mNightTemperature;
  @SerializedName("eve")
  private float mEveningTemperature;
  @SerializedName("morn")
  private float mMorningTemperature;

  public float getDayTemperature() {
    return mDayTemperature;
  }

  public void setDayTemperature(float dayTemperature) {
    mDayTemperature = dayTemperature;
  }

  public float getLowTemperature() {
    return mLowTemperature;
  }

  public void setLowTemperature(float lowTemperature) {
    mLowTemperature = lowTemperature;
  }

  public float getHighTemperature() {
    return mHighTemperature;
  }

  public void setHighTemperature(float highTemperature) {
    mHighTemperature = highTemperature;
  }

  public float getNightTemperature() {
    return mNightTemperature;
  }

  public void setNightTemperature(float nightTemperature) {
    mNightTemperature = nightTemperature;
  }

  public float getEveningTemperature() {
    return mEveningTemperature;
  }

  public void setEveningTemperature(float eveningTemperature) {
    mEveningTemperature = eveningTemperature;
  }

  public float getMorningTemperature() {
    return mMorningTemperature;
  }

  public void setMorningTemperature(float morningTemperature) {
    mMorningTemperature = morningTemperature;
  }

}
