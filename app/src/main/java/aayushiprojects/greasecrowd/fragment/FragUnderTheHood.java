package aayushiprojects.greasecrowd.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptSafetyReports;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.model.SafetyReport_DBO;

import static aayushiprojects.greasecrowd.adapter.AdptSafetyReports.selectedarrayList1;

/**
 * Created by technource on 12/2/18.
 */

public class FragUnderTheHood extends Fragment {
    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    AdptSafetyReports adptSafetyReports;
    private TextView btnDetails;
    private TextView btnPrint;
    private TextView btnSave;
    ArrayList<SafetyReport_DBO> selectedarrayList ;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_safety_report, null, false);
        getViews(view);
        return view;
    }

    public void getViews(View view){
        stringArrayList = new ArrayList<>();
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setNestedScrollingEnabled(false);
        btnSave = view.findViewById(R.id.btnSave);
        btnPrint = view.findViewById(R.id.btnPrint);
        btnDetails = view.findViewById(R.id.btnDetails);


        btnPrint.setVisibility(View.INVISIBLE);
        btnDetails.setVisibility(View.INVISIBLE);
        stringArrayList.add("Engine Oil");
        stringArrayList.add("Auto Transmission Fluid");
        stringArrayList.add("Cooling System");
        stringArrayList.add("Engine Coolant");
        stringArrayList.add("No abnormal engine noise at cold start");
        stringArrayList.add("Hoses");
        stringArrayList.add("Drive Belts");
        stringArrayList.add("Air Cond. Sys. & Operation");
        stringArrayList.add("Battery Load Test");
        stringArrayList.add("Battery Water Level/Terminals");
        stringArrayList.add("Electrical Sys. Visual Check");
        stringArrayList.add("Engine Operation");
        stringArrayList.add("Brake Fluid");
        stringArrayList.add("Brake Master Cylinder");
        stringArrayList.add("Brake Booster");
        stringArrayList.add("Brake Pipes & Connections");
        stringArrayList.add("Parking Brakes");
        stringArrayList.add("Vacuum Hose & Valve");
        stringArrayList.add("Clutch Fluid");
        stringArrayList.add("Clutch Hydraulic System");
        stringArrayList.add("Clutch Cable/Linkages");

        adptSafetyReports = new AdptSafetyReports(getActivity(), stringArrayList,"2");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptSafetyReports);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.Log("selectedarrayList",""+selectedarrayList1.size());
                HelperMethods.storeSafetyReportSharedPreferences(getActivity(), selectedarrayList1,"2");
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
