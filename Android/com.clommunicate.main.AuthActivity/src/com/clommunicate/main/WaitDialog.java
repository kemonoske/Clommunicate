package com.clommunicate.main;

import com.clommunicate.main.R;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.ImageButton;

public class WaitDialog extends Dialog {

	public WaitDialog(Context context) {
		super(context, R.style.cust_dialog);
		setContentView(R.layout.wait_dialog);

		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");
	}

	

}
