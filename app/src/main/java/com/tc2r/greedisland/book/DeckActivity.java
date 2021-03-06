package com.tc2r.greedisland.book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.tc2r.greedisland.AboutActivity;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.SettingsActivity;
import com.tc2r.greedisland.restrict.RestrictedFragment;
import com.tc2r.greedisland.spells.SpellsFragment;
import com.tc2r.greedisland.utils.Globals;
import com.tc2r.greedisland.utils.GreedSnackbar;

import java.util.ArrayList;
import java.util.List;


public class DeckActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences setting;
    private boolean[] cardCheck;
    private boolean local = false;
    private Bundle bundle;
    private ViewPager viewPager;
    private TabLayout tabs;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBar;
    private String hunterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        setting.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(setting, getString(R.string.pref_hunter_name_key));
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));

        setContentView(R.layout.deck_main);

        tabs = (TabLayout) findViewById(R.id.tabs);
        toolbar = (Toolbar) findViewById(R.id.toolbar_deck);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
        setSupportActionBar(toolbar);

        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        SetupViewPager(viewPager);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
        tabs.setupWithViewPager(viewPager);
        collapsingToolbarLayout.setTitle("BOOK!");
        tabs.setOnTouchListener(new TabLayout.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                DeckActivity.super.onTouchEvent(event);

                if (event.getAction() == MotionEvent.ACTION_UP) {

                    boolean fullyExpanded = (appBar.getHeight() - appBar.getBottom()) == 0;

                    if (!fullyExpanded) {

                        appBar.setExpanded(true, true);
                    }
                    if (fullyExpanded) {

                        appBar.setExpanded(false, true);
                    }
                }
                return true;
            }
        });

        // Add Page Change Listener to change toolbar title!
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        collapsingToolbarLayout.setTitle("Book!");
                        break;
                    case 1:
                        collapsingToolbarLayout.setTitle("Spells!");
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tabs.setOnClickListener(null);
        viewPager.setOnPageChangeListener(null);
        viewPager.addOnPageChangeListener(null);

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if (key.equals(getString(R.string.pref_hunter_name_key))) {
            //Log.d("Change", "Name!");
            hunterName = setting.getString(getString(R.string.pref_hunter_name_key), getString(R.string.default_Hunter_ID));
        } else if (key.equals(getString(R.string.pref_theme_selection_key))) {
            Globals.ChangeTheme(this);
        }
    }

    // Adds fragments to Tabs
    private void SetupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager(), this);

        // Add 1 Restricted fragment to adapter
        Fragment tempFragment = new RestrictedFragment();
        tempFragment.setArguments(getIntent().getExtras());
        adapter.addFragment(tempFragment, "Book!");

        // Add 1 Spells fragment to adapter
        tempFragment = new SpellsFragment();
        tempFragment.setArguments(getIntent().getExtras());
        adapter.addFragment(tempFragment, "Spells!");


        // Add Adapter to Viewpager
        viewPager.setAdapter(adapter);


    }

//	public static void DisplayCard(){
//		LayoutInflater inflater = LayoutInflater.from()
//		RelativeLayout reward = (RelativeLayout) findViewById(R.id.rewardLayout);
//
//
//
//
//	}

    @Override
    protected void onResume() {
        setting = PreferenceManager.getDefaultSharedPreferences(this);
        setting.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
        super.onResume();
        appBar.setExpanded(false, true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_book, menu);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_Info);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        MenuItem item = menu.findItem(R.id.action_share);

        // Fetch and store Share Action Provider
        ShareActionProvider shareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        shareActionProvider.setShareIntent(sharingIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Context context = getApplicationContext();
        Intent intent;
        switch (id) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                break;
            case R.id.action_settings:

                intent = new Intent(context, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_book:
                intent = new Intent(context, BookActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_share:
                GreedSnackbar.createSnackBar(findViewById(R.id.layout_main), R.string.menu_Share_Title, Snackbar.LENGTH_LONG).show();
                intent = new Intent(Intent.ACTION_SEND);
                startActivity(intent);
                return true;
            case R.id.action_about:
                GreedSnackbar.createSnackBar(findViewById(R.id.layout_main), R.string.menu_About_Title, Snackbar.LENGTH_LONG).show();
                intent = new Intent(context, AboutActivity.class);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Method called to refresh from within viewpager
    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private SparseArray<String> mFragmentTags;
        private FragmentManager mFragmentManager;

        Adapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new SparseArray<String>();
        }

        // Create a method to return tag of a previously created fragment.
        public Fragment getFragment(int position) {
            String tag = mFragmentTags.get(position);
            if (tag == null) return null;
            return mFragmentManager.findFragmentByTag(tag);
        }

        void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);

        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        // Override InstantiateItem to save the tag of the fragment into a Hash map
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            Object obj = super.instantiateItem(container, position);

            if (obj instanceof Fragment) {
                // record the fragment tag here.

                Fragment f = (Fragment) obj;
                String tag = f.getTag();
                mFragmentTags.put(position, tag);
            }
            return obj;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
