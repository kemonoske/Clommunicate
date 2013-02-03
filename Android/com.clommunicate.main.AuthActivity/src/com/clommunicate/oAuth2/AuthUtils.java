package com.clommunicate.oAuth2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONException;
import org.json.JSONObject;

import com.clommunicate.utils.HttpRequest;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebAPIException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

/**
 * Clasa se utilizează pentru interacțiunea cu serviciile Google API, permite
 * primirea unui token de autentificare utilizînd contul selectat shi primirea
 * de la GoogleAPI a informatiilor personale a utilizatorului prin intermediul
 * tokenului primit anterior
 * 
 * @author Bostanica Ion
 * 
 */

public class AuthUtils {
	/*
	 * Linkul catre user info din sistemul oAuth Google
	 */
	public static final String GOOGLE_API_OAUTH = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token=";

	/*
	 * Numele aplicatiei client
	 */
	public static final String PREF_NAME = "Clommunicate";

	/*
	 * Identificatorul tokenului in tabela preferences
	 */
	public static final String PREF_TOKEN = "accessToken";

	/*
	 * Domenul de acces oAuth necesar
	 */
	public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";

	/*
	 * Starea operației (codul exceptiei)
	 */
	private static int state = 0;

	/**
	 * Permite primirea unui acces token de la Google oAuth
	 * 
	 * @param activity
	 *            activitatea parinte din care e apelata
	 * @param account
	 *            contul folosit pentru autentificare
	 * @return codul errorii
	 * @throws NetworkErrorException
	 */
	public static int refreshAuthToken(final Activity activity, Account account)
			throws NetworkErrorException {

		final SharedPreferences settings = activity.getSharedPreferences(
				PREF_NAME, 0);

		String accessToken = settings.getString(PREF_TOKEN, "");

		final AccountManagerCallback<Bundle> cb = new AccountManagerCallback<Bundle>() {
			public boolean alive = true;

			public void run(AccountManagerFuture<Bundle> future) {
				try {

					alive = true;

					final Bundle result = future.getResult();
					final String accountName = result
							.getString(AccountManager.KEY_ACCOUNT_NAME);
					final String authToken = result
							.getString(AccountManager.KEY_AUTHTOKEN);
					final Intent authIntent = result
							.getParcelable(AccountManager.KEY_INTENT);

					if (accountName != null && authToken != null) {

						final SharedPreferences.Editor editor = settings.edit();
						editor.putString(PREF_TOKEN, authToken);
						editor.commit();

						/*
						 * Starea 1 indica ca tokenul a fost primit cu success
						 * shi inscris in tabela preferences a applicatiei
						 */
						state = 1;

					} else if (authIntent != null) {

						authIntent.setFlags(authIntent.getFlags()
								& ~Intent.FLAG_ACTIVITY_NEW_TASK);
						activity.startActivityForResult(authIntent, 0);
						/*
						 * Starea 2 indica ca a fost lansata o Activitate de
						 * confirmare a acceselui la contul google a
						 * utilizatorului
						 */
						state = 2;

					} else {

						/*
						 * Aparitia unei situatii exceptionale serverul google e
						 * inactiv sau inaccesibil
						 */
						state = 0;

					}

				} catch (Exception e) {

					/*
					 * Lipseste conexiunea la internet
					 */
					state = -1;

				}

				alive = false;

				/*
				 * Notoficarea despre sfirshitul operatiei
				 */
				synchronized (this) {
					this.notify();
				}

			}

			/**
			 * Intoarce true daca Activitatea inca este activa, se utilizeaza
			 * pentru a determina momentul cind activitatea termina executia
			 */
			@Override
			public boolean equals(Object o) {

				return alive;

			}

		};

		/*
		 * Invalidarea tokenului folosit la logarea anterioara de pe
		 * dispozitivul acesta
		 */
		System.err.println("INVALIDATING TOKEN:" + accessToken);
		AccountManager.get(activity).invalidateAuthToken("com.google",
				accessToken);

		/*
		 * Primirea unui nou token
		 */
		System.err.println("GETTING TOKEN:");
		AccountManager.get(activity).getAuthToken(account, SCOPE, true, cb,
				null);

		/*
		 * Functia nu poate returna o valoare atit timp cit nu sa terminat
		 * procesul de primire a tokenului
		 */
		synchronized (cb) {

			/*
			 * Daca activitatea de primire inca e activa
			 */
			if (cb.equals(null))
				try {

					/*
					 * Asteptam o notificare care indica sfirsitul activitatii
					 */
					cb.wait();

				} catch (InterruptedException e) {

					e.printStackTrace();

				}

		}

		return state;

	}

	/**
	 * Permite primirea datelor personale din sistemul google oAuth utilizaind
	 * tokenul primit anterior pentru accountul dat.
	 * 
	 * @param acces_token
	 *            tokenul
	 * @param email
	 *            emailul, folosit pentru suplinirea datelor
	 * @return User user obiectul de tip User creat din datele primite
	 * @throws NetworkErrorException
	 *             daca lipseste conexiune la internet
	 * @throws WebAPIException
	 *             daca serverul Google e inaccesibil
	 */
	public static User getGoogleUser(String acces_token, String email)
			throws NetworkErrorException, WebAPIException {

		/*
		 * Se utilizeaza linkul de acces oAuth + tokenul pentru a crea o cerere
		 * de tip GET
		 */
		HttpGet hg = new HttpGet(GOOGLE_API_OAUTH + acces_token);
		HttpEntity he = HttpRequest.doGet(hg);

		if (he == null)
			throw new NetworkErrorException();

		/*
		 * Prelucrarea raspunsului in format JSON
		 */
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

			String picture = null;
			try {
				picture = jo.getString("picture");
			} catch (JSONException e) {
				picture = null;
			}

			boolean gen = true;
			try {
				gen = (jo.getString("gender").compareToIgnoreCase("male") == 0) ? true
						: false;
			} catch (JSONException e) {

			}

			String loc = "en-GB";
			try {
				loc = jo.getString("locale");
			} catch (JSONException e) {

			}

			User aux = new User(email, jo.getString("name"), gen, loc, picture);
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
}