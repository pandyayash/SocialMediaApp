package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.RegisteredCarDBO;

public class UserDetailsActivity extends BaseActivity {

    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    LinearLayout ll_back;
    ArrayList<RegisteredCarDBO> registeredCarDBOs;
    private RelativeLayout img;
    private ImageView ivImage;
    private LinearLayout llName;
    private TextView tvName;
    private LinearLayout llAddres;
    private TextView tvAddress;
    private LinearLayout llMail;
    private TextView tvMAil;
    private TextView tvMailButton;
    private TextView tvRegisteredCarButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_details);
        setHeader("User Details");
        findViews();
        setOnclickListener();


        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("user_id")) {
                if (Connectivity.isConnected(appContext)) {
                    GetUsetDetails(intent.getStringExtra("user_id"));
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }
        }
    }

    public void GetUsetDetails(String Userid) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.Changepassword.SERVICE_NAME, "userpublicview");
        params.put("user_id", Userid);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", "" + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONObject data = response.getJSONObject("data");
                                JSONArray carData = data.getJSONArray("car_details");
                                JSONArray jarray = data.getJSONArray("user_details");
                                JSONObject userDetails = jarray.getJSONObject(0);

                                Glide.with(appContext)
                                        .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + userDetails.getString("image"))
                                        .asBitmap()
                                        .dontAnimate()
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .placeholder(R.drawable.car_demo)
                                        .error(R.drawable.car_demo)
                                        .skipMemoryCache(true)
                                        .into(ivImage);
                                tvName.setText(userDetails.getString("fname") + " " + userDetails.getString("lname"));
                                tvAddress.setText(userDetails.getString("suburb") + "," + userDetails.getString("state") + "," + userDetails.getString("postcode"));
                                tvMAil.setText(userDetails.getString("email"));

                                registeredCarDBOs = new ArrayList<>();
                                for (int i = 0; i < carData.length(); i++) {
                                    RegisteredCarDBO services = new RegisteredCarDBO();
                                    JSONObject obj = carData.getJSONObject(i);

                                    services.setId(obj.getString("id"));
                                    services.setRegistration_number(obj.getString("registration_number"));
                                    services.setCarmake(obj.getString("carmake"));
                                    services.setCarmodel(obj.getString("carmodel"));
                                    if (obj.getString("carbadge").equalsIgnoreCase("")) {
                                        services.setCarbadge("No Badge/Variant");
                                    } else {
                                        services.setCarbadge(obj.getString("carbadge"));
                                    }
                                    services.setCar_type(obj.getString("car_type"));
                                    services.setCar_img(obj.getString("car_img"));
                                    services.setYear(obj.getString("year"));
                                    registeredCarDBOs.add(services);
                                }
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tvMailButton:
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String uriText = "mailto:" + Uri.encode(tvMAil.getText().toString()) + "?subject=" + Uri.encode("Contact USer") + "&body=" + Uri.encode("");
                Uri uri = Uri.parse(uriText);
                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
                break;
            case R.id.tvRegisteredCarButton:
                Intent intent = new Intent(appContext, DisplayUserRegisterdCarsScreen.class);
                intent.putExtra("data", registeredCarDBOs);
                startActivity(intent);
                activityTransition();
                break;

        }
    }

    private void findViews() {
        appContext = this;
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        img = (RelativeLayout) findViewById(R.id.img);
        ivImage = (ImageView) findViewById(R.id.iv_image);
        llName = (LinearLayout) findViewById(R.id.llName);
        tvName = (TextView) findViewById(R.id.tvName);
        llAddres = (LinearLayout) findViewById(R.id.llAddres);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        llMail = (LinearLayout) findViewById(R.id.ll_mail);
        tvMAil = (TextView) findViewById(R.id.tvMAil);
        tvMailButton = (TextView) findViewById(R.id.tvMailButton);
        tvRegisteredCarButton = (TextView) findViewById(R.id.tvRegisteredCarButton);
    }

    private void setOnclickListener() {
        ll_back.setOnClickListener(this);
        tvMailButton.setOnClickListener(this);
        tvRegisteredCarButton.setOnClickListener(this);
    }
}
