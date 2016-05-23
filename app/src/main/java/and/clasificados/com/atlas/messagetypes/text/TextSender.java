package and.clasificados.com.atlas.messagetypes.text;


import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessageOptions;
import com.layer.sdk.messaging.MessagePart;

import and.clasificados.com.R;
import and.clasificados.com.atlas.messagetypes.MessageSender;
import and.clasificados.com.atlas.util.Log;

public class TextSender extends MessageSender {
    private final int mMaxNotificationLength;

    public TextSender() {
        this(200);
    }

    public TextSender(int maxNotificationLength) {
        mMaxNotificationLength = maxNotificationLength;
    }

    public boolean requestSend(String text) {
        if (text == null || text.trim().length() == 0) {
            if (Log.isLoggable(Log.ERROR)) Log.e("No text to send");
            return false;
        }
        if (Log.isLoggable(Log.VERBOSE)) Log.v("Sending text message");

        // Create notification string
        String myName = getParticipantProvider().getParticipant(getLayerClient().getAuthenticatedUserId()).getName();
        String notificationString = getContext().getString(R.string.atlas_notification_text, myName, (text.length() < mMaxNotificationLength) ? text : (text.substring(0, mMaxNotificationLength) + "…"));

        // Send message
        MessagePart part = getLayerClient().newMessagePart(text);
        Message message = getLayerClient().newMessage(new MessageOptions().pushNotificationMessage(notificationString), part);
        return send(message);
    }
}