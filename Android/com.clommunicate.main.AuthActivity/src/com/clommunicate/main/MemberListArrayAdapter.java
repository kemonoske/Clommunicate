package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.User;

import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Typeface;
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
 * 
 * @author Bostanica Ion
 *
 */
public class MemberListArrayAdapter extends ArrayAdapter<User> {

	private Context context = null;
	private ArrayList<User> members = new ArrayList<User>(0);
	private Typeface type = null;
	private int owner_id = 0;

	public MemberListArrayAdapter(Context context) {

		super(context, R.layout.member_list_item);
		this.context = context;
		type = Typeface.createFromAsset(context.getAssets(), "fonts/roboto_light.ttf");

	}

	public MemberListArrayAdapter(Context context, ArrayList<User> members,
			int owner_id) {

		this(context);
		this.members = members;
		this.owner_id = owner_id;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(context instanceof ProjectActivity && position == members.size()){

			LayoutInflater inf = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			View item = inf.inflate(R.layout.add_list_item, parent, false);
			TextView tv = (TextView)item.findViewById(R.id.add_list_item_tag);
			tv.setText(context.getResources().getString(R.string.member_list_array_adapter_add_member));
			tv.setTypeface(type);
			if (ProjectActivity.project.getEnd_date().compareToIgnoreCase("null") != 0)
				item.setEnabled(false);
			return item;
		}	
		
		LayoutInflater inf = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View item = inf.inflate(R.layout.member_list_item, parent, false);
		TextView name = (TextView) item
				.findViewById(R.id.member_list_item_name);
		TextView email = (TextView) item
				.findViewById(R.id.member_list_item_email);
		name.setText(members.get(position).getName());
		name.setTypeface(type);
		email.setText(members.get(position).getEmail());
		email.setTypeface(type);
		ImageView photo = (ImageView) item
				.findViewById(R.id.member_list_item_photo);
		if (members.get(position).getPicture() != null)
			photo.setImageBitmap(members.get(position).getPicture());
		ImageButton remove = (ImageButton) item
				.findViewById(R.id.member_list_item_remove_button);

		final int pos = position;
		remove.setFocusable(false);
		remove.setFocusableInTouchMode(false);
		if (context instanceof ProjectActivity) {

			if (members.get(position).getId() == owner_id
					|| User.user.getId() != owner_id)
				remove.setVisibility(View.GONE);
			else {

				if (ProjectActivity.project.getEnd_date().compareToIgnoreCase(
						"null") != 0) {

					remove.setEnabled(false);

				} else
					remove.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							final YesNoDialog ynd = new YesNoDialog(context,
									ProjectActivity.project.getId(), members
											.get(pos).getId(), true);
							ynd.setTitle(context.getResources().getString(R.string.member_list_array_adapter_yes_no_dialog_title));
							ynd.setMessage(context.getResources().getString(R.string.member_list_array_adapter_yes_no_dialog_message));

							ynd.setOnDismissListener(new OnDismissListener() {

								@Override
								public void onDismiss(DialogInterface dialog) {

									if (ynd.getStatus())
										removeMember(pos);

									Toast.makeText(
											context.getApplicationContext(),
											ynd.getMsg(), Toast.LENGTH_SHORT)
											.show();

								}
							});

							ynd.show();

						}
					});
			}
		} else
			remove.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					removeMember(pos);

				}
			});

		return item;
	}

	@Override
	public int getCount() {
		if(context instanceof ProjectActivity)
			return members.size() +1 ;
		return members.size();
	}

	public ArrayList<User> getMembers() {

		return this.members;

	}

	public void addMember(User member) {

		members.add(member);
		notifyDataSetChanged();
		if(context instanceof NewProjectActivity)
			((NewProjectActivity)context).resizeList();

	}

	public void removeMember(int position) {

		members.remove(position);
		notifyDataSetChanged();
		if(context instanceof NewProjectActivity)
			((NewProjectActivity)context).resizeList();

	}

	public boolean contains(User member) {

		for (User i : members)
			if (i.getId() == member.getId())
				return true;

		return false;

	}

	public boolean isOwner(User member) {

		if (User.user.getId() == member.getId())
			return true;

		return false;

	}

}
