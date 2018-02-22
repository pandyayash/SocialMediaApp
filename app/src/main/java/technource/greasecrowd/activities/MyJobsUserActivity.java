package technource.greasecrowd.activities;

import android.content.Context;
import android.graphics.Typeface;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import technource.greasecrowd.R;
import technource.greasecrowd.adapter.AdptMyJobTabs;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.MyPreference;
import technource.greasecrowd.model.LoginDetail_DBO;

public class MyJobsUserActivity extends BaseActivity {

    LinearLayout ll_back;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    MyPreference myPreference;
    LoginDetail_DBO loginDetail_dbo;
    Context appContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_jobs);
        setHeader("My Jobs");
        setfooter("jobs");
        setlistenrforfooter();
        getViews();
        ll_back = (LinearLayout) findViewById(R.id.ll_back);
        ll_back.setOnClickListener(this);

    }


    public void getViews(){
        appContext = MyJobsUserActivity.this;
        myPreference=new MyPreference(appContext);
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tabLayout = (TabLayout) findViewById(R.id.my_jobs_tablayout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setOffscreenPageLimit(3);
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

//               LinearLayout linearLayout = (LinearLayout)((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tab.getPosition());
//               TextView tabTextView = (TextView) linearLayout.getChildAt(1);
//               tabTextView.setTypeface(tabTextView.getTypeface(), Typeface.NORMAL);
//               tabTextView.setAllCaps(false);
           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });
        setAdapter();
    }
    public void showLoadingDialogFrag(boolean isShow){
        try {
            showLoadingDialog(isShow);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        changeTabsFont();
        setMyJobsFooter(this);


    }

    public void setAdapter(){
        viewPager.setAdapter(new AdptMyJobTabs(getSupportFragmentManager(),appContext));
        tabLayout.setupWithViewPager(viewPager);
        viewPager.getAdapter().notifyDataSetChanged();
    }

    /*
      *  Apply font to  tab layout heading text
      * */
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
}
