package com.clommunicate.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * Contine un set de metode statice destinate executiei requesturilor Http toate
 * metodele returneaza HttpEntity ce contine raspunsul la request
 * 
 * @author Bostanica Ion
 * 
 */
public class HttpRequest {

	public static HttpEntity doGet(HttpGet hg) throws WebAPIException {

		/* Creare HttRequestului GET si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hg);
		} catch (IllegalStateException e) {

			throw new WebAPIException(e.getMessage());

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}

		hc = null;
		hg = null;

		if (hr == null)
			return null;

		if (WebApi.isBadStatusCode(hr.getStatusLine().getStatusCode()))
			throw new WebAPIException(WebApi.statusCodes.get(hr.getStatusLine()
					.getStatusCode()));

		return hr.getEntity();

	}

	public static HttpEntity doPost(ArrayList<NameValuePair> parameter_list,
			HttpPost hp) throws WebAPIException {

		/* Creare HttRequestului Post si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();

		try {
			hp.setEntity(new UrlEncodedFormEntity(parameter_list, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		parameter_list.clear();
		parameter_list = null;

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hp);
		} catch (IllegalStateException e) {

			throw new WebAPIException(e.getMessage());

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		hc = null;
		hp = null;

		if (hr == null)
			return null;

		if (WebApi.isBadStatusCode(hr.getStatusLine().getStatusCode()))
			throw new WebAPIException(WebApi.statusCodes.get(hr.getStatusLine()
					.getStatusCode()));

		return hr.getEntity();

	}

	public static HttpEntity doPut(ArrayList<NameValuePair> parameter_list,
			HttpPut hp) throws WebAPIException {

		/* Creare HttRequestului Post si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();

		try {
			hp.setEntity(new UrlEncodedFormEntity(parameter_list, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		parameter_list.clear();
		parameter_list = null;

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hp);
		} catch (IllegalStateException e) {

			throw new WebAPIException(e.getMessage());

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		hc = null;
		hp = null;

		if (hr == null)
			return null;

		if (WebApi.isBadStatusCode(hr.getStatusLine().getStatusCode()))
			throw new WebAPIException(WebApi.statusCodes.get(hr.getStatusLine()
					.getStatusCode()));
		return hr.getEntity();

	}

	public static HttpEntity doDelete(ArrayList<NameValuePair> parameter_list,
			HttpDelete hd) throws WebAPIException {

		/* Creare HttRequestului Post si transmiterea acestuia */
		HttpClient hc = new DefaultHttpClient();

		parameter_list.clear();
		parameter_list = null;

		/* Receptionarea raspunsului la request shi returnarea */
		HttpResponse hr = null;
		try {

			hr = hc.execute(hd);
		} catch (IllegalStateException e) {

			throw new WebAPIException(e.getMessage());

		} catch (ClientProtocolException e) {

			e.printStackTrace();

		} catch (IOException e) {

			e.printStackTrace();

		}
		hc = null;
		hd = null;

		if (hr == null)
			return null;

		if (WebApi.isBadStatusCode(hr.getStatusLine().getStatusCode()))
			throw new WebAPIException(WebApi.statusCodes.get(hr.getStatusLine()
					.getStatusCode()));
		return hr.getEntity();

	}
}
