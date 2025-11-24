package com.mycompany.oceanica;

import java.io.BufferedReader;
import java.io.File; // <--- Necesitas esta importación
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author lacay
 */
public class BoreTunnel {
    //Esta clase existe para redireccionar los puertos y conseguir conectar servidor cliente fuera de la red local xd
    
    private static final String BORE_EXECUTABLE_NAME = "bore.exe"; 
    
    private static final int PORT = 35500; // El puerto local que tu servidor Java escucha
    private static final String BORE_HOST = "bore.pub"; // El servidor Bore público
    private static Process boreProcess; // Para poder detener el túnel más tarde

    public static Integer startBoreTunnel() throws IOException {
        Integer publicPort = null;
        try {
            String currentDir = System.getProperty("user.dir");
            File boreExecutableFile = new File(currentDir, BORE_EXECUTABLE_NAME); 
            
            System.out.println("Buscando bore.exe en: " + boreExecutableFile.getAbsolutePath());

            // 2. Verificar si el archivo bore.exe existe
            if (!boreExecutableFile.exists()) {
                throw new IOException("El archivo bore.exe no se encontró en: " + boreExecutableFile.getAbsolutePath() + 
                                      ". Asegúrate de que 'bore.exe' está en el mismo directorio que tu .exe o .jar.");
            }
            // 3. Verificar permisos de ejecución
            if (!boreExecutableFile.canExecute()) {
                // Intentar dar permisos de ejecución por si acaso (más relevante en Linux, pero no hace daño en Windows)
                boreExecutableFile.setExecutable(true); 
                if (!boreExecutableFile.canExecute()) { // Volver a verificar
                    throw new IOException("El archivo bore.exe no tiene permisos de ejecución: " + boreExecutableFile.getAbsolutePath());
                }
            }

            // localPort es el PORT de tu servidor Java. remotePort es el puerto en bore.pub
            String[] command = {boreExecutableFile.getAbsolutePath(), "local", String.valueOf(PORT), "--to", BORE_HOST};
            
            System.out.println("Comando Bore: " + String.join(" ", command));
            
            ProcessBuilder pb = new ProcessBuilder(command);
            pb.redirectErrorStream(true);
            boreProcess = pb.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(boreProcess.getInputStream()));
            String line;
            System.out.println("Iniciando Bore... Esperando puerto público...");

            Pattern pattern = Pattern.compile("bore\\.pub:(\\d+)"); 
            
            long startTime = System.currentTimeMillis();
            long timeout = 15000; // Timeout de 15 segundos para buscar el puerto
            
            while ((line = reader.readLine()) != null && (System.currentTimeMillis() - startTime < timeout)) {
                System.out.println("Bore: " + line); // Imprime la salida de bore para depuración
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    publicPort = Integer.parseInt(matcher.group(1));
                    System.out.println("¡Tunel Bore establecido! Puerto publico: " + publicPort);
                    break; // Salir una vez que encontramos el puerto
                }
            }

            // Si Bore no se inició correctamente o no encontró la URL
            if (publicPort == null) {
                System.err.println("Error: No se pudo obtener el puerto público de Bore dentro del tiempo límite. Revisa la salida de Bore arriba y el patrón Regex.");
                if (boreProcess != null) {
                    boreProcess.destroyForcibly();
                    boreProcess = null;
                }
                throw new IOException("No se pudo establecer el túnel Bore o encontrar el puerto público.");
            }
            
        } catch (IOException e) {
            System.err.println("Error crítico al iniciar Bore. Detalles: " + e.getMessage());
            e.printStackTrace();
            // Mostrar un JOptionPane para el usuario final también
            javax.swing.JOptionPane.showMessageDialog(null, 
                "Error crítico al iniciar el túnel Bore:\n" + e.getMessage() + 
                "\nAsegúrate de que 'bore.exe' está en el mismo directorio que tu juego.", 
                "Error de Túnel Bore", javax.swing.JOptionPane.ERROR_MESSAGE);
            throw e; 
        }
        return publicPort;
    }
    
    public static void stopBoreTunnel() {
        if (boreProcess != null && boreProcess.isAlive()) {
            boreProcess.destroy(); // Envía una señal de terminación suave
            try {
                // Espera un poco para que termine limpiamente
                if (!boreProcess.waitFor(5, java.util.concurrent.TimeUnit.SECONDS)) {
                    boreProcess.destroyForcibly(); // Forzado si no termina suave
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restaura el estado de interrupción
                System.err.println("Interrupción al detener Bore: " + e.getMessage());
            }
            System.out.println("Bore Tunnel detenido.");
            boreProcess = null; // Reinicia la referencia
        }
    }
}