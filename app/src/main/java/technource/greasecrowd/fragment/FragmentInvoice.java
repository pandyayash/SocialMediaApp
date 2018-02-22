package technource.greasecrowd.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

import technource.greasecrowd.CustomViews.BottomOffsetDecoration;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.adapter.AdptSettingInvoiceWeekly;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.interfaces.OndeleteNotification;
import technource.greasecrowd.model.MailBoxDBO;

/**
 * Created by technource on 23/1/18.
 */

public class FragmentInvoice extends Fragment {

    public String TAG = "FragmentMyJobsNewPosted";
    Activity appContext;
    RecyclerView jobsRecyclerview;
    TextView textView;
    AdptSettingInvoiceWeekly adptSettingInvoiceWeekly;
    ArrayList<MailBoxDBO> invoiceArrayList;

    View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_invoice, null, false);
        getViews(view);
        if (Connectivity.isConnected(appContext)) {
            getMailBoxData();
        } else {
            ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));

        }
        return view;
    }

    public void getViews(View rootView) {
        appContext = getActivity();
        jobsRecyclerview = (RecyclerView) rootView.findViewById(R.id.jobs_recyclerview);
        textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText("No Invoice Found");
        textView.setVisibility(View.GONE);
    }

    public void setData(ArrayList<MailBoxDBO> mailBoxDBOArrayList) {

        if (mailBoxDBOArrayList.size() > 0) {
            textView.setVisibility(View.GONE);
            jobsRecyclerview.setVisibility(View.VISIBLE);
            adptSettingInvoiceWeekly = new AdptSettingInvoiceWeekly(appContext, mailBoxDBOArrayList, new OndeleteNotification() {
                @Override
                public void onClick(View view, int position, String type) {
                    if (type.equalsIgnoreCase("invoice")) {
                        deleteNotification(invoiceArrayList.get(position).getId(), position);
                    }
                }
            });
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            jobsRecyclerview.setLayoutManager(mLayoutManager);
            jobsRecyclerview.setItemAnimator(new DefaultItemAnimator());
            jobsRecyclerview.setAdapter(adptSettingInvoiceWeekly);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            jobsRecyclerview.addItemDecoration(bottomOffsetDecoration);
        } else {
            textView.setVisibility(View.VISIBLE);
            jobsRecyclerview.setVisibility(View.GONE);
        }

    }

    public void getMailBoxData() {
        invoiceArrayList = new ArrayList<>();
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MAILBOX);
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

                                JSONObject data = response.getJSONObject("data");
                                JSONArray weekly = data.getJSONArray("weekly");
                                JSONArray invoice = data.getJSONArray("invoice");

                                if (invoice.length() > 0) {
                                    for (int i = 0; i < invoice.length(); i++) {
                                        JSONObject weeklyData = invoice.getJSONObject(i);
                                        MailBoxDBO mailbox = new MailBoxDBO();
                                        mailbox.setId(weeklyData.getString("id"));
                                        mailbox.setType(weeklyData.getString("type"));
                                        mailbox.setDateTime(weeklyData.getString("datetime"));
                                        mailbox.setGarageId(weeklyData.getString("garage_id"));
                                        mailbox.setJuId(weeklyData.getString("ju_id"));
                                        mailbox.setInvoiceId(weeklyData.getString("invoice_id"));
                                        mailbox.setJobTitle(weeklyData.getString("job_title"));
                                        mailbox.setCarId(weeklyData.getString("car_id"));
                                        mailbox.setCatName(weeklyData.getString("catname"));
                                        mailbox.setSubCatName(weeklyData.getString("subcatname"));
                                        mailbox.setSubSubCatName(weeklyData.getString("subsubcatname"));
                                        mailbox.setSid(weeklyData.getString("sid"));
                                        invoiceArrayList.add(mailbox);
                                    }
                                }
                                setData(invoiceArrayList);


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

    public void deleteNotification(String id, final int position) {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MAILBOX);
        params.put("delete_id", id);
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
                                invoiceArrayList.remove(position);
                                adptSettingInvoiceWeekly.notifyDataSetChanged();
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
