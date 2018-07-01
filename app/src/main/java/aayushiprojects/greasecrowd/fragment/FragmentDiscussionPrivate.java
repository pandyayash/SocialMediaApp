package aayushiprojects.greasecrowd.fragment;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.DiscussionActivity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentDiscussionPrivate extends Fragment implements View.OnClickListener {

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
    SwipeRefreshLayout pull_to_refresh;

    public FragmentDiscussionPrivate() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fragment_discussion_public, null, false);
        getViews(view);

        setOnclickListener();
        return view;
    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("BroadcastReceiver", "onReceive");
            if (intent != null) {

                String text = intent.getStringExtra("text");
                String object_type = intent.getStringExtra("object_type");
                String object_id = intent.getStringExtra("object_id");
                String img = intent.getStringExtra("img");
                String name = intent.getStringExtra("name");
                DiscussionDbo discussionDbo = new DiscussionDbo();
                discussionDbo.setMessageText(((DiscussionActivity) appContext).DecodeBase64(text));
                discussionDbo.setObjectId(object_id);
                discussionDbo.setObjectType(object_type);
                discussionDbo.setJobId(((DiscussionActivity) appContext).discusionfourjobid);
                discussionDbo.setAvatarImage(img);
                discussionDbo.setName(name);
                discussionDbo.setDateTime(setCurrentDate());
                setDataFromLocal(discussionDbo);
            }
        }
    };

    public void getViews(View rootView) {
        appContext = getActivity();
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        discussionDboArrayList = new ArrayList<>();
        rv_chat = rootView.findViewById(R.id.rv_chat);
        et_message = rootView.findViewById(R.id.et_message);
        iv_send = rootView.findViewById(R.id.iv_send);
        textView = rootView.findViewById(R.id.textview);
        typeMsg = rootView.findViewById(R.id.typeMsg);
        pull_to_refresh = rootView.findViewById(R.id.pull_to_refresh);
        typeMsg.getBackground().setLevel(3);

        if (Connectivity.isConnected(appContext)) {
            typeMsg.setVisibility(View.VISIBLE);
            getHistoryMessage();
        } else {
            ((DiscussionActivity) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));

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
                    typeMsg.setVisibility(View.VISIBLE);
                    getHistoryMessage();
                } else {
                    ((DiscussionActivity) appContext)
                            .showAlertDialog(getResources().getString(R.string.no_internet));

                }
                pull_to_refresh.setRefreshing(false);
            }
        });
        IntentFilter filter2 = new IntentFilter("OnMessageRecieved");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver, filter2);
    }

    @Override
    public void onResume() {
        super.onResume();
        myPreference.saveBooleanReponse(Constants.NotificationTags.CHAT_NOTI, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        myPreference.saveBooleanReponse(Constants.NotificationTags.CHAT_NOTI, false);

    }

    public void setOnclickListener() {
        iv_send.setOnClickListener(this);
    }

    public void getHistoryMessage() {
        ((DiscussionActivity) appContext).showLoadingDialogFrag(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.PRIVATE_INBOX_HISTORY);
        params.put("job_id", ((DiscussionActivity) appContext).discusionfourjobid);
        // params.put("job_id", "2691");
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("page_number", String.valueOf(currentPage));
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
                                JSONObject newjsonArray = response.getJSONObject("data");
                                JSONArray newjsonArrayForUserID = newjsonArray.getJSONArray("job_user_id_info");
                                JSONArray newjsonArrayForGarageID = newjsonArray.getJSONArray("job_garage_id_info");


                                JSONObject UserObj = newjsonArrayForUserID.getJSONObject(0);
                                JSONObject GarageObj = newjsonArrayForGarageID.getJSONObject(0);


                                JSONArray newjsonArrayForChat = newjsonArray.getJSONArray("chat_messages");

                                final String next = response.getString("pagination_flag");

                                if (newjsonArrayForChat.length() > 0) {
                                    for (int i = 0; i < newjsonArrayForChat.length(); i++) {
                                        JSONObject object = newjsonArrayForChat.getJSONObject(i);
                                        DiscussionDbo discussionDbo = new DiscussionDbo();
                                        discussionDbo.setId(object.getString("id"));
                                        discussionDbo.setMessageText(((DiscussionActivity) appContext).DecodeBase64(object.getString("message_text")));
                                        discussionDbo.setObjectId(object.getString("object_id"));
                                        discussionDbo.setObjectType(object.getString("object_type"));
                                        discussionDbo.setJobId(object.getString("job_id"));
                                        discussionDbo.setDateTime(object.optString("entrydate"));
                                        if (UserObj.getString("id").equalsIgnoreCase(object.getString("object_id"))) {
                                            discussionDbo.setAvatarImage(UserObj.getString("image"));
                                            discussionDbo.setName(UserObj.getString("fname"));
                                        } else if (GarageObj.getString("id").equalsIgnoreCase(object.getString("object_id"))) {
                                            discussionDbo.setAvatarImage(GarageObj.getString("logo_image"));
                                            discussionDbo.setName(GarageObj.getString("fname"));
                                        }

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
                                    pull_to_refresh.setVisibility(View.GONE);
                                }

                            } else {
                                ((DiscussionActivity) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((DiscussionActivity) appContext).showLoadingDialogFrag(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((DiscussionActivity) appContext).showLoadingDialogFrag(false);
                    }
                });
        queue.add(jsonObjReq);
    }

    public void SendMessage(final String msg) {
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOBMESSAGES);
        params.put("job_id", ((DiscussionActivity) appContext).discusionfourjobid);
        //params.put("job_id", "2763");
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("chat_type", "private inbox");
        params.put("msg", ((DiscussionActivity) appContext).EncodeBAse64(msg).trim());
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
                                discussionDbo.setJobId(((DiscussionActivity) appContext).discusionfourjobid);
                                discussionDbo.setAvatarImage(loginDetail_dbo.getImage());
                                discussionDbo.setName(loginDetail_dbo.getFirst_name());
                                discussionDbo.setDateTime(setCurrentDate());
                                setDataFromLocal(discussionDbo);
                            } else {
                                ((DiscussionActivity) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        isFirstTime = true;

                        ((DiscussionActivity) appContext).showLoadingDialogFrag(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isFirstTime = true;
                        ((DiscussionActivity) appContext).showLoadingDialogFrag(false);
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
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

    public void setData() {


        if (discussionDboArrayList.size() > 0) {
            pull_to_refresh.setVisibility(View.VISIBLE);
            textView.setVisibility(View.GONE);
            adptDiscussion = new AdptDiscussion(discussionDboArrayList, appContext);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            mLayoutManager.setReverseLayout(true);
            rv_chat.setLayoutManager(mLayoutManager);
            rv_chat.setAdapter(adptDiscussion);
        } else {
            textView.setVisibility(View.VISIBLE);
            pull_to_refresh.setVisibility(View.GONE);
        }
    }

    private String setCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate3 = df3.format(c.getTime());
        return formattedDate3;
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
        }
    }
}
