package technource.greasecrowd.helper;

/**
 * Created by technource on 17/7/17.
 */
public class AppLog {
    public static final boolean IS_DEGUG = true;

    public static final void Log(String tag, String messge) {
        if (IS_DEGUG) {
            try{
                android.util.Log.e(tag, messge);
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}