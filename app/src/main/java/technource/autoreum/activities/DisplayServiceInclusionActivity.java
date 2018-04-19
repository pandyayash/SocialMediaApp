package technource.autoreum.activities;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import technource.autoreum.CustomViews.Widgets.CircularImageView;
import technource.autoreum.R;
import technource.autoreum.helper.WebServiceURLs;
import technource.autoreum.model.AwardJobDBOCarOwner;

public class DisplayServiceInclusionActivity extends BaseActivity {


    private CardView mediaCardView;
    private CircularImageView avatarImg;
    private TextView txtUserName;
    private RatingBar ratingBar;
    private TextView txtRating;
    private TextView txtReview;
    private TextView txtDistance;
    private TextView txtGarageComment;
    private TextView txtOfferPrice;
    private TextView txtAcceptAdditionalOffer;
    private RecyclerView freeInclusionrecyclerview;
    private TextView txtJobTitle;
    private RecyclerView recyclerView;
    ArrayList<String> services;
    AwardJobDBOCarOwner awardJobDBOCarOwner;
    String title;
    Recycleradpter adapter;
    LinearLayoutManager layoutManager;
    Context appContext;
    LinearLayout ll_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_service_inclusion);
        setHeader("Service INclusion");
        findViews();
        Intent intent = getIntent();
        if (intent != null) {
            awardJobDBOCarOwner = new AwardJobDBOCarOwner();
            awardJobDBOCarOwner = intent.getParcelableExtra("data");
             title = intent.getStringExtra("title");
        }
        setView(awardJobDBOCarOwner);
        setAdapter();

        txtUserName.requestFocus();
    }

    private void setAdapter() {
        adapter = new Recycleradpter(awardJobDBOCarOwner.getServices(), appContext);
        layoutManager = new LinearLayoutManager(appContext);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }


    private void findViews() {
        appContext = this;
        mediaCardView = findViewById(R.id.media_card_view);
        avatarImg = findViewById(R.id.avatar_img);
        txtUserName = findViewById(R.id.txtUserName);
        ratingBar = findViewById(R.id.ratingBar);
        txtRating = findViewById(R.id.txtRating);
        txtReview = findViewById(R.id.txtReview);
        txtDistance = findViewById(R.id.txtDistance);
        txtGarageComment = findViewById(R.id.txtGarageComment);
        txtOfferPrice = findViewById(R.id.txtOfferPrice);
        txtAcceptAdditionalOffer = findViewById(R.id.txtAcceptAdditionalOffer);
        freeInclusionrecyclerview = findViewById(R.id.freeInclusionrecyclerview);
        txtJobTitle = findViewById(R.id.txtJobTitle);
        recyclerView = findViewById(R.id.recycler_view);
        ll_back = findViewById(R.id.ll_back);

        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void setView(AwardJobDBOCarOwner data) {
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
        txtJobTitle.setText(title);
    }


    public class Recycleradpter extends RecyclerView.Adapter<Recycleradpter.Recycleviewholder> {

        ArrayList<String> services;
        Context context;

        public Recycleradpter(ArrayList<String> services, Context context) {

            this.context = context;
            this.services = services;
        }


        @Override
        public Recycleradpter.Recycleviewholder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_row_services, parent, false);
            Recycleradpter.Recycleviewholder recycleviewholder = new Recycleradpter.Recycleviewholder(view);


            return recycleviewholder;
        }


        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onBindViewHolder(final Recycleradpter.Recycleviewholder holder, final int position) {

            holder.services_text.setText(services.get(position));

            holder.service_ll.setBackgroundColor(getResources().getColor(R.color.grey_border));
            holder.services_text.setTextColor(getResources().getColor(R.color.black));
        }


        @Override
        public int getItemCount() {
            return services.size();
        }

        public class Recycleviewholder extends RecyclerView.ViewHolder {
            private View view;
            LinearLayout service_ll;

            TextView services_text;

            public Recycleviewholder(View itemView) {
                super(itemView);
                view = itemView;
                services_text = itemView.findViewById(R.id.autoText2);
                service_ll = itemView.findViewById(R.id.service_ll);
            }
        }
    }

}
