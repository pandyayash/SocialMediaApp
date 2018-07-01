package aayushiprojects.greasecrowd.adapter;

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

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.GarageOwnerDetailsActivity;
import aayushiprojects.greasecrowd.activities.QuoteJobDetailsActivity;
import aayushiprojects.greasecrowd.activities.QuoteJobDetailsPagerActivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.AwardJobDBOCarOwner;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 19/12/17.
 */

public class AdptQuoteJobsDetails extends RecyclerView.Adapter<AdptQuoteJobsDetails.ViewHolder> {
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    Context context;
    ArrayList<String> stringArrayList;
    AdptFreeInclusion adptFreeInclusion;
    JobDetail_DBO jobs;
    LoginDetail_DBO loginDetail_dbo;

    public AdptQuoteJobsDetails(ArrayList<AwardJobDBOCarOwner> awardJobArrayList, Context context, JobDetail_DBO jobs) {
        this.awardJobArrayList = awardJobArrayList;
        this.context = context;
        stringArrayList = new ArrayList<>();
        this.jobs = jobs;

        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_quoted_job, parent, false);

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
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .skipMemoryCache(false)
                .into(holder.avatar_img);
        holder.ratingBar.setRating(awardJobDBOCarOwner.getRating());
        holder.txtReview.setText("( " + awardJobDBOCarOwner.getReview_count() + " Review)");
        holder.txtDistance.setText("Distance : " + awardJobDBOCarOwner.getDistance() + " KM");

        holder.txtRating.setText("" + awardJobDBOCarOwner.getRating());


        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            holder.txtBidPrice.setText("Quoted");
            if (!loginDetail_dbo.getUserid().equalsIgnoreCase(awardJobDBOCarOwner.getGarage_id())) {
                holder.txtPrintQuote.setVisibility(View.GONE);
            } else {
                holder.txtPrintQuote.setVisibility(View.VISIBLE);
                if (awardJobDBOCarOwner.getAdd_offer_accept().equalsIgnoreCase("1")) {
                    holder.txtBidPrice.setText("$" + awardJobDBOCarOwner.getTotal());
                } else {
                    holder.txtBidPrice.setText("$" + awardJobDBOCarOwner.getBid_price());
                }
            }
        } else {
            if (awardJobDBOCarOwner.getAdd_offer_accept().equalsIgnoreCase("1")) {
                holder.txtBidPrice.setText("$" + awardJobDBOCarOwner.getTotal());
            } else {
                holder.txtBidPrice.setText("$" + awardJobDBOCarOwner.getBid_price());
            }

        }

        if (Constants.isFromNewJobFeed) {
            holder.txtBidPrice.setText("Quoted");
            holder.txtPrintQuote.setVisibility(View.GONE);
        }
        holder.txtPrintQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QuoteJobDetailsPagerActivity.class);
                intent.putExtra("jobs", jobs);
                intent.putExtra("awardJobArrayList", awardJobArrayList);
                intent.putExtra("followupWorkArrayList", jobs.getFollowupWorkArrayList());
                intent.putExtra("pos", position);
                (context).startActivity(intent);
                ((QuoteJobDetailsActivity) context).activityTransition();
            }
        });

        holder.txtUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id", awardJobArrayList.get(position).getGarage_id());
                (context).startActivity(intent);
                ((QuoteJobDetailsActivity) context).activityTransition();
            }
        });
        holder.avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id", awardJobArrayList.get(position).getGarage_id());
                (context).startActivity(intent);
                ((QuoteJobDetailsActivity) context).activityTransition();
            }
        });
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
        TextView txtUserName, txtReview, txtDistance, txtBidPrice, txtPrintQuote, txtGarageComment, txtOfferPrice, txtAcceptAdditionalOffer;

        public ViewHolder(View itemView) {
            super(itemView);
            freeInclusionrecyclerview = itemView.findViewById(R.id.freeInclusionrecyclerview);
            avatar_img = itemView.findViewById(R.id.avatar_img);
            txtUserName = itemView.findViewById(R.id.txtUserName);
            txtReview = itemView.findViewById(R.id.txtReview);
            txtDistance = itemView.findViewById(R.id.txtDistance);
            txtBidPrice = itemView.findViewById(R.id.txtBidPrice);

            txtPrintQuote = itemView.findViewById(R.id.txtPrintQuote);
            txtGarageComment = itemView.findViewById(R.id.txtGarageComment);
            txtOfferPrice = itemView.findViewById(R.id.txtOfferPrice);
            txtAcceptAdditionalOffer = itemView.findViewById(R.id.txtAcceptAdditionalOffer);
            txtRating = itemView.findViewById(R.id.txtRating);
            ratingBar = itemView.findViewById(R.id.ratingBar);
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