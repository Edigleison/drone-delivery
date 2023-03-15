package com.velozient.dronedeliveryapi.services;

import com.velozient.dronedeliveryapi.models.Delivery;
import com.velozient.dronedeliveryapi.models.Drone;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DroneServiceTest {
    private DroneService droneService = new DroneService();

    @Test
    public void Given_aDeliveryWithInvalidPackageWeight_When_addToDroneWithFewerTrips_Then_throwsException() {
        Drone droneA = new Drone("DroneA", 200L);
        Drone droneB = new Drone("DroneB", 150L);
        Drone droneC = new Drone("DroneC", 100L);

        List<Drone> drones = Arrays.asList(droneA, droneB, droneC);
        Delivery delivery = new Delivery("LocationB", 201L);

        IllegalArgumentException actual = assertThrows(
                IllegalArgumentException.class,
                () -> droneService.addToDroneWithFewerTrips(drones, delivery)
        );
        assertEquals("Delivery with a weight greater than that supported by drones.", actual.getMessage());
    }

    @Test
    public void Given_newDeliveryAndSomeDrones_When_addToDroneWithFewerTrips_Then_theDeliveryIsAddedToTheDroneWithFewerTripsAndGreaterCapacity() {
        Drone droneA = new Drone("DroneA", 200L);
        droneA.addTrip(new Delivery("LocationA", 200L));
        droneA.closeOpenTrip();

        Drone droneB = new Drone("DroneB", 150L);
        Drone droneC = new Drone("DroneC", 100L);

        List<Drone> drones = Arrays.asList(droneA, droneB, droneC);
        Delivery delivery = new Delivery("LocationB", 50L);

        droneService.addToDroneWithFewerTrips(drones, delivery);

        assertEquals(0, droneA.getSpaceOnOpenTrip());
        assertFalse(droneA.hasOpenTrip());
        assertEquals(1, droneA.getClosedTripsLength());

        assertEquals(100, droneB.getSpaceOnOpenTrip());
        assertTrue(droneB.hasOpenTrip());
        assertEquals(0, droneB.getClosedTripsLength());

        assertEquals(0, droneC.getSpaceOnOpenTrip());
        assertFalse(droneC.hasOpenTrip());
        assertEquals(0, droneC.getClosedTripsLength());
    }

    @Test
    public void Given_newDeliveryAndDronesWithOpenTrip_When_addDeliveryToDroneWithoutOpenTrip_Then_theDeliveryIsNotAdded() {
        Drone droneA = new Drone("DroneA", 200L);
        droneA.addTrip(new Delivery("LocationA", 50L));

        Drone droneB = new Drone("DroneB", 150L);
        droneB.addTrip(new Delivery("LocationB", 50L));

        Drone droneC = new Drone("DroneC", 100L);
        droneC.addTrip(new Delivery("LocationC", 50L));

        List<Drone> drones = Arrays.asList(droneA, droneB, droneC);
        Delivery delivery = new Delivery("LocationC", 50L);

        boolean actual = droneService.addDeliveryToDroneWithoutOpenTrip(drones, delivery);
        assertFalse(actual);
        assertEquals(150, droneA.getSpaceOnOpenTrip());
        assertEquals(100, droneB.getSpaceOnOpenTrip());
        assertEquals(50, droneC.getSpaceOnOpenTrip());
    }

    @Test
    public void Given_newDelivery_When_addDeliveryToDroneWithoutOpenTrip_Then_theDeliveryIsAddedToTheDroneWithLessTripsAndWithGreaterCapacity() {
        Drone droneA = new Drone("DroneA", 200L);
        droneA.addTrip(new Delivery("LocationA", 200L));
        droneA.closeOpenTrip();

        Drone droneB = new Drone("DroneB", 200L);
        Drone droneC = new Drone("DroneC", 100L);

        List<Drone> drones = Arrays.asList(droneA, droneB, droneC);
        Delivery delivery = new Delivery("LocationC", 50L);

        boolean actual = droneService.addDeliveryToDroneWithoutOpenTrip(drones, delivery);
        assertTrue(actual);
        assertEquals(0, droneA.getSpaceOnOpenTrip());
        assertEquals(150, droneB.getSpaceOnOpenTrip());
        assertEquals(0, droneC.getSpaceOnOpenTrip());
    }

    @Test
    public void Given_someDroneWithWithoutOpenTrip_When_addDeliveryToOpenTrip_Then_theDeliveryIsNotAddedToAnyDrone() {
        Drone droneA = new Drone("DroneA", 200L);
        Drone droneB = new Drone("DroneB", 150L);

        List<Drone> drones = Arrays.asList(droneA, droneB);
        Delivery delivery = new Delivery("LocationC", 25L);

        boolean actual = droneService.addDeliveryToOpenTrip(drones, delivery);
        assertFalse(actual);
        assertEquals(0, droneA.getSpaceOnOpenTrip());
        assertEquals(0, droneB.getSpaceOnOpenTrip());
    }

    @Test
    public void Given_someDroneWithOpenTrip_When_addDeliveryToOpenTrip_Then_theDeliveryIsAddedToTheDroneWithLessSpaceAvailable() {
        Drone droneA = new Drone("DroneA", 200L);
        droneA.addTrip(new Delivery("LocationA", 50L));

        Drone droneB = new Drone("DroneB", 150L);
        droneB.addTrip(new Delivery("LocationB", 50L));

        List<Drone> drones = Arrays.asList(droneA, droneB);
        Delivery delivery = new Delivery("LocationC", 25L);

        boolean actual = droneService.addDeliveryToOpenTrip(drones, delivery);
        assertTrue(actual);
        assertEquals(150, droneA.getSpaceOnOpenTrip());
        assertEquals(75, droneB.getSpaceOnOpenTrip());
    }

}
