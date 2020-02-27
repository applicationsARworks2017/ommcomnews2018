package com.lipl.ommcom.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.TypedValue;

/**
 * Created by Android Luminous on 4/25/2016.
 */
public class Config {

    public static final String VERSION_ONE = "/v0.1";
    public static final String VERSION = VERSION_ONE;

    private static final String LOCAL = "45.114.50.54/ommcom-wip";//"192.168.1.115:8000";
    private static final String PUBLIC = "ommcomnews.com" ;//"45.114.50.54";
    private static final String PUBLIC_VC = "ommcomnews.com/ommcom-wip/ommcom-wip" ;//"45.114.50.54";


/*    public static final String DOMAIN = "https://"+ PUBLIC + "/public";
// created by amaresh
    public static final String DOMAIN_VC = "https://"+ PUBLIC_VC + "/public"; */


    public static final String DOMAIN = "https://"+ PUBLIC ;
// created by amaresh
    public static final String DOMAIN_VC = "https://"+ PUBLIC_VC;

    public static final String API_BASE_URL = DOMAIN + "/api" +VERSION;

    //created by amaresh
    public static final String API_BASE_URL_VC = DOMAIN_VC + "/api" +VERSION;

    public static final String IMAGE_DOWNLOAD_BASE_URL = DOMAIN + "/file";
    public static final String HOME_SCREEN_DETAILS_API = "/postsHome";
    public static final String BREAKING_NEWS_API = "/breakingNews";
    public static final String NEWS_DETAILS_API = "/posts";
    public static final String TOP_NEWS_LIST_API = "/posts/topNewsList";
    public static final String CITIZEN_NEWS_LATEST_VIDEOS = "/citizen/latest";
    public static final String CITIZEN_NEWS_MOST_VIEWED = "/citizen/mostviewed";
    public static final String CITIZEN_NEWS_ADV_POPUP = "/citizen/citizenAdvPopup";
    public static final String HOME_SCREEN_TOP_VIRAL_VIDEO = "/postsHome/topViralVideos";
    public static final String HOME_SCREEN_ADVERTISEMENT_POST = "/postsHome/advertisementPost";
    public static final String HOME_SCREEN_CATEGORY_NEWS = "/postsHome/categoryNews";
    public static final String HOME_SCREEN_CITIZEN_CUSTOMIZE = "/postsHome/citizenCustomize";
    public static final String HOME_SCREEN_CONFERENCE = "/postsHome/conferenceNews";
    public static final String HOME_SCREEN_FEATURED_NEWS = "/postsHome/featuredNews";
    public static final String REQUEST_JOIN_API = "/conference/mobjoin";
    public static final String REQUEST_CONFERENCE_DETAILS_API = "/conference/conference_details";
    public static final String REQUEST_CONFERENCE_STATUS_API = "/conference/conference_status";
    public static final String REQUEST_CONFERENCE_ANNOUNCEMENT_API = "/conference/showAnnouncement";
    public static final String GET_ARCHIVE_VIDEOS_API = "/conference/archivevideos";
    public static final String SEARCH_NEWS_API = "/posts/searchNewsList";

    public static final String NAVIGATION_DRAWER_CATEGORIES_API = "/categories";
    public static final String POLL_QUESTION_API = "/poll/question";
    public static final String POLL_ANSWER_API = "/poll/answer";
    public static final String POLL_SUBMIT_API = "/poll/save";
    public static final String DEVICE_REGISTER = "/deviceRegister";
    public static final String POST_COMMENT = "/postComment";
    public static final String POST_CITIZEN_NEWS = "/postCitizenNews";
    public static final String FEEDBACK = "/feedback";
    public static final String API_TOP_VIDEO_LIST= "/topVideos";
    public static final String API_VIRAL_VIDEO_LIST= "/viralVideos";
    public static final String API_SEND_USER_DETAILS = "/postUserDetails";
    public static final String API_CITIZEN_DETAILS_DETAILS = API_BASE_URL +"/citizen/";

