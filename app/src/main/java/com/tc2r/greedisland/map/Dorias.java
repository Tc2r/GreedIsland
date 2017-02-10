package com.tc2r.greedisland.map;


import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.utils.TravelHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.OkHttpClient;

/**
 * A simple {@link Fragment} subclass.
 */
public class Dorias extends Fragment implements View.OnClickListener {


	// Server Request Stuff
	public static final String url = "https://tchost.000webhostapp.com/settravel.php";
	public static final String deleteUrl = "https://tchost.000webhostapp.com/deletelocation.php";
	TextView locTitle, location, locDesc;
	ImageView locImage;
	TextView tvTravel, tvHomeSet;
	StringRequest stringRequest;
	SharedPreferences userMap;
	private int id = 6;
	// Tracks Current Set Location
	private String currentLocation;
	// Tracks Current Set Home
	private String currentHome;
	// Tracks Last Location before changing to new location.
	private String lastBase;
	// Gets from Saved Shared Preference
	private String hunterName;
	private int hunterID;
	private int actionToken;
	// saves class name (town) to a variable
	private String thisTown = this.getClass().getSimpleName();
	private Map<String, String> params;

	//Recycler View Local Hunters List
	private RecyclerView recyclerView;
	private List<localHunter> listLocalHunters;
	private RecyclerView.Adapter adapter;
	private RecyclerView.LayoutManager layoutManager;
	private ProgressBar progressBar;
	private CardView currentHomeView;

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
		currentHomeView = (CardView) view.findViewById(R.id.currentHomeView);


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
		currentLocation = userMap.getString("CurrentLocation", "FIRST RUN");
		currentHome = userMap.getString("CurrentHome", "NEVER RAN");
		lastBase = userMap.getString("CurrentHome", "FIRST RUN");
		Log.wtf("MAP:", String.valueOf(userMap.getAll()));
		SharedPreferences.Editor editor = userMap.edit();
		editor.putString("LastLocation", lastBase);
		editor.apply();


		hunterName = userMap.getString("Hunter_Name_Pref", getString(R.string.default_Hunter_ID));
		hunterID = userMap.getInt("HUNT_ID", 0);


