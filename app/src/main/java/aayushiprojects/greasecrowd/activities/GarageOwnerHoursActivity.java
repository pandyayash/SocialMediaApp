package aayushiprojects.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.TraddingHoursDBo;

public class GarageOwnerHoursActivity extends BaseActivity {

    public static String time[] = {"1:00", "1:30", "2:00", "2:30", "3:00", "3:30", "4:00", "4:30",
            "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30",
            "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30"};
    public static String ampm[] = {"AM", "PM"};
    public static String days[] = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY",
            "SUNDAY"};

    ArrayList<TraddingHoursDBo> traddingHoursDBoArrayList;
    ListView lv;
    Context appContext;
    CustomeTraddingDays customeTraddingDays;
    TextView save;
    int value;
    JSONArray jarray;
    LinearLayout ll_back;
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_hours);
        setHeader("HOURS");
        getviews();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this);
        getgaragedata();
        setdata();

    }

    private void getgaragedata() {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "get_garage_info");
        params.put("action", "tra_hrs");

        AppLog.Log("TAG", "Params In Keywords : " + params);
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
                                JSONArray jsonArray = response.getJSONArray("data");
                                for (int j = 0; j < jsonArray.length(); j++) {
                                    JSONObject jobj = jsonArray.getJSONObject(j);
                                    for (int i = 0; i < days.length; i++) {
                                        if (days[i].toLowerCase().contains(jobj.getString("day"))) {
                                            AppLog.Log("TAG", "Days matched: " + jobj.getString("day"));
                                            traddingHoursDBoArrayList.get(i).setIsselected(true);
                                            setTimings(i, jobj.getString("time"));
                                            break;
                                        }
                                    }
                                }
                                setdata();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
                showAlertDialog(getResources().getString(R.string.some_error_try_again));
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);

    }

    private void setTimings(int position, String data) {
        String temp = data.replace("-", "");
        String array1[] = temp.split(" ");
        AppLog.Log("TAG", "time size : " + array1.length);

        traddingHoursDBoArrayList.get(position).setIntime(array1[0]);
        traddingHoursDBoArrayList.get(position).setIntimeampm(array1[1]);

        traddingHoursDBoArrayList.get(position).setGetOuttimeampm(array1[4]);
        traddingHoursDBoArrayList.get(position).setOuttime(array1[3]);


    }

    private void setdata() {
        customeTraddingDays = new CustomeTraddingDays(appContext, traddingHoursDBoArrayList);
        lv.setAdapter(customeTraddingDays);
        save.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    public void getviews() {
        appContext = this;
        traddingHoursDBoArrayList = new ArrayList<>();
        jarray = new JSONArray();
        save = findViewById(R.id.save);
        save.setVisibility(View.VISIBLE);
        ll_back = findViewById(R.id.ll_back);
        lv = findViewById(R.id.listfordays);
        for (int i = 0; i < days.length; i++) {

            TraddingHoursDBo tradding = new TraddingHoursDBo();
            tradding.setTag(days[i]);
            tradding.setIsselected(false);
            traddingHoursDBoArrayList.add(tradding);
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.save:
                validatedata();
                break;

            case R.id.ll_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    private void validatedata() {
        String intime, outtime, inampm, outampm;
        int flagin = 0, flagout = 0;

        for (int i = 0; i < traddingHoursDBoArrayList.size(); i++) {

            if (traddingHoursDBoArrayList.get(i).isselected()) {
                intime = traddingHoursDBoArrayList.get(i).getIntime();
                outtime = traddingHoursDBoArrayList.get(i).getOuttime();
                inampm = traddingHoursDBoArrayList.get(i).getIntimeampm();
                outampm = traddingHoursDBoArrayList.get(i).getGetOuttimeampm();
                if (inampm.equalsIgnoreCase(outampm)) {
                    for (int j = 0; j < time.length; j++) {

                        if (intime.equalsIgnoreCase(time[j])) {
                            flagin = j;
                            break;
                        }
                    }
                    for (int j = 0; j < time.length; j++) {

                        if (outtime.equalsIgnoreCase(time[j])) {
                            flagout = j;
                            break;
                        }
                    }
                }
            } else {


            }
        }

        for (int i = 0; i < traddingHoursDBoArrayList.size(); i++) {
            JSONObject jsonObject = new JSONObject();
            try {
                if (traddingHoursDBoArrayList.get(i).isselected()) {
                    String day = traddingHoursDBoArrayList.get(i).getTag();
                    String to =
                            traddingHoursDBoArrayList.get(i).getOuttime() + " " + traddingHoursDBoArrayList.get(i)
                                    .getGetOuttimeampm();
                    String from =
                            traddingHoursDBoArrayList.get(i).getIntime() + " " + traddingHoursDBoArrayList.get(i)
                                    .getIntimeampm();
                    jsonObject.put("day", day.substring(0, 3).toLowerCase());
                    jsonObject.put("from", from);
                    jsonObject.put("to", to);
                    jarray.put(jsonObject);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (flagin > flagout) {
            showAlertDialog(getResources().getString(R.string.settime));
        } else {
            callfinalapi();

        }

    }

    public void callfinalapi() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, "edit_tra_hrs");
        params.put("tr_hrs", jarray.toString());

        AppLog.Log("TAG", "Params : " + new JSONObject(params));
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("response", "" + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                finish();
                                activityTransition();
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
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

    public class CustomeTraddingDays extends BaseAdapter {

        Context apContext;
        LayoutInflater layoutInflater;
        ChallengerHolder holder;
        ArrayList<TraddingHoursDBo> traddingHoursDBoArrayList;

        public CustomeTraddingDays(Context appContext,
                                   ArrayList<TraddingHoursDBo> traddingHoursDBoArrayList) {
            this.apContext = appContext;
            layoutInflater = LayoutInflater.from(appContext);
            this.traddingHoursDBoArrayList = traddingHoursDBoArrayList;


        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            View v = convertView;

            if (v == null) {
                holder = new ChallengerHolder();
                v = layoutInflater.inflate(R.layout.list_row_profile_hours, parent, false);

                holder.day = v.findViewById(R.id.day_name);
                holder.intime = v.findViewById(R.id.sun);
                holder.intimeampm = v.findViewById(R.id.sun1);
                holder.outtime = v.findViewById(R.id.sun2);
                holder.outtimeampm = v.findViewById(R.id.sun3);
                holder.edt_sun_time = v.findViewById(R.id.edt_sun_time);
                holder.edt1_sun_time = v.findViewById(R.id.edt1_sun_time);
                holder.edt_am_pm_sun = v.findViewById(R.id.edt_am_pm_sun);
                holder.edt1_am_pm_sun = v.findViewById(R.id.edt1_am_pm_sun);
                holder.row = v.findViewById(R.id.row);
                holder.dropdown = v.findViewById(R.id.dropdown);
                holder.dropdown1 = v.findViewById(R.id.dropdown1);
                holder.dropdown2 = v.findViewById(R.id.dropdown2);
                holder.dropdown3 = v.findViewById(R.id.dropdown3);


                v.setTag(holder);

            } else {
                holder = (ChallengerHolder) v.getTag();
            }

            holder.day.setText(traddingHoursDBoArrayList.get(position).getTag());
            holder.edt_sun_time.setText(traddingHoursDBoArrayList.get(position).getIntime());
            holder.edt1_sun_time.setText(traddingHoursDBoArrayList.get(position).getOuttime());
            holder.edt_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getIntimeampm());
            holder.edt1_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getGetOuttimeampm());
            if (traddingHoursDBoArrayList.get(position).isselected()) {
                holder.row.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                holder.day.setTextColor(getResources().getColor(R.color.white));
                holder.edt_sun_time.setTextColor(getResources().getColor(R.color.white));
                holder.edt1_sun_time.setTextColor(getResources().getColor(R.color.white));
                holder.edt_am_pm_sun.setTextColor(getResources().getColor(R.color.white));
                holder.edt1_am_pm_sun.setTextColor(getResources().getColor(R.color.white));

                holder.intime.setBackground(getDrawable(R.drawable.rect_border));
                holder.intimeampm.setBackground(getDrawable(R.drawable.rect_border));
                holder.outtime.setBackground(getDrawable(R.drawable.rect_border));
                holder.outtimeampm.setBackground(getDrawable(R.drawable.rect_border));


                holder.dropdown.setImageResource(R.drawable.arrow);
                holder.dropdown.setRotation(270.0f);

                holder.dropdown1.setImageResource(R.drawable.arrow);
                holder.dropdown1.setRotation(270.0f);

                holder.dropdown2.setImageResource(R.drawable.arrow);
                holder.dropdown2.setRotation(270.0f);

                holder.dropdown3.setImageResource(R.drawable.arrow);
                holder.dropdown3.setRotation(270.0f);

            } else {
                holder.row.setBackgroundColor(getResources().getColor(R.color.white));
                holder.day.setTextColor(getResources().getColor(R.color.text_color2));
                holder.edt_sun_time.setTextColor(getResources().getColor(R.color.text_color2));
                holder.edt1_sun_time.setTextColor(getResources().getColor(R.color.text_color2));
                holder.edt_am_pm_sun.setTextColor(getResources().getColor(R.color.text_color2));
                holder.edt1_am_pm_sun.setTextColor(getResources().getColor(R.color.text_color2));


                holder.intime.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                holder.intimeampm.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                holder.outtime.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                holder.outtimeampm.setBackground(getDrawable(R.drawable.rect_inner_boarder));

                holder.dropdown1.setImageResource(R.drawable.down);
                holder.dropdown.setRotation(-360);
                holder.dropdown.setImageResource(R.drawable.down);
                holder.dropdown1.setRotation(-360);
                holder.dropdown2.setImageResource(R.drawable.down);
                holder.dropdown2.setRotation(-360);
                holder.dropdown3.setImageResource(R.drawable.down);
                holder.dropdown3.setRotation(-360);

            }

            holder.intime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (traddingHoursDBoArrayList.get(position).isselected()) {
                        value = 1;
                        displaytime(position);
                    } else {
                        showAlertDialog("Please select the days in order to make changes in the timings");
                    }


                }
            });

            holder.intimeampm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (traddingHoursDBoArrayList.get(position).isselected()) {
                        value = 1;
                        displayAmPm(position);
                    } else {
                        showAlertDialog("Please select the days in order to make changes in the timings");
                    }

                }
            });

            holder.outtime.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (traddingHoursDBoArrayList.get(position).isselected()) {
                        value = 2;
                        displaytime(position);
                    } else {
                        showAlertDialog("Please select the days in order to make changes in the timings");
                    }


                }
            });

            holder.outtimeampm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (traddingHoursDBoArrayList.get(position).isselected()) {
                        value = 2;
                        displayAmPm(position);
                    } else {
                        showAlertDialog("Please select the days in order to make changes in the timings");
                    }

                }
            });

            holder.row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (traddingHoursDBoArrayList.get(position).isselected()) {
                        traddingHoursDBoArrayList.get(position).setIsselected(false);
                       /* traddingHoursDBoArrayList.get(position).setIntime("8:00");
                        traddingHoursDBoArrayList.get(position).setIntimeampm("AM");
                        traddingHoursDBoArrayList.get(position).setOuttime("5:30");
                        traddingHoursDBoArrayList.get(position).setGetOuttimeampm("PM");*/
                        customeTraddingDays.notifyDataSetChanged();

                    } else {
                        traddingHoursDBoArrayList.get(position).setIsselected(true);
                        customeTraddingDays.notifyDataSetChanged();
                    }

                }
            });

            return v;
        }


        class ChallengerHolder {

            TextView day;
            LinearLayout intime, intimeampm, outtime, outtimeampm, row;
            EditText edt_sun_time, edt1_sun_time, edt_am_pm_sun, edt1_am_pm_sun;
            ImageView dropdown, dropdown1, dropdown2, dropdown3;

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
            final ChallengerHolder holder;

            String model = time[position];
            if (row == null) {
                holder = new ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
                holder.tv_name = row.findViewById(R.id.tv_name);

                row.setTag(holder);
            } else {
                holder = (ChallengerHolder) row.getTag();
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

    private void displaytime(final int position) {

        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (value == 1) {
                    traddingHoursDBoArrayList.get(position).setIntime(time[i]);
                }
                if (value == 2) {
                    traddingHoursDBoArrayList.get(position).setOuttime(time[i]);
                }
                customeTraddingDays.notifyDataSetChanged();

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

    private void displayAmPm(final int position) {

        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if (value == 1) {
                    traddingHoursDBoArrayList.get(position).setIntimeampm(ampm[i]);
                }
                if (value == 2) {
                    traddingHoursDBoArrayList.get(position).setGetOuttimeampm(ampm[i]);
                }
                customeTraddingDays.notifyDataSetChanged();

                dialog.dismiss();
            }
        });

        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);


        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, ampm);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }
}
