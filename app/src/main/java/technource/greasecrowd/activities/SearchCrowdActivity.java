package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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

import static technource.greasecrowd.activities.MyCrowdUserActivity.selectedCat;
import static technource.greasecrowd.activities.MyCrowdUserActivity.selectedMake;
import static technource.greasecrowd.activities.MyCrowdUserActivity.selectedModel;
import static technource.greasecrowd.activities.MyCrowdUserActivity.strKeyword;

public class SearchCrowdActivity extends BaseActivity {

    String TAG = "SearchCrowdActivity";
    Context appContext;
    Spinner sp_category, sp_make, sp_model;
    ArrayList<String> categoryList;
    ArrayList<CatogoriesDBO> catogoriesDBOArrayList;
    ArrayAdapter<String> arraycategoryAdapter;
    ArrayAdapter<String> arrayMakersAdapter;
    ArrayAdapter<String> arrayModelAdapter;

    ArrayList<String> makersList;
    ArrayList<List_items> makersDBOArrayList;
    ArrayList<List_items> makers_list;

    ArrayList<String> carModelList;
    ArrayList<List_items> carModelDBOArrayList;
    ArrayList<List_items> model_list;

    private String strCarMakerId = "";
    private String strCarModelId = "";
    private String strCatId = "";
    EditText edtSearchforProject;
    TextView btnSearch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_crowd);
        setHeader("Search");
        setfooter("crowd");
        setlistenrforfooter();
        getViews();



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
        if (strKeyword.length()>0){
            edtSearchforProject.setText(strKeyword);
        }



    }




    @Override
    protected void onResume() {
        super.onResume();
        setMyCrowdFooter(this);
    }

    public void getViews() {
        appContext = this;
        categoryList = new ArrayList<>();
        catogoriesDBOArrayList = new ArrayList<>();
        makers_list = new ArrayList<>();
        makersDBOArrayList = new ArrayList<>();
        makersList = new ArrayList<>();
        carModelList = new ArrayList<>();
        carModelDBOArrayList = new ArrayList<>();
        model_list = new ArrayList<>();

        sp_category = (Spinner) findViewById(R.id.sp_category);
        sp_make = (Spinner) findViewById(R.id.sp_make);
        sp_model = (Spinner) findViewById(R.id.sp_model);
        btnSearch = (TextView) findViewById(R.id.btnSearch);
        edtSearchforProject = (EditText) findViewById(R.id.edtSearchforProject);
        btnSearch.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view == btnSearch) {
            //if (validate()){
            Intent intent = new Intent();
            intent.putExtra("keyword", edtSearchforProject.getText().toString());
            intent.putExtra("cat_id", strCatId);
            intent.putExtra("car_make_id", strCarMakerId);
            intent.putExtra("car_model_id", strCarModelId);
            setResult(RESULT_OK, intent);
            activityTransition();
            finish();
            //}

        }
    }


    public boolean validate() {

        if (edtSearchforProject.getText().toString().length() == 0) {
            showAlertDialog("Please enter search for project");
            return false;
        }


        return true;
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
                        selectedCat = position;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }

        sp_category.setSelection(selectedCat);
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
                        selectedMake = position;

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

        sp_make.setSelection(selectedMake);
    }

    public void setModelList() {
        carModelList = new ArrayList<>();

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
                        selectedModel = position;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
        sp_model.setSelection(selectedModel);

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
}
