package technource.autoreum.fragment;

import static technource.autoreum.activities.LoginScreen.FLAG_SIGNUP;
import static technource.autoreum.activities.SignUpGarageOwner.current_position_garage;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
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

import technource.autoreum.CustomViews.CustomTextWatcher;
import technource.autoreum.R;
import technource.autoreum.activities.SignUpGarageOwner;
import technource.autoreum.activities.StaticWebpages;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.LoginType;
import technource.autoreum.helper.Constants.SIGN_UP;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SignUpDBO;

/**
 * Created by technource on 6/9/17.
 */

public class fragmentGarageDetails extends Fragment implements OnClickListener {

    LinearLayout ll_owner_details, ll_user_details, ll_business_details;
    TextView next_owner_details, next_user_details, next_business_details, termsandpolicy, editstate;
    Context appContext;
    EditText edt_businessname, edt_fname, edt_lname, edt_email, edt_confirm_email, edt_mobile, edt_abn_number, edt_telephone;
    EditText edt_username, edt_password, edt_confimepass;
    EditText edt_subrub, edt_refferelcode, edt_post_code, edt_streetnumber;
    CheckBox newslettr, terms;
    View v;
    LinearLayout ll_password, ll_confirm_password;
    LinearLayout ll_businessname, ll_fname, ll_lname, ll_business_email, ll_business_c_email, ll_business_mobile;
    LinearLayout ll_business_telephone, ll_username, ll_streetnumber, ll_subrub, ll_state, ll_post_code, ll_abn_no;
    String lastChar = " ";
    String isnews_ = "";
    SignUpDBO data;
    String user_id;
    String device_token;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_details, container, false);

        getViews();
        setonClickListener();
        //span();
        //spanTermsOfUse(getString(R.string.by_using_grease_crowd_you_agree_to_our_to_our_terms_of_use_and_privacy_policy),"Terms of Service");
        spanPrivacyPolicy(getString(R.string.by_using_grease_crowd_you_agree_to_our_to_our_terms_of_use_and_privacy_policy), "Terms of Service", "Privacy Policy");
        if (FLAG_SIGNUP.equalsIgnoreCase("2")) {
            setViews();
            ll_confirm_password.setVisibility(View.GONE);
            ll_password.setVisibility(View.GONE);
        }

        return v;
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
        appContext = getActivity();
        ll_owner_details = (LinearLayout) v.findViewById(R.id.ll_owner_details);
        ll_user_details = (LinearLayout) v.findViewById(R.id.ll_user_details);
        ll_business_details = (LinearLayout) v.findViewById(R.id.ll_business_details);
        ll_businessname = (LinearLayout) v.findViewById(R.id.ll_businessname);
        ll_fname = (LinearLayout) v.findViewById(R.id.ll_fname);
        ll_lname = (LinearLayout) v.findViewById(R.id.ll_lname);
        ll_business_email = (LinearLayout) v.findViewById(R.id.ll_business_email);
        ll_business_c_email = (LinearLayout) v.findViewById(R.id.ll_business_c_email);
        ll_business_mobile = (LinearLayout) v.findViewById(R.id.ll_business_mobile);
        ll_business_telephone = (LinearLayout) v.findViewById(R.id.ll_business_telephone);
        ll_username = (LinearLayout) v.findViewById(R.id.ll_username);
        ll_streetnumber = (LinearLayout) v.findViewById(R.id.ll_streetnumber);
        ll_subrub = (LinearLayout) v.findViewById(R.id.ll_subrub);
        ll_state = (LinearLayout) v.findViewById(R.id.ll_state);
        ll_post_code = (LinearLayout) v.findViewById(R.id.ll_post_code);
        ll_abn_no = (LinearLayout) v.findViewById(R.id.ll_abn_no);

        ll_password = (LinearLayout) v.findViewById(R.id.ll_password);
        ll_confirm_password = (LinearLayout) v.findViewById(R.id.ll_confirm_password);

        next_owner_details = (TextView) v.findViewById(R.id.next_owner_details);
        termsandpolicy = (TextView) v.findViewById(R.id.termsandpolicy);
        next_user_details = (TextView) v.findViewById(R.id.next_user_details);
        next_business_details = (TextView) v.findViewById(R.id.next_business_details);

        edt_businessname = (EditText) v.findViewById(R.id.edt_businessname);
        edt_businessname.requestFocus();
        edt_abn_number = (EditText) v.findViewById(R.id.edt_abn_number);
        edt_fname = (EditText) v.findViewById(R.id.edt_fname);
        edt_lname = (EditText) v.findViewById(R.id.edt_lname);
        edt_email = (EditText) v.findViewById(R.id.edt_email);
        edt_confirm_email = (EditText) v.findViewById(R.id.edt_confirm_email);
        edt_mobile = (EditText) v.findViewById(R.id.edt_mobile);
        edt_telephone = (EditText) v.findViewById(R.id.edt_telephone);

        edt_username = (EditText) v.findViewById(R.id.edt_uname);
        edt_password = (EditText) v.findViewById(R.id.edt_password);
        edt_confimepass = (EditText) v.findViewById(R.id.edt_confimepass);

        edt_streetnumber = (EditText) v.findViewById(R.id.edt_streetnumber);
        editstate = (TextView) v.findViewById(R.id.edt_state);
        edt_subrub = (EditText) v.findViewById(R.id.edt_subrub);
        edt_refferelcode = (EditText) v.findViewById(R.id.edt_refferelcode);
        edt_post_code = (EditText) v.findViewById(R.id.edt_post_code);
        newslettr = (CheckBox) v.findViewById(R.id.newslettr);
        terms = (CheckBox) v.findViewById(R.id.terms);
        data = ((SignUpGarageOwner) appContext).data;
        device_token = HelperMethods.getDeviceTokenFCM();

        ((SignUpGarageOwner) appContext).tv_skipCarSign.setVisibility(View.GONE);
        setGarageDetailsLayout();

        setMobileNumberFormatting(edt_telephone);

        edt_businessname.addTextChangedListener(new CustomTextWatcher(appContext, edt_businessname, ll_businessname));
        edt_fname.addTextChangedListener(new CustomTextWatcher(appContext, edt_fname, ll_fname));
        edt_lname.addTextChangedListener(new CustomTextWatcher(appContext, edt_lname, ll_lname));
        edt_email.addTextChangedListener(new CustomTextWatcher(appContext, edt_email, ll_business_email));
        edt_confirm_email.addTextChangedListener(new CustomTextWatcher(appContext, edt_confirm_email, ll_business_c_email));
        edt_mobile.addTextChangedListener(new CustomTextWatcher(appContext, edt_mobile, ll_business_mobile));
        edt_abn_number.addTextChangedListener(new CustomTextWatcher(appContext, edt_abn_number, ll_abn_no));
        edt_username.addTextChangedListener(new CustomTextWatcher(appContext, edt_username, ll_username));
        edt_password.addTextChangedListener(new CustomTextWatcher(appContext, edt_password, ll_password));
        edt_confimepass.addTextChangedListener(new CustomTextWatcher(appContext, edt_confimepass, ll_confirm_password));
        edt_streetnumber.addTextChangedListener(new CustomTextWatcher(appContext, edt_streetnumber, ll_streetnumber));
        edt_subrub.addTextChangedListener(new CustomTextWatcher(appContext, edt_subrub, ll_subrub));
        edt_post_code.addTextChangedListener(new CustomTextWatcher(appContext, edt_post_code, ll_post_code));


    }

    public void setMobileNumberFormatting(final EditText edit) {
        edit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = edit.getText().toString().length();
                if (digits > 1) {
                    lastChar = edit.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = edit.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals(" ")) {
                    if (digits == 4 || digits == 8) {
                        edit.append(" ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    public void setGarageDetailsLayout() {
        current_position_garage = 0;
        ((SignUpGarageOwner) appContext).ll_back.setVisibility(View.VISIBLE);
        ll_owner_details.setVisibility(View.VISIBLE);
        ll_user_details.setVisibility(View.GONE);
        ll_business_details.setVisibility(View.GONE);
    }

    public void setUserLayout() {
        current_position_garage = 1;
        ((SignUpGarageOwner) appContext).ll_back.setVisibility(View.VISIBLE);
        ll_user_details.setVisibility(View.VISIBLE);
        ll_owner_details.setVisibility(View.GONE);
        ll_business_details.setVisibility(View.GONE);
    }

    public void setBUsinessDetailLayout() {
        current_position_garage = 2;
        ((SignUpGarageOwner) appContext).ll_back.setVisibility(View.VISIBLE);
        ll_business_details.setVisibility(View.VISIBLE);
        ll_user_details.setVisibility(View.GONE);
        ll_owner_details.setVisibility(View.GONE);
    }

    public void setonClickListener() {
        next_owner_details.setOnClickListener(this);
        next_user_details.setOnClickListener(this);
        next_business_details.setOnClickListener(this);
        editstate.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.next_owner_details:
                if (isOwnerDeatilsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        serviceStepOneGArage();

                    } else {
                        ((SignUpGarageOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }
                }
                break;
            case R.id.next_user_details:
                if (isUserDeatilsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        serviceStepTwoGArage();

                    } else {
                        ((SignUpGarageOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }

                }
                break;
            case R.id.next_business_details:
                if (newslettr.isChecked()) {
                    isnews_ = "1";
                } else {
                    isnews_ = "0";
                }
                if (isBusinessDetailsValidate()) {
                    if (Connectivity.isConnected(appContext)) {
                        SignUpGarageDetails();

                    } else {
                        AppLog.Log("This part", "No Connectivity");
                        ((SignUpGarageOwner) appContext)
                                .showAlertDialog(getResources().getString(R.string.no_internet));
                    }

                }
                break;
            case R.id.edt_state:
                openStateList();
                break;
        }
    }

    public void serviceStepOneGArage() {
        ((SignUpGarageOwner) appContext).showLoadingDialog(true);
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
        params.put(LoginType.USER_TYPE, "1");

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

                                ((SignUpGarageOwner) appContext).Email = edt_email.getText().toString();
                                setUserLayout();
                            } else {
                                if (response.getString("em").equalsIgnoreCase("1")) {
                                    String msg = "Email " + response.getString(Constants.MESSAGE);
                                    ((SignUpGarageOwner) appContext).showAlertDialog(msg);
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
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }

    public void serviceStepTwoGArage() {
        ((SignUpGarageOwner) appContext).showLoadingDialog(true);
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
        params.put(LoginType.USER_TYPE, "1");

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

                                ((SignUpGarageOwner) appContext).Email = edt_email.getText().toString();
                                setBUsinessDetailLayout();
                            } else {
                                if (response.getString("un").equalsIgnoreCase("1")) {
                                    String msg = "Username " + response.getString(Constants.MESSAGE);
                                    ((SignUpGarageOwner) appContext).showAlertDialog(msg);
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
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }

    public boolean isOwnerDeatilsValidate() {
        String businessnane, fname, lname, email, confirm_email, mobile, telephone, Abnnumber;
        boolean isFirst = true;
        businessnane = edt_businessname.getText().toString();
        fname = edt_fname.getText().toString();
        lname = edt_lname.getText().toString();
        email = edt_email.getText().toString();
        confirm_email = edt_confirm_email.getText().toString();
        mobile = edt_mobile.getText().toString();
        Abnnumber = edt_abn_number.getText().toString();
        telephone = edt_telephone.getText().toString();

        if (businessnane != null && businessnane.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_businessname);
        } else {
            edt_businessname.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_businessname);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.enter_business_name));
                isFirst = false;
            }

        }

        if (fname != null && fname.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_fname);
        } else {
            edt_fname.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_fname);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
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
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.enter_lname));
                isFirst = false;
            }


        }

        if (email != null && email.length() > 0) {
            HelperMethods.Valid(appContext, ll_business_email);
            if (!HelperMethods.validateEmail(email)) {
                edt_email.requestFocus();
                if (isFirst) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.valid_email));
                    isFirst = false;
                }

            }
        } else {
            edt_email.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_business_email);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.please_enter_email));
                isFirst = false;
            }

        }

        if (confirm_email != null && confirm_email.length() > 0) {
            HelperMethods.Valid(appContext, ll_business_c_email);
            if (!HelperMethods.validateEmail(confirm_email)) {
                edt_confirm_email.requestFocus();
                if (isFirst) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.valid_email));

                    isFirst = false;
                }


            }
        } else {
            edt_confirm_email.requestFocus();
            HelperMethods.ValidateFields(appContext, ll_confirm_password);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.enter_confirm_email));
                isFirst = false;
            }

        }

        if (confirm_email.equals(email)) {
        } else {
            edt_confirm_email.requestFocus();
            if (isFirst) {


                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.email_should_be_same));
                isFirst = false;
            }

        }

        if (mobile != null && mobile.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_business_mobile);
            if (mobile.length() < 9) {
                if (isFirst) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getResources().getString(R.string.mobile_length_10));
                    isFirst = false;
                }
                edt_mobile.requestFocus();

            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_business_mobile);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.please_enter_mobile));
                isFirst = false;
            }
            edt_mobile.requestFocus();

        }
        if (Abnnumber != null && Abnnumber.trim().length() > 0) {
            HelperMethods.Valid(appContext, ll_abn_no);
            if ((Abnnumber.length() < 10)) {
                if (isFirst) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.abn_should_be_11_digit));
                    isFirst = false;
                }
                edt_abn_number.requestFocus();
                return false;
            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_abn_no);
            if (isFirst) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.enter_abn_number));
                isFirst = false;
            }
            edt_abn_number.requestFocus();

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
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.error_code_length));
                    isSecond = false;
                }
            } else if (!HelperMethods.ValidUsername(edt_username.getText().toString())) {
                edt_username.requestFocus();
                if (isSecond) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.enter_valid_username));
                    isSecond = false;
                }
            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_username);
            if (isSecond) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getResources().getString(R.string.enter_username));
                isSecond = false;
            }
            edt_username.requestFocus();

        }

        if (!FLAG_SIGNUP.equalsIgnoreCase("2")) {

            patternSpace = Pattern.compile("\\s");

            matcherSpace = patternSpace.matcher(password);

            boolean foundSpace = matcherSpace.find();
            if (password != null && password.length() > 0) {
                HelperMethods.Valid(appContext, ll_password);
                if (foundSpace) {
                    if (isSecond) {
                        ((SignUpGarageOwner) appContext)
                                .showAlertDialog(getString(R.string.error_password_space));
                        isSecond = false;
                    }
                    edt_password.requestFocus();

                } else {
                    patternSpace = Pattern.compile(Constants.PASSWORD_PATTERN);
                    matcherSpace = patternSpace.matcher(password);

                    if ((password.length() < 8 || password.length() > 15)) {
                        if (isSecond) {
                            ((SignUpGarageOwner) appContext)
                                    .showAlertDialog(getString(R.string.error_password_length));
                            isSecond = false;
                        }
                        edt_password.requestFocus();

                    }
                }
            } else {
                HelperMethods.ValidateFields(appContext, ll_password);
                if (isSecond) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.error_password));
                    isSecond = false;
                }
                edt_password.requestFocus();

            }

            if (confirm_password != null && confirm_password.length() > 0) {
                HelperMethods.Valid(appContext, ll_confirm_password);
            } else {
                HelperMethods.ValidateFields(appContext, ll_confirm_password);
                if (isSecond) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.error_confirmpassword));
                    isSecond = false;
                }
                edt_confimepass.requestFocus();

            }

            if (password.equals(confirm_password)) {
                HelperMethods.Valid(appContext, ll_password);
            } else {
                if (isSecond) {
                    ((SignUpGarageOwner) appContext)
                            .showAlertDialog(getString(R.string.error_not_match_pass));
                    isSecond = false;
                }

                edt_confimepass.requestFocus();

            }
        }

        return isSecond;
    }

    public boolean isBusinessDetailsValidate() {
        String state, subrub, postcode, streetnumber;
        state = editstate.getText().toString();
        subrub = edt_subrub.getText().toString();
        postcode = edt_post_code.getText().toString();
        streetnumber = edt_streetnumber.getText().toString();
        boolean isThird = true;

        if (streetnumber != null && streetnumber.length() > 0) {
            HelperMethods.Valid(appContext, ll_streetnumber);
        } else {
            HelperMethods.ValidateFields(appContext, ll_streetnumber);
            if (isThird) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.enter_streetnumber_orname));
                isThird = false;
            }
            edt_streetnumber.requestFocus();

        }

        if (subrub != null && subrub.length() > 0) {
            HelperMethods.Valid(appContext, ll_subrub);

        } else {
            HelperMethods.ValidateFields(appContext, ll_subrub);
            if (isThird) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.error_subrub));
                isThird = false;
            }
            edt_subrub.requestFocus();

        }

        if (state != null && state.length() > 0) {
            HelperMethods.Valid(appContext, ll_state);
        } else {
            HelperMethods.ValidateFields(appContext, ll_state);
            if (isThird) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.error_state));
                isThird = false;
            }
            editstate.requestFocus();

        }

        if (postcode != null && postcode.length() > 0) {
            HelperMethods.Valid(appContext, ll_post_code);
            if (postcode.length() < 4) {
                if (isThird) {
                    ((SignUpGarageOwner) appContext).showAlertDialog(getString(R.string.error_postcode_lenght));
                    isThird = false;
                }
                edt_post_code.requestFocus();

            }
        } else {
            HelperMethods.ValidateFields(appContext, ll_post_code);
            if (isThird) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.error_postcode));
                isThird = false;
            }
            edt_post_code.requestFocus();

        }
        if (!terms.isChecked()) {
            //((SignUpGarageOwner) appContext).showAlertDialog(getString(R.string.error_terms_polity));
            if (isThird) {
                ((SignUpGarageOwner) appContext)
                        .showAlertDialog(getString(R.string.error_terms_polity));
                isThird=false;
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
            }
        });
        builder.show();
    }

    public void SignUpGarageDetails() {
        ((SignUpGarageOwner) appContext).showLoadingDialog(true);
        String strMobile = edt_mobile.getText().toString();
        strMobile = strMobile.substring(1);
        AppLog.Log("strMobile", strMobile);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(SIGN_UP.SERVICE_NAME, "signup_garage");
        params.put(SIGN_UP.SIGNUP_TYPE, FLAG_SIGNUP);
        params.put(SIGN_UP.BNAME, edt_businessname.getText().toString());
        params.put(SIGN_UP.FN, edt_fname.getText().toString());
        params.put(SIGN_UP.LN, edt_lname.getText().toString());
        params.put(SIGN_UP.EMAIL, edt_confirm_email.getText().toString());
        params.put(SIGN_UP.MOBILE, "+61 " + strMobile);
        params.put(SIGN_UP.ABN_NO, "ABN " + edt_abn_number.getText().toString());
        params.put(SIGN_UP.TEL, edt_telephone.getText().toString());
        params.put(SIGN_UP.UN, edt_username.getText().toString());
        params.put(SIGN_UP.STATE, editstate.getText().toString());
        params.put(SIGN_UP.STREET, edt_streetnumber.getText().toString());
        params.put(SIGN_UP.PWD, edt_password.getText().toString());
        params.put(SIGN_UP.SUB, edt_subrub.getText().toString());
        params.put(SIGN_UP.ZIP, edt_post_code.getText().toString());
        params.put(SIGN_UP.IS_NEWSLETTER, isnews_);
        params.put(SIGN_UP.STEP, "1");
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
                        ((SignUpGarageOwner) appContext).showLoadingDialog(false);
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

                                JSONObject json = new JSONObject(response.getString("data"));

                                loginDetail_dbo.setUserid(json.getString(LoginType.ID));
                                loginDetail_dbo.setFirst_name(json.getString(Constants.USER_DETAILS.FNAME));
                                loginDetail_dbo.setLast_name(json.getString(Constants.USER_DETAILS.LNAME));
                                loginDetail_dbo.setImage(json.getString(Constants.LoginType.IMAGE));

                                loginDetail_dbo.setUser_Type("1");
                                loginDetail_dbo.setLogin_type(json.getString("signup_type"));
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

                                MyPreference user_preference = new MyPreference(appContext);
                                user_preference.saveBooleanReponse(Constants.IS_LOGGEDIN, true);
                                user_preference.saveBooleanReponse(Constants.NotificationTags.SHOW_NOTI, true);
                                ((SignUpGarageOwner) appContext).Email = edt_email.getText().toString();
                                ((SignUpGarageOwner) appContext).Password = edt_confimepass.getText().toString();
                                HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);
                                ((SignUpGarageOwner) appContext).tabLayout.setScrollPosition(1, 0f, true);
                                ((SignUpGarageOwner) appContext).viewPager.setCurrentItem(1);
                                ((SignUpGarageOwner) appContext).tv_skipCarSign.setVisibility(View.VISIBLE);
                                ((SignUpGarageOwner) appContext).ll_back.setVisibility(View.GONE);
                                current_position_garage = 5;
                            } else {
                                ((SignUpGarageOwner) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                                if (response.getString(Constants.MESSAGE).equals("Email already exists")) {
                                    setGarageDetailsLayout();
                                } else if (response.getString(Constants.MESSAGE)
                                        .equals("Mobile number already exists")) {
                                    setGarageDetailsLayout();
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
                ((SignUpGarageOwner) appContext).showLoadingDialog(false);
            }
        });

        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
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
        ss.setSpan(clickableSpan, 39, 52, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(clickableSpan2, 56, 71, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        termsandpolicy.setText(ss);
        termsandpolicy.setMovementMethod(LinkMovementMethod.getInstance());
        termsandpolicy.setHighlightColor(Color.WHITE);
    }

    public void spanTermsOfUse(String fulltext, String subString) {
        SpannableString ss = new SpannableString(fulltext);

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(getActivity(), StaticWebpages.class);
                intent.putExtra("flag", "TERMS OF USE");
                intent.putExtra("page_id", "5");
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
        ss.setSpan(clickableSpan, i, i + subString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
