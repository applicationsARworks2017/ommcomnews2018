package com.lipl.ommcom.util;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.lipl.ommcom.R;
import com.lipl.ommcom.activity.HomeActivity;

//import co.mobiwise.fastgcm.GCMListenerService;

/**
 * Created by Android Luminous on 5/12/2016.
 */
public class CustomGCMService {//extends GCMListenerService {

//    NotificationManager manager;
//    Notification myNotication;
//    private boolean mIsNotificationMessage = false;
//    private boolean mIsConferenceMessage = false;
//
//    @Override
//    public void onMessageReceived(String from, Bundle data) {
//        super.onMessageReceived(from, data);
//        String message = data.getString("message");
//        Log.i("Notification", message);
//
//        if(message != null){
//            if(message.trim().length() > 0 && message.trim().startsWith("1")){
//                mIsNotificationMessage = true;
//                mIsConferenceMessage = false;
//                String text = message.substring(1, message.length());
//                message = text;
//                manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                Intent intent = new Intent("com.lipl.ommcom");
//                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);
//                Notification.Builder builder = new Notification.Builder(getApplicationContext());
//                builder.setAutoCancel(true);
//                builder.setTicker("OMMCOM");
//                builder.setContentTitle("OMMCOM");
//                builder.setContentText("Breaking News");
//                builder.setSmallIcon(R.mipmap.ic_launcher);
//                builder.setContentIntent(pendingIntent);
//                builder.setOngoing(true);
//                builder.setSubText(message);   //API level 16
//                builder.setNumber(100);
//                builder.build();
//
//                myNotication = builder.getNotification();
//                manager.notify(11, myNotication);
//                if(SingleTon.getInstance() != null
//                        && SingleTon.getInstance().getmOnBreakingNewsNotificationListener() != null) {
//                    SingleTon.getInstance().getmOnBreakingNewsNotificationListener().onBreakingNewsNotification();
//                }
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit().putString(Config.SP_BREAKING_NEWS_KEY, message).commit();
//            } else if(message.trim().length() > 0 && message.trim().startsWith("2")){
//                mIsConferenceMessage = true;
//                mIsNotificationMessage = false;
//                String text = message.substring(1, message.length());
//                message = text;
//                if(SingleTon.getInstance() != null
//                        && SingleTon.getInstance().getConferenceStartConference() != null) {
//                    SingleTon.getInstance().getConferenceStartConference().onConferenceStart(message);
//                }
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit()
//                        .putString(Config.SP_CONFERENCE_START_MESSAGE_KEY, message).commit();
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit()
//                        .putBoolean(Config.SP_CONFERENCE_STOP_MESSAGE_KEY, false).commit();
//            } else if(message.trim().length() > 0 && message.trim().startsWith("3")){
//                mIsConferenceMessage = true;
//                mIsNotificationMessage = false;
//                if(SingleTon.getInstance() != null
//                        && SingleTon.getInstance().getConferenceStopConference() != null) {
//                    SingleTon.getInstance().getConferenceStopConference().onConferenceStop();
//                }
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit()
//                        .putString(Config.SP_CONFERENCE_START_MESSAGE_KEY, "").commit();
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit()
//                        .putBoolean(Config.SP_CONFERENCE_STOP_MESSAGE_KEY, true).commit();
//            } else if(message.trim().length() > 0 && message.trim().startsWith("4")){
//                mIsConferenceMessage = true;
//                mIsNotificationMessage = false;
//                String text = message.substring(1, message.length());
//                message = text;
//                if(SingleTon.getInstance() != null
//                        && SingleTon.getInstance().getmOnConferenceFlashOutMessageListener() != null) {
//                    SingleTon.getInstance().getmOnConferenceFlashOutMessageListener().onConferenceFlashoutMessage(message);
//                }
//                getSharedPreferences(Config.SHARED_PREFERENCE_KEY, 2).edit()
//                        .putString(Config.SP_CONFERENCE_FLASH_MESSAGE_KEY, message).commit();
//            } else {
//                mIsConferenceMessage = false;
//                mIsNotificationMessage = false;
//            }
//        } else {
//            mIsConferenceMessage = false;
//            mIsNotificationMessage = false;
//        }
//    }
//
////    private int getNotificationIcon() {
////        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
////        return useWhiteIcon ? R.drawable.icon_silhouette : R.drawable.ic_launcher;
////    }
//
//    public interface OnConferenceStartListener {
//        public void onConferenceStart(String message);
//    }
//
//    public interface OnConferenceStopListener {
//        public void onConferenceStop();
//    }
//
//    public interface OnBreakingNewsNotificationListener {
//        public void onBreakingNewsNotification();
//    }
//
//    public interface OnConferenceFlashOutMessageListener{
//        public void onConferenceFlashoutMessage(String message);
//    }
}