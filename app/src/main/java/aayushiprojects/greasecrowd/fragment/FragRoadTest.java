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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.adapter.AdptSafetyReports;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.SharedPreference;

import static aayushiprojects.greasecrowd.adapter.AdptSafetyReports.selectedarrayList;

/**
 * Created by technource on 12/2/18.
 */

public class FragRoadTest extends Fragment {

    RecyclerView recyclerView;
    ArrayList<String> stringArrayList;
    AdptSafetyReports adptSafetyReports;
    LinearLayout ll_indication;
    private TextView btnDetails;
    private TextView btnPrint;
    private TextView btnSave;
    ArrayList<Integer> favoriteList;
    SharedPreference preference;
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
        ll_indication = view.findViewById(R.id.ll_indication);
        ll_indication.setVisibility(View.VISIBLE);
        btnSave = view.findViewById(R.id.btnSave);
        btnPrint = view.findViewById(R.id.btnPrint);
        btnDetails = view.findViewById(R.id.btnDetails);


        btnPrint.setVisibility(View.INVISIBLE);
        btnDetails.setVisibility(View.INVISIBLE);
        stringArrayList.add("Noise and Vibrations");
        stringArrayList.add("Power Train Operation");

        adptSafetyReports = new AdptSafetyReports(getActivity(),stringArrayList,"1");
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptSafetyReports);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppLog.Log("selectedarrayList",""+selectedarrayList.size());
                HelperMethods.storeSafetyReportSharedPreferences(getActivity(), selectedarrayList,"1");
                Toast.makeText(getActivity(), getString(R.string.saved), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
