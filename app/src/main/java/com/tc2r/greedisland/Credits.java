package com.tc2r.greedisland;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Credits extends Fragment {
TextView creditTitle, creditText;

	public Credits() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_credits, container, false);
		creditTitle = (TextView) view.findViewById(R.id.credits_title);
		creditTitle = (TextView) view.findViewById(R.id.credits_text);

		return view;

	}

}
