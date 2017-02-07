package com.tc2r.greedisland.map;


import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc2r.greedisland.R;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class StartingPoint extends Fragment {

	TextView locTitle, location, locDesc;
	ImageView locImage;
	int id = 0;

	private String currentLocation, currentHome, hunterName, thistown;
	private String city = this.getClass().getSimpleName();
	SharedPreferences userMap;
	private int hunterID;
	public static final String url = "https://tchost.000webhostapp.com/settravel.php";
	private Map<String, String> params;

	public StartingPoint() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup vg,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment

		View view = (View) inflater.inflate(R.layout.city_template, vg, false);
		id = 0;
		location = (TextView) view.findViewById(R.id.tv_location);
		locTitle = (TextView) view.findViewById(R.id.tv_loc_title);
		locDesc = (TextView) view.findViewById(R.id.tv_location_desc);
		locImage = (ImageView) view.findViewById(R.id.iv_location);


		location.setText(view.getResources().getStringArray(R.array.locations)[id]);
		locTitle.setText(view.getResources().getStringArray(R.array.loc_title)[id]);
		locDesc.setText(view.getResources().getStringArray(R.array.loc_desc)[id]);

		TypedArray images = getResources().obtainTypedArray(R.array.loc_image);
		Drawable drawable = images.getDrawable(id);
		locImage.setImageDrawable(drawable);
		images.recycle();
		return view;
	}

}
