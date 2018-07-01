package aayushiprojects.greasecrowd.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.relex.circleindicator.CircleIndicator;
import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.activities.JobDetailsActivity;
import aayushiprojects.greasecrowd.helper.HelperMethods;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.interfaces.OnItemClickListener;
import aayushiprojects.greasecrowd.model.AwardJobDBOCarOwner;
import aayushiprojects.greasecrowd.model.CarImageDBO;
import aayushiprojects.greasecrowd.model.JobDetail_DBO;
import aayushiprojects.greasecrowd.model.LoginDetail_DBO;

import static aayushiprojects.greasecrowd.activities.BaseActivity.setListViewHeightBasedOnChildren;

/**
 * Created by technource on 22/1/18.
 */

public class AdptQuoteDetailsPager extends PagerAdapter {

    public static ViewPager viewpager;
    public static CircleIndicator indicator;
    public static String juId, car_Id, jobTitle,cJObId;
    public static boolean  isFlexible=false;
    TextView cat1, cat2;
    Context appContext;
    LinearLayout ll_cat1, ll_cat2, linear, ll_upload_photos, llFollowupWork, llInnerTextFollow, ll_new_propose_time;
    LinearLayout ll_small_status, ll_additional_offer;
    int arraylistpos;
    ListView lvFree;
    OnItemClickListener itemClickListener;
    LoginDetail_DBO loginDetail_dbo;
    ViewPagerAdapterImage pagerAdapterImage;
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
    private TextView hintOffer;
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
    private TextView txtPickupDate;
    private TextView txtDropOffDate;
    private TextView txtacceptNewTime;
    private CheckBox checkbox;
    private ArrayList<View> views = new ArrayList<View>();
    private ArrayList<String> stringArrayList;
    private ArrayList<CarImageDBO> carImageDBOArrayList;

