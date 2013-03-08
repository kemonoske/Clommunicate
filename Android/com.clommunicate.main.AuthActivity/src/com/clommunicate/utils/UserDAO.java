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
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;

/**
 * Clasa utilizata pentru interactiunea cu entitatea User din API
 * 
 * @author Bostanica Ion
 * 
 */
public class UserDAO {

	/**
	 * 
	 * @param email
	 *            emailul folosit ca identificatorul utilizatorului
	 * @return Utilizatorul in caz ca acesta exista, null in caz contrar
	 * @throws NetworkErrorException
	 *             daca dispozitivul nu are conexiune la internet
	 * @throws WebAPIException
	 *             daca raspunsul de la server are statutul false, a aparut o
	 *             exceptie pe server
	 */
	public static User login(String email) throws NetworkErrorException,
			WebAPIException {

		HttpGet hg = new HttpGet(WebApi.API + "/users/" + email);

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

			if (jo.getBoolean("status") == true) {

				return User.deserialize(jo.getJSONObject("data"));

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

	/**
	 * Trimite datele unui obiect User catre API unde acesta este registrat in
	 * sistem
	 * 
	 * @param user
	 *            obiectul de tip user ce contine datele
	 * @return true in caz ca utilizatorul a fost registrat in sistem shi false
	 *         in caz contrar
	 * @throws NetworkErrorException
	 *             daca dispozitivul nu are conexiune la internet
	 * @throws WebAPIException
	 *             daca apare o eroare pe partea server
	 */
	public static boolean register(User user) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		/*
		 * POST parametrii
		 */
		parameter_list.add(new BasicNameValuePair("email", user.getEmail()));
		parameter_list.add(new BasicNameValuePair("name", user.getName()));
		parameter_list.add(new BasicNameValuePair("locale", user.getLocale()));
		parameter_list
				.add(new BasicNameValuePair("photo", user.getPictureURL()));
		parameter_list.add(new BasicNameValuePair("gender",
				(user.getGender() == Gender.Male) ? "1" : "0"));

		HttpPost hp = new HttpPost(WebApi.API + "/users");

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

	/**
	 * Deletes User from DB
	 * 
	 * @param id
	 *            user id
	 * @return true if user is removed
	 * @throws NetworkErrorException
	 *             if there is no internet connection
	 * @throws WebAPIException
	 *             if a server side error is thrown
	 */
	public static boolean removeUser(int id) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		HttpDelete hd = new HttpDelete(WebApi.API + "/users/" + id);

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

}
