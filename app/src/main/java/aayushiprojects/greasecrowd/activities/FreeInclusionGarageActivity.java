package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
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

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptFreeInclusions;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.Model_FreeInclusions;

public class FreeInclusionGarageActivity extends BaseActivity {

    String TAG = "SelectSubServicesGarageActivity";
    Context appContext;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<Model_FreeInclusions> inclusionsArrayList;
    ArrayList<String> stringArrayList;
    AdptFreeInclusions adptFreeInclusion;
    RecyclerView recyclerView;
    LinearLayout ll_continue,ll_back_button,ll_cancel;
    String strServiceId="",strServices="",strInclusions="";
    NestedScrollView nestedScroll;
    boolean isUpdate=false;
    LinearLayout ll_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_free_inclusion_garage);
        Intent intent = getIntent();
        if (intent!=null){
            isUpdate = intent.getBooleanExtra("isUpdate",isUpdate);
        }
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(this);
        setlistenrforfooter();

        getViews();
        setClickListeners();
       if (Connectivity.isConnected(appContext)){
           getFreeInclusions();
       }else {
           showAlertDialog(getString(R.string.no_internet));
       }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(FreeInclusionGarageActivity.this);
        setlistenrforfooter();
    }

    private void getViews(){
        appContext = this;
        inclusionsArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
       // inclusionsArrayList = jobDetail_dbo.getFreeInclusionsArrayList();
        recyclerView = findViewById(R.id.recyclerView);
        nestedScroll = findViewById(R.id.nestedScroll);
        recyclerView.setNestedScrollingEnabled(false);
        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);
        ll_cancel = findViewById(R.id.ll_cancel);
        ll_back = findViewById(R.id.ll_back);
    }
    private void setClickListeners() {
        ll_continue.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    public void setData() {
        adptFreeInclusion = new AdptFreeInclusions(inclusionsArrayList, appContext, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                strServiceId = inclusionsArrayList.get(position).getId();
                TextView textView = view.findViewById(R.id.autoText2);
                if (view==view.findViewById(R.id.ll_add_new_inclusion)){
                    ShowAddNewInclusionPopup();
                }
                if (view==view.findViewById(R.id.service_ll)){
                    Model_FreeInclusions modelFreeInclusions = inclusionsArrayList.get(position);
                    if (!modelFreeInclusions.isSelected()){
                        stringArrayList.add(modelFreeInclusions.getId());
                    }else {
                        if (stringArrayList.contains(modelFreeInclusions.getId())){

                            stringArrayList.remove(modelFreeInclusions.getId());


                        }
                    }
                    strInclusions = new Gson().toJson(stringArrayList);
                    AppLog.Log("stringArrayList",strInclusions);
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptFreeInclusion);

    }

    private void getFreeInclusions(){

        inclusionsArrayList = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GET_FREE_INCLUSIONS);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url,appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONArray json = response.getJSONArray("inclusions");

                                for (int i = 0; i < json.length(); i++) {
                                    Model_FreeInclusions modelFreeInclusions = new Model_FreeInclusions();
                                    JSONObject obj = json.getJSONObject(i);
                                    modelFreeInclusions.setId(obj.optString("id"));
                                    modelFreeInclusions.setInclusion(obj.optString("inclusion"));
                                    inclusionsArrayList.add(modelFreeInclusions);
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

    public void ShowAddNewInclusionPopup(){

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this,R.style.AppCompatAlertDialogStyle);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.new_inclusion_popup, null);
        dialogBuilder.setView(dialogView);

        final EditText edtInclucion = dialogView.findViewById(R.id.edtInclucion);

        dialogBuilder.setTitle("Add New Inclusion");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();
                SaveInclusion(edtInclucion.getText().toString());
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();

    }

    public void SaveInclusion(String strInclusion){

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SAVE_FREE_INCLUSIONS);
        params.put(Constants.FREE_INCLUSION.FREE_INCLUSION, strInclusion);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url,appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {


                                if (Connectivity.isConnected(appContext)){
                                    getFreeInclusions();
                                }else {
                                    showAlertDialog(getString(R.string.no_internet));
                                }


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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==ll_continue){

        /*    Intent intent = new Intent(appContext,QuoteJobGarageActivity.class);
            intent.putStringArrayListExtra("strInclusions",stringArrayList);
            //intent.putExtra("strServices",strServices);
            setResult(RESULT_OK, intent);
            startActivity(intent);
            activityTransition();*/

            if (isUpdate){
                finish();
                Intent intent = new Intent(appContext,UpdateQuoteGarageActivity.class);
                intent.putStringArrayListExtra("strInclusions",stringArrayList);
                startActivity(intent);
                activityTransition();

            }else {
                finish();
                Intent intent = new Intent(appContext,QuoteJobGarageActivity.class);
                intent.putStringArrayListExtra("strInclusions",stringArrayList);
                setResult(RESULT_OK, intent);
                startActivity(intent);
                activityTransition();
            }
        }
        if (view==ll_back) {

            onBackPressed();
        }

    }
}
