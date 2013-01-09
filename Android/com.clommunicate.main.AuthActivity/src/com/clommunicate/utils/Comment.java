package com.clommunicate.utils;

/**
 * 
 * @author Akira
 *
 */
public class Comment {

	private int id = 0;
	private String text = null;
	private User author = null;
	
	public Comment(int id, String text, User author, String time) {
		super();
		this.id = id;
		this.text = text;
		this.author = author;
		this.time = time;
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
	public User getAuthor() {
		return author;
	}
	public void setAuthor(User author) {
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
