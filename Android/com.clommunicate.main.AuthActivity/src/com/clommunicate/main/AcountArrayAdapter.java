package com.clommunicate.main;

import com.clommunicate.main.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 
 * Adapter used for list of accounts in AuthActivity
 * 
 * @author Bostanica Ion
 *
 */
public class AcountArrayAdapter extends ArrayAdapter<String> {

	private Context context = null;
	private String[] names = null;
	private String[] tokens = null;
	private Typeface font_asen = null;
	
	public AcountArrayAdapter(Context context, String[] names, String[] tokens){
		
		super(context,R.layout.acount_item,names);
		
		/*
		 * Load font from asserts
		 */
		font_asen = Typeface.createFromAsset(context.getAssets(), "fonts/asen.ttf");
		
		this.context =  context;
		this.names = names;
		this.tokens = tokens;
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		/*
		 * Get layout inflater service and inflate view from acount_item.xml
		 */
		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View item = inf.inflate(R.layout.acount_item, parent, false);
		
		/*
		 * Load controls from view using id
		 */
		TextView name = (TextView)item.findViewById(R.id.auth_acount_item);
		TextView token = (TextView)item.findViewById(R.id.auth_acount_item2);

		/*
		 * Set control contents and the font used
		 */
		name.setText(names[position]);
		name.setTypeface(font_asen);
		token.setText(tokens[position]);
		token.setTypeface(font_asen);
		
		return item;
	}

}
