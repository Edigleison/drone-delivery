package com.velozient.dronedeliveryapi.services;

import com.velozient.dronedeliveryapi.models.Delivery;
import com.velozient.dronedeliveryapi.models.Drone;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class FileService {

    public List<String> getLinesFromInputStream(InputStream inputStream) {
        List<String> lines = new ArrayList<>();
        try (
                InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(streamReader)
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return lines;
    }

    public List<Drone> createDronesFromFormattedText(List<String> fileLines) {
        String droneLine = fileLines.get(0);
        String lineRegex = "^(\\[\\w+\\],\\s\\[\\d+\\],\\s)*(\\[\\w+\\],\\s\\[\\d+\\])$";
        if (!droneLine.matches(lineRegex)) {
            throw new IllegalArgumentException("The file content is invalid");
        }

        List<String> values = retrieveDataFromFormattedText(droneLine);

        List<Drone> drones = new ArrayList<>();
        for (int i = 0; i < values.size(); i += 2) {
            String name = values.get(i);
            Long maximumWeight = Long.parseLong(values.get(i + 1));
            drones.add(new Drone(name, maximumWeight));
        }

        return drones;
    }

    public List<Delivery> createDeliveriesFromFormattedTextLines(List<String> fileLines) {
        List<String> deliveryLines = fileLines.subList(1, fileLines.size());
        List<Delivery> deliveries = new ArrayList<>();
        for (String deliveryLine : deliveryLines) {
            final String deliveryRegex = "\\[\\w+\\],\\s\\[\\d+\\]";
            if (!deliveryLine.matches(deliveryRegex)) {
                throw new IllegalArgumentException("The file content is invalid");
            }

            List<String> values = retrieveDataFromFormattedText(deliveryLine);

            for (int i = 0; i < values.size(); i += 2) {
                String locationName = values.get(i);
                Long packageWeight = Long.parseLong(values.get(i + 1));
                deliveries.add(new Delivery(locationName, packageWeight));
            }
        }
        return deliveries;
    }

    public String generateOutput(List<Drone> drones) {
        StringBuilder sb = new StringBuilder();
        for (Drone drone : drones) {
            sb.append(drone.toString());
            for (Map.Entry<Long, List<Delivery>> trip : drone.getTrips().entrySet().stream().sorted(Map.Entry.comparingByKey()).toList()) {
                sb.append("\n");
                sb.append("Trip #" + trip.getKey());
                sb.append("\n");
                String deliveriesStr = trip.getValue().stream().map(Delivery::toString).collect(Collectors.joining(", "));
                sb.append(deliveriesStr);
            }
            sb.append("\n\n");
        }

        return sb.toString();
    }

    protected List<String> retrieveDataFromFormattedText(String formattedData) {
        return Pattern.compile("\\[\\w+\\]")
                .matcher(formattedData)
                .results()
                .map(result -> result.group()).map(str -> str.replaceAll("[\\[-\\]]", "").trim())
                .toList();
    }
}
