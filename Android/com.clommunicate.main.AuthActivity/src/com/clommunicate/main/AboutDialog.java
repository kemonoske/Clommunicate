package com.clommunicate.main;

import android.app.Dialog;
import android.content.Context;

/**
 * 
 * @author Bostanica Ion
 *
 */
public class AboutDialog  extends Dialog{

	public AboutDialog(Context context) {
		super(context, R.style.cust_dialog);
		setContentView(R.layout.activity_about);
		
		setTitle(context.getResources().getString(R.string.about_dialog_title));
		
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		
	}

	@Override
	public void setTitle(CharSequence title) {

		super.setTitle(String.format("%-100s", title));

	}


}
