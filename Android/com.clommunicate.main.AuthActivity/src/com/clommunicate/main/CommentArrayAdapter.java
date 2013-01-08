package com.clommunicate.main;

import com.clommunicate.utils.Comment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

public class CommentArrayAdapter extends ArrayAdapter<Comment>{

	private Context context = null;
	public CommentArrayAdapter(Context context) {
		super(context, R.layout.comment_item_left);
		this.context = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inf.inflate((((position % 2) == 0)?R.layout.comment_item_left:R.layout.comment_item_right), parent, false);
		
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}
	
	
	

}
