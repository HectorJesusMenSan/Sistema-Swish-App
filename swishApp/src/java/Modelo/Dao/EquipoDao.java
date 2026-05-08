/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Equipo;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hector
 */
public class EquipoDao {
    public void insertar(Equipo E) {
        String sql = "INSERT INTO equipo(nombre, categoria, origen, id_torneo) VALUES(?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, E.getNombre());
            ps.setString(2, E.getCategoria());
            ps.setString(3, E.getOrigen());
            ps.setInt(4, E.getId_torneo());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Equipo> listar() {

        List<Equipo> lista = new ArrayList<>();

        String sql = "SELECT * FROM equipo";

        try(Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            

            while (rs.next()) {

                Equipo E = new Equipo();

                E.setId(rs.getInt("id"));
                E.setNombre(rs.getString("nombre"));
                E.setCategoria(rs.getString("categoria"));
                E.setOrigen(rs.getString("origen"));
                E.setId_torneo(rs.getInt("id_torneo"));
                
                

                lista.add(E);
            }
        }catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
}
