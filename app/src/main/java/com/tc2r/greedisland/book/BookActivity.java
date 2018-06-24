package com.tc2r.greedisland.book;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.AboutActivity;
import com.tc2r.greedisland.MainActivity;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.SettingsActivity;
import com.tc2r.greedisland.utils.Globals;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

	// Initialize Objects.
	private TextView tutText;
	private RelativeLayout tutorial;

	// Initialize Variables.
	private boolean tutorialPreference, bookTut, firsttime;
	;
	private String hunterName;
	private int tutorialCounter = 4;

	// Initialize Shared Preferences.
	private SharedPreferences sharedPreferences;
	private ShareActionProvider mShareActionProvider;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

		if (key.equals(getString(R.string.pref_hunter_name_key))) {
			hunterName = this.sharedPreferences.getString(getString(R.string.pref_hunter_name_key), getString(R.string.default_Hunter_ID));

		} else if (key.equals(getString(R.string.pref_theme_selection_key))) {
			Globals.ChangeTheme(this);

		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Check for Shared Preferences.
		getSavedSettings();

		// Set the Content View
		setContentView(R.layout.activity_book);

		// Show Tutorial Only In Some Conditions
		showTutorial();

		// Setup Toolbar
		initToolbar();

	}

	private void initToolbar() {
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_book);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		Adapter adapter = new Adapter(getSupportFragmentManager());
		adapter.addFragment(new BookTab(), "BookTab");
		viewPager.setAdapter(adapter);
		CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
	}

	private void showTutorial() {
		tutorial = (RelativeLayout) findViewById(R.id.book_tutorial);
		tutText = (TextView) findViewById(R.id.tutorial_text);
		tutText.setText(R.string.Tutorial_Book_Text_1);
		tutorialCounter = 0;
		tutorial.setEnabled(false);
		tutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (tutorialCounter) {
					case 0:
						// Second Message
						tutText.setText(R.string.Tutorial_Book_Text_2);
						tutorialCounter++;
						break;
					case 1:
						// THIRD Message
						tutText.setText(R.string.Tutorial_Book_Text_3);
						tutorialCounter++;
						break;
					case 2:
						// FOURTH Message
						tutText.setText(R.string.Tutorial_Book_Text_4);
						tutorialCounter++;
						break;
					case 3:
						// FIFTH Message
						tutText.setText(R.string.Tutorial_Book_Text_5);
						tutorialCounter++;
						break;
					case 4:
						// SIXTH Message
						tutorial.setVisibility(View.GONE);
						tutorial.setEnabled(false);
						SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
						SharedPreferences.Editor editor = settings.edit();
						editor.putBoolean("BookTut", false);
						editor.apply();
						break;
				}
			}
		});

		// Check for Tutorial
		if (firsttime) {
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			SharedPreferences.Editor firstTimeEditor = sharedPreferences.edit();
			firstTimeEditor.putBoolean(getString(R.string.pref_initiate_key), false);
			firstTimeEditor.commit();
		} else if (tutorialPreference && bookTut) {
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			tutorialPreference = sharedPreferences.getBoolean(getString(R.string.pref_first_time_tut_key), false);
		} else {
			tutorial.setVisibility(View.GONE);
			tutorial.setEnabled(false);
		}
	}

	private void getSavedSettings() {
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		firsttime = sharedPreferences.getBoolean(getString(R.string.pref_initiate_key), true);
		tutorialPreference = sharedPreferences.getBoolean(getString(R.string.pref_first_time_tut_key), false);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_hunter_name_key));
		onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_theme_selection_key));
		bookTut = sharedPreferences.getBoolean("BookTut", true);
	}

	@Override
	public boolean onSupportNavigateUp() {
		Intent intent = new Intent(BookActivity.this, MainActivity.class);
		this.startActivity(intent);
		return true;
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
		// Fetch and store ShareActionProvider
		mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
		mShareActionProvider.setShareIntent(sharingIntent);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Context context = getApplicationContext();
		Intent intent;
		switch (id) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				//intent = new Intent(context, MainActivity.class);
				//this.startActivity(intent);
				return true;
			case R.id.action_settings:
				intent = new Intent(context, SettingsActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.action_share:
				Toast.makeText(context, getString(R.string.menu_Share_Title), Toast.LENGTH_SHORT).show();
				intent = new Intent(Intent.ACTION_SEND);
				this.startActivity(intent);
				return true;
			case R.id.action_about:
				Toast.makeText(context, getString(R.string.menu_About_Title), Toast.LENGTH_SHORT).show();
				intent = new Intent(BookActivity.this, AboutActivity.class);
				this.startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		sharedPreferences.registerOnSharedPreferenceChangeListener(this);
		onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_hunter_name_key));
		onSharedPreferenceChanged(sharedPreferences, getString(R.string.pref_theme_selection_key));
	}

	private static class Adapter extends FragmentPagerAdapter {

		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		public Adapter(FragmentManager fm) {
			super(fm);
		}

		public void addFragment(Fragment fragment, String title) {
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

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}
	}

}