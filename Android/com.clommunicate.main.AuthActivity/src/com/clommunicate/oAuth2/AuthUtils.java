package com.clommunicate.oAuth2;

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
 * 
 * @author Akira
 *
 */

public class AuthUtils {
	public static final String PREF_NAME = "Clommunicate";
	public static final String PREF_TOKEN = "accessToken";
	public static final String SCOPE = "oauth2:https://www.googleapis.com/auth/userinfo.profile";
	private static int state = 0;
	
	public static int refreshAuthToken(final Activity activity, Account account) throws NetworkErrorException{
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
						state = 1;
						//System.err.println("Auth token commited:" + authToken);
					} else if (authIntent != null) {
						//System.err.println("Allow shit");
						//authIntent.
				    	authIntent.setFlags(authIntent.getFlags() & ~Intent.FLAG_ACTIVITY_NEW_TASK);
						activity.startActivityForResult(authIntent, 0);
						state = 2;
					} else {
						state = 0;
						//System.err.println("AccountManager was unable to obtain an authToken.");
					}
				} catch (Exception e) {
					state = -1;
				} 
				
				alive = false;
				synchronized (this) {
					this.notify();
				}
			}
			

			@Override
			public boolean equals(Object o) {
				return alive;
			}
			
		};
		System.err.println("INVALIDATING TOKEN:" + accessToken);
		AccountManager.get(activity).invalidateAuthToken("com.google",
				accessToken);
		System.err.println("GETTING TOKEN:");
		AccountManager.get(activity).getAuthToken(account, SCOPE, true, cb,
				null);
		synchronized (cb) {
			if(cb.equals(null))
				try {
					cb.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
		}
		return state;
	}
}