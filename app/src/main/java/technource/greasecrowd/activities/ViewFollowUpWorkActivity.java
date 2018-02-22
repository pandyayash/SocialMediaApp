package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.adapter.AdptFollowupWork;
import technource.greasecrowd.adapter.AdptGarageServiceType;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.interfaces.OnItemClickListener;
import technource.greasecrowd.model.Model_FollowupWork;

public class ViewFollowUpWorkActivity extends BaseActivity {

    String TAG = "ViewFollowUpWorkActivity";
    Context appContext;
    ArrayList<Model_FollowupWork> followupWorkArrayList;
    AdptFollowupWork adptFollowupWork;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_follow_up_work);
        appContext = this;
        setHeader("Follow Up Work");
        setfooter("job_details");
        setJobDetailsQuoteFooter(appContext);
        setlistenrforfooter();
        getViews();
    }

    public void getViews(){
        followupWorkArrayList = new ArrayList<>();
        Intent intent = getIntent();
        if (intent!=null){
            followupWorkArrayList = intent.getParcelableArrayListExtra("followupWorkArrayList");
            AppLog.Log(TAG,""+followupWorkArrayList.size());
        }
        recyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        setData();
    }


    public void setData() {
        adptFollowupWork = new AdptFollowupWork(followupWorkArrayList, appContext);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adptFollowupWork);

    }
}
