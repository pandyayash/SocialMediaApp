package aayushiprojects.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by technource on 19/12/17.
 */

public class AwardJobDBOCarOwner implements Parcelable {

    String job_id;
    String garage_id;
    String bid_price;
    String id;
    String bid_comment;
    String add_offer;
    String add_offer_price;
    String add_offer_accept;
    String total;
    String bid_status;
    String free_inclusion;
    String distance;
    String review_count;
    String avatar_img;
    String name;
    String make;
    String model;
    String badge;
    String service_id;
    String new_drop_off_date_time;
    String new_pick_up_date_time;

    public String getNew_pick_up_date_time() {
        return new_pick_up_date_time;
    }

    public void setNew_pick_up_date_time(String new_pick_up_date_time) {
        this.new_pick_up_date_time = new_pick_up_date_time;
    }

    public String getIsSeftyReportGenerated() {
        return isSeftyReportGenerated;
    }

    public void setIsSeftyReportGenerated(String isSeftyReportGenerated) {
        this.isSeftyReportGenerated = isSeftyReportGenerated;
    }

    String current_job_status;
    String paymet_garage_id;
    String payment_type;
    float rating;
    String isSeftyReportGenerated;
    ArrayList<Model_FreeInclusions> freeInclusionsArrayList = new ArrayList<>();
    ArrayList<Model_GarageServices> garageServicesArrayList = new ArrayList<>();

    public String getPaymet_garage_id() {
        return paymet_garage_id;
    }

    public void setPaymet_garage_id(String paymet_garage_id) {
        this.paymet_garage_id = paymet_garage_id;
    }

    public String getPayment_type() {
        return payment_type;
    }

    public void setPayment_type(String payment_type) {
        this.payment_type = payment_type;
    }

    public String getCurrent_job_status() {
        return current_job_status;
    }

    public void setCurrent_job_status(String current_job_status) {
        this.current_job_status = current_job_status;
    }

    public String getNew_drop_off_date_time() {
        return new_drop_off_date_time;
    }

    public void setNew_drop_off_date_time(String new_drop_off_date_time) {
        this.new_drop_off_date_time = new_drop_off_date_time;
    }

    public String getEw_pick_up_date_time() {
        return new_pick_up_date_time;
    }

    public void setEw_pick_up_date_time(String ew_pick_up_date_time) {
        this.new_pick_up_date_time = ew_pick_up_date_time;
    }

    public ArrayList<Model_FreeInclusions> getFreeInclusionsArrayList() {
        return freeInclusionsArrayList;
    }

    public void setFreeInclusionsArrayList(ArrayList<Model_FreeInclusions> freeInclusionsArrayList) {
        this.freeInclusionsArrayList = freeInclusionsArrayList;
    }

    public ArrayList<Model_GarageServices> getGarageServicesArrayList() {
        return garageServicesArrayList;
    }

    public void setGarageServicesArrayList(ArrayList<Model_GarageServices> garageServicesArrayList) {
        this.garageServicesArrayList = garageServicesArrayList;
    }

    public String getService_id() {
        return service_id;
    }

    public void setService_id(String service_id) {
        this.service_id = service_id;
    }


    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBadge() {
        return badge;
    }

    public void setBadge(String badge) {
        this.badge = badge;
    }


    public String getAdd_offer_accept() {
        return add_offer_accept;
    }

    public void setAdd_offer_accept(String add_offer_accept) {
        this.add_offer_accept = add_offer_accept;
    }

    ArrayList<String> freeInclusion = new ArrayList<>();
    ArrayList<String> carImages = new ArrayList<>();
    ArrayList<String> services = new ArrayList<>();

    public ArrayList<String> getCarImages() {
        return carImages;
    }

    public void setCarImages(ArrayList<String> carImages) {
        this.carImages = carImages;
    }

    public AwardJobDBOCarOwner() {
    }

    public String getJob_id() {
        return job_id;
    }

    public void setJob_id(String job_id) {
        this.job_id = job_id;
    }

    public String getGarage_id() {
        return garage_id;
    }

    public void setGarage_id(String garage_id) {
        this.garage_id = garage_id;
    }

    public String getBid_price() {
        return bid_price;
    }

