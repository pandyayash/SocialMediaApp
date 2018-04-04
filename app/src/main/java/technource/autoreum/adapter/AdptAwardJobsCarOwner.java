package technource.autoreum.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.activities.GarageOwnerDetailsActivity;
import technource.autoreum.activities.JobDetailsActivity;
import technource.autoreum.activities.ViewNewJobsFeedDetailActivity;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.AwardJobDBOCarOwner;

/**
 * Created by technource on 19/12/17.
 */

public class AdptAwardJobsCarOwner extends RecyclerView.Adapter<AdptAwardJobsCarOwner.ViewHolder> {
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    Context context;
    ArrayList<String> stringArrayList;
    AdptFreeInclusion adptFreeInclusion;
    boolean isAwarded = false;

    public AdptAwardJobsCarOwner(ArrayList<AwardJobDBOCarOwner> awardJobArrayList, Context context) {
        this.awardJobArrayList = awardJobArrayList;
        this.context = context;
        stringArrayList = new ArrayList<>();

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_award_job, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
//        stringArrayList.add(context.getString(R.string.str_free_inclusion_by_garage));
//        holder.setDummyData();
        AwardJobDBOCarOwner awardJobDBOCarOwner = awardJobArrayList.get(position);
        holder.txtUserName.setText(awardJobDBOCarOwner.getName());
        holder.txtUserName.setPaintFlags(holder.txtUserName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        Glide.with(context)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + awardJobDBOCarOwner.getAvatar_img())
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .skipMemoryCache(true)
                .into(holder.avatar_img);
        holder.ratingBar.setRating(awardJobDBOCarOwner.getRating());
        holder.txtReview.setText("Review : (" + awardJobDBOCarOwner.getReview_count() + ")");
        holder.txtDistance.setText(awardJobDBOCarOwner.getDistance() + " KM");
        holder.txtBidPrice.setText("$" + awardJobDBOCarOwner.getBid_price());
        if (awardJobDBOCarOwner.getBid_status().equalsIgnoreCase("awarded")) {
            holder.txtJobStatus.setVisibility(View.VISIBLE);
            holder.txtJobStatus.setText(awardJobDBOCarOwner.getBid_status());
        } else {
            holder.txtJobStatus.setVisibility(View.GONE);
        }

        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id", awardJobArrayList.get(position).getGarage_id());
                (context).startActivity(intent);
                if (context instanceof ViewNewJobsFeedDetailActivity) {
                    ((ViewNewJobsFeedDetailActivity) context).activityTransition();
                } else {
                    ((JobDetailsActivity) context).activityTransition();
                }

            }
        });
        holder.avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id", awardJobArrayList.get(position).getGarage_id());
                (context).startActivity(intent);
                if (context instanceof ViewNewJobsFeedDetailActivity) {
                    ((ViewNewJobsFeedDetailActivity) context).activityTransition();
                } else {
                    ((JobDetailsActivity) context).activityTransition();
                }
            }
        });


        /*if (awardJobDBOCarOwner.getBid_status().equalsIgnoreCase("open")) {
            holder.txtJobStatus.setText("Award");
        } else {

            if (isAwarded == false) {

            } else {
                holder.txtJobStatus.setVisibility(View.GONE);
            }

            isAwarded = true;
        }*/

        holder.txtRating.setText("" + awardJobDBOCarOwner.getRating());
    }

    @Override
    public int getItemCount() {
        //return 1;
        return awardJobArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RecyclerView freeInclusionrecyclerview;
        ImageView avatar_img;
        RatingBar ratingBar;
        TextView txtRating;
        TextView txtUserName, txtReview, txtDistance, txtBidPrice, txtJobStatus, txtPrintQuote, txtGarageComment, txtOfferPrice, txtAcceptAdditionalOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            freeInclusionrecyclerview = (RecyclerView) itemView.findViewById(R.id.freeInclusionrecyclerview);
            avatar_img = (ImageView) itemView.findViewById(R.id.avatar_img);
            txtUserName = (TextView) itemView.findViewById(R.id.txtUserName);
            txtReview = (TextView) itemView.findViewById(R.id.txtReview);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            txtBidPrice = (TextView) itemView.findViewById(R.id.txtBidPrice);
            txtJobStatus = (TextView) itemView.findViewById(R.id.txtJobStatus);
            txtPrintQuote = (TextView) itemView.findViewById(R.id.txtPrintQuote);
            txtGarageComment = (TextView) itemView.findViewById(R.id.txtGarageComment);
            txtOfferPrice = (TextView) itemView.findViewById(R.id.txtOfferPrice);
            txtAcceptAdditionalOffer = (TextView) itemView.findViewById(R.id.txtAcceptAdditionalOffer);
            txtRating = (TextView) itemView.findViewById(R.id.txtRating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }

        public void setDummyData() {
            adptFreeInclusion = new AdptFreeInclusion(stringArrayList, context);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
            freeInclusionrecyclerview.setLayoutManager(mLayoutManager);
            freeInclusionrecyclerview.setItemAnimator(new DefaultItemAnimator());
            freeInclusionrecyclerview.setAdapter(adptFreeInclusion);

        }
    }


}
