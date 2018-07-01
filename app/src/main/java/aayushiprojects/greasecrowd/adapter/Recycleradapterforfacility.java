package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.model.FacilitiesDBo;

/**
 * Created by technource on 11/9/17.
 */

public class Recycleradapterforfacility extends RecyclerView.Adapter<Recycleradapterforfacility.Recycleviewholder> {


    Context context;
    ArrayList<FacilitiesDBo> facilitiesDBos;

    public Recycleradapterforfacility(ArrayList<FacilitiesDBo> services, Context context)
    {

        this.context = context;
        this.facilitiesDBos = services;
    }



    @Override
    public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services,parent,false);
        Recycleviewholder recycleviewholder = new Recycleviewholder(view);


        return recycleviewholder;
    }

    @Override
    public void onBindViewHolder(final Recycleviewholder holder, final int position) {

        holder.services_text.setText(facilitiesDBos.get(position).getFacilites());
        holder.view.findViewById(R.id.service_ll).setBackgroundColor(facilitiesDBos.get(position).isSelected() ? Color.parseColor("#A367F8"): Color.parseColor("#773AF7"));

        holder.view.findViewById(R.id.service_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facilitiesDBos.get(position).setSelected(!facilitiesDBos.get(position).isSelected());
                holder.view.findViewById(R.id.service_ll).setBackgroundColor(facilitiesDBos.get(position).isSelected() ? Color.parseColor("#A367F8"): Color.parseColor("#773AF7"));
            }
        });
    }





    @Override
    public int getItemCount() {
        return facilitiesDBos.size();
    }

    public static class Recycleviewholder extends RecyclerView.ViewHolder
    {
        private View view;

        TextView services_text;
        public Recycleviewholder(View itemView) {
            super(itemView);
            view = itemView;
            services_text = itemView.findViewById(R.id. autoText2);
        }

        //if you implement onclick here you must have to use getposition() instead of making variable position global see documentation
    }
}
