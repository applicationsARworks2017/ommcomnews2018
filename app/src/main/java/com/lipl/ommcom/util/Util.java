package com.lipl.ommcom.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Patterns;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.lipl.ommcom.R;
import com.lipl.ommcom.pojo.Advertisement;
import com.lipl.ommcom.pojo.BreakingNews;
import com.lipl.ommcom.pojo.CitizenJournalistVideos;
import com.lipl.ommcom.pojo.ConferenceNews;
import com.lipl.ommcom.pojo.News;
import com.lipl.ommcom.pojo.PopupAdvertisement;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

/**
 * Created by Android Luminous on 4/28/2016.
 */
public class Util {

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static String getTime(String time){
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd H:mm");
            Date past = format.parse(time);
            Date now = new Date();

            long milliseconds = TimeUnit.MILLISECONDS.toMillis(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if(days == 1){
                return days + " day ago";
            } else if(days > 0){
                return days + " days ago";
            } else if(hours == 1){
                return hours + " hour ago";
            } else if(hours > 0){
                return hours + " hours ago";
            } else if(minutes == 1){
                return minutes + " minute ago";
            } else if(minutes > 0){
                return minutes + " minutes ago";
            } else if(milliseconds > 0 && (milliseconds / 1000) == 1){
                return (milliseconds / 1000) + " sec ago";
            } else if(milliseconds > 0){
                return (milliseconds / 1000) + " secs ago";
            }
        }
        catch (Exception j){
            j.printStackTrace();
        }
        return "";
    }

    public static boolean getNetworkConnectivityStatus(Context context){

        if(context == null){
            return false;
        }

        boolean isConnected = false;;
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if((info != null) && (info.isConnected())){
            isConnected = true;
        }

        return isConnected;
    }

