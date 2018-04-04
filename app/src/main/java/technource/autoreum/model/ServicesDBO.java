package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by technource on 8/9/17.
 */

public class ServicesDBO implements Parcelable {

    String service;
    int id;
    ArrayList<String> service_selected;
    private boolean isSelected = false;

    public ServicesDBO() {

    }


    public String getService() {
        return service;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public ArrayList<String> getService_selected() {
        return service_selected;
    }

    public void setService_selected(ArrayList<String> service_selected) {
        this.service_selected = service_selected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setService(String service) {
        this.service = service;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.service);
        dest.writeInt(this.id);
        dest.writeStringList(this.service_selected);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }

    public ServicesDBO(Parcel in) {
        this.service = in.readString();
        this.id = in.readInt();
        this.service_selected = in.createStringArrayList();
        this.isSelected = in.readByte() != 0;
    }

    public static final Creator<ServicesDBO> CREATOR = new Creator<ServicesDBO>() {
        @Override
        public ServicesDBO createFromParcel(Parcel source) {
            return new ServicesDBO(source);
        }

        @Override
        public ServicesDBO[] newArray(int size) {
            return new ServicesDBO[size];
        }
    };
}
