package com.pc.apps.simpletweets.Utilities;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.FlickrApi;
import org.scribe.builder.api.TwitterApi;

import android.content.Context;

import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "pRnV4PckIrOwKpdwAlxtcZ4sK";       // Change this
	public static final String REST_CONSUMER_SECRET = "sce3n9yVj54JvTcIv04kV5FMgpzjuG3nv4LSlbjxew2vtU6qiu"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)
    public static final int COUNT_PER_REQUEST = 25;

	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}

    //METHOD == ENDPOINT

    //HomeTimeline = Gets us the home timeline
    //    Get the home timeline for the user
    //    GET statuses/home_timeline.json
    //    Parameters:
    //    Count=25 (default 20, max 200)
    //    since_id=1

    public void getHomeTimeline(AsyncHttpResponseHandler handler, long max_id){
        String apiUrl = getApiUrl("statuses/home_timeline.json");
        RequestParams params = new RequestParams();
        params.put("count", COUNT_PER_REQUEST);
        //params.put("since_id", since_id);
        if(max_id != Long.MAX_VALUE){
            params.put("max_id",max_id);
        }
        client.get(apiUrl, params, handler);
    }

    //COMPOSE TWEET
    public void postTweet(AsyncHttpResponseHandler handler, String tweetText){
        String apiUrl = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweetText);
        client.post(apiUrl, params, handler);
    }

    //GET USER INFO
    public void getLoggedInUser(AsyncHttpResponseHandler handler){
        String apiUrl = getApiUrl("account/verify_credentials.json");
        RequestParams params = null;
        client.get(apiUrl, params, handler);
    }

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}