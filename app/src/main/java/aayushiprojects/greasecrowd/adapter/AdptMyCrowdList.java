package aayushiprojects.greasecrowd.adapter;

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

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.MyCrowdDetailsActivity;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.MyCrowd_DBO;

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
        char firstCharacter = 0;

        firstCharacter = myCrowdDbo.getLname().charAt(0);
        holder.textJobTitle.setText(myCrowdDbo.getJob_title());
        holder.textJobDescription.setText(myCrowdDbo.getProblem_description());
        holder.textCarModel.setText(myCrowdDbo.getFname() + " " + firstCharacter+". "+
                myCrowdDbo.getSuburb()+" "+myCrowdDbo.getMake() + ", " + myCrowdDbo.getModel()
                + ", " + myCrowdDbo.getBadge() + ", " +myCrowdDbo.getYear());
        holder.textDate.setText(holder.parseDateToddMMyyyy(myCrowdDbo.getJob_posted_date()));
        holder.textCategory.setText(myCrowdDbo.getCatname());

        holder.txtName.setText(myCrowdDbo.getFname() + " " + myCrowdDbo.getLname());
        //holder.txtPosterName.setText(myCrowdDbo.getFname() + " " + firstCharacter);
        holder.txtAddress.setText(myCrowdDbo.getSuburb());


        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            holder.textResponses.setText("Responses : " + myCrowdDbo.getRespinses());
        } else {
            holder.ll_name_address.setVisibility(View.GONE);
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
        TextView textJobTitle, textDate, textJobDescription, textCarModel, textDistance, textResponses, textCategory, txtName, txtAddress,txtPosterName;
        LinearLayout linear,ll_name_address;

        public ViewHolder(View itemView) {
            super(itemView);
            textJobTitle = itemView.findViewById(R.id.textJobTitle);
            textDate = itemView.findViewById(R.id.textDate);
            textJobDescription = itemView.findViewById(R.id.textJobDescription);
            textCarModel = itemView.findViewById(R.id.textCarModel);
            textDistance = itemView.findViewById(R.id.textDistance);
            textResponses = itemView.findViewById(R.id.textResponses);
            textCategory = itemView.findViewById(R.id.textCategory);
            txtAddress = itemView.findViewById(R.id.txtAddress);
            txtPosterName = itemView.findViewById(R.id.txtPosterName);
            txtName = itemView.findViewById(R.id.txtName);
            linear = itemView.findViewById(R.id.linear);
            ll_name_address = itemView.findViewById(R.id.ll_name_address);
        }

        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "MMM dd, yyyy HH:mm";
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
