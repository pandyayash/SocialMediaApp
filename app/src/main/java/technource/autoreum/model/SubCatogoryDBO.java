package technource.autoreum.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by technource on 1/11/17.
 */

public class SubCatogoryDBO implements Parcelable {
    String id;
    String name;
    String image;
    int position;
    String emergency;
    String insurance;
    String parent_id;
    String placeholder;
    int sub_cat_data;

    public int getSub_cat_data() {
        return sub_cat_data;
    }

    public void setSub_cat_data(int sub_cat_data) {
        this.sub_cat_data = sub_cat_data;
    }

    private boolean isSelected = false;
    private boolean isSpecialCase = false;

    String add_location_id;

    public String getAdd_location_id() {
        return add_location_id;
    }

    public void setAdd_location_id(String add_location_id) {
        this.add_location_id = add_location_id;
    }

    public String getAdd_loaction() {
        return add_loaction;
    }

    public void setAdd_loaction(String add_loaction) {
        this.add_loaction = add_loaction;
    }

    String add_loaction;

    public String getId() {
        return id;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public String getItext() {
        return itext;
    }

    public boolean isSpecialCase() {
        return isSpecialCase;
    }

    public void setSpecialCase(boolean specialCase) {
        isSpecialCase = specialCase;
    }

    public void setItext(String itext) {
        this.itext = itext;
    }

    public ArrayList<SubCatogoryDBO> getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(ArrayList<SubCatogoryDBO> subcategory) {
        this.subcategory = subcategory;
    }

    String help;
    String itext;
    ArrayList<SubCatogoryDBO>subcategory;

    public SubCatogoryDBO() {
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
        dest.writeString(this.placeholder);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSpecialCase ? (byte) 1 : (byte) 0);
        dest.writeString(this.add_location_id);
        dest.writeString(this.add_loaction);
        dest.writeString(this.help);
        dest.writeString(this.itext);
        dest.writeTypedList(this.subcategory);
    }

    protected SubCatogoryDBO(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.image = in.readString();
        this.position = in.readInt();
        this.emergency = in.readString();
        this.insurance = in.readString();
        this.parent_id = in.readString();
        this.placeholder = in.readString();
        this.isSelected = in.readByte() != 0;
        this.isSpecialCase = in.readByte() != 0;
        this.add_location_id = in.readString();
        this.add_loaction = in.readString();
        this.help = in.readString();
        this.itext = in.readString();
        this.subcategory = in.createTypedArrayList(SubCatogoryDBO.CREATOR);
    }

    public static final Creator<SubCatogoryDBO> CREATOR = new Creator<SubCatogoryDBO>() {
        @Override
        public SubCatogoryDBO createFromParcel(Parcel source) {
            return new SubCatogoryDBO(source);
        }

        @Override
        public SubCatogoryDBO[] newArray(int size) {
            return new SubCatogoryDBO[size];
        }
    };
}
