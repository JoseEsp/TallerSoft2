package com.jose.movilizateucn.POJO;

import com.jose.movilizateucn.R;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Query;

public interface RetrofitMaps {

    /*
     * Retrofit get annotation with our URL
     * And our method that will return us details of student.
     */
    @GET("api/directions/json?key=" + R.string.google_maps_key)
    Call<Example> getDistanceDuration(@Query("units") String units, @Query("origin") String origin, @Query("destination") String destination, @Query("mode") String mode);

}
