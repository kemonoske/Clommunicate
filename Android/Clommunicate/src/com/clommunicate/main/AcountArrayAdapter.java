package com.clommunicate.main;

import com.clommunicate.main.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AcountArrayAdapter extends ArrayAdapter<String> {

	private Context context = null;
	private String[] names = null;
	private String[] tokens = null;
	
	public AcountArrayAdapter(Context context, String[] names, String[] tokens){
		
		super(context,R.layout.acount_item,names);
		this.context =  context;
		this.names = names;
		this.tokens = tokens;
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inf.inflate(R.layout.acount_item, parent, false);
		TextView name = (TextView)item.findViewById(R.id.auth_acount_item);
		TextView token = (TextView)item.findViewById(R.id.auth_acount_item2);

		Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/asen.ttf");
		name.setText(names[position]);
		name.setTypeface(type);
		token.setText(tokens[position]);
		token.setTypeface(type);
		return item;
	}

}
