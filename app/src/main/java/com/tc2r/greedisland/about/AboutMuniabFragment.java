package com.tc2r.greedisland.about;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tc2r.greedisland.R;


public class AboutMuniabFragment extends Fragment {
	TextView yourDescription, yourTitle, yourRank, yourDesignation;
	ImageView yourImage, icon1, icon2, icon3, icon4, icon5;
	LinearLayout yourBorder;
	Intent intent;
	Uri uri;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.about_card_template, container, false);
		yourDescription = (TextView) view.findViewById(R.id.card_description);
		yourTitle = (TextView) view.findViewById(R.id.card_title);
		yourDesignation = (TextView) view.findViewById(R.id.card_designation);
		yourRank = (TextView) view.findViewById(R.id.card_ranklimit);
		yourImage = (ImageView) view.findViewById(R.id.card_image);
		yourBorder = (LinearLayout) view.findViewById(R.id.text_border_layout);
		icon1 = (ImageView) view.findViewById(R.id.icon_1);
		icon2 = (ImageView) view.findViewById(R.id.icon_2);
		icon3 = (ImageView) view.findViewById(R.id.icon_3);
		icon4 = (ImageView) view.findViewById(R.id.icon_4);
		icon5 = (ImageView) view.findViewById(R.id.icon_5);

		icon1.setImageResource(R.drawable.icon_linkedin);
		icon2.setImageResource(R.drawable.icon_github);
		icon3.setImageResource(R.drawable.icon_deviant);
		icon4.setImageResource(R.drawable.icon_twitter);

		icon5.setVisibility(View.VISIBLE);
		icon5.setImageResource(R.drawable.icon_at);

		icon1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://www.linkedin.com/in/munaib-hussain-37bb59138");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});

		icon2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://github.com/munaibh");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});
		icon3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("http://munaibh.deviantart.com/gallery/");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});
		icon4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://twitter.com/munaibh");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});
		icon5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// fifth icon, where does it go?
				uri = Uri.parse("https://munaibh.com");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});

		//// HI EVERYONE NEED YOU TO FILL OUT THIS INFORMATION FOR YOUR PERSONAL CARDS!


		// YOUR POSITION TITLE IN THE PROJECT.
		yourTitle.setText(R.string.Team_Munaib_Title);


		// YOUR CARD NUMBER (FAVORITE NUMBER)
		yourDesignation.setText("SS");


		// YOUR RANK
		yourRank.setText("X");

		// YOUR DESCRIPTION
		yourDescription.setText(R.string.Team_Munaib_Desc);


		// YOUR IMAGE (PLEASE SEND ME A 664X374 IMAGE TO USE FOR YOUR CARD
		yourImage.setImageResource(R.drawable.cardimage_munaib);


		yourBorder.setBackgroundResource(R.drawable.text_border_about2);




		return view;
	}

}
