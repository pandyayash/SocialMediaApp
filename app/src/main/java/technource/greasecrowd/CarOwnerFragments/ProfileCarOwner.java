package technource.greasecrowd.CarOwnerFragments;

import static android.app.Activity.RESULT_OK;
import static technource.greasecrowd.activities.SignUpCarOwner.current_position;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.signature.StringSignature;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.LoginScreen;
import technource.greasecrowd.activities.RegisterdCarsScreen;
import technource.greasecrowd.activities.SignUpCarOwner;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.Constants.LoginType;
import technource.greasecrowd.helper.Constants.SIGN_UP;
import technource.greasecrowd.helper.Constants.USER_DETAILS;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 13/9/17.
 */

public class ProfileCarOwner extends Fragment implements OnClickListener {
    private static final String TAG = "Profile Car Owner";
    EditText edt_fname, edt_lname, edt_email, edt_mobile, edt_subrub, edt_post_code;
    TextView editstate;
    LinearLayout ll_save, ll_registerdcars;
    ImageView iv_image;
    Context appContext;
    View v;
    String lastChar = " ";
    int PERMISSION_CODE = 2;
    int CAMERA_CODE = 0;
    int GALLARY_CODE = 1;
    Bitmap bitmap;
    LoginDetail_DBO loginDetail_dbo;
    String Firstname, lastname, email, phone, sub, state, zipcode;
    private File file_1;

