/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author hecto
 */
public class EstadisticasPorPartido {
    private int id;
    private int id_partido;
    private int id_jugador;
    private int puntos;
    private int faltas;
    //Constructores

    public EstadisticasPorPartido(int id, int id_partido, int id_jugador, int puntos, int faltas) {
        this.id = id;
        this.id_partido = id_partido;
        this.id_jugador = id_jugador;
        this.puntos = puntos;
        this.faltas = faltas;
    }

    public EstadisticasPorPartido() {}
    
    //Metodos de acceso
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId_partido() {
        return id_partido;
    }

    public void setId_partido(int id_partido) {
        this.id_partido = id_partido;
    }

    public int getId_jugador() {
        return id_jugador;
    }

    public void setId_jugador(int id_jugador) {
        this.id_jugador = id_jugador;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getFaltas() {
        return faltas;
    }

    public void setFaltas(int faltas) {
        this.faltas = faltas;
    }
    
    
    
}
