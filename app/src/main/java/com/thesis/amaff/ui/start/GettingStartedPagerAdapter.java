package com.thesis.amaff.ui.start;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.thesis.amaff.R;


public class GettingStartedPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private int[] slideImages = {
            R.drawable.slide1,
            R.drawable.slide1,
            R.drawable.slide1,
            R.drawable.slide1
    };
    private String[] slideCaptions = {
            "Instant Disease Detection",
            "Instant Disease Detection",
            "Instant Disease Detection",
            "Instant Disease Detection",
    };

    public GettingStartedPagerAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return slideImages.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout, container, false);

        ImageView slideImageView = view.findViewById(R.id.slideImageView);
        slideImageView.setImageResource(slideImages[position]);
        TextView caption = view.findViewById(R.id.captionTextView);
        caption.setText(slideCaptions[position]);

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
