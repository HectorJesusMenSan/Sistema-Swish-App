/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Torneo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;



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
    
    // =====================================================
// OBTENER ÚLTIMO TORNEO REGISTRADO
// =====================================================
    public Torneo obtenerUltimo() {

        Torneo t = new Torneo();

        String sql
                = "SELECT * FROM torneo "
                + "ORDER BY id DESC LIMIT 1";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                t.setId(rs.getInt("id"));

                t.setNombre(rs.getString("nombre"));

                t.setEstado(rs.getString("estado"));

                t.setFecha_inicio(rs.getString("fecha_inicio"));
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return t;
    }
    
}
