# influxdb backup

This folder containing the backup file and import instructions for the influx database used by the raspberry pi hub.

## Instructions

1. Ensure `influxd` is currently running on your system by:
   - windows: running the `influxd.exe` executable
   - mac/linux: run `sudo influxd` on cli
2. open your terminal and move to this directory containing both the `import.sh` script and `physiyooo_backup.db`
3. run the import script
   - `./import.sh`
   - or `influx -import -path=physiyooo_backup.db`
4. run the `influx` cli program
   - windowsL run the `influx.exe`
   - mac/linux: run `influx` on cli
5. check if the imported database is present through `influx` cli
   - enter `show databases` to see if the database `PHYS` is present

> If the import script failed, please contact me for further assistance

## database info

The database `PHYS` will contain 3 tables
- `RAW_ACC` - accelerometer data read directly from the sensor
- `RAW_GYRO` - gyroscope data read directly from the sensor
- `SHAKE` - processed sensor data to used in the shake test

## remove database

To remove the database, while in the `influx` cli, run

```
> drop database PHYS
```