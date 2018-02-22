package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 18/7/17.
 */

public class SignUpDBO implements Parcelable {

  public static final Creator<SignUpDBO> CREATOR = new Creator<SignUpDBO>() {
    @Override
    public SignUpDBO createFromParcel(Parcel in) {
      return new SignUpDBO(in);
    }

    @Override
    public SignUpDBO[] newArray(int size) {
      return new SignUpDBO[size];
    }
  };
  String Fb_id, name, firstname, lastname, email, image, type,socialid, signupType;


  public SignUpDBO() {

  }

  public String getSocialid() {
    return socialid;
  }

  public void setSocialid(String socialid) {
    this.socialid = socialid;
  }

  public SignUpDBO(Parcel in) {
    Fb_id = in.readString();
    name = in.readString();
    firstname = in.readString();
    lastname = in.readString();
    email = in.readString();
    image = in.readString();
    type = in.readString();
    socialid = in.readString();
    signupType = in.readString();
  }

  public String getSignupType() {
    return signupType;
  }

  public void setSignupType(String signupType) {
    this.signupType = signupType;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(Fb_id);
    dest.writeString(name);
    dest.writeString(firstname);
    dest.writeString(lastname);
    dest.writeString(email);
    dest.writeString(image);

    dest.writeString(type);
    dest.writeString(socialid);
    dest.writeString(signupType);
  }

  @Override
  public int describeContents() {
    return 0;
  }

  public String getFb_id() {
    return Fb_id;
  }

  public void setFb_id(String fb_id) {
    Fb_id = fb_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getImage() {
    return image;
  }

  public void setImage(String image) {
    this.image = image;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

}
