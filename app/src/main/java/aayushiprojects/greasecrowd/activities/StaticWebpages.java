package aayushiprojects.greasecrowd.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

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

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;

public class StaticWebpages extends BaseActivity {

    Context appContext;
    String type;

    String URL;
    String postdata;
    LinearLayout ll_back;
    WebView wv;
    private static final String TAG = "WebViewScreen";
    final String mimeType = "text/html";
    final String encoding = "UTF-8";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_webpages);
        getViews();


    }

    private void getViews() {

        appContext = this;
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        final Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("page_id")) {
                type = intent.getStringExtra("flag");
                URL = intent.getStringExtra("page_id");
                setHeader(type);
                if (Connectivity.isConnected(appContext)) {
                    GetDetails();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
            }
            if (intent.hasExtra("con_us")) {
                setHeader("Contact Us");
                if (Connectivity.isConnected(appContext)) {
                    GetContact();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
            }

        }

        wv = findViewById(R.id.wv);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
    }

    private void GetContact() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.Forgotpassword.SERVICE_NAME, "get_contact_us_content");

        AppLog.Log("TAG", "parameters : " + new JSONObject(postParam).toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("TAG", "RESPONSE : " + response);
                        String str_status;
                        try {
                            str_status = response.getString(Constants.STATUS);
                            AppLog.Log("TAG", "Status in Login" + str_status);
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    try {
                                        String status = response.getString(Constants.STATUS);
                                        if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {

                                            String summary = "<html><body>" + response.getString("content") + "</body></html>";
                                            wv.loadData(summary, mimeType, encoding);

                                            wv.getSettings().setJavaScriptEnabled(true);
                                            wv.setWebViewClient(new MyBrowser());

                                        } else {
                                            if (status != null && status.equals(Constants.FAILURE)) {

                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else if (str_status.equals(Constants.FAILURE)) {

                                    showAlertDialog(response.getString(Constants.MESSAGE));
                                }
                            }
                        } catch (Exception e) {
                            AppLog.Log("TAG", "Error in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Log("TAG", "Error : " + error.getMessage());
                error.printStackTrace();
                showLoadingDialog(false);

            }


        }) {

            @Override
            public Map<String, String> getHeaders() {
//                    String auth = "Bearer " + loginDetail_dbo.getLogin_token();
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", auth);
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        //  AppController.getInstance().addToRequestQueue(jsonObjReq);
        queue.add(jsonObjReq);
    }

    private void GetDetails() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.Forgotpassword.SERVICE_NAME, "static_pages");
        postParam.put("page_id", URL);

        AppLog.Log("TAG", "parameters : " + new JSONObject(postParam).toString());

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("TAG", "RESPONSE : " + response);
                        String str_status;
                        try {
                            str_status = response.getString(Constants.STATUS);
                            AppLog.Log("TAG", "Status in Login" + str_status);
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    try {
                                        String status = response.getString(Constants.STATUS);
                                        if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {

                                            String content = new String((Base64.decode(response.getString("page_content"), Base64.DEFAULT)));

                                            String summary = "<html><body>" + content + "</body></html>";
                                            AppLog.Log("Base 64", " " + new String((Base64.decode(response.getString("page_content"), Base64.DEFAULT))));
                                            wv.loadData(summary, mimeType, encoding);

                                            wv.getSettings().setJavaScriptEnabled(true);
                                            wv.setWebViewClient(new MyBrowser());

                                        } else {
                                            if (status != null && status.equals(Constants.FAILURE)) {

                                            }
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }


                                } else if (str_status.equals(Constants.FAILURE)) {

                                    showAlertDialog(response.getString(Constants.MESSAGE));
                                }
                            }
                        } catch (Exception e) {
                            AppLog.Log("TAG", "Error in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Log("TAG", "Error : " + error.getMessage());
                error.printStackTrace();
                showLoadingDialog(false);
            }


        }) {

            @Override
            public Map<String, String> getHeaders() {
//                    String auth = "Bearer " + loginDetail_dbo.getLogin_token();
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", auth);
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        //  AppController.getInstance().addToRequestQueue(jsonObjReq);
        queue.add(jsonObjReq);


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
            final AlertDialog.Builder builder = new AlertDialog.Builder(appContext,R.style.AppCompatAlertDialogStyle);
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
                showLoadingDialog(false);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }
}
