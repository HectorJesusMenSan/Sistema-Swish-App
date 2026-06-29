package Controlador;

import Modelo.Equipo;
import Modelo.Partido;

import Modelo.Dao.EquipoDao;
import Modelo.Dao.PartidoDao;

import java.io.IOException;

import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/PartidosServerlet")

public class PartidosServerlet extends HttpServlet {

    // ================= DAO =================
    EquipoDao equipoDao = new EquipoDao();

    PartidoDao partidoDao = new PartidoDao();

    // =====================================================
    // DO GET
    // =====================================================
    // =====================================================
// DO GET
// =====================================================
    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // Obtener torneo activo de la sesión
        // Si no hay sesión usar el último torneo
        Integer idTorneoSesion
                = (Integer) request.getSession()
                        .getAttribute("idTorneoActivo");

        List<Partido> listaPartidos;

        if (idTorneoSesion != null) {

            listaPartidos
                    = partidoDao.listarPorTorneo(idTorneoSesion);

        } else {

            listaPartidos = partidoDao.listar();
        }

        request.setAttribute("listaPartidos", listaPartidos);

        request.getRequestDispatcher(
                "pages/partidos.jsp"
        ).forward(request, response);
    }

// =====================================================
// DO POST
// =====================================================
    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if (accion == null) {
            response.sendRedirect("index.jsp");
            return;
        }

        switch (accion) {

            // =========================================
            // GENERAR PARTIDOS
            // =========================================
            case "generarPartidos":

                // Obtener equipos del torneo activo
                List<Equipo> equipos = equipoDao.listar();

                // Verificar partidos existentes
                List<Partido> partidosExistentes = partidoDao.listar();

                // Solo generar si no existen
                if (partidosExistentes.isEmpty()) {

                    // =========================================
                    // SORTEAR BYE SI HAY NÚMERO IMPAR
                    // =========================================
                    int idEquipoBye = 0;

                    if (equipos.size() % 2 != 0) {

                        int indiceBye
                                = (int) (Math.random() * equipos.size());

                        idEquipoBye
                                = equipos.get(indiceBye).getId();

                        int idTorneo
                                = equipos.get(indiceBye).getId_torneo();

                        Partido bye = new Partido();

                        bye.setNombre(
                                equipos.get(indiceBye).getNombre()
                                + " BYE"
                        );

                        bye.setEstado("FINALIZADO");
                        bye.setPuntos_a(0);
                        bye.setPuntos_b(0);
                        bye.setFecha(
                                new java.sql.Date(
                                        System.currentTimeMillis()
                                ).toString()
                        );
                        bye.setBracket("WINNERS");
                        bye.setRonda(1);
                        bye.setId_equipo_a(idEquipoBye);
                        bye.setId_equipo_b(0);
                        bye.setId_torneo(idTorneo);
                        bye.setGanador(idEquipoBye);
                        bye.setPerdedor(0);
                        bye.setBye(true);

                        partidoDao.insertar(bye);
                    }

                    // =========================================
                    // GENERAR PARTIDOS NORMALES
                    // =========================================
                    for (int i = 0; i < equipos.size(); i++) {

                        if (equipos.get(i).getId() == idEquipoBye) {
                            continue;
                        }

                        int j = i + 1;

                        while (j < equipos.size()
                                && equipos.get(j).getId() == idEquipoBye) {
                            j++;
                        }

                        if (j >= equipos.size()) {
                            break;
                        }

                        Equipo equipoA = equipos.get(i);
                        Equipo equipoB = equipos.get(j);

                        Partido p = new Partido();

                        p.setNombre(
                                equipoA.getNombre()
                                + " VS "
                                + equipoB.getNombre()
                        );

                        p.setEstado("PENDIENTE");
                        p.setPuntos_a(0);
                        p.setPuntos_b(0);
                        p.setFecha(
                                new java.sql.Date(
                                        System.currentTimeMillis()
                                ).toString()
                        );
                        p.setBracket("WINNERS");
                        p.setRonda(1);
                        p.setId_equipo_a(equipoA.getId());
                        p.setId_equipo_b(equipoB.getId());
                        p.setId_torneo(equipoA.getId_torneo());
                        p.setBye(false);

                        partidoDao.insertar(p);

                        i = j;
                    }

                    // Guardar torneo en sesión
                    if (!equipos.isEmpty()) {

                        request.getSession().setAttribute(
                                "idTorneoActivo",
                                equipos.get(0).getId_torneo()
                        );
                    }

                }

                response.sendRedirect("PartidosServerlet");
                return;

            // =========================================
            // DEFAULT
            // =========================================
            default:
                response.sendRedirect("index.jsp");
                return;
        }
    }
}