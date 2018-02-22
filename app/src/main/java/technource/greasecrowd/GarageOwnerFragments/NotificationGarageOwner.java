package technource.greasecrowd.GarageOwnerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import technource.greasecrowd.R;

/**
 * Created by technource on 13/9/17.
 */

public class NotificationGarageOwner extends Fragment {

  View v;
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_garage_notification, container, false);


    return v;
  }

  public static NotificationGarageOwner newInstance() {
    NotificationGarageOwner fragment = new NotificationGarageOwner();
    return fragment;
  }
}
