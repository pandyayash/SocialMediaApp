package aayushiprojects.greasecrowd.CarOwnerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aayushiprojects.greasecrowd.R;

/**
 * Created by technource on 13/9/17.
 */

public class ChangePasswordCarOwner extends Fragment {

  View v;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_car_owner_changepass, container, false);


    return v;
  }

  public static ChangePasswordCarOwner newInstance() {
    ChangePasswordCarOwner fragment = new ChangePasswordCarOwner();
    return fragment;
  }

}
