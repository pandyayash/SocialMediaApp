package technource.greasecrowd.GarageOwnerFragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.CarOwnerFragments.TransactionsCarOwner;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.StaticWebpages;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.WebServiceURLs;

/**
 * Created by technource on 13/9/17.
 */

public class ContactUsGarageOwner extends Fragment {

    View v;
    Context appContext;
    WebView wv;
    private static final String TAG = "WebViewScreen";
    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_contactsus, container, false);

        getViews();
        
        return v;
    }

    public void getViews() {
        appContext = getActivity();
        wv = (WebView) v.findViewById(R.id.wv);
        wv.loadUrl("http://greasecrowd.com.au/contact-us?web-view");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebViewClient(new MyBrowser());

    }

    public static ContactUsGarageOwner newInstance() {
        ContactUsGarageOwner fragment = new ContactUsGarageOwner();
        return fragment;
    }


    private class MyBrowser extends WebViewClient {

        public void onLoadResource(WebView view, String url) {

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, final SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
            builder.setMessage(R.string.notification_error_ssl_cert_invalid);
            builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.proceed();
                }
            });
            builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    handler.cancel();
                }
            });
            final AlertDialog dialog = builder.create();
            dialog.show();
        }

        public void onPageFinished(WebView view, String url) {
            try {
                ((DashboardScreen) appContext).showLoadingDialog(false);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


}
