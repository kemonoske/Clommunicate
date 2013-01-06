package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Task;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class TaskListArrayAdapter extends ArrayAdapter<Task> {

	/**
	 * 
	 */
	private Context context = null;
	private ArrayList<Task> tasks = null;

	/**
	 * GUI Elements
	 */
	private TextView name = null;
	private ImageView icon = null;
	private CheckBox completed = null;
	private Typeface typeface = null;

	public TaskListArrayAdapter(Context context, ArrayList<Task> tasks) {
		super(context, R.layout.task_list_item);
		this.context = context;
		this.tasks = tasks;
		typeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/asen.ttf");

	}

	@Override
	public int getCount() {

		return tasks.size() + 1;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(position == tasks.size()){

			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View item = inf.inflate(R.layout.add_list_item, parent, false);
			TextView tv = (TextView)item.findViewById(R.id.add_list_item_tag);
			tv.setText("Add New Task");
			tv.setTypeface(typeface);
			if (ProjectActivity.project.getEnd_date().compareToIgnoreCase("null") != 0)
				item.setEnabled(false);
			return item;
		}	
		

		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inf.inflate(R.layout.task_list_item, parent, false);
		completed = (CheckBox)item.findViewById(R.id.task_list_item_finished);
		completed.setFocusable(false);
		completed.setFocusableInTouchMode(false);
		
		name = (TextView)item.findViewById(R.id.task_list_item_name);
		icon = (ImageView)item.findViewById(R.id.task_list_item_icon);
		
		name.setTypeface(typeface);
		
		if(tasks.get(position).isCompleted())
			name.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
		
		name.setText(tasks.get(position).getName());
		completed.setChecked(tasks.get(position).isCompleted());

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
		
		
		
		return item;
	}

	public Task getTask(int position){
		
		return this.tasks.get(position);
		
	}
	
}
