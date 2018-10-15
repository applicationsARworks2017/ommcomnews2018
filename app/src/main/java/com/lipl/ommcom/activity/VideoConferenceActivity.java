package com.lipl.ommcom.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.network.connectionclass.ConnectionClassManager;
import com.facebook.network.connectionclass.ConnectionQuality;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.ConferenceNews;
import com.lipl.ommcom.pojo.ConferenceUser;
import com.lipl.ommcom.pojo.Item;
import com.lipl.ommcom.twitter.Utils;
import com.lipl.ommcom.util.Config;
import com.lipl.ommcom.util.MyAppService;
import com.lipl.ommcom.util.SharedPreUtil;
import com.lipl.ommcom.util.Util;

import org.brickred.socialauth.Profile;
import org.brickred.socialauth.android.DialogListener;
import org.brickred.socialauth.android.SocialAuthAdapter;
import org.brickred.socialauth.android.SocialAuthError;
import org.brickred.socialauth.android.SocialAuthListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.nodemedia.LivePlayer;
import cn.nodemedia.LivePublisher;
import cn.nodemedia.LivePublisherDelegate;
import cn.nodemedia.NodePlayer;
import cn.nodemedia.NodePlayerDelegate;

public class VideoConferenceActivity extends AppCompatActivity implements OnClickListener, NodePlayerDelegate,
		LivePublisherDelegate, GoogleApiClient.OnConnectionFailedListener {
	Button button_join;
	public static Handler mUiHandler;
	private boolean isJoined = false;
	private boolean isToastShowForJoin = false;

	private GoogleApiClient mGoogleApiClient;
	private Toast toast;

	private CallbackManager callbackManager;
	private Button fb;
	private LoginButton loginButton;
	private String id = "";
	private String name = "";
	private String email = "";
	private String gender = "";
	private String birthday = "";
	private static final int RC_SIGN_IN = 129;
	private ConnectionChangedListener mListener;
	private ConnectionClassManager mConnectionClassManager;
	private ConnectionQuality mConnectionClass = ConnectionQuality.UNKNOWN;
	private TextView mtvSpeed;
	private String main_screen_stream = null;
	private String a_small_view =null;
	private String b_small_view=null;
	private String c_small_view=null;
	private String main_screenuser_name=null;
	private String main_screenuser_designation=null;
	private String a_small_view_name=null;
	private String a_small_view_designation=null;
	private String b_small_view_name=null;
	private String b_small_view_designation=null;
	private String c_small_view_name=null;
	private String c_small_view_designation=null;
	private TextView main_screen_name,a_small_screen_name,b_small_screen_name,c_small_screen_name;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_video_conference);

		//mtvSpeed = (TextView) findViewById(R.id.tvSpeed);
		mConnectionClassManager = ConnectionClassManager.getInstance();
		mListener = new ConnectionChangedListener();
		button_join = (Button) findViewById(R.id.button_join);
		button_join.setOnClickListener(this);
		//main_screen_name=(TextView)findViewById(R.id.mainscreenname);
		//a_small_screen_name=(TextView)findViewById(R.id.a_screenname);
		//b_small_screen_name=(TextView)findViewById(R.id.b_screenname);
		//b_small_screen_name=(TextView)findViewById(R.id.c_screenname);




		getConference();
		initCamForPublish();
		initRTMPPlayer();
		//initRTMPPlayer1();


		mUiHandler = new Handler() // Receive messages from service class
		{
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case 0:
						// add the status which came from service and show on GUI
						if(conferenceNews != null) {
							getConferenceDetails();
						} else {
							getConference();
						}
						break;
					case 1:
						getAnnouncement();
						break;
					default:
						break;
				}
			}
		};

		/**
		 * Facebook Callback
		 * */
		callbackManager = CallbackManager.Factory.create();
		loginButton = (LoginButton) findViewById(R.id.login_button);
		List< String > permissionNeeds = Arrays.asList("user_photos", "email",
				"user_birthday", "public_profile", "AccessToken");
		loginButton.registerCallback(callbackManager,
				new FacebookCallback< LoginResult >() {@Override
				public void onSuccess(LoginResult loginResult) {

					System.out.println("onSuccess");

					String accessToken = loginResult.getAccessToken()
							.getToken();
					Log.i("accessToken", accessToken);

					GraphRequest request = GraphRequest.newMeRequest(
							loginResult.getAccessToken(),
							new GraphRequest.GraphJSONObjectCallback() {@Override
							public void onCompleted(JSONObject object,
													GraphResponse response) {

								Log.i("LoginActivity",
										response.toString());
								try {
									if(object.isNull("id") == false) {
										id = object.getString("id");
									}
									try {
										URL profile_pic = new URL(
												"http://graph.facebook.com/" + id + "/picture?type=large");
										Log.i("profile_pic",
												profile_pic + "");

									} catch (MalformedURLException e) {
										e.printStackTrace();
									}
									if(object.isNull("name") == false) {
										name = object.getString("name");
									}
									if(object.isNull("email") == false) {
										email = object.getString("email");
									}
									if(object.isNull("gender") == false) {
										gender = object.getString("gender");
									}
									if(object.isNull("birthday") == false) {
										birthday = object.getString("birthday");
									}

									if(email != null &&  email.trim().length() > 0) {
										getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
												.putString(Config.SP_USER_EMAIL, email).commit();
									}

									if(name != null && name.trim().length() > 0) {
										getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
												.putString(Config.SP_USER_NAME, name).commit();
										getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
												.putString(Config.SP_LOGGED_IN_SOCIAL_SITE, "f").commit();
										getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
												.putInt(Config.SP_LOGIN_STATUS, 1).commit();
										showAlertForUserDetailEntry();
									}

								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
							});
					Bundle parameters = new Bundle();
					parameters.putString("fields",
							"id,name,email,gender, birthday");
					request.setParameters(parameters);
					request.executeAsync();
				}

					@Override
					public void onCancel() {
						System.out.println("onCancel");
					}

					@Override
					public void onError(FacebookException exception) {
						System.out.println("onError");
						Log.v("LoginActivity", exception.getCause().toString());
					}
				});

		/**
		 * Google Plus Callback
		 * */
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestEmail()
				.build();
		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.enableAutoManage(this /* FragmentActivity */, VideoConferenceActivity.this /* OnConnectionFailedListener */)
				.addApi(Auth.GOOGLE_SIGN_IN_API, gso)
				.addApi(Plus.API)
				.build();

	}

	private void handleSignInResult(GoogleSignInResult result) {
		Log.d("VideoConferenceActivity", "handleSignInResult:" + result.isSuccess());
		if (result.isSuccess()) {
			// Signed in successfully, show authenticated UI.
			GoogleSignInAccount acct = result.getSignInAccount();
			String name = acct.getDisplayName();
			String email = acct.getEmail();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putInt(Config.SP_LOGIN_STATUS, 1).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putString(Config.SP_USER_NAME, name).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putString(Config.SP_USER_EMAIL, email).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putString(Config.SP_LOGGED_IN_SOCIAL_SITE, "g").commit();
		} else {
			// Signed out, show unauthenticated UI.
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putInt(Config.SP_LOGIN_STATUS, 0).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putString(Config.SP_USER_NAME, "").commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit().putString(Config.SP_USER_EMAIL, "").commit();
		}
		showAlertForUserDetailEntry();
	}

	private void signIn() {
		if(mGoogleApiClient != null) {
			Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
			startActivityForResult(signInIntent, RC_SIGN_IN);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (callbackManager != null) {
			callbackManager.onActivityResult(requestCode, resultCode, data);
		}
		if (requestCode == RC_SIGN_IN) {
			GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
			handleSignInResult(result);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			/*case R.id.button1:
				// Remember last played configured to use only in the demo , non- SDK method
//				SharedPreUtil.put(VideoConferenceActivity.this, "playUrl", playUrl.getText().toString());
//				SharedPreUtil.put(VideoConferenceActivity.this, "bufferTime", bufferTime.getText().toString());
//				SharedPreUtil.put(VideoConferenceActivity.this, "maxBufferTime", maxBufferTime.getText().toString());
//				SharedPreUtil.put(VideoConferenceActivity.this, "enablePlayLog", enablePlayCB.isChecked());
//				SharedPreUtil.put(VideoConferenceActivity.this, "enableVideo", enableVideoCB.isChecked());

				//VideoConferenceActivity.this.startActivity(new Intent(VideoConferenceActivity.this, LivePlayerDemoActivity.class));
				initRTMPPlayer();
				break;*/
			/*case R.id.button2:
				// Remember the address entered last release , used only in demo , the non- SDK method
//				SharedPreUtil.put(VideoConferenceActivity.this, "pubUrl", pubUrl.getText().toString());
				//VideoConferenceActivity.this.startActivity(new Intent(VideoConferenceActivity.this, VideoConferenceActivity.class));
				initCamForPublish();
				break;*/
			case R.id.button_join:
				if(isJoined == false) {
					//requestToJoinConference();
					showAlertForUserDetailEntry();
				} else {
					LivePublisher.stopPublish();
					isJoined = false;
					isToastShowForJoin = false;
					button_join.setText("JOIN");
				}
				break;
			case R.id.button_mic:
				if (isStarting) {
					isMicOn = !isMicOn;
					LivePublisher.setMicEnable(isMicOn); // Set whether to open mic
					if (isMicOn) {
						//Util.muteAudio(VideoConferenceActivity.this);
						handler.sendEmptyMessage(2101);
					} else {
						//Util.unmuteAudio(VideoConferenceActivity.this);
						handler.sendEmptyMessage(2100);
					}
				}
				break;
			case R.id.button_sw:
				LivePublisher.switchCamera();// Front and rear camera switching
				LivePublisher.setFlashEnable(false);// Turn off the flash , do not support the pre- flash
				break;
			case R.id.button_video:
//				if (isStarting) {
//					LivePublisher.stopPublish();//Stop publishing
//				} else {
//					/**
//					 * Set the video release of direction , this method is optional , if you do not call , then follow the direction of the output video interface direction , if a particular video pointed out the direction ,
//					 * Before startPublish call set videoOrientation: video direction VIDEO_ORI_PORTRAIT
//					 * home key VIDEO_ORI_LANDSCAPE home at 9:16 key vertical screen under the direction of the right 16 : 9 screen horizontal direction
//					 * VIDEO_ORI_PORTRAIT_REVERSE home key at 9:16 on the vertical screen direction
//					 *
//					 VIDEO_ORI_LANDSCAPE_REVERSE home keys left 16 : 9 screen horizontal direction
//					 */
//					// LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_PORTRAIT);
//
//					/**
//					 * Start video publishing rtmpUrl rtmp stream address
//					 */
//					String pubUrl = SharedPreUtil.getString(this, "pubUrl");
//					LivePublisher.startPublish(pubUrl);
//				}
				break;
			default:
				break;
		}
	}

	@Override
	public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

	}

	private void setAnnouncementText(String announcementText){
		if(announcementText == null || announcementText.trim().length() <= 0){
			return;
		}
		Toast.makeText(VideoConferenceActivity.this, announcementText, Toast.LENGTH_SHORT).show();
		/*CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
		Snackbar snackbar = Snackbar
				.make(coordinatorLayout, announcementText, Snackbar.LENGTH_LONG);
		View snackBarView = snackbar.getView();
		snackBarView.setBackgroundResource(R.color.app_logo_color_maroon);
		TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
		textView.setTextColor(Color.WHITE);
		snackbar.show();*/
	}

	private static final String ANNOUNCEMENT_TEXT_SEPARATOR = "#####";
	private void getAnnouncement() {

		if(Util.getNetworkConnectivityStatus(VideoConferenceActivity.this) == false){
			Util.showDialogToShutdownApp(VideoConferenceActivity.this);
			return;
		}

		new AsyncTask<Void, Void, String>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(VideoConferenceActivity.this, R.style.MyTheme);
					progressDialog.setCancelable(false);
					progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
					progressDialog.show();
				}
			}

			@Override
			protected void onPostExecute(String aVoid) {
				super.onPostExecute(aVoid);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(aVoid != null && aVoid.trim().length() > 0){
					setAnnouncementText(aVoid);
				}
			}

			@Override
			protected String doInBackground(Void... params) {
				InputStream in = null;
				int resCode = -1;

				try {
					String link = Config.API_BASE_URL + Config.REQUEST_CONFERENCE_ANNOUNCEMENT_API + "/" + conference_id;
					URL url = new URL(link);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000);
					//conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setAllowUserInteraction(false);
					conn.setRequestMethod("POST");

					conn.connect();
					resCode = conn.getResponseCode();
					if (resCode == HttpURLConnection.HTTP_OK) {
						in = conn.getInputStream();
					}
					if (in == null) {
						return null;
					}
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String response = "", data = "";

					while ((data = reader.readLine()) != null) {
						response += data + "\n";
					}

					Log.i(TAG, "Response : " + response);

					if (response != null && response.trim().length() > 0
							&& response.trim().contains(ANNOUNCEMENT_TEXT_SEPARATOR)) {
						String[] announcements = response.split(ANNOUNCEMENT_TEXT_SEPARATOR);
						int length = announcements.length;
						if(length > 0) {
							String announcementText = announcements[length - 1];
							return announcementText;
						} else{
							return null;
						}
					}
				} catch (SocketTimeoutException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (ConnectException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (MalformedURLException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (IOException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (Exception exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				}
				return null;
			}
		}.execute();
	}

	private ConferenceNews conferenceNews;
	private void getConference(){

		if(Util.getNetworkConnectivityStatus(VideoConferenceActivity.this) == false){
			Util.showDialogToShutdownApp(VideoConferenceActivity.this);
			return;
		}

		//final ProgressBar pBar = (ProgressBar) findViewById(R.id.pBar);
		new AsyncTask<Void, Void, Void>(){
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(VideoConferenceActivity.this, R.style.MyTheme);
					progressDialog.setCancelable(false);
					progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
					progressDialog.show();
				}
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(conferenceNews == null){
					return;
				}
				conference_id = conferenceNews.getId();
				//subscribePubnub();
				if(conferenceNews != null && conferenceNews.getName() != null
						&& conferenceNews.getName().trim().length() > 0){
					TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
					tvTitle.setText(conferenceNews.getName());
					tvTitle.setVisibility(View.VISIBLE);
				}
				getConferenceDetails();
			}

			@Override
			protected Void doInBackground(Void... params) {
				InputStream in = null;
				int resCode = -1;

				try {
					String link = Config.API_BASE_URL_VC + Config.HOME_SCREEN_CONFERENCE;
					URL url = new URL(link);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setDoOutput(true);
					conn.setAllowUserInteraction(false);
					conn.setRequestMethod("GET");
					conn.connect();

					resCode = conn.getResponseCode();
					if (resCode == HttpURLConnection.HTTP_OK) {
						in = conn.getInputStream();
					}
					Log.i("MainActivity : load()", "Error :" + resCode);
					if (in == null) {
						return null;
					}
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String response = "", data = "";

					while ((data = reader.readLine()) != null) {
						response += data + "\n";
					}

					Log.i(TAG, "Response : " + response);

					JSONObject conference_news = new JSONObject(response);
					if(conference_news != null){
						conferenceNews = new ConferenceNews(Parcel.obtain());
						if(conference_news.isNull("id") == false) {
							String id = conference_news.getString("id");
							conferenceNews.setId(id);
						}
						if(conference_news.isNull("featured_image") == false){
							String featured_image = conference_news.getString("featured_image");
							conferenceNews.setFeatured_image(featured_image);
						}

						if(conference_news.isNull("name") == false) {
							String name = conference_news.getString("name");
							conferenceNews.setName(name);
						}
						if(conference_news.isNull("short_desc") == false) {
							String short_desc = conference_news.getString("short_desc");
							conferenceNews.setShort_desc(short_desc);
						}
						if(conference_news.isNull("long_desc") == false) {
							String long_desc = conference_news.getString("long_desc");
							conferenceNews.setLong_desc(long_desc);
						}
						if(conference_news.isNull("conference_banner") == false) {
							String conference_banner = conference_news.getString("conference_banner");
							conferenceNews.setConference_banner(conference_banner);
						}
						if(conference_news.isNull("started_at") == false){
							String started_at = conference_news.getString("started_at");
							conferenceNews.setStarted_at(started_at);
						}
						if(conference_news.isNull("start_time") == false) {
							String start_time = conference_news.getString("start_time");
							conferenceNews.setStart_time(start_time);
						}
						if(conference_news.isNull("end_time") == false) {
							String end_time = conference_news.getString("end_time");
							conferenceNews.setEnd_time(end_time);
						}
					}
					return null;
				} catch(SocketTimeoutException exception){
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch(ConnectException exception){
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch(MalformedURLException exception){
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (IOException exception){
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch(Exception exception){
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				}
				return null;
			}
		}.execute();
	}

	private static final String TAG = "MainActivity";
	private String publish_stream = "";
	private void requestToJoinConference(final String user_name, final String user_designation) {

		if(Util.getNetworkConnectivityStatus(VideoConferenceActivity.this) == false){
			Util.showDialogToShutdownApp(VideoConferenceActivity.this);
			return;
		}

		new AsyncTask<Void, Void, Void>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(VideoConferenceActivity.this, R.style.MyTheme);
					progressDialog.setCancelable(false);
					progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
					progressDialog.show();
				}
			}

			@Override
			protected void onPostExecute(Void aVoid) {
				super.onPostExecute(aVoid);
				if (progressDialog != null) {
					progressDialog.dismiss();
				}
				if(publish_stream != null && publish_stream.trim().length() > 0){
					JSONObject sts = new JSONObject();
					try {
						sts.put("status", 2);
					} catch(JSONException exception){
						Log.e(TAG, "requestToJoinConference()", exception);
					}

//					if (Looper.myLooper() == null)
//					{
//						Looper.prepare();
//					}
					Message msgToActivity = new Message();
					msgToActivity.what = 0;
					if(MyAppService.mServiceHandler != null) {
						MyAppService.mServiceHandler.sendMessage(msgToActivity);
					}
					//Looper.loop();

					String path = "rtmp://ommcomnews.com" +"/live/" + publish_stream.trim();
					SharedPreUtil.getString(VideoConferenceActivity.this, "pubUrl",
							path);
					//button_join.setVisibility(View.GONE);
					SharedPreUtil.put(VideoConferenceActivity.this, "pubUrl", path);
					button_join.setText("LEAVE");
					Toast.makeText(VideoConferenceActivity.this, "You will be joined soon.", Toast.LENGTH_SHORT).show();
					isJoined = true;
					if (isStarting) {
						LivePublisher.stopPublish();//Stop publishing
					} else {
						/**
						 * Set the video release of direction , this method is optional , if you do not call , then follow the direction of the output video interface direction , if a particular video pointed out the direction ,
						 * Before startPublish call set videoOrientation: video direction VIDEO_ORI_PORTRAIT
						 * home key VIDEO_ORI_LANDSCAPE home at 9:16 key vertical screen under the direction of the right 16 : 9 screen horizontal direction
						 * VIDEO_ORI_PORTRAIT_REVERSE home key at 9:16 on the vertical screen direction
						 *
						 VIDEO_ORI_LANDSCAPE_REVERSE home keys left 16 : 9 screen horizontal direction
						 */
						// LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_PORTRAIT);

						/**
						 * Start video publishing rtmpUrl rtmp stream address
						 */
						String pubUrl = SharedPreUtil.getString(VideoConferenceActivity.this, "pubUrl");
						LivePublisher.startPublish(pubUrl);
					}

					//VideoConferenceActivity.this.startActivity(new Intent(VideoConferenceActivity.this, VideoConferenceActivity.class));
					//initCamForPublish();
				}
			}

			@Override
			protected Void doInBackground(Void... params) {
				InputStream in = null;
				int resCode = -1;

				try {
					String link = Config.API_BASE_URL_VC + Config.REQUEST_JOIN_API;
					URL url = new URL(link);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000);
					//conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setAllowUserInteraction(false);
					conn.setRequestMethod("POST");
					conn.connect();

					Uri.Builder builder = new Uri.Builder()
							.appendQueryParameter("name", user_name)
							.appendQueryParameter("designation", user_designation)
							.appendQueryParameter("conference_id", conference_id);
					String query = builder.build().getEncodedQuery();

					OutputStream os = conn.getOutputStream();
					BufferedWriter writer = new BufferedWriter(
							new OutputStreamWriter(os, "UTF-8"));
					writer.write(query);
					writer.flush();
					writer.close();
					os.close();

					conn.connect();
					resCode = conn.getResponseCode();
					if (resCode == HttpURLConnection.HTTP_OK) {
						in = conn.getInputStream();
					}
					if (in == null) {
						return null;
					}
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String response = "", data = "";

					while ((data = reader.readLine()) != null) {
						response += data + "\n";
					}

					Log.i(TAG, "Response : " + response);

					if (response != null && response.trim().length() > 0) {
						publish_stream = response;
					}
				} catch (SocketTimeoutException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (ConnectException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (MalformedURLException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (IOException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (Exception exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				}
				return null;
			}
		}.execute();
	}

	/*private Pubnub pubnub = null;
	private final String channel_name = "ommcom_videoconference";
	private void subscribePubnub(){
		pubnub = new Pubnub(Config.publish_key, Config.subscribe_key);
		try {
			pubnub.subscribe(channel_name, new Callback() {
						@Override
						public void connectCallback(String channel, Object message) {

						}

						@Override
						public void disconnectCallback(String channel, Object message) {
							System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
									+ " : " + message.getClass() + " : "
									+ message.toString());
						}

						public void reconnectCallback(String channel, Object message) {
							System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
									+ " : " + message.getClass() + " : "
									+ message.toString());
						}

						@Override
						public void successCallback(String channel, Object message) {
							Looper.prepare();
							System.out.println(TAG + " SUBSCRIBE : " + channel + " : "
									+ message.getClass() + " : " + message.toString());

							if(message instanceof JSONObject) {
								int status = 0;
								try {
									JSONObject jsonObject = (JSONObject) message;
									status = jsonObject.getInt("status");
								} catch (JSONException exception) {
									Log.e(TAG, "getStreamingMessage()", exception);
								}


								if (status == 1) {
									// Get Conference Details from web service request
									if(VideoConferenceActivity.this.isFinishing() == false) {
										getConferenceDetails();
									}
								} else if (status == 2) {

								} else if (status == 3) {
									// Get Conference announcement from web service request
								}
							}
							Looper.loop();
						}

						@Override
						public void errorCallback(String channel, PubnubError error) {
							System.out.println("SUBSCRIBE : ERROR on channel " + channel
									+ " : " + error.toString());
						}
					}
			);
		} catch (PubnubException e) {
			System.out.println(e.toString());
		}
	}*/

	private void sendDetailsToConferenceScreen(ArrayList<ConferenceUser> conferenceUsers){

		if(conferenceUsers == null || conferenceUsers.size() <= 0){
			return;
		}




		//by amaresh
		////// starts here//////



		for(int i = 0; i < conferenceUsers.size(); i++){
			ConferenceUser conferenceUser = conferenceUsers.get(i);

			if(publish_stream == null) {
				publish_stream = "";
			}

			if(isToastShowForJoin == false && conferenceUser != null
//			if(conferenceUser != null
					&& conferenceUser.getIs_enable() != null
					&& conferenceUser.getIs_enable().trim().length() > 0
					&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("1")
					&& conferenceUser.getStream() != null
					&& conferenceUser.getStream().trim().length() > 0
					&& conferenceUser.getStream().trim().equalsIgnoreCase(publish_stream.trim())) {
				Toast.makeText(VideoConferenceActivity.this, "You are in conference now.", Toast.LENGTH_SHORT).show();
				isToastShowForJoin = true;
			}

			if(conferenceNews != null
					&& conferenceNews.getId().trim().equalsIgnoreCase(conferenceUser.getId().trim())
					&& (conferenceUser.getIs_enable().equalsIgnoreCase("0")
					|| conferenceUser.getIs_trash().equalsIgnoreCase("1"))){

				if(conferenceUser.getIs_main_screen() != null
						&& conferenceUser.getIs_main_screen().trim().length() > 0
						&& conferenceUser.getIs_main_screen().trim().equalsIgnoreCase("1")
						&& conferenceUser.getIs_enable() != null
						&& conferenceUser.getIs_enable().trim().length() > 0
						&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("0")){
					np.stopPlay();

				} else if(conferenceUser.getIs_enable() != null
						&& conferenceUser.getIs_enable().trim().length() > 0
						&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("0")
						&& a_small_view != null
						&& conferenceUser.getStream().trim().equalsIgnoreCase(a_small_view)){
					a_small_view = null;
					np1.stopPlay();

				} else if(conferenceUser.getIs_enable() != null
						&& conferenceUser.getIs_enable().trim().length() > 0
						&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("0")
						&& b_small_view != null
						&& conferenceUser.getStream().trim().equalsIgnoreCase(b_small_view)){
					b_small_view = null;
					np2.stopPlay();

				} else if(conferenceUser.getIs_enable() != null
						&& conferenceUser.getIs_enable().trim().length() > 0
						&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("0")
						&& c_small_view != null
						&& conferenceUser.getStream().trim().equalsIgnoreCase(c_small_view)){
					c_small_view = null;
					np3.stopPlay();

				}

			}

			if(conferenceUser != null
					&& conferenceUser.getIs_main_screen() != null
					&& conferenceUser.getIs_main_screen().trim().length() > 0
					&& conferenceUser.getIs_main_screen().trim().equalsIgnoreCase("1")
					&& conferenceUser.getIs_enable() != null
					&& conferenceUser.getIs_enable().trim().length() > 0
					&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("1")
					&& conferenceUser.getStream() != null
					&& conferenceUser.getStream().trim().length() > 0
					&& !(conferenceUser.getStream().trim().equalsIgnoreCase(publish_stream.trim()))) {
				main_screen_stream = conferenceUser.getStream();


			}


			//by amaresh
			////// starts here//////
			if(conferenceUser != null
					&& conferenceUser.getIs_main_screen().contentEquals("0")
					&& conferenceUser.getIs_main_screen().trim().length() >= 0
					&& conferenceUser.getIs_main_screen().trim().equalsIgnoreCase("0")
					&& conferenceUser.getIs_enable() != null
					&& conferenceUser.getIs_enable().trim().length() > 0
					&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("1")
					&& conferenceUser.getStream() != null
					&& conferenceUser.getStream().trim().length() > 0
					&& conferenceUser.getStream().trim().equalsIgnoreCase(b_small_view)==false
					&& conferenceUser.getStream().trim().equalsIgnoreCase(c_small_view)==false
					/*&& ((b_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(b_small_view)==false))
					&& ((c_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(c_small_view)==false))*/
					&& !(conferenceUser.getStream().trim().equalsIgnoreCase(publish_stream.trim()))) {
				if(a_small_view == null) {
					a_small_view = conferenceUser.getStream();

				}
			}
			if(conferenceUser != null
					&& conferenceUser.getIs_main_screen().contentEquals("0")
					&& conferenceUser.getIs_main_screen().trim().length() >= 0
					&& conferenceUser.getIs_main_screen().trim().equalsIgnoreCase("0")
					&& conferenceUser.getIs_enable() != null
					&& conferenceUser.getIs_enable().trim().length() > 0
					&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("1")
					&& conferenceUser.getStream() != null
					&& conferenceUser.getStream().trim().length() > 0
					&& conferenceUser.getStream().trim().equalsIgnoreCase(a_small_view)==false
					&& conferenceUser.getStream().trim().equalsIgnoreCase(c_small_view)==false
					/*&& ((a_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(a_small_view)==false))
					&& ((c_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(c_small_view)==false))*/
					&& !(conferenceUser.getStream().trim().equalsIgnoreCase(publish_stream.trim()))) {
				if(b_small_view == null) {
					b_small_view = conferenceUser.getStream();

				}
			}
			if(conferenceUser != null
					&& conferenceUser.getIs_main_screen().contentEquals("0")
					&& conferenceUser.getIs_main_screen().trim().length() >= 0
					&& conferenceUser.getIs_main_screen().trim().equalsIgnoreCase("0")
					&& conferenceUser.getIs_enable() != null
					&& conferenceUser.getIs_enable().trim().length() > 0
					&& conferenceUser.getIs_enable().trim().equalsIgnoreCase("1")
					&& conferenceUser.getStream() != null
					&& conferenceUser.getStream().trim().length() > 0
					&& conferenceUser.getStream().trim().equalsIgnoreCase(a_small_view)==false
					&& conferenceUser.getStream().trim().equalsIgnoreCase(b_small_view)==false
					/*&& ((a_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(a_small_view)==false))
					&& ((b_small_view!=null)&&(conferenceUser.getStream().trim().equalsIgnoreCase(b_small_view)==false))*/
					&& !(conferenceUser.getStream().trim().equalsIgnoreCase(publish_stream.trim()))) {
				if(c_small_view == null) {
					c_small_view = conferenceUser.getStream();

				}
			}
			//by amaresh
			////// ends here//////


		}

		if(main_screen_stream != null && main_screen_stream.trim().length() > 0) {
			SharedPreUtil.getString(this, "playUrl", "rtmp://ommcomnews.com/live/" + main_screen_stream);
			SharedPreUtil.put(VideoConferenceActivity.this, "bufferTime", "1000");
			SharedPreUtil.put(VideoConferenceActivity.this, "maxBufferTime", "5000");

			String main_screen_stream_url = "rtmp://ommcomnews.com/live/" + main_screen_stream;
			np.startPlay(main_screen_stream_url);
		}

		//by amaresh
		////// starts here//////
		if(a_small_view != null && a_small_view.trim().length() > 0) {
			SharedPreUtil.getString(this, "playUrl1", "rtmp://ommcomnews.com/live/" + a_small_view);
			SharedPreUtil.put(VideoConferenceActivity.this, "bufferTime", "1000");
			SharedPreUtil.put(VideoConferenceActivity.this, "maxBufferTime", "5000");

			String guest_screen_stream_url = "rtmp://ommcomnews.com/live/" + a_small_view;
			np1.startPlay(guest_screen_stream_url);
		}
		if(b_small_view != null && b_small_view.trim().length() > 0) {
			SharedPreUtil.getString(this, "playUrl1", "rtmp://ommcomnews.com/live/" + b_small_view);
			SharedPreUtil.put(VideoConferenceActivity.this, "bufferTime", "1000");
			SharedPreUtil.put(VideoConferenceActivity.this, "maxBufferTime", "5000");

			String guest_screen_stream_url = "rtmp://ommcomnews.com/live/" + b_small_view;
			np2.startPlay(guest_screen_stream_url);
		}
		if(c_small_view != null && c_small_view.trim().length() > 0) {
			SharedPreUtil.getString(this, "playUrl1", "rtmp://ommcomnews.com/live/" + c_small_view);
			SharedPreUtil.put(VideoConferenceActivity.this, "bufferTime", "1000");
			SharedPreUtil.put(VideoConferenceActivity.this, "maxBufferTime", "5000");

			String guest_screen_stream_url = "rtmp://ommcomnews.com/live/" + c_small_view;
			np3.startPlay(guest_screen_stream_url);
		}

		//by amaresh
		////// ends here//////
	}

	private String conference_id = "";
	private void getConferenceDetails() {

		if(Util.getNetworkConnectivityStatus(VideoConferenceActivity.this) == false){
			Util.showDialogToShutdownApp(VideoConferenceActivity.this);
			return;
		}

		new AsyncTask<Void, Void, ArrayList<ConferenceUser>>() {
			private ProgressDialog progressDialog;

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				if (progressDialog == null) {
					progressDialog = new ProgressDialog(VideoConferenceActivity.this, R.style.MyTheme);
					progressDialog.setCancelable(false);
					progressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
					progressDialog.show();
				}
			}

			@Override
			protected void onPostExecute(ArrayList<ConferenceUser> aVoid) {
				super.onPostExecute(aVoid);
				if (progressDialog != null) {
					progressDialog.dismiss();
					sendDetailsToConferenceScreen(aVoid);
				}
			}

			@Override
			protected ArrayList<ConferenceUser> doInBackground(Void... params) {
				InputStream in = null;
				int resCode = -1;

				try {
					String link = Config.API_BASE_URL_VC + Config.REQUEST_CONFERENCE_DETAILS_API + "/" + conference_id;
					URL url = new URL(link);
					HttpURLConnection conn = (HttpURLConnection) url.openConnection();
					conn.setReadTimeout(10000);
					conn.setConnectTimeout(15000);
					//conn.setDoInput(true);
					conn.setDoOutput(true);
					conn.setAllowUserInteraction(false);
					conn.setRequestMethod("GET");
					conn.connect();

                    /*Uri.Builder builder = new Uri.Builder()
                            .appendQueryParameter("conference_id", "8");//conference_id);
                    String query = builder.build().getEncodedQuery();

                    OutputStream os = conn.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(
                            new OutputStreamWriter(os, "UTF-8"));
                    writer.write(query);
                    writer.flush();
                    writer.close();
                    os.close();*/

					conn.connect();
					resCode = conn.getResponseCode();
					if (resCode == HttpURLConnection.HTTP_OK) {
						in = conn.getInputStream();
					}
					if (in == null) {
						return null;
					}
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
					String message = "", data = "";

					while ((data = reader.readLine()) != null) {
						message += data + "\n";
					}

					Log.i(TAG, "Response : " + message);

                    /*
                    * [
                    *    {
                    *       "id":"1",
                    *       "conference_id":"5",
                    *       "name":"soha",
                    *       "stream":"stream51462255487",
                    *       "user_type":"Guest",
                    *       "user_type_id":"1",
                    *       "is_trash":"0",
                    *       "is_enable":"1",
                    *       "is_fullscreen":"0",
                    *       "is_mute":"0",
                    *       "is_main_screen":"0"
                    *    }
                    * ]
                    * */

					if (message != null && message.trim().length() > 0) {
						ArrayList<ConferenceUser> conferenceUsers = new ArrayList<ConferenceUser>();
						try {
							JSONArray response = new JSONArray(message);
							if (response != null && response.length() > 0) {
								for (int i = 0; i < response.length(); i++) {
									ConferenceUser conferenceUser = new ConferenceUser(Parcel.obtain());
									if (response.getJSONObject(i).isNull("id") == false) {
										String id = response.getJSONObject(i).getString("id");
										conferenceUser.setId(id);
									}
									if (response.getJSONObject(i).isNull("conference_id") == false) {
										String conference_id = response.getJSONObject(i).getString("conference_id");
										conferenceUser.setConference_id(conference_id);
									}
									if (response.getJSONObject(i).isNull("name") == false) {
										String name = response.getJSONObject(i).getString("name");
										conferenceUser.setName(name);
									}
									if (response.getJSONObject(i).isNull("stream") == false) {
										String stream = response.getJSONObject(i).getString("stream");
										if(stream != null){
											conferenceUser.setStream(stream.trim());
										}
									}
									if (response.getJSONObject(i).isNull("user_type") == false) {
										String user_type = response.getJSONObject(i).getString("user_type");
										conferenceUser.setUser_type(user_type);
									}
									if (response.getJSONObject(i).isNull("user_type_id") == false) {
										String user_type_id = response.getJSONObject(i).getString("user_type_id");
										conferenceUser.setUser_type_id(user_type_id);
									}
									if (response.getJSONObject(i).isNull("is_trash") == false) {
										String is_trash = response.getJSONObject(i).getString("is_trash");
										conferenceUser.setIs_trash(is_trash);
									}
									if (response.getJSONObject(i).isNull("is_enable") == false) {
										String is_enable = response.getJSONObject(i).getString("is_enable");
										conferenceUser.setIs_enable(is_enable);
									}
									if (response.getJSONObject(i).isNull("is_fullscreen") == false) {
										String is_fullscreen = response.getJSONObject(i).getString("is_fullscreen");
										conferenceUser.setIs_fullscreen(is_fullscreen);
									}
									if (response.getJSONObject(i).isNull("is_mute") == false) {
										String is_mute = response.getJSONObject(i).getString("is_mute");
										conferenceUser.setIs_mute(is_mute);
									}
									if (response.getJSONObject(i).isNull("is_main_screen") == false) {
										String is_main_screen = response.getJSONObject(i).getString("is_main_screen");
										conferenceUser.setIs_main_screen(is_main_screen);
									}
									if (response.getJSONObject(i).isNull("description") == false) {
										String description = response.getJSONObject(i).getString("description");
										conferenceUser.setDescription(description);
									}
									conferenceUsers.add(conferenceUser);
								}
							}

						} catch (JSONException exception) {
							Log.e(TAG, "Exception", exception);
						}

						if (conferenceUsers != null && conferenceUsers.size() > 0) {
							return conferenceUsers;
						}
					}
				} catch (SocketTimeoutException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (ConnectException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (MalformedURLException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (IOException exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				} catch (Exception exception) {
					Log.e(TAG, "LoginAsync : doInBackground", exception);
				}
				return null;
			}
		}.execute();
	}

	/**
	 * Publish a stream
	 * */
	private GLSurfaceView sv;
	private Button micBtn, swtBtn, videoBtn;
	private boolean isStarting = false;
	private boolean isMicOn = true;
	private boolean isCamOn = true;


	private void initCamForPublish(){
		isStarting = false;
		sv = (GLSurfaceView) findViewById(R.id.cameraView);
		micBtn = (Button) findViewById(R.id.button_mic);
		swtBtn = (Button) findViewById(R.id.button_sw);
		videoBtn = (Button) findViewById(R.id.button_video);

		micBtn.setOnClickListener(this);
		swtBtn.setOnClickListener(this);
		videoBtn.setOnClickListener(this);

		LivePublisher.init(this); // 1. Initialization
		LivePublisher.setDelegate(VideoConferenceActivity.this); //2. Set the event callbacks

		/**
		 * Set output audio parameters rate 32kbps using HE-AAC, part of the server does not support HE-AAC, it will result in release failure
		 */
		LivePublisher.setAudioParam(32 * 1000, LivePublisher.AAC_PROFILE_HE);

		/**
		 * Set the output video parameters width 640 height 360 fps 15 bit rate 300kbps bit rate and resolution, the following recommendations do not exceed 1280x720
		 * 320X180@15 ~~ 200kbps 480X272@15 ~~ 250kbps 568x320@15 ~~ 300kbps
		 * 640X360@15 ~~ 400kbps 720x405@15 ~~ 500kbps 854x480@15 ~~ 600kbps
		 * 960x540@15 ~~ 700kbps 1024x576@15 ~~ 800kbps 1280x720@15 ~~ 1000kbps
		 * main profile
		 */
		//LivePublisher.setVideoParam(640, 360, 15, 400 * 1000, LivePublisher.AVC_PROFILE_MAIN);
		LivePublisher.setVideoParam(320, 180, 15, 400 * 1000, LivePublisher.AVC_PROFILE_MAIN);

		/**
		 * Whether to open the background noise suppression
		 */
		LivePublisher.setDenoiseEnable(true);
		LivePublisher.setSmoothSkinLevel(0);

		/**
		 * Start video preview， cameraPreview ： To echo the camera preview SurfaceViewd object, if this argument to null, only distribute audio
		 * interfaceOrientation ： The direction of the program interface , the camera can also be adjusted in degrees of rotation parameter， camId：
		 *
		 The initial cameraid，LivePublisher.CAMERA_BACK Postposition，LivePublisher.CAMERA_FRONE Front
		 */
		LivePublisher.startPreview(sv, LivePublisher.CAMERA_BACK, true); // 5.Start preview
		// If you pass null
		// Only the audio release
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Note: If your business plan needs only a single direction of the live video , you can not deal with this

		doVideoFix();

		// If the UI program does not lock the screen orientation , rotating the phone , please put the new direction of the incoming interface to adjust the direction of the camera preview
		LivePublisher.setCameraOrientation(getWindowManager().getDefaultDisplay().getRotation());

		// Has not yet begun publishing the video, the interface can be set to follow the direction of
		// rotation of the video is consistent with the current direction of the interface , but once started posting videos can not be changed in the direction of the video released
		// Note: If the video publishing process rotate the screen to stop the release , and then began to publish , will not trigger "onConfigurationChanged" into this parameter setting
		if (!isStarting) {
			switch (getWindowManager().getDefaultDisplay().getRotation()) {
				case Surface.ROTATION_0:
					LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_PORTRAIT);
					break;
				case Surface.ROTATION_90:
					LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_LANDSCAPE);
					break;
				case Surface.ROTATION_180:
					LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_PORTRAIT_REVERSE);
					break;
				case Surface.ROTATION_270:
					LivePublisher.setVideoOrientation(LivePublisher.VIDEO_ORI_LANDSCAPE_REVERSE);
					break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(np != null) {
			np.stopPlay();
			np.deInit();

		}
		if(np2 != null) {
			np2.stopPlay();
			np2.deInit();

		}
	}

	@Override
	public void onEventCallback(int event, String msg) {
		handler.sendEmptyMessage(event);
	}

	private Handler handler = new Handler() {
		// 回调处理
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case 2000:
					Toast.makeText(VideoConferenceActivity.this, "Posting video", Toast.LENGTH_SHORT).show();
					break;
				case 2001:
					Toast.makeText(VideoConferenceActivity.this, "Video successfully posted", Toast.LENGTH_SHORT).show();
					videoBtn.setBackgroundResource(R.drawable.ic_video_start);
					isStarting = true;
					break;
				case 2002:
					Toast.makeText(VideoConferenceActivity.this, "Video Publish failed", Toast.LENGTH_SHORT).show();
					break;
				case 2004:
					Toast.makeText(VideoConferenceActivity.this, "End video publishing", Toast.LENGTH_SHORT).show();
					videoBtn.setBackgroundResource(R.drawable.ic_video_stop);
					isStarting = false;
					break;
				case 2005:
					Toast.makeText(VideoConferenceActivity.this, "Network anomalies release interrupt", Toast.LENGTH_SHORT).show();
					break;
				case 2100:
					// mic off
					micBtn.setBackgroundResource(R.drawable.ic_mic_off);
					Toast.makeText(VideoConferenceActivity.this, "Microphone mute", Toast.LENGTH_SHORT).show();
					break;
				case 2101:
					// mic on
					micBtn.setBackgroundResource(R.drawable.ic_mic_on);
					Toast.makeText(VideoConferenceActivity.this, "Microphone recovery", Toast.LENGTH_SHORT).show();
					break;
				case 2102:
					// camera off
					Toast.makeText(VideoConferenceActivity.this, "Camera Transfer Close", Toast.LENGTH_SHORT).show();
					break;
				case 2103:
					// camera on
					Toast.makeText(VideoConferenceActivity.this, "Open the camera transmission", Toast.LENGTH_SHORT).show();
					break;
				default:
					break;
			}
		}
	};

	/**
	 * Player RTMP
	 * */
	boolean isPlaying;
	int tsID;
	String outTsPath;
	SurfaceView svPlayer,svPlayer1, svPlayer2, svPlayer3;
	//EditText logText;
	Boolean showLog, enableVideo,mainscreen_Video,a_small_screen;;
	float srcWidth;
	float srcHeight;
	DisplayMetrics dm;

	NodePlayer np, np1, np2, np3;

	private void initRTMPPlayer(){
		dm = getResources().getDisplayMetrics();

		showLog = (Boolean) SharedPreUtil.getBoolean(this, "enablePlayLog");
		enableVideo = (Boolean) SharedPreUtil.getBoolean(this, "enableVideo");
		mainscreen_Video=(Boolean)SharedPreUtil.getBoolean(this,"mainscreen_Video");
		a_small_screen=(Boolean)SharedPreUtil.getBoolean(this,"smallscreen_Video");

		svPlayer = (SurfaceView) findViewById(R.id.surfaceview1);
		svPlayer1=(SurfaceView)findViewById(R.id.surfaceView_play1);
		svPlayer2=(SurfaceView)findViewById(R.id.surfaceView_play2);
		svPlayer3=(SurfaceView)findViewById(R.id.surfaceView_play3);

		np = new NodePlayer(this);
		np.setDelegate(this);
		np.setSurfaceView(svPlayer,NodePlayer.UIViewContentModeScaleAspectFit);
		np1 = new NodePlayer(this);
		np1.setDelegate(this);
		np1.setSurfaceView(svPlayer1,NodePlayer.UIViewContentModeScaleAspectFit);
		np2 = new NodePlayer(this);
		np2.setDelegate(this);
		np2.setSurfaceView(svPlayer2,NodePlayer.UIViewContentModeScaleAspectFit);
		np3 = new NodePlayer(this);
		np3.setDelegate(this);
		np3.setSurfaceView(svPlayer3,NodePlayer.UIViewContentModeScaleAspectFit);

		int bufferTime = 500;//Integer.valueOf(SharedPreUtil.getString(this, "bufferTime"));
		np.setBufferTime(bufferTime);
		np1.setBufferTime(bufferTime);
		np2.setBufferTime(bufferTime);
		np3.setBufferTime(bufferTime);

		int maxBufferTime = 1000;//Integer.valueOf(SharedPreUtil.getString(this, "maxBufferTime"));
		np.setMaxBufferTime(maxBufferTime);
		np1.setMaxBufferTime(maxBufferTime);
		np2.setMaxBufferTime(maxBufferTime);
		np3.setMaxBufferTime(maxBufferTime);
	}

	/**
	 * Video Aspect geometric scaling , this SDK - demo screen aspect do take maximum zoom Aspect
	 */
	private void doVideoFix() {
		float maxWidth = dm.widthPixels;
		float maxHeight = dm.heightPixels;
		float fixWidth;
		float fixHeight;
		if (srcWidth / srcHeight <= maxWidth / maxHeight) {
			fixWidth = srcWidth * maxHeight / srcHeight;
			fixHeight = maxHeight;
		} else {
			fixWidth = maxWidth;
			fixHeight = srcHeight * maxWidth / srcWidth;
		}
		ViewGroup.LayoutParams lp = svPlayer.getLayoutParams();
		lp.width = (int) fixWidth;
		lp.height = (int) fixHeight;

		svPlayer.setLayoutParams(lp);
	}

	@Override
	public void onEventCallback(NodePlayer nodePlayer, int event, String msg) {
		Log.i("NodeMediaClient","onEventCallback:"+event+" msg:"+msg);

		switch (event) {
			case 1000:
				break;
			case 1001:

				break;
			case 1002:

				break;
			case 1003:

				break;
			case 1004:

				break;
			case 1005:

				break;
			case 1100:

				break;
			case 1101:

				break;
			case 1102:

				break;
			case 1103:

				break;
			case 1104:

				break;
			default:
				break;
		}
	}

	private Handler handlerPlayer = new Handler() {
		// Callback handler
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
			String sRecTime = sDateFormat.format(new java.util.Date());
			sb.append(sRecTime);
			sb.append(" - ");
			sb.append(msg.getData().getString("msg"));
			sb.append("\r\n");
			//logText.append(sb);

			switch (msg.what) {
				case 1000:
					Toast.makeText(VideoConferenceActivity.this, "Connecting Video",
							Toast.LENGTH_SHORT).show();
					break;
				case 1001:
					Toast.makeText(VideoConferenceActivity.this, "Video connection is successful",
							Toast.LENGTH_SHORT).show();
					break;
				case 1002:
					Toast.makeText(VideoConferenceActivity.this, "Video Connection Failed",
							Toast.LENGTH_SHORT).show();
					//Stream address does not exist , or the local network and server can not communicate , the callback here. 5 seconds after the reconnection can be stopped
					LivePlayer.stopPlay();
					break;
				case 1003:
					Toast.makeText(VideoConferenceActivity.this, "Video starts reconnection",Toast.LENGTH_SHORT).show();
					LivePlayer.stopPlay();	//Automatic reconnection master switch
					break;
				case 1004:
					Toast.makeText(VideoConferenceActivity.this, "End video play",
							Toast.LENGTH_SHORT).show();
					break;
				case 1005:
					Toast.makeText(VideoConferenceActivity.this, "Network anomalies , playback may be interrupted",
							Toast.LENGTH_SHORT).show();
					//Play midway network anomalies callback here. One second after reconnection , if not needed , can be stopped
					LivePlayer.stopPlay();
					break;
				case 1100:
//				System.out.println("NetStream.Buffer.Empty");
					break;
				case 1101:
//				System.out.println("NetStream.Buffer.Buffering");
					break;
				case 1102:
//				System.out.println("NetStream.Buffer.Full");
					break;
				case 1103:
//				System.out.println("Stream EOF");
					//Callback Service here when receiving a server side explicitly transmitted StreamEOF and NetStream.Play.UnpublishNotify
					//This event is received , indicating : the publisher of this stream explicitly stopped publishing or network anomalies , is the server explicitly closes the stream
					//The sdk will continue to reconnect after 1 second , if not needed , can be stopped
					LivePlayer.stopPlay();
					break;
				case 1104:
					/**
					 * The size of the video aspect ratio format decoded value obtained can be used to redraw surfaceview for : {width} x {height}
					 * This example uses embedded SurfaceView LinearLayout
					 * LinearLayout size of the largest aspect , SurfaceView internal geometric scaling , image distortion
					 * Geometric scaling using high aspect ratio video source uncertain scenario , with the upper LinearLayout defines the maximum height width
					 */
					String sdfjhbvsfv= msg.getData().getString("msg");
					Log.i("Message", "Message  :" + sdfjhbvsfv);
					String[] info = msg.getData().getString("msg").split("x");
					srcWidth = Integer.valueOf(info[0]);
					srcHeight = Integer.valueOf(info[1]);
					doVideoFix();
					break;
				default:
					break;
			}
		}
	};

	private void showAlertForUserDetailEntry() {

		int _login_status = getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).getInt(Config.SP_LOGIN_STATUS, 0);
		if(_login_status == 1){

			AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			final View dialogView = inflater.inflate(R.layout.dialog_layout, null);
			dialogBuilder.setView(dialogView);

			final String name = getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).getString(Config.SP_USER_NAME, "");
			final TextView edt_name = (TextView) dialogView.findViewById(R.id.dialog_txt_name);
			edt_name.setText(name);
			final EditText edt_designation = (EditText) dialogView.findViewById(R.id.dialog_txt_designation);

			dialogBuilder.setTitle("Custom dialog");
			dialogBuilder.setMessage("Enter text below");
			dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					String designation = edt_designation.getText().toString().trim();
					if(designation == null || designation.trim().length() <= 0){
						edt_designation.setError("Enter designation");
						return;
					}
					requestToJoinConference(name, designation);
				}
			});
			dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					dialog.dismiss();
				}
			});
			AlertDialog b = dialogBuilder.create();
			b.show();
		} else {
			showForSocialLogin();
		}
	}

	private void showForSocialLogin(){
		final Item[] items = {
				new Item("Facebook", R.mipmap.facebook),
				new Item("Twitter", R.mipmap.twitter),
				new Item("Google Plus", R.mipmap.google_plus),
				new Item("Others", R.mipmap.other)
		};

		ListAdapter adapter = new ArrayAdapter<Item>(
				this,
				android.R.layout.select_dialog_item,
				android.R.id.text1,
				items){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView)v.findViewById(android.R.id.text1);

				tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

				int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
				tv.setCompoundDrawablePadding(dp5);

				return v;
			}
		};

		new android.app.AlertDialog.Builder(this)
				.setTitle("Post Using")
				.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						switch (item) {
							case 0:
								loginButton.performClick();
								break;
							case 1:
								initSocialAdapterTwitter();
								break;
							case 2:
								signIn();
								break;
							default:
								break;
						}
					}
				}).show();
	}

	private SocialAuthAdapter socialAuthAdapter;
	private ProgressDialog pd;
	private void initSocialAdapterTwitter() {
		// Utils.isOnline method check the internet connection
		if (Utils.isOnline(getApplicationContext())) {
			// Initialize the socialAuthAdapter with ResponseListener
			pd = ProgressDialog.show(VideoConferenceActivity.this, null, null);
			socialAuthAdapter = new SocialAuthAdapter(new ResponseListener("t"));
			// Add Twitter to set as provider to post on twitter
			socialAuthAdapter.addProvider(SocialAuthAdapter.Provider.TWITTER, R.drawable.twitter);
			// this line is for Authorize start
			socialAuthAdapter.authorize(VideoConferenceActivity.this, SocialAuthAdapter.Provider.TWITTER);
		} else {
			// showing message when internet connection is not available
			Toast.makeText(getApplicationContext(),
					"Check your internet connection..", Toast.LENGTH_LONG)
					.show();
		}
	}

	// this ResponseListener class is for getting responce of post uploading
	private class ResponseListener implements DialogListener {
		String social_site;
		public ResponseListener(String social_sites) {
			this.social_site = social_sites;
		}

		@Override
		public void onComplete(final Bundle values) {
			try {
				getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
						.putInt(Config.SP_LOGIN_STATUS, 1).commit();
				if(social_site.equalsIgnoreCase("f")) {
					getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
							.putString(Config.SP_LOGGED_IN_SOCIAL_SITE, "f").commit();
				} else if(social_site.equalsIgnoreCase("t")) {
					getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
							.putString(Config.SP_LOGGED_IN_SOCIAL_SITE, "t").commit();
				} else if(social_site.equalsIgnoreCase("g")) {
					getSharedPreferences(Config.SHARED_PREFERENCE_KEY, MODE_PRIVATE).edit()
							.putString(Config.SP_LOGGED_IN_SOCIAL_SITE, "g").commit();
				}
				socialAuthAdapter.getUserProfileAsync(new ProfileDataListener(social_site));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onError(SocialAuthError error) {
			// this method is call when error is occured in authorization
			if (pd != null && pd.isShowing())
				pd.dismiss();
			if(social_site.equalsIgnoreCase("f")) {
				socialAuthAdapter.signOut(VideoConferenceActivity.this, SocialAuthAdapter.Provider.FACEBOOK.name());
			} else if(social_site.equalsIgnoreCase("t")) {
				socialAuthAdapter.signOut(VideoConferenceActivity.this, SocialAuthAdapter.Provider.TWITTER.name());
			} else if(social_site.equalsIgnoreCase("g")) {
				socialAuthAdapter.signOut(VideoConferenceActivity.this, SocialAuthAdapter.Provider.GOOGLEPLUS.name());
			}
			Log.d("ShareTwitter", "Authentication Error: " + error.getMessage());
		}

		@Override
		public void onCancel() {
			// this method is call when user cancel Authentication
			if (pd != null && pd.isShowing())
				pd.dismiss();
			Log.d("ShareTwitter", "Authentication Cancelled");
		}

		@Override
		public void onBack() {
			// this method is call when user backpressed from dialog
			if (pd != null && pd.isShowing())
				pd.dismiss();
			Log.d("ShareTwitter", "Dialog Closed by pressing Back Key");
		}
	}

	private class ProfileDataListener implements SocialAuthListener {

		private String url;

		public ProfileDataListener(String site_url){
			this.url = site_url;
		}

		@Override
		public void onError(SocialAuthError socialAuthError) {
			if (pd != null && pd.isShowing())
				pd.dismiss();
		}

		@Override
		public void onExecute(String s, Object t) {
			if (pd != null && pd.isShowing())
				pd.dismiss();

			if (t instanceof Profile == false) {
				return;
			}

			Profile profileMap = (Profile) t;
			String id = profileMap.getValidatedId();
			String name = profileMap.getFullName();//getFirstName() + profileMap.getLastName();
			String email = profileMap.getEmail();
			String profile_image_url = profileMap.getProfileImageURL();

			Log.i("Postcomment", "profile : "+ profileMap.toString());

			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_NAME, name).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_EMAIL, email).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_SOCIAL_SITE_ID, id).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_SOCIAL_SITE, url).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_AVATAR, profile_image_url).commit();
			getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_USER_AVATAR_ORIGINAL, profile_image_url).commit();

			if(Util.getNetworkConnectivityStatus(VideoConferenceActivity.this) == false){
				Util.showDialogToShutdownApp(VideoConferenceActivity.this);
				return;
			}

			showAlertForUserDetailEntry();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		mConnectionClassManager.remove(mListener);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mConnectionClassManager.register(mListener);
	}

	/**
	 * Listener to update the UI upon connectionclass change.
	 */
	private class ConnectionChangedListener
			implements ConnectionClassManager.ConnectionClassStateChangeListener {

		@Override
		public void onBandwidthStateChange(ConnectionQuality bandwidthState) {
			mConnectionClass = bandwidthState;
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					mtvSpeed.setText(mConnectionClass.toString());
				}
			});
		}
	}


}
