package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.LoginType;
import technource.autoreum.helper.Constants.SocialSignUp;
import technource.autoreum.helper.Constants.USER_DETAILS;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SignUpDBO;

import static technource.autoreum.helper.Constants.notify_count;

/**
 * Created by technource on 5/9/17.
 */

public class LoginScreen extends BaseActivity implements OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final Integer FACEBOOK = 1;
    private static final Integer GOOGLE_PLUS = 2;
    private static final Integer GET_ACCOUNT = 3;
    private static final int RC_SIGN_IN = 4;
    private static final String TAG = "STATIC SCREEN";

    public static String FLAG_SIGNUP = "";
    public static String FLAG_USERTYPE = "";
    public String USER_TYPE = "";
    SignUpDBO data1;
    ImageView ll_back;
    TextView Signup;
    /*Facebook Login */
    CallbackManager callbackManager;
    LoginButton facebook_button;
    ImageView facebook, google;
    Context appContext;
    String type_id = null;
    EditText edt_username, edt_password;
    TextView loginBtn, forgetpassword, signup_txt;
    public static boolean isReviewPopupShown=false;

    /*Google + Login*/
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);
        getViews();

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        initUI();
        if (getIntent() != null) {
            USER_TYPE = getIntent().getStringExtra(Constants.USERTYPE);
        }
        clickListeners();
        registerCallback();
    }


    public void getViews() {
        Signup = (TextView) findViewById(R.id.signup_txt);
        String text = getString(R.string.Signuptext);
        Signup.setText(Html.fromHtml(text));
        ll_back = (ImageView) findViewById(R.id.ll_back);
        edt_username = (EditText) findViewById(R.id.edt_username);
        edt_password = (EditText) findViewById(R.id.edt_password);
        loginBtn = (TextView) findViewById(R.id.loginBtn);
        forgetpassword = (TextView) findViewById(R.id.forget_password);

    }

    private void initUI() {
        appContext = this;
        facebook = (ImageView) findViewById(R.id.facebook);
        google = (ImageView) findViewById(R.id.google);

        // Facebook Login Implementation
        facebook_button = (LoginButton) findViewById(R.id.login_button);
        callbackManager = CallbackManager.Factory.create();
        List<String> permissions = Arrays.asList("email", "public_profile");
        facebook_button.setReadPermissions(permissions);
        LoginManager.getInstance().logOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        mGoogleApiClient.connect();
    }

    private void clickListeners() {
        facebook.setOnClickListener(this);
        google.setOnClickListener(this);
        ll_back.setOnClickListener(this);
        loginBtn.setOnClickListener(this);
        forgetpassword.setOnClickListener(this);
        Signup.setOnClickListener(this);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.facebook:
                type_id = Constants.TYPE_FACEBOOK;

                if (Connectivity.isConnected(appContext)) {
                    LoginManager.getInstance().logOut();
                    askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, FACEBOOK);
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                break;
            case R.id.google:
                if (Connectivity.isConnected(appContext)) {
                    askForPermission(android.Manifest.permission.GET_ACCOUNTS, GET_ACCOUNT);
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.loginBtn:

                if (Connectivity.isConnected(appContext)) {
                    if (validate()) {
                        SignIn();
                    }
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }

                break;

            case R.id.forget_password:
                Intent intent = new Intent(LoginScreen.this, ForgotPassword.class);
                intent.putExtra("USER_TYPE", USER_TYPE);
                startActivity(intent);
                finish();
                activityTransition();
                break;
            case R.id.signup_txt:
                FLAG_SIGNUP = "1";
                FLAG_USERTYPE = USER_TYPE;
                if (USER_TYPE.equals(Constants.CAR_OWNER)) {
                    intent = new Intent(this, SignUpCarOwner.class);
                    startActivity(intent);
                } else {
                    intent = new Intent(this, SignUpGarageOwner.class);
                    startActivity(intent);
                }
                activityTransition();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    private boolean validate() {
        String email, password;
        Pattern patternSpace;
        Matcher matcherSpace;
        email = edt_username.getText().toString().trim();
        password = edt_password.getText().toString();

        if (email.trim().length() > 0) {
            if (email.contains("@")) {
                if (email != null && email.length() > 0) {
                    if (!HelperMethods.validateEmail(email)) {
                        showAlertDialog(getString(R.string.valid_email));
                        edt_username.requestFocus();
                        return false;
                    }
                } else {
                    showAlertDialog(getString(R.string.enter_email));
                    edt_username.requestFocus();
                    return false;
                }
            } else {
                if (email.length() < 4) {
                    edt_username.requestFocus();
                    showAlertDialog(getString(R.string.error_code_length));
                    return false;
                } else if (!HelperMethods.ValidUsername(edt_username.getText().toString())) {
                    edt_username.requestFocus();
                    showAlertDialog(getString(R.string.enter_valid_username));
                    return false;
                }
            }
        } else {
            edt_username.requestFocus();
            showAlertDialog(getString(R.string.error_enter_email_or_username));
            return false;
        }

        patternSpace = Pattern.compile("\\s");

        matcherSpace = patternSpace.matcher(password);

        boolean foundSpace = matcherSpace.find();
        if (password != null && password.length() > 0) {
            if (foundSpace) {
                showAlertDialog(getString(R.string.error_password_space));
                edt_password.requestFocus();
                return false;
            } else {
                patternSpace = Pattern.compile(Constants.PASSWORD_PATTERN);
                matcherSpace = patternSpace.matcher(password);
                if ((password.length() < 8 || password.length() > 15)) {
                    showAlertDialog(getString(R.string.error_password_length));
                    edt_password.requestFocus();
                    return false;
                }
            }
        } else {
            showAlertDialog(getString(R.string.error_password));
            edt_password.requestFocus();
            return false;
        }
        return true;
    }

    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            } else {
                ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            }
        } else {
            AppLog.Log(TAG, "Permission Already Granted");
            if (requestCode == FACEBOOK) {
                if (Connectivity.isConnected(appContext)) {
                    LoginManager.getInstance().logOut();
                    facebook_button.performClick();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
            } else if (requestCode == GOOGLE_PLUS) {
                type_id = Constants.TYPE_GOOGLEPLUS;
                if (Connectivity.isConnected(appContext)) {
                    signInWithGplus();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
            } else if (requestCode == GET_ACCOUNT) {
                askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, GOOGLE_PLUS);
            }
        }
    }

    private void signInWithGplus() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (type_id != null) {
            if (type_id.equalsIgnoreCase(Constants.TYPE_FACEBOOK)) {
                callbackManager.onActivityResult(requestCode, resultCode, data);

            }
        }

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            AppLog.Log("Result is hre", "" + result);
            handleSignInResult(result);
        }
    }


    private void handleSignInResult(GoogleSignInResult result) {
        GoogleSignInAccount acct = result.getSignInAccount();
        if (acct != null) {
            String id = acct.getId();
            String name = acct.getDisplayName();
            String profile = "";
            if (acct.getPhotoUrl() != null) {
                profile = acct.getPhotoUrl().toString();
            }

            AppLog.Log(TAG, "Profile in Google+" + profile);
            String email = acct.getEmail();
            AppLog.Log(TAG,
                    "Google+ Id: " + id + ", Name: " + name + ", email: " + email + "Profile" + profile);
            SocialLoginCheck(id, name, email, "", "", profile, SocialSignUp.GOOGLE);

            signOutFromGPlaus();
        }
    }

    //Facebook Integration
    private void registerCallback() {

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(final LoginResult loginResult) {
                        AppLog.Log(TAG, "Success Facebook");
                        final GraphRequest request = GraphRequest
                                .newMeRequest(AccessToken.getCurrentAccessToken(),
                                        new GraphRequest.GraphJSONObjectCallback() {
                                            @Override
                                            public void onCompleted(JSONObject object, GraphResponse response) {

                                                AppLog.Log(TAG, response.toString());

                                                try {
                                                    String id = object.getString("id");
                                                    try {
                                                        URL image_value = new URL(
                                                                "https://graph.facebook.com/" + object.getString("id")
                                                                        + "/picture?type=large");

                                                        AppLog.Log(TAG, "Facebook Url" + image_value.toString());

                                                        String name = object.getString("name");
                                                        String email = "";
                                                        if (object.has("email")) {
                                                            email = object.getString("email");
                                                        }
                                                        String first_name = object.getString("first_name");
                                                        String last_name = object.getString("last_name");
                                                        AppLog.Log(TAG, "Email : " + email + "\nName : " + name + "\nID : " + id
                                                                + "\n Proifile picture   " + image_value);

                                                        SocialLoginCheck(id, name, email, first_name, last_name,
                                                                "" + image_value,
                                                                SocialSignUp.FACEBOOK);

                                                        LoginManager.getInstance().logOut();

                                                    } catch (Exception e) {
                                                        AppLog.Log(TAG, e.getMessage());
                                                        e.printStackTrace();
                                                    }
                                                } catch (JSONException e) {
                                                    AppLog.Log(TAG, e.getMessage());
                                                    e.printStackTrace();
                                                }
                                            }
                                        });
                        Bundle parameters = new Bundle();
                        parameters.putString("fields", "id,name,email,gender,first_name,last_name,picture");
                        request.setParameters(parameters);
                        request.executeAsync();
                    }

                    @Override
                    public void onCancel() {
                        AppLog.Log(TAG, "onCancel Facebook");
                    }

                    @Override
                    public void onError(FacebookException error) {
                        AppLog.Log(TAG, "onError Facebook");
                        AppLog.Log(TAG, error.toString());
                    }
                });
    }

    private void signOutFromGPlaus() {
        try {
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SocialLoginCheck(final String id, final String name, final String email,
                                 final String first_name, final String last_name, final String image, final String type) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "login");
        params.put(LoginType.LOGIN_TYPE, LoginType.SOCIAL);
        params.put(LoginType.SOCIAL_ID, id);
        params.put(LoginType.DEVICE_TYPE, "1");
        params.put(LoginType.DEVICE_TOKEN, HelperMethods.getDeviceTokenFCM());
        params.put(LoginType.USER_TYPE, USER_TYPE);

        AppLog.Log("params", "" + params);

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response-->", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                                loginDetail_dbo
                                        .setUserid(response.getString(USER_DETAILS.USER_ID));
                                loginDetail_dbo
                                        .setJWTToken(response.getString(USER_DETAILS.JWT_TOKEN));
                                JSONObject json = new JSONObject(response.getString("data"));
                                loginDetail_dbo.setFirst_name(json.getString(USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(LoginType.IMAGE));
                                loginDetail_dbo.setUser_Type(USER_TYPE);
                                loginDetail_dbo.setGarage_balance(json.optString("garage_balance"));
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
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

                                Intent intent = new Intent(LoginScreen.this, DashboardScreen.class);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);
                                activityTransition();
                                finish();
                            } else {
                                if (response.has(Constants.MESSAGE) && response.getString(Constants.MESSAGE)
                                        .equalsIgnoreCase("Invalid social id")) {
                                    data1 = new SignUpDBO();
                                    data1.setSocialid(id);
                                    data1.setName(name);
                                    data1.setEmail(email);
                                    data1.setFirstname(first_name);
                                    data1.setLastname(last_name);
                                    data1.setImage(image);
                                    data1.setType(type);

                                    FLAG_SIGNUP = "2";
                                    FLAG_USERTYPE = USER_TYPE;

                                    if (FLAG_USERTYPE.equals("0")) {
                                        Intent intent = new Intent(LoginScreen.this, SignUpCarOwner.class);
                                        intent.putExtra("data", data1);
                                        startActivity(intent);
                                        finish();
                                        activityTransition();
                                    } else {
                                        Intent intent = new Intent(LoginScreen.this, SignUpGarageOwner.class);
                                        intent.putExtra("data", data1);
                                        startActivity(intent);
                                        finish();
                                        activityTransition();
                                    }

                                } else {
                                    showAlertDialog(response.getString(Constants.MESSAGE));
                                }
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

    public void SignIn() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "login");
        params.put(LoginType.LOGIN_TYPE, LoginType.NORMAL);
        params.put(LoginType.USERNAME, edt_username.getText().toString());
        params.put(LoginType.PASSWORD, edt_password.getText().toString());
        params.put(LoginType.DEVICE_TOKEN, HelperMethods.getDeviceTokenFCM());
        params.put(LoginType.DEVICE_TYPE, "1");
        params.put(LoginType.USER_TYPE, USER_TYPE);
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
                                isReviewPopupShown = true;
                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                                loginDetail_dbo
                                        .setJWTToken(response.getString(USER_DETAILS.JWT_TOKEN));

                                JSONObject json = new JSONObject(response.getString("data"));

                                loginDetail_dbo.setUserid(json.getString(LoginType.ID));

                                loginDetail_dbo.setFirst_name(json.getString(USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(LoginType.IMAGE));
                                loginDetail_dbo.setMobile(json.getString(USER_DETAILS.MOBILE));
                                loginDetail_dbo.setSuburb(json.getString(USER_DETAILS.SUBURB));
                                loginDetail_dbo.setState(json.getString(USER_DETAILS.STATE));
                                loginDetail_dbo.setPincode(json.getString(USER_DETAILS.POSTCODE));
                                loginDetail_dbo.setUser_Type(USER_TYPE);
                                loginDetail_dbo.setGarage_balance(json.optString("garage_balance"));
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
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

                                Intent intent = new Intent(LoginScreen.this, DashboardScreen.class);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);

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

            }
        });

        queue.add(jsonObjReq);
    }

}
