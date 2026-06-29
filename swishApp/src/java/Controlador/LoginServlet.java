package Controlador;

import Modelo.Usuario;
import Modelo.Dao.UsuarioDao;
import java.io.IOException;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {

    UsuarioDao usuarioDao = new UsuarioDao();

    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("pages/login.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String accion = request.getParameter("accion");

        if ("login".equals(accion)) {

            String username = request.getParameter("username");
            String password = request.getParameter("password");

            Usuario usuario = usuarioDao.login(username, password);

            if (usuario == null) {
                request.setAttribute("error",
                    "Usuario o contraseña incorrectos.");
                request.getRequestDispatcher("pages/login.jsp")
                       .forward(request, response);
                return;
            }

            HttpSession session = request.getSession();
            session.setAttribute("usuarioActivo", usuario);

            response.sendRedirect("index.jsp");

        } else if ("registro".equals(accion)) {

            String nombre    = request.getParameter("nombre");
            String username  = request.getParameter("username");
            String password  = request.getParameter("password");
            String confirmar = request.getParameter("confirmar");

            if (!password.equals(confirmar)) {
                request.setAttribute("error",
                    "Las contraseñas no coinciden.");
                request.getRequestDispatcher("pages/registro.jsp")
                       .forward(request, response);
                return;
            }

            Usuario u = new Usuario();
            u.setNombre(nombre);
            u.setUsername(username);
            u.setPassword(password);

            boolean ok = usuarioDao.registrar(u);

            if (ok) {
                response.sendRedirect("LoginServlet");
            } else {
                request.setAttribute("error",
                    "El usuario ya existe.");
                request.getRequestDispatcher("pages/registro.jsp")
                       .forward(request, response);
            }
        }
    }
}