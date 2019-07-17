package com.pjamrozowicz;

import android.support.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

class ReactEvents {
    private ReactContext reactContext;

    ReactEvents(ReactContext reactContext) {
        this.reactContext = reactContext;
    }

    void connectionInitiatedToEndpoint(
            final String endpointId,
            final ConnectionInfo connectionInfo) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpointId);
        out.putString("endpointName", connectionInfo.getEndpointName());
        out.putString("authenticationToken", connectionInfo.getAuthenticationToken());
        out.putBoolean("incomingConnection", connectionInfo.isIncomingConnection());

        sendEvent("connection_initiated_to_endpoint", out);
    }

    void endpointConnectionFailed(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("endpoint_connection_failed", out);
    }

    void connectedToEndpoint(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("connected_to_endpoint", out);
    }

    void disconnectedFromEndpoint(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("disconnected_from_endpoint", out);
    }

    void onReceivePayload(Endpoint endpoint, String data) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());
        out.putString("data", data);

        sendEvent("receive_payload", out);
    }

    void onCorruptedMessage(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("receive_corrupted_message", out);
    }

    void onPayloadUpdate(Endpoint endpoint, PayloadTransferUpdate update) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putDouble("bytesTransferred", update.getBytesTransferred());
        out.putDouble("totalBytes", update.getTotalBytes());
        out.putInt("payloadStatus", update.getStatus());
        out.putInt("payloadHashCode", update.hashCode());

        sendEvent("payload_transfer_update", out);
    }

    void onEndpointDiscovered(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("endpoint_discovered", out);
    }

    void endpointLost(Endpoint endpoint) {
        WritableMap out = Arguments.createMap();
        out.putString("endpointId", endpoint.getId());
        out.putString("endpointName", endpoint.getName());

        sendEvent("endpoint_lost", out);
    }

    private void sendEvent(String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }
}
