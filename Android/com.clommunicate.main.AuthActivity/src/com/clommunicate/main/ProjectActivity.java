package com.clommunicate.main;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

/**
 * This Activity will be used to view detailed information about a project. It
 * will also provide GUI controls for performing different project operations
 * Like:
 * <ul>
 * <li>
 * For Project owner:
 * <ul>
 * <li>Removing the project</li>
 * <li>Set the project status to completed</li>
 * <li>Adding and Removing project members</li>
 * <li>Creating Tasks(Click on Add Task List Item)</li>
 * <li>Editing Tasks (Long click on Selected Task)</li>
 * <li>Setting Task status to completed</li>
 * </ul>
 * </li>
 * <li>
 * For project member:
 * <ul>
 * <li>Exiting from the project</li>
 * <li>Adding project members</li>
 * <li>Creating Tasks(Click on Add Task List Item)</li>
 * <li>Editing Tasks (Long click on Selected Task)</li>
 * <li>Setting Task status to completed</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * @author Akira
 * 
 */
public class ProjectActivity extends Activity {

	/**
	 * Context and other data
	 */
	private Activity me = this;
	public static Project project = null;

	/**
	 * GUI Elements
	 */
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

		/*
		 * Content View
		 */
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project);

		/*
		 * Loading custom font from Asserts
		 */
		Typeface font_zekton = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");

		/*
		 * Finding controls in ContentView and loading them into GUI objects
		 */
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
		/* Enable TextView scrolling */
		description.setMovementMethod(new ScrollingMovementMethod());
		quit = (ImageButton) findViewById(R.id.activity_project_quit_project_button);
		remove = (ImageButton) findViewById(R.id.activity_project_remove_project_button);
		finish = (ImageButton) findViewById(R.id.activity_project_finish_project_button);
		member_list = (ListView) findViewById(R.id.activity_project_member_list);
		task_list = (ListView) findViewById(R.id.activity_project_task_list);

		/*
		 * Setting up TabHost
		 */
		TabHost th = (TabHost) findViewById(R.id.tabhost);
		th.setup();

		/* Inflating first Tab in TabView */
		View view = LayoutInflater.from(th.getContext()).inflate(
				R.layout.tab_item, null);
		ImageView iv = (ImageView) view.findViewById(R.id.tab_item_icon);
		iv.setBackgroundResource(R.drawable.tasks_tab_norm);
		TextView tv = (TextView) view.findViewById(R.id.tab_item_title);
		tv.setText("Project Tasks");
		tv.setTypeface(font_zekton);
		th.addTab(th.newTabSpec("1").setIndicator(view)
				.setContent(R.id.activity_project_task_tab));

		/* Inflating second Tab in TabView */
		view = LayoutInflater.from(th.getContext()).inflate(R.layout.tab_item,
				null);
		iv = (ImageView) view.findViewById(R.id.tab_item_icon);
		iv.setBackgroundResource(R.drawable.members_tab_norm);
		tv = (TextView) view.findViewById(R.id.tab_item_title);
		tv.setText("Project Members");
		tv.setTypeface(font_zekton);
		th.addTab(th.newTabSpec("2").setIndicator(view)
				.setContent(R.id.activity_project_member_tab));

		/* Setting current tab to first */
		th.setCurrentTab(0);

		/*
		 * Changing Control Fonts
		 */
		name.setTypeface(font_zekton);
		start_date.setTypeface(font_zekton);
		start_date_label.setTypeface(font_zekton);
		deadline.setTypeface(font_zekton);
		deadline_label.setTypeface(font_zekton);
		end_date.setTypeface(font_zekton);
		end_date_label.setTypeface(font_zekton);
		day_count.setTypeface(font_zekton);
		description_label.setTypeface(font_zekton);
		description.setTypeface(font_zekton);

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish();
	}

	public void loadIntoGUI() {

		/*
		 * Click on project member list item.
		 */
		member_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				/*
				 * Add Member should be the last item in list, and it's view
				 * should be enabled
				 */
				if (arg1.isEnabled())
					if (arg2 == (member_list.getAdapter().getCount() - 1)) {

						/*
						 * Adapter will be transmitted to dialog to add member
						 * to the ListView after it was added to Database
						 */
						MemberListArrayAdapter adapter = (MemberListArrayAdapter) member_list
								.getAdapter();
						/*
						 * AddMember dialog will be displayed to allow adding
						 * members by email
						 */
						AddMemberDialog amd = new AddMemberDialog(me,
								member_list, adapter, AddMemberDialog.REMOTE);
						amd.setTitle("Add new member to project.");
						amd.show();
						/*
						 * Happens after AddMemberDialog dialog is dismissed
						 */
						amd.setOnDismissListener(new OnDismissListener() {

							@Override
							public void onDismiss(DialogInterface dialog) {

								/*
								 * Shows a toast with status message from
								 * AddMemberDialog dialog
								 */
								Toast.makeText(getApplicationContext(),
										((AddMemberDialog) dialog).getResult(),
										Toast.LENGTH_SHORT).show();
								// TODO: Need to use AsyncTask here
								// AddMemberDialog
								// dialog should not perform WEBactions it's
								// just for confirming or canceling something

							}
						});

					}

			}

		});

		/*
		 * Click on project task list item
		 */
		task_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				/*
				 * Add Task should be the last item in list, and it's view
				 * should be enabled
				 */
				if (view.isEnabled())
					if (position == (task_list.getAdapter().getCount() - 1)
							|| task_list.getAdapter().getCount() == 0) {

						/*
						 * Starts NewTaskActivity, project_id is passed as a
						 * parameter
						 */
						Intent i = new Intent(me, NewTaskActivity.class);
						i.putExtra("project_id", project.getId());
						startActivity(i);

					} else /* if not last item in list */{

						/*
						 * Starts TaskActivity, task_id is passed as a parameter
						 */
						Intent i = new Intent(me, TaskActivity.class);
						i.putExtra("task_id",
								project.getTask_list().get(position).getId());
						startActivity(i);

					}

			}

		});

		/*
		 * Long Click on project task list item
		 */
		task_list.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View view,
					int position, long arg3) {

				/*
				 * Add Task should be the last item in list, and it's view
				 * should be enabled
				 */
				if (view.isEnabled() && !project.isCompleted())
					if (position == (task_list.getAdapter().getCount() - 1)
							|| task_list.getAdapter().getCount() == 0) {

						/*
						 * Starts NewTaskActivity, project_id is passed as a
						 * parameter
						 */
						Intent i = new Intent(me, NewTaskActivity.class);
						i.putExtra("project_id", project.getId());
						startActivity(i);

					} else /* if not last item in list */{

						/*
						 * Starts NewTaskActivity in edit mode, task_id,
						 * project_id, activity_type are passed as parameters
						 */
						Intent i = new Intent(me, NewTaskActivity.class);
						i.putExtra("project_id", project.getId());
						i.putExtra("activity_type",
								NewTaskActivity.EDIT_ACTIVITY);
						i.putExtra("task_id",
								project.getTask_list().get(position).getId());
						startActivity(i);

					}

				return false;
			}

		});

		/*
		 * If logged in user is project owner I hide quit button otherwise I
		 * hide remove, and complete buttons
		 */
		if (User.user.getId() == project.getOwner_id()) {
			/* hide quit button */
			quit.setVisibility(ImageButton.GONE);

			/*
			 * When remove project button is clicked
			 */
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					/*
					 * Yes/No dialog is displayed to confirm project removal
					 */
					// TODO: Rewrite YesNoDialog, user id is not used for
					// project removal
					final YesNoDialog ynd = new YesNoDialog(me,
							project.getId(), User.user.getId(), false);
					ynd.setTitle(String.format("%-100s",
							"Confirm project romove..."));
					ynd.setMessage("Do you really want to remove this project?");

					/*
					 * Happens after yes/no dialog is dismissed
					 */
					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							/*
							 * If project was successfully removed then the
							 * Activity is finished
							 */
							if (ynd.getStatus())
								finish();

							/*
							 * Shows a toast with status message from yes/no
							 * dialog
							 */
							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});

			/*
			 * When finish project button is clicked
			 */
			finish.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					/*
					 * Yes/No dialog is displayed to confirm project removal
					 */
					final YesNoDialog ynd = new YesNoDialog(me, project.getId());
					ynd.setTitle(String.format("%-100s",
							"Confirm project completion..."));
					ynd.setMessage("Do you want to mark this project: completed?");

					ynd.setOnDismissListener(new OnDismissListener() {

						/*
						 * Happens after yes/no dialog is dismissed
						 */
						@Override
						public void onDismiss(DialogInterface dialog) {

							/*
							 * If project was successfully completed then the
							 * end date is set and the Activity onResume is
							 * called to reload project info from the server and
							 * reset all listeners, hide unnecessary elements,
							 * disable any operation on project
							 */
							if (ynd.getStatus()) {
								Time t = new Time();
								t.setToNow();
								project.setEnd_date(t.year + "-"
										+ (((t.month + 1) < 10) ? "0" : "")
										+ (t.month + 1) + "-"
										+ (((t.monthDay) < 10) ? "0" : "")
										+ (t.monthDay));
								onResume();
							}

							/*
							 * Shows a toast with status message from yes/no
							 * dialog
							 */
							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});
		} else {

			/* hide remove and finish buttons */
			remove.setVisibility(ImageButton.GONE);
			finish.setVisibility(ImageButton.GONE);

			/*
			 * When the quit button is clicked
			 */
			quit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					/*
					 * Yes/No dialog is displayed to confirm user removal from
					 * project
					 */
					final YesNoDialog ynd = new YesNoDialog(me,
							project.getId(), User.user.getId(), true);
					ynd.setTitle(String.format("%-100s",
							"Confirm project quit..."));
					ynd.setMessage("Do you really want to quit from this project?");

					/*
					 * Happens after yes/no dialog is dismissed
					 */
					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							/*
							 * If user was successfully removed from the project
							 * then the Activity is finished
							 */
							if (ynd.getStatus())
								finish();

							/*
							 * Shows a toast with status message from yes/no
							 * dialog
							 */
							Toast.makeText(me.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});

		}

		/*
		 * Updating UI with loaded data
		 */
		name.setText(project.getName());
		start_date.setText(project.getStart_date());
		deadline.setText(project.getDeadline());

		/*
		 * Counting days between two dates
		 */
		String[] rsd = project.getStart_date().split("-");

		Time tsd = new Time();
		tsd.set(Integer.valueOf(rsd[2]), Integer.valueOf(rsd[1]) - 1,
				Integer.valueOf(rsd[0]));

		Time ted = new Time();

		/*
		 * If project is not completed days are counted between start date and
		 * today otherwise between start date and end date
		 */
		if (!project.isCompleted()) {

			end_date.setText("N/A");
			ted.setToNow();

		} else {

			end_date.setText(project.getEnd_date());
			String[] red = project.getEnd_date().split("-");
			ted.set(Integer.valueOf(red[2]), Integer.valueOf(red[1]) - 1,
					Integer.valueOf(red[0]));
			/*
			 * If project is completed we can't remove it, complete it, or quit
			 * from it.
			 */
			finish.setEnabled(false);
			remove.setEnabled(false);
			quit.setEnabled(false);

		}

		long difference = ted.toMillis(true) - tsd.toMillis(true);
		long days = TimeUnit.MILLISECONDS.toDays(difference);

		day_count.setText(String.valueOf(days));
		description.setText(project.getDescription());

		member_list.setAdapter(new MemberListArrayAdapter(me, project
				.getMember_list(), project.getOwner_id()));
		task_list.setAdapter(new TaskListArrayAdapter(me, project
				.getTask_list()));

	}

	@Override
	protected void onResume() {

		super.onResume();

		/*
		 * Dialog will be displayed while data loads
		 */
		WaitDialog wd = new WaitDialog(me);
		wd.setTitle(String.format("%-100s", "Loading project data..."));
		wd.show();

		AsyncTask<WaitDialog, Void, Object[]> loadMembers = new AsyncTask<WaitDialog, Void, Object[]>() {

			@Override
			protected Object[] doInBackground(WaitDialog... params) {

				Object[] aux = new Object[4];
				WaitDialog wd = params[0];
				ArrayList<User> members = new ArrayList<User>();
				ArrayList<Task> tasks = new ArrayList<Task>();
				aux[2] = 0;

				try {

					// TODO:Need to perform checks here: if the project still
					// exists,
					/*
					 * Geting tasks and members from Server
					 */
					members = WebApi.getProjectMembers(project.getId());
					tasks = WebApi.getTaskList(project.getId());
					/*
					 * If data received then error is 1 witch means everything
					 * is ok
					 */
					if (members.size() != 0)
						aux[2] = 1;
				} catch (NetworkErrorException e) {
					/*
					 * When there is no internet connection, or postRequest
					 * returns null
					 */
					aux[2] = -1;

				}

				aux[0] = wd;
				aux[1] = members;
				aux[3] = tasks;

				return aux;

			}

			@SuppressWarnings("unchecked")
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

				/*
				 * When there is an error Activity is finished, and a message is
				 * displayed
				 */
				if (error == 0 || error == -1) {
					Toast.makeText(me, text, Toast.LENGTH_SHORT).show();
					if (error == 0)
						finish();
				} else {
					project.setTask_list(tasks);
					project.setMember_list(members);
					loadIntoGUI();
				}
			}

		};
		loadMembers.execute(wd);

	}

	/*
	 * public void resizeList(){
	 * 
	 * GUIFixes.setListViewHeightBasedOnChildren( member_list,
	 * getApplicationContext().getResources().getDisplayMetrics().widthPixels);
	 * 
	 * GUIFixes.setListViewHeightBasedOnChildren( task_list,
	 * getApplicationContext().getResources().getDisplayMetrics().widthPixels);
	 * }
	 */

	public ListView getMemberList() {

		return member_list;

	}

}
