package com.clommunicate.main;

import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class AddMemberDialog extends Dialog{
	
	private EditText email = null;
	private ImageButton ok = null;
	private ImageButton cancel = null;
	private String result = null;

	public AddMemberDialog(final Context context) {
		super(context, R.style.cust_dialog);
		
		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");

		setContentView(R.layout.add_member_dialog);

		((TextView)findViewById(R.id.add_member_dialog_email_label)).setTypeface(type);
		email = ((EditText)findViewById(R.id.add_member_dialog_email));
		email.setTypeface(type);
		
		ok = (ImageButton)findViewById(R.id.add_member_dialog_ok_button);
		cancel = (ImageButton)findViewById(R.id.add_member_dialog_cancel_button);

		ok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {

				email.setEnabled(false);
				//TODO:Add connection check here
				if(WebApi.login(email.getText().toString())){
					
					User user = WebApi.getClommunicateUser(email.getText().toString());
					
					/*if(user != null)
						((NewProjectActivity)context).getMemberList().getAdapter().*/
						
					user = null;
					
				}	else	{
					
					result = "No user with such email in the system.";
					
				}
				dismiss();
			}
		});
		
		cancel.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				result = "Cancelled by user.";

				dismiss();
				
			}
		});
		

		setTitle(String.format("%-100s", "Add new member to project."));
		show();
		
	}
	
	public String getResult(){
		
		return result;
		
	}

}
