package com.clommunicate.main;


import com.clommunicate.main.R;
import com.clommunicate.oAuth2.AuthUtils;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
	public void onBackPressed() {

		finish();
		
	}

	
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
				WaitDialog wd = new WaitDialog(me);
				wd.setTitle(String.format("%-100s", "User registration."));
				wd.show();
				String text = null;
				if(WebApi.register(usr)){
					System.err.println(true);
					text = "Registration success.";
					Intent i = new Intent(getApplicationContext(),
							UserActivity.class);
					
					WebApi.fillClommunicateUser(usr);
					User.user = usr;
					startActivity(i);
					finish();
				}	else {

					System.err.println(false);
					text = "Registration failed.";
					onBackPressed();
				}
				
				wd.dismiss();

				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
			}

		});

		loadUserData();
	}
	
	private void loadUserData(){
		

		AsyncTask<String, Void, User> load_data = new AsyncTask<String, Void, User>() {
			
		    @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
				wd = new WaitDialog(me);
				wd.setTitle(String.format("%-100s","Loading oAuth information..."));
				wd.show();
		    }

		    @Override
		    protected User doInBackground(String... params) {

		    	/*GoogleAccountManager googleAccountManager = new GoogleAccountManager(
						me);*/
				AccountManager am = AccountManager.get(getBaseContext());
				Account[] accounts = am.getAccountsByType("com.google");

				Account acc = null;
				
				for(Account i : accounts)	{
					
					if(i.name.equalsIgnoreCase(getIntent().getExtras().getString("email")))	{
						
						acc = i;
						break;
						
					}
				}
				
				//TODO: If connection is closed throw somenthing.
				
				 
				//TODO: If acces token is not updated need to do somenthing

				AuthUtils.refreshAuthToken(me, acc);
				String accessToken = null;
				SharedPreferences settings = getSharedPreferences("Clommunicate", 0);
				Editor edit = settings.edit();
				edit.putString("accessToken", null);
				edit.commit();
				edit = null;
				System.err.println("before");
				do{
					AuthUtils.refreshAuthToken(me, acc);
					settings = getSharedPreferences("Clommunicate", 0);
					accessToken = settings.getString("accessToken", null);
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} while (accessToken == null);

				System.err.println(accessToken);
			    usr = WebApi.getGoogleUser(accessToken,acc.name);
				WebApi.fillClommunicateUser(usr);
			    //TODO: Check here for exception in case of failed authentification
			    
				return usr;
		    	
		    }

		    @Override
		    protected void onPostExecute(User user) {
				name.setText(user.getName());
				name.setEnabled(false);
				email.setText(user.getEmail());
				email.setEnabled(false);
				locale.setText(user.getLocale());
				locale.setEnabled(false);
				gender.setText(user.getGender().toString());
				gender.setEnabled(false);
				picture.setImageBitmap(user.getPicture());
		        wd.dismiss();
		    }
		};
		
		load_data.execute((String)null);
		
	}

	
	

	
}
