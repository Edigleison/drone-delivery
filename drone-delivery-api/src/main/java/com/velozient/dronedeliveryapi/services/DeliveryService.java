package com.velozient.dronedeliveryapi.services;

import com.velozient.dronedeliveryapi.models.Delivery;
import com.velozient.dronedeliveryapi.models.Drone;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class DeliveryService {
    private FileService fileService;
    private DroneService droneService;

    public DeliveryService(FileService fileService, DroneService droneService) {
        this.fileService = fileService;
        this.droneService = droneService;
    }

    public String processFile(MultipartFile file) throws IOException {
        List<String> fileLines = fileService.getLinesFromInputStream(file.getInputStream());
        List<Drone> drones = fileService.createDronesFromFormattedText(fileLines);
        List<Delivery> deliveries = fileService.createDeliveriesFromFormattedTextLines(fileLines);

        droneService.generateDeliveryPlanning(drones, deliveries);

        return fileService.generateOutput(drones);
    }
}
