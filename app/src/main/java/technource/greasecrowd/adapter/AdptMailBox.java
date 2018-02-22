package technource.greasecrowd.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import technource.greasecrowd.fragment.FragmentInvoice;
import technource.greasecrowd.fragment.FragmentWeekly;

/**
 * Created by technource on 12/12/17.
 */

public class AdptMailBox extends FragmentStatePagerAdapter {
    Context mContext;

    public AdptMailBox(FragmentManager fm, Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentInvoice fragmentMyJobsNewPosted = new FragmentInvoice();
                return fragmentMyJobsNewPosted;
            case 1:
                FragmentWeekly fragmentMyJobsQuoted = new FragmentWeekly();
                return fragmentMyJobsQuoted;

            default:
                FragmentInvoice fragmentMyJobsNewPosted1 = new FragmentInvoice();
                return fragmentMyJobsNewPosted1;
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
                title = "Invoice";
                return title;
            case 1:
                title = "Weekly Report";
                return title;

        }
        return null;
    }
}
