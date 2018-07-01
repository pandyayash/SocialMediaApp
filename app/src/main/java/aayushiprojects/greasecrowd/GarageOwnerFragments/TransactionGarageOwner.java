package aayushiprojects.greasecrowd.GarageOwnerFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import aayushiprojects.greasecrowd.R;

/**
 * Created by technource on 13/9/17.
 */

public class TransactionGarageOwner extends Fragment {

  View v;

  public static TransactionGarageOwner newInstance() {
    TransactionGarageOwner fragment = new TransactionGarageOwner();
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    v = inflater.inflate(R.layout.fragment_garage_transactions, container, false);

    return v;
  }
}
