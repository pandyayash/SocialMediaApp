package technource.greasecrowd.GarageOwnerFragments;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.SearchGarageActivity;
import technource.greasecrowd.adapter.AdptNewPostedJobs;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.NewPostedJob_DBO;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by technource on 13/9/17.
 */

public class HomeGarageOwner extends Fragment implements View.OnClickListener {

    public static final int REQUEST_CODE = 222;
    String TAG = "MyGarageHOmeCarOwner";
    Context appContext;
    View v;
    LoginDetail_DBO loginDetail_dbo;
    TextView btnSearch, btnSort;
    RecyclerView crowd_recyclerview;
    TextView txtNoDataFound;
    int currentPage = 1;
    LinearLayout ll_back;
    ArrayList<NewPostedJob_DBO> postedJobDboArrayList;
    ArrayList<String> sortByArrayList = new ArrayList<>();
    AdptNewPostedJobs adptNewPostedJobs;
    String strKeyword = "", strCatId = "", strMakeId = "", strModelId = "", strsubCatId = "", strKm = "", strSortby = "";
    private boolean isLoadMore = false;
    private boolean isFromSarch = false;

    public static HomeGarageOwner newInstance() {
        HomeGarageOwner fragment = new HomeGarageOwner();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_home, container, false);
        getViews();
        setClickListener();
        ((DashboardScreen) appContext).setFooterGarage();
        if (Connectivity.isConnected(appContext)) {
            getAllJobDetails();
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        ((DashboardScreen) appContext).setFooterGarage();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                postedJobDboArrayList = new ArrayList<>();
                strKeyword = data.getStringExtra("keyword");
                strCatId = data.getStringExtra("cat_id");
                strMakeId = data.getStringExtra("car_make_id");
                strModelId = data.getStringExtra("car_model_id");
                strsubCatId = data.getStringExtra("subcat_id");
                strKm = data.getStringExtra("km");
                currentPage = 1;
                if (Connectivity.isConnected(appContext)) {
                    postedJobDboArrayList = new ArrayList<>();
                    getFilteredJob();
                } else {
                    ((DashboardScreen) appContext)
                            .showAlertDialog(getString(R.string.no_internet));
                }
            } else if (resultCode == RESULT_CANCELED) {
                if (Connectivity.isConnected(appContext)) {
                    strKeyword = "";
                    strCatId = "";
                    strMakeId = "";
                    strModelId = "";
                    strsubCatId = "";
                    strKm = "";
                    currentPage = 1;
                    strSortby = "";
                    isFromSarch = false;
                    getAllJobDetails();
                } else {
                    ((DashboardScreen) appContext)
                            .showAlertDialog(getString(R.string.no_internet));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(appContext, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void getViews() {
        appContext = getActivity();
        postedJobDboArrayList = new ArrayList<>();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        btnSearch = (TextView) v.findViewById(R.id.btnSearch);
        btnSort = (TextView) v.findViewById(R.id.btnSort);
        txtNoDataFound = (TextView) v.findViewById(R.id.txtNoDataFound);
        crowd_recyclerview = (RecyclerView) v.findViewById(R.id.crowd_recyclerview);
        ll_back = (LinearLayout) v.findViewById(R.id.ll_back);

        crowd_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {
                        if (isFromSarch) {
                            getFilteredJob();
                        } else {
                            getAllJobDetails();
                        }
                    } else {
                        ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        sortByArrayList.add("Date");
        sortByArrayList.add("Distance");
        sortByArrayList.add("Not quoted");
    }

    public void setAdapter() {
        if (postedJobDboArrayList.size() > 0) {
            txtNoDataFound.setVisibility(View.GONE);
            crowd_recyclerview.setVisibility(View.VISIBLE);
            adptNewPostedJobs = new AdptNewPostedJobs(postedJobDboArrayList, appContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            crowd_recyclerview.setLayoutManager(mLayoutManager);
            crowd_recyclerview.setItemAnimator(new DefaultItemAnimator());
            crowd_recyclerview.setAdapter(adptNewPostedJobs);
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            txtNoDataFound.setVisibility(View.GONE);
        }
    }

    public void setClickListener() {
        btnSort.setOnClickListener(this);
        btnSearch.setOnClickListener(this);

    }

    private void openPopup() {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = ((DashboardScreen) appContext).getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Sort By");
        ((DashboardScreen) appContext).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ListView lv = (ListView) view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strSortby = sortByArrayList.get(i).toLowerCase();
                currentPage = 1;
                if (Connectivity.isConnected(appContext)) {
                    postedJobDboArrayList = new ArrayList<>();
                    getFilteredJob();
                } else {
                    ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));
                }
                dialog.dismiss();
            }
        });
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 50;  //Set your heights
//        int height = (int) (size.y / 1.4);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = width;
//        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;


        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, sortByArrayList);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        //       dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void getFilteredJob() {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_FILTER);
        params.put(Constants.HOMEGARAGEOWNER.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.HOMEGARAGEOWNER.KEYWORD, strKeyword);
        params.put(Constants.HOMEGARAGEOWNER.CAT, strCatId);
        params.put(Constants.HOMEGARAGEOWNER.RECORDS, "20");
        params.put(Constants.HOMEGARAGEOWNER.DISTANCE, strKm);
        params.put(Constants.HOMEGARAGEOWNER.SUBCAT, strsubCatId);
        params.put(Constants.HOMEGARAGEOWNER.SORTBY, strSortby);
        params.put(Constants.HOMEGARAGEOWNER.MAKE, strMakeId);
        params.put(Constants.HOMEGARAGEOWNER.MODEL, strModelId);
        params.put(Constants.HOMEGARAGEOWNER.PAGE_NUMBER, String.valueOf(currentPage));
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In MyCrowd --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONObject jobj = response.getJSONObject("data");

