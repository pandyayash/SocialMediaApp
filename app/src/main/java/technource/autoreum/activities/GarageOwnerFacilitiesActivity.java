package technource.autoreum.activities;

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

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.FacilitiesDBo;
import technource.autoreum.model.LoginDetail_DBO;

public class GarageOwnerFacilitiesActivity extends BaseActivity {

    LinearLayout ll_back;
    ArrayList<FacilitiesDBo> facilitiesDBos;
    StringBuffer selsected_facilities;
    String facility;
    Context appContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView cont;
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_facilities);
        setHeader("Facilities");
        getviews();
        setonclick();
        GetFacilities();

    }

    private void getviews() {
        ll_back = findViewById(R.id.ll_back);
        facilitiesDBos = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        appContext = this;
        cont = findViewById(R.id.cont);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this);
        cont.setVisibility(View.VISIBLE);
    }

    private void setonclick() {
        ll_back.setOnClickListener(this);
        cont.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cont:
                CheckData();
                break;

            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void GetFacilities() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "get_garage_info");
        params.put("action", "facilities");

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
                                JSONArray json = response.getJSONArray("services" +
                                        "");

                                for (int i = 0; i < json.length(); i++) {
                                    FacilitiesDBo facility = new FacilitiesDBo();
                                    JSONObject obj = json.getJSONObject(i);
                                    facility.setId(obj.getInt("id"));
                                    facility.setFacilites(obj.getString("name"));
                                    if (obj.getInt("selected") == 1) {
                                        facility.setSelected(true);
                                    } else {
                                        facility.setSelected(false);
                                    }
                                    facilitiesDBos.add(facility);
                                }
                                setAdapter();

                            } else {
                                showAlertDialog("some error occured please ry again later");
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

    public void Setfacilities() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "edit_facilities");
        params.put("facilities", facility);

        AppLog.Log("TAG", "Params : " + new JSONObject(params));
        AppLog.Log("TAG", "JWT : " + loginDetail_dbo.getJWTToken());
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            Toast.makeText(appContext, "Facilities saved successfully", Toast.LENGTH_LONG).show();
                            finish();
                            activityTransition();

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

    private void setAdapter() {
        adapter = new Recycleradapter(facilitiesDBos, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void CheckData() {
        int count = 0;
        selsected_facilities = new StringBuffer();

        for (int j = 0; j < facilitiesDBos.size(); j++) {

            AppLog.Log("selected facility-->", "" + facilitiesDBos.get(j).isSelected());
            if (facilitiesDBos.get(j).isSelected()) {
                count++;
                selsected_facilities = selsected_facilities.append(facilitiesDBos.get(j).getId());
                selsected_facilities.append(",");
            }
        }
        if (count == 0) {
            facility = "";
        } else {

            if (selsected_facilities != null) {
                facility = selsected_facilities.substring(0, selsected_facilities.length() - 1);
                AppLog.Log("Facilities", "-->" + facility);
            }
        }
        Setfacilities();
    }


    public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Recycleviewholder> {


        Context context;
        ArrayList<FacilitiesDBo> services;

        public Recycleradapter(ArrayList<FacilitiesDBo> services, Context context) {

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

            holder.services_text.setText(facilitiesDBos.get(position).getFacilites());
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
            return facilitiesDBos.size();
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

            //if you implement onclick here you must have to use getposition() instead of making variable position global see documentation
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

}
