package com.varunsen.gurughantal.activities;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.varunsen.gurughantal.R;
import com.varunsen.gurughantal.classes.Variables;

public class LectureDisplayActivity extends YouTubeBaseActivity {

    private YouTubePlayerView youTubePlayerView;
    private String title, url;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lecture_display);

        youTubePlayerView = findViewById(R.id.player);
        webView = findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebChromeClient(new WebChromeClient());

        if (getIntent() != null) {
            url = getIntent().getStringExtra("url");
            title = getIntent().getStringExtra("title");


            if (url.length() == 11) {

                //video is from youtube

                youTubePlayerView.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);

                youTubePlayerView.initialize(Variables.youtube_api_key,
                        new YouTubePlayer.OnInitializedListener() {
                            @Override
                            public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                                YouTubePlayer youTubePlayer, boolean b) {

                                youTubePlayer.loadVideo(url);

                            }

                            @Override
                            public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                                YouTubeInitializationResult youTubeInitializationResult) {


                            }
                        });
            } else if (url.length() == 9) {

                //video is from vimeo

                youTubePlayerView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

                String vimeoUrl = "<iframe width=\"100%\" height=\"100%\" src=\"https://player.vimeo.com/video/" + url +
                        "frameborder=\"0\" allow=\"autoplay; fullscreen; picture-in-picture\" allowfullscreen ></iframe>";

                webView.loadData(vimeoUrl, "text/html", "utf-8");

            } else {

                youTubePlayerView.setVisibility(View.GONE);
                webView.setVisibility(View.VISIBLE);

                webView.loadData(url, "text/html", "utf-8");

            }
        } else {
            Toast.makeText(this, "Intent is null! Please contact support.", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private class ChromeClient extends WebChromeClient {
        private View mCustomView;
        private WebChromeClient.CustomViewCallback mCustomViewCallback;
        protected FrameLayout mFullscreenContainer;
        private int mOriginalOrientation;
        private int mOriginalSystemUiVisibility;

        ChromeClient() {
        }

        public Bitmap getDefaultVideoPoster() {
            if (mCustomView == null) {
                return null;
            }
            return BitmapFactory.decodeResource(getResources(), 2130837573);
        }

        public void onHideCustomView() {
            ((FrameLayout) getWindow().getDecorView()).removeView(this.mCustomView);
            this.mCustomView = null;
            getWindow().getDecorView().setSystemUiVisibility(this.mOriginalSystemUiVisibility);
            setRequestedOrientation(this.mOriginalOrientation);
            this.mCustomViewCallback.onCustomViewHidden();
            this.mCustomViewCallback = null;
        }

        public void onShowCustomView(View paramView, WebChromeClient.CustomViewCallback paramCustomViewCallback) {
            if (this.mCustomView != null) {
                onHideCustomView();
                return;
            }
            this.mCustomView = paramView;
            this.mOriginalSystemUiVisibility = getWindow().getDecorView().getSystemUiVisibility();
            this.mOriginalOrientation = getRequestedOrientation();
            this.mCustomViewCallback = paramCustomViewCallback;
            ((FrameLayout) getWindow().getDecorView()).addView(this.mCustomView, new FrameLayout.LayoutParams(-1, -1));
            getWindow().getDecorView().setSystemUiVisibility(3846 | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }

}