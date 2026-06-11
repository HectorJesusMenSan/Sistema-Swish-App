package Controlador;

import Modelo.ClasificacionEquipo;
import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.Partido;

import Modelo.Dao.EquipoDao;
import Modelo.Dao.JugadorDAO;
import Modelo.Dao.PartidoDao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import Configuracion.Conexion;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/ClasificacionServlet")
public class ClasificacionServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("verClasificacion".equals(accion)) {

            cargarClasificacion(request, response);
        }
    }

    private void cargarClasificacion(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        EquipoDao equipoDao = new EquipoDao();
        PartidoDao partidoDao = new PartidoDao();
        JugadorDAO jugadorDao = new JugadorDAO();

        List<Equipo> equipos = equipoDao.listar();
        List<Partido> partidos = partidoDao.listar();

        List<ClasificacionEquipo> clasificacion = new ArrayList<>();

        for (Equipo equipo : equipos) {

            ClasificacionEquipo c = new ClasificacionEquipo();

            c.setEquipo(equipo);

            int pj = 0;
            int victorias = 0;
            int derrotas = 0;
            int puntosFavor = 0;
            int puntosContra = 0;
            int faltas = 0;

            for (Partido p : partidos) {

                if (!"FINALIZADO".equalsIgnoreCase(p.getEstado())
                        && !"Finalizado".equalsIgnoreCase(p.getEstado())) {
                    continue;
                }

                boolean participa =
                        p.getId_equipo_a() == equipo.getId()
                        || p.getId_equipo_b() == equipo.getId();

                if (!participa) {
                    continue;
                }

                pj++;

                if (p.getGanador() == equipo.getId()) {
                    victorias++;
                }

                if (p.getPerdedor() == equipo.getId()) {
                    derrotas++;
                }

                if (p.getId_equipo_a() == equipo.getId()) {

                    puntosFavor += p.getPuntos_a();
                    puntosContra += p.getPuntos_b();

                } else {

                    puntosFavor += p.getPuntos_b();
                    puntosContra += p.getPuntos_a();
                }
            }

            List<Jugador> jugadores =
                    jugadorDao.listar_por_equipo(
                            equipo.getId()
                    );

            try (
                    Connection con = Conexion.getConexion()) {

                String sql =
                        "SELECT SUM(faltas) total "
                        + "FROM estadisticas_por_partido "
                        + "WHERE id_jugador = ?";

                for (Jugador j : jugadores) {

                    PreparedStatement ps =
                            con.prepareStatement(sql);

                    ps.setInt(1, j.getId());

                    ResultSet rs = ps.executeQuery();

                    if (rs.next()) {

                        faltas += rs.getInt("total");
                    }
                }

            } catch (Exception ex) {

                ex.printStackTrace();
            }

            c.setPj(pj);
            c.setVictorias(victorias);
            c.setDerrotas(derrotas);
            c.setPuntosFavor(puntosFavor);
            c.setPuntosContra(puntosContra);
            c.setDiferencia(
                    puntosFavor - puntosContra
            );
            c.setFaltas(faltas);

            clasificacion.add(c);
        }

        clasificacion.sort(
                Comparator.comparingInt(
                        ClasificacionEquipo::getVictorias
                ).reversed()
                        .thenComparingInt(
                                ClasificacionEquipo::getDiferencia
                        ).reversed()
        );

        if (!clasificacion.isEmpty()) {

            request.setAttribute(
                    "lider",
                    clasificacion.get(0)
            );
        }

        request.setAttribute(
                "clasificacion",
                clasificacion
        );

        request.getRequestDispatcher(
                "/pages/estadisticas.jsp"
        ).forward(request, response);
    }
}