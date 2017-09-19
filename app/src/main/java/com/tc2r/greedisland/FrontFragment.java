package com.tc2r.greedisland;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc2r.greedisland.book.BookActivity;
import com.tc2r.greedisland.utils.TravelHelper;


/**
 * A simple {@link Fragment} subclass.
 */
public class FrontFragment extends Fragment implements View.OnClickListener {

	// Declare Layout Variables
	TextView tvSettings, tvBook, tvGreed;
	TextView statusHunter, statusLang, statusID, statusBase, statusLocation;
	Intent intent;



	public FrontFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_front, container, false);
		tvSettings = (TextView) view.findViewById(R.id.tv_settings);
		tvBook = (TextView) view.findViewById(R.id.tv_book);
		tvGreed = (TextView) view.findViewById(R.id.tv_map);
		ImageView imageSettings = (ImageView) view.findViewById(R.id.image_settings);
		ImageView imageBook = (ImageView) view.findViewById(R.id.image_book);
		ImageView imageGreed = (ImageView) view.findViewById(R.id.image_map);

		statusBase = (TextView) view.findViewById(R.id.statusBaseTown);
		statusHunter = (TextView) view.findViewById(R.id.statusHuntName);
		statusID = (TextView) view.findViewById(R.id.statusIdNumber);
		statusLang = (TextView) view.findViewById(R.id.statusLangName);
		statusLocation= (TextView) view.findViewById(R.id.statusLocation);


		// set listeners
		tvGreed.setOnClickListener(this);
		tvBook.setOnClickListener(this);
		tvSettings.setOnClickListener(this);
		imageBook.setOnClickListener(this);
		imageGreed.setOnClickListener(this);
		imageSettings.setOnClickListener(this);


		// get saved variables.
		SharedPreferences status = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String hunterName = status.getString(getString(R.string.pref_hunter_name_key), status.getString(getString(R.string.pref_hunter_temp_key), getString(R.string.pref_hunter_default_name)));
		String hunterIdNum = String.valueOf(status.getInt(getString(R.string.pref_hunter_id_key), 0));
		String homeTown = status.getString(getString(R.string.pref_current_home_key), getString(R.string.pref_town_default));
		String location = status.getString(getString(R.string.pref_current_location_key), getString(R.string.pref_town_default));

		String huntLang = hunterName;
		String huntTitle = getString(R.string.Main_Hunter_Title) + hunterName;
		String hunttId = getString(R.string.Main_Hunter_Num) + hunterIdNum;
		String huntCurrent = getString(R.string.Main_Hunter_Location) + location;
		String huntHome = getString(R.string.Main_Hunter_Base) + homeTown;

		// Set TextViews.
		statusHunter.setText(huntTitle);
		Typeface hunterFont = Typeface.createFromAsset(getActivity().getAssets(), "hunterxhunter.ttf");
		statusLang.setTypeface(hunterFont);
		statusLang.setText(huntLang);
		statusID.setText(hunttId);
		statusBase.setText(huntHome);
		statusLocation.setText(huntCurrent);

		return view;
}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case (R.id.tv_settings):
			case (R.id.image_settings):
				intent = new Intent(getContext(), SettingsActivity.class);
				startActivity(intent);
				break;
			case (R.id.tv_book):
			case (R.id.image_book):
				intent = new Intent(getContext(), BookActivity.class);
				startActivity(intent);
				break;
			case (R.id.tv_map):
			case (R.id.image_map):
				TravelHelper.ViewTown(getContext(), 1);
				break;
			default:
				break;
		}
	}
}
