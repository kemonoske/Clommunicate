package com.clommunicate.utils;

/**
 * Clasa POJO, descrierea entitatii Comment
 * 
 * @author Bostanica Ion
 * 
 */
public class Comment {

	private int id = 0;
	private String text = null;
	private int author = 0;
	private int owner = 0;
	
	public Comment(String text, int author, int owner) {
		super();
		this.text = text;
		this.author = author;
		this.owner = owner;
	}

	public Comment(int id, String text, int author, String time) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
		this.time = time;
	}

	public int getOwner() {
		return owner;
	}

	public void setOwner(int owner) {
		this.owner = owner;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getAuthor() {
		return author;
	}

	public void setAuthor(int author) {
		this.author = author;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	private String time = null;
}
