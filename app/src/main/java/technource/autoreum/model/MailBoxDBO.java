package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 13/2/18.
 */

public class MailBoxDBO implements Parcelable {
    String id, type, dateTime, garageId, juId, invoiceId, jobTitle, carId, catName, subCatName, subSubCatName, sid;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getGarageId() {
        return garageId;
    }

    public void setGarageId(String garageId) {
        this.garageId = garageId;
    }

    public String getJuId() {
        return juId;
    }

    public void setJuId(String juId) {
        this.juId = juId;
    }

    public String getInvoiceId() {
        return invoiceId;
    }

    public void setInvoiceId(String invoiceId) {
        this.invoiceId = invoiceId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getCarId() {
        return carId;
    }

    public void setCarId(String carId) {
        this.carId = carId;
    }

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public String getSubCatName() {
        return subCatName;
    }

    public void setSubCatName(String subCatName) {
        this.subCatName = subCatName;
    }

    public String getSubSubCatName() {
        return subSubCatName;
    }

    public void setSubSubCatName(String subSubCatName) {
        this.subSubCatName = subSubCatName;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    @Override
    public int describeContents() {
        return 0;

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.type);
        dest.writeString(this.dateTime);
        dest.writeString(this.garageId);
        dest.writeString(this.juId);
        dest.writeString(this.invoiceId);
        dest.writeString(this.jobTitle);
        dest.writeString(this.carId);
        dest.writeString(this.catName);
        dest.writeString(this.subCatName);
        dest.writeString(this.subSubCatName);
        dest.writeString(this.sid);
    }

    public MailBoxDBO() {
    }

    protected MailBoxDBO(Parcel in) {
        this.id = in.readString();
        this.type = in.readString();
        this.dateTime = in.readString();
        this.garageId = in.readString();
        this.juId = in.readString();
        this.invoiceId = in.readString();
        this.jobTitle = in.readString();
        this.carId = in.readString();
        this.catName = in.readString();
        this.subCatName = in.readString();
        this.subSubCatName = in.readString();
        this.sid = in.readString();
    }

    public static final Parcelable.Creator<MailBoxDBO> CREATOR = new Parcelable.Creator<MailBoxDBO>() {
        @Override
        public MailBoxDBO createFromParcel(Parcel source) {
            return new MailBoxDBO(source);
        }

        @Override
        public MailBoxDBO[] newArray(int size) {
            return new MailBoxDBO[size];
        }
    };
}
