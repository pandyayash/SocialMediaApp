package technource.greasecrowd.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
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

import technource.greasecrowd.R;
import technource.greasecrowd.adapter.AdptQuoteJobsDetails;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.CarImageDBO;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.Model_FollowupWork;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class QuoteJobDetailsActivity extends BaseActivity {

    private final String TAG = "QuoteJobDetails";
    Context appContext;
    int currentPage = 1;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    ArrayList<Model_FollowupWork> followupWorkArrayList;
    TextView txtNoDataFound;
    AdptQuoteJobsDetails awardedJobAdpt;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<String> sortList;
    ArrayAdapter<String> srotArrayAdapt;
    String sortby = "ratings";
    LinearLayout ll_back;
    boolean first = true;
    private TextView tvJobTitle;
    private Spinner spSort;
    private RecyclerView recyclerView;
    private boolean isLoadMore = false;
    String jobStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_job_details);
        findViews();
        setHeader("Quotes");
        setfooter("job_details");
        setJobDetailsQuoteFooter(appContext);
        setlistenrforfooter();
        setOnClickListners();
        if (Connectivity.isConnected(appContext)) {
            setCategoryList();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {
                        getMyQuoteList();
                    } else {
                        showAlertDialog(getString(R.string.no_internet));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        /*setAdapter();*/
    }

    private void findViews() {
        appContext = this;
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        awardJobArrayList = new ArrayList<>();
        sortList = new ArrayList<>();
        followupWorkArrayList = new ArrayList<>();
        txtNoDataFound = (TextView) findViewById(R.id.txtNoDataFound);
        tvJobTitle = (TextView) findViewById(R.id.tv_jobTitle);
        spSort = (Spinner) findViewById(R.id.sp_sort);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void setOnClickListners() {
        ll_back.setOnClickListener(this);
    }

    private void setAdapter(JobDetail_DBO jobs) {
        if (awardJobArrayList.size() > 0) {
            txtNoDataFound.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            awardedJobAdpt = new AdptQuoteJobsDetails(awardJobArrayList, appContext, jobs);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(awardedJobAdpt);
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setJobDetailsQuoteFooter(appContext);
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;

        }
    }

    private void getMyQuoteList() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SORTBIDS);
        params.put(Constants.QUOTEJOBDETAILS.PAGE_NUMBER, String.valueOf(currentPage));
        params.put(Constants.QUOTEJOBDETAILS.USER_TYPE, loginDetail_dbo.getUser_Type());
        // params.put(Constants.QUOTEJOBDETAILS.JOB_ID, Constants.job_idBaseActivity);
        params.put(Constants.QUOTEJOBDETAILS.JOB_ID, job_id);
        params.put(Constants.QUOTEJOBDETAILS.SORTBY, sortby.toLowerCase());
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In QUOTEJOB --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                final String next = response.getString("next");
                                jobStatus = response.optString("current_job_status");
                                if (!next.equalsIgnoreCase("false")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONObject jaray = response.getJSONObject("data");
                                JSONArray newjsonArray = jaray.getJSONArray("job_bids_master");
                                JSONArray carDetails = jaray.getJSONArray("car_detail");




                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject bidJobJsonObj = newjsonArray.getJSONObject(i);
                                        AwardJobDBOCarOwner awardJobDBOCarOwner = new AwardJobDBOCarOwner();
                                        awardJobDBOCarOwner.setJob_id(bidJobJsonObj.getString("job_id"));
                                        awardJobDBOCarOwner.setGarage_id(bidJobJsonObj.getString("garage_id"));
                                        awardJobDBOCarOwner.setId(bidJobJsonObj.getString("id"));
                                        awardJobDBOCarOwner.setBid_price(bidJobJsonObj.getString("bid_price"));
                                        awardJobDBOCarOwner.setBid_comment(bidJobJsonObj.getString("bid_comment"));
                                        awardJobDBOCarOwner.setAdd_offer(bidJobJsonObj.getString("add_offer"));
                                        awardJobDBOCarOwner.setAdd_offer_price(bidJobJsonObj.getString("add_offer_price"));
                                        awardJobDBOCarOwner.setAdd_offer_accept(bidJobJsonObj.getString("add_offer_accept"));
                                        awardJobDBOCarOwner.setTotal(bidJobJsonObj.getString("total"));
                                        awardJobDBOCarOwner.setDistance(bidJobJsonObj.getString("distance"));
                                        awardJobDBOCarOwner.setReview_count(bidJobJsonObj.getString("review_count"));
                                        awardJobDBOCarOwner.setAvatar_img(bidJobJsonObj.getString("avatar_img"));
                                        awardJobDBOCarOwner.setName(bidJobJsonObj.getString("name"));
                                        awardJobDBOCarOwner.setBid_status(jobStatus);
                                        awardJobDBOCarOwner.setRating(bidJobJsonObj.getInt("rating"));

                                        JSONArray freeInclusion = bidJobJsonObj.getJSONArray("free_inclusion");
                                        if (freeInclusion.length() > 0) {
                                            ArrayList<String> free = new ArrayList<>();
                                            for (int j = 0; j < freeInclusion.length(); j++) {

                                                free.add(freeInclusion.getJSONObject(j).getString("inclusion"));
                                            }

                                            awardJobDBOCarOwner.setFreeInclusion(free);
                                        }


                                        if (carDetails.length() > 0) {
                                            JSONObject car = carDetails.getJSONObject(0);
                                            awardJobDBOCarOwner.setMake(car.getString("carmake"));
                                            awardJobDBOCarOwner.setModel(car.getString("carmodel"));
                                            awardJobDBOCarOwner.setModel(car.getString("carbadge"));
                                        }
                                        JSONArray followupImages = bidJobJsonObj.getJSONArray("followup_image");
                                        if (followupImages.length()>0){
                                            ArrayList<String> stringArrayList = new ArrayList<>();
                                            for (int k=0;k<followupImages.length();k++){
                                                JSONObject jsonObject = followupImages.getJSONObject(k);
                                                stringArrayList.add(jsonObject.getString("image"));
                                            }
                                            awardJobDBOCarOwner.setCarImages(stringArrayList);
                                        }


                                        awardJobArrayList.add(awardJobDBOCarOwner);
                                    }

                                    JSONObject jobJsonObj = jaray.getJSONObject("job_detail");
                                    JobDetail_DBO jobDetail_dbo = new JobDetail_DBO();
                                    jobDetail_dbo.setDropoff_date_time(jobJsonObj.getString("dropoff_date_time"));
                                    jobDetail_dbo.setPickup_date_time(jobJsonObj.getString("pickup_date_time"));
                                    jobDetail_dbo.setTime_flexibility(jobJsonObj.getString("time_flexibility"));
                                    jobDetail_dbo.setProblem_description(jobJsonObj.getString("problem_description"));
                                    jobDetail_dbo.setIs_emergency(jobJsonObj.getString("is_emergency"));
                                    jobDetail_dbo.setIs_help(jobJsonObj.getString("is_help"));
                                    jobDetail_dbo.setCar_id(jobJsonObj.getString("car_id"));
                                    jobDetail_dbo.setIs_insurance(jobJsonObj.getString("is_insurance"));
                                    jobDetail_dbo.setJu_id(jobJsonObj.getString("ju_id"));
                                    jobDetail_dbo.setJob_title(jobJsonObj.getString("job_title"));
                                    jobDetail_dbo.setCategory_id(jobJsonObj.getString("category_id"));
                                    jobDetail_dbo.setSubcategory_id(jobJsonObj.getString("subcategory_id"));
                                    jobDetail_dbo.setSub_subcategory_id(jobJsonObj.getString("sub_subcategory_id"));
                                    jobDetail_dbo.setCatname(jobJsonObj.getString("catname"));
                                    jobDetail_dbo.setSubcatname(jobJsonObj.getString("subcatname"));
                                    jobDetail_dbo.setSubsubcatname(jobJsonObj.getString("subsubcatname"));
                                    jobDetail_dbo.setCurrent_tyre_brand(jobJsonObj.getString("current_tyre_brand"));
                                    jobDetail_dbo.setCurrent_tyre_model(jobJsonObj.getString("current_tyre_model"));
                                    jobDetail_dbo.setTyre_detail_and_spec(jobJsonObj.getString("tyre_detail_and_spec"));
                                    jobDetail_dbo.setNo_of_tyres(jobJsonObj.getString("no_of_tyres"));
                                    jobDetail_dbo.setLoc_for_tow_or_road_assistance(jobJsonObj.getString("loc_for_tow_or_road_assistance"));
                                    jobDetail_dbo.setDestination_address(jobJsonObj.getString("destination_address"));
                                    jobDetail_dbo.setInc_roadside_assist(jobJsonObj.getString("inc_roadside_assist"));
                                    jobDetail_dbo.setInc_std_log_service(jobJsonObj.getString("inc_std_log_service"));
                                    jobDetail_dbo.setNumber_of_vehicles(jobJsonObj.getString("number_of_vehicles"));
                                    jobDetail_dbo.setAdditional_inclusions(jobJsonObj.getString("additional_inclusions"));
                                    JSONObject followupStringNew = jobJsonObj.getJSONObject("followup_string_new");
                                    JSONArray jsonArray = followupStringNew.getJSONArray("work");
                                    if (jsonArray.length()>0){
                                        for (int i = 0; i < jsonArray.length(); i++) {
                                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                                            Model_FollowupWork model_followupWork = new Model_FollowupWork();
                                            model_followupWork.setKey(jsonObject.getString("key"));
                                            model_followupWork.setValue(jsonObject.getString("value"));
                                            if (!jsonObject.getString("key").equalsIgnoreCase("") || !jsonObject.getString("value").equalsIgnoreCase("")){
                                                followupWorkArrayList.add(model_followupWork);
                                            }


                                        }
                                        AppLog.Log("Followup Work","SIZe ***---> "+followupWorkArrayList.size());
                                        jobDetail_dbo.setFollowupWorkArrayList(followupWorkArrayList);
                                    }

                                    setAdapter(jobDetail_dbo);
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);
                                }
                                JSONObject job_details = jaray.getJSONObject("job_detail");
                                tvJobTitle.setText(job_details.getString("job_title"));

                            } else {
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



    public void setCategoryList() {

        sortList.add("Ratings");
        sortList.add("Date");
        sortList.add("Price");
        sortList.add("Reviews");
        sortList.add("Distance");

        srotArrayAdapt = new ArrayAdapter<String>(appContext, R.layout.spinner_textview_1, sortList) {

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.black));

                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        srotArrayAdapt.setDropDownViewResource(android.R.layout.simple_list_item_1);
        spSort.setAdapter(srotArrayAdapt);

        spSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                awardJobArrayList.clear();
                sortby = sortList.get(position);
                getMyQuoteList();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}



