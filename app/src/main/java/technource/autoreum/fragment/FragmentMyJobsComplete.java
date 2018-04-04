package technource.autoreum.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import technource.autoreum.CustomViews.BottomOffsetDecoration;
import technource.autoreum.R;
import technource.autoreum.activities.MyJobsUserActivity;
import technource.autoreum.adapter.AdptNewPostedJobs;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.NewPostedJob_DBO;

/**
 * Created by technource on 12/12/17.
 */

public class FragmentMyJobsComplete extends Fragment {
    public String TAG = "FragmentMyJobsComplete";
    Activity appContext;
    RecyclerView jobsRecyclerview;
    TextView textView;
    AdptNewPostedJobs adptNewPostedJobs;
    ArrayList<NewPostedJob_DBO> postedJobDboArrayList;
    SwipeRefreshLayout pull_to_refresh;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_jobs_new_posted, null, false);
        getViews(view);
        return view;
    }

    public void getViews(View rootView) {
        appContext = getActivity();
        postedJobDboArrayList = new ArrayList<>();
        jobsRecyclerview = (RecyclerView) rootView.findViewById(R.id.jobs_recyclerview);
        pull_to_refresh = (SwipeRefreshLayout) rootView.findViewById(R.id.pull_to_refresh);
        textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText("You don't have any completed jobs.");
        textView.setVisibility(View.GONE);
        if (Connectivity.isConnected(appContext)) {
            getNewPostedJobData();
        } else {
            ((MyJobsUserActivity) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }

        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (Connectivity.isConnected(appContext)) {
                    postedJobDboArrayList = new ArrayList<>();
                    getNewPostedJobData();

                } else {
                    ((MyJobsUserActivity) appContext)
                            .showAlertDialog(getResources().getString(R.string.no_internet));
                }
                pull_to_refresh.setRefreshing(false);

            }
        });
       /* new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Connectivity.isConnected(appContext)) {
                    getNewPostedJobData();
                } else {
                    ((MyJobsUserActivity) appContext)
                            .showAlertDialog(getResources().getString(R.string.no_internet));
                }

            }
        },100);*/
    }

    @Override
    public void onResume() {
        super.onResume();
        AppLog.Log("onResume","onResume");
        if (Connectivity.isConnected(appContext)) {
            //postedJobDboArrayList.clear();
            postedJobDboArrayList = new ArrayList<>();
            getNewPostedJobData();
        } else {
            ((MyJobsUserActivity) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }
    }

    public void setData() {

        if (postedJobDboArrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            jobsRecyclerview.setVisibility(View.VISIBLE);
            adptNewPostedJobs = new AdptNewPostedJobs(postedJobDboArrayList, appContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            jobsRecyclerview.setLayoutManager(mLayoutManager);
            jobsRecyclerview.setItemAnimator(new DefaultItemAnimator());
            jobsRecyclerview.setAdapter(adptNewPostedJobs);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            jobsRecyclerview.addItemDecoration(bottomOffsetDecoration);
        } else {
            textView.setVisibility(View.VISIBLE);
            jobsRecyclerview.setVisibility(View.GONE);
        }
    }

    /*-----------Web call for get new posted job list------------- */
    public void getNewPostedJobData() {

        //((MyJobsUserActivity) appContext).showLoadingDialogFrag(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MY_JOBS_USER);
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
                                JSONArray newjsonArray = jobsjsonObj.getJSONArray("completed");
                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
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
                                        postedJobDboArrayList.add(newPostedJob_dbo);
                                    }
                                    setData();
                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                    jobsRecyclerview.setVisibility(View.GONE);
                                }

                            } else {
                                ((MyJobsUserActivity) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        //((MyJobsUserActivity) appContext).showLoadingDialogFrag(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //((MyJobsUserActivity) appContext).showLoadingDialogFrag(false);
                    }
                });
        queue.add(jsonObjReq);
    }
}


