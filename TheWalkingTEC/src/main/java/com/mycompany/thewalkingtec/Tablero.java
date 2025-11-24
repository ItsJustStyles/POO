package com.mycompany.thewalkingtec;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Tablero extends javax.swing.JPanel {
    
    // ⭐️ 1. INSTANCIAR LA FÁBRICA DE DEFENSAS ⭐️
    private final inicializarDefensas defensaFactory = new inicializarDefensas(); 

    private PersonajeConfig personajeSeleccionado;
    private static final int FILAS = 25;
    private static final int COLUMNAS = 25;
    private static final int TAMANO_CELDA = 25;
    
    private int mouseX, mouseY;
    private ImageIcon iconoEnArrastre = null;
    
    private final List<Defensas> defensasColocadas = new ArrayList<>();
    private final List<Zombies> hordaZombies = new ArrayList<>();
    private Partida jugador;
    private int cantidadZombies;
    private Timer juegoTimer;
    private static final int actuacion = 100;
    
    private Class<?> tipoDefensaSeleccionada = null; // Esto ya no es necesario con PersonajeConfig
    
    private final inicializarZombies zombieFactory = new inicializarZombies(); 
    private final Random random = new Random();

    private final int F_CENTRO = FILAS / 2;     
    private final int C_CENTRO = COLUMNAS / 2;  
    private final int RADIO_MAXIMO = 5;
    private EstructuraCentral baseCentral;
    
    private boolean juegoIniciado = false;
    
    // ... (Métodos auxiliares: iniciarJuego, agregarDefensaInicial, etc. - SIN CAMBIOS) ...

    public void iniciarJuego() {
        if (!juegoIniciado) {
            this.juegoIniciado = true;
            System.out.println("🎮 El juego ha comenzado. ¡Llegan los zombis!");
            generarOleada(cantidadZombies);
            iniciarLogicaJuego();
        }
    }
    
   public void iniciarLogicaJuego() {
        if (juegoTimer != null && juegoTimer.isRunning()) {
            return; // Ya está corriendo
        }
        
        juegoTimer = new Timer(actuacion, (e) -> {
            for (Zombies zombie : new ArrayList<>(hordaZombies)) {
                zombie.actuar(this); 
            }
            
            for (Defensas defensa : this.defensasColocadas) {
            defensa.disparar(this.hordaZombies); 
        }
            repaint(); 
        });
        
        juegoTimer.start(); 
    }
    
   public void finalizarJuego(boolean victoria) {
    if (juegoTimer != null && juegoTimer.isRunning()) {
        juegoTimer.stop(); // Detiene la simulación
    }
    this.juegoIniciado = false;
    
    if (!victoria) {
        mostrarMensajeGameOver(); 
    }
}

private void mostrarMensajeGameOver() {
    javax.swing.SwingUtilities.invokeLater(() -> {
        
        // El mensaje de derrota
        javax.swing.JOptionPane.showMessageDialog(
            this, 
            "La base central ha sido destruida. ¡Game Over!", 
            "🚨 DERROTA 🚨", 
            javax.swing.JOptionPane.ERROR_MESSAGE
        );

        System.exit(0); // Cierra la aplicación después de mostrar el mensaje
    });
}
   
   
    public void reiniciarNivel(int nuevoNivel) {
    this.hordaZombies.clear(); 
    
    if (this.baseCentral != null) {
        this.baseCentral.restaurarVidaCompleta(); 
    }
    
    this.cantidadZombies = 15 + (nuevoNivel * 3);
    
    this.juegoIniciado = false; 
    repaint(); 
}
   

    public void eliminarZombie(Zombies zombie) {
        
    boolean eliminado = this.hordaZombies.remove(zombie);
        if (eliminado) {
            System.out.println("Zombi eliminado del tablero. Horda restante: " + this.hordaZombies.size());

            repaint(); 
        }
}
    
    public void generarOleada(int cantidadZombies) {
        
        List<String> nombresZombies = zombieFactory.getNombresZombiesDisponibles(); 
        
        if (nombresZombies == null || nombresZombies.isEmpty()) {
            System.err.println("🚫 No hay tipos de zombis cargados para generar la oleada.");
            return;
        }
        System.out.println("⚠️ ¡Generando oleada de " + cantidadZombies + " zombis!");
        for (int i = 0; i < cantidadZombies; i++) {
            
            // 1. Elegir un tipo de zombi al azar
            String nombreElegido = nombresZombies.get(random.nextInt(nombresZombies.size()));
            
            int filaSpawn;
            int columnaSpawn;
            
            do{
                filaSpawn = random.nextInt(FILAS); 
                columnaSpawn = random.nextInt(COLUMNAS);
                
                int distanciaAlCentro = Math.abs(filaSpawn - F_CENTRO) + Math.abs(columnaSpawn - C_CENTRO);
            }while ((Math.abs(filaSpawn - F_CENTRO) + Math.abs(columnaSpawn - C_CENTRO)) <= RADIO_MAXIMO);

                Zombies nuevoZombie = zombieFactory.crearZombie(
                    nombreElegido, 
                    filaSpawn, 
                    columnaSpawn, 
                    this 
                );
            
            if (nuevoZombie != null) {
                agregarZombie(nuevoZombie);
            }
        }
    }
    
    public boolean estanTodosLosZombiesMuertos() {
        return hordaZombies.isEmpty(); 
    }
    
    public void agregarDefensaInicial(Defensas defensa) {
        if (defensa instanceof EstructuraCentral) {
            this.baseCentral = (EstructuraCentral) defensa;
        }
        this.defensasColocadas.add(defensa);
        repaint();
    }
    
    public void agregarZombie(Zombies zombie) {
        this.hordaZombies.add(zombie);
        repaint();
    }
    
    public void setIconoEnArrastre(ImageIcon icono) {
        this.iconoEnArrastre = icono;
    }
    
    public EstructuraCentral getBaseCentral() {
        return this.baseCentral;
    }

    public Defensas obtenerDefensaEn(int fila, int columna) {
        for (Defensas d : defensasColocadas) {
            if (d.getPosicion().x == fila && d.getPosicion().y == columna) {
                return d;
            }
        }
        return null;
    }
    
    // -----------------------------------------------------------------------------------
    
    private void colocarDefensa(int clickX, int clickY) {
        int columna = clickX / TAMANO_CELDA;
        int fila = clickY / TAMANO_CELDA;

        // Si no hay icono o personaje seleccionado, no hace nada
        if (this.iconoEnArrastre == null || this.personajeSeleccionado == null) {
            System.out.println("⚠️ No hay personaje o icono seleccionado para colocar.");
            return;
        }

        String nombreDefensa = personajeSeleccionado.getNombre();
        System.out.println("🧱 Intentando colocar defensa " + nombreDefensa + " en [" + fila + "," + columna + "]");

        // --- 1️⃣ Verificar distancia desde la base central ---
        int distanciaFila = Math.abs(fila - F_CENTRO);
        int distanciaColumna = Math.abs(columna - C_CENTRO);
        if (distanciaFila > RADIO_MAXIMO || distanciaColumna > RADIO_MAXIMO) {
            System.out.println("🚫 Fuera del rango permitido para colocar personajes (radio máximo " + RADIO_MAXIMO + ").");
            return;
        }

        // --- 2️⃣ Verificar si ya hay algo en esa celda ---
        if (obtenerDefensaEn(fila, columna) != null) {
            System.out.println("❌ Ya hay una defensa en esa celda.");
            return;
        }

        // --- 3️⃣ CREAR EL PERSONAJE USANDO LA FÁBRICA JSON ---
        
        // ⭐️ AQUÍ USAMOS LA FÁBRICA EN LUGAR DEL CONSTRUCTOR ABSTRACTO ⭐️
        Defensas nuevaDefensa = defensaFactory.crearDefensa(
            nombreDefensa, // El nombre del JSON (Ej: "Megumin")
            fila, 
            columna, 
            this          // Referencia al Tablero
        );

        if (nuevaDefensa != null) {
            defensasColocadas.add(nuevaDefensa);
            System.out.println("✅ Defensa '" + nombreDefensa + "' colocada en [" + fila + "," + columna + "].");

            // Limpiar selección y refrescar
            this.iconoEnArrastre = null;
            this.personajeSeleccionado = null;
            repaint();
        } else {
             System.err.println("❌ Error: No se pudo crear la defensa. Verifique el nombre (" + nombreDefensa + ") y el constructor de la clase base Defensas.");
        }
    }
    
    // -----------------------------------------------------------------------------------

    public Tablero(Partida jugador){
        this.jugador = jugador;
        this.cantidadZombies = 12 + (jugador.getNivel() * 3);
        int anchoPanel = COLUMNAS * TAMANO_CELDA;
        int altoPanel = FILAS * TAMANO_CELDA;
        setPreferredSize(new java.awt.Dimension(COLUMNAS * TAMANO_CELDA, FILAS * TAMANO_CELDA));
        setBackground(Color.WHITE);
        
        // Permite arrastrar el icono visualmente
        addMouseMotionListener(new MouseMotionAdapter(){
            @Override
            public void mouseMoved(MouseEvent e) {
                if (juegoIniciado) {
                    return; 
                }
                verificarMovimiento(e.getX(), e.getY());
            }
        });

        // Detecta clics para colocar defensas
        addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (juegoIniciado) {
                    return; 
                }
                colocarDefensa(e.getX(), e.getY());
            }
        });
    }

    public void setPersonajeSeleccionado(PersonajeConfig personaje) {
        this.personajeSeleccionado = personaje;
        
        // Cargar el icono de arrastre basado en la ruta del JSON
        if (personaje != null) {
            try {
                java.net.URL urlRecurso = getClass().getResource(personaje.getRuta());
                if (urlRecurso != null) {
                    this.iconoEnArrastre = new ImageIcon(urlRecurso);
                }
            } catch (Exception e) {
                System.err.println("Error al cargar icono para arrastre: " + e.getMessage());
                this.iconoEnArrastre = null;
            }
        } else {
            this.iconoEnArrastre = null;
        }
    }

    // ... (Resto de métodos: verificarMovimiento, setTipoDefensaSeleccionada, eliminarDefensa, paintComponent, obtenerZombieEn - SIN CAMBIOS SIGNIFICATIVOS) ...
    
    private void verificarMovimiento(int newMouseX, int newMouseY) {
        if (this.iconoEnArrastre != null) {
            this.mouseX = newMouseX;
            this.mouseY = newMouseY;
            repaint();
        }
    }

    public void setTipoDefensaSeleccionada(Class<?> tipo) {
        this.tipoDefensaSeleccionada = tipo;
    }

    public void eliminarDefensa(Defensas defensa) {
        if(defensa instanceof EstructuraCentral) {
            ((EstructuraCentral) defensa).destruir();  
        }
        this.defensasColocadas.remove(defensa);
        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // ... (Dibujado de la cuadrícula y zona de construcción) ...
        g.setColor(Color.LIGHT_GRAY);
        int anchoTotal = getWidth();
        int altoTotal = getHeight();
        for (int i = 0; i <= FILAS; i++) { g.drawLine(0, i * TAMANO_CELDA, anchoTotal, i * TAMANO_CELDA); }
        for (int j = 0; j <= COLUMNAS; j++) { g.drawLine(j * TAMANO_CELDA, 0, j * TAMANO_CELDA, altoTotal); }
        
        g.setColor(new java.awt.Color(0, 200, 0, 50));
        for (int r = 0; r < FILAS; r++) {
            for (int c = 0; c < COLUMNAS; c++) {
                int distanciaFila = Math.abs(r - F_CENTRO);
                int distanciaColumna = Math.abs(c - C_CENTRO);
                if (distanciaFila <= RADIO_MAXIMO && distanciaColumna <= RADIO_MAXIMO) {
                    g.fillRect(c * TAMANO_CELDA, r * TAMANO_CELDA, TAMANO_CELDA, TAMANO_CELDA);
                }
            }
        }
        
        // 3️⃣ Dibuja las defensas colocadas
        for (Defensas defensa : defensasColocadas) {
            ImageIcon icono = defensa.getAssetImagen();
            Point pos = defensa.getPosicion();

            if (icono != null && pos != null) {
                int x = pos.y * TAMANO_CELDA;
                int y = pos.x * TAMANO_CELDA;
                g.drawImage(icono.getImage(), x, y, TAMANO_CELDA, TAMANO_CELDA, this);
            }
        }

        // 4️⃣ Dibuja los zombies en el tablero
        for (Zombies zombie : hordaZombies) {
            ImageIcon icono = zombie.getAssetImagen();
            Point pos = zombie.getPosicion();

            if (icono != null && pos != null) {
                int x = pos.y * TAMANO_CELDA;
                int y = pos.x * TAMANO_CELDA;
                g.drawImage(icono.getImage(), x, y, TAMANO_CELDA, TAMANO_CELDA, this);
            }
        }

        // 5️⃣ Si hay una defensa en arrastre (siguiendo el mouse)
        if (this.iconoEnArrastre != null) {
            Image imagen = this.iconoEnArrastre.getImage();
            int desplazamiento = TAMANO_CELDA / 2;
            int x = this.mouseX - desplazamiento;
            int y = this.mouseY - desplazamiento;
            g.drawImage(imagen, x, y, TAMANO_CELDA, TAMANO_CELDA, this);
        }
    }

    
    public Zombies obtenerZombieEn(int fila, int columna) {
        for (Zombies z : hordaZombies) {
            if (z.getPosicion().x == fila && z.getPosicion().y == columna) {
                return z;  
            }
        }
        return null;
    }
}