<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="Modelo.ClasificacionEquipo"%>

<%
    ClasificacionEquipo lider
            = (ClasificacionEquipo) request.getAttribute("lider");

    List<ClasificacionEquipo> clasificacion
            = (List<ClasificacionEquipo>) request.getAttribute("clasificacion");

    // =========================================
    // DESTINO DEL BOTON "VOLVER"
    // Si se entro desde Ver Torneos, regresa ahi.
    // Si no, regresa a la lista de partidos
    // (comportamiento de siempre).
    // =========================================
    String origen = (String) request.getAttribute("origen");

    String urlVolver = "torneos".equals(origen)
            ? request.getContextPath() + "/TorneoServlet?accion=verTorneos"
            : request.getContextPath() + "/PartidosServerlet";
%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">

        <meta name="viewport"
              content="width=device-width, initial-scale=1.0">

        <title>Clasificación del Torneo</title>

        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css"
              rel="stylesheet">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/tables.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/estadisticas.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/headerNav.css">

    </head>

    <body>

        <!-- =========================================
             HEADER
        ========================================== -->
        <div class="header-nav">

            <a href="<%= urlVolver%>"
               class="btn-nav-izq">
                ← Volver
            </a>

            <span class="header-nav-titulo">Clasificación General</span>

            <form action="<%= request.getContextPath()%>/PerfilServlet"
                  method="post" class="form-nav">
                <input type="hidden" name="accion" value="cerrarSesion">
                <button type="submit" class="btn-nav-der">
                    Cerrar sesión
                </button>
            </form>

        </div>

        <!-- =========================================
             BOTÓN VER RANKING MVP
        ========================================== -->
        <div class="container mt-3">

            <a href="<%= request.getContextPath()%>/RankingServlet?accion=verRanking"
               style="
               display: block;
               background: rgba(244,123,37,0.15);
               color: #f47b25;
               border: 1px solid #f47b25;
               border-radius: 12px;
               padding: 12px;
               text-align: center;
               text-decoration: none;
               font-weight: bold;
               font-size: 0.95rem;
               ">
                🏅 Ver Mejor Jugador MVP
            </a>

        </div>

        <!-- =========================================
             EQUIPO LÍDER
        ========================================== -->
        <% if (lider != null) {%>

        <div class="container mt-4">

            <div id="equipo_destacado" class="p-4">

                <span id="label_lider">LÍDER DEL TORNEO</span>

                <h4 id="nombre_equipo_destacado">
                    <%= lider.getEquipo().getNombre()%>
                </h4>

                <div id="icono_trofeo">🏆</div>

                <div class="row mt-3 text-center">

                    <div class="col stat-box">
                        <span><%= lider.getPj()%></span>
                        <p>PJ</p>
                    </div>

                    <div class="col stat-box">
                        <span><%= lider.getVictorias()%> / <%= lider.getDerrotas()%></span>
                        <p>V/D</p>
                    </div>

                    <div class="col stat-box">
                        <span><%= lider.getPuntosFavor()%></span>
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

        <!-- =========================================
             TABLA GENERAL
        ========================================== -->
        <div class="container mt-4">

            <h5>Clasificación General</h5>

            <div class="table-responsive">

                <table id="tabla_equipos" class="table table-dark mt-3">

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
                            <td><%= posicion++%></td>
                            <td><%= c.getEquipo().getNombre()%></td>
                            <td><%= c.getPj()%></td>
                            <td><%= c.getVictorias()%> / <%= c.getDerrotas()%></td>
                            <td><%= c.getPuntosFavor()%></td>
                            <td><%= c.getPuntosContra()%></td>
                            <td>
                                <% if (c.getDiferencia() >= 0) {%>
                                <span style="color:#f47b25;">+<%= c.getDiferencia()%></span>
                                <% } else {%>
                                <span style="color:red;"><%= c.getDiferencia()%></span>
                                <% }%>
                            </td>
                            <td><%= c.getFaltas()%></td>
                        </tr>
                        <%
                                }
                            }
                        %>
                    </tbody>

                </table>

            </div>

        </div>

        <!-- =========================================
             CRITERIOS
        ========================================== -->
        <div class="container mt-4 mb-5">

            <div id="criterio_box" class="p-4">

                <h5>Criterio de Clasificación</h5>

                <p id="texto_criterio">
                    La tabla se ordena por cantidad de victorias.
                    En caso de empate se utiliza la diferencia de puntos.
                </p>

                <div class="criterio-item">
                    <span>Victorias</span>
                    <span class="badge-criterio primary">Prioridad 1</span>
                </div>

                <div class="criterio-item">
                    <span>Diferencia de puntos</span>
                    <span class="badge-criterio">Prioridad 2</span>
                </div>

                <div class="criterio-item">
                    <span>Faltas acumuladas</span>
                    <span class="badge-criterio danger">Estadística</span>
                </div>

            </div>

        </div>

    </body>

</html>
