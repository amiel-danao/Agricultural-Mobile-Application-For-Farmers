package com.thesis.amaff.ui.home;

import static com.thesis.amaff.utilities.Constants.SETTINGS_COLLECTION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.R;
import com.thesis.amaff.databinding.FragmentHomeBinding;
import com.thesis.amaff.models.Weather;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.util.HashMap;

public class HomeFragment extends RequireLoginFragment {

    private FragmentHomeBinding binding;
    private String weatherDocumentKey = "weather";
    HashMap<String, Integer> weatherIconMap = new HashMap<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onFetchedUser() {
        super.onFetchedUser();
        binding.textHome.setText(String.format("%s %s %s", getString(R.string.welcome), profile.getFirstName(), profile.getLastName()));
        weatherIconMap.put("sunny", R.drawable.sunny);
        weatherIconMap.put("cloudy", R.drawable.cloudy);
        weatherIconMap.put("rainy", R.drawable.rainy);
// Add more weather conditions and icons as needed

        fetchWeather();
    }

    private void fetchWeather() {
        FirebaseFirestore.getInstance().collection(SETTINGS_COLLECTION).document(weatherDocumentKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    Weather weather = task.getResult().toObject(Weather.class);
                    binding.textWeatherStatus.setText(String.format("%s \n%s \n%s:%s", getString(R.string.current_weather), weather.status, getString(R.string.temperature),weather.temperature));

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