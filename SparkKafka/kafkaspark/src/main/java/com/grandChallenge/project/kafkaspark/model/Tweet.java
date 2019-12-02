package com.grandChallenge.project.kafkaspark.model;

import com.google.gson.annotations.SerializedName;

public class Tweet {

	private long id;
	private String text;
	private String lang;
	private User user;

	@SerializedName("retweet_count")
	private int retweetCount;

	@SerializedName("favourite_count")
	private int favCount;

	public Tweet(long id, String text, String lang, User user, int retweetCount, int favCount) {
		super();
		this.id = id;
		this.text = text;
		this.lang = lang;
		this.user = user;
		this.retweetCount = retweetCount;
		this.favCount = favCount;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public int getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(int retweetCount) {
		this.retweetCount = retweetCount;
	}

	public int getFavCount() {
		return favCount;
	}

	public void setFavCount(int favCount) {
		this.favCount = favCount;
	}
	
	
}
