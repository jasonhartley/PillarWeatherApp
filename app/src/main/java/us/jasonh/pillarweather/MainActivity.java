package us.jasonh.pillarweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
  private TextInputLayout cityInputLayout;
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

    initRetrofit();
    initUi();
    initLocationService();
    loadSavedSearches();

    displayCurrentLocation();
  }

  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    lastSearchTerm = "Current location: ";
    displayCurrentLocation();
  }

  public void onRadioButtonClicked(View view) {
    boolean checked = ((RadioButton)view).isChecked();

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

  private void initRetrofit() {
    Retrofit retrofit = new Retrofit.Builder()
        .baseUrl(BaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(OpenWeatherService.class);
  }

  private void initUi() {
    weatherOutput = findViewById(R.id.textView);
    cityInput = findViewById(R.id.city_input);
    cityInputLayout = findViewById(R.id.city_input_layout);
    zipInput = findViewById(R.id.zip_input);
    latInput = findViewById(R.id.lat_input);
    lonInput = findViewById(R.id.lon_input);

    cityInput.addTextChangedListener(new TextWatcher() {
      @Override
      public void beforeTextChanged(CharSequence s, int start, int count, int after) {
      }

      @Override
      public void onTextChanged(CharSequence s, int start, int before, int count) {
      }

      @Override
      public void afterTextChanged(Editable s) {
        if (stringContainsNumber(s.toString())) {
          cityInputLayout.setError("The city name probably shouldn't contain a digit.");
        } else {
          cityInputLayout.setError(null);
        }
      }
    });

    findViewById(R.id.city_button).setOnClickListener(v -> {
      String city = cityInput.getText().toString();
      lastSearchTerm = "City: " + city;
      displayWeatherCity(city);
    });
    findViewById(R.id.zip_button).setOnClickListener(v -> {
      String zip = zipInput.getText().toString();
      lastSearchTerm = "Zip: " + zip;
      displayWeatherZip(zip);
    });
    findViewById(R.id.lat_lon_button).setOnClickListener(v -> {
      String lat = latInput.getText().toString();
      String lon = lonInput.getText().toString();
      lastSearchTerm = "Lat: " + lat + ", Lon: " + lon;
      displayWeatherLatLon(lat, lon);
    });
  }

  private void initLocationService() {
    fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
    if (!locationPermissionGranted()) {
      askForLocationPermission();
    }
  }

  private boolean locationPermissionGranted() {
    return ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        || ActivityCompat.checkSelfPermission(this,
        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
  }

  private void askForLocationPermission() {
    ActivityCompat.requestPermissions(this,
        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 666);
  }

  private void loadSavedSearches() {
    Set<String> savedSearches = getLastSearchesFromPrefs();
    lastFewResults.addAll(savedSearches);
  }

  @SuppressLint("MissingPermission")
  private void displayCurrentLocation() {
    fusedLocationClient.getLastLocation()
        .addOnSuccessListener(this, location -> {
          if (location != null) {
            lastSearchTerm = "Current location: ";
            displayWeatherLatLon(
                Double.toString(location.getLatitude()),
                Double.toString(location.getLongitude())
            );
          } else {
            Toast.makeText(MainActivity.this, "Hmm, location was null. That's odd.",
                Toast.LENGTH_LONG).show();
          }
        });
  }

  private void displayWeatherCity(String city) {
    Call<OpenWeatherResponse> call = service.getWeatherCity(city, units, AppId);
    fetchWeather(call);
  }

  private void displayWeatherZip(String zip) {
    zip = zip + ",us";
    Call<OpenWeatherResponse> call = service.getWeatherZip(zip, units, AppId);
    fetchWeather(call);
  }

  private void displayWeatherLatLon(String lat, String lon) {
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
        lastSearchTerm + "\n" +
        "Temperature: " + weatherResponse.main.temp + unitsLabel + " " +
        "Humidity: " + weatherResponse.main.humidity + "% " +
        "Pressure: " + weatherResponse.main.pressure + "hPa";
  }

  private SharedPreferences getPreferences() {
    return PreferenceManager.getDefaultSharedPreferences(getApplication());
  }

  private Set<String> getLastSearchesFromPrefs() {
    return getPreferences().getStringSet(PrefsKey, new HashSet<>());
  }

  private void saveLastSearchesInPrefs(Set<String> value) {
    SharedPreferences.Editor editor = getPreferences().edit();
    editor.putStringSet(PrefsKey, value);
    editor.apply();
  }

  private boolean stringContainsNumber(String s) {
    Pattern p = Pattern.compile("[0-9]");
    Matcher m = p.matcher(s);
    return m.find();
  }

}
