package technource.autoreum.activities.PostJob;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

import technource.autoreum.R;
import technource.autoreum.activities.BaseActivity;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.CatogoriesDBO;
import technource.autoreum.model.DroppOffLocationDbo;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SubCatogoryDBO;

public class PostNewJobStepTwo extends BaseActivity {

    Context appContext;
    private RecyclerView recyclerView;
    ArrayList<CatogoriesDBO> catogoriesDBOArrayList;
    ArrayList<SubCatogoryDBO> subcatogoriesDBOArrayList;
    ArrayList<SubCatogoryDBO> subcatogoriesDBOArrayList_temp;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String category_id = "", category_name = "";
    String jwt, job_id;
    LoginDetail_DBO loginDetail_dbo;
    LinearLayout ll_continue, ll_cancel, ll_back, ll_back_button;
    String additional = "";
    String additional_id = "";
    int subCat=0;
    ArrayList<DroppOffLocationDbo> dropofflocation;
    String garageId="";
    boolean isFromMyGarage=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job_step_two);
        getviews();
        getAndSetData();
        setHeader("Post a New Job");
        setfooter("jobs");
        setlistenrforfooter();
        setClickListener();
        if (Connectivity.isConnected(appContext)) {
            GetCategories();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }
    }

    public void getviews() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        appContext = this;
        catogoriesDBOArrayList = new ArrayList<>();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        ll_continue = (LinearLayout) findViewById(R.id.ll_continue);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        ll_back_button = (LinearLayout) findViewById(R.id.ll_back_button);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        dropofflocation = new ArrayList<>();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPostJObFooter(this);
    }

    public void setClickListener() {
        ll_continue.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
    }

    public void getAndSetData() {

        Intent intent = getIntent();
        if(intent!=null){
            job_id = intent.getStringExtra("job_id");
            garageId = intent.getStringExtra("garageId");
            isFromMyGarage = intent.getBooleanExtra("isFromMyGarage",false);
            if (intent.hasExtra("categories")) {
                category_id = intent.getStringExtra("categories");
                AppLog.Log("cat_id", "cat_id" + " " + category_id);
            }
        }


    }

    public void setAdapter() {
        adapter = new Recycleradapter(catogoriesDBOArrayList, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void GetCategories() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put("service_name", "categories");

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
                                    CatogoriesDBO categories = new CatogoriesDBO();
                                    JSONObject obj = json.getJSONObject(i);
                                    categories.setId(obj.getString("id"));

                                    if (category_id.equalsIgnoreCase(obj.getString("id"))) {
                                        categories.setSelected(true);
                                    }

                                    categories.setName(obj.getString("name"));
                                    categories.setItext(obj.getString("itext"));

                                    catogoriesDBOArrayList.add(categories);
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

    private void CheckData() {
        for (int i = 0; i < catogoriesDBOArrayList.size(); i++) {
            if (catogoriesDBOArrayList.get(i).isSelected()) {
                category_id = catogoriesDBOArrayList.get(i).getId();
                category_name = catogoriesDBOArrayList.get(i).getName();
            }
        }
        if (!category_id.equalsIgnoreCase("")) {
            callPostJobStepTwo();
        } else {
            showAlertDialog(getString(R.string.please_select_atleast_one_category));
        }
    }

    public void callPostJobStepTwo() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, WebServiceURLs.POST_JOB);
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "2");
        params.put(Constants.PostNewJob.CAT_ID, category_id);
        params.put(Constants.PostNewJob.JID, job_id);
        if (isFromMyGarage){
            params.put(Constants.PostNewJob.ASSIGNED_TO_GARAGE, garageId);

        }

        AppLog.Log("TAG", "Params : " + new JSONObject(params));
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
                                subCat = response.getInt("sub_cat_data");
                                subcatogoriesDBOArrayList = new ArrayList<>();
                                dropofflocation = new ArrayList<>();
                                JSONArray jarray = response.getJSONArray("sub_cats");
                                for (int i = 0; i < jarray.length(); i++) {
                                    subcatogoriesDBOArrayList_temp = new ArrayList<>();
                                    JSONObject jobj = jarray.getJSONObject(i);
                                    SubCatogoryDBO catogoriesDBO = new SubCatogoryDBO();

                                    catogoriesDBO.setId(jobj.getString("id"));
                                    catogoriesDBO.setName(jobj.getString("name"));
                                    catogoriesDBO.setImage(jobj.getString("image"));
                                    catogoriesDBO.setParent_id(jobj.getString("parent_id"));
                                    catogoriesDBO.setPlaceholder(jobj.getString("placeholder"));
                                    catogoriesDBO.setItext(jobj.getString("itext"));
                                    if (jobj.getString("sub_cats") != null && jobj.getString("add_incl").equalsIgnoreCase("")) {
                                        JSONArray jarray2 = jobj.getJSONArray("sub_cats");
                                        for (int j = 0; j < jarray2.length(); j++) {
                                            SubCatogoryDBO subCatogoryDBO = new SubCatogoryDBO();
                                            JSONObject jobj2 = jarray2.getJSONObject(j);
                                            subCatogoryDBO.setId(jobj2.getString("id"));
                                            subCatogoryDBO.setName(jobj2.getString("name"));
                                            subCatogoryDBO.setParent_id(jobj2.getString("parent_id"));
                                            subCatogoryDBO.setImage(jobj2.getString("image"));
                                            subCatogoryDBO.setItext(jobj2.getString("itext"));
                                            subcatogoriesDBOArrayList_temp.add(subCatogoryDBO);
                                        }
                                    } else {
                                        additional = jobj.getString("add_incl");
                                    }
                                    catogoriesDBO.setSubcategory(subcatogoriesDBOArrayList_temp);
                                    subcatogoriesDBOArrayList.add(catogoriesDBO);
                                }

                                JSONArray jarray3 = response.getJSONArray("dorpoff_location");
                                for (int j = 0; j < jarray3.length(); j++) {
                                    DroppOffLocationDbo droppOffLocationDbo = new DroppOffLocationDbo();
                                    JSONObject jobj3 = jarray3.getJSONObject(j);
                                    droppOffLocationDbo.setName(jobj3.getString("address"));
                                    dropofflocation.add(droppOffLocationDbo);
                                }
                                if (subCat!=0) {
                                    Intent intent = new Intent(appContext, PostNewJobStepThree.class);
                                    intent.putExtra("additional", additional);
                                    intent.putExtra("category_name", category_name);
                                    intent.putExtra("drop_off", dropofflocation);
                                    intent.putExtra("subcategory", subcatogoriesDBOArrayList);
                                    intent.putExtra("job_id", job_id);
                                    startActivity(intent);
                                    activityTransition();
                                }else {
                                    Intent intent = new Intent(appContext,PostNewJobStepFour.class);
                                    startActivity(intent);
                                    activityTransition();
                                }
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
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

    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.ll_continue:
                if (Connectivity.isConnected(appContext)) {
                    CheckData();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                break;
            case R.id.ll_cancel:
                Intent intent = new Intent(appContext, DashboardScreen.class);
                intent.addFlags(Constants.INTENT_FLAGS);
                startActivity(intent);
                finish();
                activityTransition();
                break;
            case R.id.ll_back_button:
                onBackPressed();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Recycleviewholder> {
        Context context;
        ArrayList<CatogoriesDBO> services;

        public Recycleradapter(ArrayList<CatogoriesDBO> services, Context context) {

            this.context = context;
            this.services = services;
        }

        @Override
        public Recycleradapter.Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services_with_info, parent, false);
            Recycleradapter.Recycleviewholder recycleviewholder = new Recycleradapter.Recycleviewholder(view);
            return recycleviewholder;
        }

        @Override
        public void onBindViewHolder(final Recycleradapter.Recycleviewholder holder, final int position) {

            holder.services_text.setText(services.get(position).getName());
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
                        for (int i = 0; i < services.size(); i++) {
                            if (!(position == i)) {
                                catogoriesDBOArrayList.get(i).setSelected(false);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        holder.service_ll.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                        holder.services_text.setTextColor(getResources().getColor(R.color.text_color2));
                        category_id = "";

                    }

                }
            });

            if (!services.get(position).getItext().equalsIgnoreCase("")) {
                holder.ll_info.setVisibility(View.VISIBLE);
                holder.ll_info.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (!services.get(position).getItext().equalsIgnoreCase("")) {
                            showAlertDialog(services.get(position).getItext());
                        } else {

                        }
                    }
                });
            } else {
                holder.ll_info.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll, ll_info;

            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = (TextView) itemView.findViewById(R.id.autoText2);
                service_ll = (LinearLayout) itemView.findViewById(R.id.service_ll);
                ll_info = (LinearLayout) itemView.findViewById(R.id.ll_info);
            }

            //if you implement onclick here you must have to use getposition() instead of making variable position global see documentation
        }
    }
}
