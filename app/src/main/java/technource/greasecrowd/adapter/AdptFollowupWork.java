package technource.greasecrowd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.model.Model_FollowupWork;

/**
 * Created by technource on 20/2/18.
 */

public class AdptFollowupWork extends RecyclerView.Adapter<AdptFollowupWork.ViewHolder> {

    ArrayList<Model_FollowupWork> followupWorkArrayList;
    Context context;

    public AdptFollowupWork(ArrayList<Model_FollowupWork> followupWorkArrayList, Context context) {
        this.followupWorkArrayList = followupWorkArrayList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_followup_work, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Model_FollowupWork  model_followupWork = followupWorkArrayList.get(position);
        if (!model_followupWork.getKey().equalsIgnoreCase("")){
            holder.txtKey.setText(model_followupWork.getKey());
        }
        if (!model_followupWork.getValue().equalsIgnoreCase("")){
            holder.txtValue.setText(model_followupWork.getValue());
        }



    }

    @Override
    public int getItemCount() {
        return followupWorkArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtKey,txtValue;
        public ViewHolder(View itemView) {
            super(itemView);
            txtKey = (TextView)itemView.findViewById(R.id.txtKey);
            txtValue = (TextView)itemView.findViewById(R.id.txtValue);
        }
    }
}
