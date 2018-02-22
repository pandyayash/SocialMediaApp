package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import technource.greasecrowd.CustomViews.Widgets.CircularImageView;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.AskTheCrowd.AskTheCrowdStepOne;
import technource.greasecrowd.adapter.AdptJobDetailsProgress;
import technource.greasecrowd.adapter.ViewPagerAdapterCarVideo;
import technource.greasecrowd.adapter.ViewPagerAdapterImage;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.CarImageDBO;
import technource.greasecrowd.model.CarVideosDbo;
import technource.greasecrowd.model.LoginDetail_DBO;

public class MyCrowdDetailsActivity extends BaseActivity {
    String TAG = "MyCrowdDetailsActivity";
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    ViewPager viewpager, viewpager1;
    CircleIndicator indicator, indicator1;
    ViewPagerAdapterImage pagerAdapterImage;
    ViewPagerAdapterCarVideo pagerAdapterVideo;
    String job_id, userImage;
    CircularImageView iv_user;
    TextView txtUsername, txtLocation, txtDistance, txtMobile, txtMake, txtModel, txtBadge, txtRegNumber, txtTransmission, txtYear, txtDropOffDate, txtPickupDate, txtFlexibility;
    TextView txtInsuranceCompany, txtPolicynumber, txtClaimnumber, txtJobTitle, txtJobNumber, txtCategory, txtSubCategory, txtjobDesc;
    TextView txtCurrentTyreBrand, txtCurrentTyreModel, txtTyreDetail, txtNoTyreReplaced, txtSameorEquivalent, txtadditionalInclusions;
    TextView btnEdit, btnClose, btnDelete, txt_include_roadside_assistance, txt_include_standard_logbook, txt_no_of_vehicles, txtQuotes;
    ImageView iv_no_car_img;
    ArrayList<CarImageDBO> carImageArrayList;
    ArrayList<CarVideosDbo> carVideosArrayList;
    LinearLayout ll_additional_inclusions, ll_edt_del_close;
    LinearLayout ll_emergency, ll_insurance_claim, ll_insurance_company, ll_policy_number, ll_claim_number, ll_no_car_imgs, ll_no_car_videos, ll_quotes_from_garage, ll_auto_tyre, ll_fleet_managemnet;
    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    AdptJobDetailsProgress adptJobDetailsProgress;
    String responses, job_title, cjob_id, cat_id, is_closed = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crowd_details);
        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            cat_id = intent.getStringExtra("cat_id");
            responses = intent.getStringExtra("responses");
            //Toast.makeText(getApplicationContext(), cat_id, Toast.LENGTH_SHORT).show();
        }
        getViews();
        setHeader("Crowd Details");
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {

            setfooter("crowd");
        } else {
            setfooter("garageowner");
        }
        setlistenrforfooter();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);


        setOnClickListners();
        txtQuotes.setText("Responses : " + responses);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            setMyCrowdFooter(this);
        } else {
            setMyCrowdFooterGarage(this);
        }
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


        viewpager = (ViewPager) findViewById(R.id.viewpager);
        viewpager1 = (ViewPager) findViewById(R.id.viewpager1);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator1 = (CircleIndicator) findViewById(R.id.indicator1);


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

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")){
            ll_edt_del_close.setVisibility(View.GONE);
        }else {
            ll_edt_del_close.setVisibility(View.VISIBLE);
        }


        //setAwardDummuData();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnClose) {
            if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                Intent intent = new Intent(MyCrowdDetailsActivity.this, AskTheCrowdStepOne.class);
                startActivity(intent);
                activityTransition();
            } else {
                Toast.makeText(appContext, "Comming Soon", Toast.LENGTH_SHORT).show();
            }

        }
        if (view == btnDelete) {
            if (Connectivity.isConnected(appContext)) {
                if (!btnDelete.getText().toString().equalsIgnoreCase("JOB POSTED")) {
                    ConvertCrowdToJob();
                } else {
                    Toast.makeText(appContext, "Job is already posted.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(appContext, "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            }

        }
    }

    public void setOnClickListners() {

        btnEdit.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        btnDelete.setOnClickListener(this);
    }

    public void getNewPostedJobData() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GET_CROWD_DETAILS);
        params.put(Constants.PostNewJob.JID, job_id);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                AppLog.Log("Response", "Crowd Details --> " + response);
                                JSONArray jobDetailsArray = response.getJSONArray("job_details");
                                JSONArray carDetailsArray = response.getJSONArray("car_details");
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
                                        cjob_id = jobJsonObj.getString("cjob_id");
                                        job_title = jobJsonObj.getString("job_title");
                                        String category_id = jobJsonObj.getString("category_id");
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
                                        String additional_inclusions = jobJsonObj.optString("additional_data_job");
                                        JSONArray carImagesArray = jobJsonObj.getJSONArray("car_images");
                                        JSONArray carVideosArray = jobJsonObj.getJSONArray("car_videos");

                                        if (jobJsonObj.getString("user_id").equalsIgnoreCase(loginDetail_dbo.getUserid()) && loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                                            btnDelete.setVisibility(View.VISIBLE);
                                            /*if (jobJsonObj.getString("ispostedtojob").equalsIgnoreCase("1")) {
                                                btnDelete.setText("JOB POSTED");

                                            } else {
                                                btnDelete.setText("Convert to a Job");
                                            }*/
                                        } else {
                                            btnDelete.setVisibility(View.GONE);
                                        }
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
                                        txtUsername.setText(jobJsonObj.getString("fname") + " " + jobJsonObj.getString("lname"));
                                        txtMobile.setText("Mobile " + jobJsonObj.getString("mobile"));

                                        Glide.with(appContext)
                                                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobJsonObj.getString("image"))
                                                .asBitmap()
                                                .dontAnimate()
                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                                .placeholder(R.drawable.no_user)
                                                .error(R.drawable.no_user)
                                                .skipMemoryCache(true)
                                                .into(iv_user);

                                        txtLocation.setText(jobJsonObj.getString("suburb") + ", " + jobJsonObj.getString("state") + ", " + jobJsonObj.getString("postcode"));

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


                                            }
                                            pagerAdapterImage = new ViewPagerAdapterImage(appContext, carImageArrayList);
                                            viewpager.setAdapter(pagerAdapterImage);
                                            indicator.setViewPager(viewpager);
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
                                            }
                                            pagerAdapterVideo = new ViewPagerAdapterCarVideo(appContext, carVideosArrayList);
                                            viewpager1.setAdapter(pagerAdapterVideo);
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
                                        txtRegNumber.setText(registration_number);
                                        txtTransmission.setText(car_type);
                                        txtYear.setText(year);
                                    }
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


    public void ConvertCrowdToJob() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.CROWD_TO_JOB);
        params.put("ask_crowd_job_id", job_id);
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
                                Toast.makeText(appContext, "" + response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                btnDelete.setText("JOB POSTED");
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
}
