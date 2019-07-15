# react-native-ble-manager

## Getting started

`$ npm install react-native-ble-manager --save`

### Mostly automatic installation

`$ react-native link react-native-ble-manager`

### Manual installation


#### iOS

1. In XCode, in the project navigator, right click `Libraries` ➜ `Add Files to [your project's name]`
2. Go to `node_modules` ➜ `react-native-ble-manager` and add `BleManager.xcodeproj`
3. In XCode, in the project navigator, select your project. Add `libBleManager.a` to your project's `Build Phases` ➜ `Link Binary With Libraries`
4. Run your project (`Cmd+R`)<

#### Android

1. Open up `android/app/src/main/java/[...]/MainApplication.java`
  - Add `import com.pjamrozowicz.BleManagerPackage;` to the imports at the top of the file
  - Add `new BleManagerPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-ble-manager'
  	project(':react-native-ble-manager').projectDir = new File(rootProject.projectDir, 	'../node_modules/react-native-ble-manager/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-ble-manager')
  	```


## Usage
```javascript
import BleManager from 'react-native-ble-manager';

// TODO: What to do with the module?
BleManager;
```
