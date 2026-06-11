package Modelo;
import Modelo.Equipo;
public class RankingJugador {

    private Jugador jugador;

    private Equipo equipo;

    private int puntos;
    private int faltas;
    private int partidosJugados;

    private double promedio;
    private double score;

    private int mejorPartido;
    private int puntosMejorPartido;
    
   

    public Jugador getJugador() {
        return jugador;
    }

    public void setJugador(Jugador jugador) {
        this.jugador = jugador;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
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

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }

    public double getPromedio() {
        return promedio;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public int getMejorPartido() {
        return mejorPartido;
    }

    public void setMejorPartido(int mejorPartido) {
        this.mejorPartido = mejorPartido;
    }

    public int getPuntosMejorPartido() {
        return puntosMejorPartido;
    }

    public void setPuntosMejorPartido(int puntosMejorPartido) {
        this.puntosMejorPartido = puntosMejorPartido;
    }
    public int getMejorPartidoPuntos() {
        return puntosMejorPartido;
    }
    public void setMejorPartidoPuntos(int puntos) {
        this.puntosMejorPartido = puntos;
    }

}