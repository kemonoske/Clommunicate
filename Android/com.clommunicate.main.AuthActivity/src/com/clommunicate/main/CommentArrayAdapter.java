package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Comment;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebApi;

import android.accounts.NetworkErrorException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Adapter for the comment list
 * 
 * @author Akira
 * 
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

	private Context context = null;
	private ArrayList<Comment> comments = null;
	private ArrayList<User> members = null;
	private Typeface font_zekton = null;

	public CommentArrayAdapter(Context context, ArrayList<Comment> comments,
			ArrayList<User> members) {
		super(context, R.layout.comment_item_left);
		this.context = context;
		this.comments = comments;
		this.members = members;

		/*
		 * Load font from asserts
		 */
		font_zekton = Typeface.createFromAsset(context.getAssets(),
				"fonts/zekton.ttf");

	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		/*
		 * If position is even then use left sided comment otherwise right sided
		 */
		View view = inf.inflate(
				(((position % 2) == 0) ? R.layout.comment_item_left
						: R.layout.comment_item_right), parent, false);

		/*
		 * Load controls from inflated view
		 */
		ImageView avatar = (ImageView) view
				.findViewById(R.id.comment_item_avatar);
		TextView name = (TextView) view.findViewById(R.id.comment_item_name);
		TextView timestamp = (TextView) view
				.findViewById(R.id.comment_item_timestamp);
		ImageButton remove = (ImageButton) view
				.findViewById(R.id.comment_item_remove_button);
		TextView text = (TextView) view.findViewById(R.id.comment_item_text);

		/*
		 * Setting typeface
		 */
		name.setTypeface(font_zekton);
		timestamp.setTypeface(font_zekton);
		text.setTypeface(font_zekton);

		/*
		 * Associating comment with the author
		 */
		for (User i : members)
			if (i.getId() == comments.get(position).getAuthor()) {
				/*
				 * Set author name and avatar in titlebar of the comment
				 */
				name.setText(i.getName());
				if (i.getPicture() != null)
					avatar.setImageBitmap(i.getPicture());
			}

		/*
		 * Setting text and time of the comment
		 */
		text.setText(comments.get(position).getText());
		timestamp.setText(comments.get(position).getTime());

		/*
		 * User can remove only his own comments
		 */
		if (User.user.getId() != comments.get(position).getAuthor())
			remove.setVisibility(View.GONE);
		else
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					/*
					 * Yes/No dialog to confirm removal
					 */
					final YesNoDialog ynd = new YesNoDialog(context);
					ynd.setTitle("Confirm comment remove...");
					ynd.setMessage("Do you really want to remove this comment?");

					/*
					 * When yes/no dialog is dissmissed
					 */
					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							/*
							 * If yes was selected we should remove the comment
							 */
							if (ynd.getStatus()) {

								/*
								 * Wait dialog will be displayed while we are
								 * removing the comment
								 */
								WaitDialog wd = new WaitDialog(context);
								wd.setTitle("Loading task data...");
								wd.show();

								AsyncTask<WaitDialog, Void, Object[]> removeComment = new AsyncTask<WaitDialog, Void, Object[]>() {

									@Override
									protected Object[] doInBackground(
											WaitDialog... params) {

										Object[] aux = new Object[2];
										WaitDialog wd = params[0];

										aux[1] = 0;
										try {

											// TODO:Check here if the task still
											// exists
											/*
											 * If comment removed from database
											 * then the error status is OK
											 */
											if (WebApi.removeComment(comments
													.get(position).getId()))
												aux[1] = 1;
										} catch (NetworkErrorException e) {

											aux[1] = -1;

										}

										aux[0] = wd;

										return aux;

									}

									@Override
									protected void onPostExecute(Object[] result) {
										WaitDialog wd = (WaitDialog) result[0];
										Integer error = (Integer) result[1];
										wd.dismiss();

										String text = null;

										text = (error == 0) ? "Error can't remove comment."
												: "No internet connection.";

										/*
										 * If removing is successful we remove
										 * comment object from the list and
										 * notify data set changed
										 */
										if (error == 1) {

											text = "Comment removed.";
											comments.remove(position);
											notifyDataSetChanged();

										}

										Toast.makeText(context, text,
												Toast.LENGTH_SHORT).show();
									}

								};
								removeComment.execute(wd);

							}

						}
					});

					ynd.show();

				}
			});
		return view;
	}

	@Override
	public int getCount() {

		return comments.size();

	}

}
