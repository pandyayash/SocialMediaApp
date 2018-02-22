package technource.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.TraddingHoursDBo;

public class DisplayGarageOwnerHoursActivity extends BaseActivity {
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
        Intent intent = getIntent();
        if (intent != null) {
            traddingHoursDBoArrayList = intent.getParcelableArrayListExtra("data");
        }
        setdata();
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
        save = (TextView) findViewById(R.id.save);
        save.setVisibility(View.GONE);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        lv = (ListView) findViewById(R.id.listfordays);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
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
            return traddingHoursDBoArrayList.size();
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

                holder.day = (TextView) v.findViewById(R.id.day_name);
                holder.intime = (LinearLayout) v.findViewById(R.id.sun);
                holder.intimeampm = (LinearLayout) v.findViewById(R.id.sun1);
                holder.outtime = (LinearLayout) v.findViewById(R.id.sun2);
                holder.outtimeampm = (LinearLayout) v.findViewById(R.id.sun3);
                holder.edt_sun_time = (EditText) v.findViewById(R.id.edt_sun_time);
                holder.edt1_sun_time = (EditText) v.findViewById(R.id.edt1_sun_time);
                holder.edt_am_pm_sun = (EditText) v.findViewById(R.id.edt_am_pm_sun);
                holder.edt1_am_pm_sun = (EditText) v.findViewById(R.id.edt1_am_pm_sun);
                holder.row = (LinearLayout) v.findViewById(R.id.row);
                holder.dropdown = (ImageView) v.findViewById(R.id.dropdown);
                holder.dropdown1 = (ImageView) v.findViewById(R.id.dropdown1);
                holder.dropdown2 = (ImageView) v.findViewById(R.id.dropdown2);
                holder.dropdown3 = (ImageView) v.findViewById(R.id.dropdown3);

                v.setTag(holder);

            } else {
                holder = (ChallengerHolder) v.getTag();
            }

            holder.day.setText(traddingHoursDBoArrayList.get(position).getTag());
            holder.edt_sun_time.setText(traddingHoursDBoArrayList.get(position).getIntime());
            holder.edt1_sun_time.setText(traddingHoursDBoArrayList.get(position).getOuttime());
            holder.edt_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getIntimeampm());
            holder.edt1_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getGetOuttimeampm());

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

            return v;
        }

        class ChallengerHolder {
            TextView day;
            LinearLayout intime, intimeampm, outtime, outtimeampm, row;
            EditText edt_sun_time, edt1_sun_time, edt_am_pm_sun, edt1_am_pm_sun;
            ImageView dropdown, dropdown1, dropdown2, dropdown3;

        }
    }
}
