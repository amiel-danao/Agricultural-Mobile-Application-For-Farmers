package com.thesis.amaff.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.thesis.amaff.R;

import java.util.ArrayList;

public class SampleFragmentPagerAdapter extends FragmentStateAdapter {

    private final Context context;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private int[] imageResId = { R.drawable.wheat, R.drawable.corn, R.drawable.onion };

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = LayoutInflater.from(context).inflate(R.layout.custom_tab_item, null);
        ImageView img = (ImageView) v.findViewById(R.id.tabIcon);
        img.setImageResource(imageResId[position]);
        return v;
    }

    public SampleFragmentPagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, Context context) {
        super(fragmentManager, lifecycle);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragmentList.get(position);
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
    }

    @Override
    public int getItemCount() {
        return fragmentList.size();
    }
}