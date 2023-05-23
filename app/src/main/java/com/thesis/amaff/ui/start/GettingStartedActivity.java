package com.thesis.amaff.ui.start;

import com.thesis.amaff.R;
import com.thesis.amaff.ui.login.LoginActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

public class GettingStartedActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private GettingStartedPagerAdapter pagerAdapter;
    private Button nextButton;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        // Check if it's the first launch
        boolean isFirstLaunch = sharedPreferences.getBoolean("is_first_launch", true);
        if (isFirstLaunch) {
            setContentView(R.layout.activity_getting_started);

            viewPager = findViewById(R.id.viewPager);
            nextButton = findViewById(R.id.nextButton);

            pagerAdapter = new GettingStartedPagerAdapter(this);
            viewPager.setAdapter(pagerAdapter);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    // Not needed for this example
                }

                @Override
                public void onPageSelected(int position) {
                    if (position == pagerAdapter.getCount() - 1) {
                        nextButton.setText("Start");
                    } else {
                        nextButton.setText("Next");
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {
                    // Not needed for this example
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentPosition = viewPager.getCurrentItem();
                    if (currentPosition == pagerAdapter.getCount() - 1) {
                        // Update first launch status
                        sharedPreferences.edit().putBoolean("is_first_launch", false).apply();
                        startLoginActivity();
                    } else {
                        viewPager.setCurrentItem(currentPosition + 1);
                    }
                }
            });
        } else {
            startLoginActivity();
        }
    }

    private void startLoginActivity() {
        Intent intent = new Intent(GettingStartedActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
