package technource.greasecrowd.helper;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

import technource.greasecrowd.R;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.SafetyReport_DBO;
import technource.greasecrowd.model.SplashPojo;

/**
 * Created by technource on 20/3/17.
 */


public class HelperMethods {

  private static final String TAG = "HelperMethods";
  private static final char[] symbols = new char[36];

  static {
    for (int idx = 0; idx < 10; ++idx) {
      symbols[idx] = (char) ('0' + idx);
    }
    for (int idx = 10; idx < 36; ++idx) {
      symbols[idx] = (char) ('a' + idx - 10);
    }
  }

  /**
   * Method used to hide Keyboard
   */
  public static void hideSoftKeyboard(Context context, View view) {
    InputMethodManager inputMethodManager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  public static void hideSoftKeyboard(Activity activity) {
    InputMethodManager inputMethodManager = (InputMethodManager) activity
        .getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
  }

  /**
   * Method used to show Keyboard
   */
  public static void showSoftKeyboard(View view, Context context) {
    InputMethodManager inputMethodManager = (InputMethodManager) context
        .getSystemService(Context.INPUT_METHOD_SERVICE);
    view.requestFocus();
    inputMethodManager.showSoftInput(view, 0);
  }

  public static boolean ValidUsername(String name) {
    if (name != null && name.length() > 1) {
      Pattern pattern = Pattern.compile(Constants.USERNAME_PATTERN);
      Matcher matcher = pattern.matcher(name);
      return matcher.matches();
    } else if (name.length() == 0) {
      return false;
    } else {
      return false;
    }
  }


  public static boolean validateEmail(String emailID) {
    String EMAIL_PATTERN = Constants.EMAIL_PATTERN;
    if (emailID != null && emailID.length() > 1) {
      Pattern pattern = Pattern.compile(EMAIL_PATTERN);
      Matcher matcher = pattern.matcher(emailID);
      return matcher.matches();
    } else if (emailID.length() == 0) {
      return false;
    } else {
      return false;
    }
  }

  public static void getGridViewHeight(GridView gridView) {
    ListAdapter myListAdapter = gridView.getAdapter();
    if (myListAdapter == null) {
      //do nothing return null
      return;
    }
    //set listAdapter in loop for getting final size
    int totalHeight = 0;
    for (int size = 0;
        size < (Math.ceil((double) myListAdapter.getCount() / (double) gridView.getNumColumns()));
        size++) {
      View listItem = myListAdapter.getView(size, null, gridView);
      listItem.measure(0, 0);
      totalHeight += listItem.getMeasuredHeight();
    }
    //setting listview item in adapter
    ViewGroup.LayoutParams params = gridView.getLayoutParams();
    params.height = totalHeight + ((int) Math
        .ceil((double) myListAdapter.getCount() - 1 / (double) gridView.getNumColumns()));
    gridView.setLayoutParams(params);
    // print height of adapter on log
    // Log.i("height of listItem:", String.valueOf(totalHeight));
  }

  public static Date getFormattedDateTime(String s) {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(Constants.WITHOUT_SS_TIME_STAMP_FORMAT);
    String currentdate = df.format(now.getTime());
    Date date = null;
    try {
      date = df.parse(currentdate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (s != null && s.length() > 1) {
      try {
        date = df.parse(s);
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    AppLog.Log("Current date : ", date.toString());
    return date;
  }


  public static Date getFormattedDateTimeBefore15(String s) {
    Calendar now = Calendar.getInstance();
    now.add(Calendar.MINUTE, -15);
    SimpleDateFormat df = new SimpleDateFormat(Constants.WITHOUT_SS_TIME_STAMP_FORMAT);
    String currentdate = df.format(now.getTime());
    Date date = null;
    try {
      date = df.parse(currentdate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    if (s != null && s.length() > 1) {
      try {
        date = df.parse(s);
        now.setTime(date);
        now.add(Calendar.MINUTE, -15);
        date = df.parse(df.format(now.getTime()));
      } catch (ParseException e) {
        e.printStackTrace();
      }
    }

    AppLog.Log("Current date : ", date.toString());
    return date;
  }


  /**
   * Method used to dismiss ProgressDialog
   */
  public static void dismissProgressDialog(ProgressDialog progressDialog) {
    try {
      if (progressDialog != null && progressDialog.isShowing()) {
        progressDialog.dismiss();
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static boolean validatePassword(String password) {
    Pattern pattern = Pattern.compile(Constants.PASSWORD_PATTERN);
    Matcher matcher = pattern.matcher(password);
    return matcher.matches();
  }


  public static String pad(int c) {
    if (c >= 10) {
      return String.valueOf(c);
    } else {
      return "0" + String.valueOf(c);
    }
  }


  public static void storeUserDetailsSharedPreferences(Context context,
      LoginDetail_DBO userObject) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor prefsEditor = userLoginSharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(userObject);
    prefsEditor.putString(Constants.LoginUserDetails, json);
    prefsEditor.commit();

  }

  public static LoginDetail_DBO getUserDetailsSharedPreferences(Context context) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = userLoginSharedPrefs.getString(Constants.LoginUserDetails, "");
    LoginDetail_DBO objectUserDetails = gson.fromJson(json, LoginDetail_DBO.class);
    MyPreference user_preference = new MyPreference(context);
    user_preference.getBooleanReponse(Constants.IS_LOGGEDIN);
    return objectUserDetails;
  }

  public static void storeUserSplashSharedPreferences(Context context,
      ArrayList<SplashPojo> splashPojoArrayList) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor prefsEditor = userLoginSharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(splashPojoArrayList);
    prefsEditor.putString(Constants.Splashscreen.SPLASHUSERDETAILS, json);
    prefsEditor.commit();
  }

  public static void storeSafetyReportSharedPreferences(Context context,
      ArrayList<SafetyReport_DBO> safetyReportArrayList,String strFrag) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor prefsEditor = userLoginSharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(safetyReportArrayList);
    prefsEditor.putString(strFrag, json);
    prefsEditor.commit();
  }

  public static JobDetail_DBO getjobDetailsSharedPreferences(Context context){
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = userLoginSharedPrefs.getString(Constants.JobDetails, "");
    JobDetail_DBO objectUserDetails = gson.fromJson(json, JobDetail_DBO.class);
    MyPreference user_preference = new MyPreference(context);
    user_preference.getBooleanReponse(Constants.IS_LOGGEDIN);
    return objectUserDetails;
  }

  public static void storeJobDetailsSharedPreferences(Context context,
                                                      JobDetail_DBO userObject) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    SharedPreferences.Editor prefsEditor = userLoginSharedPrefs.edit();
    Gson gson = new Gson();
    String json = gson.toJson(userObject);
    prefsEditor.putString(Constants.JobDetails, json);
    prefsEditor.commit();

  }


  public static ArrayList<SplashPojo> getUserSplashSharedPreferences(Context context) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = userLoginSharedPrefs.getString(Constants.Splashscreen.SPLASHUSERDETAILS, "");
    Type type = new TypeToken<List<SplashPojo>>() {
    }.getType();
    List<SplashPojo> arraylist = gson.fromJson(json, type);
    return (ArrayList<SplashPojo>) arraylist;
  }
  public static ArrayList<SafetyReport_DBO> getSafetyrReportSharedPreferences(Context context,String strFrag) {
    SharedPreferences userLoginSharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
    Gson gson = new Gson();
    String json = userLoginSharedPrefs.getString(strFrag, "");
    Type type = new TypeToken<List<SafetyReport_DBO>>() {
    }.getType();
    List<SafetyReport_DBO> arraylist = gson.fromJson(json, type);
    return (ArrayList<SafetyReport_DBO>) arraylist;
  }


  public static boolean deleteUserDetailsSharedPreferences(Context context) {
    MyPreference user_preference = new MyPreference(context);
    user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, false);
    return context.getSharedPreferences(Constants.LoginUserDetails, 0).edit().clear().commit();
  }

  public static String getCurrentDateTime() {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT);
    String currentdate = df.format(now.getTime());
    AppLog.Log("Current date : ", currentdate);
    return currentdate;
  }

  public static String getCurrentDateTimeForDB() {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_DB);
    String currentdate = df.format(now.getTime());
    AppLog.Log("Current date : ", currentdate);
    return currentdate;
  }

  public static String getPayLoad() {
    RandomString randomString = new RandomString(36);
    String payload = randomString.nextString();
    return payload;
  }

  public static String convertTimeStampDB(String date) {
    SimpleDateFormat df = new SimpleDateFormat(Constants.TIME_STAMP_FORMAT_DB);
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.MMddyyyyFormat);
    String newDate = "";
    try {
      Date d = df.parse(date);
      newDate = sdf.format(d);
      AppLog.Log(TAG, "Converted Date: " + newDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return newDate;
  }

  public static void showToast(Context appContext, String text) {
    Toast.makeText(appContext, text, Constants.TOAST_TIME).show();
  }

  public static boolean validatePhone(String email) {
    String EMAIL_PATTERN = Constants.NUMBER_PATTERN;
    if (email != null && email.length() > 1) {
      Pattern pattern = Pattern.compile(EMAIL_PATTERN);
      Matcher matcher = pattern.matcher(email);
      return matcher.matches();
    } else if (email.length() == 0) {
      return false;
    } else {
      return false;
    }
  }

  public static boolean jsonContains(String key, JSONObject jsonObject) {
    if (jsonObject.has(key)) {
      return true;
    }
    return false;
  }

  public static int randomInteger() {
    int min = 1;
    int max = 999;

    Random r = new Random();
    int i1 = r.nextInt(max - min + 1) + min;
    return i1;
  }

  public static String getTimeZoneID() {
    Calendar c = Calendar.getInstance();
    TimeZone tz = c.getTimeZone();
    String id = tz.getID();
    AppLog.Log(TAG, "Timezone id get is " + id);
    return id;
  }

  public static final String getDeviceTokenFCM() {
    String token = FirebaseInstanceId.getInstance().getToken();
    return token;
  }

  public static String getDate(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
    Date testDate = null;
    try {
      testDate = sdf.parse(date);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(testDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MINUTE);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    cal.set(Calendar.MONTH, month - 1);
    String month_name = month_date.format(cal.getTime());

    String finalDtae = day + " " + month_name + ", " + year;

    return finalDtae;
  }

  public static String getDateasMonth(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat("mm/dd/yyyy");
    Date testDate = null;
    try {
      testDate = sdf.parse(date);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(testDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MINUTE);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    cal.set(Calendar.MONTH, month - 1);
    String month_name = month_date.format(cal.getTime());

    String finalDtae = month_name + " " + day + ", " + year;

    return finalDtae;
  }

  public static String getmonthDate(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat(Constants.MMddyyyyFormat);
    Date testDate = null;
    try {
      testDate = sdf.parse(date);
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    Calendar cal = Calendar.getInstance();
    cal.setTime(testDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat month_date = new SimpleDateFormat("MMM dd");
    cal.set(Calendar.MONTH, month);
    String month_name = month_date.format(cal.getTime());

    String dateReturn = month_name;

    return dateReturn;
  }

  public static String getTommorowDate() {
    //Display the date now:
    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String formattedDate = sdf.format(now);

    cal.add(cal.DAY_OF_MONTH, 1);
    Date tomorrow = cal.getTime();
    formattedDate = sdf.format(tomorrow);
    System.out.println(formattedDate);

    return formattedDate;
  }

  public static String getTodayDate() {
    //Display the date now:
    Calendar cal = Calendar.getInstance();
    Date now = cal.getTime();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
    String formattedDate = sdf.format(now);
    Date tomorrow = cal.getTime();
    formattedDate = sdf.format(tomorrow);
    System.out.println(formattedDate);

    return formattedDate;
  }

  public static String get12hourFormat(String _24HourTime) {
    String date = null;
    SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
    SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
    try {
      Date _24HourDt = _24HourSDF.parse(_24HourTime);

//      date = _12HourSDF.format(_24HourDt);
      date = _12HourSDF.format(_24HourDt).toUpperCase().replace(".", "");
    } catch (Exception e) {
      e.printStackTrace();
    }

    return date;
  }

  public static String splitByComma(String text) {
    try {
      String[] textArray = text.split(",");
      return textArray[0];
    } catch (Exception e) {
      e.printStackTrace();
    }
    return text;
  }

  public static String getConvertedDate(String date) {
    SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
    Date testDate = null;
    try {
      testDate = sdf.parse(date);
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    sdf.applyPattern(Constants.MMddyyyyFormat);
    Calendar cal = Calendar.getInstance();
    cal.setTime(testDate);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MINUTE);
    int day = cal.get(Calendar.DAY_OF_MONTH);

    SimpleDateFormat month_date = new SimpleDateFormat("MMM");
    cal.set(Calendar.MONTH, month - 1);
    String month_name = month_date.format(cal.getTime());

    String finalDtae = day + " " + month_name + ", " + year;

//    return finalDtae;
    AppLog.Log(TAG, "Converted Date: " + sdf.format(testDate));
    return sdf.format(testDate);
  }

  public static String getCurrentDate() {
    Calendar now = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat(Constants.MMddyyyyFormat);
    String currentdate = df.format(now.getTime());
    AppLog.Log("Current date : ", currentdate);
    return currentdate;
  }

  public static class RandomString {

    private final Random random = new Random();
    private final char[] buf;

    public RandomString(int length) {
      if (length < 1) {
        throw new IllegalArgumentException("length < 1: " + length);
      }
      buf = new char[length];
    }

    public String nextString() {
      for (int idx = 0; idx < buf.length; ++idx) {
        buf[idx] = symbols[random.nextInt(symbols.length)];
      }
      return new String(buf);
    }
  }


}
