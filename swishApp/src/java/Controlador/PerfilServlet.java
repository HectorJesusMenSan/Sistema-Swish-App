package Controlador;

import Configuracion.Contrasenia;
import Modelo.Usuario;
import Modelo.Dao.UsuarioDao;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/PerfilServlet")
public class PerfilServlet extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("pages/perfil.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");
        HttpSession session = request.getSession();
        Usuario usuarioActivo
                = (Usuario) session.getAttribute("usuarioActivo");

        switch (accion) {

            // =========================================
            // ACTUALIZAR PERFIL
            // =========================================
            case "actualizarPerfil":

                String nombre = request.getParameter("nombre");
                String username = request.getParameter("username");

                if (nombre.isBlank() || username.isBlank()) {
                    request.setAttribute("error",
                            "Los campos no pueden estar vacíos.");
                    request.getRequestDispatcher("pages/perfil.jsp")
                            .forward(request, response);
                    return;
                }

                // Verificar que el username no lo use otro
                if (!username.equals(usuarioActivo.getUsername())
                        && usuarioDao.existeUsername(username)) {
                    request.setAttribute("error",
                            "Ese usuario ya está en uso.");
                    request.getRequestDispatcher("pages/perfil.jsp")
                            .forward(request, response);
                    return;
                }

                usuarioActivo.setNombre(nombre);
                usuarioActivo.setUsername(username);

                usuarioDao.actualizar(usuarioActivo);

                session.setAttribute("usuarioActivo", usuarioActivo);

                request.setAttribute("exito",
                        "Perfil actualizado correctamente.");
                request.getRequestDispatcher("pages/perfil.jsp")
                        .forward(request, response);
                return;

            // =========================================
            // CAMBIAR PASSWORD
            // =========================================
            case "cambiarPassword":

                String passwordActual = request.getParameter("passwordActual");
                String passwordNueva = request.getParameter("passwordNueva");
                String confirmar = request.getParameter("confirmar");

                // Verificar contraseña actual
                Usuario verificado = usuarioDao.login(
                        usuarioActivo.getUsername(), passwordActual
                );

                if (verificado == null) {
                    request.setAttribute("error",
                            "La contraseña actual es incorrecta.");
                    request.getRequestDispatcher("pages/perfil.jsp")
                            .forward(request, response);
                    return;
                }

                if (!passwordNueva.equals(confirmar)) {
                    request.setAttribute("error",
                            "Las contraseñas nuevas no coinciden.");
                    request.getRequestDispatcher("pages/perfil.jsp")
                            .forward(request, response);
                    return;
                }

                if (passwordNueva.length() < 6) {
                    request.setAttribute("error",
                            "La contraseña debe tener mínimo 6 caracteres.");
                    request.getRequestDispatcher("pages/perfil.jsp")
                            .forward(request, response);
                    return;
                }

                try {
                    String nuevoHash
                            = Contrasenia.hashPassword(passwordNueva);
                    usuarioDao.actualizarPassword(
                            usuarioActivo.getId(), nuevoHash
                    );
                } catch (Exception e) {
                    e.printStackTrace();
                }

                request.setAttribute("exito",
                        "Contraseña actualizada correctamente.");
                request.getRequestDispatcher("pages/perfil.jsp")
                        .forward(request, response);
                return;

            // =========================================
            // CERRAR SESIÓN
            // =========================================
            case "cerrarSesion":

                session.invalidate();
                response.sendRedirect(
                        request.getContextPath() + "/LoginServlet"
                );
                return;

            default:
                response.sendRedirect("index.jsp");
        }
    }
}
