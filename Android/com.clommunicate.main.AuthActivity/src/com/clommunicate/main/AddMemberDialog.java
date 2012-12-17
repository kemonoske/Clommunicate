package com.clommunicate.main;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddMemberDialog extends Dialog{
	
	private EditText email = null;
	private ImageButton ok = null;
	private ImageButton cancel = null;

	public AddMemberDialog(Context context) {
		super(context, R.style.cust_dialog);
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");

		setContentView(R.layout.add_member_dialog);

		((TextView)findViewById(R.id.add_member_dialog_email_label)).setTypeface(type);
		((EditText)findViewById(R.id.add_member_dialog_email)).setTypeface(type);
		
		
	}

}
