package technource.autoreum.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;

public class PaypalActivity extends BaseActivity {

    LinearLayout ll_back;
    EditText Edit_Paypal;
    TextView BtnPaypal;
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        setHeader("Paypal");
        getviews();
        setOnClickListener();
        if (Connectivity.isConnected(appContext)) {
            GetPaymentDetails();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }

    }

    private void getviews() {
        appContext = this;
        ll_back = findViewById(R.id.ll_back);
        Edit_Paypal = findViewById(R.id.ed_paypal);
        BtnPaypal = findViewById(R.id.btnPaypal);
    }

    public void setOnClickListener() {
        ll_back.setOnClickListener(this);
        BtnPaypal.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.btnPaypal:
                if (Connectivity.isConnected(appContext)) {
                    Add_PaymentMethod();
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
                break;

        }
    }

    public void Add_PaymentMethod() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Paypal.SERVICE_NAME, Constants.Paypal.PAYPAL_INFO);
        params.put(Constants.Paypal.ACTION, Constants.Paypal.SET);
        params.put(Constants.Paypal.PAYPAL_ID, Edit_Paypal.getText().toString());

        AppLog.Log("Paypal", "Paypal" + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("params", "" + response);
                        try {

                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE),
                                        Toast.LENGTH_SHORT).show();
                                finish();
                                activityTransition();
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public void GetPaymentDetails() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Paypal.SERVICE_NAME, Constants.Paypal.PAYPAL_INFO);
        params.put(Constants.Paypal.ACTION, Constants.Paypal.GET);
        AppLog.Log("Paypal", "Paypal" + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("params", "" + response);
                        try {

                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                if (!response.getString("paypal_info").equalsIgnoreCase("null") && response.getString("paypal_info") != "")
                                    Edit_Paypal.setText(response.getString("paypal_info"));

                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

}
