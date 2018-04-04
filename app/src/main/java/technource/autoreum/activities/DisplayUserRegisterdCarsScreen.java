package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.RegisteredCarDBO;

/**
 * Created by technource on 20/9/17.
 */

public class DisplayUserRegisterdCarsScreen extends BaseActivity {
    LoginDetail_DBO loginDetail_dbo;
    private RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ArrayList<RegisteredCarDBO> registeredCarDBOs;
    Context appContext;
    RecycleradapterforRegisteredcar.Recycleviewholder holder;
    JSONObject jsonObject;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.display_activity_registered_cars);

        setHeader("Registered Cars");
        getviews();
        Intent intent = getIntent();
        if (intent != null) {
            registeredCarDBOs = intent.getParcelableArrayListExtra("data");
            setAdapter();
        }
        setOnClickListener();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void getviews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        appContext = this;

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
    }

    public void setOnClickListener() {
        ll_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int id = v.getId();
        switch (id) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    private void setAdapter() {

        adapter = new RecycleradapterforRegisteredcar();
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
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
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.car_demo)
                    .error(R.drawable.car_demo)
                    .skipMemoryCache(false)
                    .into(holder.iv_image);
            holder.viewhistory.setVisibility(View.GONE);
            holder.edit.setVisibility(View.GONE);
            holder.postjob.setVisibility(View.GONE);
            holder.askthecrowd.setVisibility(View.GONE);
            holder.iv_upload.setVisibility(View.GONE);

        }


        @Override
        public int getItemCount() {
            return registeredCarDBOs.size();
        }


        public class Recycleviewholder extends RecyclerView.ViewHolder {

            TextView carname, registration_number, viewhistory, edit, postjob, askthecrowd;
            ImageView iv_image, iv_upload;
            private View view;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                carname = (TextView) itemView.findViewById(R.id.carname);
                registration_number = (TextView) itemView.findViewById(R.id.registration_number);
                viewhistory = (TextView) itemView.findViewById(R.id.viewhistory);
                edit = (TextView) itemView.findViewById(R.id.edit);
                postjob = (TextView) itemView.findViewById(R.id.postjob);
                askthecrowd = (TextView) itemView.findViewById(R.id.askthecrowd);
                iv_image = (ImageView) itemView.findViewById(R.id.iv_image);
                iv_upload = (ImageView) itemView.findViewById(R.id.iv_upload);
            }

        }
    }
}
