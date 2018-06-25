package com.tc2r.greedisland;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.tc2r.greedisland.book.BookActivity;
import com.tc2r.greedisland.utils.AppRater;
import com.tc2r.greedisland.utils.CustomTypefaceSpan;
import com.tc2r.greedisland.utils.EventsManager;
import com.tc2r.greedisland.utils.Globals;
import com.tc2r.greedisland.utils.PerformanceTracking;
import com.tc2r.greedisland.utils.PlayerInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    // statics.
    private static final String HUNTER_FONT_NAME = "hunterxhunter.ttf";
    private static final String CREDIT_FONT_NAME = "creditcard.ttf";

    // Declare Layout Variables
    private RelativeLayout tutorial;
    private TextView tutText, hunterID;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppBarLayout appBar;

    // Declare Variables
    private boolean mainTut, tutorialPreference;
    private String titleCollapsed, titleExpanded, hunterName;
    private int tutorialCounter = 4;
    private int SpanDist;

    private Intent intent;
    private Map<String, String> params;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Check for Shared Preferences.
        PlayerInfo.getInstance().init(this);
        getSavedSettings();

        // Set the Content View
        setContentView(R.layout.activity_main);

        // Check for Ads:
        InitAds();

        // Initiate Variables
        initVariables();

        // Show Tutorial Only In Some Conditions
        showTutorial();

        // Set text on hunter ID card.
        updateHunterCard();

        // Link toolbar with ActionBar
        setSupportActionBar(toolbar);
        setToolbarandViewPager();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // When settings change, update name and them in layout.
        if (key.equals(getString(R.string.pref_hunter_name_key))) {
            //Log.d("Change", "Name!");
            hunterName = PlayerInfo.getInstance().GetHunterName(getApplicationContext());
        }
        if (key.equals(getString(R.string.pref_theme_selection_key))) {
            Globals.ChangeTheme(this);
        }
    }

    private void initVariables() {
        //View root view = findViewById(R.id.RootLayout);
        hunterID = (TextView) findViewById(R.id.tv_hunterID);
        tutorial = (RelativeLayout) findViewById(R.id.welcome_tutorial);
        tutText = (TextView) findViewById(R.id.tutorial_text);

        // INITIALIZE TOOLBAR and Collapsing Layout
        toolbar = (Toolbar) findViewById(R.id.toolbar_book);
        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsingtoolbar);
        appBar = (AppBarLayout) findViewById(R.id.appbar);
    }

    private void InitAds() {
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.
                Builder().addKeyword("Anime").addKeyword("Game").addKeyword("Manga").addKeyword("Social").build();
        adView.loadAd(adRequest);
    }


    private void setToolbarandViewPager() {
        // Set Title and Appearance for collapsing Toolbar Layout
        collapsingToolbarLayout.setTitle(getString(R.string.toolbar_Header_Main_Collasped));
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.boldText);
        titleCollapsed = getString(R.string.toolbar_Header_Main_Expanded);
        titleExpanded = getString(R.string.toolbar_Header_Main_Collasped);

        // Set Listener for appbar
        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isOpen = true;
            int scrollRange = -1;

            // Check status of appbar, change Text based on status.
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                // When Expanded show hunter ID
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbarLayout.setTitle(titleCollapsed);
                    isOpen = true;
                    // When Collapsed, show app Title
                } else if (isOpen) {
                    collapsingToolbarLayout.setTitle(titleExpanded);
                    isOpen = false;
                }
            }
        });

        // Declare and Initialize Viewpager
        final ViewPager viewPager = (ViewPager) findViewById(R.id.main_viewpager);
        SetupViewPager(viewPager);

        // Set On Page Change Listener for viewpager so we can change title text and stuff.
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        collapsingToolbarLayout.setTitle(getString(R.string.toolbar_Header_Main_Collasped));
                        titleExpanded = getString(R.string.toolbar_Header_Main_Expanded);
                        titleCollapsed = getString(R.string.toolbar_Header_Main_Collasped);

                        break;
                    case 1:
                        collapsingToolbarLayout.setTitle(getString(R.string.toolbar_Header_Credits_Collasped));
                        titleExpanded = getString(R.string.toolbar_Header_Credits_Expanded);
                        titleCollapsed = getString(R.string.toolbar_Header_Credits_Collasped);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void updateHunterCard() {
        // Get Fonts from Asset Folder

        Typeface hunterFont = Typeface.createFromAsset(getAssets(), HUNTER_FONT_NAME);
        Typeface creditFont = Typeface.createFromAsset(getAssets(), CREDIT_FONT_NAME);

        // Format TextView and Strings
        hunterName = hunterName.toUpperCase();
        SpannableStringBuilder iD = new SpannableStringBuilder(hunterName + "\n" + hunterName);
        int spanFinal = iD.length();
        iD.setSpan(new CustomTypefaceSpan("", creditFont), 0, SpanDist, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        iD.setSpan(new CustomTypefaceSpan("", hunterFont), SpanDist, spanFinal, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        hunterID.setText(iD);
    }

    /**
     * IF the tutorial setting is enabled, or first run... show tutorial.
     */
    private void showTutorial() {
        tutorialCounter = 0;
        tutText.setText(R.string.Tutorial_Main_Text_1);
        tutorial.setEnabled(false);

        // On Touch, Advance Tutorial.
        tutorial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (tutorialCounter) {
                    case 0:
                        // Second Message
                        tutText.setText(R.string.Tutorial_Main_Text_2);
                        tutorialCounter++;
                        break;
                    case 1:
                        // Third Message
                        tutText.setText(R.string.Tutorial_Main_Text_3);
                        tutorialCounter++;
                        break;
                    case 2:
                        // Fourth Message
                        tutText.setText(R.string.Tutorial_Main_Text_4);
                        tutorialCounter++;
                        break;
                    case 3:
                        // Fifth Message
                        tutText.setText(R.string.Tutorial_Main_Text_5);
                        tutorialCounter++;
                        break;
                    case 4:
                        // Remove Tutorial Layout.
                        tutorial.setVisibility(View.GONE);
                        tutorial.setEnabled(false);

                        // Update Settings.
                        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putBoolean("MainTut", false);
                        editor.apply();


                        // Go to settings on first run, this way the user will create a custom userName.
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        });

        // Check to see if we should display tutorial to player, if PlayerInfo.GetFirstRun is false, skip tutorial
        // if it is true or if the menu setting is enabled, show tutorial.
        if (PlayerInfo.getInstance().GetFirstRun(this, true)) {

            tutorial.setVisibility(View.VISIBLE);
            tutorial.bringToFront();
            tutorial.setEnabled(true);
            PlayerInfo.getInstance().SetFirstRun(this, false);
        } else if (tutorialPreference && mainTut) {

            tutorial.setVisibility(View.VISIBLE);
            tutorial.bringToFront();
            tutorial.setEnabled(true);
            tutorialPreference = PlayerInfo.getInstance().GetTutRan(this);
        } else {
            tutorial.setVisibility(View.GONE);
            tutorial.setEnabled(false);
        }
    }


    /**
     * Checks for and uses Saved Shared Preferences of settings.
     */
    private void getSavedSettings() {
        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this);


        //Check for changed listener in case layout needs updating.
        onSharedPreferenceChanged(setting, getString(R.string.pref_hunter_name_key));
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
        tutorialPreference = PlayerInfo.getInstance().GetTutRan(this);

        setting.registerOnSharedPreferenceChangeListener(this);

        // If first run, create random name and assign it to Hunter Name temporary.
        PlayerInfo.getInstance().SetRandomName(this);
        hunterName = PlayerInfo.getInstance().GetHunterName(this);
        mainTut = setting.getBoolean("MainTut", true);

        // Check bundle for first launch Initiator.
        Bundle b = getIntent().getExtras();
        if (b != null) {
            boolean init = b.getBoolean("init", false);

            if (init) {
                PerformanceTracking.TrackEvent("GREED ISLAND LAUNCHED");
                // Once Per Launch Events
                AppRater.app_launched(this);
                InitiateUser();
            }
        }
        SpanDist = hunterName.length();
    }


    // Method to Initialize and Fill View Pager
    private void SetupViewPager(ViewPager viewPager) {
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new FrontFragment(), getString(R.string.app_name));
        adapter.addFragment(new CreditsFragment(), getString(R.string.title_activity_credits));
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        //Setup Share Button.
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = getString(R.string.share_Info);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);

        // Fetch and store ShareActionProvider
        MenuItem item = menu.findItem(R.id.action_share);
        ShareActionProvider mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
        mShareActionProvider.setShareIntent(sharingIntent);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_settings:
                intent = new Intent(MainActivity.this, SettingsActivity.class);
                this.startActivity(intent);
                return true;

            case R.id.action_share:
                intent = new Intent(Intent.ACTION_SEND);
                this.startActivity(intent);
                return true;

            case R.id.action_about:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                this.startActivity(intent);
                return true;

            case R.id.action_book:
                intent = new Intent(MainActivity.this, BookActivity.class);
                this.startActivity(intent);
                return true;
            case android.R.id.home:
                super.onBackPressed();
                return true;
        }
        intent = null;
        return super.onOptionsItemSelected(item);
    }


    /**
     * Checks for internet connectivity before making a request to a private
     * database to add a new user, or get the user's name.
     */
    private void InitiateUser() {
        int huntID = PlayerInfo.getInstance().GetHunterID(this);
        RequestQueue requestQueue;
        String url = "http://tchost.000webhostapp.com/UserRegister.php";

        if (huntID == 0) {
            if (!Globals.isNetworkAvailable(this)) {
                Toast.makeText(this, R.string.internet_down_message, Toast.LENGTH_LONG).show();
            } else {
                PerformanceTracking.TransactionBegin("REGISTER USER: " + url);
                // First Run, Initiate Things.
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PerformanceTracking.TransactionEnd("REGISTER USER");
                        Toast.makeText(getApplicationContext(), R.string.successful_Registration, Toast.LENGTH_SHORT).show();

                        // Artificially Inflating IDs by 2k to imply fullness of app.
                        int temp = Integer.parseInt(response) + 20000;

                        // Locally Initialize Hunter Information
                        SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        SharedPreferences.Editor editor = setting.edit();
                        editor.putInt(getString(R.string.pref_hunter_id_key), temp);
                        editor.putString(getString(R.string.pref_current_location_key), getString(R.string.pref_town_default));
                        editor.putString(getString(R.string.pref_current_home_key), getString(R.string.pref_town_default));
                        editor.putString(getString(R.string.pref_lastlocation_key), getString(R.string.pref_town_default));
                        editor.putBoolean(getString(R.string.pref_can_travel_key), true);

                        // Initialize Reward Cards!
                        editor.putInt("Rewards", 0);
                        editor.putBoolean("DailyCards", true);
                        editor.commit();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.server_down_message, Toast.LENGTH_LONG).show();
                        PerformanceTracking.TransactionFail("REGISTER USER: " + error.getLocalizedMessage());
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        params = new HashMap<>();
                        params.put("huntname", hunterName);
                        return params;
                    }
                };

                requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

                // Retrieve HuntID from Database.
                // REGISTER Start Point (Town) as new Base
                stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                        //Log.d("Maps:", " Error: " + new String(error.networkResponse.data));
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        params = new HashMap<>();
                        params.put("oldlocation", getString(R.string.pref_town_default));
                        params.put("travelto", getString(R.string.pref_town_default));
                        params.put("hunterid", String.valueOf(PlayerInfo.getInstance().GetHunterID(getApplicationContext())));
                        params.put("huntername", hunterName);
                        params.put("actiontoken", String.valueOf(3));
                        return params;
                    }
                };
                requestQueue = Volley.newRequestQueue(getApplicationContext());
                requestQueue.add(stringRequest);
            }

        } else {
            // If user is found, get location (town) of user from database.

            if (!Globals.isNetworkAvailable(this)) {
                Toast.makeText(this, R.string.internet_down_message, Toast.LENGTH_LONG).show();

            } else {
                // Get ID and Home Base Location
                huntID = PlayerInfo.getInstance().GetHunterID(this);
                String currentHome = PlayerInfo.getInstance().GetCurrentHome(this);

                url = "http://tchost.000webhostapp.com/gettokens.php?currentlocation=" + currentHome + "&hunterid=" + huntID;

                PerformanceTracking.TransactionBegin("Get Tokens: " + url);
                // First Run, Initiate Things.
                final StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        PerformanceTracking.TransactionEnd("Get Tokens: " + response);
                        // Response is A_I ID Counter
                        int actionToken;
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();

                        try {
                            actionToken = Integer.parseInt(response);
                            EventsManager.UseTokens(getApplicationContext(), actionToken);

                        } catch (NumberFormatException ee) {
                            actionToken = 0;

                        }
                        editor.putInt("ActionToken", actionToken);
                        editor.apply();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), R.string.server_down_message, Toast.LENGTH_LONG).show();
                        PerformanceTracking.TransactionFail("Get Tokens: " + error.getLocalizedMessage());
                    }
                });
                requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_hunter_name_key));
        onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_theme_selection_key));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        android.preference.PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
    }

    private static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList;
        private final List<String> mFragmentTitleList;


        public Adapter(FragmentManager fm) {
            super(fm);
            mFragmentList = new ArrayList<>();
            mFragmentTitleList = new ArrayList<>();
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

