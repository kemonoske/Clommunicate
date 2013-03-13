package com.clommunicate.main;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.clommunicate.utils.ClommunicateSQLiteHelper;
import com.clommunicate.utils.TaskStatsDAO;
import com.clommunicate.utils.User;
import com.clommunicate.utils.UserDAO;
import com.clommunicate.utils.WebAPIException;

import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

/**
 * 
 * @author Bostanica Ion
 * 
 */
public class UserActivity extends Activity {

	private ListView userDataList = null;
	private TextView name = null;
	private TextView projects_count = null;
	private TextView part_in__count = null;
	private Button new_project = null;
	private ImageView avatar = null;
	private Activity me = this;
	private MainMenu main_menu = null;
	private WaitDialog wd = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);

		name = (TextView) findViewById(R.id.user_name);
		projects_count = (TextView) findViewById(R.id.user_created_projects);
		part_in__count = (TextView) findViewById(R.id.user_projects_part_in);
		new_project = (Button) findViewById(R.id.user_new_project);
		avatar = (ImageView) findViewById(R.id.user_avatar);

		/* Setam fontul etichetei login */
		Typeface type = Typeface.createFromAsset(getAssets(),
				"fonts/zekton.ttf");
		((TextView) findViewById(R.id.user_part_in_text)).setTypeface(type);
		((TextView) findViewById(R.id.user_projects_text)).setTypeface(type);

		name.setTypeface(type);
		projects_count.setTypeface(type);
		part_in__count.setTypeface(type);
		new_project.setTypeface(type);
		userDataList = (ListView) findViewById(R.id.user_data);
		userDataList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long id) {

				switch (position) {

				case 3: {

					Intent i = new Intent(me, ProjectListActivity.class);
					i.putExtra(
							"activityTitle",
							getResources().getString(
									R.string.user_activity_extra_projects));
					startActivity(i);

				}
					break;
				case 4: {

					Intent i = new Intent(me, ProjectListActivity.class);
					i.putExtra(
							"activityTitle",
							getResources().getString(
									R.string.user_activity_extra_part_in));
					i.putExtra("partIn", true);
					startActivity(i);

				}
					break;
				case 5: {

					final YesNoDialog ynd = new YesNoDialog(UserActivity.this);

					ynd.setTitle(getResources().getString(
							R.string.user_activity_yes_no_dialog_title));
					ynd.setMessage(getResources().getString(
							R.string.user_activity_yes_no_dialog_message));

					ynd.show();

					ynd.setOnDismissListener(new OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {

							wd = new WaitDialog(me);

							AsyncTask<Void, Void, Exception> removeUser = new AsyncTask<Void, Void, Exception>() {

								@Override
								protected void onPreExecute() {
									wd.setTitle(getResources()
											.getString(
													R.string.user_activity_remove_wait_dialog_title));
									wd.show();
								}

								@Override
								protected Exception doInBackground(
										Void... params) {

									try {
										UserDAO.removeUser(User.user.getId());
										TaskStatsDAO tsd = new TaskStatsDAO(me);
										tsd.open();
										try {
											tsd.drop();
										} catch (Exception e) {

										}
										tsd.close();
										

									} catch (NetworkErrorException e) {
										return e;
									} catch (WebAPIException e) {
										return e;
									} catch (NullPointerException e) {
										return e;
									}

									return null;
								}

								@Override
								protected void onPostExecute(Exception result) {
									if (result instanceof NetworkErrorException) {
										onBackPressed();
										Toast.makeText(
												me,
												getResources()
														.getString(
																R.string.error_no_internet_connection),
												Toast.LENGTH_SHORT).show();
										return;
									} else if (result instanceof WebAPIException) {

										onBackPressed();
										Toast.makeText(me, result.getMessage(),
												Toast.LENGTH_SHORT).show();
										return;

									} else if (result instanceof NullPointerException) {

										Intent i = new Intent(me,
												AuthActivity.class);
										startActivity(i);
										Toast.makeText(
												me,
												getResources()
														.getString(
																R.string.error_please_relogin),
												Toast.LENGTH_SHORT).show();
										finish();

									} else {

										Intent i = new Intent(me,
												AuthActivity.class);
										startActivity(i);
										Toast.makeText(
												me,
												getResources()
														.getString(
																R.string.user_activity_remove_text_result_successfull),
												Toast.LENGTH_SHORT).show();
										finish();

									}

									wd.dismiss();

								}

							};

							if (ynd.getStatus())
								removeUser.execute();

						}
					});

				}
					break;
				default: {
				}
					break;

				}

			}
		});
		new_project.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {

				Intent i = new Intent(getApplicationContext(),
						NewProjectActivity.class);
				startActivity(i);

			}
		});

	}

	@Override
	protected void onResume() {

		super.onResume();

		wd = new WaitDialog(me);

		AsyncTask<Void, Void, Exception> loadUser = new AsyncTask<Void, Void, Exception>() {

			@Override
			protected void onPreExecute() {
				wd.setTitle(getResources().getString(
						R.string.user_activity_wait_dialog_title));
				wd.show();
			}

			@Override
			protected Exception doInBackground(Void... params) {

				try {
					User.user = UserDAO.login(User.user.getEmail());
					ClommunicateSQLiteHelper.TABLE = "tasks"
							+ User.user.getId();
					TaskStatsDAO tsd = new TaskStatsDAO(me);
					tsd.open();
					try {
						tsd.create();
					} catch (Exception e) {

					}
					tsd.close();
				} catch (NetworkErrorException e) {
					return e;
				} catch (WebAPIException e) {
					return e;
				} catch (NullPointerException e) {
					return e;
				}

				return null;
			}

			@Override
			protected void onPostExecute(Exception result) {
				if (result instanceof NetworkErrorException) {
					onBackPressed();
					Toast.makeText(
							me,
							getResources().getString(
									R.string.error_no_internet_connection),
							Toast.LENGTH_SHORT).show();
					return;
				} else if (result instanceof WebAPIException) {

					onBackPressed();
					Toast.makeText(me, result.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;

				} else if (result instanceof NullPointerException) {

					Intent i = new Intent(me, AuthActivity.class);
					startActivity(i);
					Toast.makeText(
							me,
							getResources().getString(
									R.string.error_please_relogin),
							Toast.LENGTH_SHORT).show();
					finish();

				} else {
					ClommunicateSQLiteHelper.TABLE = "tasks"
							+ User.user.getId();
					TaskStatsDAO tsd = new TaskStatsDAO(me);
					tsd.open();
					try {
						tsd.create();
					} catch (Exception e) {
					}
					tsd.close();
					name.setText(User.user.getName());
					
					final ViewAnimator va = (ViewAnimator)findViewById(R.id.user_avatar_sw);

					va.showNext();
					if (User.user.getPictureURL() != "null" && User.user.getPictureURL() != null)	{
						Runnable r = new Runnable() {
							
							@Override
							public void run() {
								try {
									User.user.setPicture(User.user.getPictureURL());
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								avatar.post(new Runnable() {
									
									@Override
									public void run() {
										if(User.user.getPicture() != null)
											avatar.setImageBitmap(User.user.getPicture());
										va.showPrevious();
										
									}
								});
							}
						};
						
						new Thread(r).start();
					}
					projects_count.setText(String.valueOf(User.user
							.getProjects()));
					part_in__count
							.setText(String.valueOf(User.user.getPartIn()));
					String[] userData = {
							User.user.getEmail(),
							User.user.getGender().toString(),
							User.user.getLocale(),
							String.valueOf(User.user.getProjects()),
							String.valueOf(User.user.getPartIn()),
							getResources()
									.getString(
											R.string.user_activity_menu_delete_account_value) };
					String[] userDataTitles = {
							"Email",
							getResources().getString(
									R.string.user_activity_menu_gender),
							getResources().getString(
									R.string.user_activity_menu_locale),
							getResources()
									.getString(
											R.string.user_activity_menu_projects_created),
							getResources()
									.getString(
											R.string.user_activity_menu_projects_part_in),
							getResources().getString(
									R.string.user_activity_menu_delete_account) };
					int[] userDataIcons = { R.drawable.email_icon,
							R.drawable.gender_icon, R.drawable.locale_icon,
							R.drawable.owner_icon, R.drawable.part_in_icon,
							R.drawable.remove_user_icon };

					UserDataArrayAdapter userDataAdapter = new UserDataArrayAdapter(
							me, userData, userDataTitles, userDataIcons);
					userDataList.setAdapter(userDataAdapter);
				}

				wd.dismiss();

			}

		};

		loadUser.execute();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		main_menu = new MainMenu(this, R.id.user_activity_main);

		main_menu.addMenuItem(R.drawable.main_menu_logout, getResources()
				.getString(R.string.main_menu_logout), new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent i = new Intent(me, AuthActivity.class);
				startActivity(i);
				finish();

			}
		});

		main_menu.addMenuItem(R.drawable.main_menu_refresh, getResources()
				.getString(R.string.main_menu_refresh), new OnClickListener() {

			@Override
			public void onClick(View v) {

				onResume();
				main_menu.close();

			}
		});

		return true;
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_MENU) {
			if (main_menu != null)
				main_menu.toggle();
			return true;
		}

		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onPause() {

		super.onPause();

		if (wd != null)
			wd.dismiss();
	}

}
