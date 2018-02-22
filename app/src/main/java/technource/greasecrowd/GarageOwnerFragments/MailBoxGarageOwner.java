package technource.greasecrowd.GarageOwnerFragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.DashboardScreen;
import technource.greasecrowd.adapter.AdptMailBox;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.MailBoxDBO;

/**
 * Created by technource on 13/9/17.
 */

public class MailBoxGarageOwner extends Fragment implements View.OnClickListener {

    public String TAG = "MyJOBSGARAGE";
    public ArrayList<MailBoxDBO> weeklyArrayList;
    public ArrayList<MailBoxDBO> invoiceArrayList;
    LinearLayout ll_back;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    public ViewPager viewPager;
    public TabLayout tabLayout;
    public AdptMailBox viewpagerAdapter;
    View v;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_garage_mailbox, container, false);
        getViews();
        setAdapter();
        changeTabsFont();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        changeTabsFont();
    }

    public static MailBoxGarageOwner newInstance() {
        MailBoxGarageOwner fragment = new MailBoxGarageOwner();
        return fragment;
    }

    public void getViews() {
        appContext = getActivity();
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tabLayout = (TabLayout) v.findViewById(R.id.my_jobs_tablayout);
        viewPager = (ViewPager) v.findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public void onClick(View view) {

    }

    public void setAdapter() {
        viewpagerAdapter = new AdptMailBox(((DashboardScreen) appContext).getSupportFragmentManager(), appContext);
        viewPager.setAdapter(viewpagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void changeTabsFont() {
        Typeface typeface = Typeface.createFromAsset(((DashboardScreen) appContext).getAssets(), Constants.Fonts.POPPINS_REGULAR);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTextSize(18f);


                }
            }
        }
    }

    private void changeTabsFontUnselectedTab() {
        Typeface typeface = Typeface.createFromAsset(((DashboardScreen) appContext).getAssets(), Constants.Fonts.POPPINS_REGULAR);
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();

        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                    ((TextView) tabViewChild).setAllCaps(false);
                    ((TextView) tabViewChild).setTextSize(18f);
                }
            }
        }
    }
}
