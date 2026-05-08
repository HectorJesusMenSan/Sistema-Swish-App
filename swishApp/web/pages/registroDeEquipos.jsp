<%-- 
    Document   : registroDeEquipos
    Created on : 4 may 2026, 8:56:37 a.m.
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

    <!-- TU CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/base.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/components.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/layout.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/registroEquipos.css">
    
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
    

</head>

<body>

    <!-- ======================================================
         🔹 HEADER
    ====================================================== -->

    <header id="header_main">
        <h2 id="titulo_principal">Registro de Equipos</h2>
    </header>

    <!-- ======================================================
         🔹 CONTENIDO
    ====================================================== -->

    <main class="container pb-5">

        <!-- ================= DATOS DEL EQUIPO ================= -->
        <section id="seccion_equipo" class="card mb-4">
            <form action="<%= request.getContextPath() %>/Servlet   " method="post">
                <input type="hidden" name="accion" value="guardarEquipo">

                <h5>Datos del equipo</h5>

                <!-- NOMBRE -->
                <div class="mb-3">
                    <label>Nombre del equipo</label>
                    <input id="input_nombre_equipo" name="nombre" type="text" class="form-control" placeholder="Ej. Halcones del Norte" required>
                </div>

                <!-- ORIGEN -->
                <div class="mb-3">
                    <label>Origen</label>
                    <input id="input_origen" name="origen" type="text" class="form-control" placeholder="Ciudad o club" required>
                </div>

                <!-- ORIGEN -->
                <div class="mb-3">
                    <label>Categoria</label>
                    <input id="input_categoria" name="categoria" type="text" class="form-control" placeholder="Categoria que pertenece" required>
                </div>

                <!-- BOTÓN -->
                <button id="btn_agregar_equipo" type="submit" class="btn btn-warning w-100">
                    Agregar equipo
                </button>
            </form>
        </section>
            
        <!-- ================= JUGADORES ================= -->
        <section id="seccion_jugadores" class="card mb-4">

            <form action="<%= request.getContextPath() %>/Servlet" method = "post">
                <input type="hidden" name="accion" value="guardarJugador">
                <h5>Integrantes</h5>

                <!-- NOMBRE -->
                <div class="mb-3">
                    <label>Nombre del jugador</label>
                    <input id="input_jugador" name="nombre" type="text" class="form-control">
                </div>

                <!-- DORSAL Y POSICIÓN -->
                <div class="row">

                    <div class="col-4">
                        <label>Dorsal o Numero</label>
                        <input id="input_dorsal" name="numero" type="number" class="form-control">
                    </div>

                    <div class="col-8">
                        <label>Posición</label>
                        <select id="select_posicion" name="posicion" class="form-control">
                            <option>Base (PG)</option>
                            <option>Escolta (SG)</option>
                            <option>Alero (SF)</option>
                            <option>Ala-Pívot (PF)</option>
                            <option>Pívot (C)</option>
                        </select>
                    </div>

                </div>

                <!-- BOTÓN -->
                <button id="btn_agregar_jugador" type="submit" class="btn btn-outline-light w-100 mt-3">
                    Agregar jugador
                </button>
            </form>

            <!-- LISTA -->
            <div class="mt-4">
            <%
                List<Jugador> lista = (List<Jugador>) request.getAttribute("lista");
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
                        <button class="btn btn-sm btn-danger">X</button>
                    </li>
                    <%
                            }
                        }
                    %>

                </ul>
            </div>
            
            <!-- FINALIZAR -->
            <button id="btn_agregar_otro_equipo" class="btn btn-outline-light w-100 mt-3">
                Agregar nuevo equipo
            </button>


        </section>
        
        <!-- ================= MODAL ================= -->
        <!-- ================= MODAL PARA ABRIR ALERTA DE QUE SE AGREGO EQUIPO ================= -->
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

                        <button
                            type="button"
                            class="btn btn-warning"
                            onclick="irJugadores()">

                            Agregar jugadores

                        </button>

                    </div>

                </div>

            </div>

        </div>


        <!-- BOOTSTRAP JS -->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>


        <!-- SCRIPT -->
        <script>

        function irJugadores() {

            document
                .getElementById("seccion_jugadores")
                .scrollIntoView({
                    behavior: "smooth"
                });

            let modal = bootstrap.Modal.getInstance(
                document.getElementById('modalEquipo')
            );

            modal.hide();
        }

        </script>
        <!-- ================= ABRIR MODAL ================= -->

        <%
            Boolean equipoGuardado =
                (Boolean) request.getAttribute("equipoGuardado");

            if (equipoGuardado != null && equipoGuardado) {
        %>

        <script>

        document.addEventListener("DOMContentLoaded", function () {

            let modal = new bootstrap.Modal(
                document.getElementById("modalEquipo")
            );

            modal.show();

        });

        </script>

        <%
            }
        %>


    <!-- ================= EQUIPOS REGISTRADOS ================= -->
    <section id="seccion_equipos_registrados">

        <!-- FINALIZAR -->
        <button id="btn_finalizar_equipo"
                class="btn btn-warning w-100 mt-2 mb-2">

            Finalizar registro

        </button>

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

            <div>

                <button class="btn btn-sm btn-warning">
                    Editar
                </button>

                <button class="btn btn-sm btn-danger">
                    Eliminar
                </button>

            </div>

        </div>

        <%
                }
            }
        %>

    </section>

    </main>

    <!-- ======================================================
         🔹 FOOTER
    ====================================================== -->

    <footer id="nav_app" class="d-flex justify-content-around align-items-center">

    <div class="nav-item active">Registro</div>
    <div class="nav-item">Partidos</div>
    <div class="nav-item">Ranking</div>

    </footer>

</body>
</html>
