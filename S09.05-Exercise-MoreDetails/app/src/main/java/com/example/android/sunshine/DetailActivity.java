/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.android.sunshine;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.sunshine.data.WeatherContract;
import com.example.android.sunshine.utilities.SunshineDateUtils;
import com.example.android.sunshine.utilities.SunshineWeatherUtils;

public class DetailActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<Cursor> {

    /*
     * In this Activity, you can share the selected day's forecast. No social sharing is complete
     * without using a hashtag. #BeTogetherNotTheSame
     */
    private static final String FORECAST_SHARE_HASHTAG = " #SunshineApp";

    private static final String[] PROJECTION = {
            WeatherContract.WeatherEntry.COLUMN_DATE,
            WeatherContract.WeatherEntry.COLUMN_WEATHER_ID,
            WeatherContract.WeatherEntry.COLUMN_MAX_TEMP,
            WeatherContract.WeatherEntry.COLUMN_MIN_TEMP,
            WeatherContract.WeatherEntry.COLUMN_HUMIDITY,
            WeatherContract.WeatherEntry.COLUMN_WIND_SPEED,
            WeatherContract.WeatherEntry.COLUMN_DEGREES,
            WeatherContract.WeatherEntry.COLUMN_PRESSURE,

    };

    public static final int INDEX_WEATHER_DATE = 0;
    public static final int INDEX_WEATHER_CONDITION_ID = 1;
    public static final int INDEX_WEATHER_MAX_TEMP = 2;
    public static final int INDEX_WEATHER_MIN_TEMP = 3;
    public static final int INDEX_WEATHER_HUMIDITY = 4;
    public static final int INDEX_WEATHER_WIND_SPEED = 5;
    public static final int INDEX_WEATHER_WIND_DEGREES = 6;
    public static final int INDEX_WEATHER_PRESSURE = 7;

    private static final int ID_WEATHER_LOADER = 55;

    /* A summary of the forecast that can be shared by clicking the share button in the ActionBar */
    private String mForecastSummary;

    private Uri mUri;

    private TextView mWeatherDate;
    private TextView mWeatherDescription;
    private TextView mWeatherTempHigh;
    private TextView mWeatherTempLow;
    private TextView mWeatherHumidity;
    private TextView mWeatherWind;
    private TextView mWeatherPressure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mWeatherDate = (TextView) findViewById(R.id.tv_weather_date);
        mWeatherDescription = (TextView) findViewById(R.id.tv_weather_description);
        mWeatherTempHigh = (TextView) findViewById(R.id.tv_weather_temperature_high);
        mWeatherTempLow = (TextView) findViewById(R.id.tv_weather_temperature_low);
        mWeatherHumidity = (TextView) findViewById(R.id.tv_weather_humidity);
        mWeatherWind = (TextView) findViewById(R.id.tv_weather_wind);
        mWeatherPressure = (TextView) findViewById(R.id.tv_weather_pressure);

        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity != null) {
            Uri uri = intentThatStartedThisActivity.getData();
            if (uri == null) {
                throw new NullPointerException("Uri required!");
            }
            mUri = uri;
        }

        getSupportLoaderManager().initLoader(ID_WEATHER_LOADER, null, this);
    }

    /**
     * This is where we inflate and set up the menu for this Activity.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.detail, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    /**
     * Callback invoked when a menu item was selected from this Activity's menu. Android will
     * automatically handle clicks on the "up" button for us so long as we have specified
     * DetailActivity's parent Activity in the AndroidManifest.
     *
     * @param item The menu item that was selected by the user
     * @return true if you handle the menu click here, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /* Get the ID of the clicked item */
        int id = item.getItemId();

        /* Settings menu item clicked */
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        /* Share menu item clicked */
        if (id == R.id.action_share) {
            Intent shareIntent = createShareForecastIntent();
            startActivity(shareIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Uses the ShareCompat Intent builder to create our Forecast intent for sharing.  All we need
     * to do is set the type, text and the NEW_DOCUMENT flag so it treats our share as a new task.
     * See: http://developer.android.com/guide/components/tasks-and-back-stack.html for more info.
     *
     * @return the Intent to use to share our weather forecast
     */
    private Intent createShareForecastIntent() {
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain")
                .setText(mForecastSummary + FORECAST_SHARE_HASHTAG)
                .getIntent();
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if (id == ID_WEATHER_LOADER) {
            return new CursorLoader(this, mUri, PROJECTION, null, null, null);
        }
        throw new RuntimeException("Loader Not Implemented: " + id);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if ((data != null) && data.moveToFirst()) {
            final Context context = this;

            long dateInMillis = data.getLong(INDEX_WEATHER_DATE);
            String dateString = SunshineDateUtils.getFriendlyDateString(context, dateInMillis, false);
            mWeatherDate.setText(dateString);

            int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
            String description = SunshineWeatherUtils.getStringForWeatherCondition(context, weatherId);
            mWeatherDescription.setText(description);

            double highInCelsius = data.getDouble(INDEX_WEATHER_MAX_TEMP);
            mWeatherTempHigh.setText(SunshineWeatherUtils.formatTemperature(context, highInCelsius));

            double lowInCelsius = data.getDouble(INDEX_WEATHER_MIN_TEMP);
            mWeatherTempLow.setText(SunshineWeatherUtils.formatTemperature(context, lowInCelsius));

            double humidity = data.getDouble(INDEX_WEATHER_HUMIDITY);
            mWeatherHumidity.setText(context.getString(R.string.format_humidity, humidity));

            float wind = data.getFloat(INDEX_WEATHER_WIND_SPEED);
            float windDegrees = data.getFloat(INDEX_WEATHER_WIND_DEGREES);
            mWeatherWind.setText(SunshineWeatherUtils.getFormattedWind(context, wind, windDegrees));

            double pressure = data.getDouble(INDEX_WEATHER_PRESSURE);
            mWeatherPressure.setText(context.getString(R.string.format_pressure, pressure));

            mForecastSummary = dateString + " - " + description + " - " + SunshineWeatherUtils.formatHighLows(context, highInCelsius, lowInCelsius);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}