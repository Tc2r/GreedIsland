package com.tc2r.greedisland.map;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tc2r.greedisland.AboutActivity;
import com.tc2r.greedisland.MainActivity;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.SettingsActivity;
import com.tc2r.greedisland.book.BookActivity;
import com.tc2r.greedisland.utils.Globals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    private ViewPager viewPager;
    private SharedPreferences setting;
    private RelativeLayout tutorial;
    private int tutorialCounter = 4;
    private TextView tutText;
    private boolean tutorialPreference, mapTut;
    private int position = 4;
    private ShareActionProvider mShareActionProvider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        setting.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(setting, getString(R.string.pref_hunter_name_key));
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
        tutorialPreference = setting.getBoolean(getString(R.string.pref_first_time_tut_key), false);
        //hunterName = setting.getString(getString(R.string.pref_hunter_name_key), getString(R.string.default_Hunter_ID));
        mapTut = setting.getBoolean("MapTut", true);
        Boolean firstTime = setting.getBoolean(getString(R.string.pref_initiate_key), true);

        // Auto Set Viewpager Screen
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            position = extras.getInt("viewpager_position");
        }


        // DRAW TO SCREEN
        setContentView(R.layout.activity_map);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        final ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        // Check for Ads;
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addKeyword("Anime").addKeyword("Game").addKeyword("Manga").addKeyword("Social").build();
        adView.loadAd(adRequest);

        // Show Tutorial Only In Some Conditions
        tutorial = (RelativeLayout) findViewById(R.id.map_tutorial);
        tutText = (TextView) findViewById(R.id.tutorial_text);
        tutText.setText(R.string.Tutorial_Map_Text_1);
        tutorial.bringToFront();
        tutorialCounter = 0;
        tutorial.setEnabled(false);
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (tutorialCounter) {
                    case 0:
                        // First Message
                        tutText.setText(R.string.Tutorial_Map_Text_2);
                        tutorialCounter++;
                        break;
                    case 1:
                        // Second Message
                        tutText.setText(R.string.Tutorial_Map_Text_3);
                        tutorialCounter++;
                        break;
                    case 2:
                        // Third Message
                        tutText.setText(R.string.Tutorial_Map_Text_4);
                        tutorialCounter++;
                        break;
                    case 3:
                        // Fourth Message
                        tutText.setText(R.string.Tutorial_Map_Text_5);
                        tutorialCounter++;
                        break;
                    case 4:
                        // Fifth Message
                        tutorial.setVisibility(View.GONE);
                        tutorial.setEnabled(false);
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("MapTut", false);
                        editor.apply();
                        break;
                }
            }
        });
        // Check for Tutorial
        if (firstTime) {
            tutorial.setVisibility(View.VISIBLE);
            tutorial.bringToFront();
            tutorial.setEnabled(true);
            SharedPreferences.Editor firstTimeEditor = setting.edit();
            firstTimeEditor.putBoolean(getString(R.string.pref_initiate_key), false);
            firstTimeEditor.apply();

        } else if (tutorialPreference && mapTut) {
            tutorial.setVisibility(View.VISIBLE);
            tutorial.setVisibility(View.VISIBLE);
            tutorial.bringToFront();
            tutorial.setEnabled(true);
            tutorialPreference = setting.getBoolean(getString(R.string.pref_first_time_tut_key), false);

        } else {
            tutorial.setVisibility(View.GONE);
            tutorial.setEnabled(false);

        }

        viewPager = (ViewPager) findViewById(R.id.map_viewpager);
        SetupViewPager(viewPager);


        final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_map);
        collapsingToolbarLayout.setTitle("MAP");
        tabs.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ////Log.d("This", "OnPageSelected");
                ////Log.d("Page: ", String.valueOf(position +" "+ viewPager.getCurrentItem()));

                Fragment fragment = ((Adapter) viewPager.getAdapter()).getFragment(position);

                if (fragment != null) {
                    fragment.onResume();
                }
                //Log.wtf("Location", getResources().getStringArray(R.array.locations)[position]);
                switch (position) {
                    case 0:
                        //Log.wtf("This", "Masadora!");
                        collapsingToolbarLayout.setTitle("Masadora");
                        headerImage.setImageResource(R.drawable.header_masadora);
                        break;
                    case 1:
                        ////Log.d("This", "Soufrabi!");
                        collapsingToolbarLayout.setTitle("Soufrabi");
                        headerImage.setImageResource(R.drawable.header_soufrabi);
                        break;
                    case 2:
                        ////Log.d("This", "Aiai!");
                        collapsingToolbarLayout.setTitle("Aiai");
                        headerImage.setImageResource(R.drawable.header_aiai);
                        break;
                    case 3:
                        ////Log.d("This", "Antokiba!");
                        collapsingToolbarLayout.setTitle("Antokiba");
                        headerImage.setImageResource(R.drawable.header_antokiba);
                        break;
                    case 4:
                        ////Log.d("This", "STARTING TOWN!");
                        collapsingToolbarLayout.setTitle("Starting Point");
                        headerImage.setImageResource(R.drawable.header_startzone);
                        break;
                    case 5:
                        ////Log.d("This", "Rubicuta!");
                        collapsingToolbarLayout.setTitle("Rubicuta");
                        headerImage.setImageResource(R.drawable.header_rubicuta);
                        break;
                    case 6:
                        ////Log.d("This", "Dorias!");
                        collapsingToolbarLayout.setTitle("Dorias");
                        headerImage.setImageResource(R.drawable.header_dorias);
                        break;
                    case 7:
                        ////Log.d("This", "Limeiro!");
                        collapsingToolbarLayout.setTitle("Limeiro");
                        headerImage.setImageResource(R.drawable.header_limeiro);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(MapActivity.this, MainActivity.class);
        this.startActivity(intent);
        return true;
    }

    // Adds fragments to Tabs
    private void SetupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager(), this);

        adapter.addFragment(new Masadora(), "Masadora");
        adapter.addFragment(new Soufrabi(), "Soufrabi");
        adapter.addFragment(new Aiai(), "Aiai");
        adapter.addFragment(new Antokiba(), "Antokiba");
        adapter.addFragment(new Start(), getString(R.string.pref_town_default));
        adapter.addFragment(new Rubicuta(), "Rubicuta");
        adapter.addFragment(new Dorias(), "Dorias");
        adapter.addFragment(new Limeiro(), "Limeiro");
        viewPager.setAdapter(adapter);
        if (position >= 0) viewPager.setCurrentItem(position);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_map, menu);
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_Info);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        MenuItem item = menu.findItem(R.id.action_share);
        // Fetch and store ShareActionProvider
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case android.R.id.home:
                //intent = new Intent(MapActivity.this, MainActivity.class);
                //this.startActivity(intent);
                NavUtils.navigateUpFromSameTask(this);
                onBackPressed();
                return true;
            case R.id.action_settings:
                intent = new Intent(MapActivity.this, SettingsActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_share:
                Toast.makeText(MapActivity.this, R.string.menu_Share_Title, Toast.LENGTH_SHORT).show();
                intent = new Intent(Intent.ACTION_SEND);
                this.startActivity(intent);
                return true;
            case R.id.action_about:
                intent = new Intent(MapActivity.this, AboutActivity.class);
                this.startActivity(intent);
                return true;
            case R.id.action_book:
                Toast.makeText(MapActivity.this, R.string.menu_Book_Title, Toast.LENGTH_SHORT).show();
                intent = new Intent(MapActivity.this, BookActivity.class);
                this.startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(MapActivity.this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setting = PreferenceManager.getDefaultSharedPreferences(MapActivity.this);
        setting.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(setting, getString(R.string.pref_hunter_name_key));
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));

        if (setting.getBoolean(getString(R.string.pref_can_travel_key), false)) {
            // Clear Notifications
            NotificationManager notificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(002);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Toast.makeText(this, "CHANGE", Toast.LENGTH_SHORT).show();
        if (key.equals(getString(R.string.pref_hunter_name_key))) {
            //Log.d("Change", "Name!");
            //hunterName = setting.getString(getString(R.string.pref_hunter_name_key), getString(R.string.default_Hunter_ID));
        } else if (key.equals(getString(R.string.pref_theme_selection_key))) {
            Globals.ChangeTheme(this);
        }
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private final Map<Integer, String> mFragmentTags;
        private final FragmentManager mFragmentManager;
        // --Commented out by Inspection (6/24/18, 11:05 AM):private final Context mContext;

        Adapter(FragmentManager fm, Context context) {
            super(fm);
            mFragmentManager = fm;
            mFragmentTags = new HashMap<Integer, String>();
        }

        //Create a method to return tag of a previously created fragment.
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

        // Override InstantiateItem to save the tag of the fragment into a hash map
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
        public Parcelable saveState() {
            // Do Nothing
            return null;
        }

        @Override
        public void restoreState(Parcelable state, ClassLoader loader) {
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
