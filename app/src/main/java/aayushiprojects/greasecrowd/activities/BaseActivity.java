package aayushiprojects.greasecrowd.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.CustomViews.LoadingDialog.CustomDialog;
import aayushiprojects.greasecrowd.CustomViews.LoadingDialog.LoadingDialog;
import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.AskTheCrowd.AskTheCrowdStepOne;
import aayushiprojects.greasecrowd.activities.PostJob.EditJobStepOne;
import aayushiprojects.greasecrowd.activities.PostJob.PostNewJobStepOne;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.CarImageDBO;
import aayushiprojects.greasecrowd.model.CarVideosDbo;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

import static aayushiprojects.greasecrowd.helper.Constants.isFromJobDetails;


/**
 * Created by technource on 18/7/17.
 */
public class BaseActivity extends AppCompatActivity implements View.OnClickListener {

    static public String job_id = "", cat_id = "", fourjobid = "", currentjobstatus = "";

    // Progress Dialog
    public ProgressDialog pDialog;
    public boolean isAskCrowd = false;
    LoadingDialog dialog = new LoadingDialog(BaseActivity.this);
    CustomDialog dialogC;
    LinearLayout ll_back, ll_for_job, ll_for_crowd, ll_for_garageowner, ll_post_jobs, ll_home, ll_myjobs, ll_postjobs_crowd, ll_home_crowd, ll_myjobs_crowd, ll_postjob_garageowner, ll_home_garage, ll_myjobs_garage, ll_for_jobdetails, ll_job_details, ll_Quotes, ll_discussion;
    TextView tv_postjob, tv_home, tv_my_jobs, tv_postjob_crowd, tv_home_crowd, tv_my_jobs_crowd, tv_header, tv_postjob_garage, tv_home_garage, tv_my_jobs_garage, tv_job_details, tv_discussion, tv_quotes;
    ImageView iv_post_image, iv_home_image, iv_jobs_image, iv_post_image_crowd, iv_home_image_crowd, iv_jobs_image_crowd, iv_post_image_garage, iv_home_image_garage, iv_jobs_image_garage, iv_job_details, iv_quotes, iv_discussion;
    ArrayList<CarImageDBO> carImageArrayList;
    ArrayList<CarVideosDbo> carVideosArrayList;
    public Context appContext;
    LoginDetail_DBO loginDetail_dbo;
    String jwt;
    String cjob_id = "", job_title = "";
    int current = 1;
    boolean flag = false;
    String job_idBaseActivity = "";


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static void expand(final View v) {
        v.measure(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1 ? ActionBar.LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        v.startAnimation(a);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    public void showLoadingDialog(boolean isShow) {
        if (isShow) {
            dialog.loading();
        } else {
            dialog.dismissDialog();
        }
    }

    public void showLoadingDialogText(boolean isShow, String text) {
        if (isShow) {
            dialog.loadingText(text);
        } else {
            dialog.dismissDialog();
        }
    }

    public void setHeader(String title) {
        tv_header = findViewById(R.id.tv_header);
        tv_header.setText(title);

    }

    public void setlistenrforfooter() {
        ll_post_jobs.setOnClickListener(this);
        ll_myjobs.setOnClickListener(this);
        ll_home.setOnClickListener(this);

        ll_postjobs_crowd.setOnClickListener(this);
        ll_myjobs_crowd.setOnClickListener(this);
        ll_home_crowd.setOnClickListener(this);

        ll_postjob_garageowner.setOnClickListener(this);
        ll_home_garage.setOnClickListener(this);
        ll_myjobs_garage.setOnClickListener(this);

        ll_job_details.setOnClickListener(this);
        ll_Quotes.setOnClickListener(this);
        ll_discussion.setOnClickListener(this);
    }

    public void setfooter(String key) {
        ll_postjobs_crowd = findViewById(R.id.ll_postjob_crowd);
        ll_myjobs_crowd = findViewById(R.id.ll_myjobs_crowd);
        ll_home_crowd = findViewById(R.id.ll_home_crowd);
        ll_for_jobdetails = findViewById(R.id.ll_for_jobdetails);

        ll_post_jobs = findViewById(R.id.ll_postjob);
        ll_myjobs = findViewById(R.id.ll_myjobs);
        ll_home = findViewById(R.id.ll_home);


        ll_postjob_garageowner = findViewById(R.id.ll_postjob_garageowner);
        ll_home_garage = findViewById(R.id.ll_home_garage);
        ll_myjobs_garage = findViewById(R.id.ll_myjobs_garage);


        ll_job_details = findViewById(R.id.ll_job_details);
        ll_Quotes = findViewById(R.id.ll_Quotes);
        ll_discussion = findViewById(R.id.ll_discussion);


        tv_postjob_garage = findViewById(R.id.tv_postjob_garage);
        tv_home_garage = findViewById(R.id.tv_home_garage);
        tv_my_jobs_garage = findViewById(R.id.tv_my_jobs_garage);

        tv_job_details = findViewById(R.id.tv_job_details);
        tv_quotes = findViewById(R.id.tv_quotes);
        tv_discussion = findViewById(R.id.tv_discussion);


        iv_post_image_garage = findViewById(R.id.iv_post_image_garage);
        iv_home_image_garage = findViewById(R.id.iv_home_image_garage);
        iv_jobs_image_garage = findViewById(R.id.iv_jobs_image_garage);

        iv_job_details = findViewById(R.id.iv_job_details);
        iv_quotes = findViewById(R.id.iv_quotes);
        iv_discussion = findViewById(R.id.iv_discussion);


        ll_for_job = findViewById(R.id.ll_for_job);
        ll_for_crowd = findViewById(R.id.ll_for_crowd);
        ll_for_garageowner = findViewById(R.id.ll_for_garageowner);


        tv_postjob = findViewById(R.id.tv_postjob);
        tv_home = findViewById(R.id.tv_home);
        tv_my_jobs = findViewById(R.id.tv_my_jobs);

        iv_post_image = findViewById(R.id.iv_post_image);
        iv_home_image = findViewById(R.id.iv_home_image);
        iv_jobs_image = findViewById(R.id.iv_jobs_image);


        tv_postjob_crowd = findViewById(R.id.tv_postjob_crowd);
        tv_home_crowd = findViewById(R.id.tv_home_crowd);
        tv_my_jobs_crowd = findViewById(R.id.tv_my_jobs_crowd);


        iv_post_image_crowd = findViewById(R.id.iv_post_image_crowd);
        iv_home_image_crowd = findViewById(R.id.iv_home_image_crowd);
        iv_jobs_image_crowd = findViewById(R.id.iv_jobs_image_crowd);

        if (key.equalsIgnoreCase("jobs")) {
            ll_for_job.setVisibility(View.VISIBLE);
            ll_for_crowd.setVisibility(View.GONE);
            ll_for_garageowner.setVisibility(View.GONE);
            ll_for_jobdetails.setVisibility(View.GONE);

        } else if (key.equalsIgnoreCase("crowd")) {
            ll_for_job.setVisibility(View.GONE);
            ll_for_crowd.setVisibility(View.VISIBLE);
            ll_for_jobdetails.setVisibility(View.GONE);
            ll_for_garageowner.setVisibility(View.GONE);

        } else if (key.equalsIgnoreCase("garageowner")) {
            ll_for_job.setVisibility(View.GONE);
            ll_for_crowd.setVisibility(View.GONE);
            ll_for_jobdetails.setVisibility(View.GONE);
            ll_for_garageowner.setVisibility(View.VISIBLE);
        } else if (key.equalsIgnoreCase("job_details")) {
            ll_for_job.setVisibility(View.GONE);
            ll_for_crowd.setVisibility(View.GONE);
            ll_for_garageowner.setVisibility(View.GONE);
            ll_for_jobdetails.setVisibility(View.VISIBLE);
        }
    }

    public void setPostJObFooter(Context appContext) {
        current = 2;
        tv_postjob.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_home.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));


        iv_post_image.setImageResource(R.drawable.add_hover);
        iv_home_image.setImageResource(R.drawable.home_normal);
        iv_jobs_image.setImageResource(R.drawable.carlist_normal);


    }

