/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Torneo;
import java.sql.Connection;
import java.sql.PreparedStatement;


/**
 *
 * @author hecto
 */
public class TorneoDao {
    public void insertar(Torneo T) {
        String sql = "INSERT INTO torneo(nombre, tipo, estado, fecha_inicio) VALUES(?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, T.getNombre());
            ps.setString(2, T.getTipo());
            ps.setString(3, T.getEstado());
            ps.setString(4, T.getFecha_inicio());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
