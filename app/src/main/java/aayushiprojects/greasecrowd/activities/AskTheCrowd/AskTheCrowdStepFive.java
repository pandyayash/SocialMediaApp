package aayushiprojects.greasecrowd.activities.AskTheCrowd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.BaseActivity;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.MyCrowdUserActivity;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

public class AskTheCrowdStepFive extends BaseActivity  {
    String TAG = "AskTheCrowdStepFive";
    LinearLayout ll_continue, ll_back_button, ll_cancel, ll_back;
    EditText message;
    Context appContext;
    String jwt, job_id,placeholder;
    LoginDetail_DBO loginDetail_dbo;
    String help = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask_the_crowd_step_five);
        getViews();
        setHeader("Ask The Crowd");
        setfooter("crowd");
        setHomeFooterCrowd(AskTheCrowdStepFive.this);
        setlistenrforfooter();
        setOnClickListener();
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    message.getBackground().setLevel(1);
                }
            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        setPostCrowdFooter(this);
        isAskCrowd = true;
    }

    public void getViews(){
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);
        ll_cancel = findViewById(R.id.ll_cancel);
        ll_back = findViewById(R.id.ll_back);

        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            placeholder = intent.getStringExtra("placeholder");
            help = intent.getStringExtra("help");
        }

        message = findViewById(R.id.message);
        AppLog.Log("place4 holder", "-->" + placeholder);

        message.setHint(placeholder);

        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.message) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });
    }
    public void setOnClickListener() {

        ll_continue.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_continue:
                checkData();
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

    private void checkData() {
        if (isvalidate()) {
            if (Connectivity.isConnected(appContext)) {
                CallFinalApi();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        }
    }
    public void CallFinalApi() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "ask_crowd");
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "5");
        params.put(Constants.PostNewJob.EMERGENCY, "");
        params.put(Constants.PostNewJob.HELP, help);
        params.put(Constants.PostNewJob.INSURANCE, "");
        params.put(Constants.PostNewJob.DROP_LOC, "");
        params.put(Constants.PostNewJob.JID, job_id);
        params.put(Constants.PostNewJob.DROP_TIME, "");
        params.put(Constants.PostNewJob.PICK_TIME, "");
        params.put(Constants.PostNewJob.FLEXIBILITY, "");
        params.put(Constants.PostNewJob.JOB_DESC, message.getText().toString());

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
                                Toast.makeText(appContext, getString(R.string.str_successfully_posted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(appContext, MyCrowdUserActivity.class);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);
                                finish();
                                activityTransition();
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

    public boolean isvalidate() {
        String  mesg = message.getText().toString();

        if (!(mesg != null && mesg.trim().length() > 0)) {
            message.requestFocus();
            showAlertDialog(getString(R.string.please_enter_crowd_description));
            message.getBackground().setLevel(4);
            return false;
        }
        return true;
    }
}
