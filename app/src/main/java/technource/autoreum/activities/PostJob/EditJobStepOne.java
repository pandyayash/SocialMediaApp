package technource.autoreum.activities.PostJob;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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

import technource.autoreum.R;
import technource.autoreum.activities.BaseActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.CatogoriesDBO;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.RegisteredCarDBO;

public class EditJobStepOne extends BaseActivity {

    CatogoriesDBO catogoriesDBO;
    RegisteredCarDBO registeredCarDBO;
    EditText edt_jobtitle, edt_car;
    TextView edt_model, edt_badge, edt_registration, edt_autometic_or_manual, edt_year;
    LinearLayout ll_continue, ll_cancel, ll_back, ll_back_button;
    RelativeLayout rl_model;
    String categories_id="", registeredcar_model;
    Context appContext;
    ArrayList<RegisteredCarDBO> registeredCarDBOsArraylist;
    LoginDetail_DBO loginDetail_dbo;
    String jwt, car_id, job_id = "",job_title="";
    int from_register = 0;
    JobDetail_DBO jobDetail_dbo;
    boolean isPending=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job);

        Intent intent = getIntent();
        if (intent!=null){
            job_id = intent.getStringExtra("job_id");
            job_title = intent.getStringExtra("job_title");

        }

        getViews();
        isPending = jobDetail_dbo.isPending();
        if (isPending){
            setHeader("Post a New Job");
            setfooter("jobs");
            setPostJObFooter(getApplicationContext());
        }else {
            setHeader("Edit Job");
            setfooter("job_details");
            setJobDetailsFooter(getApplicationContext());
        }


        setlistenrforfooter();
        setViews();
        getData();
        if (Connectivity.isConnected(appContext)) {
            GetRegisteredCars();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }
        setClickListener();
        edt_jobtitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    edt_jobtitle.getBackground().setLevel(1);
                }
            }
        });
    }


    public void getViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        car_id = jobDetail_dbo.getCar_id();
        edt_jobtitle = findViewById(R.id.edt_jobtitle);
        edt_jobtitle.setText(job_title);
        edt_jobtitle.setSelection(edt_jobtitle.getText().length());
        edt_car = findViewById(R.id.edt_car);
        edt_model = findViewById(R.id.edt_model);
        edt_badge = findViewById(R.id.edt_badge);
        edt_registration = findViewById(R.id.edt_registration);
        edt_autometic_or_manual = findViewById(R.id.edt_autometic_or_manual);
        edt_year = findViewById(R.id.edt_year);
        rl_model = findViewById(R.id.rl_model);
        ll_back = findViewById(R.id.ll_back);
        ll_back_button = findViewById(R.id.ll_back_button);

        ll_continue = findViewById(R.id.ll_continue);
        ll_cancel = findViewById(R.id.ll_cancel);
    }

    public void setViews() {
        edt_jobtitle.getBackground().setLevel(1);
        rl_model.getBackground().setLevel(1);
    }
    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("registeredcar")) {
                from_register = 1;
                registeredCarDBO = intent.getParcelableExtra("registeredcar");
                registeredcar_model = registeredCarDBO.getCarmodel();
                edt_car.setText(registeredCarDBO.getCarmake() + " " + registeredCarDBO.getCarmodel());
                edt_model.setText(registeredCarDBO.getCarmodel());
                edt_badge.setText(registeredCarDBO.getCarbadge());
                edt_registration.setText(registeredCarDBO.getRegistration_number());
                edt_autometic_or_manual.setText(registeredCarDBO.getCar_type());
                edt_year.setText(registeredCarDBO.getYear());


            } else if (intent.hasExtra("categories")) {
                catogoriesDBO = intent.getParcelableExtra("categories");
                categories_id = (catogoriesDBO.getId());
            } else if (intent.hasExtra("Dashboard")) {
                categories_id = "";
            }
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.edt_car:
                showMakeDialog();
                break;
            case R.id.ll_continue:
                if (edt_jobtitle.getText().toString() != null && edt_jobtitle.getText().toString().trim().length() > 0) {

                    if (Connectivity.isConnected(appContext)) {
                        storeData();
                    } else {
                        showAlertDialog(getResources().getString(R.string.no_internet));
                    }
                } else {
                    edt_jobtitle.requestFocus();
                    edt_jobtitle.getBackground().setLevel(4);
                    showAlertDialog("Enter Job Title");
                }
                break;
            case R.id.ll_cancel:
                finish();
                activityTransition();
                break;
            case R.id.ll_back_button:
                onBackPressed();
                break;
        }
    }
    public void setClickListener() {
        ll_back.setOnClickListener(this);
        edt_car.setOnClickListener(this);
        ll_continue.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
    }
    @Override
    protected void onResume() {
        super.onResume();
        setPostJObFooter(this);
    }

    public void storeData() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, WebServiceURLs.POST_JOB);
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "1");
        params.put(Constants.PostNewJob.CAR_ID, car_id);
        params.put(Constants.PostNewJob.JT, edt_jobtitle.getText().toString());
        params.put(Constants.PostNewJob.JID, job_id);

        AppLog.Log("TAG", "Params : " + params);
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
                                job_id = response.getString("job_id");
                                Intent intent = new Intent(EditJobStepOne.this, EditJobStepTwo.class);
                                intent.putExtra("job_id", job_id);