    public static final void showDialogToShutdownApp(final Activity activity){
        /**
         * Show dialog and close application
         * */
        new AlertDialog.Builder(activity, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar)
                .setIcon(R.mipmap.ic_warning)
                .setTitle("Network Connection")
                .setMessage("No internet available.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        activity.finishAffinity();
                    }
                })
                .setCancelable(false)
                .show();
    }

    public static final String getMacAddress(Context context){
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wInfo = wifiManager.getConnectionInfo();
        String macAddress = wInfo.getMacAddress();
        return macAddress;
    }

    public static final int getNumberOfCharactersCanFitIntoALineOfTextView(TextView textView){
        int textViewWidth = textView.getMeasuredWidth();
        int numChars;
        String text = "sdfkjhbsdfhgbsfgkjsdbjgbsdkjfbksjdbkhsbdfkjbvskjbkjsdbksdjslkjdfbkjsdfbjksdbkjsdkjfblkjsdbfjbkljsdbksdjkfbjksdbkjlsdljkfbkjsldbjksdbjsjdkfbjksdbjsdbdsgb";

        Paint paint = textView.getPaint();
        for (numChars = 1; numChars <= text.length(); ++numChars) {
            if (paint.measureText(text, 0, numChars) > textViewWidth) {
                break;
            }
        }

        Log.d("tag", "Number of characters that fit = " + (numChars - 1));
        return  numChars - 1;
    }

    public static String getEmailAddress(Context context){
        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(context).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                String possibleEmail = account.name;
                return possibleEmail;
            }
        }
        return "";
    }

    public static boolean openApp(Context context, String packageName, ConferenceNews _conferenceNews) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                return false;
                //throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.putExtra("conference_id", _conferenceNews.getId());
            i.putExtra("conference_title", _conferenceNews.getName());
            context.startActivity(i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String getImageFilePathForNews(News news, ImageView imageView){
        String image_file_name = "";
        if(news != null
                && news.getImage() != null
                && news.getImage().trim().length() > 0){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS
                    + Config.FOLDER_VIDEO + "/"//Config.getFolderForDP()
                    + news.getImage();
            if(imageView != null){
                imageView.setVisibility(View.GONE);
            }
        } else if(news.getIs_video() != null && news.getIs_video().equalsIgnoreCase("1")
                && news.getFile_path() != null && news.getFile_path().trim().length() > 0){
            String[] io = news.getFile_path().trim().split("\\.");
            if (io != null && io.length > 0) {
                image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS
                        + Config.FOLDER_VIDEO
                        + "/" + io[0] + ".jpg";
                if(imageView != null){
                    imageView.setVisibility(View.VISIBLE);
                }
            }
        } else if(news != null
                && news.getFile_path() != null
                && news.getFile_path().trim().length() > 0){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS
                    + Config.FOLDER_VIDEO + "/"//Config.getFolderForDP()
                    + news.getFile_path();
            if(imageView != null){
                imageView.setVisibility(View.GONE);
            }
        }
        return image_file_name;
    }

    public static String getImageFilePathForTopNews(News news){
        String image_file_name = "";
        if(news != null
                && news.getImage() != null
                && news.getImage().trim().length() > 0){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS + Config.FOLDER_VIDEO + "/"//+ Config.getFolderForTN()
                    + news.getImage();
        } else if(news.getIs_video() != null && news.getIs_video().equalsIgnoreCase("1")
                && news.getFile_path() != null && news.getFile_path().trim().length() > 0){
            String[] io = news.getFile_path().trim().split("\\.");
            if (io != null && io.length > 0) {
                image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS
                        + Config.FOLDER_VIDEO
                        + "/" + io[0] + ".jpg";
            }
        } else if(news != null
                && news.getFile_path() != null
                && news.getFile_path().trim().length() > 0){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_NEWS + Config.FOLDER_VIDEO + "/"//+ Config.getFolderForTN()
                    + news.getFile_path();
        }
        return image_file_name;
    }

    /**
     * http://192.168.1.104/ommcom/public/file/citizenNews/cj_480w_800h/26110211214619342412066206880-topnews.jpg
     * */
    public static String getImageFilePathForCitizen(CitizenJournalistVideos news){
        String image_file_name = "";
        if(news.getFile_type() != null && news.getFile_type().equalsIgnoreCase("Video")
                && news.getFile_path() != null && news.getFile_path().trim().length() > 0){
            String[] io = news.getFile_path().trim().split("\\.");
            if (io != null && io.length > 0) {
                image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS
                        + Config.FOLDER_VIDEO
                        + "/" + io[0] + ".jpg";
            }
        } else if(news != null
                && news.getFile_type() != null
                && news.getFile_type().trim().length() > 0
                && news.getFile_type().equalsIgnoreCase("Image")){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_CITIZEN_NEWS + Config.FOLDER_VIDEO + "/"//+ Config.getFolderForCj()
                    + news.getFile_path();
        } else {
            image_file_name = "";
        }
        return image_file_name;
    }

    /**
     * http://192.168.1.104/ommcom/public/file/citizenNews/cj_480w_800h/26110211214619342412066206880-topnews.jpg
     * */
    public static String getImageFilePathForAdvertisement(Advertisement news){
        String image_file_name = "";
        if(news.getFile_type() != null && news.getFile_type().equalsIgnoreCase("Video")
                && news.getFile_path() != null && news.getFile_path().trim().length() > 0){
            String[] io = news.getFile_path().trim().split("\\.");
            if (io != null && io.length > 0) {
                image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT
                        + Config.FOLDER_VIDEO
                        + "/" + io[0] + ".jpg";
            }
        } else if(news != null
                && news.getFile_type() != null
                && news.getFile_type().trim().length() > 0
                && news.getFile_type().equalsIgnoreCase("Image")){
            image_file_name = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT + Config.getFolderForADV()
                    + news.getFile_path();
        } else {
            image_file_name = "";
        }
        return image_file_name;
    }

    public static void showPopUpAdvertisement(Context context, PopupAdvertisement popupAdvertisement,
                                              final RelativeLayout topParentLayout, final RelativeLayout layoutRParent){

        View view = LayoutInflater.from(context).inflate(R.layout.custom_dialog, null);

        RelativeLayout layoutParent = (RelativeLayout) view.findViewById(R.id.layoutParent);
        if(popupAdvertisement != null && popupAdvertisement.getFile_type() != null
                && popupAdvertisement.getFile_type().trim().length() > 0
                && popupAdvertisement.getFile_type().equalsIgnoreCase("Video")){

            View video_view = LayoutInflater.from(context).inflate(R.layout.layout_file_video, null);
            final ProgressBar progressBar = (ProgressBar) video_view.findViewById(R.id.progressBar);
            final ImageView imgPlay = (ImageView) video_view.findViewById(R.id.imgPlay);
            final VideoView myVideoView = (VideoView) video_view.findViewById(R.id.videoView);

            progressBar.setVisibility(View.VISIBLE);
            imgPlay.setVisibility(View.INVISIBLE);
            String file_path = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT
                    + Config.FOLDER_VIDEO
                    + "/" + popupAdvertisement.getFile_path();
            Uri video = Uri.parse(file_path);
            myVideoView.setVideoURI(video);
            myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    myVideoView.start();
                    progressBar.setVisibility(View.GONE);
                }
            });

            myVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    imgPlay.setVisibility(View.VISIBLE);
                }
            });
            imgPlay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    imgPlay.setVisibility(View.INVISIBLE);
                    myVideoView.start();
                }
            });

            layoutParent.addView(video_view);
        } else if(popupAdvertisement != null && popupAdvertisement.getFile_type() != null
                && popupAdvertisement.getFile_type().trim().length() > 0
                && popupAdvertisement.getFile_type().equalsIgnoreCase("Audio")){
            String url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT
                    + Config.FOLDER_VIDEO + "/" + popupAdvertisement.getFile_path();
            Uri myUri = Uri.parse(url);
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
            intent.setDataAndType(myUri, "audio/*");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } else if(popupAdvertisement != null && popupAdvertisement.getFile_type() != null
                && popupAdvertisement.getFile_type().trim().length() > 0
                && popupAdvertisement.getFile_type().equalsIgnoreCase("Image")){
            View view_image = LayoutInflater.from(context).inflate(R.layout.layout_file_image, null);
            ImageView image = (ImageView) view_image.findViewById(R.id.image);

            DisplayImageOptions options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.stub)//ic_stub
                    .showImageForEmptyUri(R.drawable.empty)//ic_empty
                    .showImageOnFail(R.drawable.error)//ic_error
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .bitmapConfig(Bitmap.Config.ARGB_8888)
                    .displayer(new SimpleBitmapDisplayer())
                    .build();
            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(ImageLoaderConfiguration.createDefault(context));
            String image_url = Config.IMAGE_DOWNLOAD_BASE_URL + Config.FOLDER_ADVERTISEMENT +
                    Config.FOLDER_VIDEO + "/" + popupAdvertisement.getFile_path();
            imageLoader.displayImage(image_url, image, options);

            layoutParent.addView(view_image);
        }

        final ImageView imgClose = (ImageView) view.findViewById(R.id.imgClose);
        imgClose.setVisibility(View.INVISIBLE);
        new CountDownTimer(5000, 1000){
            @Override
            public void onFinish() {
                imgClose.setVisibility(View.VISIBLE);
            }
            @SuppressLint("NewApi")
            @TargetApi(Build.VERSION_CODES.GINGERBREAD)
            @Override public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished;
                String hms = String.format("%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millis),
                        TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)),
                        TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                System.out.println(hms);
            }
        }.start();

        /*final PopupWindow popupWindow;
        int width = 400;//(int) (getScreenWidth() / 0.8);
        int height = 300;//(int) (getScreenHeight() / 0.6);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);*/


        //final Dialog dialog = new Dialog(context, android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar);
        //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        topParentLayout.addView(view);
        //dialog.show();
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        //lp.copyFrom(dialog.getWindow().getAttributes());
        //dialog.getWindow().setAttributes(lp);

        imgClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutRParent.setVisibility(View.GONE);
            }
        });

        layoutRParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public static boolean isIntentSafe(Context context, Intent intent){
        boolean isIntentSafe = false;
        if(context == null){
            return  isIntentSafe;
        }

        PackageManager packageManager = context.getPackageManager();
        List activities = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        isIntentSafe = activities.size() > 0;

        return isIntentSafe;
    }

    public static String[] getWords(String text){
        String[] words = text.split("\\s+");
        for (int i = 0; i < words.length; i++) {
            //words[i] = words[i].replaceAll("[^\\w]", "");
            words[i] = words[i].replaceAll("[ ]", "");
        }
        return words;
    }

    public static void setToList(String s, List<String> sss){
        String[] words = getWords(s);
        if(words != null && words.length > 0){
            int count = 0;
            String rest = "";
            for(int i = 0; i < words.length; i++) {
                count++;
                if(count == 6){
                    count = 0;
                    sss.add(rest);
                    rest = "";
                }
                if(i != words.length - 1) {
                    rest = rest + words[i] + " ";
                } else {
                    rest = rest + words[i];
                    count = 0;
                    sss.add(rest);
                    rest = "";
                }
            }
        }
    }

    public static void muteAudio(Context context){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }

    public static void unmuteAudio(Context context){
        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, maxVolume, AudioManager.FLAG_SHOW_UI + AudioManager.FLAG_PLAY_SOUND);
    }
}
