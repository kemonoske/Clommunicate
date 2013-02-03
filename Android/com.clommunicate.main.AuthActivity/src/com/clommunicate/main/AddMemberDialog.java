package com.clommunicate.main;

import com.clommunicate.utils.ProjectDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.UserDAO;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Used to add user objects to an MemberListArrayAdapter, can work in 2 modes:
 * <ul>
 * <li>LOCAL - adding User object only to the MemberListAdapter</li>
 * <li>REMOTE - adding User object to database before adding it to the adapter</li>
 * </ul>
 * 
 * 
 * @author Akira
 * 
 */
public class AddMemberDialog extends Dialog {

	/**
	 * Constants Type Definitions
	 */
	final static int LOCAL = 1;
	final static int REMOTE = 2;

	/**
	 * GUI Elements
	 */
	private TextView email_label = null;
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
		setContentView(R.layout.add_member_dialog);
		setCancelable(false);

		/*
		 * Store arguments in private variables
		 */
		this.context = cont;
		this.member_list = member_list;
		this.member_adapter = member_adapter;
		this.action_type = add_type;

		/*
		 * Load font from asserts
		 */
		Typeface font_zekton = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");
		/*
		 * Load controls from content view by id
		 */
		email_label = ((TextView) findViewById(R.id.add_member_dialog_email_label));
		email = ((EditText) findViewById(R.id.add_member_dialog_email));
		ok = (ImageButton) findViewById(R.id.add_member_dialog_ok_button);
		cancel = (ImageButton) findViewById(R.id.add_member_dialog_cancel_button);

		/*
		 * Set the font of EditText and TextView
		 */
		email_label.setTypeface(font_zekton);
		email.setTypeface(font_zekton);

		/*
		 * On OK button clicked
		 */
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				/*
				 * Disable email EditText
				 */
				email.setEnabled(false);

				/*
				 * Adding operation will be performed in separate thread
				 */
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
		 * On cancel button clicked
		 */
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				result = "Cancelled by user.";
				dismiss();

			}
		});
	}

	private void performAdd() {

		try {
			/*
			 * If such user exist in system, we can add it
			 */
			final User user = UserDAO.login(email.getText().toString());

			if (user == null) {

				result = "User cannot be added to the list, maybe a server error.";

			} else if (member_adapter.contains(user)) /*
													 * We can't add same user 2
													 * times
													 */{

				result = "This user is already a member of this project.";

			} else if (member_adapter.isOwner(user)) /*
													 * We can't add owner to his
													 * own project
													 */{

				result = "You don't have to add yourself, owner is a member by default.";

			} else {
				/*
				 * If dialog works in remote mode we must add user to database
				 * otherwise just add to the list adapter
				 */
				if (action_type == REMOTE) {

					/*
					 * If user successfully added to database then we set
					 * positive result message and add member to the adapter
					 * using post on ListView control
					 */
					if (ProjectDAO.addMember(ProjectActivity.project.getId(),
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

					/*
					 * Set positive result message and add member to the adapter
					 * using post on ListView control
					 */
					result = "Member successfully added tot the project.";
					member_list.post(new Runnable() {
						public void run() {

							member_adapter.addMember(user);

						}
					});
				}
			}
		} catch (NetworkErrorException e) /*
										 * if connection is lost or post request
										 * returns null
										 */{
			result = "No internet connection.";
		} catch (WebAPIException e) {

			result = e.getMessage();

		}

	}

	@Override
	public void setTitle(CharSequence title) {

		super.setTitle(String.format("%-100s", title));

	}

	public String getResult() {

		return result;

	}

}
