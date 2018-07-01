package aayushiprojects.greasecrowd.CarOwnerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.SplashScreen;
import aayushiprojects.greasecrowd.model.SplashPojo;

/**
 * Created by technource on 13/9/17.
 */

public class HowItWorksCarOwner extends Fragment {

    View v;
    ArrayList<SplashPojo> splashPojoArrayList;
    Context appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_carowner_how_it, container, false);
        appContext = getActivity();
        Intent intent = new Intent(appContext, SplashScreen.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
        ((DashboardScreen) appContext).activityTransition();

        return v;
    }

    public static HowItWorksCarOwner newInstance() {
        HowItWorksCarOwner fragment = new HowItWorksCarOwner();
        return fragment;
    }
}
