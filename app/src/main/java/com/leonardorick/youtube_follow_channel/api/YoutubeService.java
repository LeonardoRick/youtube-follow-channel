package com.leonardorick.youtube_follow_channel.api;

import com.leonardorick.youtube_follow_channel.model.youtubeAPI_data.YoutubeAPIData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeService {
    /**
     * Doc:https://developers.google.com/youtube/v3/docs/search/list
     * GET on https://www.googleapis.com/youtube/v3/
     * search
     * ?part=snippet
     * &order=date
     * &maxResults=200 (default 5)
     * &type="video"
     * &key=AIzaSyDLEMfmlflncPTZYRvUITWCXZ3pI75d1BY
     * &channelId=UCvaZiIpmzfWxA3YV3dJNwnw
     */
    @GET("search")
    public Call<YoutubeAPIData> getVideos(
            @Query("part") String part,
            @Query("order") String order,
            @Query("maxResults") int maxResults,
            @Query("type") String type,
            @Query("key") String key,
            @Query("channelId") String channelId
    );
}
