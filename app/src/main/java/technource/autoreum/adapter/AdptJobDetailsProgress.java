package technource.autoreum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import technource.autoreum.R;

/**
 * Created by technource on 18/12/17.
 */

public class AdptJobDetailsProgress extends RecyclerView.Adapter<AdptJobDetailsProgress.ViewHolder> {
    ArrayList<String> stringArrayList;
    Context context;
    String status;

    public AdptJobDetailsProgress(ArrayList<String> stringArrayList, Context context,String status) {
        this.stringArrayList = stringArrayList;
        this.context = context;
        this.status=status;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_job_details_status, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtStatus.setText(stringArrayList.get(position));
        if (status.equalsIgnoreCase("Posted")){
            if (position==0){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }

        } else if (status.equalsIgnoreCase("Quoted")){
            if (position==1){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }else if (position==0) {
                holder.iv_car.setImageResource(R.drawable.car_demo);
            }

        }else if (status.equalsIgnoreCase("Awarded")){
            if (position==2){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }else if (position<2){
                holder.iv_car.setImageResource(R.drawable.car_demo);
            }

        }else if (status.equalsIgnoreCase("Drop Off")){
            if (position==3){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }else if (position<3){
                holder.iv_car.setImageResource(R.drawable.car_demo);
            }

        }else if (status.equalsIgnoreCase("WorkDone")){
            if (position==4){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }else if (position<4){
                holder.iv_car.setImageResource(R.drawable.car_demo);
            }

        }else if (status.equalsIgnoreCase("Pickup")){
            if (position==5){
                holder.iv_car.setImageResource(R.drawable.car_p);
            }else if (position<5){
                holder.iv_car.setImageResource(R.drawable.car_demo);
            }

        }
    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtStatus;
        ImageView iv_car;
        public ViewHolder(View itemView) {
            super(itemView);
            txtStatus = itemView.findViewById(R.id.txtStatus);
            iv_car = itemView.findViewById(R.id.iv_car);
        }
    }
}
