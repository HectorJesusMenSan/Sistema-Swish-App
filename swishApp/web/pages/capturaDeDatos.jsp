<%-- 
    Document   : capturaDeDatos
    Created on : 4 may 2026
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="Modelo.Equipo"%>
<%@page import="Modelo.Jugador"%>
<%@page import="Modelo.Partido"%>
<%@page import="Modelo.Dao.EstadisticasDao"%>

<!DOCTYPE html>

<html lang="es">

    <head>

        <meta charset="UTF-8">

        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Captura de Datos</title>

        <!-- Bootstrap -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- CSS -->
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">

        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/capturaDatos.css">

    </head>

    <body>

        <%
            // ================= DATOS =================

            Partido partido = (Partido) request.getAttribute("partido");

            Equipo equipoA = (Equipo) request.getAttribute("equipoA");

            Equipo equipoB = (Equipo) request.getAttribute("equipoB");

            List<Jugador> jugadoresA = (List<Jugador>) request.getAttribute("jugadoresA");

            List<Jugador> jugadoresB = (List<Jugador>) request.getAttribute("jugadoresB");

            EstadisticasDao estDao = new EstadisticasDao();

            // ================= PESTAÑA ACTIVA =================
            String pestañaActiva = request.getParameter("equipo");

            if (pestañaActiva == null) {
                pestañaActiva = "A";
            }
        %>

        <!-- =========================================
             HEADER
        ========================================== -->

        <div id="header_partido" class="p-3 text-center position-sticky top-0">

            <!-- MARCADOR -->

            <h3 id="marcador" class="fw-bold m-0">

                <%
                    int marcadorA = estDao.obtenerPuntosEquipo(partido.getId(), equipoA.getId());

                    int marcadorB = estDao.obtenerPuntosEquipo(partido.getId(), equipoB.getId());
                %>

                <%= marcadorA%> - <%= marcadorB%>

            </h3>

            <!-- EQUIPOS -->

            <small id="equipos">

                <%= equipoA.getNombre()%> vs <%= equipoB.getNombre()%>

            </small>

            <!-- RONDA -->

            <div id="ronda" class="position-absolute end-0 top-0 p-3 fw-bold">

                Ronda 1

            </div>

        </div>

        <!-- =========================================
             PESTAÑAS
        ========================================== -->

        <div class="container py-3">

            <div class="d-flex bg-dark rounded-pill p-1">

                <!-- BOTON EQUIPO A -->

                <button id="btnEquipoA"
                        class="btn w-50 fw-bold rounded-pill <%= pestañaActiva.equals("A") ? "equipo-activo" : "equipo-inactivo"%>"
                        type="button"
                        onclick="mostrarEquipo('A')">

                    <%= equipoA.getNombre()%>

                </button>

                <!-- BOTON EQUIPO B -->

                <button id="btnEquipoB"
                        class="btn w-50 fw-bold rounded-pill <%= pestañaActiva.equals("B") ? "equipo-activo" : "equipo-inactivo"%>"
                        type="button"
                        onclick="mostrarEquipo('B')">

                    <%= equipoB.getNombre()%>

                </button>

            </div>

        </div>

        <!-- =========================================
             CONTENEDOR GENERAL
        ========================================== -->

        <div class="container pb-5">

            <!-- =====================================
                 EQUIPO A
            ====================================== -->

            <div id="equipoA" style="<%= pestañaActiva.equals("A") ? "display:block;" : "display:none;"%>">

                <%
                    for (Jugador j : jugadoresA) {

                        int puntosJugador = estDao.obtenerPuntos(partido.getId(), j.getId());

                        int faltasJugador = estDao.obtenerFaltas(partido.getId(), j.getId());
                %>

                <!-- CARD JUGADOR -->

                <div class="card jugador-card mb-3 <%= (faltasJugador >= 5) ? "jugador-expulsado" : ""%>">

                    <div class="d-flex justify-content-between">

                        <!-- DATOS -->

                        <div class="d-flex align-items-center gap-3">

                            <div class="numero-jugador">

                                <%= j.getNumero()%>

                            </div>

                            <div>

                                <div class="fw-bold">

                                    <%= j.getNombre()%>

                                </div>

                                <small class="text-secondary">

                                    <%= equipoA.getOrigen()%>

                                </small>

                            </div>

                        </div>

                        <!-- ESTADISTICAS -->

                        <div class="text-end">

                            <div class="puntos">

                                <%= puntosJugador%> PTS

                            </div>

                            <small class="faltas">

                                Faltas: <%= faltasJugador%>

                            </small>

                        </div>

                    </div>

                    <!-- BOTONES -->

                    <div class="d-flex gap-2 mt-3">

                        <!-- +1 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="1">

                            <input type="hidden" name="equipo" value="A">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +1

                            </button>

                        </form>

                        <!-- +2 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="2">

                            <input type="hidden" name="equipo" value="A">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +2

                            </button>

                        </form>

                        <!-- +3 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="3">

                            <input type="hidden" name="equipo" value="A">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +3

                            </button>

                        </form>

                        <!-- FALTA -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarFalta">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="equipo" value="A">

                            <button class="btn btn-falta w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                Falta

                            </button>

                        </form>

                    </div>

                </div>

                <%
                    }
                %>

            </div>

            <!-- =====================================
                 EQUIPO B
            ====================================== -->

            <div id="equipoB" style="<%= pestañaActiva.equals("B") ? "display:block;" : "display:none;"%>">

                <%
                    for (Jugador j : jugadoresB) {

                        int puntosJugador = estDao.obtenerPuntos(partido.getId(), j.getId());

                        int faltasJugador = estDao.obtenerFaltas(partido.getId(), j.getId());
                %>

                <!-- CARD JUGADOR -->

                <div class="card jugador-card mb-3 <%= (faltasJugador >= 5) ? "jugador-expulsado" : ""%>">

                    <div class="d-flex justify-content-between">

                        <!-- DATOS -->

                        <div class="d-flex align-items-center gap-3">

                            <div class="numero-jugador">

                                <%= j.getNumero()%>

                            </div>

                            <div>

                                <div class="fw-bold">

                                    <%= j.getNombre()%>

                                </div>

                                <small class="text-secondary">

                                    <%= equipoB.getOrigen()%>

                                </small>

                            </div>

                        </div>

                        <!-- ESTADISTICAS -->

                        <div class="text-end">

                            <div class="puntos">

                                <%= puntosJugador%> PTS

                            </div>

                            <small class="faltas">

                                Faltas: <%= faltasJugador%>

                            </small>

                        </div>

                    </div>

                    <!-- BOTONES -->

                    <div class="d-flex gap-2 mt-3">

                        <!-- +1 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="1">

                            <input type="hidden" name="equipo" value="B">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +1

                            </button>

                        </form>

                        <!-- +2 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="2">

                            <input type="hidden" name="equipo" value="B">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +2

                            </button>

                        </form>

                        <!-- +3 -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarPuntos">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="puntos" value="3">

                            <input type="hidden" name="equipo" value="B">

                            <button class="btn btn-accion w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                +3

                            </button>

                        </form>

                        <!-- FALTA -->

                        <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                            <input type="hidden" name="accion" value="sumarFalta">

                            <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                            <input type="hidden" name="idJugador" value="<%= j.getId()%>">

                            <input type="hidden" name="equipo" value="B">

                            <button class="btn btn-falta w-100" <%= (faltasJugador >= 5) ? "disabled" : ""%>>

                                Falta

                            </button>

                        </form>

                    </div>

                </div>

                <%
                    }
                %>

            </div>

        </div>

        <!-- =========================================
             FOOTER
        ========================================== -->

        <div id="footer_partido" class="position-fixed bottom-0 w-100 p-3">

            <div class="container d-flex gap-3">

                

                <form action="<%= request.getContextPath()%>/CapturaDeDatosServlet" method="post" style="width:100%;">

                    <input type="hidden" name="accion" value="finalizarPartido">

                    <input type="hidden" name="idPartido" value="<%= partido.getId()%>">

                    <button class="btn btn-main w-100 fw-bold">

                        Finalizar Partido

                    </button>

                </form>

            </div>

        </div>

        <!-- =========================================
             SCRIPT
        ========================================== -->

        <script>

            // ================= CAMBIAR PESTAÑA =================

            function mostrarEquipo(equipo) {

                const equipoA = document.getElementById("equipoA");

                const equipoB = document.getElementById("equipoB");

                const btnA = document.getElementById("btnEquipoA");

                const btnB = document.getElementById("btnEquipoB");

                if (equipo === 'A') {

                    equipoA.style.display = "block";

                    equipoB.style.display = "none";

                    btnA.classList.add("equipo-activo");

                    btnA.classList.remove("equipo-inactivo");

                    btnB.classList.add("equipo-inactivo");

                    btnB.classList.remove("equipo-activo");

                } else {

                    equipoA.style.display = "none";

                    equipoB.style.display = "block";

                    btnB.classList.add("equipo-activo");

                    btnB.classList.remove("equipo-inactivo");

                    btnA.classList.add("equipo-inactivo");

                    btnA.classList.remove("equipo-activo");
                }
            }

        </script>

    </body>

</html>