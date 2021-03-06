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
import aayushiprojects.greasecrowd.model.Model_FreeInclusions;

/**
 * Created by technource on 31/1/18.
 */

public class AdptFreeInclusions extends RecyclerView.Adapter<AdptFreeInclusions.ViewHolder> {
    ArrayList<Model_FreeInclusions> freeInclusionsArrayList;
    Context context;
    OnItemClickListener itemClickListener;

    public AdptFreeInclusions(ArrayList<Model_FreeInclusions> freeInclusionsArrayList, Context context, OnItemClickListener itemClickListener) {
        this.freeInclusionsArrayList = freeInclusionsArrayList;
        this.context = context;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_services_with_info, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final Model_FreeInclusions modelFreeInclusions = freeInclusionsArrayList.get(position);
        holder.services_text.setText(modelFreeInclusions.getInclusion());
        holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
        holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));
        if (position==freeInclusionsArrayList.size()-1){
            holder.ll_add_new_inclusion.setVisibility(View.VISIBLE);
        }else {
            holder.ll_add_new_inclusion.setVisibility(View.GONE);
        }

        holder.ll_add_new_inclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,position);
            }
        });

        if (modelFreeInclusions.isSelected()) {
            holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
            holder.services_text.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
            holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));
        }

        holder.service_ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,position);
                modelFreeInclusions.setSelected(!modelFreeInclusions.isSelected());
                if (modelFreeInclusions.isSelected()) {
                    holder.service_ll.setBackgroundColor(context.getResources().getColor(R.color.edittext_bg));
                    holder.services_text.setTextColor(context.getResources().getColor(R.color.white));
                } else {
                    holder.service_ll.setBackground(context.getDrawable(R.drawable.rect_inner_boarder));
                    holder.services_text.setTextColor(context.getResources().getColor(R.color.text_color2));
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return freeInclusionsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout service_ll, ll_info,ll_add_new_inclusion;

        TextView services_text;
        public ViewHolder(View itemView) {
            super(itemView);
            services_text = itemView.findViewById(R.id.autoText2);
            service_ll = itemView.findViewById(R.id.service_ll);
            ll_info = itemView.findViewById(R.id.ll_info);
            ll_add_new_inclusion = itemView.findViewById(R.id.ll_add_new_inclusion);
        }
    }
}
