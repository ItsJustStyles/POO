/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.thewalkingtec;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Image;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author araya
 */
public class PantallaPrincipal extends javax.swing.JFrame {
    private ImageIcon iconoEnArrastre; 
    private PersonajeConfig personajeSeleccionado = null;
    private Tablero panelJuego;
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PantallaPrincipal.class.getName());
    boolean modoColocacionActivo = false;
    Class<?> tipoDefensaSeleccionada = null;
    private java.util.List<PersonajeConfig> personajesCreados = new java.util.ArrayList<>();
    private String usuarioActual; // ⭐️ NUEVO: Guardar el usuario actual
    private ImageIcon iconoBombardero;
  
    public PantallaPrincipal() {
    initComponents();
    ColocarFondo();
    cargarAssets();

    cargarPersonajesGlobales();
}

private void cargarPersonajesGlobales() {
    personajesCreados.clear();
    jComboBox1.removeAllItems();
    
    List<PersonajeConfig> personajesGlobales = GestorDatosJSON.cargarPersonajes();
    personajesCreados.addAll(personajesGlobales);
    
    for (PersonajeConfig p : personajesGlobales) {
        if(p.getTipo().equals("Defensa")){
            jComboBox1.addItem(p.getNombre());
        }
    }
}

    
private void inicializarBaseCentral() {
    if (panelJuego != null) {
        int filaCentral = 25 / 2;
        int columnaCentral = 25 / 2; 
        
        EstructuraCentral base = new EstructuraCentral(filaCentral, columnaCentral, panelJuego);
        panelJuego.agregarDefensaInicial(base); 
    }
}

private void guardarProgresoActual() {
    if (usuarioActual != null) {
        Partida partidaActual = GestorDatosJSON.obtenerJugador(usuarioActual);
        if (partidaActual != null) {
            int nivelActual = Integer.parseInt(lblNivel.getText());
            partidaActual.setNivel(nivelActual);
            GestorDatosJSON.actualizarJugador(partidaActual);
        }
    }
}
    
private void cargarAssets() {
    this.iconoBombardero = crearIconoEscalado("/imagenes/bombardiro.png", 50, 50);
}

private ImageIcon crearIconoEscalado(String rutaImagen, int ancho, int alto) {
    try {

        URL urlRecurso = getClass().getResource(rutaImagen);

        if (urlRecurso != null) {
            // 2. Crear el ImageIcon original
            ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
            Image imagenOriginal = iconoOriginal.getImage();

            // 3. Escalar la imagen al tamaño deseado
            Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);

            // 4. Devolver el ImageIcon final
            return new ImageIcon(imagenEscalada);
        } else {
            System.err.println("❌ ERROR: Icono no encontrado en la ruta: " + rutaImagen);
            return null;
        }
    } catch (Exception e) {
        System.err.println("Error al procesar el icono: " + e.getMessage());
        return null;
    }
}

private void ColocarFondo(){
    JPanelImage mImagen = new JPanelImage(pnlInicio, "/imagenes/Fondo.png");
    
    pnlInicio.add(mImagen).repaint();
    
}

private static final int FILAS_TABLERO = 25;
private static final int COLUMNAS_TABLERO = 25;
private static final int F_CENTRO = FILAS_TABLERO / 2;
private static final int C_CENTRO = COLUMNAS_TABLERO / 2;
private static final int RADIO_MAXIMO = 5;

private void inicializarTablero() {
        Partida jugador = GestorDatosJSON.obtenerJugador(usuarioActual);
        this.panelJuego = new Tablero(jugador);

        this.pnlAreaJuego.setLayout(new java.awt.BorderLayout());
        this.pnlAreaJuego.add(panelJuego, java.awt.BorderLayout.CENTER); 
        
        this.pnlAreaJuego.revalidate();
        this.pnlAreaJuego.repaint();
    }
       
