package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class NewProjectActivity extends Activity {
	private Activity me = this;
	private ImageButton add_member_button = null;
	private ListView member_list = null;
	private MemberListArrayAdapter member_list_adapter = null;
	private ImageButton create_project = null;
	private EditText name = null;
	private EditText description = null;
	private DatePicker deadline = null;

	@Override
	public void onBackPressed() {

		finish();

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		setComponentsFont();

		member_list = (ListView) findViewById(R.id.new_project_member_list);
		add_member_button = (ImageButton) findViewById(R.id.new_project_add_member_button);
		create_project = (ImageButton) findViewById(R.id.new_project_create_project);
		name = (EditText) findViewById(R.id.new_project_name);
		description = (EditText) findViewById(R.id.new_project_description);
		description.setMovementMethod( new ScrollingMovementMethod());
		deadline = (DatePicker) findViewById(R.id.new_project_deadline);
		member_list_adapter = new MemberListArrayAdapter(me);
		member_list.setAdapter(member_list_adapter);
		/*ArrayList<User> usr = new ArrayList<User>();
		
		for(int i = 0; i < 20; i++)
			usr.add(User.user);
		
		member_list.setAdapter(new MemberListArrayAdapter(me, usr, 0));
		resizeList();
		/*
		 * LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams)
		 * member_list .getLayoutParams();
		 */

		add_member_button.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				final AddMemberDialog amd = new AddMemberDialog(me, member_list, member_list_adapter, AddMemberDialog.LOCAL);
				amd.setOnDismissListener(new OnDismissListener() {

					@Override
					public void onDismiss(DialogInterface dialog) {

						Toast.makeText(getApplicationContext(),
								amd.getResult(), Toast.LENGTH_SHORT).show();

					}
				});
			}
		});

		create_project.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(name.getText().toString().trim().length() == 0)	{
					
					Toast.makeText(me.getApplicationContext(), "Specify project name.", Toast.LENGTH_SHORT).show();
					return ;
					
				}else if(description.getText().toString().trim().length() == 0){

					Toast.makeText(me.getApplicationContext(), "Specify project description.", Toast.LENGTH_SHORT).show();
					return ;
				}
					
				Project project = new Project(
						name.getText().toString(),
						description.getText().toString(), 
						User.user.getId(), 
						deadline.getYear() + "-"
							+ (deadline.getMonth() + 1) + "-"
							+ deadline.getDayOfMonth(),
							((MemberListArrayAdapter)member_list.getAdapter()).getMembers());
				
				AsyncTask<Project, Void, Integer> create_project_task = new AsyncTask<Project, Void, Integer>(){
					
					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {

						wd = new WaitDialog(me);
						wd.setTitle(String.format("%-100s","Creating new project..."));
						wd.show();
					}
					
					@Override
					protected Integer doInBackground(Project... params) {

						try {
							if(WebApi.createProject(params[0]))	{
								me.finish();
								return 1;
							}
						} catch (NetworkErrorException e) {
							return -1;
						}
						
						return 0;
					}

					@Override
					protected void onPostExecute(Integer result) {

						wd.dismiss();
						String text = null;
						if(result == 1)	
							text = "Project created";
						else if(result == 0)
							text = "Error creating project.";
						else
							text = "No internet connection.";
							
						Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
						
					}
					
				};
				
				create_project_task.execute(project);

			}
		});
	}

	public void setComponentsFont() {

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
		// ((TextView) findViewById(android.R.id.text1)).setTypeface(type);

	}
	
	public void resizeList(){

		GUIFixes.setListViewHeightBasedOnChildren(
				member_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
	
}
