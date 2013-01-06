package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Spannable;
import android.text.method.MovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class NewTaskActivity extends Activity {

	/**
	 * Constants
	 */
	public final static int NEW_ACTIVITY = 1;
	public final static int EDIT_ACTIVITY = 2;

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
	private TextView name_label = null;
	private TextView description_label = null;
	private TextView type_label = null;
	private TextView asign_label = null;
	private EditText name = null;
	private EditText description = null;
	private Spinner type = null;
	private Spinner asign = null;
	private ImageButton create_task = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_new_task);

		Intent i = getIntent();
		project_id = i.getIntExtra("project_id", 0);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
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
						.getText().toString(), type.getSelectedItemPosition() + 1,
						member_ids.get(asign.getSelectedItemPosition()),
						project_id);

				AsyncTask<Task, Void, Integer> create_project_task = new AsyncTask<Task, Void, Integer>() {

					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {

						wd = new WaitDialog(me);
						wd.setTitle(String.format("%-100s",
								"Creating new task..."));
						wd.show();
					}

					@Override
					protected Integer doInBackground(Task... params) {

						try {
							if (WebApi.createTask(params[0])) {
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
						if (result == 1) {
							text = "Task created";
							finish();
						} else if (result == 0) {
							text = "Error creating task.";
							finish();
						} else
							text = "No internet connection.";

						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_LONG).show();

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
				ArrayList<Task> tasks = new ArrayList<Task>();

				try {
					members = WebApi.getProjectMembers(project_id);

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
				else {

					ArrayList<CharSequence> aux = new ArrayList<CharSequence>();
					member_ids = new ArrayList<Integer>();

					for (User i : members) {
						aux.add(i.getName() + "\t(" + i.getEmail() + ")");
						member_ids.add(i.getId());
					}

					ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(
							me, android.R.layout.simple_spinner_item, aux);
					adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
					asign.setAdapter(adapter);
				}
			}

		};
		loadMembers.execute(wd);

	}

}
