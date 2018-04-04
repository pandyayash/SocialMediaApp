package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 27/9/17.
 */

public class ReviewsDBO implements Parcelable {

  String jobtitle;
  String image;
  String rating;
  String description;
  String name;
  String address;
  String response;
  String distnace;
  String datetime;

  public String getDatetime() {
    return datetime;
  }

  public void setDatetime(String datetime) {
    this.datetime = datetime;
  }

  public String getDistnace() {
    return distnace;
  }

  public void setDistnace(String distnace) {
    this.distnace = distnace;
  }

  public String getJobtitle() {
    return jobtitle;
  }

  public void setJobtitle(String jobtitle) {
    this.jobtitle = jobtitle;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getRating() {
    return rating;
  }

  public void setRating(String rating) {
    this.rating = rating;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getResponse() {
    return response;
  }

  public void setResponse(String response) {
    this.response = response;
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.jobtitle);
    dest.writeString(this.image);
    dest.writeString(this.rating);
    dest.writeString(this.description);
    dest.writeString(this.name);
    dest.writeString(this.address);
    dest.writeString(this.response);
    dest.writeString(this.distnace);
    dest.writeString(this.datetime);
  }

  public ReviewsDBO() {
  }

  protected ReviewsDBO(Parcel in) {
    this.jobtitle = in.readString();
    this.image = in.readString();
    this.rating = in.readString();
    this.description = in.readString();
    this.name = in.readString();
    this.address = in.readString();
    this.response = in.readString();
    this.distnace = in.readString();
    this.datetime = in.readString();
  }

  public static final Parcelable.Creator<ReviewsDBO> CREATOR = new Parcelable.Creator<ReviewsDBO>() {
    @Override
    public ReviewsDBO createFromParcel(Parcel source) {
      return new ReviewsDBO(source);
    }

    @Override
    public ReviewsDBO[] newArray(int size) {
      return new ReviewsDBO[size];
    }
  };
}
