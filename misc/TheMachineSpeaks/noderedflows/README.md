# node-red flows

## dependancies

extra pallets to install

- node-red-node-serialport
- node-red-contrib-influxdb
- node-red-node-mongodb
- node-red-dashboard

## instructions

1. please make sure your `node-red` contains the pallets listed above
2. either import each flow in [individual_flows](./individual_flows/) or [a single complete_flow](./complete_flow/)

## note

- if you have the `Arduino nano 33 ble sense` please ensure the `SerialRead` flow is reading from the correct port
- if you dont have the hardware, please connect the `join` node to the `parse msg` input node instead to use **simulated data**