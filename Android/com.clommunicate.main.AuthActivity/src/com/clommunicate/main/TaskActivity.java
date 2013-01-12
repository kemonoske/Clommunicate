package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Comment;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @author Akira
 * 
 */
public class TaskActivity extends Activity {

	/**
	 * Context and other data
	 */
	private Activity me = this;
	private int task_id = 0;
	private Task task = null;

	/**
	 * GUI Elements
	 */
	private ImageView task_icon = null;
	private TextView task_name = null;
	private CheckBox task_completion = null;
	private TextView task_date_label = null;
	private TextView task_date = null;
	private TextView task_description = null;
	private TextView asigned_name = null;
	private TextView asigned_email = null;
	private ImageView asigned_photo = null;
	private ImageView user_photo = null;
	private EditText user_comment = null;
	private ImageButton submit_comment = null;
	private ListView comment_list = null;
	private ArrayList<Comment> comments = null;
	private ArrayList<User> members = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_task);

		Intent i = getIntent();
		task_id = i.getIntExtra("task_id", 0);

		task_icon = (ImageView) findViewById(R.id.activity_task_icon);
		task_name = (TextView) findViewById(R.id.activity_task_name);
		task_completion = (CheckBox) findViewById(R.id.activity_task_finished);
		task_date_label = (TextView) findViewById(R.id.activity_task_date_label);
		task_date = (TextView) findViewById(R.id.activity_task_date);
		task_description = (TextView) findViewById(R.id.activity_task_description);
		task_description.setMovementMethod(new ScrollingMovementMethod());
		asigned_name = (TextView) findViewById(R.id.activity_task_asigned_name);
		asigned_email = (TextView) findViewById(R.id.activity_task_asigned_email);
		asigned_photo = (ImageView) findViewById(R.id.activity_task_asigned_photo);
		user_photo = (ImageView) findViewById(R.id.activity_task_user_photo);
		user_comment = (EditText) findViewById(R.id.activity_task_comment);
		submit_comment = (ImageButton) findViewById(R.id.activity_task_submit_comment);
		comment_list = (ListView) findViewById(R.id.activity_task_comment_list);

		Typeface typeface = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");

		task_name.setTypeface(typeface);
		task_date_label.setTypeface(typeface);
		task_date.setTypeface(typeface);
		task_description.setTypeface(typeface);
		asigned_name.setTypeface(typeface);
		asigned_email.setTypeface(typeface);
		user_comment.setTypeface(typeface);

	}

	@Override
	protected void onResume() {

		super.onResume();
		WaitDialog wd = new WaitDialog(me);
		wd.setTitle(String.format("%-100s", "Loading task data..."));
		wd.show();
		AsyncTask<WaitDialog, Void, Object[]> loadMembers = new AsyncTask<WaitDialog, Void, Object[]>() {

			@Override
			protected Object[] doInBackground(WaitDialog... params) {

				Object[] aux = new Object[3];
				WaitDialog wd = params[0];
				Task task = null;

				aux[2] = 0;
				try {
					task = WebApi.getTask(task_id);
					if (task != null) {

						members = WebApi.getProjectMembers(task.getOwner());
						comments = WebApi.getCommentList(task.getId());

						aux[2] = 1;

					}
				} catch (NetworkErrorException e) {

					aux[2] = -1;

				}

				aux[0] = wd;
				aux[1] = task;

				return aux;

			}

			@Override
			protected void onPostExecute(Object[] result) {
				WaitDialog wd = (WaitDialog) result[0];
				Task task1 = (Task) result[1];
				Integer error = (Integer) result[2];
				wd.dismiss();

				String text = null;

				text = (error == 0) ? "Error can't load task info."
						: "No internet connection.";

				if (error == 0 || error == -1) {
					Toast.makeText(me, text, Toast.LENGTH_SHORT).show();
				} else {
					task = task1;
					loadTaskDataToUI();
				}
			}

		};
		loadMembers.execute(wd);

	}

	public void loadTaskDataToUI() {

		switch (task.getType()) {
		case Task.GENERAL:
			task_icon.setImageResource(R.drawable.general_task_icon);
			break;

		case Task.DESIGN:
			task_icon.setImageResource(R.drawable.design_task_icon);
			break;

		case Task.DEBUG:
			task_icon.setImageResource(R.drawable.debug_task_icon);
			break;

		case Task.CODE:
			task_icon.setImageResource(R.drawable.code_task_icon);
			break;

		case Task.BENCHMARK:
			task_icon.setImageResource(R.drawable.benchmark_task_icon);
			break;

		case Task.IMPLEMENTATION:
			task_icon.setImageResource(R.drawable.implementation_task_icon);
			break;

		case Task.PROJECT:
			task_icon.setImageResource(R.drawable.project_task_icon);
			break;

		default:
			task_icon.setImageResource(R.drawable.general_task_icon);
			break;
		}

		if (task.isCompleted())
			task_name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		else
			task_name.setPaintFlags(task_name.getPaintFlags()
					& (~Paint.STRIKE_THRU_TEXT_FLAG));

		task_name.setText(task.getName());
		task_completion.setChecked(task.isCompleted());
		task_date_label.setText((task.isCompleted() ? "finished on" : "from"));
		task_date.setText((task.isCompleted() ? task.getEnd_date() : task
				.getStart_date()));
		task_description.setText(task.getDescription());
		asigned_name.setText(task.getAsigned().getName());
		asigned_email.setText(task.getAsigned().getEmail());
		if (task.getAsigned().getPicture() != null)
			asigned_photo.setImageBitmap(task.getAsigned().getPicture());
		if (User.user.getPicture() != null)
			user_photo.setImageBitmap(User.user.getPicture());

		if(ProjectActivity.project.isCompleted())	{
			
			task_completion.setEnabled(false);
			user_comment.setFocusable(false);
			user_comment.clearFocus();
			user_comment.setEnabled(false);
			
		}
			
		/*
		 * When completed box is checked or unchecked
		 */
		task_completion
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							final boolean isChecked) {

						AsyncTask<Void, Void, Integer> completeTask = new AsyncTask<Void, Void, Integer>() {

							private WaitDialog wd = null;

							@Override
							protected void onPreExecute() {
								/*
								 * A WaitDialog will be displayed while
								 * performing request
								 */
								wd = new WaitDialog(me);
								wd.setTitle(String.format("%-100s",
										"Updating task..."));
								wd.show();
							}

							@Override
							protected Integer doInBackground(Void... params) {

								try {
									/*
									 * If task status changed return 1
									 */
									// TODO:if task is not removed, if project
									// is not
									// removed or finished
									if (WebApi.completeTask(task.getId(),
											(isChecked ? 1 : 0)))
										return 1;

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
									text = "Task updated.";
									task.setCompleted(isChecked);
									Time t = new Time();
									t.setToNow();
									task.setEnd_date(t.year + "-"
											+ (((t.month + 1) < 10) ? "0" : "")
											+ (t.month + 1) + "-"
											+ (((t.monthDay) < 10) ? "0" : "")
											+ (t.monthDay));
									loadTaskDataToUI();
								} else if (result == 0) {
									text = "Error updating task.";
								} else {
									text = "No internet connection.";
								}
								Toast.makeText(getApplicationContext(), text,
										Toast.LENGTH_SHORT).show();

							}

						};

						completeTask.execute();

					}
				});

		/*
		 * Submit comment on click
		 */
		submit_comment.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(user_comment.getText().toString().trim().length() == 0)	{
					return;
				}
				
				Comment comment = new Comment(
						user_comment.getText().toString().trim(), User.user.getId(),
						task.getId());

				AsyncTask<Comment, Void, Integer> create_comment = new AsyncTask<Comment, Void, Integer>() {

					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {

						wd = new WaitDialog(me);
						wd.setTitle("Adding comment...");
						wd.show();
					}

					@Override
					protected Integer doInBackground(Comment... params) {

						try {
							if (WebApi.createComment(params[0]))
								return 1;

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
							text = "Comment added.";
							user_comment.setText("");
							onResume();
						} else if (result == 0) {
							text = "Error adding comment.";
						} else
							text = "No internet connection.";

						Toast.makeText(getApplicationContext(), text,
								Toast.LENGTH_SHORT).show();

					}

				};

				create_comment.execute(comment);

			}
		});
		/*
		 * Populating comment list view
		 */

		CommentArrayAdapter ca = new CommentArrayAdapter(me, comments, members);
		comment_list.setAdapter(ca);

	}

}
