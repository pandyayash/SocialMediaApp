package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.RegisteredCarDBO;
import aayushiprojects.greasecrowd.model.ViewHistoryDBO;

public class ViewHistoryGarageActivity extends BaseActivity {

    RegisteredCarDBO registeredCarDBO;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    Context appContext;
    LinearLayout ll_back;
    ArrayList<ViewHistoryDBO> viewHistoryDBOArrayList;
    String cartype;
    TextView nohistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_history_garage);

        setHeader("History");
        getviews();
        setfooter("jobs");
        setlistenrforfooter();
        setHomeFooter(appContext);
        Intent intent = getIntent();
        if (intent != null) {
            registeredCarDBO = intent.getParcelableExtra("registeredcar");
        } else {

        }
        setonclick();
        getdetails();
    }


    private void getviews() {
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        appContext = this;
        ll_back = findViewById(R.id.ll_back);
        viewHistoryDBOArrayList = new ArrayList<>();
        nohistory = findViewById(R.id.nohistory);

    }

    private void setonclick() {
        ll_back.setOnClickListener(this);

    }

    private void getdetails() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "car_history");
        params.put("car_id", String.valueOf(registeredCarDBO.getId()));

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", "car_history ---> "+response.toString());
                        try {

                            if (true) {
                                JSONArray json = response.getJSONArray("jobs");

                                if (json.length() <= 0) {
                                    nohistory.setVisibility(View.VISIBLE);
                                    recyclerView.setVisibility(View.GONE);

                                } else if (json.length() > 0) {
                                    nohistory.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    cartype = response.getString("car_type");
//                                for (int j = 0; j <5 ; j++) {
                                    for (int i = 0; i < json.length(); i++) {
                                        ViewHistoryDBO facility = new ViewHistoryDBO();
                                        JSONObject obj = json.getJSONObject(i);
                                        facility.setJob_title(obj.getString("job_title"));
                                        facility.setJob_posted_date(obj.getString("job_posted_date"));
                                        facility.setProblem_description(obj.getString("problem_description"));
                                        facility.setGarage_name(obj.getString("garage_name"));
                                        facility.setRating_stars(obj.getString("rating_stars"));
                                        viewHistoryDBOArrayList.add(facility);
                                    }
                                    setAdapter();
                                    //  }

                                }


                            } else {
                                showAlertDialog("some error occured please try again later");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });

        queue.add(jsonObjReq);
    }

    private void setAdapter() {
        adapter = new Recycleradapter(viewHistoryDBOArrayList, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Recycleviewholder> {


        Context context;
        ArrayList<ViewHistoryDBO> services;

        public Recycleradapter(ArrayList<ViewHistoryDBO> services, Context context) {

            this.context = context;
            this.services = services;
        }


        @Override
        public Recycleradapter.Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_carowner_view_history, parent, false);
            Recycleviewholder recycleviewholder = new Recycleradapter.Recycleviewholder(view);
            return recycleviewholder;
        }

        @Override
        public void onBindViewHolder(final Recycleradapter.Recycleviewholder holder, final int position) {


            holder.jobtitle.setText(services.get(position).getJob_title());
            holder.dateandtime.setText(services.get(position).getJob_posted_date());
            holder.description.setText(services.get(position).getProblem_description());
            holder.by.append(services.get(position).getGarage_name());
            holder.ratingbarvalue.setText(services.get(position).getRating_stars());
            holder.ratingBar.setRating(Float.parseFloat(services.get(position).getRating_stars()));
            String typecar = "<font color='#8e8e8e'>" + cartype + "</font>";
            //t.setText(Html.fromHtml(first + next));
            holder.type.append(Html.fromHtml(typecar));

            holder.jobtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(ViewHistoryGarageActivity.this, CarDetailsActivity.class);
                    intent.putExtra("viewHistoryDBO", services.get(position));
                    startActivity(intent);
                    activityTransition();

                }
            });

        }


        @Override
        public int getItemCount() {
            return viewHistoryDBOArrayList.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;

            TextView jobtitle, dateandtime, description, by, ratingbarvalue, type;
            SimpleRatingBar ratingBar;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                jobtitle = itemView.findViewById(R.id.jobtitle);
                dateandtime = itemView.findViewById(R.id.dateandtime);
                description = itemView.findViewById(R.id.description);
                by = itemView.findViewById(R.id.by);
                ratingbarvalue = itemView.findViewById(R.id.ratingbarvalue);
                ratingBar = itemView.findViewById(R.id.ratingBar);
                type = itemView.findViewById(R.id.type);

            }
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }


}
