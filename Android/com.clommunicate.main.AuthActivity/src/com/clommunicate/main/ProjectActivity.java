package com.clommunicate.main;

import java.util.concurrent.TimeUnit;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Layout;
import android.text.format.Time;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
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
		th.addTab(th.newTabSpec("1").setIndicator(view).setContent(R.id.tab1));
		view = LayoutInflater.from(th.getContext()).inflate(R.layout.tab_item,
				null);
		iv = (ImageView) view.findViewById(R.id.tab_item_icon);
		iv.setBackgroundResource(R.drawable.members_tab_norm);
		tv = (TextView) view.findViewById(R.id.tab_item_title);
		tv.setText("Project Members");
		tv.setTypeface(typef);
		th.addTab(th.newTabSpec("2").setIndicator(view).setContent(R.id.tab2));
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

		if (User.user.getId() == project.getOwner_id())	{
			quit.setVisibility(ImageButton.GONE);

			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(me, project.getId(), false);
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
		}	else {
			remove.setVisibility(ImageButton.GONE);
			

			quit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					final YesNoDialog ynd = new YesNoDialog(me, project.getId(),true);
					ynd.setTitle(String.format("%-100s", "Confirm project quit..."));
					ynd.setMessage("Do you really want to quit from this project?");
					
					ynd.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {

							if (ynd.getStatus())
								finish();
							
							Toast.makeText(me.getApplicationContext(), ynd.getMsg(), Toast.LENGTH_SHORT).show();
							
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

		loadProjectData();
	}

	private void loadProjectData() {
		name.setText(project.getName());
		start_date.setText(project.getStart_date());
		deadline.setText(project.getDeadline());
		if (project.getEnd_date().compareToIgnoreCase("null") == 0)
			end_date.setText("N/A");
		else
			end_date.setText(project.getEnd_date());
		// day_count.setText(projects.get(position).getName());

		String[] rsd = project.getStart_date().split("-");

		Time tsd = new Time();
		tsd.set(Integer.valueOf(rsd[2]), Integer.valueOf(rsd[1]) - 1,
				Integer.valueOf(rsd[0]));
		// System.err.println(tsd.toString());
		Time ted = new Time();
		ted.setToNow();
		// System.err.println(ted.toString());

		long difference = ted.toMillis(true) - tsd.toMillis(true);

		long days = TimeUnit.MILLISECONDS.toDays(difference);

		day_count.setText(String.valueOf(days));

		description.setText(project.getDescription());
	}

}
