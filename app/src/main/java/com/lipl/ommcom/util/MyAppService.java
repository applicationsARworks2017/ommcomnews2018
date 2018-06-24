package com.lipl.ommcom.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.HomeActivity;
import com.lipl.ommcom.activity.NewsDetailsActivity;
import com.lipl.ommcom.activity.VideoConferenceActivity;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.pubnub.api.PubnubException;

import org.json.JSONException;
import org.json.JSONObject;

public class MyAppService extends Service {
    public MyAppService() {
    }

    private Pubnub pubnub = null;
    private final String channel_name_breaking_news = "BREAKINGNEWS";
    private final String channel_name_news = "news";
    private final String channel_name_video_conference = "ommcom_videoconference";
    private String TAG = "MyAppService";
    public static Handler mServiceHandler;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mServiceHandler = new Handler() // Receive messages from service class
        {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 0:

                        JSONObject sts = new JSONObject();
                        try {
                            sts.put("status", 2);
                        } catch(JSONException exception){
                            Log.e(TAG, "requestToJoinConference()", exception);
                        }
                        // add the status which came from service and show on GUI
                        if(pubnub != null) {
                            pubnub.publish(channel_name_video_conference, sts, new com.pubnub.api.Callback() {
                            });
                        }

                        break;
                    default:
                        break;
                }
            }
        };
        Thread t = new Thread() {
            public void run() {
                subscribePubnub();
            };
        };
        t.start();

        return super.onStartCommand(intent, flags, startId);
    }

    private void subscribePubnub() {
        pubnub = new Pubnub(Config.publish_key, Config.subscribe_key);
        try {
            pubnub.subscribe(channel_name_breaking_news, new Callback() {
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
                            //Looper.prepare();
                            System.out.println("MyApplication" + " SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());

                            if (message instanceof JSONObject) {
                                String status = "";
                                try {
                                    JSONObject jsonObject = (JSONObject) message;
                                    status = jsonObject.getString("status");
                                } catch (JSONException exception) {
                                    Log.e("MyApplication", "getStreamingMessage()", exception);
                                }

                                if (status != null && status.trim().length() > 0) {
                                    String text = status;
                                    NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                                    Intent intent = new Intent(MyAppService.this, HomeActivity.class);
                                    intent.putExtra("isFromNotification", true);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(MyAppService.this, 1, intent, 0);
                                    NotificationCompat.Builder mBuilder =
                                            new NotificationCompat.Builder(MyAppService.this)
                                                    .setSmallIcon(getNotificationIcon())
                                                    .setContentTitle("Breaking News")
                                                    .setStyle(new NotificationCompat.BigTextStyle()
                                                            .bigText(text))
                                                    .setContentText(text)
                                                    .setDefaults(NotificationCompat.DEFAULT_SOUND)
                                                    .setContentIntent(pendingIntent)
                                                    .setAutoCancel(true);
                                    manager.notify(12, mBuilder.build());
                                }
                            }
                            //Looper.loop();
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());
                        }
                    }
            );

            pubnub.subscribe(channel_name_news, new Callback() {
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
                            System.out.println("MyApplication" + " SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());

                            if (message instanceof JSONObject) {
                                String status = "";
                                try {
                                    JSONObject jsonObject = (JSONObject) message;
                                    status = jsonObject.getString("status");
                                } catch (JSONException exception) {
                                    Log.e("MyApplication", "getStreamingMessage()", exception);
                                }

                                if (status.equalsIgnoreCase("")) {

                                } else {
                                    String news_slug = status;
                                    if (news_slug != null && news_slug.contains("*****#####*****")) {
                                        String[] nnn = news_slug.split("\\*\\*\\*\\*\\*#####\\*\\*\\*\\*\\*");
                                        String title = nnn[0];
                                        String slug = nnn[1];
                                        showNotificationForNews(title, slug);
                                    }
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
            pubnub.subscribe(channel_name_video_conference, new Callback() {
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
                            //Looper.prepare();
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
                                    //Looper.prepare();
                                    Message msgToActivity = new Message();
                                    msgToActivity.what = 0;
                                    msgToActivity.obj  = "1"; // you can put extra message here
                                    if(VideoConferenceActivity.mUiHandler != null) {
                                        VideoConferenceActivity.mUiHandler.sendMessage(msgToActivity);
                                    }
                                    //Looper.loop();

                                } else if (status == 2) {

                                } else if (status == 3) {
                                    // Get Conference announcement from web service request
                                    Message msgToActivity = new Message();
                                    msgToActivity.what = 1;
                                    msgToActivity.obj  = "2"; // you can put extra message here
                                    if(VideoConferenceActivity.mUiHandler != null) {
                                        VideoConferenceActivity.mUiHandler.sendMessage(msgToActivity);
                                    }
                                }
                            }
                            //Looper.loop();
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
    }

    private void showNotificationForNews(String text, String slug){
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Intent intent = new Intent(MyAppService.this, NewsDetailsActivity.class);
        intent.putExtra("slug", slug);
        intent.putExtra("isFromNotification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(MyAppService.this, 1, intent, 0);
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(MyAppService.this)
                        .setSmallIcon(getNotificationIcon())
                        .setContentTitle("Ommcom News")
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(text))
                        .setContentText(text)
                        .setDefaults(NotificationCompat.DEFAULT_SOUND)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true);
        manager.notify(11, mBuilder.build());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.ic_small : R.mipmap.ic_launcher;
    }
}
