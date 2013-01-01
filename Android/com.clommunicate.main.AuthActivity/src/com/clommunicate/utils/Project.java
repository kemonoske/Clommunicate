package com.clommunicate.utils;

import java.util.ArrayList;

import android.R.string;
import android.graphics.Bitmap;

public class Project {

	private int id = 0;
	private String name = null;
	private String description = null;
	private Bitmap logo = null;
	private int owner_id = 0;
	private ArrayList<User> member_list = null;
	private String deadline = null;
	private String end_date = null;
	private String start_date = null;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getDeadline() {
		return deadline;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public Project(String name, String description, int owner_id,
			String deadline, ArrayList<User> member_list) {
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.deadline = deadline;
		this.member_list = member_list;
	}

	public Project(int id, String name, String description, String start_date, String deadline,
			String end_date, int owner_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.start_date = start_date;
		this.deadline = deadline;
		this.end_date = end_date;
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
