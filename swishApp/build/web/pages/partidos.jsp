<%-- 
    Document   : partidos
    Created on : 4 may 2026, 8:54:35 a.m.
    Author     : hecto
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="es" id="html_torneo">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Partidos del Torneo</title>

    <!-- Bootstrap -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- TU CSS -->
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/base.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/components.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/layout.css">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/CSS/partidos.css">
</head>

<body id="body_torneo">

    <!-- HEADER -->
    <header id="header_torneo" class="text-center py-4">
        <h2 id="titulo_torneo">Gráfica del Torneo</h2>
        <p id="subtitulo_torneo">Avance de partidos</p>
    </header>

    <!-- CONTENIDO -->
    <main id="main_torneo" class="container py-4">

        <!-- PARTIDO -->
        <section class="match-card mb-4">

            <div class="estado-badge pendiente">
                Pendiente
            </div>

            <div class="d-flex justify-content-between align-items-center">

                <div class="team-box">
                    Lakers Ráfaga
                </div>

                <div class="vs-box">VS</div>

                <div class="team-box">
                    Bulls CDMX
                </div>

            </div>

            <div class="mt-3">
                <button class="btn btn-main w-100">
                    INICIAR PARTIDO
                </button>
            </div>

        </section>

        <!-- PARTIDO -->
        <section class="match-card mb-4">

            <div class="estado-badge finalizado">
                Finalizado
            </div>

            <div class="d-flex justify-content-between align-items-center">

                <div class="team-box">
                    Halcones
                </div>

                <div class="vs-box">VS</div>

                <div class="team-box">
                    Guerreros
                </div>

            </div>

            <div class="mt-3">
                <button class="btn btn-success w-100">
                    VER ESTADÍSTICAS
                </button>
            </div>

        </section>

    </main>

    <!-- NAVBAR -->
    <footer id="nav_app" class="d-flex justify-content-around align-items-center">

        <div class="nav-item active">GRÁFICA</div>
        <div class="nav-item">ESTADÍSTICAS</div>
        <div class="nav-item">RANKING</div>

    </footer>

</body>
</html>