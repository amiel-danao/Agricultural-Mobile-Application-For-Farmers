package com.thesis.amaff.ui.profile;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.thesis.amaff.R;
import com.thesis.amaff.databinding.FragmentProfileBinding;
import com.thesis.amaff.ui.models.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;
    private ProfileViewModel profileViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        profileViewModel.getUserProfile().observe(getViewLifecycleOwner(), new Observer<UserProfile>() {
            @Override
            public void onChanged(UserProfile userProfile) {
                if (userProfile != null) {

                    // Update other TextViews with corresponding profile data
//                    TextView firstNameTextView = binding.textFirstName;
//                    TextView lastNameTextView = binding.textLastName;
                    TextView emailTextView = binding.textEmail;
                    TextView dateCreatedTextView = binding.textDateCreated;

//                    String firstNameText = getString(R.string.first_name_label, userProfile.getFirstName());
//                    firstNameTextView.setText(firstNameText);

//                    String lastNameText = getString(R.string.last_name_label, userProfile.getLastName());
//                    lastNameTextView.setText(lastNameText);

                    String emailText = getString(R.string.email_label, userProfile.getEmail());
                    emailTextView.setText(emailText);

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    String formattedDate = sdf.format(userProfile.getDateCreated().toDate());
                    dateCreatedTextView.setText(getString(R.string.date_created_label, formattedDate));

                } else {
                    // Clear the UI if the profile data is null
//                    binding.textLastName.setText("");
                    binding.textEmail.setText("");
                    binding.textDateCreated.setText("");
                }
            }
        });


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
