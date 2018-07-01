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

public class NewJobCarOwner extends Fragment {

  View v;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_carowner_newjob, container, false);


    return v;
  }

  public static NewJobCarOwner newInstance() {
    NewJobCarOwner fragment = new NewJobCarOwner();
    return fragment;
  }
}
