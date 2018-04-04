package technource.autoreum.CarOwnerFragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.activities.AskTheCrowd.AskTheCrowdStepOne;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.activities.PostJob.PostNewJobStepOne;
import technource.autoreum.activities.RegisterNewCarActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;

/**
 * Created by technource on 13/9/17.
 */

public class HomeCarOwner extends Fragment {

    public TextView tv_postjob;
    public MainPagerAdapter pagerAdapter = null;
    public String str_device;
    View v;
    ArrayList<String> arrayList = new ArrayList<>();
    Context apContext;
    private ViewPager pager = null;
    LoginDetail_DBO loginDetail_dbo;
    String jwt;
    boolean flag;

    public static HomeCarOwner newInstance() {
        HomeCarOwner fragment = new HomeCarOwner();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (DashboardScreen.state==0){
            ((DashboardScreen) apContext).setJobs();
        }else {
            ((DashboardScreen) apContext).setCrowds();
        }

        ((DashboardScreen) apContext).first = true;
        ((DashboardScreen) apContext).setHomeFooter(apContext);
        CheckForRegisteredCar();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_carowner_home, container, false);
        apContext = getActivity();
        differentDensityAndScreenSize(apContext);
        pagerAdapter = new MainPagerAdapter();
        pager = (ViewPager) v.findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(apContext);
        jwt = loginDetail_dbo.getJWTToken();

