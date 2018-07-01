package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.Model_GarageServices;

/**
 * Created by technource on 29/1/18.
 */

public class AdptGarageServiceType extends RecyclerView.Adapter<AdptGarageServiceType.ViewHolder> {
    ArrayList<Model_GarageServices> garageServicesArrayList;
    Context context;
    OnItemClickListener itemClickListener;
    public static int sSelected = -1;
    boolean isUpdate;
    String strServiceId;
    Model_GarageServices model_garageServices;


    public AdptGarageServiceType(ArrayList<Model_GarageServices> garageServicesArrayList, Context context, boolean isUpdate, String strServiceId, OnItemClickListener itemClickListener) {
        this.garageServicesArrayList = garageServicesArrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
        this.isUpdate = isUpdate;
        this.strServiceId = strServiceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_services_with_info, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        model_garageServices = garageServicesArrayList.get(position);
        holder.services_text.setText(model_garageServices.getName());
        holder.ll_info.setVisibility(View.GONE);
        if (sSelected == position) {
            model_garageServices.setSelected(true);
        }
        if (isUpdate) {
            if (strServiceId.equalsIgnoreCase(model_garageServices.getService_id())) {
                model_garageServices.setSelected(true);
            } else {
                model_garageServices.setSelected(false);
            }
        }
       /* for (int i = 0; i < UpdateQuoteGarageActivity.awardJobArrayList.size(); i++) {
            if (UpdateQuoteGarageActivity.awardJobArrayList.get(i).getService_id().equalsIgnoreCase(model_garageServices.getService_id())) {
                model_garageServices.setSelected(true);
                itemClickListener.onClick(null, position);
            } else {
                model_garageServices.setSelected(false);
            }
        }*/
        if (model_garageServices.isSelected()) {
            holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
            holder.services_text.setTextColor(context.getResources().getColor(R.color.white));

        } else {
            holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
            holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));

        }

        holder.service_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sSelected = position;
                model_garageServices.setSelected(!model_garageServices.isSelected());
                if (model_garageServices.isSelected()) {
                    holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
                    holder.services_text.setTextColor(context.getResources().getColor(R.color.white));
                    for (int i = 0; i < garageServicesArrayList.size(); i++) {
                        if ((position == i)) {
                            garageServicesArrayList.get(i).setSelected(true);
                        } else {
                            garageServicesArrayList.get(i).setSelected(false);
                        }
                    }
                } else {
                    holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
                    holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
                itemClickListener.onClick(view, position);
            }
        });
    }

    public void selectedItem() {
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return garageServicesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout service_ll, ll_info;

        TextView services_text;

        public ViewHolder(View itemView) {
            super(itemView);
            services_text = itemView.findViewById(R.id.autoText2);
            service_ll = itemView.findViewById(R.id.service_ll);
            ll_info = itemView.findViewById(R.id.ll_info);
        }
    }
}
