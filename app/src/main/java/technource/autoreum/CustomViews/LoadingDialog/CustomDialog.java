package technource.autoreum.CustomViews.LoadingDialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import technource.autoreum.R;


/**
 * Created by technource on 20/3/17.
 */
public class CustomDialog extends Dialog {

  protected boolean mDialogResult;
  Context context;

  LinearLayout btn_cancel;
  TextView tv_title;
  TextView tv_message;

  String title;
  String message;

  public CustomDialog(Context context, String title, String message) {

    super(context, R.style.CustomDialogStyle);

    this.context = context;
    this.title = title;
    this.message = message;


  }

  @Override
  public void onStart() {

    this.setContentView(R.layout.dialog_custom);

    WindowManager.LayoutParams mParams = getWindow().getAttributes();
    mParams.width = (int) (context.getResources().getDisplayMetrics().widthPixels * 0.9);
    mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    getWindow().setAttributes(mParams);
    getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    initUI();
  }

  public void initUI() {
    btn_cancel = (LinearLayout) findViewById(R.id.btn_cancel);
    btn_cancel.setOnClickListener(new View.OnClickListener() {

      @Override
      public void onClick(View v) {
        // TODO Auto-generated method stub
        dismiss();
      }
    });

    tv_title = (TextView) findViewById(R.id.tv_title);
    tv_message = (TextView) findViewById(R.id.tv_message);
    tv_title.setVisibility(View.GONE);
    tv_title.setText(title);
    tv_message.setText(message);

  }
}
