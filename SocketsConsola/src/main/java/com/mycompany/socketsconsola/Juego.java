/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.mycompany.socketsconsola;

import Cliente.Client;
import Comandos.Command;
import Comandos.CommandFactory;
import Comandos.CommandReady;
import Comandos.CommandUtil;
import Servidor.Server;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.LineUnavailableException;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.Border;

/**
 *
 * @author lacay
 */
public class Juego extends javax.swing.JFrame {
    private Random random = new Random();
    private List<Personajes> todosLosPersonajes;
    private List<Personajes> heroesElegidos;
    private Map<String, List<Armas>> catalogoArmas;
    private int indexArma = 0;
    public GestorJson gestor = new GestorJson();
    Client cliente;
    
    private int alto = 800;
    private int ancho = 1500;
    private CardLayout cardLayout = new CardLayout();
    
    private final GraphicsDevice graphicsDevice = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
    private boolean isFullScreen = false;
    private Insets originalInsets; 
    
    private final Set<String> idsSeleccionadosArmas = new HashSet<>(); 
    private final Map<String, JComponent> componentesSeleccionadosArmas = new HashMap<>(); 
    private final Set<String> idsSeleccionados = new HashSet<>(); 
    private final Map<String, JComponent> componentesSeleccionados = new HashMap<>(); 
    private final Border BORDE_SELECCION = BorderFactory.createLineBorder(new Color(13, 35, 71), 3);
    private final Border BORDE_NORMAL = BorderFactory.createEmptyBorder();
    
    private JPanel panelSeleccionadoAnterior = null;
    private Border bordeSeleccionado = BorderFactory.createLineBorder(Color.YELLOW, 3); 
    private Border bordeNormal = null; 
    