    public AdptQuoteDetailsPager(Context appContext, OnItemClickListener itemClickListener) {
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


    public int addView(View v, final int position, final AwardJobDBOCarOwner data, JobDetail_DBO jobDetails) {

        mediaCardView = v.findViewById(R.id.media_card_view);
        txtQuoteFrom = v.findViewById(R.id.txtQuoteFrom);
        avatarImg = v.findViewById(R.id.avatar_img);
        txtUserName = v.findViewById(R.id.txtUserName);
        ratingBar = v.findViewById(R.id.ratingBar);
        txtRating = v.findViewById(R.id.txtRating);
        txtReview = v.findViewById(R.id.txtReview);
        txtDistance = v.findViewById(R.id.txtDistance);
        txtGarageComment = v.findViewById(R.id.txtGarageComment);
        txtOfferPrice = v.findViewById(R.id.txtOfferPrice);
        txtAcceptAdditionalOffer = v.findViewById(R.id.txtAcceptAdditionalOffer);
        freeInclusionrecyclerview = v.findViewById(R.id.freeInclusionrecyclerview);
        txtJobTitle = v.findViewById(R.id.txtJobTitle);
        txtJobNumber = v.findViewById(R.id.txtJobNumber);
        txtCategory = v.findViewById(R.id.txtCategory);
        txtSubCategory = v.findViewById(R.id.txtSubCategory);
        txtcar = v.findViewById(R.id.txtcar);
        txtJobDescription = v.findViewById(R.id.txtJobDescription);
        txtQuoteDescription = v.findViewById(R.id.txtQuoteDescription);
        txtQuoteDescription.getBackground().setLevel(2);
        txtAdditionalDescription = v.findViewById(R.id.txtAdditionalDescription);
        hintOffer = v.findViewById(R.id.hintOffer);
        txtServiceInclusion = v.findViewById(R.id.txtServiceInclusion);
        txtFreeInclusion = v.findViewById(R.id.txtFreeInclusion);
        btnAward = v.findViewById(R.id.btnAward);
        btnInvoice = v.findViewById(R.id.btnInvoice);
        btnPaymentOption = v.findViewById(R.id.btnPaymentOption);
        btnPrintQuote = v.findViewById(R.id.btnPrintQuote);
        txtPrice = v.findViewById(R.id.txtPrice);
        txtSmallPrice = v.findViewById(R.id.txtSmallPrice);
        txtSmallStatus = v.findViewById(R.id.txtSmallStatus);
        btnOfferAccept = v.findViewById(R.id.btnOfferAccept);
        txtAddOfferPrice = v.findViewById(R.id.txtAddOfferPrice);
        btnUploadImgs = v.findViewById(R.id.btnUploadImgs);
        btnGenerateSafetyReport = v.findViewById(R.id.btnGenerateSafetyReport);
        txtFollowupWork = v.findViewById(R.id.txtFollowupWork);
        txtDropOffDate = v.findViewById(R.id.txtDropOffDate);
        txtPickupDate = v.findViewById(R.id.txtPickupDate);
        txtacceptNewTime = v.findViewById(R.id.txtacceptNewTime);
        viewpager = v.findViewById(R.id.viewpager);
        indicator = v.findViewById(R.id.indicator);
        lvFree = v.findViewById(R.id.lvFree);
        linear = v.findViewById(R.id.linear);
        ll_upload_photos = v.findViewById(R.id.ll_upload_photos);
        llFollowupWork = v.findViewById(R.id.llFollowupWork);
        checkbox = v.findViewById(R.id.checkbox);
        llInnerTextFollow = v.findViewById(R.id.llInnerTextFollow);
        ll_new_propose_time = v.findViewById(R.id.ll_new_propose_time);
        ll_small_status = v.findViewById(R.id.ll_small_status);
        ll_additional_offer = v.findViewById(R.id.ll_additional_offer);

//        txtJobTitle.getBackground().setLevel(0);
//        txtJobDescription.getBackground().setLevel(0);
//        txtAdditionalDescription.getBackground().setLevel(0);
        txtServiceInclusion.getBackground().setLevel(1);
        txtFreeInclusion.getBackground().setLevel(1);

        // final AwardJobDBOCarOwner data = awardJob.get(position);


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
        txtDistance.setText("Distance : " + data.getDistance() + " KM");
        txtRating.setText("" + data.getRating());
        txtJobTitle.setText(jobDetails.getJob_title());
        txtJobNumber.setText(jobDetails.getJu_id());
        cJObId = jobDetails.getCjob_id();
        juId = jobDetails.getJu_id();
        car_Id = jobDetails.getCar_id();
        jobTitle = jobDetails.getJob_title();
        txtCategory.setText(jobDetails.getCatname());
        txtSubCategory.setText(jobDetails.getSubcatname());
        txtcar.setText(data.getMake() + " " + data.getModel() + " " + data.getBadge());
        txtJobDescription.setText(jobDetails.getProblem_description());
        txtQuoteDescription.setText(data.getBid_comment());
        String time_flexibility = jobDetails.getTime_flexibility();
        isFlexible = time_flexibility.equalsIgnoreCase("Flexible") || time_flexibility.equalsIgnoreCase("Very Flexible");

        if (data.getCurrent_job_status().equalsIgnoreCase("WorkDone")) {
            txtSmallStatus.setText("Complete");
        } else {
            txtSmallStatus.setText(data.getCurrent_job_status());
        }
        if (data.getAdd_offer().equalsIgnoreCase("")) {
            hintOffer.setVisibility(View.GONE);
            txtAdditionalDescription.setVisibility(View.GONE);
        } else {
            hintOffer.setVisibility(View.VISIBLE);
            txtAdditionalDescription.setVisibility(View.VISIBLE);
            txtAdditionalDescription.setText(data.getAdd_offer());
        }

        String styledText = "Quote from <font color='#7d19ff'>" + data.getName() + "</font>.";
        txtQuoteFrom.setText(Html.fromHtml(data.getName()), TextView.BufferType.SPANNABLE);
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
        if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("1")) {

            if (data.getAdd_offer_accept().equalsIgnoreCase("1")) {
                btnOfferAccept.setVisibility(View.VISIBLE);
                btnOfferAccept.setText("Accepted");
                txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                txtPrice.setText("$" + data.getTotal());
                txtSmallPrice.setText("$" + data.getTotal());
            } else {
                // ll_additional_offer.setVisibility(View.GONE);
                if (data.getAdd_offer_price().equalsIgnoreCase("0.00")) {
                    ll_additional_offer.setVisibility(View.GONE);
                } else {
                    txtPrice.setText("$" + data.getBid_price());
                    txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                    txtSmallPrice.setText("$" + data.getBid_price());
                    btnOfferAccept.setText("Rejected");
                    //btnOfferAccept.setVisibility(View.GONE);
                }

            }
            if (jobDetails.isFollowUpAccepted()) {
                llInnerTextFollow.setVisibility(View.GONE);
            } else {
                llInnerTextFollow.setVisibility(View.VISIBLE);
            }

            if (data.getBid_status().equalsIgnoreCase("Awarded")) {
                btnAward.setText("Accept");
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.GONE);
                btnInvoice.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Assigned")) {

                if (data.getCurrent_job_status().equalsIgnoreCase("Drop off")) {
                    btnAward.setText("Mark Completed");
                    btnInvoice.setText("generate safety report");
                   // btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.GONE);
                    ll_upload_photos.setVisibility(View.VISIBLE);
                    llFollowupWork.setVisibility(View.VISIBLE);
                } else if (data.getCurrent_job_status().equalsIgnoreCase("WorkDone")) {
                    btnAward.setText("Completed");
                    btnInvoice.setText("generate safety report");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("download invoice");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    ll_upload_photos.setVisibility(View.VISIBLE);
                    llFollowupWork.setVisibility(View.VISIBLE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }

            } else if (data.getBid_status().equalsIgnoreCase("Open")) {
                btnAward.setText("Update Quote");
                btnPaymentOption.setText("Cancel Quote");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                btnInvoice.setVisibility(View.GONE);
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Completed")) {
                btnAward.setText("Completed");
                btnInvoice.setVisibility(View.GONE);
                //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("download invoice");
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            } else if (data.getBid_status().equalsIgnoreCase("Pickup")) {
                btnAward.setText("Completed");
                btnInvoice.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("download invoice");
               // btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            }
        } else if (loginDetail_dbo.getUser_Type().equalsIgnoreCase("0")) {
            ll_upload_photos.setVisibility(View.GONE);
            llInnerTextFollow.setVisibility(View.GONE);
            llFollowupWork.setVisibility(View.GONE);
            btnOfferAccept.setVisibility(View.VISIBLE);
            linear.setVisibility(View.VISIBLE);
            if (data.getAdd_offer_accept().equalsIgnoreCase("1")) {
                btnOfferAccept.setText("Accepted");
                txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                txtPrice.setText("$" + data.getTotal());
                txtSmallPrice.setText("$" + data.getTotal());
            } else {
                //
                if (data.getAdd_offer_price().equalsIgnoreCase("0.00")) {
                    ll_additional_offer.setVisibility(View.GONE);
                    txtPrice.setText("$" + data.getBid_price());
                    txtSmallPrice.setText("$" + data.getBid_price());
                }else if (data.getAdd_offer_accept().equalsIgnoreCase("")){
                    ll_additional_offer.setVisibility(View.VISIBLE);
                    btnOfferAccept.setText("Rejected");
                    txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                    txtPrice.setText("$" + data.getBid_price());
                    txtSmallPrice.setText("$" + data.getBid_price());
                }
                else {
                    ll_additional_offer.setVisibility(View.VISIBLE);
                    btnOfferAccept.setText("Accept?");
                    txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                    txtPrice.setText("$" + data.getBid_price());
                    txtSmallPrice.setText("$" + data.getBid_price());
                }

            }
            if (data.getBid_status().equalsIgnoreCase("Open")) {
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
                if (!data.getNew_drop_off_date_time().equalsIgnoreCase("0000-00-00 00:00:00") ||
                        !data.getEw_pick_up_date_time().equalsIgnoreCase("0000-00-00 00:00:00")) {
                    ll_new_propose_time.setVisibility(View.VISIBLE);
                    txtDropOffDate.setText(parseDateToddMMyyyy(data.getNew_drop_off_date_time()));
                    txtPickupDate.setText(parseDateToddMMyyyy(data.getEw_pick_up_date_time()));
                } else {
                    ll_new_propose_time.setVisibility(View.GONE);
                }
                btnAward.setText("Award");
                btnInvoice.setVisibility(View.GONE);
                btnPaymentOption.setVisibility(View.GONE);
            }
            if (data.getCurrent_job_status().equalsIgnoreCase("Awarded")) {
                ll_upload_photos.setVisibility(View.GONE);
                llFollowupWork.setVisibility(View.GONE);
                if (data.getBid_status().equalsIgnoreCase("Awarded")) {
                    btnAward.setText("Awarded");
                    btnInvoice.setVisibility(View.VISIBLE);
                    btnInvoice.setText("Cancel");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.GONE);
                    //ll_additional_offer.setVisibility(View.VISIBLE);
                    ll_small_status.setVisibility(View.VISIBLE);
                    if (data.getAdd_offer_accept().equalsIgnoreCase("1")) {
                        btnOfferAccept.setText("Accepted");
                        txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                        txtPrice.setText("$" + data.getTotal());
                        txtSmallPrice.setText("$" + data.getTotal());
                    } else {
                        //
                        if (data.getAdd_offer_price().equalsIgnoreCase("0.00")) {
                            ll_additional_offer.setVisibility(View.GONE);
                        } else {
                            ll_additional_offer.setVisibility(View.VISIBLE);
                            btnOfferAccept.setText("Rejected");
                            txtAddOfferPrice.setText("$" + data.getAdd_offer_price());
                            txtPrice.setText("$" + data.getBid_price());
                            txtSmallPrice.setText("$" + data.getBid_price());
                        }

                    }
                } else {
                    btnAward.setVisibility(View.GONE);
                    btnInvoice.setVisibility(View.GONE);
                    btnPaymentOption.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                    //ll_additional_offer.setVisibility(View.GONE);
                    ll_small_status.setVisibility(View.GONE);
                }
            }
            if (data.getBid_status().equalsIgnoreCase("Assigned")) {
                ll_upload_photos.setVisibility(View.GONE);
                if (data.getCurrent_job_status().equalsIgnoreCase("Drop off")) {
                    llFollowupWork.setVisibility(View.VISIBLE);
                    btnAward.setText("Assigned");
                    btnInvoice.setText("Cancel");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("payment options");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    if (data.getIsSeftyReportGenerated().equalsIgnoreCase("yes")) {
                        btnGenerateSafetyReport.setVisibility(View.VISIBLE);
                    } else {
                        btnGenerateSafetyReport.setVisibility(View.GONE);
                    }
                } else if (data.getCurrent_job_status().equalsIgnoreCase("WorkDone")) {
                    llFollowupWork.setVisibility(View.GONE);
                    btnAward.setText("accept & pickup car");
                    btnInvoice.setText("download invoice");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));

                    btnPaymentOption.setVisibility(View.VISIBLE);
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setText("payment options");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    ll_upload_photos.setVisibility(View.GONE);
                    if (data.getIsSeftyReportGenerated().equalsIgnoreCase("yes")) {
                        btnGenerateSafetyReport.setVisibility(View.VISIBLE);
                    } else {
                        btnGenerateSafetyReport.setVisibility(View.GONE);
                    }



                    /*btnAward.setVisibility(View.GONE);
                    btnInvoice.setVisibility(View.GONE);
                    btnPaymentOption.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                    ll_additional_offer.setVisibility(View.GONE);
                    ll_small_status.setVisibility(View.GONE);*/
                }

            }
            if (data.getBid_status().equalsIgnoreCase("WorkDone")) {
                llFollowupWork.setVisibility(View.GONE);
                btnAward.setText("accept & pickup car");
                btnInvoice.setText("download invoice");
                //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.VISIBLE);
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                btnPaymentOption.setText("payment options");
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.GONE);
                if (data.getIsSeftyReportGenerated().equalsIgnoreCase("yes")) {
                    btnGenerateSafetyReport.setVisibility(View.VISIBLE);
                } else {
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }
            }
            if (data.getBid_status().equalsIgnoreCase("Completed")) {
                btnAward.setText("Completed");
                btnInvoice.setText("download invoice");
                //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("Pay on pickup");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
            }

            if (data.getPayment_type().equalsIgnoreCase("PAY_ON_PICKUP")) {
                if (data.getCurrent_job_status().equalsIgnoreCase("WorkDone")) {
                    llFollowupWork.setVisibility(View.GONE);
                    btnAward.setText("accept & pickup car");
                    btnInvoice.setText("download invoice");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("Pay on pickup");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    ll_upload_photos.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                } else if (data.getBid_status().equalsIgnoreCase("Completed")) {
                    btnAward.setText("Completed");
                    btnInvoice.setText("download invoice");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("Pay on pickup");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    ll_upload_photos.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }/*else {
                    llFollowupWork.setVisibility(View.GONE);
                    btnAward.setText("Assigned");
                    btnInvoice.setText("Cancel");
                    btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("Pay on pickup");
                    btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }*/

            } else if (!data.getPayment_type().equalsIgnoreCase("") || !data.getPaymet_garage_id().equalsIgnoreCase("0")) {
                if (data.getCurrent_job_status().equalsIgnoreCase("WorkDone")) {
                    llFollowupWork.setVisibility(View.GONE);
                    btnAward.setText("accept & pickup car");
                    btnInvoice.setText("download invoice");
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("paid");
                    //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    ll_upload_photos.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                } else if (data.getCurrent_job_status().equalsIgnoreCase("Completed")) {
                    btnAward.setText("Completed");
                    btnInvoice.setText("download invoice");
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                    //btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("paid");
                   // btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    ll_upload_photos.setVisibility(View.GONE);
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }else if (data.getCurrent_job_status().equalsIgnoreCase("Drop Off")) {
                    llFollowupWork.setVisibility(View.GONE);
                    btnAward.setText("Assigned");
                    btnInvoice.setText("Cancel");
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                        btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    } else {
                        btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                        btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    }
                   // btnInvoice.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    btnPaymentOption.setVisibility(View.VISIBLE);
                    btnPaymentOption.setText("paid");
                   // btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
                    btnGenerateSafetyReport.setVisibility(View.GONE);
                }
            }

            if (JobDetailsActivity.isPaid && data.getBid_status().equalsIgnoreCase("Pickup")) {
                btnAward.setVisibility(View.VISIBLE);
                btnAward.setText("Completed");
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
                btnInvoice.setVisibility(View.VISIBLE);
                btnInvoice.setText("download invoice");
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("paid");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
            }
            if (!JobDetailsActivity.isPaid && data.getBid_status().equalsIgnoreCase("Pickup")) {
                btnAward.setVisibility(View.VISIBLE);
                btnAward.setText("accept & pickup car");
                ll_upload_photos.setVisibility(View.GONE);
                btnGenerateSafetyReport.setVisibility(View.GONE);
                btnInvoice.setVisibility(View.VISIBLE);
                btnInvoice.setText("download invoice");
                btnPaymentOption.setVisibility(View.VISIBLE);
                btnPaymentOption.setText("paid");
                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    btnInvoice.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                    btnPaymentOption.setBackgroundDrawable(ContextCompat.getDrawable(appContext, R.drawable.corner_button) );
                } else {
                    btnInvoice.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                    btnPaymentOption.setBackground(ContextCompat.getDrawable(appContext, R.drawable.corner_button));
                }
                //btnPaymentOption.setBackgroundColor(appContext.getResources().getColor(R.color.colorAccent));
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
        btnInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(appContext, "Comming Soon.", Toast.LENGTH_SHORT).show();
                itemClickListener.onClick(v, position);
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
                itemClickListener.onClick(v, position);
            }
        });
        txtServiceInclusion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(v, position);
            }
        });
        txtacceptNewTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, position);
                ll_new_propose_time.setVisibility(View.GONE);
            }
        });

        views.add(position, v);
        return position;
    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-MMM-yyyy HH:mm";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
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
                viewHolder.tv_name = convertView.findViewById(R.id.tv_name);
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
