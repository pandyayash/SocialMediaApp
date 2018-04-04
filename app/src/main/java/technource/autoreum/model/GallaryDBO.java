package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 25/9/17.
 */

public class GallaryDBO implements Parcelable {

    String image;
    String name;
    String id;
    String key;
    String video;
    String thumbnail;

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getImage() {
        return image;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public GallaryDBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.image);
        dest.writeString(this.name);
        dest.writeString(this.id);
        dest.writeString(this.key);
        dest.writeString(this.video);
        dest.writeString(this.thumbnail);
    }

    protected GallaryDBO(Parcel in) {
        this.image = in.readString();
        this.name = in.readString();
        this.id = in.readString();
        this.key = in.readString();
        this.video = in.readString();
        this.thumbnail = in.readString();
    }

    public static final Creator<GallaryDBO> CREATOR = new Creator<GallaryDBO>() {
        @Override
        public GallaryDBO createFromParcel(Parcel source) {
            return new GallaryDBO(source);
        }

        @Override
        public GallaryDBO[] newArray(int size) {
            return new GallaryDBO[size];
        }
    };
}
