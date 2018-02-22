package technource.greasecrowd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.WebServiceURLs;

public class WebViewActivity extends BaseActivity {

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        setHeader("Quote");
        Intent intent = getIntent();
        if (intent!=null){
            url = intent.getStringExtra("url");
        }
        WebView webView = (WebView) findViewById(R.id.webView);
        //webView.getSettings().setJavaScriptEnabled(true);
        String pdf = WebServiceURLs.BASE_URL_IMAGE_PROFILE+url;
        AppLog.Log("pdf",pdf);
        webView.loadUrl("http://docs.google.com/gview?embedded=true&url=" + pdf);



    }
}
