package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;
import aayushiprojects.greasecrowd.model.Transaction_DBO;

/**
 * Created by technource on 21/2/18.
 */

public class AdptTransactionHistory extends RecyclerView.Adapter<AdptTransactionHistory.ViewHolder> {

    Context context;
    ArrayList<Transaction_DBO> transactionArrayList;
    LoginDetail_DBO loginDetail_dbo;


    public AdptTransactionHistory(Context context, ArrayList<Transaction_DBO> transactionArrayList) {
        this.context = context;
        this.transactionArrayList = transactionArrayList;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Transaction_DBO transaction_dbo = transactionArrayList.get(position);
        String str="\""+transaction_dbo.getJob_title()+"\"";

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")){

            if (transaction_dbo.getType().equalsIgnoreCase("PAY_ON_PICKUP")){
                holder.textTransaction.setText(transaction_dbo.getUfname() + " " + transaction_dbo.getUlname() + " selected to pay on pickup for job "+str+
                " at "+transaction_dbo.getKm()+ " Km. eGM deducted a commission of " + transaction_dbo.getEg_comm()+ " from your account");
                //holder.textTransaction.setText("$"+transaction_dbo.getAmount()+"  to be paid to "+ transaction_dbo.getBusiness_name()+" for "+ str + " on pickup.");
            }else {
                holder.textTransaction.setText(transaction_dbo.getUfname()+" " + transaction_dbo.getUlname() +" selected to pay using PayPal/Credit Card for "+
                        str + " at " + transaction_dbo.getKm() + " KM, eGM deducted a commission of " + transaction_dbo.getEg_comm() +
                        " dollars and the balance has been credited to your account.");
            }

        }else {

            if (transaction_dbo.getType().equalsIgnoreCase("PAY_ON_PICKUP")){
                holder.textTransaction.setText("$"+transaction_dbo.getAmount()+"  to be paid to "+ transaction_dbo.getBusiness_name()+" for "+ str + " on pickup.");
            }else {
                holder.textTransaction.setText("$"+transaction_dbo.getAmount()+" paid to "+ transaction_dbo.getBusiness_name()+" for "+ str );
            }


        }



        holder.textDate.setText(parseDateToddMMyyyy(transaction_dbo.getDatetime()));

    }

    @Override
    public int getItemCount() {
        return transactionArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textTransaction,textDate;
        public ViewHolder(View itemView) {
            super(itemView);
            textTransaction = itemView.findViewById(R.id.textTransaction);
            textDate = itemView.findViewById(R.id.textDate);
        }

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "MMM dd, yyyy";
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
