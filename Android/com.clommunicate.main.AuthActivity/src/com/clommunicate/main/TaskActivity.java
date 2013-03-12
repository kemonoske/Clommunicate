package com.clommunicate.main;

import java.io.IOException;
import java.util.ArrayList;

import com.clommunicate.utils.Comment;
import com.clommunicate.utils.CommentDAO;
import com.clommunicate.utils.ProjectDAO;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.TaskDAO;
import com.clommunicate.utils.TaskStatsDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.Menu;
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
import android.widget.ViewAnimator;
import android.widget.CompoundButton.OnCheckedChangeListener;

/**
 * 
 * @author Bostanica Ion
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
	private WaitDialog wd = null;
	private MainMenu main_menu = null;

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
		Typeface typeface_roboto = Typeface.createFromAsset(getAssets(),
				"fonts/roboto_regular.ttf");

		task_name.setTypeface(typeface);
		task_date_label.setTypeface(typeface);
		task_date.setTypeface(typeface);
		task_description.setTypeface(typeface);
		if (asigned_name != null) {
			asigned_name.setTypeface(typeface);
			asigned_email.setTypeface(typeface);
			user_comment.setTypeface(typeface_roboto);
		}

	}

	@Override
	protected void onResume() {

		super.onResume();
		wd = new WaitDialog(me);
		wd.setTitle(getResources().getString(
				R.string.task_activity_load_waid_dialog_title));
		wd.show();
		AsyncTask<WaitDialog, Void, Object[]> loadMembers = new AsyncTask<WaitDialog, Void, Object[]>() {

			@Override
			protected Object[] doInBackground(WaitDialog... params) {

				Object[] aux = new Object[3];
				wd = params[0];
				Task task = null;

				aux[2] = null;
				try {
					task = TaskDAO.getTask(task_id);
					if (task != null) {

						members = ProjectDAO.getProjectMembers(task.getOwner());
						comments = CommentDAO.getCommentList(task.getId());
						
						TaskStatsDAO tsd = new TaskStatsDAO(me);

						tsd.open();

						if(tsd.exists(task_id) && comments.size() > 0){
							tsd.nullify(task_id,comments.get(comments.size() - 1).getId());
							System.err.println(tsd.get(task_id).getLast_comment());
						}
						tsd.close();
						

					}
				} catch (NetworkErrorException e) {

					aux[2] = e;

				} catch (WebAPIException e) {

					aux[2] = e;

				}

				aux[0] = wd;
				aux[1] = task;

				return aux;

			}

			@Override
			protected void onPostExecute(Object[] result) {
				wd = (WaitDialog) result[0];
				Task task1 = (Task) result[1];
				Exception error = (Exception) result[2];
				wd.dismiss();

				if (error == null) {

					task = task1;
					try {
						loadTaskDataToUI();
					} catch (NullPointerException e) {

						Intent i = new Intent(me, AuthActivity.class);
						startActivity(i);
						Toast.makeText(
								me,
								getResources().getString(
										R.string.error_please_relogin),
								Toast.LENGTH_SHORT).show();
						finish();
					}

				} else if (error instanceof WebAPIException) {

					Toast.makeText(me, error.getMessage(), Toast.LENGTH_SHORT)
							.show();

				} else if (error instanceof NetworkErrorException) {

					Toast.makeText(me, error.getMessage(), Toast.LENGTH_SHORT)
							.show();

				}
			}

		};
		loadMembers.execute(wd);

	}

	public void loadTaskDataToUI() throws NullPointerException {

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
		if (asigned_name != null) {
			asigned_name.setText(task.getAsigned().getName());
			asigned_email.setText(task.getAsigned().getEmail());

			if (task.getAsigned().getPictureURL() != "null"
					&& task.getAsigned().getPictureURL() != null) {
				final ViewAnimator va = (ViewAnimator) findViewById(R.id.activity_task_asigned_photo_sw);

				va.showNext();
				Runnable r = new Runnable() {

					@Override
					public void run() {
						try {
							task.getAsigned().setPicture(
									task.getAsigned().getPictureURL());
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						asigned_photo.post(new Runnable() {

							@Override
							public void run() {

								if (task.getAsigned().getPicture() != null)
									asigned_photo.setImageBitmap(task
											.getAsigned().getPicture());
								va.showPrevious();
							}
						});
					}
				};

				new Thread(r).start();
			}

			if (User.user.getPicture() != null)
				user_photo.setImageBitmap(User.user.getPicture());
		}
		if (ProjectActivity.project.isCompleted()) {

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

						AsyncTask<Void, Void, Exception> completeTask = new AsyncTask<Void, Void, Exception>() {

							private WaitDialog wd = null;

							@Override
							protected void onPreExecute() {
								/*
								 * A WaitDialog will be displayed while
								 * performing request
								 */
								wd = new WaitDialog(me);
								wd.setTitle(getResources()
										.getString(
												R.string.task_activity_update_wait_dialog_title));
								wd.show();
							}

							@Override
							protected Exception doInBackground(Void... params) {

								try {
									/*
									 * If task status changed return 1
									 */
									// TODO:if task is not removed, if project
									// is not
									// removed or finished
									TaskDAO.markTaskCompleted(task.getId(),
											(isChecked ? 1 : 0));

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

								if (result == null) {
									text = getResources()
											.getString(
													R.string.task_activity_update_text_result_success);
									task.setCompleted(isChecked);
									Time t = new Time();
									t.setToNow();
									task.setEnd_date(t.year + "-"
											+ (((t.month + 1) < 10) ? "0" : "")
											+ (t.month + 1) + "-"
											+ (((t.monthDay) < 10) ? "0" : "")
											+ (t.monthDay));
									loadTaskDataToUI();
								} else if (result instanceof WebAPIException) {
									text = result.getMessage();
								} else if (result instanceof NetworkErrorException) {
									text = getResources()
											.getString(
													R.string.error_no_internet_connection);
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

				if (user_comment.getText().toString().trim().length() == 0) {
					return;
				}

				Comment comment = new Comment(user_comment.getText().toString()
						.trim(), User.user.getId(), task.getId());

				AsyncTask<Comment, Void, Exception> create_comment = new AsyncTask<Comment, Void, Exception>() {

					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {

						wd = new WaitDialog(me);
						wd.setTitle(getResources()
								.getString(
										R.string.task_activity_add_comment_wait_dialog_title));
						wd.show();
					}

					@Override
					protected Exception doInBackground(Comment... params) {

						try {

							CommentDAO.addComment(params[0]);

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

						if (result == null) {
							text = getResources()
									.getString(
											R.string.task_activity_add_comment_text_result_success);
							user_comment.setText("");
							onResume();
						} else if (result instanceof WebAPIException) {
							text = result.getMessage();
						} else
							text = getResources().getString(
									R.string.error_no_internet_connection);

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

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		main_menu = new MainMenu(this, R.id.task_activity_main);

		main_menu.addMenuItem(R.drawable.main_menu_logout, getResources()
				.getString(R.string.main_menu_logout), new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(me, AuthActivity.class);
				startActivity(i);
				finish();

			}
		});

		main_menu.addMenuItem(R.drawable.main_menu_refresh, getResources()
				.getString(R.string.main_menu_refresh), new OnClickListener() {

			@Override
			public void onClick(View v) {

				onResume();
				main_menu.close();

			}
		});

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
