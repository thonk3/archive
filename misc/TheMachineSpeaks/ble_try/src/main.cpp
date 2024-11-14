#include <Arduino.h>
#include <ArduinoBLE.h>

#include "Nano33BLEAccelerometer.h"
#include "Nano33BLEGyroscope.h"

/* preprocessors */
#define BLE_BUFFER 20
#define BLE_DEVICE_NAME "Physiyooo"
#define BLE_LOCAL_NAME "Physiyooo_01"

Nano33BLEAccelerometerData accelData;
Nano33BLEGyroscopeData gyroData;

/* gguid are generated using online tools */
/* set BLE service and characteristics */
BLEService BLESensors("c5d78e24-c803-4b51-a195-96560829f4f7");
BLECharacteristic BLEgyroX("0001", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);
BLECharacteristic BLEgyroY("0002", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);
BLECharacteristic BLEgyroZ("0003", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);

BLECharacteristic BLEaccX("0004", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);
BLECharacteristic BLEaccY("0005", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);
BLECharacteristic BLEaccZ("0006", BLERead | BLENotify | BLEBroadcast, BLE_BUFFER);

/* string buffer for ble msg */
char bleBuffer[BLE_BUFFER];

void setup()
{
  Serial.begin(115200);
  while (!Serial)
    ;

  /* ble setup */
  if (!BLE.begin())
    while (1)
      ;
  else
  {
    BLE.setDeviceName(BLE_DEVICE_NAME);
    BLE.setLocalName(BLE_LOCAL_NAME);

    /* advertise signal */
    BLE.setAdvertisedService(BLESensors);

    /* characteristics */
    BLESensors.addCharacteristic(BLEgyroX);
    BLESensors.addCharacteristic(BLEgyroY);
    BLESensors.addCharacteristic(BLEgyroZ);

    BLESensors.addCharacteristic(BLEaccX);
    BLESensors.addCharacteristic(BLEaccY);
    BLESensors.addCharacteristic(BLEaccZ);

    BLE.addService(BLESensors);
    BLE.advertise();

    /* init sensors */
    Accelerometer.begin();
    Gyroscope.begin();
  }
}

void loop() {
  BLEDevice central = BLE.central();

  if(central) {
    int writeLength; 
    bool dataFlag = false;

    while(central.connected()) {
      if(Accelerometer.pop(accelData)) {
        writeLength = sprintf(bleBuffer, "%.4f", accelData.x);
        BLEaccX.writeValue(bleBuffer, writeLength);
        writeLength = sprintf(bleBuffer, "%.4f", accelData.y);
        BLEaccY.writeValue(bleBuffer, writeLength);
        writeLength = sprintf(bleBuffer, "%.4f", accelData.z);
        BLEaccZ.writeValue(bleBuffer, writeLength);
        dataFlag = true;
      }

      if(Gyroscope.pop(gyroData)) {
        writeLength = sprintf(bleBuffer, "%.4f", gyroData.x);
        BLEgyroX.writeValue(bleBuffer, writeLength);
        writeLength = sprintf(bleBuffer, "%.4f", gyroData.y);
        BLEgyroY.writeValue(bleBuffer, writeLength);
        writeLength = sprintf(bleBuffer, "%.4f", gyroData.z);
        BLEgyroZ.writeValue(bleBuffer, writeLength);
        dataFlag = true;
      }

      if(dataFlag) {
        Serial.printf(
          "%.4f,%.4f,%.4f,%.4f,%.4f,%.4f\r\n",
          accelData.x, accelData.y, accelData.z,
          gyroData.x, gyroData.y, gyroData.z
        );
      }

      delay(100);
    }
  }

}