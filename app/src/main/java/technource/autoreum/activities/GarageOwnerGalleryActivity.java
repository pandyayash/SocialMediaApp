package technource.autoreum.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog.Builder;
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
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
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

import technource.autoreum.CustomViews.Grid_image;
import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.Constants.GALLARY;
import technource.autoreum.helper.Constants.SIGN_UP;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.GallaryDBO;
import technource.autoreum.model.LoginDetail_DBO;

public class GarageOwnerGalleryActivity extends BaseActivity implements OnClickListener {

    private static final String TAG = "GarageOwnerActivity";
    public int which = 0;
    public int SelectedImagePos = 0;
    public boolean isForGallary = false;
    String key = "";
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;
    GridView gridView;
    Favorite_Adapter adapter;
    ArrayList<GallaryDBO> Gallary_lst = new ArrayList<>();
    TextView nodata;
    ScrollView scrollView;
    LinearLayout ll_back, ll_upload;
    int PERMISSION_CODE = 2;
    int CAMERA_CODE = 0;
    int GALLARY_CODE = 1;
    Bitmap bitmap;
    ImageView motorImage, editBtn;
    String Motor_Mem_Image = "";
    private File file_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_gallery);
        getViewS();
        setAdapter();
        setOnClivkLitener();

        if (Connectivity.isConnected(appContext)) {
            GetGallaryImages();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }
    }

    public void getViewS() {
        appContext = this;
        setHeader(getString(R.string.gallaery));
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        ll_back = findViewById(R.id.ll_back);
        ll_upload = findViewById(R.id.ll_upload);
        gridView = findViewById(R.id.gridview);
        nodata = findViewById(R.id.nodata);
        scrollView = findViewById(R.id.scrollview);
        motorImage = findViewById(R.id.motorImage);
        editBtn = findViewById(R.id.editBtn);

        setfooter("garageowner");
        setHomeFooterGarage(appContext);
        setlistenrforfooter();
    }

    public void setOnClivkLitener() {
        ll_back.setOnClickListener(this);
        ll_upload.setOnClickListener(this);
        editBtn.setOnClickListener(this);
    }

    public void setAdapter() {

        adapter = new Favorite_Adapter(appContext, Gallary_lst);
        gridView.setAdapter(adapter);

        if (Gallary_lst.size() == 0) {
            gridView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_upload:
                isForGallary = false;
                which = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RuntimeCameraGalleryPermission();
                } else {
                    selectImage();
                }
                break;
            case R.id.editBtn:
                isForGallary = true;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RuntimeCameraGalleryPermission();
                } else {
                    selectImage();
                }
                break;
        }
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
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] +
                    grantResults[1]) == PackageManager.PERMISSION_GRANTED) {
                // Now user should be able to use camera
                selectImage();
            } else {
               // Log.i("MainAcivity", "Permission has been denied by user");
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

                       /* mImageCaptureUri = Uri.fromFile(f);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
                        //intent.putExtra("return-data", true);
                        startActivityForResult(intent, CAMERA_CODE);*/
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
        builder.setCancelable(false);
        builder.show();
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
        Uri fileUri = getImageUri(appContext, thumbnail);
        file_1 = new File(getRealPathFromURI(fileUri));

        if (isForGallary == false) {
            if (Connectivity.isConnected(appContext)) {
                saveInGallary();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        } else {
            if (Connectivity.isConnected(appContext)) {
                saveMotorIndystryImage();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Uri fileUri = getImageUri(appContext, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));

        if (isForGallary == false) {
            if (Connectivity.isConnected(appContext)) {
                saveInGallary();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        } else {
            if (Connectivity.isConnected(appContext)) {
                saveMotorIndystryImage();
            } else {
                showAlertDialog(getResources().getString(R.string.no_internet));
            }
        }

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                getResources().getString(R.string.app_name) + "_" + System.currentTimeMillis(), null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public void GetGallaryImages() {
        Gallary_lst.clear();
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(GALLARY.SERVICE_NAME, "gallery_images");
        params.put(GALLARY.ACTION, "get");
        params.put(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("params", "" + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            Motor_Mem_Image = response.getString(Constants.MOT_MEM_IMG);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                JSONArray jarray = new JSONArray(response.getString("data"));
                                for (int i = 0; i < jarray.length(); i++) {
                                    JSONObject jobj = jarray.getJSONObject(i);
                                    GallaryDBO model = new GallaryDBO();
                                    model.setKey(jobj.getString("key"));
                                    model.setImage(jobj.getString("img"));
                                    Gallary_lst.add(model);
                                }
                                setAdapter();
                                SetIndustryMemberShipImage(Motor_Mem_Image);
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);

    }

    public void DeleteImage() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showLoadingDialog(true);
            }

            @Override
            protected Void doInBackground(Void... params) {

                ResponseHandler<String> res = new BasicResponseHandler();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);

                try {
                    reqEntity.addTextBody(GALLARY.SERVICE_NAME, "gallery_images");
                    reqEntity.addTextBody(GALLARY.ACTION, "del");
                    reqEntity.addTextBody(GALLARY.IMG_KEY, key);
                    reqEntity.addTextBody(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
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
                showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                        Gallary_lst.remove(SelectedImagePos);
                        adapter.notifyDataSetChanged();
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

    private void saveInGallary() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showLoadingDialog(true);
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
                            FileBody t_fileBody = new FileBody(file_1);
                            reqEntity.addPart(GALLARY.GALLERY_IMG, t_fileBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            reqEntity.addTextBody(GALLARY.GALLERY_IMG, "");
                        }
                    } else {
                        reqEntity.addTextBody(GALLARY.GALLERY_IMG, "");
                    }

                    reqEntity.addTextBody(GALLARY.SERVICE_NAME, "gallery_images");
                    if (which == 0) {
                        reqEntity.addTextBody(GALLARY.ACTION, "add");
                    } else {
                        reqEntity.addTextBody(GALLARY.ACTION, "edit");
                        reqEntity.addTextBody(GALLARY.IMG_KEY, key);
                    }
                    reqEntity.addTextBody(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
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
                showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {

                        JSONObject data = new JSONObject(response.getString("data"));
                        String image = data.getString("image");

                        GallaryDBO model = new GallaryDBO();
                        model.setImage(image);
                        model.setKey(data.getString("key"));

                        AppLog.Log(TAG, "Whhich... ....." + SelectedImagePos);

                        if (which == 0) {
                            Gallary_lst.add(model);
                            adapter.notifyDataSetChanged();
                            scrollView.fullScroll(View.FOCUS_DOWN);
                        } else {

                            Gallary_lst.set(SelectedImagePos, model);
                            adapter.notifyDataSetChanged();
                        }
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

    private void saveMotorIndystryImage() {
        new AsyncTask<Void, Void, Void>() {
            String status;
            String result = "";
            JSONObject response;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                showLoadingDialog(true);
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
                            FileBody t_fileBody = new FileBody(file_1);
                            reqEntity.addPart(GALLARY.MEMBERSHIP_IMG, t_fileBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            reqEntity.addTextBody(GALLARY.MEMBERSHIP_IMG, "");
                        }
                    } else {
                        reqEntity.addTextBody(GALLARY.GALLERY_IMG, "");
                    }

                    reqEntity.addTextBody(GALLARY.SERVICE_NAME, "edit_motor_membership");
                    reqEntity.addTextBody(SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
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
                showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {

                        String image = response.getString("image");
                        SetIndustryMemberShipImage(image);

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

    public void SetIndustryMemberShipImage(String url) {
        Glide.with(appContext)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.default_car)
                .error(R.drawable.default_car)
                .skipMemoryCache(true)
                .into(motorImage);
    }

    public class Favorite_Adapter extends BaseAdapter {

        Context context;
        ViewHolder holder;
        ArrayList<GallaryDBO> array_gallay = new ArrayList<GallaryDBO>();

        public Favorite_Adapter(Context context, ArrayList<GallaryDBO> array_gallay) {
            super();
            this.context = context;
            this.array_gallay = array_gallay;
        }

        @Override
        public int getCount() {
            return array_gallay.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new ViewHolder();

            final GallaryDBO model = array_gallay.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_row_gallary, null);
                holder.iv_image = convertView.findViewById(R.id.gridimage);
                holder.Edit = convertView.findViewById(R.id.editBtn);
                holder.Delete = convertView.findViewById(R.id.deleteBtn);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Glide.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.default_car)
                    .error(R.drawable.default_car)
                    .skipMemoryCache(false)
                    .into(holder.iv_image);

            holder.Edit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    isForGallary = false;
                    which = 1;
                    AppLog.Log("Key", model.getKey());
                    SelectedImagePos = position;
                    key = model.getKey();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RuntimeCameraGalleryPermission();
                    } else {
                        selectImage();
                    }
                }
            });

            holder.Delete.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectedImagePos = position;
                    key = model.getKey();

                    new Builder(appContext)
                            .setMessage("Are you sure you want to delete this image?")
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    DeleteImage();
                                }
                            })
                            .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });

            return convertView;
        }

        class ViewHolder {

            TextView tv_name;
            Grid_image iv_image;
            ImageView Edit, Delete;
        }

    }
}
