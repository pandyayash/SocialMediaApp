package technource.greasecrowd.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import technource.greasecrowd.R;
import technource.greasecrowd.activities.GenerateSafetyReportGarageActivity;
import technource.greasecrowd.fragment.FragInteriorAndExterior;
import technource.greasecrowd.fragment.FragRoadTest;
import technource.greasecrowd.fragment.FragUnderTheHood;
import technource.greasecrowd.fragment.FragUnderTheVehicle;
import technource.greasecrowd.fragment.FragmentMyJobsComplete;
import technource.greasecrowd.fragment.FragmentMyJobsNewPosted;
import technource.greasecrowd.fragment.FragmentMyJobsQuoted;

/**
 * Created by technource on 12/2/18.
 */

public class AdptsafetyReportTabs extends FragmentStatePagerAdapter {
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
        String title = " ";

        switch (position) {
            case 0:
                title = "Road Test";
                return title;
            case 1:
                title = "Under the hood";
                return title;
            case 2:
                title = "Under the vehicle";
                return title;
            case 3:
                title = "Interior & exterior";
                return title;

        }
        return null;
    }

   /* public View getTabView(int position) {
        View tab = LayoutInflater.from(mContext.inflate(R.layout.custom_tab, null);
        TextView tv = (TextView) tab.findViewById(R.id.custom_text);
        tv.setText(tabTitles[position]);
        return tab;
    }
*/

}
