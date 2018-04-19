package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

import technource.autoreum.R;
import technource.autoreum.adapter.AdptSubServiceGarage;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.interfaces.OnItemClickListener;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Model_GarageServices;

public class SelectSubServicesGarageActivity extends BaseActivity {


    String TAG = "SelectSubServicesGarageActivity";
    Context appContext;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<Model_GarageServices> garageServicesArrayList;
    ArrayList<String> stringArrayList;
    AdptSubServiceGarage adptGarageServiceType;
    RecyclerView recyclerView;
    EditText edtNoVehicle,edtUnplannedBreakDown,edtLogbookService;
    LinearLayout ll_continue, ll_back_button, ll_cancel, ll_fleet_managemnet;
    LinearLayout ll_include_roadside, ll_include_standard;
    TextView txt_include_roadside, txt_include_standard;
    TextView txtCount, txtCount_2, txtCount_3;
    ImageView iv_minus, iv_minus_2, iv_minus_3, iv_plus, iv_plus_2, iv_plus_3;
    String strServiceId = "", strServices = "", strServiceName = "";
    boolean isUpdate = false;
    int raodSideSelected = 0;
    int standardSelected = 0;
    int minteger = 0;
    int minteger_1 = 0;
    int minteger_2 = 0;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_sub_services_garage);
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(this);
        setlistenrforfooter();
        Intent intent = getIntent();
        if (intent != null) {
            strServiceId = intent.getStringExtra("strServiceId");
            strServiceName = intent.getStringExtra("strServiceName");
            isUpdate = intent.getBooleanExtra("isUpdate", isUpdate);
        }
        getViews();
        setClickListeners();
        if (Connectivity.isConnected(appContext)) {
            getSubServices();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    public void getViews() {
        appContext = this;
        garageServicesArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);
        ll_cancel = findViewById(R.id.ll_cancel);
        ll_back = findViewById(R.id.ll_back);
        ll_fleet_managemnet = findViewById(R.id.ll_fleet_managemnet);
        ll_include_roadside = findViewById(R.id.ll_include_roadside);
        ll_include_standard = findViewById(R.id.ll_include_standard);
        txt_include_roadside = findViewById(R.id.txt_include_roadside);
        txt_include_standard = findViewById(R.id.txt_include_standard);
        txtCount = findViewById(R.id.txtCount);
        txtCount_2 = findViewById(R.id.txtCount_2);
        txtCount_3 = findViewById(R.id.txtCount_3);
        iv_minus = findViewById(R.id.iv_minus);
        iv_minus_2 = findViewById(R.id.iv_minus_2);
        iv_minus_3 = findViewById(R.id.iv_minus_3);
        iv_plus = findViewById(R.id.iv_plus);
        iv_plus_2 = findViewById(R.id.iv_plus_2);
        iv_plus_3 = findViewById(R.id.iv_plus_3);
        edtNoVehicle = findViewById(R.id.edtNoVehicle);
        edtUnplannedBreakDown = findViewById(R.id.edtUnplannedBreakDown);
        edtLogbookService = findViewById(R.id.edtLogbookService);
        if (strServiceId.equalsIgnoreCase("7")) {
            recyclerView.setVisibility(View.GONE);
            ll_fleet_managemnet.setVisibility(View.VISIBLE);
        } else {
            recyclerView.setVisibility(View.VISIBLE);
            ll_fleet_managemnet.setVisibility(View.GONE);
        }
        //setData();

    }

    public void setClickListeners() {
        ll_continue.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_include_roadside.setOnClickListener(this);
        ll_include_standard.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_plus_2.setOnClickListener(this);
        iv_plus_3.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
        iv_minus_2.setOnClickListener(this);
        iv_minus_3.setOnClickListener(this);
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
                TextView textView = view.findViewById(R.id.autoText2);
                if (view == view.findViewById(R.id.service_ll)) {
                    Model_GarageServices modelFreeInclusions = garageServicesArrayList.get(position);
                    if (!modelFreeInclusions.isSelected()) {
                        stringArrayList.add(textView.getText().toString());
                    } else {
                        if (stringArrayList.contains(textView.getText().toString())) {

                            stringArrayList.remove(modelFreeInclusions.getName());


                        }
                    }
                    strServices = new Gson().toJson(stringArrayList);
                    AppLog.Log("stringArrayList", strServices);
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
        if (view == ll_continue) {
            if (isUpdate) {
                Intent intent = new Intent(appContext, UpdateQuoteGarageActivity.class);
                intent.putExtra("jobTitle", UpdateQuoteGarageActivity.jobTitle);
                intent.putExtra("jobBidmaster", UpdateQuoteGarageActivity.awardJobArrayList);
                intent.putExtra("strServiceId", strServiceId);

                if (strServiceId.equalsIgnoreCase("7")) {
                    intent.putExtra("raodSideSelected", raodSideSelected);
                    intent.putExtra("standardSelected", standardSelected);
                    intent.putExtra("txtCount", edtNoVehicle.getText().toString());
                    intent.putExtra("txtCount_2", edtUnplannedBreakDown.getText().toString());
                    intent.putExtra("txtCount_3", edtLogbookService.getText().toString());
                    //raodSideSelected
                } else {
                    intent.putStringArrayListExtra("strServices", stringArrayList);
                }
                startActivity(intent);
                activityTransition();

            } else {
                finish();
                Intent intent = new Intent(appContext, QuoteJobGarageActivity.class);
                intent.putExtra("strServiceId", strServiceId);

                if (strServiceId.equalsIgnoreCase("7")) {
                    intent.putExtra("raodSideSelected", raodSideSelected);
                    intent.putExtra("standardSelected", standardSelected);
                    intent.putExtra("txtCount", edtNoVehicle.getText().toString());
                    intent.putExtra("txtCount_2", edtUnplannedBreakDown.getText().toString());
                    intent.putExtra("txtCount_3", edtLogbookService.getText().toString());
                    //raodSideSelected
                } else {
                    intent.putStringArrayListExtra("strServices", stringArrayList);
                }

                setResult(RESULT_OK, intent);
                startActivity(intent);
                activityTransition();
            }


        }
        if (view == ll_back_button || view == ll_cancel) {
            finish();
            activityTransition();
        }
        if (view == ll_include_roadside) {
            if (ll_fleet_managemnet.getVisibility() == View.VISIBLE) {
                if (raodSideSelected == 0) {
                    ll_include_roadside.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    txt_include_roadside.setTextColor(getResources().getColor(R.color.white));
                    raodSideSelected = 1;
                } else {
                    ll_include_roadside.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                    txt_include_roadside.setTextColor(getResources().getColor(R.color.boardarcolor));
                    raodSideSelected = 0;
                }


            }
        }
        if (view == ll_include_standard) {
            if (ll_fleet_managemnet.getVisibility() == View.VISIBLE) {
                if (standardSelected == 0) {
                    ll_include_standard.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    txt_include_standard.setTextColor(getResources().getColor(R.color.white));
                    standardSelected = 1;
                } else {
                    ll_include_standard.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                    txt_include_standard.setTextColor(getResources().getColor(R.color.boardarcolor));
                    standardSelected = 0;
                }


            }
        }

        if (view == iv_plus) {

            increaseInteger(txtCount);
        }
        if (view == iv_plus_2) {
            //increaseInteger(txtCount_2);
            minteger_1 = minteger_1 + 1;
            display(minteger_1, txtCount_2);

        }
        if (view == iv_plus_3) {
            minteger_2 = minteger_2 + 1;
            display(minteger_2, txtCount_3);
        }
        if (view == iv_minus) {
            decreaseInteger(txtCount);
        }
        if (view == iv_minus_2) {
            //decreaseInteger(txtCount_2);
            if (minteger_1 > 1) {
                minteger_1 = minteger_1 - 1;
                display(minteger_1, txtCount_2);
            }
        }
        if (view == iv_minus_3) {
            //decreaseInteger(txtCount_3);
            if (minteger_2 > 1) {
                minteger_2 = minteger_2 - 1;
                display(minteger_2, txtCount_3);
            }
        }

    }

    public void increaseInteger(TextView textView) {
        minteger = minteger + 1;
        display(minteger, textView);

    }

    public void decreaseInteger(TextView textView) {
        if (minteger > 1) {
            minteger = minteger - 1;
            display(minteger, textView);
        }

    }

    private void display(int number, TextView textView) {
        textView.setText("" + number);
    }

    private void getSubServices() {

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
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response Sub Service-->", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int i = 0; i < jsonArray.length(); i++) {
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
