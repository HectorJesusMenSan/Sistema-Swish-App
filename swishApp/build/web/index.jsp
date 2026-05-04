<%-- 
    Document   : index
    Created on : 4 may 2026, 8:50:44 a.m.
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
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

        <!-- pequeño texto debajo -->
        <p id="subtitulo">Selecciona el tipo de torneo</p>


        </header>

        <!-- ======================================================
             🔹 CONTENIDO PRINCIPAL
             NOTA: usamos px-3 para dar un pequeño margen lateral
        ====================================================== -->

        <main class="px-3">


        <!-- ==================================================
             🔸 SECCIÓN: SELECCIÓN DE TORNEO
        ================================================== -->
        <section id="seleccion_torneo" class="mt-4">

            <!-- ==================================================
                 🔹 OPCIÓN 1: TORNEO RÁFAGA
            ================================================== -->
            <label class="w-100">

                <!-- 
                    IMPORTANTE:
                    Este radio está oculto, pero controla el estilo.
                    Cuando está seleccionado, activa el CSS:
                    #radio_rafaga:checked + #card_rafaga
                -->
                <input type="radio" name="tipo_torneo" id="radio_rafaga" hidden>

                <!-- CARD (tarjeta visual) -->
                <div id="card_rafaga" class="card w-100 mb-3">

                    <h5>🏀 Torneo Ráfaga</h5>

                    <p class="text-secondary mb-0">
                        Eliminación directa, partidos rápidos en un solo día o fin de semana.
                    </p>

                </div>

            </label>


            <!-- ==================================================
                 🔹 OPCIÓN 2: TORNEO DE LIGA
            ================================================== -->
            <label class="w-100">

                <!-- radio oculto -->
                <input type="radio" name="tipo_torneo" id="radio_liga" hidden>

                <!-- card -->
                <div id="card_liga" class="card w-100 mb-3">

                    <h5>📅 Torneo de Liga</h5>

                    <p class="text-secondary mb-0">
                        Todos contra todos, con tabla de posiciones y finales.
                    </p>

                </div>

            </label>

        </section>


        <!-- ==================================================
             🔸 SECCIÓN: INFORMACIÓN EXTRA
        ================================================== -->
        <section class="row mt-3">

            <!-- tarjeta informativa izquierda -->
            <div class="col-6">

                <div id="info_rafaga_card" class="card p-3 text-center">

                    <h6>⚡ Rápido</h6>

                    <p class="text-secondary mb-0">
                        Fin de semana
                    </p>

                </div>

            </div>

            <!-- tarjeta informativa derecha -->
            <div class="col-6">

                <div id="info_liga_card" class="card p-3 text-center">

                    <h6>🏆 Liga</h6>

                    <p class="text-secondary mb-0">
                        Temporada larga
                    </p>

                </div>

            </div>

        </section>


        <!-- ==================================================
             🔸 BOTÓN CONTINUAR
        ================================================== -->
        <section class="mt-4 mb-5">

            <!-- 
                IMPORTANTE:
                NO usamos class="btn" porque Bootstrap rompe el color.
                Usamos SOLO el id para aplicar tu CSS naranja.
            -->
            <button id="btn_continuar">

                Continuar

            </button>

        </section>


        </main>

        <!-- ======================================================
             🔹 NAVBAR INFERIOR
        ====================================================== -->

        <footer id="nav_app" class="d-flex justify-content-around align-items-center">


        <!-- opción activa -->
        <div class="nav-item active">Inicio</div>

        <!-- otras opciones -->
        <div class="nav-item">Equipos</div>
        <div class="nav-item">Ranking</div>


        </footer>

    </body>
</html>
