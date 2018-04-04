package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by technource on 21/12/17.
 */

public class JobDetail_DBO implements Parcelable {

    String cjob_id;
    String ju_id;
    String job_title;
    String user_id;
    String car_id;
    String category_id;
    String subcategory_id;
    String sub_subcategory_id;
    String dropoff_date_time;
    String pickup_date_time;
    String time_flexibility;
    String problem_description;
    String is_emergency;
    String is_help;
    String is_insurance;
    String insurance_company;
    String policy_number;
    String claim_number;
    String job_posted_date;
    String job_status;
    String job_completed_date;
    String invoice_number;
    String assigned_to_garage;
    String followup_string;
    String followup_seen;
    String dropoff_location;
    String alert;
    String sms_alert;
    String push_not_alert;
    String additional_data;
    String catname;
    String subcatname;
    String subsubcatname;
    String total;
    String current_tyre_brand;
    String current_tyre_model;
    String tyre_detail_and_spec;
    String no_of_tyres;
    String loc_for_tow_or_road_assistance;
    String destination_address;
    String inc_roadside_assist;
    String inc_std_log_service;
    String number_of_vehicles;
    String carmake, carmodel, carbadge, registration_number, car_type, km, year;
    ArrayList<CarImageDBO> carImageDBOArrayList = new ArrayList<>();
    ArrayList<CarVideosDbo> carVideosDboArrayList = new ArrayList<>();
    ArrayList<Model_GarageServices> garageServicesArrayList = new ArrayList<>();
    ArrayList<Model_FreeInclusions> freeInclusionsArrayList = new ArrayList<>();
    ArrayList<Model_FollowupWork> followupWorkArrayList = new ArrayList<>();
    boolean isPending;
    boolean isFollowUpAccepted;

    public boolean isFollowUpAccepted() {
        return isFollowUpAccepted;
    }

    public void setFollowUpAccepted(boolean followUpAccepted) {
        isFollowUpAccepted = followUpAccepted;
    }

    public ArrayList<Model_FollowupWork> getFollowupWorkArrayList() {
        return followupWorkArrayList;
    }

    public void setFollowupWorkArrayList(ArrayList<Model_FollowupWork> followupWorkArrayList) {
        this.followupWorkArrayList = followupWorkArrayList;
    }

    public ArrayList<Model_GarageServices> getGarageServicesArrayList() {
        return garageServicesArrayList;
    }

    public void setGarageServicesArrayList(ArrayList<Model_GarageServices> garageServicesArrayList) {
        this.garageServicesArrayList = garageServicesArrayList;
    }

    public ArrayList<Model_FreeInclusions> getFreeInclusionsArrayList() {
        return freeInclusionsArrayList;
    }

    public void setFreeInclusionsArrayList(ArrayList<Model_FreeInclusions> freeInclusionsArrayList) {
        this.freeInclusionsArrayList = freeInclusionsArrayList;
    }

    public boolean isPending() {
        return isPending;
    }

    public void setPending(boolean pending) {
        isPending = pending;
    }

    public ArrayList<CarImageDBO> getCarImageDBOArrayList() {
        return carImageDBOArrayList;
    }

    public void setCarImageDBOArrayList(ArrayList<CarImageDBO> carImageDBOArrayList) {
        this.carImageDBOArrayList = carImageDBOArrayList;
    }

    public ArrayList<CarVideosDbo> getCarVideosDboArrayList() {
        return carVideosDboArrayList;
    }

