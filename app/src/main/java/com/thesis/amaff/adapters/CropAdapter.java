package com.thesis.amaff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thesis.amaff.R;
import com.thesis.amaff.models.Crop;

import java.util.List;

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
        private final TextView variationText;
        private final ImageView cropImage;

        public CropViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            variationText = itemView.findViewById(R.id.variationText);
            cropImage = itemView.findViewById(R.id.cropImage);
        }

        public void bind(Context context, Crop crop) {
            Glide.with(context).load(crop.getIconUrl()).into(cropImage);
            nameTextView.setText(crop.getName());
            descriptionTextView.setText(crop.getDescription());
            if(!crop.getVariety().isEmpty()) {
                variationText.setText(String.format("Variation: %s", crop.getVariety()
                ));
            }
            else{
                variationText.setText("");
            }

        }
    }
}