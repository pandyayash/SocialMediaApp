package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.CatogoriesDBO;
import technource.greasecrowd.model.List_items;

public class SearchGarageActivity extends BaseActivity {

    String TAG = "SearchCrowdActivity";
    LinearLayout ll_5km, ll_10km, ll_20km, service_5km, service_10km, service_20km, ll_back;
    Context appContext;
    Spinner sp_category, sp_make, sp_model, sp_subcategory;
    TextView tv_5km, tv_10km, tv_20km;
    ArrayList<String> categoryList;
    ArrayList<String> subcategoryList;
    ArrayList<CatogoriesDBO> catogoriesDBOArrayList;
    ArrayList<CatogoriesDBO> subcatogoriesDBOArrayList;
    ArrayAdapter<String> arraycategoryAdapter;
    ArrayAdapter<String> arrayMakersAdapter;
    ArrayAdapter<String> arrayModelAdapter;
    ArrayAdapter<String> arraysubcategoryAdapter;
    ArrayList<String> makersList;
    ArrayList<List_items> makersDBOArrayList;
    ArrayList<List_items> makers_list;
    ArrayList<String> carModelList;
    ArrayList<List_items> carModelDBOArrayList;
    ArrayList<List_items> model_list;
    EditText edtSearchforProject;
    TextView btnSearch;
    String km = "5";
    private String strCarMakerId = "";
    private String strCarModelId = "";
    private String strCatId = "";
    private String strSubCatId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_garage);
        setHeader("Search");
        setfooter("garageowner");
        setlistenrforfooter();
        getViews();
        setViews();
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
        makersDBOArrayList = new ArrayList<>();
        makersList = new ArrayList<>();
        carModelList = new ArrayList<>();
        carModelDBOArrayList = new ArrayList<>();
        model_list = new ArrayList<>();

        ll_5km = (LinearLayout) findViewById(R.id.ll_5km);
        ll_10km = (LinearLayout) findViewById(R.id.ll_10km);
        ll_20km = (LinearLayout) findViewById(R.id.ll_20km);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        tv_5km = (TextView) findViewById(R.id.tv_5km);
        tv_10km = (TextView) findViewById(R.id.tv_10km);
        tv_20km = (TextView) findViewById(R.id.tv_20km);

        service_5km = (LinearLayout) findViewById(R.id.service_5km);
        service_10km = (LinearLayout) findViewById(R.id.service_10km);
        service_20km = (LinearLayout) findViewById(R.id.service_20km);

        sp_category = (Spinner) findViewById(R.id.sp_category);
        sp_subcategory = (Spinner) findViewById(R.id.sp_subcategory);
        sp_make = (Spinner) findViewById(R.id.sp_make);
        sp_model = (Spinner) findViewById(R.id.sp_model);
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        edtSearchforProject = (EditText) findViewById(R.id.edtSearchforProject);
    }

    public void setViews() {
        ll_5km.getBackground().setLevel(0);
    }

    public void setClickListner() {
        ll_5km.setOnClickListener(this);
        ll_10km.setOnClickListener(this);
        ll_20km.setOnClickListener(this);
        btnSearch.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    public void handleKiloMeters(boolean five, boolean ten, boolean twenty) {
        if (five) {
            ll_5km.getBackground().setLevel(1);
            service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
            tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.white));

            ll_10km.getBackground().setLevel(0);
            service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_20km.getBackground().setLevel(0);
            service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

        }

        if (ten) {
            ll_5km.getBackground().setLevel(0);
            service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_10km.getBackground().setLevel(1);
            service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
            tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.white));

            ll_20km.getBackground().setLevel(0);
            service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

        }

        if (twenty) {
            ll_5km.getBackground().setLevel(0);
            service_5km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_5km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_10km.getBackground().setLevel(0);
            service_10km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.white));
            tv_10km.setTextColor(ContextCompat.getColor(appContext, R.color.hint_color));

            ll_20km.getBackground().setLevel(1);
            service_20km.setBackgroundColor(ContextCompat.getColor(appContext, R.color.edittext_bg));
            tv_20km.setTextColor(ContextCompat.getColor(appContext, R.color.white));
        }
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_5km:
                km = "5";
                handleKiloMeters(true, false, false);
                break;
            case R.id.ll_10km:
                km = "10";
                handleKiloMeters(false, true, false);
                break;
            case R.id.ll_20km:
                km = "20";
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
        }
    }

    public boolean validate() {

        if (edtSearchforProject.getText().toString().length() == 0) {
            showAlertDialog("Please enter search for project");
            return false;
        }
        return true;
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

                                setMakersList();


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
                                setModelList();

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
                    if (position == 0) {

                        return false;
                    } else {

                        return true;
                    }
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
                    if (position == 0) {
                        return false;
                    } else {
                        return true;
                    }
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

    public void setMakersList() {
        if (makers_list.size() > 0) {
            makersList.add("Select Car Make");
            for (int i = 0; i < makers_list.size(); i++) {
                makersList.add(makers_list.get(i).getName());
            }
            arrayMakersAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, makersList) {

                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {

                        return true;
                    }
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

            arrayMakersAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            sp_make.setAdapter(arrayMakersAdapter);

            sp_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        strCarMakerId = makers_list.get(position - 1).id;

                        if (Connectivity.isConnected(appContext)) {
                            GetCarModels();
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }


    }

    public void setModelList() {

        if (model_list.size() > 0) {
            carModelList.add("Select Car Model");
            for (int i = 0; i < model_list.size(); i++) {
                carModelList.add(model_list.get(i).getName());
            }
            arrayModelAdapter = new ArrayAdapter<String>(appContext, R.layout.spinner_textview, carModelList) {

                @Override
                public boolean isEnabled(int position) {
                    if (position == 0) {

                        return false;
                    } else {

                        return true;
                    }
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

            arrayModelAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
            sp_model.setAdapter(arrayModelAdapter);

            sp_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position != 0) {
                        strCarModelId = model_list.get(position - 1).id;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }
}