package com.pc.apps.simpletweets.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.pc.apps.simpletweets.activities.TimelineActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

//Parse the json + Store the data, encapsulate  state logic or display logic
public class Tweet implements Parcelable {

    private final String TwitterDateFormat="EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    //list out the attributes
    private String body;
    private long uid;//unique id for the tweet
    private User user;
    private String createdAt;


    public String getBody() {
        return body;
    }

    public long getUid() {
        return uid;
    }

    public String getCreatedAt() {
        return createdAt;
    }
    public String getCreatedAtPrettyTime() {
        SimpleDateFormat sf = new SimpleDateFormat(TwitterDateFormat, Locale.US);
        sf.setLenient(true);
        try {
            Date now = new Date();
            Date date = sf.parse(createdAt);
            PrettyTime p = new PrettyTime();
            String sDate = p.format(date);
            long diffInSeconds = (now.getTime() - date.getTime())/1000;

        return sDate.replace("moments ago", (diffInSeconds < 60? diffInSeconds + "s" : diffInSeconds/60 + "m")).
                    replace(" minute ago", "m").
                    replace(" minutes ago", "m").
                    replace(" hour ago", "h").
                    replace(" hours ago", "h").
                    replace(" day ago", "d").
                    replace(" days ago", "d").
                    replace("week ago", "w").
                    replace("weeks ago", "w").
                    replace("month ago", "mo").
                    replace("months ago", "mo").
                    replace("year ago", "y").
                    replace("years ago", "y").
                    replace(" moments from now", "0s")
                ;

        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt;
        }
    }

    public User getUser() {
        return user;
    }

    //Deserialize the JSON
    //Tweet.fromJSON("{...}") ==> <Tweet>
    public static Tweet fromJSON(JSONObject jsonObject){
        Tweet tweet = new Tweet();

        //Extract the values from the json, store them
        try {
            tweet.body = jsonObject.getString("text");
            tweet.uid = jsonObject.getLong("id");
            if(TimelineActivity.MAX_ID > tweet.uid){
                TimelineActivity.MAX_ID = tweet.uid;
            }
            tweet.createdAt = jsonObject.getString("created_at");
            tweet.user = User.fromJSON(jsonObject.getJSONObject("user"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //return the tweet object
        return tweet;
    }

    public static ArrayList<Tweet> fromJSONArray(JSONArray jsonArray) {
        ArrayList<Tweet> tweets = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                Tweet tweet = fromJSON(jsonArray.getJSONObject(i));
                if(tweet != null){
                    tweets.add(tweet);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                continue;
            }
        }
        return tweets;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
        dest.writeLong(this.uid);
        dest.writeParcelable(this.user, flags);
        dest.writeString(this.createdAt);
    }

    public Tweet() {
    }

    private Tweet(Parcel in) {
        this.body = in.readString();
        this.uid = in.readLong();
        this.user = in.readParcelable(User.class.getClassLoader());
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<Tweet> CREATOR = new Parcelable.Creator<Tweet>() {
        public Tweet createFromParcel(Parcel source) {
            return new Tweet(source);
        }

        public Tweet[] newArray(int size) {
            return new Tweet[size];
        }
    };
}