//                                if (!categories_id.equalsIgnoreCase("") && categories_id!=null) {
//                                    intent.putExtra("categories", categories_id);
//                                } else {
//                                    intent.putExtra("Dashboard", "yes");
//                                }
                                startActivity(intent);
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

    public void GetRegisteredCars() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "get_reg_cars");
        AppLog.Log("TAG", "Params : " + params);
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
                                registeredCarDBOsArraylist = new ArrayList<>();
                                JSONArray json = response.getJSONArray("data");
                                for (int i = 0; i < json.length(); i++) {
                                    RegisteredCarDBO services = new RegisteredCarDBO();
                                    JSONObject obj = json.getJSONObject(i);
                                    services.setId(obj.getString("id"));
                                    ///car_id = obj.getString("id");
                                    services.setRegistration_number(obj.getString("registration_number"));
                                    services.setCarmake(obj.getString("carmake"));
                                    services.setCarmodel(obj.getString("carmodel"));
                                    if (obj.getString("carbadge").equalsIgnoreCase("")) {
                                        services.setCarbadge("No Badge/Variant");
                                    } else {
                                        services.setCarbadge(obj.getString("carbadge"));
                                    }
                                    services.setCar_type(obj.getString("car_type"));
                                    services.setCar_img(obj.getString("car_img"));
                                    services.setYear(obj.getString("year"));
                                    registeredCarDBOsArraylist.add(services);
                                }

                                if (from_register == 0) {

//                                    edt_car.setText(registeredCarDBOsArraylist.get(0).getCarmake() + " " + registeredCarDBOsArraylist.get(0).getCarmodel());
//                                    edt_model.setText(registeredCarDBOsArraylist.get(0).getCarmodel());
//                                    edt_badge.setText(registeredCarDBOsArraylist.get(0).getCarbadge());
//                                    edt_registration.setText(registeredCarDBOsArraylist.get(0).getRegistration_number());
//                                    edt_autometic_or_manual.setText(registeredCarDBOsArraylist.get(0).getCar_type());
//                                    edt_year.setText(registeredCarDBOsArraylist.get(0).getYear());
                                    edt_car.setText(jobDetail_dbo.getCarmake() + " " + jobDetail_dbo.getCarmodel());
                                    edt_model.setText(jobDetail_dbo.getCarmodel());
                                    edt_badge.setText(jobDetail_dbo.getCarbadge());
                                    edt_registration.setText(jobDetail_dbo.getRegistration_number());
                                    edt_autometic_or_manual.setText(jobDetail_dbo.getCar_type());
                                    edt_year.setText(jobDetail_dbo.getYear());
                                }

                            } else {

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

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    private void showMakeDialog() {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = this.getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        TextView title = view.findViewById(R.id.title);
        title.setText("Select Car");
        ListView lv = view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edt_car.setText(registeredCarDBOsArraylist.get(i).getCarmake() + " " + registeredCarDBOsArraylist.get(i).getCarmodel());
                edt_model.setText(registeredCarDBOsArraylist.get(i).getCarmodel());
                edt_badge.setText(registeredCarDBOsArraylist.get(i).getCarbadge());
                edt_registration.setText(registeredCarDBOsArraylist.get(i).getRegistration_number());
                edt_autometic_or_manual.setText(registeredCarDBOsArraylist.get(i).getCar_type());
                edt_year.setText(registeredCarDBOsArraylist.get(i).getYear());
                car_id = registeredCarDBOsArraylist.get(i).getId();
                dialog.dismiss();
            }
        });
//        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomListAdapterDialog clad = new CustomListAdapterDialog(this, registeredCarDBOsArraylist);
        lv.setAdapter(clad);
        dialog.setContentView(view);
//        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public class CustomListAdapterDialog extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<RegisteredCarDBO> listData;

        public CustomListAdapterDialog(Context appContext, ArrayList<RegisteredCarDBO> list) {
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
            ChallengerHolder holder;
            if (row == null) {
                holder = new ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_dialog, parent, false);
                holder.tv_name = row.findViewById(R.id.tv_name);
                holder.tv_header = row.findViewById(R.id.tv_header);

                row.setTag(holder);
            } else {
                holder = (ChallengerHolder) row.getTag();
            }
            holder.tv_name.setText(listData.get(position).getCarmake() + " " + listData.get(position).getCarmodel());
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
            TextView tv_name, tv_header;
        }
    }
}
