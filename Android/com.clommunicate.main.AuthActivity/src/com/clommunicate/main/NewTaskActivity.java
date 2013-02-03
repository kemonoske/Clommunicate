package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.ProjectDAO;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.TaskDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Akira
 * 
 */
public class NewTaskActivity extends Activity {

	/**
	 * Constants
	 */
	public final static int NEW_ACTIVITY = 1;
	public final static int EDIT_ACTIVITY = 2;
	public final static int DELETE_ACTIVITY = 3;

	/**
	 * General Usable data
	 */
	private Activity me = this;
	private int project_id = 0;
	private int task_id = 0;
	private ArrayList<Integer> member_ids = null;
	private int activity_type = NEW_ACTIVITY;

	/**
	 * GUI Elements
	 */
	private TextView activity_title = null;
	private TextView name_label = null;
	private TextView description_label = null;
	private TextView type_label = null;
	private TextView asign_label = null;
	private EditText name = null;
	private EditText description = null;
	private Spinner type = null;
	private Spinner asign = null;
	private ImageButton create_task = null;
	private CheckBox delete = null;
	private LinearLayout delete_layout = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_task);

		Intent i = getIntent();
		project_id = i.getIntExtra("project_id", 0);
		activity_type = i.getIntExtra("activity_type", NEW_ACTIVITY);
		task_id = i.getIntExtra("task_id", 0);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		activity_title = (TextView) findViewById(R.id.new_task_activity_title);
		name_label = (TextView) findViewById(R.id.new_task_name_label);
		description_label = (TextView) findViewById(R.id.new_task_description_label);
		type_label = (TextView) findViewById(R.id.new_task_type_label);
		asign_label = (TextView) findViewById(R.id.new_task_asigned_label);
		name = (EditText) findViewById(R.id.new_task_name);
		description = (EditText) findViewById(R.id.new_task_description);
		description.setMovementMethod(new ScrollingMovementMethod());
		type = (Spinner) findViewById(R.id.new_task_type);
		asign = (Spinner) findViewById(R.id.new_task_asigned);
		create_task = (ImageButton) findViewById(R.id.new_task_create_task);
		delete = (CheckBox) findViewById(R.id.new_task_delete);
		delete_layout = (LinearLayout) findViewById(R.id.new_task_delete_layout);

		if (activity_type == EDIT_ACTIVITY) {
			activity_title.setText("Edit Task");
			delete_layout.setVisibility(View.VISIBLE);
		}

		activity_title.setTypeface(typeface);
		name_label.setTypeface(typeface);
		name.setTypeface(typeface);
		description_label.setTypeface(typeface);
		description.setTypeface(typeface);
		type_label.setTypeface(typeface);
		asign_label.setTypeface(typeface);

		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.task_type_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		type.setAdapter(adapter);

		create_task.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (delete.isChecked())
					activity_type = DELETE_ACTIVITY;

				if (name.getText().toString().trim().length() == 0) {

					Toast.makeText(me.getApplicationContext(),
							"Specify project name.", Toast.LENGTH_SHORT).show();
					return;

				} else if (description.getText().toString().trim().length() == 0) {

					Toast.makeText(me.getApplicationContext(),
							"Specify project description.", Toast.LENGTH_SHORT)
							.show();
					return;
				}

				Task task = new Task(name.getText().toString(), description
						.getText().toString(),
						type.getSelectedItemPosition() + 1, member_ids
								.get(asign.getSelectedItemPosition()),
						project_id);
				if (activity_type == EDIT_ACTIVITY)
					task.setId(task_id);

				AsyncTask<Task, Void, Exception> create_project_task = new AsyncTask<Task, Void, Exception>() {

					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {

						wd = new WaitDialog(me);

						switch (activity_type) {
						case NEW_ACTIVITY:

							wd.setTitle(String.format("%-100s",
									"Creating new task..."));

							break;

						case EDIT_ACTIVITY:

							wd.setTitle(String.format("%-100s",
									"Updating task info..."));

							break;

						case DELETE_ACTIVITY:

							wd.setTitle(String.format("%-100s",
									"Deleting task..."));

							break;
						default:
							break;
						}

						wd.show();
					}

					@Override
					protected Exception doInBackground(Task... params) {

						try {

							switch (activity_type) {
								case NEW_ACTIVITY:
	
									TaskDAO.addTask(params[0]);
	
									break;
	
								case EDIT_ACTIVITY:
	
									TaskDAO.updateTask(params[0]);
	
									break;
	
								case DELETE_ACTIVITY:
	
									TaskDAO.removeTask(task_id);
	
									break;
	
								default:
									break;
							}

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
						switch (activity_type) {
						case NEW_ACTIVITY:

							if (result == null) {
								text = "Task created.";
								finish();
							} else if (result instanceof WebAPIException) {
								text = result.getMessage();
								finish();
							} else
								text = "No internet connection.";

							break;

						case EDIT_ACTIVITY:

							if (result == null) {
								text = "Task updated.";
								finish();
							} else if (result instanceof WebAPIException) {
								text = result.getMessage();
								finish();
							} else
								text = "No internet connection.";

							break;

						case DELETE_ACTIVITY:

							if (result == null) {
								text = "Task Deleted.";
								finish();
							} else if (result instanceof WebAPIException) {
								text = result.getLocalizedMessage();
								finish();
							} else
								text = "No internet connection.";

							break;

						default:
							break;
						}
						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT).show();

					}

				};

				create_project_task.execute(task);

			}
		});
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
				Task task = null;

				aux[2] = null;

				try {

					if (activity_type == EDIT_ACTIVITY) {

						members = ProjectDAO.getProjectMembers(project_id);
						task = TaskDAO.getTask(task_id);

					} else {
						members = ProjectDAO.getProjectMembers(project_id);

					}
				} catch (NetworkErrorException e) {

					aux[2] = e;

				} catch (WebAPIException e) {

					aux[2] = e;

				}

				aux[0] = wd;
				aux[1] = members;
				aux[3] = task;

				return aux;

			}

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object[] result) {
				WaitDialog wd = (WaitDialog) result[0];
				ArrayList<User> members = (ArrayList<User>) result[1];
				Task task = (Task) result[3];
				Exception error = (Exception) result[2];
				wd.dismiss();

				if (error instanceof NetworkErrorException) {

					Toast.makeText(me, "No internet connection.", Toast.LENGTH_SHORT).show();
					
				} else if (error instanceof WebAPIException) {

					Toast.makeText(me, error.getMessage(), Toast.LENGTH_SHORT).show();
					
				} else {

					ArrayList<CharSequence> aux = new ArrayList<CharSequence>();
					member_ids = new ArrayList<Integer>();

					int pos = 0;
					for (User i : members) {
						aux.add(i.getName() + "\t(" + i.getEmail() + ")");
						member_ids.add(i.getId());
						if (activity_type == EDIT_ACTIVITY)
							if (i.getId() == task.getAsigned_id())
								pos = member_ids.size() - 1;
					}

					ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
							me, android.R.layout.simple_spinner_item, aux);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					asign.setAdapter(adapter);
					if (activity_type == EDIT_ACTIVITY) {
						name.setText(task.getName());
						description.setText(task.getDescription());
						type.setSelection(task.getType() - 1, true);
						asign.setSelection(pos, true);
					}
				}
			}

		};
		loadMembers.execute(wd);

	}

}
