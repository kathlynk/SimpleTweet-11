package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.apps.restclienttemplate.databinding.ActivityDetailBinding;
import org.parceler.Parcels;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail);

        final Tweet tweet = Parcels.unwrap(getIntent().getParcelableExtra("tweet"));

        binding.tvBody.setText(tweet.body);
        binding.tvName.setText(tweet.user.name);
        binding.tvScreenName.setText("@" + tweet.user.screenName);
        binding.tvPostTime.setText(tweet.createdAt);

        Glide.with(getApplicationContext())
                .load(tweet.user.profileImageUrl)
                .placeholder(R.drawable.ic_launcher)
                .into(binding.ivProfileImage);
    }
}