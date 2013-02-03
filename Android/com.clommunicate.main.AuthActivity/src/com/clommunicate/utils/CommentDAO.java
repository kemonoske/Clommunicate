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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.accounts.NetworkErrorException;

/**
 * Clasa DAO ce contine metode pentru accesul la datele entitatii Comment prin
 * intermediul unui API Web al aplicatiei
 * 
 * @author Bostanica Ion
 * 
 */
public class CommentDAO {

	/**
	 * Adaugarea unui comentariu in BD
	 * @param comment obiectul POJO ce contine datele comenatriului
	 * @return true in caz de succes si false in caz de eroare
	 * @throws NetworkErrorException daca lipseste conexiunea la internet
	 * @throws WebAPIException daca apare o eroare pe partea server a aplicatiei
	 */
	public static boolean addComment(Comment comment)
			throws NetworkErrorException, WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("text", comment.getText()));
		parameter_list.add(new BasicNameValuePair("author", String
				.valueOf(comment.getAuthor())));

		HttpPost hp = new HttpPost(WebApi.API + "/tasks/" + comment.getOwner()
				+ "/comments");

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
	 * Primirea comentariilor la un task 
	 * @param id id'ul taskului
	 * @return ArrayList<Coment> lista de Obiecte POJO ce reprezinta comentarii
	 * @throws NetworkErrorException daca lipseste conexiunea la internet
	 * @throws WebAPIException daca a avul loc o eroare pe server
	 */
	public static ArrayList<Comment> getCommentList(int id)
			throws NetworkErrorException, WebAPIException {

		ArrayList<Comment> aux = new ArrayList<Comment>(0);

		HttpGet hg = new HttpGet(WebApi.API + "/tasks/" + id + "/comments");

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
					aux.add(new Comment(jo1.getInt("id"),
							jo1.getString("text"), jo1.getInt("author"), jo1
									.getString("time")));

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

	/**
	 * Stergerea unui comentariu din BD
	 * @param id id'ul comentariului
	 * @return true in caz de succes si false in caz contrar
	 * @throws NetworkErrorException daca lipseste conexiunea la internet
	 * @throws WebAPIException daca a avut loc o eroare pe server
	 */
	public static boolean removeComment(int id) throws NetworkErrorException,
			WebAPIException {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);

		HttpDelete hd = new HttpDelete(WebApi.API + "/comments/" + id);

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
