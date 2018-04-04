package technource.autoreum.CustomViews;

/**
 * Created by technource on 12/5/17.
 */

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Abc on 4/18/2016.
 */
public class Grid_image extends ImageView {


  public Grid_image(Context context) {
    super(context);
  }

  public Grid_image(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public Grid_image(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override
  public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, widthMeasureSpec);
  }
}
