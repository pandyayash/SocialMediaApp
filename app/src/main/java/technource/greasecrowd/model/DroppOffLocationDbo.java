package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 3/11/17.
 */

public class DroppOffLocationDbo implements Parcelable {

    String id;
    String address;
    String name;
    boolean isSelected = false;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DroppOffLocationDbo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.address);
        dest.writeString(this.name);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected DroppOffLocationDbo(Parcel in) {
        this.id = in.readString();
        this.address = in.readString();
        this.name = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<DroppOffLocationDbo> CREATOR = new Creator<DroppOffLocationDbo>() {
        @Override
        public DroppOffLocationDbo createFromParcel(Parcel source) {
            return new DroppOffLocationDbo(source);
        }

        @Override
        public DroppOffLocationDbo[] newArray(int size) {
            return new DroppOffLocationDbo[size];
        }
    };
}
