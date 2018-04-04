package technource.autoreum.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import technource.autoreum.activities.PostJob.EditJobStepOne;
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

import static technource.autoreum.helper.Constants.notify_count_footer;

public class JobDetailsActivity extends BaseActivity {
    String TAG = "JobDetailsActivity";
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    ViewPager viewpager, viewpager1;
    CircleIndicator indicator, indicator1;
    ViewPagerAdapterImage pagerAdapterImage;
    ViewPagerAdapterCarVideo pagerAdapterVideo;
    //String job_id,cat_id,userImage;
    CircularImageView iv_user;
    TextView txtUsername, txtLocation, txtDistance, txtMobile, txtMake, txtModel, txtBadge, txtRegNumber, txtTransmission, txtYear, txtDropOffDate, txtPickupDate, txtFlexibility;
    TextView txtInsuranceCompany, txtPolicynumber, txtClaimnumber, txtJobTitle, txtJobNumber, txtCategory, txtSubCategory, txtjobDesc, txtDropOffLocation;
    TextView txtCurrentTyreBrand, txtCurrentTyreModel, txtTyreDetail, txtNoTyreReplaced, txtSameorEquivalent, txtadditionalInclusions;
    TextView btnEdit, btnClose, btnDelete, txt_include_roadside_assistance, txt_include_standard_logbook, txt_no_of_vehicles, txtQuotes;
    ImageView iv_no_car_img;
    LinearLayout ll_pick_up, ll_drop_off, ll_time_flexibility;
    TextView txtCurrentLocation, txtdestAddress;
    ArrayList<CarImageDBO> carImageArrayList;
    ArrayList<CarVideosDbo> carVideosArrayList;
    LinearLayout ll_additional_inclusions, ll_edt_del_close, ll_auto_panel, ll_current_location, ll_destination_address;
    LinearLayout ll_emergency, ll_insurance_claim, ll_insurance_company, ll_policy_number, ll_claim_number, ll_no_car_imgs, ll_no_car_videos, ll_quotes_from_garage, ll_auto_tyre, ll_fleet_managemnet;
    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    AdptJobDetailsProgress adptJobDetailsProgress;
    String jobStatus, job_title, cjob_id, is_closed = "";
    RecyclerView quotesRecyclerview;
    AdptAwardJobsCarOwner adptAwardJobsCarOwner;
    ImageView iv_image;
    TextView tv_name, rate_count, tv_desc, txtSubmitReview;
    SimpleRatingBar ratingBar;
    RecyclerView jobsReviewResponses;
    LinearLayout jobReview;
    CustomListAdapterOther customListAdapterOther;
    public static boolean isPaid = false;
    public static String paymentType = "";
    ImageView shareWithFb, shareWithTwiiter;
    /*LinearLayout llReply, llReplyMessage;*//*
    CheckBox isReply;
    EditText message;*/
    boolean isFromNewJobFeed = false;
    TextView cart_badge_footer;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareButton shareButton;
    String shareGarageImg,shareGarageName,shareReview,shareRatting;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
        Intent intent = getIntent();
        if (intent != null) {

            if (intent.hasExtra("isFromNewJobFeed")) {
                isFromNewJobFeed = intent.getBooleanExtra("isFromNewJobFeed", false);
                Constants.isFromNewJobFeed = isFromNewJobFeed;
            }
            if (intent.hasExtra("job_id")) {
                //Constants.Job_idBaseActivity = intent.getStringExtra("job_id");
                job_id = intent.getStringExtra("job_id");

                // = intent.getStringExtra("cat_id");
                //Constants.cat_idBaseActivity = intent.getStringExtra("cat_id");
            }
            //Toast.makeText(getApplicationContext(), cat_id, Toast.LENGTH_SHORT).show();
        }