private void reiniciarJuego() {
    // Reiniciar nivel a 1 (se sobreescribe si se carga un usuario con progreso)
    lblNivel.setText("1");
    Partida jugador = GestorDatosJSON.obtenerJugador(usuarioActual);
    // Limpiar y recrear el tablero
    if (panelJuego != null) {
        pnlAreaJuego.removeAll();
        this.panelJuego = new Tablero(jugador);
        pnlAreaJuego.setLayout(new java.awt.BorderLayout());
        pnlAreaJuego.add(panelJuego, java.awt.BorderLayout.CENTER);
        pnlAreaJuego.revalidate();
        pnlAreaJuego.repaint();
        
        // Volver a inicializar
        inicializarBaseCentral();
        inicializarBotonesDefensa();
    }
    
    // Reactivar controles
    jButton1.setEnabled(true);
    jComboBox1.setEnabled(true);
    btnNextLevel.setEnabled(false);
    
    // Limpiar estado
    personajeSeleccionado = null;
    modoColocacionActivo = false;
    iconoEnArrastre = null;
    
    System.out.println("🔄 Juego reiniciado para: " + usuarioActual);
}

private void inicializarBotonesDefensa() {
    String rutaImagen = "/imagenes/bombardiro.png";
    int ancho = 100;
    int alto = 100;  
    

    try {
        URL urlRecurso = getClass().getResource(rutaImagen);

        if (urlRecurso != null) {
            // 2. Cargar y escalar la imagen
            ImageIcon iconoOriginal = new ImageIcon(urlRecurso);
            Image imagenOriginal = iconoOriginal.getImage();
            
            Image imagenEscalada = imagenOriginal.getScaledInstance(ancho, alto, Image.SCALE_SMOOTH);
            ImageIcon iconoFinal = new ImageIcon(imagenEscalada);

            this.bombardiro.setIcon(iconoFinal);
            
            this.bombardiro.setText("");
            this.bombardiro.setBorderPainted(false); 
            this.bombardiro.setContentAreaFilled(false);

        } else {
            System.err.println("❌ ERROR: Icono no encontrado en: " + rutaImagen);
            this.bombardiro.setText("ERROR");
        }
    } catch (Exception e) {
        System.err.println("Error al procesar el icono: " + e.getMessage());
    }
}

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlInicio = new javax.swing.JPanel();
        btnNuevoJuego = new javax.swing.JButton();
        btnCargarJuego = new javax.swing.JButton();
        btnSalir = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        pnlJuego = new javax.swing.JPanel();
        pnlAreaJuego = new javax.swing.JPanel();
        pnlDatos = new javax.swing.JPanel();
        btnVolver = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        bombardiro = new javax.swing.JButton();
        jComboBox1 = new javax.swing.JComboBox<>();
        jButton1 = new javax.swing.JButton();
        btnNextLevel = new javax.swing.JButton();
        lblNivel = new javax.swing.JLabel();
        pnlNewCharacter = new javax.swing.JPanel();
        lblNombre = new javax.swing.JLabel();
        txfRango = new javax.swing.JTextField();
        lblDano = new javax.swing.JLabel();
        lblVida = new javax.swing.JLabel();
        lblRuta = new javax.swing.JLabel();
        txfNombre = new javax.swing.JTextField();
        txfDano = new javax.swing.JTextField();
        txfVida = new javax.swing.JTextField();
        txfRuta = new javax.swing.JTextField();
        cmbType = new javax.swing.JComboBox<>();
        txfVelocidad = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.CardLayout());

        pnlInicio.setPreferredSize(new java.awt.Dimension(700, 500));

        btnNuevoJuego.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnNuevoJuego.setText("Nuevo Juego");
        btnNuevoJuego.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNuevoJuegoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNuevoJuegoMouseExited(evt);
            }
        });
        btnNuevoJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNuevoJuegoActionPerformed(evt);
            }
        });

        btnCargarJuego.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnCargarJuego.setText("Cargar Juego");
        btnCargarJuego.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCargarJuegoMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCargarJuegoMouseExited(evt);
            }
        });
        btnCargarJuego.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCargarJuegoActionPerformed(evt);
            }
        });

        btnSalir.setFont(new java.awt.Font("Liberation Sans", 1, 18)); // NOI18N
        btnSalir.setText("ZZZ-Salir");
        btnSalir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSalirMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSalirMouseExited(evt);
            }
        });
        btnSalir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSalirActionPerformed(evt);
            }
        });

        jButton2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton2.setText("Crear Personaje");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlInicioLayout = new javax.swing.GroupLayout(pnlInicio);
        pnlInicio.setLayout(pnlInicioLayout);
        pnlInicioLayout.setHorizontalGroup(
            pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInicioLayout.createSequentialGroup()
                .addContainerGap(684, Short.MAX_VALUE)
                .addGroup(pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnNuevoJuego, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCargarJuego, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSalir, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        pnlInicioLayout.setVerticalGroup(
            pnlInicioLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInicioLayout.createSequentialGroup()
                .addContainerGap(321, Short.MAX_VALUE)
                .addComponent(btnNuevoJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnCargarJuego, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSalir, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(16, 16, 16))
        );

        getContentPane().add(pnlInicio, "card2");

        pnlAreaJuego.setBackground(new java.awt.Color(255, 255, 255));

        javax.swing.GroupLayout pnlAreaJuegoLayout = new javax.swing.GroupLayout(pnlAreaJuego);
        pnlAreaJuego.setLayout(pnlAreaJuegoLayout);
        pnlAreaJuegoLayout.setHorizontalGroup(
            pnlAreaJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 545, Short.MAX_VALUE)
        );
        pnlAreaJuegoLayout.setVerticalGroup(
            pnlAreaJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 547, Short.MAX_VALUE)
        );

        pnlDatos.setBackground(new java.awt.Color(204, 204, 204));

        btnVolver.setBackground(new java.awt.Color(255, 0, 51));
        btnVolver.setFont(new java.awt.Font("Times New Roman", 1, 8)); // NOI18N
        btnVolver.setForeground(new java.awt.Color(255, 255, 255));
        btnVolver.setText("OUT");
        btnVolver.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVolverActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jLabel2.setText("Defensas disponibles:");

        bombardiro.setText("bombardiro");
        bombardiro.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bombardiroActionPerformed(evt);
            }
        });

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "BombardiroCrocodrilo", " " }));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("Comenzar horda");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnNextLevel.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        btnNextLevel.setText("Siguiente nivel");
        btnNextLevel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnNextLevelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnNextLevelMouseExited(evt);
            }
        });
        btnNextLevel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextLevelActionPerformed(evt);
            }
        });

        lblNivel.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        lblNivel.setText("1");

        javax.swing.GroupLayout pnlDatosLayout = new javax.swing.GroupLayout(pnlDatos);
        pnlDatos.setLayout(pnlDatosLayout);
        pnlDatosLayout.setHorizontalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnVolver))
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnNextLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 186, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(pnlDatosLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jLabel2))
                                .addGroup(pnlDatosLayout.createSequentialGroup()
                                    .addGap(12, 12, 12)
                                    .addComponent(bombardiro, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlDatosLayout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 112, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(pnlDatosLayout.createSequentialGroup()
                .addGap(134, 134, 134)
                .addComponent(lblNivel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDatosLayout.setVerticalGroup(
            pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlDatosLayout.createSequentialGroup()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(bombardiro, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(57, 57, 57)
                .addComponent(btnNextLevel, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblNivel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlDatosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(pnlDatosLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(btnVolver, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        javax.swing.GroupLayout pnlJuegoLayout = new javax.swing.GroupLayout(pnlJuego);
        pnlJuego.setLayout(pnlJuegoLayout);
        pnlJuegoLayout.setHorizontalGroup(
            pnlJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlJuegoLayout.createSequentialGroup()
                .addComponent(pnlAreaJuego, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlJuegoLayout.setVerticalGroup(
            pnlJuegoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlAreaJuego, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        getContentPane().add(pnlJuego, "card3");

        lblNombre.setText("Nombre");

        txfRango.setText("Ingresa el rango");
        txfRango.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfRangoActionPerformed(evt);
            }
        });

        lblDano.setText("Dano");

        lblVida.setText("Vida");

        lblRuta.setText("Ruta");

        txfNombre.setText("jTextField1");
        txfNombre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfNombreActionPerformed(evt);
            }
        });

        txfDano.setText("jTextField1");
        txfDano.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfDanoActionPerformed(evt);
            }
        });

        txfVida.setText("jTextField3");

        txfRuta.setText("jTextField4");

        cmbType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Defensa", "Zombie" }));

        txfVelocidad.setText("Ingresa la velocidad");
        txfVelocidad.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txfVelocidadActionPerformed(evt);
            }
        });

        jButton3.setText("Crear Presonaje");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlNewCharacterLayout = new javax.swing.GroupLayout(pnlNewCharacter);
        pnlNewCharacter.setLayout(pnlNewCharacterLayout);
        pnlNewCharacterLayout.setHorizontalGroup(
            pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                .addGap(121, 121, 121)
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                        .addComponent(lblNombre)
                        .addGap(96, 96, 96)
                        .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                        .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblDano)
                            .addComponent(lblVida)
                            .addComponent(lblRuta)
                            .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(64, 64, 64)
                        .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(txfDano)
                            .addComponent(txfVida)
                            .addComponent(txfRuta)
                            .addComponent(txfVelocidad, javax.swing.GroupLayout.DEFAULT_SIZE, 257, Short.MAX_VALUE)
                            .addComponent(txfRango))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 210, Short.MAX_VALUE)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))))
        );
        pnlNewCharacterLayout.setVerticalGroup(
            pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                .addGap(128, 128, 128)
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblNombre)
                    .addComponent(txfNombre, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(39, 39, 39)
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDano)
                    .addComponent(txfDano, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txfVida, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblVida))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRuta)
                    .addComponent(txfRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlNewCharacterLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlNewCharacterLayout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(cmbType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlNewCharacterLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 129, Short.MAX_VALUE)
                        .addComponent(txfVelocidad, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)))
                .addComponent(txfRango, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(14, 14, 14))
        );

        getContentPane().add(pnlNewCharacter, "card4");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCargarJuegoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCargarJuegoActionPerformed
        ImageIcon iconoPersonalizado;
        iconoPersonalizado = null;
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/imagenes/alya.jpg"));
            java.awt.Image imagenOriginal = iconoOriginal.getImage();
            java.awt.Image imagenEscalada = imagenOriginal.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
            iconoPersonalizado = new ImageIcon(imagenEscalada);
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }

        String nombreUsuario = (String) JOptionPane.showInputDialog(
            null,
            "Ingrese su nombre de usuario a cargar",
            "Cargar Partida",
            JOptionPane.PLAIN_MESSAGE,
            iconoPersonalizado,
            null,
            null
        );

        if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
            Partida partida = GestorDatosJSON.obtenerJugador(nombreUsuario);
            if (partida == null) {
                JOptionPane.showMessageDialog(this, "No creado", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                this.usuarioActual = nombreUsuario;

                // ⭐️ REINICIAR JUEGO PARA USUARIO CARGADO
                reiniciarJuego();

                // Cargar nivel guardado del usuario
                lblNivel.setText(String.valueOf(partida.getNivel()));
                
                inicializarTablero();
                inicializarBotonesDefensa();
                inicializarBaseCentral();
                CardLayout cl = (CardLayout) (getContentPane().getLayout());
                cl.show(getContentPane(), "card3");
                btnNextLevel.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnCargarJuegoActionPerformed

    private void btnSalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSalirActionPerformed
        guardarProgresoActual();
        System.exit(0);
    }//GEN-LAST:event_btnSalirActionPerformed

    
    
    
    
    
    private void btnNuevoJuegoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNuevoJuegoActionPerformed
        ImageIcon iconoPersonalizado;
        iconoPersonalizado = null;
        try {
            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource("/imagenes/alya.jpg"));
            java.awt.Image imagenOriginal = iconoOriginal.getImage();
            java.awt.Image imagenEscalada = imagenOriginal.getScaledInstance(60, 60, java.awt.Image.SCALE_SMOOTH);
            iconoPersonalizado = new ImageIcon(imagenEscalada);
        } catch (Exception e) {
            System.err.println("Error al cargar el icono: " + e.getMessage());
        }

        String nombreUsuario = (String) JOptionPane.showInputDialog(
            null,
            "Ingrese su nombre de usuario",
            "Crear Partida",
            JOptionPane.PLAIN_MESSAGE,
            iconoPersonalizado,
            null,
            null
        );

        if (nombreUsuario != null && !nombreUsuario.trim().isEmpty()) {
            if (GestorDatosJSON.existeJugador(nombreUsuario)) {
                JOptionPane.showMessageDialog(this, "Ya existe el jugador bro", "Error", JOptionPane.ERROR_MESSAGE);
            } else {
                this.usuarioActual = nombreUsuario;
                Partida nuevaPartida = new Partida(nombreUsuario);
                GestorDatosJSON.agregarJugador(nuevaPartida);

                // ⭐️ REINICIAR JUEGO PARA NUEVO USUARIO
                reiniciarJuego();
                
                
                inicializarTablero();
                inicializarBotonesDefensa();
                inicializarBaseCentral();
                CardLayout cl = (CardLayout) (getContentPane().getLayout());
                cl.show(getContentPane(), "card3");
                btnNextLevel.setEnabled(false);
            }
        }

    }//GEN-LAST:event_btnNuevoJuegoActionPerformed

    private void btnVolverActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVolverActionPerformed
        guardarProgresoActual();
    
        CardLayout cl = (CardLayout) (getContentPane().getLayout());
        cl.show(getContentPane(), "card2");
    }//GEN-LAST:event_btnVolverActionPerformed

    private void bombardiroActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bombardiroActionPerformed
    if (personajeSeleccionado == null) {
        JOptionPane.showMessageDialog(this, "Selecciona un personaje primero.");
        return;
    }

    modoColocacionActivo = true;

    if (iconoBombardero != null) {
        panelJuego.setIconoEnArrastre(iconoBombardero);
    }

    
    panelJuego.setPersonajeSeleccionado(personajeSeleccionado);

    setCursor(java.awt.Cursor.getPredefinedCursor(java.awt.Cursor.CROSSHAIR_CURSOR));

    }//GEN-LAST:event_bombardiroActionPerformed

    private void btnNuevoJuegoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoJuegoMouseEntered
        btnNuevoJuego.setBackground(Color.red);
    }//GEN-LAST:event_btnNuevoJuegoMouseEntered

    private void btnNuevoJuegoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNuevoJuegoMouseExited
        btnNuevoJuego.setBackground(Color.white);
    }//GEN-LAST:event_btnNuevoJuegoMouseExited

    private void btnCargarJuegoMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarJuegoMouseEntered
        btnCargarJuego.setBackground(Color.red);
    }//GEN-LAST:event_btnCargarJuegoMouseEntered

    private void btnCargarJuegoMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnCargarJuegoMouseExited
        btnCargarJuego.setBackground(Color.white);
    }//GEN-LAST:event_btnCargarJuegoMouseExited

    private void btnSalirMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseEntered
        btnSalir.setBackground(Color.red);
    }//GEN-LAST:event_btnSalirMouseEntered

    private void btnSalirMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnSalirMouseExited
        btnSalir.setBackground(Color.white);
    }//GEN-LAST:event_btnSalirMouseExited

    private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1ActionPerformed
        String seleccion = (String) jComboBox1.getSelectedItem();
        if (seleccion == null) return;

        for (PersonajeConfig p : personajesCreados) {
            if (p.getNombre().equals(seleccion) && p.getTipo().equals("Defensa")) {
                personajeSeleccionado = p;

                try {
                    ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(p.getRuta()));
                    Image imgEscalada = iconoOriginal.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                    ImageIcon iconoFinal = new ImageIcon(imgEscalada);
                    bombardiro.setIcon(iconoFinal);
                    bombardiro.setText("");
                    bombardiro.setBorderPainted(false);
                    bombardiro.setContentAreaFilled(false);
                    iconoBombardero = iconoFinal;
                } catch (Exception e) {
                    System.err.println("Error cargando imagen: " + e.getMessage());
                }

                break;
            }
        }
    }//GEN-LAST:event_jComboBox1ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        jButton1.setEnabled(false);
        jComboBox1.setEnabled(false);
        panelJuego.iniciarJuego();
        btnNextLevel.setEnabled(true);
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btnNextLevelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextLevelMouseEntered
        btnNextLevel.setBackground(Color.red);
    }//GEN-LAST:event_btnNextLevelMouseEntered
    
    private void btnNextLevelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextLevelActionPerformed
        // Verificar si todos los zombies murieron
        if (panelJuego != null && panelJuego.estanTodosLosZombiesMuertos()) {
            // Aumentar nivel
            int nivelActual = Integer.parseInt(lblNivel.getText());
            lblNivel.setText(String.valueOf(nivelActual + 1));

            JOptionPane.showMessageDialog(this, "🎉 ¡Nivel " + (nivelActual + 1) + " completado!");

            // Reactivar botones para el siguiente nivel
            jButton1.setEnabled(true);
            jComboBox1.setEnabled(true);
            btnNextLevel.setEnabled(false);
            
            int nuevoNivel = nivelActual + 1;
            panelJuego.reiniciarNivel(nuevoNivel); 
        
            inicializarBotonesDefensa();

        } else {
            JOptionPane.showMessageDialog(this, "❌ No puedes avanzar. ¡Aún hay zombies vivos!");
        }
    }//GEN-LAST:event_btnNextLevelActionPerformed

    private void btnNextLevelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnNextLevelMouseExited
        btnNextLevel.setBackground(Color.white);
    }//GEN-LAST:event_btnNextLevelMouseExited

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        CardLayout cl = (CardLayout) (getContentPane().getLayout());
        cl.show(getContentPane(), "card4");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void txfNombreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfNombreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfNombreActionPerformed

    private void txfDanoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfDanoActionPerformed
        
    }//GEN-LAST:event_txfDanoActionPerformed

    private void txfVelocidadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfVelocidadActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfVelocidadActionPerformed

    private void txfRangoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txfRangoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txfRangoActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
        String nombre = txfNombre.getText();
        int dano = Integer.parseInt(txfDano.getText());
        int vida = Integer.parseInt(txfVida.getText());
        String ruta = txfRuta.getText();
        String tipo = (String) cmbType.getSelectedItem();
        int velocidad = Integer.parseInt(txfVelocidad.getText());
        int rango = Integer.parseInt(txfRango.getText());

        // Crear personaje con todos los campos
        PersonajeConfig nuevo = new PersonajeConfig(nombre, vida, dano, velocidad, ruta, tipo, rango);

        // ⭐️ CAMBIADO: Guardar como personaje GLOBAL
        GestorDatosJSON.agregarPersonaje(nuevo);

        // Actualizar lista local y combo box
        personajesCreados.add(nuevo);
        jComboBox1.addItem(nuevo.getNombre());

        JOptionPane.showMessageDialog(this, "✅ Personaje creado: " + nombre);

        // Limpiar campos
        txfNombre.setText("");
        txfDano.setText("");
        txfVida.setText("");
        txfRuta.setText("");
        txfVelocidad.setText("");
        txfRango.setText("");

        // Volver al menú principal
        CardLayout cl = (CardLayout) (getContentPane().getLayout());
        cl.show(getContentPane(), "card2");

    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "⚠️ Los valores numéricos no son válidos.", "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_jButton3ActionPerformed

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PantallaPrincipal().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton bombardiro;
    private javax.swing.JButton btnCargarJuego;
    private javax.swing.JButton btnNextLevel;
    private javax.swing.JButton btnNuevoJuego;
    private javax.swing.JButton btnSalir;
    private javax.swing.JButton btnVolver;
    private javax.swing.JComboBox<String> cmbType;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel lblDano;
    private javax.swing.JLabel lblNivel;
    private javax.swing.JLabel lblNombre;
    private javax.swing.JLabel lblRuta;
    private javax.swing.JLabel lblVida;
    private javax.swing.JPanel pnlAreaJuego;
    private javax.swing.JPanel pnlDatos;
    private javax.swing.JPanel pnlInicio;
    private javax.swing.JPanel pnlJuego;
    private javax.swing.JPanel pnlNewCharacter;
    private javax.swing.JTextField txfDano;
    private javax.swing.JTextField txfNombre;
    private javax.swing.JTextField txfRango;
    private javax.swing.JTextField txfRuta;
    private javax.swing.JTextField txfVelocidad;
    private javax.swing.JTextField txfVida;
    // End of variables declaration//GEN-END:variables
}
