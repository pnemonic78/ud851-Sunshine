/*
 * Copyright (C) 2016 The Android Open Source Project
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
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * {@link ForecastAdapter} exposes a list of weather forecasts
 * from a {@link android.database.Cursor} to a {@link android.support.v7.widget.RecyclerView}.
 */
class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ForecastAdapterViewHolder> {

    private final Context mContext;

    /*
     * Below, we've defined an interface to handle clicks on items within this Adapter. In the
     * constructor of our ForecastAdapter, we receive an instance of a class that has implemented
     * said interface. We store that instance in this variable to call the onClick method whenever
     * an item is clicked in the list.
     */
    final private ForecastAdapterOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface ForecastAdapterOnClickHandler {
        void onClick(String weatherForDay);
    }

    private Cursor mCursor;

    /**
     * Creates a ForecastAdapter.
     *
     * @param context      the context.
     * @param clickHandler The on-click handler for this adapter. This single handler is called
     */
    public ForecastAdapter(Context context, ForecastAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't)
     *                  you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public ForecastAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.forecast_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ForecastAdapterViewHolder(view);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param forecastAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                  contents of the item at the given position in the data set.
     * @param position                  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(ForecastAdapterViewHolder forecastAdapterViewHolder, int position) {
        mCursor.moveToPosition(position);
        final Context context = mContext;
        long date = mCursor.getLong(MainActivity.INDEX_DATE);
        String dateString = DateUtils.formatDateTime(context, date, DateUtils.FORMAT_SHOW_DATE);
        String description = context.getString(getDescription(mCursor.getInt(MainActivity.INDEX_WEATHER_ID)));
        float maxTemp = mCursor.getFloat(MainActivity.INDEX_MAX_TEMP);
        float minTemp = mCursor.getFloat(MainActivity.INDEX_MIN_TEMP);
        String highAndLowTemperature = context.getString(R.string.format_temperature, maxTemp) + "/" + context.getString(R.string.format_temperature, minTemp);
        String weatherSummary = dateString + " - " + description + " - " + highAndLowTemperature;
        forecastAdapterViewHolder.weatherSummary.setText(weatherSummary);
    }

    protected int getDescription(int weatherId) {
        if ((weatherId >= 200) && (weatherId <= 299)) {
            return R.string.condition_2xx;
        }
        if ((weatherId >= 300) && (weatherId <= 399)) {
            return R.string.condition_3xx;
        }
        switch (weatherId) {
            case 500:
                return R.string.condition_500;
            case 501:
                return R.string.condition_501;
            case 502:
                return R.string.condition_502;
            case 503:
                return R.string.condition_503;
            case 504:
                return R.string.condition_504;
            case 511:
                return R.string.condition_511;
            case 520:
                return R.string.condition_520;
            case 521:
                return R.string.condition_521;
            case 522:
                return R.string.condition_522;
            case 531:
                return R.string.condition_531;
            case 600:
                return R.string.condition_600;
            case 601:
                return R.string.condition_601;
            case 602:
                return R.string.condition_602;
            case 611:
                return R.string.condition_611;
            case 612:
                return R.string.condition_612;
            case 615:
                return R.string.condition_615;
            case 616:
                return R.string.condition_616;
            case 620:
                return R.string.condition_620;
            case 621:
                return R.string.condition_621;
            case 622:
                return R.string.condition_622;
            case 701:
                return R.string.condition_701;
            case 711:
                return R.string.condition_711;
            case 721:
                return R.string.condition_721;
            case 731:
                return R.string.condition_731;
            case 741:
                return R.string.condition_741;
            case 751:
                return R.string.condition_751;
            case 761:
                return R.string.condition_761;
            case 762:
                return R.string.condition_762;
            case 771:
                return R.string.condition_771;
            case 781:
                return R.string.condition_781;
            case 800:
                return R.string.condition_800;
            case 801:
                return R.string.condition_801;
            case 802:
                return R.string.condition_802;
            case 803:
                return R.string.condition_803;
            case 804:
                return R.string.condition_804;
            case 900:
                return R.string.condition_900;
            case 901:
                return R.string.condition_901;
            case 902:
                return R.string.condition_902;
            case 903:
                return R.string.condition_903;
            case 904:
                return R.string.condition_904;
            case 905:
                return R.string.condition_905;
            case 906:
                return R.string.condition_906;
            case 951:
                return R.string.condition_951;
            case 952:
                return R.string.condition_952;
            case 953:
                return R.string.condition_953;
            case 954:
                return R.string.condition_954;
            case 955:
                return R.string.condition_955;
            case 956:
                return R.string.condition_956;
            case 957:
                return R.string.condition_957;
            case 958:
                return R.string.condition_958;
            case 959:
                return R.string.condition_959;
            case 960:
                return R.string.condition_960;
            case 961:
                return R.string.condition_961;
            case 962:
                return R.string.condition_962;
            default:
                return R.string.condition_unknown;
        }
    }

    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    /**
     * This method is used to set the weather forecast on a ForecastAdapter if we've already
     * created one. This is handy when we get new data from the web but don't want to create a
     * new ForecastAdapter to display it.
     *
     * @param weatherData The new weather data to be displayed.
     */
    public void swapCursor(Cursor weatherData) {
        mCursor = weatherData;
        if (weatherData != null) {
            // After the new Cursor is set, call notifyDataSetChanged
            notifyDataSetChanged();
        }
    }

    /**
     * A ViewHolder is a required part of the pattern for RecyclerViews. It mostly behaves as
     * a cache of the child views for a forecast item. It's also a convenient place to set an
     * OnClickListener, since it has access to the adapter and the views.
     */
    class ForecastAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView weatherSummary;

        ForecastAdapterViewHolder(View view) {
            super(view);
            weatherSummary = (TextView) view.findViewById(R.id.tv_weather_data);
            view.setOnClickListener(this);
        }

        /**
         * This gets called by the child views during a click.
         *
         * @param v The View that was clicked
         */
        @Override
        public void onClick(View v) {
            String weatherForDay = weatherSummary.getText().toString();
            mClickHandler.onClick(weatherForDay);
        }
    }
}