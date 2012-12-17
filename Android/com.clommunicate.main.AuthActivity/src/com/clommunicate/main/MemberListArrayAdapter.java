package com.clommunicate.main;

import android.content.Context;
import android.widget.ArrayAdapter;

public class MemberListArrayAdapter extends ArrayAdapter<String>{

	public MemberListArrayAdapter(Context context, int textViewResourceId) {
		super(context, R.id.member_list_item);
	}

}
