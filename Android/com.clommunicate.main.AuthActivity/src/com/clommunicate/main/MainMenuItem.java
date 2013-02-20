package com.clommunicate.main;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainMenuItem extends LinearLayout {

	private LinearLayout item = null;
	private ImageView icon = null;
	private TextView title = null;
	private Typeface font_zekton = null;

	public MainMenuItem(Context context, int icon, CharSequence title) {

		super(context);

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		item = (LinearLayout) inflater.inflate(R.layout.main_menu_item, null,
				false);

		this.icon = (ImageView) item.findViewById(R.id.main_menu_item_icon);
		this.title = (TextView) item.findViewById(R.id.main_menu_item_title);

		font_zekton = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");

		this.title.setTypeface(font_zekton);
		this.title.setText(title);
		
		this.icon.setImageResource(icon);

		this.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.WRAP_CONTENT));
		
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT);
		lp.setMargins(5, 2, 5, 0);
		lp.weight = 80;
		item.setLayoutParams(lp);
		this.addView(item);

	}
	
	@Override
	public void setOnClickListener(OnClickListener listener)	{
		
		item.setOnClickListener(listener);
		
	}
	
	

}
