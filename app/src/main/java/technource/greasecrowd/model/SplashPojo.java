package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 5/9/17.
 */

public class SplashPojo implements Parcelable{

    int id;
    String title;
    String description;
    String image_path;

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }

    public SplashPojo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeString(this.image_path);
    }

    protected SplashPojo(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.image_path = in.readString();
    }

    public static final Creator<SplashPojo> CREATOR = new Creator<SplashPojo>() {
        @Override
        public SplashPojo createFromParcel(Parcel source) {
            return new SplashPojo(source);
        }

        @Override
        public SplashPojo[] newArray(int size) {
            return new SplashPojo[size];
        }
    };
}