    public static ProfileCarOwner newInstance() {
        ProfileCarOwner fragment = new ProfileCarOwner();
        return fragment;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] +
                    grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                selectImage();
            } else {
                Log.i("MainAcivity", "Permission has been denied by user");
                // showAlert();
                // Your app will not have this permission. Turn off all functions
                // that require this permission or it will force close like  your
                // original question
            }
        } else if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                selectImage();
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_carowner_profile, container, false);
        getViews();
        setOnClickListener();
        if (Connectivity.isConnected(appContext)) {
            GetUserProfileData();
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }

        return v;
    }

    public void getViews() {
        appContext = getActivity();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        edt_fname = (EditText) v.findViewById(R.id.edt_fname);
        edt_lname = (EditText) v.findViewById(R.id.edt_lname);
        edt_email = (EditText) v.findViewById(R.id.edt_email);
        edt_mobile = (EditText) v.findViewById(R.id.edt_mobile);
        editstate = (TextView) v.findViewById(R.id.edt_state);
        edt_subrub = (EditText) v.findViewById(R.id.edt_subrub);
        edt_post_code = (EditText) v.findViewById(R.id.edt_post_code);
        iv_image = (ImageView) v.findViewById(R.id.iv_image);
        ll_save = (LinearLayout) v.findViewById(R.id.ll_save);
        ll_registerdcars = (LinearLayout) v.findViewById(R.id.ll_registerdcars);
    }

    public void GetUserProfileData() {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(LoginType.SERVICE_NAME, "get_usr_profile");

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                        AppLog.Log("params", "" + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONArray jarray = new JSONArray(response.getString("data"));
                                setProfileData(jarray.getJSONObject(0));
                            } else {
                                ((DashboardScreen) appContext)
                                        .showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);

    }

    public void setProfileData(JSONObject jobj) {
        try {


            edt_fname.setText(jobj.getString("fname"));
            edt_lname.setText(jobj.getString("lname"));
            edt_email.setText(jobj.getString("email"));
            edt_mobile.setText(jobj.getString("mobile").replace("+61 ", ""));
            //edt_mobile.setText("+61 "+jobj.getString("mobile"));
            edt_post_code.setText(jobj.getString("postcode"));
            editstate.setText(jobj.getString("state"));
            edt_subrub.setText(jobj.getString("suburb"));

            Firstname = jobj.getString("fname");
            lastname = jobj.getString("lname");
            email = jobj.getString("email");
            phone = jobj.getString("mobile");
            sub = jobj.getString("suburb");
            state = jobj.getString("state");
            zipcode = jobj.getString("postcode");

            AppLog.Log("Profile", WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("image"));

//      Glide.with(appContext)
//          .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("image"))
//          .asBitmap()
//          .diskCacheStrategy(DiskCacheStrategy.NONE)
//          .placeholder(R.drawable.no_user)
//          .error(R.drawable.no_user)
//          .skipMemoryCache(true)
//          .into(iv_image);

            Picasso.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("image"))
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(iv_image);

       /*Update the nav profile here*/
            loginDetail_dbo.setFirst_name(jobj.getString("fname"));
            loginDetail_dbo.setLast_name(jobj.getString("lname"));
            loginDetail_dbo.setImage(jobj.getString("image"));
            HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);

            ((DashboardScreen) appContext)
                    .updateProfile(jobj.getString("fname"), jobj.getString("lname"), jobj.getString("image"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edt_state:
                openStateList();
                break;
            case R.id.ll_save:
                if (isValidate()) {
                    if (ProfileUpdateOrNot()) {
                        saveProfile();
                    } else {
                        Toast.makeText(appContext, R.string.there_is_no_chnage, Toast.LENGTH_LONG).show();
                    }
                }
                break;
            case R.id.iv_image:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RuntimeCameraGalleryPermission();
                } else {
                    selectImage();
                }
                break;
            case R.id.ll_registerdcars:
                Intent i = new Intent(appContext, RegisterdCarsScreen.class);
                startActivity(i);
                ((DashboardScreen) appContext).activityTransition();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLARY_CODE) {
            if (resultCode == RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == CAMERA_CODE) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        }

    }

    public boolean ProfileUpdateOrNot() {

        if (edt_fname.getText().toString().equalsIgnoreCase(Firstname) &&
                edt_lname.getText().toString().equalsIgnoreCase(lastname) &&
                edt_email.getText().toString().equalsIgnoreCase(email) &&
                edt_mobile.getText().toString().equalsIgnoreCase(phone) &&
                editstate.getText().toString().equalsIgnoreCase(state) &&
                edt_subrub.getText().toString().equalsIgnoreCase(sub) &&
                edt_post_code.getText().toString().equalsIgnoreCase(zipcode)) {

            return false;
        }

        return true;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getActivity().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Glide.with(appContext)
                .load(stream.toByteArray())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .into(iv_image);
        Uri fileUri = getImageUri(appContext, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));

        saveProfileImage();
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                getResources().getString(R.string.app_name) + "_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void setOnClickListener() {
        editstate.setOnClickListener(this);
        ll_save.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        ll_registerdcars.setOnClickListener(this);
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

    public void RuntimeCameraGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(appContext, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(appContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) appContext,
                        new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            } else {
                selectImage();
            }
        } else {

            selectImage();
        }
    }

    private void onCaptureImageResult(Intent data) {

        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Glide.with(appContext)
                .load(stream.toByteArray())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .into(iv_image);
        Uri fileUri = getImageUri(appContext, thumbnail);
        file_1 = new File(getRealPathFromURI(fileUri));

        saveProfileImage();
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    File f = new File(Environment.getExternalStorageDirectory(), "temp1.jpg");
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {

                        Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        startActivityForResult(intent1, CAMERA_CODE);

                    } catch (ActivityNotFoundException e) {
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("image/*");
                    startActivityForResult(photoPickerIntent, GALLARY_CODE);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }

            }
        });
        builder.show();
    }

    public boolean isValidate() {
        String fname, lname, email, mobile, state, subrub, postcode;
        ;

        fname = edt_fname.getText().toString();
        lname = edt_lname.getText().toString();
        email = edt_email.getText().toString();
        mobile = edt_mobile.getText().toString();
        state = editstate.getText().toString();
        subrub = edt_subrub.getText().toString();
        postcode = edt_post_code.getText().toString();

        if (fname != null && fname.trim().length() > 0) {
        } else {
            edt_fname.requestFocus();
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.enter_fname));
            return false;
        }

        if (lname != null && lname.trim().length() > 0) {
        } else {
            edt_lname.requestFocus();
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.enter_lname));
            return false;
        }

        if (email != null && email.length() > 0) {
            if (!HelperMethods.validateEmail(email)) {
                edt_email.requestFocus();
                ((DashboardScreen) appContext)
                        .showAlertDialog(getString(R.string.valid_email));
                return false;
            }
        } else {
            edt_email.requestFocus();
            ((DashboardScreen) appContext)
                    .showAlertDialog(getString(R.string.please_enter_email));
            return false;
        }

        if (mobile != null && mobile.trim().length() > 0) {
            if (mobile.length() < 9) {
                ((DashboardScreen) appContext)
                        .showAlertDialog(getResources().getString(R.string.mobile_length_10));
                edt_mobile.requestFocus();
                return false;
            }
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.please_enter_mobile));
            edt_mobile.requestFocus();
            return false;
        }

        if (state != null && state.length() > 0) {

        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getString(R.string.error_state));
            editstate.requestFocus();
            return false;
        }

        if (subrub != null && subrub.length() > 0) {

        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getString(R.string.error_subrub));
            edt_subrub.requestFocus();
            return false;
        }

        if (postcode != null && postcode.length() > 0) {
            if (postcode.length() < 4) {
                ((DashboardScreen) appContext).showAlertDialog(getString(R.string.error_postcode_lenght));
                editstate.requestFocus();
                return false;
            }
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getString(R.string.error_postcode));
            editstate.requestFocus();
            return false;
        }

        return true;
    }


    private void saveProfile() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;
            String fname, lname, mobile, email, subrub, state, postcde;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    // Sending side
                    fname = edt_fname.getText().toString().trim();
                    lname = edt_lname.getText().toString().trim();
                    mobile = edt_mobile.getText().toString().trim();
                    email = edt_email.getText().toString().trim();
                    subrub = edt_subrub.getText().toString().trim();
                    state = editstate.getText().toString().trim();
                    postcde = edt_post_code.getText().toString().trim();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                ((DashboardScreen) appContext).showLoadingDialog(true);
            }

            @Override
            protected Void doInBackground(Void... params) {

                ResponseHandler<String> res = new BasicResponseHandler();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                try {

                    AppLog.Log(TAG, "First name " + fname);
                    AppLog.Log(TAG, "Last name " + lname);
                    AppLog.Log(TAG, "email " + email);
                    AppLog.Log(TAG, "mobile " + mobile);
                    AppLog.Log(TAG, "subrub " + subrub);
                    AppLog.Log(TAG, "state " + state);
                    AppLog.Log(TAG, "postcode " + postcde);
                    AppLog.Log(TAG, "postcode " + loginDetail_dbo.getJWTToken());

                    reqEntity.addTextBody(SIGN_UP.SERVICE_NAME, "edit_usr_profile");
                    reqEntity.addTextBody(SIGN_UP.FN, fname);
                    reqEntity.addTextBody(SIGN_UP.LN, lname);
                    reqEntity.addTextBody(SIGN_UP.EMAIL, email);
                    reqEntity.addTextBody(SIGN_UP.MOBILE, "+61 " + mobile);
                    reqEntity.addTextBody(SIGN_UP.SUB, subrub);
                    reqEntity.addTextBody(SIGN_UP.STATE, state);
                    reqEntity.addTextBody(SIGN_UP.ZIP, postcde);
                    reqEntity.addTextBody(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
                    AppLog.Log(TAG, "jwt " + loginDetail_dbo.getJWTToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HttpEntity httpEntity = reqEntity.build();
                httpPost.setEntity(httpEntity);

                try {
                    result = httpClient.execute(httpPost, res);
                    if (result != null) {
                        AppLog.Log(TAG, "Response in Update Profile : " + result.toString());
                        response = new JSONObject(result);

                        AppLog.Log(TAG, "Response in Update Profile : " + result.toString());
                        try {
                            status = response.getString(Constants.STATUS);
                            AppLog.Log(TAG, "Status: " + status);

                        } catch (JSONException e) {
                            AppLog.Log(TAG, "Erro in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppLog.Log(TAG, "Error in Update Profile" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ((DashboardScreen) appContext).showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                        Toast.makeText(appContext, R.string.profile_updated, Toast.LENGTH_SHORT).show();

                        GetUserProfileData();
                    } else if (status != null && status.equalsIgnoreCase(Constants.FAILURE)) {
                        Toast.makeText(appContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    AppLog.Log(TAG, "Erro in onResponse : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    private void saveProfileImage() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;
            String fname, lname, mobile, email, subrub, state, postcde;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                ((DashboardScreen) appContext).showLoadingDialog(true);
            }

            @Override
            protected Void doInBackground(Void... params) {

                ResponseHandler<String> res = new BasicResponseHandler();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                try {

                    if (file_1 != null && file_1.length() > 1) {
                        try {
                            AppLog.Log(TAG, "Image path " + file_1);
                            FileBody t_fileBody = new FileBody(file_1);
                            reqEntity.addPart(Constants.USER_DETAILS.PROFILE_IMAGE, t_fileBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            reqEntity.addTextBody(Constants.USER_DETAILS.PROFILE_IMAGE, "");
                        }

                    } else {
                        reqEntity.addTextBody(Constants.USER_DETAILS.PROFILE_IMAGE, "");
                    }

                    reqEntity.addTextBody(SIGN_UP.SERVICE_NAME, "edit_profile_img");
                    reqEntity.addTextBody(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
                    reqEntity.addTextBody(LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
                    AppLog.Log(TAG, "jwt " + loginDetail_dbo.getJWTToken());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HttpEntity httpEntity = reqEntity.build();
                httpPost.setEntity(httpEntity);

                try {
                    result = httpClient.execute(httpPost, res);
                    if (result != null) {
                        AppLog.Log(TAG, "Response in Update Profile : " + result.toString());
                        response = new JSONObject(result);

                        AppLog.Log(TAG, "Response in Update Profile : " + result.toString());
                        try {
                            status = response.getString(Constants.STATUS);
                            AppLog.Log(TAG, "Status: " + status);

                        } catch (JSONException e) {
                            AppLog.Log(TAG, "Erro in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppLog.Log(TAG, "Error in Update Profile" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                ((DashboardScreen) appContext).showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                        Toast.makeText(appContext, R.string.profile_updated, Toast.LENGTH_SHORT).show();
                        GetUserProfileData();
                    } else if (status != null && status.equalsIgnoreCase(Constants.FAILURE)) {
                        Toast.makeText(appContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    AppLog.Log(TAG, "Erro in onResponse : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();

    }

}
