package technource.greasecrowd.adapter;

import android.content.Context;
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
import technource.greasecrowd.model.NewPostedJob_DBO;

/**
 * Created by technource on 19/2/18.
 */

public class AdptCompleteJobByGarage extends RecyclerView.Adapter<AdptCompleteJobByGarage.ViewHolder> {
    ArrayList<NewPostedJob_DBO> postedJobArrayList;
    Context context;

    public AdptCompleteJobByGarage(ArrayList<NewPostedJob_DBO> postedJobArrayList, Context context) {
        this.postedJobArrayList = postedJobArrayList;
        this.context = context;
    }

    @Override
    public AdptCompleteJobByGarage.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_completed_by_garage, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AdptCompleteJobByGarage.ViewHolder holder, int position) {

        final NewPostedJob_DBO newPostedJob_dbo = postedJobArrayList.get(position);
        holder.textJobTitle.setText(newPostedJob_dbo.getJobTitle());
        if (newPostedJob_dbo.getJobDescription().equalsIgnoreCase("")) {
            holder.textJobDescription.setVisibility(View.GONE);
        } else {
            holder.textJobDescription.setVisibility(View.VISIBLE);
            holder.textJobDescription.setText(newPostedJob_dbo.getJobDescription());
        }
        holder.textDate.setText(holder.parseDateToddMMyyyy(newPostedJob_dbo.getDate()));
        holder.textDistance.setText("Distance :" + newPostedJob_dbo.getDistnace() + " Km");
        holder.textType.setText("Type: " + newPostedJob_dbo.getCatName());

    }

    @Override
    public int getItemCount() {
        return postedJobArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtAddReview, textJobTitle, textDate, textJobDescription, textType,textDistance;
        LinearLayout linear;
        public ViewHolder(View itemView) {
            super(itemView);
            txtAddReview = (TextView) itemView.findViewById(R.id.txtAddReview);
            textJobTitle = (TextView) itemView.findViewById(R.id.textJobTitle);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textJobDescription = (TextView) itemView.findViewById(R.id.textJobDescription);
            textDistance = (TextView) itemView.findViewById(R.id.textDistance);
            textType = (TextView) itemView.findViewById(R.id.textType);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
        }
        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "MMM dd, yyyy, HH:mm";
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
