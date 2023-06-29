package com.thesis.amaff.ui.dashboard;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.thesis.amaff.R;
import com.thesis.amaff.adapters.CropAdapter;
import com.thesis.amaff.databinding.FragmentDashboardBinding;
import com.thesis.amaff.models.Crop;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.util.ArrayList;
import java.util.List;

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

        firestore = FirebaseFirestore.getInstance();
        fetchCropsFromFirestore();
    }

    private void fetchCropsFromFirestore() {
        firestore.collection("Crops").get().addOnSuccessListener(queryDocumentSnapshots -> {
            cropList.clear();
            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Crop crop = documentSnapshot.toObject(Crop.class);
                if(crop.getVariation().isEmpty()){
                    if (!cropList.contains(crop)) {
                        cropList.add(crop);
                        cropAdapter.notifyItemInserted(cropList.size());
                    }
                }
                else {
                    for (String variation :
                            crop.getVariation()) {
                        Crop cropVariety = new Crop();
                        cropVariety.setName(crop.getName());
                        cropVariety.setDescription(crop.getDescription());
                        cropVariety.setVariety(variation);
                        cropVariety.setIconUrl(crop.getIconUrl());
                        cropList.add(cropVariety);
                        cropAdapter.notifyItemInserted(cropList.size());
                    }
                }

            }
        }).addOnFailureListener(e -> Log.e("Firestore", "Error fetching crops: " + e.getMessage()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}