package technource.greasecrowd.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import technource.greasecrowd.fragment.FragmentMyJobsComplete;
import technource.greasecrowd.fragment.FragmentMyJobsNewPosted;
import technource.greasecrowd.fragment.FragmentMyJobsQuoted;

/**
 * Created by technource on 12/12/17.
 */

public class AdptMyJobTabs extends FragmentStatePagerAdapter {
    Context mContext;

    public AdptMyJobTabs(FragmentManager fm,Context mContext) {
        super(fm);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                FragmentMyJobsNewPosted fragmentMyJobsNewPosted = new FragmentMyJobsNewPosted();
                return fragmentMyJobsNewPosted;
            case 1:
                FragmentMyJobsQuoted fragmentMyJobsQuoted = new FragmentMyJobsQuoted();
                return fragmentMyJobsQuoted;
            case 2:
                FragmentMyJobsComplete fragmentMyJobsComplete = new FragmentMyJobsComplete();
                return fragmentMyJobsComplete;
            default:
                FragmentMyJobsNewPosted fragmentMyJobsNewPosted1 = new FragmentMyJobsNewPosted();
                return fragmentMyJobsNewPosted1;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";

        switch (position){
            case 0:
                title = "New Posted";
                return title;
            case 1:
                title = "Quoted";
                return title;
            case 2:
                title = "Completed";//Archived
                return title;

        }
        return null;
    }
}
