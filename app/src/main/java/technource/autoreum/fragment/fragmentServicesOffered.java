package technource.autoreum.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.adapter.Recycleradpterforservice;
import technource.autoreum.activities.SignUpGarageOwner;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.ServicesDBO;


/**
 * Created by technource on 6/9/17.
 */

public class fragmentServicesOffered extends Fragment implements OnClickListener {

    TextView cont;
    Context appContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    StringBuffer selsected_services;
    CheckBox sitecheck, mobilecheck;
    String site_string = "1", mobile_string = "1";
    String selected = "";
    View v;
    ArrayList<ServicesDBO> servicesDBOs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_services_offered, container, false);

        getviews();
        GetServices();
        cont.setOnClickListener(this);
        return v;
    }

    private void setAdapter() {
        adapter = new Recycleradpterforservice(servicesDBOs, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void getviews() {
        appContext = getActivity();
        servicesDBOs = new ArrayList<>();
        recyclerView = v.findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(true);
        cont = v.findViewById(R.id.cont);
        sitecheck = v.findViewById(R.id.checksite);
        mobilecheck = v.findViewById(R.id.checkmobile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cont:
                if (!sitecheck.isChecked()) {
                    site_string = "0";
                }
                if (!mobilecheck.isChecked()) {
                    mobile_string = "0";
                }
                CheckData();
                break;
        }
    }

    private void CheckData() {
        selsected_services = new StringBuffer();
        int count = 0;
        AppLog.Log("Count-->", "" + count);
        for (int j = 0; j < servicesDBOs.size(); j++) {
            if (servicesDBOs.get(j).isSelected()) {
                count++;
                selsected_services = selsected_services.append(servicesDBOs.get(j).getId());
                selsected_services.append(",");
            }
        }
        if (count == 0)
            ((SignUpGarageOwner) appContext).showAlertDialog(getResources().getString(R.string.select_some_item));

        else {
            selected = selsected_services.substring(0, selsected_services.length() - 1);
            setSignupService();
        }
    }

    public void GetServices() {
        ((SignUpGarageOwner) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "services");
        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpGarageOwner) appContext).showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONArray json = response.getJSONArray("services");

                                for (int i = 0; i < json.length(); i++) {
                                    ServicesDBO services = new ServicesDBO();
                                    JSONObject obj = json.getJSONObject(i);
                                    services.setId(obj.getInt("id"));
                                    services.setService(obj.getString("name"));
                                    servicesDBOs.add(services);
                                }
                                setAdapter();

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

    public void setSignupService() {
        ((SignUpGarageOwner) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "signup_garage");
        params.put(Constants.SIGN_UP.STEP, "2");
        params.put(Constants.Garageownersignup.params_fixed, site_string);
        params.put(Constants.Garageownersignup.params_mobile, mobile_string);
        params.put(Constants.Garageownersignup.params_services, selected);

        AppLog.Log("TAG", "Params Services: " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpGarageOwner) appContext).showLoadingDialog(false);
                        AppLog.Log("Response service offered", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                AppLog.Log("Output-->", status);
                                ((SignUpGarageOwner) appContext).tabLayout.setScrollPosition(2, 0f, true);
                                ((SignUpGarageOwner) appContext).viewPager.setCurrentItem(2);

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            }
        });

        queue.add(jsonObjReq);
    }
}
