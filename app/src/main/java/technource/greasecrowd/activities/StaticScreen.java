package technource.greasecrowd.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.Request;
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

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import technource.greasecrowd.CustomViews.LoadingDialog.CustomDialog;
import technource.greasecrowd.R;
import technource.greasecrowd.firebase.MyFirebaseMessagingService;
import technource.greasecrowd.helper.AppController;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.Constants.LoginType;
import technource.greasecrowd.helper.Constants.SocialSignUp;
import technource.greasecrowd.helper.Constants.USER_DETAILS;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.SignUpDBO;

import static technource.greasecrowd.activities.LoginScreen.FLAG_SIGNUP;
import static technource.greasecrowd.activities.LoginScreen.FLAG_USERTYPE;

/**
 * Created by technource on 4/9/17.
 */

public class StaticScreen extends BaseActivity implements OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    private static final Integer FACEBOOK = 1;
    private static final Integer GOOGLE_PLUS = 2;
    private static final Integer GET_ACCOUNT = 3;
    private static final int RC_SIGN_IN = 4;
    private static final String TAG = "STATIC SCREEN";
    LinearLayout ll_userlogin, ll_garage_owner;

    /*Facebook Login */
    CallbackManager callbackManager;
    LoginButton facebook_button;
    ImageView facebook, google;
    Context appContext;
    String type_id = null, Show_type = null;
    String UserType = "";
    TextView UserSignUp, GarageUserSignUp;
    LoginDetail_DBO loginDetail_dbo;

    /*Google + Login*/
    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_static_screen);

        AppLog.Log("Token -----", HelperMethods.getDeviceTokenFCM());
        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));
        AppController.getInstance().printHashKey();
        initUI();
        clickListeners();
        registerCallback();

        Intent intent = getIntent();
        if (intent.hasExtra("message")) {
            if (intent.getStringExtra("flag").equalsIgnoreCase("1")) {
                String message = intent.getStringExtra("message");
                showAlertOkDialog(message);
            }
        }
    }



    public void showAlertOkDialog(String message) {

        dialogC = new CustomDialog(this, "ALERT", message);
        dialogC.setCanceledOnTouchOutside(false);
        if (dialogC != null && dialogC.isShowing()) {
            dialogC.dismiss();
            dialogC.show();
        } else {
            dialogC.show();
        }
        dialogC.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {

            }
        });
        dialogC.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {


            }
        });
    }

    private void initUI() {
        appContext = this;
        facebook = (ImageView) findViewById(R.id.facebook);
        google = (ImageView) findViewById(R.id.google);

        ll_userlogin = (LinearLayout) findViewById(R.id.ll_login);
        ll_garage_owner = (LinearLayout) findViewById(R.id.ll_garage_owner);

        UserSignUp = (TextView) findViewById(R.id.User_signup);
        GarageUserSignUp = (TextView) findViewById(R.id.GarageUser_signup);

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
        ll_userlogin.setOnClickListener(this);
        ll_garage_owner.setOnClickListener(this);
        UserSignUp.setOnClickListener(this);
        GarageUserSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent i;
        switch (view.getId()) {
            case R.id.facebook:
                Show_type = Constants.FACEBOOK;
                ShowPopup();
                break;
            case R.id.google:
                Show_type = Constants.TYPE_GOOGLEPLUS;
                ShowPopup();
                break;
            case R.id.ll_login:
                FLAG_SIGNUP = "1";
                FLAG_USERTYPE = "0";
                i = new Intent(appContext, LoginScreen.class);
                i.putExtra(Constants.USERTYPE, Constants.CAR_OWNER);
                startActivity(i);
                activityTransition();
                break;
            case R.id.ll_garage_owner:
                FLAG_SIGNUP = "1";
                FLAG_USERTYPE = "1";
                i = new Intent(appContext, LoginScreen.class);
                i.putExtra(Constants.USERTYPE, Constants.GERAGE_OWNER);
                startActivity(i);
                activityTransition();
                break;
            case R.id.User_signup:
                FLAG_SIGNUP = "1";
                FLAG_USERTYPE = "0";
                i = new Intent(appContext, SignUpCarOwner.class);
                startActivity(i);
                activityTransition();
                break;
            case R.id.GarageUser_signup:
                FLAG_SIGNUP = "1";
                FLAG_USERTYPE = "1";
                i = new Intent(appContext, SignUpGarageOwner.class);
                startActivity(i);
                activityTransition();
                break;
        }
    }

    public void ShowPopup() {
        CharSequence colors[] = new CharSequence[]{"Car Owner", "Garage Owner"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick a User");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (which == 0) {
                    UserType = "0";
                } else {
                    UserType = "1";
                }

                if (Show_type.equals(Constants.FACEBOOK)) {
                    type_id = Constants.TYPE_FACEBOOK;
                    LoginManager.getInstance().logOut();
                    askForPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, FACEBOOK);
                } else {
                    askForPermission(android.Manifest.permission.GET_ACCOUNTS, GET_ACCOUNT);
                }
            }
        });
        builder.show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (ActivityCompat.checkSelfPermission(this, permissions[0])
                == PackageManager.PERMISSION_GRANTED) {
            AppLog.Log(TAG, "requestCode " + "Yesssss" + requestCode);
            switch (requestCode) {
                //Write external Storage
                case 1:
                    AppLog.Log(TAG, "FB Permission granted");
                    LoginManager.getInstance().logOut();
                    if (Connectivity.isConnected(appContext)) {
                        LoginManager.getInstance().logOut();
                        facebook_button.performClick();
                    } else {
                        showAlertDialog(getResources().getString(R.string.no_internet));
                    }
                    break;
                case 3:
                    AppLog.Log(TAG, "Google + Permission granted");
                    if (Connectivity.isConnected(appContext)) {
                        signInWithGplus();
                    } else {
                        showAlertDialog(getResources().getString(R.string.no_internet));
                    }
                    break;
            }
            AppLog.Log(TAG, "Permission granted");
        } else {
            AppLog.Log(TAG, "Permission denied");
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
        params.put(LoginType.USER_TYPE, UserType);

        AppLog.Log("params for fb", "-->" + params);
        JsonObjectRequest jsonObjReq = new JsonObjectRequest(url, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject json = new JSONObject(response.getString("data"));
                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();

                                loginDetail_dbo.setUserid(response.getString(USER_DETAILS.USER_ID));
                                loginDetail_dbo
                                        .setJWTToken(response.getString(USER_DETAILS.JWT_TOKEN));
                                loginDetail_dbo.setUser_Type(UserType);
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
                                loginDetail_dbo.setFirst_name(json.getString(Constants.USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(Constants.USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(Constants.LoginType.IMAGE));
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                MyPreference user_preference = new MyPreference(appContext);
                                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                                user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);

                                Intent intent = new Intent(StaticScreen.this, DashboardScreen.class);
                                intent.addFlags(Constants.INTENT_FLAGS);
                                startActivity(intent);

                            } else {

                                if (response.has(Constants.MESSAGE) && response.getString(Constants.MESSAGE)
                                        .equalsIgnoreCase("Invalid social id")) {
                                    SignUpDBO data = new SignUpDBO();
                                    data.setSocialid(id);
                                    data.setName(name);
                                    data.setEmail(email);
                                    data.setFirstname(first_name);
                                    data.setLastname(last_name);
                                    data.setImage(image);
                                    AppLog.Log("image", "facebook--->" + image);
                                    data.setType(type);

                                    FLAG_SIGNUP = "2";
                                    FLAG_USERTYPE = UserType;

                                    if (FLAG_USERTYPE.equals("0")) {
                                        Intent intent = new Intent(appContext, SignUpCarOwner.class);
                                        intent.putExtra("data", data);
                                        startActivity(intent);
                                    } else {
                                        Intent intent = new Intent(appContext, SignUpGarageOwner.class);
                                        intent.putExtra("data", data);
                                        startActivity(intent);
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

}
