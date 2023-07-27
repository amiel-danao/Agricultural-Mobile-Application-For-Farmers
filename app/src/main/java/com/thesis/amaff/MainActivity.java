package com.thesis.amaff;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.thesis.amaff.databinding.ActivityMainBinding;
import com.thesis.amaff.ui.dashboard.DashboardFragment;
import com.thesis.amaff.ui.home.HomeFragment;
import com.thesis.amaff.ui.login.LoginActivity;
import com.thesis.amaff.ui.profile.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private ActivityMainBinding binding;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        binding.myToolbar.setTitle(getString(R.string.app_short_name));


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile, R.id.navigation_weather)
                .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(this);

        FirebaseAuth.getInstance().addAuthStateListener(firebaseAuth -> {
            if (firebaseAuth.getCurrentUser() == null) {
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_logout) {
            // Perform logout action here
            // For example, sign out the user
            FirebaseAuth.getInstance().signOut();
            // Redirect to login or any other desired action
            // Optional: Close the current activity
        } else if (id == R.id.action_weather) {
            navController.navigate(R.id.navigation_weather);
        } else {
            navController.navigate(R.id.navigation_about);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.navigation_home:
                navController.navigate(R.id.navigation_home);
                return true;
            case R.id.navigation_dashboard:
                navController.navigate(R.id.navigation_dashboard);
                return true;
            case R.id.navigation_profile:
                navController.navigate(R.id.navigation_profile);
                return true;
            case R.id.navigation_weather:
                navController.navigate(R.id.navigation_weather);
                return true;
            case R.id.navigation_about:
                navController.navigate(R.id.navigation_about);
                return true;
        }
        return false;
    }
}