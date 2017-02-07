package com.tc2r.greedisland.book;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.tc2r.greedisland.R;

public class CardDetail extends AppCompatActivity {

	private TextView desc, rank, id, limit, title;
	private ImageView image;
	private String sDesc, sRank, sTitle, sImage;
	private int sId, sLimit;
	LinearLayout activityCardDetail;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CheckTheme();
		setContentView(R.layout.activity_card_detail);

		desc = (TextView) findViewById(R.id.desc_desc);
		rank = (TextView) findViewById(R.id.desc_rank);
		id = (TextView) findViewById(R.id.desc_id);
		limit = (TextView) findViewById(R.id.desc_limit);
		title = (TextView) findViewById(R.id.desc_title);
		image = (ImageView) findViewById(R.id.desc_image);
		activityCardDetail = (LinearLayout) findViewById(R.id.activity_card_detail);

		Intent intent = getIntent();

		sDesc = intent.getExtras().getString("desc");
		sRank = intent.getExtras().getString("rank");
		sId = intent.getExtras().getInt("id");
		sLimit = intent.getExtras().getInt("limit");
		sTitle = intent.getExtras().getString("title");
		sImage = intent.getExtras().getString("image");
		intent = null;

		desc.setText(sDesc);
		rank.setText("RANK: "+ sRank);
		id.setText("ID: No."+ String.valueOf(sId));
		limit.setText("LIMIT: "+ String.valueOf(sLimit));
		title.setText(sTitle);

		Glide.with(getApplicationContext()).load(sImage)
						.crossFade()
						.centerCrop()
						.placeholder(R.drawable.img_placeholder).into(image);


	}
	@Override
	protected void onResume() {
		super.onResume();
		CheckTheme();
	}


	private void CheckTheme() {
		SharedPreferences setting = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
		String customTheme = setting.getString("Theme_Preference", "Fresh Greens");


		switch (customTheme) {
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
