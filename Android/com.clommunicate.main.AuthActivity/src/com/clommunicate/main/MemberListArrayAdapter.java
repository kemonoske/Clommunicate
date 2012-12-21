package com.clommunicate.main;

import java.util.ArrayList;

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

public class MemberListArrayAdapter extends ArrayAdapter<User>{
	
	private Context context = null;
	private ArrayList<User> members = null;
	
	public MemberListArrayAdapter(Context context, int textViewResourceId) {
		super(context, R.layout.member_list_item);
		this.context = context;
		members = new ArrayList<User>(0);
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inf.inflate(R.layout.member_list_item, parent, false);
		TextView name = (TextView)item.findViewById(R.id.member_list_item_name);
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
		
		
		return item;
	}

    @Override
    public int getCount() {
        return members.size();
    }

	public void addMember(User member){
		
		members.add(member);
		notifyDataSetChanged();
		
	}
	
	public void removeMember(int position){
		
		members.remove(position);
		notifyDataSetChanged();
		
	}
	
	
}
