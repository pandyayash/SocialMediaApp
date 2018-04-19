package technource.autoreum.activities;

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
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.autoreum.R;
import technource.autoreum.activities.AskTheCrowd.AskTheCrowdStepOne;
import technource.autoreum.activities.PostJob.PostNewJobStepOne;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.RegisteredCarDBO;

/**
 * Created by technource on 20/9/17.
 */

public class RegisterdCarsScreen extends BaseActivity {
    LoginDetail_DBO loginDetail_dbo;
    private RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<RegisteredCarDBO> registeredCarDBOs;
    RegisteredCarDBO registeredCarDBO;
    RecycleradapterforRegisteredcar recycleradapterforRegisteredcar;
    Context appContext;
    TextView registernewcar;
    public static String Car_id;
    public static int position_ = 0;


    int PERMISSION_CODE = 2;
    int CAMERA_CODE = 0;
    int GALLARY_CODE = 1;
    Bitmap bitmap;
    RecycleradapterforRegisteredcar.Recycleviewholder holder;
    JSONObject jsonObject;
    private File file_1;
    ImageView imageView;
    SwipeRefreshLayout pull_to_refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_cars);

        setHeader("Registered Cars");
        getviews();
        setfooter("jobs");
        setlistenrforfooter();
        setHomeFooter(appContext);
        if (Connectivity.isConnected(appContext)) {
            GetRegisteredCars();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }

        pull_to_refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Connectivity.isConnected(appContext)) {
                    GetRegisteredCars();
                } else {
                    showAlertDialog(getResources().getString(R.string.no_internet));
                }
                pull_to_refresh.setRefreshing(false);
            }
        });

        setOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getviews() {
        ll_back = findViewById(R.id.ll_back);
        appContext = this;

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        pull_to_refresh = findViewById(R.id.pull_to_refresh);
        registernewcar = findViewById(R.id.registernewcar);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
    }

    public void setOnClickListener() {
        ll_back.setOnClickListener(this);
        registernewcar.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.registernewcar:
                Intent intent = new Intent(RegisterdCarsScreen.this, RegisterNewCarActivity.class);
                startActivityForResult(intent, 333);
                activityTransition();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    public void GetRegisteredCars() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SIGN_UP.SERVICE_NAME, "get_reg_cars");
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        showLoadingDialog(false);
                        AppLog.Log("Response", response.toString());
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                registeredCarDBOs = new ArrayList<>();
                                JSONArray json = response.getJSONArray("data");

                                for (int i = 0; i < json.length(); i++) {
                                    RegisteredCarDBO services = new RegisteredCarDBO();
                                    JSONObject obj = json.getJSONObject(i);


                                    services.setId(obj.getString("id"));
                                    services.setRegistration_number(obj.getString("registration_number"));
                                    services.setCarmake(obj.getString("carmake"));
                                    services.setCarmodel(obj.getString("carmodel"));
                                    if (obj.getString("carbadge").equalsIgnoreCase("")) {
                                        services.setCarbadge("No Badge/Variant");
                                    } else {
                                        services.setCarbadge(obj.getString("carbadge"));
                                    }
                                    services.setCar_type(obj.getString("car_type"));
                                    services.setCar_img(obj.getString("car_img"));
                                    services.setYear(obj.getString("year"));
                                    registeredCarDBOs.add(services);
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
                showLoadingDialog(false);
            }
        });
        queue.add(jsonObjReq);
    }

    private void setAdapter() {

        adapter = new RecycleradapterforRegisteredcar();
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        AppLog.Log("Tag", "request code-->" + requestCode + " resultcode-->" + resultCode + " Intent data-->" + data);
        if (requestCode == GALLARY_CODE) {
            if (resultCode == RESULT_OK) {
                onSelectFromGalleryResult(data);
            }
        } else if (requestCode == CAMERA_CODE) {
            if (resultCode == RESULT_OK) {
                onCaptureImageResult(data);
            }
        } else if (requestCode == 222 && resultCode == RESULT_OK) {
            registeredCarDBOs.set(Integer.parseInt(data.getExtras().getString("position")), (RegisteredCarDBO) data.getExtras().get("data"));
            adapter.notifyDataSetChanged();
            AppLog.Log("yuo are in startactivty", "enjoy" + data.getExtras().getString("position") + "  " + ((RegisteredCarDBO) data.getExtras().get("data")).getCarmake());
        } else if (requestCode == 333 && resultCode == RESULT_OK) {
            GetRegisteredCars();
        }
    }

    private void RuntimeCameraGalleryPermission() {

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
        builder.setCancelable(false);
    }

    private void saveProfileImage(final Intent data) {
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
                            AppLog.Log("TAG", "Image path " + file_1);
                            FileBody t_fileBody = new FileBody(file_1);
                            reqEntity.addPart("car_img", t_fileBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            reqEntity.addTextBody("car_img", "");
                        }
                    } else {
                        reqEntity.addTextBody("car_img", "");
                    }

                    reqEntity.addTextBody(Constants.SIGN_UP.SERVICE_NAME, "edit_car_img");
                    reqEntity.addTextBody(Constants.SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());
                    reqEntity.addTextBody("car_id", "" + Car_id);

                    AppLog.Log("car_id", "-->" + Car_id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                HttpEntity httpEntity = reqEntity.build();
                httpPost.setEntity(httpEntity);

                try {
                    result = httpClient.execute(httpPost, res);
                    if (result != null) {
                        AppLog.Log("Tag", "Response in Update Profile : " + result.toString());
                        response = new JSONObject(result);
                        AppLog.Log("Tag", "Response in Update Profile : " + result.toString());
                        try {
                            status = response.getString(Constants.STATUS);
                            AppLog.Log("Tag", "Status: " + status);
                            jsonObject = response.getJSONObject("data");

                        } catch (JSONException e) {
                            AppLog.Log("Tag", "Erro in onResponse : " + e.getMessage());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    AppLog.Log("Tag", "Error in Update Profile" + e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showLoadingDialog(false);
                try {
                    if (status != null && status.equalsIgnoreCase(Constants.SUCCESS)) {
                        Toast.makeText(appContext, R.string.profile_updated, Toast.LENGTH_SHORT).show();

                        RegisteredCarDBO model = new RegisteredCarDBO();
                        model.setCar_img(jsonObject.getString("image"));
                        model.setId(registeredCarDBOs.get(position_).getId());
                        model.setRegistration_number(registeredCarDBOs.get(position_).getRegistration_number());
                        model.setCarmake(registeredCarDBOs.get(position_).getCarmake());
                        model.setCarmodel(registeredCarDBOs.get(position_).getCarmodel());
                        registeredCarDBOs.set(position_, model);
                        AppLog.Log("youar car", "image-->" + WebServiceURLs.BASE_URL_IMAGE_PROFILE + registeredCarDBOs.get(position_).getCar_img());
                        Glide.with(appContext)
                                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + registeredCarDBOs.get(position_).getCar_img())
                                .asBitmap()
                                .dontAnimate()
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .placeholder(R.drawable.car_demo)
                                .error(R.drawable.car_demo)
                                .skipMemoryCache(true)
                                .into(imageView);
                        adapter.notifyDataSetChanged();

                    } else if (status != null && status.equalsIgnoreCase(Constants.FAILURE)) {
                        Toast.makeText(appContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    AppLog.Log("tag", "Erro in onResponse : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();

    }

    public void onSelectFromGalleryResult(Intent data) {
        bitmap = null;
        if (data != null) {
            try {
                bitmap = MediaStore.Images.Media
                        .getBitmap(getApplicationContext().getContentResolver(), data.getData());

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                Uri fileUri = getImageUri(appContext, bitmap);
                file_1 = new File(getRealPathFromURI(fileUri));

                saveProfileImage(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void onCaptureImageResult(Intent data) {

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

        saveProfileImage(data);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,
                getResources().getString(R.string.app_name) + "_" + System.currentTimeMillis(),
                null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public class RecycleradapterforRegisteredcar extends
            RecyclerView.Adapter<RecycleradapterforRegisteredcar.Recycleviewholder> {

        @Override
        public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_registeredcar, parent, false);
            Recycleviewholder recycleviewholder = new Recycleviewholder(view);

            return recycleviewholder;
        }


        @Override
        public void onBindViewHolder(final Recycleviewholder holder, final int position) {


            holder.carname.setText(registeredCarDBOs.get(position).getCarmake() + " " + registeredCarDBOs.get(position).getCarmodel());
            holder.registration_number.setText(registeredCarDBOs.get(position).getRegistration_number());

            imageView = holder.iv_image;

            AppLog.Log("Image ", "path-->" + registeredCarDBOs.get(position).getCar_img());
            Glide.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + registeredCarDBOs.get(position).getCar_img())
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.car_demo)
                    .error(R.drawable.car_demo)
                    .skipMemoryCache(true)
                    .into(holder.iv_image);
            holder.viewhistory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(appContext.getApplicationContext(),
                            ViewHistoryGarageActivity.class);
                    intent.putExtra("registeredcar", registeredCarDBOs.get(position));
                    startActivity(intent);
                    activityTransition();
                }
            });

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(appContext.getApplicationContext(),
                            EditRegisteredCarActivity.class);
                    intent.putExtra("registeredcar", registeredCarDBOs.get(position));
                    intent.putExtra("position", String.valueOf(position));
                    startActivityForResult(intent, 222);
                    activityTransition();
                }
            });
            holder.postjob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), PostNewJobStepOne.class);
                    intent.putExtra("registeredcar", registeredCarDBOs.get(position));
                    startActivity(intent);
                    activityTransition();
                }
            });
            holder.askthecrowd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(getApplicationContext(), AskTheCrowdStepOne.class);
                    intent.putExtra("registeredcar", registeredCarDBOs.get(position));
                    startActivity(intent);
                    activityTransition();
                }
            });
            holder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position_ = position;
                    imageView = holder.iv_image;
                    registeredCarDBO = registeredCarDBOs.get(position);
                    Car_id = registeredCarDBOs.get(position).getId();
                    AppLog.Log("size of object", "size-->" + registeredCarDBO.getId());
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RuntimeCameraGalleryPermission();
                    } else {
                        selectImage();
                    }
                }
            });
        }


        @Override
        public int getItemCount() {
            return registeredCarDBOs.size();
        }


        public class Recycleviewholder extends RecyclerView.ViewHolder {

            TextView carname, registration_number, viewhistory, edit, postjob, askthecrowd;
            ImageView iv_image;
            private View view;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                carname = itemView.findViewById(R.id.carname);
                registration_number = itemView.findViewById(R.id.registration_number);
                viewhistory = itemView.findViewById(R.id.viewhistory);
                edit = itemView.findViewById(R.id.edit);
                postjob = itemView.findViewById(R.id.postjob);
                askthecrowd = itemView.findViewById(R.id.askthecrowd);
                iv_image = itemView.findViewById(R.id.iv_image);
            }

        }
    }
}
