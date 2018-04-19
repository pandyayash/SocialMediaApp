package technource.autoreum.fragment;

import static technource.autoreum.activities.LoginScreen.FLAG_SIGNUP;
import static technource.autoreum.activities.SignUpCarOwner.current_position;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

import technource.autoreum.R;
import technource.autoreum.activities.SignUpCarOwner;
import technource.autoreum.activities.SignUpGarageOwner;
import technource.autoreum.activities.StaticWebpages;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.LoginType;
import technource.autoreum.helper.Constants.SIGN_UP;
import technource.autoreum.helper.Constants.USER_DETAILS;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SignUpDBO;

/**
 * Created by technource on 6/9/17.
 */

public class fragmentRegisterAccount extends Fragment implements OnClickListener {

    LinearLayout ll_personal_details, ll_user_details, ll_your_details;
    TextView next_personal_details, next_user_details, next_your_details, termsandpolicy, editstate, txtzero;
    View v;
    EditText edt_fname, edt_lname, edt_email, edt_confirm_email, edt_mobile;
    EditText edt_username, edt_password, edt_confimepass;
    EditText edt_subrub, edt_refferelcode, edt_post_code;
    LinearLayout ll_password, ll_confirmpasseord;
    Context appContext;
    CheckBox isNews, terms;
    String isnews_ = "";
    String lastChar = " ";
    SignUpDBO data; // Normal : 1 , Social: 2
    String user_id;
    String device_token;
    LinearLayout ll_fname, ll_lname, ll_email, ll_c_email, ll_contact, ll_username, ll_state, ll_subrub, ll_post_code, ll_refferelcode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_sign_account, container, false);
        getViews();
        setonClickListener();
        //setDefault();
        if (FLAG_SIGNUP.equalsIgnoreCase("2")) {
            setViews();
            ll_confirmpasseord.setVisibility(View.GONE);
            ll_password.setVisibility(View.GONE);
        }
        return v;
    }

    public void setDefault() {
        ll_fname.getBackground().setLevel(0);
        ll_lname.getBackground().setLevel(0);
        ll_email.getBackground().setLevel(0);
        ll_c_email.getBackground().setLevel(0);
        ll_contact.getBackground().setLevel(0);
    }

    private void setViews() {
        if (data != null) {
            if (data.getFirstname() != null) {
                edt_fname.setText(data.getFirstname());
            }
            if (data.getLastname() != null) {
                edt_lname.setText(data.getLastname());
            }
            if (data.getEmail() != null) {
                edt_email.setText(data.getEmail());
                edt_confirm_email.setText(data.getEmail());
                if (!edt_email.getText().toString().equalsIgnoreCase("")) {
                    edt_email.setEnabled(false);
                }
                if (!edt_confirm_email.getText().toString().equalsIgnoreCase("")) {
                    edt_confirm_email.setEnabled(false);
                }
            }
        }
    }

    public void getViews() {
        device_token = HelperMethods.getDeviceTokenFCM();
        appContext = getActivity();
        ll_personal_details = v.findViewById(R.id.ll_personal_details);
        ll_user_details = v.findViewById(R.id.ll_user_details);
        ll_your_details = v.findViewById(R.id.ll_your_details);
        ll_fname = v.findViewById(R.id.ll_fname);
        ll_lname = v.findViewById(R.id.ll_lname);
        ll_email = v.findViewById(R.id.ll_email);
        ll_c_email = v.findViewById(R.id.ll_c_email);
        ll_contact = v.findViewById(R.id.ll_contact);
        ll_state = v.findViewById(R.id.ll_state);
        ll_subrub = v.findViewById(R.id.ll_subrub);
        ll_post_code = v.findViewById(R.id.ll_post_code);
        ll_refferelcode = v.findViewById(R.id.ll_refferelcode);
        ll_username = v.findViewById(R.id.ll_username);

        next_personal_details = v.findViewById(R.id.next_personal_details);
        next_user_details = v.findViewById(R.id.next_user_details);
        next_your_details = v.findViewById(R.id.next_your_details);
        ll_password = v.findViewById(R.id.ll_password);
        ll_confirmpasseord = v.findViewById(R.id.ll_confirm_password);

        edt_fname = v.findViewById(R.id.edt_fname);
        edt_fname.requestFocus();
        edt_lname = v.findViewById(R.id.edt_lname);
        edt_email = v.findViewById(R.id.edt_email);
        edt_confirm_email = v.findViewById(R.id.edt_confirm_email);
        edt_mobile = v.findViewById(R.id.edt_mobile);

        edt_username = v.findViewById(R.id.edt_uname);
        edt_password = v.findViewById(R.id.edt_password);
        edt_confimepass = v.findViewById(R.id.edt_confimepass);
        editstate = v.findViewById(R.id.edt_state);
        edt_subrub = v.findViewById(R.id.edt_subrub);
        edt_refferelcode = v.findViewById(R.id.edt_refferelcode);
        edt_post_code = v.findViewById(R.id.edt_post_code);
        isNews = v.findViewById(R.id.newslettr);
        terms = v.findViewById(R.id.terms);
        termsandpolicy = v.findViewById(R.id.termsandpolicy);
        txtzero = v.findViewById(R.id.txtzero);

        data = ((SignUpCarOwner) appContext).data;
        current_position = 0;
        setPersonalLayout();


        edt_fname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_fname);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_lname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_lname);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_email);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });

        edt_confirm_email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_c_email);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_contact);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_username.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_username);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_password);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_confimepass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_confirmpasseord);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });

        edt_subrub.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_subrub);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });
        edt_post_code.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() > 0) {
                    HelperMethods.Valid(appContext, ll_post_code);
                } else {
                    // HelperMethods.ValidateFields(appContext,ll_fname);
                }
            }
        });

    }


    public void setonClickListener() {
        next_personal_details.setOnClickListener(this);
        next_user_details.setOnClickListener(this);
        next_your_details.setOnClickListener(this);
        editstate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_personal_details:
                //checkValidation();
                if (isPersonalDeatilsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        serviceStepOne();
                    } else {
                        ((SignUpCarOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }

                }
                break;
            case R.id.next_user_details:
                if (isUserDeatilsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        serviceStepTwo();
                    } else {
                        ((SignUpCarOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }

                }
                break;
            case R.id.next_your_details:
                if (isNews.isChecked()) {
                    isnews_ = "1";
                } else {
                    isnews_ = "0";
                }
                if (isYourDeatilsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        SignUpCarOwner();
                    } else {
                        ((SignUpCarOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }
                }
                break;
            case R.id.edt_state:
                openStateList();
                break;
        }
    }

    public void serviceStepOne() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        String strMobile = edt_mobile.getText().toString();
        strMobile = strMobile.substring(1);
        AppLog.Log("strMobile", strMobile);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(SIGN_UP.SERVICE_NAME, "chk_unq");
        params.put("un", "");
        params.put("em", edt_confirm_email.getText().toString());
        params.put("number", "+61 " + strMobile);
        params.put(LoginType.USER_TYPE, "0");

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

                                ((SignUpCarOwner) appContext).Email = edt_email.getText().toString();
                                setUserLayout();
                            } else {
                                if (response.getString("em").equalsIgnoreCase("1")) {
                                    String msg = "Email " + response.getString(Constants.MESSAGE);
                                    ((SignUpCarOwner) appContext).showAlertDialog(msg);
                                }
                                if (response.getString("number").equalsIgnoreCase("1")) {
                                    String msg = "Number " + response.getString(Constants.MESSAGE);
                                    ((SignUpCarOwner) appContext).showAlertDialog(msg);
                                }

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }

    public void serviceStepTwo() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        String strMobile = edt_mobile.getText().toString();
        strMobile = strMobile.substring(1);
        AppLog.Log("strMobile", strMobile);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(SIGN_UP.SERVICE_NAME, "chk_unq");
        params.put("un", edt_username.getText().toString());
        params.put("em", edt_confirm_email.getText().toString());
        params.put("number", "+61 " + strMobile);
        params.put(LoginType.USER_TYPE, "0");

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

                                ((SignUpCarOwner) appContext).Email = edt_email.getText().toString();
                                setYourDetailLayout();
                            } else {
                                if (response.getString("un").equalsIgnoreCase("1")) {
                                    String msg = "Username " + response.getString(Constants.MESSAGE);
                                    ((SignUpCarOwner) appContext).showAlertDialog(msg);
                                }

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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);

    }

    public void setPersonalLayout() {
        current_position = 0;
        ll_personal_details.setVisibility(View.VISIBLE);
        ll_user_details.setVisibility(View.GONE);
        ll_your_details.setVisibility(View.GONE);
    }

    public void setUserLayout() {
        current_position = 1;
        ll_user_details.setVisibility(View.VISIBLE);
        ll_personal_details.setVisibility(View.GONE);
        ll_your_details.setVisibility(View.GONE);
    }

    public void setYourDetailLayout() {
        current_position = 2;
        ll_your_details.setVisibility(View.VISIBLE);
        ll_user_details.setVisibility(View.GONE);
        ll_personal_details.setVisibility(View.GONE);
        //span();
        spanPrivacyPolicy(getString(R.string.by_using_grease_crowd_you_agree_to_our_to_our_terms_of_use_and_privacy_policy), "Terms of Service", "Privacy Policy");
    }


    public boolean isPersonalDeatilsValidate() {
        String fname, lname, email, confirm_email, mobile;
        boolean isFirst = true;


        fname = edt_fname.getText().toString();
        lname = edt_lname.getText().toString();
        email = edt_email.getText().toString();
        confirm_email = edt_confirm_email.getText().toString();
        mobile = edt_mobile.getText().toString();
        if (fname != null && fname.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_fname);
        } else {
            HelperMethods.ValidateFields(appContext, ll_fname);
            edt_fname.requestFocus();
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.enter_fname));
                isFirst = false;
            }


        }

        if (lname != null && lname.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_lname);
        } else {
            edt_lname.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_lname);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.enter_lname));
                isFirst = false;
            }


        }

        if (email != null && email.length() > 0) {
            HelperMethods.Valid(appContext, ll_email);
            if (!HelperMethods.validateEmail(email)) {
                edt_email.requestFocus();
                if (isFirst) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.valid_email));
                    isFirst = false;
                }


            }
        } else {
            edt_email.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_email);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.please_enter_email));
                isFirst = false;
            }


        }

        if (confirm_email != null && confirm_email.length() > 0) {
            HelperMethods.Valid(appContext, ll_c_email);
            if (!HelperMethods.validateEmail(confirm_email)) {
                edt_confirm_email.requestFocus();
                if (isFirst) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.valid_email));
                    isFirst = false;
                }


            }
        } else {
            edt_confirm_email.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_c_email);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.enter_confirm_email));
                isFirst = false;
            }


        }

        if (confirm_email.equals(email)) {
        } else {
            edt_confirm_email.requestFocus();
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.email_should_be_same));

                isFirst = false;
            }

        }

        if (mobile != null && mobile.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_contact);
            if (mobile.length() < 9) {
                if (isFirst) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getResources().getString(R.string.mobile_length_10));
                    edt_mobile.requestFocus();

                    isFirst = false;
                }


            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_contact);
            if (isFirst) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.please_enter_mobile));

                isFirst = false;
            }

            edt_mobile.requestFocus();

        }
        return isFirst;

    }


    public boolean isUserDeatilsValidate() {
        String username, password, confirm_password;
        Pattern patternSpace;
        Matcher matcherSpace;
        username = edt_username.getText().toString();
        password = edt_password.getText().toString();
        confirm_password = edt_confimepass.getText().toString();
        boolean isSecond = true;

        if (username != null && username.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_username);
            if (username.length() < 4) {
                edt_username.requestFocus();
                if (isSecond) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.error_code_length));
                    isSecond = false;
                }


            } else if (!HelperMethods.ValidUsername(edt_username.getText().toString())) {
                edt_username.requestFocus();
                if (isSecond) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.enter_valid_username));
                    isSecond = false;
                }


            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_username);
            edt_username.requestFocus();
            if (isSecond) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.enter_username));
                isSecond = false;
            }
        }

        if (!FLAG_SIGNUP.equalsIgnoreCase("2")) {
            patternSpace = Pattern.compile("\\s");

            matcherSpace = patternSpace.matcher(password);

            boolean foundSpace = matcherSpace.find();
            if (password != null && password.length() > 0) {
                HelperMethods.Valid(appContext, ll_password);
                if (foundSpace) {

                    edt_password.requestFocus();
                    if (isSecond) {
                        ((SignUpCarOwner) appContext)
                                .showAlertDialog(getString(R.string.error_password_space));
                        isSecond = false;
                    }

                } else {
                    patternSpace = Pattern.compile(Constants.PASSWORD_PATTERN);
                    matcherSpace = patternSpace.matcher(password);

                    if ((password.length() < 8 || password.length() > 15)) {

                        edt_password.requestFocus();
                        if (isSecond) {
                            ((SignUpCarOwner) appContext)
                                    .showAlertDialog(getString(R.string.error_password_length));
                            isSecond = false;
                        }

                    }
                }
            } else {
                HelperMethods.ValidateFields(appContext, ll_password);

                edt_password.requestFocus();
                if (isSecond) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.error_password));
                    isSecond = false;
                }

            }

            if (confirm_password != null && confirm_password.length() > 0) {
                HelperMethods.Valid(appContext, ll_confirmpasseord);
            } else {
                HelperMethods.ValidateFields(appContext, ll_confirmpasseord);

                edt_confimepass.requestFocus();
                if (isSecond) {
                    ((SignUpCarOwner) appContext)
                            .showAlertDialog(getString(R.string.error_confirmpassword));
                    isSecond = false;
                }

            }
        }

        if (password.equals(confirm_password)) {

        } else {

            edt_confimepass.requestFocus();
            if (isSecond) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.error_not_match_pass));
                isSecond = false;
            }

        }

        return isSecond;
    }

    public boolean isYourDeatilsValidate() {
        String state, subrub, postcode;
        state = editstate.getText().toString();
        subrub = edt_subrub.getText().toString();
        postcode = edt_post_code.getText().toString();
        boolean isThird = true;

        if (state != null && state.length() > 0) {
            HelperMethods.Valid(appContext, ll_state);
        } else {
            HelperMethods.ValidateFields(appContext, ll_state);
            if (isThird) {

                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.error_state));
                isThird = false;
            }
            editstate.requestFocus();

        }

        if (subrub != null && subrub.length() > 0) {

        } else {
            HelperMethods.ValidateFields(appContext, ll_subrub);
            edt_subrub.requestFocus();
            if (isThird) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.error_subrub));
                isThird = false;
            }

        }

        if (postcode != null && postcode.length() > 0) {
            if (postcode.length() < 4) {
                if (isThird) {
                    ((SignUpCarOwner) appContext).showAlertDialog(getString(R.string.error_postcode_lenght));
                    isThird = false;
                }

                editstate.requestFocus();

            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_post_code);
            if (isThird) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.error_postcode));
                isThird = false;
            }

            editstate.requestFocus();

        }
        if (!terms.isChecked()) {
            if (isThird) {
                ((SignUpCarOwner) appContext)
                        .showAlertDialog(getString(R.string.error_terms_polity));
                isThird = false;
            }



        }


        return isThird;
    }

    public void openStateList() {
        final CharSequence colors[] = new CharSequence[]{"ACT", "NSW", "NT", "QLD", "SA", "TAS", "VIC",
                "WA"};

        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Select State");
        builder.setItems(colors, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                editstate.setText(colors[which]);
                HelperMethods.Valid(appContext, ll_state);
            }
        });
        builder.show();
    }

    public void SignUpCarOwner() {
        ((SignUpCarOwner) appContext).showLoadingDialog(true);
        String strMobile = edt_mobile.getText().toString();
        strMobile = strMobile.substring(1);
        AppLog.Log("strMobile", strMobile);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(SIGN_UP.SERVICE_NAME, "signup");
        params.put(SIGN_UP.SIGNUP_TYPE, FLAG_SIGNUP);
        params.put(SIGN_UP.FN, edt_fname.getText().toString());
        params.put(SIGN_UP.LN, edt_lname.getText().toString());
        params.put(SIGN_UP.EMAIL, edt_confirm_email.getText().toString());
        params.put(SIGN_UP.MOBILE, "+61 " + strMobile);
        params.put(SIGN_UP.UN, edt_username.getText().toString());
        params.put(SIGN_UP.STATE, editstate.getText().toString());
        params.put(SIGN_UP.PWD, edt_password.getText().toString());
        params.put(SIGN_UP.SUB, edt_subrub.getText().toString());
        params.put(SIGN_UP.ZIP, edt_post_code.getText().toString());
        params.put(SIGN_UP.IS_NEWSLETTER, isnews_);
        params.put(SIGN_UP.REF_CODE, edt_refferelcode.getText().toString());
        params.put(LoginType.DEVICE_TYPE, "1");
        params.put(LoginType.DEVICE_TOKEN, device_token);

        if (FLAG_SIGNUP.equals("2")) {
            params.put(SIGN_UP.SOCIAL_ID, data.getSocialid());
            params.put("social_image", data.getImage());
        } else {
            params.put(SIGN_UP.SOCIAL_ID, "");
            params.put("social_image", "");
        }

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

                                Toast
                                        .makeText(appContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT)
                                        .show();
                                LoginDetail_DBO loginDetail_dbo = new LoginDetail_DBO();
                                loginDetail_dbo
                                        .setJWTToken(response.getString("jwt_token"));
                                loginDetail_dbo
                                        .setUserid(response.getString(USER_DETAILS.USER_ID));

                                JSONObject json = new JSONObject(response.getString("data"));

                                loginDetail_dbo.setUserid(json.getString(LoginType.ID));
                                loginDetail_dbo.setFirst_name(json.getString(Constants.USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(Constants.USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(Constants.LoginType.IMAGE));

                                loginDetail_dbo.setUser_Type("0");
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                MyPreference user_preference = new MyPreference(appContext);
                                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                                user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);
                                ((SignUpCarOwner) appContext).Email = edt_email.getText().toString();
                                ((SignUpCarOwner) appContext).Password = edt_confimepass.getText().toString();

                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                ((SignUpCarOwner) appContext).tabLayout.setScrollPosition(1, 0f, true);
                                ((SignUpCarOwner) appContext).viewPager.setCurrentItem(1);
                                current_position = 3;
                                ((SignUpCarOwner) appContext).ll_back.setVisibility(View.GONE);
                                ((SignUpCarOwner) appContext).tv_skipCarSign.setVisibility(View.VISIBLE);
                            } else {
                                ((SignUpCarOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                                if (response.getString(Constants.MESSAGE).equals("Email already exists")) {
                                    setPersonalLayout();
                                } else if (response.getString(Constants.MESSAGE)
                                        .equals("Mobile number already exists")) {
                                    setPersonalLayout();
                                } else if (response.getString(Constants.MESSAGE)
                                        .equals("Username already exists")) {
                                    setUserLayout();
                                }
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

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }

    private void createCustomTab(String url) {
        CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder()
                .setExitAnimations(appContext, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        BitmapDrawable current = (BitmapDrawable) getResources()
                .getDrawable(R.drawable.arrow);
        builder.setCloseButtonIcon(current.getBitmap());
        builder.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        builder.setShowTitle(true);
        CustomTabsIntent customTabsIntent = builder.build();
        customTabsIntent.intent.setPackage("com.android.chrome");
        customTabsIntent.launchUrl(appContext, Uri.parse(url));

    }

    public void span() {
        SpannableString ss = new SpannableString(getString(R.string.by_using_grease_crowd_you_agree_to_our_to_our_terms_of_use_and_privacy_policy));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent intent = new Intent(getActivity(), StaticWebpages.class);
                intent.putExtra("flag", "TERMS OF USE");
                intent.putExtra("page_id", "5");
                startActivity(intent);
                ((SignUpCarOwner) appContext).activityTransition();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent intent = new Intent(getActivity(), StaticWebpages.class);
                intent.putExtra("flag", "PRIVACY POLICY");
                intent.putExtra("page_id", "6");
                startActivity(intent);
                ((SignUpCarOwner) appContext).activityTransition();

            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ss.setSpan(clickableSpan, 39, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 56, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsandpolicy.setText(ss);
        termsandpolicy.setMovementMethod(LinkMovementMethod.getInstance());
        termsandpolicy.setHighlightColor(Color.WHITE);
    }

    public void spanPrivacyPolicy(String fulltext, String subString, String sub2) {

        SpannableString ss = new SpannableString(fulltext);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(getActivity(), StaticWebpages.class);
                intent.putExtra("flag", "TERMS OF USE");
                intent.putExtra("page_id", "6");
                startActivity(intent);
                ((SignUpGarageOwner) appContext).activityTransition();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };
        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {

                Intent intent = new Intent(getActivity(), StaticWebpages.class);
                intent.putExtra("flag", "PRIVACY POLICY");
                intent.putExtra("page_id", "6");
                startActivity(intent);
                ((SignUpGarageOwner) appContext).activityTransition();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
            }
        };


        int i = fulltext.indexOf(subString);
        int j = fulltext.indexOf(sub2);
        ss.setSpan(clickableSpan, i, i + subString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, j, j + sub2.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        termsandpolicy.setText(ss);
        termsandpolicy.setMovementMethod(LinkMovementMethod.getInstance());
        termsandpolicy.setHighlightColor(Color.WHITE);
    }


}
