package com.pjamrozowicz;

import android.bluetooth.BluetoothAdapter;
import android.util.Log;
import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;

public class BleManagerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;

    public BleManagerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    @Override
    public String getName() {
        return "BleManager";
    }

    @ReactMethod
    public void sampleMethod(String stringArgument, int numberArgument, Callback callback) {
        // TODO: Implement some real useful functionality
        callback.invoke("Received numberArgument: " + numberArgument + " stringArgument: " + stringArgument);
    }

    @ReactMethod
    public void show() {
        Log.wtf("aa", Boolean.valueOf(BluetoothAdapter.getDefaultAdapter() != null).toString());
//        if (!BluetoothAdapter.getDefaultAdapter().isMultipleAdvertisementSupported()) {
//            Toast.makeText(getReactApplicationContext(), "Multiple advertisement not supported", Toast.LENGTH_SHORT).show();
//        } else {
//            Toast.makeText(getReactApplicationContext(), "Multiple advertisement supported", Toast.LENGTH_SHORT).show();
//        }
    }
}
