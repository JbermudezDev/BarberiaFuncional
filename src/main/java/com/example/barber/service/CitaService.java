package com.example.barber.service;

import com.example.barber.model.Cita;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import ch.qos.logback.classic.Logger;

import org.apache.el.stream.Stream;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class CitaService {

    private static final String STORAGE_DIR = "storage/";

    
    public String agendarCita(Cita cita) {
        try {
            String fechaCarpeta = cita.getFecha().toString();
            Path carpetaPath = Paths.get(STORAGE_DIR + fechaCarpeta);
    
            // Si la carpeta no existe, la crea
            if (!Files.exists(carpetaPath)) {
                Files.createDirectories(carpetaPath);
            }
    
            String filePath = carpetaPath + "/" + cita.getCedula() + ".json";
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.writeValue(new File(filePath), cita);
    
            return "Cita agendada exitosamente.";
        } catch (IOException e) {
            e.printStackTrace();
            return "Error al agendar la cita: " + e.getMessage();
        }
    }

      private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(CitaService.class);
    public List<Cita> buscarCitasPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        String fechaCarpeta = fecha.toString();
        Path carpetaPath = Paths.get(STORAGE_DIR, fechaCarpeta);

        LOGGER.info("Buscando citas en la carpeta: {}", carpetaPath);

        if (Files.exists(carpetaPath) && Files.isDirectory(carpetaPath)) {
            try (DirectoryStream<Path> archivos = Files.newDirectoryStream(carpetaPath)) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());

                archivos.forEach(filePath -> {
                    try {
                        if (Files.isRegularFile(filePath)) {
                            Cita cita = objectMapper.readValue(filePath.toFile(), Cita.class);
                            citas.add(cita);
                        }
                    } catch (IOException e) {
                        LOGGER.error("Error al leer archivo: {}", filePath, e);
                    }
                });
            } catch (IOException e) {
                LOGGER.error("Error al listar archivos en la carpeta: {}", carpetaPath, e);
            }
        } else {
            LOGGER.warn("La carpeta {} no existe o no es un directorio válido", carpetaPath);
        }

        return citas;
    }
}
