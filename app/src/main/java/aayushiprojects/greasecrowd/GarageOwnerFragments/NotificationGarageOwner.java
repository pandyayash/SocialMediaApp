package aayushiprojects.greasecrowd.GarageOwnerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.DiscussionActivity;
import aayushiprojects.greasecrowd.activities.JobDetailsActivity;
import aayushiprojects.greasecrowd.activities.ViewNewJobsFeedDetailActivity;
import aayushiprojects.greasecrowd.adapter.AdptNotificationList;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.NotificationList_Dbo;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by technource on 13/9/17.
 */

public class NotificationGarageOwner extends Fragment {

    View v;

    String TAG = "ViewNotificationActivity";
    RecyclerView recyclerNotification;
    ArrayList<NotificationList_Dbo> notificationArrayList;
    AdptNotificationList adptNotificationList;
    int currentPage = 1;
    boolean isLoadMore = false;
    TextView textView;
    SwipeRefreshLayout pull_to_refresh;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_notification, container, false);
        getViews(v);
        ((DashboardScreen)appContext).NoFooter();
        if (Connectivity.isConnected(appContext)) {
            ReadNotification(WebServiceURLs.READ_NOTIFICATION, "", 0);
            getNotificationList();

        } else {
            ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));
        }

        recyclerNotification.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {
                        getNotificationList();
                    } else {
                        ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));
                    }

                }
            }
        });

        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Connectivity.isConnected(appContext)) {
                    notificationArrayList = new ArrayList<>();
                    currentPage = 1;
                    getNotificationList();
                } else {
                    ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));
                }
                pull_to_refresh.setRefreshing(false);
            }
        });


        return v;
    }

    public static NotificationGarageOwner newInstance() {
        NotificationGarageOwner fragment = new NotificationGarageOwner();
        return fragment;
    }

    public void getViews(View view) {
        appContext = getActivity();
        Constants.notify_count = "0";
        notificationArrayList = new ArrayList<>();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        pull_to_refresh = view.findViewById(R.id.pull_to_refresh);
        textView = view.findViewById(R.id.text);
        textView.setText(getString(R.string.no_notifications_found));
        ((DashboardScreen) appContext).cart_badge.setEnabled(false);

    }

    public void getNotificationList() {
        notificationArrayList = new ArrayList<>();
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.LIST_NOTIFICATIONS);
        params.put(Constants.SEARCH_CROWD.PAGE_NUMBER, String.valueOf(currentPage));
        params.put(Constants.SEARCH_CROWD.USER_TYPE, loginDetail_dbo.getUser_Type());

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Notification List --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                final String next = response.getString("pagination_flag");
                                if (!next.equalsIgnoreCase("no")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONArray notiJsonArray = response.optJSONArray("notifications");

                                if (notiJsonArray != null && notiJsonArray.length() > 0) {
                                    for (int i = 0; i < notiJsonArray.length(); i++) {
                                        JSONObject jsonObject = notiJsonArray.getJSONObject(i);
                                        NotificationList_Dbo notificationListDbo = new NotificationList_Dbo();
                                        notificationListDbo.setId(jsonObject.optString("id"));
                                        notificationListDbo.setUser_id(jsonObject.optString("user_id"));
                                        notificationListDbo.setGarage_id(jsonObject.optString("garage_id"));
                                        notificationListDbo.setJob_id(jsonObject.optString("job_id"));
                                        notificationListDbo.setNotify_to(jsonObject.optString("notify_to"));
                                        notificationListDbo.setType(jsonObject.optString("type"));
                                        notificationListDbo.setIs_read(jsonObject.optString("is_read"));
                                        notificationListDbo.setDatetime(jsonObject.optString("datetime"));
                                        notificationListDbo.setData(jsonObject.optString("data"));
                                        JSONArray jobDetailsArray = jsonObject.optJSONArray("job_details");
                                        JSONArray garageDetailsArray = jsonObject.optJSONArray("garage_details");
                                        if (jobDetailsArray != null && jobDetailsArray.length() > 0) {
                                            for (int j = 0; j < jobDetailsArray.length(); j++) {

                                                JSONObject object = jobDetailsArray.getJSONObject(j);
                                                notificationListDbo.setJob_title(object.optString("job_title"));
                                                notificationListDbo.setJu_id(object.optString("ju_id"));
                                                notificationListDbo.setCjob_id(object.optString("cjob_id"));
                                                notificationListDbo.setCatname(object.optString("catname"));
                                                notificationListDbo.setCat_id(object.optString("category_id"));
                                                notificationListDbo.setStatus(object.optString("job_status"));
                                                notificationListDbo.setFname(object.optString("fname"));
                                                notificationListDbo.setLname(object.optString("lname"));


                                            }
                                        }
                                        if (garageDetailsArray != null && garageDetailsArray.length() > 0) {
                                            for (int k = 0; k < garageDetailsArray.length(); k++) {
                                                JSONObject garajJsonObj = garageDetailsArray.getJSONObject(k);
                                                notificationListDbo.setBusiness_name(garajJsonObj.optString("business_name"));
                                                notificationListDbo.setBusiness_type(garajJsonObj.optString("business_type"));
                                                notificationListDbo.setLogo_image(garajJsonObj.optString("logo_image"));
                                            }
                                        }

                                        notificationArrayList.add(notificationListDbo);
                                    }
                                }
                                setData();

                            } else {
                                ((DashboardScreen) appContext).showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }

    public void setData() {

        if (notificationArrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            recyclerNotification.setVisibility(View.VISIBLE);
            adptNotificationList = new AdptNotificationList(appContext, notificationArrayList, new OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    if (view == view.findViewById(R.id.linear)) {
                       /* if (!notificationArrayList.get(position).getIs_read().equalsIgnoreCase("Y")) {
                            //    ReadNotification(WebServiceURLs.READ_NOTIFICATION, notificationArrayList.get(position).getId(), position);
                        } else */
                        if (notificationArrayList.get(position).getType().equalsIgnoreCase("PM")) {
                            Intent intent = new Intent(appContext, DiscussionActivity.class);
                            intent.putExtra("flag", "1");
                            intent.putExtra("job_id", notificationArrayList.get(position).getCjob_id());
                            startActivity(intent);
                            ((DashboardScreen) appContext).activityTransition();
                        } else if (notificationArrayList.get(position).getType().equalsIgnoreCase("DM")) {
                            Intent intent = new Intent(appContext, DiscussionActivity.class);
                            intent.putExtra("flag", "0");
                            intent.putExtra("job_id", notificationArrayList.get(position).getCjob_id());
                            startActivity(intent);
                            ((DashboardScreen) appContext).activityTransition();
                        } else {

                            if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                                Intent intent = new Intent(appContext, JobDetailsActivity.class);
                                intent.putExtra("job_id", notificationArrayList.get(position).getJu_id());
                                intent.putExtra("cat_id", notificationArrayList.get(position).getCat_id());
                                intent.putExtra("status", notificationArrayList.get(position).getStatus());
                                startActivity(intent);
                                ((DashboardScreen) appContext).activityTransition();
                            } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                                Intent intent = new Intent(appContext, ViewNewJobsFeedDetailActivity.class);
                                intent.putExtra("job_id", notificationArrayList.get(position).getJu_id());
                                intent.putExtra("cat_id", notificationArrayList.get(position).getCat_id());
                                intent.putExtra("status", notificationArrayList.get(position).getStatus());
                                startActivity(intent);
                                ((DashboardScreen) appContext).activityTransition();
                            }
                        }
                    }
                    if (view == view.findViewById(R.id.iv_close)) {
                        ReadNotification(WebServiceURLs.DELETE_NOTIFICATION, notificationArrayList.get(position).getId(), position);
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            recyclerNotification.setLayoutManager(mLayoutManager);
            recyclerNotification.setItemAnimator(new DefaultItemAnimator());
            recyclerNotification.setAdapter(adptNotificationList);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            recyclerNotification.addItemDecoration(bottomOffsetDecoration);
        } else {
            textView.setVisibility(View.VISIBLE);
            recyclerNotification.setVisibility(View.GONE);
        }

    }


    public void ReadNotification(final String serviceName, String noti_id, final int pos) {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, serviceName);
        params.put(Constants.NOTIFICATIONS.NOTIFICATION_ID, noti_id);
        params.put(Constants.SEARCH_CROWD.USER_TYPE, loginDetail_dbo.getUser_Type());

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Read Notification--> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();

                                if (serviceName.equalsIgnoreCase(WebServiceURLs.DELETE_NOTIFICATION)) {
                                    ((DashboardScreen) appContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            notificationArrayList.remove(pos);
                                            adptNotificationList.notifyDataSetChanged();
                                            if (notificationArrayList.size() != 0) {
                                                recyclerNotification.setVisibility(View.VISIBLE);
                                                textView.setVisibility(View.GONE);
                                            } else {
                                                recyclerNotification.setVisibility(View.GONE);
                                                textView.setVisibility(View.VISIBLE);
                                            }
                                        }
                                    });
                                } else {
                                    ((DashboardScreen) appContext).setBadgeCount();
                                }


                            } else {
                                ((DashboardScreen) appContext).showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }
}
