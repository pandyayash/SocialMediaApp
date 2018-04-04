package technource.autoreum.CustomViews.Widgets;

/**
 * Created by technource on 4/9/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Nirav on 5/16/2016.
 */
public class Poppins_Bold_Textview extends TextView {

  private final static String CONDENSED_FONT = "poppins_bold.otf";

  public Poppins_Bold_Textview(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public Poppins_Bold_Textview(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public Poppins_Bold_Textview(Context context) {
    super(context);
    init();
  }

  private void init() {
    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
    setTypeface(tf);
  }


}
