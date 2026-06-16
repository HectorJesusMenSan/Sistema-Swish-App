package Controlador;

import Modelo.Torneo;
import Modelo.Dao.TorneoDao;
import Modelo.Dao.PartidoDao;
import Modelo.Partido;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import Modelo.Usuario;
import jakarta.servlet.http.HttpSession;


@WebServlet("/TorneoServlet")
public class TorneoServlet extends HttpServlet {

    // ================= DAO =================
    TorneoDao torneoDao = new TorneoDao();
    PartidoDao partidoDao = new PartidoDao();

    // =====================================================
    // DO GET
    // =====================================================
    @Override
    protected void doGet(
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
            // VER LISTA DE TORNEOS
            // =========================================
            case "verTorneos":

                HttpSession session = request.getSession();
                Usuario usuarioActivo
                        = (Usuario) session.getAttribute("usuarioActivo");

                int idUsuario = usuarioActivo != null ? usuarioActivo.getId() : 0;

                List<Torneo> lista = torneoDao.listarPorUsuario(idUsuario);

                request.setAttribute("listaTorneos", lista);

                request.getRequestDispatcher(
                        "pages/torneos.jsp"
                ).forward(request, response);

                return;

            // =========================================
            // CONTINUAR TORNEO
            // Redirige a partidos del torneo seleccionado
            // =========================================
            case "continuarTorneo":

                int idTorneoContinuar
                        = Integer.parseInt(
                                request.getParameter("idTorneo")
                        );

                // Guardar id del torneo en sesión
                request.getSession().setAttribute(
                        "idTorneoActivo",
                        idTorneoContinuar
                );

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
            // FINALIZAR TORNEO
            // =========================================
            case "finalizarTorneo":

                int idTorneoFinalizar
                        = Integer.parseInt(
                                request.getParameter("idTorneo")
                        );

                torneoDao.finalizar(idTorneoFinalizar);

                response.sendRedirect("index.jsp");

                return;

            // =========================================
            // ELIMINAR TORNEO
            // =========================================
            case "eliminarTorneo":

                int idTorneoEliminar
                        = Integer.parseInt(
                                request.getParameter("idTorneo")
                        );

                torneoDao.eliminar(idTorneoEliminar);

                response.sendRedirect(
                        "TorneoServlet?accion=verTorneos"
                );

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
