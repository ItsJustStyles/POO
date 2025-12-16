/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author gabos
 */
public class ArchivoLog {
    private String nombre;
    private String rutaCompleta;
    private long fechaModificacion;

    public ArchivoLog(String nombre, String rutaCompleta, long fechaModificacion) {
        this.nombre = nombre;
        this.rutaCompleta = rutaCompleta;
        this.fechaModificacion = fechaModificacion;
    }

    public String getNombre() {
        return nombre;
    }

    public String getRutaCompleta() {
        return rutaCompleta;
    }

    public long getFechaModificacion() {
        return fechaModificacion;
    }

    public String getFechaFormateada() {
        LocalDateTime fecha = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(fechaModificacion),
            ZoneId.systemDefault()
        );
        return fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    @Override
    public String toString() {
        return nombre + " - " + getFechaFormateada();
    }
    
}
