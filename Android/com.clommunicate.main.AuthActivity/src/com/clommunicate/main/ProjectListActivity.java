package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.GUIFixes;
import com.clommunicate.utils.Project;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class ProjectListActivity extends Activity {
	
	private TextView title = null;
	private ListView project_list = null;
	private ImageButton search_button = null;
	private EditText search_field = null;
	private boolean search_expand = false;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_project_list);
		
		title = (TextView)findViewById(R.id.project_list_activity_title);
		project_list = (ListView)findViewById(R.id.project_list_project_list);
		search_button = (ImageButton)findViewById(R.id.project_list_search_button);
		search_field = (EditText)findViewById(R.id.project_list_search_field);

		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		title.setTypeface(type);
		
		search_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				if(!search_expand){
					
					search_field.setVisibility(EditText.VISIBLE);
					search_expand = true;
					
				} else {

					search_field.setVisibility(EditText.GONE);
					search_field.setText("");
					InputMethodManager imm = (InputMethodManager)getSystemService(
						      Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(search_field.getWindowToken(), 0);
					search_expand = false;
					
				}
					
				
			}
		});
		
		Intent i = getIntent();
		
		title.setText(i.getStringExtra("activityTitle"));

		if(i.getBooleanExtra("partIn", false)){
			
		}
		
		project_list.setAdapter(new ProjectListArrayAdapter(this, new ArrayList<Project>(10)));
		/*GUIFixes.setListViewHeightBasedOnChildren(
				project_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
		*/
		project_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				
			}
		});
		
	}

	
	
}