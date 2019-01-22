package com.lipl.ommcom.twitter;

/**
 * Created by Android Luminous on 6/11/2016.
 */
public class TwitterConstant {
    public static final String CONSUMER_KEY = "Bnrsv48ZWcr7dA8R80Y4l9mzf";
    public static final String CONSUMER_SECRET= "TA88j3gudvqrfAPmLFDeFwGWrszN3I7NvMW7a9n5n7rAWo1mTR";

    public static final String REQUEST_URL = "http://api.twitter.com/oauth/request_token";
    public static final String ACCESS_URL = "http://api.twitter.com/oauth/access_token";
    public static final String AUTHORIZE_URL = "http://api.twitter.com/oauth/authorize";

    final public static String  CALLBACK_SCHEME = "x-latify-oauth-twitter";
    final public static String  CALLBACK_URL = CALLBACK_SCHEME + "://callback";
}
