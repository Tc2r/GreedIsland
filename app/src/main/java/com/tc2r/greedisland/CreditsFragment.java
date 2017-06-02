package com.tc2r.greedisland;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class CreditsFragment extends Fragment {

	// Declare Layout Variables
	TextView creditTitle;

	public CreditsFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
													 Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_credits, container, false);
		creditTitle = (TextView) view.findViewById(R.id.credits_title);
		creditTitle = (TextView) view.findViewById(R.id.credits_text);
		LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.secret);

		linearLayout.setOnClickListener(new SecretButton());
		return view;

	}

	// Secret button is a debugging button created to reset daily timers.
	private class SecretButton implements View.OnClickListener {
		@Override
		public void onClick(View v) {
			//Log.d("Working", "this is");
//			SharedPreferences userMap = PreferenceManager.getDefaultSharedPreferences(getActivity());
//			SharedPreferences.Editor editor = userMap.edit();
//			editor.putBoolean("CanTravel", true);
//			editor.putInt("Rewards", 0);
//			editor.apply();
//			RewardsHelper.BootAlarm(getContext());
		}
	}
}
