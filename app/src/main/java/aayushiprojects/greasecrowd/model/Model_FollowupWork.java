package aayushiprojects.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by technource on 20/2/18.
 */

public class Model_FollowupWork implements Parcelable {
    public boolean isRadioAccept() {
        return radioAccept;
    }

    int isFollowUpAcceptedorNot=0;

    public int getIsFollowUpAcceptedorNot() {
        return isFollowUpAcceptedorNot;
    }

    public void setIsFollowUpAcceptedorNot(int isFollowUpAcceptedorNot) {
        this.isFollowUpAcceptedorNot = isFollowUpAcceptedorNot;
    }

    public void setRadioAccept(boolean radioAccept) {
        this.radioAccept = radioAccept;
    }

    String key, value;
    boolean radioAccept;

    public int getIsAccepted() {
        return isAccepted;
    }

    public void setIsAccepted(int isAccepted) {
        this.isAccepted = isAccepted;
    }

    int isAccepted = 3; // 0-> seen but not accepted , 1--> seen and accepted, 2--> not seen, 3 -->default

    public Model_FollowupWork() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.key);
        dest.writeString(this.value);
        dest.writeByte(this.radioAccept ? (byte) 1 : (byte) 0);
        dest.writeInt(this.isAccepted);
        dest.writeInt(this.isFollowUpAcceptedorNot);
    }

    protected Model_FollowupWork(Parcel in) {
        this.key = in.readString();
        this.value = in.readString();
        this.radioAccept = in.readByte() != 0;
        this.isAccepted = in.readInt();
        this.isFollowUpAcceptedorNot = in.readInt();
    }

    public static final Creator<Model_FollowupWork> CREATOR = new Creator<Model_FollowupWork>() {
        @Override
        public Model_FollowupWork createFromParcel(Parcel source) {
            return new Model_FollowupWork(source);
        }

        @Override
        public Model_FollowupWork[] newArray(int size) {
            return new Model_FollowupWork[size];
        }
    };
}
