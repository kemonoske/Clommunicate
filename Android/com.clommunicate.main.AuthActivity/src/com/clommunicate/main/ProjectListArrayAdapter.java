package com.clommunicate.main;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author Bostanica Ion
 * 
 */
public class ProjectListArrayAdapter extends ArrayAdapter<Project> implements
		Filterable {

	private Context context = null;
	private boolean partIn = false;
	private ArrayList<Project> defprojects = new ArrayList<Project>(0);
	private ArrayList<Project> projects = new ArrayList<Project>(0);
	private Typeface typef = null;

	public ProjectListArrayAdapter(Context context,
			ArrayList<Project> projects, boolean type) {
		super(context, R.layout.project_list_item,
				R.id.project_list_item_project_name, projects);
		this.context = context;
		this.partIn = type;
		typef = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");
		this.defprojects = projects;
		this.projects = projects;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View item = inf.inflate(R.layout.project_list_item, parent, false);
		TextView name = (TextView) item
				.findViewById(R.id.project_list_item_project_name);
		TextView start_date = (TextView) item
				.findViewById(R.id.project_list_item_start_date);
		TextView start_date_label = (TextView) item
				.findViewById(R.id.project_list_item_start_date_label);
		TextView deadline = (TextView) item
				.findViewById(R.id.project_list_item_deadline);
		TextView deadline_label = (TextView) item
				.findViewById(R.id.project_list_item_deadline_label);
		TextView end_date = (TextView) item
				.findViewById(R.id.project_list_item_end_date);
		TextView end_date_label = (TextView) item
				.findViewById(R.id.project_list_item_end_date_label);
		TextView day_count = (TextView) item
				.findViewById(R.id.project_list_item_day_count);
		ImageButton quit = (ImageButton) item
				.findViewById(R.id.project_list_item_quit_project_button);
		ImageButton remove = (ImageButton) item
				.findViewById(R.id.project_list_item_remove_project_button);

		name.setTypeface(typef);
		start_date.setTypeface(typef);
		start_date_label.setTypeface(typef);
		deadline.setTypeface(typef);
		deadline_label.setTypeface(typef);
		end_date.setTypeface(typef);
		end_date_label.setTypeface(typef);
		day_count.setTypeface(typef);

		name.setText(projects.get(position).getName());
		start_date.setText(projects.get(position).getStart_date());
		deadline.setText(projects.get(position).getDeadline());
		// day_count.setText(projects.get(position).getName());

		String[] rsd = projects.get(position).getStart_date().split("-");

		Time tsd = new Time();
		tsd.set(Integer.valueOf(rsd[2]), Integer.valueOf(rsd[1]) - 1,
				Integer.valueOf(rsd[0]));
		// System.err.println(tsd.toString());
		Time ted = new Time();
		if (projects.get(position).getEnd_date().compareToIgnoreCase("null") == 0) {
			end_date.setText("N/A");
			ted.setToNow();
		} else {
			remove.setEnabled(false);
			quit.setEnabled(false);
			end_date.setText(projects.get(position).getEnd_date());
			String[] red = projects.get(position).getEnd_date().split("-");
			ted.set(Integer.valueOf(red[2]), Integer.valueOf(red[1]) - 1,
					Integer.valueOf(red[0]));
		}
		// System.err.println(ted.toString());

		long difference = ted.toMillis(true) - tsd.toMillis(true);

		long days = TimeUnit.MILLISECONDS.toDays(difference);

		day_count.setText(String.valueOf(days));

		quit.setFocusable(false);
		quit.setFocusableInTouchMode(false);
		remove.setFocusable(false);
		remove.setFocusableInTouchMode(false);

		if (!partIn) {

			quit.setVisibility(View.GONE);

			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(context, projects
							.get(position).getId(), User.user.getId(), partIn);
					ynd.setTitle(context.getResources().getString(R.string.project_list_array_adapter_yes_no_dialog_remove_title));
					ynd.setMessage(context.getResources().getString(R.string.project_list_array_adapter_yes_no_dialog_remove_message));

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus())
								removeProject(position);

							Toast.makeText(context.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});

		} else {

			remove.setVisibility(View.GONE);

			quit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(context, projects
							.get(position).getId(), User.user.getId(), partIn);
					ynd.setTitle(context.getResources().getString(R.string.project_list_array_adapter_yes_no_dialog_quit_title));
					ynd.setMessage(context.getResources().getString(R.string.project_list_array_adapter_yes_no_dialog_quit_message));

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus())
								removeProject(position);

							Toast.makeText(context.getApplicationContext(),
									ynd.getMsg(), Toast.LENGTH_SHORT).show();

						}
					});

					ynd.show();

				}
			});
		}

		return item;
	}

	@Override
	public int getCount() {
		return projects.size();
	}

	@SuppressLint("DefaultLocale")
	@Override
	public Filter getFilter() {

		Filter filter = new Filter() {

			@SuppressWarnings("unchecked")
			@Override
			protected void publishResults(CharSequence constraint,
					FilterResults results) {

				projects = (ArrayList<Project>) results.values;
				notifyDataSetChanged();

			}

			@SuppressLint("DefaultLocale")
			@Override
			protected FilterResults performFiltering(CharSequence constraint) {

				FilterResults results = new FilterResults();
				// We implement here the filter logic
				if (constraint == null
						|| constraint.toString().trim().length() == 0) {
					// No filter implemented we return all the list
					results.values = defprojects;
					results.count = defprojects.size();
				} else {
					// We perform filtering operation
					ArrayList<Project> nProjectList = new ArrayList<Project>();

					for (Project p : defprojects) {
						if (p.getName().toLowerCase()
								.contains(constraint.toString().toLowerCase()))
							nProjectList.add(p);
					}

					results.values = nProjectList;
					results.count = nProjectList.size();

				}
				return results;

			}
		};

		return filter;
	}

	public void removeProject(int position) {
		
		projects.remove(position);
		notifyDataSetChanged();

	}

	public Project getProject(int position) {

		return projects.get(position);

	}

}
