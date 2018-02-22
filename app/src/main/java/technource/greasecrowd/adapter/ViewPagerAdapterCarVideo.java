package technource.greasecrowd.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.CarImageDBO;
import technource.greasecrowd.model.CarVideosDbo;

/**
 * Created by technource on 18/12/17.
 */

public class ViewPagerAdapterCarVideo extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    // private int[] mResources;
    ArrayList<CarVideosDbo> carImageArrayList;
    int[] mResources = {
            R.drawable.car,R.drawable.car,R.drawable.car,

    };

    public ViewPagerAdapterCarVideo(Context mContext, ArrayList<CarVideosDbo> carImageArrayList) {
        this.mContext = mContext;
        this.carImageArrayList=carImageArrayList;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return carImageArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        final CarVideosDbo carImageDBO = carImageArrayList.get(position);
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);

        ImageView btn_play = (ImageView) itemView.findViewById(R.id.btn_play);
        btn_play.setVisibility(View.VISIBLE);
        //imageView.setImageResource(mResources[position]);

        if (carImageArrayList.size()>0){
            Glide.with(mContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getThumbnail())
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .placeholder(R.drawable.car_demo)
                    .error(R.drawable.car_demo)
                    .skipMemoryCache(true)
                    .into(imageView);
        }else {
            imageView.setImageResource(R.drawable.car_demo);
        }
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupForVideo(WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getVideoUrl(),mContext);
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupForVideo(WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getVideoUrl(),mContext);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }
    private void openPopupForVideo(String path,Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.video_popup, null);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        VideoView vv = (VideoView) view.findViewById(R.id.vv);
        ImageView close = (ImageView) view.findViewById(R.id.close);

        Uri uri = Uri.parse(path);
        vv.setVideoURI(uri);
        vv.requestFocus();
        vv.start();

        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                dialog.dismiss();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {

                dialog.dismiss();
                return false;
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        ;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }
}
