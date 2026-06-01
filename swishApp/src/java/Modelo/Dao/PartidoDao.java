package Modelo.Dao;

import Configuracion.Conexion;
import Modelo.Partido;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

public class PartidoDao {


    // LISTAR PARTIDOS
    public List<Partido> listar() {

        List<Partido> lista = new ArrayList<>();

        String sql = "SELECT * FROM partido";

        try (
            Connection con = Conexion.getConexion();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(sql)
        ) {

            while (rs.next()) {

                Partido p = new Partido();

                p.setId(rs.getInt("id"));

                p.setNombre(rs.getString("nombre"));

                p.setEstado(rs.getString("estado"));

                p.setPuntos_a(rs.getInt("puntos_a"));
                p.setPuntos_b(rs.getInt("puntos_b"));

                p.setFecha(rs.getString("fecha"));

                p.setId_equipo_a(rs.getInt("id_equipo_a"));
                p.setId_equipo_b(rs.getInt("id_equipo_b"));

                p.setId_torneo(rs.getInt("id_torneo"));

                lista.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
    public boolean existenPartidos() {

        String sql = "SELECT * FROM partido LIMIT 1";

        try (
                Connection con = Conexion.getConexion(); Statement st = con.createStatement(); ResultSet rs = st.executeQuery(sql)) {

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    
    public Partido buscarPorId(int id) {

        Partido p = new Partido();

        String sql
                = "SELECT * FROM partido WHERE id = ?";

        try (Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            // Enviar id
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                // Verificar si existe
                if (rs.next()) {

                    p.setId(rs.getInt("id"));

                    p.setNombre(rs.getString("nombre"));

                    p.setEstado(rs.getString("estado"));

                    p.setPuntos_a(rs.getInt("puntos_a"));

                    p.setPuntos_b(rs.getInt("puntos_b"));

                    p.setFecha(rs.getString("fecha"));

                    p.setId_equipo_a(rs.getInt("id_equipo_a"));

                    p.setId_equipo_b(rs.getInt("id_equipo_b"));

                    p.setId_torneo(rs.getInt("id_torneo"));

                    // Leer ronda y bracket para generarSiguientePartido
                    p.setRonda(rs.getInt("ronda"));

                    p.setBracket(rs.getString("bracket"));

                    p.setGanador(rs.getInt("ganador"));

                    p.setPerdedor(rs.getInt("perdedor"));
                    
                    p.setBye(rs.getBoolean("bye"));
                }
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        return p;
    }
    public void sumarPuntosEquipoA(int idPartido, int puntos) {

        String sql = "UPDATE partido SET puntos_a = puntos_a + ? WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, puntos);
            ps.setInt(2, idPartido);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void sumarPuntosEquipoB(int idPartido, int puntos) {

        String sql = "UPDATE partido SET puntos_b = puntos_b + ? WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, puntos);
            ps.setInt(2, idPartido);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    public void finalizarPartido(int idPartido, int ganador, int perdedor) {

        String sql = "UPDATE partido SET ganador = ?, perdedor = ?, finalizado = 1, estado = 'Finalizado' WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, ganador);

            ps.setInt(2, perdedor);

            ps.setInt(3, idPartido);

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public List<Partido> listarPorTorneo(int idTorneo) {

        List<Partido> lista = new ArrayList<>();

        String sql
                = "SELECT * FROM partido WHERE id_torneo = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setInt(1, idTorneo);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                Partido p = new Partido();

                p.setId(rs.getInt("id"));

                p.setNombre(rs.getString("nombre"));

                p.setEstado(rs.getString("estado"));

                p.setPuntos_a(rs.getInt("puntos_a"));

                p.setPuntos_b(rs.getInt("puntos_b"));

                p.setFecha(rs.getString("fecha"));

                p.setRonda(rs.getInt("ronda"));

                p.setBracket(rs.getString("bracket"));

                p.setGanador(rs.getInt("ganador"));

                p.setPerdedor(rs.getInt("perdedor"));

                p.setId_equipo_a(rs.getInt("id_equipo_a"));

                p.setId_equipo_b(rs.getInt("id_equipo_b"));

                p.setId_torneo(rs.getInt("id_torneo"));
                p.setBye(rs.getBoolean("bye"));

                lista.add(p);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return lista;
    }
    
    public boolean existePartido(
            int equipoA,
            int equipoB
    ) {

        String sql
                = "SELECT * FROM partido WHERE "
                + "(id_equipo_a = ? AND id_equipo_b = ?) "
                + "OR "
                + "(id_equipo_a = ? AND id_equipo_b = ?)";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql)) {

            ps.setInt(1, equipoA);

            ps.setInt(2, equipoB);

            ps.setInt(3, equipoB);

            ps.setInt(4, equipoA);

            ResultSet rs
                    = ps.executeQuery();

            return rs.next();

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
    
    public void insertar(Partido p) {

        String sql
                = "INSERT INTO partido("
                + "nombre, estado, puntos_a, puntos_b, fecha, "
                + "ronda, bracket, id_equipo_a, id_equipo_b, "
                + "id_torneo, bye, ganador, perdedor"
                + ") VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, p.getNombre());

            ps.setString(2, p.getEstado());

            ps.setInt(3, p.getPuntos_a());

            ps.setInt(4, p.getPuntos_b());

            ps.setString(5, p.getFecha());

            ps.setInt(6, p.getRonda());

            ps.setString(7, p.getBracket());

            ps.setInt(8, p.getId_equipo_a());

            ps.setInt(9, p.getId_equipo_b());

            ps.setInt(10, p.getId_torneo());

            ps.setBoolean(11, p.isBye());

            ps.setInt(12, p.getGanador());

            ps.setInt(13, p.getPerdedor());

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    public void actualizar(Partido p) {

        String sql
                = "UPDATE partido SET "
                + "nombre = ?, "
                + "estado = ?, "
                + "puntos_a = ?, "
                + "puntos_b = ?, "
                + "fecha = ?, "
                + "ronda = ?, "
                + "bracket = ?, "
                + "ganador = ?, "
                + "perdedor = ?, "
                + "id_equipo_a = ?, "
                + "id_equipo_b = ?, "
                + "id_torneo = ? "
                + "WHERE id = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql);) {

            ps.setString(1, p.getNombre());

            ps.setString(2, p.getEstado());

            ps.setInt(3, p.getPuntos_a());

            ps.setInt(4, p.getPuntos_b());

            ps.setString(5, p.getFecha());

            ps.setInt(6, p.getRonda());

            ps.setString(7, p.getBracket());

            ps.setInt(8, p.getGanador());

            ps.setInt(9, p.getPerdedor());

            ps.setInt(10, p.getId_equipo_a());

            ps.setInt(11, p.getId_equipo_b());

            ps.setInt(12, p.getId_torneo());

            ps.setInt(13, p.getId());

            ps.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    
    // =====================================================
// VERIFICAR SI YA EXISTE UN PARTIDO PENDIENTE
// =====================================================
    public boolean existePartidoPendiente(
            int equipoA,
            int equipoB,
            int idTorneo
    ) {

        String sql
                = "SELECT COUNT(*) FROM partido "
                + "WHERE id_torneo = ? "
                + "AND estado != 'FINALIZADO' "
                + "AND ( "
                + "  (id_equipo_a = ? AND id_equipo_b = ?) "
                + "  OR "
                + "  (id_equipo_a = ? AND id_equipo_b = ?) "
                + ")";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idTorneo);
            ps.setInt(2, equipoA);
            ps.setInt(3, equipoB);
            ps.setInt(4, equipoB);
            ps.setInt(5, equipoA);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt(1) > 0;
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return false;
    }
}