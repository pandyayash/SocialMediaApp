package aayushiprojects.greasecrowd.activities;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.braintreepayments.api.dropin.DropInActivity;
import com.braintreepayments.api.dropin.DropInRequest;
import com.braintreepayments.api.dropin.DropInResult;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager;
import aayushiprojects.greasecrowd.adapter.ViewPagerAdapterImage;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.CustomJsonObjectRequest;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.AwardJobDBOCarOwner;
import aayushiprojects.greasecrowd.model.CarImageDBO;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.Model_FollowupWork;

import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.cJObId;
import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.indicator;
import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.isFlexible;
import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.jobTitle;
import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.juId;
import static aayushiprojects.greasecrowd.adapter.AdptQuoteDetailsPager.viewpager;

public class QuoteJobDetailsPagerActivity extends BaseActivity {

    final int REQUEST_CODE = 1001;
    private final String TAG = "QuoteJobDetailsPager ==> ";
    public AdptQuoteDetailsPager pagerAdapter = null;
    public boolean isForGallary = false;
    Context apContext;
    LoginDetail_DBO loginDetail_dbo;
    String jwt;
    String str_device = "";
    int currentPage = 1;
    String sortby = "ratings";
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    ArrayList<Model_FollowupWork> followupWorkArrayList;
    ArrayList<CarImageDBO> carImageDBOArrayList;
    JobDetail_DBO jobs;
    LinearLayout ll_back;
    int position = 0;
    boolean isOfferAccepted = false;
    int PERMISSION_CODE = 3;
    int CAMERA_CODE = 2;
    int GALLARY_CODE = 1;
    int WRITE_EXTERNAL_STORAGE_CODE = 101;
    Bitmap bitmap;
    ImageView motorImage, editBtn;
    String Motor_Mem_Image = "";
    ViewPagerAdapterImage pagerAdapterImage;
    ArrayList<String> stringArrayList;
    int count = 1;
    String strWork, strPrice;
    String jid = "";
    String download_file_url = "";
    String dest_file_path = "Autoreum";
    int downloadedSize = 0, totalsize;
    float per = 0;
    boolean isDownloadInvoice = false;
    boolean isAcceptPickup = false;
    boolean isPrintQuote = false;
    String braintreeToken = "";
    private ViewPager pager = null;
    private boolean isLoadMore = false;
    private File file_1;
    String cjobId;
    TextView tvCounter;
    String send_new_time = "no";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quote_job_details_pager);
        findViews();
        setHeader("Quotes");
        setfooter("job_details");
        setJobDetailsQuoteFooter(apContext);
        setlistenrforfooter();
    }

    private void findViews() {
        apContext = this;
        awardJobArrayList = new ArrayList<>();
        carImageDBOArrayList = new ArrayList<>();
        stringArrayList = new ArrayList<>();
        followupWorkArrayList = new ArrayList<>();
        differentDensityAndScreenSize(apContext);
        pagerAdapter = new AdptQuoteDetailsPager(apContext, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                CheckBox checkBox = view.findViewById(R.id.checkbox);
                TextView btnAward = view.findViewById(R.id.btnAward);
                TextView btnOfferAccept = view.findViewById(R.id.btnOfferAccept);
                TextView btnPaymentOption = view.findViewById(R.id.btnPaymentOption);
                TextView txtPrice = view.findViewById(R.id.txtPrice);
                TextView textView = view.findViewById(R.id.txtJobNumber);
                TextView btnInvoice = view.findViewById(R.id.btnInvoice);
                TextView txtServiceInclusion = view.findViewById(R.id.txtServiceInclusion);
//                jid = textView.getText().toString();
                if (view == view.findViewById(R.id.btnAward)) {
                    if (btnAward.getText().toString().equalsIgnoreCase("AWARD")) {
                        if (Connectivity.isConnected(apContext)) {
                            WebCallCloseJob("AWARD", position, false);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    } else if (btnAward.getText().toString().equalsIgnoreCase("ACCEPT")) {
                        if (Connectivity.isConnected(apContext)) {
                            WebActionsgarage("ACCEPT", position);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    } else if (btnAward.getText().toString().equalsIgnoreCase("Mark Completed")) {
                        if (Connectivity.isConnected(apContext)) {
                            WebActionsgarage("WORKDONE", position);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    } else if (btnAward.getText().toString().equalsIgnoreCase("accept & pickup car")) {

                        if (Connectivity.isConnected(apContext)) {
                            WebCallCloseJob("ACCEPT", position, true);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    } else if (btnAward.getText().toString().equalsIgnoreCase("Update Quote")) {

                        Intent intent = new Intent(apContext, UpdateQuoteGarageActivity.class);
                        intent.putExtra("jobTitle", jobTitle);
                        intent.putExtra("isFlexible", isFlexible);
                        intent.putExtra("jobBidmaster", awardJobArrayList);
                        startActivity(intent);
                        activityTransition();

                    }

                }
                if (view == view.findViewById(R.id.btnPaymentOption)) {
                    if (btnPaymentOption.getText().toString().equalsIgnoreCase("download invoice")) {
                        isDownloadInvoice = true;

                        if (Connectivity.isConnected(apContext)) {
                            webCallInvoiceMailTOClient(true);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    } else if (btnPaymentOption.getText().toString().equalsIgnoreCase("payment options")) {

                        // Toast.makeText(apContext, "Clicked", Toast.LENGTH_SHORT).show();
                        BottomSheetDialog(position);


                    } else if (btnPaymentOption.getText().toString().equalsIgnoreCase("Cancel Quote")) {

                        if (Connectivity.isConnected(apContext)) {
                            WebCallCancelQuote(position);
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }


                    }
                }
                if (view == view.findViewById(R.id.btnOfferAccept)) {
                    if (btnOfferAccept.getText().toString().equalsIgnoreCase("Accept?")) {
                        isOfferAccepted = true;
                        Toast.makeText(apContext, "Offer accepted.", Toast.LENGTH_SHORT).show();
                        btnOfferAccept.setText("Accepted");
                    }
                }
                if (view == view.findViewById(R.id.txtServiceInclusion)) {
                    if (awardJobArrayList.get(position).getServices().size() > 0) {
                        Intent intent = new Intent(apContext, DisplayServiceInclusionActivity.class);
                        intent.putExtra("data", awardJobArrayList.get(position));
                        intent.putExtra("title", jobTitle);
                        startActivity(intent);
                        activityTransition();
                    } else {
                        Toast.makeText(apContext, "There is no services added.", Toast.LENGTH_SHORT).show();
                    }

                }
                if (view == view.findViewById(R.id.btnUploadImgs)) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        RuntimeCameraGalleryPermission();
                    } else {
                        selectImage();
                    }
                }
                if (view == view.findViewById(R.id.btnInvoice)) {
                    if (btnInvoice.getText().toString().equalsIgnoreCase("generate safety report")) {
                        Intent intent = new Intent(apContext, GenerateSafetyReportGarageActivity.class);
                        intent.putExtra("jid", awardJobArrayList.get(position).getJob_id());
                        startActivity(intent);
                        activityTransition();
                    } else if (btnInvoice.getText().toString().equalsIgnoreCase("download invoice")) {
                        isDownloadInvoice = false;
                        if (isReadStorageAllowed()) {
                            //If permission is already having then showing the toast
                            //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                            //Existing the method with return
                            if (Connectivity.isConnected(apContext)) {
                                webCallInvoiceMailTOClient(false);
                            } else {
                                showAlertDialog(getString(R.string.no_internet));
                            }

                            return;
                        }

                        //If the app has not the permission then asking for the permission
                        requestStoragePermission();

                    } else if (btnInvoice.getText().toString().equalsIgnoreCase("Cancel")) {
                        if (Connectivity.isConnected(apContext)) {
                            WebCallCloseJob("CANCEL", position, false);

                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }
                    }
                }
                if (view == view.findViewById(R.id.checkbox)) {
                    if (checkBox.isChecked()) {
                        popupFollowupWork(checkBox);
                    }
                }
                if (view == view.findViewById(R.id.txtacceptNewTime)) {
                    send_new_time = "yes";

                }
                if (view == view.findViewById(R.id.btnPrintQuote)) {
                    isPrintQuote = true;
                    if (isReadStorageAllowed()) {
                        //If permission is already having then showing the toast
                        //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                        //Existing the method with return
                        if (Connectivity.isConnected(apContext)) {
                            webCallPrintQuote(position);
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                        return;
                    }

                    //If the app has not the permission then asking for the permission
                    requestStoragePermission();

                }
                if (view == view.findViewById(R.id.btnGenerateSafetyReport)) {
                    String pdfUrl = "public/assets/safety_reports/" + juId + ".pdf";
                    download_file_url = WebServiceURLs.BASE_URL_IMAGE_PROFILE + pdfUrl;
                    String urlStr = download_file_url;
                    URL url = null;
                    try {
                        url = new URL(urlStr);

                        URI uri = null;
                        try {
                            uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                                    url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                        url = uri.toURL();
                        AppLog.Log("Encoded URL ", url.toString());
                        download_file_url = url.toString();

                        downloadAndOpenPDF();
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
                if (view == view.findViewById(R.id.txtFollowupWork)) {
                    if (followupWorkArrayList.size() > 0) {
                        Intent intent = new Intent(apContext, ViewFollowUpWorkActivity.class);
                        intent.putExtra("followupWorkArrayList", followupWorkArrayList);
                        intent.putExtra("total", awardJobArrayList.get(position).getTotal());
                        intent.putExtra("jid", awardJobArrayList.get(position).getJob_id());
                        intent.putExtra("garageId", awardJobArrayList.get(position).getGarage_id());
                        startActivity(intent);
                        activityTransition();
                    } else {
                        Toast.makeText(apContext, "no follow up work", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        pager = findViewById(R.id.view_pager);
        pager.setAdapter(pagerAdapter);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(apContext);
        jwt = loginDetail_dbo.getJWTToken();
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();

        if (intent != null) {
            awardJobArrayList = intent.getParcelableArrayListExtra("awardJobArrayList");
            followupWorkArrayList = intent.getParcelableArrayListExtra("followupWorkArrayList");
            //Toast.makeText(apContext, "" + followupWorkArrayList.size(), Toast.LENGTH_SHORT).show();
            jobs = intent.getParcelableExtra("jobs");
            position = intent.getIntExtra("pos", 0);
            setUpVIewPager(awardJobArrayList, jobs);
            pager.setCurrentItem(intent.getIntExtra("pos", 0));
            AppLog.Log("Followup Work", "SIZe ---> " + followupWorkArrayList.size());
        }


    }

    public void onBraintreeSubmit() {

        DropInRequest dropInRequest = new DropInRequest().clientToken(braintreeToken);

        startActivityForResult(dropInRequest.getIntent(this), REQUEST_CODE);
    }

    public void BottomSheetDialog(final int position) {

        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(apContext);
        View sheetView = getLayoutInflater().inflate(R.layout.payment_bottom_sheet, null);
        mBottomSheetDialog.setContentView(sheetView);
        TextView txtPayOnPickup = sheetView.findViewById(R.id.txtPayOnPickup);
        TextView txtPayPal = sheetView.findViewById(R.id.txtPayPal);
        TextView txtCancel = sheetView.findViewById(R.id.txtCancel);

        txtPayOnPickup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
                if (Connectivity.isConnected(apContext)) {
                    WebCallPayOnPickup("PAYMENT", position);
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
            }
        });
        txtPayPal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                    if (Connectivity.isConnected(apContext)) {
                        mBottomSheetDialog.dismiss();
                        getBraintreeTokenFromServer();
                    } else {
                        showAlertDialog(getString(R.string.no_internet));
                    }

                }


            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });

        mBottomSheetDialog.show();
    }


    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        return result == PackageManager.PERMISSION_GRANTED;

        //If permission is not granted returning false
    }

    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_EXTERNAL_STORAGE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                //denied
                //Log.e("denied", permission);
                requestStoragePermission();
            } else {
                if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                    //allowed
                    //Log.e("allowed", permission);
                    if (isPrintQuote) {
                        if (Connectivity.isConnected(apContext)) {
                            webCallPrintQuote(position);
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }

                    }
                    if (isDownloadInvoice) {
                        if (Connectivity.isConnected(apContext)) {
                            webCallInvoiceMailTOClient(isDownloadInvoice);
                        } else {
                            showAlertDialog(getString(R.string.no_internet));
                        }
                    }
                } else {
                    //set to never ask again
                   // Log.e("set to never ask again", permission);
                    // requestStoragePermission();

                    //do something here.
                }
            }
        }
    }

    public int differentDensityAndScreenSize(Context context) {
        int value = 20;
        String str = "";
        if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_SMALL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "small-ldpi";
                    str_device = "small-ldpi";
                    value = 20;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "small-mdpi";
                    str_device = "small-ldpi";
                    value = 20;
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "small-hdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "small-xhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "small-xxhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "small-xxxhdpi";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "small-tvdpi";
                    str_device = "small-ldpi";
                    break;
                default:
                    str = "small-unknown";
                    value = 20;
                    str_device = "small-ldpi";
                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "normal-ldpi";
                    str_device = "normal-ldpi";
                    value = 82;
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "normal-mdpi";
                    value = 82;
                    str_device = "normal-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "normal-hdpi";
                    str_device = "normal-ldpi";
                    value = 82;
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "normal-xhdpi";
                    str_device = "normal-xhdpi";
                    value = 90;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "normal-xxhdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "normal-xxxhdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "normal-tvdpi";
                    str_device = "normal-xxhdpi";
                    value = 96;
                    break;
                default:
                    str = "normal-unknown";
                    str_device = "normal-unknown";
                    value = 82;
                    break;
            }
        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_LARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "large-ldpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "large-mdpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "large-hdpi";
                    value = 78;
                    str_device = "large-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "large-xhdpi";
                    str_device = "large-xhdpi";
                    value = 125;
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "large-xxhdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "large-xxxhdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "large-tvdpi";
                    value = 125;
                    str_device = "large-xhdpi";
                    break;
                default:
                    str = "large-unknown";
                    value = 78;

                    break;
            }

        } else if ((context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) == Configuration.SCREENLAYOUT_SIZE_XLARGE) {
            switch (context.getResources().getDisplayMetrics().densityDpi) {
                case DisplayMetrics.DENSITY_LOW:
                    str = "xlarge-ldpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_MEDIUM:
                    str = "xlarge-mdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_HIGH:
                    str = "xlarge-hdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XHIGH:
                    str = "xlarge-xhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXHIGH:
                    str = "xlarge-xxhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_XXXHIGH:
                    str = "xlarge-xxxhdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                case DisplayMetrics.DENSITY_TV:
                    str = "xlarge-tvdpi";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
                default:
                    str = "xlarge-unknown";
                    value = 125;
                    str_device = "xlarge-ldpi";
                    break;
            }
        }

        return value;
    }

    public void setUpVIewPager(ArrayList<AwardJobDBOCarOwner> award, JobDetail_DBO job) {

        LayoutInflater inflater2 = getLayoutInflater();
        int a = 0;
        for (int i = 0; i < awardJobArrayList.size(); i++) {
            if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                if (loginDetail_dbo.getUserid().equalsIgnoreCase(awardJobArrayList.get(i).getGarage_id())) {
                    LinearLayout v0 = (LinearLayout) inflater2.inflate(R.layout.frames_quote_job_details, null);
                    pagerAdapter.addView(v0, a, awardJobArrayList.get(i), job);
                    a++;
                }
            } else {
                LinearLayout v0 = (LinearLayout) inflater2.inflate(R.layout.frames_quote_job_details, null);
                pagerAdapter.addView(v0, i, awardJobArrayList.get(i), job);
            }
        }

        pagerAdapter.notifyDataSetChanged();

        pager.setClipToPadding(false);

        /*if (str_device.equalsIgnoreCase("small-ldpi")) {
            pager.setPadding(20, 0, 20, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "small");
        } else if (str_device.equalsIgnoreCase("normal-ldpi")) {
            pager.setPadding(30, 0, 30, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "ldpi");
        } else if (str_device.equalsIgnoreCase("normal-xhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "xhdpi");
        } else if (str_device.equalsIgnoreCase("normal-xxhdpi")) {
            pager.setPadding(100, 0, 100, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "xxhdpi");
        } else if (str_device.equals("normal-hdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-hdpi");
        } else if (str_device.equals("normal-mdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-mdpi");
        } else if (str_device.equals("normal-xhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-xhdpi");
        } else if (str_device.equals("normal-xxhdpi")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "normal-xxhdpi");
        } else if (str_device.equals("normal-xxxhdpi")) {
            pager.setPadding(100, 0, 100, 0);
            pager.setPageMargin(15);
            AppLog.Log("here", "normal-xxxhdpi");
        } else if (str_device.equals("normal-unknown")) {
            pager.setPadding(80, 0, 80, 0);
            pager.setPageMargin(13);
            AppLog.Log("here", "normal-unknown");
        } else {
            pager.setPadding(20, 0, 20, 0);
            pager.setPageMargin(5);
            AppLog.Log("here", "here");

        }*/
    }

    /*------------Web call for close job---------------*/
    public void WebCallCloseJob(final String action, final int pos, final boolean isAccept_Pickup) {
        String add_offer = "";
        String price = "";
        if (isOfferAccepted) {
            add_offer = "Accepted";
            price = awardJobArrayList.get(pos).getTotal();
        } else {
            add_offer = "Rejected";
            price = awardJobArrayList.get(pos).getBid_price();
        }
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS);
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.JID, awardJobArrayList.get(pos).getJob_id());
        params.put(Constants.JOB_ACTIONS.AWARDED_GARAGE_ID, awardJobArrayList.get(pos).getGarage_id());
        params.put(Constants.JOB_ACTIONS.SEND_NEW_TIME, send_new_time);
        if (!isAccept_Pickup) {
            params.put(Constants.JOB_ACTIONS.PRICE, price);
            params.put(Constants.JOB_ACTIONS.ADD_OFFER, add_offer);

        }
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", action + "JOb --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                if (isAccept_Pickup) {
                                    cjobId = awardJobArrayList.get(pos).getJob_id();
                                    Handler handler = new Handler();
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            showPopupForReview();
                                        }
                                    }, 2000);
                                } else {

                                    finish();
                                    Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                                    startActivity(intent);
                                    activityTransition();
                                }
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }


    public void WebCallPayOnPickup(final String action, final int pos) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS);
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.JID, awardJobArrayList.get(pos).getJob_id());
        params.put(Constants.SUBMIT_BIDS.GARAGE_ID, awardJobArrayList.get(pos).getGarage_id());
        params.put(Constants.JOB_ACTIONS.AMOUNT, awardJobArrayList.get(pos).getTotal());
        params.put(Constants.JOB_ACTIONS.payment_mode, "pay_on_pickup");


        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", action + "pay_on_pickup --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                                startActivity(intent);
                                activityTransition();
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(jsonObjReq);
    }


    private void showPopupForReview() {
        final Dialog dialog = new Dialog(apContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        View view = getLayoutInflater().inflate(R.layout.activity_add_areview_popup, null);


        final SimpleRatingBar ratingBar = view.findViewById(R.id.ratingBar);
        final EditText message = view.findViewById(R.id.message);
        TextView txtSubmitReview = view.findViewById(R.id.txtSubmitReview);
        TextView txtRemindMeLetter = view.findViewById(R.id.txtRemindMeLetter);
        ImageView close = view.findViewById(R.id.close);
        TextView txtCancel = view.findViewById(R.id.txtCancel);
        TextView jobTitle = view.findViewById(R.id.jobTitle);
        tvCounter = view.findViewById(R.id.tvCounter);
        message.addTextChangedListener(mTextEditorWatcher);

        jobTitle.setVisibility(View.GONE);
        txtRemindMeLetter.setVisibility(View.VISIBLE);

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog.dismiss();
                finish();
                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                startActivity(intent);
                activityTransition();
            }
        });
        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                finish();
                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                startActivity(intent);
                activityTransition();
            }
        });

        txtSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(ratingBar.getRating() == 0)) {
                    addReviewService(message.getText().toString().trim(), String.valueOf(ratingBar.getRating()), dialog, "0");
                } else {
                    Toast.makeText(apContext, "Please rate your job", Toast.LENGTH_SHORT).show();
                }

            }
        });
        txtRemindMeLetter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addReviewService("", "", dialog, "1");
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

    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {

            tvCounter.setText(String.valueOf(s.length()) + "/200");
        }

        public void afterTextChanged(Editable s) {
        }
    };

    public void addReviewService(String message, String ratings, final Dialog dialog, String type) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);
        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.PROVIDE_JOB_REVIEW);
        params.put("user_type", loginDetail_dbo.getUser_Type());
        params.put("jid", cJObId);
        params.put("review_text", message);
        params.put("review_type", type);
        params.put("rating", ratings);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                dialog.dismiss();
                                finish();
                                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                                startActivity(intent);
                                activityTransition();
                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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


    public void popupFollowupWork(final CheckBox checkBox) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.follow_up_work, null);
        dialogBuilder.setView(dialogView);

        final EditText edtFollowup1 = dialogView.findViewById(R.id.edtFollowup1);
        final EditText edtFollowup2 = dialogView.findViewById(R.id.edtFollowup2);
        final EditText edtFollowup3 = dialogView.findViewById(R.id.edtFollowup3);
        final EditText edtFollowup4 = dialogView.findViewById(R.id.edtFollowup4);
        final EditText edtFollowup5 = dialogView.findViewById(R.id.edtFollowup5);

        final EditText edtPrice1 = dialogView.findViewById(R.id.edtPrice1);
        final EditText edtPrice2 = dialogView.findViewById(R.id.edtPrice2);
        final EditText edtPrice3 = dialogView.findViewById(R.id.edtPrice3);
        final EditText edtPrice4 = dialogView.findViewById(R.id.edtPrice4);
        final EditText edtPrice5 = dialogView.findViewById(R.id.edtPrice5);

        final ArrayList<String> workArrayList = new ArrayList<>();
        final ArrayList<String> PriceArrayList = new ArrayList<>();

        /*if (followupWorkArrayList.size()>0){
            for (int i = 0; i <followupWorkArrayList.size() ; i++) {
                model_followupWork = followupWorkArrayList.get(i);
            }
        }*/

        //dialogBuilder.setTitle(" ");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //do something with edt.getText().toString();

                String s1 = edtFollowup1.getText().toString();
                String s2 = edtFollowup2.getText().toString();
                String s3 = edtFollowup3.getText().toString();
                String s4 = edtFollowup4.getText().toString();
                String s5 = edtFollowup5.getText().toString();

                String p1 = edtPrice1.getText().toString();
                String p2 = edtPrice2.getText().toString();
                String p3 = edtPrice3.getText().toString();
                String p4 = edtPrice4.getText().toString();
                String p5 = edtPrice5.getText().toString();

