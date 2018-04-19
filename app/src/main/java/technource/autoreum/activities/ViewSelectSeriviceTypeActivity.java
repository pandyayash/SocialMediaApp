package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.adapter.AdptGarageServiceType;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.interfaces.OnItemClickListener;
import technource.autoreum.model.JobDetail_DBO;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Model_GarageServices;

public class ViewSelectSeriviceTypeActivity extends BaseActivity {

    String TAG = "ViewSelectSeriviceTypeActivity";
    Context appContext;
    JobDetail_DBO jobDetail_dbo;
    LoginDetail_DBO loginDetail_dbo;
    ArrayList<Model_GarageServices> garageServicesArrayList;
    AdptGarageServiceType adptGarageServiceType;
    RecyclerView recyclerView;
    LinearLayout ll_continue, ll_back_button, ll_cancel, ll_back;
    String strServiceId = "", strServiceName = "";
    boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_select_serivice_type);
        setHeader("Quote A Job");
        setfooter("garageowner");
        setHomeFooterGarage(ViewSelectSeriviceTypeActivity.this);
        setlistenrforfooter();
        getViews();
        setClickListeners();
        Intent intent = getIntent();
        if (intent != null) {
            isUpdate = intent.getBooleanExtra("isUpdate", isUpdate);
            if (isUpdate) {
                for (int i = 0; i < UpdateQuoteGarageActivity.awardJobArrayList.size(); i++) {
                    strServiceId = UpdateQuoteGarageActivity.awardJobArrayList.get(i).getService_id();
                }
                for (int i = 0; i < garageServicesArrayList.size(); i++) {
                    if (strServiceId.equalsIgnoreCase(garageServicesArrayList.get(i).getService_id())) {
                        garageServicesArrayList.get(i).setSelected(true);
                    } else {
                        garageServicesArrayList.get(i).setSelected(false);
                    }
                }
            }
        }


    }

    public void getViews() {
        appContext = ViewSelectSeriviceTypeActivity.this;
        garageServicesArrayList = new ArrayList<>();
        jobDetail_dbo = HelperMethods.getjobDetailsSharedPreferences(ViewSelectSeriviceTypeActivity.this);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        garageServicesArrayList = jobDetail_dbo.getGarageServicesArrayList();
        recyclerView = findViewById(R.id.recyclerView);
        ll_continue = findViewById(R.id.ll_continue);
        ll_back_button = findViewById(R.id.ll_back_button);
        ll_back = findViewById(R.id.ll_back);
        ll_cancel = findViewById(R.id.ll_cancel);
        setData();
    }

    public void setClickListeners() {
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
        adptGarageServiceType = new AdptGarageServiceType(garageServicesArrayList, appContext, isUpdate, strServiceId, new OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (view != null) {
                    adptGarageServiceType.selectedItem();
                }
                strServiceId = garageServicesArrayList.get(position).getService_id();
                strServiceName = garageServicesArrayList.get(position).getName();
                //Toast.makeText(appContext, "yo", Toast.LENGTH_SHORT).show();
               /* if (garageServicesArrayList.get(position).isSelected()){

                    strServiceId = garageServicesArrayList.get(position).getService_id();

                    //Toast.makeText(appContext, ""+garageServicesArrayList.get(position).isSelected(), Toast.LENGTH_SHORT).show();
                }*/
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
        if (view == ll_continue) {
            if (!strServiceId.equalsIgnoreCase("")) {
                Intent intent = new Intent(appContext, SelectSubServicesGarageActivity.class);
                intent.putExtra("strServiceId", strServiceId);
                intent.putExtra("strServiceName", strServiceName);
                intent.putExtra("isUpdate", isUpdate);
                startActivity(intent);
                activityTransition();
            } else {
                showAlertDialog("Please select service");
            }

        }
        if (view == ll_back_button || view == ll_cancel || view == ll_back) {
            AdptGarageServiceType.sSelected = -1;
            finish();
            onBackPressed();
            activityTransition();
        }
    }

    /*public class AdptGarageServiceType extends RecyclerView.Adapter<AdptGarageServiceType.ViewHolder> {
        Context context;

        public AdptGarageServiceType(ArrayList<Model_GarageServices> garageServicesArrayList, Context context, boolean isUpdate, String strServiceId, OnItemClickListener itemClickListener) {
            this.context = context;
        }

        @Override
        public AdptGarageServiceType.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_row_services_with_info, parent, false);

            return new AdptGarageServiceType.ViewHolder(itemView);
        }

        @SuppressLint("NewApi")
        @Override
        public void onBindViewHolder(final AdptGarageServiceType.ViewHolder holder, final int position) {

            holder.services_text.setText(garageServicesArrayList.get(position).getName());
            holder.ll_info.setVisibility(View.GONE);

            if (garageServicesArrayList.get(position).isSelected()) {
                holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
                holder.services_text.setTextColor(context.getResources().getColor(R.color.white));

            } else {
                holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
                holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));

            }

            holder.service_ll.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    garageServicesArrayList.get(position).setSelected(!garageServicesArrayList.get(position).isSelected());
                    if (garageServicesArrayList.get(position).isSelected()) {
                        isUpdate = false;
                        holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
                        holder.services_text.setTextColor(context.getResources().getColor(R.color.white));
                        strServiceId = garageServicesArrayList.get(position).getService_id();
                        strServiceName = garageServicesArrayList.get(position).getName();
                        for (int i = 0; i < garageServicesArrayList.size(); i++) {
                            if ((position == i)) {
                                garageServicesArrayList.get(i).setSelected(true);
                            } else {
                                garageServicesArrayList.get(i).setSelected(false);
                            }
                        }

                    } else {
                        holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
                        holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));
                    }
                    setData();
                }
            });
        }

        public void selectedItem() {
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return garageServicesArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout service_ll, ll_info;

            TextView services_text;

            public ViewHolder(View itemView) {
                super(itemView);
                services_text = (TextView) itemView.findViewById(R.id.autoText2);
                service_ll = (LinearLayout) itemView.findViewById(R.id.service_ll);
                ll_info = (LinearLayout) itemView.findViewById(R.id.ll_info);
            }
        }
    }*/

}
