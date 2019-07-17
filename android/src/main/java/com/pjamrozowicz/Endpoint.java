package com.pjamrozowicz;

import android.support.annotation.NonNull;

public class Endpoint {
    @NonNull
    private final String id;
    @NonNull
    private final String name;
    @NonNull
    private Boolean connected;

    Endpoint(@NonNull String id, @NonNull String name) {
        this.id = id;
        this.name = name;
        this.connected = false;
    }

//    public WritableMap toWritableMap() {
//        WritableMap out = Arguments.createMap();
//
//        out.putString("serviceId", serviceId);
//        out.putString("endpointId", id);
//        out.putString("endpointName", name);
//        out.putBoolean("connected", connected);
//        out.putString("type", type);
//
//        return out;
//    }

    public void setConnected(final Boolean newValue) {
        this.connected = newValue;
    }

    @NonNull
    public Boolean isConnected() {
        return connected;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof Endpoint) {
            Endpoint other = (Endpoint) obj;
            return id.equals(other.id);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return String.format("Endpoint{id=%s, name=%s}", id, name);
    }
}