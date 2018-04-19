package technource.autoreum.activities.AskTheCrowd;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
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
import technource.autoreum.activities.BaseActivity;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.activities.MyCrowdUserActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;

public class AskTheCrowdAdditionalStep extends BaseActivity {

    EditText current_location, destination_location, ty_company, ty_policy_num, ty_claim_number, message;
    LinearLayout ll_emergency, ll_insuarnce, ll_continue, ll_back, ll_back_button, ll_special_case;
    TextView tv_insuarnce, tv_emergency;
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;
    String jwt, job_id;
    String is_emergency = "NO";
    String is_policy = "NO", help = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job_additional_step);
        getViews();
        setHeader("Ask The Crowd");
        setfooter("crowd");
        setHomeFooterCrowd(AskTheCrowdAdditionalStep.this);
        setlistenrforfooter();
        setClickListener();
        current_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    current_location.getBackground().setLevel(1);
                }
            }
        });
        ty_company.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    ty_company.getBackground().setLevel(1);
                }
            }
        });
        ty_claim_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    ty_claim_number.getBackground().setLevel(1);
                }
            }
        });
        ty_policy_num.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    ty_policy_num.getBackground().setLevel(1);
                }
            }
        });
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



    public void getViews() {
        appContext = this;
        current_location = findViewById(R.id.current_location);
        destination_location = findViewById(R.id.destination_location);
        ty_company = findViewById(R.id.ty_company);
        ty_policy_num = findViewById(R.id.ty_policy_num);
        ty_claim_number = findViewById(R.id.ty_claim_number);
        message = findViewById(R.id.message);


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

        current_location.setOnTouchListener(new View.OnTouchListener() {
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
        destination_location.setOnTouchListener(new View.OnTouchListener() {
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
        tv_insuarnce = findViewById(R.id.tv_insuarnce);
        tv_emergency = findViewById(R.id.tv_emergency);


        ll_emergency = findViewById(R.id.ll_emergency);
        ll_special_case = findViewById(R.id.ll_special_case);
        ll_insuarnce = findViewById(R.id.ll_insuarnce);
        ll_continue = findViewById(R.id.ll_continue);
        ll_back = findViewById(R.id.ll_back);
        ll_back_button = findViewById(R.id.ll_back_button);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            help = intent.getStringExtra("help");

        }
    }

    public void setClickListener() {
        ll_emergency.setOnClickListener(this);
        ll_insuarnce.setOnClickListener(this);
        ll_continue.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.ll_continue:
                checkData();
                break;
            case R.id.ll_emergency:
                setEmergencyLayout();
                break;

            case R.id.ll_insuarnce:
                setLayout();
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

    public void checkData() {

        if (isValidate()) {
            if (Connectivity.isConnected(appContext)) {
                callAditionalStep();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        }
    }


    public void callAditionalStep() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "ask_crowd");
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "5");
        params.put(Constants.PostNewJob.EMERGENCY, is_emergency);
        params.put(Constants.PostNewJob.HELP, help);
        params.put(Constants.PostNewJob.INSURANCE, is_policy);
        params.put(Constants.PostNewJob.CU_TOW_LOC, current_location.getText().toString());
        params.put(Constants.PostNewJob.DES_TOW_LOC, destination_location.getText().toString());
        params.put(Constants.PostNewJob.JID, job_id);
        params.put(Constants.PostNewJob.JOB_DESC, message.getText().toString());
        params.put(Constants.PostNewJob.INS_COMP, ty_company.getText().toString());
        params.put(Constants.PostNewJob.POLICY_NUM, ty_policy_num.getText().toString());
        params.put(Constants.PostNewJob.CLAIM_NUM, ty_claim_number.getText().toString());

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

    public boolean isValidate() {

        String currentlocation, msg, compny, policy, claim;

        currentlocation = current_location.getText().toString();
        msg = message.getText().toString();
        compny = ty_company.getText().toString();
        policy = ty_policy_num.getText().toString();
        claim = ty_claim_number.getText().toString();

        if (!(currentlocation != null && currentlocation.trim().length() > 0)) {
            current_location.requestFocus();
            current_location.getBackground().setLevel(4);
            showAlertDialog(getString(R.string.please_enter_current_location));
            return false;
        } else if (is_policy.equalsIgnoreCase("Yes")) {
            if (!(compny != null && compny.trim().length() > 0)) {
                ty_company.requestFocus();
                ty_company.getBackground().setLevel(4);
                showAlertDialog(getString(R.string.insurence_compny_name));
                return false;
            } else if (!(policy != null && policy.trim().length() > 0)) {
                ty_policy_num.requestFocus();
                ty_policy_num.getBackground().setLevel(4);
                showAlertDialog(getString(R.string.policy_number));
                return false;
            } else if (!(compny != null && compny.trim().length() > 0)) {
                ty_claim_number.requestFocus();
                showAlertDialog(getString(R.string.please_enter_claim_number));
                return false;
            } else if (!(claim != null && claim.trim().length() > 0)) {
                ty_claim_number.requestFocus();
                ty_claim_number.getBackground().setLevel(4);
                showAlertDialog(getString(R.string.please_enter_claim_number));
                return false;
            }
        }
        if (!(msg != null && msg.trim().length() > 0)) {
            message.requestFocus();
            message.getBackground().setLevel(4);
            showAlertDialog(getString(R.string.please_enter_job_description));
            return false;
        }
        return true;
    }


    public void setLayout() {
        if (is_policy.equalsIgnoreCase("NO")) {
            is_policy = "Yes";
            ll_insuarnce.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
            tv_insuarnce.setTextColor(getResources().getColor(R.color.white));
        } else {
            is_policy = "NO";
            ll_insuarnce.setBackground(getDrawable(R.drawable.rect_inner_boarder));
            tv_insuarnce.setTextColor(getResources().getColor(R.color.black_light_text));
        }

    }

    public void setEmergencyLayout() {
        if (is_emergency.equalsIgnoreCase("NO")) {
            is_emergency = "YES";
            ll_emergency.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
            tv_emergency.setTextColor(getResources().getColor(R.color.white));
        } else {
            is_emergency = "NO";
            ll_emergency.setBackground(getDrawable(R.drawable.rect_inner_boarder));
            tv_emergency.setTextColor(getResources().getColor(R.color.black_light_text));
        }
    }
}
