package technource.greasecrowd.CarOwnerFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.CustomViews.BottomOffsetDecoration;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.MyJobsUserActivity;
import technource.greasecrowd.adapter.AdptMyGarages;
import technource.greasecrowd.adapter.AdptNewPostedJobs;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.NewPostedJob_DBO;

/**
 * Created by technource on 13/9/17.
 */

public class MyGaragesCarOwner extends Fragment {

    View v;
    public String TAG = "FragmentMyJobsNewPosted";
    Activity appContext;
    RecyclerView recyclerView;
    TextView textView;
    AdptMyGarages adptNewPostedJobs;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    //ArrayList<NewPostedJob_DBO> postedJobDboArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_carowner_garage, container, false);

        getViews(v);
        return v;
    }


    public static MyGaragesCarOwner newInstance() {
        MyGaragesCarOwner fragment = new MyGaragesCarOwner();
        return fragment;
    }


    public void getViews(View rootView) {
        appContext = getActivity();
        awardJobArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
        textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText("You don't have any garages.");
        textView.setVisibility(View.GONE);
        if (Connectivity.isConnected(appContext)) {
            //getNewPostedJobData();
            getMyGarages();
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }


    }

    public void setData() {

        if (awardJobArrayList.size() > 0) {
        textView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        adptNewPostedJobs = new AdptMyGarages(appContext,awardJobArrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptNewPostedJobs);
        float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
        BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
        recyclerView.addItemDecoration(bottomOffsetDecoration);
    } else {
      textView.setVisibility(View.VISIBLE);
      recyclerView.setVisibility(View.GONE);
    }

    }

    public void getMyGarages() {

        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.MYGARAGES);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            JSONObject jsonObject = response.optJSONObject("data");
                            JSONArray jsonArray = jsonObject.getJSONArray("mygarages");
                            if (jsonArray.length()>0){
                                for (int i = 0; i <jsonArray.length() ; i++) {
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    AwardJobDBOCarOwner awardJobDBOCarOwner = new AwardJobDBOCarOwner();
                                    awardJobDBOCarOwner.setId(object.optString("id"));
                                    awardJobDBOCarOwner.setName(object.optString("business_name"));
                                    awardJobDBOCarOwner.setAvatar_img(object.optString("logo_image"));
                                    awardJobDBOCarOwner.setReview_count(object.optString("garage_reviews"));
                                    awardJobDBOCarOwner.setRating(object.getInt("avg_rating"));
                                    awardJobDBOCarOwner.setDistance(String.format("%.0f", Float.parseFloat(object.optString("distance"))));
                                    awardJobArrayList.add(awardJobDBOCarOwner);
                                }

                            }
                            setData();


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }

}
