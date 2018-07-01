package aayushiprojects.greasecrowd.activities.PostJob;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.BaseActivity;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.DroppOffLocationDbo;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.SubCatogoryDBO;

/**
 * Created by technource on 21/12/17.
 */

public class EditJobStepThree extends BaseActivity {
    Context appContext;
    private RecyclerView recyclerView;
    ArrayList<SubCatogoryDBO> catogoriesDBOArrayList;
    ArrayList<SubCatogoryDBO> list_subcatogory;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    String sub_category_id = "";
    String jwt, job_id, child_category_id = "";
    LoginDetail_DBO loginDetail_dbo;
    LinearLayout ll_continue, help_image, ll_back_button, ll_cancel, rl_drop_off_location, ll_back, ll_relevent, ll_special_case, ll_sub_category, ll_additional_inclusion, ll_additional, ll_additional_1, ll_drop_off_location, ll_fleet_managemnet, ll_include_roadside, ll_include_standard;
    LinearLayout rl_sub_category, ll_dummy_space;
    EditText edt_sub_category, ty_spec, ty_bran, ty_mod;
    TextView tv_additional, edt_drop_off_location, current_category, no_ty, txt_include_roadside, txt_include_standard, txtCount;
    boolean flag = false;
    boolean adddloc = false;
    CheckBox newslettr;
    int additional_flag = 1;
    String additional = "", cat_id = "", placeholder = "", isnews_ = "1";
    ImageView tyre_image;
    ArrayList<DroppOffLocationDbo> droppOffLocationDboArrayList;
    int raodSideSelected = 0;
    int standardSelected = 0;
    int minteger = 0;
    ImageView iv_minus, iv_plus;
    String main_cat_id = "";
    JobDetail_DBO jobDetail_dbo;
    boolean isPending = false;
    NestedScrollView nestedScroll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job_step_three);
        getviews();
        isPending = jobDetail_dbo.isPending();
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
        //setJobDetailsFooter(getApplicationContext());
        getAndSetData();
        setClickListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setPostJObFooter(this);
    }

    public void getviews() {
        edt_sub_category = findViewById(R.id.edt_sub_category);
        tyre_image = findViewById(R.id.tyre_image);
        edt_drop_off_location = findViewById(R.id.edt_drop_off_location);
        rl_drop_off_location = findViewById(R.id.rl_drop_off_location);
        ty_spec = findViewById(R.id.ty_spec);
        ty_bran = findViewById(R.id.ty_bran);
        ty_mod = findViewById(R.id.ty_mod);
        no_ty = findViewById(R.id.no_ty);
        tv_additional = findViewById(R.id.tv_additional);
        current_category = findViewById(R.id.current_category);
        ll_drop_off_location = findViewById(R.id.ll_drop_off_location);
        rl_sub_category = findViewById(R.id.rl_sub_category);
        ll_dummy_space = findViewById(R.id.ll_dummy_space);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        nestedScroll = findViewById(R.id.nestedScroll);
        nestedScroll.scrollTo(0, 0);
        appContext = this;
        catogoriesDBOArrayList = new ArrayList<>();

        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        jwt = loginDetail_dbo.getJWTToken();
        ll_continue = findViewById(R.id.ll_continue);
        ll_cancel = findViewById(R.id.ll_cancel);
        ll_back = findViewById(R.id.ll_back);
        ll_back_button = findViewById(R.id.ll_back_button);
        help_image = findViewById(R.id.help_image);
        ll_relevent = findViewById(R.id.ll_relevent);
        ll_special_case = findViewById(R.id.ll_special_case);
        ll_sub_category = findViewById(R.id.ll_sub_category);
        ll_additional_inclusion = findViewById(R.id.ll_additional_inclusion);
        ll_additional = findViewById(R.id.ll_additional);
        ll_additional_1 = findViewById(R.id.ll_additional_1);
        ll_fleet_managemnet = findViewById(R.id.ll_fleet_managemnet);
        ll_include_roadside = findViewById(R.id.ll_include_roadside);
        ll_include_standard = findViewById(R.id.ll_include_standard);
        txt_include_roadside = findViewById(R.id.txt_include_roadside);
        txt_include_standard = findViewById(R.id.txt_include_standard);
        txtCount = findViewById(R.id.txtCount);
        iv_minus = findViewById(R.id.iv_minus);
        iv_plus = findViewById(R.id.iv_plus);
        newslettr = findViewById(R.id.newslettr);

        droppOffLocationDboArrayList = new ArrayList<>();
        list_subcatogory = new ArrayList<>();
        catogoriesDBOArrayList = new ArrayList<>();
    }

    public void getAndSetData() {
        list_subcatogory = new ArrayList<>();

        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            main_cat_id = intent.getStringExtra("category_id");
            additional = intent.getStringExtra("additional");
            droppOffLocationDboArrayList = intent.getParcelableArrayListExtra("drop_off");
            catogoriesDBOArrayList = intent.getParcelableArrayListExtra("subcategory");
            current_category.setText(intent.getStringExtra("category_name"));
            cat_id = catogoriesDBOArrayList.get(0).getParent_id();
            AppLog.Log("Category_id", "" + cat_id);
            AppLog.Log("main_cat_id", "" + main_cat_id);
            AppLog.Log("job_cat_id", "" + jobDetail_dbo.getCategory_id());
            if (cat_id.equalsIgnoreCase(jobDetail_dbo.getCategory_id())) {

                edt_sub_category.setText(jobDetail_dbo.getSubcatname());
                sub_category_id = jobDetail_dbo.getSubcategory_id();
                //Toast.makeText(appContext, sub_category_id, Toast.LENGTH_SHORT).show();
                setAdapter();

            }
            for (int i = 0; i < catogoriesDBOArrayList.size(); i++) {
                if (catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("5")) {
                    flag = true;
                    ty_spec.requestFocus();
                    ll_dummy_space.setVisibility(View.GONE);

                } else if (catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("6")) {
                    adddloc = true;
                    ll_dummy_space.setVisibility(View.GONE);

                }
                //1-2-7-8-12-14-15-16-17-18-19-20
                if (catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("1") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("3") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("7") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("8") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("12") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("14") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("15") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("16") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("17") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("18") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("19") ||
                        catogoriesDBOArrayList.get(i).getParent_id().equalsIgnoreCase("20")) {

                    ll_dummy_space.setVisibility(View.VISIBLE);
                } else {

                }
            }
            if (cat_id.equals("13")) {
                ll_dummy_space.setVisibility(View.GONE);
                ll_fleet_managemnet.setVisibility(View.VISIBLE);
                if (jobDetail_dbo.getInc_roadside_assist().equalsIgnoreCase("YES")) {
                    ll_include_roadside.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    txt_include_roadside.setTextColor(getResources().getColor(R.color.white));

                } else {
                    ll_include_roadside.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                    txt_include_roadside.setTextColor(getResources().getColor(R.color.boardarcolor));
                }
                if (jobDetail_dbo.getInc_std_log_service().equalsIgnoreCase("YES")) {
                    ll_include_standard.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                    txt_include_standard.setTextColor(getResources().getColor(R.color.white));
                } else {
                    ll_include_standard.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                    txt_include_standard.setTextColor(getResources().getColor(R.color.boardarcolor));
                }
                txtCount.setText(jobDetail_dbo.getNumber_of_vehicles());
            } else {
                ll_fleet_managemnet.setVisibility(View.GONE);
            }
            if (adddloc) {
                setAdapterForSpecial();
                ll_special_case.setVisibility(View.GONE);
                ll_relevent.setVisibility(View.VISIBLE);
                ll_drop_off_location.setVisibility(View.VISIBLE);
                ll_additional_inclusion.setVisibility(View.GONE);
                ll_sub_category.setVisibility(View.GONE);
            }
            if (flag) {
                catogoriesDBOArrayList.remove(catogoriesDBOArrayList.size() - 1);
                ll_sub_category.setVisibility(View.GONE);
                help_image.setVisibility(View.GONE);
                setAdapterForSpecial();
                ll_special_case.setVisibility(View.VISIBLE);
                tyre_image.setVisibility(View.VISIBLE);
                ll_relevent.setVisibility(View.VISIBLE);
                ll_additional_inclusion.setVisibility(View.VISIBLE);
                ll_drop_off_location.setVisibility(View.GONE);
                tv_additional.setText(additional);
                ty_spec.setText(jobDetail_dbo.getTyre_detail_and_spec());
                ty_mod.setText(jobDetail_dbo.getCurrent_tyre_model());
                ty_bran.setText(jobDetail_dbo.getCurrent_tyre_brand());
                no_ty.setText(jobDetail_dbo.getNo_of_tyres());
            }
        }


    }

    public void setClickListener() {
        rl_sub_category.setOnClickListener(this);
        edt_sub_category.setOnClickListener(this);
        ll_continue.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        no_ty.setOnClickListener(this);
        ll_additional_inclusion.setOnClickListener(this);
        rl_drop_off_location.setOnClickListener(this);
        edt_drop_off_location.setOnClickListener(this);
        ll_cancel.setOnClickListener(this);
        tyre_image.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
        help_image.setOnClickListener(this);
        ll_include_roadside.setOnClickListener(this);
        ll_include_standard.setOnClickListener(this);
        iv_plus.setOnClickListener(this);
        iv_minus.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.rl_sub_category:
            case R.id.edt_sub_category:
                openPopup();
                break;
            case R.id.edt_drop_off_location:
            case R.id.rl_drop_off_location:
                openPopupForAddress();
                break;
            case R.id.no_ty:
                openPopupForTyre();
                break;
            case R.id.tyre_image:
                openPopupForImage();
                break;
            case R.id.ll_continue:
                if (newslettr.isChecked()) {
                    isnews_ = "1";
                } else {
                    isnews_ = "0";
                }
                CheckData();
                break;
            case R.id.ll_additional_inclusion:
                if (additional_flag == 1) {
                    ll_additional_1.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                    tv_additional.setTextColor(getResources().getColor(R.color.white));
                    additional_flag = 2;
                } else if (additional_flag == 2) {
                    ll_additional_1.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                    tv_additional.setTextColor(getResources().getColor(R.color.black_light_text));
                    additional_flag = 1;
                }
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
            case R.id.ll_include_roadside:
                if (ll_fleet_managemnet.getVisibility() == View.VISIBLE) {
                    if (raodSideSelected == 0) {
                        ll_include_roadside.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        txt_include_roadside.setTextColor(getResources().getColor(R.color.white));
                        raodSideSelected = 1;
                    } else {
                        ll_include_roadside.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                        txt_include_roadside.setTextColor(getResources().getColor(R.color.boardarcolor));
                        raodSideSelected = 0;
                    }


                }
                break;
            case R.id.ll_include_standard:
                if (ll_fleet_managemnet.getVisibility() == View.VISIBLE) {
                    if (standardSelected == 0) {
                        ll_include_standard.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        txt_include_standard.setTextColor(getResources().getColor(R.color.white));
                        standardSelected = 1;
                    } else {
                        ll_include_standard.setBackground(getResources().getDrawable(R.drawable.rect_inner_boarder));
                        txt_include_standard.setTextColor(getResources().getColor(R.color.boardarcolor));
                        standardSelected = 0;
                    }


                }
                break;
            case R.id.iv_plus:
                increaseInteger();
                break;
            case R.id.iv_minus:
                decreaseInteger();
                break;
        }
    }

    public void increaseInteger() {
        minteger = Integer.parseInt(txtCount.getText().toString()) + 1;
        display(minteger);

    }

    public void decreaseInteger() {
        if (minteger > 1) {
            minteger = Integer.parseInt(txtCount.getText().toString()) - 1;
            display(minteger);
        }

    }

    private void display(int number) {
        txtCount.setText("" + number);
    }

    private void CheckData() {

        if (adddloc) {
            if (!sub_category_id.equalsIgnoreCase("")) {
                callPostJobStepThree();
            } else {
                showAlertDialog(getString(R.string.please_select_sub_category));
            }
        } else if (flag) {
            if (isValidate()) {
                if (!sub_category_id.equalsIgnoreCase("")) {
                    callPostJobStepThree();
                } else {
                    showAlertDialog(getString(R.string.please_select_sub_category));
                }
            }
        } else if (!list_subcatogory.isEmpty()) {
            for (int i = 0; i < list_subcatogory.size(); i++) {
                if (list_subcatogory.get(i).isSelected()) {
                    child_category_id = list_subcatogory.get(i).getId();
                }
            }
            if (!child_category_id.equalsIgnoreCase("")) {
                callPostJobStepThree();
            } else {
                showAlertDialog(getString(R.string.please_select_sub_category));
            }
        } else if (!sub_category_id.equalsIgnoreCase("")) {
            callPostJobStepThree();
        } else {
            showAlertDialog(getString(R.string.please_select_sub_category));
        }
    }

    public boolean isValidate() {
        String spec, brand, model, tyre;
        spec = ty_spec.getText().toString();
        brand = ty_bran.getText().toString();
        model = ty_mod.getText().toString();
        tyre = no_ty.getText().toString();
        if (spec != null && spec.trim().length() > 0) {
        } else {
            ty_spec.requestFocus();
            showAlertDialog(getString(R.string.please_select_specifiaction));
            return false;
        }
        if (brand != null && brand.trim().length() > 0) {
        } else {
            ty_bran.requestFocus();
            showAlertDialog(getString(R.string.please_enter_brand_name));
            return false;
        }

        if (model != null && model.trim().length() > 0) {
        } else {
            ty_mod.requestFocus();
            showAlertDialog(getString(R.string.please_enter_model_name));
            return false;
        }

        if (tyre != null && tyre.trim().length() > 0) {
        } else {
            no_ty.requestFocus();
            showAlertDialog(getString(R.string.please_select_number_of_tyre));
            return false;
        }
        return true;
    }

    public void callPostJobStepThree() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "post_job");
        params.put(Constants.PostNewJob.JWT, jwt);
        params.put(Constants.PostNewJob.STEP, "3");
        params.put(Constants.PostNewJob.SC_ID, sub_category_id);
        params.put(Constants.PostNewJob.PR_ID, child_category_id);
        params.put(Constants.PostNewJob.ADD_INCL, additional);
        params.put(Constants.PostNewJob.DROP_LOC, edt_drop_off_location.getText().toString());
        params.put(Constants.PostNewJob.JID, job_id);
        params.put(Constants.PostNewJob.TY_BRAN, ty_bran.getText().toString());
        params.put(Constants.PostNewJob.TY_MOD, ty_mod.getText().toString());
        params.put(Constants.PostNewJob.TY_SPEC, ty_spec.getText().toString());
        params.put(Constants.PostNewJob.NO_TY, no_ty.getText().toString());
        params.put(Constants.PostNewJob.CAT_ID, cat_id);
        if (cat_id.equals("13")) {
            params.put(Constants.PostNewJob.IN_RO_ASS, String.valueOf(raodSideSelected));
            params.put(Constants.PostNewJob.IN_STD_LOG, String.valueOf(standardSelected));
            params.put(Constants.PostNewJob.in_std_log, txtCount.getText().toString());

        }
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
                                Intent intent = new Intent(EditJobStepThree.this, EditJobStepFour.class);
                                intent.putExtra("job_id", job_id);
                                intent.putExtra("cat_id", cat_id);
                                intent.putExtra("placeholder", placeholder);
                                intent.putExtra("help", isnews_);
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

    private void openPopupForImage() {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.image_popup, null);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 50;  //Set your heights
