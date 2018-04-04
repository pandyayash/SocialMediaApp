package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.relex.circleindicator.CircleIndicator;
import technource.autoreum.R;
import technource.autoreum.adapter.ViewPagerAdapterImage;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.CarImageDBO;
import technource.autoreum.model.FacilitiesDBo;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.ReviewsDBO;
import technource.autoreum.model.ServicesDBO;
import technource.autoreum.model.TraddingHoursDBo;

public class GarageOwnerDetailsActivity extends BaseActivity {

    public TextView gallery_big, hours_big, services_big, facilities_big, reviews_big, gallery, hours, services, facilities, reviews;
    private TextView tvGarageName;
    RelativeLayout footer_small;
    RelativeLayout footer_big;
    private RelativeLayout img;
    private ImageView ivImage;
    private SimpleRatingBar ratingBar;
    private TextView rateCount;
    private TextView tvReviews;
    private TextView tvDistnace;
    private TextView tvLocation;
    private TextView tvCall;
    private TextView tvMsg;
    private TextView tvVerified;
    private LinearLayout llNoCarImgs;
    private ImageView ivNoCarImg;
    private ViewPager viewpager;
    private CircleIndicator indicator;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    ViewPagerAdapterImage pagerAdapterImage;
    ArrayList<CarImageDBO> carImageArrayList;
    LinearLayout ll_verified, ll_map, ll_call, ll_mail, ll_back;
    String subrub, state, phone, mail;
    double lat, lng;
    int screen_width;
    ArrayList<TraddingHoursDBo> traddingHoursDBoArrayList;
    ArrayList<ServicesDBO> servicesDBOs;
    ArrayList<FacilitiesDBo> facilitiesDBos;
    ArrayList<ReviewsDBO> reviewsDBOs;
    String bn = "", avg_rating = "", profile_image = "",distnace="",review_count="";
    ImageView img_verify_1,img_verify_2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_details);
        findViews();
        checkscreensize();
        setlayoutDesign();
        setOnclickListener();

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            setfooter("garageowner");
        } else {
            setfooter("jobs");
        }
        setlistenrforfooter();
        setNoFooter(appContext);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("garage_id")) {
                if (Connectivity.isConnected(appContext)) {
                    GetGarageDetails(intent.getStringExtra("garage_id"));
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }
        }
    }

    private void findViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tvGarageName = (TextView) findViewById(R.id.tvGarageName);
        carImageArrayList = new ArrayList<>();
        img = (RelativeLayout) findViewById(R.id.img);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        rateCount = (TextView) findViewById(R.id.rate_count);
        tvReviews = (TextView) findViewById(R.id.tvReviews);
        tvDistnace = (TextView) findViewById(R.id.tvDistnace);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvCall = (TextView) findViewById(R.id.tvCall);
        tvMsg = (TextView) findViewById(R.id.tvMsg);
        tvVerified = (TextView) findViewById(R.id.tvVerified);
        llNoCarImgs = (LinearLayout) findViewById(R.id.ll_no_car_imgs);
        ivNoCarImg = (ImageView) findViewById(R.id.iv_no_car_img);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        indicator = (CircleIndicator) findViewById(R.id.indicator);
        ll_verified = (LinearLayout) findViewById(R.id.ll_verified);
        ll_map = (LinearLayout) findViewById(R.id.ll_map);
        ll_call = (LinearLayout) findViewById(R.id.ll_call);
        ll_mail = (LinearLayout) findViewById(R.id.ll_mail);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        footer_small = (RelativeLayout) findViewById(R.id.footer_small);
        footer_big = (RelativeLayout) findViewById(R.id.footer_big);

        gallery_big = (TextView) findViewById(R.id.gallery_big);
        hours_big = (TextView) findViewById(R.id.hours_big);
        services_big = (TextView) findViewById(R.id.services_big);
        facilities_big = (TextView) findViewById(R.id.facilitie_big);
        reviews_big = (TextView) findViewById(R.id.reviews_big);

        gallery = (TextView) findViewById(R.id.gallery);
        hours = (TextView) findViewById(R.id.hours);
        services = (TextView) findViewById(R.id.services);
        facilities = (TextView) findViewById(R.id.facilities);
        reviews = (TextView) findViewById(R.id.reviews);
        img_verify_1 = (ImageView) findViewById(R.id.img_verify_1);
        img_verify_2 = (ImageView) findViewById(R.id.img_verify_2);
    }

    public void setOnclickListener() {
        ll_map.setOnClickListener(this);
        ll_call.setOnClickListener(this);
        ll_mail.setOnClickListener(this);
        ll_back.setOnClickListener(this);

        gallery_big.setOnClickListener(this);
        hours_big.setOnClickListener(this);
        services_big.setOnClickListener(this);
        facilities_big.setOnClickListener(this);
        reviews_big.setOnClickListener(this);

        gallery.setOnClickListener(this);
        hours.setOnClickListener(this);
        services.setOnClickListener(this);
        facilities.setOnClickListener(this);
        reviews.setOnClickListener(this);

    }

    private void checkscreensize() {
        //1080 * 1920 (height width for 5'
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screen_width = metrics.widthPixels;
    }

    private void setlayoutDesign() {
        if (screen_width >= 720) {
            footer_big.setVisibility(View.VISIBLE);
            footer_small.setVisibility(View.GONE);
        } else {
            footer_big.setVisibility(View.VISIBLE);
            footer_small.setVisibility(View.GONE);
        }

    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_map:
                intent = new Intent(appContext, GarageOwnerProfileMapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                b.putString("subrub", subrub);
                b.putString("state", state);
                intent.putExtras(b);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.ll_call:
                intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                startActivity(intent);
                activityTransition();
                break;
            case R.id.ll_mail:
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(mail) + "?subject=" + Uri.encode("Contact Garage") + "&body=" + Uri.encode("");
                Uri uri = Uri.parse(uriText);
                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.hours_big:
            case R.id.hours:
                //Add activity here
                intent = new Intent(appContext, DisplayGarageOwnerHoursActivity.class);
                intent.putExtra("data", traddingHoursDBoArrayList);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.services_big:
            case R.id.services:
                //Add activity here
                intent = new Intent(appContext, DisplayGarageOwnerServicesActivity.class);
                intent.putExtra("data", servicesDBOs);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.facilitie_big:
            case R.id.facilities:
                //Add activity here
                intent = new Intent(appContext, DisplayGarageOwnerFacilitiesActivity.class);
                intent.putExtra("data", facilitiesDBos);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.reviews_big:
            case R.id.reviews:
                //Add activity here
                intent = new Intent(appContext, DisplayGarageOwnerReviewsActivity.class);
                intent.putExtra("data", reviewsDBOs);
                intent.putExtra("bn", bn);
                intent.putExtra("avg_rating", avg_rating);
                intent.putExtra("img", profile_image);
                intent.putExtra("distnace", distnace);
                intent.putExtra("review_count", review_count);
                startActivity(intent);
                activityTransition();
                break;
        }

    }

    public void GetGarageDetails(String garageId) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Changepassword.SERVICE_NAME, "garagepublicview");
        params.put("garage_id", garageId);
        params.put("user_type", loginDetail_dbo.getUser_Type());
        AppLog.Log("params", "" + params);

        final CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", "" + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject data = response.getJSONObject("data");
                                JSONArray garageData = data.getJSONArray("garage_details");
                                JSONArray garageReviews = data.getJSONArray("garage_reviews");
                                JSONArray garageReviewsReplies = data.getJSONArray("job_user_review_replies");

                                JSONArray garageSettings = data.getJSONArray("garage_settings");
                                JSONArray garageTradding = data.getJSONArray("garage_trading_hours");
                                JSONArray garageServices = data.getJSONArray("garage_services");
                                JSONArray garageFacility = data.getJSONArray("garage_facilities");


                                JSONObject garageGallery = garageSettings.getJSONObject(0);

                                JSONArray galleryImages = garageGallery.getJSONArray("garage_gallery");

                                JSONObject garageDataobj = garageData.getJSONObject(0);

                                bn = garageDataobj.getString("business_name");
                                avg_rating = data.getString("avg_rating");
                                profile_image = garageDataobj.getString("logo_image");

                                tvGarageName.setText(garageDataobj.getString("business_name"));
                                setHeader(garageDataobj.getString("business_name"));
                                Glide.with(appContext)
                                        .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + garageDataobj.getString("logo_image"))
                                        .asBitmap()
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.NONE)
                                        .placeholder(R.drawable.car_demo)
                                        .error(R.drawable.car_demo)
                                        .skipMemoryCache(true)
                                        .into(ivImage);
                                ratingBar.setRating(Float.parseFloat(data.getString("avg_rating")));
                                rateCount.setText(data.getString("avg_rating"));
                                tvReviews.setText(garageReviews.length() + " Reviews");
                                review_count = String.valueOf(garageReviews.length());
                                distnace = String.format("%.0f", Float.parseFloat(data.getString("distnace")));


                                tvDistnace.setText("Distance: " + String.format("%.0f", Float.parseFloat(data.getString("distnace"))) + " KM");
                                String str = garageDataobj.getString("suburb") + "," + garageDataobj.getString("state") + "," + garageDataobj.getString("postcode");
                                tvLocation.setText(str);
                                tvLocation.setPaintFlags(tvLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

                                //Linkify.addLinks(tvLocation, null, null);

                                lat = garageDataobj.getDouble("lat");
                                lng = garageDataobj.getDouble("lng");
                                subrub = garageDataobj.getString("suburb");
                                state = garageDataobj.getString("state");
                                phone = garageDataobj.getString("mobile");
                                mail = garageDataobj.getString("business_email");

                                if (garageDataobj.getString("verified").equalsIgnoreCase("0")) {
                                    ll_verified.setVisibility(View.VISIBLE);
                                    tvVerified.setText("Not verified");
                                    img_verify_1.setVisibility(View.INVISIBLE);
                                    img_verify_2.setVisibility(View.INVISIBLE);
                                } else {
                                    ll_verified.setVisibility(View.VISIBLE);
                                    img_verify_1.setVisibility(View.VISIBLE);
                                    img_verify_2.setVisibility(View.VISIBLE);
                                }
                                tvCall.setText(garageDataobj.getString("mobile"));
                                tvCall.setPaintFlags(tvLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                tvMsg.setText(garageDataobj.getString("business_email"));
                                tvMsg.setPaintFlags(tvLocation.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                                if (galleryImages.length() > 0) {
                                    llNoCarImgs.setVisibility(View.GONE);
                                    viewpager.setVisibility(View.VISIBLE);
                                    indicator.setVisibility(View.VISIBLE);
                                    for (int j = 0; j < galleryImages.length(); j++) {
                                        JSONObject carImgJsonObj = galleryImages.getJSONObject(j);
                                        CarImageDBO carImageDBO = new CarImageDBO();
                                        String carImg = carImgJsonObj.getString("url");
                                        carImageDBO.setUrl(carImgJsonObj.getString("url"));
                                        carImageArrayList.add(carImageDBO);
                                    }
                                    pagerAdapterImage = new ViewPagerAdapterImage(appContext, carImageArrayList);
                                    viewpager.setAdapter(pagerAdapterImage);
                                    indicator.setViewPager(viewpager);
                                } else {
                                    llNoCarImgs.setVisibility(View.VISIBLE);
                                    viewpager.setVisibility(View.GONE);
                                    indicator.setVisibility(View.GONE);
                                }

                                traddingHoursDBoArrayList = new ArrayList<>();
                                for (int j = 0; j < garageTradding.length(); j++) {
                                    JSONObject jobj = garageTradding.getJSONObject(j);
                                    setTimings(j, jobj.getString("time"), jobj.getString("day"));
                                }
                                servicesDBOs = new ArrayList<>();
                                for (int j = 0; j < garageServices.length(); j++) {
                                    JSONObject jobj = garageServices.getJSONObject(j);
                                    ServicesDBO services = new ServicesDBO();
                                    services.setService(jobj.getString("name"));
                                    servicesDBOs.add(services);
                                }
                                facilitiesDBos = new ArrayList<>();
                                for (int j = 0; j < garageFacility.length(); j++) {
                                    JSONObject jobj = garageFacility.getJSONObject(j);
                                    FacilitiesDBo services = new FacilitiesDBo();
                                    services.setFacilites(jobj.getString("name"));
                                    facilitiesDBos.add(services);
                                }

                                reviewsDBOs = new ArrayList<>();
                                for (int i = 0; i < garageReviews.length(); i++) {
                                    JSONObject jobj = garageReviews.getJSONObject(i);
                                    JSONArray jarry2 = garageReviewsReplies.getJSONArray(i);

                                    ReviewsDBO model = new ReviewsDBO();
                                    //model.setName(jobj.getString("user_name"));
                                    model.setDescription(
                                            jobj.getString("review_text"));
                                    model.setImage(jobj.getString("user_img"));
                                    model.setRating(jobj.getString("rated_me"));
                                    model.setDistnace(distnace);

                                    JSONArray job_info = jobj.getJSONArray("job_info");
                                    JSONArray user_info = jobj.getJSONArray("user_info");

                                    for (int j = 0; j < job_info.length(); j++) {
                                        JSONObject jsonObject = job_info.getJSONObject(j);
                                        model.setJobtitle(jsonObject.getString("job_title"));

                                    }
                                    for (int j = 0; j <user_info.length() ; j++) {
                                        JSONObject jsonObject = user_info.getJSONObject(j);
                                        model.setAddress(jsonObject.getString("suburb") + " "+ jsonObject.getString("state"));
                                        model.setName(jsonObject.getString("fname") + " " + jsonObject.getString("lname"));
                                    }


                                    if (jarry2.length() == 0) {
                                        model.setResponse("");

                                    } else {
                                        JSONObject joj2 = jarry2.getJSONObject(0);
                                        model.setResponse(joj2.getString("review_text"));
                                    }
                                    reviewsDBOs.add(model);
                                }
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    private void setTimings(int position, String data, String day) {
        String temp = data.replace("-", "");
        String array1[] = temp.split(" ");
        AppLog.Log("TAG", "time size : " + array1.length);
        TraddingHoursDBo traddingHoursDBo = new TraddingHoursDBo();
        traddingHoursDBo.setIntime(array1[0]);
        traddingHoursDBo.setIntimeampm(array1[1]);
        traddingHoursDBo.setGetOuttimeampm(array1[4]);
        traddingHoursDBo.setOuttime(array1[3]);
        traddingHoursDBo.setIsselected(true);
        traddingHoursDBo.setTag(day.toUpperCase());
        traddingHoursDBoArrayList.add(traddingHoursDBo);
    }

}
