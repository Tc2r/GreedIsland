package com.tc2r.greedisland.book;


import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.tc2r.greedisland.R;
import com.tc2r.greedisland.restrict.RestrictCardObject;
import com.tc2r.greedisland.spells.SpellsHelper;
import com.tc2r.greedisland.utils.Globals;
import com.tc2r.greedisland.utils.PerformanceTracking;
import com.tc2r.greedisland.utils.RewardsHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class BookTab extends Fragment implements View.OnClickListener {

    // Initialize Objects.
    private RecyclerView rulesView;
    private TextView bookText;
    private ImageView bookImage, card1, card2, card3;
    private GridLayoutManager gridLayoutManager;
    private RelativeLayout bookLayout;

    // Initialize Variables.
    private Context context;
    private MediaPlayer mp;
    private RestrictCardObject temp;
    private RuleObject ruleObject;
    private List<RuleObject> rulesList;
    private List<Integer> notFlipped = new ArrayList<>();
    private RulesAdapter rulesAdapter;
    private boolean[] cardCheck = new boolean[100];
    private boolean dailyCards = false;
    private Random random = new Random();
    private int rewards = 0;

    // Initialize Shared Preferences.
    SharedPreferences settings;

    public BookTab() {
        // Required empty public constructor
    }

    @SuppressWarnings("ResourceType")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize Variables
        context = getContext();
        View view;
        view = inflater.inflate(R.layout.fragment_book, container, false);
        LinearLayout mainLayout = initVariables(view, container);

        // Use a Runnable Thread to Load deck from Saved Preference!
        Handler handler = new Handler();
        Runnable r = new LoadDeckRunnable();
        handler.post(r);

        // Create a Rules list (List<RuleObject>) using RuleObject(s)
        prepareRules();

        // use adapter to set ruleObject(s) in rulesList to layouts.
        rulesAdapter = new RulesAdapter(rulesList, context);

        // set adapter to recycler view
        rulesView.setAdapter(rulesAdapter);
        rulesView.setLayoutManager(gridLayoutManager);

        // Create click Listeners
        bookLayout.setOnClickListener(this);
        card1.setOnClickListener(this);
        card2.setOnClickListener(this);
        card3.setOnClickListener(this);
        mainLayout.setOnClickListener(this);
        setVisibleDailyCards();
        return view;
    }

    private void setVisibleDailyCards() {
        if (settings.getBoolean("DailyCards", false)) {
            //Log.d("DailyCards", "True");
            // Clear Notifications
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(001);

            if (settings.getInt("Rewards", 3) < 3) {
                card3.setVisibility(View.VISIBLE);
                if (settings.getInt("Rewards", 3) < 2) {
                    card2.setVisibility(View.VISIBLE);
                    if (settings.getInt("Rewards", 3) < 1) {
                        card1.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    private LinearLayout initVariables(View view, ViewGroup container) {

        settings = PreferenceManager.getDefaultSharedPreferences(view.getContext());
        dailyCards = settings.getBoolean("DailyCards", false);
        rewards = settings.getInt("Rewards", 0);

        bookText = (TextView) view.findViewById(R.id.tv_bookText);
        bookImage = (ImageView) view.findViewById(R.id.iv_book);
        bookLayout = (RelativeLayout) view.findViewById(R.id.lay_book);
        mp = MediaPlayer.create(view.getContext(), R.raw.greed);

        card1 = (ImageView) view.findViewById(R.id.dailyCard_1);
        card2 = (ImageView) view.findViewById(R.id.dailyCard_2);
        card3 = (ImageView) view.findViewById(R.id.dailyCard_3);

        // Setting up bottom section
        rulesView = (RecyclerView) view.findViewById(R.id.rules_view);

        // - Initialize Recycler view
        gridLayoutManager = new GridLayoutManager(container.getContext(), 1);

        // Create A Grid, could have used Linear. Setup Recycler view.
        rulesList = new ArrayList<>();
        rulesView.setHasFixedSize(true);

        return (LinearLayout) view.findViewById(R.id.layout_main);
    }

    @Override
    public void onResume() {
        if (settings.getBoolean("DailyCards", false)) {
            // Clear Notifications
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(001);
        }
        super.onResume();
    }

    // Localized RuleObject Creation
    private void prepareRules() {
        String ruleTitles[] = getResources().getStringArray(R.array.ruleTitles);
        String ruleDescs[] = getResources().getStringArray(R.array.ruleDescs);

        int ruleImages[] = {R.drawable.rule_1, R.drawable.rule_2, R.drawable.rule_3, R.drawable.rule_4, R.drawable.rule_5, R.drawable.rule_6, R.drawable.rule_7,};

        // We run through the rules and add them to the rules list.
        for (int i = 0; i < ruleTitles.length; i++) {
            int randRulesImage = random.nextInt(ruleImages.length);
            ruleObject = new RuleObject(ruleTitles[i], ruleDescs[i], ruleImages[randRulesImage]);
            rulesList.add(ruleObject);
        }
    }

    @Override
    public void onClick(View v) {
        ////Log.d("View ID: ", v.getResources().getResourceEntryName(v.getId()));
        switch (v.getId()) {
            case (R.id.lay_book):
                if (!Globals.isNetworkAvailable(context)) {
                    Toast.makeText(context, R.string.internet_down_message, Toast.LENGTH_LONG).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBooleanArray("data", cardCheck);
                    Intent intent = new Intent(context, DeckActivity.class);
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
                break;
            case R.id.dailyCard_1:
            case R.id.dailyCard_2:
            case R.id.dailyCard_3:

                // Check Connectivity, if no net, do nothing
                if (!Globals.isNetworkAvailable(getActivity())) {
                    Toast.makeText(getActivity(), "Must Have Internet Connection for this Action!", Toast.LENGTH_LONG).show();
                    break;
                }
                Random rnd = new Random();

                if (rnd.nextInt(10) < 3) {
                    SpellsHelper.CreateRandomSpell(context, 1);
                }

                SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(v.getContext());
                SharedPreferences.Editor editor = settings.edit();
                rewards = settings.getInt("Rewards", 0);
                rewards = rewards + 1;
                //Log.d("Rewards: ", String.valueOf(rewards));

                editor.putInt("Rewards", rewards);
                editor.apply();

                if (settings.getInt("Rewards", 0) >= 3) {
                    //Log.d("ACTIVATE", "RECEIVER");
                    RewardsHelper.EnableBroadcast(context);
                    RewardsHelper.setAlarm(context);
                }
                mp.start();

                // Cycle through entire array for all variables false
                // save false variables to notFlipped List
                // cardCheck.length
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
                        ShowCard(v, notFlipped.get(newNum));
                        SaveBook();
                        notFlipped.remove(newNum);
                        v.setVisibility(View.GONE);

                    } else if (notFlipped.size() == 0) {
                        PerformanceTracking.TrackEvent("All Cards Recieved!");
                        /// WHEN ALL CARDS ARE FLIPPED!

                    } else {
                        PerformanceTracking.TrackEvent("New Card Recieved.");

                        int newNum = random.nextInt(notFlipped.size() - 1);
                        cardCheck[notFlipped.get(newNum)] = true;
                        ShowCard(v, notFlipped.get(newNum));
                        //Log.d("SET TO TRUE ", String.valueOf(notFlipped.get(newNum)));
                        SaveBook();
                        notFlipped.remove(newNum);
                        v.setVisibility(View.GONE);
                    }
                }

        }
    }

    public void ShowCard(final View v, final int Id) {
        PerformanceTracking.TrackEvent("New Card Recieved, Card #" + (Id+1));

        @SuppressLint("StaticFieldLeak") AsyncTask<Integer, Void, Void> task = new AsyncTask<Integer, Void, Void>() {
            @Override
            protected Void doInBackground(Integer... params) {

                OkHttpClient client = new OkHttpClient.Builder().protocols(Arrays.asList(Protocol.HTTP_1_1)).build();
                Request request = new Request.Builder().url("http://tchost.000webhostapp.com/getcard.php?id=" + (Id + 1)).build();
                PerformanceTracking.TransactionBegin("Get Card " + request.url().toString());

                try {
                    PerformanceTracking.TrackEvent("GET CARD BEFORE");
                    Response response = client.newCall(request).execute();

                    PerformanceTracking.TrackEvent("GET CARD AFTER" + response.code());
                    PerformanceTracking.TransactionEnd("Get Card: Response" + String.valueOf(response.code()));

                    JSONArray array = new JSONArray(response.body().string());
                    JSONObject object = array.getJSONObject(0);
                    temp = new RestrictCardObject(object.getInt("id"), object.getString("name"), object.getString("rank"), object.getInt("limit"), object.getString("description"), object.getString("image"), object.getInt("type"));

                } catch (IOException | JSONException e) {
                    PerformanceTracking.TransactionFail("Get Card: " + e.toString());
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                v.setVisibility(View.GONE);
                View child = getActivity().getLayoutInflater().inflate(R.layout.new_card_layout, null);
                final RelativeLayout reward = (RelativeLayout) getActivity().findViewById(R.id.rewardLayout);
                reward.addView(child);
                TextView title = (TextView) child.findViewById(R.id.new_card_title);
                TextView designation = (TextView) child.findViewById(R.id.new_card_designation);
                TextView ranklimit = (TextView) child.findViewById(R.id.new_card_ranklimit);
                LinearLayout border = (LinearLayout) child.findViewById(R.id.new_text_border_layout);
                TextView description = (TextView) child.findViewById(R.id.new_card_description);
                ImageView cardImage = (ImageView) child.findViewById(R.id.new_card_image);

                title.setText(temp.getTitle());
                designation.setText(String.valueOf(temp.getId()));
                ranklimit.setText(temp.getRank() + "-" + String.valueOf(temp.getLimit()));
                description.setText(temp.getDescription());
                border.setBackgroundResource(R.drawable.text_border_restrict);
                final String newUrl = "http://res.cloudinary.com/munaibh/image/upload/v1485443356/HxH/" + (temp.getId() - 1) + ".png";
                PerformanceTracking.TransactionBegin("Get Card Image: "+ newUrl);
                Glide.with(context)
                        .load(newUrl)
                        .apply(new RequestOptions().skipMemoryCache(true).placeholder(R.drawable.placeholder).centerCrop())
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                PerformanceTracking.TransactionFail("Get Card Image: " + e.getLocalizedMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                PerformanceTracking.TransactionEnd("Get Card Image");
                                return false;
                            }
                        })
                        .transition(new DrawableTransitionOptions().crossFade())
                        .into(cardImage);
                super.onPostExecute(aVoid);
            }
        };
        task.execute(Id);
    }

    public class LoadDeckRunnable implements Runnable {

        @Override
        public void run() {
            cardCheck = loadBook(cardCheck.length);
        }
    }

    // Saves players Book data internally.
    public void SaveBook() {
        // Save Players Deck to shared preferences.
        SharedPreferences prefs_book = getContext().getSharedPreferences("UserBook", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs_book.edit();
        editor.putInt("bookPreferenceArray" + "_size", cardCheck.length);
        //Log.d("CardCheck", String.valueOf(cardCheck.length));

        // save each cards status (available or not) to shared preference.
        for (int i = 0; i < cardCheck.length; i++) {
            editor.putBoolean("bookPreferenceArray" + "_" + i, cardCheck[i]);
        }
        editor.apply();
    }

    // Load and arrange the player book from internal saved structure.
    public boolean[] loadBook(int preSize) {

        // Load data from shared Preference.
        SharedPreferences prefs_book = getContext().getSharedPreferences("UserBook", MODE_PRIVATE);
        int size = prefs_book.getInt(("bookPreferenceArray" + "_size"), MODE_PRIVATE);
        boolean array[];

        // get or initialize array.
        if (size == 0) {
            array = new boolean[preSize];

        } else {
            array = new boolean[size];
        }

        // File array with players deck information.
        for (int i = 0; i < size; i++) {

            array[i] = prefs_book.getBoolean("bookPreferenceArray" + "_" + i, false);
        }
        PerformanceTracking.TrackEvent("Loading Deck Complete");
        return array;
    }

}