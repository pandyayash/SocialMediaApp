package technource.autoreum.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.facebook.CallbackManager;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import technource.autoreum.CustomViews.Widgets.CircularImageView;
import technource.autoreum.R;
import technource.autoreum.adapter.AdptAwardJobsCarOwner;
import technource.autoreum.adapter.AdptJobDetailsProgress;
import technource.autoreum.adapter.ViewPagerAdapterCarVideo;
import technource.autoreum.adapter.ViewPagerAdapterImage;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.AwardJobDBOCarOwner;
import technource.autoreum.model.CarImageDBO;
import technource.autoreum.model.CarVideosDbo;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.List_items;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Model_FreeInclusions;
import technource.autoreum.model.Model_GarageServices;

import static technource.autoreum.helper.Constants.notify_count_footer;

public class ViewNewJobsFeedDetailActivity extends BaseActivity {
    String TAG = "NewJobsFeedDetailActivity";
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    ViewPager viewpager, viewpager1;
    CircleIndicator indicator, indicator1;
    ViewPagerAdapterImage pagerAdapterImage;
    ViewPagerAdapterCarVideo pagerAdapterVideo;
    ScrollView scroll;
    //String job_id, userImage;
    CircularImageView iv_user;
    TextView txtUsername, txtLocation, txtDistance, txtMobile, txtMake, txtModel, txtBadge, txtRegNumber, txtTransmission, txtYear, txtDropOffDate, txtPickupDate, txtFlexibility;
    TextView txtInsuranceCompany, txtPolicynumber, txtClaimnumber, txtJobTitle, txtJobNumber, txtCategory, txtSubCategory, txtjobDesc;
    TextView txtCurrentTyreBrand, txtCurrentTyreModel, txtTyreDetail, txtNoTyreReplaced, txtSameorEquivalent, txtadditionalInclusions;
    TextView btnEdit, btnClose, btnDelete, txt_include_roadside_assistance, txt_include_standard_logbook, txt_no_of_vehicles, txtQuotes;
    ImageView iv_no_car_img;
    ArrayList<CarImageDBO> carImageArrayList;
    ArrayList<CarVideosDbo> carVideosArrayList;
    LinearLayout ll_additional_inclusions, ll_edt_del_close, ll_reply_to_user;
    LinearLayout ll_emergency, ll_insurance_claim, ll_insurance_company, ll_policy_number, ll_claim_number, ll_no_car_imgs, ll_no_car_videos, ll_quotes_from_garage, ll_auto_tyre, ll_fleet_managemnet;
    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    ArrayList<String> bidStatusArrayList;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    ArrayList<AwardJobDBOCarOwner> awardJobDBOCarOwners;
    AdptJobDetailsProgress adptJobDetailsProgress;
    String jobStatus, job_title, cjob_id, is_closed = "", garage_id = "";
    ArrayList<String> garageIdList;
    RecyclerView quotesRecyclerview;
    AdptAwardJobsCarOwner adptAwardJobsCarOwner;
    ArrayList<Model_FreeInclusions> freeInclusionsArrayList;
    ArrayList<Model_GarageServices> garageServicesArrayList;
    ArrayList<Model_GarageServices> garageServicesArrayList1;
    Boolean isFlexible = false;
    ArrayList<List_items> list_itemsArrayList;