    public void setBid_price(String bid_price) {
        this.bid_price = bid_price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBid_comment() {
        return bid_comment;
    }

    public void setBid_comment(String bid_comment) {
        this.bid_comment = bid_comment;
    }

    public String getAdd_offer() {
        return add_offer;
    }

    public void setAdd_offer(String add_offer) {
        this.add_offer = add_offer;
    }

    public String getAdd_offer_price() {
        return add_offer_price;
    }

    public void setAdd_offer_price(String add_offer_price) {
        this.add_offer_price = add_offer_price;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getBid_status() {
        return bid_status;
    }

    public void setBid_status(String bid_status) {
        this.bid_status = bid_status;
    }

    public String getFree_inclusion() {
        return free_inclusion;
    }

    public void setFree_inclusion(String free_inclusion) {
        this.free_inclusion = free_inclusion;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getReview_count() {
        return review_count;
    }

    public void setReview_count(String review_count) {
        this.review_count = review_count;
    }

    public String getAvatar_img() {
        return avatar_img;
    }

    public ArrayList<String> getServices() {
        return services;
    }

    public void setServices(ArrayList<String> services) {
        this.services = services;
    }

    public void setAvatar_img(String avatar_img) {
        this.avatar_img = avatar_img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public ArrayList<String> getFreeInclusion() {
        return freeInclusion;
    }

    public void setFreeInclusion(ArrayList<String> freeInclusion) {
        this.freeInclusion = freeInclusion;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.job_id);
        dest.writeString(this.garage_id);
        dest.writeString(this.bid_price);
        dest.writeString(this.id);
        dest.writeString(this.bid_comment);
        dest.writeString(this.add_offer);
        dest.writeString(this.add_offer_price);
        dest.writeString(this.add_offer_accept);
        dest.writeString(this.total);
        dest.writeString(this.bid_status);
        dest.writeString(this.free_inclusion);
        dest.writeString(this.distance);
        dest.writeString(this.review_count);
        dest.writeString(this.avatar_img);
        dest.writeString(this.name);
        dest.writeString(this.make);
        dest.writeString(this.model);
        dest.writeString(this.badge);
        dest.writeString(this.service_id);
        dest.writeString(this.new_drop_off_date_time);
        dest.writeString(this.new_pick_up_date_time);
        dest.writeString(this.current_job_status);
        dest.writeString(this.paymet_garage_id);
        dest.writeString(this.payment_type);
        dest.writeFloat(this.rating);
        dest.writeString(this.isSeftyReportGenerated);
        //dest.writeList(this.freeInclusionsArrayList);
        dest.writeList(this.garageServicesArrayList);
        dest.writeStringList(this.freeInclusion);
        dest.writeStringList(this.carImages);
        dest.writeStringList(this.services);
    }

    protected AwardJobDBOCarOwner(Parcel in) {
        this.job_id = in.readString();
        this.garage_id = in.readString();
        this.bid_price = in.readString();
        this.id = in.readString();
        this.bid_comment = in.readString();
        this.add_offer = in.readString();
        this.add_offer_price = in.readString();
        this.add_offer_accept = in.readString();
        this.total = in.readString();
        this.bid_status = in.readString();
        this.free_inclusion = in.readString();
        this.distance = in.readString();
        this.review_count = in.readString();
        this.avatar_img = in.readString();
        this.name = in.readString();
        this.make = in.readString();
        this.model = in.readString();
        this.badge = in.readString();
        this.service_id = in.readString();
        this.new_drop_off_date_time = in.readString();
        this.new_pick_up_date_time = in.readString();
        this.current_job_status = in.readString();
        this.paymet_garage_id = in.readString();
        this.payment_type = in.readString();
        this.rating = in.readFloat();
        this.isSeftyReportGenerated = in.readString();
//        this.freeInclusionsArrayList = new ArrayList<Model_FreeInclusions>();
//        in.readList(this.freeInclusionsArrayList, Model_FreeInclusions.class.getClassLoader());
        this.garageServicesArrayList = new ArrayList<Model_GarageServices>();
        in.readList(this.garageServicesArrayList, Model_GarageServices.class.getClassLoader());
        this.freeInclusion = in.createStringArrayList();
        this.carImages = in.createStringArrayList();
        this.services = in.createStringArrayList();
    }

    public static final Creator<AwardJobDBOCarOwner> CREATOR = new Creator<AwardJobDBOCarOwner>() {
        @Override
        public AwardJobDBOCarOwner createFromParcel(Parcel source) {
            return new AwardJobDBOCarOwner(source);
        }

        @Override
        public AwardJobDBOCarOwner[] newArray(int size) {
            return new AwardJobDBOCarOwner[size];
        }
    };
}
