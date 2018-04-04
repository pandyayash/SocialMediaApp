package technource.autoreum.GarageOwnerFragments;

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
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.activities.GarageOwnerFacilitiesActivity;
import technource.autoreum.activities.GarageOwnerGalleryActivity;
import technource.autoreum.activities.GarageOwnerHoursActivity;
import technource.autoreum.activities.GarageOwnerProfileMapActivity;
import technource.autoreum.activities.GarageOwnerReviewsActivity;
import technource.autoreum.activities.GarageOwnerServicesActivity;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;

import static android.app.Activity.RESULT_OK;
import static com.bumptech.glide.gifdecoder.GifHeaderParser.TAG;

/**
 * Created by technource on 13/9/17.
 */

public class ProfileGarageOwner extends Fragment implements View.OnClickListener {

    public TextView gallery_big, editstate, hours_big, services_big, facilities_big, reviews_big, gallery, hours, services, facilities, reviews;
    EditText edt_fname, edt_lname, edt_email, edt_mobile, edt_subrub, edt_post_code, edt_bussinessname, edt_telephone;
    LinearLayout ll_save;
    RelativeLayout footer_small;
    RelativeLayout footer_big;
    ImageView iv_image, location;
    Context appContext;
    View v;
    String lastChar = " ";
    int screen_height;
    int screen_width;
    int PERMISSION_CODE = 2;
    int CAMERA_CODE = 0;
    int GALLARY_CODE = 1;
    Bitmap bitmap;
    LoginDetail_DBO loginDetail_dbo;
    double lat, lng;
    String Firstname, lastname, email, phone, sub, state, zipcode, bussinessname, telephone, image;
    private File file_1;


    public static ProfileGarageOwner newInstance() {
        ProfileGarageOwner fragment = new ProfileGarageOwner();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_profile, container, false);
        getViews();
        checkscreensize();
        setlayoutDesign();
        setOnClickListener();

        ((DashboardScreen)appContext).NoFooter();

