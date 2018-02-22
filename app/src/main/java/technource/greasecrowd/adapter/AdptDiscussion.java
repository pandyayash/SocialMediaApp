package technource.greasecrowd.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DiscussionActivity;
import technource.greasecrowd.activities.GarageOwnerDetailsActivity;
import technource.greasecrowd.activities.JobDetailsActivity;
import technource.greasecrowd.activities.UserDetailsActivity;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.DiscussionDbo;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.NewPostedJob_DBO;

/**
 * Created by technource on 12/12/17.
 */

public class AdptDiscussion extends RecyclerView.Adapter<AdptDiscussion.ViewHolder> {

    Context context;
    ArrayList<DiscussionDbo> discussionDbos;
    LoginDetail_DBO loginDetail_dbo;

    public AdptDiscussion(ArrayList<DiscussionDbo> discussionDbos, Context context) {
        this.discussionDbos = discussionDbos;
        this.context = context;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final DiscussionDbo discussionDbo = discussionDbos.get(position);

        if (loginDetail_dbo.getUserid().equalsIgnoreCase(discussionDbo.getObjectId())) {
            holder.llReceive.setVisibility(View.GONE);
            holder.llSend.setVisibility(View.VISIBLE);
            holder.tvMessageS.setText(discussionDbo.getMessageText());
            holder.senderName.setText(discussionDbo.getName());
            Glide.with(context)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + discussionDbo.getAvatarImage())
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .skipMemoryCache(false)
                    .into(holder.ivProfileS);
        } else {
            holder.llReceive.setVisibility(View.VISIBLE);
            holder.llSend.setVisibility(View.GONE);
            holder.tvMessageR.setText(discussionDbo.getMessageText());
            holder.receiverName.setText(discussionDbo.getName());
            Glide.with(context)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + discussionDbo.getAvatarImage())
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .skipMemoryCache(false)
                    .into(holder.ivProfileR);
        }

        holder.ivProfileS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discussionDbo.getObjectType().equalsIgnoreCase("G")) {
                    Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                    intent.putExtra("garage_id", discussionDbo.getObjectId());
                    context.startActivity(intent);
                    ((DiscussionActivity) context).activityTransition();
                } else {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra("user_id", discussionDbo.getObjectId());
                    context.startActivity(intent);
                    ((DiscussionActivity) context).activityTransition();
                }
            }
        });
        holder.ivProfileR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (discussionDbo.getObjectType().equalsIgnoreCase("G")) {
                    Intent intent = new Intent(context, GarageOwnerDetailsActivity.class);
                    intent.putExtra("garage_id", discussionDbo.getObjectId());
                    context.startActivity(intent);
                    ((DiscussionActivity) context).activityTransition();
                } else {
                    Intent intent = new Intent(context, UserDetailsActivity.class);
                    intent.putExtra("user_id", discussionDbo.getObjectId());
                    context.startActivity(intent);
                    ((DiscussionActivity) context).activityTransition();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return discussionDbos.size();

    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout llSend;
        private TextView tvMessageS, receiverName, senderName;
        private ImageView ivProfileS;
        private LinearLayout llReceive;
        private ImageView ivProfileR;
        private TextView tvMessageR;


        public ViewHolder(View itemView) {
            super(itemView);

            llSend = (LinearLayout) itemView.findViewById(R.id.ll_send);
            tvMessageS = (TextView) itemView.findViewById(R.id.tv_message_s);
            ivProfileS = (ImageView) itemView.findViewById(R.id.iv_profile_s);
            llReceive = (LinearLayout) itemView.findViewById(R.id.ll_receive);
            ivProfileR = (ImageView) itemView.findViewById(R.id.iv_profile_r);
            tvMessageR = (TextView) itemView.findViewById(R.id.tv_message_r);
            senderName = (TextView) itemView.findViewById(R.id.senderName);
            receiverName = (TextView) itemView.findViewById(R.id.receiverName);

        }
    }
}
