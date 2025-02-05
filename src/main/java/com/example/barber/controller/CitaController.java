package com.example.barber.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.barber.model.Cita;
import com.example.barber.service.CitaService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class CitaController {

    @Autowired
    private CitaService citaService;

    @GetMapping("/")
    public String index() {
        return "index"; // Renderiza index.html
    }

    @GetMapping("/appoiment")
    public String appoiment() {
        return "appoiment"; // Renderiza appoiment.html
    }

    @GetMapping("/search")
    public String search() {
        return "search"; // Renderiza search.html
    }

    @PostMapping("/agendar")
    public String agendarCita(
            @RequestParam String cedula,
            @RequestParam String nombre,
            @RequestParam String servicio,
            @RequestParam String fecha,
            @RequestParam String hora,
            Model model) {

        LocalDate fechaObj = LocalDate.parse(fecha);
        LocalTime horaObj = LocalTime.parse(hora);

        Cita cita = new Cita(cedula, nombre, servicio, fechaObj, horaObj);
        String mensaje = citaService.agendarCita(cita);

        model.addAttribute("mensaje", mensaje);
        return "appoiment"; // Redirige al formulario de citas
    }
    @GetMapping("/buscarCitas")
    public String buscarCitasPorFecha(@RequestParam String fecha, Model model) {
        LocalDate fechaObj = LocalDate.parse(fecha);
        List<Cita> citas = citaService.buscarCitasPorFecha(fechaObj);
        model.addAttribute("citas", citas);
        return "search"; // Nombre de la vista donde se mostrarán las citas
    }
}

