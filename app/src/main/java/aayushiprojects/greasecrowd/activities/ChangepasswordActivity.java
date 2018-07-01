package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

public class ChangepasswordActivity extends BaseActivity {

    LinearLayout ll_back;
    EditText E_CurrentPass, E_New_pass, E_Confirm_pass;
    TextView Chnag_pass;
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepassword);
        setHeader("Change password");
        getViews();
        setOnClickListener();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent = null;
        switch (v.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.buttonChange:
                if (checkValidation()) {
                    ChangePassword();
                }
                break;
        }
    }

    public void getViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        setHeader(getResources().getString(R.string.change_pass));
        ll_back = findViewById(R.id.ll_back);
        E_CurrentPass = findViewById(R.id.edt_old_password);
        E_New_pass = findViewById(R.id.et_new_password);
        E_Confirm_pass = findViewById(R.id.et_cn_password);
        Chnag_pass = findViewById(R.id.buttonChange);

    }

    public void setOnClickListener() {
        Chnag_pass.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    private boolean checkValidation() {
        String currentpass, newpass, confirmpass;
        Pattern patternSpace;
        Matcher matcherSpace;

        currentpass = E_CurrentPass.getText().toString().trim();
        newpass = E_New_pass.getText().toString();
        confirmpass = E_Confirm_pass.getText().toString();

        if (currentpass.trim().length() > 0) {
            if (currentpass.trim().length() < 8) {
                showAlertDialog(getString(R.string.valid_password));
                return false;
            }
        } else {
            showAlertDialog(getString(R.string.enter_current_password));
            return false;
        }

        if (newpass.trim().length() > 0) {
            if (newpass.trim().length() < 8) {
                showAlertDialog(getString(R.string.valid_password));
                return false;
            }
        } else {
            showAlertDialog(getString(R.string.enter_new_password));
            return false;
        }

        if (confirmpass.trim().length() > 0) {
            if (confirmpass.trim().length() < 8) {
                showAlertDialog(getString(R.string.valid_password));
                return false;
            }

            if (!confirmpass.equals(newpass)) {
                showAlertDialog(getString(R.string.valid_c_password));
                return false;
            }
        } else {
            showAlertDialog(getString(R.string.enter_confirm_password));
            return false;
        }

        return true;
    }

    public void ChangePassword() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Changepassword.SERVICE_NAME, Constants.Changepassword.CHG_USR_PWD);
        params.put(Constants.Changepassword.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.Changepassword.CPWD, E_CurrentPass.getText().toString());
        params.put(Constants.Changepassword.NPWD, E_New_pass.getText().toString());

        AppLog.Log("params", "" + params);
        AppLog.Log("params", "" + loginDetail_dbo.getJWTToken());

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
                                if (response.getString(Constants.MESSAGE).equals("Incorrect Password")) {
                                    showAlertDialog(getString(R.string.pu_valid_current_password));
                                } else {
                                    showAlertDialog(response.getString(Constants.MESSAGE));
                                }
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
