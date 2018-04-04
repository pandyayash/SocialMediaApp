package technource.autoreum.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import technource.autoreum.R;
import technource.autoreum.activities.JobDetailsActivity;
import technource.autoreum.activities.JobsWaitingForReviewActivity;
import technource.autoreum.activities.ViewNewJobsFeedDetailActivity;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.NewPostedJob_DBO;

/**
 * ,l
 * Created by technource on 12/12/17.
 */

public class AdptNewPostedJobs extends RecyclerView.Adapter<AdptNewPostedJobs.ViewHolder> {
    ArrayList<NewPostedJob_DBO> postedJobArrayList;
    Context context;
    LoginDetail_DBO loginDetail_dbo;

    public AdptNewPostedJobs(ArrayList<NewPostedJob_DBO> postedJobArrayList, Context context) {
        this.postedJobArrayList = postedJobArrayList;
        this.context = context;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
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
        char firstCharacter = 0;
        if (newPostedJob_dbo.isFromJobsFeed()) {
            firstCharacter = newPostedJob_dbo.getLname().charAt(0);
        }
        if (newPostedJob_dbo.getJobDescription().equalsIgnoreCase("")) {
            holder.textJobDescription.setVisibility(View.GONE);
        } else {
            holder.textJobDescription.setVisibility(View.VISIBLE);
            holder.textJobDescription.setText(newPostedJob_dbo.getJobDescription());
        }
        holder.textPrice.setText(newPostedJob_dbo.getPrice());
        holder.textQuotesCount.setText("Total Quotes : " + newPostedJob_dbo.getTotalQuotes());
        if (newPostedJob_dbo.isFromJobsFeed()) {
            holder.textCarModel.setText(newPostedJob_dbo.getFname() + " " + firstCharacter + ". " + newPostedJob_dbo.getSuburb() + ". " + newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel());
        } else if (newPostedJob_dbo.getBadge().equalsIgnoreCase("")) {
            holder.textCarModel.setText(newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel());
        } else {
            holder.textCarModel.setText(newPostedJob_dbo.getMake() + ", " + newPostedJob_dbo.getCarModel() + ", " + newPostedJob_dbo.getBadge());
        }

        holder.textDate.setText(holder.parseDateToddMMyyyy(newPostedJob_dbo.getDate()));

        if (!newPostedJob_dbo.getDistnace().equalsIgnoreCase("")) {
            holder.textDistance.setVisibility(View.VISIBLE);
            holder.textDistance.setText("Distance :" + newPostedJob_dbo.getDistnace() + " Km");
        }


        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newPostedJob_dbo.isFromJobsFeed() && loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                    Intent intent = new Intent(context, ViewNewJobsFeedDetailActivity.class);
                    intent.putExtra("job_id", newPostedJob_dbo.getJobID());
                    intent.putExtra("cat_id", newPostedJob_dbo.getCategory_id());
                    // intent.putExtra("status","posted");
                    intent.putExtra("status", newPostedJob_dbo.getJob_status());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else if (!newPostedJob_dbo.isFromJobsFeed() && loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")){
                    Intent intent = new Intent(context, ViewNewJobsFeedDetailActivity.class);
                    intent.putExtra("job_id", newPostedJob_dbo.getJobID());
                    intent.putExtra("cat_id", newPostedJob_dbo.getCategory_id());
                    // intent.putExtra("status","posted");
                    intent.putExtra("status", newPostedJob_dbo.getJob_status());
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }
                else if (newPostedJob_dbo.isFromJobsFeed() && loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("job_id", newPostedJob_dbo.getJobID());
                    intent.putExtra("cat_id", newPostedJob_dbo.getCategory_id());
                    // intent.putExtra("status","posted");
                    intent.putExtra("status", newPostedJob_dbo.getJob_status());
                    intent.putExtra("isFromNewJobFeed", true);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }else if (!newPostedJob_dbo.isFromJobsFeed() && loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")){
                    Intent intent = new Intent(context, JobDetailsActivity.class);
                    intent.putExtra("job_id", newPostedJob_dbo.getJobID());
                    intent.putExtra("cat_id", newPostedJob_dbo.getCategory_id());
                    // intent.putExtra("status","posted");
                    intent.putExtra("status", newPostedJob_dbo.getJob_status());
                    intent.putExtra("isFromNewJobFeed", false);
                    context.startActivity(intent);
                    ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                }

            }
        });
        if (newPostedJob_dbo.isFromCompleted()) {
            if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
                if (!newPostedJob_dbo.getGrating().equalsIgnoreCase("")) {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.txtAddReview.setVisibility(View.GONE);
                    holder.ratingBar.setRating(Float.parseFloat(newPostedJob_dbo.getGrating()));
                    //holder.ratingBar.setRating(Integer.parseInt(newPostedJob_dbo.getUrating()));
                    //holder.ratingBar.setRating(Integer.parseInt(newPostedJob_dbo.getUrating()));
                } else {
                    holder.txtAddReview.setVisibility(View.VISIBLE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                }
            } else {
                if (!newPostedJob_dbo.getUrating().equalsIgnoreCase("")) {
                    holder.ratingBar.setVisibility(View.VISIBLE);
                    holder.txtAddReview.setVisibility(View.GONE);
                    holder.ratingBar.setRating(Float.parseFloat(newPostedJob_dbo.getUrating()));
                } else {
                    holder.txtAddReview.setVisibility(View.VISIBLE);
                    holder.ratingBar.setVisibility(View.VISIBLE);
                }
            }

        }

        holder.txtAddReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, JobsWaitingForReviewActivity.class);
                intent.putExtra("data", postedJobArrayList);
                context.startActivity(intent);


            }
        });


    }

    @Override
    public int getItemCount() {
        return postedJobArrayList.size();

    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAddReview, textJobTitle, textDate, textJobDescription, textCarModel, textQuotesCount, textPrice, textDistance;
        LinearLayout linear;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
            txtAddReview = (TextView) itemView.findViewById(R.id.txtAddReview);
            textJobTitle = (TextView) itemView.findViewById(R.id.textJobTitle);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textJobDescription = (TextView) itemView.findViewById(R.id.textJobDescription);
            textDistance = (TextView) itemView.findViewById(R.id.textDistance);
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
