package technource.greasecrowd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

import technource.greasecrowd.R;
import technource.greasecrowd.activities.JobDetailsActivity;
import technource.greasecrowd.model.NewPostedJob_DBO;

/**
 * Created by technource on 12/12/17.
 */

public class AdptQuotedJobs extends RecyclerView.Adapter<AdptQuotedJobs.ViewHolder> {
    ArrayList<NewPostedJob_DBO> postedJobArrayList;
    Context context;

    public AdptQuotedJobs(ArrayList<NewPostedJob_DBO> postedJobArrayList, Context context) {
        this.postedJobArrayList = postedJobArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_new_posted_jobs, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NewPostedJob_DBO newPostedJob_dbo = postedJobArrayList.get(position);
        holder.textJobTitle.setText(newPostedJob_dbo.getJobTitle());

        if (newPostedJob_dbo.getJobDescription().equalsIgnoreCase("")) {
            holder.textJobDescription.setVisibility(View.GONE);
        } else {
            holder.textJobDescription.setVisibility(View.VISIBLE);
            holder.textJobDescription.setText(newPostedJob_dbo.getJobDescription());
        }
        holder.textPrice.setText(newPostedJob_dbo.getPrice());
        holder.textQuotesCount.setText("Total Quotes : " + newPostedJob_dbo.getTotalQuotes());
        holder.textCarModel.setText(newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel() + ", " + newPostedJob_dbo.getBadge());
        holder.textDate.setText(holder.parseDateToddMMyyyy(newPostedJob_dbo.getDate()));
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobDetailsActivity.class);
                intent.putExtra("job_id", newPostedJob_dbo.getJobID());
                intent.putExtra("cat_id", newPostedJob_dbo.getCategory_id());
                intent.putExtra("status", newPostedJob_dbo.getJob_status());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return postedJobArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJobTitle, textDate, textJobDescription, textCarModel, textQuotesCount, textPrice;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            textJobTitle = (TextView) itemView.findViewById(R.id.textJobTitle);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textJobDescription = (TextView) itemView.findViewById(R.id.textJobDescription);
            textCarModel = (TextView) itemView.findViewById(R.id.textCarModel);
            textQuotesCount = (TextView) itemView.findViewById(R.id.textQuotesCount);
            textPrice = (TextView) itemView.findViewById(R.id.textPrice);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
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
