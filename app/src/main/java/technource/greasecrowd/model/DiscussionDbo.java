package technource.greasecrowd.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 1/2/18.
 */

public class DiscussionDbo implements Parcelable {
    String id;
    String messageText;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getAvatarImage() {
        return avatarImage;
    }

    public void setAvatarImage(String avatarImage) {
        this.avatarImage = avatarImage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    String objectId;
    String objectType;
    String jobId;
    String avatarImage;
    String name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.messageText);
        dest.writeString(this.objectId);
        dest.writeString(this.objectType);
        dest.writeString(this.jobId);
        dest.writeString(this.avatarImage);
        dest.writeString(this.name);
    }

    public DiscussionDbo() {
    }

    protected DiscussionDbo(Parcel in) {
        this.id = in.readString();
        this.messageText = in.readString();
        this.objectId = in.readString();
        this.objectType = in.readString();
        this.jobId = in.readString();
        this.avatarImage = in.readString();
        this.name = in.readString();
    }

    public static final Creator<DiscussionDbo> CREATOR = new Creator<DiscussionDbo>() {
        @Override
        public DiscussionDbo createFromParcel(Parcel source) {
            return new DiscussionDbo(source);
        }

        @Override
        public DiscussionDbo[] newArray(int size) {
            return new DiscussionDbo[size];
        }
    };
}
