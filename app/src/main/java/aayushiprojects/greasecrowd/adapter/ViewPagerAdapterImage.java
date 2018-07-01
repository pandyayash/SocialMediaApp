package aayushiprojects.greasecrowd.adapter;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.CarImageDBO;

import static android.content.Context.MODE_PRIVATE;
import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by technource on 14/12/17.
 */

public class ViewPagerAdapterImage extends PagerAdapter {
    Context mContext;
    LayoutInflater mLayoutInflater;
    String imgUrl;
    ProgressDialog mProgressDialog;
    public static String STORE_IN_FOLDER = android.os.Environment
            .getExternalStorageDirectory().toString()
            + "/Android/data/com.autoreum/autoreum";

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

        ImageView imageView = itemView.findViewById(R.id.img_pager_item);
        ImageView btn_play = itemView.findViewById(R.id.btn_play);
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

    private void openPopupForImage(final String path, Context context) {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.image_popup_for_postjob_4, null);
        ImageView img = view.findViewById(R.id.img);
        TextView btnDownload = view.findViewById(R.id.btnDownload);

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 50;  //Set your heights
        int height = (int) (size.y / 1.4);
        imgUrl = path;
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

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(mContext,"No Permission",Toast.LENGTH_SHORT).show();
                    } else {
                        MyAsyncTask asyncTask = new MyAsyncTask();
                        asyncTask.execute();
                    }

            }
        });
        dialog.getWindow().setAttributes(lp);
        ((Activity) mContext).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(view);
        dialog.show();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Image Downloading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getResources().getString(R.string.app_name) + "/autoreum");

                String fileName;

                    fileName = "IMG" + System.currentTimeMillis() + ".png";
                if (dir.exists() == false) {
                    dir.mkdirs();
                }

                URL url ;

                    url= new URL(imgUrl);

                File file = new File(dir, fileName);
                AppLog.Log("Local filename:", "" + file.toString());
                if (file.createNewFile()) {
                    file.createNewFile();
                }
                long startTime = System.currentTimeMillis();
                AppLog.Log("DownloadManager", "download url:" + url);
                AppLog.Log("DownloadManager", "download file name:" + fileName);

                URLConnection uconn = url.openConnection();
                uconn.setReadTimeout(1000);
                uconn.setConnectTimeout(1000);

                InputStream is = uconn.getInputStream();
                BufferedInputStream bufferinstream = new BufferedInputStream(is);

                ByteArrayBuffer baf = new ByteArrayBuffer(5000);
                int current = 0;
                while ((current = bufferinstream.read()) != -1) {
                    baf.append((byte) current);
                }

                FileOutputStream fos = new FileOutputStream(file);
                fos.write(baf.toByteArray());
                fos.flush();
                fos.close();
                long seconds = ((System.currentTimeMillis() - startTime));
                AppLog.Log("DownloadManager", "download ready in" + (seconds / 1000) + "sec");
                int dotindex = fileName.lastIndexOf('.');
                if (dotindex >= 0) {
                    fileName = fileName.substring(0, dotindex);
                }
                Thread.sleep(seconds);

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            //mProgressDialog.setDismissMessage("Image Downloaded to sd card");
            mProgressDialog.dismiss();
            Toast.makeText(getApplicationContext(),"Image Downloaded to sd card",Toast.LENGTH_SHORT).show();
        }
    }


}
