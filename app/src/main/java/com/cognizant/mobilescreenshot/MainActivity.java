package com.cognizant.mobilescreenshot;

import static com.cognizant.mobilescreenshot.Constants.DRM_LICENSE_URL;
import static com.cognizant.mobilescreenshot.Constants.requestHeaderKey;
import static com.cognizant.mobilescreenshot.Constants.requestHeaderValue;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.SurfaceView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManager;
import com.google.android.exoplayer2.drm.DefaultDrmSessionManagerProvider;
import com.google.android.exoplayer2.drm.DrmSessionManager;
import com.google.android.exoplayer2.drm.DrmSessionManagerProvider;
import com.google.android.exoplayer2.drm.FrameworkMediaDrm;
import com.google.android.exoplayer2.drm.HttpMediaDrmCallback;
import com.google.android.exoplayer2.drm.MediaDrmCallback;
import com.google.android.exoplayer2.offline.FilteringManifestParser;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.chunk.ChunkSource;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
//import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.HttpDataSource;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public ExoPlayer simpleExoPlayer;
    public PlayerView playerView;
    public EditText playbackUrl;
    public EditText drmLicenseUrl;
    public EditText headerKey;
    public EditText headerValue;
    public Button loadButton;
    ImageView fullscreenButton;
    public Boolean fullscreen = false;
    SurfaceView surfaceView;
    public DrmSessionManager drmSessionManager;
    public HttpDataSource.Factory dataSourceFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerView = this.findViewById(R.id.playerView);
        playbackUrl = this.findViewById(R.id.videoUrlTxt);
        drmLicenseUrl = this.findViewById(R.id.licenseUrlTxt);
        headerKey = this.findViewById(R.id.headerKeyTxt);
        headerValue = this.findViewById(R.id.headerValueTxt);
        loadButton = this.findViewById(R.id.loadBtn);
        fullscreenButton = playerView.findViewById(R.id.exo_fullscreen_icon);
//        Map<String, String> headers = new HashMap<>();
//        headers.put(requestHeaderKey, requestHeaderValue);
//        dataSourceFactory = new DefaultHttpDataSource.Factory()
//                .setUserAgent(Constants.USER_AGENT)
//                .setDefaultRequestProperties(headers);
//
//        HttpMediaDrmCallback mediaDrmCallback = new HttpMediaDrmCallback(Constants.DRM_LICENSE_URL, dataSourceFactory);
//        drmSessionManager = new DefaultDrmSessionManager.Builder()
//                .setKeyRequestParameters(headers)
//                .setUuidAndExoMediaDrmProvider(Constants.drmSchemeUuid, FrameworkMediaDrm.DEFAULT_PROVIDER)
//                .setMultiSession(false)
//                .build(mediaDrmCallback);

//        setLicenseDetails();

        dataSourceFactory = new DefaultHttpDataSource.Factory()
                .setUserAgent(Constants.USER_AGENT);

        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.URL = playbackUrl.getText().toString().trim();
                Constants.DRM_LICENSE_URL = drmLicenseUrl.getText().toString().trim();
                Constants.requestHeaderKey = headerKey.getText().toString().trim();
                Constants.requestHeaderValue = headerValue.getText().toString().trim();
                if(DRM_LICENSE_URL.length()>0) {
                    setLicenseDetails();
                }
                initialisePlayer();
                Log.e("drmURL", Constants.DRM_LICENSE_URL);
//                surfaceView = (SurfaceView) playerView.getVideoSurfaceView();
            }
        });

        fullscreenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fullscreen) {
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.fullscreen_enter_icon));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = (int) ( 250 * getApplicationContext().getResources().getDisplayMetrics().density);
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                }else{
                    fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.fullscreen_exit_icon));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            |View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            |View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if(getSupportActionBar() != null){
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = params.MATCH_PARENT;
                    params.height = params.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @SuppressLint("ServiceCast")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        super.onResume();
//        initialisePlayer();
        surfaceView = (SurfaceView) playerView.getVideoSurfaceView();
    }


    @Override
    protected void onPause() {
        super.onPause();
        release();
    }

    public void release() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(fullscreen){
            fullscreenButton.setImageDrawable(ContextCompat.getDrawable(MainActivity.this, R.drawable.fullscreen_enter_icon));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            if(getSupportActionBar() != null){
                getSupportActionBar().show();
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) playerView.getLayoutParams();
            params.width = params.MATCH_PARENT;
            params.height = (int) ( 200 * getApplicationContext().getResources().getDisplayMetrics().density);
            playerView.setLayoutParams(params);
            fullscreen = false;
        } else {
            release();
            finish();
        }
    }


    private void setLicenseDetails() {
        Map<String, String> headers = new HashMap<>();
        headers.put(requestHeaderKey, requestHeaderValue);
        dataSourceFactory.setDefaultRequestProperties(headers);
        HttpMediaDrmCallback mediaDrmCallback = new HttpMediaDrmCallback(Constants.DRM_LICENSE_URL, dataSourceFactory);
        drmSessionManager = new DefaultDrmSessionManager.Builder()
                .setKeyRequestParameters(headers)
                .setUuidAndExoMediaDrmProvider(Constants.drmSchemeUuid, FrameworkMediaDrm.DEFAULT_PROVIDER)
                .setMultiSession(false)
                .build(mediaDrmCallback);
    }

    private void initialisePlayer() {
        simpleExoPlayer = new ExoPlayer.Builder(this)
                .build();
        playerView.setPlayer(simpleExoPlayer);

        simpleExoPlayer.setPlayWhenReady(true);

        MediaSource.Factory mediaSourceFactory = new DashMediaSource.Factory(
                new DefaultDashChunkSource.Factory(dataSourceFactory),
                dataSourceFactory
        );

        if(drmLicenseUrl.length()>0) {
            Log.e("drmURL", Constants.DRM_LICENSE_URL);
            DrmSessionManagerProvider drmSessionManagerProvider = new DrmSessionManagerProvider() {
                @Override
                public DrmSessionManager get(MediaItem mediaItem) {
                    return drmSessionManager;
                }
            };
            mediaSourceFactory.setDrmSessionManagerProvider(drmSessionManagerProvider);
        }

        MediaSource mediaSource = mediaSourceFactory.createMediaSource(MediaItem.fromUri(Constants.URL));
        simpleExoPlayer.setMediaSource(mediaSource);
        simpleExoPlayer.prepare();
    }
}