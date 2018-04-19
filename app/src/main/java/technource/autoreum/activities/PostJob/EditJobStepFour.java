package technource.autoreum.activities.PostJob;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import technource.autoreum.CustomViews.Grid_image;
import technource.autoreum.R;
import technource.autoreum.activities.BaseActivity;
import technource.autoreum.activities.DashboardScreen;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.CarImageDBO;
import technource.autoreum.model.CarVideosDbo;
import technource.autoreum.model.GallaryDBO;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.LoginDetail_DBO;

/**
 * Created by technource on 22/12/17.
 */

public class EditJobStepFour extends BaseActivity {
    private static final String TAG = "GarageOwnerActivity";
    public int which = 0;
    public int SelectedImagePos = 0;
    String key = "";
    Context appContext;
    GridView gridView, gridview_video;
    Favorite_Adapter adapter;
    Favorite_AdapterForVideo adapterVideo;
    ArrayList<GallaryDBO> Gallary_lst;
    ArrayList<GallaryDBO> Gallary_video;
    TextView nodata, nodata_video;
    ScrollView scrollView;
    LinearLayout ll_back, ll_upload, ll_continue, ll_back_button;
    int PERMISSION_CODE = 2;
    int CAMERA_CODE = 0;
    int GALLARY_CODE = 1;
    int VIDEO_CODE = 3;
    Bitmap bitmap;
    String job_id = "", jwt = "", cat_id = "";
    String Motor_Mem_Image = "";
    LoginDetail_DBO loginDetail_dbo;
    private File file_1;
    String imageOrVideo = null;
    boolean isCamera = true;
    String video_path, help = "";
    Uri videoUri;
    String thumbnail = "", placeholder = "";
    JobDetail_DBO jobDetail_dbo;
    ArrayList<CarImageDBO> carImageDBOArrayList;
    public static ArrayList<CarVideosDbo> carVideosDboArrayList;
    GallaryDBO gallaryDBO = new GallaryDBO();
    boolean isPending = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_new_job_step_four);
        getViewS();
        isPending = jobDetail_dbo.isPending();
        if (isPending) {
            setHeader("Post a New Job");
            setfooter("jobs");
            setPostJObFooter(getApplicationContext());
        } else {
            setHeader("Edit Job");
            setfooter("job_details");
            setJobDetailsFooter(getApplicationContext());
        }
        setlistenrforfooter();

        getCarImages();
        getCarVideos();
        setOnClivkLitener();
        setViews();

        setAdapter();
        setAdapterForVideo();

    }

    @Override
    protected void onResume() {
        super.onResume();
        setPostJObFooter(this);

    }

    public void setViews() {
        if (Gallary_lst.size() == 0) {
            gridView.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
            gridView.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
        if (Gallary_video.size() == 0) {
            gridview_video.setVisibility(View.GONE);
            nodata_video.setVisibility(View.VISIBLE);
        } else {
            gridview_video.setVisibility(View.VISIBLE);
            nodata_video.setVisibility(View.GONE);
        }
    }


    public void getViewS() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(appContext);
        setHeader(getString(R.string.gallaery));
        carImageDBOArrayList = new ArrayList<>();
        carVideosDboArrayList = new ArrayList<>();
        Gallary_lst = new ArrayList<>();
        Gallary_video = new ArrayList<>();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        ll_back = findViewById(R.id.ll_back);
        ll_upload = findViewById(R.id.ll_upload);
        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);

        gridView = findViewById(R.id.gridview);
        gridview_video = findViewById(R.id.gridview_video);
        nodata = findViewById(R.id.nodata);
        nodata_video = findViewById(R.id.nodata_video);
        scrollView = findViewById(R.id.scrollview);


        Intent intent = getIntent();
        if (intent != null) {
            job_id = intent.getStringExtra("job_id");
            cat_id = intent.getStringExtra("cat_id");
            placeholder = intent.getStringExtra("placeholder");
            help = intent.getStringExtra("help");
        }
        jwt = loginDetail_dbo.getJWTToken();
        AppLog.Log("jwt", "jwt" + jwt);
    }

    public void getCarImages() {
        Gallary_lst = new ArrayList<>();
        carImageDBOArrayList = jobDetail_dbo.getCarImageDBOArrayList();
        if (carImageDBOArrayList.size() > 0) {
            for (int i = 0; i < carImageDBOArrayList.size(); i++) {
                String s = carImageDBOArrayList.get(i).getUrl();
                gallaryDBO = new GallaryDBO();
                gallaryDBO.setImage(s);
                gallaryDBO.setKey("" + (i + 1));
                Gallary_lst.add(gallaryDBO);

            }

        }


    }

    public void getCarVideos() {
        carVideosDboArrayList = jobDetail_dbo.getCarVideosDboArrayList();
        if (carVideosDboArrayList.size() > 0) {
            for (int i = 0; i < carVideosDboArrayList.size(); i++) {
                gallaryDBO = new GallaryDBO();
                gallaryDBO.setThumbnail(carVideosDboArrayList.get(i).getThumbnail());
                gallaryDBO.setVideo(carVideosDboArrayList.get(i).getVideoUrl());
                Gallary_video.add(gallaryDBO);
            }
        }
    }

    public void setOnClivkLitener() {
        ll_back.setOnClickListener(this);
        ll_upload.setOnClickListener(this);
        ll_continue.setOnClickListener(this);
        ll_back_button.setOnClickListener(this);
    }

    public void setAdapter() {
        adapter = new Favorite_Adapter(appContext, Gallary_lst);
        gridView.setAdapter(adapter);
        scrollView.smoothScrollTo(0, 0);
    }

    public void setAdapterForVideo() {
        adapterVideo = new Favorite_AdapterForVideo(appContext, Gallary_video);
        gridview_video.setAdapter(adapterVideo);
        scrollView.smoothScrollTo(0, 0);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.ll_continue:
                Intent intent;
                if (cat_id.equalsIgnoreCase("10")) {
                    intent = new Intent(appContext, PostNewJobAdditionalStep.class);
                } else {
                    intent = new Intent(appContext, EditJobStepFive.class);
                }
                intent.putExtra("job_id", job_id);
                AppLog.Log("place4 holder", "-->" + placeholder);
                intent.putExtra("placeholder", placeholder);
                intent.putExtra("help", help);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.ll_upload:
                which = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    RuntimeCameraGalleryPermission();
                } else {
                    selectImage();
                }
                break;
            case R.id.ll_cancel:
                intent = new Intent(appContext, DashboardScreen.class);
                intent.addFlags(Constants.INTENT_FLAGS);
                startActivity(intent);
                finish();
                activityTransition();
                break;
            case R.id.ll_back_button:
                onBackPressed();
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

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Take Video", "Choose from Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(appContext);
        builder.setTitle("Add  Photo/Video !");
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
                } else if (options[item].equals("Take Video")) {
                    try {
                        Intent intent1 = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                        startActivityForResult(intent1, VIDEO_CODE);

                    } catch (ActivityNotFoundException e) {
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                    photoPickerIntent.setType("video/*, image/*");
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
                isCamera = false;
                String path = data.getData().getPath();

                AppLog.Log("Path", "" + path);
                if (path.contains("/video/")) {
                    videoUri = data.getData();
                    onSelectVideo(data);
                }
                if (path.contains("/images/")) {
                    onSelectFromGalleryResult(data);
                }
            }
        } else if (requestCode == CAMERA_CODE) {
            if (resultCode == RESULT_OK) {
                isCamera = true;


                onCaptureImageResult(data);
            }
        } else if (requestCode == VIDEO_CODE) {
            if (resultCode == RESULT_OK) {
                isCamera = true;
                videoUri = Uri.parse(data.getDataString());
                onSelectVideo(data);
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Video recording cancelled.", Constants.TOAST_TIME).show();
            } else {
                Toast.makeText(this, "Failed to record video", Constants.TOAST_TIME).show();
            }
        }
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        bitmap = null;
        if (data != null) {
            Uri selectedImageUri = data.getData();
            imageOrVideo = getRealPathFromURI(selectedImageUri);
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        Uri fileUri = getImageUri(appContext, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));
        if (Connectivity.isConnected(appContext)) {
            saveInGallary();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }

    }

    @SuppressWarnings("deprecation")
    private void onSelectVideo(Intent data) {
        Uri vid = data.getData();
        imageOrVideo = getPath(vid);

        if (isCamera) {
            video_path = videoUri.getPath();
            thumbnail = createThumbnail(imageOrVideo);
        } else {
            video_path = getPath(videoUri);
            thumbnail = createThumbnail(video_path);
        }

        file_1 = new File(getRealPathFromURI(vid));
        AppLog.Log("thumbnai", "" + imageOrVideo);
        if (Connectivity.isConnected(appContext)) {
            savevideo();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
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
        if (Connectivity.isConnected(appContext)) {
            saveInGallary();
        } else {
            showAlertDialog(getResources().getString(R.string.no_internet));
        }
    }

    private String createThumbnail(String path) {
        Bitmap bmThumbnail;
        // MICRO_KIND: 96 x 96 thumbnail
        bmThumbnail = ThumbnailUtils.createVideoThumbnail(path,
                MediaStore.Video.Thumbnails.MINI_KIND);
        Uri thumbnailURI = getImageUri(appContext, bmThumbnail);
        String t_path = getRealPathFromURI(thumbnailURI);
        return t_path;
    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            AppLog.Log(TAG, "Video Path:" + cursor.getString(column_index));
            return cursor.getString(column_index);
        } else {
            return null;
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
        //galaery
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
                FileBody t_fileBody = null;
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
                try {
                    if (file_1 != null && file_1.length() > 1) {
                        try {
                            t_fileBody = new FileBody(file_1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (which == 0) {
                        reqEntity.addPart(Constants.GALLARY.SERVICE_NAME, new StringBody("upload_job_image"));
                    } else if (which == 1) {
                        reqEntity.addPart(Constants.GALLARY.SERVICE_NAME, new StringBody(WebServiceURLs.EDIT_UPLOAD_JOB_IMAGE));
                        reqEntity.addPart("image_index", new StringBody(key));
                        AppLog.Log("image_index-->", "image_index-->" + key);
                    }

                    reqEntity.addPart(Constants.PostNewJob.CAR_IMAGE, t_fileBody);

                    reqEntity.addPart(Constants.PostNewJob.JID, new StringBody(job_id));
                    reqEntity.addPart(Constants.PostNewJob.JWT, new StringBody(jwt));
                    AppLog.Log("JWT-->", "jwt-->" + jwt);
                    AppLog.Log("job_id-->", "job_id-->" + job_id);
                    AppLog.Log("CAR_IMAGE-->", "CAR_IMAGE-->" + t_fileBody);
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
                    status = response.getString(Constants.STATUS);
                    AppLog.Log(TAG, "Status: " + status);

                    if (which == 0) {
                        GallaryDBO model = new GallaryDBO();
                        model.setImage(response.getString("path"));
                        model.setKey(String.valueOf(Gallary_lst.size() + 1));
                        Gallary_lst.add(model);
                    } else if (which == 1) {
                        GallaryDBO model = new GallaryDBO();
                        model.setImage(response.getString("path"));
                        model.setKey(String.valueOf(SelectedImagePos+1));
                        Gallary_lst.set(SelectedImagePos, model);
                    }
                    adapter.notifyDataSetChanged();
                    scrollView.fullScroll(View.FOCUS_DOWN);
                    setAdapter();
                    setViews();

                } catch (JSONException e) {
                    AppLog.Log(TAG, "Erro in onResponse : " + e.getMessage());
                    e.printStackTrace();
                }


            }
        }.execute();
    }

    private void savevideo() {
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
                FileBody t_fileBody = null, thumb_file = null;
                ResponseHandler<String> res = new BasicResponseHandler();
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(WebServiceURLs.BASE_URL);
                MultipartEntityBuilder reqEntity = MultipartEntityBuilder.create();
                reqEntity.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);


                AppLog.Log("printed_thumbnail", "" + thumbnail);

                File thumba_file = new File(thumbnail);


                try {
                    if (thumba_file != null && thumba_file.length() > 1) {
                        try {
                            thumb_file = new FileBody(thumba_file);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (file_1 != null && file_1.length() > 1) {
                        try {
                            t_fileBody = new FileBody(file_1);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    reqEntity.addPart(Constants.GALLARY.SERVICE_NAME, new StringBody("upload_job_video"));
                    reqEntity.addPart(Constants.PostNewJob.CAR_VIDEO, t_fileBody);
                    reqEntity.addPart(Constants.PostNewJob.JID, new StringBody(job_id));
                    reqEntity.addPart(Constants.PostNewJob.JWT, new StringBody(jwt));
                    reqEntity.addPart(Constants.PostNewJob.THUMBNAIL, thumb_file);

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

                GallaryDBO model = new GallaryDBO();
                model.setImage(thumbnail);
                if (isCamera) {
                    model.setVideo(imageOrVideo);
                } else {
                    model.setVideo(video_path);
                }
                Gallary_video.add(model);
                adapterVideo.notifyDataSetChanged();
                scrollView.fullScroll(View.FOCUS_DOWN);
                setAdapterForVideo();
                setViews();
            }

        }.execute();
    }

    private void openPopupForImage(String path) {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.image_popup_for_postjob_4, null);
        ImageView img = view.findViewById(R.id.img);


        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        Glide.with(appContext)
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.default_car)
                .error(R.drawable.default_car)
                .skipMemoryCache(false)
                .into(img);
        dialog.getWindow().setAttributes(lp);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
    }

    private void openPopupForVideo(String path) {
        final Dialog dialog = new Dialog(appContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.video_popup, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        VideoView vv = view.findViewById(R.id.vv);
        ImageView close = view.findViewById(R.id.close);

        Uri uri = Uri.parse(path);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                dialog.dismiss();
                return false;
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }

    public class Favorite_Adapter extends BaseAdapter {

        Context context;
        Favorite_Adapter.ViewHolder holder;
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
            holder = new Favorite_Adapter.ViewHolder();

            final GallaryDBO model = array_gallay.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_row_gallary_post_job, null);
                holder.iv_image = convertView.findViewById(R.id.gridimage);
                holder.video_button = convertView.findViewById(R.id.video_button);
                holder.imgEdit = convertView.findViewById(R.id.imgEdit);
                holder.imgEdit.setVisibility(View.VISIBLE);
                convertView.setTag(holder);
            } else {
                holder = (Favorite_Adapter.ViewHolder) convertView.getTag();
            }
            AppLog.Log("image_path", "" + model.getImage());

            holder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getImage().contains(Constants.URL)) {
                        openPopupForImage("http://" + model.getImage());
                    } else {
                        openPopupForImage(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getImage());
                    }

                }
            });
            holder.imgEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
            if (model.getImage() != null) {


                if (model.getImage().contains(Constants.URL)) {
                    Glide.with(appContext)
                            .load("http://" + model.getImage())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.default_car)
                            .error(R.drawable.default_car)
                            .skipMemoryCache(false)
                            .into(holder.iv_image);
                } else {
                    Glide.with(appContext)
                            .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getImage())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.default_car)
                            .error(R.drawable.default_car)
                            .skipMemoryCache(false)
                            .into(holder.iv_image);
                }

            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            Grid_image iv_image;
            ImageView video_button, imgEdit;
        }
    }

    public class Favorite_AdapterForVideo extends BaseAdapter {

        Context context;
        Favorite_AdapterForVideo.ViewHolder holder;
        ArrayList<GallaryDBO> array_gallay = new ArrayList<GallaryDBO>();

        public Favorite_AdapterForVideo(Context context, ArrayList<GallaryDBO> array_gallay) {
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
            holder = new Favorite_AdapterForVideo.ViewHolder();

            final GallaryDBO model = array_gallay.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.grid_row_gallary_post_job, null);
                holder.iv_image = convertView.findViewById(R.id.gridimage);
                holder.video_button = convertView.findViewById(R.id.video_button);
                convertView.setTag(holder);
            } else {
                holder = (Favorite_AdapterForVideo.ViewHolder) convertView.getTag();
            }
            AppLog.Log("video_path", "" + model.getImage());


            holder.video_button.setVisibility(View.VISIBLE);

            holder.iv_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (model.getVideo() != null) {
                        if (model.getVideo().contains(Constants.URL)) {
                            openPopupForVideo(model.getVideo());
                        } else {
                            openPopupForVideo(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getVideo());
                        }
                    }


                }
            });
            if (model.getThumbnail() != null) {
                if (model.getThumbnail().contains(Constants.URL)) {
                    Glide.with(appContext)
                            .load(model.getImage())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.default_car)
                            .error(R.drawable.default_car)
                            .skipMemoryCache(false)
                            .into(holder.iv_image);
                } else {
                    Glide.with(appContext)
                            .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getThumbnail())
                            .asBitmap()
                            .diskCacheStrategy(DiskCacheStrategy.NONE)
                            .placeholder(R.drawable.default_car)
                            .error(R.drawable.default_car)
                            .skipMemoryCache(false)
                            .into(holder.iv_image);
                }
            }

            return convertView;
        }

        class ViewHolder {
            TextView tv_name;
            Grid_image iv_image;
            ImageView video_button;
        }
    }
}
