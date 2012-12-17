package com.clommunicate.main;

import org.w3c.dom.Text;

import com.clommunicate.utils.GUIFixes;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager.LayoutParams;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

public class NewProjectActivity extends Activity {
	private Activity me = this;
	private ImageButton add_member_button = null;
	private ListView member_list = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_new_project);
		setComponentsFont();
		String[] values = { "test", "test", "test", "test", "test", "test",
				"test", "test", "test", "test", "test", "test", "test", "test",
				"test", "test", "test", "test", "test", "test", "test", "test",
				"test", "test", "test", "test", "test", "test" };
		member_list = (ListView) findViewById(R.id.new_project_member_list);
		member_list
				.setAdapter(new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_1,
						android.R.id.text1));
		
		LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) member_list
				.getLayoutParams();
		
		GUIFixes.setListViewHeightBasedOnChildren(
				member_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
		
		add_member_button = (ImageButton) findViewById(R.id.new_project_add_member_button);
		
		add_member_button.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				AddMemberDialog amd = new AddMemberDialog(me);
				amd.show();
				amd.setTitle(String.format("%-100s", "Add new member to project."));
				System.err.println("hello");
			}
		});
	}

	public void setComponentsFont()	{
		
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.new_project_activity_title)).setTypeface(type);
		((TextView) findViewById(R.id.new_project_name_label)).setTypeface(type);
		((TextView) findViewById(R.id.new_project_type_label)).setTypeface(type);
		((TextView) findViewById(R.id.new_project_logo_label)).setTypeface(type);
		((TextView) findViewById(R.id.new_project_member_list_label)).setTypeface(type);
		((EditText) findViewById(R.id.new_project_name)).setTypeface(type);
		//((TextView) findViewById(android.R.id.text1)).setTypeface(type);
		
	}
	
}
