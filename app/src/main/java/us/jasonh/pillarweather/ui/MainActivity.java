package us.jasonh.pillarweather.ui;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import us.jasonh.pillarweather.R;
import us.jasonh.pillarweather.api.OpenWeatherService;
import us.jasonh.pillarweather.api.model.OpenWeatherResponse;

public class MainActivity extends AppCompatActivity {

  public static String BaseUrl = "http://api.openweathermap.org/";
  public static String AppId = "5cfeb842f8bd997740f5cf78ccf224ef";
  public static String Fahrenheit = "imperial";
  public static String Celsius = "metric";
  public static String PrefsKey = "PillarWeatherApp";

  private OpenWeatherService service;
  private FusedLocationProviderClient fusedLocationClient;
  private TextView weatherOutput;
  private EditText cityInput;
  private EditText zipInput;
  private EditText latInput;
  private EditText lonInput;
  private String units = Fahrenheit;
  private String lastSearchTerm;
  private List<String> lastFewResults = new ArrayList<>();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(OpenWeatherService.class);

    weatherOutput = findViewById(R.id.textView);
    cityInput = findViewById(R.id.city_input);
    zipInput = findViewById(R.id.zip_input);
    latInput = findViewById(R.id.lat_input);
    lonInput = findViewById(R.id.lon_input);

    findViewById(R.id.city_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String city = cityInput.getText().toString();
        lastSearchTerm = "City: " + city;
        displayWeatherCity(city);
      }
    });
    findViewById(R.id.zip_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String zip = zipInput.getText().toString();
        lastSearchTerm = "Zip: " + zip;
        displayWeatherZip(zip);
      }
    });
    findViewById(R.id.lat_lon_button).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        String lat = latInput.getText().toString();
        String lon = lonInput.getText().toString();
        lastSearchTerm = "Lat: " + lat + ", Long: " + lon;
        displayWeatherLatLon(lat, lon);
      }
    });

    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      String locationAccess = Manifest.permission.ACCESS_FINE_LOCATION;
      if (ContextCompat.checkSelfPermission(this, locationAccess) != PackageManager.PERMISSION_GRANTED) {
        ActivityCompat.requestPermissions(this, new String[]{locationAccess}, 666);
      }
    } else {
      Set<String> savedSearches = getLastSearchesFromPrefs();
      lastFewResults.addAll(savedSearches);
      lastSearchTerm = "Current location: ";
      displayCurrentLocation();
    }
  }

  private void displayCurrentLocation() {
    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
          @Override
          public void onSuccess(Location location) {
            if (location != null) {
              displayWeatherLatLon(
                  Double.toString(location.getLatitude()),
                  Double.toString(location.getLongitude())
              );
            } else {
              Toast.makeText(MainActivity.this, "Hmm, location was null. That's odd.",
                  Toast.LENGTH_LONG).show();
            }
          }
        });
  }

  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    lastSearchTerm = "Current location: ";
    displayCurrentLocation();
  }

  public void onRadioButtonClicked(View view) {
    // Is the button now checked?
    boolean checked = ((RadioButton) view).isChecked();

    // Check which radio button was clicked
    switch(view.getId()) {
      case R.id.radio_fahrenheit:
        if (checked)
          units = Fahrenheit;
          break;
      case R.id.radio_celsius:
        if (checked)
          units = Celsius;
          break;
    }
  }

  void displayWeatherCity(String city) {
    Call<OpenWeatherResponse> call = service.getWeatherCity(city, units, AppId);
    fetchWeather(call);
  }

  void displayWeatherZip(String zip) {
    zip = zip + ",us";
    Call<OpenWeatherResponse> call = service.getWeatherZip(zip, units, AppId);
    fetchWeather(call);
  }

  void displayWeatherLatLon(String lat, String lon) {
    Call<OpenWeatherResponse> call = service.getWeatherLatLon(lat, lon, units, AppId);
    fetchWeather(call);
  }

  private void fetchWeather(Call<OpenWeatherResponse> call) {
    call.enqueue(new Callback<OpenWeatherResponse>() {
      @Override
      public void onResponse(@NonNull Call<OpenWeatherResponse> call,
                             @NonNull Response<OpenWeatherResponse> response) {
        if (response.code() == 200) {
          OpenWeatherResponse weatherResponse = response.body();
          assert weatherResponse != null;
          String weatherString = getWeatherString(weatherResponse);
          if (lastFewResults.size() == 3) {
            lastFewResults.remove(0);
          }
          lastFewResults.add(weatherString);

          saveLastSearchesInPrefs(new HashSet<>(lastFewResults));

          String lastFewString = "";
          for (String line : lastFewResults) {
            lastFewString += line + "\n";
          }
          weatherOutput.setText(lastFewString);
        }
      }

      @Override
      public void onFailure(@NonNull Call<OpenWeatherResponse> call, @NonNull Throwable t) {
        weatherOutput.setText(t.getMessage());
      }
    });
  }

  private String getWeatherString(OpenWeatherResponse weatherResponse) {
    String unitsLabel = units.equals(Fahrenheit) ? "F" : "C";
    return
        lastSearchTerm + " " +
        "Temperature: " + weatherResponse.main.temp + unitsLabel +" " +
        "Humidity: " + weatherResponse.main.humidity + " " +
        "Pressure: " + weatherResponse.main.pressure;
  }

  private SharedPreferences getPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(getApplication());
  }

  private Set<String> getLastSearchesFromPrefs() {
    return getPreferences().getStringSet(PrefsKey, new HashSet<String>());
  }

  private void saveLastSearchesInPrefs(Set<String> value) {
    SharedPreferences.Editor editor = getPreferences().edit();
    editor.putStringSet(PrefsKey, value);
    editor.apply();
  }

}
