package com.thesis.amaff;

import com.thesis.amaff.models.Crop;
import com.thesis.amaff.models.Variation;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("crops")
    Call<List<Crop>> getCrops(@Query("name") String cropName);

    @GET("variations")
    Call<List<Variation>> getVariations(@Query("crop_id")int cropId);
}
