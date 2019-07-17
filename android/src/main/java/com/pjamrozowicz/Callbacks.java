package com.pjamrozowicz;

import android.support.annotation.NonNull;

import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;

import java.io.UnsupportedEncodingException;
import java.util.Map;

class Callbacks {
    static PayloadCallback getPayloadCallback(
            final Map<String, Endpoint> mEndpoints,
            final ReactEvents reactEvents
    ) {
        return new PayloadCallback() {
            @Override
            public void onPayloadReceived(@NonNull String endpointId, @NonNull Payload payload) {
                Endpoint endpoint = mEndpoints.get(endpointId);
                if (payload.getType() == Payload.Type.BYTES && endpoint != null) {
                    String data;
                    try {
                        data = new String(payload.asBytes(), "UTF-8");
                        reactEvents.onReceivePayload(endpoint, data);
                    } catch (UnsupportedEncodingException e) {
                        reactEvents.onCorruptedMessage(endpoint);
                    }
                }

            }

            @Override
            public void onPayloadTransferUpdate(
                    @NonNull String endpointId,
                    @NonNull PayloadTransferUpdate update
            ) {
                Endpoint endpoint = mEndpoints.get(endpointId);
                if (endpoint != null) {
                    reactEvents.onPayloadUpdate(endpoint, update);
                }
            }
        };
    }

    static ConnectionLifecycleCallback getConnectionLifecycleCallback(
            final Map<String, Endpoint> mEndpoints,
            final ReactEvents reactEvents
    ) {
        return new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                Endpoint endpoint = new Endpoint(endpointId, connectionInfo.getEndpointName());
                mEndpoints.put(endpointId, endpoint);
                reactEvents.connectionInitiatedToEndpoint(endpointId, connectionInfo);
            }

            @Override
            public void onConnectionResult(@NonNull String endpointId, @NonNull ConnectionResolution result) {
                // Both sides either accepted or rejected connection
                Endpoint endpoint = mEndpoints.get(endpointId);
                if (endpoint != null) {
                    if (result.getStatus().isSuccess()) {
                        endpoint.setConnected(true);
                        reactEvents.connectedToEndpoint(endpoint);
                    } else {
                        endpoint.setConnected(false);
                        reactEvents.endpointConnectionFailed(endpoint);
                    }
                }
            }

            @Override
            public void onDisconnected(@NonNull String endpointId) {
                Endpoint endpoint = mEndpoints.get(endpointId);
                if (endpoint != null && endpoint.isConnected()) {
                    endpoint.setConnected(false);
                    reactEvents.disconnectedFromEndpoint(endpoint);
                }
            }
        };
    }

    static EndpointDiscoveryCallback getEndpointDiscoveryCallback(
            final Map<String, Endpoint> mEndpoints,
            final ReactEvents reactEvents
    ) {
        return new EndpointDiscoveryCallback() {
            @Override
            public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo info) {
                Endpoint existingEndpoint = mEndpoints.get(endpointId);
                if(existingEndpoint == null) {
                    Endpoint endpoint = new Endpoint(endpointId, info.getEndpointName());
                    mEndpoints.put(endpointId, endpoint);
                    reactEvents.onEndpointDiscovered(endpoint);
                }
            }

            @Override
            public void onEndpointLost(@NonNull String endpointId) {
                Endpoint existingEndpoint = mEndpoints.get(endpointId);
                if(existingEndpoint != null) {
                    mEndpoints.remove(endpointId);
                    reactEvents.endpointLost(existingEndpoint);
                }
            }
        };
    }
}
