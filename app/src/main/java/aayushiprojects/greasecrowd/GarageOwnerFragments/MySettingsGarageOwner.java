package aayushiprojects.greasecrowd.GarageOwnerFragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.ChangepasswordActivity;
import aayushiprojects.greasecrowd.activities.DashboardScreen;
import aayushiprojects.greasecrowd.activities.NotificationActivity;
import aayushiprojects.greasecrowd.activities.PaypalActivity;
import aayushiprojects.greasecrowd.helper.Constants;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

/**
 * Created by technource on 13/9/17.
 */

public class MySettingsGarageOwner extends Fragment implements View.OnClickListener {

    View v, view_pass, view_pay;
    LinearLayout ll_notification, ll_password, ll_paypal;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_settings, container, false);
        getviews();
        setOnClickListener();
        ((DashboardScreen)appContext).NoFooter();
        checkforlogintype();
        return v;
    }

    private void checkforlogintype() {
        if (loginDetail_dbo.getLogin_type().equalsIgnoreCase("2")) {
            ll_password.setVisibility(View.GONE);
            view_pass.setVisibility(View.GONE);
        }
    }

    private void getviews() {
        appContext = getActivity();
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        ll_notification = v.findViewById(R.id.ll_notification);
        ll_password = v.findViewById(R.id.ll_password);
        ll_paypal = v.findViewById(R.id.ll_paypal);
        view_pass = v.findViewById(R.id.view_pass);
        view_pay = v.findViewById(R.id.pay_view);

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase(Constants.CAR_OWNER)) {
            ll_paypal.setVisibility(View.GONE);
            view_pay.setVisibility(View.GONE);
        }
    }

    public void setOnClickListener() {
        ll_notification.setOnClickListener(this);
        ll_password.setOnClickListener(this);
        ll_paypal.setOnClickListener(this);
    }

    public static MySettingsGarageOwner newInstance() {
        MySettingsGarageOwner fragment = new MySettingsGarageOwner();
        return fragment;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_notification:
                Intent intent = new Intent(getActivity(), NotificationActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.ll_password:
                intent = new Intent(getActivity(), ChangepasswordActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
            case R.id.ll_paypal:
                intent = new Intent(getActivity(), PaypalActivity.class);
                startActivity(intent);
                ((DashboardScreen) appContext).activityTransition();
                break;
        }
    }

}
