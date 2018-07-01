package aayushiprojects.greasecrowd.activities;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptDiscussion;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.MyPreference;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.DiscussionDbo;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class CrowdDiscussionBoardActivity extends BaseActivity {

    public String TAG = "FragmentMyJobsQuoted";
    Activity appContext;
    RecyclerView rv_chat;
    EditText et_message;
    LinearLayout typeMsg;
    AdptDiscussion adptDiscussion;
    ImageView iv_send;
    ArrayList<DiscussionDbo> discussionDboArrayList;
    LoginDetail_DBO loginDetail_dbo;
    int currentPage = 1;
    TextView textView;
    private boolean isLoadMore = false;
    MyPreference myPreference;
    boolean isFirstTime = true;
    String juId = "", cjobId = "";
    LinearLayout ll_back;
    SwipeRefreshLayout pull_to_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crowd_discussion_board);

        setHeader("Crowd Discussion Board");
        getData();
        getViews();
        setOnclickListener();
    }

    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {

            juId = intent.getStringExtra("juId");
            cjobId = intent.getStringExtra("cjobId");
        }
    }

    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        discussionDboArrayList = new ArrayList<>();
        rv_chat = findViewById(R.id.rv_chat);
        et_message = findViewById(R.id.et_message);
        iv_send = findViewById(R.id.iv_send);
        textView = findViewById(R.id.textview);
        typeMsg = findViewById(R.id.typeMsg);
        typeMsg.getBackground().setLevel(3);
        ll_back = findViewById(R.id.ll_back);
        pull_to_refresh = findViewById(R.id.pull_to_refresh);
        if (Connectivity.isConnected(appContext)) {
            getHistoryMessage();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }

        rv_chat.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(-1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {
                        getHistoryMessage();
                    } else {
                        ((DiscussionActivity) appContext).showAlertDialog(getString(R.string.no_internet));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Connectivity.isConnected(appContext)) {
                    discussionDboArrayList = new ArrayList<>();
                    currentPage = 1;
                    getHistoryMessage();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                pull_to_refresh.setRefreshing(false);

            }
        });
    }

    public void setOnclickListener() {
        iv_send.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    public void setData() {
        if (discussionDboArrayList.size() > 0) {
            rv_chat.setVisibility(View.VISIBLE);
            pull_to_refresh.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adptDiscussion = new AdptDiscussion(discussionDboArrayList, appContext);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            mLayoutManager.setReverseLayout(true);
            rv_chat.setLayoutManager(mLayoutManager);
            rv_chat.setAdapter(adptDiscussion);
        } else {
            textView.setVisibility(View.VISIBLE);
            rv_chat.setVisibility(View.GONE);
            pull_to_refresh.setVisibility(View.GONE);
        }
    }

    public void getHistoryMessage() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.ASKCROWD_MESSAGE_HISTORY);
        params.put("ju_id", juId);
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("page_number", String.valueOf(currentPage));
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
                                JSONArray newjsonArray = response.getJSONArray("data");

                                final String next = response.getString("pagination_flag");

                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
                                        DiscussionDbo discussionDbo = new DiscussionDbo();
                                        discussionDbo.setId(object.getString("id"));
                                        discussionDbo.setMessageText(DecodeBase64(object.getString("message_text")));
                                        discussionDbo.setObjectId(object.getString("object_id"));
                                        discussionDbo.setObjectType(object.getString("object_type"));
                                        discussionDbo.setJobId(object.getString("job_id"));
                                        discussionDbo.setAvatarImage(object.getString("avatar_img"));
                                        discussionDbo.setName(object.getString("name"));
                                        discussionDbo.setDateTime(object.optString("text_time"));
                                        discussionDboArrayList.add(discussionDbo);

                                    }
                                    if (isLoadMore) {
                                        adptDiscussion.notifyDataSetChanged();
                                        rv_chat.smoothScrollBy(0, -60);
                                    } else {
                                        setData();
                                    }

                                    if (next.equalsIgnoreCase("yes")) {
                                        isLoadMore = true;
                                        currentPage += 1;
                                    } else {
                                        isLoadMore = false;
                                    }

                                } else {
                                    textView.setVisibility(View.VISIBLE);
                                    rv_chat.setVisibility(View.GONE);
                                }

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_send:
                if (isFirstTime) {
                    if (!et_message.getText().toString().trim().equalsIgnoreCase("")) {
                        if (Connectivity.isConnected(appContext)) {
                            isFirstTime = false;
                            SendMessage(et_message.getText().toString());
                        } else {
                            ((DiscussionActivity) appContext).showAlertDialog(getString(R.string.no_internet));
                        }
                    } else {
                        Toast.makeText(appContext, "You cannot send blank message.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void setDataFromLocal(DiscussionDbo discussionDbo) {
        if (discussionDboArrayList.size() > 0) {
            Collections.reverse(discussionDboArrayList);
            discussionDboArrayList.add(discussionDbo);
            Collections.reverse(discussionDboArrayList);
            adptDiscussion.notifyDataSetChanged();
            et_message.setText("");
            rv_chat.scrollToPosition(0);
        } else {
            discussionDboArrayList.add(discussionDbo);
            setData();
            et_message.setText("");
        }
    }


    public void SendMessage(final String msg) {
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.POSTMESSAGES);
        params.put("job_id", cjobId);
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("chat_type", "discussion");
        params.put("msg", EncodeBAse64(msg));
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

                                DiscussionDbo discussionDbo = new DiscussionDbo();
                                discussionDbo.setMessageText(msg);
                                discussionDbo.setObjectId(loginDetail_dbo.getUserid());
                                if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                                    discussionDbo.setObjectType("G");
                                } else {
                                    discussionDbo.setObjectType("U");
                                }
                                discussionDbo.setJobId(cjobId);
                                discussionDbo.setAvatarImage(loginDetail_dbo.getImage());
                                discussionDbo.setName(loginDetail_dbo.getFirst_name());
                                setDataFromLocal(discussionDbo);
                            } else {
                                ((DiscussionActivity) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        isFirstTime = true;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isFirstTime = true;
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }
}