    public static final String publish_key =  "pub-c-91050f44-77f4-443e-9f9c-52a9c55045f0";
    public static final String subscribe_key = "sub-c-bce8b0f8-2a17-11e6-8b91-02ee2ddab7fe";

    public static final String FOLDER_NEWS = "/news";
    public static final String FOLDER_SLUG = "/slug/";
    public static final String FOLDER_CITIZEN_JOURNALIST = "/citizenCustomize";
    public static final String FOLDER_ADVERTISEMENT = "/advertisement";
    public static final String FOLDER_CONFERENCE = "/conference";
    public static final String FOLDER_VIDEO = "/original";
    public static final String FOLDER_CITIZEN_NEWS = "/citizenNews";
    public static final String FOLDER_TOP_NEWS_IMAGE_VIDEO = "/topnewsvideo";
    public static final String FOLDER_COMMENT = "/comments";
    public static final String OD_CONENT = "?odia=true";
    public static final String OD_NEWCONENT = "&odia=true";
    public static final String EN_CONENT = "?odia=flase";
    public static final String EN_NEWCONENT = "&odia=flase";

    public static final String FOLDER_DP_720W_1280H = "/dp_720w_1280h/";
    public static final String FOLDER_DP_480W_800H = "/dp_480w_800h/";
    public static final String FOLDER_DP_1080W_1920H = "/dp_1080w_1920h/";
    public static final String FOLDER_DP_1440W_2560H = "/dp_1440w_2560h/";

    public static final String FOLDER_TN_720W_1280H = "/tn_720w_1280h/";
    public static final String FOLDER_TN_480W_800H = "/tn_480w_800h/";
    public static final String FOLDER_TN_1080W_1920H = "/tn_1080w_1920h/";
    public static final String FOLDER_TN_1440W_2560H = "/tn_1440w_2560h/";

    public static final String FOLDER_CJ_720W_1280H = "/cj_720w_1280h/";
    public static final String FOLDER_CJ_480W_800H = "/cj_480w_800h/";
    public static final String FOLDER_CJ_1080W_1920H = "/cj_1080w_1920h/";
    public static final String FOLDER_CJ_1440W_2560H = "/cj_1440w_2560h/";

    public static final String FOLDER_AD_720W_1280H = "/ad_720w_1280h/";
    public static final String FOLDER_AD_480W_800H = "/ad_480w_800h/";
    public static final String FOLDER_AD_1080W_1920H = "/ad_1080w_1920h/";
    public static final String FOLDER_AD_1440W_2560H = "/ad_1440w_2560h/";
    public static final String YOUTUBE_API_KEY = "AIzaSyCBPFulLWv_I19VelBrt0_6chxIJ21RdzE";


    public static final String getFolderForDP(){
        int width = Util.getScreenWidth();
        if(width < 480 && width >= 480){
            return FOLDER_DP_480W_800H;
        } else if(width < 720 && width >= 720){
            return FOLDER_DP_720W_1280H;
        } else if(width < 1080 && width >= 1080){
            return FOLDER_DP_1080W_1920H;
        } else if(width < 1440 && width >= 1440){
            return FOLDER_DP_1440W_2560H;
        } else {
            return FOLDER_DP_720W_1280H;
        }
    }

    public static final String getFolderForTN(){
        int width = Util.getScreenWidth();
        if(width < 480 && width >= 480){
            return FOLDER_TN_480W_800H;
        } else if(width < 720 && width >= 720){
            return FOLDER_TN_720W_1280H;
        } else if(width < 1080 && width >= 1080){
            return FOLDER_TN_1080W_1920H;
        } else if(width < 1440 && width >= 1440){
            return FOLDER_TN_1440W_2560H;
        } else {
            return FOLDER_TN_720W_1280H;
        }
    }

