package com.tc2r.greedisland.utils;

/**
 * Created by Tc2r on 2/4/2017.
 * <p>
 * Description:
 */

public class Globals {
	private static Globals instance;
	private static String USERNAME;
	private static int ACTION_TOKENS;


	private Globals(){}

	public String getUsername() {
		return USERNAME;
	}

	public void setUsername(String username) {
		Globals.USERNAME = username;
	}

	public static int getActionTokens() {
		return ACTION_TOKENS;
	}

	public static void setActionTokens(int actionTokens) {
		ACTION_TOKENS = actionTokens;
	}

	public static synchronized Globals getInstance(){
		if(instance == null){
			instance = new Globals();
		}
		return instance;
	}

}
