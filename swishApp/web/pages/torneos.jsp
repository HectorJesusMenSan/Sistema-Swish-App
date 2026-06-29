<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.Torneo"%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Torneos</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/partidos.css">

    </head>

    <body>

        <!-- =========================================
             HEADER
        ========================================== -->
        <header id="header_torneo" class="text-center py-4">

            <h2 id="titulo_torneo">Torneos</h2>

            <p id="subtitulo_torneo">Historial de torneos</p>

        </header>

        <!-- =========================================
             CONTENIDO
        ========================================== -->
        <main id="main_torneo" class="container py-4">

            <%
                List<Torneo> listaTorneos
                        = (List<Torneo>) request.getAttribute("listaTorneos");

                if (listaTorneos != null && !listaTorneos.isEmpty()) {

                    for (Torneo t : listaTorneos) {
            %>

            <!-- CARD TORNEO -->
            <section class="match-card mb-4">

                <!-- ESTADO -->
                <div class="estado-badge pendiente">

                    <%= t.getEstado()%>

                </div>

                <!-- INFO -->
                <div class="d-flex justify-content-between align-items-center mt-2">

                    <div>

                        <div class="team-box">

                            <%= t.getNombre()%>

                        </div>

                        <small class="text-secondary">

                            <%= t.getFecha_inicio()%>

                        </small>

                    </div>

                    <!-- ELIMINAR -->
                    <form action="<%= request.getContextPath()%>/TorneoServlet"
                          method="post">

                        <input type="hidden" name="accion" value="eliminarTorneo">
                        <input type="hidden" name="idTorneo" value="<%= t.getId()%>">

                        <button class="btn btn-sm btn-danger">
                            Eliminar
                        </button>

                    </form>

                </div>

                <!-- BOTONES -->
                <div class="mt-3">

                    <% if ("FINALIZADO".equals(t.getEstado())) {%>

                    <!-- VER ESTADÍSTICAS -->
                    <a href="<%= request.getContextPath()%>/ClasificacionServlet?accion=verClasificacion&idTorneo=<%= t.getId()%>&origen=torneos"
                       class="btn btn-estadisticas w-100">

                        VER ESTADÍSTICAS

                    </a>

                    <% } else {%>

                    <!-- CONTINUAR -->
                    <a href="<%= request.getContextPath()%>/TorneoServlet?accion=continuarTorneo&idTorneo=<%= t.getId()%>"
                       class="btn btn-main w-100">

                        CONTINUAR

                    </a>

                    <% } %>

                </div>

            </section>

            <%
                }

            } else {
            %>

            <div class="alert alert-warning text-center">
                No hay torneos registrados.
            </div>

            <%
                }
            %>

        </main>

        <!-- =========================================
             FOOTER
        ========================================== -->
        <footer id="nav_app"
                class="d-flex justify-content-around align-items-center">

            <div class="nav-item">

                <a href="<%= request.getContextPath()%>/index.jsp"
                   style="text-decoration:none; color:inherit;">
                    INICIO
                </a>

            </div>

            <div class="nav-item active">

                TORNEOS

            </div>

        </footer>

    </body>

</html>
