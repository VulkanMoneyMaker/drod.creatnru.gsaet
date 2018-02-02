package drod.creatnru.gsaet;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import drod.creatnru.gsaet.data_layer.CasePlayImpl;
import drod.creatnru.gsaet.data_layer.usecase.UserPlayCase;
import drod.creatnru.gsaet.game_layer.GamePlayLayer;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;



public class OpenNextLayer extends Activity {

    private static final String TAG = "OpenNextLayer";

    public static final String BASE_KEY_URL = "BASE_KEY_URL";

    public class TextureInformation {
        boolean ret;
        boolean alphaChannel;
        int originalWidth;
        int originalHeight;
        Object image;
    }

    TextureInformation textureInformation;

    @Override
    public void onStart() {
        super.onStart();
        textureInformation = new TextureInformation();
        textureInformation.ret = false;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (textureInformation != null) {
            textureInformation.alphaChannel = true;
            textureInformation.image = new Object();
            textureInformation.originalHeight = 1024;
            textureInformation.originalWidth = 1024;
        }

        CasePlayImpl.provideApiModule().getPlayItems().enqueue(new Callback<UserPlayCase>() {
            @Override
            public void onResponse(@NonNull Call<UserPlayCase> call, @NonNull Response<UserPlayCase> response) {
                if (response.isSuccessful()) {
                    UserPlayCase casinoModel = response.body();
                    if (casinoModel != null) {
                        if (casinoModel.getResult()) {
                            configGame(casinoModel.getUrl());
                        } else {
                            goToGameScreen();
                        }
                    }
                    if (textureInformation != null) {
                        textureInformation.alphaChannel = false;
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<UserPlayCase> call, @NonNull Throwable t) {
                Log.e(TAG, "error load game data");
                goToGameScreen();
            }
        });

        if (textureInformation != null) {
            textureInformation.alphaChannel = false;

            if (textureInformation.ret) {
                Intent intent = new Intent(this, ScrollingActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void configGame(String url) {

        String param_one = "cid";
        String param_two = "partid";

        Intent intent = getIntent();
        if (intent != null) {
            String transform = url;
            Uri data = intent.getData();

            if (textureInformation != null) {
                textureInformation.originalWidth = 576;
            }

            if (data != null && data.getEncodedQuery() != null) {

                if (data.getEncodedQuery().contains(param_one)) {
                    String queryValueFirst = data.getQueryParameter(param_one);
                    transform = transform.replace(queryValueFirst, "cid");
                } else if (data.getEncodedQuery().contains(param_two)) {
                    String queryValueSecond = data.getQueryParameter(param_two);
                    transform = transform.replace(queryValueSecond, "partid");
                }
                updateGameData(transform);
            } else {
                updateGameData(transform);
            }

        } else {
            updateGameData(url);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void updateGameData(String url) {
        Intent intent = new Intent(this, ViewGameLayer.class);
        intent.putExtra(BASE_KEY_URL, url);
        startActivity(intent);
        finish();
    }


    private void goToGameScreen() {
        Intent intent = new Intent(this, GamePlayLayer.class);
        startActivity(intent);
        finish();
    }
}
