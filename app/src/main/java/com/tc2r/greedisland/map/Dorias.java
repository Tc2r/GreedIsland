package com.tc2r.greedisland.map;


import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tc2r.greedisland.R;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dorias extends Fragment implements View.OnClickListener {

	TextView locTitle, location, locDesc;
	ImageView locImage;
	private int id = 6;
	TextView tvTravel, tvHomeSet;
	StringRequest stringRequest;


	// Tracks Current Set Location
	private String currentLocation;
	// Tracks Current Set Home
	private String currentHome;

	// Tracks Last Location before changing to new location.
	private String lastLocation;

	// Gets from Saved Shared Preference
	private String hunterName;
	private int hunterID;
	private int actionToken;
	// saves class name (town) to a variable
	private String thisTown = this.getClass().getSimpleName();
	SharedPreferences userMap;


	// Server Request Stuff
	public static final String url = "https://tchost.000webhostapp.com/settravel.php";
	public static final String deleteUrl = "https://tchost.000webhostapp.com/deletelocation.php";
	private Map<String, String> params;




	public Dorias() {
		// Required empty public constructor
	}

	@SuppressWarnings("ResourceType")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.city_template, container, false);
		location = (TextView) view.findViewById(R.id.tv_location);
		locTitle = (TextView) view.findViewById(R.id.tv_loc_title);
		locDesc = (TextView) view.findViewById(R.id.tv_location_desc);
		locImage = (ImageView) view.findViewById(R.id.iv_location);
		tvTravel = (TextView) view.findViewById(R.id.tv_TRAVEL);
		tvHomeSet = (TextView) view.findViewById(R.id.tv_Home);

		location.setText(view.getResources().getStringArray(R.array.locations)[id]);
		locTitle.setText(view.getResources().getStringArray(R.array.loc_title)[id]);
		locDesc.setText(view.getResources().getStringArray(R.array.loc_desc)[id]);

		TypedArray images = getResources().obtainTypedArray(R.array.loc_image);
		Drawable drawable = images.getDrawable(id);
		locImage.setImageDrawable(drawable);

		tvTravel.setOnClickListener(this);
		tvHomeSet.setOnClickListener(this);
		images.recycle();

		userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
		Log.wtf("MAP:", String.valueOf(userMap.getAll()));
		currentLocation = userMap.getString("CurrentLocation", "FIRST RUN");
		currentHome = userMap.getString("CurrentHome", "NEVER RAN");
		lastLocation = currentLocation;
		SharedPreferences.Editor editor = userMap.edit();
		editor.putString("LastLocation", lastLocation);
		editor.apply();


		hunterName = userMap.getString("Hunter_Name_Pref", "john");
		hunterID = userMap.getInt("HUNT_ID", 0);


		if (currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.GONE);



		} else if (currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.VISIBLE);
			tvTravel.setVisibility(View.GONE);


		} else if (!currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.VISIBLE);


		} else if (!currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.VISIBLE);

		}

		return view;
	}



	@Override
	public void onClick(View v) {
		SharedPreferences.Editor editor;


		switch (v.getId()) {
			case R.id.tv_TRAVEL:

				userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
				SharedPreferences.Editor editor2 = userMap.edit();
				editor2.putString("CurrentLocation", thisTown);
				editor2.apply();

				tvHomeSet.setVisibility(View.VISIBLE);
				tvTravel.setVisibility(View.GONE);
				actionToken = 10;

				registerLocation();

				break;
			case R.id.tv_Home:

				editor = userMap.edit();
				editor.putString("CurrentHome", thisTown);
				editor.commit();
				tvHomeSet.setVisibility(View.GONE);
				break;


		}
	}

	private void registerLocation(){


		// DELETES OLD LOCATION

		stringRequest = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
				Log.d("Maps:", " Error: " + new String(error.networkResponse.data));

			}

		}) {
			@Override
			protected Map<String, String> getParams() {
				params = new HashMap<String, String>();
				params.put("oldlocation", lastLocation);
				params.put("hunterid", String.valueOf(hunterID));
				return params;
			}
		};
		RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
		requestQueue2.add(stringRequest);


		// REGISTERS NEW LOCATION
		stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Toast.makeText(getContext(), response, Toast.LENGTH_LONG).show();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
				//Log.d("Maps:", " Error: " + new String(error.networkResponse.data));

			}

		}) {
			@Override
			protected Map<String, String> getParams() {
				params = new HashMap<String, String>();
				params.put("oldlocation", lastLocation);
				params.put("travelto", thisTown);
				params.put("hunterid", String.valueOf(hunterID));
				params.put("huntername", hunterName);
				params.put("actiontoken", String.valueOf(actionToken));
				return params;
			}
		};
		RequestQueue requestQueue = Volley.newRequestQueue(getContext());

		requestQueue.add(stringRequest);
	}

	@Override
	public void onResume() {

		userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
		lastLocation = userMap.getString("CurrentLocation", "FIRST RUN");
		currentLocation = userMap.getString("CurrentLocation", "NOTWORKING");
		currentHome = userMap.getString("CurrentHome", "NOTWORKING");

		if (currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.GONE);


		} else if (currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.VISIBLE);
			tvTravel.setVisibility(View.GONE);


		} else if (!currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.VISIBLE);


		} else if (!currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.VISIBLE);

		}
		super.onResume();
	}
}
