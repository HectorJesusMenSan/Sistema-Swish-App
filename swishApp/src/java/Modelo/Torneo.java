/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author hector
 */
public class Torneo {
    // Atributos
    private int id;
    private String nombre;
    private String tipo;
    private String estado;

    private String fecha_inicio;
    
    //Constructores
    public Torneo(int id, String nombre, String tipo, String fecha_inicio){
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.fecha_inicio = fecha_inicio;
        
    }
    public Torneo(){}
    
    //Metodos de acceso
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(String fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
    
}
