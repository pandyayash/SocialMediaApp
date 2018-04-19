package technource.autoreum.activities;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
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

import technource.autoreum.R;
import technource.autoreum.adapter.AdptWeeklyJobListing;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.DetailsOfGarageCarJobDBO;

public class WeeklyDetailsActivity extends BaseActivity {

    private TextView txtGarageName;
    private TextView txtGarageAddress;
    private TextView txtGarageMobile;
    private TextView txtGarageEmail;
    private TextView txtGarageAbn;
    private TextView txtGarageNameTitle;
    private TextView txtJobsCompleted;
    private TextView txtStarReview;
    private TextView txtTotalEarning, txtPrint, txtWeeklyReportDate;
    private RecyclerView recyclerView;
    AdptWeeklyJobListing adapter;
    Context appContext;
    ArrayList<DetailsOfGarageCarJobDBO> DeJobDBOArrayList;
    private RecyclerView.LayoutManager layoutManager;
    String URL;
    int PERMISSION_CODE = 2;
    String download_file_url = "";
    String dest_file_path = "Autoreum";
    int downloadedSize = 0, totalsize;
    float per = 0;
    LinearLayout ll_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_details);
        findViews();
        setOnclickListener();
        setHeader("Weekly Report");
        Intent intent = getIntent();
        if (intent != null) {
            if (Connectivity.isConnected(appContext)) {
                getMailBoxData(intent.getStringExtra("date"));
                txtWeeklyReportDate.setText("Weekly report " + intent.getStringExtra("date"));
            } else {
                showAlertDialog(getString(R.string.no_internet));
            }
        }
    }

    private void findViews() {
        appContext = this;
        DeJobDBOArrayList = new ArrayList<>();
        txtWeeklyReportDate = findViewById(R.id.txtWeeklyReportDate);
        txtPrint = findViewById(R.id.txtPrint);
        txtGarageName = findViewById(R.id.txtGarageName);
        txtGarageAddress = findViewById(R.id.txtGarageAddress);
        txtGarageMobile = findViewById(R.id.txtGarageMobile);
        txtGarageEmail = findViewById(R.id.txtGarageEmail);
        txtGarageAbn = findViewById(R.id.txtGarageAbn);
        txtGarageNameTitle = findViewById(R.id.txtGarageNameTitle);
        txtJobsCompleted = findViewById(R.id.txtJobsCompleted);
        txtStarReview = findViewById(R.id.txtStarReview);
        txtTotalEarning = findViewById(R.id.txtTotalEarning);
        recyclerView = findViewById(R.id.recycler_view);
        ll_back = findViewById(R.id.ll_back);

    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.txtPrint:
                if (URL != null) {
                    RunTimeExternalStoragePermission();
                } else {
                    Toast.makeText(appContext, "Some error occurred", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void setOnclickListener() {
        txtPrint.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    private void setAdapter() {

        adapter = new AdptWeeklyJobListing(appContext, DeJobDBOArrayList);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void getMailBoxData(String date) {
        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.WEEKLY_REPORT);
        params.put("date", date);
        AppLog.Log("TAG", "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, appContext, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "In GetJobs --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);
                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {

                                URL = response.getString("url");


                                JSONObject data = response.getJSONObject("data");
                                JSONArray garageDetailsArray = data.getJSONArray("garage_array");
                                JSONArray ratingCount = data.getJSONArray("rating");
                                JSONArray jobSum = data.getJSONArray("sum");
                                JSONArray jobArray = data.getJSONArray("jobs");

                                DetailsOfGarageCarJobDBO model = new DetailsOfGarageCarJobDBO();
                                if (garageDetailsArray.length() > 0) {
                                    JSONObject garage = garageDetailsArray.getJSONObject(0);
                                    model.setBussinessName(garage.getString("business_name"));
                                    model.setGarageSubrub(garage.getString("suburb"));
                                    model.setGarageState(garage.getString("state"));
                                    model.setGaragePostcode(garage.getString("postcode"));
                                    model.setGarageMobile(garage.getString("mobile"));
                                    model.setGarageAbnNo(garage.getString("abn_no"));
                                    model.setGarageEmail(garage.getString("business_email"));
                                }
                                if (ratingCount.length() > 0) {
                                    JSONObject ratingData = ratingCount.getJSONObject(0);
                                    model.setRating(ratingData.getString("rating"));

                                }
                                if (jobSum.length() > 0) {
                                    JSONObject ratingData = jobSum.getJSONObject(0);
                                    model.setTotalEarning(ratingData.getString("sum"));

                                }

                                if (jobArray.length() > 0) {
                                    model.setJobsCompleted(String.valueOf(jobArray.length()));
                                    for (int i = 0; i < jobArray.length(); i++) {
                                        JSONObject data1 = jobArray.getJSONObject(i);
                                        DetailsOfGarageCarJobDBO model1 = new DetailsOfGarageCarJobDBO();
                                        model1.setJuId(data1.getString("ju_id"));
                                        model1.setJobTitle(data1.getString("job_title"));
                                        model1.setDate(data1.getString("job_completed_date"));
                                        model1.setCjobId(data1.getString("cjob_id"));
                                        model1.setCatId(data1.getString("category_id"));
                                        DeJobDBOArrayList.add(model1);
                                    }
                                }
                                setData(model);
                                setAdapter();
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

    private void setData(DetailsOfGarageCarJobDBO data) {
        txtGarageName.setText(data.getBussinessName() + " " + data.getSubrub());
        txtGarageAddress.setText(data.getGarageState() + " " + data.getGaragePostcode());
        txtGarageMobile.setText("Mobile : " + data.getGarageMobile());
        txtGarageEmail.setText("Email : " + data.getGarageEmail());
        txtGarageAbn.setText("ABN : " + data.getGarageAbnNo());
        txtGarageNameTitle.setText("Weekly report for " + data.getBussinessName());
        txtJobsCompleted.setText(data.getJobsCompleted());
        txtStarReview.setText(data.getRating());
        txtTotalEarning.setText(data.getTotalEarning());
    }

    public void RunTimeExternalStoragePermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(appContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions((Activity) appContext,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        PERMISSION_CODE);
            } else {
                downladFile(URL);
            }
        } else {
            downladFile(URL);
        }
    }


    public void downladFile(String pdfUrl) {

        download_file_url = WebServiceURLs.BASE_URL_IMAGE_PROFILE + pdfUrl;
        String urlStr = download_file_url;
        java.net.URL url = null;
        try {
            url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                    url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        AppLog.Log("Encoded URL ", url.toString());
        download_file_url = url.toString();
        downloadAndOpenPDF();
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
                    showAlertDialog(getString(R.string.no_app_found_for_open_pdf));
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

}