//        int height = (int) (size.y / 1.4);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = width;
//        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(view);
        //       dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void openPopupForTyre() {
        final CharSequence colors[] = new CharSequence[]{"1", "2", "3", "4", "5"};

        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Select Tyre");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                no_ty.setText(colors[which]);
            }
        });
        builder.show();
    }

    private void openPopup() {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        TextView title = view.findViewById(R.id.title);
        title.setText("Sub Category");
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        ListView lv = view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                sub_category_id = catogoriesDBOArrayList.get(i).getId();
                placeholder = catogoriesDBOArrayList.get(i).getPlaceholder();
                list_subcatogory = new ArrayList<>();
                list_subcatogory = catogoriesDBOArrayList.get(i).getSubcategory();
                if (!list_subcatogory.isEmpty()) {
                    ll_relevent.setVisibility(View.VISIBLE);
                    ll_dummy_space.setVisibility(View.GONE);
//                    sub_category_id = "";
                    for (int j = 0; j < list_subcatogory.size(); j++) {
                        list_subcatogory.get(j).setSelected(false);
                    }

                    setAdapter();
                }
                edt_sub_category.setText(catogoriesDBOArrayList.get(i).getName());
                dialog.dismiss();
            }
        });
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 50;  //Set your heights
//        int height = (int) (size.y / 1.4);
//
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = width;
//        lp.height =  WindowManager.LayoutParams.WRAP_CONTENT;


        CustomListAdapterOther clad = new CustomListAdapterOther(appContext, catogoriesDBOArrayList);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        //       dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    private void openPopupForAddress() {
        final Dialog dialog = new Dialog(appContext);
        View view = getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        TextView title = view.findViewById(R.id.title);
        title.setText("Select Address");
        ListView lv = view.findViewById(R.id.custom_list);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                edt_drop_off_location.setText(droppOffLocationDboArrayList.get(i).getName());
                dialog.dismiss();
            }
        });
