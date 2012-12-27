package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Project;
import com.clommunicate.utils.User;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectListArrayAdapter extends ArrayAdapter<Project> {

	private Context context = null;
	private ArrayList<Project> projects = null;

	public ProjectListArrayAdapter(Context context, ArrayList<Project> arrayList) {
		super(context, R.layout.project_list_item, arrayList);
		this.context = context;
		// members = new ArrayList<User>(0);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inf.inflate(R.layout.project_list_item, parent, false);
		/*TextView name = (TextView)item.findViewById(R.id.member_list_item_name);
		TextView email = (TextView)item.findViewById(R.id.member_list_item_email);

		Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/asen.ttf");
		name.setText(members.get(position).getName());
		name.setTypeface(type);
		email.setText(members.get(position).getEmail());
		email.setTypeface(type);
		ImageView photo =  (ImageView)item.findViewById(R.id.member_list_item_photo);
		photo.setImageBitmap(members.get(position).getPicture());
		ImageButton remove = (ImageButton)item.findViewById(R.id.member_list_item_remove_button);
		
		final int pos = position;
		
		remove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				removeMember(pos);
				
			}
		});
		*/
		ImageButton remove = (ImageButton)item.findViewById(R.id.project_list_item_remove_project_button);
		remove.setFocusable(false);
		remove.setFocusableInTouchMode(false);
		
		return item;
	}

	@Override
	public int getCount() {
		return 20;
	}

}
