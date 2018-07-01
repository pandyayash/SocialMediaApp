package aayushiprojects.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 12/12/17.
 */

public class NewPostedJob_DBO implements Parcelable {
    public static final Creator<NewPostedJob_DBO> CREATOR = new Creator<NewPostedJob_DBO>() {
        @Override
        public NewPostedJob_DBO createFromParcel(Parcel source) {
            return new NewPostedJob_DBO(source);
        }

        @Override
        public NewPostedJob_DBO[] newArray(int size) {
            return new NewPostedJob_DBO[size];
        }
    };
    String jobTitle, date, jobDescription, CarModel, price, jobID, job_status, category_id, distnace = "";
   // String make, badge, year;
    int totalQuotes;
    String fname, lname, suburb, cJobId;
    boolean isFromCompleted = false;
    String grating, greview, urating, ureview;
    boolean isFromJobsFeed = false;

    public NewPostedJob_DBO() {
    }

    protected NewPostedJob_DBO(Parcel in) {
        this.jobTitle = in.readString();
        this.date = in.readString();
        this.jobDescription = in.readString();
        this.CarModel = in.readString();
        this.price = in.readString();
        this.jobID = in.readString();
        this.job_status = in.readString();
        this.category_id = in.readString();
        this.distnace = in.readString();
        this.make = in.readString();
        this.badge = in.readString();
        this.year = in.readString();
        this.totalQuotes = in.readInt();
        this.fname = in.readString();
        this.lname = in.readString();
        this.suburb = in.readString();
        this.cJobId = in.readString();
        this.isFromCompleted = in.readByte() != 0;
        this.grating = in.readString();
        this.greview = in.readString();
        this.urating = in.readString();
        this.ureview = in.readString();
        this.isFromJobsFeed = in.readByte() != 0;
    }
    String make, badge, year,catName;

    public String getcJobId() {
        return cJobId;
    }


    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public void setcJobId(String cJobId) {
        this.cJobId = cJobId;
    }

    public String getGrating() {
        return grating;
    }

    public void setGrating(String grating) {
        this.grating = grating;
    }

    public String getGreview() {
        return greview;
    }

    public void setGreview(String greview) {
        this.greview = greview;
    }

    public boolean isFromCompleted() {
        return isFromCompleted;
    }

    public void setFromCompleted(boolean fromCompleted) {
        isFromCompleted = fromCompleted;
    }

    public String getUrating() {
        return urating;
    }

    public void setUrating(String urating) {
        this.urating = urating;
    }

    public String getUreview() {
        return ureview;
    }

    public void setUreview(String ureview) {
        this.ureview = ureview;
    }

    public String getJobID() {
        return jobID;
    }

    public void setJobID(String jobID) {

        this.jobID = jobID;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getSuburb() {
        return suburb;
    }

    public void setSuburb(String suburb) {
        this.suburb = suburb;
    }

    public boolean isFromJobsFeed() {
        return isFromJobsFeed;
    }

    public void setFromJobsFeed(boolean fromJobsFeed) {
        isFromJobsFeed = fromJobsFeed;
    }

    public String getDistnace() {
        return distnace;
    }

    public void setDistnace(String distnace) {
        this.distnace = distnace;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public int getTotalQuotes() {
        return totalQuotes;
    }

    public void setTotalQuotes(int totalQuotes) {
        this.totalQuotes = totalQuotes;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public String getCarModel() {
        return CarModel;
    }

    public void setCarModel(String carModel) {
        CarModel = carModel;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobTitle);
        dest.writeString(this.date);
        dest.writeString(this.jobDescription);
        dest.writeString(this.CarModel);
        dest.writeString(this.price);
        dest.writeString(this.jobID);
        dest.writeString(this.job_status);
        dest.writeString(this.category_id);
        dest.writeString(this.distnace);
        dest.writeString(this.make);
        dest.writeString(this.badge);
        dest.writeString(this.year);
        dest.writeInt(this.totalQuotes);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.suburb);
        dest.writeString(this.cJobId);
        dest.writeByte(this.isFromCompleted ? (byte) 1 : (byte) 0);
        dest.writeString(this.grating);
        dest.writeString(this.greview);
        dest.writeString(this.urating);
        dest.writeString(this.ureview);
        dest.writeByte(this.isFromJobsFeed ? (byte) 1 : (byte) 0);
    }
}
