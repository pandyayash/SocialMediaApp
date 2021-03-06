package aayushiprojects.greasecrowd.GarageOwnerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.SplashScreen;

/**
 * Created by technource on 13/9/17.
 */

public class HowitWorksGarageOwner extends Fragment {

    View v;
    Context appContext;

    public static HowitWorksGarageOwner newInstance() {
        HowitWorksGarageOwner fragment = new HowitWorksGarageOwner();
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_how_it_works, container, false);
        appContext = getActivity();


        Intent intent = new Intent(appContext, SplashScreen.class);
        intent.putExtra("flag", "1");
        startActivity(intent);
        ((DashboardScreen) appContext).activityTransition();
        return v;
    }
}