    public SonidoMenu menuPersonajes = new SonidoMenu("/Musica/BalatroMainTheme.wav");
    private JPanelImage fondoActual = null;
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Juego.class.getName());
    
    private int tiempoRestanteSegundos = 300;
    private Timer swingTimer;
    
    private boolean comodinTipo = true;
    private boolean comodinDesbloqueado = false;
    private boolean comodinUsado = false;
    
    private LogPartida logJugador;
    String jugador;
    /**
     * Creates new form Juego
     */
    public Juego() throws IOException, LineUnavailableException {
        initComponents();
        menuPersonajes.loop();
        
        consola.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent evt){
                handleCommandInput(evt);
            }
        });
        
        this.setSize(ancho, alto);
        configurarPantallaCompleta();
        iniciarElementos(ancho, alto);
        colocarFondos();
        this.todosLosPersonajes = gestor.cargarPersonajes();
        this.catalogoArmas = gestor.cargarCatalogoArmas();
        cargarPersonajesEnScrollPanel();
        
        iniciarComodin();
    }
    
    private void iniciarElementos(int newAncho, int newAlto){
        //Menu inicio:
        TituloMenu.setBounds((newAncho - 101)/2, 20, 101, 29);
        Jugar.setBounds((newAncho - 72)/2, 150, 72, 23);
        Salir.setBounds((newAncho - 72)/2, 280, 72, 23);
        //Escoger jugadores:
        seleccionTitle.setBounds((newAncho - 274)/2, 30, 274, 29);
        scrollPersonajes.setBounds((newAncho - 600)/2, 100, 600, 600);
        btnSeleccionarPersonajes.setBounds((newAncho - 191)/2, 720, 191, 27);
        //Mapa:
        Ranking.setBounds(0, 0, 200, 230);
        Against.setBounds(0, 230, 200, 230);
        Status.setBounds(0, 460, 200, 230);
        consola.setBounds(0, 690, 1500, 77);
        attacked.setBounds(200, 0, 400, 345);
        attack.setBounds(200, 345, 400, 345);
        team.setBounds(600, 0, 600, 690);
        bitacoraScroll.setBounds(1200, 0, 295, 345);
        comodin.setBounds(1200, 345, 300, 345);
        bitacora.setLineWrap(true);
        bitacora.setWrapStyleWord(true);
        //Mapa - team:
        yourTeam.setBounds(10, 10, 79, 18);
        teamSeleccionado.setBounds(0, 30, 650, 330);
        armasPorPersonaje.setBounds(0, 360, 650, 330);
        //Mapa - armas:
        nombreVidaPersonaje.setBounds(10, 10, 300, 15);
        arma1.setBounds(10, 36, 600, 15);
        arma2.setBounds(10, 96, 600, 15);
        arma3.setBounds(10, 162, 600, 15);
        arma4.setBounds(10, 228, 600, 15);
        arma5.setBounds(10, 294, 600, 15);
        //Mapa - comodin:
        contenedorComodin.setBounds((300 - 250)/2, 0, 250, 300);
        tempComodin.setBounds((300 - 100)/2, 315, 100, 15);
        //Armas:
        TituloArmas.setBounds((newAncho - 187)/2, 30, 187, 29);
        SeleccionarArmas.setBounds((newAncho - 220)/2, 720, 220, 30);
        scrollArmas.setBounds((newAncho - 600)/2, 100, 600, 600);
        personajeArmas.setBounds((newAncho - 300)/2, 70, 300, 18);
        //Lobby:
        lobbyScroll.setBounds(10, 10, 600, 600);
        Listo.setBounds(1280, 720, 75, 25);
        
    }
    
    public void iniciarComodin(){
        JPanelImage miImagen = new JPanelImage(contenedorComodin,"/Imagenes/Comodin/ComodinNeutral.png");
        contenedorComodin.removeAll();
        contenedorComodin.add(miImagen);
        contenedorComodin.repaint();
        contenedorComodin.getParent().repaint();
        
        comodinTipo = true;
        comodinDesbloqueado = false;
        comodinUsado = false;
        temporizadorComodin();
    }
    
    public void actualizarComodin(){
        String rutaNuevoComodin;
        if(comodinTipo){
            rutaNuevoComodin = "/Imagenes/Comodin/ComodinPositivo.png";
        }else{
            rutaNuevoComodin = "/Imagenes/Comodin/ComodinNegativo.png";
        }
        JPanelImage miImagen = new JPanelImage(contenedorComodin, rutaNuevoComodin);
        contenedorComodin.removeAll();
        contenedorComodin.add(miImagen);
        contenedorComodin.repaint();
        contenedorComodin.getParent().repaint();
        comodinUsado = true;
    }
    
    public void escogerComodin(){
        int tipo = random.nextInt(2);
        if(tipo == 1){
            comodinTipo = true;
        }else{
            comodinTipo = false;
        }
    }
    
    public boolean getComodinTipo(){
        return comodinTipo;
    }
    
    public boolean getComodinDesbloqueado(){
        return comodinDesbloqueado;
    }
    
    public void setComodinDesbloqueado(boolean comodinDesbloqueado){
        this.comodinDesbloqueado = comodinDesbloqueado;
    }

    public boolean isComodinUsado() {
        return comodinUsado;
    }
    
    public void temporizadorComodin(){
        this.tiempoRestanteSegundos = 1 * 60;
        //comodinDesbloqueado = false;
        
        tempComodin.setText(formatearTiempo(this.tiempoRestanteSegundos));
        if (this.swingTimer != null && this.swingTimer.isRunning()) {
            this.swingTimer.stop();
        }
        this.swingTimer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                tiempoRestanteSegundos--; 

                tempComodin.setText(formatearTiempo(tiempoRestanteSegundos)); 

                if (tiempoRestanteSegundos <= 0) {
                    swingTimer.stop(); 
                    tempComodin.setText("¡DISPONIBLE!");
                    comodinDesbloqueado = true;
                    System.out.println(comodinDesbloqueado);
                    escogerComodin();
                }
            }
        });
    
        this.swingTimer.start();
    }
    
    private String formatearTiempo(int totalSegundos) {
        int minutos = totalSegundos / 60;
        int segundos = totalSegundos % 60;
        return String.format("%d:%02d", minutos, segundos);
    }
    
    private void colocarFondos(){
        JPanelImage DeadWoods = new JPanelImage(MenuInicio,"/Imagenes/Fondos/DeadWoods.jpg");
        JPanelImage Pergamino = new JPanelImage(MenuInicio,"/Imagenes/Fondos/Pergamino.jpg");
        MenuInicio.add(DeadWoods).repaint();
        SeleccionPersonajesMenu.add(Pergamino).repaint();
    }
    
    private void colocarFondosArmas(int indexArma){
        // Si ya existe un fondo, eliminarlo
        if (fondoActual != null) {
            MenuArmas.remove(fondoActual);
        }

        String tipo = heroesElegidos.get(indexArma).getTipo().toLowerCase();
        System.out.println(tipo);

        switch (tipo) {
            case "fuego":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Fuego.jpeg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "aire":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Aire.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "agua":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Agua.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "magia blanca":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/MagiaBlanca.jpg");
                TituloArmas.setForeground(Color.black);
                personajeArmas.setForeground(Color.black);
                break;
            case "magia negra":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/MagiaNegra.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "electricidad":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Electricidad.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "hielo":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Hielo.jpg");
                TituloArmas.setForeground(Color.black);
                personajeArmas.setForeground(Color.black);
                break;
            case "acid":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Acid.png");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "espiritualidad":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Espiritualidad.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            case "hierro":
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Hierro.jpg");
                TituloArmas.setForeground(Color.white);
                personajeArmas.setForeground(Color.white);
                break;
            default:
                fondoActual = new JPanelImage(MenuInicio, "/Imagenes/Fondos/Pergamino.jpg");
                break;
        }


        MenuArmas.add(fondoActual, 0);
        MenuArmas.setComponentZOrder(fondoActual, MenuArmas.getComponentCount() - 1);

        MenuArmas.revalidate();
        MenuArmas.repaint();
    }
    
    private void cargarPersonajesEnScrollPanel(){
        try{
            for(Personajes p : this.todosLosPersonajes){
                JPanel panelPersonaje = new JPanel();
                panelPersonaje.setOpaque(false);
                panelPersonaje.setBorder(null);
                panelPersonaje.setLayout(new javax.swing.BoxLayout(panelPersonaje, javax.swing.BoxLayout.Y_AXIS));
                
                
                JLabel lblNombre = new JLabel("Nombre: " + p.getNombre());
                lblNombre.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(p.getImagen()));
                ImageIcon iconoRedimensionado;
                
                JLabel lblIcono = new JLabel();             
                if(p.getImagen().contains(".gif")){
                    iconoRedimensionado = iconoOriginal;
                }else{
                    iconoRedimensionado = new ImageIcon(iconoOriginal.getImage().getScaledInstance(125, 125, java.awt.Image.SCALE_SMOOTH));
                }

                lblIcono.setIcon(iconoRedimensionado);
                lblIcono.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                
                JLabel lblTipo = new JLabel("Tipo: " + p.getTipo());
                lblTipo.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                
                
                panelPersonaje.add(lblNombre);
                panelPersonaje.add(Box.createVerticalStrut(10));
                panelPersonaje.add(lblIcono);
                panelPersonaje.add(Box.createVerticalStrut(10));
                panelPersonaje.add(lblTipo);
                
                panelPersonaje.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        manejarSeleccion(panelPersonaje, p.getNombre());
                    }
                });
                
                scrollPersonajesPanel.add(panelPersonaje);
                
            }
            scrollPersonajesPanel.revalidate();
            scrollPersonajesPanel.repaint();
            
        }catch(Exception e){
            System.err.println("Error al cargar personajes en la interfaz: " + e.getMessage());
        }
    }
    
    private void manejarSeleccion(JComponent componenteActual, String idPersonaje) {
        if (componentesSeleccionados.containsKey(idPersonaje)) {
            componenteActual.setBorder(BORDE_NORMAL); 
            idsSeleccionados.remove(idPersonaje);
            componentesSeleccionados.remove(idPersonaje);

        } 
        else {
            if (idsSeleccionados.size() < 3) {

                componenteActual.setBorder(BORDE_SELECCION); 
                idsSeleccionados.add(idPersonaje);
                componentesSeleccionados.put(idPersonaje, componenteActual); 

            } else {
                JOptionPane.showMessageDialog(this, "Solo puedes seleccionar un máximo de 3 personajes.");
            }
        }
        System.out.println("Personajes seleccionados: " + idsSeleccionados.size());
    }
    
    
    private void cargarArmasenScroll(int index){
        idsSeleccionadosArmas.clear();
        componentesSeleccionadosArmas.clear();
        panelArmas.removeAll();
        panelArmas.revalidate();
        panelArmas.repaint();
 
        personajeArmas.setText("Nombre: " + heroesElegidos.get(index).getNombre());
        String tipoArma = heroesElegidos.get(index).getTipo();
        List<Armas> armasTipo = catalogoArmas.get(tipoArma);
        try{
            for(Armas a : armasTipo){
                JPanel armas = new JPanel();
                armas.setOpaque(false);
                armas.setBorder(null);
                armas.setLayout(new javax.swing.BoxLayout(armas, javax.swing.BoxLayout.Y_AXIS));
                
                JLabel lblNombre = new JLabel(a.getNombre());
                lblNombre.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                
                ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(a.getImagen()));
                ImageIcon iconoRedimensionado;
                
                JLabel lblIcono = new JLabel();             
                if(a.getImagen().contains(".gif")){
                    iconoRedimensionado = iconoOriginal;
                }else{
                    iconoRedimensionado = new ImageIcon(iconoOriginal.getImage().getScaledInstance(125, 125, java.awt.Image.SCALE_SMOOTH));
                }

                lblIcono.setIcon(iconoRedimensionado);
                lblIcono.setAlignmentX(JComponent.CENTER_ALIGNMENT);
                
                armas.add(lblNombre);
                armas.add(lblIcono);
                
                armas.addMouseListener(new java.awt.event.MouseAdapter() {
                    @Override
                    public void mouseClicked(java.awt.event.MouseEvent evt) {
                        manejarSeleccionArmas(armas, a.getNombre());
                    }
                });
                
                panelArmas.add(armas);
            }
            
            panelArmas.revalidate();
            panelArmas.repaint();
            
        }catch(Exception e){
            System.err.println("Error al cargar armas en la interfaz: " + e.getMessage());
        }
    }
    
    private void manejarSeleccionArmas(JComponent componenteActual, String idArma){
        if (componentesSeleccionadosArmas.containsKey(idArma)) {
            componenteActual.setBorder(BORDE_NORMAL); 
            idsSeleccionadosArmas.remove(idArma);
            componentesSeleccionadosArmas.remove(idArma);

        } 
        else {
            if (idsSeleccionadosArmas.size() < 5) {

                componenteActual.setBorder(BORDE_SELECCION); 
                idsSeleccionadosArmas.add(idArma);
                componentesSeleccionadosArmas.put(idArma, componenteActual); 

            } else {
                JOptionPane.showMessageDialog(this, "Solo puedes seleccionar un máximo de 5 armas.");
            }
        }
        System.out.println("Armas seleccionados: " + idsSeleccionadosArmas.size());
    }
    
    private void caragrTeam(){
        boolean primerHeroe = true;
        try{
            for(Personajes p : heroesElegidos){
                JPanel panelPersonaje = new JPanel();
                panelPersonaje.setOpaque(false);
                panelPersonaje.setBorder(null);
                panelPersonaje.setLayout(new javax.swing.BoxLayout(panelPersonaje, javax.swing.BoxLayout.Y_AXIS));
                
                JPanelImage2 miImagen = new JPanelImage2(p.getImagen());
                
                miImagen.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    logicaSeleccionMapaTeam(p, panelPersonaje);
                }
            });
                
                panelPersonaje.add(miImagen);
                if(primerHeroe){
                    panelPersonaje.setBorder(bordeSeleccionado);
                    panelPersonaje.revalidate();
                    panelPersonaje.repaint();
                    panelSeleccionadoAnterior = panelPersonaje;
                    primerHeroe = false;
                }

                teamSeleccionado.add(panelPersonaje);
            }
            
            teamSeleccionado.revalidate();
            teamSeleccionado.repaint();
            
        }catch(Exception e){
            
        }
    }
    
    private void logicaSeleccionMapaTeam(Personajes heroeSeleccionado, JPanel panelDelHeroe){
        if (panelSeleccionadoAnterior != null) {
            panelSeleccionadoAnterior.setBorder(bordeNormal);
            panelSeleccionadoAnterior.revalidate();
            panelSeleccionadoAnterior.repaint();
        }

        panelDelHeroe.setBorder(bordeSeleccionado);
        panelDelHeroe.revalidate();
        panelDelHeroe.repaint();

        panelSeleccionadoAnterior = panelDelHeroe;
        cargarDamages(heroeSeleccionado);
    }
    
    
    private void cargarDamages(Personajes p){
        try {
            List<List<Integer>> a = p.getDamages();

            List<Integer> damageArma1 = a.get(0);
            
            nombreVidaPersonaje.setText(p.getNombre() + " " + p.getVida());
            arma1.setText(p.getArmas().get(0).getNombre() + ": "
                          + damageArma1.get(0) + " "
                          + damageArma1.get(1) + " "
                          + damageArma1.get(2) + " "
                          + damageArma1.get(3) + " "
                          + damageArma1.get(4) + " "
                          + damageArma1.get(5) + " "
                          + damageArma1.get(6) + " "
                          + damageArma1.get(7) + " "
                          + damageArma1.get(8) + " "
                          + damageArma1.get(9)); 

            // --- Arma 2 ---
            if (p.getArmas().size() > 1 && a.size() > 1) {
                List<Integer> damageArma2 = a.get(1);
                arma2.setText(p.getArmas().get(1).getNombre() + ": "
                              + damageArma2.get(0) + " "
                              + damageArma2.get(1) + " "
                              + damageArma2.get(2) + " "
                              + damageArma2.get(3) + " "
                              + damageArma2.get(4) + " "
                              + damageArma2.get(5) + " "
                              + damageArma2.get(6) + " "
                              + damageArma2.get(7) + " "
                              + damageArma2.get(8) + " "
                              + damageArma2.get(9));
            } else { arma2.setText(""); }

            // --- Arma 3 ---
            if (p.getArmas().size() > 2 && a.size() > 2) {
                List<Integer> damageArma3 = a.get(2);
                arma3.setText(p.getArmas().get(2).getNombre() + ": "
                              + damageArma3.get(0) + " "
                              + damageArma3.get(1) + " "
                              + damageArma3.get(2) + " "
                              + damageArma3.get(3) + " "
                              + damageArma3.get(4) + " "
                              + damageArma3.get(5) + " "
                              + damageArma3.get(6) + " "
                              + damageArma3.get(7) + " "
                              + damageArma3.get(8) + " "
                              + damageArma3.get(9));
            } else { arma3.setText(""); }

            // --- Arma 4 ---
            if (p.getArmas().size() > 3 && a.size() > 3) {
                List<Integer> damageArma4 = a.get(3);
                arma4.setText(p.getArmas().get(3).getNombre() + ": "
                              + damageArma4.get(0) + " "
                              + damageArma4.get(1) + " "
                              + damageArma4.get(2) + " "
                              + damageArma4.get(3) + " "
                              + damageArma4.get(4) + " "
                              + damageArma4.get(5) + " "
                              + damageArma4.get(6) + " "
                              + damageArma4.get(7) + " "
                              + damageArma4.get(8) + " "
                              + damageArma4.get(9));
            } else { arma4.setText(""); }

            // --- Arma 5 ---
            if (p.getArmas().size() > 4 && a.size() > 4) {
                List<Integer> damageArma5 = a.get(4);
                arma5.setText(p.getArmas().get(4).getNombre() + ": "
                              + damageArma5.get(0) + " "
                              + damageArma5.get(1) + " "
                              + damageArma5.get(2) + " "
                              + damageArma5.get(3) + " "
                              + damageArma5.get(4) + " "
                              + damageArma5.get(5) + " "
                              + damageArma5.get(6) + " "
                              + damageArma5.get(7) + " "
                              + damageArma5.get(8) + " "
                              + damageArma5.get(9));
            } else { arma5.setText(""); }

        } catch (Exception e) {
            System.err.println("Error al cargar daños: " + e.getMessage());
        }
    }
    
    private void configurarPantallaCompleta() {
        InputMap inputMap = getRootPane().getInputMap(javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();

        KeyStroke f11Key = KeyStroke.getKeyStroke("F11");

        inputMap.put(f11Key, "toggleFullScreen");

        actionMap.put("toggleFullScreen", new AbstractAction() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                toggleFullScreen(); 
            }
        });
    }
    
    private void toggleFullScreen() {
        if (graphicsDevice.isFullScreenSupported()) {
            if (!isFullScreen) {
                this.setVisible(false); 
                this.dispose(); 

                this.setUndecorated(true); 

                graphicsDevice.setFullScreenWindow(this);

                this.setVisible(true);

                isFullScreen = true;

                int x = this.getWidth();
                int y = this.getHeight();
                iniciarElementos(x,y);

            } else {
                this.setVisible(true); 
                graphicsDevice.setFullScreenWindow(null);
                this.dispose(); 

                this.setUndecorated(false);
                this.setSize(this.ancho, this.alto); 
                System.out.println(this.getWidth());
                System.out.println(this.getHeight());
                this.setLocationRelativeTo(null); 

                this.setVisible(true);

                isFullScreen = false;
                iniciarElementos(this.ancho, this.alto);
            }

        } else {
            System.err.println("El modo de pantalla completa no es compatible con este dispositivo.");
        }
    }
    
    public void startGame(){
        cardLayout = (CardLayout) (getContentPane().getLayout());
        cardLayout.show(getContentPane(), "card4");
    }
    
    public List<Personajes> obtenerPersonajesSeleccionados() {
    
        List<Personajes> listaFinal = new ArrayList<>();

        for (Personajes p : this.todosLosPersonajes) {

            if (idsSeleccionados.contains(p.getNombre())) {
                listaFinal.add(p);
            }
        }

        return listaFinal;
    }
    
    private List<Armas> obtenerArmasSeleccionados(){
        List<Armas> listaFinal = new ArrayList<>();
        for(List<Armas> listaArmasPorTipo : this.catalogoArmas.values()){
            for(Armas a : listaArmasPorTipo){
                if(idsSeleccionadosArmas.contains(a.getNombre())){
                listaFinal.add(a);
                }
            }
        }
        return listaFinal;
    }
    
    private void handleCommandInput(java.awt.event.ActionEvent evt) {
      String msg =  consola.getText().trim() + " " + comodinTipo + " " + comodinDesbloqueado + " " + comodinUsado + " " +"\"" + gestor.obtenerInfoJugador("Justin") + "\"";
        System.out.println(gestor.obtenerInfoJugador("Justin"));
      consola.setText("");
      if (msg.length()>0){
          String args[] = CommandUtil.tokenizerArgs(msg);
          if (args.length > 0){
              Command comando = CommandFactory.getCommand(args);
              if (comando != null){
                  try {
                      cliente.objectSender.writeObject(comando);
                  } catch (IOException ex) {

                  }
              }else{
                  System.out.println("Error: comando desconocido");
              }
          }
      }
    }
      
    
    public void writeLobby(String msg){
        lobbyText.append(msg + "\n");
    }
    
    public void clearLobby(){
        lobbyText.setText("");
    }
    
    public void writeBitacora(String msg){
        bitacora.append(msg + "\n");
    }
    
    public void writeConsola(String msg){
        consola.setText(msg);
    }
    
    public void writeLog(String msg, String msg2){
        log1.setText(msg);
        logArma.setText(msg2);
    }
    
    public void writeLog2(String msg, String msg2){
        log2.setText(msg);
        log2Arma.setText(msg2);
    }
    
    private void iniciarServer(){
        Server server = new Server(this);
        cliente = new Client(this, jugador, heroesElegidos, 35500, "localhost");
        logJugador = new LogPartida("Justin");
    }
    
    //Logica del ganador y perdedor:
    public void mostrarVictoria(String ganador) {
        JDialog dialog = new JDialog(this, "Victoria", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel lbl = new JLabel("¡" + ganador + " ha ganado!", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btn = new JButton("Aceptar");
        btn.addActionListener(e -> dialog.dispose());

        dialog.add(lbl, BorderLayout.CENTER);
        dialog.add(btn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    
    public void mostrarDerrota() {
        JDialog dialog = new JDialog(this, "Derrota", true);
        dialog.setSize(350, 180);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout());

        JLabel lbl = new JLabel("Has sido eliminado️", SwingConstants.CENTER);
        lbl.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btn = new JButton("Aceptar");
        btn.addActionListener(e -> dialog.dispose());

        dialog.add(lbl, BorderLayout.CENTER);
        dialog.add(btn, BorderLayout.SOUTH);

        dialog.setVisible(true);
    }
    public void actualizarHUD(String jugador){
        StatusText.setText(gestor.obtenerInfoJugador(jugador));
    }
    
    public void actualizarHUDEnemigo(String msg){
        AgainstText.setText(msg);
    }
    
    public boolean haMuerto(){
        for(Personajes p : heroesElegidos){
            if(p.getVida() > 0){
                return false;
            }
        }
        
        return true;
    }
    
    public boolean recibirAtaque(int damage){
        List<Personajes> heroesVivos = new ArrayList<>();;
        for(Personajes p : heroesElegidos){
            if(p.getVida() > 0){
                heroesVivos.add(p);
            }
        }
        
        int indexDano = random.nextInt(heroesVivos.size());
        heroesVivos.get(indexDano).recibirDano(damage);
        
        if(nombreVidaPersonaje.getText().contains(heroesVivos.get(indexDano).getNombre())){
            nombreVidaPersonaje.setText(heroesVivos.get(indexDano).getNombre() + " " + heroesVivos.get(indexDano).getVida());
        }
        return true;
    }
    
    //Comandos:
    public void verJugadores(List<Personajes> heroesJugador){

        JFrame ventanaHeroes = new JFrame("Detalles de Héroes del Jugador");
        ventanaHeroes.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // Cierra solo esta ventana
        ventanaHeroes.setSize(800, 600);
        ventanaHeroes.setLayout(new BorderLayout());

        JPanel panelContenedor = new JPanel();
        panelContenedor.setLayout(new BoxLayout(panelContenedor, BoxLayout.X_AXIS));

        for(Personajes p : heroesJugador){
            JPanel panelPersonaje = new JPanel();
            panelPersonaje.setOpaque(false);
            panelPersonaje.setBorder(null);
            panelPersonaje.setLayout(new javax.swing.BoxLayout(panelPersonaje, javax.swing.BoxLayout.Y_AXIS));


            JLabel lblNombre = new JLabel("Nombre: " + p.getNombre());
            lblNombre.setAlignmentX(JComponent.CENTER_ALIGNMENT);

            ImageIcon iconoOriginal = new ImageIcon(getClass().getResource(p.getImagen()));
            ImageIcon iconoRedimensionado;

            JLabel lblIcono = new JLabel();             
            if(p.getImagen().contains(".gif")){
                iconoRedimensionado = iconoOriginal;
            }else{
                iconoRedimensionado = new ImageIcon(iconoOriginal.getImage().getScaledInstance(125, 125, java.awt.Image.SCALE_SMOOTH));
            }

            lblIcono.setIcon(iconoRedimensionado);
            lblIcono.setAlignmentX(JComponent.CENTER_ALIGNMENT);

            JLabel lblTipo = new JLabel("Tipo: " + p.getTipo());
            lblTipo.setAlignmentX(JComponent.CENTER_ALIGNMENT);


            panelPersonaje.add(lblNombre);
            panelPersonaje.add(Box.createVerticalStrut(10));
            panelPersonaje.add(lblIcono);
            panelPersonaje.add(Box.createVerticalStrut(10));
            panelPersonaje.add(lblTipo);

            panelContenedor.add(panelPersonaje);
        }

        JScrollPane scrollPane = new JScrollPane(panelContenedor);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        
        ventanaHeroes.add(scrollPane, BorderLayout.CENTER);
        ventanaHeroes.setLocationRelativeTo(null); 
        ventanaHeroes.setVisible(true);
    }
    
    public void VerLog(String nombre){
        LogPartida.abrirVentanaLogReciente(nombre);
    }
    
    
    public void guardarLogsJugador(String comando, String parametros, String resultado){
        logJugador.registrarComando(comando, parametros, resultado);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        MenuInicio = new javax.swing.JPanel();
        TituloMenu = new javax.swing.JLabel();
        Jugar = new javax.swing.JButton();
        Salir = new javax.swing.JButton();
        SeleccionPersonajesMenu = new javax.swing.JPanel();
        seleccionTitle = new javax.swing.JLabel();
        scrollPersonajes = new javax.swing.JScrollPane();
        scrollPersonajesPanel = new javax.swing.JPanel();
        btnSeleccionarPersonajes = new javax.swing.JButton();
        Mapa = new javax.swing.JPanel();
        Ranking = new javax.swing.JScrollPane();
        rankingText = new javax.swing.JTextArea();
        Against = new javax.swing.JScrollPane();
        AgainstText = new javax.swing.JTextArea();
        Status = new javax.swing.JScrollPane();
        StatusText = new javax.swing.JTextArea();
        attacked = new javax.swing.JPanel();
        log1 = new javax.swing.JLabel();
        logArma = new javax.swing.JLabel();
        attack = new javax.swing.JPanel();
        log2 = new javax.swing.JLabel();
        log2Arma = new javax.swing.JLabel();
        team = new javax.swing.JPanel();
        yourTeam = new javax.swing.JLabel();
        teamSeleccionado = new javax.swing.JPanel();
        armasPorPersonaje = new javax.swing.JPanel();
        arma1 = new javax.swing.JLabel();
        arma2 = new javax.swing.JLabel();
        arma3 = new javax.swing.JLabel();
        arma4 = new javax.swing.JLabel();
        arma5 = new javax.swing.JLabel();
        nombreVidaPersonaje = new javax.swing.JLabel();
        consola = new javax.swing.JTextField();
        bitacoraScroll = new javax.swing.JScrollPane();
        bitacora = new javax.swing.JTextArea();
        comodin = new javax.swing.JPanel();
        contenedorComodin = new javax.swing.JPanel();
        tempComodin = new javax.swing.JLabel();
        MenuArmas = new javax.swing.JPanel();
        TituloArmas = new javax.swing.JLabel();
        SeleccionarArmas = new javax.swing.JButton();
        scrollArmas = new javax.swing.JScrollPane();
        panelArmas = new javax.swing.JPanel();
        personajeArmas = new javax.swing.JLabel();
        lobby = new javax.swing.JPanel();
        lobbyScroll = new javax.swing.JScrollPane();
        lobbyText = new javax.swing.JTextArea();
        Listo = new javax.swing.JButton();
        Partidas = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        CrearPartida = new javax.swing.JButton();
        BuscarPartida = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 800));
        setSize(new java.awt.Dimension(400, 300));
        getContentPane().setLayout(new java.awt.CardLayout());

        MenuInicio.setMinimumSize(new java.awt.Dimension(1200, 800));
        MenuInicio.setPreferredSize(new java.awt.Dimension(1500, 800));
        MenuInicio.setLayout(null);

        TituloMenu.setFont(new java.awt.Font("Unispace", 0, 24)); // NOI18N
        TituloMenu.setForeground(new java.awt.Color(255, 255, 255));
        TituloMenu.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        TituloMenu.setText("Sockets");
        MenuInicio.add(TituloMenu);
        TituloMenu.setBounds(180, 20, 110, 29);

        Jugar.setText("Jugar");
        Jugar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JugarActionPerformed(evt);
            }
        });
        MenuInicio.add(Jugar);
        Jugar.setBounds(160, 80, 72, 23);

        Salir.setText("Salir");
        Salir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirActionPerformed(evt);
            }
        });
        MenuInicio.add(Salir);
        Salir.setBounds(160, 150, 72, 23);

        getContentPane().add(MenuInicio, "card2");

        SeleccionPersonajesMenu.setLayout(null);

        seleccionTitle.setFont(new java.awt.Font("Unispace", 0, 24)); // NOI18N
        seleccionTitle.setText("Selección jugadores");
        SeleccionPersonajesMenu.add(seleccionTitle);
        seleccionTitle.setBounds(490, 30, 280, 29);

        scrollPersonajes.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPersonajes.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        scrollPersonajesPanel.setOpaque(false);
        scrollPersonajesPanel.setLayout(new java.awt.GridLayout(0, 3, 5, 25));
        scrollPersonajes.setViewportView(scrollPersonajesPanel);

        SeleccionPersonajesMenu.add(scrollPersonajes);
        scrollPersonajes.setBounds(200, 110, 800, 570);

        btnSeleccionarPersonajes.setFont(new java.awt.Font("Segoe UI Black", 0, 14)); // NOI18N
        btnSeleccionarPersonajes.setText("Seleccionar personajes");
        btnSeleccionarPersonajes.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarPersonajesActionPerformed(evt);
            }
        });
        SeleccionPersonajesMenu.add(btnSeleccionarPersonajes);
        btnSeleccionarPersonajes.setBounds(530, 750, 200, 27);

        getContentPane().add(SeleccionPersonajesMenu, "card3");

        Mapa.setLayout(null);

        Ranking.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Ranking.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Ranking.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        rankingText.setEditable(false);
        rankingText.setColumns(20);
        rankingText.setRows(5);
        Ranking.setViewportView(rankingText);

        Mapa.add(Ranking);
        Ranking.setBounds(0, 0, 250, 212);

        Against.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Against.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Against.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        AgainstText.setEditable(false);
        AgainstText.setColumns(20);
        AgainstText.setRows(5);
        Against.setViewportView(AgainstText);

        Mapa.add(Against);
        Against.setBounds(0, 218, 250, 248);

        Status.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        Status.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        Status.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        StatusText.setEditable(false);
        StatusText.setColumns(20);
        StatusText.setRows(5);
        Status.setViewportView(StatusText);

        Mapa.add(Status);
        Status.setBounds(0, 473, 250, 176);

        attacked.setBackground(new java.awt.Color(102, 102, 255));
        attacked.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        attacked.setForeground(new java.awt.Color(0, 0, 0));

        log1.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N
        log1.setText("No has sido atacado");

        logArma.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N

        javax.swing.GroupLayout attackedLayout = new javax.swing.GroupLayout(attacked);
        attacked.setLayout(attackedLayout);
        attackedLayout.setHorizontalGroup(
            attackedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attackedLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(attackedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(log1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logArma, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(404, Short.MAX_VALUE))
        );
        attackedLayout.setVerticalGroup(
            attackedLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attackedLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(log1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logArma, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(301, Short.MAX_VALUE))
        );

        Mapa.add(attacked);
        attacked.setBounds(256, 0, 587, 358);

        attack.setBackground(new java.awt.Color(102, 102, 255));
        attack.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        attack.setForeground(new java.awt.Color(0, 0, 0));

        log2.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        log2.setText("No has atacado");

        log2Arma.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N

        javax.swing.GroupLayout attackLayout = new javax.swing.GroupLayout(attack);
        attack.setLayout(attackLayout);
        attackLayout.setHorizontalGroup(
            attackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attackLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(attackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(log2Arma, javax.swing.GroupLayout.PREFERRED_SIZE, 305, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(log2, javax.swing.GroupLayout.PREFERRED_SIZE, 449, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        attackLayout.setVerticalGroup(
            attackLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(attackLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(log2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(log2Arma, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(211, Short.MAX_VALUE))
        );

        Mapa.add(attack);
        attack.setBounds(256, 389, 587, 260);

        team.setBackground(new java.awt.Color(102, 102, 255));
        team.setLayout(null);

        yourTeam.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N
        yourTeam.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        yourTeam.setText("Tu equipo");
        team.add(yourTeam);
        yourTeam.setBounds(6, 6, 79, 18);

        teamSeleccionado.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        teamSeleccionado.setLayout(new java.awt.GridLayout(0, 3));
        team.add(teamSeleccionado);
        teamSeleccionado.setBounds(6, 42, 305, 2);

        armasPorPersonaje.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        armasPorPersonaje.setLayout(null);

        arma1.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        arma1.setText("jLabel1");
        armasPorPersonaje.add(arma1);
        arma1.setBounds(21, 25, 100, 15);

        arma2.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        arma2.setText("jLabel2");
        armasPorPersonaje.add(arma2);
        arma2.setBounds(21, 75, 100, 15);

        arma3.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        arma3.setText("jLabel3");
        armasPorPersonaje.add(arma3);
        arma3.setBounds(21, 132, 100, 15);

        arma4.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        arma4.setText("jLabel4");
        armasPorPersonaje.add(arma4);
        arma4.setBounds(21, 176, 100, 15);

        arma5.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        arma5.setText("jLabel5");
        armasPorPersonaje.add(arma5);
        arma5.setBounds(21, 229, 100, 15);

        nombreVidaPersonaje.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N
        nombreVidaPersonaje.setText("Hola xd");
        armasPorPersonaje.add(nombreVidaPersonaje);
        nombreVidaPersonaje.setBounds(0, 0, 62, 18);

        team.add(armasPorPersonaje);
        armasPorPersonaje.setBounds(6, 278, 305, 0);

        Mapa.add(team);
        team.setBounds(883, 0, 0, 649);
        Mapa.add(consola);
        consola.setBounds(10, 660, 850, 140);

        bitacoraScroll.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        bitacora.setColumns(20);
        bitacora.setRows(5);
        bitacoraScroll.setViewportView(bitacora);

        Mapa.add(bitacoraScroll);
        bitacoraScroll.setBounds(860, 10, 330, 340);

        comodin.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        comodin.setLayout(null);

        javax.swing.GroupLayout contenedorComodinLayout = new javax.swing.GroupLayout(contenedorComodin);
        contenedorComodin.setLayout(contenedorComodinLayout);
        contenedorComodinLayout.setHorizontalGroup(
            contenedorComodinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 243, Short.MAX_VALUE)
        );
        contenedorComodinLayout.setVerticalGroup(
            contenedorComodinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 151, Short.MAX_VALUE)
        );

        comodin.add(contenedorComodin);
        contenedorComodin.setBounds(40, 20, 243, 151);

        tempComodin.setFont(new java.awt.Font("Unispace", 0, 12)); // NOI18N
        tempComodin.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tempComodin.setText("jLabel2");
        comodin.add(tempComodin);
        tempComodin.setBounds(1010, 640, 50, 15);

        Mapa.add(comodin);
        comodin.setBounds(870, 390, 320, 220);

        getContentPane().add(Mapa, "card4");

        MenuArmas.setLayout(null);

        TituloArmas.setFont(new java.awt.Font("Unispace", 0, 24)); // NOI18N
        TituloArmas.setText("Escoger armas");
        MenuArmas.add(TituloArmas);
        TituloArmas.setBounds(454, 59, 187, 29);

        SeleccionarArmas.setFont(new java.awt.Font("Unispace", 0, 18)); // NOI18N
        SeleccionarArmas.setText("Seleccionar armas");
        SeleccionarArmas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SeleccionarArmasActionPerformed(evt);
            }
        });
        MenuArmas.add(SeleccionarArmas);
        SeleccionarArmas.setBounds(510, 760, 230, 30);

        scrollArmas.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollArmas.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);

        panelArmas.setLayout(new java.awt.GridLayout(0, 3, 15, 15));
        scrollArmas.setViewportView(panelArmas);

        MenuArmas.add(scrollArmas);
        scrollArmas.setBounds(320, 130, 730, 600);

        personajeArmas.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N
        personajeArmas.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        personajeArmas.setText("Nombre: Juan");
        MenuArmas.add(personajeArmas);
        personajeArmas.setBounds(560, 100, 106, 18);

        getContentPane().add(MenuArmas, "card5");

        lobby.setLayout(null);

        lobbyText.setColumns(20);
        lobbyText.setRows(5);
        lobbyScroll.setViewportView(lobbyText);

        lobby.add(lobbyScroll);
        lobbyScroll.setBounds(6, 6, 339, 310);

        Listo.setFont(new java.awt.Font("Unispace", 0, 14)); // NOI18N
        Listo.setText("Listo");
        Listo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ListoActionPerformed(evt);
            }
        });
        lobby.add(Listo);
        Listo.setBounds(1023, 750, 74, 25);

        getContentPane().add(lobby, "card6");

        jLabel1.setText("Partida");

        CrearPartida.setText("Crear partida");
        CrearPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CrearPartidaActionPerformed(evt);
            }
        });

        BuscarPartida.setText("Buscar partida");
        BuscarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BuscarPartidaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout PartidasLayout = new javax.swing.GroupLayout(Partidas);
        Partidas.setLayout(PartidasLayout);
        PartidasLayout.setHorizontalGroup(
            PartidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PartidasLayout.createSequentialGroup()
                .addGap(528, 528, 528)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(PartidasLayout.createSequentialGroup()
                .addGap(163, 163, 163)
                .addComponent(CrearPartida)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 793, Short.MAX_VALUE)
                .addComponent(BuscarPartida)
                .addGap(338, 338, 338))
        );
        PartidasLayout.setVerticalGroup(
            PartidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PartidasLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(136, 136, 136)
                .addGroup(PartidasLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(CrearPartida)
                    .addComponent(BuscarPartida))
                .addContainerGap(585, Short.MAX_VALUE))
        );

        getContentPane().add(Partidas, "card7");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void SalirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_SalirActionPerformed

    private void JugarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JugarActionPerformed
        // TODO add your handling code here:
        cardLayout = (CardLayout) (getContentPane().getLayout());
        cardLayout.show(getContentPane(), "card3");
    }//GEN-LAST:event_JugarActionPerformed

    private void btnSeleccionarPersonajesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarPersonajesActionPerformed
        // TODO add your handling code here:
        if(idsSeleccionados.size() == 3){
          heroesElegidos = obtenerPersonajesSeleccionados();  
        }
        else{
            JOptionPane.showMessageDialog(this, "Faltan personajes por escoger (Deben ser 3)");
            return;
        }
        cargarArmasenScroll(indexArma);
        caragrTeam();
        colocarFondosArmas(indexArma);
        cardLayout = (CardLayout) (getContentPane().getLayout());
        cardLayout.show(getContentPane(), "card5");
    }//GEN-LAST:event_btnSeleccionarPersonajesActionPerformed

    private void SeleccionarArmasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SeleccionarArmasActionPerformed
        // TODO add your handling code here:
        if(idsSeleccionadosArmas.size() == 5){
            heroesElegidos.get(indexArma).setArmas(obtenerArmasSeleccionados());
        }else{
            JOptionPane.showMessageDialog(this, "Faltan armas por escoger (Deben ser 5)");
            return;
        }
        indexArma++;
        if(indexArma > 2){
            for(Personajes p: heroesElegidos){
                p.generarDamage();
            }
            cargarDamages(heroesElegidos.get(0));
            cardLayout = (CardLayout) (getContentPane().getLayout());
            cardLayout.show(getContentPane(), "card7");
        }else{
            colocarFondosArmas(indexArma);
            cargarArmasenScroll(indexArma);
        }
    }//GEN-LAST:event_SeleccionarArmasActionPerformed

    private void ListoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ListoActionPerformed
        try {
            CommandReady ready = new CommandReady(this.getTitle()); 
            cliente.objectSender.writeObject(ready);
            gestor.agregarJugador(cliente.name);
            StatusText.append(gestor.obtenerInfoJugador(cliente.name));
            Listo.setEnabled(false);
            JOptionPane.showMessageDialog(this, "Marcado como listo. Esperando a otros jugadores...");

        } catch (IOException ex) {
            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error al enviar estado de 'listo' al servidor.");
        }
    }//GEN-LAST:event_ListoActionPerformed

    private void CrearPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CrearPartidaActionPerformed
        jugador = JOptionPane.showInputDialog(
            "Por favor, ingresa tu nombre:"
        );
        if (jugador != null && !jugador.trim().isEmpty()) {
            iniciarServer();
        }else{
            return;
        }
    }//GEN-LAST:event_CrearPartidaActionPerformed

    private void BuscarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BuscarPartidaActionPerformed
        String puerto = JOptionPane.showInputDialog("Por favor, ingrese el ID de la partida:");
        int port;
        if(puerto != null && !puerto.trim().isEmpty()){
            port = Integer.parseInt(puerto);
        }else{
            return;
        }
        
        jugador = JOptionPane.showInputDialog(
            "Por favor, ingresa tu nombre:"
        );
        
        if(jugador != null && !jugador.trim().isEmpty()){
            cliente = new Client(this, jugador, heroesElegidos, 35500, "localhost");
            logJugador = new LogPartida(jugador);
        }else{
            return;
        }
        

    }//GEN-LAST:event_BuscarPartidaActionPerformed

    /**
     * @param args the command line arguments
     */
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
        java.awt.EventQueue.invokeLater(() -> {
            try {
                new Juego().setVisible(true);
            } catch (IOException ex) {
                System.getLogger(Juego.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            } catch (LineUnavailableException ex) {
                System.getLogger(Juego.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JScrollPane Against;
    private javax.swing.JTextArea AgainstText;
    private javax.swing.JButton BuscarPartida;
    private javax.swing.JButton CrearPartida;
    private javax.swing.JButton Jugar;
    private javax.swing.JButton Listo;
    private javax.swing.JPanel Mapa;
    private javax.swing.JPanel MenuArmas;
    private javax.swing.JPanel MenuInicio;
    private javax.swing.JPanel Partidas;
    private javax.swing.JScrollPane Ranking;
    private javax.swing.JButton Salir;
    private javax.swing.JPanel SeleccionPersonajesMenu;
    private javax.swing.JButton SeleccionarArmas;
    private javax.swing.JScrollPane Status;
    private javax.swing.JTextArea StatusText;
    private javax.swing.JLabel TituloArmas;
    private javax.swing.JLabel TituloMenu;
    private javax.swing.JLabel arma1;
    private javax.swing.JLabel arma2;
    private javax.swing.JLabel arma3;
    private javax.swing.JLabel arma4;
    private javax.swing.JLabel arma5;
    private javax.swing.JPanel armasPorPersonaje;
    private javax.swing.JPanel attack;
    private javax.swing.JPanel attacked;
    private javax.swing.JTextArea bitacora;
    private javax.swing.JScrollPane bitacoraScroll;
    private javax.swing.JButton btnSeleccionarPersonajes;
    private javax.swing.JPanel comodin;
    private javax.swing.JTextField consola;
    private javax.swing.JPanel contenedorComodin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel lobby;
    private javax.swing.JScrollPane lobbyScroll;
    private javax.swing.JTextArea lobbyText;
    private javax.swing.JLabel log1;
    private javax.swing.JLabel log2;
    private javax.swing.JLabel log2Arma;
    private javax.swing.JLabel logArma;
    private javax.swing.JLabel nombreVidaPersonaje;
    private javax.swing.JPanel panelArmas;
    private javax.swing.JLabel personajeArmas;
    private javax.swing.JTextArea rankingText;
    private javax.swing.JScrollPane scrollArmas;
    private javax.swing.JScrollPane scrollPersonajes;
    private javax.swing.JPanel scrollPersonajesPanel;
    private javax.swing.JLabel seleccionTitle;
    private javax.swing.JPanel team;
    private javax.swing.JPanel teamSeleccionado;
    private javax.swing.JLabel tempComodin;
    private javax.swing.JLabel yourTeam;
    // End of variables declaration//GEN-END:variables
}
