package com.clommunicate.main;

import com.clommunicate.main.R;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;

public class WaitDialog extends Dialog {

	public WaitDialog(Context context) {
		super(context, R.style.cust_dialog);
		setContentView(R.layout.wait_dialog);
		setCancelable(true);
		
		ImageButton ib = (ImageButton) findViewById(R.id.wait_dialog_cancel);
		
		ib.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {

				dismiss();
				
			}
		});
	}

}
