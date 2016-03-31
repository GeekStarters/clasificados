package and.clasificados.com.layer;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessagePart;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.List;

import and.clasificados.com.R;
import and.clasificados.com.actividades.Mensajes;
import and.clasificados.com.auxiliares.PrefUtils;
import and.clasificados.com.modelo.Usuario;

/**
 * Takes a Layer Message object, formats the text and attaches it to a LinearLayout
 */
public class MessageView {

    //The parent object (in this case, a LinearLayout object with a ScrollView parent)
    private LinearLayout myParent;

    //The sender and message views
    private TextView senderTV;
    private TextView messageTV;
    private Usuario login_user=null;
    private ImageView statusImage;
    private Context context;
    private LinearLayout messageDetails;
    private boolean iDid=true;

    //Takes the Layout parent object and message
    public MessageView(LinearLayout parent, Message msg) {
        myParent = parent;
        context=parent.getContext();
        login_user= PrefUtils.getCurrentUser(context);
        //The first part of each message will include the sender and status
        messageDetails = new LinearLayout(parent.getContext());
        messageDetails.setOrientation(LinearLayout.HORIZONTAL);
        myParent.addView(messageDetails);

        //Creates the sender text view, sets the text to be italic, and attaches it to the parent
        senderTV = new TextView(parent.getContext());
        senderTV.setTypeface(null, Typeface.ITALIC);
        senderTV.setGravity(Gravity.CENTER_HORIZONTAL);
        messageDetails.addView(senderTV);

        //Creates the message text view and attaches it to the parent
        messageTV = new TextView(parent.getContext());
        messageTV.setTextColor(Color.parseColor("#FFFFFF"));
        messageTV.setBackground(new ColorDrawable(Color.parseColor("#00ba68")));
        myParent.addView(messageTV);

        //The status is displayed with an icon, depending on whether the message has been read,
        // delivered, or sent
        //statusImage = new ImageView(parent.getContext());
        statusImage = createStatusImage(msg);//statusImage.setImageResource(R.drawable.sent);
        messageDetails.addView(statusImage);

        //Populates the text views
        UpdateMessage(msg);
    }

    //Takes a message and sets the text in the two text views
    public void UpdateMessage(Message msg) {
        String senderTxt = craftSenderText(msg);
        String msgTxt = craftMsgText(msg);
        String aux=senderTV.getText().toString();
        senderTV.setGravity(Gravity.CENTER_HORIZONTAL);
        if(!aux.equals(senderTxt)){
            senderTV.setText(senderTxt);
        }else{
            senderTV.setText("");
        }
        messageTV.setText(msgTxt);
    }

    //The sender text is formatted like so:
    //  User @ Timestamp - Status
    private String craftSenderText(Message msg) {

        if (msg == null)
            return "";

        //The User ID
        String senderTxt = "";
        if (msg.getSender() != null && msg.getSender().getUserId() != null)
            senderTxt = "";//msg.getSender().getUserId();
        if(!msg.getSender().getUserId().equals(login_user.id)){
            messageTV.setTextColor(Color.parseColor("#000000"));
            messageTV.setBackground(new ColorDrawable(Color.parseColor("#FFFFFF")));
        }

        //Add the timestamp
        if (msg.getSentAt() != null) {
            String aux_hora=new SimpleDateFormat("hh:mm a").format(msg.getReceivedAt());
            String aux_dia=new SimpleDateFormat("EEEE").format(msg.getReceivedAt());
            String dia = aux_dia+" a las "+aux_hora;
            String mayuscula=dia.charAt(0)+"";
            mayuscula=mayuscula.toUpperCase();
            dia=dia.replaceFirst(dia.charAt(0)+"", mayuscula);
            senderTxt+=dia;
            //senderTxt += aux_dia+" a las "+aux_hora;
        }

        //Add some formatting before the status icon
        senderTxt += "  ";

        //Return the formatted text
        return senderTxt;
    }


    //Checks the recipient status of the message (based on all participants)
    private Message.RecipientStatus getMessageStatus(Message msg) {

        if (msg == null || msg.getSender() == null || msg.getSender().getUserId() == null)
            return Message.RecipientStatus.PENDING;

        //If we didn't send the message, we already know the status - we have read it
        if (!msg.getSender().getUserId().equalsIgnoreCase(Mensajes.getUserID())){
            iDid=false;
            return Message.RecipientStatus.READ;
        }


        //Assume the message has been sent
        Message.RecipientStatus status = Message.RecipientStatus.SENT;

        //Go through each user to check the status, in this case we check each user and
        // prioritize so
        // that we return the highest status: Sent -> Delivered -> Read
        for (int i = 0; i < Mensajes.getAllParticipants().size(); i++) {

            //Don't check the status of the current user
            String participant = Mensajes.getAllParticipants().get(i);
            if (participant.equalsIgnoreCase(Mensajes.getUserID()))
                continue;

            if (status == Message.RecipientStatus.SENT) {

                if (msg.getRecipientStatus(participant) == Message.RecipientStatus.DELIVERED)
                    status = Message.RecipientStatus.DELIVERED;

                if (msg.getRecipientStatus(participant) == Message.RecipientStatus.READ)
                    return Message.RecipientStatus.READ;

            } else if (status == Message.RecipientStatus.DELIVERED) {
                if (msg.getRecipientStatus(participant) == Message.RecipientStatus.READ)
                    return Message.RecipientStatus.READ;
            }
        }

        return status;
    }

    //Checks the message parts and parses the message contents
    private String craftMsgText(Message msg) {

        //The message text
        String msgText = "";

        //Go through each part, and if it is text (which it should be by default), append it to the
        // message text
        List<MessagePart> parts = msg.getMessageParts();
        for (int i = 0; i < msg.getMessageParts().size(); i++) {

            //You can always set the mime type when creating a message part, by default the mime
            // type is initialized to plain text when the message part is created
            if (parts.get(i).getMimeType().equalsIgnoreCase("text/plain")) {
                try {
                    msgText += new String(parts.get(i).getData(), "UTF-8") + "\n";
                } catch (UnsupportedEncodingException e) {

                }
            }
        }

        //Return the assembled text
        return msgText;
    }

    //Sets the status image based on whether other users in the conversation have received or read
    //the message
    private ImageView createStatusImage(Message msg) {
        ImageView status = new ImageView(myParent.getContext());

        switch (getMessageStatus(msg)) {

            case SENT:
                status.setImageResource(R.drawable.sent);
                break;

            case DELIVERED:
                status.setImageResource(R.drawable.delivered);
                break;

            case READ:
                if(iDid){
                    status.setImageResource(R.drawable.read);

                }else{
                   // status.setImageResource(R.drawable.read);
                }
                break;
        }

        //Have the icon fill the space vertically
        status.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams
                .WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT));

        return status;
    }
}
