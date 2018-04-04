package technource.autoreum.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.Model_FollowupWork;

/**
 * Created by technource on 20/2/18.
 */

public class AdptFollowupWork extends RecyclerView.Adapter<AdptFollowupWork.ViewHolder> {

    ArrayList<Model_FollowupWork> followupWorkArrayList;
    Context context;
    LoginDetail_DBO loginDetail_dbo;

    public AdptFollowupWork(ArrayList<Model_FollowupWork> followupWorkArrayList, Context context) {
        this.followupWorkArrayList = followupWorkArrayList;
        this.context = context;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_followup_work, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_FollowupWork model_followupWork = followupWorkArrayList.get(position);

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            holder.txtAcceptOrReject.setVisibility(View.GONE);
            holder.acceptOrReject.setVisibility(View.GONE);
            if (!model_followupWork.getKey().equalsIgnoreCase("")) {
                holder.txtKey.setText(model_followupWork.getKey());
            }
            if (!model_followupWork.getValue().equalsIgnoreCase("")) {
                holder.txtValue.setText(model_followupWork.getValue());
            }
        } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            if (!model_followupWork.getKey().equalsIgnoreCase("")) {
                holder.txtKey.setText(model_followupWork.getKey());
            }
            if (!model_followupWork.getValue().equalsIgnoreCase("")) {
                holder.txtValue.setText(model_followupWork.getValue());
            }
            // 0-> seen but not accepted , 1--> seen and accepted, 2--> not seen
            if (followupWorkArrayList.get(position).getIsAccepted() == 3) {
                holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                holder.txtAcceptOrReject.setText("Rejected");
                holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.red_color));
            } else if (followupWorkArrayList.get(position).getIsAccepted() == 1) {
                holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                holder.txtAcceptOrReject.setText("Accepted");
                holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.green_btn));
            } else if (followupWorkArrayList.get(position).getIsAccepted() == 0) {
                holder.txtAcceptOrReject.setVisibility(View.VISIBLE);
                holder.txtAcceptOrReject.setText("No Response");
                holder.txtAcceptOrReject.setTextColor(ContextCompat.getColor(context, R.color.green_btn));
            } else if (followupWorkArrayList.get(position).getIsAccepted() == 2) {
                holder.acceptOrReject.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return followupWorkArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtKey, txtValue, txtAcceptOrReject;
        RadioGroup acceptOrReject;

        public ViewHolder(View itemView) {
            super(itemView);
            txtKey = (TextView) itemView.findViewById(R.id.txtKey);
            txtValue = (TextView) itemView.findViewById(R.id.txtValue);
            acceptOrReject = (RadioGroup) itemView.findViewById(R.id.acceptOrReject);
            txtAcceptOrReject = (TextView) itemView.findViewById(R.id.txtAcceptOrReject);
        }
    }
}
