package technource.autoreum.fragment;

import static technource.autoreum.activities.LoginScreen.FLAG_SIGNUP;
import static technource.autoreum.activities.LoginScreen.FLAG_USERTYPE;
import static technource.autoreum.helper.Constants.notify_count;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import technource.autoreum.R;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.activities.SignUpCarOwner;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.LoginType;
import technource.autoreum.helper.Constants.SIGN_UP;
import technource.autoreum.helper.Constants.USER_DETAILS;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.List_items;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SignUpDBO;

/**
 * Created by technource on 6/9/17.
 */

public class fragmentRegisterCar extends Fragment implements OnClickListener {

    public String Selected_Make_id = "", Selected_model_id = "", Selected_badge_id = "", Selected_Check = "";
    EditText edt_make, edt_model, edt_badge, edt_reg_number, edtyear;
    RelativeLayout ll_make, ll_model, ll_badge;
    LinearLayout add_more_car,ll_register_no,ll_year;
    CheckBox autoCheck, manualCheck;
    TextView RegisterBtn;
    View v;
    ArrayList<List_items> makers_list = new ArrayList<>();
    ArrayList<List_items> model_list = new ArrayList<>();
    ArrayList<List_items> badge_list = new ArrayList<>();
    Context appContext;
    boolean isAddCar = false;
    LoginDetail_DBO loginDetail_dbo;
    SignUpDBO data;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_signup_car, container, false);
        getViews();

        setOnCLickListener();
        data = ((SignUpCarOwner) appContext).data;

        if (Connectivity.isConnected(appContext)) {
            GetCarMakers();
        } else {
            ((SignUpCarOwner) appContext).showAlertDialog(getString(R.string.no_internet));
        }

        return v;
    }

    public void getViews() {
        appContext = getActivity();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        edt_make = (EditText) v.findViewById(R.id.edt_make);
        edt_model = (EditText) v.findViewById(R.id.edt_model);
        edt_badge = (EditText) v.findViewById(R.id.edt_badge);
        edt_reg_number = (EditText) v.findViewById(R.id.edt_reg_number);
        edtyear = (EditText) v.findViewById(R.id.edt_year);
        ll_make = (RelativeLayout) v.findViewById(R.id.ll_make);
        ll_model = (RelativeLayout) v.findViewById(R.id.ll_model);
        ll_badge = (RelativeLayout) v.findViewById(R.id.ll_badge);
        autoCheck = (CheckBox) v.findViewById(R.id.autoCheck);
        manualCheck = (CheckBox) v.findViewById(R.id.manualCheck);
        RegisterBtn = (TextView) v.findViewById(R.id.registerBtn);
        add_more_car = (LinearLayout) v.findViewById(R.id.add_more_car);
        ll_year = (LinearLayout) v.findViewById(R.id.ll_year);
        ll_register_no = (LinearLayout) v.findViewById(R.id.ll_register_no);
        autoCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCheck.setChecked(true);
                manualCheck.setChecked(false);
            }
        });

        manualCheck.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                autoCheck.setChecked(false);
                manualCheck.setChecked(true);
            }
        });
        edt_reg_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    HelperMethods.Valid(appContext,ll_register_no);
                }else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edtyear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length()>0){
                    HelperMethods.Valid(appContext,ll_year);
                }else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });

    }

    public void setOnCLickListener() {
        edt_make.setOnClickListener(this);
        edt_model.setOnClickListener(this);
        edt_badge.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
        add_more_car.setOnClickListener(this);
    }

    private void showMakeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_Make_id = makers_list.get(i).getId();
                edt_make.setText(makers_list.get(i).getName());

                Selected_model_id = "";
                edt_model.setText("");
                Selected_badge_id = "";
                edt_badge.setText("");
                HelperMethods.Valid(appContext,ll_make);

                dialog.dismiss();
                GetCarModels();
            }
        });

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4); //Set your widths

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;

        CustomListAdapterDialog clad = new CustomListAdapterDialog(getActivity(), makers_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void showModelDialog() {
        final Dialog dialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_model_id = model_list.get(i).getId();
                edt_model.setText(model_list.get(i).getName());
                HelperMethods.Valid(appContext,ll_model);
                GetBadgeList();
                dialog.dismiss();
            }
        });

        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4); //Set your widths

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;

        CustomListAdapterOther clad = new CustomListAdapterOther(getActivity(), model_list);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void showBadgeDialog() {
        final Dialog dialog = new Dialog(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_main, null);
        ListView lv = (ListView) view.findViewById(R.id.custom_list);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Selected_badge_id = badge_list.get(i).getId();
                edt_badge.setText(badge_list.get(i).getName());
                dialog.dismiss();
            }
        });
        WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4); //Set your widths

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());

        lp.width = width;
        lp.height = height;
        CustomListAdapterOther clad = new CustomListAdapterOther(getActivity(), badge_list);


        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_make:
                showMakeDialog();
                break;
            case R.id.edt_model:
                if (!Selected_Make_id.equals("")) {
                    edt_badge.setText("");
                    showModelDialog();
                } else {
                    HelperMethods.Valid(appContext,ll_make);
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.select_car_maker));
                }
                break;
            case R.id.edt_badge:
                if (!Selected_model_id.equals("")) {
                    if (!badge_list.isEmpty()) {
                        showBadgeDialog();
                    } else {
                        ((SignUpCarOwner) appContext)
                                .showAlertDialog(getString(R.string.no_badges));
                    }
                } else {
                    HelperMethods.Valid(appContext,ll_model);
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.select_car_model));
                }
                break;
            case R.id.registerBtn:
                isAddCar = false;
                if (isValidate()) {
                    if (autoCheck.isChecked()) {
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
                    if (autoCheck.isChecked()) {
                        Selected_Check = "Automatic";
                    } else {
                        Selected_Check = "Manual";
                    }
                    SignUpRegisterCar();
                }
                break;
        }
    }

    public void SignUpRegisterCar() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(SIGN_UP.SERVICE_NAME, "register_car");
        params.put(LoginType.MAKE_ID, Selected_Make_id);
        params.put(LoginType.MODEL_ID, Selected_model_id);
        params.put(LoginType.CAR_TRANS, Selected_Check);
        params.put(LoginType.BADGE_ID, Selected_badge_id);
        params.put(LoginType.REG_NO, edt_reg_number.getText().toString());
        params.put(LoginType.YEAR, edtyear.getText().toString());

        AppLog.Log("TAG", "Params : " + new JSONObject(params).toString());
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext).showLoadingDialog(false);
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
                                    if (FLAG_SIGNUP.equalsIgnoreCase("1")) {
                                        Intent i = new Intent(appContext, DashboardScreen.class);
                                        i.addFlags(Constants.INTENT_FLAGS);
                                        startActivity(i);
                                        ((SignUpCarOwner) appContext).finish();
                                        ((SignUpCarOwner) appContext).activityTransition();
                                    } else {
                                        SocialLoginCheck();
                                    }
                                }
                            } else {
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ((SignUpCarOwner) appContext).showLoadingDialog(false);
            }
        });

        queue.add(jsonObjReq);
    }

    public void SetClearData() {
        edt_make.setText("");
        edt_model.setText("");
        edt_badge.setText("");
        autoCheck.setChecked(true);
        manualCheck.setChecked(false);
        edt_reg_number.setText("");
        edtyear.setText("");
        Selected_Make_id = "";
        Selected_badge_id = "";
        Selected_model_id = "";
        Selected_Check = "";
        model_list.clear();
        badge_list.clear();
    }

    public boolean isValidate() {
        String makers, models, badges, regnumber, year;
        boolean isFirst=true;
        makers = edt_make.getText().toString();
        models = edt_model.getText().toString();
        badges = edt_badge.getText().toString();
        regnumber = edt_reg_number.getText().toString();
        year = edtyear.getText().toString();

        if (makers != null && makers.trim().length() > 0) {
            HelperMethods.Valid(appContext,ll_make);
        } else {
            edt_make.requestFocus();
            HelperMethods.ValidateFields(appContext,ll_make);
            if(isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.select_car_makers));
                isFirst=false;
            }

        }

        if (models != null && models.trim().length() > 0) {
            HelperMethods.Valid(appContext,ll_model);
        } else {
            edt_model.requestFocus();
            HelperMethods.ValidateFields(appContext,ll_model);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.please_select_model));
                isFirst=false;
            }

        }

        if (regnumber != null && regnumber.trim().length() > 0) {
            HelperMethods.Valid(appContext,ll_register_no);
        } else {
            edt_reg_number.requestFocus();
            HelperMethods.ValidateFields(appContext,ll_register_no);
            if (isFirst){
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.emter_reg_number));
                isFirst=false;
            }

        }

