package technource.autoreum.fragment;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import technource.autoreum.R;
import technource.autoreum.activities.DetailsOfGarageUserJobActivity;
import technource.autoreum.activities.GenerateSafetyReportGarageActivity;
import technource.autoreum.adapter.AdptSafetyReports;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.Connectivity;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.CustomJsonObjectRequest;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.DetailsOfGarageCarJobDBO;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.SafetyReport_DBO;

import static technource.autoreum.activities.GenerateSafetyReportGarageActivity.jid;
import static technource.autoreum.adapter.AdptQuoteDetailsPager.car_Id;
import static technource.autoreum.adapter.AdptSafetyReports.selectedarrayList;
import static technource.autoreum.adapter.AdptSafetyReports.selectedarrayList1;
import static technource.autoreum.adapter.AdptSafetyReports.selectedarrayList2;
import static technource.autoreum.adapter.AdptSafetyReports.selectedarrayList3;

/**
 * Created by technource on 12/2/18.
 */

public class FragInteriorAndExterior extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    ArrayList<String> DescArrayList;
    AdptSafetyReports adptSafetyReports;
    private TextView btnDetails;
    private TextView btnPrint;
    private TextView btnSave;
    String TAG = "FragInteriorAndExterior";
    LinearLayout ll_general_comment;
    EditText edtComment;
    String generalComm="";
    LoginDetail_DBO loginDetail_dbo;
    DetailsOfGarageCarJobDBO model;
    boolean isDetails=false;
    boolean isPrint=false;
    String download_file_url = "";
    String dest_file_path = "Autoreum";
    int downloadedSize = 0, totalsize;
    float per = 0;
    int WRITE_EXTERNAL_STORAGE_CODE = 101;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_safety_report, null, false);
        getViews(view);
        return view;
    }

    public void getViews(View view){
        stringArrayList = new ArrayList<>();
        DescArrayList = new ArrayList<>();
        recyclerView = (RecyclerView)view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        btnSave = (TextView) view.findViewById(R.id.btnSave);
        btnPrint = (TextView) view.findViewById(R.id.btnPrint);
        btnDetails = (TextView) view.findViewById(R.id.btnDetails);
        edtComment = (EditText) view.findViewById(R.id.edtComment);
        ll_general_comment = (LinearLayout) view.findViewById(R.id.ll_general_comment);
        ll_general_comment.setVisibility(View.VISIBLE);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(getActivity());

        btnPrint.setVisibility(View.VISIBLE);
        btnDetails.setVisibility(View.VISIBLE);

        stringArrayList.add("Panel, Paintwork & Body Fittings");
        stringArrayList.add("Windscreen & Other Glass");
        stringArrayList.add("Washes & Wipers");
        stringArrayList.add("All lighting & Horn Tone");
        stringArrayList.add("All Instruments & Accessories");
        stringArrayList.add("Trim & Interior Fittings");

        adptSafetyReports = new AdptSafetyReports(getActivity(),stringArrayList,"4");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptSafetyReports);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtComment.getText().length()>0){
                    generalComm = edtComment.getText().toString();
                }
                AppLog.Log("selectedarrayList",""+selectedarrayList3.size());
                HelperMethods.storeSafetyReportSharedPreferences(getActivity(), selectedarrayList3,"4");
                ArrayList<SafetyReport_DBO> reportDboArrayList = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList1 = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList2 = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList3 = new ArrayList<>();
                ArrayList<String> stringArrayList = new ArrayList<>();
                ArrayList<String> stringArrayList1 = new ArrayList<>();
                ArrayList<String> stringArrayList2 = new ArrayList<>();
                ArrayList<String> stringArrayList3 = new ArrayList<>();
                ArrayList<String> recommandationArray1 = new ArrayList<>();
                ArrayList<String> recommandationArray2 = new ArrayList<>();
                ArrayList<String> recommandationArray3 = new ArrayList<>();
                ArrayList<String> recommandationArray4 = new ArrayList<>();
                reportDboArrayList = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"1");
                reportDboArrayList1 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"2");
                reportDboArrayList2 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"3");
                //reportDboArrayList3 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"4");
                if (reportDboArrayList==null ){
                    reportDboArrayList = selectedarrayList;
                }
                if (reportDboArrayList1==null){
                    reportDboArrayList1 = selectedarrayList1;
                }
                if (reportDboArrayList2==null){
                    reportDboArrayList2 = selectedarrayList2;
                }
                for (int i=0;i<reportDboArrayList.size();i++){
                    stringArrayList.add(reportDboArrayList.get(i).getSelectedColor());
                    recommandationArray1.add(reportDboArrayList.get(i).getDescription());
                }
                for (int i=0;i<reportDboArrayList1.size();i++){
                    stringArrayList1.add(reportDboArrayList1.get(i).getSelectedColor());
                    recommandationArray2.add(reportDboArrayList1.get(i).getDescription());
                } for (int i=0;i<reportDboArrayList2.size();i++){
                    stringArrayList2.add(reportDboArrayList2.get(i).getSelectedColor());
                    recommandationArray3.add(reportDboArrayList2.get(i).getDescription());
                }for (int i=0;i<selectedarrayList3.size();i++){
                    stringArrayList3.add(selectedarrayList3.get(i).getSelectedColor());
                    recommandationArray4.add(selectedarrayList3.get(i).getDescription());
                }
                String array1 =  android.text.TextUtils.join(",", stringArrayList);
                String array2 =  android.text.TextUtils.join(",", stringArrayList1);
                String array3 =  android.text.TextUtils.join(",", stringArrayList2);
                String array4 =  android.text.TextUtils.join(",", stringArrayList3);
                String reco = array1 + ","+array2 +","+ array3 +","+ array4;

                String reco_dec1 = android.text.TextUtils.join(",", recommandationArray1);
                String reco_dec2 = android.text.TextUtils.join(",", recommandationArray2);
                String reco_dec3 = android.text.TextUtils.join(",", recommandationArray3);
                String reco_dec4 = android.text.TextUtils.join(",", recommandationArray4);

                if (reco_dec1.contains("null")){
                    reco_dec1 = reco_dec1.replace("null","");
                }
                if (reco_dec2.contains("null")){
                     reco_dec2 = reco_dec2.replace("null","");
                }
                if (reco_dec3.contains("null")){
                    reco_dec3 = reco_dec3.replace("null","");
                } if (reco_dec4.contains("null")){
                    reco_dec4 = reco_dec4.replace("null","");
                }


                String reco_dec = reco_dec1 + ","+reco_dec2 +","+ reco_dec3 +","+ reco_dec4;
                AppLog.Log("Reco--->",reco);
                AppLog.Log("reco_dec --->",reco_dec);
                isDetails=false;
                isPrint=false;
                if (Connectivity.isConnected(getActivity())){
                    webCallSafetyReport("SAFETY_REPORT_MAIL",jid,car_Id,reco,reco_dec,generalComm,"mail",isDetails,isPrint);
                }else {
                    ((GenerateSafetyReportGarageActivity)getActivity()).showAlertDialog(getString(R.string.no_internet));
                }

               // Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });

        btnDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isDetails=true;
                isPrint=false;
                if (Connectivity.isConnected(getActivity())){
                    webCallSafetyReport("SAFETY_REPORT_CLIENT_INFO",jid,car_Id,"","","","",isDetails,isPrint);
                }else {
                    ((GenerateSafetyReportGarageActivity)getActivity()).showAlertDialog(getString(R.string.no_internet));
                }

            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtComment.getText().length()>0){
                    generalComm = edtComment.getText().toString();
                }
                AppLog.Log("selectedarrayList",""+selectedarrayList3.size());
                HelperMethods.storeSafetyReportSharedPreferences(getActivity(), selectedarrayList3,"4");


                ArrayList<SafetyReport_DBO> reportDboArrayList = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList1 = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList2 = new ArrayList<>();
                ArrayList<SafetyReport_DBO> reportDboArrayList3 = new ArrayList<>();
                ArrayList<String> stringArrayList = new ArrayList<>();
                ArrayList<String> stringArrayList1 = new ArrayList<>();
                ArrayList<String> stringArrayList2 = new ArrayList<>();
                ArrayList<String> stringArrayList3 = new ArrayList<>();
                ArrayList<String> recommandationArray1 = new ArrayList<>();
                ArrayList<String> recommandationArray2 = new ArrayList<>();
                ArrayList<String> recommandationArray3 = new ArrayList<>();
                ArrayList<String> recommandationArray4 = new ArrayList<>();
                reportDboArrayList = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"1");
                reportDboArrayList1 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"2");
                reportDboArrayList2 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"3");
                //reportDboArrayList3 = HelperMethods.getSafetyrReportSharedPreferences(getActivity(),"4");
                if (reportDboArrayList==null ){
                    reportDboArrayList = selectedarrayList;
                }
                if (reportDboArrayList1==null){
                    reportDboArrayList1 = selectedarrayList1;
                }
                if (reportDboArrayList2==null){
                    reportDboArrayList2 = selectedarrayList2;
                }

                for (int i=0;i<reportDboArrayList.size();i++){
                    stringArrayList.add(reportDboArrayList.get(i).getSelectedColor());
                    recommandationArray1.add(reportDboArrayList.get(i).getDescription());
                }
                for (int i=0;i<reportDboArrayList1.size();i++){
                    stringArrayList1.add(reportDboArrayList1.get(i).getSelectedColor());
                    recommandationArray2.add(reportDboArrayList1.get(i).getDescription());
                } for (int i=0;i<reportDboArrayList2.size();i++){
                    stringArrayList2.add(reportDboArrayList2.get(i).getSelectedColor());
                    recommandationArray3.add(reportDboArrayList2.get(i).getDescription());
                }for (int i=0;i<selectedarrayList3.size();i++){
                    stringArrayList3.add(selectedarrayList3.get(i).getSelectedColor());
                    recommandationArray4.add(selectedarrayList3.get(i).getDescription());
                }
                String array1 =  android.text.TextUtils.join(",", stringArrayList);
                String array2 =  android.text.TextUtils.join(",", stringArrayList1);
                String array3 =  android.text.TextUtils.join(",", stringArrayList2);
                String array4 =  android.text.TextUtils.join(",", stringArrayList3);
                String reco = array1 + ","+array2 +","+ array3 +","+ array4;

                String reco_dec1 = android.text.TextUtils.join(",", recommandationArray1);
                String reco_dec2 = android.text.TextUtils.join(",", recommandationArray2);
                String reco_dec3 = android.text.TextUtils.join(",", recommandationArray3);
                String reco_dec4 = android.text.TextUtils.join(",", recommandationArray4);

                if (reco_dec1.contains("null")){
                    reco_dec1 = reco_dec1.replace("null","");
                }
                if (reco_dec2.contains("null")){
                    reco_dec2 = reco_dec2.replace("null","");
                }
                if (reco_dec3.contains("null")){
                    reco_dec3 = reco_dec3.replace("null","");
                } if (reco_dec4.contains("null")){
                    reco_dec4 = reco_dec4.replace("null","");
                }


                String reco_dec = reco_dec1 + ","+reco_dec2 +","+ reco_dec3 +","+ reco_dec4;
                AppLog.Log("Reco--->",reco);
                AppLog.Log("reco_dec --->",reco_dec);
                isDetails=false;
                isPrint=true;
                if(isReadStorageAllowed()){
                    //If permission is already having then showing the toast
                    //Toast.makeText(MainActivity.this,"You already have the permission",Toast.LENGTH_LONG).show();
                    //Existing the method with return
                    if (Connectivity.isConnected(getActivity())){
                        webCallSafetyReport("SAFETY_REPORT_MAIL",jid,car_Id,reco,reco_dec,generalComm," ",isDetails,isPrint);
                    }else {
                        ((GenerateSafetyReportGarageActivity)getActivity()).showAlertDialog(getString(R.string.no_internet));
                    }

                    return;
                }

                //If the app has not the permission then asking for the permission
                requestStoragePermission();


            }
        });

    }

    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED)
            return true;

        //If permission is not granted returning false
        return false;
    }

    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission

        ActivityCompat.requestPermissions(getActivity(),new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_EXTERNAL_STORAGE_CODE);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        for(String permission: permissions){
            if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permission)){
                //denied
                Log.e("denied", permission);
                requestStoragePermission();
            }else{
                if(ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED){
                    //allowed
                    Log.e("allowed", permission);

                } else{
                    //set to never ask again
                    //Log.e("set to never ask again", permission);
                    // requestStoragePermission();

                    //do something here.
                }
            }
        }
    }

    public void webCallSafetyReport(String action, String jid, String car_id, String reco, String reco_detail, String general_comments,String type, final boolean isDetail, final boolean print){
        ((GenerateSafetyReportGarageActivity)getActivity()).ShowLoadingDialog(true);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String url = WebServiceURLs.BASE_URL;
        AppLog.Log(TAG, "App URL : " + url);

        Map<String, String> params = new HashMap<String, String>();
        params.put(Constants.SERVICE_NAME, WebServiceURLs.JOB_ACTIONS_GARAGE);
        //params.put(Constants.JOB_ACTIONS.ACTION, "CLO");
        params.put(Constants.JOB_ACTIONS.ACTION, action);
        params.put(Constants.JOB_ACTIONS.JID, jid);
        params.put(Constants.SIGN_UP.USER_ID,loginDetail_dbo.getUserid() );
        params.put(Constants.JOB_ACTIONS.car_id, car_id);

        if (!isDetail){
            params.put(Constants.JOB_ACTIONS.RECO, reco);
            params.put(Constants.JOB_ACTIONS.RECO_DETAIL, reco_detail);
            params.put(Constants.JOB_ACTIONS.GENERAL_COMMENTS, general_comments);
            params.put(Constants.JOB_ACTIONS.TYPE, type);
        }


        AppLog.Log(TAG, "Params : " + params);
        CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
                url, getActivity(), new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        AppLog.Log("Response", "WebActionsgarage --> " + response);
                        try {
                            String status = response.getString(Constants.STATUS);

                            if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                                Toast.makeText(getActivity(), response.getString(Constants.MESSAGE), Toast.LENGTH_SHORT).show();
                                if (isDetail){
                                    JSONObject data = response.getJSONObject("data");
                                    JSONArray garageDetailsArray = data.getJSONArray("garage_array");
                                    JSONArray ClientDetailsArray = data.getJSONArray("client_array");
                                    JSONArray CarArray = data.getJSONArray("car_array");

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

                                    Intent intent = new Intent(getActivity(), DetailsOfGarageUserJobActivity.class);
                                    intent.putExtra("data", model);
                                    startActivity(intent);
                                    ((GenerateSafetyReportGarageActivity)getActivity()).activityTransition();
                                }
                                if (print){
                                    String pdfUrl = response.getString("url");
                                    download_file_url = WebServiceURLs.BASE_URL_IMAGE_PROFILE+pdfUrl;
                                    String urlStr = download_file_url;
                                    URL url = new URL(urlStr);
                                    URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(),
                                            url.getPort(), url.getPath(), url.getQuery(), url.getRef());
                                    url = uri.toURL();
                                    AppLog.Log("Encoded URL ", url.toString());
                                    download_file_url = url.toString();
                                    downloadAndOpenPDF();
                                }




                            } else {
                                ((GenerateSafetyReportGarageActivity)getActivity()).showAlertDialog(response.getString(Constants.MESSAGE));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ((GenerateSafetyReportGarageActivity)getActivity()).ShowLoadingDialog(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        ((GenerateSafetyReportGarageActivity)getActivity()).ShowLoadingDialog(false);
                    }
                });
        queue.add(jsonObjReq);
    }

    void downloadAndOpenPDF() {
        ((GenerateSafetyReportGarageActivity)getActivity()).ShowLoadingDialog(true);
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
                    ((GenerateSafetyReportGarageActivity)getActivity()).showAlertDialog(getString(R.string.no_app_found_for_open_pdf));
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
            byte[] buffer = new byte[1024*1024];
            int bufferLength = 0;

            while ((bufferLength = inputStream.read(buffer)) > 0) {
                fileOutput.write(buffer, 0, bufferLength);
                downloadedSize += bufferLength;
                per = ((float) downloadedSize / totalsize) *100;
            }

            ((GenerateSafetyReportGarageActivity)getActivity()).ShowLoadingDialog(false);
            // close the output stream when complete //
            fileOutput.close();

        } catch (final MalformedURLException e) {

        } catch (final IOException e) {

        } catch (final Exception e) {

        }
        return file;
    }

}
