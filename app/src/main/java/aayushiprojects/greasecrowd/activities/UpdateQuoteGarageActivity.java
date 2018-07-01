package aayushiprojects.greasecrowd.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.AwardJobDBOCarOwner;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 6/2/18.
 */

public class UpdateQuoteGarageActivity extends BaseActivity {

    private String TAG = "UpdateQuoteGarageActivity";//jobBidmaster
    private TextView btnServiceType, txtjobTitle;
    private EditText edtComments;
    private EditText edtPrice;
    private EditText edtOfferPrice;
    private EditText edtTotalPrice;
    private EditText edtOffer;
    private TextView btnInclusions;
    private TextView btnBid;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    LinearLayout ll_back;
    private LinearLayout ll_propose_new_time, ll_new_time, ll_pick_up_time, ll_pick_up_date, ll_drop_off_date, ll_drop_off_time;
    TextView tv_drop_off_date, tv_drop_off_time, tv_pick_up_date, tv_pick_up_time;
    String selected_year_drop_off = "0", selected_month_drop_off = "0", selected_day_drop_off = "0", selected_year_pick_up = "0", selected_month_pick_up = "0", selected_day_pick_up = "0";
    Context appContext;
    private CheckBox checkbox;
    int bidPrice = 0;
    int offerPrice = 0;
    String strInclusions = "";
    String txtquote = "";
    boolean isFlexible;
    int flag = 1;
    int drop_off_time = 16, pick_up_time = 32;
    String job_title = "";
    int raodSideSelected = 0;
    int standardSelected = 0;
    String strraodSideSelected = "";
    String strstandardSelected = "";
    String txtCount = "", txtCount_2 = "", txtCount_3 = "";
    public static ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    public static String jobTitle = "";
    String strBidPrice = "", strBidComment = "", strOffer = "", strOfferPrice = "", strTotal = "", strServiceId = "", strServices = "", strInclusion = "", strDateTime1 = "", strDateTime2 = "";
    public static String time[] = {"01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
            "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
            "22:00", "22:30", "23:00", "23:30", "24:00"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_job_garage);
        awardJobArrayList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            isFlexible = intent.getBooleanExtra("isFlexible", false);
            awardJobArrayList = intent.getParcelableArrayListExtra("jobBidmaster");
            job_title = jobTitle = intent.getStringExtra("jobTitle");

        }
        getViews();
        setHeader("Update Quote");
        setfooter("garageowner");
        setHomeFooterGarage(UpdateQuoteGarageActivity.this);
        setlistenrforfooter();

        setOnClickListener();
        setData(awardJobArrayList);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            if (bundle.getString("strServices") != null) {
                strServices = bundle.getString("strServices");
            }
            if (bundle.getString("strServiceId") != null) {
                strServiceId = bundle.getString("strServiceId");
            }
            if (bundle.getString("strServiceId") != null && strServiceId.equalsIgnoreCase("7")) {
                raodSideSelected = bundle.getInt("raodSideSelected");
                standardSelected = bundle.getInt("standardSelected");
                txtCount = bundle.getString("txtCount");
                txtCount_2 = bundle.getString("txtCount_2");
                txtCount_3 = bundle.getString("txtCount_3");
            }
            if (bundle.getString("strInclusions") != null) {
                strInclusions = bundle.getString("strInclusions");
            }
            if (bundle.getString("jobTitle") != null) {
                jobTitle = bundle.getString("jobTitle");
            }
            if (bundle.getParcelableArrayList("jobBidmaster") != null) {
                awardJobArrayList = bundle.getParcelableArrayList("jobBidmaster");
            }

            AppLog.Log(TAG, strServices);
            AppLog.Log(TAG, "Inclusions ---> " + strInclusions);
        }
    }

    private void getViews() {
        appContext = this;
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        ll_back = findViewById(R.id.ll_back);
        btnServiceType = findViewById(R.id.btnServiceType);
        edtComments = findViewById(R.id.edtComments);
        edtPrice = findViewById(R.id.edtPrice);
        edtOfferPrice = findViewById(R.id.edtOfferPrice);
        edtTotalPrice = findViewById(R.id.edtTotalPrice);
        edtOffer = findViewById(R.id.edtOffer);
        btnInclusions = findViewById(R.id.btnInclusions);
        btnBid = findViewById(R.id.btnBid);
        txtjobTitle = findViewById(R.id.txtjobTitle);
        //txtjobTitle.setText("Job title : " + jobTitle);
        ll_propose_new_time = findViewById(R.id.ll_propose_new_time);
        ll_new_time = findViewById(R.id.ll_new_time);
        checkbox = findViewById(R.id.checkbox);
        tv_drop_off_date = findViewById(R.id.tv_drop_off_date);
        tv_drop_off_time = findViewById(R.id.tv_drop_off_time);
        tv_pick_up_date = findViewById(R.id.tv_pick_up_date);
        tv_pick_up_time = findViewById(R.id.tv_pick_up_time);
        ll_pick_up_time = findViewById(R.id.ll_pick_up_time);
        ll_pick_up_date = findViewById(R.id.ll_pick_up_date);
        ll_drop_off_date = findViewById(R.id.ll_drop_off_date);
        ll_drop_off_time = findViewById(R.id.ll_drop_off_time);

        txtjobTitle.setText("Job title : " + job_title);
        if (isFlexible) {
            ll_propose_new_time.setVisibility(View.VISIBLE);
        } else {
            ll_propose_new_time.setVisibility(View.GONE);
        }


        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    ll_new_time.setVisibility(View.VISIBLE);
                    SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                    String date = format.format(new Date());

                    tv_drop_off_date.setText(date);
                    tv_pick_up_date.setText(date);
//                    tv_drop_off_time.setText("");
//                    tv_pick_up_time.setText("");

                } else {
                    ll_new_time.setVisibility(View.GONE);
                }
            }
        });



    }

    public void setOnClickListener() {
        btnServiceType.setOnClickListener(this);
        btnBid.setOnClickListener(this);
        btnInclusions.setOnClickListener(this);
        tv_drop_off_date.setOnClickListener(this);
        tv_drop_off_time.setOnClickListener(this);
        tv_pick_up_date.setOnClickListener(this);
        tv_pick_up_time.setOnClickListener(this);
        ll_pick_up_time.setOnClickListener(this);
        ll_pick_up_date.setOnClickListener(this);
        ll_drop_off_date.setOnClickListener(this);
        ll_drop_off_time.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        if (view == btnServiceType) {
            Intent intent = new Intent(appContext, ViewSelectSeriviceTypeActivity.class);
            intent.putExtra("isUpdate", true);
            startActivityForResult(intent, 1);
            activityTransition();

        }
        if (view == btnInclusions) {
            Intent intent = new Intent(appContext, FreeInclusionGarageActivity.class);
            intent.putExtra("isUpdate", true);
            startActivityForResult(intent, 2);
            activityTransition();
        }
        if (view == btnBid) {
            if (isValidate()) {
                if (Connectivity.isConnected(appContext)) {
                    SubmitBid();
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }

        }
        if (view == ll_drop_off_date) {
            flag = 1;
            showDateDialog();
        }
        if (view == ll_drop_off_time) {

            flag = 1;
            displaytime();
        }
        if (view == ll_pick_up_date) {
            flag = 2;
            showDateDialog();
        }
        if (view == ll_pick_up_time) {

            flag = 2;
            displaytime();
        }
        if (view == ll_back) {

            onBackPressed();
        }

    }

    public boolean isValidate() {

        if (edtComments.getText().toString().length() == 0) {
            showAlertDialog("Please enter comment");
            return false;
        } else if (edtPrice.getText().toString().length() == 0) {
            showAlertDialog("Please enter bid price");
            return false;
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader("Update Quote");
        setfooter("garageowner");
        setHomeFooterGarage(UpdateQuoteGarageActivity.this);
        setlistenrforfooter();
    }


    public void SubmitBid() {
        if (raodSideSelected == 0) {
            strraodSideSelected = "NO";
        } else {
            strraodSideSelected = "YES";
        }
        if (standardSelected == 0) {
            strstandardSelected = "NO";
        } else {
            strstandardSelected = "YES";
        }
        strBidComment = edtComments.getText().toString();
        strBidPrice = edtPrice.getText().toString();
        strTotal = edtTotalPrice.getText().toString();
        strOffer = edtOffer.getText().toString();
        strOfferPrice = edtOfferPrice.getText().toString();
        if (tv_drop_off_date.getText().toString().length() != 0 || tv_pick_up_date.getText().toString().length() != 0) {
            strDateTime1 = parseDateToddMMyyyy(tv_drop_off_date.getText().toString()) + " " + tv_drop_off_time.getText().toString() + ":00";
            strDateTime2 = parseDateToddMMyyyy(tv_pick_up_date.getText().toString()) + " " + tv_pick_up_time.getText().toString() + ":00";
        } else {

            strDateTime1 = "0000-00-00" + " " + "00:00:00";
            strDateTime2 = "0000-00-00" + " " +  "00:00:00";
        }

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.SUBMIT_BID);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.SUBMIT_BIDS.JOB_ID, jobDetail_dbo.getCjob_id());
        params.put(Constants.SUBMIT_BIDS.GARAGE_ID, loginDetail_dbo.getUserid());
        params.put(Constants.SUBMIT_BIDS.BID_PRICE, strBidPrice);
        params.put(Constants.SUBMIT_BIDS.BID_COMMENT, strBidComment);
        params.put(Constants.SUBMIT_BIDS.ADD_OFFER, strOffer);
        params.put(Constants.SUBMIT_BIDS.ADD_OFFER_PRICE, strOfferPrice);
        params.put(Constants.SUBMIT_BIDS.TOTAL, "$" + strTotal);
        params.put(Constants.SUBMIT_BIDS.SERVICES, strServices);
        params.put(Constants.SUBMIT_BIDS.SERVICE_ID, strServiceId);
        params.put(Constants.SUBMIT_BIDS.INCLUSION, strInclusion);
        params.put(Constants.SUBMIT_BIDS.DATETIME1, strDateTime1);
        params.put(Constants.SUBMIT_BIDS.DATETIME2, strDateTime2);
        if (strServiceId.equalsIgnoreCase("7")) {
            String fleet_json_value = strraodSideSelected + "," + strstandardSelected + "," + txtCount + "," + txtCount_2 + "," + txtCount_3;
            params.put(Constants.SUBMIT_BIDS.FLEET_ARR, "");
            params.put("fleet_json_key", "inc_roadside_assist,inc_std_log_service,number_of_vehicles,hourly_rate_breakdows,cost_of_log");
            params.put("fleet_json_value", fleet_json_value);
        }


        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Bid JOb --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(appContext, MyJobsGarageActivity.class);
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
    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "dd-MM-yyyy";
        String outputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    private void showDateDialog() {
        final Calendar c = Calendar.getInstance();
        int today_date = 0, today_month = 0, today_year = 0;
        Calendar min = Calendar.getInstance();
        min.set(Calendar.MONTH, Calendar.JANUARY);
        min.set(Calendar.DAY_OF_MONTH, 1);
        min.set(Calendar.YEAR, 1970);
        try {

        } catch (NumberFormatException e) {
            today_date = c.get(Calendar.DAY_OF_MONTH);
            today_month = c.get(Calendar.MONTH);
            today_year = c.get(Calendar.YEAR);
            AppLog.Log(TAG, "Number Format Exception");
        } catch (Exception e) {
            AppLog.Log(TAG, "Exception");
        }


        DatePickerDialog dpd = new DatePickerDialog(appContext, R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        AppLog.Log(TAG, "onDateSet date");

                        if (flag == 1) {

                            tv_drop_off_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            selected_year_drop_off = HelperMethods.pad(year);
                            selected_month_drop_off = HelperMethods.pad(monthOfYear + 1);
                            selected_day_drop_off = HelperMethods.pad(dayOfMonth);

                        } else if (flag == 2) {
                            tv_pick_up_date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            selected_year_pick_up = HelperMethods.pad(year);
                            selected_month_pick_up = HelperMethods.pad(monthOfYear + 1);
                            selected_day_pick_up = HelperMethods.pad(dayOfMonth);
                        }

                    }
                }, today_year, today_month, today_date);
        dpd.setTitle(getResources().getString(R.string.select_trip_date));
        Calendar maxDate = Calendar.getInstance();
