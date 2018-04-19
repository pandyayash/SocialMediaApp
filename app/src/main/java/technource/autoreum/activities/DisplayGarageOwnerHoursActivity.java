package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.TraddingHoursDBo;

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
    LinearLayout footer;

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
        save = findViewById(R.id.save);
        save.setVisibility(View.GONE);
        ll_back = findViewById(R.id.ll_back);
        lv = findViewById(R.id.listfordays);
        footer = findViewById(R.id.footer);
        footer.setVisibility(View.GONE);
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
