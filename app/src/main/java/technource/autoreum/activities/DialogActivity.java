package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import technource.autoreum.R;
import static technource.autoreum.activities.SplashScreen.flagvideo;

public class DialogActivity extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "splash screen";

    WebView video;
    ImageView close;
    String url;
    Context appContext;
    boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialuge);
        Intent intent = getIntent();
        close= findViewById(R.id.close);
        appContext = this;
        url = intent.getStringExtra("String");
        if (flag) {
            OpenWebview(appContext);
        }
       close.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        flagvideo = false;
        super.onConfigurationChanged(newConfig);
    }
    public void OpenWebview(final Context context) {
        video = findViewById(R.id.videoweb);
        video.loadUrl(url);
        video.getSettings().setDomStorageEnabled(true);
        video.getSettings().setJavaScriptEnabled(true);
        video.getSettings().setPluginState(WebSettings.PluginState.ON);
        video.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SplashScreen.flagforsplash = false;
        finish();
    }
}