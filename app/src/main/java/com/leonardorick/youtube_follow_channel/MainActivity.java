package com.leonardorick.youtube_follow_channel;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;

import com.leonardorick.youtube_follow_channel.activity.PlayerActivity;
import com.leonardorick.youtube_follow_channel.adapter.VideosAdapter;
import com.leonardorick.youtube_follow_channel.api.YoutubeService;
import com.leonardorick.youtube_follow_channel.helper.Constants;
import com.leonardorick.youtube_follow_channel.helper.RecyclerItemClickListener;
import com.leonardorick.youtube_follow_channel.helper.RetrofitConfig;
import com.leonardorick.youtube_follow_channel.helper.YoutubeConfig;
import com.leonardorick.youtube_follow_channel.model.youtubeAPI_data.YoutubeAPIData;
import com.leonardorick.youtube_follow_channel.model.youtubeAPI_data.YoutubeVideoItem;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerViewVideos;
    private MaterialSearchView searchView;
    private VideosAdapter adapter;
    private List<YoutubeVideoItem> videosList = new ArrayList<>();

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true); // show backButton

        // retrofit config
        retrofit = RetrofitConfig.getRetrofit();
        configSearchView();

        // Reading data
        SharedPreferences sharedPref = getSharedPreferences(
                Constants.SharedPreferencesKeys.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String channelId = sharedPref.getString(Constants.SharedPreferencesKeys.channelInfo,
                Constants.SharedPreferencesKeys.DEFAULT);
        recoverVideosFromChannel(channelId);
    }


    private void configRecyclerView() {
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);
        recyclerViewVideos.setHasFixedSize(true);
        // Layout Manager
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));
        // Adapter
        updateAdapter(videosList);
        configRecycclerViewListener();
    }

    private void updateAdapter(List<YoutubeVideoItem> videosList) {
        adapter = new VideosAdapter(videosList);
        recyclerViewVideos.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void recoverVideosFromChannel(String channelId) {

        YoutubeService youtubeService = retrofit.create(YoutubeService.class);
        youtubeService.getVideos(
                "snippet",
                "date",
                200,
                "video",
                YoutubeConfig.GOOGLE_API_KEY,
                channelId
        ).enqueue(new Callback<YoutubeAPIData>() {
            @Override
            public void onResponse(Call<YoutubeAPIData> call, Response<YoutubeAPIData> response) {
                if (response.isSuccessful()) {
                    YoutubeAPIData result = response.body();
                    videosList = result.items;
                    configRecyclerView();
                }
            }

            @Override
            public void onFailure(Call<YoutubeAPIData> call, Throwable t) { }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        searchView.setMenuItem(menu.findItem(R.id.menuSearch));
        return super.onCreateOptionsMenu(menu);
    }

    private void configSearchView() {
        searchView = findViewById(R.id.searchView);

        // listener to searchView (open and close bar)
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() { }

            @Override
            public void onSearchViewClosed() {
                updateAdapter(videosList);
            }
        });

        // listener to search in did
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.isEmpty())
                    search(newText.toLowerCase());
                return true;
            }
        });
    }

    private void search(String newText) {
        List<YoutubeVideoItem> videosFilteredList = new ArrayList<>();
        for (YoutubeVideoItem video : videosList) {
            String title = video.snippet.title.toLowerCase();
            if (title.contains(newText))
                videosFilteredList.add(video);
        }
        updateAdapter(videosFilteredList);
    }

    private void configRecycclerViewListener() {
        recyclerViewVideos.addOnItemTouchListener(
                new RecyclerItemClickListener(
                        getApplicationContext(),
                        recyclerViewVideos,
                        new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                YoutubeVideoItem item = videosList.get(position);
                                String videoId = item.id.videoId;
                                Intent intent = new Intent(getApplicationContext(), PlayerActivity.class);
                                intent.putExtra(Constants.IntentKey.videoIdKey, videoId);
                                startActivity(intent);
                            }

                            @Override
                            public void onLongItemClick(View view, int position) { }

                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) { }
                        }
                )
        );
    }
}