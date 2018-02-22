package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import technource.greasecrowd.R;
import technource.greasecrowd.adapter.AdptAwardJobsCarOwner;
import technource.greasecrowd.adapter.AdptGarageServiceType;
import technource.greasecrowd.adapter.ViewPagerAdapterCarVideo;
import technource.greasecrowd.adapter.ViewPagerAdapterImage;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.interfaces.OnItemClickListener;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.CarImageDBO;
import technource.greasecrowd.model.CarVideosDbo;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.Model_GarageServices;

public class ViewSelectSeriviceTypeActivity extends BaseActivity {

    String TAG = "ViewSelectSeriviceTypeActivity";
    Context appContext;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<Model_GarageServices> garageServicesArrayList;
    AdptGarageServiceType adptGarageServiceType;
    RecyclerView recyclerView;
    LinearLayout ll_continue,ll_back_button,ll_cancel,ll_back;
    String strServiceId="",strServiceName="";
    boolean isUpdate=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_select_serivice_type);
        Intent intent = getIntent();
        if (intent!=null){
            isUpdate = intent.getBooleanExtra("isUpdate",isUpdate);
        }
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(ViewSelectSeriviceTypeActivity.this);
        setlistenrforfooter();
        getViews();
        setClickListeners();




   }

   public void getViews(){
       appContext = ViewSelectSeriviceTypeActivity.this;
       garageServicesArrayList = new ArrayList<>();
       jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(ViewSelectSeriviceTypeActivity.this);
       loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
       garageServicesArrayList = jobDetail_dbo.getGarageServicesArrayList();
       recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
       ll_continue = (LinearLayout) findViewById(R.id.ll_continue);
       ll_back_button = (LinearLayout) findViewById(R.id.ll_back_button);
       ll_back = (LinearLayout) findViewById(R.id.ll_back);
       ll_cancel = (LinearLayout) findViewById(R.id.ll_cancel);
       setData();

   }

   public void setClickListeners(){
       ll_continue.setOnClickListener(this);
       ll_back_button.setOnClickListener(this);
       ll_cancel.setOnClickListener(this);
       ll_back.setOnClickListener(this);
   }

    @Override
    protected void onResume() {
        super.onResume();
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(ViewSelectSeriviceTypeActivity.this);
        setlistenrforfooter();
    }

    public void setData() {
        adptGarageServiceType = new AdptGarageServiceType(garageServicesArrayList, appContext, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                adptGarageServiceType.selectedItem();
                strServiceId = garageServicesArrayList.get(position).getService_id();
                strServiceName = garageServicesArrayList.get(position).getName();
                if (garageServicesArrayList.get(position).isSelected()){
                    Toast.makeText(appContext, ""+garageServicesArrayList.get(position).isSelected(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptGarageServiceType);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);
        if (view==ll_continue){
            if (!strServiceId.equalsIgnoreCase("")){
                Intent intent = new Intent(appContext,SelectSubServicesGarageActivity.class);
                intent.putExtra("strServiceId",strServiceId);
                intent.putExtra("strServiceName",strServiceName);
                intent.putExtra("isUpdate",isUpdate);
                startActivity(intent);
                activityTransition();
            }else {
                showAlertDialog("Please select service");
            }

        }
        if (view==ll_back_button || view==ll_cancel || view==ll_back){
            adptGarageServiceType.sSelected = -1;
           finish();
           onBackPressed();
           activityTransition();
        }
    }
}
