package com.clommunicate.oAuth2;

/*
 * Copyright 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

/**
 * Collection of OAuth 2 utilities to achieve "one-click" approval on Android.
 * 
 * @author Chirag Shah <chirags@google.com>
 */
public class AuthUtils {
	private static final String TAG = AuthUtils.class.getName();

	public static final String PREF_NAME = "Clommunicate";
	public static final String PREF_TOKEN = "accessToken";
	public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile"; // Or
																									// "Google Buzz"

	public static void refreshAuthToken(final Activity activity, Account account) {
		final SharedPreferences settings = activity.getSharedPreferences(
				PREF_NAME, 0);
		String accessToken = settings.getString(PREF_TOKEN, "");
		final AccountManagerCallback<Bundle> cb = new AccountManagerCallback<Bundle>() {
			public boolean alive = true;
			
			public void run(AccountManagerFuture<Bundle> future) {
				try {
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
						System.err.println("Auth token commited:" + authToken);
					} else if (authIntent != null) {
						activity.startActivityForResult(authIntent,0);
					} else {
						Log.e(TAG,
								"AccountManager was unable to obtain an authToken.");
					}
				} catch (Exception e) {
					Log.e(TAG, "Auth Error", e);
				}
				
				alive = false;
			}
			


			@Override
			public boolean equals(Object o) {
				return alive;
			}
			
		};
		
		
		AccountManager.get(activity).invalidateAuthToken("com.google",
				accessToken);
		AccountManager.get(activity).getAuthToken(account, SCOPE, true, cb,
				null);
		while(cb.equals(null))
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
	}
}