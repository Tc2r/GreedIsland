package com.tc2r.greedisland;

import android.content.Context;
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
import com.tc2r.greedisland.book.BookActivity;
import com.tc2r.greedisland.utils.AppRater;
import com.tc2r.greedisland.utils.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity {


	private boolean firsttime, mainTut;
	private boolean tutorialPreference;
	private Intent intent;
	private String titleCollasped, titleExpanded, hunterName;
	private ShareActionProvider mShareActionProvider;
	private Map<String, String> params;
	private RelativeLayout tutorial;
	private int tutorialCounter = 4;
	private TextView tutText;


	private SharedPreferences setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		SharedPreferences firstPrefer = getSharedPreferences("first_Pref", Context.MODE_PRIVATE);
		firsttime = firstPrefer.getBoolean("first_Pref", true);

		SharedPreferences.Editor firstTimeEditor = firstPrefer.edit();
		setting = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
		tutorialPreference = setting.getBoolean("Tutor_Preference", false);

		SharedPreferences.Editor tempName = setting.edit();
		Random rand = new Random();
		String[] tempNames = getResources().getStringArray(R.array.random_names);
		int n = rand.nextInt(tempNames.length) + 1;
		String tName = tempNames[n];
		tempName.putString("TempName", tName);
		tempName.commit();
		hunterName = setting.getString("Hunter_Name_Pref", tName);
		mainTut = setting.getBoolean("MainTut", true);


		InitiateUser();
		CheckTheme();

		int SpanDist = hunterName.length();


		// Set the Content View
		setContentView(R.layout.activity_main);

		// Check for Ratings:
		AppRater.app_launched(this);


		TextView hunterID = (TextView) findViewById(R.id.tv_hunterID);
		// Show Tutorial Only In Some Conditions
		tutorial = (RelativeLayout) findViewById(R.id.welcome_tutorial);
		tutText = (TextView) findViewById(R.id.tutorial_text);
		tutText.setText(R.string.Tutorial_Main_Text_1);
		tutorialCounter = 0;
		tutorial.setEnabled(false);
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
						// Sixth Message
						tutorial.setVisibility(View.GONE);
						tutorial.setEnabled(false);
						SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("MainTut", false);
						editor.apply();
						break;
				}
			}
		});

		// Check for Tutorial
		if (firsttime){
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			firstTimeEditor.putBoolean("first_Pref", false);
			firstTimeEditor.commit();
		} else if (tutorialPreference && mainTut) {
			tutorial.setVisibility(View.VISIBLE);
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			tutorialPreference = setting.getBoolean("Tutor_Preference", false);
		}else{
			tutorial.setVisibility(View.GONE);
			tutorial.setEnabled(false);
		}


			//tvHunterName = (TextView) findViewById(R.id.tv_hunter_name);
			//tvHunterName.setText("Hunter ID:" + hunterName);
			Typeface hunterFont = Typeface.createFromAsset(getAssets(), "hunterxhunter.ttf");
		Typeface creditFont = Typeface.createFromAsset(getAssets(), "creditcard.ttf");
		hunterName = hunterName.toUpperCase();
		SpannableStringBuilder iD = new SpannableStringBuilder(hunterName + "\n" + hunterName);
		int spanFinal = iD.length();

		iD.setSpan(new CustomTypefaceSpan("", creditFont), 0, SpanDist, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		iD.setSpan(new CustomTypefaceSpan("", hunterFont), SpanDist, spanFinal, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		hunterID.setText(iD);


		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// TOOLBAR CODE

		// INITALIZE TOOLBAR and Collasping Layout
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_book);
		final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsingtoolbar);
		AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
		// Link toolbar with ActionBar
		setSupportActionBar(toolbar);

		// Set Title and Appearance for collapsing Toolbar Layout
		collapsingToolbarLayout.setTitle(getString(R.string.toolbar_Header_Main_Collasped));
		titleCollasped = getString(R.string.toolbar_Header_Main_Expanded);
		titleExpanded = getString(R.string.toolbar_Header_Main_Collasped);
		collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.boldText);

		// Set Listener for appbar
		appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
			boolean isOpen = true;
			int scrollRange = -1;

			@Override
			public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
				if (scrollRange == -1) {
					scrollRange = appBarLayout.getTotalScrollRange();

				}
				// When Expanded show hunter ID
				if (scrollRange + verticalOffset == 0) {
					collapsingToolbarLayout.setTitle(titleCollasped);
					isOpen = true;

					// When Collasped show app Title
				} else if (isOpen) {

					collapsingToolbarLayout.setTitle(titleExpanded);
					isOpen = false;
				}

			}
		});


		// ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
		// VIEW PAGER CODE

		// Declare and Initalize Viewpager
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
						titleCollasped = getString(R.string.toolbar_Header_Main_Collasped);

						break;
					case 1:
						collapsingToolbarLayout.setTitle(getString(R.string.toolbar_Header_Credits_Collasped));
						titleExpanded = getString(R.string.toolbar_Header_Credits_Expanded);
						titleCollasped = getString(R.string.toolbar_Header_Credits_Collasped);
						break;
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});


	}


	// Method to Initalize and Fill View Pager
	private void SetupViewPager(ViewPager viewPager) {
		Adapter adapter = new Adapter(getSupportFragmentManager());
		adapter.addFragment(new FrontFragment(), getString(R.string.app_name));
		adapter.addFragment(new Credits(), getString(R.string.Activity_Credits_Name));
		viewPager.setAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		Intent sharingIntent = new Intent(Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		String sharebody = getString(R.string.share_Info);
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		sharingIntent.putExtra(Intent.EXTRA_TEXT, sharebody);
		MenuItem item = menu.findItem(R.id.action_share);
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
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


				return true;
			case R.id.action_about:
				intent = new Intent(MainActivity.this, AboutActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.action_book:

				intent = new Intent(MainActivity.this, BookActivity.class);
				this.startActivity(intent);
				return true;
		}
		intent = null;
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onResume() {
		super.onResume();
		CheckTheme();
	}

	private void CheckTheme() {
		String customTheme = setting.getString("Theme_Preference", "Fresh Greens");
		switch (customTheme) {
			case "Sunlight":
				setTheme(R.style.AppTheme_Sunlight);
				break;
			case "Bouquets":
				setTheme(R.style.AppTheme_Bouquets);
				break;
			case "Red Wedding":
				setTheme(R.style.AppTheme_RedWedding);
				break;
			case "Royal Flush":
				setTheme(R.style.AppTheme_RoyalF);
				break;
			case "Birds n Berries":
				//Log.wtf("Test", "Birds n Berries");
				setTheme(R.style.AppTheme_BirdBerries);

				break;
			case "Blue Berry":
				//Log.wtf("Test", "Blue Berry!");
				setTheme(R.style.AppTheme_BlueBerry);
				break;
			case "Cinnamon":
				//Log.wtf("Test", "Cinnamon!");
				setTheme(R.style.AppTheme_Cinnamon);
				break;
			case "Day n Night":
				//Log.wtf("Test", "Day n Night");
				setTheme(R.style.AppTheme_Night);
				break;
			case "Earthly":
				//Log.wtf("Test", "Earthly!");
				setTheme(R.style.AppTheme_Earth);
				break;
			case "Forest":
				//Log.wtf("Test", "Forest!");
				setTheme(R.style.AppTheme_Forest);
				break;
			case "Fresh Greens":
				//Log.wtf("Test", "GREENS!");
				setTheme(R.style.AppTheme_Greens);
				break;
			case "Fresh n Energetic":
				//Log.wtf("Test", "Fresh n Energetic");
				setTheme(R.style.AppTheme_Fresh);
				break;
			case "Icy Blue":
				//Log.wtf("Test", "Icy!");
				setTheme(R.style.AppTheme_Icy);
				break;
			case "Ocean":
				//Log.wtf("Test", "Ocean");
				setTheme(R.style.AppTheme_Ocean);
				break;
			case "Play Green/blues":
				//Log.wtf("Test", "Play Green/blues");
				setTheme(R.style.AppTheme_GrnBlu);
				break;
			case "Primary":
				//Log.wtf("Test", "Primary");
				setTheme(R.style.AppTheme_Prime);
				break;
			case "Rain":
				//Log.wtf("Test", "Rain!");
				setTheme(R.style.AppTheme_Rain);
				break;
			case "Tropical":
				//Log.wtf("Test", "Tropical");
				setTheme(R.style.AppTheme_Tropical);
				break;
			default:
				//Log.wtf("Test", "Default");
				setTheme(R.style.AppTheme_Greens);
				break;
		}


	}

	private void InitiateUser() {


		int huntID = setting.getInt("HUNT_ID", 0);

		String url = "https://tchost.000webhostapp.com/UserRegister.php";


		if (huntID == 0) {

			// First Run, Initiate Things.
			StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

				@Override
				public void onResponse(String response) {

					// Response is A_I ID Counter

					Toast.makeText(getApplicationContext(), R.string.successful_Registration, Toast.LENGTH_SHORT).show();


					// Artificially Inflating IDs by 2k to imply fullness of app.
					int temp = Integer.parseInt(response) + 20000;


					// Locally Initialize Hunter Information
					SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
					SharedPreferences.Editor editor = setting.edit();
					editor.putInt("HUNT_ID", temp);
					editor.putString("CurrentLocation", "Start");
					editor.putString("CurrentHome", "Start");
					editor.putString("LastLocation", "Start");
					editor.putBoolean("CanTravel", true);

					// Initialize Reward Cards!
					Long time = new GregorianCalendar().getTimeInMillis() + 1 * 60 * 1000;
					editor.putInt("Rewards", 0);
					editor.putBoolean("DailyCards", true);
					editor.putLong("RewardTime", time);
					editor.commit();


				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
				}
			}) {
				@Override
				protected Map<String, String> getParams() {
					params = new HashMap<>();
					params.put("huntname", hunterName);
					return params;
				}
			};
			RequestQueue requestQueue = Volley.newRequestQueue(this);
			requestQueue.add(stringRequest);

			// Retrieve HuntID from Database.


		} else {

			// Regular Runs.


		}
	}

	private static class Adapter extends FragmentPagerAdapter {
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();


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

