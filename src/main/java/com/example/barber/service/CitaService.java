package com.example.barber.service;

import com.example.barber.model.Cita;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
    public List<Cita> obtenerCitasPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        String fechaCarpeta = fecha.toString();
        File carpeta = new File(STORAGE_DIR + fechaCarpeta);

        if (carpeta.exists() && carpeta.isDirectory()) {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());

            for (File file : carpeta.listFiles()) {
                try {
                    Cita cita = objectMapper.readValue(file, Cita.class);
                    citas.add(cita);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return citas;
    }
}
