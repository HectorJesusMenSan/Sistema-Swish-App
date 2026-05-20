/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Equipo;
import Modelo.Jugador;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author hecto
 */
public class JugadorDAO {
    public void insertar(Jugador j) {
        String sql = "INSERT INTO jugador(nombre, numero, posicion, id_equipo) VALUES(?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, j.getNombre());
            ps.setInt(2, j.getNumero());
            ps.setString(3, j.getPosicion());
            ps.setInt(4, j.getId_equipo());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<Jugador> listar() {

        List<Jugador> lista = new ArrayList<>();

        String sql = "SELECT * FROM jugador";

        try(Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)){
            

            while (rs.next()) {

                Jugador j = new Jugador();

                j.setId(rs.getInt("id"));
                j.setNombre(rs.getString("nombre"));
                j.setNumero(rs.getInt("numero"));
                j.setPosicion(rs.getString("posicion"));

                lista.add(j);
            }
        }catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
    
    public List<Jugador> listar_por_equipo(int id) {

        List<Jugador> lista = new ArrayList<>();

        String sql =
            "SELECT * FROM jugador WHERE id_equipo = ?";

        try (
            Connection con = Conexion.getConexion();
            PreparedStatement ps =
            con.prepareStatement(sql);
        ) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Jugador j = new Jugador();

                j.setId(rs.getInt("id"));
                j.setNombre(rs.getString("nombre"));
                j.setNumero(rs.getInt("numero"));
                j.setPosicion(rs.getString("posicion"));
                j.setId_equipo(
                    rs.getInt("id_equipo")
                );

                lista.add(j);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }

    
}
