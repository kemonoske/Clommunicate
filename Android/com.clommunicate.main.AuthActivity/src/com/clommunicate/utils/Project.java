package com.clommunicate.utils;

import java.util.ArrayList;

/**
 * Container class for project fields
 * 
 * @author Akira
 * 
 */
public class Project {

	private int id = 0;
	private String name = null;
	private String description = null;
	private int owner_id = 0;
	private ArrayList<User> member_list = null;
	private ArrayList<Task> task_list = null;
	private String deadline = null;
	private String end_date = null;
	private String start_date = null;

	public Project(int id, String name, String description, String start_date,
			String deadline, String end_date, int owner_id) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.start_date = start_date;
		this.deadline = deadline;
		this.end_date = end_date;
	}

	public Project(String name, String description, int owner_id,
			String deadline, ArrayList<User> member_list) {
		this.name = name;
		this.description = description;
		this.owner_id = owner_id;
		this.deadline = deadline;
		this.member_list = member_list;
	}

	public Project(int id, String name, String description, String deadline) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.deadline = deadline;
	}

	public String getDeadline() {
		return deadline;
	}

	public String getDescription() {
		return description;
	}

	public String getEnd_date() {
		return end_date;
	}

	public int getId() {
		return id;
	}

	public ArrayList<User> getMember_list() {
		return member_list;
	}

	public String getName() {
		return name;
	}

	public int getOwner_id() {
		return owner_id;
	}

	public String getStart_date() {
		return start_date;
	}

	public ArrayList<Task> getTask_list() {
		return task_list;
	}

	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setMember_list(ArrayList<User> member_list) {
		this.member_list = member_list;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setOwner_id(int owner_id) {
		this.owner_id = owner_id;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public void setTask_list(ArrayList<Task> task_list) {
		this.task_list = task_list;
	}

	public boolean isCompleted() {

		return getEnd_date().compareToIgnoreCase("null") != 0;

	}

}
