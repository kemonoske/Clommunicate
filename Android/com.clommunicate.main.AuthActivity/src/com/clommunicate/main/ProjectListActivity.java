package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * List of projects is displayed on this activity and user can search through
 * list, can access project by clicking on it or edit project data by long
 * pressing
 * 
 * @author Akira
 * 
 */
public class ProjectListActivity extends Activity {

	/*
	 * Constants
	 */
	public static final int CREATED_PROJECTS = 1;
	public static final int PROJECTS_PART_IN = 2;

	/*
	 * Context and other data
	 */
	private boolean search_expand = false;
	private boolean partIn = false;
	private Activity me = this;
	private int activity_type = CREATED_PROJECTS;

	/*
	 * GUI elements
	 */
	private TextView title = null;
	private ListView project_list = null;
	private ImageButton search_button = null;
	private EditText search_field = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);

		/*
		 * Loading controls from content view
		 */
		title = (TextView) findViewById(R.id.project_list_activity_title);
		project_list = (ListView) findViewById(R.id.project_list_project_list);
		search_button = (ImageButton) findViewById(R.id.project_list_search_button);
		search_field = (EditText) findViewById(R.id.project_list_search_field);

		/*
		 * Loading font from asserts
		 */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");

		title.setTypeface(type);

		/*
		 * On search button click
		 */
		search_button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * If search bar is not expanded, it is made visible and
				 * expanded is set to true otherwise its hidden, search bar is
				 * cleared and the expanded is set to false
				 */
				if (!search_expand) {

					search_field.setVisibility(EditText.VISIBLE);
					search_expand = true;

				} else {

					search_field.setVisibility(EditText.GONE);
					search_field.setText("");
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search_field.getWindowToken(),
							0);
					search_expand = false;

				}

			}
		});

		/*
		 * Getting activity title from intent with wich the activity was
		 * started, also get the type of activity
		 */
		Intent i = getIntent();
		this.partIn = i.getBooleanExtra("partIn", false);
		activity_type = (partIn) ? PROJECTS_PART_IN : CREATED_PROJECTS;
		title.setText(i.getStringExtra("activityTitle"));

		/*
		 * When a project from list is clicked
		 */
		project_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				/*
				 * Collapse search bar before going to other activity
				 */
				if (search_expand)
					search_button.performClick();
				/*
				 * Set the project for project activity, fuck encapsulation
				 */
				ProjectActivity.project = ((ProjectListArrayAdapter) project_list
						.getAdapter()).getProject(arg2);
				Intent i = new Intent(me, ProjectActivity.class);
				startActivity(i);

			}
		});

		/*
		 * When a project from list is long clicked
		 */
		if (activity_type == CREATED_PROJECTS)
			project_list
					.setOnItemLongClickListener(new OnItemLongClickListener() {

						@Override
						public boolean onItemLongClick(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {

							/*
							 * Collapse search bar before going to other
							 * activity
							 */
							if (search_expand)
								search_button.performClick();
							/*
							 * Set the project for new project activity, fuck
							 * encapsulation
							 */
							NewProjectActivity
									.setProject(((ProjectListArrayAdapter) project_list
											.getAdapter()).getProject(arg2));
							Intent i = new Intent(me, NewProjectActivity.class);
							i.putExtra("activity_type", NewProjectActivity.EDIT_PROJECT);
							startActivity(i);

							return false;
						}

					});

		/*
		 * When something is typed or deleted from search bar
		 */
		search_field.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

				/*
				 * Filter list view using text from search bar
				 */
				((ProjectListArrayAdapter) project_list.getAdapter())
						.getFilter().filter(s);

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				/*
				 * Unused
				 */
			}

			@Override
			public void afterTextChanged(Editable s) {
				/*
				 * Unused
				 */
			}
		});

	}

	@Override
	protected void onResume() {

		super.onResume();

		/*
		 * Wait Dialog will be displayed while project list loads
		 */
		final WaitDialog wd = new WaitDialog(me);

		AsyncTask<Void, Void, Object[]> loadList = new AsyncTask<Void, Void, Object[]>() {

			@Override
			protected void onPreExecute() {
				wd.setTitle("Project list loading...");
				wd.show();
			}

			@SuppressLint("UseValueOf")
			@Override
			protected Object[] doInBackground(Void... params) {

				Object[] res = new Object[2];
				res[0] = null;
				res[1] = new Integer(0);

				try {
					res[0] = WebApi.getProjectList(User.user.getId(), partIn);
				} catch (NetworkErrorException e) {
					res[1] = -1;
				}

				if (res[0] != null)
					res[1] = 1;

				return res;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object[] res) {

				Integer result = (Integer) res[1];

				wd.dismiss();
				/*
				 * If everything is ok the the adapter is set using data from
				 * server otherwise an empty list is used to set adapter
				 */
				if (result == 1)
					project_list.setAdapter(new ProjectListArrayAdapter(me,
							((ArrayList<Project>) res[0]), partIn));
				else if (result == 0) {
					project_list.setAdapter(new ProjectListArrayAdapter(me,
							(new ArrayList<Project>(0)), partIn));
					Toast.makeText(getApplicationContext(),
							"Project list is empty...", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(getApplicationContext(),
							"No internet connection.", Toast.LENGTH_SHORT)
							.show();
				}
			}

		};

		loadList.execute();

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

}