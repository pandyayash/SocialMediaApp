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
import technource.greasecrowd.activities.MyCrowdDetailsActivity;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.MyCrowd_DBO;

/**
 * Created by technource on 26/12/17.
 */

public class AdptMyCrowdList extends RecyclerView.Adapter<AdptMyCrowdList.ViewHolder> {
    ArrayList<MyCrowd_DBO> crowdArrayList;
    Context context;
    LoginDetail_DBO loginDetail_dbo;

    public AdptMyCrowdList(ArrayList<MyCrowd_DBO> crowdArrayList, Context context) {
        this.crowdArrayList = crowdArrayList;
        this.context = context;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_crowd_list, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_my_crowd_list_crowd, parent, false);
        }


        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final MyCrowd_DBO myCrowdDbo = crowdArrayList.get(position);
        holder.textJobTitle.setText(myCrowdDbo.getJob_title());
        holder.textJobDescription.setText(myCrowdDbo.getProblem_description());
        holder.textCarModel.setText(myCrowdDbo.getMake() + ", " + myCrowdDbo.getModel() + ", " + myCrowdDbo.getBadge());
        holder.textDate.setText(holder.parseDateToddMMyyyy(myCrowdDbo.getJob_posted_date()));
        holder.textCategory.setText(myCrowdDbo.getCatname());

        holder.txtName.setText(myCrowdDbo.getFname() + " " + myCrowdDbo.getLname());
        holder.txtAddress.setText(myCrowdDbo.getSuburb());


        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            holder.textResponses.setText("Responses : " + myCrowdDbo.getRespinses());
        } else {
            holder.textResponses.setText(myCrowdDbo.getRespinses());
        }

        holder.textDistance.setText("Distance : " + myCrowdDbo.getDistance() + " KM");
        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, MyCrowdDetailsActivity.class);
                intent.putExtra("job_id", myCrowdDbo.getJu_id());
                intent.putExtra("cat_id", myCrowdDbo.getCategory_id());
                intent.putExtra("responses", myCrowdDbo.getRespinses());
                context.startActivity(intent);
                ((Activity) context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        return crowdArrayList.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textJobTitle, textDate, textJobDescription, textCarModel, textDistance, textResponses, textCategory, txtName, txtAddress;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            textJobTitle = (TextView) itemView.findViewById(R.id.textJobTitle);
            textDate = (TextView) itemView.findViewById(R.id.textDate);
            textJobDescription = (TextView) itemView.findViewById(R.id.textJobDescription);
            textCarModel = (TextView) itemView.findViewById(R.id.textCarModel);
            textDistance = (TextView) itemView.findViewById(R.id.textDistance);
            textResponses = (TextView) itemView.findViewById(R.id.textResponses);
            textCategory = (TextView) itemView.findViewById(R.id.textCategory);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtName = (TextView) itemView.findViewById(R.id.txtName);
            linear = (LinearLayout) itemView.findViewById(R.id.linear);
        }

        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "MMM dd, yyyy hh:mm";
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