//                String work = s1 + s2 + s3 + s4 + s5;
//                String amt = p1 + p2 + p3 + p4 + p5;


                if (s1.length() > 0 && p1.length() > 0) {
                    Model_FollowupWork model_followupWork = new Model_FollowupWork();
                    model_followupWork.setKey(s1);
                    model_followupWork.setValue(p1);
                    followupWorkArrayList.add(model_followupWork);
                    workArrayList.add(s1);
                    PriceArrayList.add(p1);
                }

                if (s2.length() > 0 && p2.length() > 0) {
                    Model_FollowupWork model_followupWork = new Model_FollowupWork();
                    model_followupWork.setKey(s2);
                    model_followupWork.setValue(p2);
                    followupWorkArrayList.add(model_followupWork);
                    workArrayList.add(s2);
                    PriceArrayList.add(p2);
                }

                if (s3.length() > 0 && p3.length() > 0) {
                    Model_FollowupWork model_followupWork = new Model_FollowupWork();
                    model_followupWork.setKey(s3);
                    model_followupWork.setValue(p3);
                    followupWorkArrayList.add(model_followupWork);
                    workArrayList.add(s3);
                    PriceArrayList.add(p3);
                }


                if (s4.length() > 0 && p4.length() > 0) {
                    Model_FollowupWork model_followupWork = new Model_FollowupWork();
                    model_followupWork.setKey(s4);
                    model_followupWork.setValue(p4);
                    followupWorkArrayList.add(model_followupWork);
                    workArrayList.add(s4);
                    PriceArrayList.add(p4);
                }


                if (s5.length() > 0 && p5.length() > 0) {
                    Model_FollowupWork model_followupWork = new Model_FollowupWork();
                    model_followupWork.setKey(s5);
                    model_followupWork.setValue(p5);
                    followupWorkArrayList.add(model_followupWork);
                    workArrayList.add(s5);
                    PriceArrayList.add(p5);
                }


               /* StringBuilder sb = new StringBuilder();
                StringBuilder sb1 = new StringBuilder();

                if (s1.length() > 0) {
                    sb.append(s1);
                }
                if (s2.length() > 0) {
                    sb.append(",");
                    sb.append(s2);

                }
                if (s3.length() > 0) {
                    sb.append(",");
                    sb.append(s3);

                }
                if (s4.length() > 0) {
                    sb.append(",");
                    sb.append(s4);

                }
                if (s5.length() > 0) {
                    sb.append(",");
                    sb.append(s5);
                }

                if (p1.length() > 0) {
                    sb1.append(p1);
                }
                if (p2.length() > 0) {
                    sb1.append(",");
                    sb1.append(p2);

                }
                if (p3.length() > 0) {
                    sb1.append(",");
                    sb1.append(p3);

                }
                if (p4.length() > 0) {
                    sb1.append(",");
                    sb1.append(p4);

                }
                if (p5.length() > 0) {
                    sb1.append(",");
                    sb1.append(p5);
                }




                // sb.append(",");
                AppLog.Log(TAG, "sb--> " + sb);
                AppLog.Log(TAG, "sb1--> " + sb1);*/

                String work = android.text.TextUtils.join(",", workArrayList);
                String price = android.text.TextUtils.join(",", PriceArrayList);

                if (Connectivity.isConnected(apContext)) {
                    webCallFollowUpWork("followup", position, "" + work, "" + price);
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }


            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
                if (checkBox.isChecked()) {
                    checkBox.setChecked(false);
                }
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void webCallFollowUpWork(String action, int pos, String work, String amt) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS_GARAGE);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.JID, awardJobArrayList.get(pos).getJob_id());
        params.put(Constants.JOB_ACTIONS.WORK, work);
        params.put(Constants.JOB_ACTIONS.AMOUNT, amt);
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "WebActionsgarage --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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

    public void webCallInvoiceMailTOClient(final boolean isDownloadInvoice) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.INVOICE_DETAIL_MAIL);
        params.put(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.LoginType.ID, juId);
        params.put("type", "");

            /*if (isDownloadInvoice) {
                params.put("type", "mail");
            } else {
                params.put("type", "");
            }*/

            AppLog.Log(TAG, "Params : " + params);
            CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                    url, apContext, new JSONObject(params),
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            AppLog.Log("Response", "InvoiceMailTOClient --> " + response);
                            try {
                                String status = response.getString(Constants.STATUS);

                                if (status.equalsIgnoreCase(Constants.SUCCESS)) {


                                    //if (!isDownloadInvoice) {
                                        String pdfUrl = response.getString("url");
                                        download_file_url = WebServiceURLs.BASE_URL_IMAGE_PROFILE + pdfUrl;
                                        String urlStr = download_file_url;
                                        URL url = new URL(urlStr);
                                        URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                                                url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                        url = uri.toURL();
                                        AppLog.Log("Encoded URL ", url.toString());
                                        download_file_url = url.toString();
                                        downloadAndOpenPDF();
                                   /* }else {
                                        //Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                    }*/


                                } else {
                                    showAlertDialog(response.getString(Constants.MESSAGE));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            showLoadingDialog(false);
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

    public void webCallPrintQuote(int position) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.PRINT_QUOTE);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.QUOTEJOBDETAILS.JOB_ID, juId);
        params.put(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
        params.put(Constants.SUBMIT_BIDS.GARAGE_ID, awardJobArrayList.get(position).getGarage_id());
        params.put(Constants.SIGN_UP.USER_ID, loginDetail_dbo.getUserid());
        params.put("bid_id", awardJobArrayList.get(position).getId());
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "InvoiceMailTOClient --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                String pdfUrl = response.getString("data");
//                                Intent intent = new Intent(apContext,WebViewActivity.class);
//                                intent.putExtra("url",url);
//                                startActivity(intent);
//                                activityTransition();
                                download_file_url = WebServiceURLs.BASE_URL_IMAGE_PROFILE + pdfUrl;
//                                Intent intent = new Intent(apContext,WebViewActivity.class);
//                                intent.putExtra("url",download_file_url);
//                                startActivity(intent);
//                                activityTransition();
                                String urlStr = download_file_url;
                                URL url = new URL(urlStr);
                                URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                                        url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                url = uri.toURL();
                                AppLog.Log("Encoded URL ", url.toString());
                                download_file_url = url.toString();
                                downloadAndOpenPDF();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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


    public void WebCallCancelQuote(int position) {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.CANCEL_BID);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.SUBMIT_BIDS.JOB_ID, awardJobArrayList.get(position).getJob_id());


        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Cancel Quote --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(QuoteJobDetailsPagerActivity.this, DashboardScreen.class);
                                startActivity(intent);
                                activityTransition();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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

    public void getBraintreeTokenFromServer() {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.GENERATE_BRAINTREE_TOKEN);

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Cancel Quote --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                braintreeToken = response.getString("data");
                                if (Connectivity.isConnected(apContext)) {

                                    onBraintreeSubmit();
                                } else {
                                    showAlertDialog(getString(R.string.no_internet));
                                }
                                // Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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


    public void SendPayment(String ju_id, String amt, String garage_id, String nonse) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.BRAINTREE_SAVE_DATA);
        params.put(Constants.BRAINTREE_PAYMENT.CURRENCY, "AUD");
        params.put(Constants.BRAINTREE_PAYMENT.JU_ID, ju_id);
        params.put(Constants.BRAINTREE_PAYMENT.AMT, amt);
        params.put(Constants.BRAINTREE_PAYMENT.GARAGE_ID, garage_id);
        params.put(Constants.BRAINTREE_PAYMENT.PAYMENTMETHODNONCE, nonse);
        //params.put(Constants.BRAINTREE_PAYMENT.PAYMENTMETHODNONCE, "53246d57-4e8b-0aca-8b08-fa71ad8a98ce");

        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "Payment --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
                                startActivity(intent);
                                activityTransition();


                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        showLoadingDialog(false);
                    }
                });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(jsonObjReq);
    }

    void downloadAndOpenPDF() {
        showLoadingDialog(true);
        AppLog.Log("File", download_file_url);
        new Thread(new Runnable() {
            public void run() {
                Uri path = Uri.fromFile(downloadFile(download_file_url));
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(path, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    //showAlertDialog(getString(R.string.no_app_found_for_open_pdf));
                    Intent intent = new Intent(apContext,WebViewActivity.class);
                    intent.putExtra("url",download_file_url);
                    startActivity(intent);
                    activityTransition();

                }
            }
        }).start();
    }

    File downloadFile(String dwnload_file_path) {
        File file = null;
        try {

            URL url = new URL(dwnload_file_path);
            HttpURLConnection urlConnection = (HttpURLConnection) url
                    .openConnection();

            // connect
            urlConnection.connect();

            // set the path where we want to save the file
            File SDCardRoot = Environment.getExternalStorageDirectory();
            // create a new file, to save the downloaded file
            Random r = new Random();
            int i1 = r.nextInt() + 111;
            file = new File(SDCardRoot, dest_file_path + i1);

            FileOutputStream fileOutput = new FileOutputStream(file);

            // Stream used for reading the data from the internet
            InputStream inputStream = urlConnection.getInputStream();

            // this is the total size of the file which we are
            // downloading
            totalsize = urlConnection.getContentLength();

            // create a buffer...
            byte[] buffer = new byte[1024 * 1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) * 100;
            }

            showLoadingDialog(false);
            // close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {

        } catch (final IOException e) {

        } catch (final Exception e) {

        }
        return file;
    }

    public void WebActionsgarage(String action, final int pos) {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(apContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS_GARAGE);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.JID, awardJobArrayList.get(pos).getJob_id());
        // params.put(Constants.JOB_ACTIONS.AWARDED_GARAGE_ID, garadId);
        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, apContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "WebActionsgarage --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                Toast.makeText(apContext, response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                finish();
                                Intent intent = new Intent(apContext, QuoteJobDetailsActivity.class);
