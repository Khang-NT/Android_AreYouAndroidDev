package com.hasbrain.areyouandroiddev;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class PostViewActivity extends AppCompatActivity {
    private static final String TAG = "PostViewActivity";
    private WebView wv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.postview);

        String url = getIntent().getStringExtra("URL");
        if (TextUtils.isEmpty(url))
            finish();
        else {
            wv = (WebView) findViewById(R.id.webview);
            wv.getSettings().setBuiltInZoomControls(true);
            wv.getSettings().setDisplayZoomControls(false);

            wv.setWebChromeClient(new WebChromeClient());
            wv.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                    return false;
                }
            });
            wv.loadUrl(url);
        }
    }

    @Override
    public void onBackPressed() {
        if (wv != null && wv.canGoBack())
            wv.goBack();
        else
            super.onBackPressed();
    }
}
