package com.clommunicate.main;

import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class YesNoDialog extends Dialog {

	private TextView message = null;
	private ImageButton ok = null;
	private ImageButton cancel = null;
	private int id = 0;
	private boolean status = false;
	private String msg = null;

	protected YesNoDialog(Context context,
			int pid, final boolean partIn) {

		super(context, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.id = pid;

		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				msg = "Operation cancelled by user.";
				dismiss();

			}
		});

		ok.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Runnable r = new Runnable() {

					@Override
					public void run() {
						try {
							if (partIn)
								status = WebApi.removeMember(id,
										User.user.getId());
							else
								status = WebApi.removeProject(id);
							msg = (status) ? ((partIn)?"You are no longer a member of the project.":"Project removed.")
									: "Operation failed, possible server problem.";
						} catch (NetworkErrorException e) {

							msg = "No internet connection.";
							
						}
						dismiss();
					}
				};

				new Thread(r).start();

			}
		});

	}

	public void setMessage(CharSequence message) {

		this.message.setText('\n' + (String) message + '\n');

	}

	public boolean getStatus() {
		return this.status;
	}

	public String getMsg() {

		return this.msg;

	}

}
