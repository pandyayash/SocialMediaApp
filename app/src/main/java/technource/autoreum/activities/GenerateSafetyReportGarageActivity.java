package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import technource.autoreum.R;
import technource.autoreum.adapter.AdptsafetyReportTabs;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.model.LoginDetail_DBO;

public class GenerateSafetyReportGarageActivity extends BaseActivity {

    LinearLayout ll_back;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    public static String jid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        Intent intent = getIntent();
        if (intent!= null){
            jid = intent.getStringExtra("jid");
        }
        getViews();
        setHeader("Safety Report");
        setfooter("garageowner");
        setMyJObFooterGarage(appContext);
        setlistenrforfooter();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        setAdapter();

    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTabsFont();
        setMyJObFooterGarage(this);
    }
    public void ShowLoadingDialog(boolean isShow){
        showLoadingDialog(isShow);
    }
    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tabLayout = (TabLayout) findViewById(R.id.my_jobs_tablayout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
        //tabLayout.setTabGravity(TabLayout.MODE_FIXED);
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

    public void setAdapter() {
        AdptsafetyReportTabs adptsafetyReportTabs = new AdptsafetyReportTabs(getSupportFragmentManager(), appContext);
        viewPager.setAdapter(adptsafetyReportTabs);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    private void changeTabsFont() {
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.Fonts.POPPINS_REGULAR);
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
                   // ((TextView) tabViewChild).setTextSize(10f);
                }
            }
        }
    }

/*
    public class AdptsafetyReportTabs extends FragmentStatePagerAdapter {
        String tabTitles[] = new String[] { "Road Test", "Under the hood", "Under the vehicle", "Interior and exterior"};

        Context mContext;

        public AdptsafetyReportTabs(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragRoadTest fragRoadTest = new FragRoadTest();
                    return fragRoadTest;
                case 1:
                    FragUnderTheHood fragUnderTheHood = new FragUnderTheHood();
                    return fragUnderTheHood;
                case 2:
                    FragUnderTheVehicle fragUnderTheVehicle = new FragUnderTheVehicle();
                    return fragUnderTheVehicle;
                case 3:
                    FragInteriorAndExterior fragInteriorAndExterior = new FragInteriorAndExterior();
                    return fragInteriorAndExterior;
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

    public View getTabView(int position) {
        View tab = LayoutInflater.from(GenerateSafetyReportGarageActivity.this).inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) tab.findViewById(R.id.custom_text);
        tv.setText(tabTitles[position]);
        return tab;
    }

    }*/
}
