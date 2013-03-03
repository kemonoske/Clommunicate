package com.clommunicate.main;

import com.clommunicate.main.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Bostanica Ion
 *
 */
public class UserDataArrayAdapter extends ArrayAdapter<String> {

	private Context context = null;
	private String[] values = null;
	private String[] titles = null;
	private int[] icons = null;
	
	public UserDataArrayAdapter(Context context, String[] values, String[] titles, int[] icons){
		
		super(context,R.layout.acount_item,values);
		this.context =  context;
		this.values = values;
		this.titles = titles;
		this.icons = icons;
		
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View item = inf.inflate(R.layout.user_data_item, parent, false);
		TextView value = (TextView)item.findViewById(R.id.user_data_title);
		TextView title = (TextView)item.findViewById(R.id.user_data_value);
		ImageView icon = (ImageView)item.findViewById(R.id.user_data_icon);

		Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/asen.ttf");
		value.setText(values[position]);
		value.setTypeface(type);
		title.setText(titles[position]);
		title.setTypeface(type);
		icon.setImageResource(icons[position]);
		return item;
	}

}
