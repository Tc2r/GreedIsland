package com.tc2r.greedisland.map;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.R;
import com.tc2r.greedisland.SettingsActivity;
import com.tc2r.greedisland.book.BookActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MapActivity extends AppCompatActivity {

	private ViewPager viewPager;
	private SharedPreferences setting;
	private RelativeLayout tutorial;
	private int tutorialCounter = 4;
	private TextView tutText;
	private boolean tutorialPreference;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		tutorialPreference = setting.getBoolean("Tutor_Preference", false);
		SharedPreferences firstPrefer = getSharedPreferences("first_Pref", Context.MODE_PRIVATE);
		Boolean firsttime = firstPrefer.getBoolean("first_Pref", true);
		String hunterName = setting.getString("Hunter_Name_Pref", "john");
		String customTheme = setting.getString("Theme_Preference", "Fresh Greens");
		CheckTheme();
		SharedPreferences.Editor firstTimeEditor = firstPrefer.edit();


		/// ~~~ DRAW TO SCREEN
		setContentView(R.layout.activity_map);
		TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
		final ImageView headerImage = (ImageView) findViewById(R.id.headerImage);
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_map);


		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);




		// Show Tutorial Only In Some Conditions
		tutorial = (RelativeLayout) findViewById(R.id.map_tutorial);
		tutText = (TextView) findViewById(R.id.tutorial_text);
		tutText.setText("Welcome to the Island!");
		tutorialCounter = 0;
		tutorial.setEnabled(false);
		tutorial.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				switch (tutorialCounter) {
					case 0:
						// Second Message
						tutText.setText("You are free to travel to different locations, but remember most trips will take a day at least. \n\n");
						tutorialCounter++;
						break;
					case 1:
						// Second Message
						tutText.setText("Some cards are unique to different locations. You'll have to set up base in those locations in order to recieve them.");
						tutorialCounter++;
						break;
					case 2:
						// Second Message
						tutText.setText("(Coming Soon) You may use spell cards on any location you travel to, they will effect players with bases in that location.");
						tutorialCounter++;
						break;
					case 3:
						// Second Message
						tutText.setText("But Beware! Players visiting your base location can steal from you!");
						tutorialCounter++;
						break;
					case 4:
						// Second Message
						tutorial.setVisibility(View.GONE);
						tutorial.setEnabled(false);
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
		}else if(tutorialPreference){
			tutorial.setVisibility(View.VISIBLE);
			tutorial.setVisibility(View.VISIBLE);
			tutorial.bringToFront();
			tutorial.setEnabled(true);
			tutorialPreference = setting.getBoolean("Tutor_Preference", false);
		}else{
			tutorial.setVisibility(View.GONE);
			tutorial.setEnabled(false);
		}

		viewPager = (ViewPager) findViewById(R.id.map_viewpager);
		SetupViewPager(viewPager);


		final CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.ctl_map);
		tabs.setupWithViewPager(viewPager);

		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

			}
			@Override
			public void onPageScrollStateChanged(int state) {
				Log.wtf("This", "OnPageStateChanged");

			}

			@Override
			public void onPageSelected(int position) {
				//Log.wtf("This", "OnPageSelected");
				//Log.wtf("Page: ", String.valueOf(position +" "+ viewPager.getCurrentItem()));

				Fragment fragment = ((Adapter)viewPager.getAdapter()).getFragment(position);

				if (fragment != null)
				{
					fragment.onResume();
				}
				switch(position){
					case 0:
						//Log.wtf("This", "STARTING TOWN!");
						collapsingToolbarLayout.setTitle("Starting Point");
						headerImage.setImageResource(R.drawable.startzone_header);

						break;
					case 1:
						//Log.wtf("This", "STARTING TOWN!");
						collapsingToolbarLayout.setTitle("Masadora");
						headerImage.setImageResource(R.drawable.masadora_header);

						break;
					case 2:
						//Log.wtf("This", "STARTING TOWN!");
						collapsingToolbarLayout.setTitle("Soufrabi");
						headerImage.setImageResource(R.drawable.soufrabi_header);

						break;
					case 3:
						//Log.wtf("This", "STARTING TOWN!");
						collapsingToolbarLayout.setTitle("Aiai");
						headerImage.setImageResource(R.drawable.hisoka_header);

						break;
					case 4:
						//Log.wtf("This", "Antokiba!");
						collapsingToolbarLayout.setTitle("Antokiba");
						headerImage.setImageResource(R.drawable.antokiba_header);

						break;
					case 5:
						//Log.wtf("This", "Rubicuta!");
						collapsingToolbarLayout.setTitle("Rubicuta");
						headerImage.setImageResource(R.drawable.rubicuta_header);

						break;
					case 6:
						//Log.wtf("This", "Dorias!");
						collapsingToolbarLayout.setTitle("Dorias");
						headerImage.setImageResource(R.drawable.dorias_header);

						break;
					case 7:
						//Log.wtf("This", "Limeiro!");
						collapsingToolbarLayout.setTitle("Limeiro");
						headerImage.setImageResource(R.drawable.limeiro_header);

						break;
				}

			}


		});

	}

	// Adds fragments to Tabs
	private void SetupViewPager(ViewPager viewPager) {
		Adapter adapter = new Adapter(getSupportFragmentManager(), this);
		adapter.addFragment(new StartingPoint(), "Start Zone");
		adapter.addFragment(new Masadora(), "Masadora");
		adapter.addFragment(new Soufrabi(), "Soufrabi");
		adapter.addFragment(new Aiai(), "Aiai");
		adapter.addFragment(new Antokiba(), "Antokiba");
		adapter.addFragment(new Rubicuta(), "Rubicuta");
		adapter.addFragment(new Dorias(), "Dorias");
		adapter.addFragment(new Limeiro(), "Limeiro");
		viewPager.setAdapter(adapter);
	}

	private static class Adapter extends FragmentPagerAdapter {
		private Map<Integer, String> mFragmentTags;
		private FragmentManager mFragmentManager;
		private Context mContext;
		private final List<Fragment> mFragmentList = new ArrayList<>();
		private final List<String> mFragmentTitleList = new ArrayList<>();

		@Override
		public CharSequence getPageTitle(int position) {
			return mFragmentTitleList.get(position);
		}

		Adapter(FragmentManager fm, Context context) {
			super(fm);
			mFragmentManager = fm;
			mFragmentTags = new HashMap<Integer, String>();
			mContext = context;
		}


		// Override InstantiateItem to save the tag of the fragment into a hashmap
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Object obj = super.instantiateItem(container, position);

			if(obj instanceof Fragment){
				// record the fragment tag here.

				Fragment f = (Fragment) obj;
				String tag = f.getTag();
				mFragmentTags.put(position, tag);
			}
			return  obj;

		}

		// Create a method to return tag of a previously created fragment.
		public Fragment getFragment(int position) {
			String tag = mFragmentTags.get(position);
			if (tag == null)
				return null;
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

		@Override
		public int getCount() {
			return mFragmentList.size();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_map, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		Intent intent;
		switch (id) {
			case R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.action_settings:
				Toast.makeText(MapActivity.this, "Settings", Toast.LENGTH_SHORT).show();
				intent = new Intent(MapActivity.this, SettingsActivity.class);
				this.startActivity(intent);
				return true;
			case R.id.action_share:
				Toast.makeText(MapActivity.this, "Share", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_about:
				Toast.makeText(MapActivity.this, "About", Toast.LENGTH_SHORT).show();
				return true;
			case R.id.action_book:
				Toast.makeText(MapActivity.this, "Book", Toast.LENGTH_SHORT).show();
				intent = new Intent(MapActivity.this, BookActivity.class);
				this.startActivity(intent);
				return true;
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	protected void onResume() {
		super.onResume();
		CheckTheme();
	}
	private void CheckTheme(){
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
}
