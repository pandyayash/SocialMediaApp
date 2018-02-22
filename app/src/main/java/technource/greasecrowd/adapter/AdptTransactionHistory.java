package technource.greasecrowd.adapter;

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

import technource.greasecrowd.R;
import technource.greasecrowd.model.Transaction_DBO;

/**
 * Created by technource on 21/2/18.
 */

public class AdptTransactionHistory extends RecyclerView.Adapter<AdptTransactionHistory.ViewHolder> {

    Context context;
    ArrayList<Transaction_DBO> transactionArrayList;


    public AdptTransactionHistory(Context context, ArrayList<Transaction_DBO> transactionArrayList) {
        this.context = context;
        this.transactionArrayList = transactionArrayList;
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

        holder.textTransaction.setText("$"+transaction_dbo.getAmount()+"  to be paid to "+ transaction_dbo.getBusiness_name()+" for "+ str + " on pickup.");
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
            textTransaction = (TextView)itemView.findViewById(R.id.textTransaction);
            textDate = (TextView)itemView.findViewById(R.id.textDate);
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
