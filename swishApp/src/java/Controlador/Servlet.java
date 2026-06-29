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
 * También incluye algunas validaciones para evitar errores
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

import Modelo.Usuario;
import jakarta.servlet.http.HttpSession;

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

                    // Obtener equipos
                    List<Equipo> lista_equipos = c.listar();

                    // Enviar equipos al JSP
                    request.setAttribute("lista2", lista_equipos);

                    // Obtener parámetro
                    String mostrarJugadores
                            = request.getParameter("mostrarJugadores");

                    // Solo mostrar jugadores si viene del registro
                    if ("true".equals(mostrarJugadores) && !lista_equipos.isEmpty()) {

                        // ANTES: se usaba "el ultimo equipo de la
                        // lista" para saber de quien mostrar los
                        // jugadores. AHORA: se usa el equipo activo
                        // guardado en sesion (idEquipoActivo), para
                        // no mezclar jugadores de equipos distintos
                        // cuando se esta editando un equipo que NO
                        // es el ultimo de la lista.
                        Integer idEquipoActivo
                                = (Integer) request.getSession()
                                        .getAttribute("idEquipoActivo");

                        Equipo E = (idEquipoActivo != null && idEquipoActivo > 0)
                                ? c.buscarPorId(idEquipoActivo)
                                : lista_equipos.get(lista_equipos.size() - 1);

                        List<Jugador> jugadores = j.listar_por_equipo(E.getId());

                        request.setAttribute("lista", jugadores);

                        // FIX: esta bandera faltaba. Sin ella, al
                        // volver aqui despues de guardar un jugador
                        // (este caso llega via redirect), pasoActual
                        // en el JSP volvia a 1 y bloqueaba la
                        // seccion de jugadores -> por eso solo
                        // dejaba agregar UN jugador.
                        request.setAttribute("irJugadores", true);

                    } else {

                        // NO mostrar jugadores automáticamente
                        request.setAttribute("lista", null);
                    }

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
            } // ================================================
            // TORNEO LIGA
            // ================================================
            else {
                t.setNombre("Liga");
                t.setEstado("Activo");
                t.setFecha_inicio(new java.sql.Date(System.currentTimeMillis()).toString());
            }

            //Inserta torneo en BD
            HttpSession session = request.getSession();
            Usuario usuarioActivo
                    = (Usuario) session.getAttribute("usuarioActivo");

            int idUsuario = usuarioActivo != null ? usuarioActivo.getId() : 0;

            t_dao.insertarConUsuario(t, idUsuario);

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
                    // Obtener el torneo activo
                    Torneo torneoActivo = t_dao.obtenerUltimo();

                    e.setId_torneo(torneoActivo.getId());

                    String idStr = request.getParameter("id");

                    int idEquipo = 0;

                    if (idStr != null && !idStr.trim().isEmpty()) {

                        idEquipo = Integer.parseInt(idStr);
                    }

                    e.setId(idEquipo);
                    //Guardar equipo                    
                    if (idEquipo > 0) {

                        c.actualizar(e);

                    } else {
                        // Estado inicial siempre en mayúsculas
                        e.setEstado("ACTIVO");
                        e.setDerrotas(0);
                        c.insertar(e);
                    }

                    //Obtener lista actualizada de equipos                    
                    List<Equipo> lista2 = c.listar();

                    // RESPALDO: si era un equipo NUEVO (idEquipo
                    // era 0) y despues de insertar() "e.getId()"
                    // SIGUE en 0, significa que EquipoDao.insertar
                    // no esta devolviendo el id generado dentro de
                    // "e". Lo inferimos como el ultimo equipo de
                    // la lista recien actualizada (asumiendo que
                    // listar() viene ordenado por id ascendente,
                    // igual que ya asumia el resto del codigo
                    // original con "el ultimo equipo de la lista").
                    if (idEquipo == 0 && e.getId() <= 0 && !lista2.isEmpty()) {

                        Equipo ultimoInsertado = lista2.get(lista2.size() - 1);

                        e.setId(ultimoInsertado.getId());
                    }

                    // NUEVO: guardar en sesion cual es el equipo
                    // activo. Asi, guardarJugador/nuevoEquipo saben
                    // exactamente a cual equipo pertenecen los
                    // siguientes jugadores (antes se adivinaba
                    // usando "el ultimo equipo de la lista", lo
                    // cual fallaba al editar un equipo viejo).
                    if (e.getId() > 0) {

                        request.getSession().setAttribute("idEquipoActivo", e.getId());

                    } else {

                        // Si aun asi no se pudo determinar un id
                        // valido, mejor no dejar nada en sesion
                        // que pueda ensuciar el siguiente equipo.
                        request.getSession().removeAttribute("idEquipoActivo");
                    }

                    //Obtener jugadores del equipo                    
                    List<Jugador> lista = j.listar_por_equipo(e.getId());

                    //Enviar listas al JSP                    
                    request.setAttribute("lista", lista);
                    request.setAttribute("lista2", lista2);

                    //Variable para mostrar modal                    
                    request.setAttribute("equipoGuardado", true);

                    //Forward al JSP                   
                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    return;

                // =================================================
                // GUARDAR JUGADOR
                // =================================================
                case "guardarJugador":
                    try {
                        // Crear objeto jugador
                        Jugador jugador = new Jugador();

                        // Obtener nombre
                        jugador.setNombre(request.getParameter("nombre"));

                        // VALIDAR NÚMERO
                        String numeroStr = request.getParameter("numero");
                        int numero = 0;

                        // Verifica que NO esté vacío
                        if (numeroStr != null && !numeroStr.isEmpty()) {
                            numero = Integer.parseInt(numeroStr);
                            jugador.setNumero(numero);
                        } else {
                            // Valor por defecto
                            jugador.setNumero(0);
                        }

                        // Obtener posición
                        jugador.setPosicion(request.getParameter("posicion"));

                        // ANTES: se usaba "el ultimo equipo de la
                        // lista completa" para saber a cual equipo
                        // pertenece este jugador. AHORA: se usa el
                        // equipo activo guardado en sesion
                        // (seteado en guardarEquipo/editarEquipo).
                        // Esto evita que, al editar un equipo que
                        // NO es el ultimo creado, los jugadores
                        // nuevos se agreguen al equipo equivocado.
                        Integer idEquipoActivo
                                = (Integer) request.getSession()
                                        .getAttribute("idEquipoActivo");

                        // OJO: tambien se descarta si es <= 0 (no
                        // solo null), por si alguna vez quedo un 0
                        // guardado en sesion de un insert que no
                        // devolvio el id generado.
                        Equipo E = (idEquipoActivo != null && idEquipoActivo > 0)
                                ? c.buscarPorId(idEquipoActivo)
                                : null;

                        // VALIDACIÓN IMPORTANTE: Verifica si hay un equipo activo
                        if (E != null) {

                            // ✅ VERIFICAR SI EL NÚMERO YA EXISTE EN ESTE EQUIPO
                            if (j.existeNumeroEnEquipo(numero, E.getId())) {

                                request.setAttribute("errorNumero", "❌ El número " + numero + " ya está registrado en este equipo");

                                request.setAttribute("numeroRepetido", true);

                                request.setAttribute("nombreJugador", jugador.getNombre());
                                request.setAttribute("numeroJugador", numero);
                                request.setAttribute("posicionJugador", jugador.getPosicion());

                                request.setAttribute("irJugadores", true);

                                List<Equipo> lista2_error = c.listar();
                                List<Jugador> lista_jugadores_error = j.listar_por_equipo(E.getId());

                                request.setAttribute("lista", lista_jugadores_error);
                                request.setAttribute("lista2", lista2_error);

                                request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);

                                return;
                            }

                            // Asignar ID equipo al jugador
                            jugador.setId_equipo(E.getId());

                            // Guardar jugador
                            j.insertar(jugador);

                            // Redireccionar
                            response.sendRedirect("Servlet?accion=irRegistro&mostrarJugadores=true#seccion_jugadores");
                        } else {
                            // Si NO hay equipo activo
                            // (antes esto se guardaba en el
                            // atributo "error", que el JSP nunca
                            // mostraba; ahora usa "errorJugadores",
                            // que si se pinta en pantalla)
                            request.setAttribute("errorJugadores", "❌ Primero debes registrar un equipo");
                            request.setAttribute("lista2", c.listar());
                            request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                        }
                    } catch (NumberFormatException ex) {
                        request.setAttribute("errorNumero", "❌ El número debe ser un valor válido");
                        request.setAttribute("irJugadores", true);
                        request.setAttribute("lista2", c.listar());
                        request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                    }
                    return;
                // =================================================
                // NUEVO EQUIPO
                // =================================================
                case "nuevoEquipo":

                    // ANTES: se validaba el conteo de jugadores de
                    // "el ultimo equipo de la lista". AHORA: se usa
                    // el equipo activo en sesion.
                    Integer idEquipoActivoNuevo
                            = (Integer) request.getSession()
                                    .getAttribute("idEquipoActivo");

                    if (idEquipoActivoNuevo != null && idEquipoActivoNuevo > 0) {

                        Equipo equipoActivo = c.buscarPorId(idEquipoActivoNuevo);

                        // Obtener jugadores
                        List<Jugador> jugadoresEquipo = j.listar_por_equipo(idEquipoActivoNuevo);

                        // Validar mínimo
                        if (jugadoresEquipo.size() < 5) {

                            // OJO: este mensaje es sobre el equipo
                            // ANTERIOR (el que aun no llega al
                            // minimo), no sobre un equipo nuevo.
                            // Se incluye su nombre para que no se
                            // confunda con la lista de un equipo
                            // recien creado (que siempre empieza
                            // vacia).
                            request.setAttribute("errorJugadores",
                                    "❌ \"" + equipoActivo.getNombre()
                                    + "\" necesita mínimo 5 jugadores antes de poder registrar otro equipo");

                            request.setAttribute("lista", jugadoresEquipo);

                            request.setAttribute("lista2", c.listar());

                            request.setAttribute("equipoEditar", equipoActivo);

                            request.setAttribute("irJugadores", true);

                            request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                            return;
                        }

                        // Validar máximo
                        if (jugadoresEquipo.size() > 12) {

                            request.setAttribute("errorJugadores",
                                    "❌ \"" + equipoActivo.getNombre()
                                    + "\" supera el máximo de 12 jugadores");

                            request.setAttribute("lista", jugadoresEquipo);

                            request.setAttribute("lista2", c.listar());

                            request.setAttribute("equipoEditar", equipoActivo);

                            request.setAttribute("irJugadores", true);

                            request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);
                            return;
                        }
                    }

                    // Validacion superada (o no habia equipo
                    // activo todavia) -> se libera el equipo
                    // activo para que el siguiente equipo/jugador
                    // no se mezcle con este.
                    request.getSession().removeAttribute("idEquipoActivo");

                    // Limpiar jugadores
                    request.setAttribute("lista", null);

                    // Enviar equipos
                    request.setAttribute("lista2", c.listar());

                    // Mostrar modal (esto SOLO controla el modal;
                    // el paso visible en el JSP ya no depende de
                    // esta bandera, porque aqui justo queremos
                    // pasar a paso 1, no quedarnos en paso 2)
                    request.setAttribute("jugadoresGuardado", true);

                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);

                    return;

                case "editarEquipo":

                    // Obtener id del equipo
                    int idEquipo1 = Integer.parseInt(request.getParameter("id"));

                    // Buscar equipo
                    Equipo equipoEditar = c.buscarPorId(idEquipo1);

                    // Obtener jugadores del equipo
                    List<Jugador> jugadoresEquipo = j.listar_por_equipo(idEquipo1);

                    // Obtener todos los equipos
                    List<Equipo> listaEquipos = c.listar();

                    // NUEVO: marcar este equipo como el activo en
                    // sesion, para que guardarJugador/nuevoEquipo
                    // trabajen sobre ESTE equipo (y no sobre "el
                    // ultimo de la lista").
                    request.getSession().setAttribute("idEquipoActivo", idEquipo1);

                    // Enviar datos al JSP
                    request.setAttribute("equipoEditar", equipoEditar);

                    request.setAttribute("lista", jugadoresEquipo);

                    request.setAttribute("lista2", listaEquipos);

                    // NUEVO: validar minimo/maximo tambien aqui.
                    // Antes esto solo se validaba en "nuevoEquipo",
                    // por eso al editar no se respetaban estas
                    // reglas. Si el conteo esta mal, se manda a
                    // paso de jugadores (irJugadores=true) para
                    // que se pueda corregir ahi mismo.
                    if (jugadoresEquipo.size() < 5) {

                        request.setAttribute("errorJugadores",
                                "❌ \"" + equipoEditar.getNombre()
                                + "\" necesita mínimo 5 jugadores");

                        request.setAttribute("irJugadores", true);

                    } else if (jugadoresEquipo.size() > 12) {

                        request.setAttribute("errorJugadores",
                                "❌ \"" + equipoEditar.getNombre()
                                + "\" supera el máximo de 12 jugadores");

                        request.setAttribute("irJugadores", true);
                    }

                    // Ir al JSP
                    request.getRequestDispatcher(
                            "pages/registroDeEquipos.jsp"
                    ).forward(request, response);

                    return;

                case "eliminarEquipo":

                    String idEliminarStr = request.getParameter("id");

                    int idEquipoEliminar = 0;

                    if (idEliminarStr != null && !idEliminarStr.trim().isEmpty()) {

                        idEquipoEliminar = Integer.parseInt(idEliminarStr);
                    }

                    c.eliminar(idEquipoEliminar);

                    // NUEVO: si el equipo activo en sesion era el
                    // que se acaba de eliminar, hay que soltarlo
                    // (ya no existe en la BD).
                    Integer idActivoActual
                            = (Integer) request.getSession()
                                    .getAttribute("idEquipoActivo");

                    if (idActivoActual != null
                            && idActivoActual == idEquipoEliminar) {

                        request.getSession().removeAttribute("idEquipoActivo");
                    }

                    List<Equipo> listaEquiposEliminar = c.listar();

                    request.setAttribute("lista2", listaEquiposEliminar);

                    request.setAttribute("lista", null);

                    request.getRequestDispatcher("pages/registroDeEquipos.jsp").forward(request, response);

                    return;

                case "eliminarJugador": {

                    // Obtener id del jugador
                    String idJugadorStr = request.getParameter("idJugador");

                    int idJugadorEliminar = 0;

                    if (idJugadorStr != null
                            && !idJugadorStr.trim().isEmpty()) {

                        idJugadorEliminar
                                = Integer.parseInt(idJugadorStr);
                    }

                    // Obtener id del equipo
                    String idEquipoStr
                            = request.getParameter("idEquipo");

                    int idEquipoEditar = 0;

                    if (idEquipoStr != null
                            && !idEquipoStr.trim().isEmpty()) {

                        idEquipoEditar
                                = Integer.parseInt(idEquipoStr);
                    }

                    // NUEVO: mantener sincronizado el equipo
                    // activo en sesion con el equipo al que
                    // pertenece el jugador que se esta eliminando.
                    request.getSession().setAttribute("idEquipoActivo", idEquipoEditar);

                    // Obtener jugadores actuales ANTES de eliminar
                    List<Jugador> jugadoresActuales
                            = j.listar_por_equipo(idEquipoEditar);

                    // =========================================
                    // VALIDAR MÍNIMO DE JUGADORES
                    // =========================================
                    if (jugadoresActuales.size() <= 5) {

                        // No se puede eliminar, ya tiene el mínimo
                        Equipo equipoEditar1
                                = c.buscarPorId(idEquipoEditar);

                        List<Equipo> listaEquiposJugador
                                = c.listar();

                        request.setAttribute(
                                "errorJugadores",
                                "❌ No se puede eliminar, el equipo necesita mínimo 5 jugadores"
                        );

                        request.setAttribute(
                                "equipoEditar",
                                equipoEditar1
                        );

                        request.setAttribute(
                                "lista",
                                jugadoresActuales
                        );

                        request.setAttribute(
                                "lista2",
                                listaEquiposJugador
                        );

                        request.setAttribute("irJugadores", true);

                        request.getRequestDispatcher(
                                "pages/registroDeEquipos.jsp"
                        ).forward(request, response);

                        return;
                    }

                    // Si pasa la validación, eliminar jugador
                    j.eliminar(idJugadorEliminar);

                    // Buscar equipo que se está editando
                    Equipo equipoEditar1
                            = c.buscarPorId(idEquipoEditar);

                    // Obtener jugadores actualizados
                    List<Jugador> jugadoresActualizados
                            = j.listar_por_equipo(idEquipoEditar);

                    // Obtener equipos
                    List<Equipo> listaEquiposJugador
                            = c.listar();

                    // Enviar datos al JSP
                    request.setAttribute(
                            "equipoEditar",
                            equipoEditar1
                    );

                    request.setAttribute(
                            "lista",
                            jugadoresActualizados
                    );

                    request.setAttribute(
                            "lista2",
                            listaEquiposJugador
                    );

                    request.setAttribute("irJugadores", true);

                    // Regresar al JSP
                    request.getRequestDispatcher(
                            "pages/registroDeEquipos.jsp"
                    ).forward(request, response);

                    return;
                }

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
