package com.example.barber.service;

import com.example.barber.model.Cita;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
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
    public List<Cita> buscarCitasPorFecha(LocalDate fecha) {
        List<Cita> citas = new ArrayList<>();
        String fechaCarpeta = fecha.toString();
        String rutaCarpeta = STORAGE_DIR + fechaCarpeta;

        System.out.println("📂 Buscando citas en: " + rutaCarpeta);

        File carpeta = new File(rutaCarpeta);
        if (!carpeta.exists()) {
            System.out.println("❌ ERROR: La carpeta NO existe -> " + rutaCarpeta);
            return citas;
        } else {
            System.out.println("✅ La carpeta SÍ existe.");
        }

        if (!carpeta.isDirectory()) {
            System.out.println("❌ ERROR: " + rutaCarpeta + " NO es un directorio.");
            return citas;
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        File[] archivos = carpeta.listFiles();
        if (archivos == null || archivos.length == 0) {
            System.out.println("⚠ La carpeta está vacía.");
            return citas;
        }

        for (File archivo : archivos) {
            System.out.println("📄 Leyendo archivo: " + archivo.getName());
            try {
                Cita cita = objectMapper.readValue(archivo, Cita.class);
                System.out.println("✔ Cita encontrada: " + cita);
                citas.add(cita);
            } catch (IOException e) {
                System.out.println("❌ ERROR al leer el archivo: " + archivo.getName());
                e.printStackTrace();
            }
        }

        return citas;
    }
}
