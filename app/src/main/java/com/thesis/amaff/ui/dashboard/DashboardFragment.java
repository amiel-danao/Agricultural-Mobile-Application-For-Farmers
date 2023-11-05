package com.thesis.amaff.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.thesis.amaff.ApiService;
import com.thesis.amaff.R;
import com.thesis.amaff.adapters.CropAdapter;
import com.thesis.amaff.databinding.FragmentDashboardBinding;
import com.thesis.amaff.models.Crop;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DashboardFragment extends RequireLoginFragment {

    private FragmentDashboardBinding binding;
    private CropAdapter cropAdapter;
    private List<Crop> cropList;
    private FirebaseFirestore firestore;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        DashboardViewModel dashboardViewModel =
                new ViewModelProvider(this).get(DashboardViewModel.class);

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pageTitle = getString(R.string.your_crops);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onFetchedUser() {
        super.onFetchedUser();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        cropList = new ArrayList<>();
        cropAdapter = new CropAdapter(cropList, requireContext());
        binding.recyclerView.setAdapter(cropAdapter);

//        firestore = FirebaseFirestore.getInstance();
        fetchCropsFromServer();
    }

    private void fetchCropsFromServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://amaff-dce3f29acc84.herokuapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ApiService apiService = retrofit.create(ApiService.class);

        // Make the API request
        Call<List<Crop>> call = apiService.getCrops("");
        call.enqueue(new Callback<List<Crop>>() {
            @Override
            public void onResponse(Call<List<Crop>> call, Response<List<Crop>> response) {
                if (response.isSuccessful()) {
                    cropList.clear();
                    List<Crop> crops = response.body();
//                    adapter.setCrops(crops);
                    for (Crop crop: crops) {
                        cropList.add(crop);
//                        cropAdapter.notifyItemInserted(cropList.size());
                    }

                    cropAdapter.notifyDataSetChanged();
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