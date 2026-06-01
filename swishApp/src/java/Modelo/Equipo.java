/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author hector
 */
public class Equipo {
    //Atributos
    private int id;
    private String nombre;
    private String categoria;
    private String origen;
    private int id_torneo;
    private int derrotas;
    private String estado;
    //Constructores
    public Equipo(int id, String nombre, String categoria, String origen, int id_torneo) {
        this.id = id;
        this.nombre = nombre;
        this.categoria = categoria;
        this.origen = origen;
        this.id_torneo = id_torneo;
    }
    
    public Equipo(){}
    //Metodos de acceso
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

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public int getId_torneo() {
        return id_torneo;
    }

    public void setId_torneo(int id_torneo) {
        this.id_torneo = id_torneo;
    }
    public int getDerrotas() {
    return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
