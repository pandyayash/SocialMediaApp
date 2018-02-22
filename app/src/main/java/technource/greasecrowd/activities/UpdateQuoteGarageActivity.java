package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 6/2/18.
 */

public class UpdateQuoteGarageActivity extends BaseActivity {

    private String TAG = "UpdateQuoteGarageActivity";//jobBidmaster
    private TextView btnServiceType,txtjobTitle;
    private EditText edtComments;
    private EditText edtPrice;
    private EditText edtOfferPrice;
    private EditText edtTotalPrice;
    private EditText edtOffer;
    private TextView btnInclusions;
    private TextView btnBid;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    int bidPrice=0;
    int offerPrice=0;
    String strInclusions="";
    String txtquote="";
    public static ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    public static String jobTitle="";
    String strBidPrice="",strBidComment="",strOffer="",strOfferPrice="",strTotal="",strServiceId="",strServices="",strInclusion="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_job_garage);
        awardJobArrayList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            awardJobArrayList = intent.getParcelableArrayListExtra("jobBidmaster");
            jobTitle = intent.getStringExtra("jobTitle");

        }
        getViews();
        setHeader("Update Quote");
        setfooter("garageowner");
        setHomeFooterGarage(UpdateQuoteGarageActivity.this);
        setlistenrforfooter();

        setOnClickListener();
        setData(awardJobArrayList);

    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            if (bundle.getString("strServices")!=null){
                strServices = bundle.getString("strServices");
            }
            if (bundle.getString("strServiceId")!=null){
                strServiceId = bundle.getString("strServiceId");
            }
            if ( bundle.getString("strInclusions")!=null){
                strInclusions = bundle.getString("strInclusions");
            }
            if ( bundle.getString("jobTitle")!=null){
                jobTitle = bundle.getString("jobTitle");
            }
            if ( bundle.getParcelableArrayList("jobBidmaster")!=null){
                awardJobArrayList = bundle.getParcelableArrayList("jobBidmaster");
            }

            AppLog.Log(TAG,strServices);
            AppLog.Log(TAG,"Inclusions ---> " +strInclusions);
        }
    }
    private void getViews() {
        appContext = this;

        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        btnServiceType = (TextView)findViewById( R.id.btnServiceType );
        edtComments = (EditText)findViewById( R.id.edtComments );
        edtPrice = (EditText)findViewById( R.id.edtPrice );
        edtOfferPrice = (EditText)findViewById( R.id.edtOfferPrice );
        edtTotalPrice = (EditText)findViewById( R.id.edtTotalPrice );
        edtOffer = (EditText)findViewById( R.id.edtOffer );
        btnInclusions = (TextView)findViewById( R.id.btnInclusions );
        btnBid = (TextView)findViewById( R.id.btnBid );
        txtjobTitle = (TextView)findViewById( R.id.txtjobTitle );
        txtjobTitle.setText("Job title : "+ jobTitle);


    }

    public void setOnClickListener(){
        btnServiceType.setOnClickListener(this);
        btnBid.setOnClickListener(this);
        btnInclusions.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view==btnServiceType ){
            Intent intent = new Intent(appContext,ViewSelectSeriviceTypeActivity.class);
            intent.putExtra("isUpdate",true);
            startActivityForResult(intent,1);
            activityTransition();

        }
        if (view==btnInclusions){
            Intent intent = new Intent(appContext,FreeInclusionGarageActivity.class);
            intent.putExtra("isUpdate",true);
            startActivityForResult(intent,2);
            activityTransition();
        }
        if (view==btnBid){
            if (isValidate()){
                if (Connectivity.isConnected(appContext)){
                    SubmitBid();
                }else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }

        }
    }

    public boolean isValidate(){

        if (edtComments.getText().toString().length()==0){
            showAlertDialog("Please enter comment");
            return false;
        }else if (edtPrice.getText().toString().length()==0){
            showAlertDialog("Please enter bid price");
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader("Update Quote");
        setfooter("garageowner");
        setHomeFooterGarage(UpdateQuoteGarageActivity.this);
        setlistenrforfooter();
    }


    public void SubmitBid(){
        strBidComment = edtComments.getText().toString();
        strBidPrice = edtPrice.getText().toString();
        strTotal = edtTotalPrice.getText().toString();
        strOffer = edtOffer.getText().toString();
        strOfferPrice = edtOfferPrice.getText().toString();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SUBMIT_BID);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.SUBMIT_BIDS.JOB_ID, jobDetail_dbo.getCjob_id());
        params.put(Constants.SUBMIT_BIDS.GARAGE_ID, loginDetail_dbo.getUserid());
        params.put(Constants.SUBMIT_BIDS.BID_PRICE, strBidPrice);
        params.put(Constants.SUBMIT_BIDS.BID_COMMENT, strBidComment);
        params.put(Constants.SUBMIT_BIDS.ADD_OFFER, strOffer);
        params.put(Constants.SUBMIT_BIDS.ADD_OFFER_PRICE, strOfferPrice);
        params.put(Constants.SUBMIT_BIDS.TOTAL, strTotal);
        params.put(Constants.SUBMIT_BIDS.SERVICES, strServices);
        params.put(Constants.SUBMIT_BIDS.SERVICE_ID, strServiceId);
        params.put(Constants.SUBMIT_BIDS.INCLUSION, strInclusion);
        params.put(Constants.SUBMIT_BIDS.FLEET_ARR, "{}");

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Bid JOb --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(appContext,MyJobsGarageActivity.class);
                                startActivity(intent);
                                activityTransition();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                },
                 new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }

    public void setData(ArrayList<AwardJobDBOCarOwner> awardJobArrayList){
        for (int i=0;i<awardJobArrayList.size();i++){
            if (awardJobArrayList.get(i).getGarage_id().equalsIgnoreCase(loginDetail_dbo.getUserid())){
                edtComments.setText(awardJobArrayList.get(i).getBid_comment());

                edtPrice.setText(String.format("%.0f", Float.parseFloat(awardJobArrayList.get(i).getBid_price())));
                edtOfferPrice.setText(String.format("%.0f", Float.parseFloat(awardJobArrayList.get(i).getAdd_offer_price())));
                //edtTotalPrice.setText(awardJobArrayList.get(i).getTotal());
                edtOffer.setText(awardJobArrayList.get(i).getAdd_offer());
                if (edtOfferPrice.getText().toString().length()==0){
                    offerPrice = 0;
                }else {
                    offerPrice = Integer.parseInt(edtOfferPrice.getText().toString());
                }
                if (edtPrice.getText().toString().length()==0){
                    bidPrice = 0;
                }else {
                    bidPrice = Integer.parseInt(edtPrice.getText().toString());
                }


                int  total = bidPrice + offerPrice;

                edtTotalPrice.setText(String.valueOf(total));
            }
        }
    }
}
