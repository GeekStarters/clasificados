package and.clasificados.com.layer.atlas;

import android.net.Uri;

import com.layer.atlas.provider.Participant;

public class MyParticipant implements Participant {
    private String mId;
    private String mName;
    private Uri mAvatarUrl;

    @Override
    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    @Override
    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public Uri getAvatarUrl() {
        return mAvatarUrl;
    }

    public void setAvatarUrl(Uri avatarUrl) {
        mAvatarUrl = avatarUrl;
    }

    @Override
    public int compareTo(com.layer.atlas.provider.Participant another) {
        return getName().toLowerCase().compareTo(another.getName().toUpperCase());
    }
}
