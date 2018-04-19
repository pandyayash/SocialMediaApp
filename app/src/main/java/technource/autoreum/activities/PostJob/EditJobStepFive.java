package technource.autoreum.activities.PostJob;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
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
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.activities.BaseActivity;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.activities.MyJobsUserActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.LoginDetail_DBO;

/**
 * Created by technource on 22/12/17.
 */

public class EditJobStepFive extends BaseActivity {

    String TAG = "tag";
    TextView tv_drop_off_date, tv_drop_off_time, tv_pick_up_date, tv_pick_up_time, tv_flexibility;
    LinearLayout ll_continue, ll_back_button, ll_cancel, ll_back, ll_pick_up_time, ll_pick_up_date, ll_drop_off_date, ll_drop_off_time, mroot, footer;
    EditText message;
    Context appContext;
    public static String time[] = {"01:00", "01:30", "02:00", "02:30", "03:00", "03:30", "04:00", "04:30",
            "05:00", "05:30", "06:00", "06:30", "07:00", "07:30", "08:00", "08:30",
            "09:00", "09:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30",
            "14:00", "14:30", "15:00", "15:30", "16:00", "16:30",
            "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30",
            "22:00", "22:30", "23:00", "23:30", "24:00"};

    String jwt, job_id, merged_dropoff_time, merged_pickup_time, placeholder;
    int flag = 1; //1->  drop off 2-> pick up
    int drop_off_time = 16, pick_up_time = 32;
    LoginDetail_DBO loginDetail_dbo;
    String selected_year_drop_off = "0", selected_month_drop_off = "0", selected_day_drop_off = "0", selected_year_pick_up = "0", selected_month_pick_up = "0", selected_day_pick_up = "0";
    String help = "";
    JobDetail_DBO jobDetail_dbo;
    boolean isPending = false;
    ScrollView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job_five);
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(EditJobStepFive.this);
        isPending = jobDetail_dbo.isPending();
        getViewS();

        if (isPending) {
            setHeader("Post a New Job");
            setfooter("jobs");
            setPostJObFooter(getApplicationContext());
        } else {
            setHeader("Edit Job");
            setfooter("job_details");
            setJobDetailsFooter(getApplicationContext());
        }
        setlistenrforfooter();
        setOnClickListener();
        message.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    message.getBackground().setLevel(1);
                }
            }
        });
    }

    public void getViewS() {
        appContext = this;
        tv_drop_off_date = findViewById(R.id.tv_drop_off_date);
        tv_drop_off_time = findViewById(R.id.tv_drop_off_time);
        tv_pick_up_date = findViewById(R.id.tv_pick_up_date);
        tv_pick_up_time = findViewById(R.id.tv_pick_up_time);
        tv_flexibility = findViewById(R.id.tv_flexibility);

        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);
        ll_cancel = findViewById(R.id.ll_cancel);
        ll_back = findViewById(R.id.ll_back);

        ll_pick_up_time = findViewById(R.id.ll_pick_up_time);
        ll_pick_up_date = findViewById(R.id.ll_pick_up_date);
        ll_drop_off_date = findViewById(R.id.ll_drop_off_date);
        ll_drop_off_time = findViewById(R.id.ll_drop_off_time);

        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);

        jwt = loginDetail_dbo.getJWTToken();
        sv = findViewById(R.id.sv);
        mroot = findViewById(R.id.mroot);

        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            placeholder = intent.getStringExtra("placeholder");
            help = intent.getStringExtra("help");
        }

        message = findViewById(R.id.message);
        footer = findViewById(R.id.footer);
        AppLog.Log("place4 holder", "-->" + placeholder);

        //message.setHint(placeholder);
        message.setText(jobDetail_dbo.getProblem_description());
        tv_flexibility.setText(jobDetail_dbo.getTime_flexibility());

        message.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (v.getId() == R.id.message) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (event.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                }
                return false;
            }
        });

        mroot.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mroot.getWindowVisibleDisplayFrame(r);
                int screenHeight = mroot.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                    footer.setVisibility(View.GONE);
                } else {
                    footer.setVisibility(View.VISIBLE);
                }
            }
        });

        if (isPending) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            String date = format.format(new Date());

            tv_drop_off_date.setText(date);
            tv_pick_up_date.setText(date);
        } else {
            tv_drop_off_date.setText(parseDateToddMMyyyy(jobDetail_dbo.getDropoff_date_time()));
            tv_pick_up_date.setText(parseDateToddMMyyyy(jobDetail_dbo.getPickup_date_time()));
            tv_drop_off_time.setText(parseDateToTime(jobDetail_dbo.getDropoff_date_time()));
            tv_pick_up_time.setText(parseDateToTime(jobDetail_dbo.getPickup_date_time()));
        }


    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy";
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

    public String parseDateToTime(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "HH:mm";
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

    protected void onResume() {
        super.onResume();
        setPostJObFooter(this);
    }

    public void setOnClickListener() {
        tv_drop_off_date.setOnClickListener(this);
        tv_drop_off_time.setOnClickListener(this);
        tv_pick_up_date.setOnClickListener(this);
        tv_pick_up_time.setOnClickListener(this);
        tv_flexibility.setOnClickListener(this);

        ll_pick_up_time.setOnClickListener(this);
        ll_pick_up_date.setOnClickListener(this);
        ll_drop_off_date.setOnClickListener(this);
        ll_drop_off_time.setOnClickListener(this);

        tv_drop_off_date.setOnClickListener(this);
        tv_drop_off_time.setOnClickListener(this);
        tv_pick_up_date.setOnClickListener(this);
        tv_pick_up_time.setOnClickListener(this);

        ll_continue.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);

        message.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {

            case R.id.tv_drop_off_date:
            case R.id.ll_drop_off_date:
                flag = 1;
                showDateDialog();
                break;

            case R.id.tv_drop_off_time:
            case R.id.ll_drop_off_time:
                flag = 1;
                displaytime();
                break;

            case R.id.tv_pick_up_date:
            case R.id.ll_pick_up_date:
                flag = 2;
                showDateDialog();
                break;

            case R.id.tv_pick_up_time:
            case R.id.ll_pick_up_time:
                flag = 2;
                displaytime();
                break;
            case R.id.tv_flexibility:
                displaytimeFlexibility();
                break;
            case R.id.ll_continue:
                checkData();
                break;
            case R.id.ll_cancel:
                Intent intent = new Intent(appContext, DashboardScreen.class);
                intent.addFlags(Constants.INTENT_FLAGS);
                startActivity(intent);
                finish();
                activityTransition();
                break;
            case R.id.ll_back_button:
                onBackPressed();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }


    public void checkData() {

        long dropoff, pickup;
        if (isvalidate()) {
//            merged_dropoff_time = selected_year_drop_off + "-" + selected_month_drop_off + "-" + selected_day_drop_off + " " + time[drop_off_time] + ":" + "00";
//            merged_pickup_time = selected_year_pick_up + "-" + selected_month_pick_up + "-" + selected_day_pick_up + " " + time[pick_up_time] + ":" + "00";

            String dropOffSplit[] = tv_drop_off_date.getText().toString().split("-");
            String pickUpSplit[] = tv_pick_up_date.getText().toString().split("-");

            merged_dropoff_time = dropOffSplit[2] + "-" + dropOffSplit[1] + "-" + dropOffSplit[0] + " " + time[WhatIsMyPosition(tv_drop_off_time.getText().toString())] + ":" + "00";
            merged_pickup_time = pickUpSplit[2] + "-" + pickUpSplit[1] + "-" + pickUpSplit[0] + " " + time[WhatIsMyPosition(tv_pick_up_time.getText().toString())] + ":" + "00";

            dropoff = convertIntoMillies(merged_dropoff_time);
            pickup = convertIntoMillies(merged_pickup_time);

            AppLog.Log("dropoff", "" + dropoff);
            AppLog.Log("dropoff", "" + pickup);
            if ((dropoff <= pickup)) {
                if (Connectivity.isConnected(appContext)) {
                    CallFinalApi();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
            } else {
                showAlertDialog(getString(R.string.drop_off_not_less_than_pick_up));
            }

        }
    }


    public int WhatIsMyPosition(String date) {
        int pos = 0;
        for (int i = 0; i < time.length; i++) {
            if (date.equalsIgnoreCase(time[i])) {
                pos = i;
                break;
            }
        }

        return pos;

    }

    public long convertIntoMillies(String date) {
        String givenDateString = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
        try {
            Date mDate = sdf.parse(givenDateString);
            long timeInMilliseconds = mDate.getTime();
            return timeInMilliseconds;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public void CallFinalApi() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "post_job");
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "5");
        params.put(Constants.PostNewJob.EMERGENCY, "");
        params.put(Constants.PostNewJob.HELP, help);
        params.put(Constants.PostNewJob.INSURANCE, "");
        params.put(Constants.PostNewJob.DROP_LOC, "");
        params.put(Constants.PostNewJob.JID, job_id);
        params.put(Constants.PostNewJob.DROP_TIME, merged_dropoff_time);
        params.put(Constants.PostNewJob.PICK_TIME, merged_pickup_time);
        params.put(Constants.PostNewJob.FLEXIBILITY, tv_flexibility.getText().toString());
        params.put(Constants.PostNewJob.JOB_DESC, message.getText().toString());

        AppLog.Log("TAG", "Params : " + new JSONObject(params));
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, getString(R.string.str_successfully_posted), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(appContext, MyJobsUserActivity.class);
                                intent.putExtra("jobStatus", jobDetail_dbo.getJob_status());
                                intent.putExtra("isFromEdit", true);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);
                                finish();
                                activityTransition();
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
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

    public boolean isvalidate() {

        String drop_off, pick_up, mesg;

        drop_off = tv_drop_off_date.getText().toString();
        pick_up = tv_pick_up_date.getText().toString();
        mesg = message.getText().toString();
        if (!(drop_off != null && drop_off.trim().length() > 0)) {
            tv_drop_off_date.requestFocus();
            showAlertDialog(getString(R.string.drop_off_date));
            return false;
        } else if (!(pick_up != null && pick_up.trim().length() > 0)) {
            tv_pick_up_date.requestFocus();
            showAlertDialog(getString(R.string.enter_pick_up_date));
            return false;
        } else if (!(mesg != null && mesg.trim().length() > 0)) {
            message.requestFocus();
            message.getBackground().setLevel(4);
            showAlertDialog(getString(R.string.please_enter_job_description));
            return false;
        }
        return true;
    }

    public void displaytimeFlexibility() {

        final CharSequence colors[] = new CharSequence[]{"Not Flexible", "Flexible", "Very Flexible"};

        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Select Flexibility ");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                tv_flexibility.setText(colors[which]);
            }
        });
        builder.show();
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


                            selected_year_drop_off = HelperMethods.pad(year);
                            selected_month_drop_off = HelperMethods.pad(monthOfYear + 1);
                            selected_day_drop_off = HelperMethods.pad(dayOfMonth);

                            tv_drop_off_date.setText(selected_day_drop_off + "-" + selected_month_drop_off + "-" + selected_year_drop_off);
                        } else if (flag == 2) {

                            selected_year_pick_up = HelperMethods.pad(year);
                            selected_month_pick_up = HelperMethods.pad(monthOfYear + 1);
                            selected_day_pick_up = HelperMethods.pad(dayOfMonth);
                            tv_pick_up_date.setText(selected_day_pick_up + "-" + selected_month_pick_up + "-" + selected_year_pick_up);
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
