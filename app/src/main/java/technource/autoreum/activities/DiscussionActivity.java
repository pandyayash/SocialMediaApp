package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import technource.autoreum.CustomViews.Widgets.CustomViewPager;
import technource.autoreum.R;
import technource.autoreum.fragment.FragmentDiscussionPrivate;
import technource.autoreum.fragment.FragmentDiscussionPublic;
import technource.autoreum.helper.Constants;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.MyPreference;
import technource.autoreum.model.LoginDetail_DBO;

import static technource.autoreum.helper.Constants.notify_count_footer;

public class DiscussionActivity extends BaseActivity {

    LinearLayout ll_back;
    private CustomViewPager viewPager;
    private TabLayout tabLayout;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;
    TextView cart_badge_footer;


    public String discusionfourjobid = "", currentjobStatus = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discussion);
        setHeader("Discussion");
        setfooter("job_details");
        setlistenrforfooter();
        getViews();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("job_id") && intent.hasExtra("job_status")) {
                discusionfourjobid = intent.getStringExtra("job_id");
                currentjobStatus = intent.getStringExtra("job_status");
                if (!(((DiscussionActivity) appContext).currentjobStatus.equalsIgnoreCase("Drop Off") || ((DiscussionActivity) appContext).currentjobStatus.equalsIgnoreCase("Complete") || ((DiscussionActivity) appContext).currentjobStatus.equalsIgnoreCase("Awarded") || ((DiscussionActivity) appContext).currentjobStatus.equalsIgnoreCase("Pickup") || ((DiscussionActivity) appContext).currentjobStatus.equalsIgnoreCase("WorkDone"))) {
                    LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
                    tabStrip.setEnabled(false);
                    tabStrip.getChildAt(1).setClickable(false);
                    viewPager.setPagingEnabled(false);
                } else {
                    LinearLayout tabStrip = ((LinearLayout) tabLayout.getChildAt(0));
                    tabStrip.setEnabled(true);
                    tabStrip.getChildAt(1).setClickable(true);
                    viewPager.setPagingEnabled(true);
                }

            }
            if (intent.hasExtra("flag")) {
                tabLayout.setScrollPosition(Integer.parseInt(intent.getStringExtra("flag")), 0f, true);
                viewPager.setCurrentItem(Integer.parseInt(intent.getStringExtra("flag")));
            }
            if (intent.hasExtra("job_id")) {
                discusionfourjobid = intent.getStringExtra("job_id");
            }
        }
    }


    public void getViews() {
        appContext = this;
        myPreference = new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);

        cart_badge_footer = (TextView) findViewById(R.id.cart_badge_footer);
        cart_badge_footer.setText(notify_count_footer);
        tabLayout = (TabLayout) findViewById(R.id.my_jobs_tablayout);
        viewPager = (CustomViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //viewPager.setSaveFromParentEnabled(false);
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


        setAdapter();
    }

    public void showLoadingDialogFrag(boolean isShow) {
        try {
            showLoadingDialog(isShow);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeTabsFont();
        setJobDetailsDiscussionFooter(this);
    }

    public void setAdapter() {
        viewPager.setAdapter(new Discussion(getSupportFragmentManager(), appContext));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void onClick(View view) {
        super.onClick(view);
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
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
                    ((TextView) tabViewChild).setTextSize(18f);
                }
            }
        }
    }

    private void changeTabsFontUnselectedTab() {
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
                    ((TextView) tabViewChild).setTextSize(18f);
                }
            }
        }
    }

    public class Discussion extends FragmentStatePagerAdapter {
        Context mContext;

        public Discussion(FragmentManager fm, Context mContext) {
            super(fm);
            this.mContext = mContext;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    FragmentDiscussionPublic fragmentdiscussionpublic = new FragmentDiscussionPublic();
                    return fragmentdiscussionpublic;
                case 1:
                    FragmentDiscussionPrivate fragmentdiscussionprivate = new FragmentDiscussionPrivate();
                    return fragmentdiscussionprivate;

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String title = " ";

            switch (position) {
                case 0:
                    title = "Public";
                    return title;
                case 1:
                    title = "Private";
                    return title;

            }
            return null;
        }
    }


}