    public void setCarVideosDboArrayList(ArrayList<CarVideosDbo> carVideosDboArrayList) {
        this.carVideosDboArrayList = carVideosDboArrayList;
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

    public String getCarbadge() {
        return carbadge;
    }

    public void setCarbadge(String carbadge) {
        this.carbadge = carbadge;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getAdditional_inclusions() {
        return additional_inclusions;
    }

    public void setAdditional_inclusions(String additional_inclusions) {
        this.additional_inclusions = additional_inclusions;
    }

    String additional_inclusions;

    JSONArray car_images, car_videos;

    public String getCjob_id() {
        return cjob_id;
    }

    public void setCjob_id(String cjob_id) {
        this.cjob_id = cjob_id;
    }

    public String getJu_id() {
        return ju_id;
    }

    public void setJu_id(String ju_id) {
        this.ju_id = ju_id;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getCar_id() {
        return car_id;
    }

    public void setCar_id(String car_id) {
        this.car_id = car_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSubcategory_id() {
        return subcategory_id;
    }

    public void setSubcategory_id(String subcategory_id) {
        this.subcategory_id = subcategory_id;
    }

    public String getSub_subcategory_id() {
        return sub_subcategory_id;
    }

    public void setSub_subcategory_id(String sub_subcategory_id) {
        this.sub_subcategory_id = sub_subcategory_id;
    }

    public String getDropoff_date_time() {
        return dropoff_date_time;
    }

    public void setDropoff_date_time(String dropoff_date_time) {
        this.dropoff_date_time = dropoff_date_time;
    }

    public String getPickup_date_time() {
        return pickup_date_time;
    }

    public void setPickup_date_time(String pickup_date_time) {
        this.pickup_date_time = pickup_date_time;
    }

    public String getTime_flexibility() {
        return time_flexibility;
    }

    public void setTime_flexibility(String time_flexibility) {
        this.time_flexibility = time_flexibility;
    }

    public String getProblem_description() {
        return problem_description;
    }

    public void setProblem_description(String problem_description) {
        this.problem_description = problem_description;
    }

    public String getIs_emergency() {
        return is_emergency;
    }

    public void setIs_emergency(String is_emergency) {
        this.is_emergency = is_emergency;
    }

    public String getIs_help() {
        return is_help;
    }

    public void setIs_help(String is_help) {
        this.is_help = is_help;
    }

    public String getIs_insurance() {
        return is_insurance;
    }

    public void setIs_insurance(String is_insurance) {
        this.is_insurance = is_insurance;
    }

    public String getInsurance_company() {
        return insurance_company;
    }

    public void setInsurance_company(String insurance_company) {
        this.insurance_company = insurance_company;
    }

    public String getPolicy_number() {
        return policy_number;
    }

    public void setPolicy_number(String policy_number) {
        this.policy_number = policy_number;
    }

    public String getClaim_number() {
        return claim_number;
    }

    public void setClaim_number(String claim_number) {
        this.claim_number = claim_number;
    }

    public String getJob_posted_date() {
        return job_posted_date;
    }

    public void setJob_posted_date(String job_posted_date) {
        this.job_posted_date = job_posted_date;
    }

    public String getJob_status() {
        return job_status;
    }

    public void setJob_status(String job_status) {
        this.job_status = job_status;
    }

    public String getJob_completed_date() {
        return job_completed_date;
    }

    public void setJob_completed_date(String job_completed_date) {
        this.job_completed_date = job_completed_date;
    }

    public String getInvoice_number() {
        return invoice_number;
    }

    public void setInvoice_number(String invoice_number) {
        this.invoice_number = invoice_number;
    }

    public String getAssigned_to_garage() {
        return assigned_to_garage;
    }

    public void setAssigned_to_garage(String assigned_to_garage) {
        this.assigned_to_garage = assigned_to_garage;
    }

    public String getFollowup_string() {
        return followup_string;
    }

    public void setFollowup_string(String followup_string) {
        this.followup_string = followup_string;
    }

    public String getFollowup_seen() {
        return followup_seen;
    }

    public void setFollowup_seen(String followup_seen) {
        this.followup_seen = followup_seen;
    }

    public String getDropoff_location() {
        return dropoff_location;
    }

    public void setDropoff_location(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getSms_alert() {
        return sms_alert;
    }

    public void setSms_alert(String sms_alert) {
        this.sms_alert = sms_alert;
    }

    public String getPush_not_alert() {
        return push_not_alert;
    }

    public void setPush_not_alert(String push_not_alert) {
        this.push_not_alert = push_not_alert;
    }

    public String getAdditional_data() {
        return additional_data;
    }

    public void setAdditional_data(String additional_data) {
        this.additional_data = additional_data;
    }

    public String getCatname() {
        return catname;
    }

    public void setCatname(String catname) {
        this.catname = catname;
    }

    public String getSubcatname() {
        return subcatname;
    }

    public void setSubcatname(String subcatname) {
        this.subcatname = subcatname;
    }

    public String getSubsubcatname() {
        return subsubcatname;
    }

    public void setSubsubcatname(String subsubcatname) {
        this.subsubcatname = subsubcatname;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getCurrent_tyre_brand() {
        return current_tyre_brand;
    }

    public void setCurrent_tyre_brand(String current_tyre_brand) {
        this.current_tyre_brand = current_tyre_brand;
    }

    public String getCurrent_tyre_model() {
        return current_tyre_model;
    }

    public void setCurrent_tyre_model(String current_tyre_model) {
        this.current_tyre_model = current_tyre_model;
    }

    public String getTyre_detail_and_spec() {
        return tyre_detail_and_spec;
    }

    public void setTyre_detail_and_spec(String tyre_detail_and_spec) {
        this.tyre_detail_and_spec = tyre_detail_and_spec;
    }

    public String getNo_of_tyres() {
        return no_of_tyres;
    }

    public void setNo_of_tyres(String no_of_tyres) {
        this.no_of_tyres = no_of_tyres;
    }

    public String getLoc_for_tow_or_road_assistance() {
        return loc_for_tow_or_road_assistance;
    }

    public void setLoc_for_tow_or_road_assistance(String loc_for_tow_or_road_assistance) {
        this.loc_for_tow_or_road_assistance = loc_for_tow_or_road_assistance;
    }

    public String getDestination_address() {
        return destination_address;
    }

    public void setDestination_address(String destination_address) {
        this.destination_address = destination_address;
    }

    public String getInc_roadside_assist() {
        return inc_roadside_assist;
    }

    public void setInc_roadside_assist(String inc_roadside_assist) {
        this.inc_roadside_assist = inc_roadside_assist;
    }

    public String getInc_std_log_service() {
        return inc_std_log_service;
    }

    public void setInc_std_log_service(String inc_std_log_service) {
        this.inc_std_log_service = inc_std_log_service;
    }

    public String getNumber_of_vehicles() {
        return number_of_vehicles;
    }

    public void setNumber_of_vehicles(String number_of_vehicles) {
        this.number_of_vehicles = number_of_vehicles;
    }

    public JSONArray getCar_images() {
        return car_images;
    }

    public void setCar_images(JSONArray car_images) {
        this.car_images = car_images;
    }

    public JSONArray getCar_videos() {
        return car_videos;
    }

    public void setCar_videos(JSONArray car_videos) {
        this.car_videos = car_videos;
    }

    public JobDetail_DBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.cjob_id);
        dest.writeString(this.ju_id);
        dest.writeString(this.job_title);
        dest.writeString(this.user_id);
        dest.writeString(this.car_id);
        dest.writeString(this.category_id);
        dest.writeString(this.subcategory_id);
        dest.writeString(this.sub_subcategory_id);
        dest.writeString(this.dropoff_date_time);
        dest.writeString(this.pickup_date_time);
        dest.writeString(this.time_flexibility);
        dest.writeString(this.problem_description);
        dest.writeString(this.is_emergency);
        dest.writeString(this.is_help);
        dest.writeString(this.is_insurance);
        dest.writeString(this.insurance_company);
        dest.writeString(this.policy_number);
        dest.writeString(this.claim_number);
        dest.writeString(this.job_posted_date);
        dest.writeString(this.job_status);
        dest.writeString(this.job_completed_date);
        dest.writeString(this.invoice_number);
        dest.writeString(this.assigned_to_garage);
        dest.writeString(this.followup_string);
        dest.writeString(this.followup_seen);
        dest.writeString(this.dropoff_location);
        dest.writeString(this.alert);
        dest.writeString(this.sms_alert);
        dest.writeString(this.push_not_alert);
        dest.writeString(this.additional_data);
        dest.writeString(this.catname);
        dest.writeString(this.subcatname);
        dest.writeString(this.subsubcatname);
        dest.writeString(this.total);
        dest.writeString(this.current_tyre_brand);
        dest.writeString(this.current_tyre_model);
        dest.writeString(this.tyre_detail_and_spec);
        dest.writeString(this.no_of_tyres);
        dest.writeString(this.loc_for_tow_or_road_assistance);
        dest.writeString(this.destination_address);
        dest.writeString(this.inc_roadside_assist);
        dest.writeString(this.inc_std_log_service);
        dest.writeString(this.number_of_vehicles);
        dest.writeString(this.carmake);
        dest.writeString(this.carmodel);
        dest.writeString(this.carbadge);
        dest.writeString(this.registration_number);
        dest.writeString(this.car_type);
        dest.writeString(this.km);
        dest.writeString(this.year);
        dest.writeList(this.carImageDBOArrayList);
        dest.writeList(this.carVideosDboArrayList);
        dest.writeList(this.garageServicesArrayList);
        dest.writeList(this.freeInclusionsArrayList);
        dest.writeTypedList(this.followupWorkArrayList);
        dest.writeByte(this.isPending ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isFollowUpAccepted ? (byte) 1 : (byte) 0);
        dest.writeString(this.additional_inclusions);

    }

    protected JobDetail_DBO(Parcel in) {
        this.cjob_id = in.readString();
        this.ju_id = in.readString();
        this.job_title = in.readString();
        this.user_id = in.readString();
        this.car_id = in.readString();
        this.category_id = in.readString();
        this.subcategory_id = in.readString();
        this.sub_subcategory_id = in.readString();
        this.dropoff_date_time = in.readString();
        this.pickup_date_time = in.readString();
        this.time_flexibility = in.readString();
        this.problem_description = in.readString();
        this.is_emergency = in.readString();
        this.is_help = in.readString();
        this.is_insurance = in.readString();
        this.insurance_company = in.readString();
        this.policy_number = in.readString();
        this.claim_number = in.readString();
        this.job_posted_date = in.readString();
        this.job_status = in.readString();
        this.job_completed_date = in.readString();
        this.invoice_number = in.readString();
        this.assigned_to_garage = in.readString();
        this.followup_string = in.readString();
        this.followup_seen = in.readString();
        this.dropoff_location = in.readString();
        this.alert = in.readString();
        this.sms_alert = in.readString();
        this.push_not_alert = in.readString();
        this.additional_data = in.readString();
        this.catname = in.readString();
        this.subcatname = in.readString();
        this.subsubcatname = in.readString();
        this.total = in.readString();
        this.current_tyre_brand = in.readString();
        this.current_tyre_model = in.readString();
        this.tyre_detail_and_spec = in.readString();
        this.no_of_tyres = in.readString();
        this.loc_for_tow_or_road_assistance = in.readString();
        this.destination_address = in.readString();
        this.inc_roadside_assist = in.readString();
        this.inc_std_log_service = in.readString();
        this.number_of_vehicles = in.readString();
        this.carmake = in.readString();
        this.carmodel = in.readString();
        this.carbadge = in.readString();
        this.registration_number = in.readString();
        this.car_type = in.readString();
        this.km = in.readString();
        this.year = in.readString();
        this.carImageDBOArrayList = new ArrayList<CarImageDBO>();
        in.readList(this.carImageDBOArrayList, CarImageDBO.class.getClassLoader());
        this.carVideosDboArrayList = new ArrayList<CarVideosDbo>();
        in.readList(this.carVideosDboArrayList, CarVideosDbo.class.getClassLoader());
        this.garageServicesArrayList = new ArrayList<Model_GarageServices>();
        in.readList(this.garageServicesArrayList, Model_GarageServices.class.getClassLoader());
        this.freeInclusionsArrayList = new ArrayList<Model_FreeInclusions>();
        in.readList(this.freeInclusionsArrayList, Model_FreeInclusions.class.getClassLoader());
        this.followupWorkArrayList = in.createTypedArrayList(Model_FollowupWork.CREATOR);
        this.isPending = in.readByte() != 0;
        this.isFollowUpAccepted = in.readByte() != 0;
        this.additional_inclusions = in.readString();
        this.car_images = in.readParcelable(JSONArray.class.getClassLoader());
        this.car_videos = in.readParcelable(JSONArray.class.getClassLoader());
    }

    public static final Creator<JobDetail_DBO> CREATOR = new Creator<JobDetail_DBO>() {
        @Override
        public JobDetail_DBO createFromParcel(Parcel source) {
            return new JobDetail_DBO(source);
        }

        @Override
        public JobDetail_DBO[] newArray(int size) {
            return new JobDetail_DBO[size];
        }
    };
}
