package com.clommunicate.utils;

import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;

public class User {

	private int id = 0;
	private String email = null;
	private String name = null;
	private Gender gender = Gender.Undefined;
	private String locale = null;
	private Bitmap picture = null;
	private String pictureURL = null;
	private int projects = 0;
	private int partIn = 0;
	public static User user = null;

	User(String email, String name, boolean gender, String locale,
			String picture) throws IOException {

		setEmail(email);
		setName(name);
		setGender((gender) ? Gender.Male : Gender.Female);
		setLocale(locale);
		setPicture(picture);
		setPictureURL(picture);

	}

	User(int id, String email, String name, boolean gender, String locale,
			String picture, int projects, int partIn) throws IOException {
		setId(id);
		setEmail(email);
		setName(name);
		setGender((gender) ? Gender.Male : Gender.Female);
		setLocale(locale);
		setPicture(picture);
		setPictureURL(picture);
		setProjects(projects);
		setPartIn(partIn);

	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getProjects() {
		return projects;
	}

	public void setProjects(int projects) {
		this.projects = projects;
	}

	public int getPartIn() {
		return partIn;
	}

	public void setPartIn(int partIn) {
		this.partIn = partIn;
	}

	public String getPictureURL() {
		return pictureURL;
	}

	public void setPictureURL(String pictureURL) {
		this.pictureURL = pictureURL;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public Bitmap getPicture() {
		return picture;
	}

	public void setPicture(String picture) throws IOException {
		if (picture != null) {
			URL u = new URL(picture);
			this.picture = BitmapFactory.decodeStream(u.openConnection()
					.getInputStream());
		}
	}

}
