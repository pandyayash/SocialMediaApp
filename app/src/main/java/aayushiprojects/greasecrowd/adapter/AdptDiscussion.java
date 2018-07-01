package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.DiscussionActivity;
import aayushiprojects.greasecrowd.activities.GarageOwnerDetailsActivity;
import aayushiprojects.greasecrowd.activities.UserDetailsActivity;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.DiscussionDbo;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

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
            String[] splited = discussionDbo.getName().split("\\s+");
            holder.senderName.setText(splited[0]);
            if (discussionDbo.getDateTime().equalsIgnoreCase("0000-00-00 00:00:00")) {
                holder.senderDateTime.setText(holder.parseDateToddMMyyyy1("2018-04-27 12:12:12"));
            } else {
                holder.senderDateTime.setText(holder.parseDateToddMMyyyy1(discussionDbo.getDateTime()));

            }

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
            String[] splited = discussionDbo.getName().split("\\s+");
            holder.receiverName.setText(splited[0]);
            if (discussionDbo.getDateTime().equalsIgnoreCase("0000-00-00 00:00:00")) {
                holder.receiverDateTime.setText(holder.parseDateToddMMyyyy1("2018-04-27 12:12:12"));
            } else {
                holder.receiverDateTime.setText(holder.parseDateToddMMyyyy1(discussionDbo.getDateTime()));

            }
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
//        holder.receiverName.setText(discussionDbo.getDateTime());
//        holder.senderDateTime.setText(discussionDbo.getDateTime());
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
        private TextView tvMessageR, senderDateTime, receiverDateTime;


        public ViewHolder(View itemView) {
            super(itemView);

            llSend = itemView.findViewById(R.id.ll_send);
            tvMessageS = itemView.findViewById(R.id.tv_message_s);
            ivProfileS = itemView.findViewById(R.id.iv_profile_s);
            llReceive = itemView.findViewById(R.id.ll_receive);
            ivProfileR = itemView.findViewById(R.id.iv_profile_r);
            tvMessageR = itemView.findViewById(R.id.tv_message_r);
            senderName = itemView.findViewById(R.id.senderName);
            receiverName = itemView.findViewById(R.id.receiverName);
            receiverDateTime = itemView.findViewById(R.id.receiverDateTime);
            senderDateTime = itemView.findViewById(R.id.senderDateTime);

        }

        public String parseDateToddMMyyyy1(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd-MM-yyyy HH:mm a";
            SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
            SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

            Date date = null;
            String str = null;

            try {
                date = inputFormat.parse(time);
                str = outputFormat.format(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return str;
        }
    }
}
