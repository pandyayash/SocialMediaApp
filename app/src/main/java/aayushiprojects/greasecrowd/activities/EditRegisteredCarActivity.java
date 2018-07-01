package aayushiprojects.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.List_items;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.RegisteredCarDBO;

public class
EditRegisteredCarActivity extends BaseActivity {

    TextView tv;
    RegisteredCarDBO registeredCarDBO;

    public String Selected_Make_id = "", Selected_model_id = "", Selected_badge_id = "", Selected_Check = "";
    EditText edt_make, edt_model, edt_badge, edt_reg_number, edtyear, edt_km;
    RelativeLayout ll_make, ll_model, ll_badge;
    LinearLayout add_more_car, autometic, manual, ll_back, ll_km;
    TextView RegisterBtn, autotxt, manualtxt;
    View v;
    ArrayList<List_items> makers_list = new ArrayList<>();
    ArrayList<List_items> model_list = new ArrayList<>();
    ArrayList<List_items> badge_list = new ArrayList<>();
    Context appContext;
    int check;
    String posiotion;
    boolean isAddCar = false;
    LoginDetail_DBO loginDetail_dbo;
    boolean is_carmake = false, is_model = false, is_badge = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_registered_car);

        setHeader("Edit Your Car");
        Intent intent = getIntent();

        setfooter("jobs");
        setlistenrforfooter();
        if (intent != null) {
            registeredCarDBO = intent.getParcelableExtra("registeredcar");
            posiotion = intent.getStringExtra("position");
        }
        getviews();
        setOnCLickListener();
        setHomeFooter(appContext);
        if (Connectivity.isConnected(appContext)) {
            GetregisteredData();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    private void getviews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        edt_make = findViewById(R.id.edt_make);
        edt_model = findViewById(R.id.edt_model);
        edt_badge = findViewById(R.id.edt_badge);
        edt_reg_number = findViewById(R.id.edt_reg_number);
        edtyear = findViewById(R.id.edt_year);
        edt_km = findViewById(R.id.edt_km);
        ll_make = findViewById(R.id.ll_make);
        ll_model = findViewById(R.id.ll_model);
        ll_badge = findViewById(R.id.ll_badge);
        RegisterBtn = findViewById(R.id.registerBtn);
        add_more_car = findViewById(R.id.add_more_car);
        autometic = findViewById(R.id.autometic);
        manual = findViewById(R.id.manual);
        ll_back = findViewById(R.id.ll_back);
        ll_km = findViewById(R.id.ll_km);
        autotxt = findViewById(R.id.autoText);
        manualtxt = findViewById(R.id.manualTxt);

    }

    public void setOnCLickListener() {
        edt_make.setOnClickListener(this);
        edt_model.setOnClickListener(this);
        edt_badge.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        add_more_car.setOnClickListener(this);
        autotxt.setOnClickListener(this);
        manualtxt.setOnClickListener(this);
        ll_back.setOnClickListener(this);

    }

    public void GetregisteredData() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, "get_car");
        params.put("car_id", String.valueOf(registeredCarDBO.getId()));

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

                                JSONObject jobj = response.getJSONObject("data");
                                edt_make.setText(jobj.getString("carmake"));
                                edt_model.setText(jobj.getString("carmodel"));
                                edt_badge.setText(jobj.getString("carbadge"));
                                edt_km.setText(jobj.getString("km"));
                                Selected_Make_id = jobj.getString("manufacture_id");
                                Selected_model_id = jobj.getString("model_id");
                                Selected_badge_id = jobj.getString("badge_id");
                                if (jobj.getString("car_type").equalsIgnoreCase("Automatic")) {
                                    check = 1;
                                    autometic.setBackgroundColor(getResources().getColor(R.color.color_Splash));
                                    autotxt.setTextColor(getResources().getColor(R.color.white));
                                    manualtxt.setTextColor(getResources().getColor(R.color.black_light_text));
                                    manual.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));

                                } else {
                                    check = 2;
                                    manual.setBackgroundColor(getResources().getColor(R.color.color_Splash));
                                    manualtxt.setTextColor(getResources().getColor(R.color.white));
                                    autotxt.setTextColor(getResources().getColor(R.color.black_light_text));
                                    autometic.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                                }
                                edt_reg_number.setText(jobj.getString("registration_number"));
                                edtyear.setText(jobj
                                        .getString("year"));


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

            }
        });

        queue.add(jsonObjReq);

    }

    public void GetCarMakers() {
        makers_list = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, "carmakes");

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject jobj = new JSONObject(response.getString("carmakes"));
                                JSONArray popularArray = new JSONArray(jobj.getString("popular"));
                                JSONArray allArray = new JSONArray(jobj.getString("all"));

                                for (int i = 0; i < popularArray.length(); i++) {
                                    JSONObject job2 = popularArray.getJSONObject(i);
                                    List_items model = new List_items();
                                    model.setName(job2.getString("name"));
                                    model.setId(job2.getString("id"));
                                    model.setPopular(1);
                                    makers_list.add(model);
                                }

                                for (int i = 0; i < allArray.length(); i++) {
                                    JSONObject job2 = allArray.getJSONObject(i);
                                    List_items model = new List_items();
                                    model.setName(job2.getString("name"));
                                    model.setId(job2.getString("id"));
                                    model.setPopular(0);
                                    makers_list.add(model);
                                }


                                Selected_model_id = "";
                                edt_model.setText("");
                                Selected_badge_id = "";
                                edt_badge.setText("");
                                showMakeDialog();
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

            }
        });

        queue.add(jsonObjReq);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.edt_make:
                GetCarMakers();

                break;
            case R.id.ll_back:
                finish();
                activityTransition();
                break;
            case R.id.edt_model:
                if (!edt_make.getText().toString().equalsIgnoreCase("")) {
                    GetCarModels();
                } else {
                    showAlertDialog(getString(R.string.select_car_maker));
                }
                break;
            case R.id.edt_badge:
                if (!edt_model.getText().toString().equalsIgnoreCase("") && !edt_make.getText().toString().equalsIgnoreCase("")) {
                    GetBadgeList();
                } else {
                    showAlertDialog(getString(R.string.select_car_model));
                }
                break;
            case R.id.autoText:
                check = 1;
                autometic.setBackgroundColor(getResources().getColor(R.color.color_Splash));
                autotxt.setTextColor(getResources().getColor(R.color.white));
                manualtxt.setTextColor(getResources().getColor(R.color.black_light_text));
                manual.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                break;
            case R.id.manualTxt:
                check = 2;
                manual.setBackgroundColor(getResources().getColor(R.color.color_Splash));
                manualtxt.setTextColor(getResources().getColor(R.color.white));
                autotxt.setTextColor(getResources().getColor(R.color.black_light_text));
                autometic.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                break;


            case R.id.registerBtn:
                isAddCar = false;
                if (isValidate()) {
                    if (check == 1) {
                        Selected_Check = "Automatic";
                    } else {
                        Selected_Check = "Manual";
                    }
                    SignUpRegisterCar();

                }
                break;
        }
    }

    private void showMakeDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_Make_id = makers_list.get(i).getId();
                edt_make.setText(makers_list.get(i).getName());

                Selected_model_id = "";
                edt_model.setText("");
                Selected_badge_id = "";
                edt_badge.setText("");

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

        CustomListAdapterDialog clad = new CustomListAdapterDialog(this, makers_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void GetCarModels() {
        model_list = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, WebServiceURLs.GET_CAR_MODELS);
        params.put(Constants.LoginType.MAKE_ID, Selected_Make_id);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONArray modelArray = new JSONArray(response.getString("carmodels"));

                                for (int i = 0; i < modelArray.length(); i++) {
                                    JSONObject job2 = modelArray.getJSONObject(i);
                                    List_items model = new List_items();
                                    model.setName(job2.getString("name"));
                                    model.setId(job2.getString("id"));
                                    model_list.add(model);
                                }
                                Selected_badge_id = "";
                                edt_badge.setText("");
                                showModelDialog();
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

            }
        });

        queue.add(jsonObjReq);
    }

    private void showModelDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_model_id = model_list.get(i).getId();
                edt_model.setText(model_list.get(i).getName());
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

        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, model_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void GetBadgeList() {
        badge_list = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, "carbadges");
        params.put(Constants.LoginType.MODEL_ID, Selected_model_id);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONArray modelArray = new JSONArray(response.getString("carbadges"));
                                if (modelArray.length() > 0) {
                                    for (int i = 0; i < modelArray.length(); i++) {
                                        JSONObject job2 = modelArray.getJSONObject(i);
                                        List_items model = new List_items();
                                        model.setName(job2.getString("name"));
                                        model.setId(job2.getString("id"));
                                        badge_list.add(model);

                                    }
                                    showBadgeDialog();
                                    AppLog.Log("TAG", "Size of arraybadge : " + modelArray.length());
                                } else {

                                    showAlertDialog(getString(R.string.no_badges));
                                }


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
            }
        });

        queue.add(jsonObjReq);
    }

    private void showBadgeDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_badge_id = badge_list.get(i).getId();
                edt_badge.setText(badge_list.get(i).getName());
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

        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, badge_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public boolean isValidate() {
        String makers, models, badges, regnumber, year, km;

        makers = edt_make.getText().toString();
        models = edt_model.getText().toString();
        badges = edt_badge.getText().toString();
        regnumber = edt_reg_number.getText().toString();
        year = edtyear.getText().toString();
        km = edt_km.getText().toString();

        if (makers != null && makers.trim().length() > 0) {
        } else {
            edt_make.requestFocus();

            showAlertDialog(getString(R.string.select_car_makers));
            return false;
        }

        if (models != null && models.trim().length() > 0) {
        } else {
            edt_model.requestFocus();

            showAlertDialog(getString(R.string.please_select_model));
            return false;
        }

        if (regnumber != null && regnumber.trim().length() > 0) {
        } else {
            edt_reg_number.requestFocus();
            showAlertDialog(getString(R.string.emter_reg_number));
            return false;
        }

        if (badges != null && badges.trim().length() > 0) {
        } else {
            edt_badge.requestFocus();
            showAlertDialog(getString(R.string.select_badge));
            return false;
        }

        if (year != null && year.trim().length() > 0) {
        } else {
            edtyear.requestFocus();
            showAlertDialog(getString(R.string.please_enter_year));
            return false;
        }

        if (km != null && km.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_km);
        } else {
            edt_km.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_km);
            showAlertDialog(getString(R.string.please_enter_km));
            return false;
        }
        return true;
    }

    public void SignUpRegisterCar() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "edit_car");
        params.put("car_id", String.valueOf(registeredCarDBO.getId()));
        params.put(Constants.LoginType.MAKE_ID, Selected_Make_id);
        params.put(Constants.LoginType.MODEL_ID, Selected_model_id);
        params.put(Constants.LoginType.CAR_TRANS, Selected_Check);
        params.put(Constants.LoginType.BADGE_ID, Selected_badge_id);
        params.put(Constants.LoginType.REG_NO, edt_reg_number.getText().toString());
        params.put(Constants.LoginType.YEAR, edtyear.getText().toString());
        params.put(Constants.LoginType.KM, edt_km.getText().toString());

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {

                            registeredCarDBO.setCarmake(edt_make.getText().toString());
                            registeredCarDBO.setCarmodel(edt_model.getText().toString());
                            registeredCarDBO.setCarbadge(edt_badge.getText().toString());
                            registeredCarDBO.setCar_type(Selected_Check);
                            registeredCarDBO.setRegistration_number(edt_reg_number.getText().toString());
                            registeredCarDBO.setYear(edtyear.getText().toString());
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent();
                                intent.putExtra("data", registeredCarDBO);
                                intent.putExtra("position", posiotion);
                                setResult(RESULT_OK, intent);
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

    public class CustomListAdapterOther extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<List_items> listData;

        public CustomListAdapterOther(Context appContext, ArrayList<List_items> list) {
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

            List_items model = listData.get(position);
            if (row == null) {
                holder = new ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);

                holder.tv_name = row.findViewById(R.id.tv_name);

                row.setTag(holder);
            } else {
                holder = (ChallengerHolder) row.getTag();
            }

            holder.tv_name.setText(model.getName());
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

    public class CustomListAdapterDialog extends BaseAdapter {

        Context context;
        LayoutInflater layoutInflater;
        boolean popularFlag = false, AllFlag = false;
        private ArrayList<List_items> listData;

        public CustomListAdapterDialog(Context appContext, ArrayList<List_items> list) {
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

            List_items model = listData.get(position);
            if (row == null) {
                holder = new ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_dialog, parent, false);

                holder.tv_name = row.findViewById(R.id.tv_name);
                holder.tv_header = row.findViewById(R.id.tv_header);

                row.setTag(holder);
            } else {
                holder = (ChallengerHolder) row.getTag();
            }

            if (model.getPopular() == 1) {
                if (popularFlag == false) {
                    holder.tv_header.setVisibility(View.VISIBLE);
                    holder.tv_header.setText("Popular Makes");
                }
                popularFlag = true;
            } else if (model.getPopular() == 0) {
                if (AllFlag == false) {
                    holder.tv_header.setVisibility(View.VISIBLE);
                    holder.tv_header.setText("All Makes");
                }
                AllFlag = true;
            } else {
                holder.tv_header.setVisibility(View.GONE);
            }

            holder.tv_name.setText(model.getName());
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

    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        finish();
        activityTransition();
    }
}