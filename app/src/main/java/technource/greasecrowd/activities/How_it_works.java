package technource.greasecrowd.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.CustomViews.LoadingDialog.CustomDialog;
import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.SplashPojo;

public class How_it_works extends BaseActivity {

    public static final String TAG = "How_it_works";
    ArrayList<SplashPojo> splashPojoArrayList;
    Context context;
    Intent intent;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private TextView title, description;
    private int[] layouts;
    MyPreference myPreference;

    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            if (position == layouts.length - 1) {
                btnNext.setText(getString(R.string.got_it));

            } else {
                btnNext.setText(getString(R.string.next));
            }
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };
    private TextView btnSkip, btnNext;
    private ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_how_it_works);
        context = How_it_works.this;

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            if (Connectivity.isConnected(context)) {
                getSplashSteeps();
            } else {
                splashPojoArrayList = HelperMethods.getUserSplashSharedPreferences(context);
                if (splashPojoArrayList == null) {
                    showAlertOkDialog(getResources().getString(R.string.no_internet));
                }
            }
        }


        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnSkip = (TextView) findViewById(R.id.btn_skip);
        btnNext = (TextView) findViewById(R.id.btn_next);

        myPreference = new MyPreference(this);

        layouts = new int[]{R.layout.splash_screen_1, R.layout.splash_screen_2,
                R.layout.splash_screen_3, R.layout.splash_screen_4, R.layout.splash_screen_5};
        addBottomDots(0);
        changeStatusBarColor();

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //launchHomeScreen();
            }
        });
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (btnNext.getText().toString().equalsIgnoreCase("GOT IT")) {

                    if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                        intent = new Intent(How_it_works.this, DashboardScreen.class);
                        startActivity(intent);
                        finish();

                    } else {
                        intent = new Intent(How_it_works.this, StaticScreen.class);
                        startActivity(intent);
                        finish();
                    }
                } else if (current < layouts.length) {
                    viewPager.setCurrentItem(current);
                } else {
                }
            }
        });
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                    intent = new Intent(How_it_works.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();

                } else {
                    intent = new Intent(How_it_works.this, StaticScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();
                }
            }
        });

    }

    public void showAlertOkDialog(String message) {

        dialogC = new CustomDialog(this, "ALERT", message);
        dialogC.setCanceledOnTouchOutside(false);
        if (dialogC != null && dialogC.isShowing()) {
            dialogC.dismiss();
            dialogC.show();
        } else {
            dialogC.show();
        }
        dialogC.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                    intent = new Intent(How_it_works.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();

                } else {
                    intent = new Intent(How_it_works.this, StaticScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();
                }
            }
        });
        dialogC.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                if (myPreference.getBooleanReponse(Constants.IS_LOGGEDIN)) {
                    intent = new Intent(How_it_works.this, DashboardScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();

                } else {
                    intent = new Intent(How_it_works.this, StaticScreen.class);
                    startActivity(intent);
                    finish();
                    activityTransition();
                }
            }
        });
    }

    private void getSplashSteeps() {
        if (splashPojoArrayList != null) {
            splashPojoArrayList.clear();
            splashPojoArrayList = new ArrayList<>();
        } else {
            splashPojoArrayList = new ArrayList<>();
        }

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = WebServiceURLs.BASE_URL;

        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put(Constants.Splashscreen.SERVICE_NAME, Constants.Splashscreen.SPLASH);

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
                            if (str_status != null) {
                                if (str_status.equals(Constants.SUCCESS)) {
                                    JSONArray json = response.getJSONArray(Constants.Splashscreen.SERVICES);

                                    for (int i = 0; i < json.length(); i++) {
                                        SplashPojo model = new SplashPojo();
                                        JSONObject obj = json.getJSONObject(i);
                                        model.setId(obj.getInt(Constants.Splashscreen.ID));
                                        model.setTitle(obj.getString(Constants.Splashscreen.TITLE));

                                        model
                                                .setDescription(obj.getString(Constants.Splashscreen.DESCRIPTION));
                                        model.setImage_path(obj.getString(Constants.Splashscreen.IMAGE));
                                        AppLog.Log(TAG, "Image: " + model.getImage_path());
                                        splashPojoArrayList.add(model);

                                    }
                                    HelperMethods.storeUserSplashSharedPreferences(context, splashPojoArrayList);
                                    splashPojoArrayList = HelperMethods.getUserSplashSharedPreferences(context);

                                    myViewPagerAdapter = new MyViewPagerAdapter();
                                    viewPager.setAdapter(myViewPagerAdapter);
                                    viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

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

        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
//                    String auth = "Bearer " + loginDetail_dbo.getLogin_token();
                HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", auth);
                return headers;
            }

        };
        jsonObjReq.setRetryPolicy(
                new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        jsonObjReq.setShouldCache(false);
        //  AppController.getInstance().addToRequestQueue(jsonObjReq);
        queue.add(jsonObjReq);


    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("•"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.pager_dot_inactive));
            dotsLayout.addView(dots[i]);
        }
        if (dots.length > 0) {
            dots[currentPage].setTextColor(getResources().getColor(R.color.pager_dot_active));
        }
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;
    }

    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            title = (TextView) view.findViewById(R.id.title);
            description = (TextView) view.findViewById(R.id.description);
            title.setText(splashPojoArrayList.get(position).getTitle());
            description.setText(splashPojoArrayList.get(position).getDescription());
            logo = (ImageView) view.findViewById(R.id.image);

            Glide.with(context).load(splashPojoArrayList.get(position).getImage_path())
                    .dontAnimate()
                    .into(logo);

            return view;
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }
}

