package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 28/9/17.
 */

public class ViewHistoryDBO implements Parcelable {

    String job_title,garage_name,cjob_id,ju_id,problem_description,car_type,job_posted_date,rating_stars;

    @Override
    public int describeContents() {
        return 0;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public String getGarage_name() {
        return garage_name;
    }

    public void setGarage_name(String garage_name) {
        this.garage_name = garage_name;
    }

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

    public String getProblem_description() {
        return problem_description;
    }

    public void setProblem_description(String problem_description) {
        this.problem_description = problem_description;
    }

    public String getCar_type() {
        return car_type;
    }

    public void setCar_type(String car_type) {
        this.car_type = car_type;
    }

    public String getJob_posted_date() {
        return job_posted_date;
    }

    public void setJob_posted_date(String job_posted_date) {
        this.job_posted_date = job_posted_date;
    }

    public String getRating_stars() {
        return rating_stars;
    }

    public void setRating_stars(String rating_stars) {
        this.rating_stars = rating_stars;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.job_title);

        dest.writeString(this.garage_name);
        dest.writeString(this.cjob_id);
        dest.writeString(this.ju_id);
        dest.writeString(this.problem_description);
        dest.writeString(this.car_type);
        dest.writeString(this.job_posted_date);
        dest.writeString(this.rating_stars);
    }

    public ViewHistoryDBO() {
    }

    protected ViewHistoryDBO(Parcel in) {
        this.job_title = in.readString();
        this.garage_name = in.readString();
        this.cjob_id = in.readString();
        this.ju_id = in.readString();
        this.problem_description = in.readString();
        this.car_type = in.readString();
        this.job_posted_date = in.readString();
        this.rating_stars = in.readString();
    }

    public static final Creator<ViewHistoryDBO> CREATOR = new Creator<ViewHistoryDBO>() {
        @Override
        public ViewHistoryDBO createFromParcel(Parcel source) {
            return new ViewHistoryDBO(source);
        }

        @Override
        public ViewHistoryDBO[] newArray(int size) {
            return new ViewHistoryDBO[size];
        }
    };
}
