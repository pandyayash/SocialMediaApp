package technource.greasecrowd.CustomViews.LoadingDialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import technource.greasecrowd.R;


/**
 * Created by technource on 16/3/17.
 */
public class LoadingDialog {

  public Dialog mDialog;

  CirculerProgressView progressBar;
  Thread updateThread;
  TextView tv_loading;
  private Context context;
  View view;
  private boolean isLDShow = false;

  public LoadingDialog(Context context) {
    this.context = context;
  }

  public void loading() {
    try {
      if (isLDShow) {
        hideLoadingDialog();
      } else {
        createDialog(false, "");
        mDialog.show();
        isLDShow = true;
      }

    } catch (NullPointerException e) {
      e.printStackTrace();
    } catch (Exception e) {

      if (isLDShow && mDialog != null) {
        hideLoadingDialog();
      }
    }
  }

  public void loadingText(String text) {
    try {
      if (isLDShow) {
        hideLoadingDialog();
      } else {
        createDialog(true, text);
        mDialog.show();
        isLDShow = true;
      }
    } catch (Exception e) {
      if (isLDShow && mDialog != null) {
        hideLoadingDialog();
      }
    }
  }

  private void hideLoadingDialog() {
    tv_loading = (TextView) view.findViewById(R.id.tv_loding);
    isLDShow = false;
    if (mDialog != null) {
      tv_loading.setVisibility(View.GONE);
      mDialog.dismiss();
    }
  }

  private void createDialog(boolean text, String text_to_Set) {
    mDialog = null;
    mDialog = new Dialog(context, R.style.loading_dialog);
    view = LayoutInflater.from(context).inflate(
        R.layout.dialog_loading, null);
    progressBar = (CirculerProgressView) view.findViewById(R.id.progressBar1);
    startAnimationThreadStuff(1000);
    tv_loading = (TextView) view.findViewById(R.id.tv_loding);
    if (text) {
      tv_loading.setVisibility(View.VISIBLE);
      tv_loading.setText(text_to_Set);
    } else {
      tv_loading.setVisibility(View.GONE);
    }

    mDialog.setCanceledOnTouchOutside(false);
    mDialog.setContentView(view);
  }

  public void dismissDialog() {
    hideLoadingDialog();
  }

  private void startAnimationThreadStuff(long delay) {
    if (updateThread != null && updateThread.isAlive()) {
      updateThread.interrupt();
    }
    // Start animation after a delay so there's no missed frames while the app loads up
    final Handler handler = new Handler();
    handler.postDelayed(new Runnable() {
      @Override
      public void run() {
        if (!progressBar.isIndeterminate()) {
          progressBar.setProgress(0f);
          // Run thread to update progress every quarter second until full
          updateThread = new Thread(new Runnable() {
            @Override
            public void run() {
              while (progressBar.getProgress() < progressBar.getMaxProgress() && !Thread
                  .interrupted()) {
                // Must set progress in UI thread
                handler.post(new Runnable() {
                  @Override
                  public void run() {
                    progressBar.setProgress(progressBar.getProgress() + 10);
                  }
                });
                SystemClock.sleep(250);
              }
            }
          });
          updateThread.start();
        }
        // Alias for resetAnimation, it's all the same
        progressBar.startAnimation();
      }
    }, delay);
  }

}
