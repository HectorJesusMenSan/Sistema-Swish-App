<%-- 
    Document   : registroDeEquipos
    Created on : 4 may 2026, 8:56:37 a.m.
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<%@page import="Modelo.Jugador"%>
<%@page import="Modelo.Equipo"%>

<!DOCTYPE html>

<html lang="es">

    <head>

        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Registro de Equipos</title>

        <!-- BOOTSTRAP -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <c:set var="path" value="${pageContext.request.contextPath}" />

        <!-- CSS -->
        <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/layout.css">
        <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/registroEquipos.css">

    </head>

    <body>

        <!-- ======================================================
             🔹 HEADER
        ====================================================== -->

        <header id="header_main">

            <h2 id="titulo_principal">
                Registro de Equipos
            </h2>

        </header>

        <!-- ======================================================
             🔹 CONTENIDO
        ====================================================== -->

        <main class="container pb-5">

            <!-- ================= DATOS DEL EQUIPO ================= -->

            <section id="seccion_equipo" class="card mb-4">
                <%
                Equipo equipoEditar = (Equipo) request.getAttribute("equipoEditar");
                %>
                <form action="<%= request.getContextPath() %>/Servlet" method="post">

                    <input type="hidden" name="accion" value="guardarEquipo" value="<%= equipoEditar != null ? equipoEditar.getId() : 0 %>">

                    <h5>Datos del equipo</h5>

                    <!-- NOMBRE -->
                    <div class="mb-3">

                        <label>Nombre del equipo</label>

                        <input id="input_nombre_equipo"
                               name="nombre"
                               type="text"
                               pattern="^[^\d]*$" 
                               title="Solo se permiten letras y espacios, sin números"
                               class="form-control"
                               placeholder="Ej. Halcones del Norte"
                               value="<%= equipoEditar != null ? equipoEditar.getNombre() : "" %>"
                               required>

                    </div>

                    <!-- ORIGEN -->
                    <div class="mb-3">

                        <label>Origen</label>

                        <input id="input_origen"
                               name="origen"
                               type="text"
                               pattern="^[^\d]*$" 
                               title="Solo se permiten letras y espacios, sin números"
                               class="form-control"
                               placeholder="Ciudad o club"
                               value="<%= equipoEditar != null ? equipoEditar.getOrigen() : "" %>"
                               required>

                    </div>

                    <!-- CATEGORIA -->
                    <div class="mb-3">

                        <label>Categoria</label>

                        <input id="input_categoria"
                               name="categoria"
                               type="text"
                               pattern="^[^\d]*$" 
                               title="Solo se permiten letras y espacios, sin números"
                               class="form-control"
                               placeholder="Categoria que pertenece"
                               value="<%= equipoEditar != null ? equipoEditar.getCategoria() : "" %>"
                               required>

                    </div>

                    <!-- BOTÓN -->
                    <button id="btn_agregar_equipo"
                            type="submit"
                            class="btn btn-warning w-100">

                        Guardar

                    </button>

                </form>

            </section>

            <!-- ================= JUGADORES ================= -->

            <section id="seccion_jugadores" class="card mb-4">

                <form action="<%= request.getContextPath() %>/Servlet" method="post">

                    <input type="hidden" name="accion" value="guardarJugador">

                    <h5>Integrantes</h5>

                    <!-- NOMBRE -->
                    <div class="mb-3">

                        <label>Nombre del jugador</label>

                        <input id="input_jugador"
                               name="nombre"
                               type="text"
                               pattern="^[^\d]*$" 
                               title="Solo se permiten letras y espacios, sin números"
                               class="form-control">

                    </div>

                    <!-- DORSAL Y POSICIÓN -->
                    <div class="row">

                        <div class="col-4">

                            <label>Dorsal o Numero</label>

                            <input id="input_dorsal"
                                   type="number"
                                   name="numero"
                                   min="0"
                                   class="form-control">

                        </div>

                        <div class="col-8">

                            <label>Posición</label>

                            <select id="select_posicion"
                                    name="posicion"
                                    class="form-control">

                                <option>Base (PG)</option>
                                <option>Escolta (SG)</option>
                                <option>Alero (SF)</option>
                                <option>Ala-Pívot (PF)</option>
                                <option>Pívot (C)</option>

                            </select>

                        </div>

                    </div>

                    <!-- BOTÓN -->
                    <button id="btn_agregar_jugador"
                            type="submit"
                            class="btn btn-outline-light w-100 mt-3"
                            >

                        Agregar jugador

                    </button>

                </form>

                <!-- LISTA -->

                <div class="mt-4">

                    <%
                        List<Jugador> lista =
                            (List<Jugador>) request.getAttribute("lista");
                    %>

                    <h6>Lista de jugadores</h6>

                    <ul id="lista_jugadores" class="list-group">

                        <%
                            if (lista != null) {

                                for (Jugador j : lista) {
                        %>

                        <li class="list-group-item d-flex justify-content-between align-items-center">

                            <span>

                                #<%= j.getNumero() %>
                                <%= j.getNombre() %>

                            </span>

                            <form action="<%= request.getContextPath()%>/Servlet"
                                  method="post"
                                  style="display:inline;">

                                <input type="hidden"
                                       name="accion"
                                       value="eliminarJugador">

                                <input type="hidden"
                                       name="idJugador"
                                       value="<%= j.getId()%>">
                                <input type="hidden"
                                       name="idEquipo"
                                       value="<%= j.getId_equipo()%>">

                                <button type="submit"
                                        class="btn btn-sm btn-danger">

                                    X

                                </button>

                            </form>

                        </li>

                        <%
                                }
                            }
                        %>

                    </ul>

                </div>

                <form action="<%= request.getContextPath() %>/Servlet" method="post">

                    <input type="hidden" name="accion" value="nuevoEquipo">

                    <button id="btn_agregar_otro_equipo"
                            type="submit"
                            class="btn btn-outline-light w-100 mt-3">

                        Agregar nuevo equipo

                    </button>

                </form>
                
            <!-- Mostrar error si el número está repetido -->
            <% if (request.getAttribute("errorNumero") != null) { %>
                <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                    <strong>¡Error!</strong> <%= request.getAttribute("errorNumero") %>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
            <% } %>
            <!-- Error cantidad de jugadores -->
            <% if (request.getAttribute("errorJugadores") != null) {%>

            <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">

                <strong>¡Error!</strong>
                <%= request.getAttribute("errorJugadores")%>

                <button type="button"
                        class="btn-close"
                        data-bs-dismiss="alert">
                </button>

            </div>

            <% }%>

            </section>

            <!-- ================= EQUIPOS REGISTRADOS ================= -->

            <section id="seccion_equipos_registrados">
                <form action="<%= request.getContextPath() %>/PartidosServerlet" method="post">

                    <!-- FINALIZAR -->
                    <input type="hidden" name="accion" value="generarPartidos">
                    <button id="btn_finalizar_equipo"
                            class="btn btn-warning w-100 mt-2 mb-2">

                        Finalizar registro

                    </button>
                </form>

                <h5>Equipos registrados</h5>

                <%
                    List<Equipo> lista2 =
                        (List<Equipo>) request.getAttribute("lista2");
                %>

                <%
                    if (lista2 != null) {

                        for (Equipo e : lista2) {
                %>

                <!-- CARD -->

                <div class="card mb-3 d-flex flex-row justify-content-between align-items-center p-3">

                    <div>

                        <h6><%= e.getNombre() %></h6>

                        <small>

                            <%= e.getOrigen() %>
                            --
                            <%= e.getCategoria() %>

                        </small>

                    </div>

                            <div class="d-flex gap-2">

                                <!-- EDITAR -->
                                <form action="<%= request.getContextPath()%>/Servlet"
                                      method="post">

                                    <input type="hidden"
                                           name="accion"
                                           value="editarEquipo">

                                    <input type="hidden"
                                           name="id"
                                           value="<%= e.getId()%>">

                                    <button type="submit"
                                            class="btn btn-sm btn-warning">

                                        Editar

                                    </button>

                                </form>

                                <!-- ELIMINAR -->
                                <form action="<%= request.getContextPath()%>/Servlet"
                                      method="post">

                                    <input type="hidden"
                                           name="accion"
                                           value="eliminarEquipo">

                                    <input type="hidden"
                                           name="id"
                                           value="<%= e.getId()%>">

                                    <button type="submit"
                                            class="btn btn-sm btn-danger">

                                        Eliminar

                                    </button>

                                </form>

                            </div>

                </div>

                <%
                        }
                    }
                %>

            </section>

        </main>

        <!-- ======================================================
             🔹 MODAL JUGADOR
        ====================================================== -->

        <div class="modal fade" id="modalJugador" tabindex="-1">

            <div class="modal-dialog modal-dialog-centered">

                <div class="modal-content bg-dark text-white">

                    <div class="modal-header border-secondary">

                        <h5 class="modal-title">
                            Jugadores registrados
                        </h5>

                    </div>

                    <div class="modal-body">

                        Los jugadores se agregaron correctamente.

                    </div>

                    <div class="modal-footer border-secondary">

                        <button type="button"
                                class="btn btn-warning"
                                onclick="irEquipo()">

                            Agregar nuevo equipo

                        </button>

                    </div>

                </div>

            </div>

        </div>

        <!-- ======================================================
             🔹 MODAL EQUIPO
        ====================================================== -->

        <div class="modal fade" id="modalEquipo" tabindex="-1">

            <div class="modal-dialog modal-dialog-centered">

                <div class="modal-content bg-dark text-white">

                    <div class="modal-header border-secondary">

                        <h5 class="modal-title">
                            Equipo registrado
                        </h5>

                    </div>

                    <div class="modal-body">

                        El equipo se agregó correctamente.

                    </div>

                    <div class="modal-footer border-secondary">

                        <button type="button"
                                class="btn btn-warning"
                                onclick="irJugadores()">

                            Agregar jugadores

                        </button>

                    </div>

                </div>

            </div>

        </div>

        <!-- ======================================================
             🔹 FOOTER
        ====================================================== -->

        <footer id="nav_app"
                class="d-flex justify-content-around align-items-center">

            <div class="nav-item active">
                Registro
            </div>

            <div class="nav-item">
                Partidos
            </div>

            <div class="nav-item">
                Ranking
            </div>

        </footer>

        <!-- ======================================================
             🔹 BOOTSTRAP JS
        ====================================================== -->

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
        <%
            Boolean irJugadores
                    = (Boolean) request.getAttribute("irJugadores");
        %>

        <% if (irJugadores != null && irJugadores) { %>

        <script>
            window.onload = function () {

                const seccion =
                    document.getElementById("seccion_jugadores");

                if (seccion) {
                    seccion.scrollIntoView({
                        behavior: "smooth"
                    });
                }
            };
        </script>

        <% } %>

        <!-- ======================================================
             🔹 SCRIPTS
        ====================================================== -->

        <script>

            function irEquipo() {

                document
                    .getElementById("seccion_equipo")
                    .scrollIntoView({
                        behavior: "smooth"
                    });

                let modal = bootstrap.Modal.getInstance(
                    document.getElementById("modalJugador")
                );

                modal.hide();
            }

            function irJugadores() {

                document
                    .getElementById("seccion_jugadores")
                    .scrollIntoView({
                        behavior: "smooth"
                    });

                let modal = bootstrap.Modal.getInstance(
                    document.getElementById("modalEquipo")
                );

                modal.hide();
            }

            document.addEventListener("DOMContentLoaded", function () {

                <%
                    Boolean jugadoresGuardado =
                        (Boolean) request.getAttribute("jugadoresGuardado");

                    if (jugadoresGuardado != null && jugadoresGuardado) {
                %>

                    let modalJugador = new bootstrap.Modal(
                        document.getElementById("modalJugador")
                    );

                    modalJugador.show();

                <%
                    }
                %>

                <%
                    Boolean equipoGuardado =
                        (Boolean) request.getAttribute("equipoGuardado");

                    if (equipoGuardado != null && equipoGuardado) {
                %>

                    let modalEquipo = new bootstrap.Modal(
                        document.getElementById("modalEquipo")
                    );

                    modalEquipo.show();

                <%
                    }
                %>

            });

        </script>

    </body>

</html>