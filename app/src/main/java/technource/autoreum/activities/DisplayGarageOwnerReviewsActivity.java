package technource.autoreum.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;

import java.util.ArrayList;

import technource.autoreum.R;
import technource.autoreum.helper.HelperMethods;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.LoginDetail_DBO;
import technource.autoreum.model.ReviewsDBO;

public class DisplayGarageOwnerReviewsActivity extends BaseActivity implements OnClickListener {

    Context appContext;

    LoginDetail_DBO loginDetail_dbo;
    ListView reviewList;
    ReviewAdapter adapter;
    ArrayList<ReviewsDBO> reviewArray;
    TextView nodata,distance;
    LinearLayout ll_back;
    SimpleRatingBar ratingBar;
    TextView tv_businessname, tv_ratingCount, tvrreview_count;
    ImageView Profile_omg;
    String bn = "", avg_rating = "", profile_image = "",strdistnace="",review_count="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garage_owner_reviews);
        getViews();
        setOnCLick();
        Intent intent = getIntent();
        if (intent != null) {
            reviewArray = intent.getParcelableArrayListExtra("data");
            bn = intent.getStringExtra("bn");
            avg_rating = intent.getStringExtra("avg_rating");
            profile_image = intent.getStringExtra("img");
            strdistnace = intent.getStringExtra("distnace");
            review_count = intent.getStringExtra("review_count");
            setReviewData();
        }
    }

    public void getViews() {
        appContext = this;
        setHeader(getString(R.string.reviews));
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        tv_businessname = findViewById(R.id.tv_businessname);
        tv_ratingCount = findViewById(R.id.tv_ratingCount);
        distance = findViewById(R.id.distance);
        tvrreview_count = findViewById(R.id.tvrreview_count);
        Profile_omg = findViewById(R.id.Profile_omg);
        reviewList = findViewById(R.id.reviewList);
        ll_back = findViewById(R.id.ll_back);
        ratingBar = findViewById(R.id.ratingBar);

        nodata = findViewById(R.id.nodata);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_back:
                onBackPressed();
                break;
        }
    }

    public void setOnCLick() {
        ll_back.setOnClickListener(this);
    }

    public void setAdapter() {
        adapter = new ReviewAdapter(appContext, reviewArray);
        reviewList.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        if (reviewArray.size() == 0) {
            reviewList.setVisibility(View.GONE);
            nodata.setVisibility(View.VISIBLE);
        } else {
            reviewList.setVisibility(View.VISIBLE);
            nodata.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        activityTransition();
    }

    public void setReviewData() {
        try {
            tv_businessname.setText(bn);
            tv_ratingCount.setText(avg_rating);
            distance.setText("Distance: "+strdistnace + " KM");
            tvrreview_count.setText("( "+ review_count + " Reviews )");
            ratingBar.setRating(Float.parseFloat(avg_rating));
            Glide.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + profile_image)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .skipMemoryCache(false)
                    .into(Profile_omg);
            setAdapter();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class ReviewAdapter extends BaseAdapter {

        Context context;
        ViewHolder holder;
        ArrayList<ReviewsDBO> array_gallay = new ArrayList<ReviewsDBO>();

        public ReviewAdapter(Context context, ArrayList<ReviewsDBO> array_gallay) {
            super();
            this.context = context;
            this.array_gallay = array_gallay;
        }

        @Override
        public int getCount() {
            return array_gallay.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder = new ViewHolder();

            final ReviewsDBO model = array_gallay.get(position);
            LayoutInflater inflater = LayoutInflater.from(context);

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.lost_row_reviews, null);
                holder.iv_image = convertView.findViewById(R.id.iv_image);
                holder.tv_job_title = convertView.findViewById(R.id.tv_job_title);
                holder.tv_desc = convertView.findViewById(R.id.tv_desc);
                holder.tv_address = convertView.findViewById(R.id.tv_address);
                holder.tv_name = convertView.findViewById(R.id.tv_name);
                holder.rate_count = convertView.findViewById(R.id.rate_count);
                holder.tv_response = convertView.findViewById(R.id.tv_response);
                holder.response_title = convertView.findViewById(R.id.response_title);
                holder.tv_date = convertView.findViewById(R.id.tv_date);
                holder.row_rating = convertView.findViewById(R.id.ratingBar);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_job_title.setText("Job Title: '" + model.getJobtitle() + "'");
            holder.tv_job_title.setVisibility(View.VISIBLE);
            holder.tv_desc.setText(model.getDescription());
            holder.tv_address.setText(model.getAddress());
            holder.tv_name.setText(model.getName());
            if (model.getResponse().toString().length() > 0) {
                holder.tv_response.setVisibility(View.VISIBLE);
                holder.response_title.setVisibility(View.VISIBLE);
                holder.tv_response.setText(model.getResponse());
            } else {
                holder.tv_response.setVisibility(View.VISIBLE);
                holder.response_title.setVisibility(View.VISIBLE);
            }
            holder.tv_response.setText(model.getResponse());
            holder.rate_count.setText(model.getRating());
            if (model.getRating().equalsIgnoreCase("")) {
                holder.row_rating.setRating(0);
            } else {
                holder.row_rating.setRating(Float.parseFloat(model.getRating()));
            }

            Glide.with(appContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getImage())
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.no_user)
                    .error(R.drawable.no_user)
                    .skipMemoryCache(false)
                    .into(holder.iv_image);

            return convertView;
        }

        class ViewHolder {

            TextView tv_job_title, tv_desc, tv_name, tv_address, tv_response, rate_count, response_title,tv_date;
            ImageView iv_image;
            SimpleRatingBar row_rating;
        }

    }
}
