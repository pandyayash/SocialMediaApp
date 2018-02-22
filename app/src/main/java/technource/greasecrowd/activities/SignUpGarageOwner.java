package technource.greasecrowd.activities;

import static technource.greasecrowd.R.id.tv_skipCarSign;
import static technource.greasecrowd.activities.LoginScreen.FLAG_SIGNUP;
import static technource.greasecrowd.helper.Constants.CONDENSED_FONT;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import technource.greasecrowd.CustomViews.Widgets.CustomViewPager;
import technource.greasecrowd.R;
import technource.greasecrowd.fragment.fragmentGarageDetails;
import technource.greasecrowd.fragment.fragmentKeywords;
import technource.greasecrowd.fragment.fragmentRegisterAccount;
import technource.greasecrowd.fragment.fragmentRegisterCar;
import technource.greasecrowd.fragment.fragmentServicesOffered;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.SignUpDBO;

/**
 * Created by technource on 8/9/17.
 */

public class SignUpGarageOwner extends BaseActivity implements OnClickListener {

    public static int current_position_garage = 0;
    public String TAG = "SignUp Garage Owner";
    public TabLayout tabLayout;
    public CustomViewPager viewPager;
    public SignUpDBO data;
    public ImageView ll_back;
    public boolean isfirst= false;
    Context appContext;
    LoginDetail_DBO loginDetail_dbo;
    ViewPagerAdapter adapter;
    public TextView tv_skipCarSign;
    public String Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_garageowner);
        getViews();
    }

    public void getData() {
        Intent intent = getIntent();
        if (intent != null) {
            data = intent.getParcelableExtra("data");
        }
    }

    public void getViews() {
        appContext = this;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        viewPager = (CustomViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tv_skipCarSign = (TextView) findViewById(R.id.tv_skipGarageSign);
        viewPager.setPagingEnabled(false);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        changeTabsFont();
        ll_back = (ImageView) findViewById(R.id.ll_back);
        setOnClickListener();
        setTabLayoutWeight();
        getData();
        DisableTabLAyoutClick();
//    ((SignUpGarageOwner) appContext).tabLayout.setScrollPosition(2, 0f, true);
//   ((SignUpGarageOwner) appContext).viewPager.setCurrentItem(2);

    }

    public void setOnClickListener() {
        ll_back.setOnClickListener(this);
        tv_skipCarSign.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
            case R.id.tv_skipGarageSign:
                Fragment fr = adapter.getItem(2);

                if (FLAG_SIGNUP.equalsIgnoreCase("1")) {
                    if (fr instanceof fragmentKeywords) {
                        ((fragmentKeywords) fr).LoginCheck();
                    }
                } else {
                    if (fr instanceof fragmentKeywords) {
                        ((fragmentKeywords) fr).SocialLoginCheck();
                    }
                }

                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (current_position_garage == 0) {
            finish();
            activityTransition();
        }
       else if (current_position_garage == 5) {

        } else if (current_position_garage == 1) {
            Fragment fr = adapter.getItem(0);
            if (fr instanceof fragmentGarageDetails) {
                ((fragmentGarageDetails) fr).setGarageDetailsLayout();
            }
        } else if (current_position_garage == 2) {
            Fragment fr = adapter.getItem(0);
            if (fr instanceof fragmentGarageDetails) {
                ((fragmentGarageDetails) fr).setUserLayout();
            }
        } else if (current_position_garage == 3) {
            Fragment fr = adapter.getItem(2);
            if (fr instanceof fragmentKeywords) {
                ((fragmentKeywords) fr).setfacilitieslayout();
            }
        } else if (current_position_garage == 4) {
            Fragment fr = adapter.getItem(2);
            if (fr instanceof fragmentKeywords) {
                ((fragmentKeywords) fr).settraddinghourslayout();
            }
        }
    }

    public void DisableTabLAyoutClick() {
        LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
        tabStrip.setEnabled(false);
        for (int i = 0; i < tabStrip.getChildCount(); i++) {
            tabStrip.getChildAt(i).setClickable(false);
        }

    }

    public void setTabLayoutWeight() {
        ViewGroup slidingTabStrip = (ViewGroup) tabLayout.getChildAt(0);
        //second tab in SlidingTabStrip
        View tab1 = slidingTabStrip.getChildAt(0);
        View tab2 = slidingTabStrip.getChildAt(1);
        View tab3 = slidingTabStrip.getChildAt(2);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tab1.getLayoutParams();
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tab2.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) tab3.getLayoutParams();
        layoutParams.weight = (float) 3;
        layoutParams1.weight = (float) 3.5;
        layoutParams2.weight = (float) 2.5;
        tab1.setLayoutParams(layoutParams);
        tab2.setLayoutParams(layoutParams1);
        tab3.setLayoutParams(layoutParams2);
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new fragmentGarageDetails(), getString(R.string.garage_details));
        adapter.addFragment(new fragmentServicesOffered(), getString(R.string.services_offered));
        adapter.addFragment(new fragmentKeywords(), getString(R.string.keywords));
        viewPager.setAdapter(adapter);
    }

    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    Typeface tf = Typeface.createFromAsset(getAssets(), CONDENSED_FONT);
                    ((TextView) tabViewChild).setTypeface(tf);

                }
            }
        }
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
