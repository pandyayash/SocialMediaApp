package technource.greasecrowd.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
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

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.activities.GarageOwnerDetailsActivity;
import technource.greasecrowd.activities.JobsCompletedByGarageActivity;
import technource.greasecrowd.activities.PostJob.PostNewJobStepOne;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 19/2/18.
 */

public class AdptMyGarages extends RecyclerView.Adapter<AdptMyGarages.ViewHolder> {
    Context context;
    ArrayList<AwardJobDBOCarOwner> awardJobArrayList;
    LoginDetail_DBO loginDetail_dbo;

    public AdptMyGarages(Context context, ArrayList<AwardJobDBOCarOwner> awardJobArrayList) {
        this.context = context;
        this.awardJobArrayList = awardJobArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_my_garage, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final AwardJobDBOCarOwner awardJobDBOCarOwner = awardJobArrayList.get(position);
        holder.txtGarageName.setText(awardJobDBOCarOwner.getName());
        holder.txtGarageName.setPaintFlags(holder.txtGarageName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
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
        holder.txtReview.setText("(" + awardJobDBOCarOwner.getReview_count() +" Reviews )");
        holder.txtDistance.setText("Distance: "+awardJobDBOCarOwner.getDistance() + " KM");
        holder.txtRating.setText("" + awardJobDBOCarOwner.getRating());
        holder.btnJobCompleteByGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, JobsCompletedByGarageActivity.class);
                intent.putExtra("garageId",awardJobDBOCarOwner.getId());
                intent.putExtra("garageName",awardJobDBOCarOwner.getName());
                context.startActivity(intent);
                ((DashboardScreen)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        holder.btnPostJobGarage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, PostNewJobStepOne.class);
                intent.putExtra("garageId",awardJobDBOCarOwner.getId());
                intent.putExtra("isFromMyGarage",true);
                context.startActivity(intent);
                ((DashboardScreen)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        holder.txtGarageName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //GarageOwnerDetailsActivity
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id",awardJobDBOCarOwner.getId());
                //intent.putExtra("isFromMyGarage",true);
                context.startActivity(intent);
                ((DashboardScreen)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        holder.avatar_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                intent.putExtra("garage_id",awardJobDBOCarOwner.getId());
                //intent.putExtra("isFromMyGarage",true);
                context.startActivity(intent);
                ((DashboardScreen)context).overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

    }

    @Override
    public int getItemCount() {
        return awardJobArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar_img;
        RatingBar ratingBar;
        TextView txtRating;
        TextView txtGarageName, txtReview, txtDistance, btnJobCompleteByGarage, btnPostJobGarage;
        public ViewHolder(View itemView) {
            super(itemView);

            avatar_img = (ImageView) itemView.findViewById(R.id.avatar_img);
            txtGarageName = (TextView) itemView.findViewById(R.id.txtGarageName);
            btnJobCompleteByGarage = (TextView) itemView.findViewById(R.id.btnJobCompleteByGarage);
            btnPostJobGarage = (TextView) itemView.findViewById(R.id.btnPostJobGarage);
            txtReview = (TextView) itemView.findViewById(R.id.txtReview);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
            txtRating = (TextView) itemView.findViewById(R.id.txtRating);
            ratingBar = (RatingBar) itemView.findViewById(R.id.ratingBar);
        }
    }
}
