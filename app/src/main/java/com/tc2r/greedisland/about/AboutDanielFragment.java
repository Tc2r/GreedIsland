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


public class AboutDanielFragment extends Fragment {
	TextView yourDescription, yourTitle, yourRank, yourDesignation;
	ImageView yourImage, icon1, icon2, icon3, icon4;
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

		icon1.setImageResource(R.drawable.icon_instagram);
		icon2.setImageResource(R.drawable.icon_youtube);
		icon3.setImageResource(R.drawable.icon_tumblr);
		icon4.setImageResource(R.drawable.icon_twitter);


		icon1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://www.instagram.com/dan_catface/");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});

		icon2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://www.youtube.com/c/dancatface");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});
		icon3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://www.tumblr.com/blog/danhxhmemes");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});
		icon4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				uri = Uri.parse("https://twitter.com/DanCatface");
				intent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(intent);


			}
		});

		//// HI EVERYONE NEED YOU TO FILL OUT THIS INFORMATION FOR YOUR PERSONAL CARDS!


		// YOUR POSITION TITLE IN THE PROJECT.
		yourTitle.setText(R.string.Team_Daniel_Title);


		// YOUR CARD NUMBER (FAVORITE NUMBER)
		yourDesignation.setText("#11");


		// YOUR RANK
		yourRank.setText("2Star\n Hunter");

		// YOUR DESCRIPTION
		yourDescription.setText(R.string.Team_Daniel_Desc);


		// YOUR IMAGE (PLEASE SEND ME A 664X374 IMAGE TO USE FOR YOUR CARD
		yourImage.setImageResource(R.drawable.cardimage_daniel);


		yourBorder.setBackgroundResource(R.drawable.text_border_about1);




		return view;
	}

}
