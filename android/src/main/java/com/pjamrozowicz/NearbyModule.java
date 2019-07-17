package com.pjamrozowicz;


import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableArray;
import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionsClient;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.Strategy;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NearbyModule extends ReactContextBaseJavaModule {

    private final ReactEvents reactEvents;
    private final ConnectionsClient singletonClient;

    public NearbyModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactEvents = new ReactEvents(reactContext);
        this.singletonClient = Nearby.getConnectionsClient(reactContext);
    }

    @Override
    public String getName() {
        return "Nearby";
    }

    private final Map<String, Endpoint> mEndpoints = new HashMap<>();


    @Override
    public void onCatalystInstanceDestroy() {
        super.onCatalystInstanceDestroy();
        singletonClient.stopDiscovery();
        singletonClient.stopAdvertising();
        singletonClient.stopAllEndpoints();
    }

    @ReactMethod
    protected void startAdvertising(
            final String endpointName,
            final String serviceId,
            final int strategy,
            Promise promise
    ) {
        Strategy finalStrategy = Strategy.P2P_CLUSTER;
        switch (strategy) {
            case 0:
                finalStrategy = Strategy.P2P_CLUSTER;
                break;
            case 1:
                finalStrategy = Strategy.P2P_STAR;
                break;
        }

        final AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(finalStrategy).build();

        singletonClient
                .startAdvertising(
                        endpointName, serviceId,
                        Callbacks.getConnectionLifecycleCallback(mEndpoints, reactEvents),
                        advertisingOptions)
                .addOnSuccessListener(
                        (result) -> promise.resolve(null))
                .addOnFailureListener(
                        promise::reject);
    }

    @ReactMethod
    public void stopAdvertising() {
        singletonClient.stopAdvertising();
    }

    @ReactMethod
    public void acceptConnection(final String endpointId, Promise promise) {
        final Endpoint endpoint = mEndpoints.get(endpointId);
        if(endpoint != null) {
            singletonClient
                    .acceptConnection(endpointId, Callbacks.getPayloadCallback(mEndpoints, reactEvents))
                    .addOnFailureListener(promise::reject)
                    .addOnSuccessListener(promise::resolve);
        } else {
            promise.reject(new Exception("Endpoint doesn't exist"));
        }
    }

    @ReactMethod
    public void rejectConnection(final String endpointId, Promise promise) {
        final Endpoint endpoint = mEndpoints.get(endpointId);
        if(endpoint != null) {
            singletonClient
                    .rejectConnection(endpointId)
                    .addOnFailureListener(promise::reject)
                    .addOnSuccessListener((result) -> promise.resolve(null));
        } else {
            promise.reject(new Exception("Endpoint doesn't exist"));
        }
    }

    @ReactMethod
    public void startDiscovering(final String serviceId, final int strategy, Promise promise) {
        Strategy finalStrategy = Strategy.P2P_CLUSTER;
        switch(strategy) {
            case 0: finalStrategy = Strategy.P2P_CLUSTER;
                break;
            case 1: finalStrategy = Strategy.P2P_STAR;
                break;
        }

        final DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder()
                .setStrategy(finalStrategy)
                .build();

        singletonClient
                .startDiscovery(
                        serviceId,
                        Callbacks.getEndpointDiscoveryCallback(mEndpoints, reactEvents),
                        discoveryOptions)
                .addOnSuccessListener((result) -> promise.resolve(null))
                .addOnFailureListener(
                        promise::reject
                );
    }

    @ReactMethod
    public void stopDiscovering() {
        singletonClient.stopDiscovery();
    }

    @ReactMethod
    public void connectToEndpoint(final String endpointId, final String presentedName, Promise promise) {
        final Endpoint endpoint = mEndpoints.get(endpointId);
        if(endpoint != null) {
            // Ask to connect
            singletonClient
                    .requestConnection(presentedName, endpointId, Callbacks.getConnectionLifecycleCallback(mEndpoints, reactEvents))
                    .addOnSuccessListener((result) -> promise.resolve(null))
                    .addOnFailureListener(promise::reject);
        } else {
            promise.reject(new Exception("Given endpoint doesn't exist"));
        }
    }

    @ReactMethod
    public void disconnectFromEndpoint(final String endpointId, Promise promise) {
        final Endpoint endpoint = mEndpoints.get(endpointId);
        if(endpoint != null) {
            singletonClient.disconnectFromEndpoint(endpointId);
            endpoint.setConnected(false);
        } else {
            promise.resolve(null);
        }
    }

    @ReactMethod
    public void sendPayload(final ReadableArray endpointIds, final String bytes, Promise promise) {
        LinkedList<String> ids = new LinkedList<>();
        for(int i = 0;i<endpointIds.size();i++) {
            String endpointId = endpointIds.getString(i);
            final Endpoint endpoint = mEndpoints.get(endpointId);
            if(endpoint != null && endpoint.isConnected()) {
                ids.push(endpointId);
            } else {
                promise.reject(new Exception("Endpoint not recognized or not connected"));
                return;
            }
        }

        singletonClient
                .sendPayload(ids, Payload.fromBytes(bytes.getBytes()))
                .addOnFailureListener(promise::reject)
                .addOnSuccessListener((result) -> promise.resolve(null));
    }
}
