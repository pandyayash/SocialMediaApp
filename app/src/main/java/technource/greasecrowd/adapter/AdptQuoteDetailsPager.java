package technource.greasecrowd.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import me.relex.circleindicator.CircleIndicator;
import technource.greasecrowd.R;
import technource.greasecrowd.helper.HelperMethods;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.interfaces.OnItemClickListener;
import technource.greasecrowd.model.AwardJobDBOCarOwner;
import technource.greasecrowd.model.CarImageDBO;
import technource.greasecrowd.model.JobDetail_DBO;
import technource.greasecrowd.model.LoginDetail_DBO;

import static technource.greasecrowd.activities.BaseActivity.setListViewHeightBasedOnChildren;

/**
 * Created by technource on 22/1/18.
 */

public class AdptQuoteDetailsPager extends PagerAdapter {

    TextView cat1, cat2;
    Context appContext;
    LinearLayout ll_cat1, ll_cat2, linear, ll_upload_photos;
    int arraylistpos;
    ListView lvFree;
    private CustomAdapter adapter;
    private CardView mediaCardView;
    private ImageView avatarImg;
    private TextView txtUserName, txtPrice, txtSmallPrice, txtSmallStatus, txtQuoteFrom;
    private RatingBar ratingBar;
    private TextView txtRating;
    private TextView txtReview;
    private TextView txtDistance;
    private TextView txtGarageComment;
    private TextView txtOfferPrice;
    private TextView txtAcceptAdditionalOffer;
    private RecyclerView freeInclusionrecyclerview;
    private TextView txtJobTitle;
    private TextView txtJobNumber;
    private TextView txtCategory;
    private TextView txtSubCategory;
    private TextView txtcar;
    private TextView txtJobDescription;
    private TextView txtQuoteDescription;
    private TextView txtAdditionalDescription;
    private TextView txtServiceInclusion;
    private TextView txtFreeInclusion;
    private TextView btnAward;
    private TextView btnInvoice;
    private TextView btnPaymentOption;
    private TextView btnPrintQuote;
    private TextView btnOfferAccept;
    private TextView txtAddOfferPrice;
    private TextView btnUploadImgs;
    private TextView btnGenerateSafetyReport;
    private TextView txtFollowupWork;
    private CheckBox checkbox;
    public static ViewPager viewpager;
    public static CircleIndicator indicator;
    public static String juId,car_Id,jobTitle;


    private ArrayList<View> views = new ArrayList<View>();
    OnItemClickListener itemClickListener;
    LoginDetail_DBO loginDetail_dbo;
    private ArrayList<String> stringArrayList;
    private ArrayList<CarImageDBO> carImageDBOArrayList;
    ViewPagerAdapterImage pagerAdapterImage;

    public AdptQuoteDetailsPager(Context appContext,OnItemClickListener itemClickListener) {
        this.appContext = appContext;
        this.itemClickListener = itemClickListener;
        loginDetail_dbo = HelperMethods.getUserDetailsSharedPreferences(appContext);
        stringArrayList = new ArrayList<>();
        carImageDBOArrayList = new ArrayList<>();
    }

