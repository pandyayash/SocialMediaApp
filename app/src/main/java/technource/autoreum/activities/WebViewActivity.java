package technource.autoreum.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.WebServiceURLs;

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
        WebView webView = findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        String pdf = url;
        AppLog.Log("pdf",pdf);
        webView.loadUrl("https://docs.google.com/gview?embedded=true&url="+url);
        AppLog.Log("pdf1","https://docs.google.com/gview?embedded=true&url="+url);
        //webView.setWebViewClient(new MyWebViewClient());




    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            showLoadingDialog(true);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            AppLog.Log("WebViewActivity",Uri.parse(url).getHost());
            if (Uri.parse(url).getHost().equals("autoreum.com.au")) { //Force to open the url in WEBVIEW
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            finish();
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            showLoadingDialog(false);
        }
    }
}
