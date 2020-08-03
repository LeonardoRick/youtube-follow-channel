package com.leonardorick.youtube_follow_channel.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.leonardorick.youtube_follow_channel.R;
import com.leonardorick.youtube_follow_channel.helper.Constants;
import com.leonardorick.youtube_follow_channel.helper.YoutubeConfig;

public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {

    private YouTubePlayerView youTubePlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initializePlayer();
    }

    private void initializePlayer() {
        youTubePlayer = findViewById(R.id.youTubePlayer);
        youTubePlayer.initialize(YoutubeConfig.GOOGLE_API_KEY, this);
    }
    private String getVideoId() {
        if (getIntent().getExtras() != null)
            return getIntent().getExtras().getString(Constants.IntentKey.videoIdKey);
        return null;
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        youTubePlayer.setFullscreen(true);
        youTubePlayer.setShowFullscreenButton(false);
        String videoId = getVideoId();
        if(!wasRestored)
            youTubePlayer.loadVideo(videoId);
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Erro ao iniciar o video" + youTubeInitializationResult.toString() , Toast.LENGTH_SHORT).show();
    }
}