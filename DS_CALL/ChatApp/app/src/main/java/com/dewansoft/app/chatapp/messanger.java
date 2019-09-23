package com.dewansoft.app.chatapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class messanger extends Fragment {
    // TabLayout Instance
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FragmentAdapter fa;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_messanger, container, false);
        // TabLayout Setup
        tabLayout = v.findViewById(R.id.tabs_id);
        viewPager = v.findViewById(R.id.fragmentContainer_id);
        fa = new FragmentAdapter(getFragmentManager());
        viewPager.setAdapter(fa);
        tabLayout.setupWithViewPager(viewPager);
        return v;
    }
}
