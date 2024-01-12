package com.example.inclass;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ArrayList<String> savedates = new ArrayList<>();
    public ArrayList<String> savetemps = new ArrayList<>();

    public WebView wv1;
    public ArrayList<TextView> days = new ArrayList<>();
    public ArrayList<TextView> tempdays = new ArrayList<>();
    public FusedLocationProviderClient fusedLocationClient;

    public double lat, lonng;

    public JSONArray temps, probabilityhr, windyhair, probabilityday, timedays, wcode, timehours;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            ActivityCompat.requestPermissions(MainActivity.this, "android.permission.FINE_LOCATION", 1);
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                lat = location.getLatitude();
                lonng = location.getLongitude();
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                }
            }
        });
        */
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.open-meteo.com/v1/forecast?latitude=30.2672&longitude=-97.7431&hourly=temperature_2m,precipitation_probability,wind_speed_10m&daily=weather_code,precipitation_probability_max&temperature_unit=fahrenheit&wind_speed_unit=mph&precipitation_unit=inch&timezone=America%2FChicago";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            temps = response.getJSONObject("hourly").getJSONArray("temperature_2m");
                            probabilityhr = response.getJSONObject("hourly").getJSONArray("precipitation_probability");
                            windyhair = response.getJSONObject("hourly").getJSONArray("wind_speed_10m");
                            probabilityday = response.getJSONObject("daily").getJSONArray("precipitation_probability_max");
                            timedays = response.getJSONObject("daily").getJSONArray("time");
                            timehours = response.getJSONObject("hourly").getJSONArray("time");
                            wcode = response.getJSONObject("daily").getJSONArray("weather_code");

                            String date = "failed";

                            days.add((TextView) findViewById(R.id.textView2));
                            days.add((TextView) findViewById(R.id.textView3));
                            days.add((TextView) findViewById(R.id.textView4));
                            days.add((TextView) findViewById(R.id.textView7));
                            days.add((TextView) findViewById(R.id.textView9));
                            days.add((TextView) findViewById(R.id.textView11));
                            days.add((TextView) findViewById(R.id.textView12));

                            tempdays.add((TextView) findViewById(R.id.textView13));
                            tempdays.add((TextView) findViewById(R.id.textView14));
                            tempdays.add((TextView) findViewById(R.id.textView15));
                            tempdays.add((TextView) findViewById(R.id.textView16));
                            tempdays.add((TextView) findViewById(R.id.textView17));
                            tempdays.add((TextView) findViewById(R.id.textView18));
                            tempdays.add((TextView) findViewById(R.id.textView19));
                            try {
                                for (int i = 0; i < days.size(); i++) {
                                    date = (String) timedays.getString(i);
                                    String month = date.substring(5, 7);
                                    if (month.equals("12")) {
                                        month = "December";
                                    } else if (month.equals("11")) {
                                        month = "November";
                                    } else if (month.equals("10")) {
                                        month = "October";
                                    } else if (month.equals("9")) {
                                        month = "September";
                                    } else if (month.equals("8")) {
                                        month = "August";
                                    } else if (month.equals("7")) {
                                        month = "July";
                                    } else if (month.equals("6")) {
                                        month = "June";
                                    } else if (month.equals("5")) {
                                        month = "May";
                                    } else if (month.equals("4")) {
                                        month = "April";
                                    } else if (month.equals("3")) {
                                        month = "March";
                                    } else if (month.equals("2")) {
                                        month = "Feburary";
                                    } else if (month.equals("1")) {
                                        month = "January";
                                    }
                                    savedates.add(month + " " + date.substring(8) + ", " + date.substring(0, 4));
                                    days.get(i).setText(savedates.get(i));
                                }
                                for (int i = 0; i < tempdays.size(); i++) {
                                    double dailyaverage = 0;
                                    for (int j = 0; j < 24; j++)
                                        dailyaverage = dailyaverage + temps.getDouble((i * 24) + j);
                                    dailyaverage = dailyaverage / 24;
                                    String truncation = "" + dailyaverage;
                                    String truncated = "";
                                    int maxD = 0;
                                    for (int k = 0; !truncation.substring(k, k + 1).equals("."); k++) {
                                        truncated = truncated + truncation.substring(k, k + 1);
                                        maxD = k;
                                    }
                                    truncated = truncated + truncation.substring(maxD + 1, maxD + 3);

                                    savetemps.add(truncated + "°F");
                                    tempdays.get(i).setText(savetemps.get(i));

                                }

                            } catch (org.json.JSONException nothandling) {
                            }


                        } catch (org.json.JSONException nothandling) {

                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // TODO: Handle error
                    }
                });

        // Access the RequestQueue through your singleton class.
        queue.add(jsonObjectRequest);


    }

    public void Buttondetails(View view) {
        int day = 0;


        if (view.getId() == R.id.imageButton11)
        {

        }
        else if (view.getId() == R.id.imageButton25)
        {
            day = 1;
        }
        else if (view.getId() == R.id.imageButton26)
        {
            day = 2;
        }
        else if (view.getId() == R.id.imageButton27)
        {
            day = 3;
        }
        else if (view.getId() == R.id.imageButton28)
        {
            day = 4;
        }
        else if (view.getId() == R.id.imageButton29)
        {
            day = 5;
        }
        else if (view.getId() == R.id.imageButton30)
        {
            day = 6;
        }
        setContentView(R.layout.moreinfo);

        ImageView cloudy = findViewById(R.id.imageView3);
        int greyness = 0;
        ArrayList<Integer> reds = new ArrayList<>();
        ArrayList<Integer> blues = new ArrayList<>();
        ArrayList<Integer> greens = new ArrayList<>();
        reds.add(83);
        reds.add(118);
        reds.add(152);
        reds.add(192);
        greens.add(108);
        greens.add(136);
        greens.add(165);
        greens.add(198);
        blues.add(181);
        blues.add(187);
        blues.add(192);
        blues.add(203);
        try {
            greyness = (wcode.getInt(day));
            if  (greyness > 3)
                greyness = 3;
        }
        catch (JSONException nothandling)
        {

        }
        cloudy.setColorFilter(Color.argb(255, reds.get(greyness), greens.get(greyness), blues.get(greyness)));
        TextView datey = findViewById(R.id.textView);
        datey.setText(savedates.get(day));
        LineChart tempschart = (LineChart) findViewById(R.id.line_chart);
        LineChart rainchart = (LineChart) findViewById(R.id.line_chart2);
        LineChart windchart = (LineChart) findViewById(R.id.line_chart3);
        List<Entry> tempentries = new ArrayList<>();
        List<Entry> windentries = new ArrayList<>();
        List<Entry> rainentries = new ArrayList<>();
        for (int i = day*24; i < (day*24)+24; i++)
        {
            try {
            tempentries.add(new Entry(Float.parseFloat(timehours.getString(i).substring(11,13)),(float) temps.getDouble(i)));
            rainentries.add(new Entry(Float.parseFloat(timehours.getString(i).substring(11,13)),(float) probabilityhr.getDouble(i)));
            windentries.add(new Entry(Float.parseFloat(timehours.getString(i).substring(11,13)),(float) windyhair.getDouble(i)));
            }
            catch (JSONException nothandling)
            {

            }
        }
        LineDataSet tempdataSet = new LineDataSet(tempentries, "hourly temps");
        LineDataSet raindataSet = new LineDataSet(rainentries, "hourly probability of Rain");
        LineDataSet winddataSet = new LineDataSet(windentries, "hourly wind speeds");
        LineData templineData = new LineData(tempdataSet);
        LineData rainlineData = new LineData(raindataSet);
        LineData windlineData = new LineData(winddataSet);
        Description balls = new Description();
        balls.setText("Hourly Temperatures (°F)");
        Description balls2 = new Description();
        balls2.setText("Hourly Probability of Rain (%)");
        Description balls3 = new Description();
        balls3.setText("Hourly Wind Speeds (mph)");
        tempschart.setDescription(balls);
        tempschart.getLegend().setEnabled(false);
        rainchart.setDescription(balls2);
        rainchart.getLegend().setEnabled(false);
        windchart.setDescription(balls3);
        windchart.getLegend().setEnabled(false);
        tempschart.setData(templineData);
        rainchart.setData(rainlineData);
        windchart.setData(windlineData);
        tempschart.invalidate();
        rainchart.invalidate();
        windchart.invalidate();


    }

    public void exittomain(View view) {
        setContentView(R.layout.activity_main);
        tempdays.clear();
        days.clear();
        days.add((TextView) findViewById(R.id.textView2));
        days.add((TextView) findViewById(R.id.textView3));
        days.add((TextView) findViewById(R.id.textView4));
        days.add((TextView) findViewById(R.id.textView7));
        days.add((TextView) findViewById(R.id.textView9));
        days.add((TextView) findViewById(R.id.textView11));
        days.add((TextView) findViewById(R.id.textView12));
        tempdays.add((TextView) findViewById(R.id.textView13));
        tempdays.add((TextView) findViewById(R.id.textView14));
        tempdays.add((TextView) findViewById(R.id.textView15));
        tempdays.add((TextView) findViewById(R.id.textView16));
        tempdays.add((TextView) findViewById(R.id.textView17));
        tempdays.add((TextView) findViewById(R.id.textView18));
        tempdays.add((TextView) findViewById(R.id.textView19));
        for (int i = 0; i < tempdays.size(); i++) {
            tempdays.get(i).setText(savetemps.get(i));
            days.get(i).setText(savedates.get(i));
        }
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}