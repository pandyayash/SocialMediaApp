package technource.greasecrowd.activities;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import technource.greasecrowd.R;

public class GarageOwnerProfileMapActivity extends BaseActivity implements OnMapReadyCallback,View.OnClickListener {

    private GoogleMap mMap;
    double lat, lng;
    String subrub, state;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_profile_map);

        getviews();
        setonclicklistener();


    }

    private void setonclicklistener() {
        ll_back.setOnClickListener(this);

    }

    private void getviews() {

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ll_back = (LinearLayout) findViewById(R.id.ll_back);

        Bundle b = getIntent().getExtras();
        if (b != null) {
            lat = b.getDouble("lat");
            lng = b.getDouble("lng");
            subrub = b.getString("subrub");
            state = b.getString("state");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng adress = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(adress, 12.0f));
        MarkerOptions marker = new MarkerOptions()
                .title(state + " " + subrub)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET))
                .position(adress);
        mMap.addMarker(marker);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                activityTransition();
                break;
        }
    }
    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }


}
