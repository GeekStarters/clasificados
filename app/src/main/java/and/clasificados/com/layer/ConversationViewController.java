package and.clasificados.com.layer;

import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.layer.sdk.LayerClient;
import com.layer.sdk.changes.LayerChange;
import com.layer.sdk.changes.LayerChangeEvent;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerChangeEventListener;
import com.layer.sdk.listeners.LayerSyncListener;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.LayerObject;
import com.layer.sdk.messaging.Message;
import com.layer.sdk.messaging.MessageOptions;
import com.layer.sdk.messaging.MessagePart;
import com.layer.sdk.query.Predicate;
import com.layer.sdk.query.Query;
import com.layer.sdk.query.SortDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.List;

import and.clasificados.com.R;
import and.clasificados.com.actividades.Mensajes;

/**
 * Handles the conversation between the pre-defined participants (Device, Emulator) and displays
 * messages in the GUI.
 */
public class ConversationViewController implements View.OnClickListener, LayerChangeEventListener,
        TextWatcher, LayerSyncListener {

    private static final String TAG = ConversationViewController.class.getSimpleName();

    private LayerClient layerClient;

    //GUI elements
    private RelativeLayout sendButton;
    private EditText userInput;
    private ScrollView conversationScroll;
    private LinearLayout conversationView;

    //List of all users currently typing
    private ArrayList<String> typingUsers;

    //Current conversation
    private Conversation activeConversation;

    //All messages
    private Hashtable<String, MessageView> allMessages;

    public ConversationViewController(Mensajes ma, LayerClient client) {

        //Cache off LayerClient
        layerClient = client;

        //When conversations/messages change, capture them
        layerClient.registerEventListener(this);

        //List of users that are typing which is used with LayerTypingIndicatorListener
        typingUsers = new ArrayList<>();

        //Change the layout
        ma.setContentView(R.layout.activity_mimensaje);
        agregarToolbar(ma);
        //Cache off gui objects
        sendButton = (RelativeLayout) ma.findViewById(R.id.send);
        userInput = (EditText) ma.findViewById(R.id.input);
        conversationScroll = (ScrollView) ma.findViewById(R.id.scrollView2);
        conversationView = (LinearLayout) ma.findViewById(R.id.conversation);
        //Capture user input
        sendButton.setOnClickListener(this);
        userInput.setText(getInitialMessage());
        userInput.addTextChangedListener(this);

        //If there is an active conversation between the Device, Simulator, and Dashboard (web
        // client), cache it
        activeConversation = getConversation();

        //If there is an active conversation, draw it
        drawConversation();

    }

    private void agregarToolbar(Mensajes ma) {
        Toolbar toolbar = (Toolbar) ma.findViewById(R.id.toolbar);
        ma.setSupportActionBar(toolbar);
        ma.getSupportActionBar().setElevation(0);
        ma.getSupportActionBar().setHomeAsUpIndicator(R.drawable.back_custom);
        final ActionBar ab = ma.getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static String getInitialMessage() {
        return "Hey, everyone! This is your friend, " + Mensajes.getUserID();
    }

    //Create a new message and send it
    private void sendButtonClicked() {

        //Check to see if there is an active conversation between the pre-defined participants
        if (activeConversation == null) {
            activeConversation = getConversation();

            //If there isn't, create a new conversation with those participants
            if (activeConversation == null) {
                activeConversation = layerClient.newConversation(Mensajes.getAllParticipants());
            }
        }

        sendMessage(userInput.getText().toString());

        //Clears the text input field
        userInput.setText("");
    }

    private void sendMessage(String text) {

        //Put the user's text into a message part, which has a MIME type of "text/plain" by default
        MessagePart messagePart = layerClient.newMessagePart(text);

        //Formats the push notification that the other participants will receive
        MessageOptions options = new MessageOptions();
        options.pushNotificationMessage(Mensajes.getUserID() + ": " + text);

        //Creates and returns a new message object with the given conversation and array of
        // message parts
        Message message = layerClient.newMessage(options, Arrays.asList(messagePart));

        //Sends the message
        if (activeConversation != null)
            activeConversation.send(message);
    }

    //Checks to see if there is already a conversation between the device and emulator
    private Conversation getConversation() {

        if (activeConversation == null) {

            Query query = Query.builder(Conversation.class)
                    .predicate(new Predicate(Conversation.Property.PARTICIPANTS, Predicate
                            .Operator.EQUAL_TO, Mensajes.getAllParticipants()))
                    .sortDescriptor(new SortDescriptor(Conversation.Property.CREATED_AT,
                            SortDescriptor.Order.DESCENDING)).build();

            List<Conversation> results = layerClient.executeQuery(query, Query.ResultType.OBJECTS);
            if (results != null && results.size() > 0) {
                return results.get(0);
            }
        }

        //Returns the active conversation (which is null by default)
        return activeConversation;
    }

    //Redraws the conversation window in the GUI
    private void drawConversation() {

        //Only proceed if there is a valid conversation
        if (activeConversation != null) {

            //Clear the GUI first and empty the list of stored messages
            conversationView.removeAllViews();
            allMessages = new Hashtable<String, MessageView>();

            //Grab all the messages from the conversation and add them to the GUI
            List<Message> allMsgs = layerClient.getMessages(activeConversation);
            for (int i = 0; i < allMsgs.size(); i++) {
                addMessageToView(allMsgs.get(i));
            }

            //After redrawing, force the scroll view to the bottom (most recent message)
            conversationScroll.post(new Runnable() {
                @Override
                public void run() {
                    conversationScroll.fullScroll(View.FOCUS_DOWN);
                }
            });
        }
    }

    //Creates a GUI element (header and body) for each Message
    private void addMessageToView(Message msg) {

        //Make sure the message is valid
        if (msg == null || msg.getSender() == null || msg.getSender().getUserId() == null)
            return;

        //Once the message has been displayed, we mark it as read
        //NOTE: the sender of a message CANNOT mark their own message as read
        if (!msg.getSender().getUserId().equalsIgnoreCase(layerClient.getAuthenticatedUserId()))
            msg.markAsRead();

        //Grab the message id
        String msgId = msg.getId().toString();

        //If we have already added this message to the GUI, skip it
        if (!allMessages.contains(msgId)) {
            //Build the GUI element and save it
            MessageView msgView = new MessageView(conversationView, msg);
            allMessages.put(msgId, msgView);
        }
    }

    //================================================================================
    // View.OnClickListener methods
    //================================================================================

    public void onClick(View v) {
        //When the "send" button is clicked, grab the ongoing conversation (or create it) and
        // send the message
        if (v == sendButton) {
            sendButtonClicked();
        }

    }

    //================================================================================
    // LayerChangeEventListener methods
    //================================================================================

    @Override
    public void onChangeEvent(LayerChangeEvent event) {

        //You can choose to handle changes to conversations or messages however you'd like:
        List<LayerChange> changes = event.getChanges();
        for (int i = 0; i < changes.size(); i++) {
            LayerChange change = changes.get(i);
            if (change.getObjectType() == LayerObject.Type.CONVERSATION) {

                Conversation conversation = (Conversation) change.getObject();
                Log.v(TAG, "Conversation " + conversation.getId() + " attribute " +
                        change.getAttributeName() + " was changed from " + change.getOldValue() +
                        " to " + change.getNewValue());

                switch (change.getChangeType()) {
                    case INSERT:
                        break;

                    case UPDATE:
                        break;

                    case DELETE:
                        break;
                }

            } else if (change.getObjectType() == LayerObject.Type.MESSAGE) {

                Message message = (Message) change.getObject();
                Log.v(TAG, "Message " + message.getId() + " attribute " + change
                        .getAttributeName() + " was changed from " + change.getOldValue() + " to " +
                        "" + change.getNewValue());

                switch (change.getChangeType()) {
                    case INSERT:
                        break;

                    case UPDATE:
                        break;

                    case DELETE:
                        break;
                }
            }
        }

        //If we don't have an active conversation, grab the oldest one
        if (activeConversation == null)
            activeConversation = getConversation();

        //If anything in the conversation changes, re-draw it in the GUI
        drawConversation();
    }

    //================================================================================
    // TextWatcher methods
    //================================================================================

    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void afterTextChanged(Editable s) {

    }


    //Called before syncing with the Layer servers
    public void onBeforeSync(LayerClient layerClient, SyncType syncType) {
        Log.v(TAG, "Sync starting");
    }

    //Called during a sync, you can drive a spinner or progress bar using pctComplete, which is a
    // range between 0 and 100
    public void onSyncProgress(LayerClient layerClient, SyncType syncType, int pctComplete) {
        Log.v(TAG, "Sync is "  + pctComplete + "% Complete");
    }

    //Called after syncing with the Layer servers
    public void onAfterSync(LayerClient layerClient, SyncType syncType) {
        Log.v(TAG, "Sync complete");
    }

    //Captures any errors with syncing
    public void onSyncError(LayerClient layerClient, List<LayerException> layerExceptions) {
        for (LayerException e : layerExceptions) {
            Log.v(TAG, "onSyncError: " + e.toString());
        }
    }

}
