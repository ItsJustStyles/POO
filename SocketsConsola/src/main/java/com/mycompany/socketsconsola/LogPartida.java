/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.socketsconsola;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

/**
 *
 * @author gabos
 */

public class LogPartida {
    private static final String CARPETA_LOGS = "logs/";
    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter FORMATO_ARCHIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");
    private String archivoLog;
    private String nombreJugador;
    
    public LogPartida(String nombreJugador) {
        this.nombreJugador = nombreJugador;
        
        String carpetaJugador = CARPETA_LOGS + nombreJugador + "/";
        
        String timestamp = LocalDateTime.now().format(FORMATO_ARCHIVO);
        this.archivoLog = carpetaJugador + "partida_" + timestamp + ".txt";
        
        File carpeta = new File(carpetaJugador);
        boolean esNuevo = !carpeta.exists();
        
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
        
        registrarInicio(esNuevo);
    }
    
    private void registrarInicio(boolean esNuevo) {
        try (FileWriter fw = new FileWriter(archivoLog, false);
             PrintWriter pw = new PrintWriter(fw)) {
            
            if (esNuevo) {
                pw.println("╔═══════════════════════════════════════════════════════╗");
                pw.println("║           NUEVO JUGADOR REGISTRADO                   ║");
                pw.println("╚═══════════════════════════════════════════════════════╝");
                pw.println();
            }
            
            pw.println("═══════════════════════════════════════════════════════");
            pw.println("         LOG DE PARTIDA - " + nombreJugador);
            pw.println("         Inicio: " + LocalDateTime.now().format(FORMATO_FECHA));
            pw.println("═══════════════════════════════════════════════════════");
            pw.println();
        } catch (IOException e) {
            System.err.println("Error al crear log: " + e.getMessage());
        }
    }
    
    // Registrar comando
    public void registrarComando(String comando, String parametros, String resultado) {
        try (FileWriter fw = new FileWriter(archivoLog, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            String fecha = LocalDateTime.now().format(FORMATO_FECHA);
            
            pw.println("[" + fecha + "]");
            pw.println("  Comando: " + comando);
            if (parametros != null && !parametros.isEmpty()) {
                pw.println("  Parámetros: " + parametros);
            }
            pw.println("  Resultado: " + resultado);
            pw.println("─────────────────────────────────────────────────────");
            pw.println();
            
        } catch (IOException e) {
            System.err.println("Error al escribir en log: " + e.getMessage());
        }
    }
    
    // Registrar ataque
    public void registrarAtaque(String personajeAtacante, String arma, String personajeDefensor, 
                                int damage, boolean exito) {
        String resultado = exito ? 
            "ÉXITO - Daño causado: " + damage : 
            "FALLÓ - Sin daño";
        
        String parametros = "Atacante: " + personajeAtacante + " | Arma: " + arma + 
                           " | Defensor: " + personajeDefensor;
        
        registrarComando("ATACAR", parametros, resultado);
    }
    
    
    
    // Finalizar partida
    public void registrarFin(String ganador) {
        try (FileWriter fw = new FileWriter(archivoLog, true);
             PrintWriter pw = new PrintWriter(fw)) {
            pw.println();
            pw.println("═══════════════════════════════════════════════════════");
            pw.println("         FIN DE PARTIDA");
            pw.println("         Ganador: " + ganador);
            pw.println("         Fin: " + LocalDateTime.now().format(FORMATO_FECHA));
            pw.println("═══════════════════════════════════════════════════════");
        } catch (IOException e) {
            System.err.println("Error al finalizar log: " + e.getMessage());
        }
    }
    
    // Obtener contenido del log actual
    public String obtenerContenidoLog() {
        StringBuilder contenido = new StringBuilder();
        
        try (BufferedReader br = new BufferedReader(new FileReader(archivoLog))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            return "Error al leer el log: " + e.getMessage();
        }
        
        return contenido.toString();
    }
    
    // Método estático para obtener log de un archivo específico
    public static String obtenerContenidoLog(String rutaArchivo) {
        StringBuilder contenido = new StringBuilder();
        
        File archivo = new File(rutaArchivo);
        if (!archivo.exists()) {
            return "Archivo no encontrado: " + rutaArchivo;
        }
        
        try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                contenido.append(linea).append("\n");
            }
        } catch (IOException e) {
            return "Error al leer el log: " + e.getMessage();
        }
        
        return contenido.toString();
    }
    
    // Listar todos los logs de un jugador
    public static List<ArchivoLog> listarLogsDeJugador(String nombreJugador) {
        List<ArchivoLog> logs = new ArrayList<>();
        String carpetaJugador = CARPETA_LOGS + nombreJugador + "/";
        File carpeta = new File(carpetaJugador);
        
        if (!carpeta.exists() || !carpeta.isDirectory()) {
            return logs; // Lista vacía
        }
        
        File[] archivos = carpeta.listFiles((dir, name) -> name.endsWith(".txt"));
        
        if (archivos != null) {
            for (File archivo : archivos) {
                logs.add(new ArchivoLog(
                    archivo.getName(),
                    archivo.getAbsolutePath(),
                    archivo.lastModified()
                ));
            }
            
            logs.sort((a, b) -> Long.compare(b.getFechaModificacion(), a.getFechaModificacion()));
        }
        
        return logs;
    }
    
    // Obtener el log más reciente de un jugador
    public static String obtenerLogMasReciente(String nombreJugador) {
        List<ArchivoLog> logs = listarLogsDeJugador(nombreJugador);
        
        if (logs.isEmpty()) {
            return "No hay logs disponibles para el jugador: " + nombreJugador;
        }
        
        return obtenerContenidoLog(logs.get(0).getRutaCompleta());
    }
    public static void abrirVentanaLogReciente(String nombreJugador) {
        String contenido = obtenerLogMasReciente(nombreJugador);
        abrirVentanaConContenido(nombreJugador, contenido, "Log más reciente");
    }

    public static void abrirVentanaConContenido(String nombreJugador, String contenido, String titulo) {
        SwingUtilities.invokeLater(() -> {
            VentanaLog ventana = new VentanaLog(nombreJugador, contenido, titulo);
            ventana.setVisible(true);
        });
    }
    
    
    // Verificar si un jugador tiene logs
    public static boolean tieneLog(String nombreJugador) {
        File carpeta = new File(CARPETA_LOGS + nombreJugador + "/");
        return carpeta.exists() && carpeta.isDirectory();
    }
    
    // Eliminar todos los logs de un jugador
    public static boolean eliminarLogsJugador(String nombreJugador) {
        String carpetaJugador = CARPETA_LOGS + nombreJugador + "/";
        File carpeta = new File(carpetaJugador);
        
        if (!carpeta.exists()) {
            return false;
        }
        
        // Eliminar todos los archivos dentro
        File[] archivos = carpeta.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                archivo.delete();
            }
        }
        
        // Eliminar la carpeta
        return carpeta.delete();
    }
    
    public String getRutaArchivo() {
        return archivoLog;
    }
    
}