                                final String next = jobj.getString("next");
                                AppLog.Log(TAG, next);
                                if (next.equalsIgnoreCase("yes")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONArray newjsonArray = jobj.getJSONArray("job_detail");
                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setCarModel(object.getString("model"));
                                        newPostedJob_dbo.setMake(object.getString("make"));
                                        newPostedJob_dbo.setBadge(object.getString("badge"));
                                        newPostedJob_dbo.setYear(object.getString("year"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setPrice(object.getString("average_bid"));
                                        newPostedJob_dbo.setTotalQuotes(object.getInt("bid_count"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setDistnace(object.optString("distance"));
                                        newPostedJob_dbo.setFromJobsFeed(true);
                                        postedJobDboArrayList.add(newPostedJob_dbo);
                                    }
                                    setAdapter();
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    crowd_recyclerview.setVisibility(View.GONE);
                                }

                            } else {
                                ((DashboardScreen) appContext).showAlertDialog(response.getString(Constants.MESSAGE));
                            }
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }

    public void getAllJobDetails() {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GETALLJOBSDETAILS);
        params.put(Constants.HOMEGARAGEOWNER.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.HOMEGARAGEOWNER.GARANGE_ID, loginDetail_dbo.getUserid());
        params.put(Constants.HOMEGARAGEOWNER.PAGE_NUMBER, String.valueOf(currentPage));
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In MyCrowd --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONObject jobj = response.getJSONObject("data");

                                final String next = jobj.getString("pagination_flag");
                                AppLog.Log(TAG, next);
                                if (next.equalsIgnoreCase("yes")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONArray newjsonArray = jobj.getJSONArray("job_detail");
                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
                                        NewPostedJob_DBO newPostedJob_dbo = new NewPostedJob_DBO();
                                        newPostedJob_dbo.setJobID(object.getString("ju_id"));
                                        newPostedJob_dbo.setCategory_id(object.getString("category_id"));
                                        newPostedJob_dbo.setJobTitle(object.getString("job_title"));
                                        newPostedJob_dbo.setCarModel(object.getString("model"));
                                        newPostedJob_dbo.setMake(object.getString("make"));
                                        newPostedJob_dbo.setBadge(object.getString("badge"));
                                        newPostedJob_dbo.setYear(object.getString("year"));
                                        newPostedJob_dbo.setDate(object.getString("job_posted_date"));
                                        newPostedJob_dbo.setPrice(object.getString("average_bid"));
                                        newPostedJob_dbo.setTotalQuotes(object.getInt("bid_count"));
                                        newPostedJob_dbo.setJobDescription(object.getString("problem_description"));
                                        newPostedJob_dbo.setJob_status(object.getString("job_status"));
                                        newPostedJob_dbo.setDistnace(object.optString("distance"));
                                        newPostedJob_dbo.setFname(object.optString("fname"));
                                        newPostedJob_dbo.setLname(object.optString("lname"));
                                        newPostedJob_dbo.setSuburb(object.optString("suburb"));
                                        newPostedJob_dbo.setFromJobsFeed(true);
                                        postedJobDboArrayList.add(newPostedJob_dbo);
                                    }
                                    setAdapter();
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    crowd_recyclerview.setVisibility(View.GONE);
                                }

                            } else {
                                ((DashboardScreen) appContext).showAlertDialog(response.getString(Constants.MESSAGE));
                            }
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSearch:
                Intent intent = new Intent(appContext, SearchGarageActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.btnSort:
                isFromSarch = true;
                openPopup();
                break;
        }
    }

    public class CustomListAdapterOther extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<String> listData;

        public CustomListAdapterOther(Context appContext, ArrayList<String> list) {
            context = appContext;
            layoutInflater = LayoutInflater.from(appContext);
            listData = list;
        }

        @Override
        public int getCount() {
            return listData.size();
        }

        @Override
        public Object getItem(int position) {
            return listData.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            final ChallengerHolder holder;

            String strSortBy = sortByArrayList.get(position);
            if (row == null) {
                holder = new ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
                holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
                row.setTag(holder);
            } else {
                holder = (ChallengerHolder) row.getTag();
            }

            holder.tv_name.setText(strSortBy);
            return row;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ChallengerHolder {
            TextView tv_name;
        }
    }
}
