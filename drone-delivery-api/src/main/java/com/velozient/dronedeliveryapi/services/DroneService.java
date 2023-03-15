package com.velozient.dronedeliveryapi.services;

import com.velozient.dronedeliveryapi.models.Delivery;
import com.velozient.dronedeliveryapi.models.Drone;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class DroneService {
    /**
     * Generates the delivery planning of the given drones with the provided deliveries.
     * The algorithm will balance the workload of the drones to keep as few idle drones as possible.
     * The algorithm will also generate as few trips as possible to optimize cost.
     *
     * @param drones
     * @param deliveries
     */
    public void generateDeliveryPlanning(List<Drone> drones, List<Delivery> deliveries) {
        for (Delivery delivery : deliveries) {
            if (addDeliveryToOpenTrip(drones, delivery)) continue;
            if (addDeliveryToDroneWithoutOpenTrip(drones, delivery)) continue;
            addToDroneWithFewerTrips(drones, delivery);
        }
        drones.parallelStream().forEach(drone -> drone.closeOpenTrip());
    }

    /**
     * Add delivery to the drone with fewer closed trips
     * Will be prioritized drones with fewer trips to balance the work load and with greater capacity to optimize cost.
     *
     * @param drones
     * @param delivery
     */
    protected void addToDroneWithFewerTrips(List<Drone> drones, Delivery delivery) {
        Drone droneWithFewerTrips = drones.stream()
                .filter(drone -> drone.getMaximumWeight() >= delivery.getPackageWeight())
                .sorted(Comparator.comparing(Drone::getClosedTripsLength)
                        .thenComparing(Comparator.comparing(Drone::getMaximumWeight).reversed()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Delivery with a weight greater than that supported by drones."));

        droneWithFewerTrips.addTrip(delivery);
    }

    /**
     * Trys to add delivery to drones without open trip.
     * The goal of this step is to avoid close trips with space
     * Will be prioritized drones with fewer closer trips and with greater capacity
     *
     * @param drones
     * @param delivery
     * @return
     */
    protected boolean addDeliveryToDroneWithoutOpenTrip(List<Drone> drones, Delivery delivery) {
        Optional<Drone> droneWithoutOpenTrip = drones.stream()
                .filter(drone -> drone.getMaximumWeight() >= delivery.getPackageWeight())
                .filter(drone -> !drone.hasOpenTrip())
                .sorted(Comparator.comparing(Drone::getClosedTripsLength)
                        .thenComparing(Comparator.comparing(Drone::getMaximumWeight).reversed()))
                .findFirst();

        if (droneWithoutOpenTrip.isPresent()) {
            droneWithoutOpenTrip.get().addTrip(delivery);
            return true;
        }
        return false;
    }

    /**
     * Trys to add delivery to an open trip
     * Will be prioritized drones with less space available.
     * The goal is complete the open trip as soon as possible
     *
     * @param drones
     * @param delivery
     * @return
     */
    protected boolean addDeliveryToOpenTrip(List<Drone> drones, Delivery delivery) {
        Optional<Drone> droneWithLessSpaceAvailable = drones.stream()
                .filter(drone -> drone.getSpaceOnOpenTrip() >= delivery.getPackageWeight())
                .sorted(Comparator.comparing(Drone::getSpaceOnOpenTrip))
                .findFirst();

        if (droneWithLessSpaceAvailable.isPresent()) {
            droneWithLessSpaceAvailable.get().addTrip(delivery);
            return true;
        }

        return false;
    }
}
