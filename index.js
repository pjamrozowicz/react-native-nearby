import { NativeModules, DeviceEventEmitter } from 'react-native';

const { Nearby } = NativeModules;

export const Strategy = {
  "P2P_CLUSTER": 0,
  "P2P_STAR": 1,
};

class NearbyConnection {

  static sendPayload(endpointIds, bytes) {
    return Nearby.sendPayload(endpointIds, String(bytes));
  }

  // Start/Stop Advertise
  static startAdvertising(localEndpointName, serviceId, strategy = Strategy.P2P_CLUSTER) {
    return Nearby.startAdvertising(localEndpointName, serviceId, strategy);
  }
  static stopAdvertising() {
    return Nearby.stopAdvertising();
  }

  // Start/Stop Discover
  static startDiscovering(serviceId, strategy = Strategy.P2P_CLUSTER) {
    return Nearby.startDiscovering(serviceId, strategy);
  }
  static stopDiscovering() {
    return Nearby.stopDiscovering();
  }

  // Accept or Reject
  static acceptConnection(endpointId) {
    return Nearby.acceptConnection(endpointId);
  }
  static rejectConnection(endpointId) {
    return Nearby.rejectConnection(endpointId);
  }

  // Connect or Disconnect
  static connectToEndpoint(endpointId, presentedName) {
    return Nearby.connectToEndpoint(endpointId, presentedName);
  }
  static disconnectFromEndpoint(endpointId) {
    return Nearby.disconnectFromEndpoint(endpointId);
  }

  // ------------------------------------------------------------------------
  // Callbacks
  // ------------------------------------------------------------------------
  // Connection
  static onConnectionInitiatedToEndpoint(listener) {
    return DeviceEventEmitter.addListener('connection_initiated_to_endpoint', listener);
  }
  static onConnectedToEndpoint(listener) {
    return DeviceEventEmitter.addListener('connected_to_endpoint', listener);
  }
  static onEndpointConnectionFailed(listener) {
    return DeviceEventEmitter.addListener('endpoint_connection_failed', listener);
  }
  static onDisconnectedFromEndpoint(listener) {
    return DeviceEventEmitter.addListener('disconnected_from_endpoint', listener);
  }

  // Discovery
  static onEndpointDiscovered(listener) {
    return DeviceEventEmitter.addListener('endpoint_discovered', listener);
  }
  static onEndpointLost(listener) {
    return DeviceEventEmitter.addListener('endpoint_lost', listener);
  }

  // Payload
  static onReceivePayload(listener) {
    return DeviceEventEmitter.addListener('receive_payload', listener);
  }
  static onPayloadTransferUpdate(listener) {
    return DeviceEventEmitter.addListener('payload_transfer_update', listener);
  }
  static onReceiveCorruptedMessage(listener) {
    return DeviceEventEmitter.addListener('receive_corrupted_message', listener);
  }
}

export default NearbyConnection;
