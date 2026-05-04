<%-- 
    Document   : rankingMVP
    Created on : 4 may 2026, 8:55:46 a.m.
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>

<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">

    <!-- TÍTULO -->
    <title>Ranking MVP</title>

    <!-- BOOTSTRAP -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- TU CSS -->

    <link rel="stylesheet" href="../CSS/base.css">
    <link rel="stylesheet" href="../CSS/components.css">
    <link rel="stylesheet" href="../CSS/layout.css">
    <link rel="stylesheet" href="../CSS/tables.css">
    <link rel="stylesheet" href="../CSS/rankingMVP.css">

</head>
<body>
    <!-- ======================================================
         🔹 HEADER
    ====================================================== -->
    <header id="header_clasificacion" class="p-3 text-center">
        <h4 id="titulo_torneo">Ranking de Jugadores</h4>
    </header>


    <!-- ======================================================
         🔹 MVP DESTACADO
    ====================================================== -->
    <section id="ranking_destacado" class="p-3 m-3">

        <!-- CONTENIDO PRINCIPAL MVP -->
        <div id="ranking_header">

            <!-- FOTO -->
            <img id="foto_mvp" src="https://via.placeholder.com/100" alt="Jugador MVP">

            <!-- INFO -->
            <div id="info_mvp">
                <span class="label-top">MVP DEL TORNEO</span>
                <h5 id="nombre_mvp">Carlos Rivera</h5>
                <p id="equipo_mvp">Lakers • San Juan</p>
            </div>

        </div>

        <!-- ESTADÍSTICAS -->
        <div id="stats_mvp">

            <div class="stat-mini text-center">
                <span>245</span>
                <p>PTS</p>
            </div>

            <div class="stat-mini text-center">
                <span>12</span>
                <p>Fouls</p>
            </div>

            <div class="stat-mini text-center">
                <span>10</span>
                <p>GP</p>
            </div>

            <div class="stat-mini text-center">
                <span>24.5</span>
                <p>PPG</p>
            </div>

        </div>
    </section>


    <!-- ======================================================
         🔹 MEJOR PARTIDO
    ====================================================== -->
    <section id="mejorPartido" class="m-3 p-3 card">

        <h5>Mejor partido</h5>

        <p>
            <strong>Lakers</strong> 
            <span style="color:#f47b25;">VS</span> 
            <strong>Bulls</strong>
        </p>

        <p class="text-secondary">
            Anotó 38 puntos (máximo de la temporada)
        </p>

    </section>


    <!-- ======================================================
         🔹 TABLA RANKING
    ====================================================== -->
    <section class="m-3">

        <h5>Top 10 Rendimiento</h5>

        <div id="tabla_ranking_nuevo">

            <!-- ENCABEZADO -->
            <div class="fila encabezado">
                <div>#</div>
                <div>Jugador</div>
            </div>

            <!-- FILA 1 -->
            <div class="fila lider">
                <div>1</div>
                <div>
                    C. Rivera 
                    <span class="badge-mini">TOP</span>
                </div>
            </div>

            <!-- FILA 2 -->
            <div class="fila">
                <div>2</div>
                <div>J. Smith</div>
            </div>

            <!-- FILA 3 -->
            <div class="fila">
                <div>3</div>
                <div>M. Jordan</div>
            </div>

            <!-- FILA 4 -->
            <div class="fila">
                <div>4</div>
                <div>S. Curry</div>
            </div>

        </div>

    </section>


    <!-- ======================================================
         🔹 FORMULA
    ====================================================== -->
    <section id="criterio_box" class="m-3 p-3 text-center">

        <p id="texto_criterio">
            Score = Puntos - (Faltas × 0.5)
        </p>

    </section>

</body>
</html>