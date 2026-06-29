<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="Modelo.Usuario"%>
<%
    Usuario usuarioActivo = (Usuario) session.getAttribute("usuarioActivo");
    if (usuarioActivo == null) {
        response.sendRedirect(request.getContextPath() + "/LoginServlet");
        return;
    }
%>
<!DOCTYPE html>
<html lang="es">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Mi Perfil</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/base.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/components.css">
        <link rel="stylesheet" href="<%= request.getContextPath()%>/CSS/layout.css">
        <style>
            .perfil-card {
                background: rgba(28,28,28,0.9);
                border: 1px solid rgba(255,255,255,0.08);
                border-radius: 20px;
                padding: 1.5rem;
                margin-bottom: 1rem;
            }
            .avatar {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                background: rgba(244,123,37,0.2);
                border: 2px solid #f47b25;
                color: #f47b25;
                font-size: 2rem;
                font-weight: bold;
                display: flex;
                align-items: center;
                justify-content: center;
                margin: 0 auto 1rem auto;
            }
            .nombre-usuario {
                color: white;
                font-size: 1.3rem;
                font-weight: bold;
                text-align: center;
            }
            .username-badge {
                color: #aaa;
                text-align: center;
                font-size: 0.9rem;
            }
            input.form-control {
                background: rgba(255,255,255,0.08) !important;
                border: 1px solid rgba(255,255,255,0.1) !important;
                color: white !important;
                border-radius: 10px;
                padding: 0.75rem;
            }
            input.form-control:focus {
                border-color: #f47b25 !important;
                box-shadow: 0 0 0 2px rgba(244,123,37,0.3) !important;
            }
            .btn-guardar {
                background: #f47b25;
                color: white;
                border: none;
                border-radius: 12px;
                padding: 0.75rem;
                font-weight: bold;
                width: 100%;
            }
            .btn-guardar:hover {
                background: #ff8a3d;
                color: white;
            }
            .btn-cerrar {
                background: rgba(220,53,69,0.15);
                color: #dc3545;
                border: 1px solid #dc3545;
                border-radius: 12px;
                padding: 0.75rem;
                font-weight: bold;
                width: 100%;
            }
            .btn-cerrar:hover {
                background: #dc3545;
                color: white;
            }
            .seccion-titulo {
                color: #f47b25;
                font-weight: bold;
                font-size: 1rem;
                margin-bottom: 1rem;
            }
        </style>
    </head>
    <body>

        <div style="
    position: sticky;
    top: 0;
    background: #121212;
    border-bottom: 1px solid #2a2a2a;
    padding: 10px 15px;
    display: flex;
    justify-content: space-between;
    align-items: center;
    z-index: 100;
             ">
            <a href="javascript:history.back()" style="
        background: rgba(244,123,37,0.15);
        color: #f47b25;
        border: 1px solid #f47b25;
        border-radius: 20px;
        padding: 6px 14px;
        text-decoration: none;
        font-size: 0.85rem;
        font-weight: bold;
               ">
                ← Volver
            </a>

            <span style="
        color: #f47b25;
        font-weight: 800;
        font-size: 1.1rem;
                  ">
                Mi Perfil
            </span>

            <div style="width:80px;"></div>

        </div>

        <main class="container pb-5" style="max-width:420px;">

            <!-- =========================================
                 INFO USUARIO
            ========================================== -->
            <div class="perfil-card text-center">

                <div class="avatar">
                    <%= String.valueOf(usuarioActivo.getNombre().charAt(0)).toUpperCase()%>
                </div>

                <div class="nombre-usuario">
                    <%= usuarioActivo.getNombre()%>
                </div>

                <div class="username-badge">
                    @<%= usuarioActivo.getUsername()%>
                </div>

            </div>

            <!-- =========================================
                 MENSAJES
            ========================================== -->
            <% if (request.getAttribute("exito") != null) {%>
            <div class="alert alert-success">
                <%= request.getAttribute("exito")%>
            </div>
            <% } %>

            <% if (request.getAttribute("error") != null) {%>
            <div class="alert alert-danger">
                <%= request.getAttribute("error")%>
            </div>
            <% }%>

            <!-- =========================================
                 CAMBIAR DATOS
            ========================================== -->
            <div class="perfil-card">

                <div class="seccion-titulo">Actualizar información</div>

                <form action="<%= request.getContextPath()%>/PerfilServlet"
                      method="post">

                    <input type="hidden" name="accion" value="actualizarPerfil">

                    <div class="mb-3">
                        <label class="form-label">Nuevo nombre</label>
                        <input type="text"
                               name="nombre"
                               class="form-control"
                               value="<%= usuarioActivo.getNombre()%>">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nuevo usuario</label>
                        <input type="text"
                               name="username"
                               class="form-control"
                               value="<%= usuarioActivo.getUsername()%>">
                    </div>

                    <button type="submit" class="btn btn-guardar">
                        Guardar cambios
                    </button>

                </form>

            </div>

            <!-- =========================================
                 CAMBIAR CONTRASEÑA
            ========================================== -->
            <div class="perfil-card">

                <div class="seccion-titulo">Cambiar contraseña</div>

                <form action="<%= request.getContextPath()%>/PerfilServlet"
                      method="post">

                    <input type="hidden" name="accion" value="cambiarPassword">

                    <div class="mb-3">
                        <label class="form-label">Contraseña actual</label>
                        <input type="password"
                               name="passwordActual"
                               class="form-control"
                               required>
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Nueva contraseña</label>
                        <input type="password"
                               name="passwordNueva"
                               class="form-control"
                               required
                               minlength="6">
                    </div>

                    <div class="mb-3">
                        <label class="form-label">Confirmar contraseña</label>
                        <input type="password"
                               name="confirmar"
                               class="form-control"
                               required>
                    </div>

                    <button type="submit" class="btn btn-guardar">
                        Cambiar contraseña
                    </button>

                </form>

            </div>

            <!-- =========================================
                 CERRAR SESIÓN
            ========================================== -->
            <div class="perfil-card">

                <form action="<%= request.getContextPath()%>/PerfilServlet"
                      method="post">

                    <input type="hidden" name="accion" value="cerrarSesion">

                    <button type="submit" class="btn btn-cerrar">
                        Cerrar sesión
                    </button>

                </form>

            </div>

        </main>

        <footer id="nav_app" class="d-flex justify-content-around align-items-center">

            <div class="nav-item">
                <a href="<%= request.getContextPath()%>/PartidosServerlet"
                   style="text-decoration:none;color:inherit;">
                    PARTIDOS
                </a>
            </div>

            <div class="nav-item">
                <a href="<%= request.getContextPath()%>/TorneoServlet?accion=verTorneos"
                   style="text-decoration:none;color:inherit;">
                    TORNEOS
                </a>
            </div>

            <div class="nav-item active">PERFIL</div>

        </footer>

    </body>
</html>