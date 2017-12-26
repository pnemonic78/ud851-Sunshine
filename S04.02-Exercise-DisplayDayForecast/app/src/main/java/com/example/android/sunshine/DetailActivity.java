package com.example.android.sunshine;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_WEATHER = "weather";

    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String weatherData = getIntent().getStringExtra(EXTRA_WEATHER);

        TextView textView = (TextView) findViewById(R.id.tv_weather_data);
        textView.setText(weatherData);
    }
}