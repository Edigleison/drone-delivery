package com.velozient.dronedeliveryapi.services;

import com.velozient.dronedeliveryapi.models.Delivery;
import com.velozient.dronedeliveryapi.models.Drone;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileServiceTest {
    private FileService fileService = new FileService();

    @Test
    public void Given_aValidFileContent_When_getLinesFromInputStream_Then_ReturnAllLines() {
        String validFileContent = givenAValidFileContent();
        InputStream inputStream = new ByteArrayInputStream(validFileContent.getBytes());

        List<String> linesFromInputStream = fileService.getLinesFromInputStream(inputStream);
        assertEquals(17, linesFromInputStream.size());

    }

    @Test
    public void Given_aValidDroneString_When_createDronesFromFormattedText_Then_returnTheDroneList() {
        String validDroneString = "[DroneA], [200], [DroneB], [250], [DroneC], [100]";
        List<String> fileLines = Arrays.asList(validDroneString);
        List<Drone> expected = Arrays.asList(
                new Drone("DroneA", 200L),
                new Drone("DroneB", 250L),
                new Drone("DroneC", 100L)
        );

        List<Drone> actual = fileService.createDronesFromFormattedText(fileLines);

        assertIterableEquals(expected, actual);
    }

    @Test
    public void Given_anInvalidDroneString_When_createDronesFromFormattedText_Then_throwsException() {
        String validDroneString = "DroneA, 200, DroneB, 250, DroneC, 100";
        List<String> fileLines = Arrays.asList(validDroneString);

        assertThrows(
                IllegalArgumentException.class,
                () -> fileService.createDronesFromFormattedText(fileLines)
        );

    }

    @Test
    public void Given_someValidFileLines_When_createDeliveriesFromFormattedTextLines_Then_returnTheDeliveryList() {
        List<String> validFileLines = Arrays.asList(
                "[DroneA], [200], [DroneB], [250], [DroneC], [100]",
                "[LocationA], [100]",
                "[LocationB], [80]",
                "[LocationC], [50]"
        );
        List<Delivery> expected = Arrays.asList(
                new Delivery("LocationA", 100L),
                new Delivery("LocationB", 80L),
                new Delivery("LocationC", 50L)
        );

        List<Delivery> actual = fileService.createDeliveriesFromFormattedTextLines(validFileLines);

        assertIterableEquals(expected, actual);
    }

    @Test
    public void Given_someInvalidFileLines_When_createDeliveriesFromFormattedTextLines_Then_throwsException() {
        List<String> invalidFileLines = Arrays.asList(
                "[DroneA], [200], [DroneB], [250], [DroneC], [100]",
                "[LocationA], [100]",
                "[LocationB], [80]",
                "LocationC, 50"
        );

        assertThrows(
                IllegalArgumentException.class,
                () -> fileService.createDeliveriesFromFormattedTextLines(invalidFileLines)
        );
    }

    @Test
    public void Given_aListOfDronesWithTrips_When_generateOutput_Then_returnTheExpectedString() {
        List<Drone> drones = giveAListOfDronesWithTrips();
        String expected = new StringBuilder()
                .append("[DroneA]\n")
                .append("Trip #1\n")
                .append("[LocationA], [LocationB]\n")
                .append("\n")
                .append("[DroneB]\n")
                .append("Trip #1\n")
                .append("[LocationC]\n")
                .append("Trip #2\n")
                .append("[LocationD]\n")
                .append("\n")
                .toString();

        String actual = fileService.generateOutput(drones);

        assertEquals(expected, actual);
    }

    @Test
    public void Given_aStringWithFormattedData_When_retrieveDataFromFormattedText_Then_returnsTheExpectedListOfString() {
        String formattedData = "[LocationA], [100]";
        List<String> expected = Arrays.asList("LocationA", "100");

        List<String> actual = fileService.retrieveDataFromFormattedText(formattedData);

        assertIterableEquals(expected, actual);
    }

    private static List<Drone> giveAListOfDronesWithTrips() {
        Drone droneA = new Drone("DroneA", 200L);
        Drone droneB = new Drone("DroneB", 100L);

        droneA.addTrip(new Delivery("LocationA", 100L));
        droneA.addTrip(new Delivery("LocationB", 100L));
        droneB.addTrip(new Delivery("LocationC", 100L));
        droneB.addTrip(new Delivery("LocationD", 100L));

        droneA.closeOpenTrip();
        droneB.closeOpenTrip();

        List<Drone> drones = Arrays.asList(droneA, droneB);
        return drones;
    }

    private String givenAValidFileContent() {
        return new StringBuilder()
                .append("[DroneA], [200], [DroneB], [250], [DroneC], [100]\n")
                .append("[LocationA], [200]\n")
                .append("[LocationB], [150]\n")
                .append("[LocationC], [50]\n")
                .append("[LocationD], [150]\n")
                .append("[LocationE], [100]\n")
                .append("[LocationF], [200]\n")
                .append("[LocationG], [50]\n")
                .append("[LocationH], [80]\n")
                .append("[LocationI], [70]\n")
                .append("[LocationJ], [50]\n")
                .append("[LocationK], [30]\n")
                .append("[LocationL], [20]\n")
                .append("[LocationM], [50]\n")
                .append("[LocationN], [30]\n")
                .append("[LocationO], [20]\n")
                .append("[LocationP], [90]\n")
                .toString();
    }

}




















