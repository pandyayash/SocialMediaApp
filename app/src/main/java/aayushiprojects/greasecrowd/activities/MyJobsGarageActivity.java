package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptMyJobTabsgarage;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.MyPreference;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.NewPostedJob_DBO;

public class MyJobsGarageActivity extends BaseActivity {

    public String TAG = "MyJOBSGARAGE";
    public ArrayList<NewPostedJob_DBO> postedJobDboArrayList;
    public ArrayList<NewPostedJob_DBO> postedJobDboArrayList_Awarded;
    public ArrayList<NewPostedJob_DBO> postedJobDboArrayList_Completed;
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        getViews();
        setHeader("My Jobs");
        setfooter("garageowner");
        setMyJObFooterGarage(appContext);
        setlistenrforfooter();
       /* if (Connectivity.isConnected(appContext)){
            getNewPostedJobData();
        }else {
            showAlertDialog(getString(R.string.no_internet));
        }*/

        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();.
        Intent intent = new Intent(appContext,DashboardScreen.class);
        startActivity(intent);
        activityTransition();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==ll_back){
            Intent intent = new Intent(appContext,DashboardScreen.class);
            startActivity(intent);
            activityTransition();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setMyJObFooterGarage(this);
        changeTabsFont();
    }

    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tabLayout = findViewById(R.id.my_jobs_tablayout);
        viewPager = findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        setAdapter();
    }

    public void setAdapter() {
        viewPager.setAdapter(new AdptMyJobTabsgarage(getSupportFragmentManager(), appContext));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void changeTabsFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.Fonts.POPPINS_REGULAR);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTextSize(18f);
                }
            }
        }
    }

    public void getNewPostedJobData() {
        postedJobDboArrayList = new ArrayList<>();
        postedJobDboArrayList_Awarded = new ArrayList<>();
        postedJobDboArrayList_Completed = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MY_JOBS_GARAGE);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject jsonObject = response.getJSONObject("data");
                                JSONObject jobsjsonObj = jsonObject.getJSONObject("jobs");
                                JSONArray newjsonArray = jobsjsonObj.getJSONArray("quoted");
                                JSONArray newjsonArrayAwarded = jobsjsonObj.getJSONArray("awarded");
                                JSONArray newjsonArrayCompleted = jobsjsonObj.getJSONArray("completed");

                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setcJobId(object.getString("cjob_id"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setCarModel(object.getString("model"));
                                        newPostedJob_dbo.setMake(object.getString("make"));
                                        newPostedJob_dbo.setBadge(object.getString("badge"));
                                        newPostedJob_dbo.setYear(object.getString("year"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setPrice(object.getString("average_bid"));
                                        newPostedJob_dbo.setTotalQuotes(object.getInt("bid_count"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setFname(object.getString("fname"));
                                        newPostedJob_dbo.setLname(object.getString("lname"));
                                        postedJobDboArrayList.add(newPostedJob_dbo);
                                    }
                                }

                                if (newjsonArrayAwarded.length() > 0) {
                                    for (int i = 0; i < newjsonArrayAwarded.length(); i++) {
                                        JSONObject object = newjsonArrayAwarded.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setcJobId(object.getString("cjob_id"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setCarModel(object.getString("model"));
                                        newPostedJob_dbo.setMake(object.getString("make"));
                                        newPostedJob_dbo.setBadge(object.getString("badge"));
                                        newPostedJob_dbo.setYear(object.getString("year"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setPrice(object.getString("average_bid"));
                                        newPostedJob_dbo.setTotalQuotes(object.getInt("bid_count"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setFname(object.getString("fname"));
                                        newPostedJob_dbo.setLname(object.getString("lname"));
                                        postedJobDboArrayList_Awarded.add(newPostedJob_dbo);
                                    }
                                }
                                if (newjsonArrayCompleted.length() > 0) {
                                    for (int i = 0; i < newjsonArrayCompleted.length(); i++) {
                                        JSONObject object = newjsonArrayCompleted.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setcJobId(object.getString("cjob_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setCarModel(object.getString("model"));
                                        newPostedJob_dbo.setMake(object.getString("make"));
                                        newPostedJob_dbo.setBadge(object.getString("badge"));
                                        newPostedJob_dbo.setYear(object.getString("year"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setPrice(object.getString("average_bid"));
                                        newPostedJob_dbo.setTotalQuotes(object.getInt("bid_count"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setFromCompleted(true);
                                        newPostedJob_dbo.setGrating(object.getString("grating"));
                                        newPostedJob_dbo.setGreview(object.getString("greview"));
                                        newPostedJob_dbo.setUrating(object.getString("rating"));
                                        newPostedJob_dbo.setUreview(object.getString("review"));
                                        newPostedJob_dbo.setFname(object.getString("fname"));
                                        newPostedJob_dbo.setLname(object.getString("lname"));
                                        postedJobDboArrayList_Completed.add(newPostedJob_dbo);
                                    }
                                }
                                setAdapter();
                                changeTabsFont();
                            } else {

                                setAdapter();
                                changeTabsFont();
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }
}
