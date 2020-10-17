package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import okhttp3.Headers;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.parceler.Parcels;

public class ComposeActivity extends AppCompatActivity {

    public static final String TAG = "ComposeActivity";
    public static final int MAX_TWEET_LENGTH = 280;

    EditText etCompose;
    TextView tvCharCount;
    Button btnTweet;

    TwitterClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        client = TwitterApp.getRestClient(this);

        etCompose = findViewById(R.id.etCompose);
        tvCharCount = findViewById(R.id.tvCharCount);
        btnTweet = findViewById(R.id.btnTweet);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Fires right as the text is being changed (even supplies the range of the text)
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int length_before, int length_after) {
                // Fires right before text is changing
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Fires right after the text has changed
                tvCharCount.setText(String.format("%d/%d", editable.length(), MAX_TWEET_LENGTH));
                if (editable.length() > MAX_TWEET_LENGTH || editable.length() == 0) {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                } else {
                    tvCharCount.setTextColor(getResources().getColor(android.R.color.darker_gray));
                }
            }
        });

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Make API call to twitter
                String tweetContent = etCompose.getText().toString();
                if (tweetContent.isEmpty()) {
                    Snackbar.make(findViewById(android.R.id.content).getRootView(), "Sorry your tweet cannot be empty", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (tweetContent.length() > MAX_TWEET_LENGTH) {
                    Snackbar.make(findViewById(android.R.id.content).getRootView(), "Sorry your tweet is too long", Snackbar.LENGTH_LONG).show();
                    return;
                }

                client.publishTweet(tweetContent, new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.i(TAG, "onSuccess to publish tweet");
                        try {
                            Tweet tweet = Tweet.fromJson(json.jsonObject);
                            Intent intent = new Intent();
                            intent.putExtra("tweet", Parcels.wrap(tweet));
                            // set result code and bundle data for response
                            setResult(RESULT_OK, intent);
                            // close the activity, pass the data to parent
                            finish();
                            Log.i(TAG, "published tweet says" + tweet.toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish tweet " + statusCode + " " + response, throwable);
                    }
                });

            }
        });


    }
}