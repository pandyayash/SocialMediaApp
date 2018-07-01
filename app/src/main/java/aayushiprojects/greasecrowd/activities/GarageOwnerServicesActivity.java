package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.ServicesDBO;

public class GarageOwnerServicesActivity extends BaseActivity {

    LinearLayout ll_back;

    TextView cont;
    StringBuffer selsected_services;
    String selected = "";
    Context appContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    View v;
    ArrayList<ServicesDBO> servicesDBOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_services);
        setHeader("Services");
        getviews();
        GetServices();
        setonclick();
    }


    private void getviews() {
        ll_back = findViewById(R.id.ll_back);
        appContext = this;
        servicesDBOs = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        cont = findViewById(R.id.cont);
        cont.setVisibility(View.VISIBLE);
    }


    private void setonclick() {
        ll_back.setOnClickListener(this);
        cont.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.cont:
                CheckData();
                break;

        }
    }

    public void GetServices() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "get_garage_info");
        params.put("action", "services");
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
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
                                    if (obj.getInt("selected") == 1) {
                                        services.setSelected(true);
                                    } else {
                                        services.setSelected(false);
                                    }
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
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

    public void setSignupService() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "edit_services");
        params.put(Constants.Garageownersignup.params_services, selected);

        AppLog.Log("TAG", "Params Services: " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                AppLog.Log("Output-->", status);

                                Toast.makeText(appContext, "Services saved successfully", Toast.LENGTH_LONG).show();
                                finish();
                                activityTransition();

                            } else {

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });

        queue.add(jsonObjReq);
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
            showAlertDialog(getResources().getString(R.string.select_some_item));

        else {
            selected = selsected_services.substring(0, selsected_services.length() - 1);
            setSignupService();
        }
    }

    private void setAdapter() {
        adapter = new Recycleradpter(servicesDBOs, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    public class Recycleradpter extends RecyclerView.Adapter<Recycleradpter.Recycleviewholder> {

        ArrayList<ServicesDBO> services;
        Context context;

        public Recycleradpter(ArrayList<ServicesDBO> services, Context context) {

            this.context = context;
            this.services = services;
        }


        @Override
        public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services, parent, false);
            Recycleviewholder recycleviewholder = new Recycleviewholder(view);


            return recycleviewholder;
        }


        @Override
        public void onBindViewHolder(final Recycleviewholder holder, final int position) {

            holder.services_text.setText(services.get(position).getService());
            if (services.get(position).isSelected()) {
                holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                holder.services_text.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.service_ll.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                holder.services_text.setTextColor(getResources().getColor(R.color.text_color2));
            }

            holder.service_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    services.get(position).setSelected(!services.get(position).isSelected());
                    if (services.get(position).isSelected()) {
                        holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                        holder.services_text.setTextColor(getResources().getColor(R.color.white));
                    } else {
                        holder.service_ll.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                        holder.services_text.setTextColor(getResources().getColor(R.color.text_color2));
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return services.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll;

            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = itemView.findViewById(R.id.autoText2);
                service_ll = itemView.findViewById(R.id.service_ll);
            }
        }
    }

}
