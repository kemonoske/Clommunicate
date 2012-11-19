package com.clommunicate.main;

import com.clommunicate.main.R;
import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.Login;
import com.clommunicate.utils.WebApi;

import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AuthActivity extends Activity {

	private ImageButton expand_button = null;
	private ImageButton exit_button = null;
	private ImageButton add_account_button = null;
	private ListView accounts_list = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_auth);

		expand_button = (ImageButton) findViewById(R.id.loginButton);
		exit_button = (ImageButton) findViewById(R.id.exitButton);
		add_account_button = (ImageButton) findViewById(R.id.auth_add_account_button);
		accounts_list = (ListView) findViewById(R.id.authAcountList);

		/* Setam fontul etichetei login */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.authLabel)).setTypeface(type);
		type = null;

		/* Atribuim actiune la click pentru butonul ok */
		expand_button.setOnClickListener(new OnClickListener() {

			boolean ex = false;

			public void onClick(View v) {
				LinearLayout ll = (LinearLayout) findViewById(R.id.midFrame);

				if (!ex) {
					ll.setVisibility(LinearLayout.VISIBLE);

					String[] tokens = { "test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com",
							"test@gmail.com", "test@gmail.com"  };// Login.getDeviceAccounts(getBaseContext());
					String[] values = new String[tokens.length];
					for (int i = 0; i < tokens.length; i++)
						values[i] = (tokens[i].split("@"))[0];
					ArrayAdapter<String> adapter = new AcountArrayAdapter(
							getBaseContext(), values, tokens);
					accounts_list.setAdapter(adapter);
					GUIFixes.setListViewHeightBasedOnChildren(accounts_list);
					accounts_list.setVisibility(ListView.VISIBLE);

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

		/* Actiunea pentru butonul cancel */
		exit_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				moveTaskToBack(true);
			}
		});

		/* Actiunea pentru butonul de adaugare a acountului */
		add_account_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				findViewById(R.id.loginButton).performClick();
				startActivity(new Intent(Settings.ACTION_ADD_ACCOUNT));

			}
		});

		/* Actiunea la tastarea unui account din lista */

		accounts_list.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				final String s = (String) ((TextView) view
						.findViewById(R.id.auth_acount_item2)).getText();
				final View v = view;
				Runnable r = new Runnable() {

					public void run() {

						if (WebApi.login(s)) {

						} else {

							/*
							 * Daca emailul nu este in baza de date atunci facem
							 * redirect la activitatea de inregistrare
							 */
							Intent i = new Intent(getApplicationContext(),
									RegistrationActivity.class);
							i.putExtra("email", s);
							startActivity(i);

						}
					}
				};

				WaitDialog wd = new WaitDialog(view.getRootView().getContext());
				wd.setTitle(String.format("%100s", "User Authentification..."));
				wd.show();
				Thread t = new Thread(r);
				t.start();
				while (t.isAlive())
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				wd.dismiss();

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_auth, menu);
		return true;
	}

}
