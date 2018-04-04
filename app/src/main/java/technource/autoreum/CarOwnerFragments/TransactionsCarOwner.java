package technource.autoreum.CarOwnerFragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.CustomViews.BottomOffsetDecoration;
import technource.autoreum.R;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.adapter.AdptTransactionHistory;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Transaction_DBO;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;

/**
 * Created by technource on 13/9/17.
 */

public class TransactionsCarOwner extends Fragment {
    String TAG = "TransactionsCarOwner";
    ArrayList<Transaction_DBO> transactionArrayList;
    AdptTransactionHistory adptTransactionHistory;
    View v;
    Activity appContext;
    LoginDetail_DBO loginDetail_dbo;
    TextView txtNoDataFound;
    RecyclerView recyclerview;
    Spinner sp_month,sp_year;
    ArrayList<String> monthArrayList;
    ArrayList<String> yearArrayList;
    String months[];
    ArrayAdapter<String> arraymonthAdapter;
    ArrayAdapter<String> arrayYearAdapter;
    String strMonth="";
    String strYear="";
    SwipeRefreshLayout pull_to_refresh;
    int currentPage = 1;
    private boolean isLoadMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_carowner_transaction, container, false);
        getViews();
        setMonths();
        setYear();
        ((DashboardScreen)appContext).NoFooter();
        if (Connectivity.isConnected(appContext)) {

            getTransactionHistory();
        } else {
            ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));

        }

        recyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState == SCROLL_STATE_IDLE && isLoadMore) {
                    if (Connectivity.isConnected(appContext)) {

                        getTransactionHistory();
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


        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Connectivity.isConnected(appContext)) {
                    transactionArrayList= new ArrayList<>();
                    currentPage = 1;
                    getTransactionHistory();
                } else {
                    ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));

                }
                pull_to_refresh.setRefreshing(false);
            }
        });


        return v;
    }

    public static TransactionsCarOwner newInstance() {
        TransactionsCarOwner fragment = new TransactionsCarOwner();
        return fragment;
    }

    public void getViews() {
        appContext = getActivity();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        transactionArrayList = new ArrayList<>();
        monthArrayList = new ArrayList<>();
        yearArrayList = new ArrayList<>();

        txtNoDataFound = (TextView)v.findViewById(R.id.txtNoDataFound);
        recyclerview = (RecyclerView) v.findViewById(R.id.recyclerview);
        pull_to_refresh = (SwipeRefreshLayout)v.findViewById(R.id.pull_to_refresh);
        sp_month = (Spinner) v.findViewById(R.id.sp_month);
        sp_year = (Spinner) v.findViewById(R.id.sp_year);
        months = appContext.getResources().getStringArray(R.array.months_array);


    }

    public void getTransactionHistory() {
        transactionArrayList = new ArrayList<>();
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.TRANSACTION_HISTORY);
        params.put(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.TRANSACTION_HISTORY.MONTH, strMonth);
        params.put(Constants.TRANSACTION_HISTORY.YEAR, strYear);
        params.put(Constants.TRANSACTION_HISTORY.PAGE_NUMBER,  String.valueOf(currentPage));
        params.put(Constants.TRANSACTION_HISTORY.NUMBER_OF_RECORDS, "20");
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In transaction_history --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                //pagination_flag
                                final String next = response.getString("pagination_flag");
                                if (!next.equalsIgnoreCase("no")) {
                                    isLoadMore = true;
                                    currentPage += 1;
                                } else {
                                    isLoadMore = false;
                                }
                                JSONObject data = response.getJSONObject("data");
                                //JSONArray weekly = data.getJSONArray("transactions");
                                JSONArray transactions = data.getJSONArray("transactions");



                                if (transactions.length() > 0) {
                                    for (int i = 0; i < transactions.length(); i++) {
                                        JSONObject jsonObject = transactions.getJSONObject(i);
                                        Transaction_DBO transaction_dbo = new Transaction_DBO();

                                        JSONArray car_details = jsonObject.getJSONArray("car_details");

                                        transaction_dbo.setId(jsonObject.getString("id"));
                                        transaction_dbo.setJob_id(jsonObject.getString("job_id"));
                                        transaction_dbo.setUser_id(jsonObject.getString("user_id"));
                                        transaction_dbo.setGarage_id(jsonObject.getString("garage_id"));
                                        transaction_dbo.setAmount(jsonObject.getString("amount"));
                                        transaction_dbo.setType(jsonObject.getString("type"));
                                        transaction_dbo.setDatetime(jsonObject.getString("datetime"));
                                        transaction_dbo.setJob_title(jsonObject.getString("job_title"));
                                        transaction_dbo.setBusiness_name(jsonObject.getString("business_name"));
                                        transaction_dbo.setUfname(jsonObject.getString("ufname"));
                                        transaction_dbo.setUlname(jsonObject.getString("ulname"));
                                        transaction_dbo.setEg_comm(jsonObject.getString("eg_comm"));
                                        if (car_details.length()>0){
                                            for (int j = 0; j <car_details.length() ; j++) {

                                                JSONObject object = car_details.getJSONObject(j);
                                                transaction_dbo.setKm(object.getString("km"));

                                            }
                                        }
                                        transactionArrayList.add(transaction_dbo);

                                    }
                                }


                                 setData();


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
        queue.add(jsonObjReq);

    }

    private void setData() {

        if (transactionArrayList.size() > 0) {
            txtNoDataFound.setVisibility(View.GONE);
            recyclerview.setVisibility(View.VISIBLE);
            adptTransactionHistory = new AdptTransactionHistory(appContext, transactionArrayList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            recyclerview.setLayoutManager(mLayoutManager);
            recyclerview.setItemAnimator(new DefaultItemAnimator());
            recyclerview.setAdapter(adptTransactionHistory);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            recyclerview.addItemDecoration(bottomOffsetDecoration);
        } else {
            txtNoDataFound.setVisibility(View.VISIBLE);
            recyclerview.setVisibility(View.GONE);
        }
    }


    public void setMonths(){

        if (months.length>0){
            monthArrayList.add("Month");
            for (int i = 0; i <months.length ; i++) {
                monthArrayList.add(months[i]);
            }

            arraymonthAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, monthArrayList) {

                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {

                        return true;
                    }
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.tab_text));

                    } else {
                        tv.setTextColor(getResources().getColor(R.color.black));
                    }
                    return view;
                }
            };

            arraymonthAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            sp_month.setAdapter(arraymonthAdapter);

            sp_month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {

                        strMonth = HelperMethods.pad(position);

                        if (Connectivity.isConnected(appContext)) {
                            getTransactionHistory();
                        } else {
                            ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }



    }


    public void setYear(){
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        yearArrayList.add("Year");
        for (int i = 2010; i <= thisYear; i++) {
            yearArrayList.add(Integer.toString(i));
        }
        arrayYearAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, yearArrayList) {

            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {

                    return false;
                } else {

                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(getResources().getColor(R.color.tab_text));

                } else {
                    tv.setTextColor(getResources().getColor(R.color.black));
                }
                return view;
            }
        };

        arrayYearAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        sp_year.setAdapter(arrayYearAdapter);

        sp_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position != 0) {

                    strYear = sp_year.getSelectedItem().toString();
                    if (Connectivity.isConnected(appContext)) {
                        getTransactionHistory();
                    } else {
                        ((DashboardScreen) appContext).showAlertDialog(getString(R.string.no_internet));

                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}
