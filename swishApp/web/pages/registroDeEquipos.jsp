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

        <!-- CSS -->
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/registroEquipos.css">

        <style>

            /* =====================================================
               INDICADOR DE PASOS
            ===================================================== */
            #paso_indicador {
                display: flex;
                align-items: center;
                justify-content: center;
                gap: 0;
                padding: 1.5rem 1rem 0.5rem;
            }

            .paso {
                display: flex;
                flex-direction: column;
                align-items: center;
                gap: 6px;
                position: relative;
            }

            .paso-circulo {
                width: 36px;
                height: 36px;
                border-radius: 50%;
                border: 2px solid #2a2a2a;
                background: #1c1c1c;
                color: #555;
                display: flex;
                align-items: center;
                justify-content: center;
                font-weight: bold;
                font-size: 0.85rem;
                transition: all 0.3s ease;
            }

            .paso-label {
                font-size: 0.7rem;
                color: #555;
                transition: color 0.3s ease;
                white-space: nowrap;
            }

            .paso.activo .paso-circulo {
                border-color: #f47b25;
                background: rgba(244,123,37,0.15);
                color: #f47b25;
            }

            .paso.activo .paso-label {
                color: #f47b25;
            }

            .paso.completado .paso-circulo {
                border-color: #00cc66;
                background: rgba(0,204,102,0.15);
                color: #00cc66;
            }

            .paso.completado .paso-label {
                color: #00cc66;
            }

            .paso-linea {
                width: 48px;
                height: 2px;
                background: #2a2a2a;
                margin-bottom: 22px;
                transition: background 0.3s ease;
            }

            .paso-linea.completada {
                background: #00cc66;
            }

            /* =====================================================
               SECCIÓN DESHABILITADA
            ===================================================== */
            .seccion-bloqueada {
                position: relative;
                pointer-events: none;
            }

            .seccion-bloqueada::after {
                content: "";
                position: absolute;
                inset: 0;
                background: rgba(18, 18, 18, 0.6);
                border-radius: 15px;
                z-index: 10;
                backdrop-filter: blur(1px);
            }

            .seccion-bloqueada .bloqueo-msg {
                display: flex;
            }

            .bloqueo-msg {
                display: none;
                position: absolute;
                top: 50%;
                left: 50%;
                transform: translate(-50%, -50%);
                z-index: 20;
                background: #1c1c1c;
                border: 1px solid #f47b25;
                border-radius: 12px;
                padding: 10px 18px;
                font-size: 0.8rem;
                color: #f47b25;
                white-space: nowrap;
                pointer-events: none;
            }

            /* =====================================================
               SECCIÓN EQUIPO - deshabilitada cuando hay equipo activo
            ===================================================== */
            .seccion-equipo-bloqueada {
                position: relative;
                pointer-events: none;
                opacity: 0.5;
            }

            /* =====================================================
               BOTONES - jerarquía clara
            ===================================================== */

            /* Primario - naranja: acción principal del paso actual */
            .btn-paso-primario {
                background: #f47b25 !important;
                color: white !important;
                border: none !important;
                border-radius: 12px;
                padding: 12px;
                width: 100%;
                font-weight: bold;
                font-size: 0.95rem;
                transition: all 0.3s ease;
                cursor: pointer;
            }

            .btn-paso-primario:hover {
                background: #ff8a3d !important;
                transform: scale(1.02);
            }

            /* Secundario - borde naranja: acción opcional del paso */
            .btn-paso-secundario {
                background: transparent !important;
                color: #f47b25 !important;
                border: 1px solid #f47b25 !important;
                border-radius: 12px;
                padding: 10px;
                width: 100%;
                font-weight: 500;
                font-size: 0.9rem;
                transition: all 0.3s ease;
                cursor: pointer;
            }

            .btn-paso-secundario:hover {
                background: rgba(244,123,37,0.1) !important;
            }

            /* Terciario - gris: acción de reinicio/nuevo ciclo */
            .btn-paso-terciario {
                background: #2a2a2a !important;
                color: #aaa !important;
                border: 1px solid #333 !important;
                border-radius: 12px;
                padding: 10px;
                width: 100%;
                font-weight: 500;
                font-size: 0.9rem;
                transition: all 0.3s ease;
                cursor: pointer;
            }

            .btn-paso-terciario:hover {
                background: #333 !important;
                color: white !important;
            }

            /* Finalizar - verde: acción de cierre total */
            .btn-finalizar {
                background: #00cc66 !important;
                color: #121212 !important;
                border: none !important;
                border-radius: 12px;
                padding: 14px;
                width: 100%;
                font-weight: bold;
                font-size: 1rem;
                transition: all 0.3s ease;
                cursor: pointer;
            }

            .btn-finalizar:hover {
                background: #00e673 !important;
                transform: scale(1.02);
            }

            /* Separador visual entre grupos de botones */
            .separador-botones {
                border: none;
                border-top: 1px solid #2a2a2a;
                margin: 1rem 0;
            }

            /* =====================================================
               ETIQUETA DE CONTEXTO encima de sección jugadores
            ===================================================== */
            .ctx-label {
                font-size: 0.75rem;
                color: #f47b25;
                font-weight: bold;
                letter-spacing: 0.5px;
                text-transform: uppercase;
                margin-bottom: 0.35rem;
            }

        </style>

    </head>

    <body>

        <!-- ======================================================
             HEADER
        ====================================================== -->

        <header id="header_main">
            <h2 id="titulo_principal">Registro de Equipos</h2>
        </header>

        <!-- ======================================================
             INDICADOR DE PASOS
        ====================================================== -->

        <%
            Boolean jugadoresGuardado = (Boolean) request.getAttribute("jugadoresGuardado");
            Boolean equipoGuardado = (Boolean) request.getAttribute("equipoGuardado");
            Boolean irJugadores = (Boolean) request.getAttribute("irJugadores");
            Equipo equipoEditar = (Equipo) request.getAttribute("equipoEditar");

            // Se sube aqui arriba (antes solo se declaraba mas abajo,
            // dentro de la seccion de jugadores) porque ahora tambien
            // se necesita para calcular pasoActual.
            List<Jugador> lista = (List<Jugador>) request.getAttribute("lista");

            /*
                pasoActual:
                1 = llenando datos del equipo
                2 = llenando jugadores (equipo ya guardado/activo)

                Ahora se basa SOLO en lo que el Servlet manda
                explicitamente para esto. Se queda en paso 2
                cuando:
                - irJugadores = true (lo manda guardarJugador
                  exitoso via irRegistro, editarEquipo con error,
                  eliminarJugador, nuevoEquipo con error)
                - equipoGuardado = true (recien se guardo/edito
                  un equipo -> toca agregar sus jugadores)
                - hubo un error de validacion en jugadores
                  (errorNumero / errorJugadores)

                YA NO se usa jugadoresGuardado para esto: esa
                bandera la manda "nuevoEquipo" cuando la
                validacion SI paso, justo para decir "ya
                terminamos con jugadores, vamos a paso 1" -- si
                la usabamos para forzar paso 2, hacia lo
                contrario de lo que el boton "Registrar otro
                equipo" necesitaba.

                YA NO se fuerza paso 1 solo por "equipoEditar !=
                null": el Servlet usa equipoEditar tanto para
                "estoy editando el equipo" (sin irJugadores ->
                cae en paso 1 de todos modos) como para "este es
                el equipo cuyos jugadores hay que corregir" (CON
                irJugadores=true, ej. editarEquipo con conteo
                invalido) -> en ese segundo caso SI debe quedarse
                en paso 2, y forzar paso 1 lo bloqueaba.
             */
            boolean enPasoJugadores
                    = (irJugadores != null && irJugadores)
                    || (equipoGuardado != null && equipoGuardado)
                    || request.getAttribute("errorNumero") != null
                    || request.getAttribute("errorJugadores") != null;

            int pasoActual = enPasoJugadores ? 2 : 1;
        %>

        <div id="paso_indicador">

            <div class="paso <%= pasoActual >= 1 ? (pasoActual > 1 ? "completado" : "activo") : ""%>">
                <div class="paso-circulo"><%= pasoActual > 1 ? "✓" : "1"%></div>
                <span class="paso-label">Equipo</span>
            </div>

            <div class="paso-linea <%= pasoActual > 1 ? "completada" : ""%>"></div>

            <div class="paso <%= pasoActual >= 2 ? "activo" : ""%>">
                <div class="paso-circulo">2</div>
                <span class="paso-label">Jugadores</span>
            </div>

        </div>

        <!-- ======================================================
             CONTENIDO
        ====================================================== -->

        <main class="container pb-5">

            <!-- ================= DATOS DEL EQUIPO ================= -->

            <section id="seccion_equipo"
                     class="card mb-4 <%= pasoActual == 2 ? "seccion-equipo-bloqueada" : ""%>">

                <form action="<%= request.getContextPath()%>/Servlet" method="post">

                    <input type="hidden" name="accion" value="guardarEquipo">

                    <!-- Necesario para que el Servlet sepa si es
                         INSERT (id=0) o UPDATE (id real). Sin esto,
                         guardar despues de "Editar" siempre creaba
                         un equipo nuevo en vez de actualizar. -->
                    <input type="hidden"
                           name="id"
                           value="<%= equipoEditar != null ? equipoEditar.getId() : 0%>">

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
                               value="<%= equipoEditar != null ? equipoEditar.getNombre() : ""%>"
                               <%= pasoActual == 2 ? "disabled" : "required"%>>
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
                               value="<%= equipoEditar != null ? equipoEditar.getOrigen() : ""%>"
                               <%= pasoActual == 2 ? "disabled" : "required"%>>
                    </div>

                    <!-- CATEGORIA -->
                    <div class="mb-3">
                        <label>Categoría</label>
                        <input id="input_categoria"
                               name="categoria"
                               type="text"
                               pattern="^[^\d]*$"
                               title="Solo se permiten letras y espacios, sin números"
                               class="form-control"
                               placeholder="Categoría que pertenece"
                               value="<%= equipoEditar != null ? equipoEditar.getCategoria() : ""%>"
                               <%= pasoActual == 2 ? "disabled" : "required"%>>
                    </div>

                    <!-- BOTÓN GUARDAR EQUIPO - solo visible en paso 1 -->
                    <% if (pasoActual == 1) { %>
                    <button id="btn_agregar_equipo"
                            type="submit"
                            class="btn-paso-primario">
                        Guardar equipo
                    </button>
                    <% }%>

                </form>

            </section>

            <!-- ================= JUGADORES ================= -->

            <section id="seccion_jugadores"
                     class="card mb-4 position-relative">

                <% if (pasoActual == 2) { %>
                <p class="ctx-label">Equipo activo</p>
                <% }%>

                <!-- =========================================
                     FORMULARIO DE AGREGAR JUGADOR
                     Solo este bloque se bloquea/difumina en
                     paso 1. La lista (mas abajo) se queda
                     siempre visible, igual que en el JSP
                     original, asi que al editar un equipo
                     siempre puedes ver quien ya esta en el.
                ========================================= -->
                <div class="<%= pasoActual == 1 ? "seccion-bloqueada" : ""%>">

                    <!-- Mensaje de bloqueo visible solo en paso 1 -->
                    <div class="bloqueo-msg">
                        Guarda el equipo primero
                    </div>

                    <form action="<%= request.getContextPath()%>/Servlet" method="post">

                        <input type="hidden" name="accion" value="guardarJugador">

                        <h5>Integrantes</h5>

                        <!-- NOMBRE JUGADOR -->
                        <div class="mb-3">
                            <label>Nombre del jugador</label>
                            <input id="input_jugador"
                                   name="nombre"
                                   type="text"
                                   pattern="^[^\d]*$"
                                   title="Solo se permiten letras y espacios, sin números"
                                   class="form-control"
                                   <%= pasoActual == 1 ? "disabled" : ""%>>
                        </div>

                        <!-- DORSAL Y POSICIÓN -->
                        <div class="row">

                            <div class="col-4">
                                <label>Dorsal</label>
                                <input id="input_dorsal"
                                       type="number"
                                       name="numero"
                                       min="0"
                                       class="form-control"
                                       <%= pasoActual == 1 ? "disabled" : ""%>>
                            </div>

                            <div class="col-8">
                                <label>Posición</label>
                                <select id="select_posicion"
                                        name="posicion"
                                        class="form-control"
                                        <%= pasoActual == 1 ? "disabled" : ""%>>
                                    <option>Base (PG)</option>
                                    <option>Escolta (SG)</option>
                                    <option>Alero (SF)</option>
                                    <option>Ala-Pívot (PF)</option>
                                    <option>Pívot (C)</option>
                                </select>
                            </div>

                        </div>

                        <!-- BOTÓN AGREGAR JUGADOR - acción principal del paso 2 -->
                        <button id="btn_agregar_jugador"
                                type="submit"
                                class="btn-paso-primario mt-3"
                                <%= pasoActual == 1 ? "disabled" : ""%>>
                            Agregar jugador
                        </button>

                    </form>

                </div>

                <!-- LISTA DE JUGADORES
                     Siempre visible, sin importar el paso
                     (igual que en el JSP original). -->
                <div class="mt-4">

                    <h6>Lista de jugadores</h6>

                    <ul id="lista_jugadores" class="list-group">

                        <%
                            if (lista != null) {
                                for (Jugador j : lista) {
                        %>

                        <li class="list-group-item d-flex justify-content-between align-items-center">

                            <span>
                                #<%= j.getNumero()%> <%= j.getNombre()%>
                            </span>

                            <form action="<%= request.getContextPath()%>/Servlet"
                                  method="post"
                                  style="display:inline;">
                                <input type="hidden" name="accion" value="eliminarJugador">
                                <input type="hidden" name="idJugador" value="<%= j.getId()%>">
                                <input type="hidden" name="idEquipo" value="<%= j.getId_equipo()%>">
                                <button type="submit" class="btn btn-sm btn-danger">X</button>
                            </form>

                        </li>

                        <%
                                }
                            }
                        %>

                    </ul>

                </div>

                <!-- ERRORES -->
                <% if (request.getAttribute("errorNumero") != null) {%>
                <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                    <strong>¡Error!</strong> <%= request.getAttribute("errorNumero")%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% } %>

                <% if (request.getAttribute("errorJugadores") != null) {%>
                <div class="alert alert-danger alert-dismissible fade show mt-3" role="alert">
                    <strong>¡Error!</strong> <%= request.getAttribute("errorJugadores")%>
                    <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                </div>
                <% } %>

                <!-- ─── BOTONES DEL PASO 2 ────────────────────────────── -->
                <% if (pasoActual == 2) {%>

                <hr class="separador-botones">

                <!-- Agregar otro equipo: reinicia el flujo al paso 1 -->
                <form action="<%= request.getContextPath()%>/Servlet" method="post">
                    <input type="hidden" name="accion" value="nuevoEquipo">
                    <button type="submit" class="btn-paso-terciario mb-2">
                        + Registrar otro equipo
                    </button>
                </form>

                <% } %>

            </section>

            <!-- ================= EQUIPOS REGISTRADOS ================= -->

            <section id="seccion_equipos_registrados">

                <h5>Equipos registrados</h5>

                <%
                    List<Equipo> lista2 = (List<Equipo>) request.getAttribute("lista2");
                %>

                <%
                    if (lista2 != null) {
                        for (Equipo e : lista2) {
                %>

                <div class="card mb-3 d-flex flex-row justify-content-between align-items-center p-3">

                    <div>
                        <h6><%= e.getNombre()%></h6>
                        <small><%= e.getOrigen()%> — <%= e.getCategoria()%></small>
                    </div>

                    <div class="d-flex gap-2">

                        <!-- EDITAR -->
                        <form action="<%= request.getContextPath()%>/Servlet" method="post">
                            <input type="hidden" name="accion" value="editarEquipo">
                            <input type="hidden" name="id" value="<%= e.getId()%>">
                            <button type="submit" class="btn btn-sm btn-warning">Editar</button>
                        </form>

                        <!-- ELIMINAR -->
                        <form action="<%= request.getContextPath()%>/Servlet" method="post">
                            <input type="hidden" name="accion" value="eliminarEquipo">
                            <input type="hidden" name="id" value="<%= e.getId()%>">
                            <button type="submit" class="btn btn-sm btn-danger">Eliminar</button>
                        </form>

                    </div>

                </div>

                <%
                        }
                    }
                %>

                <!-- BOTÓN FINALIZAR - solo cuando hay equipos y estamos en paso 2 -->
                <% if (lista2 != null && !lista2.isEmpty()) {%>

                <hr class="separador-botones">

                <form action="<%= request.getContextPath()%>/PartidosServerlet" method="post">
                    <input type="hidden" name="accion" value="generarPartidos">
                    <button id="btn_finalizar_equipo" class="btn-finalizar">
                        Finalizar registro e iniciar torneo
                    </button>
                </form>

                <% } %>

            </section>

        </main>

        <!-- ======================================================
             MODAL - EQUIPO GUARDADO (paso 1 → paso 2)
        ====================================================== -->

        <div class="modal fade" id="modalEquipo" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content bg-dark text-white">

                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Equipo registrado</h5>
                    </div>

                    <div class="modal-body">
                        El equipo se guardó correctamente. Ahora agrega los jugadores.
                    </div>

                    <div class="modal-footer border-secondary">
                        <button type="button"
                                class="btn btn-warning"
                                onclick="irAJugadores()">
                            Agregar jugadores →
                        </button>
                    </div>

                </div>
            </div>
        </div>

        <!-- ======================================================
             MODAL - JUGADORES GUARDADOS
        ====================================================== -->

        <div class="modal fade" id="modalJugador" tabindex="-1">
            <div class="modal-dialog modal-dialog-centered">
                <div class="modal-content bg-dark text-white">

                    <div class="modal-header border-secondary">
                        <h5 class="modal-title">Jugadores registrados</h5>
                    </div>

                    <div class="modal-body">
                        Los jugadores se agregaron correctamente.
                        Puedes seguir agregando o registrar otro equipo.
                    </div>

                    <div class="modal-footer border-secondary d-flex gap-2">
                        <button type="button"
                                class="btn btn-outline-secondary"
                                data-bs-dismiss="modal">
                            Seguir agregando
                        </button>
                    </div>

                </div>
            </div>
        </div>

        
        

        <!-- ======================================================
             BOOTSTRAP JS
        ====================================================== -->

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

        <!-- ======================================================
             SCRIPTS
        ====================================================== -->

        <script>

                                    /* ── Ir a sección jugadores desde modal ── */
                                    function irAJugadores() {

                                        const modal = bootstrap.Modal.getInstance(
                                                document.getElementById("modalEquipo")
                                                );
                                        modal.hide();

                                        setTimeout(function () {
                                            document
                                                    .getElementById("seccion_jugadores")
                                                    .scrollIntoView({behavior: "smooth"});
                                        }, 300);
                                    }

                                    /* ── Mostrar modales según atributos del servidor ── */
                                    document.addEventListener("DOMContentLoaded", function () {

            <% if (equipoGuardado != null && equipoGuardado) { %>

                                        const modalEquipo = new bootstrap.Modal(
                                                document.getElementById("modalEquipo")
                                                );
                                        modalEquipo.show();

            <% } %>

            <% if (jugadoresGuardado != null && jugadoresGuardado) { %>

                                        const modalJugador = new bootstrap.Modal(
                                                document.getElementById("modalJugador")
                                                );
                                        modalJugador.show();

            <% } %>

                                        /* ── Si ya estamos en paso 2, scroll directo a jugadores ── */
            <% if (irJugadores != null && irJugadores) { %>

                                        document
                                                .getElementById("seccion_jugadores")
                                                .scrollIntoView({behavior: "smooth"});

            <% }%>

                                    });

        </script>

    </body>

</html>
