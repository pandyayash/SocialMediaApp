package technource.autoreum.activities;

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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import technource.autoreum.R;
import technource.autoreum.helper.AppLog;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.NewPostedJob_DBO;


public class JobsWaitingForReviewActivity extends BaseActivity {

    RecyclerView recyclerView;
    ArrayList<NewPostedJob_DBO> newPostedJob_dboArrayList;
    ArrayList<NewPostedJob_DBO> tempNewPostedJob_dboArrayList;
    JobsAdapter adapter;
    LinearLayout ll_back;
    TextView noData;
    LoginDetail_DBO loginDetail_dbo;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobs_waiting_for_review);

        setHeader("Jobs Awaiting Review");
        getviews();
        setONclickListener();

        Intent intent = getIntent();
        if (intent != null) {
            newPostedJob_dboArrayList = new ArrayList<>();
            tempNewPostedJob_dboArrayList = intent.getParcelableArrayListExtra("data");

            AppLog.Log("before removing ", "" + newPostedJob_dboArrayList.size());
            for (int i = 0; i < tempNewPostedJob_dboArrayList.size(); i++) {
                if (tempNewPostedJob_dboArrayList.get(i).isFromCompleted()) {

                    if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                        if (tempNewPostedJob_dboArrayList.get(i).getGrating().equalsIgnoreCase("")) {
                            newPostedJob_dboArrayList.add(tempNewPostedJob_dboArrayList.get(i));
                        }
                    } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                        if (tempNewPostedJob_dboArrayList.get(i).getUrating().equalsIgnoreCase("")) {
                            newPostedJob_dboArrayList.add(tempNewPostedJob_dboArrayList.get(i));
                        }
                    }
                }
            }
        }
        AppLog.Log("After removing ", "" + newPostedJob_dboArrayList.size());
        setAdapter();
    }

    private void getviews() {
        appContext = this;
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setNestedScrollingEnabled(false);
        noData = (TextView) findViewById(R.id.noData);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 111 && resultCode == RESULT_OK) {
            int pos = data.getIntExtra("pos", -1);
            if (pos != -1) {
                newPostedJob_dboArrayList.remove(pos);
                adapter.notifyDataSetChanged();

                if (!(newPostedJob_dboArrayList.size() > 0)) {

                    noData.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
            }
        }
        if (requestCode == 111 && resultCode == RESULT_CANCELED) {
        }
    }

    public void setONclickListener() {
        ll_back.setOnClickListener(this);
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

    private void setAdapter() {
        adapter = new JobsAdapter(appContext, newPostedJob_dboArrayList);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    public class JobsAdapter extends RecyclerView.Adapter<JobsAdapter.ViewHolder> {
        Context context;
        ArrayList<NewPostedJob_DBO> newPostedJob_dboArrayList;


        public JobsAdapter(Context context, ArrayList<NewPostedJob_DBO> newPostedJob_dboArrayList) {
            this.context = context;
            this.newPostedJob_dboArrayList = newPostedJob_dboArrayList;
        }

        @Override
        public JobsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_add_review_completejobs, parent, false);

            return new JobsAdapter.ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(JobsAdapter.ViewHolder holder, final int position) {

            final NewPostedJob_DBO newPostedJob_dbo = newPostedJob_dboArrayList.get(position);

            String modelName = "";

            modelName = newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel();

            AppLog.Log("Model Name", "" + modelName);
            holder.textJobTitle.setText(newPostedJob_dbo.getJobTitle());

            if (newPostedJob_dbo.getBadge().equalsIgnoreCase("")) {
                holder.textCarModel.setText(newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel());
            } else {
                if (modelName.contains("null")) {
                    holder.textCarModel.setText(modelName.replace("null", "").replace(",", ""));
                } else {
                    holder.textCarModel.setText(modelName.replace("null", ""));
                }

            }
            holder.textDate.setText(holder.parseDateToddMMyyyy(newPostedJob_dbo.getDate()));

            holder.txtToUser.setText("To User : " + newPostedJob_dbo.getFname() + " " + newPostedJob_dbo.getLname());

            holder.textPrice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(appContext, AddAReviewScreenActivity.class);
                    intent.putExtra("jid", newPostedJob_dbo.getcJobId());
                    intent.putExtra("pos", position);
                    ((JobsWaitingForReviewActivity) appContext).startActivityForResult(intent, 111);
                }
            });

        }

        @Override
        public int getItemCount() {
            return newPostedJob_dboArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            LinearLayout linear;
            TextView textJobTitle;
            TextView textDate;
            TextView txtToUser;
            TextView textCarModel;
            TextView textPrice;

            public ViewHolder(View itemView) {
                super(itemView);
                linear = (LinearLayout) itemView.findViewById(R.id.linear);
                textJobTitle = (TextView) itemView.findViewById(R.id.textJobTitle);
                textDate = (TextView) itemView.findViewById(R.id.textDate);
                txtToUser = (TextView) itemView.findViewById(R.id.txtToUser);
                textCarModel = (TextView) itemView.findViewById(R.id.textCarModel);
                textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            }

            public String parseDateToddMMyyyy(String time) {
                String inputPattern = "yyyy-MM-dd HH:mm:ss";
                String outputPattern = "MMM dd, yyyy";
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


    }


}
