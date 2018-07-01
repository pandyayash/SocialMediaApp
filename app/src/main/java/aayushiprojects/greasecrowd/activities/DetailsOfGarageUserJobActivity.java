package aayushiprojects.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.model.DetailsOfGarageCarJobDBO;

public class DetailsOfGarageUserJobActivity extends BaseActivity {

    private TextView txtGarageName;
    private TextView txtGarageAddress;
    private TextView txtGarageMobile;
    private TextView txtGarageEmail;
    private TextView txtGarageAbn;
    private TextView txtClientName;
    private TextView txtClientSubrub;
    private TextView txtClientAddress;
    private TextView txtClientMobile;
    private TextView txtClientEmail;
    private TextView txtCarMake;
    private TextView txtCarModel;
    private TextView txtCarBadge;
    private TextView txtCarRegNum;
    private TextView txtCarType;
    Context appContext;
    DetailsOfGarageCarJobDBO data;
    LinearLayout ll_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_of_garage_user_job);
        findViews();
        setInIntent();
        setHeader("Details");
    }

    private void findViews() {
        appContext = this;
        txtGarageName = findViewById(R.id.txtGarageName);
        txtGarageAddress = findViewById(R.id.txtGarageAddress);
        txtGarageMobile = findViewById(R.id.txtGarageMobile);
        txtGarageEmail = findViewById(R.id.txtGarageEmail);
        txtGarageAbn = findViewById(R.id.txtGarageAbn);
        txtClientName = findViewById(R.id.txtClientName);
        txtClientSubrub = findViewById(R.id.txtClientSubrub);
        txtClientAddress = findViewById(R.id.txtClientAddress);
        txtClientMobile = findViewById(R.id.txtClientMobile);
        txtClientEmail = findViewById(R.id.txtClientEmail);
        txtCarMake = findViewById(R.id.txtCarMake);
        txtCarModel = findViewById(R.id.txtCarModel);
        txtCarBadge = findViewById(R.id.txtCarBadge);
        txtCarRegNum = findViewById(R.id.txtCarRegNum);
        txtCarType = findViewById(R.id.txtCarType);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setInIntent() {

        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("data")) {
                data = intent.getParcelableExtra("data");
            }
        }

        txtGarageName.setText(data.getBussinessName() + "-" + data.getGarageSubrub());

        txtGarageAddress.setText(data.getGarageState() + " " + data.getGaragePostcode());
        txtGarageMobile.setText("Mobile : " + data.getGarageMobile());
        txtGarageEmail.setText("Email : " + data.getGarageEmail());
        txtGarageAbn.setText("ABN : " + data.getGarageAbnNo());


        txtClientName.setText(data.getFname() + " " + data.getLname());
        txtClientSubrub.setText(data.getSubrub());
        txtClientAddress.setText(data.getState() + " " + data.getPostcode());
        txtClientMobile.setText("Mobile : " + data.getMobile());
        txtClientEmail.setText("Email : " + data.getEmail());


        txtCarMake.setText("Make : " + data.getCarMake());
        txtCarModel.setText("Model : " + data.getCarModel());
        txtCarBadge.setText("Badge : " + data.getCarBadge());
        txtCarRegNum.setText("Reg No. : " + data.getRegistrationNumber());
        txtCarType.setText("Transmission : " + data.getCarType());


    }
}
