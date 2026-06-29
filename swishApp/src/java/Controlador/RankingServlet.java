package Controlador;

import Configuracion.Conexion;

import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.RankingJugador;

import Modelo.Dao.EquipoDao;
import Modelo.Dao.JugadorDAO;
import Modelo.Partido;
import Modelo.Dao.PartidoDao;

import java.io.IOException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/RankingServlet")
public class RankingServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("verRanking".equals(accion)) {

            // Si viene idTorneo en parámetro usarlo
            // Si no, usar el de la sesión
            String idTorneoParam
                    = request.getParameter("idTorneo");

            if (idTorneoParam != null
                    && !idTorneoParam.isEmpty()) {

                request.getSession().setAttribute(
                        "idTorneoActivo",
                        Integer.parseInt(idTorneoParam)
                );
            }

            cargarRanking(request, response);
        }
    }

    private void cargarRanking(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        JugadorDAO jugadorDao = new JugadorDAO();
        EquipoDao equipoDao = new EquipoDao();

        // Obtener torneo de la sesión
        Integer idTorneoSesion
                = (Integer) request.getSession()
                        .getAttribute("idTorneoActivo");

        List<Jugador> jugadores;

        if (idTorneoSesion != null) {

            jugadores = jugadorDao.listarPorTorneo(idTorneoSesion);

        } else {

            jugadores = jugadorDao.listarPorTorneoActivo();
        }

        List<RankingJugador> ranking = new ArrayList<>();

        try (Connection con = Conexion.getConexion()) {

            for (Jugador jugador : jugadores) {

                RankingJugador r = new RankingJugador();

                r.setJugador(jugador);

                // =====================================
                // EQUIPO DEL JUGADOR
                // =====================================
                Equipo equipo
                        = equipoDao.buscarPorId(
                                jugador.getId_equipo()
                        );

                r.setEquipo(equipo);

                // =====================================
                // ESTADISTICAS TOTALES
                // =====================================
                    String sql
                        = "SELECT "
                        + "COALESCE(SUM(e.puntos),0) puntos, "
                        + "COALESCE(SUM(e.faltas),0) faltas, "
                        + "COUNT(*) partidos "
                        + "FROM estadisticas_por_partido e "
                        + "INNER JOIN partido p ON e.id_partido = p.id "
                        + "WHERE e.id_jugador = ? "
                        + "AND p.id_torneo = ?";

                PreparedStatement ps = con.prepareStatement(sql);
                ps.setInt(1, jugador.getId());
                ps.setInt(2, idTorneoSesion != null ? idTorneoSesion : 0);

                ps.setInt(1, jugador.getId());

                ResultSet rs = ps.executeQuery();

                int puntos = 0;
                int faltas = 0;
                int partidos = 0;

                if (rs.next()) {

                    puntos = rs.getInt("puntos");
                    faltas = rs.getInt("faltas");
                    partidos = rs.getInt("partidos");
                }

                double promedio = 0;

                if (partidos > 0) {

                    promedio
                            = (double) puntos
                            / partidos;
                }

                double score
                        = puntos - (faltas * 0.5);

                r.setPuntos(puntos);
                r.setFaltas(faltas);
                r.setPartidosJugados(partidos);
                r.setPromedio(promedio);
                r.setScore(score);

                // =====================================
                // MEJOR PARTIDO DEL JUGADOR
                // =====================================
                String sqlMejorPartido
                        = "SELECT e.id_partido, e.puntos "
                        + "FROM estadisticas_por_partido e "
                        + "INNER JOIN partido p ON e.id_partido = p.id "
                        + "WHERE e.id_jugador = ? "
                        + "AND p.id_torneo = ? "
                        + "ORDER BY e.puntos DESC "
                        + "LIMIT 1";

                PreparedStatement ps2 = con.prepareStatement(sqlMejorPartido);
                ps2.setInt(1, jugador.getId());
                ps2.setInt(2, idTorneoSesion != null ? idTorneoSesion : 0);


                ResultSet rs2
                        = ps2.executeQuery();

                if (rs2.next()) {

                    r.setMejorPartido(
                            rs2.getInt("id_partido")
                    );

                    r.setPuntosMejorPartido(
                            rs2.getInt("puntos")
                    );
                }

                ranking.add(r);
            }

        } catch (Exception ex) {

            ex.printStackTrace();
        }

        // =====================================
        // ORDENAR POR SCORE
        // =====================================
        ranking.sort(
                Comparator.comparingDouble(
                        RankingJugador::getScore
                ).reversed()
        );

        // =====================================
        // MVP
        // =====================================
        RankingJugador mvp = null;

        if (!ranking.isEmpty()) {

            mvp = ranking.get(0);
        }

        // =====================================
        // TOP 10
        // =====================================
        List<RankingJugador> top10 = ranking;

        if (ranking.size() > 10) {

            top10 = ranking.subList(
                    0,
                    10
            );
        }

        request.setAttribute("mvp", mvp);
        request.setAttribute("ranking", top10);

// Construir texto del mejor partido del MVP
        if (mvp != null && mvp.getMejorPartido() > 0) {

            PartidoDao partidoDao = new PartidoDao();
            EquipoDao equipoDao1;
            equipoDao1 = new EquipoDao();

            Partido mejorP = partidoDao.buscarPorId(
                    mvp.getMejorPartido()
            );

            if (mejorP != null && mejorP.getId() > 0) {

                Equipo eqA = equipoDao1.buscarPorId(mejorP.getId_equipo_a());
                Equipo eqB = equipoDao1.buscarPorId(mejorP.getId_equipo_b());

                String textoMejorPartido
                        = (eqA != null ? eqA.getNombre() : "?")
                        + " vs "
                        + (eqB != null ? eqB.getNombre() : "?")
                        + " — " + mvp.getMejorPartidoPuntos() + " pts";

                request.setAttribute("mejorPartido", textoMejorPartido);
            }
        }

        request.getRequestDispatcher(
                "/pages/rankingMVP.jsp"
        ).forward(
                request,
                response
        );
    }
}
