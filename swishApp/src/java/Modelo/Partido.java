/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author hecto
 */
public class Partido {
    private int id;
    private String nombre;
    private String estado;
    private int puntos_a;
    private int puntos_b;
    private String fecha;
    private int id_equipo_a;
    private int id_equipo_b;
    private int id_torneo;
    private int ronda;
    private String bracket;
    private int ganador;
    private int perdedor;
    private boolean finalizado;
    private boolean bye;
    
    //Constructores
    public Partido(int id, String nombre, int puntos_a, int puntos_b, String fecha, int id_equipo_a, int id_equipo_b, int id_torneo) {
        this.id = id;
        this.nombre = nombre;
        this.puntos_a = puntos_a;
        this.puntos_b = puntos_b;
        this.fecha = fecha;
        this.id_equipo_a = id_equipo_a;
        this.id_equipo_b = id_equipo_b;
        this.id_torneo = id_torneo;
    }

    public Partido() {}
    
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getPuntos_a() {
        return puntos_a;
    }

    public void setPuntos_a(int puntos_a) {
        this.puntos_a = puntos_a;
    }

    public int getPuntos_b() {
        return puntos_b;
    }

    public void setPuntos_b(int puntos_b) {
        this.puntos_b = puntos_b;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId_equipo_a() {
        return id_equipo_a;
    }

    public void setId_equipo_a(int id_equipo_a) {
        this.id_equipo_a = id_equipo_a;
    }

    public int getId_equipo_b() {
        return id_equipo_b;
    }

    public void setId_equipo_b(int id_equipo_b) {
        this.id_equipo_b = id_equipo_b;
    }

    public int getId_torneo() {
        return id_torneo;
    }

    public void setId_torneo(int id_torneo) {
        this.id_torneo = id_torneo;
    }

    public int getRonda() {
        return ronda;
    }

    public void setRonda(int ronda) {
        this.ronda = ronda;
    }

    public String getBracket() {
        return bracket;
    }

    public void setBracket(String bracket) {
        this.bracket = bracket;
    }

    public int getGanador() {
        return ganador;
    }

    public void setGanador(int ganador) {
        this.ganador = ganador;
    }

    public int getPerdedor() {
        return perdedor;
    }

    public void setPerdedor(int perdedor) {
        this.perdedor = perdedor;
    }

    public boolean isFinalizado() {
        return finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }
    
    public boolean isBye() {
        return bye;
    }

    public void setBye(boolean bye) {
        this.bye = bye;
    }
    
    
}
