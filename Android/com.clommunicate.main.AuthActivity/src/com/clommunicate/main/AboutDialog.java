package com.clommunicate.main;

import android.app.Dialog;
import android.content.Context;

public class AboutDialog  extends Dialog{

	public AboutDialog(Context context) {
		super(context, R.style.cust_dialog);
		setContentView(R.layout.activity_about);
		
		setTitle("About");
		
		setCancelable(true);
		setCanceledOnTouchOutside(true);
		
	}

	@Override
	public void setTitle(CharSequence title) {

		super.setTitle(String.format("%-100s", title));

	}


}
