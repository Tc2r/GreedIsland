package com.tc2r.greedisland.book;

/**
 * Created by Tc2r on 1/27/2017.
 * <p>
 * Description:
 */

public class Rule {
	String ruleTitle, ruleDescription;

	public Rule(String ruleTitle, String ruleDescription, int ruleImage) {
		this.ruleTitle = ruleTitle;
		this.ruleDescription = ruleDescription;
		this.ruleImage = ruleImage;
	}

	public String getRuleTitle() {
		return ruleTitle;
	}

	public void setRuleTitle(String ruleTitle) {
		this.ruleTitle = ruleTitle;
	}

	public String getRuleDescription() {
		return ruleDescription;
	}

	public void setRuleDescription(String ruleDescription) {
		this.ruleDescription = ruleDescription;
	}

	public int getRuleImage() {
		return ruleImage;
	}

	public void setRuleImage(int ruleImage) {
		this.ruleImage = ruleImage;
	}

	int ruleImage;
}
