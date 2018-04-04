package technource.autoreum.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import technource.autoreum.fragment.FragmentGarageAwardedJobs;
import technource.autoreum.fragment.FragmentGarageCompletedJobs;
import technource.autoreum.fragment.FragmentGarageQuotedJobs;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.model.LoginDetail_DBO;

/**
 * Created by technource on 23/1/18.
 */

public class AdptMyJobTabsgarage extends FragmentStatePagerAdapter {
    Context mContext;
    LoginDetail_DBO loginDetail_dbo;

    public AdptMyJobTabsgarage(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(this.mContext);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentGarageQuotedJobs fragmentGarageQuotedJobs = new FragmentGarageQuotedJobs();
                return fragmentGarageQuotedJobs;
            case 1:
                FragmentGarageAwardedJobs fragmentgarageawardedjobs = new FragmentGarageAwardedJobs();
                return fragmentgarageawardedjobs;
            case 2:
                FragmentGarageCompletedJobs fragmentgarageawardedjobs1 = new FragmentGarageCompletedJobs();
                return fragmentgarageawardedjobs1;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title = " ";

        switch (position) {
            case 0:
                title = "Quoted";
                return title;
            case 1:
                title = "Awarded";
                return title;
            case 2:
                title = "Completed";//Archived
                return title;

        }
        return null;
    }
}


