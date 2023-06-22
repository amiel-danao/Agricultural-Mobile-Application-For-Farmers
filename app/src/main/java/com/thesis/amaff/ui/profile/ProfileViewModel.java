package com.thesis.amaff.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.thesis.amaff.models.UserProfile;

public class ProfileViewModel extends ViewModel {

    private final MutableLiveData<UserProfile> userProfile = new MutableLiveData<>();
    private ListenerRegistration profileListener;

    public ProfileViewModel() {
        // Start listening for profile changes when ViewModel is created
        startProfileListener();
    }

    public LiveData<UserProfile> getUserProfile() {
        return userProfile;
    }

    private void startProfileListener() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            profileListener = db.collection("Profiles")
                    .document(uid)
                    .addSnapshotListener((snapshot, exception) -> {
                        if (exception != null) {
                            // Handle any errors
                            return;
                        }

                        if (snapshot != null && snapshot.exists()) {
                            // Retrieve the profile data from the document snapshot
                            UserProfile profile = snapshot.toObject(UserProfile.class);
                            userProfile.setValue(profile);
                        } else {
                            // Document doesn't exist or is empty, clear the profile data
                            userProfile.setValue(null);
                        }
                    });
        }
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        // Stop listening for profile changes when ViewModel is cleared
        if (profileListener != null) {
            profileListener.remove();
        }
    }
}
