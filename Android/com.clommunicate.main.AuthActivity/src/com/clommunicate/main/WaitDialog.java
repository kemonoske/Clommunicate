package com.clommunicate.main;

import com.clommunicate.main.R;

import android.app.Dialog;
import android.content.Context;

/**
 * 
 * @author Akira
 *
 */
public class WaitDialog extends Dialog {

	public WaitDialog(Context context) {
		super(context, R.style.cust_dialog);
		setContentView(R.layout.wait_dialog);
		setCancelable(false);
	}

	@Override
	public void setTitle(CharSequence title) {

		super.setTitle(String.format("%-100s", title));

	}

}