//        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//        Display display = wm.getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int width = size.x - 50;  //Set your heights
//        int height = (int) (size.y / 1.4);
//        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
//        lp.copyFrom(dialog.getWindow().getAttributes());
//        lp.width = width;
//        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        CustomListAdapterOtherForAddress clad = new CustomListAdapterOtherForAddress(appContext, droppOffLocationDboArrayList);
        lv.setAdapter(clad);
        dialog.setContentView(view);
        dialog.getWindow().getDecorView().getRootView().setBackgroundColor(getResources().getColor(R.color.colorPrimary_transaparent));
//        dialog.getWindow().setAttributes(lp);
        dialog.show();
    }

    public void setAdapter() {

        adapter = new Recycleradapter(list_subcatogory, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        for (int i = 0; i < list_subcatogory.size(); i++) {
            if (jobDetail_dbo.getSubcatname().equalsIgnoreCase(list_subcatogory.get(i).getName())) {
                if (!list_subcatogory.isEmpty()) {
                    ll_relevent.setVisibility(View.VISIBLE);
                    list_subcatogory.get(i).setSelected(true);
                }
            }
        }
        ll_additional_1.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
        tv_additional.setTextColor(getResources().getColor(R.color.white));
        additional_flag = 2;

    }

    public void setAdapterForSpecial() {
        adapter = new Recycleradapter(catogoriesDBOArrayList, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public class CustomListAdapterOther extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<SubCatogoryDBO> listData;

        public CustomListAdapterOther(Context appContext, ArrayList<SubCatogoryDBO> list) {
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
            final CustomListAdapterOther.ChallengerHolder holder;

            SubCatogoryDBO model = listData.get(position);
            if (row == null) {
                holder = new CustomListAdapterOther.ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
                holder.tv_name = row.findViewById(R.id.tv_name);
                row.setTag(holder);
            } else {
                holder = (CustomListAdapterOther.ChallengerHolder) row.getTag();
            }

            holder.tv_name.setText(model.getName());
            return row;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ChallengerHolder {
            TextView tv_name;
        }
    }

    public class CustomListAdapterOtherForAddress extends BaseAdapter {
        Context context;
        LayoutInflater layoutInflater;
        private ArrayList<DroppOffLocationDbo> listData;

        public CustomListAdapterOtherForAddress(Context appContext, ArrayList<DroppOffLocationDbo> list) {
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
            final CustomListAdapterOtherForAddress.ChallengerHolder holder;

            DroppOffLocationDbo model = listData.get(position);
            if (row == null) {
                holder = new CustomListAdapterOtherForAddress.ChallengerHolder();
                row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
                holder.tv_name = row.findViewById(R.id.tv_name);
                row.setTag(holder);
            } else {
                holder = (CustomListAdapterOtherForAddress.ChallengerHolder) row.getTag();
            }

            holder.tv_name.setText(model.getName());
            return row;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        class ChallengerHolder {
            TextView tv_name;
        }
    }

    public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Recycleviewholder> {
        Context context;
        ArrayList<SubCatogoryDBO> services;

        public Recycleradapter(ArrayList<SubCatogoryDBO> services, Context context) {
            this.context = context;
            this.services = services;
        }

        @Override
        public Recycleradapter.Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services, parent, false);
            Recycleradapter.Recycleviewholder recycleviewholder = new Recycleradapter.Recycleviewholder(view);
            return recycleviewholder;
        }

        @Override
        public void onBindViewHolder(final Recycleradapter.Recycleviewholder holder, final int position) {

            holder.services_text.setText(services.get(position).getName());
            if (services.get(position).getName().equalsIgnoreCase(jobDetail_dbo.getSubcatname())) {
                services.get(position).setSelected(true);
            }
            if (services.get(position).isSelected()) {
                holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                holder.services_text.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.service_ll.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                holder.services_text.setTextColor(getResources().getColor(R.color.text_color2));
            }
            holder.service_ll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    services.get(position).setSelected(!services.get(position).isSelected());
                    if (services.get(position).isSelected()) {
                        holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
                        holder.services_text.setTextColor(getResources().getColor(R.color.white));
                        for (int i = 0; i < services.size(); i++) {
                            if (!(position == i)) {
                                if (flag || adddloc) {
                                    catogoriesDBOArrayList.get(i).setSelected(false);
                                } else {
                                    list_subcatogory.get(i).setSelected(false);
                                }
                            }
                        }
                        if (flag || adddloc) {
                            sub_category_id = catogoriesDBOArrayList.get(position).getId();
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        holder.service_ll.setBackground(getDrawable(R.drawable.rect_inner_boarder));
                        holder.services_text.setTextColor(getResources().getColor(R.color.text_color2));
                        child_category_id = "";
                        if (flag || adddloc) {
                            sub_category_id = "";
                        }
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return services.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll;
            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = itemView.findViewById(R.id.autoText2);
                service_ll = itemView.findViewById(R.id.service_ll);
            }

            //if you implement onclick here you must have to use getposition() instead of making variable position global see documentation
        }
    }
}
