package Modelo.Dao;

import Configuracion.Conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EstadisticasDao {

    // =====================================================
    // SUMAR PUNTOS
    // =====================================================
    public void sumarPuntos(int idPartido, int idJugador, int puntos) {

        String verificar
                = "SELECT * FROM estadisticas_por_partido "
                + "WHERE id_partido = ? "
                + "AND id_jugador = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement psVerificar
                = con.prepareStatement(verificar);) {

            psVerificar.setInt(1, idPartido);

            psVerificar.setInt(2, idJugador);

            ResultSet rs = psVerificar.executeQuery();

            // =========================================
            // SI YA EXISTE
            // =========================================
            if (rs.next()) {

                String update
                        = "UPDATE estadisticas_por_partido "
                        + "SET puntos = puntos + ? "
                        + "WHERE id_partido = ? "
                        + "AND id_jugador = ?";

                PreparedStatement psUpdate
                        = con.prepareStatement(update);

                psUpdate.setInt(1, puntos);

                psUpdate.setInt(2, idPartido);

                psUpdate.setInt(3, idJugador);

                psUpdate.executeUpdate();

            } else {

                // =====================================
                // SI NO EXISTE
                // =====================================
                String insert
                        = "INSERT INTO estadisticas_por_partido("
                        + "id_partido, "
                        + "id_jugador, "
                        + "puntos, "
                        + "faltas"
                        + ") VALUES (?, ?, ?, 0)";

                PreparedStatement psInsert
                        = con.prepareStatement(insert);

                psInsert.setInt(1, idPartido);

                psInsert.setInt(2, idJugador);

                psInsert.setInt(3, puntos);

                psInsert.executeUpdate();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // SUMAR FALTA
    // =====================================================
    public void sumarFalta(int idPartido, int idJugador) {

        String verificar
                = "SELECT * FROM estadisticas_por_partido "
                + "WHERE id_partido = ? "
                + "AND id_jugador = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement psVerificar
                = con.prepareStatement(verificar);) {

            psVerificar.setInt(1, idPartido);

            psVerificar.setInt(2, idJugador);

            ResultSet rs = psVerificar.executeQuery();

            // =========================================
            // SI YA EXISTE
            // =========================================
            if (rs.next()) {

                String update
                        = "UPDATE estadisticas_por_partido "
                        + "SET faltas = faltas + 1 "
                        + "WHERE id_partido = ? "
                        + "AND id_jugador = ?";

                PreparedStatement psUpdate
                        = con.prepareStatement(update);

                psUpdate.setInt(1, idPartido);

                psUpdate.setInt(2, idJugador);

                psUpdate.executeUpdate();

            } else {

                // =====================================
                // SI NO EXISTE
                // =====================================
                String insert
                        = "INSERT INTO estadisticas_por_partido("
                        + "id_partido, "
                        + "id_jugador, "
                        + "puntos, "
                        + "faltas"
                        + ") VALUES (?, ?, 0, 1)";

                PreparedStatement psInsert
                        = con.prepareStatement(insert);

                psInsert.setInt(1, idPartido);

                psInsert.setInt(2, idJugador);

                psInsert.executeUpdate();
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    // =====================================================
    // OBTENER PUNTOS
    // =====================================================
    public int obtenerPuntos(int idPartido, int idJugador) {

        int puntos = 0;

        String sql
                = "SELECT puntos "
                + "FROM estadisticas_por_partido "
                + "WHERE id_partido = ? "
                + "AND id_jugador = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql);) {

            ps.setInt(1, idPartido);

            ps.setInt(2, idJugador);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                puntos = rs.getInt("puntos");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return puntos;
    }

    // =====================================================
    // OBTENER FALTAS
    // =====================================================
    public int obtenerFaltas(int idPartido, int idJugador) {

        int faltas = 0;

        String sql
                = "SELECT faltas "
                + "FROM estadisticas_por_partido "
                + "WHERE id_partido = ? "
                + "AND id_jugador = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql);) {

            ps.setInt(1, idPartido);

            ps.setInt(2, idJugador);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                faltas = rs.getInt("faltas");
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return faltas;
    }

    // =====================================================
    // ACTUALIZAR MARCADOR
    // =====================================================
    public void actualizarMarcador(int idPartido) {

        int puntosA = 0;

        int puntosB = 0;

        try (
                Connection con = Conexion.getConexion();) {

            // =========================================
            // BUSCAR PARTIDO
            // =========================================
            String sqlPartido
                    = "SELECT * FROM partido WHERE id = ?";

            PreparedStatement psPartido
                    = con.prepareStatement(sqlPartido);

            psPartido.setInt(1, idPartido);

            ResultSet rsPartido
                    = psPartido.executeQuery();

            int idEquipoA = 0;

            int idEquipoB = 0;

            if (rsPartido.next()) {

                idEquipoA
                        = rsPartido.getInt("id_equipo_a");

                idEquipoB
                        = rsPartido.getInt("id_equipo_b");
            }

            // =========================================
            // OBTENER ESTADISTICAS
            // =========================================
            String sqlEst
                    = "SELECT * FROM estadisticas_por_partido "
                    + "WHERE id_partido = ?";

            PreparedStatement psEst
                    = con.prepareStatement(sqlEst);

            psEst.setInt(1, idPartido);

            ResultSet rsEst
                    = psEst.executeQuery();

            while (rsEst.next()) {

                int idJugador
                        = rsEst.getInt("id_jugador");

                int puntos
                        = rsEst.getInt("puntos");

                // =====================================
                // BUSCAR JUGADOR
                // =====================================
                String sqlJugador
                        = "SELECT * FROM jugador "
                        + "WHERE id = ?";

                PreparedStatement psJugador
                        = con.prepareStatement(sqlJugador);

                psJugador.setInt(1, idJugador);

                ResultSet rsJugador
                        = psJugador.executeQuery();

                if (rsJugador.next()) {

                    int idEquipoJugador
                            = rsJugador.getInt("id_equipo");

                    // =================================
                    // SUMAR AL EQUIPO
                    // =================================
                    if (idEquipoJugador == idEquipoA) {

                        puntosA += puntos;

                    } else if (idEquipoJugador == idEquipoB) {

                        puntosB += puntos;
                    }
                }
            }

            // =========================================
            // ACTUALIZAR PARTIDO
            // =========================================
            String update
                    = "UPDATE partido "
                    + "SET puntos_a = ?, puntos_b = ? "
                    + "WHERE id = ?";

            PreparedStatement psUpdate
                    = con.prepareStatement(update);

            psUpdate.setInt(1, puntosA);

            psUpdate.setInt(2, puntosB);

            psUpdate.setInt(3, idPartido);

            psUpdate.executeUpdate();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
    // =====================================================
// OBTENER PUNTOS DEL EQUIPO
// =====================================================

    public int obtenerPuntosEquipo(int idPartido, int idEquipo) {

        int total = 0;

        String sql
                = "SELECT * FROM estadisticas_por_partido "
                + "WHERE id_partido = ?";

        try (
                Connection con = Conexion.getConexion(); PreparedStatement ps
                = con.prepareStatement(sql);) {

            ps.setInt(1, idPartido);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                int idJugador
                        = rs.getInt("id_jugador");

                int puntos
                        = rs.getInt("puntos");

                String sqlJugador
                        = "SELECT * FROM jugador "
                        + "WHERE id = ?";

                PreparedStatement psJugador
                        = con.prepareStatement(sqlJugador);

                psJugador.setInt(1, idJugador);

                ResultSet rsJugador
                        = psJugador.executeQuery();

                if (rsJugador.next()) {

                    int equipoJugador
                            = rsJugador.getInt("id_equipo");

                    if (equipoJugador == idEquipo) {

                        total += puntos;
                    }
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

        return total;
    }
}
