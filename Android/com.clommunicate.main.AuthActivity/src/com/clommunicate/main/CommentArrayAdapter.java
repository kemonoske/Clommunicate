package com.clommunicate.main;

import java.util.ArrayList;

import com.clommunicate.utils.Comment;
import com.clommunicate.utils.User;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Adapter for the comment list
 * @author Akira
 *
 */
public class CommentArrayAdapter extends ArrayAdapter<Comment>{

	private Context context = null;
	private ArrayList<Comment> comments = null;
	private Typeface font_zekton = null;
	
	public CommentArrayAdapter(Context context, ArrayList<Comment> comments) {
		super(context, R.layout.comment_item_left);
		this.context = context;
		this.comments =  comments;
		
		font_zekton = Typeface.createFromAsset(context.getAssets(), "fonts/zekton.ttf");
		
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inf = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		View view = inf.inflate((((position % 2) == 0)?R.layout.comment_item_left:R.layout.comment_item_right), parent, false);
		
		ImageView avatar = (ImageView)view.findViewById(R.id.comment_item_avatar);
		TextView name = (TextView)view.findViewById(R.id.comment_item_name);
		TextView timestamp = (TextView)view.findViewById(R.id.comment_item_timestamp);
		ImageButton remove = (ImageButton)view.findViewById(R.id.comment_item_remove_button);
		TextView text = (TextView)view.findViewById(R.id.comment_item_text);
		
		name.setTypeface(font_zekton);
		timestamp.setTypeface(font_zekton);
		text.setTypeface(font_zekton);
		
		name.setText(comments.get(position).getAuthor().getName());
		text.setText(comments.get(position).getText());
		timestamp.setText(comments.get(position).getTime());
		if(comments.get(position).getAuthor().getPicture() != null)
			avatar.setImageBitmap(comments.get(position).getAuthor().getPicture());
		if(User.user.getId() != comments.get(position).getAuthor().getId())
			remove.setVisibility(View.GONE);
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 20;
	}
	
	
	

}
