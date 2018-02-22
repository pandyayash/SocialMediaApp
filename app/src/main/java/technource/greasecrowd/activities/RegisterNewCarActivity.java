package technource.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
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

import technource.greasecrowd.R;
import technource.greasecrowd.fragment.fragmentRegisterCar;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.List_items;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.SignUpDBO;

import static technource.greasecrowd.activities.LoginScreen.FLAG_SIGNUP;

public class RegisterNewCarActivity extends BaseActivity {


    public String Selected_Make_id = "", Selected_model_id = "", Selected_badge_id = "", Selected_Check = "";
    EditText edt_make, edt_model, edt_badge, edt_reg_number, edtyear;
    RelativeLayout ll_make, ll_model, ll_badge;
    LinearLayout add_more_car, autometic, manual;
    TextView RegisterBtn, autotxt, manualtxt;
    View v;
    ArrayList<List_items> makers_list = new ArrayList<>();
    ArrayList<List_items> model_list = new ArrayList<>();
    ArrayList<List_items> badge_list = new ArrayList<>();
    Context appContext;
    int check;
    boolean isAddCar = false;
    LoginDetail_DBO loginDetail_dbo;
    LinearLayout ll_back;
    SignUpDBO data;
    boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_new_car);
        setHeader("Register New Car");
        getViews();
        setOnCLickListener();
        if (Connectivity.isConnected(appContext)) {
            GetCarMakers();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    public void getViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        edt_make = (EditText) findViewById(R.id.edt_make);
        edt_model = (EditText) findViewById(R.id.edt_model);
        edt_badge = (EditText) findViewById(R.id.edt_badge);
        edt_reg_number = (EditText) findViewById(R.id.edt_reg_number);
        edtyear = (EditText) findViewById(R.id.edt_year);
        ll_make = (RelativeLayout) findViewById(R.id.ll_make);
        ll_model = (RelativeLayout) findViewById(R.id.ll_model);
        ll_badge = (RelativeLayout) findViewById(R.id.ll_badge);
        RegisterBtn = (TextView) findViewById(R.id.registerBtn);
        add_more_car = (LinearLayout) findViewById(R.id.add_more_car);
        autometic = (LinearLayout) findViewById(R.id.autometic);
        manual = (LinearLayout) findViewById(R.id.manual);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        autotxt = (TextView) findViewById(R.id.autoText);
        manualtxt = (TextView) findViewById(R.id.manualTxt);
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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_make:
                showMakeDialog();
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.edt_model:
                if (!Selected_Make_id.equals("")) {
                    edt_badge.setText("");
                    showModelDialog();
                } else {
                    showAlertDialog(getString(R.string.select_car_maker));
                }
                break;
            case R.id.edt_badge:
                if (!Selected_model_id.equals("")) {

                    if (!badge_list.isEmpty()) {
                        showBadgeDialog();
                    } else {
                        showAlertDialog(getString(R.string.no_badges));
                    }
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
            case R.id.add_more_car:
                isAddCar = true;
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
        View view = this.getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Select make");
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
                GetCarModels();
            }
        });

//        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomListAdapterDialog clad = new CustomListAdapterDialog(this, makers_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
//        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void showModelDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Model");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_model_id = model_list.get(i).getId();
                edt_model.setText(model_list.get(i).getName());

                GetBadgeList();
                dialog.dismiss();
            }
        });

//        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, model_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
//        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void GetCarMakers() {
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

    public boolean isValidate() {
        String makers, models, badges, regnumber, year;

        makers = edt_make.getText().toString();
        models = edt_model.getText().toString();
        badges = edt_badge.getText().toString();
        regnumber = edt_reg_number.getText().toString();
        year = edtyear.getText().toString();

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

//        if (badges != null && badges.trim().length() > 0) {
//        } else {
//            edt_badge.requestFocus();
//            showAlertDialog(getString(R.string.select_badge));
//            return false;
//        }

        if (year != null && year.trim().length() > 0) {
        } else {
            edtyear.requestFocus();
            showAlertDialog(getString(R.string.please_enter_year));
            return false;
        }

        return true;
    }

    public void GetBadgeList() {
        badge_list.clear();
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
                                for (int i = 0; i < modelArray.length(); i++) {
                                    JSONObject job2 = modelArray.getJSONObject(i);
                                    List_items model = new List_items();
                                    model.setName(job2.getString("name"));
                                    model.setId(job2.getString("id"));
                                    badge_list.add(model);
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

    public void SignUpRegisterCar() {
        flag = true;
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "register_car");
        params.put(Constants.LoginType.MAKE_ID, Selected_Make_id);
        params.put(Constants.LoginType.MODEL_ID, Selected_model_id);
        params.put(Constants.LoginType.CAR_TRANS, Selected_Check);
        params.put(Constants.LoginType.BADGE_ID, Selected_badge_id);
        params.put(Constants.LoginType.REG_NO, edt_reg_number.getText().toString());
        params.put(Constants.LoginType.YEAR, edtyear.getText().toString());

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
                                Toast
                                        .makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT)
                                        .show();
                                if (isAddCar) {
                                    SetClearData();
                                } else {
                                    setResult(RESULT_OK);
                                    finish();
                                    activityTransition();
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
                showLoadingDialog(false);
            }
        });

        queue.add(jsonObjReq);
    }

    public void SetClearData() {
        edt_make.setText("");
        edt_model.setText("");
        edt_badge.setText("");
        autometic.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
        manual.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
        autotxt.setTextColor(getResources().getColor(R.color.black_light_text));
        manualtxt.setTextColor(getResources().getColor(R.color.black_light_text));
        edt_reg_number.setText("");
        edtyear.setText("");
        Selected_Make_id = "";
        Selected_badge_id = "";
        Selected_model_id = "";
        Selected_Check = "";
        model_list.clear();
        badge_list.clear();
    }

    private void showBadgeDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText("Select Badge");

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_badge_id = badge_list.get(i).getId();
                edt_badge.setText(badge_list.get(i).getName());
                dialog.dismiss();
            }
        });

//        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//
//        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, badge_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
//        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void GetCarModels() {
        model_list.clear();
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

                holder.tv_name = (TextView) row.findViewById(R.id.tv_name);
                holder.tv_header = (TextView) row.findViewById(R.id.tv_header);

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

                holder.tv_name = (TextView) row.findViewById(R.id.tv_name);

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

    @Override
    public void onBackPressed() {
        if (flag) {
            setResult(RESULT_OK);
            finish();
            activityTransition();
        } else {
            setResult(RESULT_CANCELED);
            finish();
            activityTransition();
        }
    }
}
