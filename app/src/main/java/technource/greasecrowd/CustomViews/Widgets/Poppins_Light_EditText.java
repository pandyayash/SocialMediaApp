package technource.greasecrowd.CustomViews.Widgets;

/**
 * Created by technource on 4/9/17.
 */

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Nirav on 5/16/2016.
 */
public class Poppins_Light_EditText extends EditText {

  private final static String CONDENSED_FONT = "poppins_light.otf";

  public Poppins_Light_EditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public Poppins_Light_EditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public Poppins_Light_EditText(Context context) {
    super(context);
    init();
  }

  private void init() {
    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), CONDENSED_FONT);
    setTypeface(tf);
  }


}
