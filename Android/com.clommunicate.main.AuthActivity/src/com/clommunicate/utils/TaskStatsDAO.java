package com.clommunicate.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TaskStatsDAO {

	// Database fields
	private SQLiteDatabase database;
	private ClommunicateSQLiteHelper dbHelper;
	private String[] columns = { "id", "count", "last_comment" };
	private static final String DATABASE_CREATE = "CREATE TABLE "
			+ ClommunicateSQLiteHelper.TABLE
			+ " ( id INTEGER PRIMARY KEY AUTOINCREMENT, count INTEGER, last_comment INTEGER)";

	public TaskStatsDAO(Context context) {
		dbHelper = new ClommunicateSQLiteHelper(context);
	}

	public void open() throws SQLException {
		database = dbHelper.getWritableDatabase();
	}

	public void close() {
		dbHelper.close();
	}

	public void create() {

		database.execSQL(DATABASE_CREATE);

	}

	public TaskStats add(TaskStats ts) {
		ContentValues values = new ContentValues();
		values.put("id", ts.getId());
		values.put("count", ts.getCount());
		values.put("last_comment", ts.getLast_comment());

		long insertId = database.insert(ClommunicateSQLiteHelper.TABLE, null,
				values);
		Cursor cursor = database.query(ClommunicateSQLiteHelper.TABLE, columns,
				"id = " + insertId, null, null, null, null);
		cursor.moveToFirst();
		TaskStats newComment = cursorToTaskStats(cursor);
		cursor.close();
		return newComment;
	}

	public boolean exists(int id) {
		
		Cursor cursor = database.rawQuery("SELECT COUNT(*) cnt " + "FROM "
				+ ClommunicateSQLiteHelper.TABLE + " " + "WHERE id = " + id,
				null);

		cursor.moveToFirst();
		if (!cursor.isAfterLast())
			if (cursor.getLong(0) > 0)
				return true;

		return false;
	}

	public boolean nullify(int id, int last_comment){

		ContentValues args = new ContentValues();
		args.put("count", 0);
		args.put("last_comment", last_comment);
		database.update(ClommunicateSQLiteHelper.TABLE, args, "id = " + id, null);
		
		return true;
	}

	public TaskStats get(int id) {

		Cursor cursor = database.rawQuery("SELECT * " + "FROM "
				+ ClommunicateSQLiteHelper.TABLE + " " + "WHERE id = " + id,
				null);

		TaskStats ts = null;

		cursor.moveToFirst();
		if (!cursor.isAfterLast())
			ts = cursorToTaskStats(cursor);

		return ts;
	}

	public void delete(int id) {

		database.delete(ClommunicateSQLiteHelper.TABLE, "id = " + id, null);
	}

	/*
	 * public List<Comment> getAllComments() { List<Comment> comments = new
	 * ArrayList<Comment>();
	 * 
	 * Cursor cursor = database.query(MySQLiteHelper.TABLE_COMMENTS, allColumns,
	 * null, null, null, null, null);
	 * 
	 * cursor.moveToFirst(); while (!cursor.isAfterLast()) { Comment comment =
	 * cursorToComment(cursor); comments.add(comment); cursor.moveToNext(); } //
	 * Make sure to close the cursor cursor.close(); return comments; }
	 */

	private TaskStats cursorToTaskStats(Cursor cursor) {
		TaskStats taskStats = new TaskStats();
		taskStats.setId(cursor.getInt(0));
		taskStats.setCount(cursor.getInt(1));
		taskStats.setLast_comment(cursor.getInt(2));
		return taskStats;
	}
}
