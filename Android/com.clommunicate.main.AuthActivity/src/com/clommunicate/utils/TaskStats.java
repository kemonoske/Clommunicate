package com.clommunicate.utils;

public class TaskStats {

	private int id;
	private int count;
	private int last_comment;

	public TaskStats(int id, int count, int last_comment) {
		this.id = id;
		this.count = count;
		this.last_comment = last_comment;
	}

	public TaskStats() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getLast_comment() {
		return last_comment;
	}

	public void setLast_comment(int last_comment) {
		this.last_comment = last_comment;
	}

}
