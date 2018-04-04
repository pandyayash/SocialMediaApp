package technource.autoreum.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.model.ServicesDBO;

public class DisplayGarageOwnerServicesActivity extends BaseActivity {

    LinearLayout ll_back;

    TextView cont;
    StringBuffer selsected_services;
    String selected = "";
    Context appContext;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    View v;
    ArrayList<ServicesDBO> servicesDBOs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_services);
        setHeader("Services");
        getviews();
        Intent intent = getIntent();
        if (intent != null) {
            servicesDBOs = intent.getParcelableArrayListExtra("data");
            setAdapter();
            cont.setVisibility(View.GONE);
        }
        setonclick();
    }

    private void getviews() {
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        appContext = this;
        servicesDBOs = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        cont = (TextView) findViewById(R.id.cont);
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
        adapter = new Recycleradpter(servicesDBOs, appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    public class Recycleradpter extends RecyclerView.Adapter<Recycleradpter.Recycleviewholder> {

        ArrayList<ServicesDBO> services;
        Context context;

        public Recycleradpter(ArrayList<ServicesDBO> services, Context context) {

            this.context = context;
            this.services = services;
        }


        @Override
        public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services, parent, false);
            Recycleviewholder recycleviewholder = new Recycleviewholder(view);


            return recycleviewholder;
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final Recycleviewholder holder, final int position) {

            holder.services_text.setText(services.get(position).getService());

            holder.service_ll.setBackgroundColor(getResources().getColor(R.color.edittext_bg));
            holder.services_text.setTextColor(getResources().getColor(R.color.white));

        }


        @Override
        public int getItemCount() {
            return services.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll;

            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = (TextView) itemView.findViewById(R.id.autoText2);
                service_ll = (LinearLayout) itemView.findViewById(R.id.service_ll);
            }
        }
    }

}
