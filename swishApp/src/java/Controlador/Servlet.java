/*
 * Servlet principal del sistema
 * Se encarga de:
 * 
 * - Registrar torneos
 * - Registrar equipos
 * - Registrar jugadores
 * - Redireccionar entre páginas
 * - Enviar listas al JSP
 * 
 * También incluye validaciones para evitar errores
 * cuando NO existen equipos o jugadores registrados.
 */

package Controlador;

// ================= IMPORTACIONES =================

// Clases modelo
import Modelo.Equipo;
import Modelo.Jugador;
import Modelo.Torneo;

// Clases DAO (acceso a BD)
import Modelo.Dao.EquipoDao;
import Modelo.Dao.JugadorDAO;
import Modelo.Dao.TorneoDao;

// Librerías Java
import java.io.IOException;
import java.util.List;

// Librerías Jakarta Servlet
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


/*
 * El servlet responderá a:
 * 
 * http://localhost:8080/proyecto/Servlet
 */
@WebServlet("/Servlet")
public class Servlet extends HttpServlet {

    // ================= OBJETOS DAO =================
    // Permiten acceder a la base de datos

    EquipoDao c = new EquipoDao();
    JugadorDAO j = new JugadorDAO();
    TorneoDao t_dao = new TorneoDao();
    // =========================================================
    // ======================= DO GET ===========================
    // =========================================================
    /*
     * Se ejecuta cuando usamos:
     * 
     * response.sendRedirect()
     * enlaces <a href="">
     * URL directa
     */
    @Override
    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response)
                         throws ServletException, IOException {

        // Obtiene el parámetro accion de la URL
        // Ejemplo:
        // Servlet?accion=irRegistro
        String accion = request.getParameter("accion");
        // Verifica que accion NO sea null
        if (accion != null) {
            switch (accion) {
                // =================================================
                // IR A REGISTRO DE EQUIPOS
                // =================================================
                case "irRegistro":
                    
                      //Obtiene todos los equipos registrados
                    List<Equipo> lista_equipos = c.listar();
                      //Enviamos la lista de equipos al JSP
                    request.setAttribute("lista2", lista_equipos);
                    
                    /*
                     * VALIDACIÓN IMPORTANTE
                     * 
                     * Verifica si la lista tiene elementos
                     * para evitar:
                     * 
                     * IndexOutOfBoundsException
                     */
                    if (!lista_equipos.isEmpty()) {                        
                         // Obtiene el último equipo registrado
                        Equipo E = lista_equipos.get(lista_equipos.size() - 1);                 
                        //Busca los jugadores de ese equipo                        
                        List<Jugador> jugadores = j.listar_por_equipo(E.getId());
                        // Envía la lista de jugadores al JSP
                        request.setAttribute("lista", jugadores);

                    } else {
                          //Si NO hay equiposenviamos null o lista vacía
                        request.setAttribute("lista", null);
                    }
                    
                    // Redirecciona al JSP
                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    break;
                    
                //Redirecion a pestaña de partidos.
                case "irPartidos":
                    request.getRequestDispatcher("pages/partidos.jsp").forward(request, response);
                
                // =================================================
                // ACCIÓN NO VÁLIDA
                // =================================================
                default:
                    //Si la acción no existe vuelve al index
                    response.sendRedirect("index.jsp");

                    break;
            }
        }
    }



    // =========================================================
    // ======================= DO POST ==========================
    // =========================================================
    //Se ejecuta cuando enviamos formulario method="POST"
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
                          throws ServletException, IOException {



        // =====================================================
        // OBTENER PARÁMETROS
        // =====================================================

        //Obtiene el tipo de torneo enviado desde el formulario         
        String tipoTorneo = request.getParameter("tipoTorneo");
        
        //Obtiene la acción del formulario  
        String accion1 = request.getParameter("accion");
       
        //Objeto torneo
        Torneo t = new Torneo();

        // =====================================================
        // REGISTRAR TORNEO
        // =====================================================
        //Si tipoTorneo tiene valor significa que se registrará un torneo
         
        if (tipoTorneo != null) {
            // ================================================
            // TORNEO RÁFAGA
            // ================================================
            if ("rafaga".equals(tipoTorneo)) {

                t.setNombre("Rafaga");

                t.setEstado("Activo");
                
                 // Guarda fecha actual                 
                t.setFecha_inicio(new java.sql.Date(System.currentTimeMillis()).toString());
            }

            // ================================================
            // TORNEO LIGA
            // ================================================
            else {
                t.setNombre("Liga");
                t.setEstado("Activo");
                t.setFecha_inicio(new java.sql.Date(System.currentTimeMillis()).toString());
            }
            
             //Inserta torneo en BD
            t_dao.insertar(t);

            //Redirecciona a registro
            response.sendRedirect("Servlet?accion=irRegistro");
            return;
        }



        // =====================================================
        // VERIFICAR ACCIONES POST
        // =====================================================
        if (accion1 != null) {

            switch (accion1) {



                // =================================================
                // GUARDAR EQUIPO
                // =================================================
                case "guardarEquipo":
                    //Crear objeto equipo
                    Equipo e = new Equipo();
                   
                    //Obtener datos del formulario                     
                    e.setNombre(request.getParameter("nombre"));
                    e.setCategoria(request.getParameter("categoria"));
                    e.setOrigen(request.getParameter("origen"));
                    /*
                     * IMPORTANTE
                     * 
                     * Aquí obtener el ID real
                     * del torneo activo.
                     * 
                     * Actualmente:
                     * t.getId() probablemente es 0
                     */
                    e.setId_torneo(t.getId());
                    
                    //Guardar equipo                    
                    c.insertar(e);
                    
                    //Obtener lista actualizada de equipos                    
                    List<Equipo> lista2 = c.listar();



                    
                    //Obtener jugadores del equipo                    
                    List<Jugador> lista = j.listar_por_equipo(e.getId());
                    
                    //Enviar listas al JSP                    
                    request.setAttribute("lista", lista);
                    request.setAttribute("lista2", lista2);
               
                    //Variable para mostrar modal                    
                    request.setAttribute("equipoGuardado",true);
                    
                    //Forward al JSP                   
                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    return;

                // =================================================
                // GUARDAR JUGADOR
                // =================================================
                case "guardarJugador":                    
                    //Crear objeto jugador                     
                    Jugador jugador = new Jugador();
                    
                    //Obtener nombre                    
                    jugador.setNombre(request.getParameter("nombre"));
                    
                    //VALIDAR NÚMERO
                    String numero = request.getParameter("numero");
                    
                    //Verifica que NO esté vacío
                    if (numero != null && !numero.isEmpty()) {
                        jugador.setNumero(Integer.parseInt(numero));
                    } else {                        
                        //Valor por defecto
                        jugador.setNumero(0);
                    }

                    //Obtener posición
                    jugador.setPosicion(request.getParameter("posicion"));

                    //obtener equipos registrados
                    List<Equipo> lista_equipos = c.listar();
                    
                     //VALIDACIÓN IMPORTANTE 
                     //Verifica si existen equipos
                    if (!lista_equipos.isEmpty()) {
                        
                        //Obtiene último equipo                         
                        Equipo E = lista_equipos.get(lista_equipos.size() - 1);
                        
                        //Asignar ID equipo al jugador
                        jugador.setId_equipo(E.getId());
                        
                        //Guardar jugador                     
                        j.insertar(jugador);
                        
                        //Redireccionar                        
                        response.sendRedirect("Servlet?accion=irRegistro#seccion_jugadores");
                    } else {
                       
                        //Si NO existen equipos
                        request.setAttribute("error","Primero debes registrar un equipo");
                        
                        //Reenviar página
                        request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    }

                    return;
                // =================================================
                // NUEVO EQUIPO
                // =================================================
                case "nuevoEquipo":
                    //Obtener lista de equipos existentes
                    List<Equipo> lista2_nuevo = c.listar();
                 
                    //Enviar lista de equipos al JSP
                    request.setAttribute("lista2", lista2_nuevo);
                    /*
                     * IMPORTANTE
                     * 
                     * Limpiar jugadores
                     * para que NO aparezcan
                     * los del equipo anterior
                     */
                    request.setAttribute("lista", null);

                    //Mostrar modal correcto                    
                    request.setAttribute("jugadoresGuardado",true);
                    
                    //Regresar al JSP
                 
                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    return;


                // =================================================
                // ACCIÓN INVÁLIDA
                // =================================================
                default:
                    //Regresar al index                    
                    response.sendRedirect("index.jsp");
                    break;
            }
        }
    }
}