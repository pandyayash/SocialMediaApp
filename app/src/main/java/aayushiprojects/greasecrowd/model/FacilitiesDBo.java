package aayushiprojects.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 11/9/17.
 */

public class FacilitiesDBo implements Parcelable {

    public int id;
    public String facilites;
    private boolean isSelected = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getFacilites() {
        return facilites;
    }

    public void setFacilites(String facilites) {
        this.facilites = facilites;
    }

    public FacilitiesDBo() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.facilites);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    protected FacilitiesDBo(Parcel in) {
        this.id = in.readInt();
        this.facilites = in.readString();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<FacilitiesDBo> CREATOR = new Creator<FacilitiesDBo>() {
        @Override
        public FacilitiesDBo createFromParcel(Parcel source) {
            return new FacilitiesDBo(source);
        }

        @Override
        public FacilitiesDBo[] newArray(int size) {
            return new FacilitiesDBo[size];
        }
    };
}
