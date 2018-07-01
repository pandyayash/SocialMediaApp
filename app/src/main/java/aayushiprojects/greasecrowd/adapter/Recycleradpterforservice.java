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
import aayushiprojects.greasecrowd.model.ServicesDBO;

/**
 * Created by technource on 11/9/17.
 */
public class Recycleradpterforservice extends RecyclerView.Adapter<Recycleradpterforservice.Recycleviewholder> {

   ArrayList<ServicesDBO> services;
    Context context;
    ArrayList<FacilitiesDBo> facilitiesDBos;

    public Recycleradpterforservice(ArrayList<ServicesDBO> services, Context context)
    {

        this.context = context;
        this.services = services;
    }



    @Override
    public Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services,parent,false);
        Recycleviewholder recycleviewholder = new Recycleviewholder(view);


        return recycleviewholder;
    }

    @Override
    public void onBindViewHolder(final Recycleviewholder holder, final int position) {

        holder.services_text.setText(services.get(position).getService());
        holder.view.findViewById(R.id.service_ll).setBackgroundColor(services.get(position).isSelected() ? Color.parseColor("#A367F8"): Color.parseColor("#773AF7"));

        holder.view.findViewById(R.id.service_ll).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                services.get(position).setSelected(!services.get(position).isSelected());
                holder.view.findViewById(R.id.service_ll).setBackgroundColor(services.get(position).isSelected() ? Color.parseColor("#A367F8"): Color.parseColor("#773AF7"));
            }
        });
    }





    @Override
    public int getItemCount() {
        return services.size();
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