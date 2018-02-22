package technource.greasecrowd.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import technource.greasecrowd.R;
import technource.greasecrowd.model.DetailsOfGarageCarJobDBO;

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
        txtGarageName = (TextView) findViewById(R.id.txtGarageName);
        txtGarageAddress = (TextView) findViewById(R.id.txtGarageAddress);
        txtGarageMobile = (TextView) findViewById(R.id.txtGarageMobile);
        txtGarageEmail = (TextView) findViewById(R.id.txtGarageEmail);
        txtGarageAbn = (TextView) findViewById(R.id.txtGarageAbn);
        txtClientName = (TextView) findViewById(R.id.txtClientName);
        txtClientSubrub = (TextView) findViewById(R.id.txtClientSubrub);
        txtClientAddress = (TextView) findViewById(R.id.txtClientAddress);
        txtClientMobile = (TextView) findViewById(R.id.txtClientMobile);
        txtClientEmail = (TextView) findViewById(R.id.txtClientEmail);
        txtCarMake = (TextView) findViewById(R.id.txtCarMake);
        txtCarModel = (TextView) findViewById(R.id.txtCarModel);
        txtCarBadge = (TextView) findViewById(R.id.txtCarBadge);
        txtCarRegNum = (TextView) findViewById(R.id.txtCarRegNum);
        txtCarType = (TextView) findViewById(R.id.txtCarType);
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
