package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by root on 14/2/18.
 */

public class DetailsOfGarageCarJobDBO implements Parcelable {

    String jobTitle, bidPrice, additionalOffer, additonalOfferPrice;
    ArrayList<String> followUpWorkList;
    String date;
    String cjobId;
    String rating;
    String jobsCompleted;

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    String totalEarning;
    String catId;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCjobId() {
        return cjobId;
    }

    public void setCjobId(String cjobId) {
        this.cjobId = cjobId;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getBidPrice() {
        return bidPrice;
    }

    public void setBidPrice(String bidPrice) {
        this.bidPrice = bidPrice;
    }

    public String getAdditionalOffer() {
        return additionalOffer;
    }

    public void setAdditionalOffer(String additionalOffer) {
        this.additionalOffer = additionalOffer;
    }

    public String getAdditonalOfferPrice() {
        return additonalOfferPrice;
    }

    public void setAdditonalOfferPrice(String additonalOfferPrice) {
        this.additonalOfferPrice = additonalOfferPrice;
    }

    public ArrayList<String> getFollowUpWorkList() {
        return followUpWorkList;
    }

    public void setFollowUpWorkList(ArrayList<String> followUpWorkList) {
        this.followUpWorkList = followUpWorkList;
    }

    public String getJuId() {
        return juId;
    }

    public void setJuId(String juId) {
        this.juId = juId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    String juId, description, unit, unitPrice;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCuId() {
        return cuId;
    }

    public void setCuId(String cuId) {
        this.cuId = cuId;
    }

    public String getManufactureId() {
        return manufactureId;
    }

    public void setManufactureId(String manufactureId) {
        this.manufactureId = manufactureId;
    }

    public String getModelId() {
        return modelId;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public String getBadgeId() {
        return badgeId;
    }

    public void setBadgeId(String badgeId) {
        this.badgeId = badgeId;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCarMake() {
        return carMake;
    }

    public void setCarMake(String carMake) {
        this.carMake = carMake;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarBadge() {
        return carBadge;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSubrub() {
        return subrub;
    }

    public void setSubrub(String subrub) {
        this.subrub = subrub;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBussinessName() {
        return bussinessName;
    }

    public void setBussinessName(String bussinessName) {
        this.bussinessName = bussinessName;
    }

    public String getGarageSubrub() {
        return garageSubrub;
    }

    public void setGarageSubrub(String garageSubrub) {
        this.garageSubrub = garageSubrub;
    }

    public String getGarageState() {
        return garageState;
    }

    public void setGarageState(String garageState) {
        this.garageState = garageState;
    }

    public String getGaragePostcode() {
        return garagePostcode;
    }

    public void setGaragePostcode(String garagePostcode) {
        this.garagePostcode = garagePostcode;
    }

    public String getGarageMobile() {
        return garageMobile;
    }

    public void setGarageMobile(String garageMobile) {
        this.garageMobile = garageMobile;
    }

    public String getGarageAbnNo() {
        return garageAbnNo;
    }

    public void setGarageAbnNo(String garageAbnNo) {
        this.garageAbnNo = garageAbnNo;
    }

    public String getGarageEmail() {
        return garageEmail;
    }

    public void setGarageEmail(String garageEmail) {
        this.garageEmail = garageEmail;
    }

    public void setCarBadge(String carBadge) {

        this.carBadge = carBadge;
    }


    String id, cuId, manufactureId, modelId, badgeId, carType, km, registrationNumber, year, cylinders, comment,
            userId, carMake, carModel, carBadge, userName, fname, lname, email, subrub, state, postcode, mobile,
            bussinessName, garageSubrub, garageState, garagePostcode, garageMobile, garageAbnNo, garageEmail;

    public DetailsOfGarageCarJobDBO() {
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getJobsCompleted() {
        return jobsCompleted;
    }

    public void setJobsCompleted(String jobsCompleted) {
        this.jobsCompleted = jobsCompleted;
    }

    public String getTotalEarning() {
        return totalEarning;
    }

    public void setTotalEarning(String totalEarning) {
        this.totalEarning = totalEarning;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.jobTitle);
        dest.writeString(this.bidPrice);
        dest.writeString(this.additionalOffer);
        dest.writeString(this.additonalOfferPrice);
        dest.writeStringList(this.followUpWorkList);
        dest.writeString(this.date);
        dest.writeString(this.cjobId);
        dest.writeString(this.rating);
        dest.writeString(this.jobsCompleted);
        dest.writeString(this.totalEarning);
        dest.writeString(this.catId);
        dest.writeString(this.juId);
        dest.writeString(this.description);
        dest.writeString(this.unit);
        dest.writeString(this.unitPrice);
        dest.writeString(this.id);
        dest.writeString(this.cuId);
        dest.writeString(this.manufactureId);
        dest.writeString(this.modelId);
        dest.writeString(this.badgeId);
        dest.writeString(this.carType);
        dest.writeString(this.km);
        dest.writeString(this.registrationNumber);
        dest.writeString(this.year);
        dest.writeString(this.cylinders);
        dest.writeString(this.comment);
        dest.writeString(this.userId);
        dest.writeString(this.carMake);
        dest.writeString(this.carModel);
        dest.writeString(this.carBadge);
        dest.writeString(this.userName);
        dest.writeString(this.fname);
        dest.writeString(this.lname);
        dest.writeString(this.email);
        dest.writeString(this.subrub);
        dest.writeString(this.state);
        dest.writeString(this.postcode);
        dest.writeString(this.mobile);
        dest.writeString(this.bussinessName);
        dest.writeString(this.garageSubrub);
        dest.writeString(this.garageState);
        dest.writeString(this.garagePostcode);
        dest.writeString(this.garageMobile);
        dest.writeString(this.garageAbnNo);
        dest.writeString(this.garageEmail);
    }

    protected DetailsOfGarageCarJobDBO(Parcel in) {
        this.jobTitle = in.readString();
        this.bidPrice = in.readString();
        this.additionalOffer = in.readString();
        this.additonalOfferPrice = in.readString();
        this.followUpWorkList = in.createStringArrayList();
        this.date = in.readString();
        this.cjobId = in.readString();
        this.rating = in.readString();
        this.jobsCompleted = in.readString();
        this.totalEarning = in.readString();
        this.catId = in.readString();
        this.juId = in.readString();
        this.description = in.readString();
        this.unit = in.readString();
        this.unitPrice = in.readString();
        this.id = in.readString();
        this.cuId = in.readString();
        this.manufactureId = in.readString();
        this.modelId = in.readString();
        this.badgeId = in.readString();
        this.carType = in.readString();
        this.km = in.readString();
        this.registrationNumber = in.readString();
        this.year = in.readString();
        this.cylinders = in.readString();
        this.comment = in.readString();
        this.userId = in.readString();
        this.carMake = in.readString();
        this.carModel = in.readString();
        this.carBadge = in.readString();
        this.userName = in.readString();
        this.fname = in.readString();
        this.lname = in.readString();
        this.email = in.readString();
        this.subrub = in.readString();
        this.state = in.readString();
        this.postcode = in.readString();
        this.mobile = in.readString();
        this.bussinessName = in.readString();
        this.garageSubrub = in.readString();
        this.garageState = in.readString();
        this.garagePostcode = in.readString();
        this.garageMobile = in.readString();
        this.garageAbnNo = in.readString();
        this.garageEmail = in.readString();
    }

    public static final Creator<DetailsOfGarageCarJobDBO> CREATOR = new Creator<DetailsOfGarageCarJobDBO>() {
        @Override
        public DetailsOfGarageCarJobDBO createFromParcel(Parcel source) {
            return new DetailsOfGarageCarJobDBO(source);
        }

        @Override
        public DetailsOfGarageCarJobDBO[] newArray(int size) {
            return new DetailsOfGarageCarJobDBO[size];
        }
    };
}
