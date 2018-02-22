package technource.greasecrowd.fragment;

import static technource.greasecrowd.activities.LoginScreen.FLAG_SIGNUP;
import static technource.greasecrowd.activities.LoginScreen.FLAG_USERTYPE;
import static technource.greasecrowd.activities.SignUpGarageOwner.current_position_garage;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.android.volley.DefaultRetryPolicy;
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
import org.json.JSONException;
import org.json.JSONObject;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.LoginScreen;
import technource.greasecrowd.adapter.Recycleradapterforfacility;
import technource.greasecrowd.activities.SignUpGarageOwner;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.FacilitiesDBo;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.SignUpDBO;
import technource.greasecrowd.model.TraddingHoursDBo;

/**
 * Created by technource on 6/9/17.
 */

public class fragmentKeywords extends Fragment implements OnClickListener {

  public static String time[] = {"1:00", "1:30", "2:00", "2:30", "3:00", "3:30", "4:00", "4:30",
      "5:00", "5:30", "6:00", "6:30", "7:00", "7:30", "8:00", "8:30",
      "9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30"};
  public static String ampm[] = {"AM", "PM"};
  public static String days[] = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY",
      "SUNDAY"};
  EditText keywords;
  TextView registerBtn, edt_models;
  TextView cont;
  Context appContext;
  String device_token;
  CustomeTraddingDays customeTraddingDays;
  ArrayList<FacilitiesDBo> facilitiesDBos;
  StringBuffer selsected_facilities;
  String facility;
  View v;
  int value;
  LinearLayout t_hours;
  TextView continueto;
  NestedScrollView facilities;
  ScrollView laststep;
  ListView lv;
  JSONArray jarray;
  SignUpDBO data;
  CheckBox checkmodels, checkmakes;
  String is_checkmodels = "0", is_checkmakes = "0";
  ArrayList<TraddingHoursDBo> traddingHoursDBoArrayList;
  private RecyclerView recyclerView;
  private RecyclerView.Adapter adapter;
  private RecyclerView.LayoutManager layoutManager;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_keywords, container, false);

    getviews();
    GetFacilities();
    setfacilitieslayout();

    cont.setOnClickListener(this);
    continueto.setOnClickListener(this);
    registerBtn.setOnClickListener(this);
    data = ((SignUpGarageOwner) appContext).data;

    return v;
  }


  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.cont:
        CheckData();
        break;
      case R.id.continueto:
        validatedata();
        break;
      case R.id.registerBtn:
        callfinalapi();
        break;
    }
  }

  private void getviews() {
    device_token = HelperMethods.getDeviceTokenFCM();
    keywords = (EditText) v.findViewById(R.id.keywords);
    appContext = getActivity();
    facilitiesDBos = new ArrayList<>();
    recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
    recyclerView.setNestedScrollingEnabled(false);
    cont = (TextView) v.findViewById(R.id.cont);
    t_hours = (LinearLayout) v.findViewById(R.id.t_hours);
    continueto = (TextView) v.findViewById(R.id.continueto);
    registerBtn = (TextView) v.findViewById(R.id.registerBtn);
    facilities = (NestedScrollView) v.findViewById(R.id.facilities);
    laststep = (ScrollView) v.findViewById(R.id.laststep);
    traddingHoursDBoArrayList = new ArrayList<>();

    data = new SignUpDBO();

    checkmodels = (CheckBox) v.findViewById(R.id.checkmodels);
    checkmakes = (CheckBox) v.findViewById(R.id.checkmakes);
    edt_models = (EditText) v.findViewById(R.id.edt_models);

    lv = (ListView) v.findViewById(R.id.listfordays);
    jarray = new JSONArray();

    for (int i = 0; i < days.length; i++) {

      TraddingHoursDBo tradding = new TraddingHoursDBo();
      tradding.setTag(days[i]);
      tradding.setIsselected(false);
      traddingHoursDBoArrayList.add(tradding);
    }
  }

  public void setfacilitieslayout() {
    current_position_garage = 5;
    ((SignUpGarageOwner)appContext).ll_back.setVisibility(View.GONE);
    t_hours.setVisibility(View.GONE);
    facilities.setVisibility(View.VISIBLE);
    laststep.setVisibility(View.GONE);
  }

  private void setAdapter() {
    adapter = new Recycleradapterforfacility(facilitiesDBos, appContext);
    layoutManager = new LinearLayoutManager(appContext);
    recyclerView.setLayoutManager(layoutManager);
    recyclerView.setHasFixedSize(true);
    recyclerView.setAdapter(adapter);
  }

  private void callfinalapi() {
    ((SignUpGarageOwner) appContext).showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log("TAG", "App URL : " + url);

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.SIGN_UP.SERVICE_NAME, "signup_garage");
    params.put(Constants.SIGN_UP.STEP, "3");
    params.put(Constants.SIGN_UP.isallmakes, is_checkmodels);
    params.put(Constants.SIGN_UP.isselmakes, is_checkmakes);

    String selmakes = null;
    if (is_checkmakes.equalsIgnoreCase("1")) {
      selmakes = edt_models.getText().toString();
      if (selmakes.equalsIgnoreCase(""))
      { ((SignUpGarageOwner) appContext)
              .showAlertDialog(getResources().getString(R.string.please_enter_carmake));
      }
      else {
        params.put(Constants.SIGN_UP.selmakes, selmakes);
      }
    }
    params.put(Constants.SIGN_UP.keywords, keywords.getText().toString());
    params.put(Constants.SIGN_UP.facilities, facility);
    params.put(Constants.SIGN_UP.hours, jarray.toString());

    AppLog.Log("TAG", "Params In Keywords : " + params);
    CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
        url, appContext, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            AppLog.Log("Response", response.toString());
            try {
              String status = response.getString(Constants.STATUS);
              if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                AppLog.Log("congrets-->", " " + response);

                if (FLAG_SIGNUP.equalsIgnoreCase("1")) {
                  Intent i = new Intent(appContext, DashboardScreen.class);
                  startActivity(i);
                  ((SignUpGarageOwner) appContext).finish();
                  ((SignUpGarageOwner) appContext).activityTransition();
                } else {
                  SocialLoginCheck();
                }
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        ((SignUpGarageOwner) appContext).showLoadingDialog(false);
        ((SignUpGarageOwner) appContext)
            .showAlertDialog(getResources().getString(R.string.some_error_try_again));
      }
    });

    jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

    queue.add(jsonObjReq);
  }

  public void LoginCheck() {
    ((SignUpGarageOwner) appContext).showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log("TAG", "App URL : " + url);

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.LoginType.SERVICE_NAME, "login");
    params.put(Constants.LoginType.LOGIN_TYPE, Constants.LoginType.NORMAL);
    params.put(Constants.LoginType.USERNAME, ((SignUpGarageOwner) appContext).Email);
    params.put(Constants.LoginType.PASSWORD, ((SignUpGarageOwner) appContext).Password);
    params.put(Constants.LoginType.DEVICE_TOKEN, device_token);
    params.put(Constants.LoginType.DEVICE_TYPE, "1");
    params.put(Constants.LoginType.USER_TYPE, FLAG_USERTYPE);
    AppLog.Log("TAG", "Params : " + params);
    JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                ((SignUpGarageOwner) appContext).showLoadingDialog(false);
                AppLog.Log("Response", response.toString());
                try {
                  String status = response.getString(Constants.STATUS);
                  if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                    LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                    loginDetail_dbo
                            .setJWTToken(response.getString(Constants.USER_DETAILS.JWT_TOKEN));

                    JSONObject json = new JSONObject(response.getString("data"));

                    loginDetail_dbo.setUserid(json.getString(Constants.LoginType.ID));
                    loginDetail_dbo.setFirst_name(json.getString(Constants.USER_DETAILS.FNAME));
                    loginDetail_dbo.setLast_name(json.getString(Constants.USER_DETAILS.LNAME));
                    loginDetail_dbo.setImage(json.getString(Constants.LoginType.IMAGE));

                    loginDetail_dbo.setUser_Type(FLAG_USERTYPE);
                    loginDetail_dbo.setLogin_type(json.getString("signup_type"));
                    HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                    MyPreference user_preference = new MyPreference(appContext);
                    user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                    user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);

                    Intent intent = new Intent(appContext, DashboardScreen.class);
                    intent.addFlags(Constants.INTENT_FLAGS);
                    startActivity(intent);
                    ((SignUpGarageOwner) appContext).finish();
                    ((SignUpGarageOwner) appContext).activityTransition();
                  } else {
                    ((SignUpGarageOwner) appContext)
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

  public void SocialLoginCheck() {
    ((SignUpGarageOwner) appContext).showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log("TAG", "App URL : " + url);

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.LoginType.SERVICE_NAME, "login");
    params.put(Constants.LoginType.LOGIN_TYPE, Constants.LoginType.SOCIAL);
    params.put(Constants.LoginType.SOCIAL_ID, data.getSocialid());
    params.put(Constants.LoginType.DEVICE_TYPE, "1");
    params.put(Constants.LoginType.DEVICE_TOKEN,device_token);
    params.put(Constants.LoginType.USER_TYPE, FLAG_USERTYPE);
    AppLog.Log("TAG", "device token-> : " + device_token);

    AppLog.Log("params", "" + params);

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            AppLog.Log("Response-->", response.toString());
            try {
              String status = response.getString(Constants.STATUS);
              if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                loginDetail_dbo.setUserid(response.getString(Constants.USER_DETAILS.USER_ID));
                loginDetail_dbo.setJWTToken(response.getString(Constants.USER_DETAILS.JWT_TOKEN));
                JSONObject jobj = response.getJSONObject("data");
                loginDetail_dbo.setFirst_name(jobj.getString(Constants.USER_DETAILS.FNAME));
                loginDetail_dbo.setLast_name(jobj.getString(Constants.USER_DETAILS.LNAME));
                loginDetail_dbo.setImage(jobj.getString(Constants.LoginType.IMAGE));
                loginDetail_dbo.setLogin_type(jobj.getString("signup_type"));
                loginDetail_dbo.setUser_Type(FLAG_USERTYPE);
                MyPreference user_preference = new MyPreference(appContext);
                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);
                Intent intent = new Intent(appContext, DashboardScreen.class);
                intent.addFlags(Constants.INTENT_FLAGS);
                startActivity(intent);
              } else {
                ((SignUpGarageOwner) appContext)
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

  private void validatedata() {
    String intime, outtime, inampm, outampm;
    int flagin = 0, flagout = 0;

    for (int i = 0; i < traddingHoursDBoArrayList.size(); i++) {

      if (traddingHoursDBoArrayList.get(i).isselected()) {
        intime = traddingHoursDBoArrayList.get(i).getIntime();
        outtime = traddingHoursDBoArrayList.get(i).getOuttime();
        inampm = traddingHoursDBoArrayList.get(i).getIntimeampm();
        outampm = traddingHoursDBoArrayList.get(i).getGetOuttimeampm();
        if (inampm.equalsIgnoreCase(outampm)) {
          for (int j = 0; j < time.length; j++) {

            if (intime.equalsIgnoreCase(time[j])) {
              flagin = j;
              break;
            }
          }
          for (int j = 0; j < time.length; j++) {

            if (outtime.equalsIgnoreCase(time[j])) {
              flagout = j;
              break;
            }
          }
        }
      }
    }

    for (int i = 0; i < traddingHoursDBoArrayList.size(); i++) {
      JSONObject jsonObject = new JSONObject();
      try {

        if (traddingHoursDBoArrayList.get(i).isselected()) {
          String day = traddingHoursDBoArrayList.get(i).getTag();
          String to =
              traddingHoursDBoArrayList.get(i).getOuttime() + " " + traddingHoursDBoArrayList.get(i)
                  .getGetOuttimeampm();
          String from =
              traddingHoursDBoArrayList.get(i).getIntime() + " " + traddingHoursDBoArrayList.get(i)
                  .getIntimeampm();
          jsonObject.put("day", day.substring(0, 3).toLowerCase());
          jsonObject.put("from", from);
          jsonObject.put("to", to);
          jarray.put(jsonObject);
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
    if (flagin > flagout) {
      ((SignUpGarageOwner) appContext).showAlertDialog(getResources().getString(R.string.settime));
    } else {
      Carmakesserviced();
    }

  }


  private void Carmakesserviced() {

    setcarmakeslayout();

    checkmodels.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        is_checkmodels = "1";
        is_checkmakes = "0";

        checkmodels.setChecked(true);
        checkmakes.setChecked(false);
        edt_models.setEnabled(false);
        edt_models.setText("");

      }
    });

    checkmakes.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        is_checkmakes = "1";
        is_checkmodels = "0";
        checkmodels.setChecked(false);
        checkmakes.setChecked(true);
        edt_models.setEnabled(true);

      }
    });
  }

  public void setcarmakeslayout() {
    current_position_garage = 4;
    t_hours.setVisibility(View.GONE);
    facilities.setVisibility(View.GONE);
    laststep.setVisibility(View.VISIBLE);
    edt_models.setEnabled(false);
    ((SignUpGarageOwner)appContext).ll_back.setVisibility(View.VISIBLE);
  }

  private void TraddingHours() {
    settraddinghourslayout();
    customeTraddingDays = new CustomeTraddingDays(appContext, traddingHoursDBoArrayList);
    lv.setAdapter(customeTraddingDays);
  }

  public void settraddinghourslayout() {
    current_position_garage = 3;
    ((SignUpGarageOwner)appContext).ll_back.setVisibility(View.VISIBLE);
    t_hours.setVisibility(View.VISIBLE);
    facilities.setVisibility(View.GONE);
    laststep.setVisibility(View.GONE);
  }

  private void CheckData() {
    int count = 0;
    selsected_facilities = new StringBuffer();

    for (int j = 0; j < facilitiesDBos.size(); j++) {

      AppLog.Log("selected facility-->", "" + facilitiesDBos.get(j).isSelected());
      if (facilitiesDBos.get(j).isSelected()) {
        count++;
        selsected_facilities = selsected_facilities.append(facilitiesDBos.get(j).getId());
        selsected_facilities.append(",");
      }
    }
    if (count == 0) {
      facility = "";
    } else {

      if (selsected_facilities != null) {
        facility = selsected_facilities.substring(0, selsected_facilities.length() - 1);
      }
    }
    TraddingHours();
  }

  public void GetFacilities() {
    ((SignUpGarageOwner) appContext).showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log("TAG", "App URL : " + url);

    Map<String, String> params = new HashMap<String, String>();
    params.put(Constants.SIGN_UP.SERVICE_NAME, "facilities");

    AppLog.Log("TAG", "Params : " + params);
    CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
        url, appContext, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            AppLog.Log("Response", response.toString());
            try {
              String status = response.getString(Constants.STATUS);
              if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                JSONArray json = response.getJSONArray("facilities");

                for (int i = 0; i < json.length(); i++) {
                  FacilitiesDBo facility = new FacilitiesDBo();
                  JSONObject obj = json.getJSONObject(i);
                  facility.setId(obj.getInt("id"));
                  facility.setFacilites(obj.getString("name"));
                  facilitiesDBos.add(facility);
                }
                setAdapter();

              } else {

              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        ((SignUpGarageOwner) appContext).showLoadingDialog(false);
      }
    });

    queue.add(jsonObjReq);
  }

  private void displaytime(final int position) {

    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_main, null);
    ListView lv = (ListView) view.findViewById(R.id.custom_list);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (value == 1) {
          traddingHoursDBoArrayList.get(position).setIntime(time[i]);
        }
        if (value == 2) {
          traddingHoursDBoArrayList.get(position).setOuttime(time[i]);
        }
        customeTraddingDays.notifyDataSetChanged();

        dialog.dismiss();
      }
    });

    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);

    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());

    int width = size.x - 50;  //Set your heights
    int height = (int) (size.y / 1.4); //Set your widths


    lp.width = width;
    lp.height = height;

    CustomListAdapterOther clad = new CustomListAdapterOther(getActivity(), time);
    lv.setAdapter(clad);
    dialog.setContentView(view);
    dialog.getWindow().setAttributes(lp);
    dialog.show();
  }

  private void displayAmPm(final int position) {

    final Dialog dialog = new Dialog(getActivity());
    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_main, null);
    ListView lv = (ListView) view.findViewById(R.id.custom_list);

    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        if (value == 1) {
          traddingHoursDBoArrayList.get(position).setIntimeampm(ampm[i]);
        }
        if (value == 2) {
          traddingHoursDBoArrayList.get(position).setGetOuttimeampm(ampm[i]);
        }
        customeTraddingDays.notifyDataSetChanged();

        dialog.dismiss();
      }
    });

    WindowManager wm = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
    Display display = wm.getDefaultDisplay();
    Point size = new Point();
    display.getSize(size);


    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(dialog.getWindow().getAttributes());

    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;


    CustomListAdapterOther clad = new CustomListAdapterOther(getActivity(), ampm);
    lv.setAdapter(clad);
    dialog.setContentView(view);
    dialog.getWindow().setAttributes(lp);
    dialog.show();
  }

  public class CustomListAdapterOther extends BaseAdapter {

    Context context;
    LayoutInflater layoutInflater;
    private String time[];

    public CustomListAdapterOther(Context appContext, String list[]) {
      context = appContext;
      layoutInflater = LayoutInflater.from(appContext);
      time = list;
    }

    @Override
    public int getCount() {
      return time.length;
    }

    @Override
    public Object getItem(int position) {
      return time[position];
    }

    @Override
    public long getItemId(int position) {
      return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View row = convertView;
      final ChallengerHolder holder;

      String model = time[position];
      if (row == null) {
        holder = new ChallengerHolder();
        row = layoutInflater.inflate(R.layout.list_row_items, parent, false);
        holder.tv_name = (TextView) row.findViewById(R.id.tv_name);

        row.setTag(holder);
      } else {
        holder = (ChallengerHolder) row.getTag();
      }

      holder.tv_name.setText(model);
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
      return 7;
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
        v = layoutInflater.inflate(R.layout.custome_traddinghour_row, parent, false);

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

        v.setTag(holder);

      } else {
        holder = (ChallengerHolder) v.getTag();
      }

      holder.day.setText(traddingHoursDBoArrayList.get(position).getTag());
      holder.edt_sun_time.setText(traddingHoursDBoArrayList.get(position).getIntime());
      holder.edt1_sun_time.setText(traddingHoursDBoArrayList.get(position).getOuttime());
      holder.edt_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getIntimeampm());
      holder.edt1_am_pm_sun.setText(traddingHoursDBoArrayList.get(position).getGetOuttimeampm());
      if (traddingHoursDBoArrayList.get(position).isselected()) {
        holder.row.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
      } else {
        holder.row.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
      }

      holder.intime.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (traddingHoursDBoArrayList.get(position).isselected())
          {
            value = 1;
            displaytime(position);
          }
          else {
            ((SignUpGarageOwner)appContext).showAlertDialog("Please select the days in order to make changes in the timings");
          }


        }
      });

      holder.intimeampm.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (traddingHoursDBoArrayList.get(position).isselected())
          {
            value = 1;
            displayAmPm(position);
          }
          else {
            ((SignUpGarageOwner)appContext).showAlertDialog("Please select the days in order to make changes in the timings");
          }

        }
      });

      holder.outtime.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (traddingHoursDBoArrayList.get(position).isselected())
          {
            value = 2;
            displaytime(position);
          }
          else {
            ((SignUpGarageOwner)appContext).showAlertDialog("Please select the days in order to make changes in the timings");
          }
        }
      });

      holder.outtimeampm.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (traddingHoursDBoArrayList.get(position).isselected())
          {
            value = 2;
            displayAmPm(position);
          }
          else {
            ((SignUpGarageOwner)appContext).showAlertDialog("Please select the days in order to make changes in the timings");
          }

        }
      });

      holder.row.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {

          if (traddingHoursDBoArrayList.get(position).isselected()) {
            traddingHoursDBoArrayList.get(position).setIsselected(false);

            traddingHoursDBoArrayList.get(position).setIntime("8:00");
            traddingHoursDBoArrayList.get(position).setIntimeampm("AM");
            traddingHoursDBoArrayList.get(position).setOuttime("5:30");
            traddingHoursDBoArrayList.get(position).setGetOuttimeampm("PM");
            customeTraddingDays.notifyDataSetChanged();

          } else {
            traddingHoursDBoArrayList.get(position).setIsselected(true);
            customeTraddingDays.notifyDataSetChanged();
          }

        }
      });

      return v;
    }


    class ChallengerHolder {

      TextView day;
      LinearLayout intime, intimeampm, outtime, outtimeampm, row;
      EditText edt_sun_time, edt1_sun_time, edt_am_pm_sun, edt1_am_pm_sun;

    }
  }
}
