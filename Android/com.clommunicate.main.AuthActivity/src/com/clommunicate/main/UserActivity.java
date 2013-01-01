package com.clommunicate.main;

import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class UserActivity extends Activity {

	private ListView userDataList = null;
	private User user = null;
	private TextView name = null;
	private TextView projects_count = null;
	private TextView part_in__count = null;
	private Button new_project = null;
	private ImageView avatar = null;
	private Activity me = this;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		user = User.user;
		name = (TextView) findViewById(R.id.user_name);
		projects_count = (TextView) findViewById(R.id.user_created_projects);
		part_in__count = (TextView) findViewById(R.id.user_projects_part_in);
		new_project = (Button) findViewById(R.id.user_new_project);
		avatar = (ImageView) findViewById(R.id.user_avatar);

		/* Setam fontul etichetei login */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.user_part_in_text)).setTypeface(type);
		((TextView) findViewById(R.id.user_projects_text)).setTypeface(type);
		name.setTypeface(type);
		name.setText(User.user.getName());
		projects_count.setTypeface(type);
		part_in__count.setTypeface(type);
		new_project.setTypeface(type);
		if(user.getPicture() != null)
			avatar.setImageBitmap(user.getPicture());

		userDataList = (ListView) findViewById(R.id.user_data);
		userDataList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				switch (position) {

				case 3: {

					Intent i = new Intent(me, ProjectListActivity.class);
					i.putExtra("activityTitle", "Created Projects");
					startActivity(i);

				}
					break;
				case 4: {

					Intent i = new Intent(me, ProjectListActivity.class);
					i.putExtra("activityTitle", "Memeber Of Projects");
					i.putExtra("partIn", true);
					startActivity(i);

				}
					break;
				default: {
				}
					break;

				}

			}
		});
		new_project.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						NewProjectActivity.class);
				startActivity(i);

			}
		});

	}

	@Override
	protected void onResume() {

		super.onResume();

		AsyncTask<Void, Void, Boolean> loadUser = new AsyncTask<Void, Void, Boolean>() {

			@Override
			protected Boolean doInBackground(Void... params) {

				try {
					WebApi.fillClommunicateUser(User.user);
				} catch (NetworkErrorException e) {
					return false;
				}

				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				if (!result)	{
					onBackPressed();
					Toast.makeText(me,
							"No internet connection, or server is inactive",
							Toast.LENGTH_SHORT).show();
					return;
				}
				projects_count.setText(String.valueOf(user.getProjects()));
				part_in__count.setText(String.valueOf(user.getPartIn()));
				String[] userData = { user.getEmail(),
						user.getGender().toString(), user.getLocale(),
						String.valueOf(user.getProjects()),
						String.valueOf(user.getPartIn()) };
				String[] userDataTitles = { "Email", "Gender", "Locale",
						"Projects Created", "Projects Part In" };
				int[] userDataIcons = { R.drawable.email_icon,
						R.drawable.gender_icon, R.drawable.locale_icon,
						R.drawable.owner_icon, R.drawable.part_in_icon };

				UserDataArrayAdapter userDataAdapter = new UserDataArrayAdapter(
						me, userData, userDataTitles, userDataIcons);
				userDataList.setAdapter(userDataAdapter);
			}

		};

		loadUser.execute();

	}

}
