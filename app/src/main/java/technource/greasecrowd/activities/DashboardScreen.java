package technource.greasecrowd.activities;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlideGravity;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.CarOwnerFragments.ContactUsCarOwner;
import technource.greasecrowd.CarOwnerFragments.HomeCarOwner;
import technource.greasecrowd.CarOwnerFragments.HowItWorksCarOwner;
import technource.greasecrowd.CarOwnerFragments.MyGaragesCarOwner;
import technource.greasecrowd.CarOwnerFragments.ProfileCarOwner;
import technource.greasecrowd.CarOwnerFragments.TransactionsCarOwner;
import technource.greasecrowd.GarageOwnerFragments.ContactUsGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.HomeGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.HowitWorksGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.MailBoxGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.MySettingsGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.NotificationGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.ProfileGarageOwner;
import technource.greasecrowd.GarageOwnerFragments.TransactionGarageOwner;
import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.menu.DrawerAdapter;
import technource.greasecrowd.menu.DrawerItem;
import technource.greasecrowd.menu.SimpleItem;
import technource.greasecrowd.model.CatogoriesDBO;
import technource.greasecrowd.model.LoginDetail_DBO;

public class DashboardScreen extends BaseActivity implements
        DrawerAdapter.OnItemSelectedListener, OnClickListener {

    public static final String TAG = "DASHBOARDSCREEN";
    public static boolean first = false;
    public static int state = 0;
    public boolean PostFlag = false;
    public boolean selected = false;
    public ArrayList<CatogoriesDBO> catogoriesDBOsaArrayList;
    public Context appContext;
    DrawerAdapter adapter;
    int mSelected = 0, currentFragmentPosition;
    LoginDetail_DBO loginDetail_dbo;
    int PERMISSION_CODE = 2;
    // Header Configurations ....
    LinearLayout ll_jobs, ll_crowd, ll_tab, ll_jobs_crowd;
    TextView tv_jobs, tv_crowd, tv_title;
    ImageView img_job, ImgCrowd, imgNotification;
    // Footer Configurations ....
    android.support.v4.app.Fragment fragment = null;
    Bitmap bitmap;
    ImageView iv_image;
    TextView textiew,balance;
    private String[] screenTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        first = false;
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        getViews();
        setOnClicklistener();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AppLog.Log("FCM-->", "device token-->" + HelperMethods.getDeviceTokenFCM());

        new SlidingRootNavBuilder(this)
                .withMenuOpened(false)
                .withGravity(SlideGravity.RIGHT)
                .withToolbarMenuToggle(toolbar)
                .withSavedState(savedInstanceState)
                .withMenuLayout(R.layout.menu_left_drawer)
                .inject();

        setSliderdata();
        displayView(0);

        if (Connectivity.isConnected(appContext)) {
            callForCatogories();
        } else {
            AppLog.Log("This part", "No Connectivity");
            showAlertDialog(getResources().getString(R.string.no_internet));
        }
    }

    private void callForCatogories() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.categories.SERVICE_NAME, Constants.categories.CATEGORIES);

        AppLog.Log(TAG, "parameters : " + new JSONObject(postParam).toString());
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(postParam),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log(TAG, "RESPONSE : " + response);
                        String str_status;
                        try {
                            str_status = response.getString(Constants.STATUS);
                            AppLog.Log(TAG, "Status in Login" + str_status);
                            AppLog.Log(TAG, "Status in Login" + response);
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    JSONArray json = response.getJSONArray(Constants.categories.SERVICES);

                                    for (int i = 0; i < json.length(); i++) {
                                        CatogoriesDBO model = new CatogoriesDBO();
                                        JSONObject obj = json.getJSONObject(i);
                                        model.setId(obj.getString(Constants.categories.ID));
                                        model.setName((obj.getString(Constants.categories.NAME)));
                                        model.setImage(obj.getString(Constants.categories.IMAGE));
                                        model.setPlaceholder(obj.getString(Constants.categories.PLACEHOLDER));
                                        model.setItext(obj.getString(Constants.categories.ITEXT));
                                        catogoriesDBOsaArrayList.add(model);
                                    }
                                    ((HomeCarOwner) fragment).setUpVIewPager();

                                    AppLog.Log(TAG, "Image: " + catogoriesDBOsaArrayList);
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
        });
        queue.add(jsonObjReq);
    }

    public void getViews() {
        iv_image = (ImageView) findViewById(R.id.iv_image);
        ll_jobs = (LinearLayout) findViewById(R.id.ll_jobs);
        ll_crowd = (LinearLayout) findViewById(R.id.ll_crowd);
        ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
        ll_jobs_crowd = (LinearLayout) findViewById(R.id.ll_jobs_crowd);


        tv_jobs = (TextView) findViewById(R.id.tv_jobs);
        tv_crowd = (TextView) findViewById(R.id.tv_crowd);
        tv_title = (TextView) findViewById(R.id.tv_title_header);
        img_job = (ImageView) findViewById(R.id.imgJob);
        ImgCrowd = (ImageView) findViewById(R.id.imgCrowd);
        imgNotification = (ImageView) findViewById(R.id.imgNotification);
        catogoriesDBOsaArrayList = new ArrayList<>();
    }

    public void setCrowds() {
        selected = true;
        ll_tab.getBackground().setLevel(7);
        ll_crowd.getBackground().setLevel(6);
        ll_jobs.getBackground().setLevel(8);
        ImgCrowd.setVisibility(View.VISIBLE);
        img_job.setVisibility(View.GONE);
        ((HomeCarOwner) fragment).notifypageadapter();
        setfooter("crowd");
        setHomeFooterCrowd(appContext);
    }

    public void setJobs() {
        selected = false;
        ll_tab.getBackground().setLevel(7);
        ll_jobs.getBackground().setLevel(6);
        ll_crowd.getBackground().setLevel(8);
        ImgCrowd.setVisibility(View.GONE);
        img_job.setVisibility(View.VISIBLE);
        setfooter("jobs");
        setlistenrforfooter();
        if (first) {
            ((HomeCarOwner) fragment).notifypageadapter();
        }
    }

    public void setFooterGarage() {
        setfooter("garageowner");
        setHomeFooterGarage(appContext);
        setlistenrforfooter();
    }

    @Override
    public void onItemSelected(int positions) {
        displayView(positions);
    }


    private void setSliderdata() {
        screenTitles = loadScreenTitles();

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.CAR_OWNER)) {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(0).setChecked(true),
                    createItemFor(1),
                    createItemFor(2),
                    createItemFor(3),
                    createItemFor(4),
                    createItemFor(5),
                    createItemFor(6),
                    createItemFor(7)));
        } else {
            adapter = new DrawerAdapter(Arrays.asList(
                    createItemFor(0).setChecked(true),
                    createItemFor(1),
                    createItemFor(2),
                    createItemFor(3),
                    createItemFor(4),
                    createItemFor(5),
                    createItemFor(6),
                    createItemFor(7)));
        }

        adapter.setListener(this);

        RecyclerView list = (RecyclerView) findViewById(R.id.list);
        iv_image = (ImageView) findViewById(R.id.iv_image);
        textiew = (TextView) findViewById(R.id.username);
        balance = (TextView) findViewById(R.id.balance);
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")){
            balance.setVisibility(View.VISIBLE);
            balance.setText("Balance: $"+loginDetail_dbo.getGarage_balance());
        }else {
            balance.setVisibility(View.GONE);
        }
        LinearLayout ll_top = (LinearLayout) findViewById(R.id.ll_top);
        LinearLayout ll_logout = (LinearLayout) findViewById(R.id.ll_logout);


        AppLog.Log("image path", " " + loginDetail_dbo.getImage());
        textiew.setText(loginDetail_dbo.getFirst_name() + " " + loginDetail_dbo.getLast_name());

        Glide.with(appContext)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + loginDetail_dbo.getImage())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .skipMemoryCache(true)
                .into(iv_image);

        list.setNestedScrollingEnabled(false);
        list.setLayoutManager(new LinearLayoutManager(this));
        list.setAdapter(adapter);

        adapter.setSelected(mSelected);

        ll_logout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Connectivity.isConnected(appContext)) {
                    LogoutFromApp();

                } else {
                    AppLog.Log("This part", "No Connectivity");
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }

            }
        });
    }

    public void updateProfile(String fname, String lname, String image) {

        textiew.setText(fname + " " + lname);

        Picasso.with(appContext)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + image)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(iv_image);
    }

    public void LogoutFromApp() {

        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "logout");
        params.put(Constants.Forgotpassword.USER_TYPE, loginDetail_dbo.getUser_Type());

        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String push, email, text;
                        AppLog.Log("TAG", "Response : " + response);
                        try {

                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                LogOut();
                            } else {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE),
                                        Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error

                    }
                }
        );

        queue.add(jsonObjReq);
    }

    private void LogOut() {
        MyPreference myPreference = new MyPreference(appContext);

        myPreference.removeBooleanReponse();
        myPreference.removeIntegerReponse();
        myPreference.removeStringReponse();

        HelperMethods.deleteUserDetailsSharedPreferences(appContext);

        Toast.makeText(appContext, "LOGOUT SUCCESSFULL", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(appContext, StaticScreen.class);
        intent.addFlags(Constants.INTENT_FLAGS);
        startActivity(intent);
        activityTransition();
        finish();
    }

    private String[] loadScreenTitles() {
        String[] array = new String[0];

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.CAR_OWNER)) {
            array = getResources().getStringArray(R.array.navigation_drawer_items);
        } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.GERAGE_OWNER)) {
            array = getResources().getStringArray(R.array.navigation_drawer_items_garage_owner);
        }

        return array;
    }

    @Override
    public void onBackPressed() {
        AppLog.Log(TAG, "on BackPressed");
        if (currentFragmentPosition != 0) {
            mSelected = 0;
            first = false;
            adapter.setSelected(mSelected);
            displayView(0);
        } else {
            new Builder(appContext)
                    .setMessage(R.string.are_you_srure_to_exit)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            finishAffinity();
                            activityTransition();
                        }
                    })
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void displayView(int position) {
        currentFragmentPosition = position;

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.CAR_OWNER)) {

            if (position != 0) {
                ll_jobs_crowd.setVisibility(View.GONE);
                tv_title.setVisibility(View.VISIBLE);
            } else {
                tv_title.setVisibility(View.GONE);
                ll_jobs_crowd.setVisibility(View.VISIBLE);
            }
            switch (position) {
                case 0:
                    fragment = new HomeCarOwner().newInstance();
                    break;
                case 1:
                    tv_title.setText("My Profile");
                    fragment = new ProfileCarOwner().newInstance();
                    break;
                case 2:
                    tv_title.setText("New Jobs Feed");
                    fragment = new HomeGarageOwner().newInstance();
                    break;
                case 3:
                    tv_title.setText("My Garages");
                    fragment = new MyGaragesCarOwner().newInstance();
                    break;
                case 4:
                    tv_title.setText("Transaction History");
                    fragment = new TransactionsCarOwner().newInstance();
                    break;
                case 5:
                    fragment = new HowItWorksCarOwner().newInstance();
                    break;
                case 6:
                    tv_title.setText("My Settings");
                    fragment = new MySettingsGarageOwner().newInstance();
                    break;
                case 7:
                    tv_title.setText("CONTACT US");
                    fragment = new ContactUsCarOwner().newInstance();
                    break;

                default:
                    break;
            }
        } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.GERAGE_OWNER)) {
            ll_jobs_crowd.setVisibility(View.GONE);
            tv_title.setVisibility(View.VISIBLE);
            switch (position) {
                case 0:
                    tv_title.setText("New Jobs Feed");
                    fragment = new HomeGarageOwner().newInstance();
                    break;
                case 1:
                    tv_title.setText("My Profile");
                    fragment = new ProfileGarageOwner().newInstance();
                    break;
                case 2:
                    tv_title.setText("Mail Box");
                    fragment = new MailBoxGarageOwner().newInstance();
                    break;
                case 3:
                    fragment = new NotificationGarageOwner().newInstance();
                    break;
                case 4:
                    tv_title.setText("Transaction History");
                    fragment = new TransactionsCarOwner().newInstance();
                    break;
                case 5:
                    fragment = new HowitWorksGarageOwner().newInstance();
                    break;
                case 6:
                    tv_title.setText("My Settings");
                    fragment = new MySettingsGarageOwner().newInstance();
                    break;
                case 7:
                    tv_title.setText("CONTACT US");
                    fragment = new ContactUsGarageOwner().newInstance();
                    break;
                default:
                    break;
            }

        }

        if (fragment != null) {
            mSelected = position;
            AppLog.Log(TAG, "selected Position " + position);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment).addToBackStack(null)
                    .commit();
        } else {
            AppLog.Log(TAG, "Error in creating fragment");
        }
    }


    private DrawerItem createItemFor(int position) {
        return new SimpleItem(screenTitles[position])
                .withTextTint(color(R.color.black))
                .withSelectedTextTint(color(R.color.colorPrimary));
    }


    public void setOnClicklistener() {
        ll_jobs.setOnClickListener(this);
        ll_crowd.setOnClickListener(this);
        imgNotification.setOnClickListener(this);

    }

    @ColorInt
    private int color(@ColorRes int res) {
        return ContextCompat.getColor(this, res);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        int id = view.getId();
        switch (id) {
            case R.id.ll_jobs:
                state = 0;
                setJobs();
                break;
            case R.id.ll_crowd:
                state = 1;
                setCrowds();
                break;
            case R.id.imgNotification:
                Intent intent = new Intent(this, ViewNotificationActivity.class);
                startActivity(intent);
                activityTransition();
                break;

        }
    }


}
