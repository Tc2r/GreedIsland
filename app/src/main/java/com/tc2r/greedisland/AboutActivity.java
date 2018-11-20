package com.tc2r.greedisland;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.tc2r.greedisland.about.AboutCourtneyFragment;
import com.tc2r.greedisland.about.AboutDanielFragment;
import com.tc2r.greedisland.about.AboutDreFragment;
import com.tc2r.greedisland.about.AboutMuniabFragment;

import java.util.ArrayList;
import java.util.List;

public class AboutActivity extends AppCompatActivity {

    // Declare Layout Variables
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SetupViewPager(viewPager);
    }

    private void SetupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        // ADD FRAGMENTS HERE:
        adapter.addFragment(new AboutDanielFragment(), "Daniel");
        adapter.addFragment(new AboutMuniabFragment(), "Munaib");
        adapter.addFragment(new AboutCourtneyFragment(), "Courtney");
        adapter.addFragment(new AboutDreFragment(), "Dre");
        viewPager.setAdapter(adapter);
    }

    private static class Adapter extends FragmentPagerAdapter {
        private List<Fragment> mFragmentList = new ArrayList<>();
        private List<String> mFragmentTitleList = new ArrayList<>();


        public Adapter(FragmentManager fm) {
            super(fm);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();

        }
    }

}


