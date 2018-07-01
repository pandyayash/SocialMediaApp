package aayushiprojects.greasecrowd.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AlertDialog;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import aayushiprojects.greasecrowd.R;
import aayushiprojects.greasecrowd.helper.AppLog;
import aayushiprojects.greasecrowd.helper.Connectivity;
import aayushiprojects.greasecrowd.helper.WebServiceURLs;
import aayushiprojects.greasecrowd.model.CarVideosDbo;

import static com.facebook.FacebookSdk.getApplicationContext;

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
    ProgressDialog mProgressDialog;
    String imgUrl;

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

        ImageView imageView = itemView.findViewById(R.id.img_pager_item);

        ImageView btn_play = itemView.findViewById(R.id.btn_play);
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
        imageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imgUrl = WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getVideoUrl();
                showPopup();
                return false;
            }
        });
        btn_play.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                imgUrl = WebServiceURLs.BASE_URL_IMAGE_PROFILE + carImageDBO.getVideoUrl();
                showPopup();
                return false;
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
        VideoView vv = view.findViewById(R.id.vv);
        ImageView close = view.findViewById(R.id.close);

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
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        dialog.setContentView(view);
        dialog.getWindow().setAttributes(lp);
        dialog.show();


    }

    public void showPopup(){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

        builder.setTitle("Download Video")
                .setMessage("Want to download video?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (Connectivity.isConnected(mContext)) {
                            MyAsyncTask asyncTask = new MyAsyncTask();
                            asyncTask.execute();
                        } else {
                            Toast.makeText(mContext, mContext.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .show();
    }

    class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setMessage("Video Downloading...");
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                File dir = new File(Environment.getExternalStorageDirectory() + "/" + mContext.getResources().getString(R.string.app_name) + "/autoreum");

                String fileName;

                fileName = "VID" + System.currentTimeMillis() + ".mp4";
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
            Toast.makeText(mContext,"Video Downloaded to sd card",Toast.LENGTH_SHORT).show();
            mProgressDialog.cancel();

        }
    }
}
