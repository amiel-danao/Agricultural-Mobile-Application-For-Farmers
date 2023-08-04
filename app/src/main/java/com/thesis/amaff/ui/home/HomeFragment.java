package com.thesis.amaff.ui.home;

import static com.thesis.amaff.utilities.Constants.SETTINGS_COLLECTION;

import android.os.Bundle;
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
import com.thesis.amaff.PermissionsFragment;
import com.thesis.amaff.R;
import com.thesis.amaff.adapters.SampleFragmentPagerAdapter;
import com.thesis.amaff.databinding.FragmentHomeBinding;
import com.thesis.amaff.models.Weather;
import com.thesis.amaff.ui.RequireLoginFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

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
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}