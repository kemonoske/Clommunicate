package com.clommunicate.main;

import java.io.IOException;

import com.clommunicate.main.R;
import com.clommunicate.oAuth2.AuthUtils;
import com.clommunicate.oAuth2.GoogleAccountManager;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.accounts.AuthenticatorException;
import android.accounts.OperationCanceledException;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistrationActivity extends Activity {
	private TextView name = null;
	private TextView email = null;
	private TextView locale = null;
	private TextView gender = null;
	private ImageView picture = null;
	private WaitDialog wd = null;
	private Activity me = this;
	private User usr = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		

		name = (TextView) findViewById(R.id.reg_nume);
		email = (TextView) findViewById(R.id.reg_email);
		locale = (TextView) findViewById(R.id.reg_location);
		gender = (TextView) findViewById(R.id.reg_gender);
		picture = (ImageView) findViewById(R.id.reg_foto);
		
		/* Setam fontul etichetei login */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.regLabel)).setTypeface(type);

		/* Setam fontul la restul etichetelor */
		type = Typeface.createFromAsset(getAssets(), "fonts/asen.ttf");
		((TextView) findViewById(R.id.reg_name_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_email_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_location_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_gender_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_photo_label)).setTypeface(type);
		((EditText) findViewById(R.id.reg_nume)).setTypeface(type);
		((EditText) findViewById(R.id.reg_email)).setTypeface(type);
		((EditText) findViewById(R.id.reg_location)).setTypeface(type);
		((EditText) findViewById(R.id.reg_gender)).setTypeface(type);

		ImageButton ib = (ImageButton) findViewById(R.id.registerButton);

		ib.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				WaitDialog wd = new WaitDialog(v.getRootView().getContext());
				wd.setTitle(String.format("%100s", "User registration."));
				wd.show();
			}

		});
	
		AsyncTask<String, Void, Void> load_data = new AsyncTask<String, Void, Void>() {
			
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
				wd = new WaitDialog(me);
				wd.setTitle(String.format("%-100s","Loading oAuth information..."));
				wd.show();
		    }

		    @Override
		    protected Void doInBackground(String... params) {

		    	GoogleAccountManager googleAccountManager = new GoogleAccountManager(
						me);
				AccountManager am = AccountManager.get(getBaseContext());
				Account[] accounts = am.getAccountsByType("com.google");

				Account acc = null;
				
				for(Account i : accounts)	{
					
					if(i.name.equalsIgnoreCase(getIntent().getExtras().getString("email")))	{
						
						acc = i;
						break;
						
					}
				}
				
				AuthUtils.refreshAuthToken(me, acc);
				
				System.err.println(acc.name);
				SharedPreferences settings = getSharedPreferences("Clommunicate", 0);
				String accessToken = null;
				
				do{
					accessToken = settings.getString("accessToken", null);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} while (accessToken.equals(null));

				System.err.println(accessToken);
			    usr = WebApi.getGoogleUser(accessToken,acc.name);
			    
				return null;
		    	
		    }

		    @Override
		    protected void onPostExecute(Void result) {
				name.setText(usr.getName());
				name.setEnabled(false);
				email.setText(usr.getEmail());
				email.setEnabled(false);
				locale.setText(usr.getLocale());
				locale.setEnabled(false);
				gender.setText(usr.getGender().toString());
				gender.setEnabled(false);
				picture.setImageBitmap(usr.getPicture());
		        wd.dismiss();
		    }
		};
		
		load_data.execute(null);
		
	}

	
	

	
}
