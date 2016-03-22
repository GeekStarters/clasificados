package and.clasificados.com.layer;

import com.layer.sdk.LayerClient;
import com.layer.sdk.exceptions.LayerException;
import com.layer.sdk.listeners.LayerConnectionListener;

/**
 * Created by Gabriela Mejia on 21/3/2016.
 */
public class MyConnectionListener implements LayerConnectionListener {

    @Override
    // Called when the LayerClient establishes a network connection
    public void onConnectionConnected(LayerClient layerClient) {
        // Ask the LayerClient to authenticate. If no auth credentials are present,
        // an authentication challenge is issued
        layerClient.authenticate();
    }

    @Override
    public void onConnectionDisconnected(LayerClient arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onConnectionError(LayerClient arg0, LayerException e) {
        // TODO Auto-generated method stub
    }

}