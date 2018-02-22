package technource.greasecrowd.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;

public class NotificationActivity extends BaseActivity {

  LinearLayout iv_back;
  SwitchCompat SwitchPushNotifcation, SwitchTextNotification, SwitchEmailNotification;
  Context appContext;
  LoginDetail_DBO loginDetail_dbo;
  String PushStatus = "0", EmailStatus = "0", SMS_Status = "0";


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_notification);
    setHeader("Notification");
    appContext = this;
    getViews();
    setClickListener();
    //setSwitchState();

  }


  public void getViews() {
    setHeader(getResources().getString(R.string.notification));
    iv_back = (LinearLayout) findViewById(R.id.ll_back);
    SwitchPushNotifcation = (SwitchCompat) findViewById(R.id.switchPushNotification);
    SwitchEmailNotification = (SwitchCompat) findViewById(R.id.switchEmailNotification);
    SwitchTextNotification = (SwitchCompat) findViewById(R.id.switchTextnotification);
    ll_back = (LinearLayout) findViewById(R.id.ll_back);
    loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);

    getnotificationsetting();

    SwitchPushNotifcation
        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
              PushStatus = "1";
            } else {
              PushStatus = "0";
            }
            ChnagenotifictionSetting();
          }
        });

    SwitchEmailNotification
        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
              EmailStatus = "1";
            } else {
              EmailStatus = "0";
            }
            ChnagenotifictionSetting();
          }
        });

    SwitchTextNotification
        .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if (b) {
              SMS_Status = "1";
            } else {
              SMS_Status = "0";
            }
            ChnagenotifictionSetting();
          }
        });

  }


  public void setSwitchState() {

    if (PushStatus.equals("0")) {
      SwitchPushNotifcation.setChecked(false);
    } else {
      SwitchPushNotifcation.setChecked(true);
    }

    if (EmailStatus.equals("0")) {
      SwitchEmailNotification.setChecked(false);
    } else {
      SwitchEmailNotification.setChecked(true);
    }

    if (SMS_Status.equals("0")) {
      SwitchTextNotification.setChecked(false);
    } else {
      SwitchTextNotification.setChecked(true);
    }

  }

  public void setClickListener() {
    ll_back.setOnClickListener(this);

  }

  public void getnotificationsetting() {
    showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.Changepassword.SERVICE_NAME, "not_set");
    params.put("action", "get");
    params.put(Constants.NotificationSetting.Params, loginDetail_dbo.getUser_Type());
    AppLog.Log("parameters", "" + params);
    CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
        url, appContext, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            showLoadingDialog(false);
            String push, email, text;
            AppLog.Log("TAG", "Response : " + response);
            try {
              String status = response.getString(Constants.STATUS);
              if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                PushStatus = response.getString(Constants.NotificationSetting.PUSH_NOTIFICATION);
                EmailStatus = response.getString(Constants.NotificationSetting.EMAIL_NOTIFICATION);
                SMS_Status = response.getString(Constants.NotificationSetting.SMS_NOTIFICATION);
                setSwitchState();
              } else {

                showAlertDialog(response.getString(Constants.MESSAGE));
                PushStatus = response.getString(Constants.NotificationSetting.PUSH_NOTIFICATION);
                EmailStatus = response.getString(Constants.NotificationSetting.EMAIL_NOTIFICATION);
                SMS_Status = response.getString(Constants.NotificationSetting.SMS_NOTIFICATION);
                setSwitchState();
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

          }
        });
    queue.add(jsonObjReq);
  }

  private void ChnagenotifictionSetting() {

    showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.Changepassword.SERVICE_NAME, "not_set");
    params.put("action", "set");
    params.put(Constants.NotificationSetting.PUSH_NOTIFICATION, PushStatus);
    params.put(Constants.NotificationSetting.EMAIL_NOTIFICATION, EmailStatus);
    params.put(Constants.NotificationSetting.SMS_NOTIFICATION, SMS_Status);
    params.put(Constants.NotificationSetting.Params, loginDetail_dbo.getUser_Type());

    AppLog.Log("parameters", "" + params);
    CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
        url, appContext, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            showLoadingDialog(false);
            String push, email, text;
            AppLog.Log("TAG", "Response : " + response);
            try {
              String status = response.getString(Constants.STATUS);
              if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                setSwitchState();

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
            // error
            showLoadingDialog(false);

          }
        });
    queue.add(jsonObjReq);

  }

  @Override
  public void onClick(View view) {
    super.onClick(view);
    int id = view.getId();
    switch (id) {
      case R.id.ll_back:
        finish();
        activityTransition();
        break;
    }
  }


}
