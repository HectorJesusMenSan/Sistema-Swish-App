package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Torneo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TorneoDao {

    // =====================================================
    // INSERTAR
    // =====================================================
    public void insertar(Torneo t) {

        String sql
                = "INSERT INTO torneo(nombre, tipo, estado, fecha_inicio) "
                + "VALUES(?, ?, ?, ?)";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getTipo());
            ps.setString(3, t.getEstado());
            ps.setString(4, t.getFecha_inicio());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // LISTAR TODOS
    // =====================================================
    public List<Torneo> listar() {

        List<Torneo> lista = new ArrayList<>();

        String sql
                = "SELECT * FROM torneo ORDER BY id DESC";

        try (
                Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {

                Torneo t = new Torneo();

                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(rs.getString("tipo"));
                t.setEstado(rs.getString("estado"));
                t.setFecha_inicio(rs.getString("fecha_inicio"));

                lista.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

    // =====================================================
    // BUSCAR POR ID
    // =====================================================
    public Torneo buscarPorId(int id) {

        Torneo t = new Torneo();

        String sql
                = "SELECT * FROM torneo WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
                t.setTipo(rs.getString("tipo"));
                t.setEstado(rs.getString("estado"));
                t.setFecha_inicio(rs.getString("fecha_inicio"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    // =====================================================
    // OBTENER ÚLTIMO
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
                t.setTipo(rs.getString("tipo"));
                t.setEstado(rs.getString("estado"));
                t.setFecha_inicio(rs.getString("fecha_inicio"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    // =====================================================
    // FINALIZAR TORNEO
    // =====================================================
    public void finalizar(int idTorneo) {

        String sql
                = "UPDATE torneo SET estado = 'FINALIZADO' "
                + "WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTorneo);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =====================================================
    // ELIMINAR TORNEO Y TODOS SUS DATOS
    // =====================================================
    public void eliminar(int idTorneo) {

        try (Connection con = Conexion.getConexion()) {

            // Eliminar estadísticas
            String sqlEst
                    = "DELETE FROM estadisticas_por_partido "
                    + "WHERE id_partido IN ("
                    + "  SELECT id FROM partido "
                    + "  WHERE id_torneo = ?"
                    + ")";

            PreparedStatement psEst
                    = con.prepareStatement(sqlEst);

            psEst.setInt(1, idTorneo);
            psEst.executeUpdate();

            // Eliminar partidos
            String sqlPartido
                    = "DELETE FROM partido WHERE id_torneo = ?";

            PreparedStatement psPartido
                    = con.prepareStatement(sqlPartido);

            psPartido.setInt(1, idTorneo);
            psPartido.executeUpdate();

            // Eliminar jugadores
            String sqlJugador
                    = "DELETE FROM jugador WHERE id_equipo IN ("
                    + "  SELECT id FROM equipo WHERE id_torneo = ?"
                    + ")";

            PreparedStatement psJugador
                    = con.prepareStatement(sqlJugador);

            psJugador.setInt(1, idTorneo);
            psJugador.executeUpdate();

            // Eliminar equipos
            String sqlEquipo
                    = "DELETE FROM equipo WHERE id_torneo = ?";

            PreparedStatement psEquipo
                    = con.prepareStatement(sqlEquipo);

            psEquipo.setInt(1, idTorneo);
            psEquipo.executeUpdate();

            // Eliminar torneo
            String sqlTorneo
                    = "DELETE FROM torneo WHERE id = ?";

            PreparedStatement psTorneo
                    = con.prepareStatement(sqlTorneo);

            psTorneo.setInt(1, idTorneo);
            psTorneo.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void insertarConUsuario(Torneo t, int idUsuario) {
        String sql
                = "INSERT INTO torneo(nombre, tipo, estado, fecha_inicio, id_usuario) "
                + "VALUES(?, ?, ?, ?, ?)";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, t.getNombre());
            ps.setString(2, t.getTipo());
            ps.setString(3, t.getEstado());
            ps.setString(4, t.getFecha_inicio());
            ps.setInt(5, idUsuario);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Torneo> listarPorUsuario(int idUsuario) {
        List<Torneo> lista = new ArrayList<>();

        String sql
                = "SELECT * FROM torneo WHERE id_usuario = ? ORDER BY id DESC";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idUsuario);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Torneo t = new Torneo();
                t.setId(rs.getInt("id"));
                t.setNombre(rs.getString("nombre"));
                t.setEstado(rs.getString("estado"));
                t.setFecha_inicio(rs.getString("fecha_inicio"));
                lista.add(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
