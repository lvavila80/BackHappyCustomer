package com.project.Project.project.service.impl;

import com.project.Project.project.service.ErrorLoggingService;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

@Service
public class ErrorLoggingServiceImpl implements ErrorLoggingService {

    @Override
    public void logError(String errorLocation, String ex, String usedData) {
        try (FileWriter fileWriter = new FileWriter("errors.txt", true)) {
            fileWriter.write(errorLocation + " - " + LocalDateTime.now() + " - " + usedData + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
