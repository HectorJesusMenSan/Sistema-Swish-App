<%-- 
    Document   : partidos
    Created on : 4 may 2026
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="Modelo.Partido"%>
<%@page import="Modelo.Dao.EquipoDao"%>
<%@page import="Modelo.Equipo"%>

<!DOCTYPE html>

<html lang="es" id="html_torneo">

    <head>

        <meta charset="UTF-8">

        <meta name="viewport"
              content="width=device-width, initial-scale=1.0">

        <title>Partidos del Torneo</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <!-- CSS -->
        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/base.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/components.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/layout.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/partidos.css">

    </head>

    <body id="body_torneo">

        <!-- =========================================
             HEADER
        ========================================== -->
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/headerNav.css">

        <div class="header-nav">

            <a href="<%= request.getContextPath()%>/PerfilServlet"
               class="btn-nav-izq">
                👤 Perfil
            </a>

            <span class="header-nav-titulo">Lista de Partidos</span>

            <form action="<%= request.getContextPath()%>/PerfilServlet"
                  method="post" class="form-nav">
                <input type="hidden" name="accion" value="cerrarSesion">
                <button type="submit" class="btn-nav-der">
                    Cerrar sesión
                </button>
            </form>

        </div>

        <!-- =========================================
             CONTENIDO
        ========================================== -->

        <main id="main_torneo"
              class="container py-4">

            <%
                List<Partido> listaPartidos
                        = (List<Partido>) request.getAttribute("listaPartidos");

                EquipoDao equipoDao = new EquipoDao();
            %>

            <!-- SI HAY PARTIDOS -->

            <%
                if (listaPartidos != null
                        && !listaPartidos.isEmpty()) {

                    for (Partido p : listaPartidos) {

                        // Ocultar partidos BYE
                        if (p.getId_equipo_b() == 0) {
                            continue;
                        }

                        Equipo equipoA
                                = equipoDao.buscarPorId(
                                        p.getId_equipo_a()
                                );

                        Equipo equipoB
                                = equipoDao.buscarPorId(
                                        p.getId_equipo_b()
                                );
            %>

            <!-- PARTIDO -->

            <section class="match-card mb-4">

                <!-- ESTADO -->

                <div class="estado-badge pendiente">

                    <%= p.getEstado()%>

                </div>

                <!-- EQUIPOS -->

                <div class="d-flex justify-content-between align-items-center">

                    <!-- EQUIPO A -->

                    <div class="team-box">

                        <%= equipoA.getNombre()%>

                    </div>

                    <!-- VS -->

                    <div class="vs-box">

                        VS

                    </div>

                    <!-- EQUIPO B -->

                    <!-- EQUIPO B -->

                    <div class="team-box">

                        <%= p.getId_equipo_b() == 0 ? "BYE" : equipoB.getNombre()%>

                    </div>

                </div>

                <!-- BOTÓN -->

                <!-- BOTÓN -->

                <div class="mt-3">

                    <% if (p.getId_equipo_b() != 0) { %>

                    <% if ("FINALIZADO".equals(p.getEstado())) {%>

                    <form action="<%= request.getContextPath()%>/ClasificacionServlet"
                          method="get">

                        <input type="hidden"
                               name="accion"
                               value="verClasificacion">

                        <button class="btn btn-estadisticas w-100">
                            VER ESTADÍSTICAS
                        </button>

                    </form>

                    <% } else {%>

                    <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet"
                          method="get">

                        <input type="hidden"
                               name="accion"
                               value="abrirPartido">

                        <input type="hidden"
                               name="idPartido"
                               value="<%= p.getId()%>">

                        <button class="btn btn-main w-100">

                            INICIAR PARTIDO

                        </button>

                    </form>

                    <% } %>

                    <% } else { %>

                    <div class="text-center text-secondary py-2 fw-bold">

                        PASE AUTOMÁTICO

                    </div>

                    <% } %>

                </div>

            </section>

            <%
                    }
                }
            %>

            <!-- SI NO HAY PARTIDOS -->

            <%
                if (listaPartidos == null
                        || listaPartidos.isEmpty()) {
            %>

            <div class="alert alert-warning text-center">

                No existen partidos generados.

            </div>

            <%
                }
            %>

            <%
                // =========================================
                // VERIFICAR SI EL TORNEO REALMENTE TERMINÓ
                // (considerando el posible desempate GRAN_FINAL_2)
                // =========================================
                boolean granFinalFinalizada = false;
                boolean existeGranFinal2 = false;

                if (listaPartidos != null) {

                    for (Partido p : listaPartidos) {

                        if ("GRAN_FINAL_2".equals(p.getBracket())) {

                            existeGranFinal2 = true;

                            if ("FINALIZADO".equals(p.getEstado())) {
                                granFinalFinalizada = true;
                            }
                        }
                    }

                    // Solo si NO hubo desempate, basta con que
                    // la Gran Final normal haya terminado
                    if (!existeGranFinal2) {

                        for (Partido p : listaPartidos) {

                            if ("GRAN_FINAL".equals(p.getBracket())
                                    && "FINALIZADO".equals(p.getEstado())) {

                                granFinalFinalizada = true;
                                break;
                            }
                        }
                    }
                }
            %>

            <% if (granFinalFinalizada) {%>

            <div class="container mb-5">

                <form action="<%= request.getContextPath()%>/TorneoServlet"
                      method="post">

                    <input type="hidden" name="accion" value="finalizarTorneo">

                    <input type="hidden"
                           name="idTorneo"
                           value="<%
                               Integer idSesion = (Integer) session.getAttribute("idTorneoActivo");
                               if (idSesion != null) {
                                   out.print(idSesion);
                               }
                           %>">

                    <button class="btn btn-main w-100 fw-bold">

                        🏆 FINALIZAR TORNEO

                    </button>

                </form>

            </div>

            <% }%>

        </main>

        <!-- =========================================
             NAVBAR
        ========================================== -->



    </body>

</html>
