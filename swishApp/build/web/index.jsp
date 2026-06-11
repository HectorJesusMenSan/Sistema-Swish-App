<%-- 
    Document   : index
    Created on : 4 may 2026, 8:50:44 a.m.
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List" %>
<!DOCTYPE html>

<html lang="es">
    <head>
        <meta charset="UTF-8">

        <!-- Hace que la página sea responsive (se adapte a celular) -->
        <meta name="viewport" content="width=device-width, initial-scale=1.0">

        <title>Selección de Torneo</title>

        <!-- ======================================================
             🔹 BOOTSTRAP (para grid y utilidades)
        ====================================================== -->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

        <!-- ======================================================
             🔹 TU CSS (SIEMPRE DESPUÉS para que sobrescriba)
        ====================================================== -->
        <!-- Reemplaza la línea de CSS por estas: -->
        <link rel="stylesheet" href="CSS/base.css">
        <link rel="stylesheet" href="CSS/components.css">
        <link rel="stylesheet" href="CSS/layout.css">
        <link rel="stylesheet" href="CSS/index.css">

    </head>

    <body>

        <!-- ======================================================
             🔹 HEADER (parte superior de la app)
        ====================================================== -->

        <header id="header_main" class="text-center">


        <!-- título principal -->
        <h2 id="titulo_principal">Gestión de Torneos</h2>
        <h4>"Para pueblos regionales de Oaxaca"</h4>

        </header>

        <!-- ======================================================
             🔹 CONTENIDO PRINCIPAL
             NOTA: usamos px-3 para dar un pequeño margen lateral
        ====================================================== -->

        <main class="px-3">


        <!-- ==================================================
             🔸 SECCIÓN: SELECCIÓN DE TORNEO
        ================================================== -->
        <form action="<%= request.getContextPath()%>/Servlet" method="post"
              id="formTorneo">

            <section id="seleccion_torneo" class="mt-4">

                <label class="w-100">

                    <input type="radio" name="tipoTorneo"
                           value="rafaga" id="radio_rafaga" hidden>

                    <div id="card_rafaga" class="card w-100 mb-3">

                        <h5>🏀 Torneo Ráfaga</h5>

                        <p class="text-secondary mb-0">
                            Eliminación directa, partidos rápidos
                            en un solo día o fin de semana.
                        </p>

                    </div>

                </label>

                <label class="w-100">

                    <input type="radio" name="tipoTorneo"
                           value="verTorneos" id="radio_liga" hidden>

                    <div id="card_liga" class="card w-100 mb-3">

                        <h5>📅 Ver Torneos</h5>

                        <p class="text-secondary mb-0">
                            Ver torneos existentes o finalizados.
                        </p>

                    </div>

                </label>

            </section>

            <section class="row mt-3">

                <div class="col-6">

                    <div id="info_rafaga_card" class="card p-3 text-center">

                        <h6>⚡ Rápido</h6>

                        <p class="text-secondary mb-0">Ideal para merces o torneos representativos</p>

                    </div>

                </div>

                <div class="col-6">

                    <div id="info_liga_card" class="card p-3 text-center">

                        <h6>🏆 Historial</h6>

                        <p class="text-secondary mb-0">Para ver torneos finalizados o dar continuidad</p>

                    </div>

                </div>

            </section>

            <section class="mt-4 mb-5">

                <button type="button" id="btn_continuar"
                        onclick="continuar()">
                    Continuar
                </button>

            </section>

        </form>

        <script>
            function continuar() {

                const opcion = document.querySelector(
                    'input[name="tipoTorneo"]:checked'
                );

                if (!opcion) {
                    alert("Selecciona una opción");
                    return;
                }

                if (opcion.value === "verTorneos") {

                    window.location.href =
                        "<%= request.getContextPath()%>/TorneoServlet?accion=verTorneos";

                } else {

                    document.getElementById("formTorneo").submit();
                }
            }
        </script>


        </main>


    </body>
</html>
