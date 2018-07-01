package aayushiprojects.greasecrowd.helper;


import android.content.Context;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 12/4/17.
 */
public class CustomJsonObjectRequest extends JsonObjectRequest {

  Context context;
  LoginDetail_DBO loginDetail_dbo;

  public CustomJsonObjectRequest(int method, String url, Context appcontext, JSONObject jsonRequest,
      Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
    super(method, url, jsonRequest, listener, errorListener);
    context = appcontext;
    loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
  }


  @Override
  public Map<String, String> getHeaders() {
    Map<String, String> params = new HashMap<>();
    AppLog.Log("CustomJsonObjectRequest",
        "loginDetail_dbo.getLogin_token()=" + loginDetail_dbo.getJWTToken());
    params.put("Authorization", "Bearer" + " " + loginDetail_dbo.getJWTToken());
    return params;
  }

}