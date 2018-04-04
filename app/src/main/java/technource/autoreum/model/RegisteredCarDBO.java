package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 25/9/17.
 */

public class RegisteredCarDBO implements Parcelable {

    String id;
            String cu_id;
    String manufacture_id;
    String model_id;
    String badge_id;
    String car_type;
    String km;
    String registration_number;
    String year;
    String cylinders;
    String comment;
    int user_id;
    String carmake;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCu_id() {
        return cu_id;
    }

    public void setCu_id(String cu_id) {
        this.cu_id = cu_id;
    }

    public String getManufacture_id() {
        return manufacture_id;
    }

    public void setManufacture_id(String manufacture_id) {
        this.manufacture_id = manufacture_id;
    }

    public String getBadge_id() {
        return badge_id;
    }

    public void setBadge_id(String badge_id) {
        this.badge_id = badge_id;
    }

    public String getModel_id() {
        return model_id;
    }

    public void setModel_id(String model_id) {
        this.model_id = model_id;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCylinders() {
        return cylinders;
    }

    public void setCylinders(String cylinders) {
        this.cylinders = cylinders;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCarmake() {
        return carmake;
    }

    public void setCarmake(String carmake) {
        this.carmake = carmake;
    }

    public String getCarmodel() {
        return carmodel;
    }

    public void setCarmodel(String carmodel) {
        this.carmodel = carmodel;
    }

    public String getCar_img() {
        return car_img;
    }

    public void setCar_img(String car_img) {
        this.car_img = car_img;
    }

    public String getCarbadge() {
        return carbadge;
    }

    public void setCarbadge(String carbadge) {
        this.carbadge = carbadge;
    }

    String carmodel;
    String car_img;
    String carbadge;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.cu_id);
        dest.writeString(this.manufacture_id);
        dest.writeString(this.model_id);
        dest.writeString(this.badge_id);
        dest.writeString(this.car_type);
        dest.writeString(this.km);
        dest.writeString(this.registration_number);
        dest.writeString(this.year);
        dest.writeString(this.cylinders);
        dest.writeString(this.comment);
        dest.writeInt(this.user_id);
        dest.writeString(this.carmake);
        dest.writeString(this.carmodel);
        dest.writeString(this.car_img);
        dest.writeString(this.carbadge);
    }

    public RegisteredCarDBO() {
    }

    protected RegisteredCarDBO(Parcel in) {
        this.id = in.readString();
        this.cu_id = in.readString();
        this.manufacture_id = in.readString();
        this.model_id = in.readString();
        this.badge_id = in.readString();
        this.car_type = in.readString();
        this.km = in.readString();
        this.registration_number = in.readString();
        this.year = in.readString();
        this.cylinders = in.readString();
        this.comment = in.readString();
        this.user_id = in.readInt();
        this.carmake = in.readString();
        this.carmodel = in.readString();
        this.car_img = in.readString();
        this.carbadge = in.readString();
    }

    public static final Creator<RegisteredCarDBO> CREATOR = new Creator<RegisteredCarDBO>() {
        @Override
        public RegisteredCarDBO createFromParcel(Parcel source) {
            return new RegisteredCarDBO(source);
        }

        @Override
        public RegisteredCarDBO[] newArray(int size) {
            return new RegisteredCarDBO[size];
        }
    };
}
