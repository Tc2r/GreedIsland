package com.tc2r.greedisland.about;


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
	ImageView yourImage;
	LinearLayout yourBorder;
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


		//// HI EVERYONE NEED YOU TO FILL OUT THIS INFORMATION FOR YOUR PERSONAL CARDS!


		// YOUR POSITION TITLE IN THE PROJECT.
		yourTitle.setText("Junior\n Graphic Designer");


		// YOUR CARD NUMBER (FAVORITE NUMBER)
		yourDesignation.setText("#11");


		// YOUR RANK
		yourRank.setText("2Star\n Hunter");

		// YOUR DESCRIPTION
		yourDescription.setText("Youngest In The Group, And I Love Killua");


		// YOUR IMAGE (PLEASE SEND ME A 664X374 IMAGE TO USE FOR YOUR CARD
		yourImage.setImageResource(R.drawable.cardimage_daniel);





		yourBorder.setBackgroundResource(R.drawable.text_border_about);




		return view;
	}

}