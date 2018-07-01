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

import static aayushiprojects.greasecrowd.adapter.AdptSafetyReports.selectedarrayList2;

/**
 * Created by technource on 12/2/18.
 */

public class FragUnderTheVehicle extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    AdptSafetyReports adptSafetyReports;
    private TextView btnDetails;
    private TextView btnPrint;
    private TextView btnSave;
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

        stringArrayList.add("Engine Oil Leaks");
        stringArrayList.add("Transmission Oil Leaks");
        stringArrayList.add("Transmission Cooler Pipes");
        stringArrayList.add("Transmission Linkages");
        stringArrayList.add("Gearbox oil Leaks");
        stringArrayList.add("Differential Oil Leaks");
        stringArrayList.add("Engine Mounts");
        stringArrayList.add("Transmission Mounts");
        stringArrayList.add("Exhaust System");
        stringArrayList.add("Drive Shafts");
        stringArrayList.add("Universal Joints");
        stringArrayList.add("C.V. Joints");
        stringArrayList.add("Front Shock Absorbers");
        stringArrayList.add("Rear Shock Absorbers");
        stringArrayList.add("Springs");
        stringArrayList.add("Spring Mounts & Shackles");
        stringArrayList.add("Bail Joints");
        stringArrayList.add("Tie Rods");
        stringArrayList.add("Idler/Pitman Arms");
        stringArrayList.add("Suspension Arms");
        stringArrayList.add("Steering Box/Rack");
        stringArrayList.add("Steering Box/Rack Oil Leaks");
        stringArrayList.add("Wheels & Tyres (Inc. Spare)");
        stringArrayList.add("Check Tyre Pressure");
        stringArrayList.add("Front Pads/Shoes");
        stringArrayList.add("Front Disc/Drums");
        stringArrayList.add("Front Callipers/Wheel Cylinders");
        stringArrayList.add("Front Hoses, Pipes, Clips & Springs");
        stringArrayList.add("Rear Pads/Shoes");
        stringArrayList.add("Rear Disc");
        stringArrayList.add("Rear Callipers/Wheel Cylinders");
        stringArrayList.add("Hand Brake Cables & Linkages");

        adptSafetyReports = new AdptSafetyReports(getActivity(),stringArrayList,"3");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptSafetyReports);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.Log("selectedarrayList",""+selectedarrayList2.size());
                HelperMethods.storeSafetyReportSharedPreferences(getActivity(), selectedarrayList2,"3");
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
