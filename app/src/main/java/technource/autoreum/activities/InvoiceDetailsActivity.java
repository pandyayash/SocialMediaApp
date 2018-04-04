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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import technource.autoreum.R;
import technource.autoreum.adapter.AdptInvoiceListing;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.DetailsOfGarageCarJobDBO;
import technource.autoreum.model.LoginDetail_DBO;

public class InvoiceDetailsActivity extends BaseActivity {

    RecyclerView recyclerView;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    AdptInvoiceListing adapter;
    private RecyclerView.LayoutManager layoutManager;
    String id = "";
    TextView btnDetails, btnPrint, btmMail;
    DetailsOfGarageCarJobDBO model;
    ArrayList<DetailsOfGarageCarJobDBO> DeJobDBOArrayList;
    String juId = "";
    TextView txtInvoice, txtInvoiceDate;
    String download_file_url = "";
    String dest_file_path = "Autoreum";
    int downloadedSize = 0, totalsize;
    float per = 0;
    int PERMISSION_CODE = 2;
    String URL;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_details);
        setHeader("Invoice");
        getviews();
        setOnClickListener();

        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("id");
        }
        if (Connectivity.isConnected(appContext)) {
            getMailBoxData(id, false);
        } else {
            showAlertDialog(getString(R.string.no_internet));
        }
    }

    private void getviews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        appContext = this;
        DeJobDBOArrayList = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        btnDetails = (TextView) findViewById(R.id.btnDetails);
        btnPrint = (TextView) findViewById(R.id.btnPrint);
        btmMail = (TextView) findViewById(R.id.btmMail);
        txtInvoiceDate = (TextView) findViewById(R.id.txtInvoiceDate);
        txtInvoice = (TextView) findViewById(R.id.txtInvoice);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.btnDetails:
                Intent intent = new Intent(appContext, DetailsOfGarageUserJobActivity.class);
                intent.putExtra("data", model);
                startActivity(intent);
                activityTransition();
                break;
            case R.id.btmMail:
                if (Connectivity.isConnected(appContext)) {
                    getMailBoxData(id, true);
                } else {
                    showAlertDialog(getString(R.string.no_internet));
                }
                break;
            case R.id.btnPrint:
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
        URL url = null;
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


    private void setOnClickListener() {
        btnDetails.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btmMail.setOnClickListener(this);
        ll_back.setOnClickListener(this);
    }

    private void setAdapter() {

        adapter = new AdptInvoiceListing(appContext, DeJobDBOArrayList);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public void getMailBoxData(String id, final boolean flag) {

        showLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(appContext);
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log("TAG", "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.INVOICE_DETAIL_MAIL);
        params.put("id", id);
        params.put(Constants.LoginType.USER_TYPE, loginDetail_dbo.getUser_Type());
        if (flag) {
            params.put("type", "mail");
        }

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
                                if (!flag) {
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray garageDetailsArray = data.getJSONArray("garageDetails");
                                    JSONArray ClientDetailsArray = data.getJSONArray("job_array");
                                    JSONArray CarArray = data.getJSONArray("car_array");
                                    JSONArray jobArray = data.getJSONArray("job_array");
                                    JSONArray additionalOfferArray = data.getJSONArray("additional");


                                    model = new DetailsOfGarageCarJobDBO();
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

                                    if (ClientDetailsArray.length() > 0) {
                                        JSONObject client = ClientDetailsArray.getJSONObject(0);
                                        model.setFname(client.getString("fname"));
                                        model.setLname(client.getString("lname"));
                                        model.setSubrub(client.getString("suburb"));
                                        model.setState(client.getString("state"));
                                        model.setMobile(client.getString("mobile"));
                                        model.setEmail(client.getString("email"));
                                        model.setPostcode(client.getString("postcode"));
                                    }

                                    if (CarArray.length() > 0) {
                                        JSONObject car = CarArray.getJSONObject(0);
                                        model.setCarMake(car.getString("carmake"));
                                        model.setCarModel(car.getString("carmodel"));
                                        model.setCarBadge(car.getString("carbadge"));
                                        model.setRegistrationNumber(car.getString("registration_number"));
                                        model.setCarType(car.getString("car_type"));
                                    }


                                    DetailsOfGarageCarJobDBO invoiceDetails = new DetailsOfGarageCarJobDBO();
                                    if (jobArray.length() > 0) {
                                        JSONObject jobData = jobArray.getJSONObject(0);

                                        invoiceDetails.setJobTitle(jobData.getString("job_title"));
                                        invoiceDetails.setJuId(jobData.getString("ju_id"));
                                        juId = jobData.getString("ju_id");
                                        txtInvoice.setText("Tax Invoice " + jobData.getString("invoice_number"));
                                        txtInvoiceDate.setText("Invoice Date " + parseDateToddMMyyyy1(jobData.getString("job_completed_date")));

                                        if (additionalOfferArray.length() > 0) {
                                            JSONObject addData = additionalOfferArray.getJSONObject(0);
                                            invoiceDetails.setBidPrice(addData.getString("bid_price"));
                                            DeJobDBOArrayList.add(invoiceDetails);

                                            if (addData.getString("add_offer_accept").equalsIgnoreCase("1")) {

                                                invoiceDetails = new DetailsOfGarageCarJobDBO();
                                                invoiceDetails.setJobTitle(addData.getString("add_offer"));
                                                invoiceDetails.setBidPrice(addData.getString("add_offer_price"));
                                                invoiceDetails.setJuId(juId);
                                                DeJobDBOArrayList.add(invoiceDetails);
                                            }
                                        }

                                        JSONObject followUpWork = jobData.getJSONObject("followup_string1");
                                        JSONArray workArray = followUpWork.getJSONArray("work");
                                        JSONArray accept = followUpWork.getJSONArray("accept");

                                        if ((workArray.length() > 0 && accept.length() > 0) && (workArray.length() == accept.length())) {
                                            for (int i = 0; i < workArray.length(); i++) {
                                                JSONObject workObj = workArray.getJSONObject(i);
                                                JSONObject acceptObj = accept.getJSONObject(i);
                                                if (acceptObj.getString("value").equalsIgnoreCase("true")) {
                                                    DetailsOfGarageCarJobDBO detailsOfGarageCarJobDBO = new DetailsOfGarageCarJobDBO();
                                                    detailsOfGarageCarJobDBO.setJobTitle(acceptObj.getString("key"));
                                                    detailsOfGarageCarJobDBO.setBidPrice(workObj.getString("value"));
                                                    detailsOfGarageCarJobDBO.setJuId(juId);
                                                    DeJobDBOArrayList.add(detailsOfGarageCarJobDBO);
                                                }

                                            }
                                        }

                                    }
                                    setAdapter();
                                } else {
                                    Toast.makeText(appContext, "" + response.getString("message"), Toast.LENGTH_SHORT).show();
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
        queue.add(jsonObjReq);
    }

    public String parseDateToddMMyyyy1(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MM-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


}
