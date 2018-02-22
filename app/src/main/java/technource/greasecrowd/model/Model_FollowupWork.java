package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 20/2/18.
 */

public class Model_FollowupWork implements Parcelable{

    String key,value;

    public Model_FollowupWork(){}

    public Model_FollowupWork(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<Model_FollowupWork> CREATOR = new Creator<Model_FollowupWork>() {
        @Override
        public Model_FollowupWork createFromParcel(Parcel in) {
            return new Model_FollowupWork(in);
        }

        @Override
        public Model_FollowupWork[] newArray(int size) {
            return new Model_FollowupWork[size];
        }
    };

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }
}
