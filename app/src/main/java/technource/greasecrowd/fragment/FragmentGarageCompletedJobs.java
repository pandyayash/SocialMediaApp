package technource.greasecrowd.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import technource.greasecrowd.CustomViews.BottomOffsetDecoration;
import technource.greasecrowd.R;
import technource.greasecrowd.activities.MyJobsGarageActivity;
import technource.greasecrowd.adapter.AdptNewPostedJobs;

/**
 * Created by technource on 23/1/18.
 */

public class FragmentGarageCompletedJobs extends Fragment {

    public String TAG = "FragmentMyJobsNewPosted";
    Activity appContext;
    RecyclerView jobsRecyclerview;
    TextView textView;
    AdptNewPostedJobs adptNewPostedJobs;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag_my_jobs_new_posted, null, false);
        getViews(view);
        setData();
        return view;
    }


    public void getViews(View rootView) {
        appContext = getActivity();
        jobsRecyclerview = (RecyclerView) rootView.findViewById(R.id.jobs_recyclerview);
        textView = (TextView) rootView.findViewById(R.id.text);
        textView.setText("You don't have any new jobs.");
        textView.setVisibility(View.GONE);
    }

    public void setData() {

        if (((MyJobsGarageActivity) appContext).postedJobDboArrayList_Completed.size() > 0) {
            textView.setVisibility(View.GONE);
            jobsRecyclerview.setVisibility(View.VISIBLE);
            adptNewPostedJobs = new AdptNewPostedJobs(((MyJobsGarageActivity) appContext).postedJobDboArrayList_Completed, appContext);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
            jobsRecyclerview.setLayoutManager(mLayoutManager);
            jobsRecyclerview.setItemAnimator(new DefaultItemAnimator());
            jobsRecyclerview.setAdapter(adptNewPostedJobs);
            float offsetPx = getResources().getDimension(R.dimen.txt_size_6);
            BottomOffsetDecoration bottomOffsetDecoration = new BottomOffsetDecoration((int) offsetPx);
            jobsRecyclerview.addItemDecoration(bottomOffsetDecoration);
        } else {
            textView.setVisibility(View.VISIBLE);
            jobsRecyclerview.setVisibility(View.GONE);
        }

    }
}
