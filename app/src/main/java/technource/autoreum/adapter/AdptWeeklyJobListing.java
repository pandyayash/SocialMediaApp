package technource.autoreum.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import technource.autoreum.R;
import technource.autoreum.activities.JobDetailsActivity;
import technource.autoreum.activities.WeeklyDetailsActivity;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.DetailsOfGarageCarJobDBO;
import technource.autoreum.model.LoginDetail_DBO;

/**
 * Created by technource on 5/1/18.
 */

public class AdptWeeklyJobListing extends RecyclerView.Adapter<AdptWeeklyJobListing.ViewHolder> {

    Context context;
    ArrayList<DetailsOfGarageCarJobDBO> detailsOfGarageCarJobDBOArrayList;
    LoginDetail_DBO loginDetail_dbo;


    public AdptWeeklyJobListing(Context context, ArrayList<DetailsOfGarageCarJobDBO> detailsOfGarageCarJobDBOArrayList) {
        this.context = context;
        this.detailsOfGarageCarJobDBOArrayList = detailsOfGarageCarJobDBOArrayList;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_weekly_list_completedjobs, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final DetailsOfGarageCarJobDBO mailbox = detailsOfGarageCarJobDBOArrayList.get(position);


        holder.txtNotification.setText("JOB Title :" + mailbox.getJobTitle());


        holder.txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JobDetailsActivity.class);
                intent.putExtra("job_id", mailbox.getJuId());
                intent.putExtra("cat_id", mailbox.getCatId());
                context.startActivity(intent);
                ((WeeklyDetailsActivity) context).activityTransition();
            }
        });

        holder.txtDate.setText(holder.parseDateToddMMyyyy1(mailbox.getDate()));
    }

    @Override
    public int getItemCount() {
        return detailsOfGarageCarJobDBOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_close;
        TextView txtNotification, txtDate;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_close = (ImageView) itemView.findViewById(R.id.iv_close);
            txtNotification = (TextView) itemView.findViewById(R.id.txtNotification);
            txtDate = (TextView) itemView.findViewById(R.id.txtDate);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
        }

        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "HH:mm";
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


            try {
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(str);
                return (_12HourSDF.format(_24HourDt));
            } catch (Exception e) {
                e.printStackTrace();
                return str;
            }


        }

        public String parseDateToddMMyyyy1(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "yyyy-MM-dd";
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
