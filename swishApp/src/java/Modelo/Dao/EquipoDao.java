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

        String sql
                = "INSERT INTO equipo("
                + "nombre, categoria, origen, id_torneo, derrotas, estado"
                + ") VALUES(?, ?, ?, ?, ?, ?)";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, E.getNombre());

            ps.setString(2, E.getCategoria());

            ps.setString(3, E.getOrigen());

            ps.setInt(4, E.getId_torneo());

            ps.setInt(5, 0);

            ps.setString(6, "ACTIVO");

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public List<Equipo> listar() {

        List<Equipo> lista = new ArrayList<>();

        // Solo equipos del último torneo registrado
        String sql
                = "SELECT * FROM equipo "
                + "WHERE id_torneo = ("
                + "  SELECT MAX(id) FROM torneo"
                + ") "
                + "ORDER BY id";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Equipo E = new Equipo();

                E.setId(rs.getInt("id"));
                E.setNombre(rs.getString("nombre"));
                E.setCategoria(rs.getString("categoria"));
                E.setOrigen(rs.getString("origen"));
                E.setId_torneo(rs.getInt("id_torneo"));
                E.setDerrotas(rs.getInt("derrotas"));
                E.setEstado(rs.getString("estado"));

                lista.add(E);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
    public Equipo buscarPorId(int id) {

        Equipo e = new Equipo();

        String sql = "SELECT * FROM equipo WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    e.setId(rs.getInt("id"));

                    e.setNombre(rs.getString("nombre"));

                    e.setCategoria(rs.getString("categoria"));

                    e.setOrigen(rs.getString("origen"));

                    e.setId_torneo(rs.getInt("id_torneo"));

                    // Leer derrotas y estado
                    e.setDerrotas(rs.getInt("derrotas"));

                    e.setEstado(rs.getString("estado"));
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return e;
    }
    
    public void actualizar(Equipo e) {

        String sql
                = "UPDATE equipo "
                + "SET nombre=?, categoria=?, origen=? "
                + "WHERE id=?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setString(1, e.getNombre());
            ps.setString(2, e.getCategoria());
            ps.setString(3, e.getOrigen());
            ps.setInt(4, e.getId());

            ps.executeUpdate();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    public void actualizarDerrotas(Equipo e) {

        try {

            Connection con = Conexion.getConexion();

            String sql = """
                     UPDATE equipo
                     SET derrotas = ?,
                         estado = ?
                     WHERE id = ?
                     """;

            PreparedStatement ps
                    = con.prepareStatement(sql);

            ps.setInt(1, e.getDerrotas());

            ps.setString(2, e.getEstado());

            ps.setInt(3, e.getId());

            ps.executeUpdate();

            con.close();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    public void eliminar(int id) {

        String sql
                = "DELETE FROM equipo WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ps.executeUpdate();

        } catch (Exception ex) {

            ex.printStackTrace();
        }
    }
    // =====================================================
// LISTAR EQUIPOS POR TORNEO ESPECÍFICO
// =====================================================

    public List<Equipo> listarPorTorneo(int idTorneo) {

        List<Equipo> lista = new ArrayList<>();

        String sql
                = "SELECT * FROM equipo "
                + "WHERE id_torneo = ? "
                + "ORDER BY id";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTorneo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Equipo e = new Equipo();

                e.setId(rs.getInt("id"));
                e.setNombre(rs.getString("nombre"));
                e.setCategoria(rs.getString("categoria"));
                e.setOrigen(rs.getString("origen"));
                e.setId_torneo(rs.getInt("id_torneo"));
                e.setDerrotas(rs.getInt("derrotas"));
                e.setEstado(rs.getString("estado"));

                lista.add(e);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
}
