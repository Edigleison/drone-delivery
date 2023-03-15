package com.velozient.dronedeliveryapi.resource;

import com.velozient.dronedeliveryapi.services.DeliveryService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/delivery")
public class DeliveryResource {

    private DeliveryService deliveryService;

    public DeliveryResource(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @PostMapping("/file")
    public ResponseEntity<Resource> uploadDeliveryFile(@RequestParam("file") MultipartFile inputFile) {
        try {
            String outputText = deliveryService.processFile(inputFile);
            Resource outputFile = new ByteArrayResource(outputText.getBytes());
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"output.txt\"")
                    .body(outputFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