    @Override
    public int getItemPosition(Object object) {
        int index = views.indexOf(object);
        if (index == -1) {
            return POSITION_NONE;
        } else {

            return index;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v = views.get(position);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(views.get(position));
    }


    @Override
    public int getCount() {
        return views.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }


    public int addView(View v, final int position, ArrayList<AwardJobDBOCarOwner> awardJob, JobDetail_DBO jobDetails) {

        mediaCardView = (CardView) v.findViewById(R.id.media_card_view);
        txtQuoteFrom = (TextView) v.findViewById(R.id.txtQuoteFrom);
        avatarImg = (ImageView) v.findViewById(R.id.avatar_img);
        txtUserName = (TextView) v.findViewById(R.id.txtUserName);
        ratingBar = (RatingBar) v.findViewById(R.id.ratingBar);
        txtRating = (TextView) v.findViewById(R.id.txtRating);
        txtReview = (TextView) v.findViewById(R.id.txtReview);
        txtDistance = (TextView) v.findViewById(R.id.txtDistance);
        txtGarageComment = (TextView) v.findViewById(R.id.txtGarageComment);
        txtOfferPrice = (TextView) v.findViewById(R.id.txtOfferPrice);
        txtAcceptAdditionalOffer = (TextView) v.findViewById(R.id.txtAcceptAdditionalOffer);
        freeInclusionrecyclerview = (RecyclerView) v.findViewById(R.id.freeInclusionrecyclerview);
        txtJobTitle = (TextView) v.findViewById(R.id.txtJobTitle);
        txtJobNumber = (TextView) v.findViewById(R.id.txtJobNumber);
        txtCategory = (TextView) v.findViewById(R.id.txtCategory);
        txtSubCategory = (TextView) v.findViewById(R.id.txtSubCategory);
        txtcar = (TextView) v.findViewById(R.id.txtcar);
        txtJobDescription = (TextView) v.findViewById(R.id.txtJobDescription);
        txtQuoteDescription = (TextView) v.findViewById(R.id.txtQuoteDescription);
        txtAdditionalDescription = (TextView) v.findViewById(R.id.txtAdditionalDescription);
        txtServiceInclusion = (TextView) v.findViewById(R.id.txtServiceInclusion);
        txtFreeInclusion = (TextView) v.findViewById(R.id.txtFreeInclusion);
        btnAward = (TextView) v.findViewById(R.id.btnAward);
        btnInvoice = (TextView) v.findViewById(R.id.btnInvoice);
        btnPaymentOption = (TextView) v.findViewById(R.id.btnPaymentOption);
        btnPrintQuote = (TextView) v.findViewById(R.id.btnPrintQuote);
        txtPrice = (TextView) v.findViewById(R.id.txtPrice);
        txtSmallPrice = (TextView) v.findViewById(R.id.txtSmallPrice);
        txtSmallStatus = (TextView) v.findViewById(R.id.txtSmallStatus);
        btnOfferAccept = (TextView) v.findViewById(R.id.btnOfferAccept);
        txtAddOfferPrice = (TextView) v.findViewById(R.id.txtAddOfferPrice);
        btnUploadImgs = (TextView) v.findViewById(R.id.btnUploadImgs);
        btnGenerateSafetyReport = (TextView) v.findViewById(R.id.btnGenerateSafetyReport);
        txtFollowupWork = (TextView) v.findViewById(R.id.txtFollowupWork);
        viewpager = (ViewPager) v.findViewById(R.id.viewpager);
        indicator = (CircleIndicator) v.findViewById(R.id.indicator);
        lvFree = (ListView) v.findViewById(R.id.lvFree);
        linear = (LinearLayout) v.findViewById(R.id.linear);
        ll_upload_photos = (LinearLayout) v.findViewById(R.id.ll_upload_photos);
        checkbox = (CheckBox) v.findViewById(R.id.checkbox);

        txtJobTitle.getBackground().setLevel(2);
        txtJobDescription.getBackground().setLevel(2);
        txtAdditionalDescription.getBackground().setLevel(2);
        txtServiceInclusion.getBackground().setLevel(1);
        txtFreeInclusion.getBackground().setLevel(1);

        final AwardJobDBOCarOwner data = awardJob.get(position);


        Glide.with(appContext)
                .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + data.getAvatar_img())
                .asBitmap()
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.no_user)
                .error(R.drawable.no_user)
                .skipMemoryCache(false)
                .into(avatarImg);
        txtUserName.setText(data.getName());
        txtUserName.setPaintFlags(txtUserName.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        ratingBar.setRating(data.getRating());
        txtReview.setText("( " + data.getReview_count() + " Review)");
        txtDistance.setText("Distnace : " + data.getDistance() + " KM");
        txtRating.setText("" + data.getRating());
        txtJobTitle.setText(jobDetails.getJob_title());
        txtJobNumber.setText(jobDetails.getJu_id());
        juId = jobDetails.getJu_id();
        car_Id = jobDetails.getCar_id();
        jobTitle = jobDetails.getJob_title();
        txtCategory.setText(jobDetails.getCatname());
        txtSubCategory.setText(jobDetails.getSubcatname());
        txtcar.setText(data.getMake() + " " + data.getModel() + " " + data.getBadge());
        txtJobDescription.setText(jobDetails.getProblem_description());
        txtQuoteDescription.setText(data.getBid_comment());
        txtAdditionalDescription.setText(data.getAdd_offer());

        txtSmallStatus.setText(data.getBid_status());

        String styledText = "Quote from <font color='#7d19ff'>" + data.getName() + "</font>.";
        txtQuoteFrom.setText(Html.fromHtml(styledText), TextView.BufferType.SPANNABLE);
        txtQuoteFrom.setPaintFlags(txtQuoteFrom.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        if (!data.getFreeInclusion().isEmpty()) {

            adapter = new CustomAdapter(data.getFreeInclusion(), appContext);
            setListViewHeightBasedOnChildren(lvFree);
            lvFree.setAdapter(adapter);
        }
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                itemClickListener.onClick(buttonView, position);
            }
        });

        // if (position==0){
        stringArrayList = data.getCarImages();
        if (stringArrayList.size() > 0) {
            for (int i = 0; i < stringArrayList.size(); i++) {
                CarImageDBO carImageDBO = new CarImageDBO();
                carImageDBO.setUrl(stringArrayList.get(i));
                carImageDBOArrayList.add(carImageDBO);
            }
            pagerAdapterImage = new ViewPagerAdapterImage(appContext, carImageDBOArrayList);
            viewpager.setAdapter(pagerAdapterImage);
            //viewpager1.setAdapter(pagerAdapterImage);
            indicator.setViewPager(viewpager);
        }

        // }

        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {

            if (data.getAdd_offer_accept().equalsIgnoreCase("1")) {

                btnOfferAccept.setText("Accepted");
                txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                txtPrice.setText("$" + data.getTotal());
                txtSmallPrice.setText("$" + data.getTotal());
            } else {
                txtPrice.setText("$" + data.getBid_price());
                txtSmallPrice.setText("$" + data.getBid_price());
                btnOfferAccept.setText("Rejected");
            }
            if (data.getBid_status().equalsIgnoreCase("Awarded")) {
                btnAward.setText("Accept");
                ll_upload_photos.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Drop Off")) {
                btnAward.setText("Mark Completed");
                btnInvoice.setText("generate safety report");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.GONE);
                ll_upload_photos.setVisibility(View.VISIBLE);
            } else if (data.getBid_status().equalsIgnoreCase("Quoted")) {
                btnAward.setText("Update Quote");
                btnPaymentOption.setText("Cancel Quote");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnInvoice.setVisibility(View.GONE);
                ll_upload_photos.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("WorkDone")) {
                btnAward.setText("Completed");
                btnInvoice.setText("generate safety report");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("Mail invoice to client");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.VISIBLE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            }else if (data.getBid_status().equalsIgnoreCase("Pickup")) {
                btnAward.setText("Completed");
                btnPaymentOption.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("Mail invoice to client");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            }
        } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            ll_upload_photos.setVisibility(View.GONE);
            btnOfferAccept.setVisibility(View.VISIBLE);
            linear.setVisibility(View.VISIBLE);
            if (data.getAdd_offer_accept().equalsIgnoreCase("1")) {
                btnOfferAccept.setText("Accepted");
                txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                txtPrice.setText("$" + data.getTotal());
                txtSmallPrice.setText("$" + data.getTotal());
            } else {
                btnOfferAccept.setText("Accept?");
                txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                txtPrice.setText("$" + data.getBid_price());
                txtSmallPrice.setText("$" + data.getBid_price());

            }
            if (data.getBid_status().equalsIgnoreCase("Quoted")) {
                btnAward.setText("Award");
                btnInvoice.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Awarded")) {
                btnAward.setText("Awarded");
                btnInvoice.setVisibility(View.VISIBLE);
                btnInvoice.setText("Cancel");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Drop Off")) {
                btnAward.setText("Assigned");
                btnInvoice.setVisibility(View.VISIBLE);
                btnInvoice.setText("Cancel");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("payment options");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
            }
            else if (data.getBid_status().equalsIgnoreCase("WorkDone")) {
                btnAward.setText("accept & pickup car");
                btnInvoice.setText("download invoice");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("payment options");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.VISIBLE);
            }
            else if (data.getBid_status().equalsIgnoreCase("Pickup")) {
                btnAward.setText("Completed");
                btnInvoice.setText("download invoice");
                btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("Pay on pickup");
                btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            }
        }

        btnOfferAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, position);
                txtPrice.setText("$" + data.getTotal());
                txtSmallPrice.setText("$" + data.getTotal());
            }
        });
        btnAward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
            }
        });

        btnUploadImgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
            }
        });

            btnInvoice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                    itemClickListener.onClick(v,position);
                }
            });

        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
            }
        });
        btnPaymentOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, position);
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
            }
        });

        btnPrintQuote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
            }
        });
        btnGenerateSafetyReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
            }
        });
        txtFollowupWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v,position);
            }
        });


        views.add(position, v);
        return position;
    }

    public int removeView(ViewPager pager, View v) {
        return removeView(pager, views.indexOf(v));
    }

    public int removeView(ViewPager pager, int position) {
        pager.setAdapter(null);
        views.remove(position);
        pager.setAdapter(this);

        return position;
    }

    public View getView(int position) {
        return views.get(position);
    }

    public class CustomAdapter extends ArrayAdapter<String> {

        Context mContext;
        private ArrayList<String> dataSet;
        private int lastPosition = -1;

        public CustomAdapter(ArrayList<String> data, Context context) {
            super(context, R.layout.list_row_items_for_inclusion, data);
            this.dataSet = data;
            this.mContext = context;

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            // Get the data item for this position
            String name = getItem(position);
            // Check if an existing view is being reused, otherwise inflate the view
            ViewHolder viewHolder; // view lookup cache stored in tag

            final View result;

            if (convertView == null) {

                viewHolder = new ViewHolder();
                LayoutInflater inflater = LayoutInflater.from(getContext());
                convertView = inflater.inflate(R.layout.list_row_items_for_inclusion, parent, false);
                viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                result = convertView;

                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
                result = convertView;
            }

            lastPosition = position;

            viewHolder.tv_name.setText(name);

            return convertView;
        }

        // View lookup cache
        private class ViewHolder {
            TextView tv_name;

        }
    }


}
