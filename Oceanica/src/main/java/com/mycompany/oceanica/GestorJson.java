/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.oceanica;

import java.io.*;
import java.util.*;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
/**
 *
 * @author lacay
 */
public class GestorJson {
    private static final String ARCHIVO = "/Personajes.json";
    
    public static List<Personaje> cargarPersonajes() throws IOException{
        
        InputStream stream = GestorJson.class.getResourceAsStream(ARCHIVO);
        
        if (stream == null) {
            System.err.println("Error: Archivo no encontrado en el classpath: " + ARCHIVO);
            return Collections.emptyList();
        }
        
        try(Reader reader = new InputStreamReader(stream)){

            Gson gson = new Gson();
        
            Type listType = new TypeToken<List<Personaje>>() {}.getType();
            List<Personaje> listaPersonajes = gson.fromJson(reader, listType);
            return (listaPersonajes != null) ? listaPersonajes : Collections.emptyList();
        
        }catch(Exception e){
            System.err.println("Error al procesar el JSON: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
    }      
}
