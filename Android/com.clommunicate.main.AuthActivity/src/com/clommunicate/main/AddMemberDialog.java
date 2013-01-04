package com.clommunicate.main;

import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

public class AddMemberDialog extends Dialog {

	/**
	 * Constants Type Definitions
	 */
	final static int LOCAL = 1;
	final static int REMOTE = 2;

	/**
	 * GUI Elements
	 */
	private EditText email = null;
	private ImageButton ok = null;
	private ImageButton cancel = null;
	private Context context = null;
	private ListView member_list = null;
	private MemberListArrayAdapter member_adapter = null;

	/**
	 * Result and other data
	 */
	private String result = "Cancelled by user.";
	private int action_type = LOCAL;

	public AddMemberDialog(Context cont, ListView member_list,
			MemberListArrayAdapter member_adapter, int add_type) {

		super(cont, R.style.cust_dialog);
		setCancelable(false);

		/**
		 * 
		 */
		this.context = cont;
		this.member_list = member_list;
		this.member_adapter = member_adapter;
		this.action_type = add_type;

		Typeface type = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");

		setContentView(R.layout.add_member_dialog);

		((TextView) findViewById(R.id.add_member_dialog_email_label))
				.setTypeface(type);
		email = ((EditText) findViewById(R.id.add_member_dialog_email));
		email.setTypeface(type);

		ok = (ImageButton) findViewById(R.id.add_member_dialog_ok_button);
		cancel = (ImageButton) findViewById(R.id.add_member_dialog_cancel_button);

		/**
		 * OK Button Listener
		 */
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				email.setEnabled(false);

				Runnable r = new Runnable() {

					@Override
					public void run() {

						performAdd();

						dismiss();
					}

				};

				new Thread(r).start();
			}
		});

		/**
		 * Cancel Button Listener
		 */
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				result = "Cancelled by user.";

				dismiss();

			}
		});

		setTitle(String.format("%-100s", "Add new member to project."));
		show();
	}

	private void performAdd() {

		try {
			if (WebApi.login(email.getText().toString())) {

				final User user = WebApi.getClommunicateUser(email.getText()
						.toString());
				System.err.println(context.getClass());
				if (user == null) {

					result = "User cannot be added to the list, check your internet connection.";

				} else if (member_adapter.contains(user)) {

					result = "This user is already a member of this project.";
				} else if (member_adapter.isOwner(user)) {

					result = "You don't have to add yourself, owner is a member by default.";

				} else {
					if (action_type == REMOTE) {

						if (WebApi.addMember(ProjectActivity.project.getId(),
								user.getId())) {
							result = "Member successfully added tot the project.";
							member_list.post(new Runnable() {
										public void run() {

											member_adapter.addMember(user);

										}
									});
						} else
							result = "Error adding member to the project.";

					} else {
						result = "Member successfully added tot the project.";
						member_list.post(new Runnable() {
							public void run() {

								member_adapter.addMember(user);

							}
						});
					}
				}

			} else {

				result = "No user with such email in the system.";

			}
		} catch (NetworkErrorException e) {
			result = "No internet connection.";
		}

	}

	public String getResult() {

		return result;

	}

}
