package com.tc2r.greedisland.book;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tc2r.greedisland.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookTab extends Fragment implements View.OnClickListener {

	Context context;
	RecyclerView rulesView;
	TextView bookText;
	ImageView bookImage, card1, card2, card3;
	Rule rule;
	List<Rule> rulesList;
	RulesAdapter rulesAdapter;
	GridLayoutManager gridLayoutManager;
	RelativeLayout bookLayout;
	boolean[] cardCheck = new boolean[100];
	List<Integer> notFlipped = new ArrayList<>();
	Random random = new Random();
	private MediaPlayer mp;

	public void SaveBook() {
		SharedPreferences prefs_book = getContext().getSharedPreferences("UserBook", MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs_book.edit();

		editor.putInt("bookPreferenceArray" + "_size", cardCheck.length);
		//Log.wtf("CardCheck", String.valueOf(cardCheck.length));
		for (int i = 0; i < cardCheck.length; i++) {

			editor.putBoolean("bookPreferenceArray" + "_" + i, cardCheck[i]);

		}
		editor.commit();
	}

	public boolean[] loadArray(String arrayName, int preSize) {
		SharedPreferences prefs_book = getContext().getSharedPreferences("UserBook", MODE_PRIVATE);
		int size = prefs_book.getInt(("bookPreferenceArray" + "_size"), MODE_PRIVATE);
		boolean array[];
		if (size == 0) {
			array = new boolean[preSize];
			Log.wtf("ERROR", "SIZE IS ZERO");
		} else {
			array = new boolean[size];
			Log.wtf("SIZE", String.valueOf(array.length));
		}


		for (int i = 0; i < size; i++) {
			array[i] = prefs_book.getBoolean("bookPreferenceArray" + "_" + i, false);
			Log.wtf("Load: ", "CardID " + i + " is " + String.valueOf(array[i]));
		}
		Log.wtf("Loading Deck", "Complete");

		return array;
	}


	public BookTab() {
		// Required empty public constructor

	}


	@SuppressWarnings("ResourceType")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {

		// Initalize Variables
		context = getContext();
		View view = (View) inflater.inflate(R.layout.book_fragment, container, false);

		bookText = (TextView) view.findViewById(R.id.tv_bookText);
		bookImage = (ImageView) view.findViewById(R.id.iv_book);
		bookLayout = (RelativeLayout) view.findViewById(R.id.lay_book);
		mp = MediaPlayer.create(view.getContext(), R.raw.greed);

		card1 = (ImageView) view.findViewById(R.id.dailyCard_1);
		card2 = (ImageView) view.findViewById(R.id.dailyCard_2);
		card3 = (ImageView) view.findViewById(R.id.dailyCard_3);

		LinearLayout mainLayout = (LinearLayout) view.findViewById(R.id.layout_main);


		// Use a Runnable Thread to Load deck from Saved Preference!
		Handler handler = new Handler();
		Runnable r = new LoadDeckRunnable();
		handler.post(r);
		//cardCheck = loadArray("bookPreferenceArray", cardCheck.length);


		// Setting up bottom section
		rulesView = (RecyclerView) view.findViewById(R.id.rules_view);
		// - Initalize Recyclerview
		gridLayoutManager = new GridLayoutManager(container.getContext(), 1);
		// Create A Grid, could have used Linear. Setup Recyclerview.
		rulesView.setHasFixedSize(true);
		rulesList = new ArrayList<>();

		// Create a Rules list (List<Rule>) using Rule(s)
		prepareRules();

		// use adapter to set rule(s) in rulesList to layouts.
		rulesAdapter = new RulesAdapter(rulesList, context);

		// set adapter to recyclerview
		rulesView.setAdapter(rulesAdapter);
		rulesView.setLayoutManager(gridLayoutManager);


		// Create click Listeners
		bookLayout.setOnClickListener(this);
		card1.setOnClickListener(this);
		card2.setOnClickListener(this);
		card3.setOnClickListener(this);
		mainLayout.setOnClickListener(this);
		//bookText.setOnClickListener(this);
		//bookImage.setOnClickListener(this);


		return view;
	}

	// Localized Rule Creation
	private void prepareRules() {
		String ruleTitles[] = {
						"WELCOME TO GREED ISLAND ",
						"Rule 1: ",
						"BOOK: ",
						"Free slots: 45 ",
						"GOOOOON~",
						"Items Become Cards:",
						"Gain: ",
						"Transformation:",
						"Store in Book: ",
						"How To Win: ",
						"How To Lose: ",
						"How To Play: ",
		};
		String ruleDescs[] = {
						"The goal of the game is to complete the book.",

						"In this game, whoever has the ring can use two spells \"Book\" and \"Gain\".",

						"Imposed slots: 100 (numbered 000 - 099) \nEach slot has a number. Each slot only accepts the card with the right number.",

						"Any cards can be slotted into free slots.",

						" ",

						"All the items you find in the game can be converted into cards. \n Book is used to store the cards.",

						"When you acquire an item, it immediately turns into a card. (Eg: Finding and taking a sword will automatically turn it into a card)\n" +
										"Cards slotted into the book can no longer be used unless the player uses \"gain\". However, a card transformed via \"gain\" can no longer be converted back into a card.\n" +
										"- If you want to transform the sword into a card again, you must acquire the same sword again.",

						"All the items in the island have a limited number of transformations.\n Although there's more than 100 copies of each total, each card only has a limit of 3 that can be in card-state at a time.",

						"All cards must be stored in the book, othewise the card will automatically turn back into an item within 1 minute. In that case, retransformation is no longer possible.",

						"You must collect the numbered slots in order to complete the game.",

						"If you were to die during the game, the book and the ring will be destroyed.",

						"Have Fun!",

		};

		int ruleImages[] = {
						R.drawable.rule_2,
						R.drawable.rule_3,
						R.drawable.rule_5,
						R.drawable.rule_6,
						R.drawable.hisoka_rule,
						R.drawable.rule_1,
						R.drawable.rule_2,
						R.drawable.rule_3,
						R.drawable.rule_1,
						R.drawable.rule_4,
						R.drawable.rule_5,
						R.drawable.rule_4,
		};

		// We run through the rules and add them to the rules list.

		for (int i = 0; i < ruleTitles.length; i++) {
			rule = new Rule(ruleTitles[i], ruleDescs[i], ruleImages[i]);
			rulesList.add(rule);
		}
	}

	@Override
	public void onClick(View v) {
		//Log.wtf("View ID: ", v.getResources().getResourceEntryName(v.getId()));
		switch (v.getId()) {
			case (R.id.lay_book):

				Bundle bundle = new Bundle();
				bundle.putBooleanArray("data", cardCheck);
				Intent intent = new Intent(context, DeckActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
				break;
			case R.id.dailyCard_1:
			case R.id.dailyCard_2:
			case R.id.dailyCard_3: {
				mp.start();

				//v.setVisibility(View.GONE);

				// Cycle through entire array for all variables false
				// save false variables to notFlipped List
				//cardCheck.length
				for (int i = 1; i < cardCheck.length; i++) {

					// if boolean at i in cardCheck is false
					if (!cardCheck[i]) {

						//Add position i to notFlipped List
						notFlipped.add(i);
					}
				}
				// Take
				for (int j = 0; j < 1; j++) {
					// ONLY 1 CARD LEFT!

					if (notFlipped.size() == 1) {
						int newNum = random.nextInt(notFlipped.size());
						cardCheck[notFlipped.get(newNum)] = true;

						Toast toast = Toast.makeText(v.getContext(), "You Recieved Card: #" + String.valueOf(notFlipped.get(newNum)), Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();


						SaveBook();
						notFlipped.remove(newNum);
						v.setVisibility(View.GONE);

					} else if (notFlipped.size() == 0) {
						Log.wtf("NO CARDS LEFT ", "oh really?");
					/// WHEN ALL CARDS ARE FLIPPED!


					} else {
						int newNum = random.nextInt(notFlipped.size() - 1);
						cardCheck[notFlipped.get(newNum)] = true;
						cardCheck[99] = true;

						Toast toast = Toast.makeText(v.getContext(), "You Recieved Card: #" + String.valueOf(notFlipped.get(newNum)), Toast.LENGTH_LONG);
						toast.setGravity(Gravity.CENTER, 0, 0);
						toast.show();

						//Log.wtf("SET TO TRUE ", String.valueOf(notFlipped.get(newNum)));
						SaveBook();
						notFlipped.remove(newNum);
						v.setVisibility(View.GONE);

					}
				}
			}
		}
	}

	public class LoadDeckRunnable implements Runnable {

		@Override
		public void run() {
			cardCheck = loadArray("bookPreferenceArray", (cardCheck.length));
		}
	}
}