package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import technource.autoreum.R;

import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Model_FollowupWork;

public class ViewFollowUpWorkActivity extends BaseActivity {

    String TAG = "ViewFollowUpWorkActivity";
    Context appContext;
    ArrayList<Model_FollowupWork> followupWorkArrayList;
    AdptFollowupWork adptFollowupWork;
    RecyclerView recyclerView;
    TextView btnSubmit;
    LinearLayout ll_back;
    String total, jid, awarded_garage_id;
    ArrayList<String> work;
    ArrayList<String> accept;
    ArrayList<String> amount;
    LoginDetail_DBO loginDetail_dbo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_follow_up_work);
        appContext = this;
        setHeader("Follow Up Work");
        setfooter("job_details");
        setJobDetailsQuoteFooter(appContext);
        setlistenrforfooter();
        getViews();
        setOnClickListener();
    }

    public void getViews() {
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        followupWorkArrayList = new ArrayList<>();
        btnSubmit = findViewById(R.id.btnSubmit);
        ll_back = findViewById(R.id.ll_back);
        Intent intent = getIntent();
        if (intent != null) {
            followupWorkArrayList = intent.getParcelableArrayListExtra("followupWorkArrayList");
            total = intent.getStringExtra("total");
            jid = intent.getStringExtra("jid");
            awarded_garage_id = intent.getStringExtra("garageId");
        }
        recyclerView = findViewById(R.id.recyclerView);
        setData();
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            btnSubmit.setVisibility(View.GONE);
        } else {
            btnSubmit.setVisibility(View.VISIBLE);
        }
    }

    public void setOnClickListener() {
        btnSubmit.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    public void setData() {
        adptFollowupWork = new AdptFollowupWork(appContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptFollowupWork);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (isValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        submitFollowUp();
                    } else {
                        showAlertDialog(getString(R.string.no_internet));
                    }
                }
                break;
        }
    }

    /*{
        "service_name":"job_actions",
            "action":"FOLLOWUP_SUBMIT",
            "total":"30",
            "work":{"test":"10","test1":"20"},
        "jid":"1733",
            "accept":{"test":"true","test1":"false"},
        "awarded_garage_id":"29"
    }*/


    public boolean isValidate() {
        work = new ArrayList<>();
        accept = new ArrayList<>();
        amount = new ArrayList<>();
        try {
            for (int i = 0; i < followupWorkArrayList.size(); i++) {
                work.add(followupWorkArrayList.get(i).getKey());
                amount.add(followupWorkArrayList.get(i).getValue());
                /*if (followupWorkArrayList.get(i).isRadioAccept()) {
                    accept.add("true");
                } else {
                    accept.add("no response");
                }*/
                if (followupWorkArrayList.get(i).getIsFollowUpAcceptedorNot() == 1) {
                    accept.add("true");
                } else if (followupWorkArrayList.get(i).getIsFollowUpAcceptedorNot() == 2) {
                    accept.add("false");
                } else {
                    accept.add("no response");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        AppLog.Log("work", "" + accept);
        return true;
    }

    public void submitFollowUp() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS);
        params.put("action", WebServiceURLs.FOLLWUP_SUBMIT_USER_SIDE);
        params.put("total", total);
        params.put("jid", jid);
        params.put("awarded_garage_id", awarded_garage_id);
        params.put("work", android.text.TextUtils.join(",", work));
        params.put("accept", android.text.TextUtils.join(",", accept));
        params.put("amount", android.text.TextUtils.join(",", amount));
        AppLog.Log("TAG", "Params : " + params);
        params.put("no_of_records", "10");
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(appContext, QuoteJobDetailsActivity.class);
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

    public class AdptFollowupWork extends RecyclerView.Adapter<AdptFollowupWork.ViewHolder> {

        Context context;
        LoginDetail_DBO loginDetail_dbo;

        public AdptFollowupWork(Context context) {
            this.context = context;
            loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.context);
        }

        @Override
        public AdptFollowupWork.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_followup_work, parent, false);

            return new AdptFollowupWork.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final AdptFollowupWork.ViewHolder holder, final int position) {
            Model_FollowupWork model_followupWork = followupWorkArrayList.get(position);

            if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                holder.txtAcceptOrReject.setVisibility(View.GONE);
                holder.acceptOrReject.setVisibility(View.GONE);
                if (!model_followupWork.getKey().equalsIgnoreCase("")) {
                    holder.txtKey.setText(model_followupWork.getKey());
                }
                if (!model_followupWork.getValue().equalsIgnoreCase("")) {
                    holder.txtValue.setText(model_followupWork.getValue());
                }
                if (followupWorkArrayList.get(position).getIsAccepted() == 0) {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("Rejected");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.red_color));
                } else if (followupWorkArrayList.get(position).getIsAccepted() == 1) {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("Accepted");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.green_btn));
                }

            } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                if (!model_followupWork.getKey().equalsIgnoreCase("")) {
                    holder.txtKey.setText(model_followupWork.getKey());
                }
                if (!model_followupWork.getValue().equalsIgnoreCase("")) {
                    holder.txtValue.setText(model_followupWork.getValue());
                }
               /* if (followupWorkArrayList.get(position).getIsFollowUpAcceptedorNot()==1){
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("Accepted");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.red_color));
                }else if (followupWorkArrayList.get(position).getIsFollowUpAcceptedorNot()==2){
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("Rejected");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.green_btn));
                }else {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("No Response");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.boardarcolor));
                }*/
                // 0-> seen but not accepted , 1--> seen and accepted, 2--> not seen
                if (followupWorkArrayList.get(position).getIsAccepted() == 0) {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("No Response");
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.boardarcolor));
                    btnSubmit.setVisibility(View.GONE);

                } else if (followupWorkArrayList.get(position).getIsAccepted() == 1) {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    //holder.txtAcceptOrReject.setText("Accepted");
                    btnSubmit.setVisibility(View.GONE);
                    holder.txtAcceptOrReject.setText("Accepted");
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.green_btn));
                } else if (followupWorkArrayList.get(position).getIsAccepted() == 2) {
                    holder.acceptOrReject.setVisibility(View.VISIBLE);
                } else if (followupWorkArrayList.get(position).getIsAccepted() == 4) {
                    holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                    holder.txtAcceptOrReject.setText("Rejected");
                    holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.red_color));

                }


                holder.acceptOrReject.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup radioGroup, int i) {
                        if (i == R.id.radioAccept) {
                            followupWorkArrayList.get(position).setRadioAccept(true);
                            followupWorkArrayList.get(position).setIsFollowUpAcceptedorNot(1);
                        } else if (i == R.id.radioReject) {
                            followupWorkArrayList.get(position).setRadioAccept(false);
                            followupWorkArrayList.get(position).setIsFollowUpAcceptedorNot(2);
                        }
                    }
                });

            }
        }

        @Override
        public int getItemCount() {
            return followupWorkArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txtKey, txtValue, txtAcceptOrReject;
            RadioGroup acceptOrReject;
            RadioButton radioAccept, radioReject;

            public ViewHolder(View itemView) {
                super(itemView);
                txtKey = itemView.findViewById(R.id.txtKey);
                txtValue = itemView.findViewById(R.id.txtValue);
                acceptOrReject = itemView.findViewById(R.id.acceptOrReject);
                txtAcceptOrReject = itemView.findViewById(R.id.txtAcceptOrReject);
                radioAccept = itemView.findViewById(R.id.radioAccept);
                radioReject = itemView.findViewById(R.id.radioReject);
            }
        }
    }

}
