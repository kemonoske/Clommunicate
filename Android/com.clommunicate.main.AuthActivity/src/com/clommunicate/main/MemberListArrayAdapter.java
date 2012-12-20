package com.clommunicate.main;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MemberListArrayAdapter extends ArrayAdapter<String>{
	
	private Context context = null;
	private String[] members = null;
	//private 
	
	public MemberListArrayAdapter(Context context, int textViewResourceId) {
		super(context, R.id.member_list_item);
	}

}