        setHeader("Job Details");
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
        //shareDialog.registerCallback(callbackManager, callback);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setJobDetailsFooter(getApplicationContext());
        Constants.isFromJobDetails = true;
    }


    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        carImageArrayList = new ArrayList<>();
        carVideosArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        awardJobArrayList = new ArrayList<>();

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
        txtDropOffLocation = (TextView) findViewById(R.id.txtDropOffLocation);
        txtCurrentLocation = (TextView) findViewById(R.id.txtCurrentLocation);
        txtdestAddress = (TextView) findViewById(R.id.txtdestAddress);

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
        ll_auto_panel = (LinearLayout) findViewById(R.id.ll_auto_panel);
        ll_current_location = (LinearLayout) findViewById(R.id.ll_current_location);
        ll_destination_address = (LinearLayout) findViewById(R.id.ll_destination_address);
        ll_pick_up = (LinearLayout) findViewById(R.id.ll_pick_up);
        ll_drop_off = (LinearLayout) findViewById(R.id.ll_drop_off);
        ll_time_flexibility = (LinearLayout) findViewById(R.id.ll_time_flexibility);


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
        cart_badge_footer = (TextView) findViewById(R.id.cart_badge_footer);
        /*llReply = (LinearLayout) findViewById(R.id.llReply);
        llReplyMessage = (LinearLayout) findViewById(R.id.llReplyMessage);
        message = (EditText) findViewById(R.id.message);*/


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
        if (cat_id.equalsIgnoreCase("6")) {
            ll_auto_panel.setVisibility(View.VISIBLE);
        } else {
            ll_auto_panel.setVisibility(View.GONE);
        }
        if (cat_id.equalsIgnoreCase("10")) {
            ll_destination_address.setVisibility(View.VISIBLE);
            ll_current_location.setVisibility(View.VISIBLE);
            ll_pick_up.setVisibility(View.GONE);
            ll_drop_off.setVisibility(View.GONE);
            ll_time_flexibility.setVisibility(View.GONE);
        } else {
            ll_destination_address.setVisibility(View.GONE);
            ll_current_location.setVisibility(View.GONE);
            ll_pick_up.setVisibility(View.VISIBLE);
            ll_drop_off.setVisibility(View.VISIBLE);
            ll_time_flexibility.setVisibility(View.VISIBLE);
        }


        //setAwardDummuData();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    public void setOnClickListners() {

        btnEdit.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
        shareWithTwiiter.setOnClickListener(this);
        shareWithFb.setOnClickListener(this);
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

    public void setReplyReviewAdapter(ArrayList<List_items> items) {
        customListAdapterOther = new CustomListAdapterOther(items, appContext);
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
                                JSONObject jsonObject = response.getJSONObject("data");
                                // userImage = response.getString("user_img");
                                JSONArray userDetailsArray = jsonObject.getJSONArray("user_details");
                                JSONArray dropoff_location = jsonObject.getJSONArray("drop_off_location");
                                JSONArray jobDetailsArray = jsonObject.getJSONArray("job_detail");
                                JSONArray carDetailsArray = jsonObject.getJSONArray("car_detail");
                                JSONArray jobBidsMasterArray = jsonObject.getJSONArray("job_bids_master");
                                JSONArray jobUSerReview = jsonObject.getJSONArray("job_user_review");
                                JSONArray jobUserReviewReplies = jsonObject.getJSONArray("job_user_review_replies");
                                JSONArray paymentDetails = jsonObject.getJSONArray("payment_det");
                                txtQuotes.setText("Quotes: " + jobBidsMasterArray.length());
                                cart_badge_footer.setText("" + jobBidsMasterArray.length());
                                notify_count_footer = String.valueOf(jobBidsMasterArray.length());
                                JobDetail_DBO jobDetail_dbo = new JobDetail_DBO();

                                if (paymentDetails.length() > 0) {
                                    isPaid = true;
                                    for (int i = 0; i < paymentDetails.length(); i++) {
                                        JSONObject object = paymentDetails.getJSONObject(i);
                                        paymentType = object.getString("type");
                                    }
                                } else {

                                    isPaid = false;
                                }

                                if (jobBidsMasterArray.length() > 0) {
                                    for (int i = 0; i < jobBidsMasterArray.length(); i++) {
                                        JSONObject bidJobJsonObj = jobBidsMasterArray.getJSONObject(i);

                                        AwardJobDBOCarOwner awardJobDBOCarOwner = new AwardJobDBOCarOwner();

                                        awardJobDBOCarOwner.setJob_id(bidJobJsonObj.getString("job_id"));
                                        awardJobDBOCarOwner.setGarage_id(bidJobJsonObj.getString("garage_id"));
                                        awardJobDBOCarOwner.setId(bidJobJsonObj.getString("id"));
                                        awardJobDBOCarOwner.setBid_price(bidJobJsonObj.getString("bid_price"));
                                        awardJobDBOCarOwner.setBid_comment(bidJobJsonObj.getString("bid_comment"));
                                        awardJobDBOCarOwner.setAdd_offer(bidJobJsonObj.getString("add_offer"));
                                        awardJobDBOCarOwner.setAdd_offer_price(bidJobJsonObj.getString("add_offer_price"));
                                        awardJobDBOCarOwner.setTotal(bidJobJsonObj.getString("total"));
                                        awardJobDBOCarOwner.setDistance(bidJobJsonObj.getString("distance"));
                                        awardJobDBOCarOwner.setReview_count(bidJobJsonObj.getString("review_count"));
                                        awardJobDBOCarOwner.setAvatar_img(bidJobJsonObj.getString("avatar_img"));
                                        awardJobDBOCarOwner.setName(bidJobJsonObj.getString("name"));
                                        awardJobDBOCarOwner.setBid_status(bidJobJsonObj.getString("bid_status"));
                                        awardJobDBOCarOwner.setRating(Float.parseFloat(bidJobJsonObj.getString("rating")));
                                        shareGarageImg = WebServiceURLs.BASE_URL_IMAGE_PROFILE+bidJobJsonObj.getString("avatar_img");
                                        shareGarageName = bidJobJsonObj.getString("name");
                                        shareReview = bidJobJsonObj.getString("review_count");
                                        shareRatting = String.valueOf(Float.parseFloat(bidJobJsonObj.getString("rating")));
                                        if (bidJobJsonObj.getString("bid_status").equalsIgnoreCase("awarded"))
                                            awardJobArrayList.add(awardJobDBOCarOwner);

                                    }
                                    setAwardDummuData();
                                }

                                if (userDetailsArray.length() > 0) {
                                    for (int i = 0; i < userDetailsArray.length(); i++) {
                                        JSONObject userJsonObj = userDetailsArray.getJSONObject(i);
                                        String fname = userJsonObj.getString("fname");
                                        String lname = userJsonObj.getString("lname");
                                        String contact = userJsonObj.getString("mobile");
                                        String image = userJsonObj.getString("image");
                                        String suburb = userJsonObj.getString("suburb");
                                        String state = userJsonObj.getString("state");
                                        String postcode = userJsonObj.getString("postcode");

                                        txtUsername.setText(fname + " " + lname);
                                        //txtDistance.setText();
                                        txtMobile.setText("Mobile: " + contact);
                                        Glide.with(appContext)
                                                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + image)
                                                .asBitmap()
                                                .dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .placeholder(R.drawable.no_user)
                                                .error(R.drawable.no_user)
                                                .skipMemoryCache(true)
                                                .into(iv_user);
                                        txtLocation.setText(suburb + ", " + state + ", " + postcode);
                                    }
                                }
                                if (jobDetailsArray.length() > 0) {
                                    for (int i = 0; i < jobDetailsArray.length(); i++) {
                                        JSONObject jobJsonObj = jobDetailsArray.getJSONObject(i);
                                        String dropoff_date_time = jobJsonObj.getString("dropoff_date_time");
                                        String pickup_date_time = jobJsonObj.getString("pickup_date_time");
                                        String time_flexibility = jobJsonObj.getString("time_flexibility");
                                        String problem_description = jobJsonObj.getString("problem_description");
                                        String is_emergency = jobJsonObj.getString("is_emergency");
                                        String is_help = jobJsonObj.getString("is_help");
                                        String is_insurance = jobJsonObj.getString("is_insurance");
                                        String jobnumber = jobJsonObj.getString("ju_id");
                                        fourjobid = cjob_id = jobJsonObj.getString("cjob_id");
                                        job_title = jobJsonObj.getString("job_title");
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

                                        for (int j = 0; j < dropoff_location.length(); j++) {
                                            JSONObject addressObj = dropoff_location.getJSONObject(j);
                                            txtDropOffLocation.setText(addressObj.getString("address"));
                                        }
                                        currentjobstatus = jobStatus = jobJsonObj.getString("current_job_status");
                                        is_closed = jobJsonObj.getString("job_status");
                                        JSONArray carImagesArray = jobJsonObj.getJSONArray("car_images");
                                        JSONArray carVideosArray = jobJsonObj.getJSONArray("car_videos");

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
                                        txtCurrentLocation.setText(loc_for_tow_or_road_assistance);
                                        txtdestAddress.setText(destination_address);

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


                                        if (jobStatus.equalsIgnoreCase("Quoted")) {
                                            ll_quotes_from_garage.setVisibility(View.GONE);
                                            ll_edt_del_close.setVisibility(View.VISIBLE);

                                        } else if (jobStatus.equalsIgnoreCase("Posted")) {
                                            ll_quotes_from_garage.setVisibility(View.GONE);
                                            ll_edt_del_close.setVisibility(View.VISIBLE);

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
                                        jobDetail_dbo.setJob_status(jobStatus);


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
                                        if (carbadge.equalsIgnoreCase("")) {
                                            txtBadge.setText("N/A");
                                        } else {
                                            txtBadge.setText(carbadge);
                                        }

                                        txtRegNumber.setText(registration_number);
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
                                    JSONObject jobj = jobUSerReview.getJSONObject(0);
                                    JSONObject userJsonObj = userDetailsArray.getJSONObject(0);


                                    Glide.with(appContext)
                                            .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + userJsonObj.getString("image"))
                                            .asBitmap()
                                            .dontAnimate()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .placeholder(R.drawable.no_user)
                                            .error(R.drawable.no_user)
                                            .skipMemoryCache(false)
                                            .into(iv_image);

                                    tv_name.setText(userJsonObj.getString("fname") + " " + userJsonObj.getString("lname"));
                                    rate_count.setText(jobj.getString("rating"));
                                    tv_desc.setText(jobj.getString("review_text"));
                                    ratingBar.setRating(Float.parseFloat(jobj.getString("rating")));
                                    ArrayList<List_items> items = new ArrayList<>();
                                    if (jobUserReviewReplies.length() > 0) {
                                        for (int i = 0; i < jobUserReviewReplies.length(); i++) {
                                            JSONObject reviewRepies = jobUserReviewReplies.getJSONObject(i);
                                            List_items list = new List_items();
                                            list.setId(reviewRepies.getString("name"));
                                            list.setName(reviewRepies.getString("review_text"));
                                            items.add(list);
                                        }
                                        setReplyReviewAdapter(items);
                                    } else {
                                        jobsReviewResponses.setVisibility(View.GONE);
                                    }

                                    if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                                        //  llReply.setVisibility(View.VISIBLE);
                                    }
                                } else {
                                    jobReview.setVisibility(View.GONE);

                                }

                                if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                                    if (isFromNewJobFeed && job_id.equalsIgnoreCase(txtJobNumber.getText().toString())) {

                                        ll_edt_del_close.setVisibility(View.VISIBLE);


                                    } /*else {

                                        ll_edt_del_close.setVisibility(View.GONE);
                                    }*/
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnEdit) {
            Intent intent = new Intent(appContext, EditJobStepOne.class);
            intent.putExtra("job_id", cjob_id);
            intent.putExtra("job_title", job_title);
            intent.putExtra("pending_job", false);
            startActivity(intent);
            activityTransition();
        }

        if (view == btnClose) {
            popupCloseJOb();
        }
        if (view == btnDelete) {
            popupDeleteJOb();
        }
        if (view == shareWithFb) {
            sendData();
            // shareAppLinkViaFacebook("http://greasecrowd.com.au/");
        }
        if (view == shareWithTwiiter) {
            shareTwitter();
        }
    }

    public void popupCloseJOb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);

        builder.setTitle("Close Job")
                .setMessage("Are you sure you want to close this job?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (Connectivity.isConnected(appContext)) {
                            WebCallCloseJob("CLO");
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
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

            shareButton.setShareContent(linkContent);


        }
       /* SharePhoto photo = new SharePhoto.Builder()
                .setImageUrl(Uri.parse(shareGarageImg))
                .setCaption(shareGarageName)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();
        shareButton.setShareContent(content);*/
       /* if (ShareDialog.canShow(ShareOpenGraphContent.class)) {

            ShareOpenGraphObject object = new ShareOpenGraphObject.Builder()
                    .putString("og:title", "A Game of Thrones")
                    .putString("og:description", "In the frozen wastes to the north of Winterfell, sinister and supernatural forces are mustering.")
                    .putString("og:image", "https://www.enterprise.com/content/dam/ecom/utilitarian/common/Meet%20the%20Fleet/luxury_cadi-xts.png.wrend.1280.720.png")
                    .putString("og:url", "http://greasecrowd.com.au/")
                    .build();
            ShareOpenGraphAction action = new ShareOpenGraphAction.Builder()
                    .setActionType("books.reads")
                    .putObject("book", object)
                    .build();

            ShareOpenGraphContent content = new ShareOpenGraphContent.Builder()
                    .setPreviewPropertyName("book")
                    .setAction(action)
                    .build();
            shareDialog.show(content);
            shareButton.setShareContent(content);
        }
*/
    }

    private void shareAppLinkViaFacebook(String urlToShare) {

        String str = "\"" + loginDetail_dbo.getLast_name() + "\"";
        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Content to share");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, urlToShare);
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, str);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
        for (final ResolveInfo app : activityList) {
            if ((app.activityInfo.name).contains("facebook")) {
                final ActivityInfo activity = app.activityInfo;
                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                shareIntent.setComponent(name);
                startActivity(shareIntent);
                break;
            }
        }
    }

    private void shareTwitter() {
        String reviewComment="\""+tv_desc.getText().toString()+"\"";
        String str = "Rated "+shareRatting+" stars for "+loginDetail_dbo.getFirst_name()+" " +loginDetail_dbo.getLast_name()+"\n"+reviewComment+"\n\n"+shareGarageName+"\n#AUTOREUM"+"\n"+WebServiceURLs.BASE_URL_IMAGE_PROFILE;
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT,  "http://greasecrowd.com.au/");
        tweetIntent.putExtra(Intent.EXTRA_TEXT, str);
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
            i.putExtra(Intent.EXTRA_TEXT, str);
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


    public void popupDeleteJOb() {
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext, R.style.AppCompatAlertDialogStyle);

        builder.setTitle("Delete Job")
                .setMessage("Are you sure you want to delete this job?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (Connectivity.isConnected(appContext)) {
                            WebCallCloseJob("DEL");
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }


    /*------------Web call for close job---------------*/
    public void WebCallCloseJob(String action) {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.PRICE, "");
        params.put(Constants.JOB_ACTIONS.ADD_OFFER, "");
        params.put(Constants.JOB_ACTIONS.JID, cjob_id);
        params.put(Constants.JOB_ACTIONS.AWARDED_GARAGE_ID, "");
        params.put(Constants.JOB_ACTIONS.SEND_NEW_TIME, "");

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Close JOb --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(JobDetailsActivity.this, MyJobsUserActivity.class);
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
