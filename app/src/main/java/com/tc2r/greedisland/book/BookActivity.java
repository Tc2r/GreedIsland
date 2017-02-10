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
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.MainActivity;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.SettingsActivity;

import java.util.ArrayList;
import java.util.List;

public class BookActivity extends AppCompatActivity {
	String hunterName;
	private RelativeLayout tutorial;
	private int tutorialCounter = 4;
	private TextView tutText;
	private boolean tutorialPreference, bookTut;


	private SharedPreferences setting;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		SharedPreferences firstPrefer = getSharedPreferences("first_Pref", Context.MODE_PRIVATE);
		Boolean firsttime = firstPrefer.getBoolean("first_Pref", true);
		tutorialPreference = setting.getBoolean("Tutor_Preference", false);
		bookTut = setting.getBoolean("BookTut", true);

		hunterName = setting.getString("Hunter_Name_Pref", getString(R.string.default_Hunter_ID));
		CheckTheme();
		SharedPreferences.Editor firstTimeEditor = firstPrefer.edit();

		setContentView(R.layout.activity_book);


		// Setup Toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_book);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Show Tutorial Only In Some Conditions
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
			firstTimeEditor.putBoolean("first_Pref", false);
			firstTimeEditor.commit();
		} else if (tutorialPreference && bookTut) {
			tutorial.setVisibility(View.VISIBLE);
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			tutorialPreference = setting.getBoolean("Tutor_Preference", false);
		} else {
			tutorial.setVisibility(View.GONE);
			tutorial.setEnabled(false);
		}


		ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
		Adapter adapter = new Adapter(getSupportFragmentManager());
		adapter.addFragment(new BookTab(), "BookTab");
		viewPager.setAdapter(adapter);


		CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsingtoolbar);
		//collapsingToolbarLayout.setTitle("TEXT HERE?");

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_book, menu);
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
			case R.id.home:
				intent = new Intent(context, MainActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.action_settings:
				Toast.makeText(context, getString(R.string.menu_Settings_Title), Toast.LENGTH_SHORT).show();
				intent = new Intent(context, SettingsActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.action_share:
				Toast.makeText(context, getString(R.string.menu_Share_Title), Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_about:
				Toast.makeText(context, getString(R.string.menu_About_Title), Toast.LENGTH_SHORT).show();
				return true;
		}

		return super.onOptionsItemSelected(item);
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

	@Override
	public void onBackPressed() {
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		super.onBackPressed();
	}

	@Override
	protected void onResume() {
		super.onResume();
		CheckTheme();
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