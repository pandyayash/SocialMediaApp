package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.model.FacilitiesDBo;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

public class DisplayGarageOwnerFacilitiesActivity extends BaseActivity {

    LinearLayout ll_back;
    ArrayList<FacilitiesDBo> facilitiesDBos;
    StringBuffer selsected_facilities;
    String facility;
    Context appContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    TextView cont;
    LoginDetail_DBO loginDetail_dbo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_facilities);
        setHeader("Facilities");
        getviews();
        setonclick();
        Intent intent = getIntent();
        if (intent != null) {
            facilitiesDBos = intent.getParcelableArrayListExtra("data");
            setAdapter();
            cont.setVisibility(View.GONE);
        }
    }

    private void getviews() {
        ll_back = findViewById(R.id.ll_back);
        facilitiesDBos = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        appContext = this;
        cont = findViewById(R.id.cont);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this);
    }

    private void setonclick() {
        ll_back.setOnClickListener(this);
        cont.setOnClickListener(this);
    }
    
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    private void setAdapter() {
        adapter = new Recycleradapter(facilitiesDBos, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public class Recycleradapter extends RecyclerView.Adapter<Recycleradapter.Recycleviewholder> {


        Context context;
        ArrayList<FacilitiesDBo> services;

        public Recycleradapter(ArrayList<FacilitiesDBo> services, Context context) {

            this.context = context;
            this.services = services;
        }

        @Override
        public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services, parent, false);
            Recycleviewholder recycleviewholder = new Recycleviewholder(view);
            return recycleviewholder;
        }

        @Override
        public void onBindViewHolder(final Recycleviewholder holder, final int position) {

            holder.services_text.setText(facilitiesDBos.get(position).getFacilites());
            holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
            holder.services_text.setTextColor(getResources().getColor(R.color.white));
        }

        @Override
        public int getItemCount() {
            return facilitiesDBos.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll;

            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = itemView.findViewById(R.id.autoText2);
                service_ll = itemView.findViewById(R.id.service_ll);
            }

            //if you implement onclick here you must have to use getposition() instead of making variable position global see documentation
        }
    }


    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

}
