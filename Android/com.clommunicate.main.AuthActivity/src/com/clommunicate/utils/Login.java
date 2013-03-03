package com.clommunicate.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * 
 * @author Bostanica Ion
 *
 */
public class Login{

	public static String[] getDeviceAccounts(Context sender){
		
		AccountManager am = AccountManager.get(sender);
		Account[] accounts = am.getAccountsByType("com.google");
		
		String[] values = new String[accounts.length];
		for(int i = 0; i < accounts.length; i++)
			values[i] = accounts[i].name;
		
		return values;
		
	}

}
