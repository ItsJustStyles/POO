package com.mycompany.thewalkingtec;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Clase Fábrica especializada en cargar configuraciones e inicializar objetos Zombies.
 * Delega la carga de datos al GestorDatosJSON.
 */
public class inicializarZombies { 

    // Mapa para almacenar las configuraciones de SOLO los zombis
    private final Map<String, PersonajeConfig> configuraciones = new HashMap<>();

    public inicializarZombies() {
        cargarConfiguracionDesdeJSON();
    }
    
    // ------------------------------------------------------------------------
    
    // --- LÓGICA DE CARGA DE CONFIGURACIÓN (Usando GestorDatosJSON) ---

    private void cargarConfiguracionDesdeJSON() {
        try {
            // Obtenemos todos los personajes del Gestor
            List<PersonajeConfig> todosLosPersonajes = GestorDatosJSON.cargarPersonajes();
            
            if (todosLosPersonajes == null) {
                System.err.println("❌ ERROR: La lista de personajes devuelta por GestorDatosJSON es nula.");
                return;
            }

            // Almacenar SOLO las configuraciones de ZOMBIE en el mapa
            int zombiesCargados = 0;
            for (PersonajeConfig p : todosLosPersonajes) {
                 if (p.getTipo() != null && p.getTipo().equalsIgnoreCase("Zombie")) { 
                    configuraciones.put(p.getNombre(), p); 
                    zombiesCargados++;
                 }
            }
            System.out.println("✅ Configuraciones JSON de zombis cargadas: " + zombiesCargados + " zombis listos.");
            
        } catch (Exception e) {
            System.err.println("❌ ERROR al obtener la configuración de zombis: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    // ------------------------------------------------------------------------

    // --- MÉTODOS DE FÁBRICA ---

    private PersonajeConfig getConfig(String nombrePersonaje) {
        return configuraciones.get(nombrePersonaje);
    }

    public Zombies crearZombie(String nombreZombie, int fila, int columna, Tablero tablero) {
        PersonajeConfig config = getConfig(nombreZombie);

        if (config == null) {
            System.err.println("Zombi '" + nombreZombie + "' no encontrado en la configuración cargada (¿Falta en el JSON?).");
            return null;
        }

        // ⭐️ Llamada al constructor de Zombies (9 argumentos) ⭐️
        // (Debe coincidir con la firma del constructor de la clase base Defensas/Zombies)
        return new Zombies(
            config.getNombre(),
            config.getVida(), 
            config.getDano(), 
            config.getVelocidad(), 
            config.getRuta(), 
            config.getRango(), // El rango se puede usar para el radio de explosión del suicida, por ejemplo
            fila, columna, 
            tablero
        ) {
        };
    }
    
    public List<String> getNombresZombiesDisponibles() {
        // 1. Obtenemos el conjunto de todas las claves (nombres) del mapa 'configuraciones'.
        Set<String> nombres = configuraciones.keySet();
        
        // 2. Devolvemos el conjunto convertido a una lista para facilitar la selección aleatoria
        //    (usando .get(indice) en el Tablero).
        return new ArrayList<>(nombres); 
    }
    
}