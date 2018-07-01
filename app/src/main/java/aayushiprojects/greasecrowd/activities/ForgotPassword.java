package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import aayushiprojects.greasecrowd.CustomViews.LoadingDialog.CustomDialog;
import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;

public class ForgotPassword extends BaseActivity implements View.OnClickListener {

  private static final String TAG = "ForgotPasswordScreen";
  Context appContext;
  EditText et_email, et_code, et_password, et_confirm_password;
  TextView tv_restore_password;
  CustomDialog dialog;
  boolean is_show = false;
  String email, phone, code;
  LinearLayout ll_enter_email, ll_enter_code, ll_new_password;
  LinearLayout ll_main;
  ScrollView sv;
  ImageView ll_back;
  String type;
  boolean isPhone = false;
  String otp;
  String user_type;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    initUI();

  }
  private void initUI() {
    appContext = ForgotPassword.this;
    Intent intent = getIntent();
    type = intent.getStringExtra("USER_TYPE");
    et_email = findViewById(R.id.et_email);
    et_code = findViewById(R.id.et_code);
    et_password = findViewById(R.id.et_password);
    et_confirm_password = findViewById(R.id.et_confirm_password);
    ll_back = findViewById(R.id.ll_back);
    ll_enter_email = findViewById(R.id.ll_enter_email);
    ll_enter_email.setVisibility(View.VISIBLE);
    ll_enter_code = findViewById(R.id.ll_enter_code);
    ll_enter_code.setVisibility(View.GONE);
    ll_new_password = findViewById(R.id.ll_new_password);
    ll_new_password.setVisibility(View.GONE);

    tv_restore_password = findViewById(R.id.tv_restore_password);
    sv = findViewById(R.id.sv);
    et_email.setOnEditorActionListener(new EditText.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId,
          KeyEvent event) {
        // TODO Auto-generated method stub
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          restorePassword();
          return true;
        }

        return false;
      }
    });
    et_code.setOnEditorActionListener(new EditText.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId,
          KeyEvent event) {
        // TODO Auto-generated method stub
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          restorePassword();
          return true;
        }

        return false;
      }
    });

    et_password.setOnEditorActionListener(new EditText.OnEditorActionListener() {

      @Override
      public boolean onEditorAction(TextView v, int actionId,
          KeyEvent event) {
        // TODO Auto-generated method stub
        if (actionId == EditorInfo.IME_ACTION_DONE) {
          restorePassword();
          return true;
        }

        return false;
      }
    });
    clickListeners();
    setTouchListener();
  }

  private void setTouchListener() {
    sv.setOnTouchListener(new View.OnTouchListener() {
      @Override
      public boolean onTouch(View v, MotionEvent event) {
        HelperMethods.hideSoftKeyboard(appContext, et_email);
        return false;
      }
    });
  }

  private void clickListeners() {
    ll_back.setOnClickListener(this);
    tv_restore_password.setOnClickListener(this);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    AppLog.Log(TAG, "Inside onTouch");
    try {
      HelperMethods.hideSoftKeyboard(ForgotPassword.this);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return true;
  }

  @Override
  public void onBackPressed() {
    goBack();
  }

  private void goBack() {
    if (tv_restore_password.getText().toString()
        .equalsIgnoreCase(getString(R.string.verify_email))) {
      super.onBackPressed();
      finish();
      activityTransition();
    } else if (tv_restore_password.getText().toString()
        .equalsIgnoreCase(getString(R.string.verify_code))) {
      ll_enter_email.setVisibility(View.VISIBLE);
      ll_enter_code.setVisibility(View.GONE);
      ll_new_password.setVisibility(View.GONE);
      tv_restore_password.setText(getString(R.string.verify_email));
    } else if (tv_restore_password.getText().toString()
        .equalsIgnoreCase(getString(R.string.restore_password_))) {
      ll_enter_email.setVisibility(View.GONE);
      ll_enter_code.setVisibility(View.VISIBLE);
      ll_new_password.setVisibility(View.GONE);
      tv_restore_password.setText(getString(R.string.verify_code));
    }
  }

  @Override
  public void onClick(View v) {
    Intent intent = null;
    switch (v.getId()) {
      case R.id.ll_back:
        goBack();
        break;
      case R.id.tv_restore_password:
        restorePassword();
        break;
    }
  }

  private void restorePassword() {

    if (Connectivity.isConnected(appContext)) {
      if (tv_restore_password.getText().toString()
          .equalsIgnoreCase(getString(R.string.verify_email))) {
        if (et_email.getText().toString().length() > 0) {
          if (validate()) {
            if (email.contains("@") || email.contains(".")) {
              isPhone = false;
              email = et_email.getText().toString();
            }
            verifyEmail();
          }
        } else {
          showAlertDialog(getString(R.string.blank_email));
        }

      } else if (tv_restore_password.getText().toString()
          .equalsIgnoreCase(getString(R.string.verify_code))) {
        if (et_code.getText().toString().length() > 0) {
          verifyCode();
        } else {
          showAlertDialog(getString(R.string.enter_code));
        }
      } else if (tv_restore_password.getText().toString()
          .equalsIgnoreCase(getString(R.string.restore_password_))) {
        String password = et_password.getText().toString().trim();
        String c_password = et_confirm_password.getText().toString().trim();
        Pattern patternSpace;
        Matcher matcherSpace;
        patternSpace = Pattern.compile("\\s");

        matcherSpace = patternSpace.matcher(password);

        boolean foundSpace = matcherSpace.find();
        if (password != null && password.length() > 0) {
          if (foundSpace) {
            showAlertDialog(getString(R.string.password_contains_space));
          } else {
            patternSpace = Pattern.compile(Constants.PASSWORD_PATTERN);
            matcherSpace = patternSpace.matcher(password);
            //one condition more for pattern match : !matcherSpace.matches() ||
            if ((password.length() < 8)) {
              showAlertDialog(getString(R.string.valid_password));
            } else {
              if (c_password.length() < 1) {
                showAlertDialog(getResources().getString(R.string.blank_c_password));
              } else if (!c_password.equalsIgnoreCase(password)) {
                showAlertDialog(getResources().getString(R.string.valid_c_password));
              } else {
                resetPassword();
              }
            }
          }
        } else {
          showAlertDialog(getString(R.string.blank_password));
        }
      }
    } else {
      showAlertDialog(getString(R.string.no_internet));
    }
  }

  private void verifyEmail() {

    showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;

    AppLog.Log(TAG, "App URL : " + url);
    Map<String, String> postParam = new HashMap<String, String>();
    postParam.put(Constants.Forgotpassword.SERVICE_NAME,
        Constants.Forgotpassword.FORGOT_PWD);
    postParam.put(Constants.Forgotpassword.USER_TYPE, type);
    postParam.put(Constants.Forgotpassword.EMAIL, et_email.getText().toString());

    AppLog.Log(TAG, "parameters : " + new JSONObject(postParam).toString());

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
        url, new JSONObject(postParam),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {

            AppLog.Log(TAG, "RESPONSE : " + response);
            String str_status;
            try {
              str_status = response.getString(Constants.STATUS);
              AppLog.Log(TAG, "Status in Login" + str_status);
              if (str_status != null) {
                if (str_status.equals(Constants.SUCCESS)) {
                  try {
                    String status = response.getString(Constants.STATUS);
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                      ll_enter_email.setVisibility(View.GONE);
                      ll_enter_code.setVisibility(View.VISIBLE);
                      ll_new_password.setVisibility(View.GONE);
                      tv_restore_password.setText(getString(R.string.verify_code));
                      code = response.getString(Constants.Forgotpassword.OTP);
                    } else {
                      if (status != null && status.equals(Constants.FAILURE)) {
                        if (response.getString(Constants.MESSAGE).contains("verified")) {

//                                         showAlertOkDialog(response.getString(Constants.MESSAGE),response.getString(Constants.USER_DETAILS.USER_ID),true) ;
                        }
//                                    else if(response.getString(Constants.MESSAGE).contains("incorrect_user")){
//                                        showAlertDialog(getResources().getString(R.string.email_or_passwrod_incorrect));
//                                    }
//
                        else {
                          showAlertDialog(response.getString(Constants.MESSAGE));
                        }
                      }
                    }
                  } catch (Exception e) {
                    e.printStackTrace();
                  }


                } else if (str_status.equals(Constants.FAILURE)) {

                  showAlertDialog(response.getString(Constants.MESSAGE));
                }
              }
            } catch (Exception e) {
              AppLog.Log(TAG, "Error in onResponse : " + e.getMessage());
              e.printStackTrace();
            }
            showLoadingDialog(false);
          }
        }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        AppLog.Log(TAG, "Error : " + error.getMessage());
        error.printStackTrace();
        showLoadingDialog(false);

      }


    }) {

      @Override
      public Map<String, String> getHeaders() {
//                    String auth = "Bearer " + loginDetail_dbo.getLogin_token();
        HashMap<String, String> headers = new HashMap<String, String>();
//                headers.put("Authorization", auth);
        return headers;
      }

    };
    jsonObjReq.setRetryPolicy(
        new DefaultRetryPolicy(60 * 1000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    // Adding request to request queue
    jsonObjReq.setShouldCache(false);
    //  AppController.getInstance().addToRequestQueue(jsonObjReq);
    queue.add(jsonObjReq);


  }

  public void showAlertOkDialog(String message, final String id, final boolean isCode) {

    dialogC = new CustomDialog(this, "ALERT", message);
    dialogC.setCanceledOnTouchOutside(false);
    if (dialogC != null && dialogC.isShowing()) {
      dialogC.dismiss();
      dialogC.show();
    } else {
      dialogC.show();
    }
    dialogC.setOnCancelListener(new DialogInterface.OnCancelListener() {
      @Override
      public void onCancel(DialogInterface dialogInterface) {

        Intent intent = new Intent(appContext,
            LoginScreen.class);
        intent.addFlags(Constants.INTENT_FLAGS);
        intent.putExtra("usertype",type);
        startActivity(intent);
        activityTransition();
        finish();


      }
    });
    dialogC.setOnDismissListener(new DialogInterface.OnDismissListener() {
      @Override
      public void onDismiss(DialogInterface dialogInterface) {
        Intent intent = new Intent(appContext, LoginScreen.class);
        intent.addFlags(Constants.INTENT_FLAGS);
        intent.putExtra("usertype",type);
        startActivity(intent);
        activityTransition();
        finish();
      }
    });
  }

  private void verifyCode() {
    if (et_code.getText().toString().length() > 0) {
      if (code.equalsIgnoreCase(et_code.getText().toString())) {
        ll_enter_email.setVisibility(View.GONE);
        ll_enter_code.setVisibility(View.GONE);
        ll_new_password.setVisibility(View.VISIBLE);
        tv_restore_password.setText(getString(R.string.restore_password_));
      } else {
        showAlertDialog(getString(R.string.code_not_verified));
      }
    } else {
      showAlertDialog(getString(R.string.enter_code));
    }
  }

  private void resetPassword() {
    showLoadingDialog(true);
    Map<String, String> postParam = new HashMap<String, String>();
    postParam.put(Constants.ResetPassword.SERVICE_NAME,
        Constants.ResetPassword.RESET_PWD);
    postParam.put(Constants.ResetPassword.OTP, code);
    postParam.put(Constants.ResetPassword.USER_TYPE, type);
    postParam
        .put(Constants.ResetPassword.EMAIL, et_email.getText().toString());
    postParam.put(Constants.ResetPassword.PWD,
        et_password.getText().toString().trim());
    RequestQueue queue = Volley.newRequestQueue(this);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log(TAG, "verifyEmail URL : " + url);

    JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, url,
        new JSONObject(postParam),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response2) {
            showLoadingDialog(false);
            AppLog.Log(TAG, "Signup Response: " + response2);
            try {
              String status = response2.getString(Constants.STATUS);
              if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                showAlertOkDialog(response2.getString(Constants.MESSAGE), "00", false);
              } else {
                if (status != null && status.equals(Constants.FAILURE)) {
                  if (response2.getString(Constants.MESSAGE).contains("verified")) {
                    showAlertOkDialog(response2.getString(Constants.MESSAGE),
                        response2.getString(Constants.USER_DETAILS.USER_ID), true);
                  }
//                                    else if(response.getString(Constants.MESSAGE).contains("incorrect_user")){
//                                        showAlertDialog(getResources().getString(R.string.email_or_passwrod_incorrect));
//                                    }
//
                  else {
                    showAlertDialog(response2.getString(Constants.MESSAGE));
                  }
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
            // error
            showLoadingDialog(false);
            AppLog.Log(TAG, "Error.Response: " + error.getMessage());
          }
        }
    ) {
      @Override
      protected Map<String, String> getParams() {
        Map<String, String> params = new HashMap<String, String>();
        if (isPhone) {
          params.put(Constants.SocialSignUp.PHONE, phone);
        } else {
          params.put(Constants.SocialSignUp.EMAIL, email);
        }
        params.put(Constants.SocialSignUp.VERIFICATION_CODE, et_code.getText().toString().trim());
        params.put(Constants.SocialSignUp.PASSWORD, et_password.getText().toString().trim());
        AppLog.Log(TAG, "parameters : " + params);
        return params;
      }
    };
    queue.add(postRequest);

  }

  private boolean validate() {
    email = et_email.getText().toString().trim();
    if (email != null && email.length() > 0) {
      if (email.contains("@") || email.contains(".")) {
        if (!HelperMethods.validateEmail(email)) {
          showAlertDialog(getString(R.string.please_enter_valid_email));
          return false;
        }
      }
    }
    return true;
  }
}
