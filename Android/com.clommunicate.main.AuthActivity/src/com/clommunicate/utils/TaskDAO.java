package com.clommunicate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;

/**
 * Clasa e destinata pentru interactiunea cu entitatea Tasks din API
 * 
 * @author Bostanica Ion
 * 
 */
public class TaskDAO {

	public static boolean addTask(Task task) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		parameter_list.add(new BasicNameValuePair("name", task.getName()));
		parameter_list.add(new BasicNameValuePair("description", task
				.getDescription()));
		parameter_list.add(new BasicNameValuePair("owner", String.valueOf(task
				.getOwner())));
		parameter_list.add(new BasicNameValuePair("type", String.valueOf(task
				.getType())));
		parameter_list.add(new BasicNameValuePair("assigned", String
				.valueOf(task.getAsigned_id())));

		HttpPost hp = new HttpPost(WebApi.API + "/tasks");

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doPost(parameter_list, hp);

		if (he == null)
			throw new NetworkErrorException();
		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				// User.user = UserDAO.login(User.user.getEmail());
				return true;

			} else {

				throw new WebAPIException(jo.getString("message"));

			}

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

	public static boolean updateTask(Task task) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		parameter_list.add(new BasicNameValuePair("name", task.getName()));
		parameter_list.add(new BasicNameValuePair("description", task
				.getDescription()));
		parameter_list.add(new BasicNameValuePair("type", String.valueOf(task
				.getType())));
		parameter_list.add(new BasicNameValuePair("assigned", String
				.valueOf(task.getAsigned_id())));

		HttpPut hp = new HttpPut(WebApi.API + "/tasks/" + task.getId());

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doPut(parameter_list, hp);

		if (he == null)
			throw new NetworkErrorException();
		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				return true;

			} else {

				throw new WebAPIException(jo.getString("message"));

			}

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

	public static Task getTask(int id) throws NetworkErrorException,
			WebAPIException {

		HttpGet hg = new HttpGet(WebApi.API + "/tasks/" + id);

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doGet(hg);

		if (he == null)
			throw new NetworkErrorException();
		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				JSONObject task = jo.getJSONObject("data");
				JSONObject assigned = task.getJSONObject("assigned");

				return new Task(task.getInt("id"), task.getString("name"),
						task.getString("description"), task.getInt("type"),
						task.getString("start_date"),
						task.getString("end_date"),
						((task.getInt("completed") == 1) ? true : false),
						task.getInt("owner"), new User(assigned.getInt("id"),
								assigned.getString("email"),
								assigned.getString("name"),
								assigned.getString("photo")));
			} else {

				throw new WebAPIException(jo.getString("message"));

			}

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return null;

	}

	public static ArrayList<Task> getTaskList(int id)
			throws NetworkErrorException, WebAPIException {

		ArrayList<Task> aux = new ArrayList<Task>(0);

		HttpGet hg = new HttpGet(WebApi.API + "/projects/" + id + "/tasks");

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doGet(hg);

		if (he == null)
			throw new NetworkErrorException();

		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				JSONArray ja = jo.getJSONArray("data");
				JSONObject jo1 = null;

				for (int i = 0; i < ja.length(); i++) {

					jo1 = ja.getJSONObject(i);
					aux.add(new Task(jo1.getInt("id"), jo1.getString("name"),
							jo1.getString("description"), jo1.getInt("type"),
							jo1.getString("start_date"), jo1
									.getString("end_date"), ((jo1
									.getInt("completed") == 1) ? true : false),
							jo1.getInt("owner")));

				}

			} else {

				throw new WebAPIException(jo.getString("message"));

			}
			return aux;
		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return aux;

	}

	public static boolean removeTask(int id) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		HttpDelete hd = new HttpDelete(WebApi.API + "/tasks/" + id);

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doDelete(parameter_list, hd);

		if (he == null)
			throw new NetworkErrorException();

		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				return true;

			} else {

				throw new WebAPIException(jo.getString("message"));

			}

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

	public static boolean markTaskCompleted(int id, int completed)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("complete", String
				.valueOf(completed)));

		HttpPut hp = new HttpPut(WebApi.API + "/tasks/" + id);

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = HttpRequest.doPut(parameter_list, hp);

		if (he == null)
			throw new NetworkErrorException();
		try {

			InputStream is = he.getContent();
			he = null;
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "utf-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;

			while ((line = reader.readLine()) != null) {

				sb.append(line + "\n");
			}

			JSONObject jo = new JSONObject(sb.toString());

			if (jo.getBoolean("status")) {

				return true;

			} else {

				throw new WebAPIException(jo.getString("message"));

			}

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

}
