package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ProjectListActivity extends Activity {
	
	private TextView title = null;
	private ListView project_list = null;
	private ImageButton search_button = null;
	private EditText search_field = null;
	private boolean search_expand = false;
	private boolean partIn = false;
	private Activity me = this;
	

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

		this.partIn = i.getBooleanExtra("partIn", false);
			
		
		project_list.setAdapter(new ProjectListArrayAdapter(this, new ArrayList<Project>(10), partIn));
		/*GUIFixes.setListViewHeightBasedOnChildren(
				project_list,
				getApplicationContext().getResources().getDisplayMetrics().widthPixels);
		*/
		project_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				if(search_expand)
					search_button.performClick();
				ProjectActivity.project = ((ProjectListArrayAdapter)project_list.getAdapter()).getProject(arg2);
				Intent i = new Intent(me, ProjectActivity.class);
				startActivity(i);
				
			}
		});
		
		search_field.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

				((ProjectListArrayAdapter)project_list.getAdapter()).getFilter().filter(s);
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}


	@Override
	protected void onResume() {

		super.onResume();
		final WaitDialog wd = new WaitDialog(me);
		
		AsyncTask<Void, Void, Object[]> loadList = new AsyncTask<Void, Void, Object[]>() {

			@Override
			protected void onPreExecute() {
				wd.setTitle(String.format("%-100s", "Project list loading..."));
				wd.show();
			}
			
			@SuppressLint("UseValueOf")
			@Override
			protected Object[] doInBackground(Void... params) {
					
				Object[] res = new Object[2];
				res[0] = null;
				res[1] = new Integer(0);
				
					try {
						res[0] = WebApi.getProjectList(User.user.getId(), partIn);
					} catch (NetworkErrorException e) {
						res[1] = -1;
					}
				
					if (res[0] != null)
						res[1] = 1;
						
					
				return res;
			}

			@SuppressWarnings("unchecked")
			@Override
			protected void onPostExecute(Object[] res) {
				
				Integer result = (Integer) res[1];
				
				wd.dismiss();
				if(result == 1)
					project_list.setAdapter(new ProjectListArrayAdapter(me, ((ArrayList<Project>) res[0]), partIn));
				else if(result == 0){
					project_list.setAdapter(new ProjectListArrayAdapter(me, (new ArrayList<Project>(0)), partIn));
					Toast.makeText(getApplicationContext(), "Project list is empty...", Toast.LENGTH_SHORT).show();
				}else	{
					project_list.setAdapter(new ProjectListArrayAdapter(me, (new ArrayList<Project>(0)), partIn));
					Toast.makeText(getApplicationContext(), "No internet connection.", Toast.LENGTH_SHORT).show();
				}
			}
			
		};
		
		loadList.execute();
		
	}
	
	
	
}