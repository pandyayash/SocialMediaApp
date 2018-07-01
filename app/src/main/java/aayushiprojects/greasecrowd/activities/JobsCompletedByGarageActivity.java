package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

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

import aayushiprojects.greasecrowd.CustomViews.BottomOffsetDecoration;
import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptCompleteJobByGarage;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.MyPreference;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.NewPostedJob_DBO;

public class JobsCompletedByGarageActivity extends BaseActivity {

    String TAG = "JobsCompletedByGarageActivity";//MYGARAGES_DETAIL
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    String garageId,garageName;
    ArrayList<NewPostedJob_DBO> postedJobDboArrayList;
    RecyclerView jobsRecyclerview;
    AdptCompleteJobByGarage adptCompleteJobByGarage;
    SwipeRefreshLayout pull_to_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_completed_by_garage);
        Intent intent = getIntent();
        if (intent!=null){
            garageId = intent.getStringExtra("garageId");
            garageName = intent.getStringExtra("garageName");
        }
        getViews();

    }

    public void getViews(){
        appContext = this;
        postedJobDboArrayList=new ArrayList<>();
        setHeader("Jobs Completed By "+garageName);
        setfooter("jobs");
        setJobDetailsQuoteFooter(appContext);
        setlistenrforfooter();
        jobsRecyclerview = findViewById(R.id.recyclerView);
        pull_to_refresh = findViewById(R.id.pull_to_refresh);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (Connectivity.isConnected(appContext)){
            getCompletedJobsBygarage();
        }else {
            showAlertDialog(getString(R.string.no_internet));
        }
        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Connectivity.isConnected(appContext)){
                    postedJobDboArrayList = new ArrayList<>();
                    getCompletedJobsBygarage();
                }else {
                    showAlertDialog(getString(R.string.no_internet));
                }
                pull_to_refresh.setRefreshing(false);
            }
        });

    }


    public void getCompletedJobsBygarage() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MYGARAGES_DETAIL);
        params.put(Constants.SUBMIT_BIDS.GARAGE_ID, garageId);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "MYGARAGES_DETAIL --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)){
                                JSONObject jsonObject = response.optJSONObject("data");
                                JSONArray jsonArray = jsonObject.getJSONArray("jobs");
                                if (jsonArray.length()>0){
                                    for (int i = 0; i <jsonArray.length() ; i++) {

                                        JSONObject object = jsonArray.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setcJobId(object.getString("cjob_id"));
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setFname(object.getString("fname"));
                                        newPostedJob_dbo.setLname(object.getString("lname"));
                                        newPostedJob_dbo.setCatName(object.getString("catname"));
                                        newPostedJob_dbo.setDistnace(String.format("%.0f", Float.parseFloat(object.optString("distance"))));
                                        postedJobDboArrayList.add(newPostedJob_dbo);


                                    }
                                    setData();
                                }

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

    public void setData() {

       // if (postedJobDboArrayList.size() > 0) {
           // textView.setVisibility(View.GONE);
            jobsRecyclerview.setVisibility(View.VISIBLE);
            adptCompleteJobByGarage = new AdptCompleteJobByGarage(postedJobDboArrayList, appContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            jobsRecyclerview.setLayoutManager(mLayoutManager);
            jobsRecyclerview.setItemAnimator(new DefaultItemAnimator());
            jobsRecyclerview.setAdapter(adptCompleteJobByGarage);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            jobsRecyclerview.addItemDecoration(bottomOffsetDecoration);
//        } else {
//            textView.setVisibility(View.VISIBLE);
//            jobsRecyclerview.setVisibility(View.GONE);
//        }

    }
}
