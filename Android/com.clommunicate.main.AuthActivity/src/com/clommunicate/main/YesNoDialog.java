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
 * @author Bostanica Ion
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
	private Context context = null;

	protected YesNoDialog(Context contex, int p_id, int u_id,
			final boolean partIn) {

		super(contex, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.pid = p_id;
		this.uid = u_id;
		this.context = contex;
		
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				msg = context.getResources().getString(R.string.yes_no_dialog_cancelled_by_user);
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
							msg = (status) ? ((partIn) ? context.getResources().getString(R.string.yes_no_dialog_remove_member_text_result_success)
									: context.getResources().getString(R.string.yes_no_dialog_remove_project_text_result_success))
									: context.getResources().getString(R.string.yes_no_dialog_remove_text_result_fail);
						} catch (NetworkErrorException e) {

							msg = context.getResources().getString(R.string.error_no_internet_connection);

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

	protected YesNoDialog(Context contex, int p_id) {

		super(contex, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.pid = p_id;
		this.context = contex;
		
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				msg = context.getResources().getString(R.string.yes_no_dialog_cancelled_by_user);
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
							msg = (status) ? context.getResources().getString(R.string.yes_no_dialog_finish_text_result_success)
									: context.getResources().getString(R.string.yes_no_dialog_remove_text_result_fail);
						} catch (NetworkErrorException e) {

							msg = context.getResources().getString(R.string.error_no_internet_connection);

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

	protected YesNoDialog(Context contex) {

		super(contex, R.style.cust_dialog);
		setContentView(R.layout.yes_no_dialog);
		setCancelable(false);

		message = (TextView) findViewById(R.id.yes_no_dialog_message);
		ok = (ImageButton) findViewById(R.id.yes_no_dialog_yes_button);
		cancel = (ImageButton) findViewById(R.id.yes_no_dialog_cancel_button);

		this.context = contex;
		
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				msg = context.getResources().getString(R.string.yes_no_dialog_cancelled_by_user);
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