        if (Connectivity.isConnected(appContext)) {
            GetUserProfileData();
        } else {
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.no_internet));
        }


        return v;
    }

    private void setlayoutDesign() {
        if (screen_width >= 720) {

            footer_big.setVisibility(View.VISIBLE);
            footer_small.setVisibility(View.GONE);

        } else {
            footer_big.setVisibility(View.GONE);
            footer_small.setVisibility(View.VISIBLE);
        }

    }

    private void checkscreensize() {
        //1080 * 1920 (height width for 5'
        WindowManager wm = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        screen_width = metrics.widthPixels;
        screen_height = metrics.heightPixels;

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
        edt_bussinessname = (EditText) v.findViewById(R.id.edt_bussinessname);
        edt_telephone = (EditText) v.findViewById(R.id.edt_telephone);
        setPhoneFormatting();
        iv_image = (ImageView) v.findViewById(R.id.iv_image);
        location = (ImageView) v.findViewById(R.id.location);
        ll_save = (LinearLayout) v.findViewById(R.id.ll_save);
        footer_small = (RelativeLayout) v.findViewById(R.id.footer_small);
        footer_big = (RelativeLayout) v.findViewById(R.id.footer_big);

        gallery_big = (TextView) v.findViewById(R.id.gallery_big);
        hours_big = (TextView) v.findViewById(R.id.hours_big);
        services_big = (TextView) v.findViewById(R.id.services_big);
        facilities_big = (TextView) v.findViewById(R.id.facilitie_big);
        reviews_big = (TextView) v.findViewById(R.id.reviews_big);

        gallery = (TextView) v.findViewById(R.id.gallery);
        hours = (TextView) v.findViewById(R.id.hours);
        services = (TextView) v.findViewById(R.id.services);
        facilities = (TextView) v.findViewById(R.id.facilities);
        reviews = (TextView) v.findViewById(R.id.reviews);


    }


    public void setOnClickListener() {
        editstate.setOnClickListener(this);
        ll_save.setOnClickListener(this);
        iv_image.setOnClickListener(this);
        location.setOnClickListener(this);

        gallery_big.setOnClickListener(this);
        hours_big.setOnClickListener(this);
        services_big.setOnClickListener(this);
        facilities_big.setOnClickListener(this);
        reviews_big.setOnClickListener(this);

        gallery.setOnClickListener(this);
        hours.setOnClickListener(this);
        services.setOnClickListener(this);
        facilities.setOnClickListener(this);
        reviews.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
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

            case R.id.location:
                intent = new Intent(getActivity(), GarageOwnerProfileMapActivity.class);
                Bundle b = new Bundle();
                b.putDouble("lat", lat);
                b.putDouble("lng", lng);
                b.putString("subrub", edt_subrub.getText().toString().trim());
                b.putString("state", editstate.getText().toString().trim());
                intent.putExtras(b);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;

            case R.id.gallery_big:
            case R.id.gallery:
                intent = new Intent(getActivity(), GarageOwnerGalleryActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.hours_big:
            case R.id.hours:
                //Add activity here
                intent = new Intent(getActivity(), GarageOwnerHoursActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();

                break;
            case R.id.services_big:
            case R.id.services:
                //Add activity here
                intent = new Intent(getActivity(), GarageOwnerServicesActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.facilitie_big:
            case R.id.facilities:
                //Add activity here
                intent = new Intent(getActivity(), GarageOwnerFacilitiesActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.reviews_big:
            case R.id.reviews:
                //Add activity here
                intent = new Intent(getActivity(), GarageOwnerReviewsActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
        }
    }

    public boolean ProfileUpdateOrNot() {

        if (edt_fname.getText().toString().equalsIgnoreCase(Firstname) &&
                edt_lname.getText().toString().equalsIgnoreCase(lastname) &&
                edt_email.getText().toString().equalsIgnoreCase(email) &&
                edt_mobile.getText().toString().equalsIgnoreCase(phone) &&
                editstate.getText().toString().equalsIgnoreCase(state) &&
                edt_subrub.getText().toString().equalsIgnoreCase(sub) &&
                edt_post_code.getText().toString().equalsIgnoreCase(zipcode) &&
                edt_bussinessname.getText().toString().equalsIgnoreCase(bussinessname) &&
                edt_telephone.getText().toString().equalsIgnoreCase(telephone)) {

            return false;
        }

        return true;
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
        /*Glide.with(appContext)
                .load(stream.toByteArray())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .into(iv_image);*/

        Uri fileUri = getImageUri(appContext, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));

        saveProfileImage();
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
        /*Glide.with(appContext)
                .load(stream.toByteArray())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)

                .into(iv_image);*/
        Uri fileUri = getImageUri(appContext, thumbnail);
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

    private void saveProfileImage() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;

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

                    reqEntity.addTextBody(Constants.SIGN_UP.SERVICE_NAME, "edit_profile_img");
                    reqEntity.addTextBody(Constants.SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
                    reqEntity.addTextBody(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
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
                        GetUserProfileData();
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

    private void saveProfile() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;
            String fname, lname, mobile, email, subrub, state, postcde, bussinessname, telephone;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    // Sending side
                    String strMobile = edt_mobile.getText().toString().trim();
                    strMobile = strMobile.substring(1);
                    AppLog.Log("strMobile",strMobile);
                    fname = edt_fname.getText().toString().trim();
                    lname = edt_lname.getText().toString().trim();
                    mobile = strMobile;
                    email = edt_email.getText().toString().trim();
                    subrub = edt_subrub.getText().toString().trim();
                    state = editstate.getText().toString().trim();
                    postcde = edt_post_code.getText().toString().trim();
                    bussinessname = edt_bussinessname.getText().toString().trim();
                    telephone = edt_telephone.getText().toString().trim();
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
                    reqEntity.addTextBody(Constants.SIGN_UP.SERVICE_NAME, "edit_garage_info");
                    reqEntity.addTextBody(Constants.SIGN_UP.BN, bussinessname);
                    reqEntity.addTextBody(Constants.SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
                    reqEntity.addTextBody(Constants.SIGN_UP.TEL, telephone);
                    reqEntity.addTextBody(Constants.SIGN_UP.FN, fname);
                    reqEntity.addTextBody(Constants.SIGN_UP.LN, lname);
                    reqEntity.addTextBody(Constants.SIGN_UP.EMAIL, email);
                    reqEntity.addTextBody(Constants.SIGN_UP.MOBILE, "+61 " + mobile);
                    reqEntity.addTextBody(Constants.SIGN_UP.SUB, subrub);
                    reqEntity.addTextBody(Constants.SIGN_UP.STATE, state);
                    reqEntity.addTextBody(Constants.SIGN_UP.ZIP, postcde);
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

    public boolean isValidate() {
        String fname, lname, email, mobile, state, subrub, postcode, bussinessname, telephone;

        fname = edt_fname.getText().toString();
        lname = edt_lname.getText().toString();
        email = edt_email.getText().toString();
        mobile = edt_mobile.getText().toString();
        state = editstate.getText().toString();
        subrub = edt_subrub.getText().toString();
        postcode = edt_post_code.getText().toString();
        bussinessname = edt_bussinessname.getText().toString();

        if (fname != null && fname.trim().length() > 0) {
        } else {
            edt_fname.requestFocus();
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.enter_fname));
            return false;
        }
        if (bussinessname != null && bussinessname.trim().length() > 0) {
        } else {
            edt_bussinessname.requestFocus();
            ((DashboardScreen) appContext)
                    .showAlertDialog(getResources().getString(R.string.enter_business_name));
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

    public void GetUserProfileData() {
        ((DashboardScreen) appContext).showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.LoginType.SERVICE_NAME, "get_garage_info");
        params.put(Constants.Paypal.ACTION, "prof_data");

        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        ((DashboardScreen) appContext).showLoadingDialog(false);
                        AppLog.Log("Response", "In GetUser --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONObject jsonObject = new JSONObject(response.getString("prf_data"));
                                String strLat = jsonObject.getString("lat");
                                String strLong = jsonObject.getString("lng");
                                if (!strLat.equalsIgnoreCase(" ") && !strLong.equalsIgnoreCase("")) {
                                    lat = Double.parseDouble(strLat);
                                    lng = Double.parseDouble(strLong);
                                }

                                setProfileData(jsonObject);
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

            String mobile = jobj.getString("mobile").replace("+61 ", "");
            edt_bussinessname.setText(jobj.getString("business_name"));
            edt_fname.setText(jobj.getString("fname"));
            edt_lname.setText(jobj.getString("lname"));
            edt_email.setText(jobj.getString("business_email"));
            edt_mobile.setText(mobile);
            edt_post_code.setText(jobj.getString("postcode"));
            editstate.setText(jobj.getString("state"));
            edt_subrub.setText(jobj.getString("suburb"));
            edt_telephone.setText(jobj.getString("telephone"));

            bussinessname = jobj.getString("business_name");
            Firstname = jobj.getString("fname");
            lastname = jobj.getString("lname");
            email = jobj.getString("business_email");
            phone = jobj.getString("mobile");
            sub = jobj.getString("suburb");
            state = jobj.getString("state");
            zipcode = jobj.getString("postcode");
            telephone = jobj.getString("telephone");
            image = jobj.getString("logo_image");

            AppLog.Log("Image String in response-->", "" + WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("logo_image"));

            /*Glide.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("logo_image"))
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)
                    .dontAnimate()
                    .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                    .into(iv_image);*/

            /*Glide.with(appContext).load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("logo_image"))
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .dontAnimate()
                    .into(iv_image);*/

            Picasso.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + jobj.getString("logo_image"))
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(iv_image);

            loginDetail_dbo.setFirst_name(Firstname);
            loginDetail_dbo.setLast_name(lastname);
            loginDetail_dbo.setImage(image);
            HelperMethods.storeUserDetailsSharedPreferences(appContext, loginDetail_dbo);
            ((DashboardScreen) appContext).updateProfile(jobj.getString("fname"), jobj.getString("lname"), jobj.getString("logo_image"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPhoneFormatting() {

        edt_telephone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int digits = edt_telephone.getText().toString().length();
                if (digits > 1) {
                    lastChar = edt_telephone.getText().toString().substring(digits - 1);
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int digits = edt_telephone.getText().toString().length();
                Log.d("LENGTH", "" + digits);
                if (!lastChar.equals(" ")) {
                    if (digits == 4 || digits == 8) {
                        edt_telephone.append(" ");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
