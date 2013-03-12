package com.clommunicate.main;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

import com.clommunicate.utils.CommentDAO;
import com.clommunicate.utils.Task;
import com.clommunicate.utils.TaskDAO;
import com.clommunicate.utils.TaskStats;
import com.clommunicate.utils.TaskStatsDAO;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * Adapter for task list, contains an array list of Task objects that provide
 * data for the list
 * 
 * @author Bostanica Ion
 * 
 */
public class TaskListArrayAdapter extends ArrayAdapter<Task> {

	/**
	 * Context and other data
	 */
	private Context context = null;
	private ArrayList<Task> tasks = null;
	private Map<Integer, Integer> task_notifications = null;
	private TaskListArrayAdapter me = this;

	/**
	 * GUI Elements
	 */
	private TextView name = null;
	private ImageView icon = null;
	private CheckBox completed = null;
	private Typeface font_asen = null;

	public TaskListArrayAdapter(Context contex, ArrayList<Task> tasks) {
		super(contex, R.layout.task_list_item);
		this.context = contex;
		this.tasks = tasks;
		this.task_notifications = new TreeMap<Integer, Integer>();

		for (int i = 0; i < tasks.size(); i++)
			task_notifications.put(tasks.get(i).getId(), -1);
		/*
		 * Load font from Asserts
		 */
		font_asen = Typeface.createFromAsset(context.getAssets(),
				"fonts/roboto_light.ttf");

		try {

			for (final Task a : tasks) {

				Runnable r = new Runnable() {

					@Override
					public void run() {
						TaskStatsDAO tsd = new TaskStatsDAO(context);
						tsd.open();
						if (tsd.exists(a.getId())
								&& tsd.get(a.getId()).getLast_comment() != 0) {
							try {
								task_notifications.put(a.getId(), CommentDAO
										.getCountAfter(a.getId(),
												tsd.get(a.getId())
														.getLast_comment()));
								((Activity) context)
										.runOnUiThread(new Runnable() {

											@Override
											public void run() {
												me.notifyDataSetChanged();
											}
										});
							} catch (NetworkErrorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (WebAPIException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						} else if (tsd.exists(a.getId())
								&& tsd.get(a.getId()).getLast_comment() == 0) {

							try {
								task_notifications.put(a.getId(),
										CommentDAO.getCount(a.getId()));
							} catch (NetworkErrorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (WebAPIException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

						} else {
							int last_comment = 0;
							try {
								last_comment = CommentDAO.getLastComment(a
										.getId());
							} catch (NetworkErrorException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (WebAPIException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							tsd.add(new TaskStats(a.getId(), 0, last_comment));
						}

						tsd.close();
					}
				};

				new Thread(r).start();
			}

		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	@Override
	public int getCount() {

		return tasks.size() + 1;

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		/*
		 * Get layout inflater service
		 */
		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*
		 * If position higher tham max index then it's the add task item
		 */

		if (position == tasks.size()) {

			/*
			 * Inflate add list item
			 */
			View item = inf.inflate(R.layout.add_list_item, parent, false);

			TextView tv = (TextView) item.findViewById(R.id.add_list_item_tag);
			tv.setText(context.getResources().getString(
					R.string.task_list_array_adapter_add_task));
			tv.setTypeface(font_asen);

			/*
			 * If project is completed we can't add a task
			 */
			if (ProjectActivity.project.isCompleted())
				item.setEnabled(false);
			return item;
		}

		/*
		 * inflate Task list item
		 */
		final View item = inf.inflate(R.layout.task_list_item, parent, false);

		final TextView tv = (TextView) item
				.findViewById(R.id.task_list_item_comment_count);
		final LinearLayout ll = (LinearLayout) item
				.findViewById(R.id.task_list_item_notification);

		/*
		 * if (task_notifications.get(tasks.get(position).getId()) == -1) {
		 * Runnable r = new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * synchronized (task_notifications) {
		 * 
		 * while (task_notifications.get(tasks.get(position) .getId()) == -1);
		 * 
		 * if (task_notifications.get(tasks.get(position).getId()) > 0) {
		 * tv.post(new Runnable() {
		 * 
		 * @Override public void run() { tv.setText(String
		 * .valueOf(task_notifications .get(tasks.get(position) .getId())));
		 * 
		 * } });
		 * 
		 * ((Activity)context).runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() { ll.setVisibility(LinearLayout.VISIBLE);
		 * 
		 * } }); }
		 * 
		 * task_notifications.notify();
		 * 
		 * }
		 * 
		 * } };
		 * 
		 * new Thread(r).start();
		 * 
		 * } else
		 */if (task_notifications.get(tasks.get(position).getId()) > 0) {
			tv.setText(String.valueOf(task_notifications.get(tasks
					.get(position).getId())));
			ll.setVisibility(LinearLayout.VISIBLE);
		}

		/*
		 * Locate all controls in item view by id
		 */
		completed = (CheckBox) item.findViewById(R.id.task_list_item_finished);
		name = (TextView) item.findViewById(R.id.task_list_item_name);
		icon = (ImageView) item.findViewById(R.id.task_list_item_icon);

		/*
		 * Disable focus for CheckBox so the list item can be clicked
		 */
		completed.setFocusable(false);
		completed.setFocusableInTouchMode(false);

		name.setTypeface(font_asen);

		/*
		 * If task is completed it's name is crossed
		 */
		if (tasks.get(position).isCompleted())
			name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);

		name.setText(tasks.get(position).getName());
		completed.setChecked(tasks.get(position).isCompleted());

		/*
		 * Set icon based on task type
		 */
		switch (tasks.get(position).getType()) {
		case Task.GENERAL:
			icon.setImageResource(R.drawable.general_task_icon);
			break;

		case Task.DESIGN:
			icon.setImageResource(R.drawable.design_task_icon);
			break;

		case Task.DEBUG:
			icon.setImageResource(R.drawable.debug_task_icon);
			break;

		case Task.CODE:
			icon.setImageResource(R.drawable.code_task_icon);
			break;

		case Task.BENCHMARK:
			icon.setImageResource(R.drawable.benchmark_task_icon);
			break;

		case Task.IMPLEMENTATION:
			icon.setImageResource(R.drawable.implementation_task_icon);
			break;

		case Task.PROJECT:
			icon.setImageResource(R.drawable.project_task_icon);
			break;

		default:
			icon.setImageResource(R.drawable.general_task_icon);
			break;
		}

		/*
		 * If project is completed we update a task
		 */
		if (ProjectActivity.project.isCompleted())
			completed.setEnabled(false);

		/*
		 * When completed box is checked or unchecked
		 */
		completed.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					final boolean isChecked) {

				AsyncTask<Void, Void, Exception> completeTask = new AsyncTask<Void, Void, Exception>() {

					private WaitDialog wd = null;

					@Override
					protected void onPreExecute() {
						/*
						 * A WaitDialog will be displayed while performing
						 * request
						 */
						wd = new WaitDialog(context);
						wd.setTitle(context
								.getResources()
								.getString(
										R.string.task_list_array_adapter_update_wait_dialog_title));
						wd.show();
					}

					@Override
					protected Exception doInBackground(Void... params) {

						try {
							/*
							 * If task status changed return 1
							 */
							// TODO:if task is not removed, if project is not
							// removed or finished
							TaskDAO.markTaskCompleted(tasks.get(position)
									.getId(), (isChecked ? 1 : 0));

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
							text = context
									.getResources()
									.getString(
											R.string.task_list_array_adapter_update_text_result_success);
							tasks.get(position).setCompleted(isChecked);
							notifyDataSetChanged();
						} else if (result instanceof WebAPIException) {
							text = result.getMessage();
						} else {
							text = context.getResources().getString(
									R.string.error_no_internet_connection);
						}
						Toast.makeText(context.getApplicationContext(), text,
								Toast.LENGTH_SHORT).show();

					}

				};

				completeTask.execute();

			}
		});

		return item;
	}

	public Task getTask(int position) {

		return this.tasks.get(position);

	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}

}
