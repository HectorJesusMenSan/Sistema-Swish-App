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

        <header id="header_torneo"
                class="text-center py-4">

            <h2 id="titulo_torneo">

                Gráfica del Torneo

            </h2>

            <p id="subtitulo_torneo">

                Avance de partidos

            </p>

        </header>

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
                // Verificar si la Gran Final está finalizada
                boolean granFinalFinalizada = false;

                if (listaPartidos != null) {

                    for (Partido p : listaPartidos) {

                        if ("GRAN_FINAL".equals(p.getBracket())
                                && "FINALIZADO".equals(p.getEstado())) {

                            granFinalFinalizada = true;
                            break;
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

        <footer id="nav_app"
                class="d-flex justify-content-around align-items-center">

            <div class="nav-item active">

                PARTIVOS

            </div>

            <div class="nav-item">

                ESTADÍSTICAS

            </div>

            <div class="nav-item">

                RANKING

            </div>

        </footer>

    </body>

</html>