package technource.greasecrowd.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.model.DetailsOfGarageCarJobDBO;
import technource.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 5/1/18.
 */
public class AdptInvoiceListing extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    ArrayList<DetailsOfGarageCarJobDBO> detailsOfGarageCarJobDBOArrayList;
    LoginDetail_DBO loginDetail_dbo;
    private static final int TYPE_FOOTER = 2;
    private static final int TYPE_ITEM = 1;
    private static final int GST_PERCENT = 10;
    float total = 0;


    public AdptInvoiceListing(Context context, ArrayList<DetailsOfGarageCarJobDBO> detailsOfGarageCarJobDBOArrayList) {
        this.context = context;
        this.detailsOfGarageCarJobDBOArrayList = detailsOfGarageCarJobDBOArrayList;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(context);

    }


    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }

        return TYPE_ITEM;
    }


    private boolean isPositionFooter(int position) {
        return position >= detailsOfGarageCarJobDBOArrayList.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        if (viewType == TYPE_ITEM) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_list, viewGroup, false);
            return new ItemViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {

            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_invoice_list_2,
                    viewGroup, false);
            return new FooterViewHolder(view);

        }

        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof ItemViewHolder) {
            DetailsOfGarageCarJobDBO data = detailsOfGarageCarJobDBOArrayList.get(position);
            total = total + Float.parseFloat(data.getBidPrice());
            ((ItemViewHolder) holder).txtJobDescription.setText(data.getJobTitle());
            ((ItemViewHolder) holder).txtJobId.setText(data.getJuId());
            ((ItemViewHolder) holder).txtUnit.setText("1");
            ((ItemViewHolder) holder).txtUnitPrice.setText(data.getBidPrice());
            ((ItemViewHolder) holder).txtGstPercent.setText("" + GST_PERCENT + "%");
            ((ItemViewHolder) holder).txtGstAmount.setText(data.getBidPrice());

        } else if (holder instanceof FooterViewHolder) {
            float temp = 0;
            temp = total / GST_PERCENT;
            total = total - temp;
            ((FooterViewHolder) holder).txtExclGst.setText("" + total);
            ((FooterViewHolder) holder).txtGst.setText("" + temp);
            ((FooterViewHolder) holder).txtTotal.setText("" + (total + temp));
        }
    }

    @Override
    public int getItemCount() {
        return this.detailsOfGarageCarJobDBOArrayList.size() + 1;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public View View;
        public TextView txtJobId;
        public TextView txtJobDescription;
        public TextView txtUnit;
        public TextView txtUnitPrice;
        public TextView txtGstPercent;
        public TextView txtGstAmount;

        public ItemViewHolder(View v) {
            super(v);
            View = v;
            txtJobId = (TextView) View.findViewById(R.id.txtJobId);
            txtJobDescription = (TextView) View.findViewById(R.id.txtJobDescription);
            txtUnit = (TextView) View.findViewById(R.id.txtUnit);
            txtUnitPrice = (TextView) View.findViewById(R.id.txtUnitPrice);
            txtGstPercent = (TextView) View.findViewById(R.id.txtGstPercent);
            txtGstAmount = (TextView) View.findViewById(R.id.txtGstAmount);

        }

    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public View View;

        public TextView txtJobId;
        public TextView txtExclGst;
        public TextView txtUnit;
        public TextView txtGst;
        public TextView txtGstPercent;
        public TextView txtTotal;

        public FooterViewHolder(View v) {
            super(v);
            View = v;
            txtJobId = (TextView) View.findViewById(R.id.txtJobId);
            txtExclGst = (TextView) View.findViewById(R.id.txtExclGst);
            txtUnit = (TextView) View.findViewById(R.id.txtUnit);
            txtGst = (TextView) View.findViewById(R.id.txtGst);
            txtGstPercent = (TextView) View.findViewById(R.id.txtGstPercent);
            txtTotal = (TextView) View.findViewById(R.id.txtTotal);

        }

    }
}