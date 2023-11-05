package com.thesis.amaff.ui;

import static com.thesis.amaff.utilities.Constants.EMPLOYEES_COLLECTION;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.thesis.amaff.R;
import com.thesis.amaff.models.Employee;

public abstract class RequireLoginFragment extends Fragment {
    protected Employee profile;
    private FirebaseAuth firebaseAuth;
    protected String pageTitle = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Activity activity = getActivity();
        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.my_toolbar);
        if(toolbar != null)
            toolbar.setTitle(pageTitle);
//        actionBar.setTitle(pageTitle);
        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null){
            return;
        }

        fetchUserData();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchUserData();
    }

    private void fetchUserData(){
        FirebaseFirestore.getInstance().collection(EMPLOYEES_COLLECTION).document(firebaseAuth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    profile = task.getResult().toObject(Employee.class);
                    onFetchedUser();
                }
            }
        });
    }



    public void onFetchedUser(){
    }
}
