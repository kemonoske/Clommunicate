package com.clommunicate.main;

import java.io.IOException;
import java.util.ArrayList;

import com.clommunicate.utils.Comment;
import com.clommunicate.utils.CommentDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.WebAPIException;

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
import android.widget.ViewAnimator;

/**
 * Adapter for the comment list
 * 
 * @author Bostanica Ion
 * 
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment> {

	private Context context = null;
	private ArrayList<Comment> comments = null;
	private ArrayList<User> members = null;
	private Typeface font_zekton = null;
	private Typeface font_roboto = null;

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
		font_roboto = Typeface.createFromAsset(context.getAssets(),
				"fonts/roboto_regular.ttf");

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
		final ImageView avatar = (ImageView) view
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
		text.setTypeface(font_roboto);

		/*
		 * Associating comment with the author
		 */
		for (final User i : members)
			if (i.getId() == comments.get(position).getAuthor()) {
				/*
				 * Set author name and avatar in titlebar of the comment
				 */
				name.setText(i.getName());
				/*
				 * if (i.getPicture() != null)
				 * avatar.setImageBitmap(i.getPicture());
				 */
				if (i.getPictureURL().compareToIgnoreCase("null") != 0
						&& i.getPictureURL() != null && i.getPicture() == null) {
					final ViewAnimator va = (ViewAnimator) view
							.findViewById(R.id.comment_item_avatar_sw);

					va.showNext();

					Runnable r = new Runnable() {

						@Override
						public void run() {
							try {
								i.setPicture(i.getPictureURL());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							avatar.post(new Runnable() {

								@Override
								public void run() {

									if (i.getPicture() != null) {
										avatar.setImageBitmap(i.getPicture());
									}
									va.showPrevious();
								}
							});
						}
					};
					new Thread(r).start();
				} else if (i.getPicture() != null) {
					avatar.setImageBitmap(i.getPicture());
				}
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
					ynd.setTitle(context.getResources().getString(
							R.string.comment_array_adapter_yes_no_dialog_title));
					ynd.setMessage(context
							.getResources()
							.getString(
									R.string.comment_array_adapter_yes_no_dialog_message));

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
								wd.setTitle(context
										.getResources()
										.getString(
												R.string.comment_array_adapter_wait_dialog_title));
								wd.show();

								AsyncTask<WaitDialog, Void, Object[]> removeComment = new AsyncTask<WaitDialog, Void, Object[]>() {

									@Override
									protected Object[] doInBackground(
											WaitDialog... params) {

										Object[] aux = new Object[2];
										WaitDialog wd = params[0];

										aux[1] = null;

										try {

											// TODO:Check here if the task still
											// exists
											/*
											 * If comment removed from database
											 * then the error status is OK
											 */
											CommentDAO.removeComment(comments
													.get(position).getId());

										} catch (NetworkErrorException e) {

											aux[1] = e;

										} catch (WebAPIException e) {

											aux[1] = e;

										}

										aux[0] = wd;

										return aux;

									}

									@Override
									protected void onPostExecute(Object[] result) {
										WaitDialog wd = (WaitDialog) result[0];
										Exception e = (Exception) result[1];
										wd.dismiss();

										String text = null;

										/*
										 * If removing is successful we remove
										 * comment object from the list and
										 * notify data set changed
										 */
										if (e == null) {

											text = context
													.getResources()
													.getString(
															R.string.comment_array_adapter_result);
											comments.remove(position);
											notifyDataSetChanged();

										} else if (e instanceof NetworkErrorException) {

											text = context
													.getResources()
													.getString(
															R.string.error_no_internet_connection);

										} else if (e instanceof WebAPIException) {

											text = e.getMessage();

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
