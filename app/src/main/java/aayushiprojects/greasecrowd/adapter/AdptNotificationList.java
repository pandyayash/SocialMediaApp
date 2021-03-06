package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
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
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.ViewNotificationActivity;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.NotificationList_Dbo;

/**
 * Created by technource on 5/1/18.
 */

public class AdptNotificationList extends RecyclerView.Adapter<AdptNotificationList.ViewHolder> {

    Context context;
    ArrayList<NotificationList_Dbo> notificationListArrayList;
    OnItemClickListener onItemClick;
    LoginDetail_DBO loginDetail_dbo;

    public AdptNotificationList(Context context, ArrayList<NotificationList_Dbo> notificationListArrayList, OnItemClickListener onItemClick) {
        this.context = context;
        this.notificationListArrayList = notificationListArrayList;
        this.onItemClick = onItemClick;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_list, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        NotificationList_Dbo notificationListDbo = notificationListArrayList.get(position);
        if (notificationListDbo.getType().equalsIgnoreCase("BP")) {
            holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " placed a bid " + notificationListDbo.getData() + " on job " + notificationListDbo.getJob_title());
        }
        if (notificationListDbo.getType().equalsIgnoreCase("ACC")) {
            holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " accepted your job " + notificationListDbo.getJob_title());
        }
        if (notificationListDbo.getType().equalsIgnoreCase("BU")) {
            holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " updated a bid " + notificationListDbo.getData() + " on job " + notificationListDbo.getJob_title());
        }
        if (notificationListDbo.getType().equalsIgnoreCase("FW")) {
            holder.txtNotification.setText(loginDetail_dbo.getFirst_name() + " " + loginDetail_dbo.getLast_name() + " respond to followup works for job " + notificationListDbo.getJob_title());
        }
        if (notificationListDbo.getType().equalsIgnoreCase("WD")) {
            holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " marked your job " + notificationListDbo.getJob_title() + " done");
        }
        if (notificationListDbo.getType().equalsIgnoreCase("PM")) {
            if (notificationListDbo.getNotify_to().equalsIgnoreCase("U")) {
                holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " sent you a new message. ");

            } else {
                holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " sent you a new message. ");
            }
            holder.txtChatMessage.setVisibility(View.VISIBLE);
            if (context instanceof ViewNotificationActivity) {
                holder.txtChatMessage.setText(((ViewNotificationActivity) context).DecodeBase64(notificationListDbo.getData()));
            } else if (context instanceof DashboardScreen) {
                holder.txtChatMessage.setText(((DashboardScreen) context).DecodeBase64(notificationListDbo.getData()));
            }
        }
        if (notificationListDbo.getType().equalsIgnoreCase("DM")) {
            if (notificationListDbo.getNotify_to().equalsIgnoreCase("U")) {
                holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " added a new message in discussion board. ");
            } else {
                holder.txtNotification.setText(notificationListDbo.getBusiness_name() + " added a new message in discussion board. ");

            }
            holder.txtChatMessage.setVisibility(View.VISIBLE);
            if (context instanceof ViewNotificationActivity) {
                holder.txtChatMessage.setText(((ViewNotificationActivity) context).DecodeBase64(notificationListDbo.getData()));
            } else if (context instanceof DashboardScreen) {
                holder.txtChatMessage.setText(((DashboardScreen) context).DecodeBase64(notificationListDbo.getData()));
            }

        }
        if (notificationListDbo.getType().equalsIgnoreCase("NJ")) {
            holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " posted a new job " + notificationListDbo.getJob_title() + " in " + notificationListDbo.getCatname());

        }
        if (notificationListDbo.getType().equalsIgnoreCase("AWA")) {
            holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " awarded ypu the job  " + notificationListDbo.getJob_title());

        }
        if (notificationListDbo.getType().equalsIgnoreCase("SR")) {
            holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " reviewed the job " + notificationListDbo.getJob_title() + " done ");
        }
        if (notificationListDbo.getType().equalsIgnoreCase("RTP")) {
            holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " marked the job " + notificationListDbo.getJob_title() + " ready to pickup ");
        }
        if (notificationListDbo.getType().equalsIgnoreCase("FWR")) {
            holder.txtNotification.setText(notificationListDbo.getFname() + " " + notificationListDbo.getLname() + " proposed followup works for job " + notificationListDbo.getJob_title());
        }

        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(v, position);
            }
        });
        Glide.with(context)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + notificationListDbo.getLogo_image())
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .skipMemoryCache(false)
                .into(holder.iv_headerprofile);
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            holder.txtJobDetail.setText("Job Number: " + notificationListDbo.getJu_id() + " \nJob Title : " + notificationListDbo.getJob_title());
        }
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {
            holder.txtJobDetail.setVisibility(View.GONE);
        }

        holder.tv_date.setText(holder.parseDateToddMMyyyy(notificationListDbo.getDatetime()));

        holder.linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.onClick(v, position);
            }
        });


    }

    @Override
    public int getItemCount() {
        return notificationListArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_close, iv_headerprofile;
        TextView txtNotification, txtJobDetail, tv_date, txtChatMessage;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_close = itemView.findViewById(R.id.iv_close);
            iv_headerprofile = itemView.findViewById(R.id.iv_headerprofile);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            txtJobDetail = itemView.findViewById(R.id.txtJobDetail);
            txtChatMessage = itemView.findViewById(R.id.txtChatMessage);
            tv_date = itemView.findViewById(R.id.tv_date);
            linear = itemView.findViewById(R.id.linear);
        }

        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "HH:mm dd,MMM yyyy";
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
