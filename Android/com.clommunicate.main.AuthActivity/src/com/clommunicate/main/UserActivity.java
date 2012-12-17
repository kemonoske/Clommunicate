package com.clommunicate.main;

import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class UserActivity extends Activity {

	private ListView userDataList = null;
	private User user = null;
	private TextView name = null;
	private TextView projects_count = null;
	private TextView part_in__count = null;
	private Button new_project = null;
	private ImageView avatar = null;

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
		projects_count.setText(String.valueOf(user.getProjects()));
		part_in__count.setTypeface(type);
		part_in__count.setText(String.valueOf(user.getPartIn()));
		new_project.setTypeface(type);
		avatar.setImageBitmap(user.getPicture());

		userDataList = (ListView) findViewById(R.id.user_data);
		String[] userData = { user.getEmail(), user.getGender().toString(),
				user.getLocale(), String.valueOf(user.getProjects()),
				String.valueOf(user.getPartIn()) };
		String[] userDataTitles = { "Email", "Gender", "Locale",
				"Projects Created", "Projects Part In" };
		int[] userDataIcons = { R.drawable.email_icon, R.drawable.gender_icon,
				R.drawable.locale_icon, R.drawable.owner_icon,
				R.drawable.part_in_icon };

		UserDataArrayAdapter userDataAdapter = new UserDataArrayAdapter(this,
				userData, userDataTitles, userDataIcons);
		userDataList.setAdapter(userDataAdapter);
		new_project.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						NewProjectActivity.class);
				startActivity(i);

			}
		});

	}

}