//    maxDate.add(Calendar.DATE,1);
        maxDate.set(Calendar.HOUR_OF_DAY, 23);
        maxDate.set(Calendar.MINUTE, 59);
        maxDate.set(Calendar.SECOND, 59);
        dpd.getDatePicker().setMinDate(maxDate.getTimeInMillis());
//    dpd.getDatePicker().setMinDate(min.getTimeInMillis());
        dpd.setButton(DialogInterface.BUTTON_NEGATIVE, getResources().getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        dialog.dismiss();

                    }
                });
        dpd.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.done), dpd);
        dpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
        dpd.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface arg0) {

            }
        });
        dpd.show();
    }

    private void displaytime() {

        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (flag == 1) {
                    tv_drop_off_time.setText(time[i]);
                    drop_off_time = i;
                } else if (flag == 2) {
                    tv_pick_up_time.setText(time[i]);
                    pick_up_time = i;
                }
                dialog.dismiss();
            }
        });
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4); //Set your widths


        lp.width = width;
        lp.height = height;

        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, time);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void setData(ArrayList<AwardJobDBOCarOwner> awardJobArrayList) {
        for (int i = 0; i < awardJobArrayList.size(); i++) {
            if (awardJobArrayList.get(i).getGarage_id().equalsIgnoreCase(loginDetail_dbo.getUserid())) {
                edtComments.setText(awardJobArrayList.get(i).getBid_comment());

                edtPrice.setText(String.format("%.0f", Float.parseFloat(awardJobArrayList.get(i).getBid_price())));
                edtOfferPrice.setText(String.format("%.0f", Float.parseFloat(awardJobArrayList.get(i).getAdd_offer_price())));
                //edtTotalPrice.setText(awardJobArrayList.get(i).getTotal());
                edtOffer.setText(awardJobArrayList.get(i).getAdd_offer());
                if (edtOfferPrice.getText().toString().length() == 0) {
                    offerPrice = 0;
                } else {
                    offerPrice = Integer.parseInt(edtOfferPrice.getText().toString());
                }
                if (edtPrice.getText().toString().length() == 0) {
                    bidPrice = 0;
                } else {
                    bidPrice = Integer.parseInt(edtPrice.getText().toString());
                }


                int total = bidPrice + offerPrice;

                edtTotalPrice.setText(String.valueOf(total));

                edtPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if (edtOfferPrice.getText().toString().length() == 0) {
                            offerPrice = 0;
                        } else {
                            offerPrice = Integer.parseInt(edtOfferPrice.getText().toString());
                        }
                        if (edtPrice.getText().toString().length() == 0) {
                            bidPrice = 0;
                        } else {
                            bidPrice = Integer.parseInt(edtPrice.getText().toString());
                        }


                        int total = bidPrice + offerPrice;

                        edtTotalPrice.setText(String.valueOf(total));

                    }
                });

                edtOfferPrice.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {
                        if (edtOfferPrice.getText().toString().length() == 0) {
                            offerPrice = 0;
                        } else {
                            offerPrice = Integer.parseInt(edtOfferPrice.getText().toString());
                        }
                        if (edtPrice.getText().toString().length() == 0) {
                            bidPrice = 0;
                        } else {
                            bidPrice = Integer.parseInt(edtPrice.getText().toString());
                        }


                        int total = bidPrice + offerPrice;

                        edtTotalPrice.setText(String.valueOf(total));
                    }
                });
            }
        }
    }

    public class CustomListAdapterOther extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        private String time[];

        public CustomListAdapterOther(Context appContext, String list[]) {
            context = appContext;
            layoutInflater = LayoutInflater.from(appContext);
            time = list;
        }

        @Override
        public int getCount() {
            return time.length;
        }

        @Override
        public Object getItem(int position) {
            return time[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            final CustomListAdapterOther.ChallengerHolder holder;

            String model = time[position];
            if (row == null) {
                holder = new CustomListAdapterOther.ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
                holder.tv_name = row.findViewById(R.id.tv_name);

                row.setTag(holder);
            } else {
                holder = (CustomListAdapterOther.ChallengerHolder) row.getTag();
            }

            holder.tv_name.setText(model);
            return row;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
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
