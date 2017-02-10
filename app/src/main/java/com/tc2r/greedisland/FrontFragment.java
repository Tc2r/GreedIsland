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

	TextView statusHunter, statusLang, statusID, statusBase, statusLocation;
	TextView tvSettings;
	TextView tvBook;
	TextView tvGreed;
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


		tvGreed.setOnClickListener(this);
		tvBook.setOnClickListener(this);
		tvSettings.setOnClickListener(this);
		imageBook.setOnClickListener(this);
		imageGreed.setOnClickListener(this);
		imageSettings.setOnClickListener(this);


		SharedPreferences status = PreferenceManager.getDefaultSharedPreferences(getActivity());
		String hunterName = status.getString("Hunter_Name_Pref", status.getString("TempName", "Chrollo"));
		String hunterIdNum = String.valueOf(status.getInt("HUNT_ID", 0));
		String homeTown = status.getString("CurrentHome", "Start");
		String location = status.getString("CurrentLocation", "Start");

		String huntTitle = getString(R.string.Main_Hunter_Title) + hunterName;
		String hunttLang = hunterName;
		String hunttId = getString(R.string.Main_Hunter_Num) + hunterIdNum;
		String huntCurrent = getString(R.string.Main_Hunter_Location) + location;
		String huntHome = getString(R.string.Main_Hunter_Base) + homeTown;

		statusHunter.setText(huntTitle);
		Typeface hunterFont = Typeface.createFromAsset(getActivity().getAssets(), "hunterxhunter.ttf");
		statusLang.setTypeface(hunterFont);
		statusLang.setText(hunttLang);
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
				//Log.wtf("CLICKED", "SETTINGS");
				//Toast.makeText(this, "CLICKED SETTINGS", Toast.LENGTH_LONG).show();
				intent = new Intent(getContext(), SettingsActivity.class);
				startActivity(intent);
				break;
			case (R.id.tv_book):
			case (R.id.image_book):
				//Log.wtf("CLICKED", "BOOK");
				//Toast.makeText(this, "CLICKED BOOK", Toast.LENGTH_LONG).show();
				intent = new Intent(getContext(), BookActivity.class);

				startActivity(intent);
				break;
			case (R.id.tv_map):
			case (R.id.image_map):
				//Toast.makeText(this, "CLICKED MAP", Toast.LENGTH_LONG).show();
				TravelHelper.ViewTown(getContext(), 1);
				break;
			default:
				break;
		}
	}
}
