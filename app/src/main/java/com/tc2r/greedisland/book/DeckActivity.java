package com.tc2r.greedisland.book;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.View;

import com.tc2r.greedisland.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DeckActivity extends AppCompatActivity {
	RecyclerView recyclerView;
	GridLayoutManager layoutManager;
	CardAdapter adapter;
	GreedCard card;
	List<GreedCard> deck;
	SharedPreferences setting;
	boolean[] cardCheck;
	boolean local = false;
	Bundle bundle;



	String cardTitle[] = {
					"Ruler's Blessing",
					"Patch of Forest",
					"Patch of Stone",
					"Pitcher of Eternal Water",
					"Ruler's Blessing",
					"Patch of Forest",
					"Patch of Stone",
					"Pitcher of Eternal Water",
					"Ruler's Blessing",
	};
	String cardDesc[] = {
					"A castle given as a prize for winning the contest, town with population 10,000 included. Its residents will live according to whatever laws and commands you issue.",
					"The entrance to the giant forest called the \"Mountain God's Garden\" where many unique endemic species live. They are all tame and friendly. ",
					"The entrance to a cave called \"Poseidon's Cavern.\" The cave changes its path at each visit, confusing intruders.",
					"A jar from which pure, clean water (1440 L per day) continually flows. ",
					"A castle given as a prize for winning the contest, town with population 10,000 included. Its residents will live according to whatever laws and commands you issue.",
					"The entrance to the giant forest called the \"Mountain God's Garden\" where many unique endemic species live. They are all tame and friendly. ",
					"The entrance to a cave called \"Poseidon's Cavern.\" The cave changes its path at each visit, confusing intruders.",
					"A jar from which pure, clean water (1440 L per day) continually flows. ",
					"A castle given as a prize for winning the contest, town with population 10,000 included. Its residents will live according to whatever laws and commands you issue.",

	};

	int cardImage[] = {
					R.drawable.img_placeholder,


	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		SharedPreferences firstPrefer = getSharedPreferences("first_Pref", Context.MODE_PRIVATE);
		Boolean firsttime = firstPrefer.getBoolean("first_Pref", true);
		String hunterName = setting.getString("Hunter_Name_Pref", "john");
		CheckTheme();
		SharedPreferences.Editor firstTimeEditor = firstPrefer.edit();
		setContentView(R.layout.deck_main);

		deck = new ArrayList<>();
		prepareCards(1);

		recyclerView = (RecyclerView) findViewById(R.id.recycleview);
		layoutManager = new GridLayoutManager(this, 3);

		recyclerView.setHasFixedSize(true);
		recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dpToPx(10), true));

		recyclerView.setLayoutManager(layoutManager);

		// TODO: BEST FIT TEXT OBJECT

		recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


				if (layoutManager.findLastCompletelyVisibleItemPosition() == (deck.size() -1)) {

					prepareCards((deck.get(deck.size()-1).getId()) +1);
				}

			}
		});


		/// MANAGING THE VISIBLE DECK!
		bundle = this.getIntent().getExtras();
		//Log.wtf("huh", Arrays.toString(bundle.getBooleanArray("data")));
		cardCheck = bundle.getBooleanArray("data");


		adapter = new CardAdapter(this, deck, local, cardCheck);
		recyclerView.setAdapter(adapter);


	}


	private void prepareCards(final int id) {


		// For Local Data
		if (local) {
			for (int i = 0; i < cardImage.length; i++) {
				card = new GreedCard(cardTitle[i], cardDesc[i], cardImage[i]);
				deck.add(card);
			}
		}else{

			// For Server Data
			AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
				@Override
				protected Void doInBackground(Integer... integers) {


					OkHttpClient client = new OkHttpClient();
					Request request = new Request.Builder()
									.url("https://tchost.000webhostapp.com/greedget.php?id=" + (id))
									.build();

					try {
						Response response = client.newCall(request).execute();

						JSONArray array = new JSONArray(response.body().string());

						for (int i = 0; i < array.length(); i++) {
							JSONObject object = array.getJSONObject(i);
							GreedCard temp = new GreedCard((object.getInt("id") -1), object.getString("name"), object.getString("rank"), object.getInt("limit"), object.getString("description"), object.getString("image"), object.getInt("type"));
							deck.add(temp);
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
				}
			};
			task.execute(id);
		}

	}


	private class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
		private int spanCount;
		private int spacing;
		private boolean includeEdge;

		public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
			this.spanCount = spanCount;
			this.spacing = spacing;
			this.includeEdge = includeEdge;
		}

		@Override
		public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
			int position = parent.getChildAdapterPosition(view);
			int column = position % spanCount;
			if (includeEdge) {
				outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
				outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

				if (position < spanCount) { // top edge
					outRect.top = spacing;
				}
				outRect.bottom = spacing; // item bottom
			} else {
				outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
				outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
				if (position >= spanCount) {
					outRect.top = spacing; // item top
				}

			}
		}
	}

	/**
	 * Converting dp to pixel
	 */
	private int dpToPx(int dp) {
		Resources r = getResources();
		return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
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
}
