package com.clommunicate.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

public class WebApi {

	public static boolean login(String email) {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("email", email));

		/* Receptionarea entitatii din raspuns shi decodarea JSON */
		HttpEntity he = DoPost(parameter_list);

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
			return jo.getBoolean("status");

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}

/*public static boolean register(User user) {

		ArrayList<NameValuePair> parameter_list = new ArrayList<NameValuePair>(
				0);
		parameter_list.add(new BasicNameValuePair("email", email));

		/* Receptionarea entitatii din raspuns shi decodarea JSON *//*
		HttpEntity he = DoPost(parameter_list);

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
			return jo.getBoolean("status");

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return false;

	}
*/
	public static User getGoogleUser(String acces_token, String email) {

		HttpEntity he = DoGet(acces_token);
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

			String picture= null;
			try{
				picture = jo.getString("picture");
			} catch(JSONException e){
				picture = null;
			}
			
			User aux = new User(email, jo.getString("name"),
					jo.getString("gender"), jo.getString("locale"),picture
					);
			return aux;

		} catch (IllegalStateException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		} catch (JSONException e) {

			e.printStackTrace();

		}

		return null;

	}

	private static HttpEntity DoPost(ArrayList<NameValuePair> parameter_list) {

		/* Creare HttRequestului Post si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();
		HttpPost hp = new HttpPost(
				"http://clommunicate.freehosting.md/CheckUser.php");

		try {
			hp.setEntity(new UrlEncodedFormEntity(parameter_list));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		parameter_list.clear();
		parameter_list = null;

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hp);

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		hc = null;
		hp = null;

		return hr.getEntity();

	}

	private static HttpEntity DoGet(String acces_token) {

		/* Creare HttRequestului Post si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();
		HttpGet hg = new HttpGet(
				"https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="
						+ acces_token);

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hg);

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		hc = null;
		hg = null;

		return hr.getEntity();

	}

}
