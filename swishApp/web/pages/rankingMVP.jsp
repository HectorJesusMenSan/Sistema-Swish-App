<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="Modelo.RankingJugador"%>

<%
    RankingJugador mvp
            = (RankingJugador) request.getAttribute("mvp");

    List<RankingJugador> ranking
            = (List<RankingJugador>) request.getAttribute("ranking");

    String mejorPartido
            = (String) request.getAttribute("mejorPartido");
%>

<!DOCTYPE html>
<html lang="es">

    <head>

        <meta charset="UTF-8">

        <meta name="viewport"
              content="width=device-width, initial-scale=1.0">

        <title>Ranking MVP</title>

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
              href="<%= request.getContextPath()%>/CSS/rankingMVP.css">
                <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/headerNav.css">
    </head>

    <body>

        <!-- ========================================= -->
        <!-- HEADER -->
        <!-- ========================================= -->
        <div class="header-nav">

            <a href="<%= request.getContextPath()%>/ClasificacionServlet?accion=verClasificacion"
               class="btn-nav-izq">
                ← Volver
            </a>

            <span class="header-nav-titulo">Ranking MVP</span>

            <form action="<%= request.getContextPath()%>/PerfilServlet"
                  method="post" class="form-nav">
                <input type="hidden" name="accion" value="cerrarSesion">
                <button type="submit" class="btn-nav-der">
                    Cerrar sesión
                </button>
            </form>

        </div>

        <!-- ========================================= -->
        <!-- MVP -->
        <!-- ========================================= -->

        <% if (mvp != null) {%>

        <section id="ranking_destacado"
                 class="p-3 m-3">

            <div id="ranking_header">

                <div id="foto_mvp">

                    <%= mvp.getJugador().getNumero()%>

                </div>

                <div id="info_mvp">

                    <span class="label-top">

                        MVP DEL TORNEO

                    </span>

                    <h5 id="nombre_mvp">

                        <%= mvp.getJugador().getNombre()%>

                    </h5>

                    <p id="equipo_mvp">

                        <%= mvp.getEquipo().getNombre()%>

                    </p>

                </div>

            </div>

            <div id="stats_mvp">

                <div class="stat-mini text-center">

                    <span>

                        <%= mvp.getPuntos()%>

                    </span>

                    <p>PTS</p>

                </div>

                <div class="stat-mini text-center">

                    <span>

                        <%= mvp.getFaltas()%>

                    </span>

                    <p>Fouls</p>

                </div>

                <div class="stat-mini text-center">

                    <span>

                        <%= mvp.getPartidosJugados()%>

                    </span>

                    <p>GP</p>

                </div>

                <div class="stat-mini text-center">

                    <span>

                        <%= String.format("%.1f",
                            mvp.getPromedio())%>

                    </span>

                    <p>PPG</p>

                </div>

            </div>

        </section>

        <% }%>

        <!-- ========================================= -->
        <!-- MEJOR PARTIDO -->
        <!-- ========================================= -->

        <section id="mejorPartido"
                 class="m-3 p-3 card">

            <h5>

                Mejor Partido

            </h5>

            <p>

                <%= mejorPartido != null
                        ? mejorPartido
                        : "Sin datos disponibles"%>

            </p>

            <% if (mvp != null) {%>

            <p class="text-secondary">

                Máximo registro individual:
                <strong>

                    <%= mvp.getMejorPartidoPuntos()%>

                </strong>

                puntos

            </p>

            <% } %>

        </section>

        <!-- ========================================= -->
        <!-- TABLA -->
        <!-- ========================================= -->

        <section class="m-3">

            <h5>

                Top 10 Rendimiento

            </h5>

            <div id="tabla_ranking_nuevo">

                <div class="fila encabezado">

                    <div>#</div>

                    <div>Jugador</div>

                </div>

                <%
                    if (ranking != null) {

                        int pos = 1;

                        for (RankingJugador r : ranking) {
                %>

                <div class="fila <%= pos == 1 ? "lider" : ""%>">

                    <div>

                        <%= pos%>

                    </div>

                    <div>

                        <%= r.getJugador().getNombre()%>

                        <% if (pos == 1) { %>

                        <span class="badge-mini">

                            TOP

                        </span>

                        <% }%>

                        <br>

                        <small>

                            <%= r.getEquipo().getNombre()%>

                            |
                            Score:
                            <%= String.format("%.1f",
                                r.getScore())%>

                        </small>

                    </div>

                </div>

                <%
                            pos++;
                        }
                    }
                %>

            </div>

        </section>

        <!-- ========================================= -->
        <!-- FORMULA -->
        <!-- ========================================= -->

        <section id="criterio_box"
                 class="m-3 p-3 text-center">

            <p id="texto_criterio">

                Score = Puntos - (Faltas × 0.5)

            </p>

        </section>

    </body>

</html>