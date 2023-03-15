# drone-delivery
Drone delivery service

This application receives a formatted file with the drones and deliveryies data and produces an output file with the delivery planning.

The algorithm will balance the workload of the drones to keep as few idle drones as possible.

The algorithm will also generate as few trips as possible to optimize cost.

This project consists of two applications
- **drone-delivery-api**: a spring boot application that provides a rest endpoint that allows sending the drone delivery input file and downloading the output file.

To start the API you should execute the command below inside the directory **drone-delivery-api**.

`./mvnw spring-boot:run`

- **drone-delivery-ui**: a web application (using react) that alows the user to select the input file and to send to the API and save the output file.
To start the ui application you should execute the command below inside the directory **drone-delivery-ui**

`npm install && npm start`

> To use the ui application you should starts the API application.