    public void setHomeFooter(Context appContext) {
        CheckForRegisteredCar();
        current = 1;
        tv_postjob.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_my_jobs.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_post_image.setImageResource(R.drawable.add_normal);
        iv_home_image.setImageResource(R.drawable.home_hover);
        iv_jobs_image.setImageResource(R.drawable.carlist_normal);


    }

    public void setMyJobsFooter(Context appContext) {
        CheckForRegisteredCar();
        current = 3;
        tv_postjob.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));

        iv_post_image.setImageResource(R.drawable.add_normal);
        iv_home_image.setImageResource(R.drawable.home_normal);
        iv_jobs_image.setImageResource(R.drawable.carlist_hover);

    }
    public void setNoFooter(Context appContext) {
        //CheckForRegisteredCar();
        current = 101;
//        tv_postjob.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
//        tv_home.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
//        tv_my_jobs.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
//
//        iv_post_image.setImageResource(R.drawable.add_normal);
//        iv_home_image.setImageResource(R.drawable.home_normal);
//        iv_jobs_image.setImageResource(R.drawable.carlist_hover);

    }

    public void setMyJObFooterGarage(Context appContext) {
        current = 2;
        tv_postjob_garage.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_home_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_post_image_garage.setImageResource(R.drawable.carlist_hover);
        iv_home_image_garage.setImageResource(R.drawable.home_normal);
        iv_jobs_image_garage.setImageResource(R.drawable.carlist_normal);

    }

    public void setHomeFooterGarage(Context appContext) {
        current = 1;
        tv_postjob_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home_garage.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_my_jobs_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_post_image_garage.setImageResource(R.drawable.carlist_normal);
        iv_home_image_garage.setImageResource(R.drawable.home_hover);
        iv_jobs_image_garage.setImageResource(R.drawable.carlist_normal);

    }

    public void setMyCrowdFooterGarage(Context appContext) {
        current = 3;
        tv_postjob_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home_garage.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs_garage.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));

        iv_post_image_garage.setImageResource(R.drawable.carlist_normal);
        iv_home_image_garage.setImageResource(R.drawable.home_normal);
        iv_jobs_image_garage.setImageResource(R.drawable.carlist_hover);

    }


    public void setPostCrowdFooter(Context appContext) {
        current = 2;
        tv_postjob_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_home_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        iv_post_image_crowd.setImageResource(R.drawable.add_hover);
        iv_home_image_crowd.setImageResource(R.drawable.home_normal);
        iv_jobs_image_crowd.setImageResource(R.drawable.carlist_normal);
    }

    public void setHomeFooterCrowd(Context appContext) {
        CheckForRegisteredCar();
        current = 1;
        tv_postjob_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_my_jobs_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_post_image_crowd.setImageResource(R.drawable.add_normal);
        iv_home_image_crowd.setImageResource(R.drawable.home_hover);
        iv_jobs_image_crowd.setImageResource(R.drawable.carlist_normal);
    }

    public void setMyCrowdFooter(Context appContext) {
        CheckForRegisteredCar();
        current = 3;
        tv_postjob_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_home_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_my_jobs_crowd.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));

        iv_post_image_crowd.setImageResource(R.drawable.add_normal);
        iv_home_image_crowd.setImageResource(R.drawable.home_normal);
        iv_jobs_image_crowd.setImageResource(R.drawable.carlist_hover);
    }

    public void setJobDetailsFooter(Context appContext) {
        current = 1;
        tv_job_details.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_quotes.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_discussion.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_job_details.setImageResource(R.drawable.info_p);
        iv_quotes.setImageResource(R.drawable.quotes_g);
        iv_discussion.setImageResource(R.drawable.discussion_g);
    }

    public void setJobDetailsQuoteFooter(Context appContext) {
        current = 2;
        tv_job_details.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_quotes.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));
        tv_discussion.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));

        iv_job_details.setImageResource(R.drawable.info);
        iv_quotes.setImageResource(R.drawable.quotes_p);
        iv_discussion.setImageResource(R.drawable.discussion_g);
    }

    public void setJobDetailsDiscussionFooter(Context appContext) {
        current = 3;
        tv_job_details.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_quotes.setTextColor(ContextCompat.getColor(appContext, R.color.text_color));
        tv_discussion.setTextColor(ContextCompat.getColor(appContext, R.color.colorPrimary));

        iv_job_details.setImageResource(R.drawable.info);
        iv_quotes.setImageResource(R.drawable.quotes_g);
        iv_discussion.setImageResource(R.drawable.discussion_p);
    }


    public void activityTransition() {
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    public void showAlertDialog(String message) {

        dialogC = new CustomDialog(this, "ALERT", message);
        dialogC.setCanceledOnTouchOutside(false);
        if (dialogC != null && dialogC.isShowing()) {
            dialogC.dismiss();
            dialogC.show();
        } else {
            dialogC.show();
        }
    }

    public String EncodeBAse64(String text) {
        byte[] data = new byte[0];
        try {
            data = text.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String base64 = Base64.encodeToString(data, Base64.DEFAULT);

        return base64;
    }

    public String DecodeBase64(String text) {
        try {
            String text1 = "";
            byte[] data = Base64.decode(text, Base64.DEFAULT);
            text1 = new String(data, "UTF-8");
            return text1;
        } catch (Exception e) {
            return text;
        }


    }

    public boolean IsBase64Encoded(String value) {
        try {
            byte[] decodedString = Base64.decode(value, Base64.DEFAULT);
            BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void CheckForRegisteredCar() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        //showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "has_cars");
        params.put(Constants.PostNewJob.JWT, jwt);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        //showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.optString(Constants.STATUS);
                            flag = status.equalsIgnoreCase(Constants.SUCCESS);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flag = false;
                //showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);

    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_postjob:
                if (current != 2) {

                    if (Connectivity.isConnected(appContext)) {
                        validateJobSteps();
                    } else {
                        showAlertDialog(getResources().getString(R.string.no_internet));
                    }


                }

                break;
            case R.id.ll_home:
                if (current != 1) {
                    setHomeFooter(getApplicationContext());
                    AppLog.Log("application context", "--->" + getApplicationContext());
                    intent = new Intent(getApplicationContext(), DashboardScreen.class);
                    startActivity(intent);
                    activityTransition();
                }

                break;
            case R.id.ll_myjobs_crowd:
                if (current != 3) {
                    setMyCrowdFooter(getApplicationContext());
                    intent = new Intent(getApplicationContext(), MyCrowdUserActivity.class);
                    startActivity(intent);
                    activityTransition();
                }
                break;
            case R.id.ll_postjob_crowd:
                if (current != 2) {

                    if (flag) {
                        setPostCrowdFooter(getApplicationContext());
                        intent = new Intent(getApplicationContext(), AskTheCrowdStepOne.class);
                    } else {
                        intent = new Intent(getApplicationContext(), RegisterNewCarActivity.class);
                    }
                    startActivity(intent);
                    activityTransition();
                }
                if (isAskCrowd) {

                    if (flag) {
                        setPostCrowdFooter(getApplicationContext());
                        intent = new Intent(getApplicationContext(), AskTheCrowdStepOne.class);
                    } else {
                        intent = new Intent(getApplicationContext(), RegisterNewCarActivity.class);
                    }
                    startActivity(intent);
                    activityTransition();
                }
                break;
            case R.id.ll_home_crowd:
                if (current != 1) {
                    setHomeFooterCrowd(getApplicationContext());
                    intent = new Intent(getApplicationContext(), DashboardScreen.class);
                    intent.addFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                    activityTransition();
                }
                break;
            case R.id.ll_myjobs:
                if (current != 3) {
                    setMyJobsFooter(getApplicationContext());
                    intent = new Intent(getApplicationContext(), MyJobsUserActivity.class);
                    startActivity(intent);
                    activityTransition();
                }
                break;
            case R.id.ll_myjobs_garage:
                if (current != 3) {
                    setMyCrowdFooterGarage(getApplicationContext());
                    intent = new Intent(getApplicationContext(), MyCrowdUserActivity.class);
                    startActivity(intent);
                    activityTransition();
                }
                break;
            case R.id.ll_postjob_garageowner:
                if (current != 2) {
                    setMyJObFooterGarage(getApplicationContext());
                    intent = new Intent(getApplicationContext(), MyJobsGarageActivity.class);
                    startActivity(intent);
                    activityTransition();
                }

                break;
            case R.id.ll_home_garage:
                if (current != 1) {
                    setHomeFooterGarage(getApplicationContext());
                    intent = new Intent(getApplicationContext(), DashboardScreen.class);
                    intent.addFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                    activityTransition();
                }

            case R.id.ll_job_details: {
                if (current != 1) {
                    setJobDetailsFooter(getApplicationContext());
                    if (isFromJobDetails) {
                        intent = new Intent(getApplicationContext(), JobDetailsActivity.class);

                    } else {
                        intent = new Intent(getApplicationContext(), ViewNewJobsFeedDetailActivity.class);
                    }
                    intent.putExtra("job_id", job_id);
                    startActivity(intent);
                    activityTransition();

                }
            }
            break;
            case R.id.ll_Quotes: {
                if (current != 2) {
                    setJobDetailsQuoteFooter(getApplicationContext());
                    intent = new Intent(getApplicationContext(), QuoteJobDetailsActivity.class);
                    startActivity(intent);
                    activityTransition();
                }
            }
            break;
            case R.id.ll_discussion: {
                if (current != 3) {
                    setJobDetailsDiscussionFooter(getApplicationContext());
                    intent = new Intent(getApplicationContext(), DiscussionActivity.class);
                    intent.putExtra("job_id", fourjobid);
                    intent.putExtra("job_status", currentjobstatus);
                    startActivity(intent);
                    activityTransition();
                }
            }
            break;
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText
                && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()),
                                0);
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    public void validateJobSteps() {
        carImageArrayList = new ArrayList<>();
        carVideosArrayList = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, WebServiceURLs.VALIDATE_STEP_JOB);

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject jsonObject = response.optJSONObject("data");
                                // userImage = response.getString("user_img");
                                JSONArray userDetailsArray = jsonObject.optJSONArray("user_details");
                                JSONArray jobDetailsArray = jsonObject.optJSONArray("job_detail");
                                JSONArray carDetailsArray = jsonObject.optJSONArray("car_detail");
                                JSONArray jobBidsMasterArray = jsonObject.optJSONArray("job_bids_master");
                                JobDetail_DBO jobDetail_dbo = new JobDetail_DBO();
                                if (jobDetailsArray.length() > 0) {
                                    for (int i = 0; i < jobDetailsArray.length(); i++) {
                                        JSONObject jobJsonObj = jobDetailsArray.getJSONObject(i);
                                        String dropoff_date_time = jobJsonObj.optString("dropoff_date_time");
                                        String pickup_date_time = jobJsonObj.optString("pickup_date_time");
                                        String time_flexibility = jobJsonObj.optString("time_flexibility");
                                        String problem_description = jobJsonObj.optString("problem_description");
                                        String is_emergency = jobJsonObj.optString("is_emergency");
                                        String is_help = jobJsonObj.optString("is_help");
                                        String is_insurance = jobJsonObj.optString("is_insurance");
                                        String jobnumber = jobJsonObj.optString("ju_id");
                                        cjob_id = jobJsonObj.optString("cjob_id");
                                        job_title = jobJsonObj.optString("job_title");
                                        String category_id = jobJsonObj.optString("category_id");
                                        String subcategory_id = jobJsonObj.optString("subcategory_id");
                                        String sub_subcategory_id = jobJsonObj.optString("sub_subcategory_id");
                                        String catname = jobJsonObj.optString("catname");
                                        String subcatname = jobJsonObj.optString("subcatname");
                                        String subsubcatname = jobJsonObj.optString("subsubcatname");
                                        String current_tyre_brand = jobJsonObj.optString("current_tyre_brand");
                                        String current_tyre_model = jobJsonObj.optString("current_tyre_model");
                                        String tyre_detail_and_spec = jobJsonObj.optString("tyre_detail_and_spec");
                                        String no_of_tyres = jobJsonObj.optString("no_of_tyres");
                                        String loc_for_tow_or_road_assistance = jobJsonObj.optString("loc_for_tow_or_road_assistance");
                                        String destination_address = jobJsonObj.optString("destination_address");
                                        String inc_roadside_assist = jobJsonObj.optString("inc_roadside_assist");
                                        String inc_std_log_service = jobJsonObj.optString("inc_std_log_service");
                                        String number_of_vehicles = jobJsonObj.optString("number_of_vehicles");
                                        String additional_inclusions = jobJsonObj.optString("additional_inclusions");
                                        String jobStatus = jobJsonObj.optString("current_job_status");
                                        String is_closed = jobJsonObj.optString("job_status");
                                        JSONArray carImagesArray = jobJsonObj.getJSONArray("car_images");
                                        JSONArray carVideosArray = jobJsonObj.getJSONArray("car_videos");
                                        jobDetail_dbo.setCjob_id(cjob_id);
                                        jobDetail_dbo.setDropoff_date_time(dropoff_date_time);
                                        jobDetail_dbo.setPickup_date_time(pickup_date_time);
                                        jobDetail_dbo.setTime_flexibility(time_flexibility);
                                        jobDetail_dbo.setProblem_description(problem_description);
                                        jobDetail_dbo.setIs_emergency(is_emergency);
                                        jobDetail_dbo.setIs_help(is_help);
                                        jobDetail_dbo.setIs_insurance(is_insurance);
                                        jobDetail_dbo.setJu_id(jobnumber);
                                        jobDetail_dbo.setJob_title(job_title);
                                        jobDetail_dbo.setCategory_id(category_id);
                                        jobDetail_dbo.setSubcategory_id(subcategory_id);
                                        jobDetail_dbo.setSub_subcategory_id(sub_subcategory_id);
                                        jobDetail_dbo.setCatname(catname);
                                        jobDetail_dbo.setSubcatname(subcatname);
                                        jobDetail_dbo.setSubsubcatname(subsubcatname);
                                        jobDetail_dbo.setCurrent_tyre_brand(current_tyre_brand);
                                        jobDetail_dbo.setCurrent_tyre_model(current_tyre_model);
                                        jobDetail_dbo.setTyre_detail_and_spec(tyre_detail_and_spec);
                                        jobDetail_dbo.setNo_of_tyres(no_of_tyres);
                                        jobDetail_dbo.setLoc_for_tow_or_road_assistance(loc_for_tow_or_road_assistance);
                                        jobDetail_dbo.setDestination_address(destination_address);
                                        jobDetail_dbo.setInc_roadside_assist(inc_roadside_assist);
                                        jobDetail_dbo.setInc_std_log_service(inc_std_log_service);
                                        jobDetail_dbo.setNumber_of_vehicles(number_of_vehicles);
                                        jobDetail_dbo.setAdditional_inclusions(additional_inclusions);
                                        jobDetail_dbo.setCar_images(carImagesArray);
                                        jobDetail_dbo.setCar_videos(carVideosArray);

                                        if (carImagesArray.length() > 0) {
                                            for (int j = 0; j < carImagesArray.length(); j++) {
                                                JSONObject carImgJsonObj = carImagesArray.getJSONObject(j);
                                                CarImageDBO carImageDBO = new CarImageDBO();
                                                String carImg = carImgJsonObj.getString("url");
                                                carImageDBO.setUrl(carImgJsonObj.getString("url"));
                                                carImageArrayList.add(carImageDBO);
                                                jobDetail_dbo.setCarImageDBOArrayList(carImageArrayList);


                                            }

                                        }

                                        if (carVideosArray.length() > 0) {

                                            for (int j = 0; j < carVideosArray.length(); j++) {
                                                JSONObject carVideoJsonObj = carVideosArray.getJSONObject(j);
                                                CarVideosDbo carVideosDbo = new CarVideosDbo();
                                                carVideosDbo.setThumbnail(carVideoJsonObj.getString("thumbnail"));
                                                carVideosDbo.setVideoUrl(carVideoJsonObj.getString("video"));
                                                carVideosArrayList.add(carVideosDbo);
                                                jobDetail_dbo.setCarVideosDboArrayList(carVideosArrayList);


                                            }
                                        }

                                    }


                                }
                                if (carDetailsArray.length() > 0) {
                                    for (int i = 0; i < carDetailsArray.length(); i++) {
                                        JSONObject carJsonObj = carDetailsArray.getJSONObject(i);
                                        String carmake = carJsonObj.optString("carmake");
                                        String carmodel = carJsonObj.optString("carmodel");
                                        String carbadge = carJsonObj.optString("carbadge");
                                        String registration_number = carJsonObj.optString("registration_number");
                                        String car_type = carJsonObj.optString("car_type");
                                        String km = carJsonObj.optString("km");
                                        String year = carJsonObj.optString("year");
                                        String car_id = carJsonObj.optString("id");


                                        jobDetail_dbo.setCarmake(carmake);
                                        jobDetail_dbo.setCarmodel(carmodel);
                                        if (carbadge.equalsIgnoreCase("")) {
                                            jobDetail_dbo.setCarbadge("No Badge/Variant");
                                        } else {
                                            jobDetail_dbo.setCarbadge(carbadge);
                                        }
                                        jobDetail_dbo.setRegistration_number(registration_number);
                                        jobDetail_dbo.setCar_type(car_type);
                                        jobDetail_dbo.setKm(km);
                                        jobDetail_dbo.setYear(year);
                                        jobDetail_dbo.setCar_id(car_id);
                                        jobDetail_dbo.setPending(true);

                                        HelperMethods.storeJobDetailsSharedPreferences(appContext, jobDetail_dbo);


                                    }
                                }

                                Intent intent = new Intent(appContext, EditJobStepOne.class);
                                intent.putExtra("job_id", cjob_id);
                                intent.putExtra("job_title", job_title);
                                startActivity(intent);
                                activityTransition();


                            } else {
                                //showAlertDialog(response.getString(Constants.MESSAGE));
                                Intent intent;
                                if (flag) {
                                    setPostJObFooter(getApplicationContext());
                                    intent = new Intent(getApplicationContext(), PostNewJobStepOne.class);
                                    intent.putExtra("Dashboard", "yes");
                                } else {
                                    intent = new Intent(getApplicationContext(), RegisterNewCarActivity.class);
                                }
                                startActivity(intent);
                                activityTransition();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

}
