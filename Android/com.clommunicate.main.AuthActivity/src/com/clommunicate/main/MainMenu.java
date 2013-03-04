package com.clommunicate.main;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;

/**
 * 
 * @author Bostanica Ion
 *
 */
public class MainMenu {

	private SlidingDrawer menu = null;
	private LinearLayout content = null;
	private Activity context = null;

	public MainMenu(Activity contex, int host) {

		this.context = contex;
		RelativeLayout main = (RelativeLayout) context.findViewById(host);
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		menu = (SlidingDrawer) inflater
				.inflate(R.layout.main_menu, main, false);
		main.addView(menu);
		content = (LinearLayout) menu.findViewById(R.id.main_menu_items);

		MainMenuItem about = new MainMenuItem(context,
				R.drawable.main_menu_about, context.getResources().getString(
						R.string.main_menu_about));

		about.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AboutDialog ad = new AboutDialog(context);
				ad.show();
				MainMenu.this.close();

			}
		});

		content.addView(about, 0);


	}

	public void open() {

		menu.animateOpen();

	}

	public void close() {

		menu.animateClose();

	}

	public void toggle() {

		menu.animateToggle();

	}
	
	public void addMenuItem(int icon, CharSequence name, OnClickListener listener)	{
		
		MainMenuItem aux = new MainMenuItem(context,
				icon, name);
		
		aux.setOnClickListener(listener);
		
		content.addView(aux, 0);
		
	}

}
