package com.leonardorick.youtube_follow_channel.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.leonardorick.youtube_follow_channel.MainActivity;
import com.leonardorick.youtube_follow_channel.R;
import com.leonardorick.youtube_follow_channel.helper.Constants;

import org.apache.commons.validator.routines.UrlValidator;

public class SetChannelActivity extends AppCompatActivity {

    private EditText inputUrl;
    // regex to mack
    private final String channelIdPatternRegex = "^(?:(http|https):\\/\\/[a-zA-Z-]*\\.{0,1}[a-zA-Z-]{3,}\\.[a-z]{2,})\\/channel\\/([a-zA-Z0-9_]{3,})$";
    private SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_channel);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("YouTube Follow Channel");
        setSupportActionBar(toolbar);

        inputUrl = findViewById(R.id.editTextInputURl);

        // Reading data
        sharedPref = getSharedPreferences(
                Constants.SharedPreferencesKeys.PREFERENCE_FILE_KEY, Context.MODE_PRIVATE);
        String channelId = sharedPref.getString(Constants.SharedPreferencesKeys.channelInfo,
                Constants.SharedPreferencesKeys.DEFAULT);

        if (channelId != Constants.SharedPreferencesKeys.DEFAULT)
            startActivity(new Intent(this, MainActivity.class));

    }

    public void goToMain(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        Editable inputText = inputUrl.getText();
        String[] schemes = {"https"};
        UrlValidator urlValidator = new UrlValidator(schemes);

        if (inputText != null && !inputText.toString().isEmpty()) {


            if (urlValidator.isValid(inputText.toString())) {
                Uri url = Uri.parse(inputUrl.getText().toString());
                if (url.getLastPathSegment() != null &&
                        url.toString().matches(channelIdPatternRegex)) {
                    saveSharedPreference(url.getLastPathSegment());
                    Log.d("TAG", "goToMain: " + url.getLastPathSegment());
                } else
                    Toast.makeText(this, "Insira uma Url válida", Toast.LENGTH_SHORT).show();
            } else
                Toast.makeText(this, "Insira uma Url válida", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(this, "Insira um valor antes de avançar", Toast.LENGTH_SHORT).show();
    }

    private void saveSharedPreference(String channelId) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.SharedPreferencesKeys.channelInfo, channelId);
        if(editor.commit())
            startActivity(new Intent(this, MainActivity.class));
        else
            Toast.makeText(this, "Erro ao salvar a URL, tente novamenta mais tarde", Toast.LENGTH_SHORT).show();
    }
}