/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package Controlador;

import Modelo.Equipo;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import Modelo.Dao.EquipoDao;
import Modelo.Dao.JugadorDAO;
import Modelo.Jugador;
import java.util.List;
/**
 *
 * @author hecto
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {
    EquipoDao c = new EquipoDao();
    JugadorDAO j = new JugadorDAO();

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion = request.getParameter("accion");

        if (accion != null) {

            switch (accion) {

                case "irRegistro":
                    request.setAttribute("lista", j.listar());
                    request.setAttribute("lista2", c.listar());
                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    
                    break;

                default:
                    response.sendRedirect("index.jsp");
                    break;
            }
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String accion1 = request.getParameter("accion");

        if (accion1 != null) {

            switch (accion1) {

                case "guardarEquipo":
                    Equipo e = new Equipo();
                    e.setNombre(request.getParameter("nombre"));
                    e.setCategoria(request.getParameter("categoria"));
                    e.setOrigen(request.getParameter("origen"));
                    e.setId_torneo(1);
                    
                    
                    c.insertar(e);
                    List<Jugador> lista = j.listar();
                    List<Equipo> lista2 = c.listar();
                    request.setAttribute("lista", lista);
                    request.setAttribute("lista2", lista2);
                    request.setAttribute("equipoGuardado", true);

                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response); 
                    
                    return;
                case "guardarJugador":
                    Jugador jugador = new Jugador();
                    jugador.setNombre(request.getParameter("nombre"));
                    //Convertir en entero la entrada
                    jugador.setNumero(Integer.parseInt(request.getParameter("numero")));
                    jugador.setPosicion(request.getParameter("posicion"));
                    //Converti la entrada a entero
                    jugador.setId_equipo(1);
                    j.insertar(jugador);
                    response.sendRedirect("Servlet?accion=irRegistro");
                    return;
                default:
                    response.sendRedirect("index.jsp");
                    break;
            }
            
        }
        
    }

}