        return v;
    }

    public void setUpVIewPager() {
        //Create an initial view to display; must be a subclass of FrameLayout.
        int count_1 = 0;
        int count;

        if (((DashboardScreen) apContext).catogoriesDBOsaArrayList.size() % 2 == 0) {
            count = ((DashboardScreen) apContext).catogoriesDBOsaArrayList.size() / 2;
        } else {
            count = (((DashboardScreen) apContext).catogoriesDBOsaArrayList.size() / 2 + 1);
        }
        int totalsize = count - 1;
        AppLog.Log("Size of  array", "" + count);
        AppLog.Log("Size of  arraylist",
                "" + ((DashboardScreen) apContext).catogoriesDBOsaArrayList.size());
        LayoutInflater inflater2 = getActivity().getLayoutInflater();
        for (int i = 0; i < count; i++) {
            LinearLayout v0 = (LinearLayout) inflater2.inflate(R.layout.frames_categories, null);
            LinearLayout ll_cat1 = (LinearLayout) v0.findViewById(R.id.ll_cat1);
            LinearLayout ll_cat2 = (LinearLayout) v0.findViewById(R.id.ll_cat2);
            TextView cat1 = (TextView) v0.findViewById(R.id.cat_1);
            TextView cat2 = (TextView) v0.findViewById(R.id.cat_2);
            ImageView img1 = (ImageView) v0.findViewById(R.id.img1);
            ImageView img2 = (ImageView) v0.findViewById(R.id.img2);
            tv_postjob = (TextView) v0.findViewById(R.id.tv_new_postjob);
            cat1.setText(((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getName());
            Glide.with(apContext)
                    .load(((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getImage())
                    .dontAnimate()
                    .into(img1);
            if (((DashboardScreen) apContext).catogoriesDBOsaArrayList.size() % 2 != 0) {
                if (i == totalsize) {
                    ll_cat2.setVisibility(View.INVISIBLE);
                    cat2.setText("");
                } else {
                    count_1 = count_1 + 1;
                    cat2.setText(
                            ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getName());
                    Glide.with(apContext)
                            .load(((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getImage())
                            .dontAnimate()
                            .into(img2);
                }
            } else {
                count_1 = count_1 + 1;
                cat2.setText(((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getName());
                Glide.with(apContext)
                        .load(((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(count_1).getImage())
                        .dontAnimate()
                        .into(img2);
            }

            pagerAdapter.addView(v0, i, cat1, cat2, ll_cat1, ll_cat2, count_1);
            count_1++;
        }

        pagerAdapter.notifyDataSetChanged();

        pager.setClipToPadding(false);

        if (str_device.equalsIgnoreCase("small-ldpi")) {
            pager.setPadding(20, 0, 20, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "small");
        } else if (str_device.equalsIgnoreCase("normal-ldpi")) {
            pager.setPadding(30, 0, 30, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "ldpi");
        } else if (str_device.equalsIgnoreCase("normal-xhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "xhdpi");
        } else if (str_device.equalsIgnoreCase("normal-xxhdpi")) {
            pager.setPadding(100, 0, 100, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "xxhdpi");
        } else if (str_device.equals("normal-hdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-hdpi");
        } else if (str_device.equals("normal-mdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-mdpi");
        } else if (str_device.equals("normal-xhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-xhdpi");
        } else if (str_device.equals("normal-xxhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "normal-xxhdpi");
        } else if (str_device.equals("normal-xxxhdpi")) {
            pager.setPadding(100, 0, 100, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "normal-xxxhdpi");
        } else if (str_device.equals("normal-unknown")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-unknown");
        } else {
            pager.setPadding(20, 0, 20, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "here");

        }

    }

    public void notifypageadapter() {
        ((DashboardScreen) apContext).first = true;
        if (pager != null) {
            pager.setAdapter(null);
        }
        pagerAdapter = new MainPagerAdapter();
        pager.setAdapter(pagerAdapter);
        setUpVIewPager();
        AppLog.Log("here", "you are in notify" + ((DashboardScreen) apContext).first);
        AppLog.Log("here", "" + ((DashboardScreen) apContext).selected);

    }

    public int differentDensityAndScreenSize(Context context) {
        int value = 20;
        String str = "";
        if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "small-ldpi";
                    str_device = "small-ldpi";
                    value = 20;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    str_device = "small-ldpi";
                    value = 20;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    str_device = "small-ldpi";
                    break;
                default:
                    str = "small-unknown";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    str_device = "normal-ldpi";
                    value = 82;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    value = 82;
                    str_device = "normal-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    str_device = "normal-ldpi";
                    value = 82;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    str_device = "normal-xhdpi";
                    value = 90;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "normal-xxhdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                default:
                    str = "normal-unknown";
                    str_device = "normal-unknown";
                    value = 82;
                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    str_device = "large-xhdpi";
                    value = 125;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                default:
                    str = "large-unknown";
                    value = 78;

                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                default:
                    str = "xlarge-unknown";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
            }
        }

        return value;
    }


    public void CheckForRegisteredCar() {
        ((DashboardScreen) apContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "has_cars");
        params.put(Constants.PostNewJob.JWT, jwt);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DashboardScreen) apContext).showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                flag = true;
                            } else {
                                flag = false;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                flag = false;
                ((DashboardScreen) apContext).showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);

    }

    public class MainPagerAdapter extends PagerAdapter {

        TextView cat1, cat2;
        LinearLayout ll_cat1, ll_cat2;
        int arraylistpos;
        private ArrayList<View> views = new ArrayList<View>();

        @Override
        public int getItemPosition(Object object) {
            int index = views.indexOf(object);
            if (index == -1) {
                return POSITION_NONE;
            } else {
                return index;
            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View v = views.get(position);
            container.addView(v);
            return v;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(views.get(position));
        }


        @Override
        public int getCount() {
            return views.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public int addView(View v) {
            return addView(v, views.size(), cat1, cat2, ll_cat1, ll_cat2, arraylistpos);
        }

        public int addView(View v, int position, final TextView cat1, final TextView cat2,
                           final LinearLayout ll_cat1, final LinearLayout ll_cat2, final int arraylistpos) {

            final TextView title = (TextView) v.findViewById(R.id.tv_new_postjob);
            if (!(((DashboardScreen) apContext).selected)) {
                title.setText("POST A NEW JOB");
            } else {
                title.setText("ASK THE CROWD");
            }
            views.add(position, v);

            title.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (title.getText().toString().equalsIgnoreCase("POST A NEW JOB")) {
                        /*Intent intent;
                        if (flag) {
                            intent = new Intent(getActivity(), PostNewJobStepOne.class);
                            intent.putExtra("Dashboard", "yes");
                        } else {
                            intent = new Intent(getActivity(), RegisterNewCarActivity.class);
                        }

                        startActivity(intent);
                        ((DashboardScreen) apContext).activityTransition();*/
                        if (Connectivity.isConnected(apContext)) {
                            ((DashboardScreen) apContext).validateJobSteps();
                        } else {
                            ((DashboardScreen) apContext).showAlertDialog(getResources().getString(R.string.no_internet));
                        }

                    } else {
                        Intent intent;
                        if (flag) {
                            intent = new Intent(getActivity(), AskTheCrowdStepOne.class);
                            intent.putExtra("Dashboard", "yes");
                        } else {
                            intent = new Intent(getActivity(), RegisterNewCarActivity.class);
                        }

                        startActivity(intent);
                        ((DashboardScreen) apContext).activityTransition();
                    }

                }
            });
            ll_cat1.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        if (title.getText().toString().equalsIgnoreCase("POST A NEW JOB")) {
                            Intent intent = new Intent(getActivity(), PostNewJobStepOne.class);
                            if (!cat2.getText().toString().equalsIgnoreCase("")) {

                                intent.putExtra("categories",
                                        ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(arraylistpos - 1));
                            } else {
                                intent.putExtra("categories",
                                        ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(arraylistpos));
                            }
                            startActivity(intent);
                            ((DashboardScreen) apContext).activityTransition();
                        } else {

                            Intent intent = new Intent(getActivity(), AskTheCrowdStepOne.class);
                            intent.putExtra("categories",
                                    ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(arraylistpos - 1));
                            startActivity(intent);
                            ((DashboardScreen) apContext).activityTransition();
                        }
                    } else {
                        Intent intent = new Intent(apContext, RegisterNewCarActivity.class);
                        startActivity(intent);
                        ((DashboardScreen) apContext).activityTransition();

                    }
                }
            });

            ll_cat2.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (flag) {
                        if (title.getText().toString().equalsIgnoreCase("POST A NEW JOB")) {


                            Intent intent = new Intent(getActivity(), PostNewJobStepOne.class);
                            intent.putExtra("categories",
                                    ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(arraylistpos));
                            startActivity(intent);
                            ((DashboardScreen) apContext).activityTransition();
                        } else {

                            Intent intent = new Intent(getActivity(), AskTheCrowdStepOne.class);
                            intent.putExtra("categories",
                                    ((DashboardScreen) apContext).catogoriesDBOsaArrayList.get(arraylistpos));
                            startActivity(intent);
                            ((DashboardScreen) apContext).activityTransition();
                        }
                    } else {
                        Intent intent = new Intent(apContext, RegisterNewCarActivity.class);
                        startActivity(intent);
                        ((DashboardScreen) apContext).activityTransition();
                    }
                }
            });

            return position;
        }

        public int removeView(ViewPager pager, View v) {
            return removeView(pager, views.indexOf(v));
        }

        public int removeView(ViewPager pager, int position) {
            pager.setAdapter(null);
            views.remove(position);
            pager.setAdapter(this);

            return position;
        }

        public View getView(int position) {
            return views.get(position);
        }
    }

}
