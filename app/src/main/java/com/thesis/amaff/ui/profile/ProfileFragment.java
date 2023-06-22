package com.thesis.amaff.ui.profile;

import static com.thesis.amaff.utilities.Constants.PROFILE_COLLECTION;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.R;
import com.thesis.amaff.databinding.FragmentProfileBinding;
import com.thesis.amaff.ui.RequireLoginFragment;
import com.thesis.amaff.models.UserProfile;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class ProfileFragment extends RequireLoginFragment {

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



                } else {
                    // Clear the UI if the profile data is null
//                    binding.textLastName.setText("");
                    binding.textEmail.setText("");
                    binding.textDateCreated.setText("");
                    binding.textFirstName.setText("");
                    binding.textLastName.setText("");
                }
            }
        });


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSaveProfile.setOnClickListener(v -> {
            if(!formIsValid())
                return;
            binding.buttonSaveProfile.setEnabled(false);
            profile.setFirstName(binding.textFirstName.getText().toString());
            profile.setLastName(binding.textLastName.getText().toString());
            FirebaseFirestore.getInstance().collection(PROFILE_COLLECTION).document(profile.getUid()).set(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(requireContext(), "Profile was saved successfully", Toast.LENGTH_LONG).show();
                    }

                    binding.buttonSaveProfile.setEnabled(true);
                }
            });
        });
    }

    private boolean formIsValid() {
        if(binding.textLastName.getText().toString().trim().isEmpty())
        {
            binding.textLastName.setError("This field is required");
            return false;
        }

        if(binding.textFirstName.getText().toString().trim().isEmpty())
        {
            binding.textFirstName.setError("This field is required");
            return false;
        }

        return true;
    }

    @Override
    public void onFetchedUser() {
        super.onFetchedUser();
        String emailText = getString(R.string.email_label, profile.getEmail());
        binding.textEmail.setText(emailText);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String formattedDate = sdf.format(profile.getDateCreated().toDate());
        binding.textDateCreated.setText(getString(R.string.date_created_label, formattedDate));

        binding.textFirstName.setText(profile.getFirstName());
        binding.textLastName.setText(profile.getLastName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
