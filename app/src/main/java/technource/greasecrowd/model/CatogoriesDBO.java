package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 20/9/17.
 */

public class CatogoriesDBO implements Parcelable {

    public String id;
    String name;
    String image;
    int position;
    String emergency;
    String insurance;
    String parent_id;

    private boolean isSelected = false;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getEmergency() {
        return emergency;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public String getInsurance() {
        return insurance;
    }

    public void setInsurance(String insurance) {
        this.insurance = insurance;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getItext() {
        return itext;
    }

    public void setItext(String itext) {
        this.itext = itext;
    }

    String placeholder;
    String help;
    String itext;

    public CatogoriesDBO() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.image);
        dest.writeInt(this.position);
        dest.writeString(this.emergency);
        dest.writeString(this.insurance);
        dest.writeString(this.parent_id);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeString(this.placeholder);
        dest.writeString(this.help);
        dest.writeString(this.itext);
    }

    protected CatogoriesDBO(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.position = in.readInt();
        this.emergency = in.readString();
        this.insurance = in.readString();
        this.parent_id = in.readString();
        this.isSelected = in.readByte() != 0;
        this.placeholder = in.readString();
        this.help = in.readString();
        this.itext = in.readString();
    }

    public static final Creator<CatogoriesDBO> CREATOR = new Creator<CatogoriesDBO>() {
        @Override
        public CatogoriesDBO createFromParcel(Parcel source) {
            return new CatogoriesDBO(source);
        }

        @Override
        public CatogoriesDBO[] newArray(int size) {
            return new CatogoriesDBO[size];
        }
    };
}
