package com.clommunicate.utils;

public class Task {

	public final static int GENERAL = 1;
	public final static int CODE = 2;
	public final static int DESIGN = 3;
	public final static int IMPLEMENTATION = 4;
	public final static int PROJECT = 5;
	public final static int DEBUG = 6;
	public final static int BENCHMARK = 7;

	private int id = 0;
	private String name = null;
	private String description = null;
	private int type = 0;
	private String start_date = null;
	private String end_date = null;
	private boolean completed = false;
	private int owner = 0;
	private int asigned  = 0;


	public Task(String name, String description, int type,int asigned,  int owner) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.asigned = asigned;
		this.owner = owner;
	}
	
	public Task(int id, String name, String description, int type,
			String start_date, String end_date, boolean completed, int owner) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.type = type;
		this.start_date = start_date;
		this.end_date = end_date;
		this.completed = completed;
		this.owner = owner;
	}

	
	
	public int getAsigned() {
		return asigned;
	}

	public void setAsigned(int asigned) {
		this.asigned = asigned;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getStart_date() {
		return start_date;
	}

	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}

	public String getEnd_date() {
		return end_date;
	}

	public void setEnd_date(String end_date) {
		this.end_date = end_date;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

}