//    if (badges != null && badges.trim().length() > 0) {
//    } else {
//      edt_badge.requestFocus();
//      ((SignUpCarOwner) appContext)
//          .showAlertDialog(getString(R.string.select_badge));
//      return false;
//    }

        if (year != null && year.trim().length() > 0) {
            HelperMethods.Valid(appContext,ll_year);
        } else {
            edtyear.requestFocus();
            HelperMethods.ValidateFields(appContext,ll_year);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.please_enter_year));
                isFirst=false;
            }

        }

        return isFirst;
    }

    public void GetCarMakers() {
        ((SignUpCarOwner) appContext)
                .showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "carmakes");

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext)
                                .showLoadingDialog(false);
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
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
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

    public void GetCarModels() {
        model_list.clear();
        ((SignUpCarOwner) appContext)
                .showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "carmodels");
        params.put(LoginType.MAKE_ID, Selected_Make_id);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext)
                                .showLoadingDialog(false);
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
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
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

    public void GetBadgeList() {
        badge_list.clear();
        ((SignUpCarOwner) appContext)
                .showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "carbadges");
        params.put(LoginType.MODEL_ID, Selected_model_id);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext)
                                .showLoadingDialog(false);
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

    public void SocialLoginCheck() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "login");
        params.put(LoginType.LOGIN_TYPE, LoginType.SOCIAL);
        params.put(LoginType.SOCIAL_ID, data.getSocialid());
        params.put(LoginType.DEVICE_TYPE, "1");
        params.put(LoginType.DEVICE_TOKEN, HelperMethods.getDeviceTokenFCM());
        params.put(LoginType.USER_TYPE, FLAG_USERTYPE);

        AppLog.Log("params", "" + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext).showLoadingDialog(false);
                        AppLog.Log("Response-->", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(appContext, "You are logged in.", Toast.LENGTH_LONG).show();
                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                                loginDetail_dbo
                                        .setUserid(response.getString(USER_DETAILS.USER_ID));
                                loginDetail_dbo
                                        .setJWTToken(response.getString(USER_DETAILS.JWT_TOKEN));
                                // loginDetail_dbo.setEmail(response.getString(USER_DETAILS.EMAIL));
                                JSONObject jobj = response.getJSONObject("data");
                                loginDetail_dbo.setUser_Type(FLAG_USERTYPE);
                                loginDetail_dbo.setLogin_type(jobj.getString("signup_type"));
                                loginDetail_dbo.setFirst_name(jobj.getString(Constants.USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(jobj.getString(Constants.USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(jobj.getString(Constants.LoginType.IMAGE));
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                MyPreference user_preference = new MyPreference(appContext);
                                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                                user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);

                                Intent intent = new Intent(appContext, DashboardScreen.class);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);
                            } else {
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
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

    public void LoginCheck() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "login");
        params.put(LoginType.LOGIN_TYPE, LoginType.NORMAL);
        params.put(LoginType.USERNAME, ((SignUpCarOwner) appContext).Email);
        params.put(LoginType.PASSWORD, ((SignUpCarOwner) appContext).Password);
        params.put(LoginType.DEVICE_TOKEN, HelperMethods.getDeviceTokenFCM());
        params.put(LoginType.DEVICE_TYPE, "1");
        params.put(LoginType.USER_TYPE, FLAG_USERTYPE);
        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((SignUpCarOwner) appContext).showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                                loginDetail_dbo
                                        .setJWTToken(response.getString(USER_DETAILS.JWT_TOKEN));

                                JSONObject json = new JSONObject(response.getString("data"));

                                loginDetail_dbo.setUserid(json.getString(LoginType.ID));
                                loginDetail_dbo.setFirst_name(json.getString(Constants.USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(Constants.USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(Constants.LoginType.IMAGE));

                                loginDetail_dbo.setUser_Type(FLAG_USERTYPE);
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
                                loginDetail_dbo.setGarage_balance(json.optString("garage_balance"));
                                if (json.optString("remind_flag").equalsIgnoreCase("yes")) {
                                    loginDetail_dbo.setRemindMeLater(true);
                                    JSONObject jobj = json.getJSONObject("job_detail");
                                    loginDetail_dbo.setcJObId(jobj.getString("id"));
                                    loginDetail_dbo.setJobTitle(jobj.getString("job_title"));
                                }
                                if (!json.getString("notify_count").equalsIgnoreCase("")) {
                                    notify_count = json.getString("notify_count");
                                }
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                MyPreference user_preference = new MyPreference(appContext);
                                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                                user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);

                                Intent intent = new Intent(appContext, DashboardScreen.class);
                                startActivity(intent);
                                ((SignUpCarOwner) appContext).finish();
                                ((SignUpCarOwner) appContext).activityTransition();
                            } else {
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
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
            final ChallengerHolder holder;

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
}
