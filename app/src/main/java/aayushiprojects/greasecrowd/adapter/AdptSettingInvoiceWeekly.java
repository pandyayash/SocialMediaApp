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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.InvoiceDetailsActivity;
import aayushiprojects.greasecrowd.activities.WeeklyDetailsActivity;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.interfaces.OndeleteNotification;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.MailBoxDBO;

/**
 * Created by technource on 5/1/18.
 */

public class AdptSettingInvoiceWeekly extends RecyclerView.Adapter<AdptSettingInvoiceWeekly.ViewHolder> {

    Context context;
    ArrayList<MailBoxDBO> mailBoxDBOArrayList;
    LoginDetail_DBO loginDetail_dbo;
    OndeleteNotification ondeleteNotification;

    public AdptSettingInvoiceWeekly(Context context, ArrayList<MailBoxDBO> mailBoxDBOArrayList, OndeleteNotification ondeleteNotification) {
        this.context = context;
        this.mailBoxDBOArrayList = mailBoxDBOArrayList;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
        this.ondeleteNotification = ondeleteNotification;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notification_list_mailbox, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final MailBoxDBO mailbox = mailBoxDBOArrayList.get(position);

        if (mailbox.getType().equalsIgnoreCase("invoice")) {
            holder.txtNotification.setText(mailbox.getType() + " # " + mailbox.getInvoiceId() + " for job # " + mailbox.getJuId());

        } else {
            String data = holder.parseDateToddMMyyyy1(mailbox.getDateTime());
            holder.txtNotification.setText("Weekly report weekending " + data);

        }

        holder.txtNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mailbox.getType().equalsIgnoreCase("invoice")) {
                    Intent intent = new Intent(context, InvoiceDetailsActivity.class);
                    intent.putExtra("id", mailbox.getJuId());
                    context.startActivity(intent);
                    ((DashboardScreen) context).activityTransition();
                } else {
                    Intent intent = new Intent(context, WeeklyDetailsActivity.class);
                    intent.putExtra("date", holder.parseDateToddMMyyyy1(mailbox.getDateTime()));
                    context.startActivity(intent);
                    ((DashboardScreen) context).activityTransition();
                }

            }
        });


        holder.iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ondeleteNotification.onClick(v, position, mailbox.getType());
            }
        });

        holder.tv_date.setText(holder.parseDateToddMMyyyy(mailbox.getDateTime()));
    }


    @Override
    public int getItemCount() {
        return mailBoxDBOArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_close;
        TextView txtNotification, tv_date;
        LinearLayout linear;

        public ViewHolder(View itemView) {
            super(itemView);
            iv_close = itemView.findViewById(R.id.iv_close);
            txtNotification = itemView.findViewById(R.id.txtNotification);
            tv_date = itemView.findViewById(R.id.tv_date);
            linear = itemView.findViewById(R.id.linear);
        }

        public String parseDateToddMMyyyy(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "HH:mm";
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


            try {
                SimpleDateFormat _24HourSDF = new SimpleDateFormat("HH:mm");
                SimpleDateFormat _12HourSDF = new SimpleDateFormat("hh:mm a");
                Date _24HourDt = _24HourSDF.parse(str);
                return (_12HourSDF.format(_24HourDt));
            } catch (Exception e) {
                e.printStackTrace();
                return str;
            }


        }

        public String parseDateToddMMyyyy1(String time) {
            String inputPattern = "yyyy-MM-dd HH:mm:ss";
            String outputPattern = "dd-MM-yyyy";
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
