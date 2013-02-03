package com.clommunicate.main;

import com.clommunicate.utils.ProjectDAO;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * 
 * @author Akira
 * 
 */
public class YesNoDialog extends Dialog {

	private TextView message = null;
	private ImageButton ok = null;
	private ImageButton cancel = null;
	private int pid = 0;
	private int uid = 0;
	private boolean status = false;
	private String msg = null;

	protected YesNoDialog(Context context, int p_id, int u_id,
			final boolean partIn) {

		super(context, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.pid = p_id;
		this.uid = u_id;

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
								status = ProjectDAO.removeMember(pid, uid);
							else
								status = ProjectDAO.removeProject(pid);
							msg = (status) ? ((partIn) ? "You are no longer a member of the project."
									: "Project removed.")
									: "Operation failed, possible server problem.";
						} catch (NetworkErrorException e) {

							msg = "No internet connection.";

						} catch (WebAPIException e) {

							msg = e.getMessage();
							
						}
						dismiss();
					}
				};

				new Thread(r).start();

			}
		});

	}

	protected YesNoDialog(Context context, int p_id) {

		super(context, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.pid = p_id;

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

							status = ProjectDAO.markProjectCompleted(pid);
							msg = (status) ? "This project is now completed."
									: "Operation failed, possible server problem.";
						} catch (NetworkErrorException e) {

							msg = "No internet connection.";

						} catch (WebAPIException e) {

							msg = e.getMessage();
							
						}
						dismiss();
					}
				};

				new Thread(r).start();

			}
		});

	}

	protected YesNoDialog(Context context) {

		super(context, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

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
				status = true;
				dismiss();

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

	@Override
	public void setTitle(CharSequence title) {

		super.setTitle(String.format("%-100s", title));

	}

}
