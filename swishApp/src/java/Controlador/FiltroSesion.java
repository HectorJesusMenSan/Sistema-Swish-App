package Controlador;

import Modelo.Usuario;
import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.*;

@WebFilter("/*")
public class FiltroSesion implements Filter {

    @Override
    public void doFilter(ServletRequest req,
            ServletResponse res,
            FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        String uri = request.getRequestURI();

        // Rutas públicas que no necesitan login
        boolean esPublica
                = uri.contains("LoginServlet")
                || uri.contains("login.jsp")
                || uri.contains("registro.jsp")
                || uri.contains("/CSS/")
                || uri.contains("/js/")
                || uri.endsWith(".css")
                || uri.endsWith(".js");

        if (esPublica) {
            chain.doFilter(req, res);
            return;
        }

        HttpSession session = request.getSession(false);
        Usuario usuario = session != null
                ? (Usuario) session.getAttribute("usuarioActivo")
                : null;

        if (usuario == null) {
            response.sendRedirect(
                    request.getContextPath() + "/LoginServlet"
            );
            return;
        }

        chain.doFilter(req, res);
    }
}
