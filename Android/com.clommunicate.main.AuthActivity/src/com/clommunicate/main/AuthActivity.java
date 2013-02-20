package com.clommunicate.main;

import com.clommunicate.main.R;
import com.clommunicate.utils.Login;
import com.clommunicate.utils.User;
import com.clommunicate.utils.UserDAO;
import com.clommunicate.utils.WebAPIException;

import android.R.bool;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity reads google accounts registered on device and displays them in a
 * list for log in
 * 
 * @author Bostanica Ion
 * 
 */
public class AuthActivity extends Activity {

	/**
	 * Context and other data
	 */
	private Activity me = this;

	/**
	 * GUI Elements
	 */
	private ImageButton expand_button = null;
	private ImageButton exit_button = null;
	private ImageButton add_account_button = null;
	private ListView accounts_list = null;
	private TextView auth_label = null;
	private Typeface font_zekton = null;
	private WaitDialog wd = null;
	private MainMenu main_menu = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);
		/*
		 * Loading controls from content view
		 */
		expand_button = (ImageButton) findViewById(R.id.loginButton);
		exit_button = (ImageButton) findViewById(R.id.exitButton);
		add_account_button = (ImageButton) findViewById(R.id.auth_add_account_button);
		accounts_list = (ListView) findViewById(R.id.authAcountList);
		auth_label = (TextView) findViewById(R.id.authLabel);

		/*
		 * Load font from asserts
		 */
		font_zekton = Typeface.createFromAsset(getAssets(), "fonts/zekton.ttf");

		auth_label.setTypeface(font_zekton);

		/*
		 * Expand/Collapse button on click
		 */
		expand_button.setOnClickListener(new OnClickListener() {

			boolean ex = false;

			public void onClick(View v) {
				LinearLayout ll = (LinearLayout) findViewById(R.id.midFrame);

				/*
				 * If the login form is nor expanded then layout visibility is
				 * set to VISIBLE otherwise to GONE
				 */
				if (!ex) {
					ll.setVisibility(LinearLayout.VISIBLE);

					/*
					 * Get user accounts from the device
					 */
					String[] tokens = Login.getDeviceAccounts(getBaseContext());
					String[] values = new String[tokens.length];
					for (int i = 0; i < tokens.length; i++)
						values[i] = (tokens[i].split("@"))[0];
					/*
					 * Create an array adapter containing user nickname and
					 * email and set the adapter to the ListView
					 */
					ArrayAdapter<String> adapter = new AcountArrayAdapter(
							getBaseContext(), values, tokens);
					accounts_list.setAdapter(adapter);
					accounts_list.setVisibility(ListView.VISIBLE);

					/*
					 * Set translate animation to bottom layout and expand
					 * animation to middle layout
					 */
					((LinearLayout) findViewById(R.id.botChoice))
							.startAnimation(AnimationUtils.loadAnimation(
									ll.getContext(), R.anim.auth_bot_frame));

					((LinearLayout) findViewById(R.id.midFrame))
							.startAnimation(AnimationUtils.loadAnimation(
									ll.getContext(), R.anim.auth_mid_frame));

					ex = true;
				} else {
					ll.setVisibility(LinearLayout.GONE);
					((ListView) findViewById(R.id.authAcountList))
							.setVisibility(ListView.GONE);
					ex = false;
				}
			}
		});

		/*
		 * If the exit button is pressed the task is minimized and goes to stack
		 */
		exit_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});

		/*
		 * On click Add account button
		 */
		add_account_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				/*
				 * Login form is collapsed then Android account add service is
				 * starting using an intent
				 */
				findViewById(R.id.loginButton).performClick();
				startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));

			}
		});

		/*
		 * Click on account list item
		 */
		accounts_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				/*
				 * Get email from list item
				 */
				final String s = (String) ((TextView) view
						.findViewById(R.id.auth_acount_item2)).getText();

				/*
				 * Wait dialog is displayed while login is performed
				 */
				wd = new WaitDialog(me);
				AsyncTask<String, Void, Exception> auth = new AsyncTask<String, Void, Exception>() {

					@Override
					protected void onPreExecute() {
						wd.setTitle("User Authentification...");
						wd.show();
					}

					@Override
					protected Exception doInBackground(String... params) {

						try {

							/*
							 * If user exists in database we get user object
							 * containing all user data
							 */
							User.user = UserDAO.login(params[0]);

						} catch (NetworkErrorException e) {

							return e;

						} catch (WebAPIException e) {

							return e;

						}

						return null;
					}

					@Override
					protected void onPostExecute(Exception result) {

						wd.dismiss();
						if (result == null) {
							/*
							 * I the user data received start activity showing
							 * user information
							 */
							Intent i = new Intent(getApplicationContext(),
									UserActivity.class);
							startActivity(i);

						} else if (result instanceof WebAPIException) {

							/*
							 * If there is no such user in database then a
							 * registration activity is displayed so the user
							 * can register his email in the system, email is
							 * passed to the register activity as a parameter
							 */
							Intent i = new Intent(getApplicationContext(),
									RegistrationActivity.class);
							i.putExtra("email", s);
							startActivity(i);

						} else if (result instanceof NetworkErrorException) {/*
																			 * If
																			 * there
																			 * is
																			 * no
																			 * internet
																			 * or
																			 * post
																			 * request
																			 * returns
																			 * null
																			 */
							Toast.makeText(me.getApplicationContext(),
									"No internet connection.",
									Toast.LENGTH_SHORT).show();
						} else
							Toast.makeText(me.getApplicationContext(),
									"Unknown exception.", Toast.LENGTH_SHORT)
									.show();
					}

				};

				auth.execute(s);
			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		main_menu = new MainMenu(this, R.id.activity_auth_main);

		return true;
	}
	
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (main_menu != null)
				main_menu.toggle();
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onPause() {

		super.onPause();

		if (wd != null)
			wd.dismiss();
	}

}
