package com.clommunicate.main;

import com.clommunicate.main.R;
import com.clommunicate.oAuth2.AuthUtils;
import com.clommunicate.utils.User;
import com.clommunicate.utils.UserDAO;
import com.clommunicate.utils.WebAPIException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.NetworkErrorException;
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

/**
 * 
 * @author Bostanica Ion
 *
 */
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
		((TextView) findViewById(R.id.reg_name_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_email_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_location_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_gender_label)).setTypeface(type);
		((TextView) findViewById(R.id.reg_photo_label)).setTypeface(type);

		// type = Typeface.createFromAsset(getAssets(), "fonts/asen.ttf");
		((EditText) findViewById(R.id.reg_nume)).setTypeface(type);
		((EditText) findViewById(R.id.reg_email)).setTypeface(type);
		((EditText) findViewById(R.id.reg_location)).setTypeface(type);
		((EditText) findViewById(R.id.reg_gender)).setTypeface(type);

		ImageButton ib = (ImageButton) findViewById(R.id.registerButton);

		ib.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				wd = new WaitDialog(me);

				AsyncTask<Void, Void, Exception> registerTask = new AsyncTask<Void, Void, Exception>() {

					@Override
					protected void onPreExecute() {

						wd.setTitle(String.format("%-100s",
								"User registration."));
						wd.show();

					}

					@Override
					protected Exception doInBackground(Void... params) {

						try {
							
							if (UserDAO.register(usr))
								return null;
							
						} catch (Exception e) {

							return e;

						}

						return null;
					}

					@Override
					protected void onPostExecute(Exception result) {

						wd.dismiss();
						String text = null;

						if (result == null) {
							text = "Registration success.";
							Intent i = new Intent(getApplicationContext(),
									UserActivity.class);
							User.user = usr;
							startActivity(i);
							finish();
						} else if (result instanceof WebAPIException) {
							text = result.getMessage();
							onBackPressed();
						} else if(result instanceof NetworkErrorException){
							text = "No internet connection.";
							onBackPressed();
						}
						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT).show();
					}

				};

				registerTask.execute();
			}

		});

		loadUserData();
	}

	private void loadUserData() {

		AsyncTask<String, Void, Integer> load_data = new AsyncTask<String, Void, Integer>() {

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				wd = new WaitDialog(me);
				wd.setTitle(String.format("%-100s",
						"Loading oAuth information..."));
				wd.show();
			}

			@Override
			protected Integer doInBackground(String... params) {

				/*
				 * GoogleAccountManager googleAccountManager = new
				 * GoogleAccountManager( me);
				 */
				AccountManager am = AccountManager.get(getBaseContext());
				Account[] accounts = am.getAccountsByType("com.google");

				Account acc = null;

				for (Account i : accounts) {

					if (i.name.equalsIgnoreCase(getIntent().getExtras()
							.getString("email"))) {

						acc = i;
						break;

					}
				}

				// TODO: If connection is closed throw somenthing.

				// TODO: If acces token is not updated need to do somenthing

				// AuthUtils.refreshAuthToken(me, acc);
				String accessToken = null;
				SharedPreferences settings = getSharedPreferences(
						"Clommunicate", 0);
				Editor edit = settings.edit();
				edit.putString("accessToken", null);
				edit.commit();
				edit = null;
				// System.err.println("before");
				
				int error = 0;
				try {
					error = AuthUtils.refreshAuthToken(me, acc);
						
				} catch (NetworkErrorException e1) {
					return -1;
				}
				settings = getSharedPreferences("Clommunicate", 0);
				accessToken = settings.getString("accessToken", null);
				if (accessToken == null && error != 2) {
					onBackPressed();
					return 0;
				} else if (error == 2)
					return 2;
				// System.err.println(accessToken);
				try {
					usr = AuthUtils.getGoogleUser(accessToken, acc.name);
				} catch (NetworkErrorException e) {
					return -1;
				} catch (WebAPIException e) {
					return 0;
				}

				if (usr != null)
					return 1;

				return 0;

			}

			@Override
			protected void onPostExecute(Integer result) {
				wd.dismiss();

				String text;
				if (result == 1) {
					text = "oAuth data loaded.";
					name.setText(usr.getName());
					name.setEnabled(false);
					email.setText(usr.getEmail());
					email.setEnabled(false);
					locale.setText(usr.getLocale());
					locale.setEnabled(false);
					gender.setText(usr.getGender().toString());
					gender.setEnabled(false);
					if(usr.getPicture() != null)
						picture.setImageBitmap(usr.getPicture());
				} else if (result == 0)
					text = "Failed to load oAuth data.";
				else if (result == 2){
					text = "Waiting for confirmation.";
				}	else
					text = "No internet connection.";

				Toast.makeText(me, text, Toast.LENGTH_SHORT).show();
			}
		};

		load_data.execute((String) null);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		
		if(requestCode == 0)	{
			if(resultCode == RESULT_CANCELED)	{
				onBackPressed();
				Toast.makeText(me, "Access denied by user..", Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(me, "Access granted..", Toast.LENGTH_SHORT).show();
				loadUserData();
			}
		}
		
	}


	@Override
	protected void onPause() {
		
		super.onPause();
		
		if(wd != null)
			wd.dismiss();
	}

}
