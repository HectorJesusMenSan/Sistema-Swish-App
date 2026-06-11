<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="Modelo.ClasificacionEquipo"%>

<%
    ClasificacionEquipo lider
            = (ClasificacionEquipo) request.getAttribute("lider");

    List<ClasificacionEquipo> clasificacion
            = (List<ClasificacionEquipo>) request.getAttribute("clasificacion");
%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">

        <title>Clasificación del Torneo</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/base.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/components.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/layout.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/tables.css">

        <link rel="stylesheet"
              href="<%= request.getContextPath()%>/CSS/estadisticas.css">

    </head>

    <body>

        <!-- HEADER -->
        <header id="header_clasificacion"
                class="p-3 d-flex justify-content-between align-items-center">

            <button class="btn btn-secundario"
                    onclick="history.back()">
                ←
            </button>

            <h5 id="titulo_torneo">
                Clasificación General
            </h5>

            <div style="width:40px"></div>

        </header>

        <!-- EQUIPO LÍDER -->

        <% if (lider != null) {%>

        <div class="container mt-4">

            <div id="equipo_destacado"
                 class="p-4">

                <span id="label_lider">
                    LÍDER DEL TORNEO
                </span>

                <h4 id="nombre_equipo_destacado">
                    <%= lider.getEquipo().getNombre()%>
                </h4>

                <div id="icono_trofeo">
                    🏆
                </div>

                <div class="row mt-3 text-center">

                    <div class="col stat-box">

                        <span>
                            <%= lider.getPj()%>
                        </span>

                        <p>PJ</p>

                    </div>

                    <div class="col stat-box">

                        <span>
                            <%= lider.getVictorias()%> /
                            <%= lider.getDerrotas()%>
                        </span>

                        <p>V/D</p>

                    </div>

                    <div class="col stat-box">

                        <span>
                            <%= lider.getPuntosFavor()%>
                        </span>

                        <p>PTS+</p>

                    </div>

                    <div class="col stat-box">

                        <span style="color:#f47b25;">

                            <%= lider.getDiferencia() >= 0
                                    ? "+" + lider.getDiferencia()
                                    : lider.getDiferencia()%>

                        </span>

                        <p>DIF</p>

                    </div>

                </div>

            </div>

        </div>

        <% } %>

        <!-- TABLA GENERAL -->

        <div class="container mt-4">

            <h5>
                Clasificación General
            </h5>

            <table id="tabla_equipos"
                   class="table table-dark mt-3">

                <thead>

                    <tr>

                        <th>Pos</th>
                        <th>Equipo</th>
                        <th>PJ</th>
                        <th>V/D</th>
                        <th>Pts+</th>
                        <th>Pts-</th>
                        <th>Dif</th>
                        <th>Faltas</th>

                    </tr>

                </thead>

                <tbody>

                    <%
                        if (clasificacion != null) {

                            int posicion = 1;

                            for (ClasificacionEquipo c : clasificacion) {
                    %>

                    <tr class="<%= posicion == 1 ? "fila-lider" : ""%>">

                        <td>
                            <%= posicion++%>
                        </td>

                        <td>
                            <%= c.getEquipo().getNombre()%>
                        </td>

                        <td>
                            <%= c.getPj()%>
                        </td>

                        <td>
                            <%= c.getVictorias()%> /
                            <%= c.getDerrotas()%>
                        </td>

                        <td>
                            <%= c.getPuntosFavor()%>
                        </td>

                        <td>
                            <%= c.getPuntosContra()%>
                        </td>

                        <td>

                            <% if (c.getDiferencia() >= 0) {%>

                            <span style="color:#f47b25;">
                                +<%= c.getDiferencia()%>
                            </span>

                            <% } else {%>

                            <span style="color:red;">
                                <%= c.getDiferencia()%>
                            </span>

                            <% }%>

                        </td>

                        <td>
                            <%= c.getFaltas()%>
                        </td>

                    </tr>

                    <%
                            }
                        }
                    %>

                </tbody>

            </table>

        </div>

        <!-- CRITERIOS -->

        <div class="container mt-4 mb-5">

            <div id="criterio_box"
                 class="p-4">

                <h5>
                    Criterio de Clasificación
                </h5>

                <p id="texto_criterio">

                    La tabla se ordena por cantidad de victorias.
                    En caso de empate se utiliza la diferencia
                    de puntos.

                </p>

                <div class="criterio-item">

                    <span>Victorias</span>

                    <span class="badge-criterio primary">
                        Prioridad 1
                    </span>

                </div>

                <div class="criterio-item">

                    <span>Diferencia de puntos</span>

                    <span class="badge-criterio">
                        Prioridad 2
                    </span>

                </div>

                <div class="criterio-item">

                    <span>Faltas acumuladas</span>

                    <span class="badge-criterio danger">
                        Estadística
                    </span>

                </div>

            </div>

        </div>


        <footer id="nav_app"
                class="d-flex justify-content-around align-items-center">

            <a href="<%=request.getContextPath()%>/PartidosServerlet"
               class="nav-item text-decoration-none">

                PARTIDOS

            </a>

            <a href="<%=request.getContextPath()%>/ClasificacionServlet?accion=verClasificacion"
               class="nav-item text-decoration-none active" >

                ESTADÍSTICAS

            </a>

            <a href="<%=request.getContextPath()%>/RankingServlet?accion=verRanking"
               class="nav-item text-decoration-none">

                RANKING

            </a>

        </footer>
    </body>

</html>