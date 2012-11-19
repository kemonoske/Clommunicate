package com.clommunicate.utils;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Picture;

public class User {

	private String email = null;
	private String name = null;
	private Gender gender = Gender.Undefined;
	private String locale = null;
	private Bitmap picture = null;
	
	User(String email, String name, String gender, String locale, String picture) throws IOException{
		
		setEmail(email);
		setName(name);
		setGender((gender.compareToIgnoreCase("male") == 0)?Gender.Male:Gender.Female);
		setLocale(locale);
		setPicture(picture);
		
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
		if(picture != null){
			URL u = new URL(picture);
			this.picture = BitmapFactory.decodeStream(u.openConnection().getInputStream());
		}
	}

}
