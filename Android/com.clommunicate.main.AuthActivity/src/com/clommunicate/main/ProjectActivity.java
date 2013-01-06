package com.clommunicate.main;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.Project;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectActivity extends Activity {

	private Activity me = this;
	public static Project project = null;
	TextView name = null;
	TextView start_date = null;
	TextView start_date_label = null;
	TextView deadline = null;
	TextView deadline_label = null;
	TextView end_date = null;
	TextView end_date_label = null;
	TextView day_count = null;
	TextView description_label = null;
	TextView description = null;
	ImageButton quit = null;
	ImageButton remove = null;
	ImageButton finish = null;
	ListView task_list = null;
	ListView member_list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);
		TabHost th = (TabHost) findViewById(R.id.tabhost);
		th.setup();

		Typeface typef = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");

		View view = LayoutInflater.from(th.getContext()).inflate(
				R.layout.tab_item, null);
		ImageView iv = (ImageView) view.findViewById(R.id.tab_item_icon);
		iv.setBackgroundResource(R.drawable.tasks_tab_norm);
		TextView tv = (TextView) view.findViewById(R.id.tab_item_title);
		tv.setText("Project Tasks");
		tv.setTypeface(typef);
		th.addTab(th.newTabSpec("1").setIndicator(view)
				.setContent(R.id.activity_project_task_tab));
		view = LayoutInflater.from(th.getContext()).inflate(R.layout.tab_item,
				null);
		iv = (ImageView) view.findViewById(R.id.tab_item_icon);
		iv.setBackgroundResource(R.drawable.members_tab_norm);
		tv = (TextView) view.findViewById(R.id.tab_item_title);
		tv.setText("Project Members");
		tv.setTypeface(typef);
		th.addTab(th.newTabSpec("2").setIndicator(view)
				.setContent(R.id.activity_project_member_tab));
		th.setCurrentTab(0);

		name = (TextView) findViewById(R.id.activity_project_project_name);
		start_date = (TextView) findViewById(R.id.activity_project_start_date);
		start_date_label = (TextView) findViewById(R.id.activity_project_start_date_label);
		deadline = (TextView) findViewById(R.id.activity_project_deadline);
		deadline_label = (TextView) findViewById(R.id.activity_project_deadline_label);
		end_date = (TextView) findViewById(R.id.activity_project_end_date);
		end_date_label = (TextView) findViewById(R.id.activity_project_end_date_label);
		day_count = (TextView) findViewById(R.id.activity_project_day_count);
		description_label = (TextView) findViewById(R.id.activity_project_project_description_label);
		description = (TextView) findViewById(R.id.activity_project_project_description);
		description.setMovementMethod(new ScrollingMovementMethod());
		quit = (ImageButton) findViewById(R.id.activity_project_quit_project_button);
		remove = (ImageButton) findViewById(R.id.activity_project_remove_project_button);
		finish = (ImageButton) findViewById(R.id.activity_project_finish_project_button);
		member_list = (ListView) findViewById(R.id.activity_project_member_list);
		task_list = (ListView) findViewById(R.id.activity_project_task_list);

		member_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == (member_list.getAdapter().getCount() - 1)
						&& arg1.isEnabled()) {
					AddMemberDialog amd = new AddMemberDialog(me, member_list,
							(MemberListArrayAdapter) member_list.getAdapter(),
							AddMemberDialog.REMOTE);
					amd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							Toast.makeText(getApplicationContext(),
									((AddMemberDialog) dialog).getResult(),
									Toast.LENGTH_SHORT).show();

						}
					});

				}

			}

		});


		task_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if (arg2 == (task_list.getAdapter().getCount() - 1)
						&& arg1.isEnabled()) {
					
					Intent i = new Intent(me, NewTaskActivity.class);
					i.putExtra("project_id", project.getId());
					startActivity(i);

				} else	{
					
					Intent i = new Intent(me, TaskActivity.class);
					i.putExtra("task_id", ((TaskListArrayAdapter)task_list.getAdapter()).getTask(arg2).getId());
					startActivity(i);
					
				}

			}

		});
		if (project.getEnd_date().compareToIgnoreCase("null") != 0) {
			finish.setEnabled(false);
			remove.setEnabled(false);
			quit.setEnabled(false);
		}
		if (User.user.getId() == project.getOwner_id()) {
			quit.setVisibility(ImageButton.GONE);

			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(me,
							project.getId(), User.user.getId(), false);
					ynd.setTitle(String.format("%-100s",
							"Confirm project romove..."));
					ynd.setMessage("Do you really want to remove this project?");

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus())
								finish();

							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});

			finish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(me, project.getId());
					ynd.setTitle(String.format("%-100s",
							"Confirm project completion..."));
					ynd.setMessage("Do you want to mark this project: completed?");

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus()) {

								Time t = new Time();
								t.setToNow();
								project.setEnd_date(t.year + "-"
										+ (((t.month + 1) < 10) ? "0" : "")
										+ (t.month + 1) + "-"
										+ ((t.monthDay < 10) ? "0" : "")
										+ t.monthDay);
								end_date.setText(project.getEnd_date());

								finish.setEnabled(false);
							}

							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});
		} else {
			remove.setVisibility(ImageButton.GONE);
			finish.setVisibility(ImageButton.GONE);

			quit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(me,
							project.getId(), User.user.getId(), true);
					ynd.setTitle(String.format("%-100s",
							"Confirm project quit..."));
					ynd.setMessage("Do you really want to quit from this project?");

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus())
								finish();

							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});

		}
		name.setTypeface(typef);
		start_date.setTypeface(typef);
		start_date_label.setTypeface(typef);
		deadline.setTypeface(typef);
		deadline_label.setTypeface(typef);
		end_date.setTypeface(typef);
		end_date_label.setTypeface(typef);
		day_count.setTypeface(typef);
		description_label.setTypeface(typef);
		description.setTypeface(typef);

		/*ArrayList<Task> aux = new ArrayList<Task>();
		
		for(int i = 0; i < 20; i++)
			aux.add(new Task());
		
		task_list.setAdapter(new TaskListArrayAdapter(me, aux));
		*/
		loadProjectData();
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}
	

	private void loadProjectData() {
		name.setText(project.getName());
		start_date.setText(project.getStart_date());
		deadline.setText(project.getDeadline());
		// day_count.setText(projects.get(position).getName());

		String[] rsd = project.getStart_date().split("-");

		Time tsd = new Time();
		tsd.set(Integer.valueOf(rsd[2]), Integer.valueOf(rsd[1]) - 1,
				Integer.valueOf(rsd[0]));
		// System.err.println(tsd.toString());
		Time ted = new Time();
		if (project.getEnd_date().compareToIgnoreCase("null") == 0) {
			end_date.setText("N/A");
			ted.setToNow();
		} else {
			end_date.setText(project.getEnd_date());
			String[] red = project.getEnd_date().split("-");
			ted.set(Integer.valueOf(red[2]), Integer.valueOf(red[1]) - 1,
					Integer.valueOf(red[0]));
		}
		// System.err.println(ted.toString());

		long difference = ted.toMillis(true) - tsd.toMillis(true);

		long days = TimeUnit.MILLISECONDS.toDays(difference);

		day_count.setText(String.valueOf(days));

		description.setText(project.getDescription());
	}

	@Override
	protected void onResume() {

		super.onResume();
		WaitDialog wd = new WaitDialog(me);
		wd.setTitle(String.format("%-100s", "Loading project members..."));
		wd.show();
		AsyncTask<WaitDialog, Void, Object[]> loadMembers = new AsyncTask<WaitDialog, Void, Object[]>() {

			@Override
			protected Object[] doInBackground(WaitDialog... params) {

				Object[] aux = new Object[4];
				WaitDialog wd = params[0];
				ArrayList<User> members = new ArrayList<User>();
				ArrayList<Task> tasks = new ArrayList<Task>();

				try {
					members = WebApi.getProjectMembers(project.getId());
					tasks = WebApi.getTaskList(project.getId());
					
					if (members.size() != 0)
						aux[2] = 1;
				} catch (NetworkErrorException e) {

					aux[2] = -1;

				}

				aux[0] = wd;
				aux[1] = members;
				aux[3] = tasks;

				return aux;

			}

			@Override
			protected void onPostExecute(Object[] result) {
				WaitDialog wd = (WaitDialog) result[0];
				ArrayList<User> members = (ArrayList<User>) result[1];
				ArrayList<Task> tasks = (ArrayList<Task>) result[3];
				Integer error = (Integer) result[2];
				wd.dismiss();

				String text = null;

				text = (error == 0) ? "Error can't load project info."
						: "No internet connection.";

				if (error == 0 || error == -1)
					Toast.makeText(me, text, Toast.LENGTH_SHORT).show();
				else		{
					member_list.setAdapter(new MemberListArrayAdapter(me,
							members, project.getOwner_id()));
					task_list.setAdapter(new TaskListArrayAdapter(me, tasks));
				}
			}

		};
		loadMembers.execute(wd);

	}

	
/*	public void resizeList(){

		GUIFixes.setListViewHeightBasedOnChildren(
				member_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);

		GUIFixes.setListViewHeightBasedOnChildren(
				task_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
	}*/


	public ListView getMemberList() {

		return member_list;

	}

}
