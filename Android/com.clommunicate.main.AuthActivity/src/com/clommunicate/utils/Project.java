package com.clommunicate.utils;

import java.util.ArrayList;

import android.R.string;
import android.graphics.Bitmap;

public class Project {

	private String name = null;
	private String description = null;
	private Bitmap logo = null;
	private int owner_id = 0;
	private ArrayList<User> member_list = null;
	private String end_date = null;
	private String start_date = null;

	
	public Project(String name, String description, int owner_id, String end_date, ArrayList<User> member_list) {
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.end_date = end_date;
		this.member_list = member_list;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Bitmap getLogo() {
		return logo;
	}

	public void setLogo(Bitmap logo) {
		this.logo = logo;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public ArrayList<User> getMember_list() {
		return member_list;
	}

	public void setMember_list(ArrayList<User> member_list) {
		this.member_list = member_list;
	}

}
