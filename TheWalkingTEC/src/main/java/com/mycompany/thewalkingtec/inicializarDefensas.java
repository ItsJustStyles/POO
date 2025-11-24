package com.mycompany.thewalkingtec;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
// Ya no necesitamos importar com.google.gson, java.io.InputStreamReader, etc.

/**
 * Clase Fábrica especializada en cargar configuraciones e inicializar objetos Defensas.
 * Ahora usa GestorDatosJSON para obtener la lista de configuraciones.
 */
public class inicializarDefensas { 

    // Mapa para almacenar las configuraciones de SOLO las defensas
    private final Map<String, PersonajeConfig> configuraciones = new HashMap<>();

    public inicializarDefensas() {
        cargarConfiguracionDesdeJSON();
    }
    
    // ------------------------------------------------------------------------
    
    // --- LÓGICA DE CARGA DE CONFIGURACIÓN (Usando GestorDatosJSON) ---

    private void cargarConfiguracionDesdeJSON() {
        try {
            // ⭐️ CARGA DELEGADA ⭐️: Obtenemos todos los personajes del Gestor
            List<PersonajeConfig> todosLosPersonajes = GestorDatosJSON.cargarPersonajes();
            
            if (todosLosPersonajes == null) {
                System.err.println("❌ ERROR: La lista de personajes devuelta por GestorDatosJSON es nula.");
                return;
            }

            // Almacenar SOLO las configuraciones de DEFENSA en el mapa
            int defensasCargadas = 0;
            for (PersonajeConfig p : todosLosPersonajes) {
                 // Usamos los Getters que definiste:
                 if (p.getTipo() != null && p.getTipo().equalsIgnoreCase("Defensa")) { 
                    configuraciones.put(p.getNombre(), p); 
                    defensasCargadas++;
                 }
            }
            System.out.println("✅ Configuraciones JSON de defensas cargadas: " + defensasCargadas + " defensas listas.");
            
        } catch (Exception e) {
            // Este catch atraparía cualquier error dentro de GestorDatosJSON.cargarPersonajes()
            System.err.println("❌ ERROR al obtener la configuración de defensas: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ------------------------------------------------------------------------

    // --- MÉTODOS DE FÁBRICA ---

    private PersonajeConfig getConfig(String nombrePersonaje) {
        return configuraciones.get(nombrePersonaje);
    }

    /**
     * Crea e inicializa una instancia de la clase base Defensas.
     * @param nombreDefensa El nombre de la defensa (Ej: "Megumin").
     * @param fila Fila de colocación.
     * @param columna Columna de colocación.
     * @param tablero Referencia al Tablero.
     * @return Una instancia de Defensas o null si falla.
     */
    public Defensas crearDefensa(String nombreDefensa, int fila, int columna, Tablero tablero) {
        PersonajeConfig config = getConfig(nombreDefensa);

        if (config == null) {
            // Este es el mensaje de error que estabas viendo. Ahora indica que el nombre no está en el JSON.
            System.err.println("Defensa '" + nombreDefensa + "' no encontrada en la configuración cargada (¿Falta en el JSON?).");
            return null;
        }

        // Llamada al constructor de Defensas (9 argumentos)
        return new Defensas(
            config.getVida(), 
            config.getDano(), 
            config.getVelocidad(), 
            config.getRuta(), 
            config.getRango(), 
            fila, columna, 
            tablero
        ) {
        };
    }
}