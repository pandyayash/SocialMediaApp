package technource.autoreum.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.SharedPreference;
import technource.autoreum.model.SafetyReport_DBO;

/**
 * Created by technource on 12/2/18.
 */

public class AdptSafetyReports extends RecyclerView.Adapter<AdptSafetyReports.ViewHolder> {
    Context context;
    ArrayList<String> stringArrayList;
    public static ArrayList<SafetyReport_DBO> selectedarrayList;
    public static ArrayList<SafetyReport_DBO> selectedarrayList1;
    public static ArrayList<SafetyReport_DBO> selectedarrayList2;
    public static ArrayList<SafetyReport_DBO> selectedarrayList3;
    ArrayList<SafetyReport_DBO> getSelectedarrayList1;
    ArrayList<SafetyReport_DBO> getSelectedarrayList2;
    ArrayList<SafetyReport_DBO> getSelectedarrayList3;
    ArrayList<SafetyReport_DBO> getSelectedarrayList4;
    SharedPreference sharedPreference;
    String strFrag;


    public AdptSafetyReports(Context context, ArrayList<String> stringArrayList, String strFrag) {
        this.context = context;
        this.stringArrayList = stringArrayList;
        sharedPreference = new SharedPreference();
        this.strFrag = strFrag;
        if (strFrag.equalsIgnoreCase("1")) {
            selectedarrayList = new ArrayList<>();
            getSelectedarrayList1 = new ArrayList<>();
            for (int i = 0; i < stringArrayList.size(); i++) {
                SafetyReport_DBO safetyReport_dbo = new SafetyReport_DBO();
                safetyReport_dbo.setName(stringArrayList.get(i));
                safetyReport_dbo.setSelectedColor("G");
                safetyReport_dbo.setDescription("");
                selectedarrayList.add(safetyReport_dbo);

            }

        } else if (strFrag.equalsIgnoreCase("2")) {
            selectedarrayList1 = new ArrayList<>();
            getSelectedarrayList2 = new ArrayList<>();
            for (int i = 0; i < stringArrayList.size(); i++) {
                SafetyReport_DBO safetyReport_dbo = new SafetyReport_DBO();
                safetyReport_dbo.setName(stringArrayList.get(i));
                safetyReport_dbo.setSelectedColor("G");
                safetyReport_dbo.setDescription("");
                selectedarrayList1.add(safetyReport_dbo);

            }

        } else if (strFrag.equalsIgnoreCase("3")) {
            selectedarrayList2 = new ArrayList<>();
            getSelectedarrayList3 = new ArrayList<>();
            for (int i = 0; i < stringArrayList.size(); i++) {
                SafetyReport_DBO safetyReport_dbo = new SafetyReport_DBO();
                safetyReport_dbo.setName(stringArrayList.get(i));
                safetyReport_dbo.setSelectedColor("G");
                safetyReport_dbo.setDescription("");
                selectedarrayList2.add(safetyReport_dbo);

            }

        } else if (strFrag.equalsIgnoreCase("4")) {
            selectedarrayList3 = new ArrayList<>();
            getSelectedarrayList4 = new ArrayList<>();
            for (int i = 0; i < stringArrayList.size(); i++) {
                SafetyReport_DBO safetyReport_dbo = new SafetyReport_DBO();
                safetyReport_dbo.setName(stringArrayList.get(i));
                safetyReport_dbo.setSelectedColor("G");
                safetyReport_dbo.setDescription("");
                selectedarrayList3.add(safetyReport_dbo);
            }

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_safety_reports, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.textView.setText(stringArrayList.get(position));


        SafetyReport_DBO safetyReport_dbo = new SafetyReport_DBO();

        if (strFrag.equalsIgnoreCase("1")) {
            safetyReport_dbo = selectedarrayList.get(position);
            safetyReport_dbo.setDescription(holder.edtRecommendation.getText().toString());
            selectedarrayList.set(position,safetyReport_dbo);
                getSelectedarrayList1 = HelperMethods.getSafetyrReportSharedPreferences(context, strFrag);
                if (getSelectedarrayList1!=null) {
                if (getSelectedarrayList1.get(position).getSelectedColor().equalsIgnoreCase("G")) {
                    holder.imgGreen.getBackground().setLevel(0);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(5);

                } else if (getSelectedarrayList1.get(position).getSelectedColor().equalsIgnoreCase("Y")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(2);
                    holder.imgRed.getBackground().setLevel(5);
                } else if (getSelectedarrayList1.get(position).getSelectedColor().equalsIgnoreCase("Y")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(4);
                }
            }else {
                holder.imgGreen.getBackground().setLevel(0);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(5);
            }
        }  if (strFrag.equalsIgnoreCase("2")) {
            safetyReport_dbo = selectedarrayList1.get(position);
            safetyReport_dbo.setDescription(holder.edtRecommendation.getText().toString());
            selectedarrayList1.set(position,safetyReport_dbo);
            getSelectedarrayList2 = HelperMethods.getSafetyrReportSharedPreferences(context,strFrag);
            if (getSelectedarrayList2!=null) {
                if (getSelectedarrayList2.get(position).getSelectedColor().equalsIgnoreCase("G")) {
                    holder.imgGreen.getBackground().setLevel(0);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(5);

                } else if (getSelectedarrayList2.get(position).getSelectedColor().equalsIgnoreCase("Y")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(2);
                    holder.imgRed.getBackground().setLevel(5);
                } else if (getSelectedarrayList2.get(position).getSelectedColor().equalsIgnoreCase("R")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(4);
                }
            }else {
                holder.imgGreen.getBackground().setLevel(0);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(5);
            }
        }  if (strFrag.equalsIgnoreCase("3")) {
            safetyReport_dbo = selectedarrayList2.get(position);
            safetyReport_dbo.setDescription(holder.edtRecommendation.getText().toString());
            selectedarrayList2.set(position,safetyReport_dbo);
            getSelectedarrayList3 = HelperMethods.getSafetyrReportSharedPreferences(context,strFrag);
            if (getSelectedarrayList2!=null) {
                if (getSelectedarrayList3.get(position).getSelectedColor().equalsIgnoreCase("G")) {
                    holder.imgGreen.getBackground().setLevel(0);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(5);

                } else if (getSelectedarrayList3.get(position).getSelectedColor().equalsIgnoreCase("Y")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(2);
                    holder.imgRed.getBackground().setLevel(5);
                } else if (getSelectedarrayList3.get(position).getSelectedColor().equalsIgnoreCase("R")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(4);
                }
            }else {
                holder.imgGreen.getBackground().setLevel(0);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(5);
            }
        }  if (strFrag.equalsIgnoreCase("4")) {
            safetyReport_dbo = selectedarrayList3.get(position);
            safetyReport_dbo.setDescription(holder.edtRecommendation.getText().toString());
            selectedarrayList3.set(position,safetyReport_dbo);
            getSelectedarrayList4 = HelperMethods.getSafetyrReportSharedPreferences(context,strFrag);
            if (getSelectedarrayList4!=null) {
                if (getSelectedarrayList4.get(position).getSelectedColor().equalsIgnoreCase("G")) {
                    holder.imgGreen.getBackground().setLevel(0);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(5);

                } else if (getSelectedarrayList4.get(position).getSelectedColor().equalsIgnoreCase("Y")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(2);
                    holder.imgRed.getBackground().setLevel(5);
                } else if (getSelectedarrayList4.get(position).getSelectedColor().equalsIgnoreCase("R")) {
                    holder.imgGreen.getBackground().setLevel(1);
                    holder.imgYellow.getBackground().setLevel(3);
                    holder.imgRed.getBackground().setLevel(4);
                }
            }else {
                holder.imgGreen.getBackground().setLevel(0);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(5);
            }
        }



        final SafetyReport_DBO finalSafetyReport_dbo = safetyReport_dbo;
        holder.imgGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.imgGreen.getBackground().setLevel(0);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(5);
                finalSafetyReport_dbo.setSelectedColor("G");
                if (strFrag.equalsIgnoreCase("1")) {
                    selectedarrayList.set(position, finalSafetyReport_dbo);
                } else if (strFrag.equalsIgnoreCase("2")) {
                    selectedarrayList1.set(position, finalSafetyReport_dbo);
                } else if (strFrag.equalsIgnoreCase("3")) {
                    selectedarrayList2.set(position, finalSafetyReport_dbo);
                } else if (strFrag.equalsIgnoreCase("4")) {
                    selectedarrayList3.set(position, finalSafetyReport_dbo);
                }

            }
        });

        final SafetyReport_DBO finalSafetyReport_dbo2 = safetyReport_dbo;
        holder.imgYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgGreen.getBackground().setLevel(1);
                holder.imgYellow.getBackground().setLevel(2);
                holder.imgRed.getBackground().setLevel(5);

                finalSafetyReport_dbo2.setSelectedColor("Y");
                if (strFrag.equalsIgnoreCase("1")) {
                    selectedarrayList.set(position, finalSafetyReport_dbo2);
                } else if (strFrag.equalsIgnoreCase("2")) {
                    selectedarrayList1.set(position, finalSafetyReport_dbo2);
                } else if (strFrag.equalsIgnoreCase("3")) {
                    selectedarrayList2.set(position, finalSafetyReport_dbo2);
                } else if (strFrag.equalsIgnoreCase("4")) {
                    selectedarrayList3.set(position, finalSafetyReport_dbo2);
                }

            }
        });
        final SafetyReport_DBO finalSafetyReport_dbo1 = safetyReport_dbo;
        holder.imgRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.imgGreen.getBackground().setLevel(1);
                holder.imgYellow.getBackground().setLevel(3);
                holder.imgRed.getBackground().setLevel(4);
                finalSafetyReport_dbo1.setSelectedColor("R");
                if (strFrag.equalsIgnoreCase("1")) {
                    selectedarrayList.set(position, finalSafetyReport_dbo1);
                } else if (strFrag.equalsIgnoreCase("2")) {
                    selectedarrayList1.set(position, finalSafetyReport_dbo1);
                } else if (strFrag.equalsIgnoreCase("3")) {
                    selectedarrayList2.set(position, finalSafetyReport_dbo1);
                } else if (strFrag.equalsIgnoreCase("4")) {
                    selectedarrayList3.set(position, finalSafetyReport_dbo1);
                }
            }
        });

        final SafetyReport_DBO finalSafetyReport_dbo3 = safetyReport_dbo;
        final SafetyReport_DBO finalSafetyReport_dbo4 = safetyReport_dbo;
        final SafetyReport_DBO finalSafetyReport_dbo5 = safetyReport_dbo;
        final SafetyReport_DBO finalSafetyReport_dbo6 = safetyReport_dbo;
        holder.edtRecommendation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (strFrag.equalsIgnoreCase("1")) {
                    finalSafetyReport_dbo4.setDescription(holder.edtRecommendation.getText().toString());
                    selectedarrayList.set(position, finalSafetyReport_dbo4);
                } else if (strFrag.equalsIgnoreCase("2")) {
                    finalSafetyReport_dbo3.setDescription(holder.edtRecommendation.getText().toString());
                    selectedarrayList1.set(position, finalSafetyReport_dbo3);
                } else if (strFrag.equalsIgnoreCase("3")) {
                    finalSafetyReport_dbo5.setDescription(holder.edtRecommendation.getText().toString());
                    selectedarrayList2.set(position, finalSafetyReport_dbo5);
                } else if (strFrag.equalsIgnoreCase("4")) {
                    finalSafetyReport_dbo6.setDescription(holder.edtRecommendation.getText().toString());
                    selectedarrayList3.set(position, finalSafetyReport_dbo6);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return stringArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imgGreen, imgYellow, imgRed;
        EditText edtRecommendation;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text);
            imgGreen = itemView.findViewById(R.id.imgGreen);
            imgYellow = itemView.findViewById(R.id.imgYellow);
            imgRed = itemView.findViewById(R.id.imgRed);
            edtRecommendation = itemView.findViewById(R.id.edtRecommendation);
        }
    }
}
