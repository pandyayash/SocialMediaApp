package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
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
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.DetailsOfGarageCarJobDBO;
import technource.greasecrowd.model.LoginDetail_DBO;

public class AddAReviewScreenActivity extends BaseActivity {

    LinearLayout ll_back;
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;
    String jid;
    int pos;
    private SimpleRatingBar ratingBar;
    private EditText message;
    private TextView txtSubmitReview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_areview_screen);
        setHeader("Add Review");
        findViews();
        setOnclickListener();
        Intent intent = getIntent();
        if (intent != null) {
            jid = intent.getStringExtra("jid");
            pos = intent.getIntExtra("pos", 0);
        }
    }

    private void findViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
        message = (EditText) findViewById(R.id.message);
        txtSubmitReview = (TextView) findViewById(R.id.txtSubmitReview);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(RESULT_CANCELED);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.txtSubmitReview:
                if (isValidate()) {
                    addReviewService(jid);
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void setOnclickListener() {
        txtSubmitReview.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }


    public boolean isValidate() {

        if (ratingBar.getRating() == 0) {
            showAlertDialog(getString(R.string.please_rate_the_job));
            return false;
        }
        return true;
    }

    public void addReviewService(String jid) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.PROVIDE_JOB_REVIEW);
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("jid", jid);
        params.put("review_text", message.getText().toString());
        params.put("rating", String.valueOf(ratingBar.getRating()));
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Intent intent = new Intent();
                                intent.putExtra("pos", pos);
                                setResult(RESULT_OK, intent);
                                finish();
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


}
