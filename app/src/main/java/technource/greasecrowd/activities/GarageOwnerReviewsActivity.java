package technource.greasecrowd.activities;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.iarcuschin.simpleratingbar.SimpleRatingBar;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import technource.greasecrowd.R;
import technource.greasecrowd.helper.AppLog;
import technource.greasecrowd.helper.Connectivity;
import technource.greasecrowd.helper.Constants;
import technource.greasecrowd.helper.Constants.GALLARY;
import technource.greasecrowd.helper.Constants.Reviews;
import technource.greasecrowd.helper.Constants.SIGN_UP;
import technource.greasecrowd.helper.CustomJsonObjectRequest;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.GallaryDBO;
import technource.greasecrowd.model.LoginDetail_DBO;
import technource.greasecrowd.model.ReviewsDBO;

public class GarageOwnerReviewsActivity extends BaseActivity implements OnClickListener {

  Context appContext;

  LoginDetail_DBO loginDetail_dbo;
  ListView reviewList;
  ReviewAdapter adapter;
  ArrayList<ReviewsDBO> reviewArray = new ArrayList<>();
  TextView nodata;
  LinearLayout ll_back;
  SimpleRatingBar ratingBar;
  TextView tv_businessname, tv_ratingCount, tvrreview_count;
  ImageView Profile_omg;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_garage_owner_reviews);
    getViews();
    setOnCLick();
    if (Connectivity.isConnected(appContext)) {
      GetGarageReviews();
    } else {
      showAlertDialog(getResources().getString(R.string.no_internet));
    }
  }

  public void getViews() {
    appContext = this;
    setHeader(getString(R.string.reviews));
    loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);

    tv_businessname = (TextView) findViewById(R.id.tv_businessname);
    tv_ratingCount = (TextView) findViewById(R.id.tv_ratingCount);
    tvrreview_count = (TextView) findViewById(R.id.tvrreview_count);
    Profile_omg = (ImageView) findViewById(R.id.Profile_omg);

    ll_back = (LinearLayout) findViewById(R.id.ll_back);
    ratingBar = (SimpleRatingBar) findViewById(R.id.ratingBar);
    adapter = new ReviewAdapter(appContext, reviewArray);
    reviewList = (ListView) findViewById(R.id.reviewList);
    reviewList.setAdapter(adapter);
    nodata = (TextView) findViewById(R.id.nodata);
  }

  @Override
  public void onClick(View view) {
    switch (view.getId()) {
      case R.id.ll_back:
        onBackPressed();
        break;
    }
  }

  public void setOnCLick(){
    ll_back.setOnClickListener(this);
  }

  public void setAdapter() {

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

  public void GetGarageReviews() {
    reviewArray.clear();
    showLoadingDialog(true);
    RequestQueue queue = Volley.newRequestQueue(appContext);
    String url = WebServiceURLs.BASE_URL;
    AppLog.Log("TAG", "App URL : " + url);

    Map<String, String> params = new HashMap<String, String>();
    params.put(GALLARY.SERVICE_NAME, "ga_ow_rev");

    AppLog.Log("TAG", "Params : " + params);
    CustomJsonObjectRequest jsonObjReq = new CustomJsonObjectRequest(Request.Method.POST,
        url, appContext, new JSONObject(params),
        new Response.Listener<JSONObject>() {
          @Override
          public void onResponse(JSONObject response) {
            showLoadingDialog(false);
            AppLog.Log("params", "" + response);
            try {
              String status = response.getString(Constants.STATUS);

              if (status.equalsIgnoreCase(Constants.SUCCESS)) {
                setReviewData(response.toString());
              } else {
                showAlertDialog(response.getString(Constants.MESSAGE));
              }
            } catch (Exception e) {
              e.printStackTrace();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            showLoadingDialog(false);
          }
        });
    queue.add(jsonObjReq);

  }

  public void setReviewData(String response) {
    try {
      JSONObject json = new JSONObject(response);
      tv_businessname.setText(json.getString(Reviews.BN));
      tv_ratingCount.setText(json.getString(Reviews.AVG_RATING));
      ratingBar.setRating(Float.parseFloat(json.getString(Reviews.AVG_RATING)));
      Glide.with(appContext)
          .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + json.getString(Reviews.IMG))
          .asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .placeholder(R.drawable.no_user)
          .error(R.drawable.no_user)
          .skipMemoryCache(false)
          .into(Profile_omg);

      JSONArray jarray = new JSONArray(json.getString("reviews"));

      for (int i = 0; i < jarray.length(); i++) {
        JSONObject jobj = jarray.getJSONObject(i);
        ReviewsDBO model = new ReviewsDBO();
        model.setName(jobj.getString(Reviews.NAME));
        model.setAddress(jobj.getString(Reviews.LoC));
        model.setJobtitle(jobj.getString(Reviews.TITLE));
        model.setDescription(
            jobj.getString(Reviews.REVIEW));
        model.setImage(jobj.getString((Reviews.IMAGE)));
        model.setRating(jobj.getString((Reviews.RATING)));
        JSONArray jarry2 = new JSONArray(jobj.getString("job_user_review_replies"));

        if (jarry2.length() == 0) {
          model.setResponse("");

        } else {
          JSONObject joj2 = jarry2.getJSONObject(0);
          model.setResponse(joj2.getString("review_text"));
        }
        reviewArray.add(model);
      }

      tvrreview_count.setText("(" + reviewArray.size() + " Reviews)");
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
        holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
        holder.tv_job_title = (TextView) convertView.findViewById(R.id.tv_job_title);
        holder.tv_desc = (TextView) convertView.findViewById(R.id.tv_desc);
        holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
        holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
        holder.rate_count = (TextView) convertView.findViewById(R.id.rate_count);
        holder.tv_response = (TextView) convertView.findViewById(R.id.tv_response);
        holder.response_title = (TextView) convertView.findViewById(R.id.response_title);
        holder.row_rating = (SimpleRatingBar) convertView.findViewById(R.id.ratingBar);
        convertView.setTag(holder);
      } else {
        holder = (ViewHolder) convertView.getTag();
      }

      holder.tv_job_title.setText("Job Title: '" + model.getJobtitle() + "'");
      holder.tv_desc.setText(model.getDescription());
      holder.tv_address.setText(model.getAddress());
      holder.tv_name.setText(model.getName());
      if (model.getResponse().toString().length() > 0) {
        holder.tv_response.setVisibility(View.VISIBLE);
        holder.response_title.setVisibility(View.VISIBLE);
        holder.tv_response.setText(model.getResponse());
      } else {
        holder.tv_response.setVisibility(View.GONE);
        holder.response_title.setVisibility(View.GONE);
      }
      holder.tv_response.setText(model.getResponse());
      holder.rate_count.setText(model.getRating());
      holder.row_rating.setRating(Float.parseFloat(model.getRating()));
      Glide.with(appContext)
          .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + model.getImage())
          .asBitmap()
          .diskCacheStrategy(DiskCacheStrategy.NONE)
          .placeholder(R.drawable.no_user)
          .error(R.drawable.no_user)
          .skipMemoryCache(false)
          .into(holder.iv_image);

      return convertView;
    }

    class ViewHolder {

      TextView tv_job_title, tv_desc, tv_name, tv_address, tv_response, rate_count, response_title;
      ImageView iv_image;
      SimpleRatingBar row_rating;
    }

  }
}
