package com.thesis.amaff;

import static com.thesis.amaff.utilities.Constants.SETTINGS_COLLECTION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.R;
import com.thesis.amaff.adapters.SampleFragmentPagerAdapter;
import com.thesis.amaff.databinding.FragmentHomeBinding;
import com.thesis.amaff.databinding.FragmentWeatherBinding;
import com.thesis.amaff.models.Weather;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class WeatherFragment extends RequireLoginFragment {

    private @NonNull FragmentWeatherBinding binding;
    private String weatherDocumentKey = "weather";
    HashMap<String, Integer> weatherIconMap = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentWeatherBinding.inflate(inflater, container, false);
        Date date = new Date();

        // Create a SimpleDateFormat object with the desired format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM", Locale.getDefault());

        // Format the date using the SimpleDateFormat object
        String formattedDate = dateFormat.format(date);
        binding.textHome.setText(String.format("%s, %s", getString(R.string.today), formattedDate));
        weatherIconMap.put("sunny", R.drawable.sunny);
        weatherIconMap.put("cloudy", R.drawable.cloudy);
        weatherIconMap.put("rainy", R.drawable.rainy);
        fetchWeather();
        return binding.getRoot();
    }



    private void fetchWeather() {
        FirebaseFirestore.getInstance().collection(SETTINGS_COLLECTION).document(weatherDocumentKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Weather weather = task.getResult().toObject(Weather.class);

                    binding.textWeatherStatus.setText(String.format("%s%sC", weather.temperature, getString(R.string.degree_sign)));
                    binding.textStatus.setText(weather.status);
                    // Check if the weather condition exists in the mapping and update the icon
                    if (weatherIconMap.containsKey(weather.status)) {
                        int iconResource = weatherIconMap.get(weather.status);
                        binding.weatherIconImageView.setImageResource(iconResource);
                    } else {
                        // Default icon or placeholder for unknown conditions
                        binding.weatherIconImageView.setImageResource(R.drawable.baseline_error_24);
                    }
                }
                else{
                    Toast.makeText(requireContext(), "Error fetching weather data!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}