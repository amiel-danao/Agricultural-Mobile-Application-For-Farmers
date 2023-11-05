package com.thesis.amaff.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thesis.amaff.ApiService;
import com.thesis.amaff.R;
import com.thesis.amaff.models.Crop;
import com.thesis.amaff.models.Variation;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropViewHolder> {
    private final List<Crop> cropList;
    private final Context context;

    public CropAdapter(List<Crop> cropList, Context context) {
        this.cropList = cropList;
        this.context = context;
    }

    @NonNull
    @Override
    public CropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_crop, parent, false);
        return new CropViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropViewHolder holder, int position) {
        Crop crop = cropList.get(position);
        holder.bind(context, crop);
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public static class CropViewHolder extends RecyclerView.ViewHolder {
        private final TextView nameTextView;
        private final TextView descriptionTextView;
        private final ImageView cropImage;
        private final TextView cropVariationTextView;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            cropImage = itemView.findViewById(R.id.cropImage);
            cropVariationTextView = itemView.findViewById(R.id.textVariations);
        }

        public void bind(Context context, Crop crop) {
            Glide.with(context).load(crop.getIconUrl()).into(cropImage);
            nameTextView.setText(crop.getName());
            descriptionTextView.setText(crop.getDescription());


            fetchVariationsFromServer(crop.getId(), cropVariationTextView);

        }

        private void fetchVariationsFromServer(int crop_id, TextView textView) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://amaff-dce3f29acc84.herokuapp.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            // Make the API request
            Call<List<Variation>> call = apiService.getVariations(crop_id);
            call.enqueue(new Callback<List<Variation>>() {
                @Override
                public void onResponse(Call<List<Variation>> call, Response<List<Variation>> response) {
                    if (response.isSuccessful()) {
                        List<Variation> variations = response.body();

                        if (variations != null && !variations.isEmpty()) {
                            // Prepend "Months good for planting" and format the months with new lines
                            StringBuilder monthsStringBuilder = new StringBuilder("Variations:\n");

                            for (Variation variation : variations) {
                                monthsStringBuilder.append(variation.getName()).append("\n");
                            }

                            textView.setText(monthsStringBuilder.toString());
                        } else {
                            // Handle the case where the months list is empty
                            textView.setText("No variation");
                        }

                    } else {
                        textView.setText(response.message());
                        Log.e("Firestore", response.message());
                    }
                }

                @Override
                public void onFailure(Call<List<Variation>> call, Throwable t) {
                    textView.setText(t.getMessage());
                    // Handle network or other errors
                    Log.e("Firestore", "Error fetching crops: " + t.getMessage());
                }
            });
        }
    }
}