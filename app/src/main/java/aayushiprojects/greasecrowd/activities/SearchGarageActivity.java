package aayushiprojects.greasecrowd.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.CatogoriesDBO;
import aayushiprojects.greasecrowd.model.List_items;

public class SearchGarageActivity extends BaseActivity {

    String TAG = "SearchCrowdActivity";
    LinearLayout ll_5km, ll_10km, ll_20km, service_5km, service_10km, service_20km, ll_back;
    Context appContext;
    Spinner sp_category, sp_subcategory;
    TextView tv_5km, tv_10km, tv_20km;
    ArrayList<String> categoryList;
    ArrayList<String> subcategoryList;
    ArrayList<CatogoriesDBO> catogoriesDBOArrayList;
    ArrayList<CatogoriesDBO> subcatogoriesDBOArrayList;
    ArrayAdapter<String> arraycategoryAdapter;
    ArrayAdapter<String> arraysubcategoryAdapter;
    ArrayList<List_items> makers_list;
    ArrayList<List_items> model_list;
    EditText edtSearchforProject;
    TextView btnSearch;
    String km = "";
    private String strCarMakerId = "";
    private String strCarModelId = "";
    private String strCatId = "";
    private String strSubCatId = "";
    RelativeLayout ll_make, ll_model;
    EditText edt_make, edt_model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_garage);
        setHeader("Search");
        setfooter("garageowner");
        setlistenrforfooter();
        getViews();
        setClickListner();
        if (Connectivity.isConnected(appContext)) {
            GetCategories();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
        if (Connectivity.isConnected(appContext)) {
            GetCarMakers();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }

        if (Connectivity.isConnected(appContext)) {
            GetCarModels();
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    public void getViews() {
        appContext = this;
        categoryList = new ArrayList<>();
        subcategoryList = new ArrayList<>();
        catogoriesDBOArrayList = new ArrayList<>();
        subcatogoriesDBOArrayList = new ArrayList<>();
        makers_list = new ArrayList<>();
        model_list = new ArrayList<>();

        ll_5km = findViewById(R.id.ll_5km);
        ll_10km = findViewById(R.id.ll_10km);
        ll_20km = findViewById(R.id.ll_20km);
        ll_back = findViewById(R.id.ll_back);

        tv_5km = findViewById(R.id.tv_5km);
        tv_10km = findViewById(R.id.tv_10km);
        tv_20km = findViewById(R.id.tv_20km);

        service_5km = findViewById(R.id.service_5km);
        service_10km = findViewById(R.id.service_10km);
        service_20km = findViewById(R.id.service_20km);

        sp_category = findViewById(R.id.sp_category);
        sp_subcategory = findViewById(R.id.sp_subcategory);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearchforProject = findViewById(R.id.edtSearchforProject);

        ll_make = findViewById(R.id.ll_make);
        ll_model = findViewById(R.id.ll_model);
        edt_make = findViewById(R.id.edt_make);
        edt_model = findViewById(R.id.edt_model);

    }


    public void setClickListner() {
        ll_5km.setOnClickListener(this);
        ll_10km.setOnClickListener(this);
        ll_20km.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        ll_make.setOnClickListener(this);
        edt_make.setOnClickListener(this);
        edt_model.setOnClickListener(this);
        ll_model.setOnClickListener(this);
    }

    public void handleKiloMeters(boolean five, boolean ten, boolean twenty) {
        if (five) {
            if (km.equalsIgnoreCase("5")) {
                ll_5km.getBackground().setLevel(0);
                service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
                tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));
                km = "";
            } else {
                ll_5km.getBackground().setLevel(1);
                service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
                tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.white));
                km = "5";
            }
            ll_10km.getBackground().setLevel(0);
            service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_20km.getBackground().setLevel(0);
            service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));
        }

        if (ten) {

            if (km.equalsIgnoreCase("10")) {
                ll_10km.getBackground().setLevel(0);
                service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
                tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));
                km = "";
            } else {
                ll_10km.getBackground().setLevel(1);
                service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
                tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.white));
                km = "10";
            }
            ll_5km.getBackground().setLevel(0);
            service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));


            ll_20km.getBackground().setLevel(0);
            service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));
        }

        if (twenty) {

            if (km.equalsIgnoreCase("20")) {
                ll_20km.getBackground().setLevel(0);
                service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
                tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

                km = "";
            } else {
                ll_20km.getBackground().setLevel(1);
                service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
                tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.white));

                km = "20";
            }
            ll_5km.getBackground().setLevel(0);
            service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_10km.getBackground().setLevel(0);
            service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));


        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_5km:

                handleKiloMeters(true, false, false);
                break;
            case R.id.ll_10km:

                handleKiloMeters(false, true, false);
                break;
            case R.id.ll_20km:

                handleKiloMeters(false, false, true);
                break;
            case R.id.btnSearch:
                // if (validate()) {
                Intent intent = new Intent();
                intent.putExtra("keyword", edtSearchforProject.getText().toString());
                intent.putExtra("cat_id", strCatId);
                intent.putExtra("subcat_id", strSubCatId);
                intent.putExtra("car_make_id", strCarMakerId);
                intent.putExtra("car_model_id", strCarModelId);
                intent.putExtra("km", km);
                setResult(RESULT_OK, intent);
                activityTransition();
                finish();
                //  }
                break;

            case R.id.ll_back:
                onBackPressed();
                break;

            case R.id.ll_make:
            case R.id.edt_make:
                showMakeDialog();
                break;

            case R.id.ll_model:
            case R.id.edt_model:
                if (model_list.size() > 0) {
                    showModelDialog();
                } else {
                    showAlertDialog(getString(R.string.select_make));
                }
                break;
        }
    }

    public boolean validate() {

        if (edtSearchforProject.getText().toString().length() == 0) {
            showAlertDialog("Please enter search for project");
            return false;
        }
        return true;
    }

    private void showMakeDialog() {
        final Dialog dialog = new Dialog(appContext);
        View view = this.getLayoutInflater().inflate(R.layout.custome_dialogue_header_popup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        ListView lv = view.findViewById(R.id.custom_list);
        TextView title = view.findViewById(R.id.title);
        title.setText("Select make");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strCarMakerId = makers_list.get(i).getId();
                strCarModelId = "";
                edt_model.setText("");
                edt_make.setText(makers_list.get(i).getName());
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
        ListView lv = view.findViewById(R.id.custom_list);
        TextView title = view.findViewById(R.id.title);
        title.setText("Model");
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                strCarModelId = model_list.get(i).getId();
                edt_model.setText(model_list.get(i).getName());
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

    public void GetCategories() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GET_CATEGORIES);

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONArray json = response.getJSONArray("services");

                                for (int i = 0; i < json.length(); i++) {
                                    CatogoriesDBO categories = new CatogoriesDBO();
                                    JSONObject obj = json.getJSONObject(i);
                                    categories.setId(obj.getString("id"));
                                    categories.setName(obj.getString("name"));
                                    categories.setItext(obj.getString("itext"));
                                    catogoriesDBOArrayList.add(categories);
                                }
                                setCategoryList();

                            } else {
                                showAlertDialog("some error occured please ry again later");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

    public void GetSubCategories() {
        subcatogoriesDBOArrayList = new ArrayList<>();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GETSUBCATEGORY);
        params.put("category_id", strCatId);
        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                JSONArray json = response.getJSONArray("services");

                                for (int i = 0; i < json.length(); i++) {
                                    CatogoriesDBO categories = new CatogoriesDBO();
                                    JSONObject obj = json.getJSONObject(i);
                                    categories.setId(obj.getString("id"));
                                    categories.setName(obj.getString("name"));
                                    categories.setItext(obj.getString("itext"));
                                    subcatogoriesDBOArrayList.add(categories);
                                }
                                setSubCategoryList();


                            } else {
                                showAlertDialog("some error occured please ry again later");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

    public void GetCarMakers() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, WebServiceURLs.GET_CAR_MAKES);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response Car Makers --> ", response.toString());
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

                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        showLoadingDialog(false);
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
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, WebServiceURLs.GET_CAR_MODELS);
        params.put(Constants.LoginType.MAKE_ID, strCarMakerId);

        AppLog.Log("TAG", "Params : " + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response Car Models --> ", response.toString());
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


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(jsonObjReq);
    }

    public void setCategoryList() {
        if (catogoriesDBOArrayList.size() > 0) {
            categoryList.add("Select Category");
            for (int i = 0; i < catogoriesDBOArrayList.size(); i++) {
                categoryList.add(catogoriesDBOArrayList.get(i).getName());
            }
            arraycategoryAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, categoryList) {

                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.tab_text));

                    } else {
                        tv.setTextColor(getResources().getColor(R.color.black));
                    }
                    return view;
                }
            };

            arraycategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            sp_category.setAdapter(arraycategoryAdapter);

            sp_category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        strCatId = catogoriesDBOArrayList.get(position - 1).id;
                        GetSubCategories();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public void setSubCategoryList() {
        subcategoryList.clear();
        if (subcatogoriesDBOArrayList.size() > 0) {
            subcategoryList.add("Select Category");
            for (int i = 0; i < subcatogoriesDBOArrayList.size(); i++) {
                subcategoryList.add(subcatogoriesDBOArrayList.get(i).getName());
            }
            arraysubcategoryAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, subcategoryList) {

                @Override
                public boolean isEnabled(int position) {
                    return position != 0;
                }

                @Override
                public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                    View view = super.getDropDownView(position, convertView, parent);
                    TextView tv = (TextView) view;
                    if (position == 0) {
                        // Set the hint text color gray
                        tv.setTextColor(getResources().getColor(R.color.tab_text));

                    } else {
                        tv.setTextColor(getResources().getColor(R.color.black));
                    }
                    return view;
                }
            };

            arraysubcategoryAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            sp_subcategory.setAdapter(arraysubcategoryAdapter);

            sp_subcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        strSubCatId = subcatogoriesDBOArrayList.get(position - 1).id;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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

}