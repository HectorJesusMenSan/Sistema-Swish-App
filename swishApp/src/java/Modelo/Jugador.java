/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author hecto
 */
public class Jugador {
    //Atributos
    private int id;
    private String nombre;
    private int numero;
    private String posicion;
    private int id_equipo;      //Equipo al que pertenece 

    //Constructores
    public Jugador(int id, String nombre, int numero, String posicion, int id_equipo) {
        this.id = id;
        this.nombre = nombre;
        this.numero = numero;
        this.posicion = posicion;
        this.id_equipo = id_equipo;
    }

    public Jugador() {}
    
    
    ///Metodos de acceso
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public int getId_equipo() {
        return id_equipo;
    }

    public void setId_equipo(int id_equipo) {
        this.id_equipo = id_equipo;
    }
    
    
}