    ImageView iv_image;
    TextView tv_name, rate_count, tv_desc, txtSubmitReview;
    SimpleRatingBar ratingBar;
    RecyclerView jobsReviewResponses;
    LinearLayout jobReview;
    CustomListAdapterOther customListAdapterOther;
    ImageView shareWithFb, shareWithTwiiter;
    LinearLayout llCheckedOrNot;
    CheckBox isReply;
    TextView tvReply;
    EditText message;
    String name = "";
    CallbackManager callbackManager;
    TextView cart_badge_footer;
    ShareDialog shareDialog;
    ShareButton shareButton;
    String shareGarageImg,shareGarageName,shareReview,shareRatting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_new_jobs_feed_detail);
        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            //Toast.makeText(getApplicationContext(), cat_id, Toast.LENGTH_SHORT).show();
        }
        setHeader("Market Feed Details");
        setfooter("job_details");

        setlistenrforfooter();
        setJobDetailsFooter(getApplicationContext());
        // setJobDetailsFooter(appContext);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);

        getViews();
        setOnClickListners();

        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        shareButton = (ShareButton) findViewById(R.id.fb_share_button);
        //Toast.makeText(appContext, ""+firstCharacter+S, Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setJobDetailsFooter(getApplicationContext());
        Constants.isFromJobDetails = false;
    }

    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        carImageArrayList = new ArrayList<>();
        carVideosArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        bidStatusArrayList = new ArrayList<>();
        awardJobDBOCarOwners = new ArrayList<>();
        awardJobArrayList = new ArrayList<>();
        freeInclusionsArrayList = new ArrayList<>();
        garageServicesArrayList = new ArrayList<>();
        garageServicesArrayList1 = new ArrayList<>();
        garageIdList = new ArrayList<>();
        list_itemsArrayList = new ArrayList<>();

        txtUsername = (TextView) findViewById(R.id.txtUsername);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtDistance = (TextView) findViewById(R.id.txtDistance);
        txtQuotes = (TextView) findViewById(R.id.txtQuotes);
        txtMobile = (TextView) findViewById(R.id.txtMobile);
        txtMake = (TextView) findViewById(R.id.txtMake);
        txtModel = (TextView) findViewById(R.id.txtModel);
        txtBadge = (TextView) findViewById(R.id.txtBadge);
        txtRegNumber = (TextView) findViewById(R.id.txtRegNumber);
        txtTransmission = (TextView) findViewById(R.id.txtTransmission);
        txtYear = (TextView) findViewById(R.id.txtYear);
        txtDropOffDate = (TextView) findViewById(R.id.txtDropOffDate);
        txtPickupDate = (TextView) findViewById(R.id.txtPickupDate);
        txtFlexibility = (TextView) findViewById(R.id.txtFlexibility);
        btnEdit = (TextView) findViewById(R.id.btnEdit);
        btnClose = (TextView) findViewById(R.id.btnClose);
        btnDelete = (TextView) findViewById(R.id.btnDelete);
        txtInsuranceCompany = (TextView) findViewById(R.id.txtInsuranceCompany);
        txtPolicynumber = (TextView) findViewById(R.id.txtPolicynumber);
        txtClaimnumber = (TextView) findViewById(R.id.txtClaimnumber);
        txtJobTitle = (TextView) findViewById(R.id.txtJobTitle);
        txtJobTitle.getBackground().setLevel(3);
        txtJobNumber = (TextView) findViewById(R.id.txtJobNumber);
        txtCategory = (TextView) findViewById(R.id.txtCategory);
        txtSubCategory = (TextView) findViewById(R.id.txtSubCategory);
        txtjobDesc = (TextView) findViewById(R.id.txtjobDesc);
        txtCurrentTyreBrand = (TextView) findViewById(R.id.txtCurrentTyreBrand);
        txtCurrentTyreModel = (TextView) findViewById(R.id.txtCurrentTyreModel);
        txtTyreDetail = (TextView) findViewById(R.id.txtTyreDetail);
        txtNoTyreReplaced = (TextView) findViewById(R.id.txtNoTyreReplaced);
        txtSameorEquivalent = (TextView) findViewById(R.id.txtSameorEquivalent);
        txtadditionalInclusions = (TextView) findViewById(R.id.txtadditionalInclusions);
        txt_include_roadside_assistance = (TextView) findViewById(R.id.txt_include_roadside_assistance);
        txt_include_standard_logbook = (TextView) findViewById(R.id.txt_include_standard_logbook);
        txt_no_of_vehicles = (TextView) findViewById(R.id.txt_no_of_vehicles);

        iv_user = (CircularImageView) findViewById(R.id.iv_user);
        iv_no_car_img = (ImageView) findViewById(R.id.iv_no_car_img);
        ll_emergency = (LinearLayout) findViewById(R.id.ll_emergency);
        ll_insurance_claim = (LinearLayout) findViewById(R.id.ll_insurance_claim);
        ll_insurance_company = (LinearLayout) findViewById(R.id.ll_insurance_company);
        ll_policy_number = (LinearLayout) findViewById(R.id.ll_policy_number);
        ll_claim_number = (LinearLayout) findViewById(R.id.ll_claim_number);
        ll_no_car_imgs = (LinearLayout) findViewById(R.id.ll_no_car_imgs);
        ll_no_car_videos = (LinearLayout) findViewById(R.id.ll_no_car_videos);
        ll_quotes_from_garage = (LinearLayout) findViewById(R.id.ll_quotes_from_garage);
        ll_auto_tyre = (LinearLayout) findViewById(R.id.ll_auto_tyre);
        ll_additional_inclusions = (LinearLayout) findViewById(R.id.ll_additional_inclusions);
        ll_edt_del_close = (LinearLayout) findViewById(R.id.ll_edt_del_close);
        ll_fleet_managemnet = (LinearLayout) findViewById(R.id.ll_fleet_managemnet);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        quotesRecyclerview = (RecyclerView) findViewById(R.id.quotesRecyclerview);


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager1 = (ViewPager) findViewById(R.id.viewpager1);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator1 = (CircleIndicator) findViewById(R.id.indicator1);

        stringArrayList.add("Posted");
        stringArrayList.add("Quoted");
        stringArrayList.add("Awarded");
        stringArrayList.add("Drop Off");
        stringArrayList.add("Complete");
        stringArrayList.add("Pickup");
        iv_image = (ImageView) findViewById(R.id.iv_image);
        tv_name = (TextView) findViewById(R.id.tv_name);
        rate_count = (TextView) findViewById(R.id.rate_count);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        txtSubmitReview = (TextView) findViewById(R.id.txtSubmitReview);
        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        jobsReviewResponses = (RecyclerView) findViewById(R.id.jobsReviewResponses);
        jobReview = (LinearLayout) findViewById(R.id.jobReview);

        shareWithFb = (ImageView) findViewById(R.id.shareWithFb);
        shareWithTwiiter = (ImageView) findViewById(R.id.shareWithTwiiter);

        llCheckedOrNot = (LinearLayout) findViewById(R.id.llCheckedOrNot);
        isReply = (CheckBox) findViewById(R.id.isReply);
        tvReply = (TextView) findViewById(R.id.tvReply);
        message = (EditText) findViewById(R.id.message);
        scroll = (ScrollView) findViewById(R.id.scroll);
        ll_reply_to_user = (LinearLayout) findViewById(R.id.ll_reply_to_user);
        cart_badge_footer = (TextView) findViewById(R.id.cart_badge_footer);

        isReply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    collapse(llCheckedOrNot);
                } else {
                    expand(llCheckedOrNot);
                    scroll.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            scroll.fullScroll(ScrollView.FOCUS_DOWN);
                        }
                    }, 200);
                }
            }
        });


        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            btnEdit.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
        } else {
            btnDelete.setVisibility(View.VISIBLE);
            btnEdit.setVisibility(View.VISIBLE);
            btnClose.setVisibility(View.VISIBLE);
            btnEdit.setText("Edit");
            btnClose.setText("Close");
            btnEdit.setBackgroundColor(getResources().getColor(R.color.dark_grey_bg));
            btnClose.setBackgroundColor(getResources().getColor(R.color.dark_grey_bg));
        }

        getNewPostedJobData();


        if (cat_id.equalsIgnoreCase("5")) {
            ll_auto_tyre.setVisibility(View.VISIBLE);
        } else {
            ll_auto_tyre.setVisibility(View.GONE);
        }
        if (cat_id.equalsIgnoreCase("13")) {
            ll_fleet_managemnet.setVisibility(View.VISIBLE);
        } else {
            ll_fleet_managemnet.setVisibility(View.GONE);
        }


        //setAwardDummuData();
    }

    public void setOnClickListners() {

        btnEdit.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        shareWithTwiiter.setOnClickListener(this);
        shareWithFb.setOnClickListener(this);
        tvReply.setOnClickListener(this);
    }


    public void setJobStatusdata() {

        adptJobDetailsProgress = new AdptJobDetailsProgress(stringArrayList, appContext, jobStatus);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptJobDetailsProgress);
        if (jobStatus.equalsIgnoreCase("WorkDone")) {
            mLayoutManager.scrollToPosition(4);
        }
        if (jobStatus.equalsIgnoreCase("Pickup")) {
            mLayoutManager.scrollToPosition(5);
        }
    }

    public void setAwardDummuData() {
        adptAwardJobsCarOwner = new AdptAwardJobsCarOwner(awardJobArrayList, appContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        quotesRecyclerview.setLayoutManager(mLayoutManager);
        quotesRecyclerview.setItemAnimator(new DefaultItemAnimator());
        quotesRecyclerview.setAdapter(adptAwardJobsCarOwner);

    }

    public void setReplyReviewAdapter() {
        customListAdapterOther = new CustomListAdapterOther(list_itemsArrayList, appContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        jobsReviewResponses.setLayoutManager(mLayoutManager);
        jobsReviewResponses.setItemAnimator(new DefaultItemAnimator());
        jobsReviewResponses.setAdapter(customListAdapterOther);
    }

    public void getNewPostedJobData() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_VIEW);
        params.put(Constants.Reviews.JOB_ID, job_id);
        params.put(Constants.Forgotpassword.USER_TYPE, loginDetail_dbo.getUser_Type());
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "JOb Details --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                String userId = "";
                                JSONObject jsonObject = response.getJSONObject("data");
                                // userImage = response.getString("user_img");
                                JSONArray userDetailsArray = jsonObject.getJSONArray("garage_details");
                                JSONArray jobDetailsArray = jsonObject.getJSONArray("job_detail");
                                JSONArray carDetailsArray = jsonObject.getJSONArray("car_detail");
                                JSONArray jobBidsMasterArray = jsonObject.getJSONArray("job_bids_master");
                                JSONArray garageServicesArray = jsonObject.getJSONArray("garage_services");
                                JSONArray freeInclusionsArray = jsonObject.getJSONArray("free_inclusions");
                                JSONArray jobUSerReview = jsonObject.getJSONArray("job_user_review");
                                JSONArray jobUserReviewReplies = jsonObject.getJSONArray("job_user_review_replies");
                                JSONArray myBidArray = jsonObject.getJSONArray("mybid");
                                txtQuotes.setText("Quotes: " + jobBidsMasterArray.length());
                                cart_badge_footer.setText("" + jobBidsMasterArray.length());
                                notify_count_footer = String.valueOf(jobBidsMasterArray.length());
                                JobDetail_DBO jobDetail_dbo = new JobDetail_DBO();

                                if (jobBidsMasterArray.length() > 0) {
                                    for (int i = 0; i < jobBidsMasterArray.length(); i++) {
                                        JSONObject bidJobJsonObj = jobBidsMasterArray.getJSONObject(i);

                                        AwardJobDBOCarOwner awardJobDBOCarOwner = new AwardJobDBOCarOwner();
                                        awardJobDBOCarOwner.setJob_id(bidJobJsonObj.getString("job_id"));
                                        awardJobDBOCarOwner.setGarage_id(bidJobJsonObj.getString("garage_id"));
                                        garage_id = bidJobJsonObj.getString("garage_id");
                                        awardJobDBOCarOwner.setId(bidJobJsonObj.getString("id"));
                                        awardJobDBOCarOwner.setBid_price(bidJobJsonObj.getString("bid_price"));
                                        awardJobDBOCarOwner.setBid_comment(bidJobJsonObj.getString("bid_comment"));
                                        awardJobDBOCarOwner.setAdd_offer(bidJobJsonObj.getString("add_offer"));
                                        awardJobDBOCarOwner.setAdd_offer_price(bidJobJsonObj.getString("add_offer_price"));
                                        awardJobDBOCarOwner.setTotal(bidJobJsonObj.getString("total"));
                                        awardJobDBOCarOwner.setDistance(bidJobJsonObj.getString("distance"));
                                        awardJobDBOCarOwner.setReview_count(bidJobJsonObj.getString("review_count"));
                                        awardJobDBOCarOwner.setAvatar_img(bidJobJsonObj.getString("avatar_img"));
                                        name = bidJobJsonObj.getString("name");
                                        awardJobDBOCarOwner.setName(bidJobJsonObj.getString("name"));
                                        awardJobDBOCarOwner.setBid_status(bidJobJsonObj.getString("bid_status"));
                                        awardJobDBOCarOwner.setRating(bidJobJsonObj.getInt("rating"));
                                        awardJobDBOCarOwner.setService_id(bidJobJsonObj.optString("service_id"));
                                        shareGarageImg = WebServiceURLs.BASE_URL_IMAGE_PROFILE+bidJobJsonObj.getString("avatar_img");
                                        shareGarageName = bidJobJsonObj.getString("name");
                                        shareReview = bidJobJsonObj.getString("review_count");

                                        JSONArray servicesArray = bidJobJsonObj.optJSONArray("services");
                                        JSONArray inclusionArray = bidJobJsonObj.optJSONArray("free_inclusion");

                                        if (servicesArray.length() > 0) {
                                            garageServicesArrayList = new ArrayList<>();
                                            for (int j = 0; j < servicesArray.length(); j++) {
                                                JSONObject object = servicesArray.getJSONObject(j);
                                                Model_GarageServices model_garageServices = new Model_GarageServices();
                                                model_garageServices.setName(object.getString("value"));
                                                garageServicesArrayList.add(model_garageServices);

                                            }
                                            awardJobDBOCarOwner.setGarageServicesArrayList(garageServicesArrayList);

                                        }
                                        if (inclusionArray.length() > 0) {
                                            freeInclusionsArrayList = new ArrayList<>();
                                            for (int j = 0; j < inclusionArray.length(); j++) {
                                                JSONObject object = inclusionArray.getJSONObject(j);
                                                Model_FreeInclusions modelFreeInclusions = new Model_FreeInclusions();
                                                modelFreeInclusions.setId(object.getString("id"));
                                                modelFreeInclusions.setInclusion(object.getString("inclusion"));
                                                freeInclusionsArrayList.add(modelFreeInclusions);
                                            }
                                        }

                                        awardJobDBOCarOwner.setFreeInclusionsArrayList(freeInclusionsArrayList);

                                        awardJobDBOCarOwners.add(awardJobDBOCarOwner);
                                        garageIdList.add(garage_id);
                                        if (bidJobJsonObj.getString("bid_status").equalsIgnoreCase("awarded")) {
                                            awardJobArrayList.add(awardJobDBOCarOwner);
                                        }


                                    }

                                    setAwardDummuData();
                                }


                                if (garageServicesArray.length() > 0) {
                                    for (int i = 0; i < garageServicesArray.length(); i++) {
                                        JSONObject garageServiceJsonObj = garageServicesArray.optJSONObject(i);
                                        if (garageServiceJsonObj != null) {
                                            Model_GarageServices model_garageServices = new Model_GarageServices();
                                            model_garageServices.setId(garageServiceJsonObj.getString("id"));
                                            model_garageServices.setGarage_master_id(garageServiceJsonObj.getString("garage_master_id"));
                                            model_garageServices.setService_id(garageServiceJsonObj.getString("service_id"));
                                            model_garageServices.setName(garageServiceJsonObj.getString("name"));
                                            garageServicesArrayList1.add(model_garageServices);
                                        }
                                    }
                                    jobDetail_dbo.setGarageServicesArrayList(garageServicesArrayList1);
                                }
                                if (freeInclusionsArray.length() > 0) {
                                    for (int i = 0; i < freeInclusionsArray.length(); i++) {
                                        JSONObject freeInclJsonObj = freeInclusionsArray.optJSONObject(i);
                                        if (freeInclJsonObj != null) {
                                            Model_FreeInclusions modelFreeInclusions = new Model_FreeInclusions();
                                            modelFreeInclusions.setId(freeInclJsonObj.getString("id"));
                                            modelFreeInclusions.setInclusion(freeInclJsonObj.getString("inclusion"));
                                            freeInclusionsArrayList.add(modelFreeInclusions);
                                            jobDetail_dbo.setFreeInclusionsArrayList(freeInclusionsArrayList);
                                        }
                                    }
                                }
                                if (myBidArray.length() > 0) {
                                    for (int i = 0; i < myBidArray.length(); i++) {

                                        JSONObject jobj = myBidArray.getJSONObject(i);
                                        currentjobstatus = jobj.getString("bid_status");
                                    }
                                }
                                if (jobDetailsArray.length() > 0) {
                                    for (int i = 0; i < jobDetailsArray.length(); i++) {
                                        JSONObject jobJsonObj = jobDetailsArray.getJSONObject(i);
                                        String dropoff_date_time = jobJsonObj.getString("dropoff_date_time");
                                        String pickup_date_time = jobJsonObj.getString("pickup_date_time");
                                        String time_flexibility = jobJsonObj.getString("time_flexibility");
                                        if (time_flexibility.equalsIgnoreCase("Flexible") || time_flexibility.equalsIgnoreCase("Very Flexible")) {
                                            isFlexible = true;
                                        } else {
                                            isFlexible = false;
                                        }

                                        String problem_description = jobJsonObj.getString("problem_description");
                                        String is_emergency = jobJsonObj.getString("is_emergency");
                                        String is_help = jobJsonObj.getString("is_help");
                                        String is_insurance = jobJsonObj.getString("is_insurance");
                                        String jobnumber = jobJsonObj.getString("ju_id");
                                        fourjobid = cjob_id = jobJsonObj.getString("cjob_id");
                                        job_title = jobJsonObj.getString("job_title");
                                        userId = jobJsonObj.getString("user_id");
                                        String category_id = jobJsonObj.getString("category_id");
                                        cat_id = category_id;
                                        String subcategory_id = jobJsonObj.getString("subcategory_id");
                                        String sub_subcategory_id = jobJsonObj.getString("sub_subcategory_id");
                                        String catname = jobJsonObj.getString("catname");
                                        String subcatname = jobJsonObj.getString("subcatname");
                                        String subsubcatname = jobJsonObj.getString("subsubcatname");
                                        String current_tyre_brand = jobJsonObj.getString("current_tyre_brand");
                                        String current_tyre_model = jobJsonObj.getString("current_tyre_model");
                                        String tyre_detail_and_spec = jobJsonObj.getString("tyre_detail_and_spec");
                                        String no_of_tyres = jobJsonObj.getString("no_of_tyres");
                                        String loc_for_tow_or_road_assistance = jobJsonObj.getString("loc_for_tow_or_road_assistance");
                                        String destination_address = jobJsonObj.getString("destination_address");
                                        String inc_roadside_assist = jobJsonObj.getString("inc_roadside_assist");
                                        String inc_std_log_service = jobJsonObj.getString("inc_std_log_service");
                                        String number_of_vehicles = jobJsonObj.getString("number_of_vehicles");
                                        String additional_inclusions = jobJsonObj.getString("additional_data_job");
                                        currentjobstatus = jobStatus = jobJsonObj.getString("current_job_status");
                                        is_closed = jobJsonObj.optString("job_status");
                                        JSONArray carImagesArray = jobJsonObj.getJSONArray("car_images");
                                        JSONArray carVideosArray = jobJsonObj.getJSONArray("car_videos");


                                        String fname = jobJsonObj.optString("fname");
                                        String lname = jobJsonObj.optString("lname");
                                        String contact = jobJsonObj.optString("mobile");
                                        String image = jobJsonObj.optString("image");
                                        String suburb = jobJsonObj.optString("suburb");
                                        String state = jobJsonObj.optString("state");
                                        String postcode = jobJsonObj.optString("postcode");


                                        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {

                                            if (userId.equalsIgnoreCase(loginDetail_dbo.getUserid())) {
                                                txtUsername.setText(fname + " " + lname);
                                                txtMobile.setText("Mobile: " + contact);
                                                txtLocation.setText(suburb + ", " + state + ", " + postcode);
                                                btnEdit.setVisibility(View.VISIBLE);
                                                btnClose.setVisibility(View.VISIBLE);
                                                btnDelete.setVisibility(View.VISIBLE);

                                            } else {
                                                char firstCharacter = lname.charAt(0);

                                                String S = lname.replace(lname, "****");

                                                txtUsername.setText(fname + " " + firstCharacter + S);
                                                txtMobile.setText("Mobile: *********");
                                                txtLocation.setText("****" + ", " + state + ", " + postcode);
                                                btnEdit.setVisibility(View.GONE);
                                                btnClose.setVisibility(View.GONE);
                                                btnDelete.setVisibility(View.GONE);
                                            }
                                        } else {
                                            char firstCharacter = lname.charAt(0);

                                            String S = lname.replace(lname, "****");

                                            txtUsername.setText(fname + " " + firstCharacter + S);
                                            txtMobile.setText("Mobile: *********");
                                            txtLocation.setText("****" + ", " + state + ", " + postcode);
                                        }
                                        Glide.with(appContext)
                                                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + image)
                                                .asBitmap()
                                                .dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .placeholder(R.drawable.no_user)
                                                .error(R.drawable.no_user)
                                                .skipMemoryCache(true)
                                                .into(iv_user);

                                        txtJobTitle.setText(job_title);
                                        txtJobNumber.setText(jobnumber);
                                        txtCategory.setText(catname);
                                        txtSubCategory.setText(subcatname);
                                        txtjobDesc.setText(problem_description);
                                        txtCurrentTyreBrand.setText(current_tyre_brand);
                                        txtCurrentTyreModel.setText(current_tyre_model);
                                        txtTyreDetail.setText(tyre_detail_and_spec);
                                        txtSameorEquivalent.setText(subcatname);
                                        txtadditionalInclusions.setText(additional_inclusions);
                                        txt_include_roadside_assistance.setText(inc_roadside_assist);
                                        txt_include_standard_logbook.setText(inc_std_log_service);
                                        txt_no_of_vehicles.setText(number_of_vehicles);
                                        if (no_of_tyres.equalsIgnoreCase("1")) {
                                            txtNoTyreReplaced.setText(no_of_tyres + " Tyre");
                                        } else {
                                            txtNoTyreReplaced.setText(no_of_tyres + " Tyres");
                                        }
                                        if (additional_inclusions.equalsIgnoreCase("")) {
                                            ll_additional_inclusions.setVisibility(View.GONE);
                                        } else {
                                            ll_additional_inclusions.setVisibility(View.VISIBLE);

                                        }
                                        if (is_closed.equalsIgnoreCase("Closed")) {
                                            btnEdit.setVisibility(View.INVISIBLE);
                                            btnClose.setVisibility(View.INVISIBLE);
                                            //ll_quotes_from_garage.setVisibility(View.GONE);
                                        } else {
                                            btnEdit.setVisibility(View.VISIBLE);
                                            btnClose.setVisibility(View.VISIBLE);
                                        }


                                        if (jobStatus.equalsIgnoreCase("Quoted") && garageIdList.contains(loginDetail_dbo.getUserid())) {
                                            ll_quotes_from_garage.setVisibility(View.GONE);
                                            ll_edt_del_close.setVisibility(View.VISIBLE);
                                            btnEdit.setText("Update Quote");
                                            btnClose.setText("Cancel Quote");

                                        } else if (!garageIdList.contains(loginDetail_dbo.getUserid()) && jobStatus.equalsIgnoreCase("Quoted")) {
                                            ll_quotes_from_garage.setVisibility(View.GONE);
                                            ll_edt_del_close.setVisibility(View.VISIBLE);
                                            btnClose.setVisibility(View.GONE);
                                            btnEdit.setText("Quote");
                                            //btnClose.setText("Message");

                                        } else if (jobStatus.equalsIgnoreCase("Posted")) {
                                            ll_quotes_from_garage.setVisibility(View.GONE);
                                            ll_edt_del_close.setVisibility(View.VISIBLE);
                                            btnEdit.setText("Quote");
                                            btnClose.setVisibility(View.GONE);

                                        } else {
                                            ll_edt_del_close.setVisibility(View.GONE);
                                            ll_quotes_from_garage.setVisibility(View.VISIBLE);

                                        }


                                        setJobStatusdata();
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


                                        if (is_emergency.equalsIgnoreCase("YES")) {
                                            ll_emergency.setVisibility(View.VISIBLE);
                                        } else {
                                            ll_emergency.setVisibility(View.GONE);
                                        }
                                        if (is_insurance.equalsIgnoreCase("YES")) {
                                            ll_insurance_claim.setVisibility(View.VISIBLE);
                                            ll_insurance_company.setVisibility(View.VISIBLE);
                                            ll_policy_number.setVisibility(View.VISIBLE);
                                            ll_claim_number.setVisibility(View.VISIBLE);

                                        } else {
                                            ll_insurance_claim.setVisibility(View.GONE);
                                            ll_insurance_company.setVisibility(View.GONE);
                                            ll_policy_number.setVisibility(View.GONE);
                                            ll_claim_number.setVisibility(View.GONE);
                                        }


                                        if (carImagesArray.length() > 0) {
                                            ll_no_car_imgs.setVisibility(View.GONE);
                                            viewpager.setVisibility(View.VISIBLE);
                                            indicator.setVisibility(View.VISIBLE);
                                            for (int j = 0; j < carImagesArray.length(); j++) {
                                                JSONObject carImgJsonObj = carImagesArray.getJSONObject(j);
                                                CarImageDBO carImageDBO = new CarImageDBO();
                                                String carImg = carImgJsonObj.getString("url");
                                                carImageDBO.setUrl(carImgJsonObj.getString("url"));
                                                carImageArrayList.add(carImageDBO);
                                                jobDetail_dbo.setCarImageDBOArrayList(carImageArrayList);
                                            }
                                            pagerAdapterImage = new ViewPagerAdapterImage(appContext, carImageArrayList);
                                            viewpager.setAdapter(pagerAdapterImage);
                                            //viewpager1.setAdapter(pagerAdapterImage);
                                            indicator.setViewPager(viewpager);
                                            // indicator1.setViewPager(viewpager1);
                                        } else {
                                            ll_no_car_imgs.setVisibility(View.VISIBLE);
                                            viewpager.setVisibility(View.GONE);
                                            indicator.setVisibility(View.GONE);

                                        }

                                        if (carVideosArray.length() > 0) {
                                            ll_no_car_videos.setVisibility(View.GONE);
                                            viewpager1.setVisibility(View.VISIBLE);
                                            indicator1.setVisibility(View.VISIBLE);
                                            for (int j = 0; j < carVideosArray.length(); j++) {
                                                JSONObject carVideoJsonObj = carVideosArray.getJSONObject(j);
                                                CarVideosDbo carVideosDbo = new CarVideosDbo();
                                                carVideosDbo.setThumbnail(carVideoJsonObj.getString("thumbnail"));
                                                carVideosDbo.setVideoUrl(carVideoJsonObj.getString("video"));
                                                carVideosArrayList.add(carVideosDbo);
                                                jobDetail_dbo.setCarVideosDboArrayList(carVideosArrayList);


                                            }
                                            pagerAdapterVideo = new ViewPagerAdapterCarVideo(appContext, carVideosArrayList);
                                            viewpager1.setAdapter(pagerAdapterVideo);
                                            //viewpager1.setAdapter(pagerAdapterImage);
                                            indicator1.setViewPager(viewpager1);
                                        } else {
                                            ll_no_car_videos.setVisibility(View.VISIBLE);
                                            viewpager1.setVisibility(View.GONE);
                                            indicator1.setVisibility(View.GONE);
                                        }

                                        txtFlexibility.setText(time_flexibility);
                                        txtDropOffDate.setText(parseDateToddMMyyyy(dropoff_date_time));
                                        txtPickupDate.setText(parseDateToddMMyyyy(pickup_date_time));


                                    }
                                }
                                if (carDetailsArray.length() > 0) {
                                    for (int i = 0; i < carDetailsArray.length(); i++) {
                                        JSONObject carJsonObj = carDetailsArray.getJSONObject(i);
                                        String carmake = carJsonObj.getString("carmake");
                                        String carmodel = carJsonObj.getString("carmodel");
                                        String carbadge = carJsonObj.getString("carbadge");
                                        String registration_number = carJsonObj.getString("registration_number");
                                        String car_type = carJsonObj.getString("car_type");
                                        String km = carJsonObj.getString("km");
                                        String year = carJsonObj.getString("year");
                                        String car_id = carJsonObj.getString("id");

                                        txtMake.setText(carmake);
                                        txtModel.setText(carmodel);
                                        txtBadge.setText(carbadge);
                                        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {

                                            if (userId.equalsIgnoreCase(loginDetail_dbo.getUserid())) {
                                                txtRegNumber.setText(registration_number);
                                            } else {
                                                txtRegNumber.setText("*****");
                                            }

                                        } else {
                                            txtRegNumber.setText("*****");
                                        }

                                        txtTransmission.setText(car_type);
                                        txtYear.setText(year);

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
                                        jobDetail_dbo.setPending(false);

                                        HelperMethods.storeJobDetailsSharedPreferences(appContext, jobDetail_dbo);


                                    }
                                }

                                if (jobUSerReview.length() > 0) {
                                    jobReview.setVisibility(View.VISIBLE);
                                    ll_reply_to_user.setVisibility(View.VISIBLE);
                                    JSONObject jobj = jobUSerReview.getJSONObject(0);
                                    JSONObject userJsonObj = userDetailsArray.getJSONObject(0);


                                    Glide.with(appContext)
                                            .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + userJsonObj.getString("logo_image"))
                                            .asBitmap()
                                            .dontAnimate()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.no_user)
                                            .error(R.drawable.no_user)
                                            .skipMemoryCache(false)
                                            .into(iv_image);

                                    tv_name.setText(userJsonObj.getString("business_name"));
                                    rate_count.setText(jobj.getString("rating"));
                                    tv_desc.setText(jobj.getString("review_text"));
                                    ratingBar.setRating(Float.parseFloat(jobj.getString("rating")));

                                    if (jobUserReviewReplies.length() > 0) {
                                        for (int i = 0; i < jobUserReviewReplies.length(); i++) {
                                            JSONObject reviewRepies = jobUserReviewReplies.getJSONObject(i);
                                            List_items list = new List_items();
                                            list.setId(reviewRepies.getString("name"));
                                            list.setName(reviewRepies.getString("review_text"));
                                            list_itemsArrayList.add(list);
                                        }
                                        setReplyReviewAdapter();
                                    } else {
                                        jobsReviewResponses.setVisibility(View.GONE);
                                    }

                                    if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                                        //  llReply.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    jobReview.setVisibility(View.GONE);
                                    ll_reply_to_user.setVisibility(View.GONE);

                                }
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
                        showAlertDialog(getString(R.string.some_error_try_again));
                    }
                });
        queue.add(jsonObjReq);
    }

    public void ReplyToUser() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.REVIEW_REPLY);
        params.put(Constants.Reviews.JID, cjob_id);
        params.put(Constants.Reviews.REVIEW_TEXT, message.getText().toString().trim());
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "JOb Details --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                List_items data = new List_items();
                                data.setId(name);
                                data.setName(message.getText().toString());
                                list_itemsArrayList.add(data);
                                setReplyReviewAdapter();
                                isReply.setChecked(false);
                                message.setText("");

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
                        showAlertDialog(getString(R.string.some_error_try_again));
                    }
                });
        queue.add(jsonObjReq);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnEdit) {
            if (btnEdit.getText().toString().equalsIgnoreCase("Update Quote")) {
                Intent intent = new Intent(appContext, UpdateQuoteGarageActivity.class);
                intent.putExtra("jobTitle", job_title);
                intent.putExtra("isFlexible", isFlexible);
                intent.putExtra("jobBidmaster", awardJobDBOCarOwners);
                startActivity(intent);
                activityTransition();
            } else if (btnEdit.getText().toString().equalsIgnoreCase("Quote")) {
                Intent intent = new Intent(appContext, QuoteJobGarageActivity.class);
                intent.putExtra("isFlexible", isFlexible);
                intent.putExtra("jobTitle", job_title);
                startActivity(intent);
                activityTransition();
            }

        }
        if (view == btnClose) {
            if (btnClose.getText().toString().equalsIgnoreCase("Cancel Quote")) {
                ShowCancelQuoteDialog();
            }
        }
        if (view == shareWithFb) {
            sendData();
        }
        if (view == shareWithTwiiter) {
            shareTwitter();
        }
        if (view == tvReply) {
            if (!message.getText().toString().trim().equalsIgnoreCase("")) {
                if (Connectivity.isConnected(appContext)) {
                    ReplyToUser();
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            } else {
                showAlertDialog(getString(R.string.type_message));
            }
        }
    }

    public void ShowCancelQuoteDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext, R.style.AppCompatAlertDialogStyle);

        builder.setTitle("Cancel Quote")
                .setMessage("Are you sure you want to cancel your quote?")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (Connectivity.isConnected(appContext)) {
                            WebCallCancelQuote();
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();


    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    /*------------Web call for close job---------------*/
    public void WebCallCancelQuote() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.CANCEL_BID);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.SUBMIT_BIDS.JOB_ID, cjob_id);


        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Cancel Quote --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ViewNewJobsFeedDetailActivity.this, DashboardScreen.class);
                                startActivity(intent);
                                activityTransition();


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

    public void sendData() {
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String str="\""+tv_desc.getText().toString()+"\"";
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentUrl(Uri.parse("http://greasecrowd.com.au/"))
                    .setQuote(str+"\n\n"+shareGarageName+ "\nRated \""+ shareRatting +"\" out of 5 and "+ shareReview +" reviewed."+" \n#AUTOREUM")
                    .build();
            shareDialog.show(linkContent);
            // Show facebook ShareDialog

            //shareButton.setShareContent(linkContent);


        }
    }

    private void shareTwitter() {
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, "http://greasecrowd.com.au/");
        tweetIntent.setType("text/plain");

        PackageManager packManager = getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            startActivity(tweetIntent);
        } else {
            Intent i = new Intent();
            i.putExtra(Intent.EXTRA_TEXT, "http://greasecrowd.com.au/");
            i.setAction(Intent.ACTION_VIEW);
            i.setData(Uri.parse("https://twitter.com/intent/tweet?text=" + urlEncode("http://greasecrowd.com.au/")));
            startActivity(i);
        }
    }

    private String urlEncode(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.wtf(TAG, "UTF-8 should always be supported", e);
            return "";
        }
    }


    public class CustomListAdapterOther extends RecyclerView.Adapter<CustomListAdapterOther.ViewHolder> {
        ArrayList<List_items> list_items;
        Context context;

        public CustomListAdapterOther(ArrayList<List_items> list_items, Context context) {
            this.list_items = list_items;
            this.context = context;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_review_responses, parent, false);

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {

            List_items model = list_items.get(position);
            holder.response_title.setText("Response from " + model.getId());
            holder.tv_response.setText(model.getName());
        }

        @Override
        public int getItemCount() {
            return list_items.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView response_title, tv_response;

            public ViewHolder(View itemView) {
                super(itemView);

                response_title = (TextView) itemView.findViewById(R.id.response_title);
                tv_response = (TextView) itemView.findViewById(R.id.tv_response);

            }

        }
    }
}
