package technource.greasecrowd.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import technource.greasecrowd.R;
import technource.greasecrowd.helper.WebServiceURLs;
import technource.greasecrowd.model.CarImageDBO;

/**
 * Created by technource on 14/12/17.
 */

public class ViewPagerAdapterImage extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    // private int[] mResources;
    ArrayList<CarImageDBO> carImageArrayList;
    int[] mResources = {
            R.drawable.car, R.drawable.car, R.drawable.car,

    };

    public ViewPagerAdapterImage(Context mContext, ArrayList<CarImageDBO> carImageArrayList) {
        this.mContext = mContext;
        this.carImageArrayList = carImageArrayList;
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
        final CarImageDBO carImageDBO = carImageArrayList.get(position);
        View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

        ImageView imageView = (ImageView) itemView.findViewById(R.id.img_pager_item);
        //imageView.setImageResource(mResources[position]);

        if (carImageArrayList.size() > 0) {
            Glide.with(mContext)
                    .load(WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getUrl())
                    .asBitmap()
                    .dontAnimate()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.car_demo)
                    .error(R.drawable.car_demo)
                    .skipMemoryCache(false)
                    .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.car_demo);
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPopupForImage(WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getUrl(), mContext);
            }
        });

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void openPopupForImage(String path, Context context) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_popup_for_postjob_4, null);
        ImageView img = (ImageView) view.findViewById(R.id.img);


        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = width;
        lp.height = height;
        Glide.with(context)
                .load(path)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .placeholder(R.drawable.default_car)
                .error(R.drawable.default_car)
                .skipMemoryCache(false)
                .into(img);
        dialog.getWindow().setAttributes(lp);
        ((Activity) mContext).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
    }
}
