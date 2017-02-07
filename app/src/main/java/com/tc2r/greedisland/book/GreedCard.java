package com.tc2r.greedisland.book;

/**
 * Created by Tc2r on 1/22/2017.
 * <p>
 * Description:
 */

public class GreedCard {
	int imageItem;
	String thumbnail;
	String title, description, imageUrl;

	int id, limit, type;
	String rank;

	public GreedCard(String title, String description, int imageItem) {
		this.title = title;
		this.description = description;
		this.imageItem = imageItem;
	}

	// for servers
	public GreedCard(int id, String title, String rank, int limit, String description, String imageurl, int type) {
		this.title = title;
		this.description = description;
		this.imageUrl = imageurl;
		this.id = id;
		this.limit = limit;
		this.rank = rank;
		this.type = type;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank.toUpperCase().trim();
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getImageItem() {
		return imageItem;
	}

	public void setImageItem(int imageItem) {
		this.imageItem = imageItem;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description.trim();
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
