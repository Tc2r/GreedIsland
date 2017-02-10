package com.tc2r.greedisland.utils;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.tc2r.greedisland.R;

import java.util.ArrayList;
import java.util.List;

public class TestActivity extends AppCompatActivity {

	boolean[] cardCheck = new boolean[99];
	List<Integer> notFlipped = new ArrayList<>();


//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_test);
//
//
//		// Create random for random numbers
//		Random random = new Random();
//
//
//		// Cycle through entire array for all variables false
//		// save false variables to notFlipped List
//		for(int i=0; i < cardCheck.length; i++){
//
//			// if boolean at i in cardCheck is false
//			if(!cardCheck[i]){
//
//				//Add position i to notFlipped List
//					notFlipped.add(i);
//			}
//		}
//
//
//		// Take
//		for(int j =0; j < 3 ; j++) {
//			int newNum = random.nextInt(notFlipped.size() - 1);
//			cardCheck[notFlipped.get(newNum)] = true;
//			//Log.wtf("SET TO TRUE ", String.valueOf(notFlipped.get(newNum)));
//			notFlipped.remove(newNum);
//		}
















//		for (int i = 0; i <= 10; i++) {
//
//
//			int rand = random.nextInt(10);
//
//			if (cardCheck[rand] == true) {
//				i-=1;
//				Log.wtf("REPEAT", String.valueOf(rand));
//			} else {
//				cardCheck[rand] = true;
//				Log.wtf("SET TO TRUE ", String.valueOf(rand));
//			}
//		}
//
//





//		///PRINT TRUE ARRAY SLOTS
//		int k = 1;
//		for (int j = 0; j < cardCheck.length; j++) {
//			if (cardCheck[j]) {
//				//Log.wtf("booleans: ", j + " is true");
//				k++;
//			}
//		}
//		Log.wtf("TOTAL TRUE BOOLEANS: ", String.valueOf(k));
//
//


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_test);
		scheduleAlarm();
	}

	public void scheduleAlarm() {
		RewardsHelper.setAlarm(this);

	}
}