    public static final String getFolderForADV(){
        int width = Util.getScreenWidth();
        if(width < 480 && width >= 480){
            return FOLDER_AD_480W_800H;
        } else if(width < 720 && width >= 720){
            return FOLDER_AD_720W_1280H;
        } else if(width < 1080 && width >= 1080){
            return FOLDER_AD_1080W_1920H;
        } else if(width < 1440 && width >= 1440){
            return FOLDER_AD_1440W_2560H;
        } else {
            return FOLDER_AD_720W_1280H;
        }
    }

    public static final String getFolderForCj(){
        int width = Util.getScreenWidth();
        if(width < 480 && width >= 480){
            return FOLDER_CJ_480W_800H;
        } else if(width < 720 && width >= 720){
            return FOLDER_CJ_720W_1280H;
        } else if(width < 1080 && width >= 1080){
            return FOLDER_CJ_1080W_1920H;
        } else if(width < 1440 && width >= 1440){
            return FOLDER_CJ_1440W_2560H;
        } else {
            return FOLDER_CJ_720W_1280H;
        }
    }

    public static float pixelsToSp(Context context, float px) {
        float scaledDensity = context.getResources().getDisplayMetrics().scaledDensity;
        return px/scaledDensity;
    }

    public static float get_text_size(Context context, float size){
        //28sp for 720pxw x 1280pxh
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size,
                context.getResources().getDisplayMetrics());
        if(Util.getScreenHeight() <= 800){
            px = (800.0f / 1280.0f) * px;
        } else if(Util.getScreenHeight() <= 1280){
            px = 1.0f * px;
        } else if(Util.getScreenHeight() <= 1920){
            px = (1920.0f / 1280.0f) * px;
        } else if(Util.getScreenHeight() <= 2560){
            px = (2560.0f / 1280.0f) * px;
        }

        float sp = pixelsToSp(context, px);

        return sp;
    }

    public static final String DEFAULT_VIDEO_THUMBNAIL = DOMAIN + "/images/videoDefault.jpg";
    public static final String SHARED_PREFERENCE_KEY = "sharedpreference_key";
    public static final String SP_BREAKING_NEWS_KEY = "sp_breaking_news_key";
    public static final String SP_LOGGED_IN_SOCIAL_SITE = "logged_in_social_site";
    public static final String SP_CONFERENCE_START_MESSAGE_KEY = "sp_conference_start_message_key";
    public static final String SP_CONFERENCE_FLASH_MESSAGE_KEY = "sp_conference_flash_message_key";
    public static final String SP_IS_FROM_CHILD_ACTIVITY = "sp_is_app_closed";
    public static final String SP_CONFERENCE_STOP_MESSAGE_KEY = "sp_conference_stop_message_key";
    public static final String STREAM_PLAY_PATH_BASE_URL = "rtmp://stream.ssh101.com:1935/live/";
    public static final String STREAM_PUBLISH_PATH_BASE_URL = "rtmp://stream.ssh101.com:1935/live?publish=";

    public static final String SP_USER_NAME = "sp_user_name";
    public static final String SP_USER_EMAIL = "sp_user_email";
    public static final String SP_LATITUDE = "sp_latitude";
    public static final String SP_LONGITUDE = "sp_longitude";
    public static final String SP_USER_SOCIAL_SITE_ID = "sp_user_social_site_id";
    public static final String SP_USER_SOCIAL_SITE = "sp_user_social_site";
    public static final String SP_USER_AVATAR = "sp_user_avatar";
    public static final String SP_USER_AVATAR_ORIGINAL = "sp_user_avatar_original";
    public static final String SP_LOGIN_STATUS = "sp_user_login_status";

    public static final String SHAREDPREFERENCE_LANGUAGE = "ommcomlanguage" ;
    public static final String LANG="languange";

    public static Intent getTwitterIntent(Context ctx, String shareText) {
        Intent shareIntent;
        shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setClassName("com.twitter.android",
                "com.twitter.android.PostActivity");
        shareIntent.setType("text/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareText);
        return shareIntent;

    }

}
