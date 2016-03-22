package and.clasificados.com.layer;

import android.app.Application;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerConversationException;
import com.layer.sdk.messaging.Conversation;
import com.layer.sdk.messaging.ConversationOptions;

import java.util.Arrays;

import and.clasificados.com.R;

public class MiMensaje extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Conversation conversation;
        // Lets the LayerClient track Application state for connection and notification management.
        LayerClient.applicationCreated(this);
        // Create a LayerClient ready to receive push notifications through GCM
        LayerClient layerClient = LayerClient.newInstance(this, "layer:///apps/staging/613d665c-d4c9-11e5-bfa3-44c9010027fe",
                new LayerClient.Options().googleCloudMessagingSenderId("GCM Project Number"));
        MyConnectionListener connectionListener = new MyConnectionListener();
        MyAuthListener authenticationListener = new MyAuthListener();

        //Note: It is possible to register more than one listener for an activity. If you
        // execute this code more than once in your app, pass in the same listener to avoid
        // memory leaks and multiple callbacks.
        layerClient.registerConnectionListener(connectionListener);
        layerClient.registerAuthenticationListener(authenticationListener);

        // Asks the LayerSDK to establish a network connection with the Layer service
        layerClient.connect();
        try {
            // Try creating a new distinct conversation with the given user
            conversation = layerClient.newConversation(Arrays.asList("USER-IDENTIFIER"));
        } catch (LayerConversationException e) {
            // If a distinct conversation with the given user already exists, use that one instead
            conversation = e.getConversation();
        }
        ConversationOptions options = new ConversationOptions().distinct(false);
        Conversation topicA = layerClient.newConversation(options, Arrays.asList("USER-IDENTIFIER"));
        Conversation topicB = layerClient.newConversation(options, Arrays.asList("USER-IDENTIFIER"));
    }





}
