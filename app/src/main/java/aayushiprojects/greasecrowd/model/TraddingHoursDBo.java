package aayushiprojects.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 13/9/17.
 */

public class TraddingHoursDBo implements Parcelable{

    String day;

    public boolean isselected() {
        return isselected;
    }

    public void setIsselected(boolean isselected) {
        this.isselected = isselected;
    }

    public String getGetOuttimeampm() {
        return getOuttimeampm;
    }

    public void setGetOuttimeampm(String getOuttimeampm) {
        this.getOuttimeampm = getOuttimeampm;
    }

    public String getOuttime() {
        return outtime;
    }

    public void setOuttime(String outtime) {
        this.outtime = outtime;
    }

    public String getIntimeampm() {
        return intimeampm;
    }

    public void setIntimeampm(String intimeampm) {
        this.intimeampm = intimeampm;
    }

    public String getIntime() {
        return intime;
    }

    public void setIntime(String intime) {
        this.intime = intime;
    }

    public String getTag() {
        return day;
    }

    public void setTag(String day) {
        this.day = day;
    }

    String intime="8:00";
    String intimeampm="AM";
    String outtime="5:30";
    String getOuttimeampm="PM";
    boolean isselected = false;


    String shortday;
    String from;
    String to;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeString(this.intime);
        dest.writeString(this.intimeampm);
        dest.writeString(this.outtime);
        dest.writeString(this.getOuttimeampm);
        dest.writeByte(this.isselected ? (byte) 1 : (byte) 0);
        dest.writeString(this.shortday);
        dest.writeString(this.from);
        dest.writeString(this.to);
    }

    public TraddingHoursDBo() {
    }

    protected TraddingHoursDBo(Parcel in) {
        this.day = in.readString();
        this.intime = in.readString();
        this.intimeampm = in.readString();
        this.outtime = in.readString();
        this.getOuttimeampm = in.readString();
        this.isselected = in.readByte() != 0;
        this.shortday = in.readString();
        this.from = in.readString();
        this.to = in.readString();
    }

    public static final Creator<TraddingHoursDBo> CREATOR = new Creator<TraddingHoursDBo>() {
        @Override
        public TraddingHoursDBo createFromParcel(Parcel source) {
            return new TraddingHoursDBo(source);
        }

        @Override
        public TraddingHoursDBo[] newArray(int size) {
            return new TraddingHoursDBo[size];
        }
    };
}
