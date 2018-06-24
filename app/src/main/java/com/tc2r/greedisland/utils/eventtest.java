package com.tc2r.greedisland.utils;

import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.tc2r.greedisland.R;

public class eventtest extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    Thread thread = new Thread() {
        @Override
        public void run() {
            try {
                Thread.sleep(10000);
                eventtest.this.finish();

            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    };
    private SharedPreferences setting;
    private ImageView topImage, botImage;
    private TextView topTitle, topText, botTitle, botText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        setting.registerOnSharedPreferenceChangeListener(this);
        onSharedPreferenceChanged(setting, getString(R.string.pref_theme_selection_key));
        setContentView(R.layout.activity_eventtest);
        topImage = (ImageView) findViewById(R.id.iv_location);
        botImage = (ImageView) findViewById(R.id.iv_event);

        topTitle = (TextView) findViewById(R.id.tv_loc_title);
        topText = (TextView) findViewById(R.id.tv_loc_text);

        botTitle = (TextView) findViewById(R.id.tv_details_title);
        botText = (TextView) findViewById(R.id.tv_details_desc);

        SharedPreferences location = PreferenceManager.getDefaultSharedPreferences(this);


        String locTitle = location.getString(getString(R.string.pref_current_location_key), "Greed Island");
        topTitle.setText("CURRENT LOCATION: " + locTitle);

        // set bg images:
        int townId = 4;
        switch (locTitle) {
            case "Masadora":
                townId = 0;
                break;
            case "Soufrabi":
                townId = 1;
                break;
            case "Aiai":
                townId = 2;
                break;
            case "Antokiba":
                townId = 3;
                break;
            case "Start":
                townId = 4;
                break;
            case "Rubicuta":
                townId = 5;
                break;
            case "Dorias":
                townId = 6;
                break;
            case "Limeiro":
                townId = 7;
                break;
            default:
                townId = 4;
        }

        TypedArray images = getResources().obtainTypedArray(R.array.loc_image);
        @SuppressWarnings("ResourceType") Drawable drawable = images.getDrawable(townId);
        topImage.setImageDrawable(drawable);

        images = getResources().obtainTypedArray(R.array.loc_image2);
        @SuppressWarnings("ResourceType") Drawable botDrawable = images.getDrawable(townId);
        botImage.setImageDrawable(botDrawable);

        images.recycle();
        botDrawable = null;
        drawable = null;


        SharedPreferences.Editor editor = setting.edit();
        editor.putBoolean("EventSwitch", false);
        editor.apply();
        EventsManager eventsManager = new EventsManager(this);
        thread.start();


    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        //Toast.makeText(this, "CHANGE", Toast.LENGTH_SHORT).show();
        if (key.equals(getString(R.string.pref_theme_selection_key))) {
            String customTheme = setting.getString(getString(R.string.pref_theme_selection_key), "Fresh Greens");
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
                    ////Log.d("Test", "Birds n Berries");
                    setTheme(R.style.AppTheme_BirdBerries);
                    break;
                case "Blue Berry":
                    ////Log.d("Test", "Blue Berry!");
                    setTheme(R.style.AppTheme_BlueBerry);
                    break;
                case "Cinnamon":
                    ////Log.d("Test", "Cinnamon!");
                    setTheme(R.style.AppTheme_Cinnamon);
                    break;
                case "Day n Night":
                    ////Log.d("Test", "Day n Night");
                    setTheme(R.style.AppTheme_Night);
                    break;
                case "Earthly":
                    ////Log.d("Test", "Earthly!");
                    setTheme(R.style.AppTheme_Earth);
                    break;
                case "Forest":
                    ////Log.d("Test", "Forest!");
                    setTheme(R.style.AppTheme_Forest);
                    break;
                case "Fresh Greens":
                    ////Log.d("Test", "GREENS!");
                    setTheme(R.style.AppTheme_Greens);
                    break;
                case "Fresh n Energetic":
                    ////Log.d("Test", "Fresh n Energetic");
                    setTheme(R.style.AppTheme_Fresh);
                    break;
                case "Icy Blue":
                    ////Log.d("Test", "Icy!");
                    setTheme(R.style.AppTheme_Icy);
                    break;
                case "Ocean":
                    ////Log.d("Test", "Ocean");
                    setTheme(R.style.AppTheme_Ocean);
                    break;
                case "Play Green/blues":
                    ////Log.d("Test", "Play Green/blues");
                    setTheme(R.style.AppTheme_GrnBlu);
                    break;
                case "Primary":
                    ////Log.d("Test", "Primary");
                    setTheme(R.style.AppTheme_Prime);
                    break;
                case "Rain":
                    ////Log.d("Test", "Rain!");
                    setTheme(R.style.AppTheme_Rain);
                    break;
                case "Tropical":
                    ////Log.d("Test", "Tropical");
                    setTheme(R.style.AppTheme_Tropical);
                    break;
                default:
                    ////Log.d("Test", "Default");
                    setTheme(R.style.AppTheme_Greens);
                    break;
            }
        }
    }
}
