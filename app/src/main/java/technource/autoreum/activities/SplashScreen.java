package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.Splashscreen;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SplashPojo;

public class SplashScreen extends BaseActivity implements View.OnClickListener {

    public static final String TAG = "splash screen";
    public static boolean flagvideo;
    public static boolean flagforsplash;
    private static int TIME_OUT = 3000;
    TextView howitworks, skip;
    ImageView play;
    Intent intent;
    Context appContext;
    ArrayList<SplashPojo> splashPojoArrayList;
    MyPreference myPreference;
    String urlString = "";
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        myPreference = new MyPreference(SplashScreen.this);


        getVies();

        SetOnClicklisteners();
        if (Connectivity.isConnected(appContext)) {
            getVideoUrl();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }

        AppLog.Log(TAG, "Refreshed token is here: " + FirebaseInstanceId.getInstance().getToken());


    }

    public void getVies() {
        appContext = this;
        howitworks = findViewById(R.id.howitworks);
        skip = findViewById(R.id.skip);
        play = findViewById(R.id.play);
        splashPojoArrayList = new ArrayList<>();
        flagvideo = false;
        flagforsplash = false;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("flag")) {
                flagforsplash = true;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
            checkUser();
        }
    }

    public void SetOnClicklisteners() {

        howitworks.setOnClickListener(this);
        skip.setOnClickListener(this);
        play.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.howitworks:
                flagforsplash = true;
                intent = new Intent(SplashScreen.this, How_it_works.class);
                startActivity(intent);
                break;
            case R.id.skip:
                flagforsplash = true;
                if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                    intent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();

                } else {
                    intent = new Intent(SplashScreen.this, StaticScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();
                }
                break;
            case R.id.play:
                flagforsplash = true;
                if (Connectivity.isConnected(appContext)) {
                    if (flagvideo == false) {
                        if (!urlString.equalsIgnoreCase("")) {

                            intent = new Intent(SplashScreen.this, DialogActivity.class);
                            intent.putExtra("String", urlString);
                            startActivity(intent);
                        }
                    }
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                break;
        }
    }


    private void getVideoUrl() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        final String[] url = {WebServiceURLs.BASE_URL_VIDEO};
        AppLog.Log(TAG, "App URL : " + url[0]);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Splashscreen.SERVICE_NAME,
                Constants.Splashscreen.SPLASH_VIDEO);
        AppLog.Log(TAG, "parameters : " + new JSONObject(postParam).toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url[0], new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log(TAG, "RESPONSE : " + response);
                        String str_status;
                        try {
                            str_status = response.getString(Constants.STATUS);
                            AppLog.Log(TAG, "Status in Login" + str_status);
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    urlString = response.getString(Constants.Splashscreen.VIDEO);
                                    if (flagvideo == false) {
                                        if (urlString.equalsIgnoreCase("")) {
                                            play.setVisibility(View.INVISIBLE);
                                        }
                                    }

                                } else if (str_status.equals(Constants.FAILURE) && response.getString("msg")
                                        .contains("invalid")) {
                                }
                            }
                        } catch (Exception e) {
                            AppLog.Log(TAG, "Error in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Log(TAG, "Error : " + error.getMessage());
                error.printStackTrace();
                showLoadingDialog(false);
            }
        }) {

            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        queue.add(jsonObjReq);


    }

    private void checkUser() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        final String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Splashscreen.SERVICE_NAME, "usr_adm_st");
        postParam.put("user_type", loginDetail_dbo.getUser_Type());
        AppLog.Log(TAG, "parameters  : " + loginDetail_dbo.getJWTToken());
        AppLog.Log(TAG, "parameters : " + new JSONObject(postParam).toString());
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log(TAG, "RESPONSE : " + response);
                        String str_status;
                        try {
                            str_status = response.getString(Constants.STATUS);
                            AppLog.Log(TAG, "Status in Login" + str_status);
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    if (response.getString("deleted").equalsIgnoreCase("1") || response.getString("active").equalsIgnoreCase("0")) {

                                        if (response.getString("deleted").equalsIgnoreCase("1")) {
                                            showAlertDialog("Your account has been deleted from admin, so you are being logged out of the system");
                                        } else if (response.getString("active").equalsIgnoreCase("0")) {
                                            showAlertDialog("Your account has been deactivated from admin, so you are being logged out of the system");
                                        }
                                        myPreference.removeBooleanReponse();
                                        myPreference.removeIntegerReponse();
                                        myPreference.removeStringReponse();
                                        HelperMethods.deleteUserDetailsSharedPreferences(appContext);
                                    } else {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!flagforsplash) {
                                                    Intent mainIntent = new Intent(SplashScreen.this, DashboardScreen.class);
                                                    startActivity(mainIntent);
                                                    finish();
                                                    activityTransition();
                                                }
                                            }
                                        }, 500);
                                    }

                                } else {
                                    myPreference.removeBooleanReponse();
                                    myPreference.removeIntegerReponse();
                                    myPreference.removeStringReponse();
                                    HelperMethods.deleteUserDetailsSharedPreferences(appContext);
                                }
                            }
                        } catch (Exception e) {
                            AppLog.Log(TAG, "Error in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                AppLog.Log(TAG, "Error : " + error.getMessage());
                error.printStackTrace();
                showLoadingDialog(false);
            }
        });
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        jsonObjReq.setShouldCache(false);
        queue.add(jsonObjReq);


    }

    private void callActivity(int l1, int l2) {

        new CountDownTimer(l1, l2) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                    Intent intent = new Intent(SplashScreen.this, DashboardScreen.class);
                    startActivity(intent);
                    activityTransition();
                    finish();

                } else {
                    Intent intent = new Intent(SplashScreen.this, StaticScreen.class);
                    startActivity(intent);
                    activityTransition();
                    finish();
                }

            }
        }.start();
    }
}



