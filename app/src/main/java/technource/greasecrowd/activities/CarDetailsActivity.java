package technource.greasecrowd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;

import technource.greasecrowd.R;

public class CarDetailsActivity extends BaseActivity {

    LinearLayout ll_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_details);

        setHeader("Car Details");
        getviews();
        setonclicklistener();
    }

    private void getviews() {

        ll_back = (LinearLayout)findViewById(R.id.ll_back);
    }

    private void setonclicklistener() {

        ll_back.setOnClickListener(this);
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }
}