//                                intent.putExtra("job_id", juId);
//                                intent.putExtra("cat_id", cat_id);
//                                // intent.putExtra("status","posted");
//                                intent.putExtra("status", awardJobArrayList.get(pos).getBid_status());
                                startActivity(intent);
                                activityTransition();

                            } else {
                                showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        showLoadingDialog(false);
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

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void RuntimeCameraGalleryPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(apContext, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(apContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) apContext,
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
        AlertDialog.Builder builder = new AlertDialog.Builder(apContext);
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
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                DropInResult result = data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String stringNonce = nonce.getNonce();
                AppLog.Log("****BrainTree Nonse --->***", "Result: " + stringNonce);
                String amt = String.format("%.0f", Float.parseFloat(awardJobArrayList.get(position).getTotal()));
                AppLog.Log("Amount --->", amt);
                /*String[] parts = amt.split("\\."); // escape .
                String part1 = parts[0];
                String part2 = parts[1];
                AppLog.Log("part1 --->",part1);*/
                if (Connectivity.isConnected(apContext)) {
                    SendPayment(awardJobArrayList.get(position).getJob_id(), amt, awardJobArrayList.get(position).getGarage_id(), stringNonce);
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }


            } else if (resultCode == Activity.RESULT_CANCELED) {
                // the user canceled
                AppLog.Log("mylog", "user canceled");
            } else {
                // handle errors here, an exception may be available in
                Exception error = (Exception) data.getSerializableExtra(DropInActivity.EXTRA_ERROR);
                AppLog.Log("mylog", "Error : " + error.toString());
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
        Uri fileUri = getImageUri(apContext, thumbnail);
        file_1 = new File(getRealPathFromURI(fileUri));

        if (isForGallary == false) {
            if (Connectivity.isConnected(apContext)) {
                saveInGallary();
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
        Uri fileUri = getImageUri(apContext, bitmap);
        file_1 = new File(getRealPathFromURI(fileUri));

        if (isForGallary == false) {
            if (Connectivity.isConnected(apContext)) {
                saveInGallary();
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
                            reqEntity.addPart("files[0]", t_fileBody);
                        } catch (Exception e) {
                            e.printStackTrace();
                            reqEntity.addTextBody("files[0]", "");
                        }
                    } else {
                        reqEntity.addTextBody("files[0]", "");
                    }

                    reqEntity.addTextBody(Constants.GALLARY.SERVICE_NAME, WebServiceURLs.UPLOADFOLLOWUPIMAGE);
                    reqEntity.addTextBody(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
                    reqEntity.addTextBody(Constants.QUOTEJOBDETAILS.JOB_ID, awardJobArrayList.get(position).getJob_id());
                    reqEntity.addTextBody(Constants.SIGN_UP.LOGIN_TOKEN, loginDetail_dbo.getJWTToken());

                    AppLog.Log("JWT-->", "jwt-->" + loginDetail_dbo.getJWTToken());
                    AppLog.Log("USER_TYPE-->", "USER_TYPE-->" + loginDetail_dbo.getUser_Type());
                    AppLog.Log("JOB_ID-->", "JOB_ID -->" + awardJobArrayList.get(position).getJob_id());
                    AppLog.Log("SERVICE_NAME-->", "SERVICE_NAME-->" + WebServiceURLs.UPLOADFOLLOWUPIMAGE);

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

                        JSONArray data = new JSONArray(response.getString("data"));
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject jsonObject = data.getJSONObject(i);
                            CarImageDBO model = new CarImageDBO();
                            model.setUrl(jsonObject.getString("image"));
                            carImageDBOArrayList.add(model);
                        }
                        //findViews();
                        //Toast.makeText(apContext, "" + carImageDBOArrayList.size(), Toast.LENGTH_SHORT).show();
                        pagerAdapterImage = new ViewPagerAdapterImage(apContext, carImageDBOArrayList);
                        viewpager.setAdapter(pagerAdapterImage);
                        //viewpager1.setAdapter(pagerAdapterImage);
                        indicator.setViewPager(viewpager);


                    } else if (status != null && status.equalsIgnoreCase(Constants.FAILURE)) {
                        Toast.makeText(apContext, response.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    AppLog.Log(TAG, "Error in onResponse : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }.execute();

    }

}