		boolean canTravel = userMap.getBoolean("CanTravel", false);
		if (currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.GONE);


		} else if (currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.VISIBLE);
			tvTravel.setVisibility(View.GONE);


		} else if (!currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			if (canTravel) {
				tvTravel.setVisibility(View.VISIBLE);
			}

		} else if (!currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			if (canTravel) {
				tvTravel.setVisibility(View.VISIBLE);
			}
		}
		currentHome = userMap.getString("CurrentHome", "NOTWORKING");
		if (currentHome.equals(thisTown)) {
			currentHomeView.setVisibility(View.VISIBLE);
			// Initiate Views
			recyclerView = (RecyclerView) view.findViewById(R.id.localsView);
			recyclerView.setHasFixedSize(true);
			layoutManager = new LinearLayoutManager(view.getContext());
			recyclerView.setLayoutManager(layoutManager);
			progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);

			// Populate Locals List

			listLocalHunters = new ArrayList<>();
			prepareLocalHunters();
			adapter = new localAdapter(view.getContext(), listLocalHunters);
			recyclerView.setAdapter(adapter);
		} else {
			currentHomeView.setVisibility(View.GONE);

		}

		return view;
	}

	@Override
	public void onResume() {
		userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
		lastBase = userMap.getString("CurrentHome", "FIRST RUN");
		currentLocation = userMap.getString("CurrentLocation", "NOTWORKING");
		currentHome = userMap.getString("CurrentHome", "NOTWORKING");
		boolean canTravel = userMap.getBoolean("CanTravel", false);
		if (currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			tvTravel.setVisibility(View.GONE);


		} else if (currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.VISIBLE);
			tvTravel.setVisibility(View.GONE);


		} else if (!currentLocation.equals(thisTown) && !currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			if (canTravel) {
				tvTravel.setVisibility(View.VISIBLE);
			}

		} else if (!currentLocation.equals(thisTown) && currentHome.equals(thisTown)) {
			tvHomeSet.setVisibility(View.GONE);
			if (canTravel) {
				tvTravel.setVisibility(View.VISIBLE);
			}
		}
		if (userMap.getBoolean("CanTravel", true)) {
			// Clear Notifications
			NotificationManager notificationManager =
							(NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
			notificationManager.cancel(002);
			super.onResume();
		}

		super.onResume();
	}

	@Override
	public void onClick(View v) {
		userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
		SharedPreferences.Editor editor = userMap.edit();

		switch (v.getId()) {
			case R.id.tv_TRAVEL:
				// Switch Deny Travel On, Set Alarm.
				editor.putBoolean("CanTravel", false);
				editor.putString("CurrentLocation", thisTown);
				//editor.putString("LastLocation", lastBase);
				editor.apply();
				Log.wtf("MAP:", String.valueOf(userMap.getAll()));
				TravelHelper.SetAlarm(getContext());
				tvHomeSet.setVisibility(View.VISIBLE);
				tvTravel.setVisibility(View.GONE);
				Intent travelIntent = new Intent(getContext(), MapActivity.class);
				travelIntent.putExtra("viewpager_position", id);
				this.startActivity(travelIntent);
				break;
			case R.id.tv_Home:
				actionToken = 10;
				editor = userMap.edit();
				editor.putString("CurrentHome", thisTown);
				editor.putString("LastLocation", lastBase);
				editor.apply();
				tvHomeSet.setVisibility(View.GONE);
				Log.wtf("id:", String.valueOf(id));
				RegisterBase();
				Intent baseIntent = new Intent(getContext(), MapActivity.class);

				baseIntent.putExtra("viewpager_position", id);
				this.startActivity(baseIntent);
				break;


		}
	}

	private void RegisterBase() {


		// DELETES OLD BASE

		//Log.wtf("Old Town =", lastBase);
		stringRequest = new StringRequest(Request.Method.POST, deleteUrl, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				RegisterBase();
				Toast.makeText(getActivity().getApplicationContext(), String.valueOf(response), Toast.LENGTH_LONG).show();

			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				Toast.makeText(getActivity().getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
				Log.d("Maps:", " Error: " + new String(error.networkResponse.data));

			}

		}) {
			@Override
			protected Map<String, String> getParams() {
				params = new HashMap<String, String>();
				params.put("oldlocation", lastBase);
				params.put("hunterid", String.valueOf(hunterID));
				return params;
			}
		};
		RequestQueue requestQueue2 = Volley.newRequestQueue(getContext());
		requestQueue2.add(stringRequest);


		// REGISTERS NEW BASE
		stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
			@Override
			public void onResponse(String response) {
				Toast.makeText(getActivity().getApplicationContext(), response, Toast.LENGTH_LONG).show();

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
				params.put("oldlocation", lastBase);
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

	private void prepareLocalHunters() {

		// Displaying Progressbar
		progressBar.setVisibility(View.VISIBLE);
		getActivity().setProgressBarIndeterminate(true);
		AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
			@Override
			protected Void doInBackground(Integer... integers) {
				OkHttpClient client = new OkHttpClient();
				okhttp3.Request request = new okhttp3.Request.Builder()
								.url("https://tchost.000webhostapp.com/getHomeUsers.php?currentlocation=" + (thisTown))
								.build();

				try {
					okhttp3.Response response = client.newCall(request).execute();
					JSONArray array = new JSONArray(response.body().string());
					for (int i = 0; i < array.length(); i++) {
						JSONObject object = array.getJSONObject(i);
						localHunter temp = new localHunter(object.getString("huntername"));
						listLocalHunters.add(temp);
						Log.wtf("Adding Hunter:", temp.getHunterName());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return null;
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				adapter.notifyDataSetChanged();
				progressBar.setVisibility(View.GONE);
				super.onPostExecute(aVoid);
			}
		};
		task.execute();

	}

}
