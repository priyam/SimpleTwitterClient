package com.pc.apps.simpletweets.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.content.DialogInterface;
import android.graphics.Rect;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pc.apps.simpletweets.R;
import com.pc.apps.simpletweets.models.User;
import com.squareup.picasso.Picasso;


public class ComposeTweetDialog extends DialogFragment {

    private Button iBtnPostTweet;
    private EditText etComposeTweet;
    private ImageView ivUserPic;
    private TextView tvUserName;
    private TextView tvUserScreenName;
    private TextView tvTweetCharCount;

    private final TextWatcher etComposeTweetTextWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvTweetCharCount.setText(String.valueOf(140 - s.length()));
        }

        public void afterTextChanged(Editable s) {
            if (s.length() > 140){
                s.delete(140, s.length()-1);
            }
        }
    };
    public ComposeTweetDialog(){

    }

    public static ComposeTweetDialog newInstance(User user) {
        ComposeTweetDialog frag = new ComposeTweetDialog();
        Bundle args = new Bundle();
        args.putParcelable("user", user);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_compose_tweet, container);
        etComposeTweet = (EditText) view.findViewById(R.id.etComposeTweet);
        iBtnPostTweet = (Button) view.findViewById(R.id.iBtnPostTweet);
        ivUserPic = (ImageView) view.findViewById(R.id.ivUserPic);
        tvUserName = (TextView) view.findViewById(R.id.tvUserName);
        tvUserScreenName = (TextView) view.findViewById(R.id.tvUserScreenName);
        tvTweetCharCount = (TextView) view.findViewById(R.id.tvTweetCharCount);

        etComposeTweet.addTextChangedListener(etComposeTweetTextWatcher);
        Dialog dialog = getDialog();
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_compose_tweet);

        User user = (User) getArguments().getParcelable("user");
        if(user !=null){
            Picasso.with(dialog.getContext()).load(user.getProfileImageUrl()).into(ivUserPic);
            tvUserName.setText(user.getName());
            tvUserScreenName.setText(user.getScreenName());
        }
        // retrieve display dimensions
        Rect displayRectangle = new Rect();
        Window window = dialog.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);

        int height = (int)(displayRectangle.width() * 0.7f);
        int width = (int)(displayRectangle.height() * 0.6f);
        window.setLayout(width, height);
        // Show soft keyboard automatically
        etComposeTweet.requestFocus();
        dialog.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        iBtnPostTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = etComposeTweet.getText().toString();
                if (text != null && !text.isEmpty() && text.length() <= 140) {
                    //Fire the event so that the activity can post the tweet and update view
                    listener.onTweetPostClicked(text);
                    //dismiss the dialog
                    getDialog().dismiss();
                }
                else {
                    Toast.makeText(getDialog().getContext(), "Tweet cannot be empty or more than 140 chars", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    // ...
    // Define the listener of the interface type
    // listener is the activity itself
    private OnTweetPostListener listener;

    // Define the events that the fragment will use to communicate
    public interface OnTweetPostListener {
        public void onTweetPostClicked(String text);
    }

    // Store the listener (activity) that will have events fired once the fragment is attached
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnTweetPostListener) {
            listener = (OnTweetPostListener) activity;
        } else {
            throw new ClassCastException(activity.toString()
                    + " must implement MyListFragment.OnItemSelectedListener");
        }
    }

}
