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
 * Clasa e destinata pentru interactiunea cu Entitatea Projects din API
 * 
 * @author Akira
 * 
 */
public class ProjectDAO {

	public static boolean addProject(Project project)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("name", project.getName()));
		parameter_list.add(new BasicNameValuePair("description", project
				.getDescription()));
		parameter_list.add(new BasicNameValuePair("owner", String
				.valueOf(project.getOwner_id())));
		parameter_list.add(new BasicNameValuePair("deadline", project
				.getDeadline()));

		ArrayList<User> users = project.getMember_list();
		for (int i = 0; i < users.size(); i++) {
			parameter_list.add(new BasicNameValuePair("members[" + i + "]",
					String.valueOf(users.get(i).getId())));
		}

		HttpPost hp = new HttpPost(WebApi.API + "/projects");

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

				User.user = UserDAO.login(User.user.getEmail());
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

	public static boolean updateProject(Project project)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("name", project.getName()));
		parameter_list.add(new BasicNameValuePair("description", project
				.getDescription()));
		parameter_list.add(new BasicNameValuePair("deadline", String
				.valueOf(project.getDeadline())));

		HttpPut hp = new HttpPut(WebApi.API + "/projects/" + project.getId());

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

	public static boolean markProjectCompleted(int id)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list
				.add(new BasicNameValuePair("complete", String.valueOf(1)));

		HttpPut hp = new HttpPut(WebApi.API + "/projects/" + id);

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

	public static ArrayList<Project> getProjectList(int user_id, boolean partIn)
			throws NetworkErrorException, WebAPIException {

		ArrayList<Project> aux = new ArrayList<Project>(0);

		HttpGet hg = null;
		
		if (partIn)
			hg = new HttpGet(WebApi.API + "/users/" + user_id
					+ "/part_in_projects");
		else
			hg = new HttpGet(WebApi.API + "/users/" + user_id + "/projects");
		
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

					aux.add(new Project(jo1.getInt("id"),
							jo1.getString("name"),
							jo1.getString("description"), jo1
									.getString("start_date"), jo1
									.getString("deadline"), jo1
									.getString("end_date"), jo1.getInt("owner")));

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

	public static ArrayList<User> getProjectMembers(int id)
			throws NetworkErrorException, WebAPIException {

		ArrayList<User> aux = new ArrayList<User>(0);

		HttpGet hg = null;

		hg = new HttpGet(WebApi.API + "/projects/" + id + "/members");

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
					aux.add(new User(jo1.getInt("id"), jo1.getString("email"),
							jo1.getString("name"), jo1.getString("photo")));

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

	public static boolean addMember(int pid, int uid)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		parameter_list.add(new BasicNameValuePair("uid", String.valueOf(uid)));

		HttpPost hp = new HttpPost(WebApi.API + "/projects/" + pid + "/members");

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

	public static boolean removeMember(int pid, int uid)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		HttpDelete hd = new HttpDelete(WebApi.API + "/projects/" + pid
				+ "/members/" + uid);

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

			if (jo.getBoolean("status") == true) {

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

	public static boolean removeProject(int id) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		HttpDelete hd = new HttpDelete(WebApi.API + "/projects/" + id);

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

			if (jo.getBoolean("status") == true) {

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
