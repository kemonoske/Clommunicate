package com.clommunicate.utils;

import java.io.IOException;
import java.net.URL;

import org.json.JSONObject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

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

	User()	{
		
		
		
	}
	
	User(int id, String name, String picture) throws IOException {

		setId(id);
		setName(name);
		setPicture(picture);
		setPictureURL((picture == null) ? "null" : picture);

	}

	User(int id, String email, String name, String picture) throws IOException {

		setId(id);
		setEmail(email);
		setName(name);
		setPicture(picture);
		setPictureURL((picture == null) ? "null" : picture);

	}

	public User(String email, String name, boolean gender, String locale,
			String picture) throws IOException {

		setEmail(email);
		setName(name);
		setGender((gender) ? Gender.Male : Gender.Female);
		setLocale(locale);
		setPicture(picture);
		setPictureURL((picture == null) ? "null" : picture);

	}

	User(int id, String email, String name, boolean gender, String locale,
			String picture, int projects, int partIn) throws IOException {
		setId(id);
		setEmail(email);
		setName(name);
		setGender((gender) ? Gender.Male : Gender.Female);
		setLocale(locale);
		setPicture(picture);
		setPictureURL((picture == null) ? "null" : picture);
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
		if (picture != null && picture.compareToIgnoreCase("null") != 0) {
			URL u = new URL(picture);
			this.picture = BitmapFactory.decodeStream(u.openConnection()
					.getInputStream());
		}
	}

	public static User deserialize(JSONObject jo) {

		User aux = new User();
		
		try{
			aux.setId(jo.getInt("id"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setEmail(jo.getString("email"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setName(jo.getString("name"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setGender((jo.getInt("gender") == 1) ? Gender.Male : Gender.Female);
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}

		try{
			aux.setLocale(jo.getString("locale"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setPicture(jo.getString("photo"));
			aux.setPictureURL(jo.getString("photo"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setProjects(jo.getInt("projects_created"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}
		
		try{
			aux.setPartIn(jo.getInt("projects_part_in"));
		}	catch (Exception e){
			
			e.printStackTrace();
			
		}

		return aux;
		
	}
}
