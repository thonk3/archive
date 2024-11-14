#include <Arduino.h>

#include "Nano33BLEAccelerometer.h"
#include "Nano33BLEGyroscope.h"

/* preprocessor directives */
// #define IDE_PLOT

Nano33BLEAccelerometerData accelData;
Nano33BLEGyroscopeData gyroData;


void setup() {
  /* serial baud rate */
  Serial.begin(115200);
  while(!Serial);

  // initializing imu sensors
  Accelerometer.begin();
  Gyroscope.begin();

  #ifdef IDE_PLOT   
    delay(200);   /* arduino IDE plotter legends */
    Serial.printf("aX,aY,aZ,gX,gY,gZ\n");
  #endif
}

void loop() {
  /* retrieve data from sensors */
  Accelerometer.pop(accelData);
  Gyroscope.pop(gyroData);

  /*
    print sensor data on serial to be processed by node-red
    string format:
    accX,accY,accZ,gyroX,gyroY,gyroZ,
  */
  Serial.printf("%.04f,%.04f,%.04f,", accelData.x, accelData.y, accelData.z);
  Serial.printf("%.04f,%.04f,%.04f,\n", gyroData.x, gyroData.y, gyroData.z);

  delay(100); /* 100ms loops, 10 msg per sec */
}