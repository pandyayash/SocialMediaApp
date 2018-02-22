package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.adapter.AdptGarageServiceType;
import technource.greasecrowd.adapter.AdptSubServiceGarage;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.interfaces.OnItemClickListener;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.Model_FreeInclusions;
import technource.greasecrowd.model.Model_GarageServices;

import static technource.greasecrowd.adapter.AdptQuoteDetailsPager.jobTitle;

public class SelectSubServicesGarageActivity extends BaseActivity {


    String TAG = "SelectSubServicesGarageActivity";
    Context appContext;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<Model_GarageServices> garageServicesArrayList;
    ArrayList<String>stringArrayList;
    AdptSubServiceGarage adptGarageServiceType;
    RecyclerView recyclerView;
    LinearLayout ll_continue,ll_back_button,ll_cancel;
    String strServiceId="",strServices="",strServiceName="";
    boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_services_garage);
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(this);
        setlistenrforfooter();
        Intent intent = getIntent();
        if (intent!=null){
            strServiceId = intent.getStringExtra("strServiceId");
            strServiceName = intent.getStringExtra("strServiceName");
            isUpdate = intent.getBooleanExtra("isUpdate",isUpdate);
        }
        getViews();
        setClickListeners();
        if (Connectivity.isConnected(appContext)){
            getSubServices();
        }else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    public void getViews(){
        appContext = this;
        garageServicesArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        ll_continue = (LinearLayout) findViewById(R.id.ll_continue);
        ll_back_button = (LinearLayout) findViewById(R.id.ll_back_button);
        ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
        //setData();

    }

    public void setClickListeners(){
        ll_continue.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(appContext);
        setlistenrforfooter();
    }

    public void setData() {
        adptGarageServiceType = new AdptSubServiceGarage(garageServicesArrayList, appContext, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                strServiceId = garageServicesArrayList.get(position).getService_id();
                TextView textView = (TextView) view.findViewById(R.id.autoText2);
                if (view==view.findViewById(R.id.service_ll)){
                    Model_GarageServices modelFreeInclusions = garageServicesArrayList.get(position);
                    if (!modelFreeInclusions.isSelected()){
                        stringArrayList.add(textView.getText().toString());
                    }else {
                        if (stringArrayList.contains(textView.getText().toString())){

                            stringArrayList.remove(modelFreeInclusions.getName());


                        }
                    }
                    strServices = new Gson().toJson(stringArrayList);
                    AppLog.Log("stringArrayList",strServices);
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptGarageServiceType);

    }
    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==ll_continue){
            if (isUpdate){
                Intent intent = new Intent(appContext,UpdateQuoteGarageActivity.class);
                intent.putExtra("jobTitle",UpdateQuoteGarageActivity.jobTitle);
                intent.putExtra("jobBidmaster",UpdateQuoteGarageActivity.awardJobArrayList);
                startActivity(intent);
                activityTransition();

            }else {
                finish();
                Intent intent = new Intent(appContext,QuoteJobGarageActivity.class);
                intent.putExtra("strServiceId",strServiceId);
                intent.putStringArrayListExtra("strServices",stringArrayList);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                activityTransition();
            }


        }
        if (view==ll_back_button || view==ll_cancel){
            finish();
            activityTransition();
        }
    }

    private void getSubServices(){

        //inclusionsArrayList = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SELECT_SUB_SERVICES);
        params.put("service_id", strServiceId);
        params.put("ss", strServiceName);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url,appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response Sub Service-->", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i=0;i<jsonArray.length();i++){
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    Model_GarageServices modelGarageServices = new Model_GarageServices();
                                    modelGarageServices.setId(jsonObject.optString("id"));
                                    modelGarageServices.setName(jsonObject.optString("name"));
                                    modelGarageServices.setService_id(jsonObject.optString("service_id"));
                                    modelGarageServices.setIs_checked(jsonObject.optString("is_checked"));
                                    garageServicesArrayList.add(modelGarageServices);
                                }
                                setData();


                            } else {
                                showAlertDialog(getString(R.string.some_error_try_again));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

}
