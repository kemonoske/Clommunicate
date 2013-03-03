package com.clommunicate.main;

import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.Project;
import com.clommunicate.utils.ProjectDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity for creating new projects and editing existing ones
 * 
 * @author Bostanica Ion
 * 
 */
public class NewProjectActivity extends Activity {

	/*
	 * Constants
	 */
	public static final int NEW_PROJECT = 1;
	public static final int EDIT_PROJECT = 2;

	/*
	 * Context and other data
	 */
	private Activity me = this;
	private int activity_type = NEW_PROJECT;
	private static Project project = null;

	/*
	 * GUI Elements
	 */
	private ImageButton add_member_button = null;
	private ListView member_list = null;
	private MemberListArrayAdapter member_list_adapter = null;
	private ImageButton create_project = null;
	private EditText name = null;
	private EditText description = null;
	private DatePicker deadline = null;
	private WaitDialog wd = null;
	private MainMenu main_menu = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		setComponentsFont();

		/*
		 * Loading controls from the content view
		 */
		member_list = (ListView) findViewById(R.id.new_project_member_list);
		add_member_button = (ImageButton) findViewById(R.id.new_project_add_member_button);
		create_project = (ImageButton) findViewById(R.id.new_project_create_project);
		name = (EditText) findViewById(R.id.new_project_name);
		description = (EditText) findViewById(R.id.new_project_description);
		description.setMovementMethod(new ScrollingMovementMethod());
		deadline = (DatePicker) findViewById(R.id.new_project_deadline);

		activity_type = getIntent().getIntExtra("activity_type", NEW_PROJECT);
		/*
		 * We don't need to add or remove users in edit mode because it can be
		 * done from the project activity
		 */
		if (activity_type == EDIT_PROJECT) {

			((TextView) findViewById(R.id.new_project_member_list_label))
					.setVisibility(View.GONE);
			add_member_button.setVisibility(View.GONE);
			((TextView) findViewById(R.id.new_project_activity_title))
					.setText("Edit Project");
			try {

				name.setText(project.getName());
				description.setText(project.getDescription());
				String[] dl = project.getDeadline().split("-");
				int mYear = Integer.valueOf(dl[0]);
				int mMonth = Integer.valueOf(dl[1]) - 1;
				int mDay = Integer.valueOf(dl[2]);
				deadline.init(mYear, mMonth, mDay, null);
				
			} catch (NullPointerException e) {

				Intent i = new Intent(me, AuthActivity.class);
				startActivity(i);
				Toast.makeText(me, getResources().getString(R.string.error_please_relogin), Toast.LENGTH_SHORT)
				.show();
				finish();
				
			}
		}

		/*
		 * Setting up list view
		 */
		member_list_adapter = new MemberListArrayAdapter(me);
		member_list.setAdapter(member_list_adapter);

		/*
		 * Add member button click
		 */
		add_member_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				/*
				 * Add member dialog is displayed
				 */
				final AddMemberDialog amd = new AddMemberDialog(me,
						member_list, member_list_adapter, AddMemberDialog.LOCAL);
				amd.setTitle(getResources().getString(R.string.new_project_activity_add_member_dialog_title));
				amd.show();
				amd.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {

						/*
						 * When dialog closes a message with status is displayed
						 */
						Toast.makeText(getApplicationContext(),
								amd.getResult(), Toast.LENGTH_SHORT).show();

					}
				});
			}
		});

		/*
		 * Create project button Click
		 */
		create_project.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				/*
				 * Check if required fields are filled
				 */
				if (name.getText().toString().trim().length() == 0) {

					Toast.makeText(me.getApplicationContext(),
							getResources().getString(R.string.new_project_activity_project_name_not_specified), Toast.LENGTH_SHORT).show();
					return;

				} else if (description.getText().toString().trim().length() == 0) {

					Toast.makeText(me.getApplicationContext(),
							getResources().getString(R.string.new_project_activity_project_description_not_specified), Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Project project = null;

				/*
				 * If activity creates new project it is initialized using
				 * completed fields otherwise current object is edited
				 */
				if (activity_type == NEW_PROJECT)
					project = new Project(name.getText().toString(),
							description.getText().toString(),
							User.user.getId(), deadline.getYear() + "-"
									+ (deadline.getMonth() + 1) + "-"
									+ deadline.getDayOfMonth(),
							((MemberListArrayAdapter) member_list.getAdapter())
									.getMembers());
				else {
					NewProjectActivity.getProject().setName(
							name.getText().toString());
					NewProjectActivity.getProject().setDescription(
							description.getText().toString());
					NewProjectActivity.getProject().setDeadline(
							deadline.getYear() + "-"
									+ (deadline.getMonth() + 1) + "-"
									+ deadline.getDayOfMonth());

				}

				AsyncTask<Project, Void, Exception> create_project_task = new AsyncTask<Project, Void, Exception>() {

					@Override
					protected void onPreExecute() {

						/*
						 * Wait dialog will be displayed while project is
						 * created or edited
						 */
						wd = new WaitDialog(me);
						if (activity_type == NEW_PROJECT)
							wd.setTitle(getResources().getString(R.string.new_project_activity_wait_dialog_title_create));
						else
							wd.setTitle(getResources().getString(R.string.new_project_activity_wait_dialog_title_update));
						wd.show();
					}

					@Override
					protected Exception doInBackground(Project... params) {

						try {

							// TODO:Check here if project still exists
							/*
							 * Calling update or create project based on
							 * activity type
							 */
							if (activity_type == NEW_PROJECT)
								ProjectDAO.addProject(params[0]);
							else
								ProjectDAO.updateProject(NewProjectActivity
										.getProject());
							me.finish();

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
						String text = null;
						if (result == null)
							text = (activity_type == NEW_PROJECT) ? getResources().getString(R.string.new_project_activity_create_result_text_success)
									: getResources().getString(R.string.new_project_activity_update_text_result_success);
						else if (result instanceof WebAPIException)
							text = result.getMessage();
						else
							text = getResources().getString(R.string.error_no_internet_connection);

						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG).show();

					}

				};

				create_project_task.execute(project);

			}
		});
	}

	public void setComponentsFont() {

		/*
		 * Load font and set it to the controls
		 */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.new_project_activity_title))
				.setTypeface(type);
		((TextView) findViewById(R.id.new_project_name_label))
				.setTypeface(type);
		((TextView) findViewById(R.id.new_project_description_label))
				.setTypeface(type);
		((TextView) findViewById(R.id.new_project_logo_label))
				.setTypeface(type);
		((TextView) findViewById(R.id.new_project_member_list_label))
				.setTypeface(type);
		((EditText) findViewById(R.id.new_project_name)).setTypeface(type);

	}

	@Override
	public void onBackPressed() {

		finish();

	}

	public void resizeList() {
		/*
		 * if list is contained by scroll view this will resize list so it can
		 * wrap its content
		 */
		GUIFixes.setListViewHeightBasedOnChildren(
				member_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
	}

	public static Project getProject() {
		return project;
	}

	public static void setProject(Project project) {
		NewProjectActivity.project = project;
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		main_menu =  new MainMenu(this, R.id.new_project_activity_main);
		
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
