package com.thesis.amaff.ui.home;

import static com.thesis.amaff.utilities.Constants.SETTINGS_COLLECTION;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.ApiService;
import com.thesis.amaff.PermissionsFragment;
import com.thesis.amaff.R;
import com.thesis.amaff.adapters.SampleFragmentPagerAdapter;
import com.thesis.amaff.databinding.FragmentHomeBinding;
import com.thesis.amaff.models.Crop;
import com.thesis.amaff.models.Weather;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomeFragment extends RequireLoginFragment {

    private FragmentHomeBinding binding;
    private String weatherDocumentKey = "weather";
    HashMap<String, Integer> weatherIconMap = new HashMap<>();

    Button captureBtn;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pageTitle = getString(R.string.title_home);
        super.onViewCreated(view, savedInstanceState);

        SampleFragmentPagerAdapter myAdapter = new SampleFragmentPagerAdapter(getChildFragmentManager(), getLifecycle(), requireContext());

        // add Fragments in your ViewPagerFragmentAdapter class
//        myAdapter.addFragment(new HomeFragment());

        // set Orientation in your ViewPager2
        binding.viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        binding.viewPager.setUserInputEnabled(false);
        binding.viewPager.setAdapter(myAdapter);
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = binding.tabLayout.getTabAt(i);
            tab.setCustomView(myAdapter.getTabView(i));
        }

        updateCropDescription("Rice");

        captureBtn = view.findViewById(R.id.captureBtn);
        captureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PermissionsFragment fragment = new PermissionsFragment();
                requireActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_activity_main, fragment)
                        .commit();
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Tab is selected, do something here

                String selectedDescription = tab.getContentDescription().toString();

                updateCropDescription(selectedDescription);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Tab is unselected
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Tab is reselected (if needed)
            }
        });


    }

    private void updateCropDescription(String selectedCropName) {
        binding.cropDescription.setText("");
        binding.cropHarvest.setText("");
        binding.cropMonths.setText("");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amaff-dce3f29acc84.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API request
        Call<List<Crop>> call = apiService.getCrops(selectedCropName);
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if (response.isSuccessful()) {
                    List<Crop> crops = response.body();

                    if(!crops.isEmpty()) {
                        binding.cropDescription.setText(crops.get(0).getDescription());
                        binding.cropDescription.setVisibility(View.VISIBLE);
                        binding.cropHarvest.setText(crops.get(0).getHarvest());
                        List<String> months = crops.get(0).getMonths();

                        if (months != null && !months.isEmpty()) {
                            // Prepend "Months good for planting" and format the months with new lines
                            StringBuilder monthsStringBuilder = new StringBuilder("Months good for planting:\n");

                            for (String month : months) {
                                monthsStringBuilder.append(month).append("\n");
                            }

                            binding.cropMonths.setText(monthsStringBuilder.toString());
                        } else {
                            // Handle the case where the months list is empty
                            binding.cropMonths.setText("No specific planting months available");
                        }
                    }
                    else{
                        binding.cropDescription.setVisibility(View.GONE);
                    }
//                    cropList.add(cropVariety);

                } else {
                    Log.e("Firestore", response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Crop>> call, Throwable t) {
                // Handle network or other errors
                Log.e("Firestore", "Error fetching crops: " + t.getMessage());
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}