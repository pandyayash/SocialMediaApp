package technource.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import technource.greasecrowd.adapter.AdptMyCrowdList;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.MyCrowd_DBO;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

public class MyCrowdUserActivity extends BaseActivity {
    public static final int REQUEST_CODE = 1;
    String TAG = "MyCrowdUserActivity";
    LinearLayout ll_back;
    TextView btnSearch, btnSort, btnAskCrowd;
    RecyclerView crowd_recyclerview;
    ArrayList<MyCrowd_DBO> crowdArrayList;
    ArrayList<String> sortByArrayList;
    LoginDetail_DBO loginDetail_dbo;
    MyPreference myPreference;
    Context appContext;
    TextView txtNoDataFound;
    AdptMyCrowdList adptMyCrowdList;
    int currentPage = 1;
    public static String strKeyword = "", strCatId = "", strMakeId = "", strModelId = "", strSortby = "";
    public static int selectedCat=0;
    public static int selectedMake=0;
    public static int selectedModel=0;
    private boolean isLoadMore = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crowd);
        getViews();

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            setHeader("My Crowd");
            setfooter("crowd");
            setlistenrforfooter();
        } else {
            setHeader("My Crowd");
            setfooter("garageowner");
            setlistenrforfooter();
        }

        setOnClickListners();
        if (Connectivity.isConnected(appContext)) {
            getMyCrowdList();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }


        crowd_recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {
                        getMyCrowdList();
                    } else {
                        showAlertDialog(getString(R.string.no_internet));
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        setAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            setMyCrowdFooter(this);
        } else {
            setMyCrowdFooterGarage(this);
        }

    }


    public void getViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        myPreference = new MyPreference(appContext);
        crowdArrayList = new ArrayList<>();
        sortByArrayList = new ArrayList<>();
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        btnSort = (TextView) findViewById(R.id.btnSort);
        btnAskCrowd = (TextView) findViewById(R.id.btnAskCrowd);
        txtNoDataFound = (TextView) findViewById(R.id.txtNoDataFound);
        crowd_recyclerview = (RecyclerView) findViewById(R.id.crowd_recyclerview);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);


        sortByArrayList.add("Date");
        sortByArrayList.add("Distance");
        sortByArrayList.add("No response");

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            btnAskCrowd.setVisibility(View.GONE);

        } else {
            btnAskCrowd.setVisibility(View.GONE);

        }
    }

    public void setOnClickListners() {
        btnSort.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == ll_back) {
            onBackPressed();
        }
        if (view == btnSort) {
            //Toast.makeText(this, "click"+btnSort.getText(), Toast.LENGTH_SHORT).show();
            openPopup();
        }
        if (view == btnSearch) {
            //Toast.makeText(this, "click"+btnSearch.getText(), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MyCrowdUserActivity.this, SearchCrowdActivity.class);
            startActivityForResult(intent, REQUEST_CODE);
            activityTransition();
        }
    }

    public void setAdapter() {
        if (crowdArrayList.size() > 0) {
            txtNoDataFound.setVisibility(View.GONE);
            crowd_recyclerview.setVisibility(View.VISIBLE);
            adptMyCrowdList = new AdptMyCrowdList(crowdArrayList, appContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            crowd_recyclerview.setLayoutManager(mLayoutManager);
            crowd_recyclerview.setItemAnimator(new DefaultItemAnimator());
            crowd_recyclerview.setAdapter(adptMyCrowdList);
         /*   float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            crowd_recyclerview.addItemDecoration(bottomOffsetDecoration);*/
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            crowd_recyclerview.setVisibility(View.GONE);
        }

    }

    /*-----------Web call for get crowd list------------- */
    public void getMyCrowdList() {
        //crowdArrayList = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SEARCH_CROWD);
        params.put(Constants.SEARCH_CROWD.PAGE_NUMBER, String.valueOf(currentPage));
        params.put(Constants.SEARCH_CROWD.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.SEARCH_CROWD.KEYWORD, strKeyword);
        params.put(Constants.SEARCH_CROWD.CATEGORY, strCatId);
        params.put(Constants.SEARCH_CROWD.MAKE, strMakeId);
        params.put(Constants.SEARCH_CROWD.MODEL, strModelId);
        params.put(Constants.SEARCH_CROWD.SORT_BY, strSortby);
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
                                final String next = response.getString("next");
                                if (!next.equalsIgnoreCase("false")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONArray newjsonArray = response.getJSONArray("data");
                                if (newjsonArray.length() > 0) {
                                    for (int i = 0; i < newjsonArray.length(); i++) {
                                        JSONObject object = newjsonArray.getJSONObject(i);
                                        MyCrowd_DBO myCrowd_dbo = new MyCrowd_DBO();
                                        myCrowd_dbo.setCjob_id(object.getString("cjob_id"));
                                        myCrowd_dbo.setJu_id(object.getString("ju_id"));
                                        myCrowd_dbo.setJob_title(object.getString("job_title"));
                                        myCrowd_dbo.setProblem_description(object.getString("problem_description"));
                                        myCrowd_dbo.setCatname(object.getString("catname"));
                                        myCrowd_dbo.setCategory_id(object.getString("category_id"));
                                        myCrowd_dbo.setMake(object.getString("make"));
                                        myCrowd_dbo.setModel(object.getString("model"));
                                        myCrowd_dbo.setBadge(object.getString("badge"));
                                        myCrowd_dbo.setYear(object.getString("year"));
                                        myCrowd_dbo.setJob_posted_date(object.getString("job_posted_date"));
                                        myCrowd_dbo.setDistance(object.getString("distance"));
                                        myCrowd_dbo.setFname(object.getString("fname"));
                                        myCrowd_dbo.setLname(object.getString("lname"));
                                        myCrowd_dbo.setSuburb(object.getString("suburb"));
                                        JSONArray jsonArray = object.getJSONArray("responses");
                                        if (jsonArray.length() > 0) {
                                            for (int j = 0; j < jsonArray.length(); j++) {
                                                JSONObject jsonObject = jsonArray.getJSONObject(j);
                                                myCrowd_dbo.setRespinses(jsonObject.getString("res_count"));
                                            }
                                        }
                                        crowdArrayList.add(myCrowd_dbo);
                                    }
                                    setAdapter();
                                } else {
                                    txtNoDataFound.setVisibility(View.VISIBLE);
                                    crowd_recyclerview.setVisibility(View.GONE);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);

            if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
                crowdArrayList = new ArrayList<>();
                strKeyword = data.getStringExtra("keyword");
                strCatId = data.getStringExtra("cat_id");
                strMakeId = data.getStringExtra("car_make_id");
                strModelId = data.getStringExtra("car_model_id");
                currentPage = 1;
                if (Connectivity.isConnected(appContext)) {
                    getMyCrowdList();
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }
        } catch (Exception ex) {
            Toast.makeText(appContext, ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private void openPopup() {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Sort By");
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ListView lv = (ListView) view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strSortby = sortByArrayList.get(i);
                currentPage = 1;
                if (Connectivity.isConnected(appContext)) {
                    crowdArrayList = new ArrayList<>();
                    getMyCrowdList();
                } else {
                    showAlertDialog(getString(R.string.no_internet));
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
