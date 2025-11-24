/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
// GestorDatosJSON.java
package com.mycompany.thewalkingtec;
/**
 *
 * @author maicol
 */
import java.io.*;
import java.util.*;
import com.google.gson.*;

public class GestorDatosJSON {
    private static final String ARCHIVO = "datos.json";

    private static class Datos {
        List<Partida> jugadores = new ArrayList<>();
        List<PersonajeConfig> personajes = new ArrayList<>();
    }

    private static Datos cargarDatos() {
        File archivo = new File(ARCHIVO);
        if (!archivo.exists()) return new Datos();

        try (Reader reader = new FileReader(archivo)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, Datos.class);
        } catch (Exception e) {
            e.printStackTrace();
            return new Datos();
        }
    }

    private static void guardarDatos(Datos datos) {
        try (Writer writer = new FileWriter(ARCHIVO)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(datos, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // MÉTODOS JUGADORES
    public static List<Partida> cargarJugadores() {
        return cargarDatos().jugadores;
    }

    public static void agregarJugador(Partida nueva) {
        Datos datos = cargarDatos();
        datos.jugadores.add(nueva);
        guardarDatos(datos);
    }

    public static void actualizarJugador(Partida actualizada) {
        Datos datos = cargarDatos();
        for (int i = 0; i < datos.jugadores.size(); i++) {
            if (datos.jugadores.get(i).getNombre().equalsIgnoreCase(actualizada.getNombre())) {
                datos.jugadores.set(i, actualizada);
                break;
            }
        }
        guardarDatos(datos);
    }

    public static boolean existeJugador(String nombre) {
        for (Partida p : cargarDatos().jugadores) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return true;
        }
        return false;
    }

    public static Partida obtenerJugador(String nombre) {
        for (Partida p : cargarDatos().jugadores) {
            if (p.getNombre().equalsIgnoreCase(nombre)) return p;
        }
        return null;
    }

    // MÉTODOS PERSONAJES
    public static List<PersonajeConfig> cargarPersonajes() {
        return cargarDatos().personajes;
    }

    public static void agregarPersonaje(PersonajeConfig personaje) {
        Datos datos = cargarDatos();
        datos.personajes.add(personaje);
        guardarDatos(datos);
    }
}
