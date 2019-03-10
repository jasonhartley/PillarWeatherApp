package us.jasonh.pillarweather.api.model;

import com.google.gson.annotations.SerializedName;

public class City {

  @SerializedName("id")
  private int id;
  @SerializedName("name")
  private String name;
  @SerializedName("coord")
  private Coord coord;
  @SerializedName("country")
  private String country;
  @SerializedName("population")
  private int population;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Coord getCoord() {
    return coord;
  }

  public void setCoord(Coord coord) {
    this.coord = coord;
  }

  public String getCountry() {
    return country;
  }

  public void setCountry(String country) {
    this.country = country;
  }

  public int getPopulation() {
    return population;
  }

  public void setPopulation(int population) {
    this.population = population;
  }

}